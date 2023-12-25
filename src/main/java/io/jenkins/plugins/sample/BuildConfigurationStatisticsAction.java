package io.jenkins.plugins.sample;

import hudson.model.Action;
import hudson.model.Job;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.bind.JavaScriptMethod;

import javax.servlet.ServletException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

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

    public Map<String, Double> getBuildDuration(IntervalDate interval) throws ParseException {
        return new BuildDurationLogic(interval, job.getBuilds()).getBuildsDuration();
    }

    public Map<String, Double> getBuildSuccessRate(IntervalDate interval) throws ParseException {
        return new BuildSuccessRateLogic(interval, job.getBuilds()).getSuccessRate();
    }

    @JavaScriptMethod
    public Map<String, Double> mark(String job1) throws ParseException {
        return new BuildDurationLogic(IntervalDate.YEAR, job.getBuilds()).getBuildsDuration();
    }

    public void doAction(StaplerRequest request, StaplerResponse response) throws IOException, ServletException, ParseException {
        IntervalDate intervalDate = IntervalDate.valueOf(request.getParameter("IntervalDate"));
        BuildConfigurationStatisticsAction myPluginAction = new BuildConfigurationStatisticsAction(job);
        myPluginAction.getBuildDuration(intervalDate);
        response.sendRedirect2(request.getContextPath() + "/jenkins");
    }
}
