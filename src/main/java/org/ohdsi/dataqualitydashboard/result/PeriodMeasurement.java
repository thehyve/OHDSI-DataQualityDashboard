package org.ohdsi.dataqualitydashboard.result;

import lombok.Getter;

import java.util.Date;

@Getter
public class PeriodMeasurement extends Measurement {

    private final Date startDate;
    private final Date endDate;

    public PeriodMeasurement(Integer checkId, Integer count, Float proportion, Date startDate, Date endDate) {

        super(checkId, count, proportion);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}