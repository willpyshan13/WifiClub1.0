/**
 * Project Name: itee
 * File Name:  JsonEditAdministration.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonEditAdministration <br/>
 * Function: To edit administration. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonEditAdministration extends BaseJsonObject implements Serializable {

    private DataList dataList;

    public class DataList implements Serializable {
        private DefaultCity defaultCity;
        private List<Currency> currencyList;
        private DefaultCurrency defaultCurrency;
        private String logoutAfter;
        private Integer logoutAfterStatus;
        private Integer sendReserveMessage;
        private Integer defaultOneTeeStartOnly;
        private String defaultPaymentPattern;
        private Integer salesTaxExcluding;
        private String salesTax;
        private String unit;
        private String firstDay;
        private Integer caddieFee;
        private String showSalesTax;
        private String dateFormat;

        public Integer getCaddieFee() {
            return caddieFee;
        }

        public void setCaddieFee(Integer caddieFee) {
            this.caddieFee = caddieFee;
        }

        public DefaultCity getDefaultCity() {
            return defaultCity;
        }

        public void setDefaultCity(DefaultCity defaultCity) {
            this.defaultCity = defaultCity;
        }


        public List<Currency> getCurrencyList() {
            return currencyList;
        }

        public void setCurrencyList(List<Currency> currencyList) {
            this.currencyList = currencyList;
        }

        public DefaultCurrency getDefaultCurrency() {
            return defaultCurrency;
        }

        public void setDefaultCurrency(DefaultCurrency defaultCurrency) {
            this.defaultCurrency = defaultCurrency;
        }

        public String getLogoutAfter() {
            return logoutAfter;
        }

        public void setLogoutAfter(String logoutAfter) {
            this.logoutAfter = logoutAfter;
        }

        public Integer getLogoutAfterStatus() {
            return logoutAfterStatus;
        }

        public void setLogoutAfterStatus(Integer logoutAfterStatus) {
            this.logoutAfterStatus = logoutAfterStatus;
        }

        public Integer getSendReserveMessage() {
            return sendReserveMessage;
        }

        public void setSendReserveMessage(Integer sendReserveMessage) {
            this.sendReserveMessage = sendReserveMessage;
        }

        public Integer getDefaultOneTeeStartOnly() {
            return defaultOneTeeStartOnly;
        }

        public void setDefaultOneTeeStartOnly(Integer defaultOneTeeStartOnly) {
            this.defaultOneTeeStartOnly = defaultOneTeeStartOnly;
        }

        public String getDefaultPaymentPattern() {
            return defaultPaymentPattern;
        }

        public void setDefaultPaymentPattern(String defaultPaymentPattern) {
            this.defaultPaymentPattern = defaultPaymentPattern;
        }

        public Integer getSalesTaxExcluding() {
            return salesTaxExcluding;
        }

        public void setSalesTaxExcluding(Integer salesTaxExcluding) {
            this.salesTaxExcluding = salesTaxExcluding;
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

        public String getFirstDay() {
            return firstDay;
        }

        public void setFirstDay(String firstDay) {
            this.firstDay = firstDay;
        }

        public String getShowSalesTax() {
            return showSalesTax;
        }

        public void setShowSalesTax(String showSalesTax) {
            this.showSalesTax = showSalesTax;
        }

        public String getDateFormat() {
            return dateFormat;
        }

        public void setDateFormat(String dateFormat) {
            this.dateFormat = dateFormat;
        }
    }

//    public class City implements Serializable {
//        private String country;
//        private String cityName;
//        private Integer cityId;
//        private String zoneCode;
//
//        public void setZoneCode(String zoneCode) {
//            this.zoneCode = zoneCode;
//        }
//
//        public String getCountry() {
//            return country;
//        }
//
//        public void setCountry(String country) {
//            this.country = country;
//        }
//
//        public void setCityName(String cityName) {
//            this.cityName = cityName;
//        }
//
//        public void setCityId(Integer cityId) {
//            this.cityId = cityId;
//        }
//    }

    public class DefaultCity implements Serializable {
        private String country;
        private String cityName;
        private Integer cityId;
        private String zoneCode;

        public String getZoneCode() {
            return zoneCode;
        }

        public void setZoneCode(String zoneCode) {
            this.zoneCode = zoneCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public Integer getCityId() {
            return cityId;
        }

        public void setCityId(Integer cityId) {
            this.cityId = cityId;
        }
    }


    public class Currency implements Serializable {
        private String currency;
        private Integer currId;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public Integer getCurrId() {
            return currId;
        }

        public void setCurrId(Integer currId) {
            this.currId = currId;
        }
    }

    public class DefaultCurrency implements Serializable {
        private String defaultCurrency;
        private Integer defaultCurrId;

        public String getDefaultCurrency() {
            return defaultCurrency;
        }

        public void setDefaultCurrency(String defaultCurrency) {
            this.defaultCurrency = defaultCurrency;
        }

        public Integer getDefaultCurrId() {
            return defaultCurrId;
        }

        public void setDefaultCurrId(Integer defaultCurrId) {
            this.defaultCurrId = defaultCurrId;
        }
    }

    public JsonEditAdministration(JSONObject jsonObject) {
        super(jsonObject);
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        Utils.log(jsonObj.toString());
        try {
            DataList dl = new DataList();
            setDataList(dl);
            if (!jsonObj.isNull(JsonKey.COMMON_DATA_LIST)) {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);


//                ArrayList<City> myCityList = new ArrayList<>();
//                JSONArray arrCity = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_CITY_LIST);
//                for (int indexCity = 0; indexCity < arrCity.length(); indexCity++) {
//                    JSONObject joCityList = arrCity.getJSONObject(indexCity);
//                    City cl = new City();
//                    cl.setCityId(Utils.getIntegerFromJsonObjectWithKey(joCityList, JsonKey.EDIT_ADMINISTRATION_CITY_ID));
//                    cl.setCountry(Utils.getStringFromJsonObjectWithKey(joCityList, JsonKey.EDIT_ADMINISTRATION_COUNTRY));
//                    cl.setCityName(Utils.getStringFromJsonObjectWithKey(joCityList, JsonKey.EDIT_ADMINISTRATION_CITY_NAME));
//                    cl.setZoneCode(Utils.getStringFromJsonObjectWithKey(joCityList, JsonKey.EDIT_ADMINISTRATION_ZONE_CODE));
//                    myCityList.add(cl);
//                }
//                dl.setCityList(myCityList);

                JSONObject joDefaultCity = joDataList.getJSONObject(JsonKey.EDIT_ADMINISTRATION_DEFAULT_CITY);
                DefaultCity defaultCity = new DefaultCity();
                defaultCity.setCityId(Utils.getIntegerFromJsonObjectWithKey(joDefaultCity, JsonKey.EDIT_ADMINISTRATION_CITY_ID));
                defaultCity.setCountry(Utils.getStringFromJsonObjectWithKey(joDefaultCity, JsonKey.EDIT_ADMINISTRATION_COUNTRY));
                defaultCity.setCityName(Utils.getStringFromJsonObjectWithKey(joDefaultCity, JsonKey.EDIT_ADMINISTRATION_CITY_NAME));
                defaultCity.setZoneCode(Utils.getStringFromJsonObjectWithKey(joDefaultCity, JsonKey.EDIT_ADMINISTRATION_ZONE_CODE));
                dl.setDefaultCity(defaultCity);


                ArrayList<Currency> myCurrencyList = new ArrayList<>();
                JSONArray arrCurrency = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_CURRENCY_LIST);
                for (int i = 0; i < arrCurrency.length(); i++) {
                    JSONObject joCurrency = arrCurrency.getJSONObject(i);
                    Currency c = new Currency();
                    c.setCurrency(Utils.getStringFromJsonObjectWithKey(joCurrency, JsonKey.EDIT_ADMINISTRATION_CURRENCY));
                    c.setCurrId(Utils.getIntegerFromJsonObjectWithKey(joCurrency, JsonKey.EDIT_ADMINISTRATION_CURR_ID));
                    myCurrencyList.add(c);
                }
                dl.setCurrencyList(myCurrencyList);

                JSONObject joDefaultCurrency = joDataList.getJSONObject(JsonKey.EDIT_ADMINISTRATION_DEFAULT_CURRENCY);
                DefaultCurrency dc = new DefaultCurrency();
                dc.setDefaultCurrId(Utils.getIntegerFromJsonObjectWithKey(joDefaultCurrency, JsonKey.EDIT_ADMINISTRATION_D_CURR_ID));
                dc.setDefaultCurrency(Utils.getStringFromJsonObjectWithKey(joDefaultCurrency, JsonKey.EDIT_ADMINISTRATION_D_CURRENCY));
                dl.setDefaultCurrency(dc);

                dl.setLogoutAfter(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_LOGOUT_AFTER));
                dl.setLogoutAfterStatus(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_LOGOUT_AFTER_STATUS));
                dl.setSendReserveMessage(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_SEND_RESERVE_MESSAGE));
                dl.setDefaultOneTeeStartOnly(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_DEFAULT_ONE_TEE_START_ONLY));
                dl.setDefaultPaymentPattern(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_DEFAULT_PAYMENT_PATTERN));
                dl.setSalesTaxExcluding(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_SALES_TAX_EXCLUDING));
                dl.setSalesTax(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_SALES_TAX));
                dl.setUnit(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_UNIT));
                dl.setFirstDay(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_FIRST_DAY));
                dl.setCaddieFee(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_CADDIE_FEE));
                dl.setShowSalesTax(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_SHOW_SALES_TAX));
                dl.setDateFormat(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.EDIT_ADMINISTRATION_DATE_FORMAT));
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
