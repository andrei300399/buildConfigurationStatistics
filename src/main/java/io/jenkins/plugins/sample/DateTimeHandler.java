package io.jenkins.plugins.sample;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DateTimeHandler {

    static Logger LOGGER = Logger.getLogger(DateTimeHandler.class.getName());

    public static Date convertLongTimeToDate(long time) {
        Date date = new Date(time);
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

    /**
     * Create map format {23.12: 0.0, 24.12: 0.0 ...}
     * on 30-31 days
     *
     * **/
    public static HashMap<String, Double> createDateMonthMap() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(1);
        Logger LOGGER;
        LOGGER = Logger.getLogger(DateTimeHandler.class.getName());
        LOGGER.log(Level.INFO, "dateTime" + dateTime);
        HashMap<String, Double> dayDuration = new HashMap<String, Double>();
        int lenMonth = getLastMonthDays();
        LOGGER.log(Level.INFO, "lenMonth: " + lenMonth);
        for (int i = 1; i <= lenMonth; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusDays(i).toInstant()).getTime());
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            dayDuration.put(strDate, 0.0);
        }
        LOGGER.log(Level.INFO, "dayDuration: " + dayDuration.entrySet());
        return dayDuration;
    }

    /**
     * Create map format {1:00:00 0.0, 2:00:00: 0.0 ...}
     * on 24 hours
     *
     * **/
    public static HashMap<String, Double> createDateDayMap() throws ParseException {
        ZonedDateTime dateTime = ZonedDateTime.now().minusHours(24);
        Logger LOGGER;
        LOGGER = Logger.getLogger(DateTimeHandler.class.getName());
        LOGGER.log(Level.INFO, "dateTime days" + dateTime);
        HashMap<String, Double> hourDuration = new HashMap<String, Double>();
        int lenDay = 24;
        LOGGER.log(Level.INFO, "lenDay: " + lenDay);
        for (int i = 1; i <= lenDay; i++) {

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(
                    Date.from(dateTime.plusHours(i).toInstant()).getTime());
            strDate = DateTimeHandler.dateSetZeroMinutesSeconds(strDate);
            LOGGER.log(Level.INFO, "strdate i: " + i + " - " + strDate);
            hourDuration.put(strDate, 0.0);
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
        ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(4);
        LOGGER.log(Level.INFO, "dateTime QuarterMapSuccess" + dateTime);
        HashMap<String, HashMap<String,Integer>> successFailSuccess = new HashMap();
        int lenQuarterSuccess = 4;
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
        LOGGER.log(Level.INFO, "lenMonth: " + lenMonth);
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
     * Create map format {2/2022: 0.0, 3/2022: 0.0 ...}
     * on 12 month
     *
     * **/
    public static HashMap<String, Double> createDateYearMap() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusYears(1);
        LOGGER.log(Level.INFO, "last year dateTime" + dateTime);
        HashMap<String, Double> monthDuration = new HashMap<String, Double>();
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
            monthDuration.put(strDate, 0.0);
        }
        LOGGER.log(Level.INFO, "monthDuration: " + monthDuration.entrySet());
        return monthDuration;
    }

    /**
     * Create map format {2/2022: 0.0, 3/2022: 0.0 ...}
     * on 3 month
     *
     * **/
    public static HashMap<String, Double> createDateQuarterMap() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(4);
        LOGGER.log(Level.INFO, "last quarter dateTime" + dateTime);
        HashMap<String, Double> quarterDuration = new HashMap<String, Double>();
        int lengthQuarter = 4; // any year length in month
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
            quarterDuration.put(strDate, 0.0);
        }
        LOGGER.log(Level.INFO, "quarterDuration: " + quarterDuration.entrySet());
        return quarterDuration;
    }

    /**
     * Create map format {1/2/2022: 0.0, 3/2/2022: 0.0 ...}
     * on 1 week
     *
     * **/
    public static HashMap<String, Double> createDateWeekMap() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusWeeks(1);
        LOGGER.log(Level.INFO, "last week dateTime duration" + dateTime);
        HashMap<String, Double> weekDuration = new HashMap<String, Double>();
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
            weekDuration.put(strDate, 0.0);
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


