package io.jenkins.plugins.sample;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeHandler {
    public static String convertLongTimeToDate(long time){
        Date date = new Date(time);
        Format format = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return format.format(date);
    }
    /*
    * date format = yyyy MM dd HH:mm:ss
    *
    * */
    public static long convertDateToLongTime(String date) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        return formatter.parse(date).getTime();
    }


    public static int getDayOfMonth(String aDate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy MM dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(formatter.parse(aDate));
        return cal.get(Calendar.DAY_OF_MONTH);
    }
}
