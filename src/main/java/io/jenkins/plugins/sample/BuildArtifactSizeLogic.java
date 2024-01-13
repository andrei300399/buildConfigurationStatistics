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
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildArtifactSizeLogic extends BuildLogic {

    static Logger LOGGER = Logger.getLogger(BuildDurationLogic.class.getName());
    HashMap<String, List<Double>> dateFormatArtifact;
    String dateFormatKey;
    public BuildArtifactSizeLogic(IntervalDate period, Boolean failed, RunList<Run> buildList) {
        super(period, failed,buildList);
    }

    public Map<String, Double> getArtifactSize(Statistics statistics) throws ParseException {
        filterPeriodBuild();
        filterFailedBuild();
        switch (this.period){
            case MONTH:
                dateFormatArtifact = DateTimeHandler.createDateMonthMap();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case WEEK:
                dateFormatArtifact = DateTimeHandler.createDateWeekMap();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case YEAR:
                dateFormatArtifact = DateTimeHandler.createDateYearMap();
                dateFormatKey = "yyyy-MM";
                break;
            case QUARTER:
                dateFormatArtifact = DateTimeHandler.createDateQuarterMap();
                dateFormatKey = "yyyy-MM";
                break;
            case DAY:
                dateFormatArtifact = DateTimeHandler.createDateDayMap();
                dateFormatKey = "yyyy-MM-dd HH";
                break;
            case ALL:
                dateFormatArtifact = DateTimeHandler.createDateYearMap();
                dateFormatKey = "yyyy-MM";
                break;
        }


//        HashMap<String, Integer> dayArtifactAverage = new HashMap<>();
        for (Run run : this.buildList) {
            String dateFormatKeyAfterCheckPeriod =
                    DateTimeHandler.dateToString(
                            DateTimeHandler.convertLongTimeToDate(
                                    run.getStartTimeInMillis()
                            ), dateFormatKey
                    );
            LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod artifact: " + dateFormatKeyAfterCheckPeriod);
            if (this.period == IntervalDate.DAY) {
                dateFormatKeyAfterCheckPeriod = DateTimeHandler.dateSetZeroMinutesSeconds(dateFormatKeyAfterCheckPeriod);
                LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod for day zero: " + dateFormatKeyAfterCheckPeriod);
            }
            LOGGER.log(Level.WARNING, "getArtifacts: " + run.getArtifacts());
            List<Run.Artifact> listArtifacts = run.getArtifacts();
            double artifactsRunSize = 0;

            for (Run.Artifact artifact : listArtifacts) {
                artifactsRunSize += artifact.getFileSize()/1024.0;
                LOGGER.log(Level.WARNING, "artifact.getFileSize(): " +artifact.getFileSize());
            }

            LOGGER.log(Level.WARNING, "artifactsRunSize: " + artifactsRunSize);

//            if (dateFormatArtifact.get(dateFormatKeyAfterCheckPeriod) == 0.0) {
//                dateFormatArtifact.put(dateFormatKeyAfterCheckPeriod, artifactsRunSize);
//                dayArtifactAverage.put(dateFormatKeyAfterCheckPeriod, 1);
//            } else {
//                dateFormatArtifact.put(dateFormatKeyAfterCheckPeriod, dateFormatArtifact.get(dateFormatKeyAfterCheckPeriod) + artifactsRunSize);
//                dayArtifactAverage.put(dateFormatKeyAfterCheckPeriod, dayArtifactAverage.get(dateFormatKeyAfterCheckPeriod) + 1);
//            }
            dateFormatArtifact.get(dateFormatKeyAfterCheckPeriod).add(artifactsRunSize);
            LOGGER.log(Level.WARNING, "dateFormatArtifact: " + dateFormatArtifact);
        }


        HashMap<String, Double> dayArtifactMetric = new HashMap<String, Double>();

        for (Map.Entry<String, List<Double>> entry : dateFormatArtifact.entrySet()) {
            // work with one date array metric [1.2, 2.09, 5,09]
            DescriptiveStatistics descriptiveStatistics = new DescriptiveStatistics();
            for (double v : entry.getValue()) {
                descriptiveStatistics.addValue(v);
            }

            if (entry.getValue().size() == 0) {
                dayArtifactMetric.put(entry.getKey(), 0.0);
                continue;
            }

            switch (statistics){
                case SUM:
                    double sum = descriptiveStatistics.getSum();
                    LOGGER.log(Level.WARNING, "sum: " + sum);
                    dayArtifactMetric.put(entry.getKey(), sum);
                    break;
                case AVG:
                    double mean = descriptiveStatistics.getMean();
                    LOGGER.log(Level.WARNING, "mean: " + mean);
                    dayArtifactMetric.put(entry.getKey(), mean);
                    break;
                case RANGE:
                    double range = descriptiveStatistics.getMax() - descriptiveStatistics.getMin();
                    LOGGER.log(Level.WARNING, "range: " + range);
                    dayArtifactMetric.put(entry.getKey(), range);
                    break;
                case MEDIAN:
                    double median = descriptiveStatistics.getPercentile(50);
                    LOGGER.log(Level.WARNING, "median: " + median);
                    dayArtifactMetric.put(entry.getKey(), median);
                    break;
                case DISPERSION:
                    double dispersion = descriptiveStatistics.getPopulationVariance();
                    LOGGER.log(Level.WARNING, "sum: " + dispersion);
                    dayArtifactMetric.put(entry.getKey(), dispersion);
                    break;
                case SDUNBIASED:
                    double sdUnbiased = descriptiveStatistics.getStandardDeviation();
                    LOGGER.log(Level.WARNING, "sdUnbiased: " + sdUnbiased);
                    dayArtifactMetric.put(entry.getKey(), sdUnbiased);
                    break;
                case SD:
                    double sd = FastMath.sqrt(descriptiveStatistics.getPopulationVariance());
                    LOGGER.log(Level.WARNING, "sd: " + sd);
                    dayArtifactMetric.put(entry.getKey(), sd);
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
                    dayArtifactMetric.put(entry.getKey(), mode);
                    break;
            }


        }

//        if (average) {
//            for (Map.Entry<String, Integer> entry : dayArtifactAverage.entrySet()) {
//                LOGGER.log(Level.INFO, "sum time duration: " + dateFormatArtifact.get(entry.getKey()));
//                LOGGER.log(Level.INFO, "count runs: " + entry.getValue());
//                dateFormatArtifact.put(entry.getKey(),
//                        dateFormatArtifact.get(entry.getKey())/entry.getValue()
//                );
//            }
//        }
        LOGGER.log(Level.INFO, "dayArtifactMetric: " + dayArtifactMetric);
//        LOGGER.log(Level.INFO, "dayArtifactAverage: " + dayArtifactAverage);
        return dayArtifactMetric;
    }
}
