package io.jenkins.plugins.sample;

import hudson.model.Action;
import hudson.model.Job;

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

    public Map<Integer, Double> getBuildDuration() throws ParseException {
        return new BuildDurationLogic("month", job.getBuilds()).getBuildsDuration();
    }

}