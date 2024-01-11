package io.jenkins.plugins.sample;

import hudson.model.Result;
import hudson.model.Run;
import hudson.util.RunList;
import java.text.ParseException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildLogic {
    IntervalDate period;
    RunList<Run> buildList;

    Boolean failed;
    static Logger LOGGER = Logger.getLogger(BuildLogic.class.getName());


    public BuildLogic(IntervalDate period, Boolean failed, RunList<Run> buildList) {
        this.period = period;
        this.buildList = buildList;
        this.failed = failed;
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
                        LOGGER.log(Level.WARNING, "runTime: " + DateTimeHandler.convertLongTimeToDate(run.getStartTimeInMillis()));
                        LOGGER.log(Level.WARNING, "runTime: " + run.getStartTimeInMillis() + "now " + DateTimeHandler.convertDateToLongTime(dateDay));
                        LOGGER.log(Level.WARNING, "bool check: " + (run.getStartTimeInMillis() >= DateTimeHandler.convertDateToLongTime(dateDay)));
                        return run.getStartTimeInMillis() >= DateTimeHandler.convertDateToLongTime(dateDay);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                });
                LOGGER.log(Level.WARNING, "dat filter: " + (this.buildList));
                break;
            case WEEK:
                Date dateWeek = Date.from(ZonedDateTime.now().minusDays(6).toInstant());
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

    public void filterFailedBuild() {
        if (!failed) {
            this.buildList = buildList.filter(run -> {
                return run.getResult().isBetterOrEqualTo(Result.SUCCESS);
            });
        }
    }
}
