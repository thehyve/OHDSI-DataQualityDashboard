package org.ohdsi.dataqualitydashboard;

import lombok.var;
import org.junit.Assert;
import org.junit.Test;
import org.ohdsi.dataqualitydashboard.check.Check;
import org.ohdsi.dataqualitydashboard.db.ConnectionDetails;
import org.ohdsi.dataqualitydashboard.result.CheckResult;
import org.ohdsi.dataqualitydashboard.util.Utils;

import java.util.List;

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
        List<Check> checkList = checkExecutor.loadChecks(configYml);
        List<CheckResult> results = checkExecutor.run(checkList);

        Assert.assertTrue("No results were generated", results.size() > 0);
    }
}
