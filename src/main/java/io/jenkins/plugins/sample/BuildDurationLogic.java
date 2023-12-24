package io.jenkins.plugins.sample;

import hudson.model.Run;
import hudson.util.RunList;
import java.text.ParseException;
import java.util.*;

public class BuildDurationLogic extends BuildLogic {
    public BuildDurationLogic(IntervalDate period, RunList<Run> buildList) {
        super(period, buildList);
    }

    public Map<String, Double> getBuildsDuration() throws ParseException {
        filterPeriodBuild();
        HashMap<String, Double> dayDuration = DateTimeHandler.createDateMonthMap();
        for (Run run : this.buildList) {
            String day = DateTimeHandler.dateToString(DateTimeHandler.convertLongTimeToDate(run.getStartTimeInMillis()));
            if (dayDuration.get(day) == 0.0) {
                dayDuration.put(day, run.getDuration() / 1000.0);
            } else {
                dayDuration.put(day, dayDuration.get(day) + run.getDuration() / 1000.0);
            }
        }
        return dayDuration;
    }
}
