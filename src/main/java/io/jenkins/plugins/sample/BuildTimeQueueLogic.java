package io.jenkins.plugins.sample;

import hudson.model.Run;
import hudson.util.RunList;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.FastMath;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildTimeQueueLogic extends BuildLogic {
    static Logger LOGGER = Logger.getLogger(BuildDurationLogic.class.getName());

    HashMap<String, List<Double>> dateFormatDuration;
    String dateFormatKey;
    public BuildTimeQueueLogic(IntervalDate period, RunList<Run> buildList) {
        super(period, true, buildList);
    }

    public Map<String, Double> getTimeQueue(Statistics statistics) throws ParseException {
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
            case QUARTER:
                dateFormatDuration = DateTimeHandler.createDateQuarterMap();
                dateFormatKey = "yyyy-MM";
                break;
            case DAY:
                dateFormatDuration = DateTimeHandler.createDateDayMap();
                dateFormatKey = "yyyy-MM-dd HH";
                break;
            case ALL:
                dateFormatDuration = DateTimeHandler.createDateYearMap();
                dateFormatKey = "yyyy-MM";
                break;
        }


//        HashMap<String, Integer> dayDurationAverage = new HashMap<>();
        for (Run run : this.buildList) {
            String dateFormatKeyAfterCheckPeriod =
                    DateTimeHandler.dateToString(
                            DateTimeHandler.convertLongTimeToDate(
                                    run.getStartTimeInMillis()
                            ), dateFormatKey
                    );
            LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod time Queue: " + dateFormatKeyAfterCheckPeriod);
            if (this.period == IntervalDate.DAY) {
                dateFormatKeyAfterCheckPeriod = DateTimeHandler.dateSetZeroMinutesSeconds(dateFormatKeyAfterCheckPeriod);
                LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod for day zero: " + dateFormatKeyAfterCheckPeriod);
            }
            long runTimeInQueue = new TimeInQueueFetcher().getTimeInQueue(run);
//            if (dateFormatDuration.get(dateFormatKeyAfterCheckPeriod) == 0.0) {
//
//                LOGGER.log(Level.WARNING, "getTimeInQueue long: " + runTimeInQueue);
//                dateFormatDuration.put(dateFormatKeyAfterCheckPeriod,  (double) runTimeInQueue);
//                LOGGER.log(Level.WARNING, "getTimeInQueue double: " + (double) runTimeInQueue);
//                dayDurationAverage.put(dateFormatKeyAfterCheckPeriod, 1);
//            } else {
//                dateFormatDuration.put(dateFormatKeyAfterCheckPeriod, dateFormatDuration.get(dateFormatKeyAfterCheckPeriod) + (double) runTimeInQueue);
//                dayDurationAverage.put(dateFormatKeyAfterCheckPeriod, dayDurationAverage.get(dateFormatKeyAfterCheckPeriod) + 1);
//            }
            dateFormatDuration.get(dateFormatKeyAfterCheckPeriod).add((double) runTimeInQueue);
            LOGGER.log(Level.WARNING, "dateFormatDurationListValues: " + dateFormatDuration);
        }
//        if (average) {
//            for (Map.Entry<String, Integer> entry : dayDurationAverage.entrySet()) {
//                LOGGER.log(Level.INFO, "sum time queue: " + dateFormatDuration.get(entry.getKey()));
//                LOGGER.log(Level.INFO, "count runs time queue: " + entry.getValue());
//                dateFormatDuration.put(entry.getKey(),
//                        dateFormatDuration.get(entry.getKey())/entry.getValue()
//                );
//            }
//        }
        HashMap<String, Double> dayTimeQueueMetric = new HashMap<String, Double>();

        for (Map.Entry<String, List<Double>> entry : dateFormatDuration.entrySet()) {
            // work with one date array metric [1.2, 2.09, 5,09]
            DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
            for (double v : entry.getValue()) {
                descriptiveStatistics.addValue(v);
            }

            if (entry.getValue().size() == 0) {
                dayTimeQueueMetric.put(entry.getKey(), 0.0);
                continue;
            }

            switch (statistics){
                case SUM:
                    double sum = descriptiveStatistics.getSum();
                    LOGGER.log(Level.WARNING, "sum: " + sum);
                    dayTimeQueueMetric.put(entry.getKey(), sum);
                    break;
                case AVG:
                    double mean = descriptiveStatistics.getMean();
                    LOGGER.log(Level.WARNING, "mean: " + mean);
                    dayTimeQueueMetric.put(entry.getKey(), mean);
                    break;
                case RANGE:
                    double range = descriptiveStatistics.getMax() - descriptiveStatistics.getMin();
                    LOGGER.log(Level.WARNING, "range: " + range);
                    dayTimeQueueMetric.put(entry.getKey(), range);
                    break;
                case MEDIAN:
                    double median = descriptiveStatistics.getPercentile(50);
                    LOGGER.log(Level.WARNING, "median: " + median);
                    dayTimeQueueMetric.put(entry.getKey(), median);
                    break;
                case DISPERSION:
                    double dispersion = descriptiveStatistics.getPopulationVariance();
                    LOGGER.log(Level.WARNING, "sum: " + dispersion);
                    dayTimeQueueMetric.put(entry.getKey(), dispersion);
                    break;
                case SDUNBIASED:
                    double sdUnbiased = descriptiveStatistics.getStandardDeviation();
                    LOGGER.log(Level.WARNING, "sdUnbiased: " + sdUnbiased);
                    dayTimeQueueMetric.put(entry.getKey(), sdUnbiased);
                    break;
                case SD:
                    double sd = FastMath.sqrt(descriptiveStatistics.getPopulationVariance());
                    LOGGER.log(Level.WARNING, "sd: " + sd);
                    dayTimeQueueMetric.put(entry.getKey(), sd);
                    break;
                case MODE:
                    //prepare for mode with StatUtils methods
                    double[] doublesArray = entry.getValue().stream().mapToDouble(d -> d).toArray();
                    double[] modes = StatUtils.mode(doublesArray);
                    double mode;

                    if (modes.length == doublesArray.length) {
                        mode = 0;
                    } else {
                        mode = modes[0];
                    }

                    LOGGER.log(Level.WARNING, "mode: " + mode);
                    dayTimeQueueMetric.put(entry.getKey(), mode);
                    break;
            }


        }
        LOGGER.log(Level.INFO, "dayTimeQueueMetric time queue: " + dayTimeQueueMetric);
        //LOGGER.log(Level.INFO, "dayDurationAverage time queue: " + dayDurationAverage);
        return dayTimeQueueMetric;
    }
}


