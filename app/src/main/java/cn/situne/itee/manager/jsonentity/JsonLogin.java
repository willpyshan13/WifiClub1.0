/**
 * Project Name: itee
 * File Name:  JsonLogin.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-01-27
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonLogin <br/>
 * Function: To login. <br/>
 * Date: 2015-01-27 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
@SuppressWarnings("UnusedDeclaration")
public class JsonLogin extends BaseJsonObject implements Serializable {

    private static final long serialVersionUID = -5429825943056427709L;


    public static final String BOOKING_TYPE_1 = "1";
    public static final String BOOKING_TYPE_2 = "2";
    private String token;
    private String refreshToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    private Integer userId;
    private Boolean userIsChangePwd;
    private String userName;
    private String userLogo;
    private Integer clubId;
    private String weekFirstDay;
    private String courseId;
    private String currencyId;
    private String currencySymbol;
    private String auth;
    private String oneTeeOnly;
    private String paymentPattern;
    private String salesTaxExcluding;
    private String salesTax;
    private String unit;
    private String caddieFee;
    private String countryName;
    private String timeZone;
    private String adminFlag;
    private String showSalesTax;
    private String userType;
    private String userTel;
    private String dateFormat;
    private String isCaddie;

    private String isSellCaddie;



    private String bookingType;

    public String getBookingType() {
        return bookingType;
    }

    public void setBookingType(String bookingType) {
        this.bookingType = bookingType;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getIsSellCaddie() {
        return isSellCaddie;
    }

    public void setIsSellCaddie(String isSellCaddie) {
        this.isSellCaddie = isSellCaddie;
    }

    public String getIsCaddie() {
        return isCaddie;
    }

    public void setIsCaddie(String isCaddie) {
        this.isCaddie = isCaddie;
    }

    private ArrayList<MenuAuth> setMenuAuth;

    public JsonLogin(JSONObject jsonObject) {
        super(jsonObject);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Boolean isUserIsChangePwd() {
        return userIsChangePwd;
    }

    public void setUserIsChangePwd(Boolean userIsChangePwd) {
        this.userIsChangePwd = userIsChangePwd;
    }

    public String getWeekFirstDay() {
        return weekFirstDay;
    }

    public void setWeekFirstDay(String weekFirstDay) {
        this.weekFirstDay = weekFirstDay;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public Integer getClubId() {
        return clubId;
    }

    public void setClubId(Integer clubId) {
        this.clubId = clubId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public ArrayList<MenuAuth> getSetMenuAuth() {
        return setMenuAuth;
    }

    public void setSetMenuAuth(ArrayList<MenuAuth> setMenuAuth) {
        this.setMenuAuth = setMenuAuth;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public Boolean getUserIsChangePwd() {
        return userIsChangePwd;
    }

    public String getOneTeeOnly() {
        return oneTeeOnly;
    }

    public void setOneTeeOnly(String oneTeeOnly) {
        this.oneTeeOnly = oneTeeOnly;
    }

    public String getPaymentPattern() {
        return paymentPattern;
    }

    public void setPaymentPattern(String paymentPattern) {
        this.paymentPattern = paymentPattern;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(String salesTax) {
        this.salesTax = salesTax;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCaddieFee() {
        return caddieFee;
    }

    public void setCaddieFee(String caddieFee) {
        this.caddieFee = caddieFee;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getSalesTaxExcluding() {
        return salesTaxExcluding;
    }

    public void setSalesTaxExcluding(String salesTaxExcluding) {
        this.salesTaxExcluding = salesTaxExcluding;
    }

    public String getAuth() {
        return this.auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    private String showConfigFlag;

    public String getShowConfigFlag() {
        return showConfigFlag;
    }

    public void setShowConfigFlag(String showConfigFlag) {
        this.showConfigFlag = showConfigFlag;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getAdminFlag() {
        return adminFlag;
    }

    public void setAdminFlag(String adminFlag) {
        this.adminFlag = adminFlag;
    }

    public String getShowSalesTax() {
        return showSalesTax;
    }

    public void setShowSalesTax(String showSalesTax) {
        this.showSalesTax = showSalesTax;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserTel() {
        return userTel;
    }

    public void setUserTel(String userTel) {
        this.userTel = userTel;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public static class MenuAuth implements Serializable {
        private Integer menuId;

        public Integer getMenuId() {
            return menuId;
        }

        public void setMenuId(Integer menuId) {
            this.menuId = menuId;
        }
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {

        Utils.log(jsonObj.toString());

        setReturnCode(Utils.getIntegerFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_RETURN_CODE));
        setReturnInfo(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_RETURN_INFO));

        if (Constants.RETURN_CODE_LOGIN_SUCCESS == getReturnCode()) {
            setAuth(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_SET_AUTH));

            setToken(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_TOKEN));
            setRefreshToken(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_REFRESH_TOKEN));
            setUserId(Utils.getIntegerFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_USER_ID));
            setClubId(Utils.getIntegerFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_CLUB_ID));
            setUserIsChangePwd(Utils.getBooleanFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_USER_IS_CHANGE_PWD));
            setUserLogo(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_USER_LOGO));
            setUserName(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_USER_NAME));
            setCourseId(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_COURSE_ID));
            setWeekFirstDay(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_WEEK_FIRST_DAY));

            setOneTeeOnly(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_ONE_TEE_ONLY));
            setPaymentPattern(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_PAYMENT_PATTERN));
            setSalesTaxExcluding(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_SALES_TAX_EXCLUDING));
            setSalesTax(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_SALES_TAX));
            setUnit(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_UNIT));
            setCaddieFee(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_CADDIE_FEE));
            setCountryName(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_COUNTRY_NAME));

            setCurrencyId(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_CURR_ID));
            setCurrencySymbol(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_CURR_CODE));

            setAdminFlag(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_ADMIN_FLAG));
            setShowSalesTax(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_SHOW_SALES_TAX));

            setUserType(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_USER_TYPE));
            setUserTel(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_USER_TEL));

            setDateFormat(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_DATE_FORMAT));

            setIsCaddie(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_IS_CADDIE));
            setBookingType(BOOKING_TYPE_2);

            setShowConfigFlag(Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_SHOW_CONFIG_FLAG));
            timeZone = Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_TIME_ZONE);

            try {
                setMenuAuth = new ArrayList<>();
                JSONArray arrSetMenuAuth = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.LOGIN_SET_MENU_AUTH);
                for (int i = 0; i < arrSetMenuAuth.length(); i++) {
                    JSONObject joMenuAuth = arrSetMenuAuth.getJSONObject(i);
                    MenuAuth ma = new MenuAuth();
                    ma.setMenuId(Utils.getIntegerFromJsonObjectWithKey(joMenuAuth, JsonKey.LOGIN_MENU_ID));
                    setMenuAuth.add(ma);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }
}
