package io.jenkins.plugins.sample;

import com.google.gson.Gson;
import hudson.model.Action;
import hudson.model.Job;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import java.text.ParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class BuildConfigurationStatisticsAction implements Action {
    private Job job;

    public BuildConfigurationStatisticsAction(Job job) {
        this.job = job;
    }

    @Override
    public String getIconFileName() {
        return "document.png";
    }

    @Override
    public String getDisplayName() {
        return "Build Configuration Statistics";
    }

    @Override
    public String getUrlName() {
        return "buildConfigurationStatistics";
    }

    public Job getJob() {
        return job;
    }

    @JavaScriptMethod
    public String getBuildDuration(String period, String fail, String statistics) throws ParseException {
        Logger LOGGER = Logger.getLogger("uuu");
        LOGGER.log(Level.INFO, "arg jelly: " + period);
        LOGGER.log(Level.INFO, "failed status: " + fail);
        LOGGER.log(Level.INFO, "statistics status: " + statistics);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Statistics statisticsEnum = Statistics.valueOf(statistics);
        Boolean failed = fail.equals("1");
        //Boolean averageTime = average.equals("1");
        Gson gson = new Gson();
        Map<String, Double> map = new BuildDurationLogic(intreval, failed,job.getBuilds()).getBuildsDuration(statisticsEnum);
         String gsonData = gson.toJson(map);
        LOGGER.log(Level.INFO, "gson: " + gsonData);
        return gsonData;
    }
    @JavaScriptMethod
    public String getBuildSuccessRate(String period) throws ParseException {
        Logger LOGGER = Logger.getLogger("uuu1");
        LOGGER.log(Level.INFO, "arg jelly period success: " + period);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Gson gson = new Gson();
        Map<String, Double> map = new BuildSuccessRateLogic(intreval,job.getBuilds()).getSuccessRate();
        String gsonData = gson.toJson(map);
        LOGGER.log(Level.INFO, "gson: " + gsonData);
        return gsonData;
    }

    @JavaScriptMethod
    public String getBuildArtifactSize(String period, String fail, String statistics) throws ParseException {
        Logger LOGGER = Logger.getLogger("artifact");
        LOGGER.log(Level.INFO, "arg jelly artifact: " + period);
        LOGGER.log(Level.INFO, "failed artifact: " + fail);
        LOGGER.log(Level.INFO, "statistics artifact: " + statistics);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Statistics statisticsEnum = Statistics.valueOf(statistics);
        Boolean failed = fail.equals("1");
//        Boolean averageTime = average.equals("1");

        Gson gson = new Gson();
        Map<String, Double> map = new BuildArtifactSizeLogic(intreval, failed, job.getBuilds()).getArtifactSize(statisticsEnum);
        String gsonData = gson.toJson(map);
        LOGGER.log(Level.INFO, "gson artifact: " + gsonData);
        return gsonData;
    }

    @JavaScriptMethod
    public String getBuildTestCount(String period, String fail) throws ParseException {
        Logger LOGGER = Logger.getLogger("TestCount");
        LOGGER.log(Level.INFO, "arg jelly TestCount: " + period);
        LOGGER.log(Level.INFO, "failed TestCount: " + fail);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Boolean failed = fail.equals("1");

        Gson gson = new Gson();
        Map<String, Integer> map = new BuildTestCountLogic(intreval, job.getBuilds()).getTestCount();
        String gsonData = gson.toJson(map);
        LOGGER.log(Level.INFO, "gson TestCount: " + gsonData);
        return gsonData;
    }

    @JavaScriptMethod
    public String getBuildTimeQueue(String period, String statistics) throws ParseException {
        Logger LOGGER = Logger.getLogger("queue");
        LOGGER.log(Level.INFO, "arg jelly queue: " + period);
        LOGGER.log(Level.INFO, "statistics queue: " + statistics);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Statistics statisticsEnum = Statistics.valueOf(statistics);
//        Boolean averageTime = average.equals("1");

        Gson gson = new Gson();
        Map<String, Double> map = new BuildTimeQueueLogic(intreval, job.getBuilds()).getTimeQueue(statisticsEnum);
        String gsonData = gson.toJson(map);
        LOGGER.log(Level.INFO, "gson TimeQueue: " + gsonData);
        return gsonData;

    }

    @JavaScriptMethod
    public double getPredicted(String period) throws ParseException {
        Logger LOGGER = Logger.getLogger("getPredicted");
        LOGGER.log(Level.INFO, "getPredicted period: " + period);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Map<String, Double> map = new BuildDurationLogic(intreval, true,job.getBuilds()).getBuildsDuration(Statistics.AVG);
        String formatDate;

        Map<Long, Double> newMap = new HashMap<Long, Double>();
        for(Map.Entry<String, Double> entry : map.entrySet()) {
            //LOGGER.log(Level.INFO, "map entrySet: " + map.entrySet());
            LOGGER.log(Level.INFO, "entrySet: " + entry.getKey() + " - " + entry.getValue());
            LOGGER.log(Level.INFO, "string date zero: " + DateTimeHandler.dateSetZeroDay(entry.getKey()));
            String parsedDate;
            if (intreval == IntervalDate.YEAR || intreval == IntervalDate.QUARTER) {
                parsedDate = DateTimeHandler.dateSetZeroDay(entry.getKey());
                formatDate = "yyyy-MM-dd";
            } else if (intreval == IntervalDate.DAY){
                parsedDate = entry.getKey();
                formatDate = "yyyy-MM-dd HH";
            }
            else {
                parsedDate = entry.getKey();
                formatDate = "yyyy-MM-dd";
            }

            LOGGER.log(Level.INFO, "string date: " + DateTimeHandler.convertStringToDate(parsedDate, formatDate));
            LOGGER.log(Level.INFO, "long time: " + DateTimeHandler.convertDateToLongTime(DateTimeHandler.convertStringToDate(parsedDate, formatDate)));
            newMap.put(DateTimeHandler.convertDateToLongTime(DateTimeHandler.convertStringToDate(parsedDate, formatDate)), entry.getValue());
        }
        LOGGER.log(Level.INFO, "newMap: " + newMap.keySet());
        SortedSet<Long> keys = new TreeSet<>(newMap.keySet());
        double[] listValuesMetric = new double[keys.size()];

        int i = 0;
        for (Long key : keys) {
            Double value = newMap.get(key);
            LOGGER.log(Level.INFO, "sorted key and value: " + key + " - " + value);
            // do something
            listValuesMetric[i] = value;
            LOGGER.log(Level.INFO, "listValuesMetric[i]: " + listValuesMetric[i]);
            LOGGER.log(Level.INFO, "listValuesMetric: " + Arrays.toString(listValuesMetric));
            i++;
        }
        LOGGER.log(Level.INFO, "listValuesMetric: " + Arrays.toString(listValuesMetric));
        double[] arrWeights = LinearRegressionHandler.calculateWeightMetric(listValuesMetric);
        LOGGER.log(Level.INFO, "arrWeights: " + Arrays.toString(arrWeights));
        LOGGER.log(Level.INFO, "listValuesMetric: " + Arrays.toString(listValuesMetric));
        double predictedNextValue = LinearRegressionHandler.linearRegression(listValuesMetric, arrWeights);
        return predictedNextValue;

    }
}
