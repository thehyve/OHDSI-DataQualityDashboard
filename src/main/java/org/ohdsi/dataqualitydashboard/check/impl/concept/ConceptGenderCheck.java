package org.ohdsi.dataqualitydashboard.check.impl.concept;

import lombok.Getter;
import lombok.var;
import org.ohdsi.dataqualitydashboard.util.Utils;

import java.util.Map;

@Getter
public class ConceptGenderCheck extends ConceptCheck {

    private String plausibleGender;

    @Override
    public String getDescription() {

        return String.format(description, getCdmTableName(), getCdmFieldName(), getConceptId(), getPlausibleGender());
    }

    @Override
    public String getViolatingRowsSql() {

        return Utils.getResourceAsString("/check/concept/genderViolators.sql");
    }

    @Override
    public String getAllCountSql() {

        return Utils.getResourceAsString("/check/concept/genderAllCount.sql");
    }

    public Map<String, String> getParams() {

        var params = super.getParams();
        params.put("plausibleGender", plausibleGender);
        return params;
    }
}
