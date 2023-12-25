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
                Date dateMonth = Date.from(ZonedDateTime.now().minusMonths(1).toInstant());
                this.buildList = buildList.filter(run -> {
                    try {
                        return run.getStartTimeInMillis() >= DateTimeHandler.convertDateToLongTime(dateMonth);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case YEAR:
                Date dateYear = Date.from(ZonedDateTime.now().minusYears(1).toInstant());
                this.buildList = buildList.filter(run -> {
                    try {
                        return run.getStartTimeInMillis() >= DateTimeHandler.convertDateToLongTime(dateYear);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case DAY:
                Date dateDay = Date.from(ZonedDateTime.now().minusHours(24).toInstant());
                this.buildList = buildList.filter(run -> {
                    try {
                        return run.getStartTimeInMillis() >= DateTimeHandler.convertDateToLongTime(dateDay);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case WEEK:
                Date dateWeek = Date.from(ZonedDateTime.now().minusWeeks(1).toInstant());
                this.buildList = buildList.filter(run -> {
                    try {
                        return run.getStartTimeInMillis() >= DateTimeHandler.convertDateToLongTime(dateWeek);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case QUARTER:
                Date dateQuarter = Date.from(ZonedDateTime.now().minusMonths(3).toInstant());
                this.buildList = buildList.filter(run -> {
                    try {
                        return run.getStartTimeInMillis() >= DateTimeHandler.convertDateToLongTime(dateQuarter);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
                break;
            case ALL:
                break;
        }
    }
}
