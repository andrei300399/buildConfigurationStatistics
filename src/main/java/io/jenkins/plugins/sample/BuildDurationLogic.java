package io.jenkins.plugins.sample;

import hudson.model.Run;
import hudson.util.RunList;
import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildDurationLogic extends BuildLogic {
    static Logger LOGGER = Logger.getLogger(BuildDurationLogic.class.getName());

    public BuildDurationLogic(IntervalDate period, Boolean failed, RunList<Run> buildList) {
        super(period, failed, buildList);
    }

    public Map<String, Double> getBuildsDuration(Boolean average) throws ParseException {
        filterPeriodBuild();
        filterFailedBuild();
        HashMap<String, Double> dayDuration = DateTimeHandler.createDateMonthMap();
        HashMap<String, Integer> dayDurationAverage = new HashMap<>();
        for (Run run : this.buildList) {
            String day =
                    DateTimeHandler.dateToString(DateTimeHandler.convertLongTimeToDate(run.getStartTimeInMillis()));
            LOGGER.log(Level.INFO, "day: " + day);
            if (dayDuration.get(day) == 0.0) {
                dayDuration.put(day, run.getDuration() / 1000.0);
                dayDurationAverage.put(day, 1);
            } else {
                dayDuration.put(day, dayDuration.get(day) + run.getDuration() / 1000.0);
                dayDurationAverage.put(day, dayDurationAverage.get(day) + 1);
            }
        }
        if (average) {
            for (Map.Entry<String, Integer> entry : dayDurationAverage.entrySet()) {
                LOGGER.log(Level.INFO, "sum time duration: " + dayDuration.get(entry.getKey()));
                LOGGER.log(Level.INFO, "count runs: " + entry.getValue());
                dayDuration.put(entry.getKey(),
                        dayDuration.get(entry.getKey())/entry.getValue()
                );
            }
        }
        LOGGER.log(Level.INFO, "dayDuration: " + dayDuration);
        LOGGER.log(Level.INFO, "dayDurationAverage: " + dayDurationAverage);
        return dayDuration;
    }
}
