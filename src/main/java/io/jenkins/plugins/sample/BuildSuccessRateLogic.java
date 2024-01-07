package io.jenkins.plugins.sample;

import hudson.model.Result;
import hudson.model.Run;
import hudson.util.RunList;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildSuccessRateLogic extends BuildLogic {

    static Logger LOGGER = Logger.getLogger(BuildSuccessRateLogic.class.getName());
    HashMap<String, HashMap<String,Integer>> successRateOnFormatDate;
    String dateFormatKey;

    public BuildSuccessRateLogic(IntervalDate period, RunList<Run> buildList) {
        super(period,false, buildList);
    }

//    public Map<String, Double> getSuccessRate() throws ParseException {
//        filterPeriodBuild();
//        Map<String, Double> successRate = new HashMap<String, Double>();
//        for (Run run : this.buildList) {
//            String day ="kjk";
//                    //DateTimeHandler.dateToString(DateTimeHandler.convertLongTimeToDate(run.getStartTimeInMillis()));
//            if (successRate.containsKey(day)) {
//                successRate.put(day, successRate.get(day) + run.getDuration() / 1000.0);
//            } else {
//                successRate.put(day, run.getDuration() / 1000.0);
//            }
//        }
//        return successRate;
//    }

    public Map<String, Double> getSuccessRate() throws ParseException {
        filterPeriodBuild();
        switch (this.period){
            case MONTH:
                successRateOnFormatDate = DateTimeHandler.createDateMonthMapSuccessRate();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case WEEK:
                successRateOnFormatDate = DateTimeHandler.createDateWeekMapSuccessRate();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case YEAR:
                successRateOnFormatDate = DateTimeHandler.createDateYearMapSuccessRate();
                dateFormatKey = "yyyy-MM";
                break;
        }

        for (Run run : this.buildList) {
            String dateFormatKeyAfterCheckPeriod =
                    DateTimeHandler.dateToString(
                            DateTimeHandler.convertLongTimeToDate(
                                    run.getStartTimeInMillis()
                            ), dateFormatKey
                    );
            LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod: " + dateFormatKeyAfterCheckPeriod);
            if (run.getResult().isBetterOrEqualTo(Result.SUCCESS)){
                successRateOnFormatDate.get(dateFormatKeyAfterCheckPeriod).put("success", (successRateOnFormatDate.get(dateFormatKeyAfterCheckPeriod).get("success")) + 1);
            } else {
                successRateOnFormatDate.get(dateFormatKeyAfterCheckPeriod).put("fail", (successRateOnFormatDate.get(dateFormatKeyAfterCheckPeriod).get("fail")) + 1);
            }
            LOGGER.log(Level.INFO, "successRateOnFormatDate: " + successRateOnFormatDate);

//            if (successRateOnFormatDate.get(dateFormatKeyAfterCheckPeriod).get("fail") == 0.0) {
//                successRateOnFormatDate.put(dateFormatKeyAfterCheckPeriod,
//                        //(run.getResult().isBetterOrEqualTo(Result.SUCCESS)) ? );
//
//            } else {
//                successRateOnFormatDate.put(dateFormatKeyAfterCheckPeriod, successRateOnFormatDate.get(dateFormatKeyAfterCheckPeriod) + run.getDuration() / 1000.0);
//            }
        }
        HashMap<String, Double> successRateMap = new HashMap<String, Double>();
        for (Map.Entry<String, HashMap<String, Integer>> entry : successRateOnFormatDate.entrySet()) {
                LOGGER.log(Level.INFO, "succes value on date: " + entry.getValue());
                LOGGER.log(Level.INFO, "key success rate: " + entry.getKey());
                if ((entry.getValue().get("success")+entry.getValue().get("fail")) == 0) {
                    successRateMap.put(entry.getKey(), 0.0);
                } else {
                    successRateMap.put(entry.getKey(),
                            Double.valueOf(entry.getValue().get("success"))/(entry.getValue().get("success")+entry.getValue().get("fail"))
                    );
                }
        }
        LOGGER.log(Level.INFO, "successRateMap: " + successRateMap);

        return successRateMap;
    }
}
