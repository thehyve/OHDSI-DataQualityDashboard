package org.ohdsi.dataqualitydashboard.check.impl.table;

import org.ohdsi.dataqualitydashboard.check.meta.CheckCategory;
import org.ohdsi.dataqualitydashboard.check.meta.CheckContext;
import org.ohdsi.dataqualitydashboard.check.meta.CheckSubcategory;
import org.ohdsi.dataqualitydashboard.check.meta.CheckType;

import java.beans.ConstructorProperties;

public class TableRequiredCheck extends TableCheck {

    private static final String template = "/check/table/tableRequiredCheck.sql";

    @ConstructorProperties({ "id", "type", "description", "context", "category", "subcategory", "threshold", "tableName" })
    public TableRequiredCheck(
        Integer id,
        CheckType type,
        String description,
        CheckContext context,
        CheckCategory category,
        CheckSubcategory subcategory,
        Float threshold,
        String tableName
    ) {

        super(id, type, String.format(description, tableName), context, category, subcategory, threshold, tableName);
    }

    public String getTemplatePath() {

        return template;
    }
}
