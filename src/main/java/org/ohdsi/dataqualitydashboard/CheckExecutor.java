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
    private static final String SCHEMA_PROP = "schema";

    private final ConnectionDetails connectionDetails;
    private DataSource dataSource;
    private ExecutorService executor;

    private Boolean writeToDb; // TODO

    public CheckExecutor(ConnectionDetails connectionDetails) {

        this(connectionDetails, 10);
    }


    public CheckExecutor(ConnectionDetails connectionDetails, Integer maxThreadCount) {

        this.connectionDetails = connectionDetails;
        this.initConnectionPool();
        this.initThreadPool(maxThreadCount);
    }

    public List<Check> loadChecks(String configYml) {

        Yaml yaml = new Yaml();
        Map<String, Object> configMap = yaml.load(configYml);

        List<Map<String, String>> referenceDataList = (List) configMap.get(REFERENCE_PROP);
        List<Map<String, String>> checkDataList = (List) configMap.get(CHECKS_PROP);

        var objectMapper = new ObjectMapper();
        return checkDataList.stream()
                .map(checkData -> {
                    var ref = referenceDataList.stream()
                            .filter(r -> Objects.equals(r.get(TYPE_PROP), checkData.get(TYPE_PROP)))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException(CHECK_LOADING_ERROR));

                    var data = new HashMap<>(ref);
                    data.putAll(checkData);

                    return objectMapper.convertValue(data, Check.class);
                })
                .collect(Collectors.toList());
    }

    public List<CheckResult> run(List<Check> checkList) {

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

    private String buildCheckSql(PrimaryCheck check) {

        var template = Utils.getResourceAsString(check.getTemplatePath());

        var paramKeys = new ArrayList<String>(Arrays.asList(SCHEMA_PROP));
        paramKeys.addAll(check.getParams().keySet());

        var paramVals = new ArrayList<String>(Arrays.asList(connectionDetails.getSchema()));
        paramVals.addAll(check.getParams().values());

        var renderedSql = SqlRender.renderSql(
                template,
                paramKeys.toArray(new String[0]),
                paramVals.toArray(new String[0])
        );
        var translatedSql = SqlTranslate.translateSql(renderedSql, connectionDetails.getDbms());

        return translatedSql;
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
            throw new RuntimeException(ex);
        }
    }

    private CompletableFuture<CheckResult> runCheckAsync(PrimaryCheck check) {

        return CompletableFuture.supplyAsync(() -> runCheck(check), executor);
    }
}
