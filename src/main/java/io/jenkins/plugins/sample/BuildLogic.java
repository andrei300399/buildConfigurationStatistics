package io.jenkins.plugins.sample;

import hudson.model.Run;
import hudson.util.RunList;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Date;

public class BuildLogic {
    IntervalDate period;
    RunList<Run> buildList;

    public BuildLogic(IntervalDate period, RunList<Run> buildList) {
        this.period = period;
        this.buildList = buildList;
    }

    public void filterPeriodBuild() {
        switch (period) {
            case MONTH:
                Date date = Date.from(ZonedDateTime.now().minusMonths(1).toInstant());
                this.buildList = buildList.filter(run -> {
                    try {
                        return run.getStartTimeInMillis() >= DateTimeHandler.convertDateToLongTime(date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case YEAR:
                Date date2 = Date.from(ZonedDateTime.now().minusYears(1).toInstant());
                this.buildList = buildList.filter(run -> {
                    try {
                        return run.getStartTimeInMillis() >= DateTimeHandler.convertDateToLongTime(date2);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case DAY:
                break;
        }
    }
}
