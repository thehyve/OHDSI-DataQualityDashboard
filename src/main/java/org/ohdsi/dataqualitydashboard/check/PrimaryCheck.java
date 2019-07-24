package org.ohdsi.dataqualitydashboard.check;

import java.util.Map;

public abstract class PrimaryCheck extends Check {

    public abstract String getViolatingRowsSql();
    public abstract String getAllCountSql();
    public abstract Map<String, String> getParams();
}
