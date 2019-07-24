package org.ohdsi.dataqualitydashboard.check;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import org.ohdsi.dataqualitydashboard.check.impl.concept.ConceptGenderCheck;
import org.ohdsi.dataqualitydashboard.check.impl.field.FieldForeignKeyCheck;
import org.ohdsi.dataqualitydashboard.check.impl.table.TableRequiredCheck;
import org.ohdsi.dataqualitydashboard.check.meta.CheckCategory;
import org.ohdsi.dataqualitydashboard.check.meta.CheckContext;
import org.ohdsi.dataqualitydashboard.check.meta.CheckSubcategory;
import org.ohdsi.dataqualitydashboard.check.meta.CheckType;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TableRequiredCheck.class, name = "TableRequiredCheck"),
        @JsonSubTypes.Type(value = FieldForeignKeyCheck.class, name = "FieldForeignKeyCheck"),
        @JsonSubTypes.Type(value = ConceptGenderCheck.class, name = "ConceptGenderCheck")
})
@Getter
public abstract class Check {

    protected Integer id;
    protected CheckType type;
    protected String description;
    protected CheckContext context;
    protected CheckCategory category;
    protected CheckSubcategory subcategory;
    protected Float threshold;
}
