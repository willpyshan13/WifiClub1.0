/**
 * Project Name: itee
 * File Name:	 DateUtils.java
 * Package Name: cn.situne.itee.common.utils
 * Date:		 2015-07-06
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.common.utils;

import android.content.Context;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import cn.situne.itee.common.constant.Constants;

/**
 * ClassName:DateUtils <br/>
 * Function: Format date. <br/>
 * Date: 2015-07-06 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class DateUtils {

    public static final String DATE_FORMAT_API_YYYY_MM_DD = "yyyy/MM/dd";
    public static final String DATE_FORMAT_API_YYYY_MM_DD_HH_MM_SS = "yyyy/MM/dd HH:mm:ss";

    public static final String DATE_FORMAT_SHOW_YYYY_MM_DD = "yyyy/MM/dd";
    public static final String DATE_FORMAT_SHOW_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String DATE_FORMAT_SHOW_MM_DD_YYYY = "MM/dd/yyyy";

    public static final String DATE_FORMAT_SHOW_MM_YYYY = "MM/yyyy";
    public static final String DATE_FORMAT_SHOW_YYYY_MM = "yyyy/MM";

    public static final String TIME_FORMAT_HH_MM_SS = "HH:mm:ss";
    public static final String TIME_FORMAT_HH_MM = "HH:mm";

    public static String getTimeHourMinuteSecond(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.TIME_FORMAT_HH_MM_SS, Locale.getDefault());
        return format.format(date);
    }

    public static String getAPIYearMonthDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_FORMAT_API_YYYY_MM_DD, Locale.getDefault());
        return format.format(date);
    }

    public static String getAPIYearMonthDayFromCurrentShow(String currentShow, Context mContext) {
        Date date = getDateFromCurrentShowYearMonthDay(currentShow, mContext);
        return getAPIYearMonthDay(date);
    }

    public static Date getDateFromAPIYearMonthDay(String apiYearMonthDay) {
        if (StringUtils.isEmpty(apiYearMonthDay)) {
            return new Date();
        } else {
            return getDateFromAPIFormat(apiYearMonthDay, DateUtils.DATE_FORMAT_API_YYYY_MM_DD);
        }
    }

    public static Date getDateFromAPIYearMonthDayHourMonthSecond(String apiYearMonthDayHourMonthSecond) {
        if (StringUtils.isEmpty(apiYearMonthDayHourMonthSecond)) {
            return new Date();
        } else {
            return getDateFromAPIFormat(apiYearMonthDayHourMonthSecond, DateUtils.DATE_FORMAT_API_YYYY_MM_DD_HH_MM_SS);
        }
    }

    private static Date getDateFromAPIFormat(String apiDateString, String apiFormat) {
        Date res = new Date();
        SimpleDateFormat format = new SimpleDateFormat(apiFormat, Locale.getDefault());
        try {
            res = format.parse(apiDateString);
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return res;
    }

    public static Date getDateFromCurrentShowYearMonthDay(String currentShow, Context mContext) {
        Date res = new Date();
        SimpleDateFormat format = new SimpleDateFormat(DateUtils.getShowYearMonthDayFormat(mContext), Locale.getDefault());
        try {
            res = format.parse(currentShow);
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return res;
    }

    public static String getCurrentShowYearMonthDayFromDate(Date date, Context mContext) {
        String res = StringUtils.EMPTY;
        if (date != null) {
            SimpleDateFormat currentShowDateFormat = new SimpleDateFormat(getShowYearMonthDayFormat(mContext), Locale.getDefault());
            res = currentShowDateFormat.format(date);
        }
        return res;
    }

    public static String getCurrentShowYearMonthDayFromApiDateStr(String apiFormatDate, Context mContext) {
        String res = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(apiFormatDate)) {
            SimpleDateFormat apiDateFormat = new SimpleDateFormat(DATE_FORMAT_API_YYYY_MM_DD, Locale.getDefault());
            SimpleDateFormat currentShowDateFormat = new SimpleDateFormat(getShowYearMonthDayFormat(mContext), Locale.getDefault());
            try {
                Date date = apiDateFormat.parse(apiFormatDate);
                res = currentShowDateFormat.format(date);
            } catch (ParseException e) {
                Utils.log(e.getMessage());
            }
        }
        return res;
    }

    public static String getCurrentShowYearMonthDayHourMinuteSecondFromApiDateTime(String apiFormatDateTime, Context mContext) {
        String res = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(apiFormatDateTime)) {
            SimpleDateFormat apiDateFormat = new SimpleDateFormat(DATE_FORMAT_API_YYYY_MM_DD_HH_MM_SS, Locale.getDefault());
            SimpleDateFormat currentShowDateFormat
                    = new SimpleDateFormat(getShowYearMonthDayHourMinuteSecondFormat(mContext), Locale.getDefault());
            try {
                Date date = apiDateFormat.parse(apiFormatDateTime);
                res = currentShowDateFormat.format(date);
            } catch (ParseException e) {
                Utils.log(e.getMessage());
            }
        }
        return res;
    }

    public static String getShowYearMonthFormat(Context mContext) {
        String currentDateFormat = AppUtils.getCurrentDateFormat(mContext);
        String format;
        if (DATE_FORMAT_SHOW_YYYY_MM_DD.equals(currentDateFormat)) {
            format = DATE_FORMAT_SHOW_MM_YYYY;
        } else {
            format = DATE_FORMAT_SHOW_YYYY_MM;
        }
        return format;
    }

    public static String getShowYearMonthDayHourMinuteSecondFormat(Context mContext) {
        return AppUtils.getCurrentDateFormat(mContext) + Constants.STR_SPACE + TIME_FORMAT_HH_MM_SS;
    }

    public static String getShowYearMonthDayHourMinuteFormat(Context mContext) {
        return AppUtils.getCurrentDateFormat(mContext) + Constants.STR_SPACE + TIME_FORMAT_HH_MM;
    }

    public static String getShowYearMonthDayFormat(Context mContext) {
        return AppUtils.getCurrentDateFormat(mContext);
    }

    /**
     * 获取当前日期是星期几
     *
     * @param dateString
     * @return 当前日期是星期几
     */
    public static String getWeekOfDate(String dateString) {

        String[] weekDays = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(DateUtils.getDateFromAPIYearMonthDay(dateString));

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public static Date getDateFromFormat(String dateString, String format) {
        Date res = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        try {
            res = simpleDateFormat.parse(dateString);
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return res;
    }

    public static String getStringFromFormat(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    /**
     * 日期比较
     */
    public static class DateCompare implements Comparator<Date> {
        public int compare(Date obj1, Date obj2) {
            if (obj1.after(obj2)) {
                return 1;
            } else {
                return -1;
            }
        }
    }

}  