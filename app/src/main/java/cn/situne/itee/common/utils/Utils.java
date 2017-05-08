/**
 * Project Name: itee
 * File Name:  Utils.java
 * Package Name: cn.situne.itee.common.utils
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.entity.TimeArea;

/**
 * ClassName:Utils <br/>
 * Function: utils of this app. <br/>
 * Date: 2015-01-15 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
@SuppressWarnings("UnusedDeclaration")
public class Utils {

    private static final boolean DEBUG = true;
    private static final String TAG = "iTee";

    public static void log(String msg) {
        if (DEBUG) {
            Log.e(TAG, msg);
        }
    }


    public static String getVersionName(Context mContext) {
        String versionName = Constants.STR_EMPTY;
        try {
            String pkName = mContext.getPackageName();
            versionName = mContext.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
        } catch (Exception e) {
            Utils.log(e.getMessage());
        }
        return versionName;
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT_YYYYMMDD_HHMMSSSSS, Locale.getDefault());
        return simpleDateFormat.format(new Date());
    }

    public static void pushActivity(Activity mActivity, Class<? extends Activity> clazz, boolean isWithAnimation) {
        Intent intent = new Intent();
        intent.setClass(mActivity, clazz);
        mActivity.startActivity(intent);
        if (isWithAnimation) {
            mActivity.overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
        }
    }


    public static void pushActivity(Activity mActivity, Class<? extends Activity> clazz, boolean isWithAnimation,Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(mActivity, clazz);
        intent.putExtras(bundle);
        mActivity.startActivity(intent);
        if (isWithAnimation) {
            mActivity.overridePendingTransition(R.anim.push_in_right, R.anim.push_out_left);
        }
    }

    public static void popActivity(Activity mActivity, Class<? extends Activity> clazz, boolean isWithAnimation) {
        Intent intent = new Intent();
        intent.setClass(mActivity, clazz);
        mActivity.startActivity(intent);
        if (isWithAnimation) {
            mActivity.overridePendingTransition(R.anim.push_in_left, R.anim.push_out_right);
        }
    }

    public static double getDoubleFromString(String str) {
        double res = 0;
        try {
            res = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            Utils.log(e.getMessage());
        }
        return res;
    }


    public static String getStringFromJsonObjectWithKey(JSONObject jo, String key) {
        String res = null;
        if (jo != null) {
            boolean isNUll = jo.isNull(key);
            if (!isNUll) {
                try {
                    res = jo.getString(key);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                    res = Constants.STR_EMPTY;
                }
            }
        }
        return res == null ? Constants.STR_EMPTY : res;
    }

    public static Integer getIntegerFromJsonObjectWithKey(JSONObject jo, String key) {
        Integer res = null;
        if (jo != null) {
            boolean isNUll = jo.isNull(key);
            if (!isNUll) {
                try {
                    res = jo.getInt(key);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
            }
        }
        return res;
    }

    public static Boolean getBooleanFromJsonObjectWithKey(JSONObject jo, String key) {
        Boolean res = null;
        if (jo != null) {
            boolean isNUll = jo.isNull(key);
            if (!isNUll) {
                try {
                    res = jo.getBoolean(key);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
            }
        }
        return res;
    }

    public static Double getDoubleFromJsonObjectWithKey(JSONObject jo, String key) {
        Double res = null;
        if (jo != null) {
            boolean isNUll = jo.isNull(key);
            if (!isNUll) {
                try {
                    res = jo.getDouble(key);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
            }
        }
        return res;
    }

    public static JSONArray getArrayFromJsonObjectWithKey(JSONObject jo, String key) {
        JSONArray res = new JSONArray();
        if (jo != null) {
            boolean isNUll = jo.isNull(key);
            if (!isNUll) {
                try {
                    return jo.getJSONArray(key);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
            }
        }
        return res;
    }

    public static void save2SP(Context mContext, String key, Object object) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.KEY_SP_IDENTIFIER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String base64String = getStringFromObject(object);
        editor.putString(key, base64String);
        editor.apply();
    }


    public static void removeFromSP(Context mContext, String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.KEY_SP_IDENTIFIER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static Object readFromSP(Context mContext, String key) {
        Object res = null;
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.KEY_SP_IDENTIFIER, Context.MODE_PRIVATE);
        String productBase64 = sharedPreferences.getString(key, null);
        if (productBase64 != null) {
            res = getObjectFromString(productBase64);
        }
        return res;
    }

    public static String getStringFromObject(Object object) {
        String oAuthBase64 = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符串
            oAuthBase64 = new String(Base64.encodeBase64(byteArrayOutputStream
                    .toByteArray()));
        } catch (Exception e) {
            Utils.log(e.getMessage());
        }
        return oAuthBase64;
    }

    public static Object getObjectFromString(String base64String) {
        Object res = null;
        if (base64String != null) {
            // 读取字节
            byte[] base64 = Base64.decodeBase64(base64String.getBytes());
            // 封装到字节流
            ByteArrayInputStream bas = new ByteArrayInputStream(base64);
            try {
                // 再次封装
                ObjectInputStream bis = new ObjectInputStream(bas);

                // 读取对象
                res = bis.readObject();

            } catch (Exception e) {
                Utils.log(e.getMessage());
            }
        }
        return res;
    }

    public static void debug(String msg) {
        Log.d(TAG, msg);
    }

    private static long timeStart;

    public static void calcMethodTimeStart() {
        timeStart = System.currentTimeMillis();
    }

    public static void calcMethodTimeEnd() {
        Log.d(TAG, "method cost : " + String.valueOf(System.currentTimeMillis() - timeStart));
    }

    public static void hideKeyboard(Activity mActivity) {
        View view = mActivity.getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputManger = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(EditText et, Activity mActivity) {
        et.setFocusable(true);
        et.requestFocus();
        InputMethodManager inputManger = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManger.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    public static boolean isTimeInArea(String currentTime, TimeArea timeArea) {
        boolean res = false;
        if (timeArea != null) {
            res = currentTime.compareTo(timeArea.getStartTime()) >= 0 && currentTime.compareTo(timeArea.getEndTime()) <= 0;
        }
        return res;
    }

    public static boolean isTransferBlockTimeInArea(String currentTime, TimeArea timeArea) {
        boolean res = false;
        if (timeArea != null) {
            res = currentTime.compareTo(timeArea.getStartTime()) > 0 && currentTime.compareTo(timeArea.getEndTime()) <= 0;
        }
        return res;
    }

    public static boolean isTimeInAreaList(String currentTime, ArrayList<TimeArea> timeAreaList) {
        boolean res = false;
        if (timeAreaList != null) {
            for (TimeArea timeArea : timeAreaList) {
                res = isTimeInArea(currentTime, timeArea);
                if (res) {
                    break;
                }
            }
        }
        return res;
    }

    public static int getHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static void showShortToast(Context mContext, String message) {
        if (mContext != null) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }

    public static void showShortToast(Context mContext, int messageId) {
        if (mContext != null) {
            Toast.makeText(mContext, mContext.getText(messageId), Toast.LENGTH_SHORT).show();
        }
    }

    public static void showLongToast(Context mContext, String message) {
        if (mContext != null) {
            Toast.makeText(mContext, message, Toast.LENGTH_LONG).show();
        }
    }

    public static void showLongToast(Context mContext, int messageId) {
        if (mContext != null) {
            Toast.makeText(mContext, mContext.getText(messageId), Toast.LENGTH_LONG).show();
        }
    }

    public static boolean isStringNullOrEmpty(String value) {
        return value == null || Constants.STR_EMPTY.equals(value);
    }

    public static boolean isStringNotNullOrEmpty(String value) {
        return !isStringNullOrEmpty(value);
    }

    public static String getHHMMSSTimeStringWithHourMinute(int hour, int minute) {
        return String.format("%02d:%02d:00", hour, minute);
    }

    public static String getHHMMTimeStringWithHourMinute(int hour, int minute) {
        return String.format("%02d:%02d", hour, minute);
    }

    public static String getHHMMTimeStringWithHour(int hour) {
        return String.format("%02d:00", hour);
    }

    public static long getMillionSecondsFromHHMMSS(String hhmmssString) {
        long res = 0;

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT_HHMMSS, Locale.getDefault());
        try {
            c.setTime(sdf.parse(hhmmssString));
            res = c.getTimeInMillis();
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return res;
    }

    public static String getHHMMSSFromMillionSecondsWithType(long millionSeconds, String type) {
        Date date = new Date(millionSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat(type, Locale.getDefault());
        return sdf.format(date);
    }

    public static String get2DigitDecimalString(String value) {
        String res = StringUtils.EMPTY;
        if (StringUtils.isNotEmpty(value)) {
            DecimalFormat format = new DecimalFormat("0.00");
            res = format.format(new BigDecimal(value));
            if (res.endsWith("0")) res = res.substring(0, res.length() - 1);
            if (res.endsWith(".0")) {
                res = res.replace(".0", StringUtils.EMPTY);
            }
        }
        return res;
    }

    public static String get2DigitDecimalString(double doubleValue) {
        DecimalFormat format = new DecimalFormat("0.00");
        String res = format.format(new BigDecimal(doubleValue));
        if (res.endsWith("0")) {
            res = res.substring(0, res.length() - 1);
        }
        if (res.endsWith(".0")) {
            res = res.replace(".0", StringUtils.EMPTY);
        }
        return res;
    }

    public static String get2DigitDecimalStringWithAbandon(double value) {
        String res = String.valueOf(value);
        if (StringUtils.isNotEmpty(res)) {
            if (res.contains(Constants.STR_DOT)) {
                int total = res.length();
                int dotIndex = res.indexOf(Constants.STR_DOT);
                if (total > dotIndex + 3) {
                    res = res.substring(0, dotIndex + 3);
                } else if (total == dotIndex + 2) {
                    res += "0";
                } else if (total == dotIndex + 1) {
                    res += "00";
                }
            } else {
                res += ".00";
            }
        }
        return res;
    }

    // the mobile is match or not
    public static boolean isMobile(String mobiles) {
        boolean res = false;
        if (StringUtils.isNotEmpty(mobiles)) {
            if (mobiles.contains(Constants.STR_SEPARATOR)) {
                if (mobiles.indexOf(Constants.STR_SEPARATOR) == mobiles.lastIndexOf(Constants.STR_SEPARATOR)) {
                    mobiles = mobiles.replace(Constants.STR_SEPARATOR, Constants.STR_EMPTY);
                } else {
                    return false;
                }
            }
            Pattern p = Pattern
                    .compile(Constants.CORRECT_FORMAT_MOBILE);
            Matcher m = p.matcher(mobiles);
            res = m.matches();
        }

        return res;
    }

    // the email is match or not
    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile(Constants.CORRECT_FORMAT_EMAIL);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isCaddieLevel(String caddieLevel) {
        Pattern p = Pattern.compile(Constants.CORRECT_FORMAT_CADDIE_LEVEL);
        Matcher m = p.matcher(caddieLevel);
        return m.matches();
    }

    public static ArrayList<String> getTimes(String startTime, String endTime, int gap) {
        ArrayList<String> res = new ArrayList<>();

        Calendar startCalender = Calendar.getInstance();
        Calendar endCalender = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT_HHMMSS, Locale.getDefault());
        try {
            startCalender.setTime(sdf.parse(startTime));
            endCalender.setTime(sdf.parse(endTime));
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }

        while (startCalender.getTimeInMillis() <= endCalender.getTimeInMillis()) {
            res.add(sdf.format(startCalender.getTime()));
            startCalender.set(Calendar.MINUTE, startCalender.get(Calendar.MINUTE) + gap);
        }

        return res;
    }

    public static boolean isSecondDateLaterThanFirst(String firstDateString, String secondDateString, String format) {
        boolean res = false;
        if (firstDateString.equals(secondDateString)) {
            return true;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Calendar first = Calendar.getInstance();
            Calendar second = Calendar.getInstance();
            Date firstDate = sdf.parse(firstDateString);
            Date secondDate = sdf.parse(secondDateString);
            first.setTime(firstDate);
            second.setTime(secondDate);
            res = first.before(second);
        } catch (ParseException e) {
            Utils.log(e.getMessage() + "......");
        }
        return res;
    }

    public static boolean isSecondDateLaterEqualFirst(String firstDateString, String secondDateString, String format) {
        boolean res = false;
        if (firstDateString.equals(secondDateString)) {
            return true;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        try {
            Calendar first = Calendar.getInstance();
            Calendar second = Calendar.getInstance();
            Date firstDate = sdf.parse(firstDateString);
            Date secondDate = sdf.parse(secondDateString);
            first.setTime(firstDate);
            second.setTime(secondDate);
            res = second.after(first);
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return res;
    }

    public static boolean isListNotNullOrEmpty(List list) {
        return list != null && list.size() > 0;
    }

    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
}
