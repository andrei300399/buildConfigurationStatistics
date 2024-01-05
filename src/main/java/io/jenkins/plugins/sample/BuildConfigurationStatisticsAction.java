package io.jenkins.plugins.sample;

import hudson.model.Action;
import hudson.model.Job;
import java.text.ParseException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public Map<String, Double> getBuildDuration(String period, String fail, String average) throws ParseException {
        Logger LOGGER = Logger.getLogger("uuu");
        LOGGER.log(Level.INFO, "arg jelly: " + period);
        LOGGER.log(Level.INFO, "failed status: " + fail);
        LOGGER.log(Level.INFO, "avg status: " + average);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Boolean failed = fail.equals("1");
        Boolean averageTime = average.equals("1");
        return new BuildDurationLogic(intreval, failed,job.getBuilds()).getBuildsDuration(averageTime);
    }

    public Map<String, Double> getBuildSuccessRate(String period) throws ParseException {
        Logger LOGGER = Logger.getLogger("uuu1");
        LOGGER.log(Level.INFO, "arg jelly period success: " + period);
        IntervalDate intreval = IntervalDate.valueOf(period);
        return new BuildSuccessRateLogic(intreval, job.getBuilds()).getSuccessRate();
    }

    public Map<String, Double> getBuildArtifactSize(String period, String fail, String average) throws ParseException {
        IntervalDate intreval = IntervalDate.valueOf(period);
        return new BuildSuccessRateLogic(intreval, job.getBuilds()).getSuccessRate();
    }

    public Map<String, Double> getBuildTestCount(String period, String fail) throws ParseException {
        IntervalDate intreval = IntervalDate.valueOf(period);
        return new BuildSuccessRateLogic(intreval, job.getBuilds()).getSuccessRate();
    }

    public Map<String, Double> getBuildTimeQueue(String period, String average) throws ParseException {
        IntervalDate intreval = IntervalDate.valueOf(period);
        return new BuildSuccessRateLogic(intreval, job.getBuilds()).getSuccessRate();
    }

    //    @JavaScriptMethod
    //    public Map<String, Double> mark(String job1) throws ParseException {
    //        return new BuildDurationLogic(IntervalDate.YEAR, job.getBuilds()).getBuildsDuration();
    //    }

    //    public void doAction(StaplerRequest request, StaplerResponse response) throws IOException, ServletException,
    // ParseException {
    //        IntervalDate intervalDate = IntervalDate.valueOf(request.getParameter("IntervalDate"));
    //        BuildConfigurationStatisticsAction myPluginAction = new BuildConfigurationStatisticsAction(job);
    //        myPluginAction.getBuildDuration(intervalDate);
    //        response.sendRedirect2(request.getContextPath() + "/jenkins");
    //    }
}
