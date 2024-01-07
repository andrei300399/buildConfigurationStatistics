package io.jenkins.plugins.sample;

import hudson.model.Run;
import hudson.util.RunList;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BuildArtifactSizeLogic extends BuildLogic {

    static Logger LOGGER = Logger.getLogger(BuildDurationLogic.class.getName());
    HashMap<String, Double> dateFormatArtifact;
    String dateFormatKey;
    public BuildArtifactSizeLogic(IntervalDate period, Boolean failed, RunList<Run> buildList) {
        super(period, failed,buildList);
    }

    public Map<String, Double> getArtifactSize(Boolean average) throws ParseException {
        filterPeriodBuild();
        filterFailedBuild();
        switch (this.period){
            case MONTH:
                dateFormatArtifact = DateTimeHandler.createDateMonthMap();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case WEEK:
                dateFormatArtifact = DateTimeHandler.createDateWeekMap();
                dateFormatKey = "yyyy-MM-dd";
                break;
            case YEAR:
                dateFormatArtifact = DateTimeHandler.createDateYearMap();
                dateFormatKey = "yyyy-MM";
                break;
        }


        HashMap<String, Integer> dayArtifactAverage = new HashMap<>();
        for (Run run : this.buildList) {
            String dateFormatKeyAfterCheckPeriod =
                    DateTimeHandler.dateToString(
                            DateTimeHandler.convertLongTimeToDate(
                                    run.getStartTimeInMillis()
                            ), dateFormatKey
                    );
            LOGGER.log(Level.INFO, "dateFormatKeyAfterCheckPeriod artifact: " + dateFormatKeyAfterCheckPeriod);
            LOGGER.log(Level.WARNING, "getArtifacts: " + run.getArtifacts());
            List<Run.Artifact> listArtifacts = run.getArtifacts();
            double artifactsRunSize = 0;

            for (Run.Artifact artifact : listArtifacts) {
                artifactsRunSize += artifact.getFileSize()/1024.0;
                LOGGER.log(Level.WARNING, "artifact.getFileSize(): " +artifact.getFileSize());
            }

            LOGGER.log(Level.WARNING, "artifactsRunSize: " + artifactsRunSize);

            if (dateFormatArtifact.get(dateFormatKeyAfterCheckPeriod) == 0.0) {
                dateFormatArtifact.put(dateFormatKeyAfterCheckPeriod, artifactsRunSize);
                dayArtifactAverage.put(dateFormatKeyAfterCheckPeriod, 1);
            } else {
                dateFormatArtifact.put(dateFormatKeyAfterCheckPeriod, dateFormatArtifact.get(dateFormatKeyAfterCheckPeriod) + artifactsRunSize);
                dayArtifactAverage.put(dateFormatKeyAfterCheckPeriod, dayArtifactAverage.get(dateFormatKeyAfterCheckPeriod) + 1);
            }
        }
        if (average) {
            for (Map.Entry<String, Integer> entry : dayArtifactAverage.entrySet()) {
                LOGGER.log(Level.INFO, "sum time duration: " + dateFormatArtifact.get(entry.getKey()));
                LOGGER.log(Level.INFO, "count runs: " + entry.getValue());
                dateFormatArtifact.put(entry.getKey(),
                        dateFormatArtifact.get(entry.getKey())/entry.getValue()
                );
            }
        }
        LOGGER.log(Level.INFO, "dateFormatArtifact: " + dateFormatArtifact);
        LOGGER.log(Level.INFO, "dayArtifactAverage: " + dayArtifactAverage);
        return dateFormatArtifact;
    }
}
