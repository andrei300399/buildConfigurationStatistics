package io.jenkins.plugins.sample;

import hudson.model.*;
import hudson.tasks.Shell;
import hudson.util.RunList;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Test
    public void testGetDayOfMonth() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        Date date2 = dateFormat.parse("29/02/2008");
        int daysDate = DateTimeHandler.getDayOfMonth(date);
        int daysDate2 = DateTimeHandler.getDayOfMonth(date2);
        assert daysDate == 23;
        assert daysDate2 == 29;
    }

    @Test
    public void testGetCurrentMonthDays()  {
        int daysDate = DateTimeHandler.getCurrentMonthDays();
        Calendar mycal = new GregorianCalendar();
        int daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);
        assert daysDate == daysInMonth;
    }

    @Test
    public void testGetLastMonthDays(){
        int daysDate = DateTimeHandler.getLastMonthDays();
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.add(Calendar.MONTH, -1);
        int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        assert daysDate == daysInMonth;
    }

    @Test
    public void testDateToString() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        String strDate = DateTimeHandler.dateToString(date, "dd-MM-yyyy");
        assert strDate.equals("23-09-2007");
    }

    @Test
    public void testDateMonthToString() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = dateFormat.parse("23/09/2007");
        String strDate = DateTimeHandler.dateMonthToString(date);
        assert strDate.equals("2007-09");
    }

    @Test
    public void testCreateDateMonthMap()  {
        int daysDate = DateTimeHandler.getLastMonthDays();
        HashMap<String, List<Double>> dictDateMonthZero = DateTimeHandler.createDateMonthMap();
        assert  dictDateMonthZero.size() == daysDate;
        assert  !dictDateMonthZero.isEmpty();
        for (Map.Entry<String, List<Double>> entry : dictDateMonthZero.entrySet()) {
            assert entry.getValue().isEmpty();
        }
    }

    @Test
    public void testCreateDateWeekMapSuccessRate()  {

        HashMap<String, HashMap<String, Integer>> dictDateMonthZero = DateTimeHandler.createDateWeekMapSuccessRate();
        assert  dictDateMonthZero.size() == 7;
        assert  !dictDateMonthZero.isEmpty();
        for (Map.Entry<String, HashMap<String, Integer>> entry : dictDateMonthZero.entrySet()) {
            assert entry.getValue().equals(new HashMap(){{
                put("fail", 0);
                put("success", 0);
            }});
        }
    }

    @Test
    public void testCreateDateMonthMapSuccessRate()  {
        int daysDate = DateTimeHandler.getLastMonthDays();
        HashMap<String, HashMap<String, Integer>> dictDateMonthZero = DateTimeHandler.createDateMonthMapSuccessRate();
        assert  dictDateMonthZero.size() == daysDate;
        assert  !dictDateMonthZero.isEmpty();
        for (Map.Entry<String, HashMap<String, Integer>> entry : dictDateMonthZero.entrySet()) {
            assert entry.getValue().equals(new HashMap(){{
                put("fail", 0);
                put("success", 0);
            }});
        }
    }

    @Test
    public void testCreateDateMonthMapTestCount()  {
        int daysDate = DateTimeHandler.getLastMonthDays();
        HashMap<String, Integer> dictDateMonthZero = DateTimeHandler.createDateMonthMapTestCount();
        assert  dictDateMonthZero.size() == daysDate;
        assert  !dictDateMonthZero.isEmpty();
        for (Map.Entry<String, Integer> entry : dictDateMonthZero.entrySet()) {
            assert entry.getValue() == 0;
        }
    }

    @Test
    public void testGetTimeInQueue() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(new BuildConfigurationStatisticsBuilder());
        jenkins.buildAndAssertSuccess(project);
        Run run = project.getBuilds().getLastBuild();
        long time = new TimeInQueueFetcher().getTimeInQueue(run);
        long queuedTime = run.getStartTimeInMillis() - run.getTimeInMillis();
        assert time == queuedTime;
    }

    @Test
    public void testCreateDateYearMap()  {
        HashMap<String, Double> dictDateYearZero = DateTimeHandler.createDateYearMap();
        assert  dictDateYearZero.size() == 12;
        assert  !dictDateYearZero.isEmpty();
        for (Map.Entry<String, Double> entry : dictDateYearZero.entrySet()) {
            assert entry.getValue() == 0.0;
        }
    }

    @Test
    public void testCreateDateWeekMap()  {
        HashMap<String, Double> dictDateWeekZero = DateTimeHandler.createDateWeekMap();
        assert  dictDateWeekZero.size() == 7;
        assert  !dictDateWeekZero.isEmpty();
        for (Map.Entry<String, Double> entry : dictDateWeekZero.entrySet()) {
            assert entry.getValue() == 0.0;
        }
    }

    @Test
    public void testCreateDateYearMapSuccessRate()  {
        HashMap<String, HashMap<String, Integer>> dictDateYearZero = DateTimeHandler.createDateYearMapSuccessRate();
        assert  dictDateYearZero.size() == 12;
        assert  !dictDateYearZero.isEmpty();
        for (Map.Entry<String, HashMap<String, Integer>> entry : dictDateYearZero.entrySet()) {
            assert entry.getValue().equals(new HashMap(){{
                put("fail", 0);
                put("success", 0);
            }});
        }
    }

    @Test
    public void testFilterPeriodBuild() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(new BuildConfigurationStatisticsBuilder());
        jenkins.buildAndAssertSuccess(project);
        jenkins.buildAndAssertSuccess(project);
        List<Run> runList = new RunList<>(project);

        BuildLogic instance1 = new BuildLogic(IntervalDate.WEEK, true, (RunList<Run>) runList);
        instance1.filterPeriodBuild();

        assert  instance1.buildList.size() == 2;

        BuildLogic instance2 = new BuildLogic(IntervalDate.ALL, true, (RunList<Run>) runList);
        instance2.filterPeriodBuild();

        assert  instance2.buildList.size() == 2;

        BuildLogic instance3 = new BuildLogic(IntervalDate.MONTH, true, (RunList<Run>) runList);
        instance3.filterPeriodBuild();

        assert  instance3.buildList.size() == 2;

        BuildLogic instance4 = new BuildLogic(IntervalDate.YEAR, true, (RunList<Run>) runList);
        instance4.filterPeriodBuild();

        assert  instance4.buildList.size() == 2;
    }
    @Test
    public void testFilterFailedBuild() throws Exception {
        FreeStyleProject project = jenkins.createFreeStyleProject();
        project.getBuildersList().add(new BuildConfigurationStatisticsBuilder());
        jenkins.buildAndAssertSuccess(project);
        project.getBuildersList().add(new Shell("echo1 hello"));
        jenkins.buildAndAssertStatus(Result.FAILURE, project);
        List<Run> runList = new RunList<>(project);
        for (Run run :runList) {
            System.out.println(run.getResult());
        }

        BuildLogic instance1 = new BuildLogic(IntervalDate.WEEK, false, (RunList<Run>) runList);
        instance1.filterFailedBuild();

        assert  instance1.buildList.size() == 1;

    }

    @Test
    public void testCreateDateQuarterMap()  {
        HashMap<String, Double> dictDateQuarterZero = DateTimeHandler.createDateQuarterMap();
        assert  dictDateQuarterZero.size() == 4;
        assert  !dictDateQuarterZero.isEmpty();
        for (Map.Entry<String, Double> entry : dictDateQuarterZero.entrySet()) {
            assert entry.getValue() == 0.0;
        }
    }

    @Test
    public void testCreateDateDayMap() throws ParseException {
        HashMap<String, Double> dictDateDayZero = DateTimeHandler.createDateDayMap();
        assert  dictDateDayZero.size() == 24;
        assert  !dictDateDayZero.isEmpty();
        for (Map.Entry<String, Double> entry : dictDateDayZero.entrySet()) {
            System.out.println(entry.getKey());
            assert entry.getValue() == 0.0;
        }
    }
}
