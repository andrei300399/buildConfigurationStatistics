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


