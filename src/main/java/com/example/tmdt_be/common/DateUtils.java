package com.example.tmdt_be.common;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
    public static final String FORMAT_DATE = "dd/MM/yyyy";
    public static final String FORMAT_DATE2 = "yyyy/MM/dd";
    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_DATE_TIME = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMAT_DATE_TIME2 = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_MONTH = "MM/yyyy";
    public static final String FORMAT_MONTH2 = "MMyyyy";

    public static Long stringHMSToMillis(String strHMS) {
        if (strHMS == null)
            return null;

        String[] arrStr = strHMS.split(":");
        if (arrStr.length == 3) {
            int h = Integer.parseInt(arrStr[0]);
            int m = Integer.parseInt(arrStr[1]);
            int s = Integer.parseInt(arrStr[2]);
            long mH = h * (60 * (60 * 1000));
            long mM = m * (60 * 1000);
            long mS = s * 1000;
            return (mH + mM + mS);
        } else
            return null;
    }

    public static String convertHourToDateAndHour(int hour) {

        int day = Math.round(hour / 24);
        int hour2 = hour % 24;

        String dateAndHour = "";

        if (day != 0) {
            dateAndHour = day + "d " + hour2 + "h";

        } else {
            dateAndHour = hour2 + "h";
        }

        return dateAndHour;
    }

    public static int convertDateAndHourToHour(int day, int hour) {
        int rs = 0;

        rs = day * 24 + hour;

        return rs;
    }

    public static String formatStringToHHmmss(int strHMS) {
        int h = strHMS / (3600);
        int m = (strHMS % (3600)) / 60;
        int s = (strHMS % (3600)) % 60;
        String strH = String.format("%02d", h);
        String strM = String.format("%02d", m);
        String strS = String.format("%02d", s);
        return strH + ":" + strM + ":" + strS;
    }

    public static Date timestampToDate(Timestamp stamp) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(stamp.getTime());
        return cal.getTime();
    }

    public static String dateToString(Date date, String strFormat) {
        if (date != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);
            return simpleDateFormat.format(date);
        } else
            return null;
    }

    public static Date stringToDate(String dateString, String strFormat) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(strFormat);
            return simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isDate(String strSource) {
        return isDate(strSource, DateFormat.getDateInstance());
    }

    public static boolean isDate(String strSource, String strFormat) {
        SimpleDateFormat fmt = new SimpleDateFormat(strFormat);
        fmt.setLenient(false);
        return isDate(strSource, fmt);
    }

    public static boolean isDate(String strSource, DateFormat fmt) {
        try {
            if (fmt.parse(strSource) == null)
                return false;
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static Date addSecond(Date dt, int iValue) {
        return add(dt, iValue, Calendar.SECOND);
    }

    public static Date addMinute(Date dt, int iValue) {
        return add(dt, iValue, Calendar.MINUTE);
    }

    public static Date addHour(Date dt, int iValue) {
        return add(dt, iValue, Calendar.HOUR);
    }

    public static Date addDay(Date dt, int iValue) {
        return add(dt, iValue, Calendar.DATE);
    }

    public static Date addMonth(Date dt, int iValue) {
        return add(dt, iValue, Calendar.MONTH);
    }

    public static Date addYear(Date dt, int iValue) {
        return add(dt, iValue, Calendar.YEAR);
    }

    public static Date add(Date dt, int iValue, int iType) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(dt);
        cld.add(iType, iValue);
        return cld.getTime();
    }

    public static int getHoursFromDate(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinuteFromDate(Date date) {
        Calendar cld = Calendar.getInstance();
        cld.setTime(date);
        return cld.get(Calendar.MINUTE);
    }

    public static Date stringToDate(String str) {
        if (DataUtil.isStringNullOrEmpty(str))
            return null;
        DateFormat df = new SimpleDateFormat(FORMAT_DATE);
        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date stringToDateTime(String str) {
        if (DataUtil.isStringNullOrEmpty(str))
            return null;
        DateFormat df = new SimpleDateFormat(FORMAT_DATE_TIME);
        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String dateToString(Date date) {
        if (date == null)
            return null;
        DateFormat df = new SimpleDateFormat(FORMAT_DATE);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return df.format(cal.getTime());
    }

    public static String dateTimeToString(Date date) {
        if (date == null)
            return null;
        DateFormat df = new SimpleDateFormat(FORMAT_DATE_TIME);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return df.format(cal.getTime());
    }

    public static Integer compare(Date d1, Date d2, String pattern) {
        if (d1 == null || d2 == null) {
            return null;
        }
        String str1 = "";
        String str2 = "";
        if (pattern.startsWith("yyyy-Q")) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(d1);
            int y1 = c1.get(1);
            int m1 = c1.get(2);
            int q1 = (m1 / 3) + 1;
            str1 = y1 + "-" + q1;
            Calendar c2 = Calendar.getInstance();
            c2.setTime(d2);
            int y2 = c2.get(1);
            int m2 = c2.get(2);
            int q2 = (m2 / 3) + 1;
            str2 = y2 + "-" + q2;
        } else {
            str1 = dateToString(d1, pattern);
            str2 = dateToString(d2, pattern);
        }
        return str1.compareTo(str2);
    }

    public static Date getFirstOfCurrentMonth() {
        Calendar date = Calendar.getInstance();
        date.set(Calendar.DAY_OF_MONTH, 1);
        return date.getTime();
    }

    public static Integer dateDiffDays(Date d1, Date d2) {
        return dateDiff(d1, d2, 1);
    }

    /**
     * @param d1
     * @param d2
     * @param type 1:days | 2: hours | 3: minutes | 4: seconds
     * @return
     */
    public static Integer dateDiff(Date d1, Date d2, int type) {
        if (d1 == null || d2 == null) {
            return null;
        }
        long diffMiliseconds = d1.getTime() - d2.getTime();
        switch (type) {
            case 1://days
                return (int) (diffMiliseconds / (24 * 60 * 60 * 1000));
            case 2://hours
                return (int) (diffMiliseconds / (60 * 60 * 1000));
            case 3://minutes
                return (int) (diffMiliseconds / (60 * 1000));
            case 4://seconds
                return (int) (diffMiliseconds / 1000);
            default:
                return (int) diffMiliseconds;
        }
    }

    public static boolean isWeekend(Date d) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        if (Calendar.SATURDAY == c.get(c.DAY_OF_WEEK) || Calendar.SUNDAY == c.get(c.DAY_OF_WEEK)) {
            return true;
        } else {
            return false;
        }
    }


    public static String diffDateToTime(Date startDate, Date endDate) {
        String ret = "";
        if (startDate == null || endDate == null) {
            return "00:00:00";
        } else {
            Integer hrs = dateDiff(startDate, endDate, 1) * 24;
            Integer mins = dateDiff(startDate, endDate, 3);
            Integer secs = dateDiff(startDate, endDate, 4);

            ret = ((hrs > 9 || hrs < 0) ? hrs.toString() : "0" + hrs.toString())
                    + ":" + ((mins > 9 || mins < 0) ? mins.toString() : "0" + mins.toString())
                    + ":" + ((secs > 9 || secs < 0) ? secs.toString() : "0" + secs.toString());
        }
        return ret;
    }

    public static LocalDate stringToLocalDate(String dateString, String strFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(strFormat);
        LocalDate localDate = LocalDate.parse(dateString, formatter);
        return localDate;
    }

}
