package io.jenkins.plugins.sample;

import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.Queue;
import hudson.util.RunList;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.TimeUnit;
import hudson.model.AbstractBuild;

public class BuildDurationLogic extends BuildLogic {
    static Logger LOGGER = Logger.getLogger(BuildDurationLogic.class.getName());
    HashMap<String, Double> dateFormatDuration;
    String dateFormatKey;

    public BuildDurationLogic(IntervalDate period, Boolean failed, RunList<Run> buildList) {
        super(period, failed, buildList);
    }

    public Map<String, Double> getBuildsDuration(Boolean average) throws ParseException {
        filterPeriodBuild();
        filterFailedBuild();
        switch (this.period){
            case MONTH:
                dateFormatDuration = DateTimeHandler.createDateMonthMap();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case WEEK:
                dateFormatDuration = DateTimeHandler.createDateWeekMap();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case YEAR:
                dateFormatDuration = DateTimeHandler.createDateYearMap();
                dateFormatKey = "yyyy-MM";
                break;
        }


        HashMap<String, Integer> dayDurationAverage = new HashMap<>();
        for (Run run : this.buildList) {
            String dateFormatKeyAfterCheckPeriod =
                    DateTimeHandler.dateToString(
                            DateTimeHandler.convertLongTimeToDate(
                                    run.getStartTimeInMillis()
                            ), dateFormatKey
                    );
            LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod: " + dateFormatKeyAfterCheckPeriod);
            if (dateFormatDuration.get(dateFormatKeyAfterCheckPeriod) == 0.0) {
                dateFormatDuration.put(dateFormatKeyAfterCheckPeriod, run.getDuration() / 1000.0);
                LOGGER.log(Level.WARNING, "getDuration: " + run.getDuration());
                dayDurationAverage.put(dateFormatKeyAfterCheckPeriod, 1);
            } else {
                dateFormatDuration.put(dateFormatKeyAfterCheckPeriod, dateFormatDuration.get(dateFormatKeyAfterCheckPeriod) + run.getDuration() / 1000.0);
                dayDurationAverage.put(dateFormatKeyAfterCheckPeriod, dayDurationAverage.get(dateFormatKeyAfterCheckPeriod) + 1);
            }
        }
        if (average) {
            for (Map.Entry<String, Integer> entry : dayDurationAverage.entrySet()) {
                LOGGER.log(Level.INFO, "sum time duration: " + dateFormatDuration.get(entry.getKey()));
                LOGGER.log(Level.INFO, "count runs: " + entry.getValue());
                dateFormatDuration.put(entry.getKey(),
                        dateFormatDuration.get(entry.getKey())/entry.getValue()
                );
            }
        }
        LOGGER.log(Level.INFO, "dateFormatDuration: " + dateFormatDuration);
        LOGGER.log(Level.INFO, "dayDurationAverage: " + dayDurationAverage);
        return dateFormatDuration;
    }
}


