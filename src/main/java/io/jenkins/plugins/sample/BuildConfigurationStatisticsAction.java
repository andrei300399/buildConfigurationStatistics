package io.jenkins.plugins.sample;

import com.google.gson.Gson;
import hudson.model.Action;
import hudson.model.Job;
import jenkins.security.stapler.StaplerDispatchable;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.bind.JavaScriptMethod;
import org.kohsuke.stapler.interceptor.RequirePOST;

import javax.servlet.ServletException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BuildConfigurationStatisticsAction implements Action {



    private Job job;
    private String str = "asd";

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

    public String getStr() {
        return str;
    }

    @JavaScriptMethod
    public String getBuildDuration(String period, String fail, String average) throws ParseException {
        Logger LOGGER = Logger.getLogger("uuu");
        LOGGER.log(Level.INFO, "arg jelly: " + period);
        LOGGER.log(Level.INFO, "failed status: " + fail);
        LOGGER.log(Level.INFO, "avg status: " + average);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Boolean failed = fail.equals("1");
        Boolean averageTime = average.equals("1");
        Gson gson = new Gson();
        Map<String, Double> map = new BuildDurationLogic(intreval, failed,job.getBuilds()).getBuildsDuration(averageTime);
         String gsonData = gson.toJson(map);
        LOGGER.log(Level.INFO, "gson: " + gsonData);
        return gsonData;
        //return new BuildDurationLogic(intreval, failed,job.getBuilds()).getBuildsDuration(averageTime);
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

    public Map<String, Double> getBuildArtifactSize(String period, String fail, String average) throws ParseException {
        Logger LOGGER = Logger.getLogger("artifact");
        LOGGER.log(Level.INFO, "arg jelly artifact: " + period);
        LOGGER.log(Level.INFO, "failed artifact: " + fail);
        LOGGER.log(Level.INFO, "avg artifact: " + average);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Boolean failed = fail.equals("1");
        Boolean averageTime = average.equals("1");
        return new BuildArtifactSizeLogic(intreval, failed, job.getBuilds()).getArtifactSize(averageTime);
    }

    public Map<String, Integer> getBuildTestCount(String period, String fail) throws ParseException {
        Logger LOGGER = Logger.getLogger("TestCount");
        LOGGER.log(Level.INFO, "arg jelly TestCount: " + period);
        LOGGER.log(Level.INFO, "failed TestCount: " + fail);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Boolean failed = fail.equals("1");

        return new BuildTestCountLogic(intreval, job.getBuilds()).getTestCount();
    }

    public Map<String, Double> getBuildTimeQueue(String period, String average) throws ParseException {
        Logger LOGGER = Logger.getLogger("queue");
        LOGGER.log(Level.INFO, "arg jelly queue: " + period);
        LOGGER.log(Level.INFO, "avg queue: " + average);
        IntervalDate intreval = IntervalDate.valueOf(period);
        Boolean averageTime = average.equals("1");
        return new BuildTimeQueueLogic(intreval, job.getBuilds()).getTimeQueue(averageTime);
    }
    // methods for js -> jelly -> java communication
    @JavaScriptMethod
    public int add(int x, int y) {
        this.str = "MONTH";
        Logger LOGGER = Logger.getLogger("uuu22312");
        LOGGER.log(Level.WARNING, "js test" + this.str);
        return x+y;

    }

        @JavaScriptMethod
        public Map<String, Double> buildto(String job1) throws ParseException {
            return new BuildDurationLogic(IntervalDate.YEAR, true, job.getBuilds()).getBuildsDuration(false);
        }
        public void doAction(StaplerRequest request, StaplerResponse response) throws IOException, ServletException,
     ParseException {
            IntervalDate intervalDate = IntervalDate.valueOf(request.getParameter("IntervalDate"));
            BuildConfigurationStatisticsAction myPluginAction = new BuildConfigurationStatisticsAction(job);
            myPluginAction.getBuildDuration("MONTH", "1", "0");
            response.sendRedirect2(request.getContextPath() + "/jenkins");
        }
    @JavaScriptMethod
    @RequirePOST
    public void mark(String job) {
        Logger LOGGER = Logger.getLogger("uuu2");
        LOGGER.log(Level.WARNING, "js test");
    }

    @StaplerDispatchable
    public void doMark(StaplerRequest req, StaplerResponse res) throws IOException {
        Logger LOGGER = Logger.getLogger("uuu3");
        LOGGER.log(Level.WARNING, "js stepler");
    }

    @JavaScriptMethod
    public void marker(String str1) {
        Logger LOGGER = Logger.getLogger("uuu1222");
        LOGGER.log(Level.INFO, "set vat: " + this.str);
    }
}
