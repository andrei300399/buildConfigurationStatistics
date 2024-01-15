package io.jenkins.plugins.sample;

import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.tasks.test.AbstractTestResultAction;
import hudson.tasks.test.AggregatedTestResultAction;
import hudson.util.RunList;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class BuildTestCountLogic extends BuildLogic {

    static Logger LOGGER = Logger.getLogger(BuildTestCountLogic.class.getName());
    HashMap<String, Integer> testCountOnFormatDate;
    String dateFormatKey;

    public BuildTestCountLogic(IntervalDate period, RunList<Run> buildList) {
        super(period, true, buildList);
    }

    public Map<String, Integer> getTestCount() throws ParseException {
        filterPeriodBuild();
        filterPeriodBuild();
        switch (this.period) {
            case MONTH:
                testCountOnFormatDate = DateTimeHandler.createDateMonthMapTestCount();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case WEEK:
                testCountOnFormatDate = DateTimeHandler.createDateWeekMapTestCount();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case YEAR:
                testCountOnFormatDate = DateTimeHandler.createDateYearMapTestCount();
                dateFormatKey = "yyyy-MM";
                break;
            case QUARTER:
                testCountOnFormatDate = DateTimeHandler.createDateQuarterMapTestCount();
                dateFormatKey = "yyyy-MM";
                break;
            case DAY:
                testCountOnFormatDate = DateTimeHandler.createDateDayMapTestCount();
                dateFormatKey = "yyyy-MM-dd HH";
                break;
            case ALL:
                testCountOnFormatDate = DateTimeHandler.createDateDayMapTestCount();
                dateFormatKey = "yyyy-MM";
                break;
        }

        for (Run run : this.buildList) {
            LOGGER.log(Level.FINEST, "testCountMap: " + getTestCountForRun(run));

            String dateFormatKeyAfterCheckPeriod =
                    DateTimeHandler.dateToString(
                            DateTimeHandler.convertLongTimeToDate(
                                    run.getStartTimeInMillis()
                            ), dateFormatKey
                    );
            LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod: " + dateFormatKeyAfterCheckPeriod);
            if (this.period == IntervalDate.DAY) {
                dateFormatKeyAfterCheckPeriod = DateTimeHandler.dateSetZeroMinutesSeconds(dateFormatKeyAfterCheckPeriod);
                LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod for day zero: " + dateFormatKeyAfterCheckPeriod);
            }
            if (testCountOnFormatDate.get(dateFormatKeyAfterCheckPeriod) == 0) {
                testCountOnFormatDate.put(dateFormatKeyAfterCheckPeriod, getTestCountForRun(run));
                LOGGER.log(Level.WARNING, "getTestCountForRun: " + getTestCountForRun(run));

            } else {
                testCountOnFormatDate.put(dateFormatKeyAfterCheckPeriod, testCountOnFormatDate.get(dateFormatKeyAfterCheckPeriod) + getTestCountForRun(run));
            }
        }
        LOGGER.log(Level.INFO, "testCountOnFormatDate: " + testCountOnFormatDate);
        return testCountOnFormatDate;
    }


    public int getTestCountForRun(Run run) {
        int testCount = 0;
        List<AbstractTestResultAction> testActions = run.getActions(AbstractTestResultAction.class);
        for (AbstractTestResultAction testAction : testActions) {
            LOGGER.log(Level.INFO, "testAction: " + testAction);
            LOGGER.log(Level.INFO, "getPassedTests: " + testAction.getPassedTests());
            LOGGER.log(Level.INFO, "getPassedTests: " + testAction.getPassedTests().size());
            LOGGER.log(Level.INFO, "getFailedTests: " + testAction.getFailedTests());
            testCount+=testAction.getPassedTests().size();

        }

        return testCount;

    }
}
