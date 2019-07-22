package org.ohdsi.dataqualitydashboard.check;

import org.ohdsi.dataqualitydashboard.check.meta.CheckCategory;
import org.ohdsi.dataqualitydashboard.check.meta.CheckContext;
import org.ohdsi.dataqualitydashboard.check.meta.CheckSubcategory;
import org.ohdsi.dataqualitydashboard.check.meta.CheckType;

import java.util.Map;

public abstract class PrimaryCheck extends Check {

    public PrimaryCheck(Integer id, CheckType type, String description, CheckContext context, CheckCategory category, CheckSubcategory subcategory, Float threshold) {

        super(id, type, description, context, category, subcategory, threshold);
    }

    public abstract String getTemplatePath();
    public abstract Map<String, String> getParams();
}
