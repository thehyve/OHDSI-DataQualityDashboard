package org.ohdsi.dataqualitydashboard;

import lombok.var;
import org.junit.Assert;
import org.junit.Test;
import org.ohdsi.dataqualitydashboard.check.Check;
import org.ohdsi.dataqualitydashboard.db.ConnectionDetails;
import org.ohdsi.dataqualitydashboard.result.CheckResult;
import org.ohdsi.dataqualitydashboard.result.Measurement;
import org.ohdsi.dataqualitydashboard.util.Utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CheckExecutorTest {

    @Test
    public void testChecks() {

        var connectionDetails = new ConnectionDetails(
                "postgresql",
                "jdbc:postgresql://localhost:5432/synpuf_531",
                "postgres",
                "postgres",
                "public"
        );
        String configYml = Utils.getResourceAsString("/spec.yml");

        var checkExecutor = new CheckExecutor(connectionDetails);
        Map<Integer, Check> checks = checkExecutor.loadChecks(configYml);

//        String sql = checks.values().stream().map(c -> checkExecutor.buildCheckSql((PrimaryCheck) c)).collect(Collectors.joining("\n"));

        List<CheckResult> results = checkExecutor.run(checks.values());

        List<CheckResult> failedChecks = results.stream()
                .filter(r -> ((Measurement) r).getProportion() > checks.get(r.getCheckId()).getThreshold())
                .collect(Collectors.toList());

        if (failedChecks.size() > 0) {

            System.out.println("Failed checks:");
            failedChecks.forEach(cr -> {
                Check check = checks.get(cr.getCheckId());
                Measurement measurement = (Measurement) cr;
                System.out.println(
                        String.format("%s. %s rows, %s from total", check.getDescription(), measurement.getCount(), measurement.getProportion())
                );
            });
        }

        Assert.assertTrue("No results were generated", results.size() > 0);
    }
}
