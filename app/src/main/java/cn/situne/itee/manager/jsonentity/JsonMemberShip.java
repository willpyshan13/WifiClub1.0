/**
 * Project Name: itee
 * File Name:  JsonMemberShip.java
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
 * ClassName:JsonMemberShip <br/>
 * Function: To set membership. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonMemberShip extends BaseJsonObject implements Serializable {

    private DataList dataList;

    public JsonMemberShip(JSONObject jsonObject) {
        super(jsonObject);
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }


    public class DataList implements Serializable {


        private String splitAmount;
        private Integer paymentId;
        private String paymentDate;
        private Integer confirmStatus;
        private String endDateOld;
        private String effectiveDate;
        private String effectiveEndDate;
        private String email;
        private String message;

        private List<MemberTypeListItem> memberTypeList;
        private ArrayList<MemberTimes> memberTimesList;

        public ArrayList<MemberTimes> getMemberTimesList() {
            return memberTimesList;
        }

        public void setMemberTimesList(ArrayList<MemberTimes> memberTimesList) {
            this.memberTimesList = memberTimesList;
        }

        public String getSplitAmount() {
            return splitAmount;
        }

        public void setSplitAmount(String splitAmount) {
            this.splitAmount = splitAmount;
        }

        public Integer getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(Integer paymentId) {
            this.paymentId = paymentId;
        }

        public String getPaymentDate() {
            return paymentDate;
        }

        public void setPaymentDate(String paymentDate) {
            this.paymentDate = paymentDate;
        }

        public Integer getConfirmStatus() {
            return confirmStatus;
        }

        public void setConfirmStatus(Integer confirmStatus) {
            this.confirmStatus = confirmStatus;
        }

        public String getEndDateOld() {
            return endDateOld;
        }

        public void setEndDateOld(String endDateOld) {
            this.endDateOld = endDateOld;
        }

        public String getEffectiveDate() {
            return effectiveDate;
        }

        public void setEffectiveDate(String effectiveDate) {
            this.effectiveDate = effectiveDate;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<MemberTypeListItem> getMemberTypeList() {
            return memberTypeList;
        }

        public void setMemberTypeList(List<MemberTypeListItem> memberTypeList) {
            this.memberTypeList = memberTypeList;
        }

        public String getEffectiveEndDate() {
            return effectiveEndDate;
        }

        public void setEffectiveEndDate(String effectiveEndDate) {
            this.effectiveEndDate = effectiveEndDate;
        }
    }

    public class MemberTimes implements Serializable {

        private String totalTimes;
        private String availTimes;
        private String pricingType;

        public String getPricingType() {
            return pricingType;
        }

        public void setPricingType(String pricingType) {
            this.pricingType = pricingType;
        }

        public String getTotalTimes() {
            return totalTimes;
        }

        public void setTotalTimes(String totalTimes) {
            this.totalTimes = totalTimes;
        }

        public String getAvailTimes() {
            return availTimes;
        }

        public void setAvailTimes(String availTimes) {
            this.availTimes = availTimes;
        }
    }
    public class MemberTypeListItem implements Serializable {

        private Integer memberTypeId;
        private String memberType;
        private Integer memberParentId;
        private String memberEndDate;
        private Integer memberPeriod;
        private Integer annualFeeStatus;
        private String annualFee;

        public Integer getMemberParentId() {
            return memberParentId;
        }

        public void setMemberParentId(Integer memberParentId) {
            this.memberParentId = memberParentId;
        }

        public Integer getMemberTypeId() {
            return memberTypeId;
        }

        public void setMemberTypeId(Integer memberTypeId) {
            this.memberTypeId = memberTypeId;
        }

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }

        public Integer getMemberPeriod() {
            return memberPeriod;
        }

        public void setMemberPeriod(Integer memberPeriod) {
            this.memberPeriod = memberPeriod;
        }

        public Integer getAnnualFeeStatus() {
            return annualFeeStatus;
        }

        public void setAnnualFeeStatus(Integer annualFeeStatus) {
            this.annualFeeStatus = annualFeeStatus;
        }

        public String getAnnualFee() {
            return annualFee;
        }

        public void setAnnualFee(String annualFee) {
            this.annualFee = annualFee;
        }

        public String getMemberEndDate() {
            return memberEndDate;
        }

        public void setMemberEndDate(String memberEndDate) {
            this.memberEndDate = memberEndDate;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.setSplitAmount(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_SPLIT_AMOUNT));
            dataList.setPaymentId(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_PAYMENT_ID));
            dataList.setPaymentDate(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_PAYMENT_DATE));
            dataList.setConfirmStatus(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_CONFIRM_STATUS));
            dataList.setEndDateOld(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_END_DATE_OLD));
            dataList.setEffectiveDate(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_EFFECTIVE_DATE));
            dataList.setEffectiveEndDate(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_EFFECTIVE_END_DATE));
            dataList.setEmail(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_EMAIL));
            dataList.setMessage(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_MESSAGE));
            List<MemberTypeListItem> memberTypeList = new ArrayList<>();
            JSONArray jaTypeList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_SHIP_MEMBER_TYPE_LIST);
            for (int i = 0; i < jaTypeList.length(); i++) {

                JSONObject joType = jaTypeList.getJSONObject(i);
                MemberTypeListItem listItem = new MemberTypeListItem();
                listItem.setMemberParentId(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_MEMBER_SHIP_MEMBER_PARENT_ID));
                listItem.setMemberTypeId(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_MEMBER_SHIP_MEMBER_TYPE_ID));
                listItem.setMemberType(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_MEMBER_SHIP_MEMBER_TYPE));
                listItem.setMemberPeriod(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_MEMBER_SHIP_MEMBER_PERIOD));
                listItem.setAnnualFeeStatus(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_MEMBER_SHIP_ANNUAL_FEE_STATUS));
                listItem.setAnnualFee(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_MEMBER_SHIP_ANNUAL_FEE));
                listItem.setMemberEndDate(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_MEMBER_SHIP_END_DATE));
                memberTypeList.add(listItem);
            }




            ArrayList<MemberTimes> memberTimesList = new ArrayList<>();
            JSONArray jsMemberTimes = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_MEMBER_PRICING_TIMES);

            for (int t = 0; t < jsMemberTimes.length(); t++) {
                MemberTimes memberTimes = new MemberTimes();
                JSONObject jsTimes = jsMemberTimes.getJSONObject(t);
                memberTimes.setAvailTimes(Utils.getStringFromJsonObjectWithKey(jsTimes, JsonKey.PLAYER_MEMBER_AVAIL_TIMES));
                memberTimes.setTotalTimes(Utils.getStringFromJsonObjectWithKey(jsTimes, JsonKey.PLAYER_MEMBER_TOTAL_TIMES));
                memberTimes.setPricingType(Utils.getStringFromJsonObjectWithKey(jsTimes, JsonKey.PLAYER_MEMBER_PRICING_TYPE));
                memberTimesList.add(memberTimes);
            }

            dataList.setMemberTimesList(memberTimesList);
            dataList.setMemberTypeList(memberTypeList);

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
