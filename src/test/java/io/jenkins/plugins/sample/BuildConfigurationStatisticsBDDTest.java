package io.jenkins.plugins.sample;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hudson.model.*;
import hudson.tasks.Shell;
import hudson.util.RunList;
import org.apache.commons.lang.time.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

@RunWith(MockitoJUnitRunner.Silent.class)
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


//    RunList<Run> buildList;

    private BuildDurationLogic buildDurationLogic;

    BuildConfigurationStatisticsAction buildConfigurationStatisticsAction;

    Date now;
    Date twoMonthAgo;

    Date fiveMonthAgo;
    Date twoWeekAgo;
    String formatNow;
    String formatTwoMonthAgo;
    String formatFiveMonthAgo;

    String formatNowQuarter;
    String formatTwoMonthAgoQuarter;
    String formatFiveMonthAgoQuarter;
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

        // quarter date
        formatNowQuarter = DateTimeHandler.dateToString(now, "yyyy-MM");
        formatTwoMonthAgoQuarter = DateTimeHandler.dateToString(twoMonthAgo, "yyyy-MM");
        formatFiveMonthAgoQuarter = DateTimeHandler.dateToString(fiveMonthAgo, "yyyy-MM");

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

        given(build2.getResult())
                .willReturn(Result.SUCCESS);

        given(build3.getResult())
                .willReturn(Result.SUCCESS);

        given(build4.getResult())
                .willReturn(Result.SUCCESS);


    }

    @Test
    public void testGetBuildsDuration() throws ParseException {

        // Given
        RunList<Run> buildList = RunList.fromRuns(Arrays.asList(build, build2));
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
        RunList<Run> buildList2 = RunList.fromRuns(Arrays.asList(build, build2, build3, build4));
        given(job.getBuilds())
                .willReturn(buildList2);
        buildConfigurationStatisticsAction = new BuildConfigurationStatisticsAction(job);

        // When
        String jsonData = buildConfigurationStatisticsAction.getBuildDuration("QUARTER", "0","AVG");

        Map<String, Object> jsonMap = new Gson().fromJson(
                jsonData, new TypeToken<HashMap<String, Object>>() {}.getType()
        );

        // Then
        then(jsonMap)
                .as("Check that json is correct")
                .doesNotContainKey(formatFiveMonthAgoQuarter)
                .as("Check that map is not contain date two month ago")
                .doesNotContainEntry(formatNowQuarter, 10.0)
                .as("Check that map is contain date now")
                .containsKey(formatTwoMonthAgoQuarter)
                .as("Check that map is contain date now value and initial values")
                .containsValues(15.0)
                .as("Check that map is contain date now entry with 10.0 time build duration")
                .containsEntry(formatTwoMonthAgoQuarter, 15.0)
                .as("Check that map has size how last month of days")
                .hasSize(3);

    }


}
