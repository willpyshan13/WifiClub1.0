package cn.situne.itee.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.situne.itee.common.constant.Constants;

/**
 * Created by liaojian on 2015/2/5.
 * 判断选择的日期是否连续
 */
public class UtilsIsSerialDate {
    private static SimpleDateFormat dateFormatSimple = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD, Locale.getDefault());

    private static SimpleDateFormat dateFormatSimpleOblique
            = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD_OBLIQUE, Locale.getDefault());

    /**
     * 判断日期2与日期1相差的天数
     *
     * @param d1
     * @param d2
     * @return
     */
    public static long betweenDays(Date d1, Date d2) {
        long time1 = d1.getTime();
        long time2 = d2.getTime();
        long days = (time2 - time1) / (24 * 60 * 60 * 1000L);
        return Math.abs(days);
    }

    /**
     * 判断日期2与日期1是否连续
     *
     * @param d1
     * @param d2
     * @return
     */
    public static boolean isNextDay(Date d1, Date d2) {
        return betweenDays(d1, d2) == 1;
    }

    public static boolean isSerialDate(List<Date> dates) {
        for (int i = 0; i < dates.size() - 1; i++) {
            if (!isNextDay(dates.get(i), dates.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getCurrYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 在某一天基础上减掉月份
     *
     * @param month 年份
     * @return Date
     */
    public static String getCutMonthDate(String date, int month) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormatSimple.parse(date));
            calendar.add(Calendar.MONTH, month);
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return dateFormatSimple.format(calendar.getTime());
    }

    /**
     * 在某一天基础上减掉月份
     *
     * @param month 年份
     * @return Date
     */
    public static String getCutMonthDateOblique(String date, int month) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(dateFormatSimpleOblique.parse(date));
            calendar.add(Calendar.MONTH, month);
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return dateFormatSimpleOblique.format(calendar.getTime());
    }
}
