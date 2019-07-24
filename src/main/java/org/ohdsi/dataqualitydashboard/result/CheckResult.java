package org.ohdsi.dataqualitydashboard.result;

import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class CheckResult {

    @NonNull
    protected final Integer checkId;

    protected CheckResult(Integer checkId) {

        this.checkId = checkId;
    }
}
