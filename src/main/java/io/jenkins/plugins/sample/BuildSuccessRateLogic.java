package io.jenkins.plugins.sample;

import hudson.model.Run;
import hudson.util.RunList;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class BuildSuccessRateLogic extends BuildLogic {

    public BuildSuccessRateLogic(IntervalDate period, RunList<Run> buildList) {
        super(period,true, buildList);
    }

    public Map<String, Double> getSuccessRate() throws ParseException {
        filterPeriodBuild();
        Map<String, Double> successRate = new HashMap<String, Double>();
        for (Run run : this.buildList) {
            String day =
                    DateTimeHandler.dateToString(DateTimeHandler.convertLongTimeToDate(run.getStartTimeInMillis()));
            if (successRate.containsKey(day)) {
                successRate.put(day, successRate.get(day) + run.getDuration() / 1000.0);
            } else {
                successRate.put(day, run.getDuration() / 1000.0);
            }
        }
        return successRate;
    }
}
