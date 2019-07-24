package org.ohdsi.dataqualitydashboard.check.impl.table;

import org.ohdsi.dataqualitydashboard.util.Utils;

public class TableRequiredCheck extends TableCheck {

    @Override
    public String getDescription() {

        return String.format(description, getCdmTableName());
    }

    @Override
    public String getViolatingRowsSql() {

        return Utils.getResourceAsString("/check/table/requiredViolators.sql");
    }

    @Override
    public String getAllCountSql() {

        return Utils.getResourceAsString("/check/table/requiredAllCount.sql");
    }
}
