package io.jenkins.plugins.sample;

import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Result;
import hudson.tasks.Shell;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BuildConfigurationStatisticsBuilderTest {

    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void testWorkingSystem() {
        assert 1 == 1;
    }

    @Test
    public void testSuccessBuildFromCustomBuild() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(new BuildConfigurationStatisticsBuilder());
        jenkins.buildAndAssertSuccess(project);
    }

    @Test
    public void testFailBuildFromCustomBuild() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(new Shell("echo1 hello"));
        jenkins.buildAndAssertStatus(Result.FAILURE, project);
    }

    @Test
    public void testConvertLongTimeToDate() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        long time = date.getTime();
        long resultDate = DateTimeHandler.convertDateToLongTime(date);
        assert resultDate == time;
    }

    @Test
    public void testConvertDateToLongTime() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        long time = date.getTime();
        Date resultDate = DateTimeHandler.convertLongTimeToDate(time);
        assert resultDate.equals(date);
    }
}
