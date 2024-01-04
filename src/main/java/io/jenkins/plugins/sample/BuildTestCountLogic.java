package io.jenkins.plugins.sample;

import hudson.model.Run;
import hudson.util.RunList;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class BuildTestCountLogic extends BuildLogic {

    public BuildTestCountLogic(IntervalDate period, RunList<Run> buildList) {
        super(period, true,buildList);
    }

    public Map<Integer, Double> getBuildsDuration() throws ParseException {
        filterPeriodBuild();
        Map<Integer, Double> dayDuration = new HashMap<Integer, Double>();
        for (Run run : this.buildList) {
            int day = DateTimeHandler.getDayOfMonth(DateTimeHandler.convertLongTimeToDate(run.getStartTimeInMillis()));
            if (dayDuration.containsKey(day)) {
                dayDuration.put(day, dayDuration.get(day) + run.getDuration() / 1000.0);
            } else {
                dayDuration.put(day, run.getDuration() / 1000.0);
            }
        }
        return dayDuration;
    }
}
