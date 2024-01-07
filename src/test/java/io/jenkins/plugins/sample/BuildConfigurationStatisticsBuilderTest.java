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
        HashMap<String, Double> dictDateMonthZero = DateTimeHandler.createDateMonthMap();
        assert  dictDateMonthZero.size() == daysDate;
        assert  !dictDateMonthZero.isEmpty();
        for (Map.Entry<String, Double> entry : dictDateMonthZero.entrySet()) {
            assert entry.getValue() == 0.0;
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




}
