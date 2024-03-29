package io.jenkins.plugins.sample;

import hudson.model.Run;
import hudson.util.RunList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateTimeHandler {

    static Logger LOGGER = Logger.getLogger(DateTimeHandler.class.getName());

    public static Date convertLongTimeToDate(long time) {
        Date date = new Date(time);
        return date;
    }

    public static Date convertStringToDate(String dateString, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date date = formatter.parse(dateString);
        return date;
    }



    /*
     * date format = yyyy MM dd HH:mm:ss
     *
     * */
    public static long convertDateToLongTime(Date date) throws ParseException {
        return date.getTime();
    }

    public static int getDayOfMonth(Date aDate) throws ParseException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        return cal.get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentMonthDays() {
        Calendar c = Calendar.getInstance();
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public static int getLastMonthDays() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

//    public static int getLastMonths() {
//        Calendar c = Calendar.getInstance();
//        c.add(Calendar.MONTH, -1);
//        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
//    }

    public static String dateToString(Date date, String format) {
        DateFormat dateFormat = new SimpleDateFormat(format);
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static String dateMonthToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static String dateSetZeroMinutesSeconds(String dateString) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
        Date date = sdf.parse(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return dateToString(cal.getTime(), "yyyy-MM-dd HH:mm:ss");
    }

    public static String dateSetZeroDay(String dateString) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date date = sdf.parse(dateString);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return dateToString(cal.getTime(), "yyyy-MM-dd");
    }

    /**
     * Create map format {23.12: [], 24.12: [] ...}
     * on 30-31 days
     *
     * **/
    public static HashMap<String, List<Double>> createDateMonthMap() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(1);
        Logger LOGGER;
        LOGGER = Logger.getLogger(DateTimeHandler.class.getName());
        LOGGER.log(Level.INFO, "dateTime" + dateTime);
        HashMap<String, List<Double>> dayDuration = new HashMap<String, List<Double>>();
        int lenMonth = getLastMonthDays();
        LOGGER.log(Level.INFO, "lenMonth: " + lenMonth);
        for (int i = 1; i <= lenMonth; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusDays(i).toInstant()).getTime());
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            dayDuration.put(strDate, new ArrayList<Double>());
            //dayDuration.get(strDate).add(0.0);
        }
        LOGGER.log(Level.INFO, "dayDuration: " + dayDuration.entrySet());
        return dayDuration;
    }


    /**
     * Create map format {23.12.2023: 0.0, 24.12.2023: 0.0 ...}
     * on 8 equal periods
     *
     * **/
    public static HashMap<String, List<Double>> createDateAllMap(RunList<Run> runs) {
        //ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(1);
        Logger LOGGER;
        LOGGER = Logger.getLogger(DateTimeHandler.class.getName());
        LOGGER.log(Level.INFO, "run1" + runs.getFirstBuild());
        LOGGER.log(Level.INFO, "run2" + runs.getLastBuild());
        long timeStart = runs.getFirstBuild().getStartTimeInMillis();
        long timeEnd = runs.getLastBuild().getStartTimeInMillis();
        LocalDate startDate =
                LocalDate.ofInstant(Instant.ofEpochMilli(timeStart),
                        TimeZone.getDefault().toZoneId());
        LocalDate endDate =
                LocalDate.ofInstant(Instant.ofEpochMilli(timeEnd),
                        TimeZone.getDefault().toZoneId());
        Period period = Period.between(startDate, endDate);
        LOGGER.log(Level.INFO, "period between first and last build: " + period);
        HashMap<String, List<Double>> dayDuration = new HashMap<String, List<Double>>();
        int lenAll = 8;
        LOGGER.log(Level.INFO, "lenMonth: " + lenAll);
//        for (int i = 1; i <= lenAll; i++) {
//
//            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String strDate = dateFormat.format(
//                    Date.from(dateTime.plusDays(i).toInstant()).getTime());
//            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
//            dayDuration.put(strDate, 0.0);
//        }
        LOGGER.log(Level.INFO, "dayDuration: " + dayDuration.entrySet());
        return dayDuration;
    }

    /**
     * Create map format {1:00:00 0.0, 2:00:00: 0.0 ...}
     * on 24 hours
     *
     * **/
    public static HashMap<String, List<Double>> createDateDayMap() throws ParseException {
        ZonedDateTime dateTime = ZonedDateTime.now().minusHours(24);
        Logger LOGGER;
        LOGGER = Logger.getLogger(DateTimeHandler.class.getName());
        LOGGER.log(Level.INFO, "dateTime days" + dateTime);
        HashMap<String, List<Double>> hourDuration = new HashMap<String, List<Double>>();
        int lenDay = 24;
        LOGGER.log(Level.INFO, "lenDay: " + lenDay);
        for (int i = 1; i <= lenDay; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusHours(i).toInstant()).getTime());
            strDate = DateTimeHandler.dateSetZeroMinutesSeconds(strDate);
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            hourDuration.put(strDate, new ArrayList<Double>());
            //hourDuration.put(strDate, 0.0);
        }
        LOGGER.log(Level.INFO, "hourDuration: " + hourDuration.entrySet());
        return hourDuration;
    }

    /**
     * Create map format {1:00:00 {success: 0, fail : 0}, 2:00:00: {success: 0, fail : 0} ...}
     * on 24 hours
     *
     * **/
    public static HashMap<String, HashMap<String,Integer>> createDateDayMapSuccess() throws ParseException {
        ZonedDateTime dateTime = ZonedDateTime.now().minusHours(24);
        Logger LOGGER;
        LOGGER = Logger.getLogger(DateTimeHandler.class.getName());
        LOGGER.log(Level.INFO, "dateTime days" + dateTime);
        HashMap<String, HashMap<String,Integer>> successFailSuccess = new HashMap();
        int lenDay = 24;
        LOGGER.log(Level.INFO, "lenDay: " + lenDay);
        for (int i = 1; i <= lenDay; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusHours(i).toInstant()).getTime());
            strDate = DateTimeHandler.dateSetZeroMinutesSeconds(strDate);
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            successFailSuccess.put(strDate, new HashMap(){{
                put("fail", 0);
                put("success", 0);
            }});
        }
        LOGGER.log(Level.INFO, "hour successFailSuccess: " + successFailSuccess.entrySet());
        return successFailSuccess;
    }

    public static HashMap<String, HashMap<String,Integer>> createDateWeekMapSuccessRate() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusWeeks(1);
        LOGGER.log(Level.INFO, "dateTime - 1 week" + dateTime);
        HashMap<String, HashMap<String,Integer>> successFailSuccess = new HashMap();
        int lenWeek = 7;
        LOGGER.log(Level.INFO, "lenWeek" + lenWeek);
        for (int i = 1; i <= lenWeek; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //get dateTime previous week + i = 1...7 day and getTime, after in strDate=2022-03-22
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusDays(i).toInstant()).getTime());
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            successFailSuccess.put(strDate, new HashMap(){{
                put("fail", 0);
                put("success", 0);
            }});
        }
        LOGGER.log(Level.INFO, "successFailSuccess: " + successFailSuccess.entrySet());
        return successFailSuccess;
    }
    /**
     * Create map format {23.12: {success: 0, fail : 0}, 24.12: {success: 0, fail : 0} ...}
     * on 30-31 days
     *
     * **/

    public static HashMap<String, HashMap<String,Integer>> createDateMonthMapSuccessRate() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(1);
        LOGGER.log(Level.INFO, "dateTime" + dateTime);
        HashMap<String, HashMap<String,Integer>> successFailSuccess = new HashMap();
        int lenMonth = getLastMonthDays();
        LOGGER.log(Level.INFO, "lenMonth: " + lenMonth);
        for (int i = 1; i <= lenMonth; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //get dateTime previous month + i = 1...31 day and getTime, after in strDate=2022-03-22
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusDays(i).toInstant()).getTime());
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            successFailSuccess.put(strDate, new HashMap(){{
                put("fail", 0);
                put("success", 0);
            }});
        }
        LOGGER.log(Level.INFO, "successFailSuccess: " + successFailSuccess.entrySet());
        return successFailSuccess;
    }

    /**
     * Create map format {2/2022: {success: 0, fail : 0}, 3/2022: {success: 0, fail : 0} ...}
     * on 4 month
     *
     * **/

    public static HashMap<String, HashMap<String,Integer>> createDateQuarterMapSuccessRate() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(3);
        LOGGER.log(Level.INFO, "dateTime QuarterMapSuccess" + dateTime);
        HashMap<String, HashMap<String,Integer>> successFailSuccess = new HashMap();
        int lenQuarterSuccess = 3;
        LOGGER.log(Level.INFO, "lenQuarterSuccess QuarterMapSuccess: " + lenQuarterSuccess);
        for (int i = 1; i <= lenQuarterSuccess; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            //get dateTime previous quarter + i = 1...4 month and getTime, after in strDate=2022-03
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusMonths(i).toInstant()).getTime());
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            successFailSuccess.put(strDate, new HashMap(){{
                put("fail", 0);
                put("success", 0);
            }});
        }
        LOGGER.log(Level.INFO, "successFailSuccess: " + successFailSuccess.entrySet());
        return successFailSuccess;
    }

    /**
     * Create map format {23.12: 0, 24.12: 0 ...}
     * on 30-31 days
     *
     * **/

    public static HashMap<String, Integer> createDateMonthMapTestCount() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(1);
        LOGGER.log(Level.INFO, "dateTime test count" + dateTime);
        HashMap<String, Integer> testCount = new HashMap();
        int lenMonth = getLastMonthDays();
        LOGGER.log(Level.INFO, "lenMonth test count: " + lenMonth);
        for (int i = 1; i <= lenMonth; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            //get dateTime previous month + i = 1...31 day and getTime, after in strDate=2022-03-22
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusDays(i).toInstant()).getTime());
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            testCount.put(strDate, 0);
        }
        LOGGER.log(Level.INFO, "testCount: " + testCount.entrySet());
        return testCount;
    }

    /**
     * Create map format {12 10:00: 0, 12 11:00: 0 ...}
     * on 24 hours
     *
     * **/

    public static HashMap<String, Integer> createDateDayMapTestCount() throws ParseException {
        ZonedDateTime dateTime = ZonedDateTime.now().minusHours(24);
        Logger LOGGER;
        LOGGER = Logger.getLogger(DateTimeHandler.class.getName());
        LOGGER.log(Level.INFO, "dateTime days test count" + dateTime);
        HashMap<String, Integer> hourDuration = new HashMap<String, Integer>();
        int lenDay = 24;
        LOGGER.log(Level.INFO, "lenDay: " + lenDay);
        for (int i = 1; i <= lenDay; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusHours(i).toInstant()).getTime());
            strDate = DateTimeHandler.dateSetZeroMinutesSeconds(strDate);
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            hourDuration.put(strDate, 0);
        }
        LOGGER.log(Level.INFO, "hourDuration: " + hourDuration.entrySet());
        return hourDuration;
    }

    /**
     * Create map format {2/2022: 0, 3/2022: 0 ...}
     * on 12 month
     *
     * **/
    public static HashMap<String, Integer> createDateYearMapTestCount() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusYears(1);
        LOGGER.log(Level.INFO, "last year dateTime test" + dateTime);
        HashMap<String, Integer> monthDuration = new HashMap<String, Integer>();
        int lengthYear = 12; // any year length in month
        LOGGER.log(Level.INFO, "lengthYear test: " + lengthYear);
        for (int i = 1; i <= lengthYear; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

            //get dateTime previous year + i = 1...12 and getTime, after in strDate=2022-03
            String strDate = dateFormat.format(
                    Date.from(
                            dateTime.plusMonths(i).toInstant()
                    ).getTime()
            );

            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            monthDuration.put(strDate, 0);
        }
        LOGGER.log(Level.INFO, "monthDuration: " + monthDuration.entrySet());
        return monthDuration;
    }

    /**
     * Create map format {2/2022: 0, 3/2022: 0 ...}
     * on 3 month
     *
     * **/
    public static HashMap<String, Integer> createDateQuarterMapTestCount() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(3);
        LOGGER.log(Level.INFO, "last quarter dateTime test" + dateTime);
        HashMap<String, Integer> quarterDuration = new HashMap<String, Integer>();
        int lengthQuarter = 3; // any year length in month
        LOGGER.log(Level.INFO, "lengthQuarter test: " + lengthQuarter);
        for (int i = 1; i <= lengthQuarter; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

            //get dateTime previous year + i = 1...3 and getTime, after in strDate=2022-03
            String strDate = dateFormat.format(
                    Date.from(
                            dateTime.plusMonths(i).toInstant()
                    ).getTime()
            );

            LOGGER.log(Level.INFO, "strdate i test: " + i + " - " + strDate);
            quarterDuration.put(strDate, 0);
        }
        LOGGER.log(Level.INFO, "quarterDuration test: " + quarterDuration.entrySet());
        return quarterDuration;
    }

    /**
     * Create map format {1/2/2022: 0.0, 3/2/2022: 0.0 ...}
     * on 1 week
     *
     * **/
    public static HashMap<String, Integer> createDateWeekMapTestCount() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusWeeks(1);
        LOGGER.log(Level.INFO, "last week dateTime test" + dateTime);
        HashMap<String, Integer> weekDuration = new HashMap<String, Integer>();
        int lengthWeek = 7; // any week length in days
        LOGGER.log(Level.INFO, "lengthWeek test: " + lengthWeek);
        for (int i = 1; i <= lengthWeek; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            //get dateTime previous week + i = 1...7 and getTime, after in strDate=2022-03-01
            String strDate = dateFormat.format(
                    Date.from(
                            dateTime.plusDays(i).toInstant()
                    ).getTime()
            );

            LOGGER.log(Level.INFO, "strdate i test: " + i + " - " + strDate);
            weekDuration.put(strDate, 0);
        }
        LOGGER.log(Level.INFO, "weekDuration test: " + weekDuration.entrySet());
        return weekDuration;
    }


    /**
     * Create map format {2/2022: 0.0, 3/2022: 0.0 ...}
     * on 12 month
     *
     * **/
    public static HashMap<String, List<Double>> createDateYearMap() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusYears(1);
        LOGGER.log(Level.INFO, "last year dateTime" + dateTime);
        HashMap<String, List<Double>> monthDuration = new HashMap<String, List<Double>>();
        int lengthYear = 12; // any year length in month
        LOGGER.log(Level.INFO, "lengthYear: " + lengthYear);
        for (int i = 1; i <= lengthYear; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

            //get dateTime previous year + i = 1...12 and getTime, after in strDate=2022-03
            String strDate = dateFormat.format(
                    Date.from(
                            dateTime.plusMonths(i).toInstant()
                    ).getTime()
            );

            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            monthDuration.put(strDate, new ArrayList<Double>());
        }
        LOGGER.log(Level.INFO, "monthDuration: " + monthDuration.entrySet());
        return monthDuration;
    }

    /**
     * Create map format {2/2022: 0.0, 3/2022: 0.0 ...}
     * on 3 month
     *
     * **/
    public static HashMap<String, List<Double>> createDateQuarterMap() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(3);
        LOGGER.log(Level.INFO, "last quarter dateTime" + dateTime);
        HashMap<String, List<Double>> quarterDuration = new HashMap<String, List<Double>>();
        int lengthQuarter = 3; // any year length in month
        LOGGER.log(Level.INFO, "lengthQuarter: " + lengthQuarter);
        for (int i = 1; i <= lengthQuarter; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");

            //get dateTime previous year + i = 1...3 and getTime, after in strDate=2022-03
            String strDate = dateFormat.format(
                    Date.from(
                            dateTime.plusMonths(i).toInstant()
                    ).getTime()
            );

            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            quarterDuration.put(strDate, new ArrayList<Double>());
        }
        LOGGER.log(Level.INFO, "quarterDuration: " + quarterDuration.entrySet());
        return quarterDuration;
    }

    /**
     * Create map format {1/2/2022: 0.0, 3/2/2022: 0.0 ...}
     * on 1 week
     *
     * **/
    public static HashMap<String, List<Double>> createDateWeekMap() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusWeeks(1);
        LOGGER.log(Level.INFO, "last week dateTime duration" + dateTime);
        HashMap<String, List<Double>> weekDuration = new HashMap<String, List<Double>>();
        int lengthWeek = 7; // any week length in days
        LOGGER.log(Level.INFO, "lengthWeek: " + lengthWeek);
        for (int i = 1; i <= lengthWeek; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            //get dateTime previous week + i = 1...7 and getTime, after in strDate=2022-03-01
            String strDate = dateFormat.format(
                    Date.from(
                            dateTime.plusDays(i).toInstant()
                    ).getTime()
            );

            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            weekDuration.put(strDate, new ArrayList<Double>());
        }
        LOGGER.log(Level.INFO, "weekDuration: " + weekDuration.entrySet());
        return weekDuration;
    }

    public static HashMap<String, HashMap<String,Integer>> createDateYearMapSuccessRate() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusYears(1);
        LOGGER.log(Level.INFO, "last year dateTime success" + dateTime);
        HashMap<String, HashMap<String,Integer>> successFailSuccess = new HashMap();
        int lenMonth = 12;
        LOGGER.log(Level.INFO, "lenMonth: " + lenMonth);
        for (int i = 1; i <= lenMonth; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
            //get dateTime previous year + i = 1...12 and getTime, after in strDate=2022-03
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusMonths(i).toInstant()).getTime());
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            successFailSuccess.put(strDate, new HashMap(){{
                put("fail", 0);
                put("success", 0);
            }});
        }
        LOGGER.log(Level.INFO, "successFailSuccess: " + successFailSuccess.entrySet());
        return successFailSuccess;
    }
}


