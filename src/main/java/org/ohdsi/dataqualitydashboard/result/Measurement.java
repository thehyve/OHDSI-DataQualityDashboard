package org.ohdsi.dataqualitydashboard.result;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class Measurement extends CheckResult {

    @NonNull
    protected final Integer count;

    @NonNull
    protected final Float proportion;

    @Builder
    protected Measurement(Integer checkId, Integer count, Float proportion) {

        super(checkId);
        this.count = count;
        this.proportion = proportion;
    }
}
