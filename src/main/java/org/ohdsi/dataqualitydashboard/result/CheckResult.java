package org.ohdsi.dataqualitydashboard.result;

import lombok.NonNull;

public abstract class CheckResult {

    @NonNull
    protected final Integer checkId;

    protected CheckResult(Integer checkId) {

        this.checkId = checkId;
    }
}
