package io.jenkins.plugins.sample;

import hudson.model.Action;
import hudson.model.Job;
import hudson.model.Run;
import jenkins.model.RunAction2;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import java.io.IOException;

public class BuildConfigurationStatisticsAction implements Action {

    private String name;
    private transient Run run;
    Job job;

    public BuildConfigurationStatisticsAction(Job job) {
        this.job = job;
    }

    public String getName() {
        return name;
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
    

    public Run getRun() {
        return run;
    }

    public void doReprocessBlameReport(StaplerRequest request, StaplerResponse response) throws IOException {
        String originalRequestURI = request.getOriginalRequestURI();
        response.sendRedirect(originalRequestURI.substring(0, originalRequestURI.lastIndexOf('/')));
    }
}