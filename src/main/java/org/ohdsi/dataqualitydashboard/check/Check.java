package org.ohdsi.dataqualitydashboard.check;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.ohdsi.dataqualitydashboard.check.impl.table.TableRequiredCheck;
import org.ohdsi.dataqualitydashboard.check.meta.CheckCategory;
import org.ohdsi.dataqualitydashboard.check.meta.CheckContext;
import org.ohdsi.dataqualitydashboard.check.meta.CheckSubcategory;
import org.ohdsi.dataqualitydashboard.check.meta.CheckType;

@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TableRequiredCheck.class, name = "TableRequiredCheck")
})
public abstract class Check {

    private final Integer id;
    private final CheckType type;
    private final String description;
    private final CheckContext context;
    private final CheckCategory category;
    private final CheckSubcategory subcategory;
    private final Float threshold;
}
