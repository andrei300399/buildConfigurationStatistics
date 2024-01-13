package io.jenkins.plugins.sample;

import hudson.model.AbstractBuild;
import hudson.model.Run;
import hudson.model.Queue;
import hudson.util.RunList;
import jenkins.model.Jenkins;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.function.IntFunction;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.concurrent.TimeUnit;
import hudson.model.AbstractBuild;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.FastMath;

public class BuildDurationLogic extends BuildLogic {
    static Logger LOGGER = Logger.getLogger(BuildDurationLogic.class.getName());
    HashMap<String, List<Double>> dateFormatDuration;
    //HashMap<String, List<Double>> dateFormatDurationListValues;
    String dateFormatKey;

    public BuildDurationLogic(IntervalDate period, Boolean failed, RunList<Run> buildList) {
        super(period, failed, buildList);
    }

    public Map<String, Double> getBuildsDuration(Statistics statistics) throws ParseException {
        filterPeriodBuild();
        filterFailedBuild();
        switch (this.period){
            case MONTH:
                dateFormatDuration = DateTimeHandler.createDateMonthMap();
                //dateFormatDurationListValues = DateTimeHandler.createDateMonthMap();
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
                dateFormatDuration = DateTimeHandler.createDateAllMap(this.buildList);
                dateFormatKey = "yyyy-MM-dd";
                break;
        }


        //HashMap<String, Integer> dayDurationAverage = new HashMap<>();
        LOGGER.log(Level.WARNING, "buildList: " + (this.buildList));


        for (Run run : this.buildList) {
            String dateFormatKeyAfterCheckPeriod =
                    DateTimeHandler.dateToString(
                            DateTimeHandler.convertLongTimeToDate(
                                    run.getStartTimeInMillis()
                            ), dateFormatKey
                    );
            LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod: " + dateFormatKeyAfterCheckPeriod);
            if (this.period == IntervalDate.DAY) {
                dateFormatKeyAfterCheckPeriod = DateTimeHandler.dateSetZeroMinutesSeconds(dateFormatKeyAfterCheckPeriod);
                LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod for day zero: " + dateFormatKeyAfterCheckPeriod);
            }
//            if (dateFormatDuration.get(dateFormatKeyAfterCheckPeriod) == 0.0) {
//                dateFormatDuration.put(dateFormatKeyAfterCheckPeriod, run.getDuration() / 1000.0);
//                LOGGER.log(Level.WARNING, "getDuration: " + run.getDuration());
//                dayDurationAverage.put(dateFormatKeyAfterCheckPeriod, 1);
//            } else {
//                dateFormatDuration.put(dateFormatKeyAfterCheckPeriod, dateFormatDuration.get(dateFormatKeyAfterCheckPeriod) + run.getDuration() / 1000.0);
//                dayDurationAverage.put(dateFormatKeyAfterCheckPeriod, dayDurationAverage.get(dateFormatKeyAfterCheckPeriod) + 1);
//            }
            dateFormatDuration.get(dateFormatKeyAfterCheckPeriod).add(run.getDuration() / 1000.0);
            LOGGER.log(Level.WARNING, "dateFormatDurationListValues: " + dateFormatDuration);



        }

        HashMap<String, Double> dayDurationMetric = new HashMap<String, Double>();

        for (Map.Entry<String, List<Double>> entry : dateFormatDuration.entrySet()) {
            // work with one date array metric [1.2, 2.09, 5,09]
            DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
            for (double v : entry.getValue()) {
                descriptiveStatistics.addValue(v);
            }

            if (entry.getValue().size() == 0) {
                dayDurationMetric.put(entry.getKey(), 0.0);
                continue;
            }

            switch (statistics){
                case SUM:
                    double sum = descriptiveStatistics.getSum();
                    LOGGER.log(Level.WARNING, "sum: " + sum);
                    dayDurationMetric.put(entry.getKey(), sum);
                    break;
                case AVG:
                    double mean = descriptiveStatistics.getMean();
                    LOGGER.log(Level.WARNING, "mean: " + mean);
                    dayDurationMetric.put(entry.getKey(), mean);
                    break;
                case MEDIAN:
                    double median = descriptiveStatistics.getPercentile(50);
                    LOGGER.log(Level.WARNING, "median: " + median);
                    dayDurationMetric.put(entry.getKey(), median);
                    break;
                case DISPERSION:
                    double dispersion = descriptiveStatistics.getPopulationVariance();
                    LOGGER.log(Level.WARNING, "sum: " + dispersion);
                    dayDurationMetric.put(entry.getKey(), dispersion);
                    break;
                case SDUNBIASED:
                    double sdUnbiased = descriptiveStatistics.getStandardDeviation();
                    LOGGER.log(Level.WARNING, "sdUnbiased: " + sdUnbiased);
                    dayDurationMetric.put(entry.getKey(), sdUnbiased);
                    break;
                case SD:
                    double sd = FastMath.sqrt(descriptiveStatistics.getPopulationVariance());
                    LOGGER.log(Level.WARNING, "sd: " + sd);
                    dayDurationMetric.put(entry.getKey(), sd);
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
                    dayDurationMetric.put(entry.getKey(), mode);
                    break;
            }


        }

        LOGGER.log(Level.INFO, "dayDurationMetric: " + dayDurationMetric);
        return dayDurationMetric;
    }
}


