package io.jenkins.plugins.sample;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateTimeHandler {
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

    public static String dateToString(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public static HashMap<String, Double> createDateMonthMap() {
        ZonedDateTime dateTime = ZonedDateTime.now().minusMonths(1);
        HashMap<String, Double> dayDuration = new HashMap<String, Double>();
        int lenMonth = getCurrentMonthDays();
        for (int i = 0; i < lenMonth; i++) {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String strDate = dateFormat.format(Date.from(dateTime.plusDays(i).toInstant()).getTime());
            dayDuration.put(strDate, 0.0);
        }

        return dayDuration;
    }
}
