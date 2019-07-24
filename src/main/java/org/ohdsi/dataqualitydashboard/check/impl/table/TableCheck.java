package org.ohdsi.dataqualitydashboard.check.impl.table;

import lombok.Getter;
import org.ohdsi.dataqualitydashboard.check.PrimaryCheck;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class TableCheck extends PrimaryCheck {

    private String cdmTableName;

    public Map<String, String> getParams() {

        return new HashMap<String, String>() {{
            put("cdmTableName", cdmTableName);
        }};
    }
}
