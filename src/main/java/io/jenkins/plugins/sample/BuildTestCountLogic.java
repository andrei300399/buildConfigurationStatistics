package io.jenkins.plugins.sample;

import hudson.model.AbstractProject;
import hudson.model.Result;
import hudson.model.Run;
import hudson.util.RunList;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import hudson.tasks.junit.TestResultAction;

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
                testCountOnFormatDate = DateTimeHandler.createDateMonthMapTestCount();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case YEAR:
                testCountOnFormatDate = DateTimeHandler.createDateMonthMapTestCount();
                dateFormatKey = "yyyy-MM";
                break;
        }

        for (Run run : this.buildList) {
            String dateFormatKeyAfterCheckPeriod =
                    DateTimeHandler.dateToString(
                            DateTimeHandler.convertLongTimeToDate(
                                    run.getStartTimeInMillis()
                            ), dateFormatKey
                    );
            LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod: " + dateFormatKeyAfterCheckPeriod);
            if (testCountOnFormatDate.get(dateFormatKeyAfterCheckPeriod) == 0) {
                testCountOnFormatDate.put(dateFormatKeyAfterCheckPeriod, getTestCountForRun(run));
            } else {
                testCountOnFormatDate.put(dateFormatKeyAfterCheckPeriod, testCountOnFormatDate.get(dateFormatKeyAfterCheckPeriod) + getTestCountForRun(run));
            }
            LOGGER.log(Level.INFO, "testCountOnFormatDate: " + testCountOnFormatDate);

        }

        LOGGER.log(Level.INFO, "testCountMap: " + testCountOnFormatDate);

        return testCountOnFormatDate;
    }


    public int getTestCountForRun(Run run) {
        int testCount = 0;
//        Jenkins jenkinsInstance = Jenkins.getInstance();
//        if (jenkinsInstance != null) {
//            Job job = (Job) jenkinsInstance.getItem(jobName);
//            if (job != null && job instanceof AbstractProject) {
//                AbstractProject abstractProject = (AbstractProject) job;
//                AbstractTestResultAction abstractTestResultAction = abstractProject.getLastCompletedBuild().getAction(AbstractTestResultAction.class);
//                if (abstractTestResultAction != null) {
//                    return abstractTestResultAction.getTotalCount();
//                }
//            }
//        }
        return testCount;
    }
}
