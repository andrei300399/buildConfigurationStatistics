package io.jenkins.plugins.sample;

import hudson.model.Run;
import hudson.util.RunList;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class BuildDurationLogic {

    String period;
    RunList<Run> buildList;
    public BuildDurationLogic(String period, RunList<Run> buildList) {
        this.period = period;
        this.buildList = buildList;
    }

    public void filterPeriodBuild(){
        if (period == "month") {
            this.buildList = buildList
                    .filter(run -> {
                        try {
                            return run.getStartTimeInMillis() >= DateTimeHandler.convertDateToLongTime("2023 11 01 00:00:00");
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    public Map<Integer, Double> getBuildsDuration() throws ParseException {
        filterPeriodBuild();
        Map<Integer, Double> dayDuration = new HashMap<Integer, Double>();
        for (Run run : this.buildList) {
            int day = DateTimeHandler.getDayOfMonth(DateTimeHandler.convertLongTimeToDate(run.getStartTimeInMillis()));
            if (dayDuration.containsKey(day)){
                dayDuration.put(day,dayDuration.get(day) + run.getDuration()/1000.0);
            } else {
                dayDuration.put(day,run.getDuration()/1000.0);
            }
        }
        return dayDuration;
    }
}
