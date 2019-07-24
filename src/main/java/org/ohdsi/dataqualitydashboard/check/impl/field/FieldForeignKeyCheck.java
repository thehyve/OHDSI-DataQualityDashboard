package org.ohdsi.dataqualitydashboard.check.impl.field;

import lombok.Getter;
import lombok.var;
import org.ohdsi.dataqualitydashboard.util.Utils;

import java.util.Map;

@Getter
public class FieldForeignKeyCheck extends FieldCheck {

    private String fkTableName;
    private String fkFieldName;

    @Override
    public String getDescription() {

        return String.format(description, getCdmTableName(), getCdmFieldName(), getFkTableName(), getFkFieldName());
    }

    @Override
    public String getViolatingRowsSql() {

        return Utils.getResourceAsString("/check/field/foreignKeyViolators.sql");
    }

    @Override
    public String getAllCountSql() {

        return Utils.getResourceAsString("/check/field/foreignKeyAllCount.sql");
    }

    public Map<String, String> getParams() {

        var params = super.getParams();
        params.put("fkTableName", fkTableName);
        params.put("fkFieldName", fkFieldName);
        return params;
    }

}
