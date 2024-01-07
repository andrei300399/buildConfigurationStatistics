package io.jenkins.plugins.sample;

import hudson.model.Run;
import hudson.util.RunList;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildTimeQueueLogic extends BuildLogic {
    static Logger LOGGER = Logger.getLogger(BuildDurationLogic.class.getName());

    HashMap<String, Double> dateFormatDuration;
    String dateFormatKey;
    public BuildTimeQueueLogic(IntervalDate period, RunList<Run> buildList) {
        super(period, true, buildList);
    }

    public Map<String, Double> getTimeQueue(Boolean average) throws ParseException {
        filterPeriodBuild();

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
            LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod time Queue: " + dateFormatKeyAfterCheckPeriod);
            long runTimeInQueue = new TimeInQueueFetcher().getTimeInQueue(run);
            if (dateFormatDuration.get(dateFormatKeyAfterCheckPeriod) == 0.0) {

                LOGGER.log(Level.WARNING, "getTimeInQueue long: " + runTimeInQueue);
                dateFormatDuration.put(dateFormatKeyAfterCheckPeriod,  (double) runTimeInQueue);
                LOGGER.log(Level.WARNING, "getTimeInQueue double: " + (double) runTimeInQueue);
                dayDurationAverage.put(dateFormatKeyAfterCheckPeriod, 1);
            } else {
                dateFormatDuration.put(dateFormatKeyAfterCheckPeriod, dateFormatDuration.get(dateFormatKeyAfterCheckPeriod) + (double) runTimeInQueue);
                dayDurationAverage.put(dateFormatKeyAfterCheckPeriod, dayDurationAverage.get(dateFormatKeyAfterCheckPeriod) + 1);
            }
        }
        if (average) {
            for (Map.Entry<String, Integer> entry : dayDurationAverage.entrySet()) {
                LOGGER.log(Level.INFO, "sum time queue: " + dateFormatDuration.get(entry.getKey()));
                LOGGER.log(Level.INFO, "count runs time queue: " + entry.getValue());
                dateFormatDuration.put(entry.getKey(),
                        dateFormatDuration.get(entry.getKey())/entry.getValue()
                );
            }
        }
        LOGGER.log(Level.INFO, "dateFormatDuration time queue: " + dateFormatDuration);
        LOGGER.log(Level.INFO, "dayDurationAverage time queue: " + dayDurationAverage);
        return dateFormatDuration;
    }
}


