package org.ohdsi.dataqualitydashboard.check.impl.field;

import lombok.Getter;
import lombok.var;
import org.ohdsi.dataqualitydashboard.check.impl.table.TableCheck;

import java.util.Map;

@Getter
public abstract class FieldCheck extends TableCheck {

    private String cdmFieldName;

    public Map<String, String> getParams() {

        var params = super.getParams();
        params.put("cdmFieldName", cdmFieldName);
        return params;
    }
}
