package io.jenkins.plugins.sample;

import hudson.model.*;
import hudson.tasks.Shell;
import hudson.util.RunList;
import org.apache.commons.lang.time.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@RunWith(MockitoJUnitRunner.class)
public class BuildConfigurationStatisticsBDDTest {

    @Mock
    Job job;

    @Mock
    FreeStyleBuild build;

    @Mock
    FreeStyleBuild build2;

    @Mock
    FreeStyleBuild build3;

    @Mock
    FreeStyleBuild build4;


    RunList<Run> buildList;

    private BuildDurationLogic buildDurationLogic;

    BuildConfigurationStatisticsAction buildConfigurationStatisticsAction;

    Date now;
    Date twoMonthAgo;

    Date fiveMonthAgo;
    Date twoWeekAgo;
    String formatNow;
    String formatTwoMonthAgo;
    String formatFiveMonthAgo;
    String formatTwoWeekAgo;


    @Before
    public void setup() throws ParseException {
        now = new Date();
        twoMonthAgo = DateUtils.addMonths(now, -2);
        fiveMonthAgo = DateUtils.addMonths(now, -5);
        //twoWeekAgo = DateUtils.addWeeks(now, -2);

        formatNow = DateTimeHandler.dateToString(now, "yyyy-MM-dd");
        formatTwoMonthAgo = DateTimeHandler.dateToString(twoMonthAgo, "yyyy-MM-dd");
        formatFiveMonthAgo = DateTimeHandler.dateToString(fiveMonthAgo, "yyyy-MM-dd");
        //formatTwoWeekAgo = DateTimeHandler.dateToString(fiveMonthAgo, "yyyy-MM-dd");

        given(build.getStartTimeInMillis())
                .willReturn(DateTimeHandler.convertDateToLongTime(now));
        given(build2.getStartTimeInMillis())
                .willReturn(DateTimeHandler.convertDateToLongTime(twoMonthAgo));
        given(build3.getStartTimeInMillis())
                .willReturn(DateTimeHandler.convertDateToLongTime(fiveMonthAgo));
        given(build4.getStartTimeInMillis())
                .willReturn(DateTimeHandler.convertDateToLongTime(twoMonthAgo));

        given(build.getDuration())
                .willReturn(10000L);
        given(build2.getDuration())
                .willReturn(20000L);
        given(build3.getDuration())
                .willReturn(10000L);
        given(build4.getDuration())
                .willReturn(10000L);

        given(build.getResult())
                .willReturn(Result.FAILURE);


    }

    @Test
    public void testGetBuildsDuration() throws ParseException {

        // Given
        buildList = RunList.fromRuns(Arrays.asList(build, build2));
        given(job.getBuilds())
                .willReturn(buildList);
        buildDurationLogic = new BuildDurationLogic(IntervalDate.MONTH, true,  job.getBuilds());

        // When
        Map<String, Double> map = buildDurationLogic.getBuildsDuration(Statistics.SUM);

        // Then
        then(map)
                .as("Check that map is not contain date two month ago entry with 20.0 time build duration")
                .doesNotContainEntry(formatTwoMonthAgo, 20.0)
                .as("Check that map is not contain date two month ago")
                .doesNotContainKey(formatTwoMonthAgo)
                .as("Check that map is contain date now")
                .containsKey(formatNow)
                .as("Check that map is contain date now value and initial values")
                .containsValues(10.0, 0.0)
                .as("Check that map is contain date now entry with 10.0 time build duration")
                .containsEntry(formatNow, 10.0)
                .as("Check that map has size how last month of days")
                .hasSize(DateTimeHandler.getLastMonthDays());

    }

    @Test
    public void testGetBuildsDurationJSON() throws ParseException {

        // Given
        buildList = RunList.fromRuns(Arrays.asList(build, build2, build3, build4));
        given(job.getBuilds())
                .willReturn(buildList);

        // When
        String jsonData = buildConfigurationStatisticsAction.getBuildDuration("QUARTER", "0","AVG");

        // Then
        then(jsonData)
                .as("Check that map is correct");
//                .doesNotContainKey(str)
//                .containsKey(str2)
//                .containsValues(10.0, 0.0);

    }


}
