package org.ohdsi.dataqualitydashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zaxxer.hikari.HikariDataSource;
import lombok.var;
import org.ohdsi.dataqualitydashboard.check.Check;
import org.ohdsi.dataqualitydashboard.check.PrimaryCheck;
import org.ohdsi.dataqualitydashboard.db.ConnectionDetails;
import org.ohdsi.dataqualitydashboard.exception.InvalidCheckResultException;
import org.ohdsi.dataqualitydashboard.result.CheckResult;
import org.ohdsi.dataqualitydashboard.result.Measurement;
import org.ohdsi.dataqualitydashboard.util.Utils;
import org.ohdsi.sql.SqlRender;
import org.ohdsi.sql.SqlTranslate;
import org.yaml.snakeyaml.Yaml;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class CheckExecutor {

    private static final String CHECK_LOADING_ERROR = "Cannot process configuration";

    private static final String REFERENCE_PROP = "reference";
    private static final String CHECKS_PROP = "checks";
    private static final String TYPE_PROP = "type";
    private static final String SCHEMA_PROP = "cdmDatabaseSchema";

    private final ConnectionDetails connectionDetails;
    private DataSource dataSource;
    private ExecutorService executor;
    private String checkSkeletonSql = Utils.getResourceAsString("/check/check.sql");

    private Boolean writeToDb; // TODO

    public CheckExecutor(ConnectionDetails connectionDetails) {

        this(connectionDetails, 10);
    }


    public CheckExecutor(ConnectionDetails connectionDetails, Integer maxThreadCount) {

        this.connectionDetails = connectionDetails;
        this.initConnectionPool();
        this.initThreadPool(maxThreadCount);
    }

    public Map<Integer, Check> loadChecks(String configYml) {

        Yaml yaml = new Yaml();
        Map<String, Object> configMap = yaml.load(configYml);

        List<Map<String, String>> referenceDataList = (List) configMap.get(REFERENCE_PROP);
        List<Map<String, String>> checkDataList = (List) configMap.get(CHECKS_PROP);

        var objectMapper = new ObjectMapper();

        Map<Integer, Check> idToCheckMap = new HashMap<>();
        checkDataList.forEach(checkData -> {
            var ref = referenceDataList.stream()
                    .filter(r -> Objects.equals(r.get(TYPE_PROP), checkData.get(TYPE_PROP)))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException(CHECK_LOADING_ERROR));

            var data = new HashMap<>(ref);
            data.putAll(checkData);

            Check check = objectMapper.convertValue(data, Check.class);

            idToCheckMap.put(check.getId(), check);
        });
        return idToCheckMap;
    }

    public List<CheckResult> run(Collection<Check> checkList) {

        List<CompletableFuture<CheckResult>> futures = checkList.stream()
                .filter(c -> c instanceof PrimaryCheck)
                .map(c -> runCheckAsync((PrimaryCheck) c))
                .collect(Collectors.toList());

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        return futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    private void initConnectionPool() {

        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(connectionDetails.getJdbcUrl());
        ds.setUsername(connectionDetails.getUsername());
        ds.setPassword(connectionDetails.getPassword());
        this.dataSource = ds;
    }

    private void initThreadPool(Integer maxThreadCount) {

        this.executor = Executors.newFixedThreadPool(maxThreadCount);
    }

    public String buildCheckSql(PrimaryCheck check) {


        var paramKeys = new ArrayList<String>(Arrays.asList(SCHEMA_PROP));
        paramKeys.addAll(check.getParams().keySet());

        var paramVals = new ArrayList<String>(Arrays.asList(connectionDetails.getSchema()));
        paramVals.addAll(check.getParams().values());

        var violatorsSql = SqlRender.renderSql(check.getViolatingRowsSql(), paramKeys.toArray(new String[0]), paramVals.toArray(new String[0]));
        var allCntSql = SqlRender.renderSql(check.getAllCountSql(), paramKeys.toArray(new String[0]), paramVals.toArray(new String[0]));

        var sql = SqlRender.renderSql(checkSkeletonSql, new String[]{"violatorsSql", "allCountSql"}, new String[]{violatorsSql, allCntSql});
        sql = SqlTranslate.translateSql(sql, connectionDetails.getDbms());

        return sql;
    }

    private CheckResult runCheck(PrimaryCheck check) {

        String sql = buildCheckSql(check);

        try (Connection conn = dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                try (final ResultSet resultSet = stmt.executeQuery()) {

                    Measurement.MeasurementBuilder builder = Measurement.builder().checkId(check.getId());

                    if (!resultSet.next()) {
                        throw new InvalidCheckResultException();
                    }

                    builder.count(resultSet.getInt("COUNT"));
                    builder.proportion(resultSet.getFloat("PROPORTION"));

                    if (resultSet.next()) {
                        throw new InvalidCheckResultException();
                    }

                    return builder.build();
                }
            }
        } catch (SQLException ex) {
            return new Measurement(check.getId(), null, 1F);
        }
    }

    private CompletableFuture<CheckResult> runCheckAsync(PrimaryCheck check) {

        return CompletableFuture.supplyAsync(() -> runCheck(check), executor);
    }
}
