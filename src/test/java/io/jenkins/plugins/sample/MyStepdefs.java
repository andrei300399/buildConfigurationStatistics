package io.jenkins.plugins.sample;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.ru.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import hudson.model.*;
import hudson.tasks.Shell;
import hudson.util.RunList;
import org.apache.commons.lang.time.DateUtils;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.jvnet.hudson.test.JenkinsRule;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.MockitoRule;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class MyStepdefs {


    //@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();

    Job job = mock(Job.class);

    FreeStyleBuild build = mock(FreeStyleBuild.class);
    FreeStyleBuild build2 = mock(FreeStyleBuild.class);
    FreeStyleBuild build3 = mock(FreeStyleBuild.class);
    FreeStyleBuild build4 = mock(FreeStyleBuild.class);


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

    Map<String, Double> map;
    Map<String, Object> jsonMap;


    @Before
    public void prepareData() throws ParseException {
        //подготовить данные
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

    @Дано("^выбраны параметры отображения за период \"([^\"]*)\" и с флагом отображения упавших сборок \"([^\"]*)\"$")
    public void получениеСборокЗаПериодИСФлагомОтображенияУпавшихСборок(IntervalDate period, Boolean failed) throws Throwable {

        RunList<Run> buildList = RunList.fromRuns(Arrays.asList(build, build2));
        given(job.getBuilds())
                .willReturn(buildList);
        buildDurationLogic = new BuildDurationLogic(period, failed, job.getBuilds());
    }

    @Когда("^выбран статистический показатель \"([^\"]*)\"$")
    public void выбранСтатистическийПоказатель(Statistics statistics) throws Throwable {
        map = buildDurationLogic.getBuildsDuration(statistics);
    }

    @Тогда("^отбираются успешные и упавшие сборки за месяц с вычислением суммарного времени$")
    public void отбираютсяУспешныеИУпавшиеСборкиЗаМесяцСВычислениемСуммарногоВремени() throws Throwable {
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

    @Дано("^сформировано (\\d+) запуска задания$")
    public void сформированыЗапускиЗадания(int countRuns) throws Throwable {

        if (countRuns == 4) {
            RunList<Run> buildList2 = RunList.fromRuns(Arrays.asList(build, build2, build3, build4));
            given(job.getBuilds())
                    .willReturn(buildList2);
            buildConfigurationStatisticsAction = new BuildConfigurationStatisticsAction(job);
        } else {
            throw new PendingException();
        }
    }

    @Когда("^выбраны параметры отображения за период \"([^\"]*)\" и с флагом отображения упавших сборок \"([^\"]*)\" и статистический показатель \"([^\"]*)\"$")
    public void получениеСборокЗаПериодИСФлагомОтображенияУпавшихСборокПоПоказателю(String period, String failed, String statistics) throws Throwable {
        String jsonData = buildConfigurationStatisticsAction.getBuildDuration(period, failed, statistics);

        jsonMap = new Gson().fromJson(
                jsonData, new TypeToken<HashMap<String, Object>>() {}.getType()
        );
    }

    @Тогда("^отбираются успешные сборки за последние (\\d+) месяца с вычислением среднего времени$")
    public void отбираютсяУспешныеСборкиЗаКварталСВычислениемСреднегоВремени(int countMonth) throws Throwable {
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
                .hasSize(countMonth);
    }

}