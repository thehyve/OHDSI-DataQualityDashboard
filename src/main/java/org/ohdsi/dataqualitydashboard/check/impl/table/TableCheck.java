package org.ohdsi.dataqualitydashboard.check.impl.table;

import lombok.Getter;
import org.ohdsi.dataqualitydashboard.check.PrimaryCheck;
import org.ohdsi.dataqualitydashboard.check.meta.CheckCategory;
import org.ohdsi.dataqualitydashboard.check.meta.CheckContext;
import org.ohdsi.dataqualitydashboard.check.meta.CheckSubcategory;
import org.ohdsi.dataqualitydashboard.check.meta.CheckType;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class TableCheck extends PrimaryCheck {

    private final String tableName;

    public TableCheck(
            Integer id,
            CheckType type,
            String description,
            CheckContext context,
            CheckCategory category,
            CheckSubcategory subcategory,
            Float threshold,
            String tableName
    ) {

        super(id, type, description, context, category, subcategory, threshold);
        this.tableName = tableName;
    }

    public Map<String, String> getParams() {

        return new HashMap<String, String>() {{
            put("tableName", tableName);
        }};
    }
}
