package com.example.smartrestaurant.util;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dell 5521 on 12/11/2017.
 */

public class DateTimeUtil {
    public static boolean isToday(long dateInMillis){
        return DateUtils.isToday(dateInMillis);
    }
    public static boolean isYesterday(long dateInMillis){
        Calendar now = Calendar.getInstance();
        Calendar cdate = Calendar.getInstance();
        cdate.setTimeInMillis(dateInMillis);

        now.add(Calendar.DATE,-1);

        return now.get(Calendar.YEAR) == cdate.get(Calendar.YEAR)
                && now.get(Calendar.MONTH) == cdate.get(Calendar.MONTH)
                && now.get(Calendar.DATE) == cdate.get(Calendar.DATE);
    }

    public static String getDateWithDayName(Date date){
        SimpleDateFormat sd = new SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault());
        return sd.format(date);
    }

    public static String getDateWithDayName(long timeInMillis){
        Date date = new Date(timeInMillis);
        SimpleDateFormat sd = new SimpleDateFormat("EEEE, MMM d, yyyy", Locale.getDefault());
        return sd.format(date);
    }

    public static Date getStandardDateTimeFormat(String timeStr){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
    public static String getTimeInAmPm(String serverFormattedDateTime){
        DateFormat readFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        DateFormat outFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Calendar calendar = new GregorianCalendar();
        try {
            Date date = readFormat.parse(serverFormattedDateTime);
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, DateTimeUtil.getTimeZoneHour());
            calendar.add(Calendar.MINUTE, DateTimeUtil.getTimeZoneMinutes());
        } catch (Exception e){e.printStackTrace();}
        return outFormat.format(calendar.getTime());
    }
    public static String getFormattedDateTime(String timeStr, String inputFormat, String outputFormat) {

        String formattedDate = timeStr;

        DateFormat readFormat = new SimpleDateFormat(inputFormat, Locale.getDefault());
        DateFormat writeFormat = new SimpleDateFormat(outputFormat, Locale.getDefault());

        Date date = null;

        try {
            date = readFormat.parse(timeStr);
        } catch (ParseException e) {
        }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        return formattedDate;
    }
    public static int getTimeZoneHour() {
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = calendar.getTimeZone();
        int offset = timeZone.getRawOffset();
        return (int) TimeUnit.MILLISECONDS.toHours(offset);
    }

    public static int getTimeZoneMinutes() {
        Calendar calendar = new GregorianCalendar();
        TimeZone timeZone = calendar.getTimeZone();
        int offset = timeZone.getRawOffset();
        long hours = TimeUnit.MILLISECONDS.toHours(offset);
        return (int) TimeUnit.MILLISECONDS.toMinutes(offset - TimeUnit.HOURS.toMillis(hours));
    }

}
