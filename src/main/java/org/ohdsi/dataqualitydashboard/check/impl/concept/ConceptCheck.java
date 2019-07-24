package org.ohdsi.dataqualitydashboard.check.impl.concept;

import lombok.Getter;
import lombok.var;
import org.ohdsi.dataqualitydashboard.check.impl.field.FieldCheck;

import java.util.Map;

@Getter
public abstract class ConceptCheck extends FieldCheck {

    private Integer conceptId;

    public Map<String, String> getParams() {

        var params = super.getParams();
        params.put("conceptId", conceptId.toString());
        return params;
    }
}
