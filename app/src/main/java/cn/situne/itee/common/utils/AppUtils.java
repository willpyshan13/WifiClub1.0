/**
 * Project Name: itee
 * File Name:  AppUtils.java
 * Package Name: cn.situne.itee.common.utils
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.baoyz.actionsheet.ActionSheet;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import cn.situne.itee.R;
import cn.situne.itee.activity.BaseActivity;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.entity.ShoppingProduct;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.manager.IteeApplication;
import cn.situne.itee.manager.RequestImageManager;
import cn.situne.itee.manager.jsonentity.JsonLogin;
import cn.situne.itee.view.popwindow.SelectSaveOrNotPopupWindow;

import static cn.situne.itee.R.color.common_gray;

/**
 * ClassName:AppUtils <br/>
 * Function: utils of this app. <br/>
 * Date: 2015-01-15 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
@SuppressWarnings(value = {"UnusedDeclaration", "unchecked"})
public class AppUtils {

    public static int getColorFromRGBString(String rgbString) {
        return Color.parseColor("#" + rgbString);
    }


    private static HashMap<AuthControl, Boolean> authControlBooleanHashMap = new HashMap<>();
    private static String baseUrl = StringUtils.EMPTY;

    public static String getChannelId(Context mContext) {
        String res = StringUtils.EMPTY;
        try {
            PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            if (pi.versionName.startsWith("Beta")) {
                res = Constants.CHANNEL_ID_BETA;
            } else if (pi.versionName.startsWith("v")) {
                res = Constants.CHANNEL_ID_RELEASE;
            } else if (pi.versionName.startsWith("Develop")) {
                res = Constants.CHANNEL_ID_DEVELOP;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void saveShoppingCart(LinkedHashMap<String, ShoppingProduct> shoppingCartMap, Context mContext) {
        Utils.save2SP(mContext, Constants.KEY_SP_SHOPPING_CART, shoppingCartMap);
    }

    public static void removeShoppingCart(Context mContext) {
        Utils.removeFromSP(mContext, Constants.KEY_SP_SHOPPING_CART);
    }

    public static LinkedHashMap<String, ShoppingProduct> getShoppingCart(Context mContext) {
        Object readFromSP = Utils.readFromSP(mContext, Constants.KEY_SP_SHOPPING_CART);
        if (readFromSP != null) {
            return (LinkedHashMap<String, ShoppingProduct>) readFromSP;
        }
        return new LinkedHashMap<>();
    }

    public static boolean doCheckUserName(String userName) {
        boolean res = true;
        boolean isPhone = Utils.isMobile(userName);
        boolean isEmail = Utils.isEmail(userName);
        if (!isPhone && !isEmail) {
            res = false;
        }
        return res;
    }

    public static void openSMS(String message, Context mContext) {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:"));
        sendIntent.putExtra("sms_body", message);
        mContext.startActivity(sendIntent);
    }

    public static void sendEmail(String mailAddress, String subject, String cc, String content, Context mContext) {
        String[] receiver = new String[]{mailAddress};
        String myCc = cc;
        Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
        myIntent.setType("plain/text");
        myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, receiver);
        myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
        myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        myIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(content));
        mContext.startActivity(Intent.createChooser(myIntent, mContext.getString(R.string.create_chooser_send_mail)));
    }

    public static String getCurrentDateFormat(Context mContext) {
        String dateFormat = (String) Utils.readFromSP(mContext, Constants.KEY_DATE_FORMAT);
        if (DateUtils.DATE_FORMAT_SHOW_MM_DD_YYYY.equals(dateFormat)) {
            dateFormat = DateUtils.DATE_FORMAT_SHOW_MM_DD_YYYY;
        } else if (DateUtils.DATE_FORMAT_SHOW_DD_MM_YYYY.equals(dateFormat)) {
            dateFormat = DateUtils.DATE_FORMAT_SHOW_DD_MM_YYYY;
        } else {
            dateFormat = DateUtils.DATE_FORMAT_SHOW_YYYY_MM_DD;
        }
        return dateFormat;
    }

    public static void saveCurrentDateFormat(String dateFormat, Context mContext) {
        Utils.save2SP(mContext, Constants.KEY_DATE_FORMAT, dateFormat);
    }

    public static String getBaseUrl(Context mContext) {
        if (StringUtils.isEmpty(AppUtils.baseUrl)) {
            try {
                PackageInfo pi = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
                if (pi.versionName.startsWith("Beta")) {
                    baseUrl = Constants.BASE_API_URL_BETA;
                } else if (pi.versionName.startsWith("v")) {
                    baseUrl = Constants.BASE_API_URL_RELEASE;
                } else if (pi.versionName.startsWith("Develop")) {
                    baseUrl = Constants.BASE_API_URL_DEVELOP;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return baseUrl;
    }


    public static void setBaseUrl(String baseUrl) {
        AppUtils.baseUrl = baseUrl;
    }

    public static JsonLogin getLoginInfo(Context mContext) {
        return (JsonLogin) Utils.readFromSP(mContext, Constants.KEY_SP_LOGIN_INFO);
    }

    public static void initAuthMap(Context mContext) {
        int maxAuthNum = 37;
        for (int i = 1; i <= maxAuthNum; i++) {
            AuthControl authControl = AuthControl.valueOf(i);
            authControlBooleanHashMap.put(authControl, false);
        }
        JsonLogin jl = getLoginInfo(mContext);
        String setAuth = jl.getAuth();
        if (Utils.isStringNotNullOrEmpty(setAuth)) {
            String[] split = setAuth.split(Constants.STR_COMMA);
            for (String aSplit : split) {
                AuthControl authControl = AuthControl.valueOf(Integer.valueOf(aSplit));
                authControlBooleanHashMap.put(authControl, true);
            }
        }
    }

    public static boolean getAuth(AuthControl authControl, Context mContext) {
        boolean res = false;
        if (authControlBooleanHashMap.size() == 0) {
            initAuthMap(mContext);
        }
        Boolean auth = authControlBooleanHashMap.get(authControl);
        if (auth != null) {
            res = auth;
        }
        return res;
    }

    public static String getToken(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        if (jl != null) {
            return jl.getToken();
        } else {
            return null;
        }
    }


    public static String getRefreshToken(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        if (jl != null) {
            return jl.getRefreshToken() == null?"":jl.getRefreshToken();
        } else {
            return "";
        }
    }


    public static String getBookingType(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        if (jl != null) {
            return jl.getBookingType();
        } else {
            return null;
        }
    }

    public static String getTimeZone(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        if (jl != null) {
            return jl.getTimeZone();
        } else {
            return null;
        }
    }

    public static String getCurrentUserId(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        if (jl != null) {
            return String.valueOf(jl.getUserId());
        } else {
            return null;
        }
    }

    public static String getDefaultPaymentPattern(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        if (jl != null) {
            return String.valueOf(jl.getPaymentPattern());
        } else {
            return null;
        }
    }

    public static String getUnit(Context mContext) {
        String res = null;
        JsonLogin jl = getLoginInfo(mContext);
        if (jl != null) {
            res = jl.getUnit();
        }
        return res;
    }

    public static String getAdminFlag(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        return jl == null ? Constants.STR_0 : jl.getAdminFlag();
    }

    public static boolean isTaxExcludeGoods(Context mContext) {
        boolean res = false;
        JsonLogin jl = getLoginInfo(mContext);
        if (jl != null) {
            res = Constants.STR_1.equals(jl.getSalesTaxExcluding());
        }


        if (!AppUtils.getShowSalesTax(mContext)) {
            return false;
        }
        return res;
    }

    public static String getSalesTax(Context mContext) {
        String res = null;
        JsonLogin jl = getLoginInfo(mContext);
        if (jl != null) {
            res = jl.getSalesTax();
        }
        return res;
    }

    public static boolean isChina(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        return jl != null && Constants.COUNTRY_CHINA.equalsIgnoreCase(jl.getCountryName());
    }

    public static boolean isOneTeeOnly(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        return jl != null && Constants.STR_1.equalsIgnoreCase(jl.getOneTeeOnly());
    }

    public static int getAvailableTimePointInCurrentHour(int interval, String startTime, int currentHour) {
        int startMinuteInCurrentHour = getStartMinuteInCurrentHour(interval, startTime, currentHour);
        return (Constants.MINUTES_PER_HOUR - 1 - startMinuteInCurrentHour) / interval + 1;
    }

    public static int getUnavailableTimePointInStartHour(int interval, String startTime, int startHour) {
        int availableTimePointInCurrentHour = getAvailableTimePointInCurrentHour(interval, startTime, startHour);
        int unavailableTimePointInCurrentHour = (Constants.MINUTES_PER_HOUR - availableTimePointInCurrentHour * interval) / interval;
        return unavailableTimePointInCurrentHour > 0 ? unavailableTimePointInCurrentHour : 0;
    }

    public static int getStartMinuteInCurrentHour(int interval, String startTime, int currentHour) {
        int res = 0;
        int minutesOfStart;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT_HHMM, Locale.getDefault());
        try {
            Date d = sdf.parse(startTime);
            c.setTime(d);

            minutesOfStart = c.get(Calendar.MINUTE) + c.get(Calendar.HOUR_OF_DAY) * Constants.MINUTES_PER_HOUR;
            if (currentHour > c.get(Calendar.HOUR_OF_DAY)) {
                int minutesLeft = currentHour * Constants.MINUTES_PER_HOUR - minutesOfStart;
                if (minutesLeft > interval) {
                    minutesLeft = minutesLeft % interval;
                }
                res = minutesLeft == 0 ? 0 : interval - minutesLeft;
            } else if (currentHour == c.get(Calendar.HOUR_OF_DAY)) {
                res = c.get(Calendar.MINUTE);
            }

        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return res;
    }

    public static int getCurrentTimeWithColIdx(int interval, String startTime, int currentHour, int currentCol) {
        int minutesInCurrentHour = 0;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT_HHMM, Locale.getDefault());
        try {
            Date d = sdf.parse(startTime);
            c.setTime(d);
            int startMinutesInCurrentHour = getStartMinuteInCurrentHour(interval, startTime, currentHour);

            if (currentHour > c.get(Calendar.HOUR_OF_DAY)) {
                minutesInCurrentHour = currentCol * interval + startMinutesInCurrentHour;
            } else if (currentHour == c.get(Calendar.HOUR_OF_DAY)) {
                int colOfStartTime = startMinutesInCurrentHour / interval;
                if (currentCol < colOfStartTime) {
                    minutesInCurrentHour = startMinutesInCurrentHour - (colOfStartTime - currentCol) * interval;
                } else if (currentCol > colOfStartTime) {
                    minutesInCurrentHour = startMinutesInCurrentHour + (currentCol - colOfStartTime) * interval;
                } else {
                    minutesInCurrentHour = startMinutesInCurrentHour;
                }
            }
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }

        return minutesInCurrentHour;
    }

    public static String getToday() {
        return getYMD(new Date());
    }

    public static String getTodaySlash() {
        return getYMDWithSlash(new Date());
    }

    public static String getYMD(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD, Locale.getDefault());
        return sdf.format(date);
    }

    public static Date getYMD(String date) {
        Date returnDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD, Locale.getDefault());
        try {
            returnDate = sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    public static String getYMDWithSlash(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD_OBLIQUE, Locale.getDefault());
        return sdf.format(date);
    }

    public static String getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMM, Locale.getDefault());
        return sdf.format(new Date());
    }

    public static int getHourFromHHMMSS(String hmsTimeString) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT_HHMMSS, Locale.getDefault());
        try {
            Date d = sdf.parse(hmsTimeString);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            return c.get(Calendar.HOUR_OF_DAY);
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return -1;
    }

    public static void saveCurrencyId(Context mContext, String currencyId) {
        Utils.save2SP(mContext, Constants.KEY_CURRENCY, currencyId);
    }



    public static void saveRefreshToken(Context mContext, String refreshToken) {
        JsonLogin jl = getLoginInfo(mContext);
        jl.setRefreshToken(refreshToken);
        Utils.save2SP(mContext, Constants.KEY_SP_LOGIN_INFO, jl);
    }


    public static void saveToken(Context mContext, String token) {
        JsonLogin jl = getLoginInfo(mContext);
        jl.setToken(token);
        Utils.save2SP(mContext, Constants.KEY_SP_LOGIN_INFO, jl);
    }


    public static void saveSellCaddie(Context mContext, boolean isSell) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.KEY_SP_IDENTIFIER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("saveSellCaddie", isSell);
        editor.commit();


    }

    public static boolean getSellCaddie(Context mContext) {

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Constants.KEY_SP_IDENTIFIER, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("saveSellCaddie", false);


    }


    public static void saveCaddie(Context mContext, String isCaddie) {
        Utils.save2SP(mContext, Constants.KEY_IS_CADDIE, isCaddie);
    }

    public static boolean getIsCaddie(Context mContext) {
        JsonLogin loginInfo = AppUtils.getLoginInfo(mContext);
        if (Constants.STR_0.equals(loginInfo.getIsCaddie())) {
            return false;
        }
        return true;
    }

    public static void saveCurrencySymbol(Context mContext, String currencySymbol) {
        Utils.save2SP(mContext, Constants.KEY_CURRENCY_SYMBOL, currencySymbol);
    }

    public static void saveFirstDayOfWeek(Context mContext, String firstDayOfWeek) {
        Utils.save2SP(mContext, Constants.KEY_FIRST_DAY_OF_WEEK, firstDayOfWeek);
    }

    public static String getCurrentFirstDayOfWeek(Context mContext) {
        JsonLogin loginInfo = AppUtils.getLoginInfo(mContext);
        String res = loginInfo.getWeekFirstDay();
        if (res == null) {
            res = mContext.getResources().getString(R.string.calendar_sun);
        }
        return res;
    }

    public static String getCurrentCurrency(Context mContext) {
        JsonLogin loginInfo = AppUtils.getLoginInfo(mContext);
        String currencySymbol = loginInfo.getCurrencySymbol();
        return currencySymbol == null ? Constants.CURRENCY_EU : currencySymbol;
    }

    public static String getCurrentCurrencySymbolFromId(Context mContext, String id) {
        String res;
        if (Constants.CURRENCY_ID_CN.equals(id)) {
            res = Constants.CURRENCY_CN;
        } else if (Constants.CURRENCY_ID_US.equals(id)) {
            res = Constants.CURRENCY_US;
        } else if (Constants.CURRENCY_ID_FR.equals(id)) {
            res = Constants.CURRENCY_FR;
        } else {
            res = Constants.CURRENCY_EU;
        }
        return res;
    }

    public static String getCurrentCurrencyId(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        return jl.getCurrencyId();
    }

    public static String getCurrentCourseId(Context mContext) {
        JsonLogin jl = getLoginInfo(mContext);
        if (jl != null) {
            return jl.getCourseId();
        } else {
            return null;
        }
    }

    public static void showNetworkImage(NetworkImageView mImageView, String url) {
        ImageLoader imageLoader = RequestImageManager.getImageLoader();
        mImageView.setImageUrl(url, imageLoader);
    }

    public static void showDeleteAlert(BaseFragment fragment, final DeleteConfirmListener listener) {
//        fragment.getActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
//        ActionSheet.ActionSheetListener listenerActionSheet = new ActionSheet.ActionSheetListener() {
//            @Override
//            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
//            }
//
//            @Override
//            public void onDismissWithCancelButton(ActionSheet actionSheet) {
//
//            }
//
//            @Override
//            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//                listener.onClickDelete();
//            }
//        };
//        ActionSheet.createBuilder(fragment.getActivity(), fragment.getFragmentManager())
//                .setCancelButtonTitle(fragment.getString(R.string.common_cancel))
//                .setOtherButtonTitles(fragment.getString(R.string.common_delete))
//                .setCancelableOnTouchOutside(true).setListener(listenerActionSheet).show();


        SelectSaveOrNotPopupWindow savePopupWindow
                = new SelectSaveOrNotPopupWindow(fragment, new TeeTimeAddFragment.OnPopupWindowWheelClickListener() {
            @Override
            public void onSaveClick(String a, String b) {
                listener.onClickDelete();
            }
        }, 5, null, null,fragment.getString(R.string.common_delete),fragment.getString(R.string.common_cancel));
        savePopupWindow.showAtLocation(fragment.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);



    }

    public static void showSaveConfirm(BaseFragment fragment, final SaveConfirmListener listener) {
//        fragment.getActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
//        ActionSheet.ActionSheetListener listenerActionSheet = new ActionSheet.ActionSheetListener() {
//            @Override
//            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
//            }
//
//            @Override
//            public void onDismissWithCancelButton(ActionSheet actionSheet) {
//
//            }
//
//            @Override
//            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//                listener.onClickSave();
//            }
//        };
//        ActionSheet.createBuilder(fragment.getActivity(), fragment.getFragmentManager())
//                .setCancelButtonTitle(fragment.getString(R.string.common_cancel))
//                .setOtherButtonTitles(fragment.getString(R.string.common_confirm_save))
//                .setCancelableOnTouchOutside(true).setListener(listenerActionSheet).show();


        SelectSaveOrNotPopupWindow savePopupWindow
                = new SelectSaveOrNotPopupWindow(fragment, new TeeTimeAddFragment.OnPopupWindowWheelClickListener() {
            @Override
            public void onSaveClick(String a, String b) {
                listener.onClickSave();
            }
        }, 5, null, null,fragment.getString(R.string.common_confirm_save),fragment.getString(R.string.common_cancel));
        savePopupWindow.showAtLocation(fragment.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);


    }

    public static void showMessageOkCancel(BaseFragment fragment, String message, final DeleteConfirmListener listener) {
        fragment.getActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
        ActionSheet.ActionSheetListener listenerActionSheet = new ActionSheet.ActionSheetListener() {
            @Override
            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
            }

            @Override
            public void onDismissWithCancelButton(ActionSheet actionSheet) {

            }

            @Override
            public void onOtherButtonClick(ActionSheet actionSheet, int index) {
                listener.onClickDelete();
            }
        };
        ActionSheet.createBuilder(fragment.getActivity(), fragment.getFragmentManager())
                .setCancelButtonTitle(fragment.getString(R.string.common_cancel))
                .setOtherButtonTitles(new String[]{message, fragment.getString(R.string.common_ok)})
                .setCancelableOnTouchOutside(true).setListener(listenerActionSheet).show();





    }

    public static void showMustBeFilledIn(BaseFragment fragment, String fieldName) {
//        fragment.getActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
//        ActionSheet.createBuilder(fragment.getActivity(), fragment.getFragmentManager())
//                .setCancelButtonTitle(fragment.getString(R.string.common_ok))
//                .setOtherButtonTitles(MessageFormat.format(fragment.getString(R.string.common_must_be_filled_in), fieldName))
//                .setCancelableOnTouchOutside(true).setListener(null).show();


        SelectSaveOrNotPopupWindow savePopupWindow
                = new SelectSaveOrNotPopupWindow(fragment, new TeeTimeAddFragment.OnPopupWindowWheelClickListener() {
            @Override
            public void onSaveClick(String a, String b) {

            }
        }, 5, null, null,MessageFormat.format(fragment.getString(R.string.common_must_be_filled_in), fieldName),fragment.getString(R.string.common_ok));
        savePopupWindow.showAtLocation(fragment.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);



    }

    public static void showMessageWithOkButton(BaseFragment fragment, String fieldName) {
//        fragment.getActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
//        ActionSheet.createBuilder(fragment.getActivity(), fragment.getFragmentManager())
//                .setCancelButtonTitle(fragment.getString(R.string.common_ok))
//                .setOtherButtonTitles(MessageFormat.format(fieldName, StringUtils.EMPTY))
//                .setCancelableOnTouchOutside(true).setListener(null).show();
        SelectSaveOrNotPopupWindow savePopupWindow
                = new SelectSaveOrNotPopupWindow(fragment, new TeeTimeAddFragment.OnPopupWindowWheelClickListener() {
            @Override
            public void onSaveClick(String a, String b) {

            }
        }, 5, null, null,fieldName,fragment.getString(R.string.common_ok));
            savePopupWindow.showAtLocation(fragment.getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    public static void showMessageWithOkButton(BaseFragment fragment, String fieldName, final OnOkClick listener) {
        fragment.getActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
        ActionSheet.ActionSheetListener listenerActionSheet = new ActionSheet.ActionSheetListener() {
            @Override
            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
            }

            @Override
            public void onDismissWithCancelButton(ActionSheet actionSheet) {
                actionSheet.dismiss();
                listener.onClick();
            }

            @Override
            public void onOtherButtonClick(ActionSheet actionSheet, int index) {

            }
        };
        ActionSheet.createBuilder(fragment.getActivity(), fragment.getFragmentManager())
                .setCancelButtonTitle(fragment.getString(R.string.common_ok))
                .setOtherButtonTitles(MessageFormat.format(fieldName, StringUtils.EMPTY))
                .setCancelableOnTouchOutside(false).setIsOtherButtonCanClick(false).setListener(listenerActionSheet).show();
    }

    public static String getShortBookingNo(String longBookingNo) {
        String res = StringUtils.EMPTY;
        if (Utils.isStringNotNullOrEmpty(longBookingNo)) {
            int len = longBookingNo.length();
            if (len > 4) {
                if (longBookingNo.contains(Constants.STR_SEPARATOR)) {
                    int indexOfSep = longBookingNo.indexOf(Constants.STR_SEPARATOR);
                    if (indexOfSep > -1 && indexOfSep > 4) {
                        res = longBookingNo.substring(indexOfSep - 4);
                    }
                } else {
                    res = longBookingNo.substring(len - 4);
                }
            } else {
                res = longBookingNo;
            }
        }
        return res;
    }

    public static int getMinuteFromHHMMSS(String hmsString) {
        int res = Constants.MINUTES_PER_HOUR;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT_HHMMSS, Locale.getDefault());
        try {
            Date date = sdf.parse(hmsString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            res = calendar.get(Calendar.MINUTE);
        } catch (ParseException e) {
            Utils.log(e.getMessage());
        }
        return res;
    }

    public static String generateNotNullMessage(Context context, int stringId) {
        return MessageFormat.format(context.getString(R.string.common_must_be_filled_in), context.getString(stringId));
    }

    public static String generateNotNullMessage(BaseFragment mBaseFragment, int stringId) {
        return MessageFormat.format(mBaseFragment.getString(R.string.common_must_be_filled_in), mBaseFragment.getString(stringId));
    }

    public static String generateLargerThanMessage(BaseFragment mBaseFragment, int largeStringId, int smallStringId) {
        return MessageFormat.format(mBaseFragment.getString(R.string.common_must_be_larger_than), mBaseFragment.getString(largeStringId), mBaseFragment.getString(smallStringId));
    }

    public static String generateSegmentTimeWholeMonthMessage(BaseFragment mBaseFragment, String monthString) {
        return MessageFormat.format(mBaseFragment.getString(R.string.segment_time_title_whole_month), monthString);
    }

    public static String generateSegmentTimeWholeMonthMessage(Context mContext, String monthString) {
        return MessageFormat.format(mContext.getString(R.string.segment_time_title_whole_month), monthString);
    }

    public static String generateSegmentTimeEveryWeekMessage(BaseFragment mBaseFragment, String weekString, String monthString) {
        return MessageFormat.format(mBaseFragment.getString(R.string.segment_time_title_every_week), weekString, monthString);
    }

    public static String generateSegmentTimeEveryWeekMessage(Context mContext, String weekString, String monthString) {
        return MessageFormat.format(mContext.getString(R.string.segment_time_title_every_week), weekString, monthString);
    }

    public static String generateNoMoreProductMessage(BaseFragment mBaseFragment, String productName) {
        return MessageFormat.format(mBaseFragment.getString(R.string.msg_no_more_product), productName);
    }

    public static void addBottomSeparatorLine(RelativeLayout relativeLayout, BaseFragment mBaseFragment) {
        addSeparatorLine(relativeLayout, mBaseFragment.getActivity(), false);
    }

    public static void addBottomSeparatorLine(RelativeLayout relativeLayout, Context mContext) {
        addSeparatorLine(relativeLayout, mContext, false);
    }

    public static void addTopSeparatorLine(RelativeLayout relativeLayout, BaseFragment mBaseFragment) {
        addSeparatorLine(relativeLayout, mBaseFragment.getActivity(), true);
    }

    public static void addTopSeparatorLine(RelativeLayout relativeLayout, Context mContext) {
        addSeparatorLine(relativeLayout, mContext, true);
    }

    private static void addSeparatorLine(RelativeLayout relativeLayout, Context mContext, boolean isTop) {
        int position;
        if (isTop) {
            position = RelativeLayout.ALIGN_PARENT_TOP;
        } else {
            position = RelativeLayout.ALIGN_PARENT_BOTTOM;
        }
        ImageView ivSeparator = new ImageView(mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(BaseFragment.MATCH_PARENT, 1);
        layoutParams.addRule(position, BaseFragment.LAYOUT_TRUE);
        ivSeparator.setLayoutParams(layoutParams);
        ivSeparator.setBackgroundColor(mContext.getResources().getColor(R.color.common_separator_gray));
        relativeLayout.addView(ivSeparator);
    }

    public static View getSeparatorLine(BaseFragment mBaseFragment) {
        return getSeparatorView(mBaseFragment, 1);
    }

    public static View getSeparatorLine(Context mContext) {
        return getSeparatorView(mContext, 1);
    }


    public static View getSeparatorLine(Context mContext,int height) {
        return getSeparatorView(mContext, height);
    }

    public static View getSeparatorView(BaseFragment mBaseFragment, int height) {
        return getSeparatorView(mBaseFragment.getBaseActivity(), height);
    }

    public static View getSeparatorView(Context mContext, int height) {
        View ivSeparator = null;
        if (mContext != null) {
            ivSeparator = new View(mContext);
            RelativeLayout.LayoutParams layoutParams
                    = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, height);
            ivSeparator.setLayoutParams(layoutParams);
            ivSeparator.setBackgroundColor(mContext.getResources().getColor(R.color.chart_separator_gray));
        }
        return ivSeparator;
    }


    public static View getSeparatorView(Context mContext,int width, int height) {
        View ivSeparator = null;
        if (mContext != null) {
            ivSeparator = new View(mContext);
            RelativeLayout.LayoutParams layoutParams
                    = new RelativeLayout.LayoutParams(width, height);
            ivSeparator.setLayoutParams(layoutParams);
            ivSeparator.setBackgroundColor(mContext.getResources().getColor(R.color.chart_separator_gray));
        }
        return ivSeparator;
    }




    public static View getVerticalLine(Context mContext, int width) {
        View ivSeparator = null;
        if (mContext != null) {
            ivSeparator = new View(mContext);
            RelativeLayout.LayoutParams layoutParams
                    = new RelativeLayout.LayoutParams(width,RelativeLayout.LayoutParams.MATCH_PARENT);
            ivSeparator.setLayoutParams(layoutParams);
            ivSeparator.setBackgroundColor(mContext.getResources().getColor(R.color.chart_separator_gray));
        }
        return ivSeparator;
    }


    public static View getHorizonSeparatorLine(Context mContext) {
        return getHorizonSeparatorView(mContext, 1);
    }

    public static View getHorizonSeparatorView(BaseFragment mBaseFragment) {
        return getHorizonSeparatorView(mBaseFragment.getBaseActivity(), 1);
    }

    public static View getHorizonSeparatorView(BaseFragment mBaseFragment, int width) {
        return getHorizonSeparatorView(mBaseFragment.getBaseActivity(), width);
    }

    public static View getHorizonSeparatorView(Context mContext, int width) {
        View ivSeparator = null;
        if (mContext != null) {
            ivSeparator = new View(mContext);
            RelativeLayout.LayoutParams layoutParams
                    = new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.MATCH_PARENT);
            ivSeparator.setLayoutParams(layoutParams);
            ivSeparator.setBackgroundColor(mContext.getResources().getColor(R.color.common_separator_gray));
        }
        return ivSeparator;
    }

    public static String getStringHmWithMinute(int minute) {
        return minute / 60 + "h" + minute % 60 + "m";
    }

    public static int getMinuteWithHmString(String hm) {
        if (hm == null || hm.length() <= 0) {
            return 0;
        }
        hm = hm.replace("h", Constants.STR_COLON);
        hm = hm.replace("m", Constants.STR_COLON);
        String[] times = hm.split(Constants.STR_COLON);
        return Integer.parseInt(times[0]) * 60 + Integer.parseInt(times[1]);
    }

    public static String addCurrencySymbol(String price, Context context) {
        return AppUtils.getCurrentCurrency(context) + Constants.STR_SPACE + price;
    }

    public static String removeCurrencySymbol(String price, Context context) {
        return price.replace(AppUtils.getCurrentCurrency(context), Constants.STR_EMPTY).trim();
    }

    public static int getLargerButtonWidth(BaseActivity activity) {
        return activity.getWidth(640);
    }

    public static int getLargerButtonWidth(BaseFragment fragment) {
        return fragment.getActualWidthOnThisDevice(640);
    }

    public static int getLargerButtonHeight(BaseActivity activity) {
        return activity.getWidth(80);
    }

    public static int getLargerButtonHeight(BaseFragment fragment) {
        return fragment.getActualWidthOnThisDevice(90);
    }

    public static void showHaveNoPermission(BaseFragment fragment) {
        Utils.showShortToast(fragment.getActivity(), R.string.common_do_not_have_permission);
    }

    public static void showHaveNoPermission(Context mContext) {
        Utils.showShortToast(mContext, R.string.common_do_not_have_permission);
    }

    public static int getMonthDaySize(int year, int month) {
        int res = 31;
        switch (month) {
            case 4:
            case 6:
            case 9:
            case 11:
                res = 30;
                break;
            case 2:
                if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {
                    res = 29;
                } else {
                    res = 28;
                }
                break;
        }
        return res;
    }

    public static boolean getShowSalesTax(Context mContext) {
        boolean res = false;
        JsonLogin loginInfo = getLoginInfo(mContext);
        if (loginInfo != null) {
            res = Constants.STR_FLAG_YES.equals(loginInfo.getShowSalesTax());
        }
        return res;
    }

    public static boolean isAgent(Context mContext) {
        boolean res = false;
        JsonLogin loginInfo = getLoginInfo(mContext);
        if (loginInfo != null) {
            res = Constants.STR_AGENT_LOGIN.equalsIgnoreCase(loginInfo.getUserType());
        }
        return res;
    }

    public static int getRightButtonWidth(Context mContext) {
        return DensityUtil.getActualWidthOnThisDevice(150, mContext);
    }

//    public static int[] getFontSize(Context mContext) {  px
//        int[] res = new int[]{20, 25, 30, 35, 40, 45,15};
//        int screenWidth = DensityUtil.getScreenWidth(mContext);
//        int screenHeight = DensityUtil.getScreenHeight(mContext);
//        if (screenWidth == 720 && screenHeight == 1280) {
//            res = new int[]{18, 25, 30, 35, 40, 45,32};
//        }
//        if (screenWidth == 1080 && screenHeight == 1920) {
//            res = new int[]{26, 32, 38, 44, 50, 56,15};
//        }
//        if (screenWidth == 480 && screenHeight == 800) {
//            res = new int[]{15, 18, 22, 26, 30, 34,15};
//        } if (screenWidth == 480 && screenHeight == 800) {
//            res = new int[]{15, 18, 22, 26, 30, 34,15};
//        }
//        if (screenWidth == 1080 && screenHeight == 1776) {
//            res = new int[]{30, 37, 45, 52, 60, 68,46};
//        }
//
//        if (screenWidth == 540) {
//            res = new int[]{15, 18, 22, 26, 30, 34,15};
//        }
//        return res;
//    }

    public static float FONT_SIZE_SMALLEST = 7.f;
    public static float FONT_SIZE_MORE_SMALLER = 9.f;
    public static float FONT_SIZE_SMALLER = 11.f;
    public static float FONT_SIZE_NORMAL = 15.f;
    public static float FONT_SIZE_LARGER = 19.f;
    public static float FONT_SIZE_MORE_LARGER = 23.f;


//    public static final int FONT_SIZE_INDEX_SMALLEST = 0;
//    public static final int FONT_SIZE_INDEX_MORE_SMALLER = 1;
//    public static final int FONT_SIZE_INDEX_SMALLER = 2;
//    public static final int FONT_SIZE_INDEX_NORMAL = 3;
//    public static final int FONT_SIZE_INDEX_LARGER = 4;
//    public static final int FONT_SIZE_INDEX_LARGEST = 5;



//    public static final int FONT_SIZE_INDEX_SMALLEST = 0;
//    public static final int FONT_SIZE_INDEX_MORE_SMALLER = 1;
//    public static final int FONT_SIZE_INDEX_SMALLER = 2;
//    public static final int FONT_SIZE_INDEX_NORMAL = 3;
//    public static final int FONT_SIZE_INDEX_LARGER = 4;
//    public static final int FONT_SIZE_INDEX_LARGEST = 5;
//
//    public static final int FONT_SIZE_15 = 6;
public static int[] getFontSize(Context mContext) {//xp
   // int[] res = new int[]{20, 25, 30, 35, 40, 45,15};
    //int[]  res = new int[]{9, 13, 15, 18, 20, 23,16};
    int[]  res = new int[]{8, 12, 14, 17, 19, 22,15};

//    int screenWidth = DensityUtil.getScreenWidth(mContext);
//    int screenHeight = DensityUtil.getScreenHeight(mContext);
//    if (screenWidth == 720 && screenHeight == 1280) {// zx
//        res = new int[]{9, 13, 15, 18, 20, 23,16};
//    }
//    if (screenWidth == 1080 && screenHeight == 1920) {
//        res = new int[]{26, 32, 38, 44, 50, 56,15};
//    }
//    if (screenWidth == 480 && screenHeight == 800) {
//        res = new int[]{15, 18, 22, 26, 30, 34,15};
//    } if (screenWidth == 480 && screenHeight == 800) {
//        res = new int[]{15, 18, 22, 26, 30, 34,15};
//    }
//    if (screenWidth == 1080 && screenHeight == 1776) {
//        res = new int[]{30, 37, 45, 52, 60, 68,46};
//    }
//
//    if (screenWidth == 540) {
//        res = new int[]{15, 18, 22, 26, 30, 34,15};
//    }
    return res;
}
    public static ArrayList<String> changeString2List(String strWithSplit, String splitString) {
        ArrayList<String> res = new ArrayList<>();
        String[] split = strWithSplit.split(splitString);
        for (String s : split) {
            res.add(s);
        }
        return res;
    }

    public static String getEtTimeTitle(List<String> dates, Context mContext) {
        boolean[] everyWeekDay = new boolean[]{true, true, true, true, true, true, true};
        String[] weekDayNames = new String[]{mContext.getString(R.string.calendar_sun)
                , mContext.getString(R.string.calendar_mon)
                , mContext.getString(R.string.calendar_tue)
                , mContext.getString(R.string.calendar_wed)
                , mContext.getString(R.string.calendar_thu)
                , mContext.getString(R.string.calendar_fri)
                , mContext.getString(R.string.calendar_sat)};

        String[] monthNames = new String[]{
                mContext.getString(R.string.calendar_jan),
                mContext.getString(R.string.calendar_feb),
                mContext.getString(R.string.calendar_mar),
                mContext.getString(R.string.calendar_apr),
                mContext.getString(R.string.calendar_may),
                mContext.getString(R.string.calendar_jun),
                mContext.getString(R.string.calendar_jul),
                mContext.getString(R.string.calendar_aug),
                mContext.getString(R.string.calendar_sep),
                mContext.getString(R.string.calendar_oct),
                mContext.getString(R.string.calendar_nov),
                mContext.getString(R.string.calendar_dec)
        };

        ArrayList<ArrayList<String>> weekDaysContainer = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDaysContainer.add(new ArrayList<String>());
        }

        StringBuilder monthString = new StringBuilder();
        StringBuilder weekString = new StringBuilder();
        StringBuilder periodString = new StringBuilder();
        StringBuilder singleString = new StringBuilder();

        Calendar calendar = Calendar.getInstance();
        ArrayList<String> weekDays = new ArrayList<>();
        ArrayList<String> monthDays = new ArrayList<>();
        LinkedList<String> periodDays = new LinkedList<>();

        int start;
        int end;
        int year = calendar.get(Calendar.YEAR);

        calendar.setTime(DateUtils.getDateFromAPIYearMonthDay(dates.get(0)));
        start = calendar.get(Calendar.MONTH);
        calendar.setTime(DateUtils.getDateFromAPIYearMonthDay(dates.get(dates.size() - 1)));
        end = calendar.get(Calendar.MONTH);

        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        String lastDay = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dates.get(0), mContext);

        for (int month = start; month <= end; month++) {
            monthDays.clear();
            for (int day = 1; day <= 31; day++) {
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                if (calendar.get(Calendar.MONTH) == month) {
                    String dateStr = DateUtils.getAPIYearMonthDay(calendar.getTime());
                    int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    if (everyWeekDay[weekDay]) {
                        if (!dates.contains(dateStr)) {
                            everyWeekDay[weekDay] = false;
                        } else {
                            monthDays.add(dateStr);
                            weekDaysContainer.get(weekDay).add(dateStr);
                        }
                    }
                }
            }

            if (monthDays.size() == AppUtils.getMonthDaySize(year, month + 1)) {
                if (monthString.length() > 0) {
                    monthString.append(Constants.STR_COMMA);
                }
                monthString.append(AppUtils.generateSegmentTimeWholeMonthMessage(mContext, monthNames[month]));
                continue;
            }

            String choseWeekDay = Constants.STR_EMPTY;
            for (int weekDay = 0; weekDay < everyWeekDay.length; weekDay++) {
                if (everyWeekDay[weekDay]) {
                    if (Utils.isStringNotNullOrEmpty(choseWeekDay)) {
                        choseWeekDay += Constants.STR_COMMA;
                    }
                    choseWeekDay += weekDayNames[weekDay];
                    weekDays.addAll(weekDaysContainer.get(weekDay));
                }
                weekDaysContainer.get(weekDay).clear();
            }
            if (Utils.isStringNotNullOrEmpty(choseWeekDay)) {
                if (weekString.length() > 0) {
                    weekString.append(Constants.STR_COMMA);
                }
                weekString.append(AppUtils.generateSegmentTimeEveryWeekMessage(mContext, choseWeekDay, monthNames[month]));
            }

            for (int day = 1; day <= 31; day++) {
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                if (calendar.get(Calendar.MONTH) == month) {
                    String dateStr = DateUtils.getAPIYearMonthDay(calendar.getTime());
                    if (dates.contains(dateStr) && !weekDays.contains(dateStr)) {
                        if (periodDays.size() == 0 || lastDay.equals(periodDays.getLast())) {
                            periodDays.add(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dateStr, mContext));
                        } else {
                            generatePeriodString(periodDays, periodString, singleString);
                            periodDays.add(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dateStr, mContext));
                        }
                    }
                    lastDay = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dateStr, mContext);
                }
            }
        }

        if (periodDays.size() > 0) {
            generatePeriodString(periodDays, periodString, singleString);
        }

        String res = monthString.toString();
        if (Utils.isStringNotNullOrEmpty(res) && weekString.length() > 0) {
            res += Constants.STR_COMMA;
        }
        res += weekString.toString().replace(Constants.STR_SEPARATOR, Constants.STR_SLASH);

        if (Utils.isStringNotNullOrEmpty(res) && periodString.length() > 0) {
            res += Constants.STR_COMMA;
        }
        res += periodString.toString().replace(Constants.STR_SEPARATOR, Constants.STR_SLASH);

        if (Utils.isStringNotNullOrEmpty(res) && singleString.length() > 0) {
            res += Constants.STR_COMMA;
        }
        res += singleString.toString().replace(Constants.STR_SEPARATOR, Constants.STR_SLASH);

        return res;
    }

    private static void generatePeriodString(LinkedList<String> periodDays, StringBuilder periodString, StringBuilder singleString) {
        if (periodDays.size() == 1) {
            if (singleString.length() > 0) {
                singleString.append(Constants.STR_COMMA);
            }
            singleString.append(periodDays.get(0));
        } else {
            if (periodString.length() > 0) {
                periodString.append(Constants.STR_COMMA);
            }
            periodString.append(periodDays.get(0))
                    .append(Constants.STR_WAVE)
                    .append(periodDays.getLast());
        }
        periodDays.clear();
    }

    public enum AuthControl {
        AuthControlMenuControlTeeTimes(1),
        AuthControlMenuControlEvents(2),
        AuthControlMenuControlShops(3),
        AuthControlMenuControlCustomers(4),
        AuthControlMenuControlAgents(5),
        AuthControlMenuControlStaffs(6),
        AuthControlMenuControlNews(7),
        AuthControlMenuControlDiscounts(8),
        AuthControlEditCourseData(9),
        AuthControlEditTeeTimes(10),
        AuthControlCheckInOrUndo(11),
        AuthControlCheckOut(12),
        AuthControlSeeOtherReservations(13),
        AuthControlMenuEvents(14),
        AuthControlShopsEdit(15),
        AuthControlProductEdit(16),
        AuthControlSelling(17),
        AuthControlMinusBadInventory(18),
        AuthControlAddInventory(19),
        AuthControlCustomersTypeEdit(20),
        AuthControlProfileEdit(21),
        AuthControlRecharge(22),
        AuthControlMembershipManagement(23),
        AuthControlRefund(24),
        AuthControlMenuAgents(25),
        AuthControlAgentsEdit(26),
        AuthControlAgentsRecharge(27),
        AuthControlAgentsPricingTable(28),
        AuthControlAgentsRefund(29),
        AuthControlStaffEdit(30),
        AuthControlSeeOtherDepartments(31),
        AuthControlDiscount(32),
        AuthControlPercent(33),
        AuthControlDiscountMoney(34),
        AuthControlMenuAdministration(35),
        AuthControlProfileEditMember(36),
        AuthControlProfileEditNonmember(37);

        private int value = 0;

        AuthControl(int value) {
            this.value = value;
        }

        public static AuthControl valueOf(int value) {
            switch (value) {
                case 1:
                    return AuthControlMenuControlTeeTimes;
                case 2:
                    return AuthControlMenuControlEvents;
                case 3:
                    return AuthControlMenuControlShops;
                case 4:
                    return AuthControlMenuControlCustomers;
                case 5:
                    return AuthControlMenuControlAgents;
                case 6:
                    return AuthControlMenuControlStaffs;
                case 7:
                    return AuthControlMenuControlNews;
                case 8:
                    return AuthControlMenuControlDiscounts;
                case 9:
                    return AuthControlEditCourseData;
                case 10:
                    return AuthControlEditTeeTimes;
                case 11:
                    return AuthControlCheckInOrUndo;
                case 12:
                    return AuthControlCheckOut;
                case 13:
                    return AuthControlSeeOtherReservations;
                case 14:
                    return AuthControlMenuEvents;
                case 15:
                    return AuthControlShopsEdit;
                case 16:
                    return AuthControlProductEdit;
                case 17:
                    return AuthControlSelling;
                case 18:
                    return AuthControlMinusBadInventory;
                case 19:
                    return AuthControlAddInventory;
                case 20:
                    return AuthControlCustomersTypeEdit;
                case 21:
                    return AuthControlProfileEdit;
                case 22:
                    return AuthControlRecharge;
                case 23:
                    return AuthControlMembershipManagement;
                case 24:
                    return AuthControlRefund;
                case 25:
                    return AuthControlMenuAgents;
                case 26:
                    return AuthControlAgentsEdit;
                case 27:
                    return AuthControlAgentsRecharge;
                case 28:
                    return AuthControlAgentsPricingTable;
                case 29:
                    return AuthControlAgentsRefund;
                case 30:
                    return AuthControlStaffEdit;
                case 31:
                    return AuthControlSeeOtherDepartments;
                case 32:
                    return AuthControlDiscount;
                case 33:
                    return AuthControlPercent;
                case 34:
                    return AuthControlDiscountMoney;
                case 35:
                    return AuthControlMenuAdministration;
                case 36:
                    return AuthControlProfileEditMember;
                case 37:
                    return AuthControlProfileEditNonmember;
                default:
                    return AuthControlMenuAdministration;
            }
        }

        public int value() {
            return this.value;
        }
    }

    public interface DeleteConfirmListener {
        void onClickDelete();
    }

    public interface SaveConfirmListener {
        void onClickSave();
    }

    public interface OnOkClick {
        void onClick();
    }

    public static class EditViewMinuteWatcher implements TextWatcher {

        private EditText mEditText;
        private int maxValue;

        public EditViewMinuteWatcher(EditText editText, int maxValue) {
            this.mEditText = editText;
            this.maxValue = maxValue;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText.hasFocus()) {
                String currentString = s.toString();
                String beforeStr = currentString.substring(0, start);
                String addStr = currentString.substring(start, start + count);

                if (Utils.isStringNotNullOrEmpty(addStr)) {
                    boolean isDigit = true;
                    for (int i = 0; i < addStr.length(); i++) {
                        isDigit = isDigit && Character.isDigit(addStr.charAt(i));
                    }
                    if (isDigit) {
                        int age = Integer.parseInt(currentString);
                        if (age > maxValue) {
                            mEditText.setText(beforeStr);
                        }
                    } else {
                        mEditText.setText(beforeStr);
                    }
                    mEditText.setSelection(this.mEditText.getText().toString().length());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    public static class EditViewIntegerWatcher implements TextWatcher {

        private EditText mEditText;
        private int maxValue;
        private int maxSize;
        private boolean isQty;

        public EditViewIntegerWatcher(EditText editText) {
            this.mEditText = editText;
            maxValue = Integer.MAX_VALUE;
            maxSize = 11;
        }

        public EditViewIntegerWatcher(EditText editText, int maxValue) {
            this.mEditText = editText;
            this.maxValue = maxValue;
            maxSize = 11;
        }

        public EditViewIntegerWatcher(EditText editText, int maxValue, int maxSize) {
            this.mEditText = editText;
            this.maxSize = maxSize;
            this.maxValue = maxValue;
        }

        public boolean isQty() {
            return isQty;
        }

        public void setIsQty(boolean isQty) {
            this.isQty = isQty;
        }

        public void setMaxValue(int maxValue) {
            this.maxValue = maxValue;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText.hasFocus()) {
                String currentString = s.toString();
                String beforeStr = currentString.substring(0, start);
                String addStr = currentString.substring(start, start + count);

                if (Utils.isStringNotNullOrEmpty(addStr)) {
                    boolean isDigit = true;
                    for (int i = 0; i < addStr.length(); i++) {
                        isDigit = isDigit && Character.isDigit(addStr.charAt(i));
                    }
                    if (isDigit) {
                        if (currentString.length() < maxSize) {
                            long value = Long.parseLong(currentString);
                            if (value > maxValue) {
                                mEditText.setText(beforeStr);
                            }
                        } else {
                            mEditText.setText(beforeStr);
                        }
                    } else {
                        mEditText.setText(beforeStr);
                    }
                    mEditText.setSelection(this.mEditText.getText().toString().length());
                }

                if (isQty()) {
                    String v = mEditText.getText().toString();
                    if (StringUtils.isNotEmpty(v)) {
                        if (Constants.STR_0.equals(v)) {
                            mEditText.setTextColor(mEditText.getResources().getColor(R.color.common_red));
                        } else {
                            mEditText.setTextColor(mEditText.getResources().getColor(R.color.common_black));
                        }
                    }
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }

    public static class DownImage extends AsyncTask<Void, Void, byte[]> {

        private String url;
        private DownLoadedImageListener listener;

        public DownImage(String url, DownLoadedImageListener listener) {
            this.url = url;
            this.listener = listener;
        }

        @Override
        protected byte[] doInBackground(Void... voids) {
            byte[] res = null;
            URL myFileUrl = null;
            try {
                myFileUrl = new URL(url);
            } catch (MalformedURLException e) {
                Utils.log(e.getMessage());
            }
            if (myFileUrl != null) {
                try {
                    HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                    conn.setConnectTimeout(0);
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    res = new byte[is.available()];
                    int read = is.read(res);
                    Utils.log(String.valueOf("read : " + read));
                    is.close();
                } catch (IOException e) {
                    Utils.log(e.getMessage());
                }
            }
            return res;
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            if (listener != null) {
                listener.onDownLoaded(bytes);
            }
        }

        public interface DownLoadedImageListener {
            void onDownLoaded(byte[] bytes);

        }
    }

    public static class DownImageClient extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private DownLoadedImageClientListener listener;
        private Context mContext;

        private View waitingView;
        private RelativeLayout relativeLayout;

        public DownImageClient(String url, DownLoadedImageClientListener listener, Context mContext) {
            this.url = url;
            this.listener = listener;
            this.mContext = mContext;
        }

        public void setRelativeLayout(RelativeLayout relativeLayout) {
            this.relativeLayout = relativeLayout;
        }

        @Override
        protected void onPreExecute() {
            if (relativeLayout != null) {
                LayoutInflater inflater = LayoutInflater.from(mContext);
                waitingView = inflater.inflate(R.layout.view_common_waiting, null);
                relativeLayout.addView(waitingView);

                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) waitingView.getLayoutParams();
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                waitingView.setLayoutParams(layoutParams);
            }
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {
            Bitmap bitmap = null;
            if (url != null) {
                try {
                    HttpGet httpRequest = new HttpGet(url);
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpResponse response = httpclient.execute(httpRequest);
                    HttpEntity entity = response.getEntity();
                    BufferedHttpEntity bufferedHttpEntity = new BufferedHttpEntity(entity);
                    InputStream is = bufferedHttpEntity.getContent();
                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    Utils.log(e.getMessage());
                }
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (listener != null) {
                listener.onDownLoadedClient(bitmap);
            }
            if (relativeLayout != null) {
                relativeLayout.removeView(waitingView);
            }
        }

        public interface DownLoadedImageClientListener {
            void onDownLoadedClient(Bitmap bitmap);
        }
    }

    public static class EditViewMoneyWatcher implements TextWatcher {
        private EditText mEditText;

        private Double maxValue = 9999999.99;

        public EditViewMoneyWatcher(EditText editText) {
            this.mEditText = editText;
        }

        public EditViewMoneyWatcher(EditText editText, double maxValue) {
            this.mEditText = editText;
            this.maxValue = maxValue;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText.hasFocus()) {
                String currentString = s.toString();
                String beforeStr = currentString.substring(0, start);
                String addStr = currentString.substring(start, start + count);
                int length = currentString.length();

                if (Utils.isStringNullOrEmpty(beforeStr) && Constants.STR_DOT.equals(addStr)) {
                    mEditText.setText(Constants.STR_EMPTY);
                    return;
                }

                if (Utils.isStringNotNullOrEmpty(addStr)) {
                    boolean isMatchFormat = true;
                    for (int i = 0; i < addStr.length(); i++) {
                        isMatchFormat = isMatchFormat && (Character.isDigit(addStr.charAt(i)) || Constants.STR_DOT.equals(String.valueOf(addStr.charAt(i))));
                    }
                    if (isMatchFormat) {
                        if (Constants.STR_DOT.equals(addStr)) {
                            isMatchFormat = !beforeStr.contains(Constants.STR_DOT);
                        }
                    }
                    if (isMatchFormat) {
                        if (currentString.contains(Constants.STR_DOT) && length - currentString.indexOf(Constants.STR_DOT) > 3) {
                            mEditText.setText(beforeStr);
                        } else {
                            double money = Double.parseDouble(currentString);
                            if (getMaxValue() != null && money > getMaxValue()) {
                                mEditText.setText(beforeStr);
                            }
                        }
                    } else {
                        mEditText.setText(beforeStr);
                    }
                    mEditText.setSelection(this.mEditText.getText().toString().length());
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        public Double getMaxValue() {
            return maxValue;
        }

        public void setMaxValue(Double maxValue) {
            this.maxValue = maxValue;
        }
    }

    public static class AddRemoveMoneySymbolListener extends AddRemoveSymbolListener {
        public AddRemoveMoneySymbolListener(Context context) {
            super(true, AppUtils.getCurrentCurrency(context));
        }
    }

    public static class AddRemoveMSymbolListener extends AddRemoveSymbolListener {
        public AddRemoveMSymbolListener(Context context) {
            super(false, Constants.CURRENCY_M);
        }
    }

    public static class AddRemoveDaySymbolListener extends AddRemoveSymbolListener {
        public AddRemoveDaySymbolListener(Context context) {
            super(false, Constants.CURRENCY_DAY);
        }
    }

    public static class AddIntegerFocusListener implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean b) {
            EditText editText = (EditText) view;
            String currentString = editText.getText().toString();
            double money = 0;
            if (b) {
                try {
                    money = Double.parseDouble(currentString);
                } catch (NumberFormatException e) {
                    Utils.log(e.getMessage());
                }
                if (money != 0) {
                    editText.setText(currentString);
                } else {
                    editText.setText(Constants.STR_EMPTY);
                }
            }
        }
    }

    public static class AddRemoveSymbolListener implements View.OnFocusChangeListener {

        private boolean isPrefix;
        private String symbol;

        public AddRemoveSymbolListener(boolean isPrefix, String symbol) {
            this.isPrefix = isPrefix;
            this.symbol = symbol;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            EditText editText = (EditText) v;
            String currentString = editText.getText().toString();

            double money;
            if (hasFocus) {
                if (currentString.contains(symbol)) {
                    currentString = currentString.replace(symbol, Constants.STR_EMPTY);
                }

                try {
                    money = Double.parseDouble(currentString);
                } catch (NumberFormatException e) {
                    money = 0;
                }
                if (money != 0) {
                    editText.setText(currentString);
                } else {
                    editText.setText(Constants.STR_EMPTY);
                }
            } else {
                if (!currentString.contains(symbol)) {
                    if (isPrefix) {
                        currentString = symbol + Constants.STR_EMPTY + currentString;
                    } else {
                        currentString += Constants.STR_EMPTY + symbol;
                    }
                }
                editText.setText(currentString);
            }


        }
    }

    public static abstract class NoDoubleClickListener implements View.OnClickListener {

        private Activity mContext;

        public NoDoubleClickListener(Activity mContext) {
            this.mContext = mContext;
        }

        public abstract void noDoubleClick(View v);

        public void onClick(View v) {
            Utils.hideKeyboard(mContext);
            v.setEnabled(false);
            new DelayTask(v).execute();
            noDoubleClick(v);
        }

        class DelayTask extends AsyncTask<String, Void, Void> {

            private View view;

            public DelayTask(View view) {
                this.view = view;
            }

            @Override
            protected Void doInBackground(String... params) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Utils.log(e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if (view != null) {
                    view.setEnabled(true);
                }
            }
        }
    }

    public static int getActionBarHeight() {
        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (IteeApplication.getInstance().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, IteeApplication.getInstance().getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }


}