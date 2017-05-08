/**
 * Project Name: itee
 * File Name:	 JsonMemberEditList.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-25
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonMemberEditList <br/>
 * Function: entity of api memberEditList. <br/>
 * Date: 2015-03-25 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonMemberEditList extends BaseJsonObject implements Serializable {

    private String memberName;
    private String memberValidity;
    private int annualFee;
    private String annualPrice;
    private int guestNum;
    private int checkIn;
    private String overdraft;
    private String timeLimit;
    private String ageStart;
    private String ageEnd;
    private String week;
    private String endType;
    private String endDate;
    private ArrayList<Fee> feeList;

    private int signGuest;

    public int getSignGuest() {
        return signGuest;
    }

    public void setSignGuest(int signGuest) {
        this.signGuest = signGuest;
    }

    public JsonMemberEditList(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                memberName = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_MEMBER_NAME);
                memberValidity = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_MEMBER_VALIDITY);
                annualFee = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_ANNUAL_FEE);
                annualPrice = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_ANNUAL_PRIZE);
                guestNum = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_GUEST);
                checkIn = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_CHECK_IN);
                overdraft = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_OVERDRAFT);
                timeLimit = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_TIME_LIMIT);
                ageStart = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_AGE_START);
                ageEnd = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_AGE_END);
                week = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_WEEK);
                endType = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_END_TYPE);
                endDate = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_END_DATE);

                setSignGuest(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_SIGN_GUEST));

                feeList = new ArrayList<>();
                JSONArray arrFeeList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_11_FEE_LIST);
                for (int i = 0; i < arrFeeList.length(); i++) {
                    try {
                        JSONObject joFee = arrFeeList.getJSONObject(i);
                        Fee fee = new Fee();
                        fee.greenFeeId = Utils.getIntegerFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_GREEN_FEE_ID);
                        fee.ftgfId = Utils.getStringFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_GREEN_FEE_FTGF_ID);
                        fee.greenFee = Utils.getStringFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_GREEN_FEE);
                        fee.areaName = Utils.getStringFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_AREA_NAME);
                        fee.memberDiscount = Utils.getStringFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_MEMBER_DISCOUNT);
                        fee.memberDiscountMoney = Utils.getStringFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_MEMBER_DISCOUNT_MONEY);
                        fee.memberNowCost = Utils.getStringFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_MEMBER_NOW_COST);
                        fee.guestDiscount = Utils.getStringFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_GUEST_DISCOUNT);
                        fee.guestDiscountMoney = Utils.getStringFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_GUEST_DISCOUNT_MONEY);
                        fee.guestNowCost = Utils.getStringFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_GUEST_NOW_COST);
                        fee.memberMoneyDefault = Utils.getIntegerFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_MEMBER_MONEY_DEFAULT);
                        fee.guestMoneyDefault = Utils.getIntegerFromJsonObjectWithKey(joFee, JsonKey.CUSTOMER_11_GUEST_MONEY_DEFAULT);
                        feeList.add(fee);
                    } catch (JSONException e) {
                        Utils.log(e.getMessage());
                    }
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public String getMemberName() {
        return memberName;
    }

    public String getMemberValidity() {
        return memberValidity;
    }

    public int getAnnualFee() {
        return annualFee;
    }

    public String getAnnualPrice() {
        return annualPrice;
    }

    public int getGuestNum() {
        return guestNum;
    }

    public int getCheckIn() {
        return checkIn;
    }

    public String getOverdraft() {
        return overdraft;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public String getAgeStart() {
        return ageStart;
    }

    public String getAgeEnd() {
        return ageEnd;
    }

    public String getWeek() {
        return week;
    }

    public String getEndType() {
        return endType;
    }

    public String getEndDate() {
        return endDate;
    }

    public ArrayList<Fee> getFeeList() {
        return feeList;
    }

    public static class Fee implements Serializable {

        private Integer greenFeeId;
        private String ftgfId;
        private String greenFee;
        private String areaName;
        private String memberDiscount;
        private String memberDiscountMoney;
        private String memberNowCost;
        private int memberMoneyDefault;
        private String guestDiscount;
        private String guestDiscountMoney;
        private String guestNowCost;
        private int guestMoneyDefault;

        public Integer getGreenFeeId() {
            return greenFeeId;
        }

        public String getAreaName() {
            return areaName;
        }

        public String getMemberDiscount() {
            return memberDiscount;
        }

        public String getMemberDiscountMoney() {
            return memberDiscountMoney;
        }

        public String getMemberNowCost() {
            return memberNowCost;
        }

        public String getGuestDiscount() {
            return guestDiscount;
        }

        public String getGuestDiscountMoney() {
            return guestDiscountMoney;
        }

        public String getGuestNowCost() {
            return guestNowCost;
        }

        public String getGreenFee() {
            return greenFee;
        }

        public int getMemberMoneyDefault() {
            return memberMoneyDefault;
        }

        public int getGuestMoneyDefault() {
            return guestMoneyDefault;
        }

        public String getFtgfId() {
            return ftgfId;
        }
    }
}