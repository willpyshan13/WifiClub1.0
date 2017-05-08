/**
 * Project Name: itee
 * File Name:  JsonEmailContent.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-05-31
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

/**
 * ClassName:JsonEmailContent <br/>
 * Function: Get email content. <br/>
 * Date: 2015-05-31 <br/>
 *
 * @author Administrator
 * @version 1.0
 * @see
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

public class JsonEmailContent extends BaseJsonObject implements Serializable {

    private DataList dataList;

    public JsonEmailContent(JSONObject jsonObject) {
        super(jsonObject);
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }


    public class DataList implements Serializable {


        private String memberTypeTitle;
        private String memberTypeContent;
        private String memberPerpoidTitle;
        private String memberPerpoidContent;
        private String arrearageTitle;
        private String arrearageContent;
        private String annualFeeTitle;
        private String feeDateTitle;
        private String feeTitle;
        private String expireDateTitle;
        private String payTypeTitle;

        private List<MessageItem> messageList;

        public String getAnnualFeeTitle() {
            return annualFeeTitle;
        }

        public void setAnnualFeeTitle(String annualFeeTitle) {
            this.annualFeeTitle = annualFeeTitle;
        }

        public String getArrearageContent() {
            return arrearageContent;
        }

        public void setArrearageContent(String arrearageContent) {
            this.arrearageContent = arrearageContent;
        }

        public String getArrearageTitle() {
            return arrearageTitle;
        }

        public void setArrearageTitle(String arrearageTitle) {
            this.arrearageTitle = arrearageTitle;
        }

        public String getExpireDateTitle() {
            return expireDateTitle;
        }

        public void setExpireDateTitle(String expireDateTitle) {
            this.expireDateTitle = expireDateTitle;
        }

        public String getFeeDateTitle() {
            return feeDateTitle;
        }

        public void setFeeDateTitle(String feeDateTitle) {
            this.feeDateTitle = feeDateTitle;
        }

        public String getFeeTitle() {
            return feeTitle;
        }

        public void setFeeTitle(String feeTitle) {
            this.feeTitle = feeTitle;
        }

        public String getMemberPerpoidContent() {
            return memberPerpoidContent;
        }

        public void setMemberPerpoidContent(String memberPerpoidContent) {
            this.memberPerpoidContent = memberPerpoidContent;
        }

        public String getMemberPerpoidTitle() {
            return memberPerpoidTitle;
        }

        public void setMemberPerpoidTitle(String memberPerpoidTitle) {
            this.memberPerpoidTitle = memberPerpoidTitle;
        }

        public String getMemberTypeContent() {
            return memberTypeContent;
        }

        public void setMemberTypeContent(String memberTypeContent) {
            this.memberTypeContent = memberTypeContent;
        }

        public String getMemberTypeTitle() {
            return memberTypeTitle;
        }

        public void setMemberTypeTitle(String memberTypeTitle) {
            this.memberTypeTitle = memberTypeTitle;
        }

        public List<MessageItem> getMessageList() {
            return messageList;
        }

        public void setMessageList(List<MessageItem> messageList) {
            this.messageList = messageList;
        }

        public String getPayTypeTitle() {
            return payTypeTitle;
        }

        public void setPayTypeTitle(String payTypeTitle) {
            this.payTypeTitle = payTypeTitle;
        }
    }


    public class MessageItem implements Serializable {

        private String feeDateContent;
        private String annualFeeContent;
        private String expireDateContent;
        private Integer payTypeContent;

        public String getAnnualFeeContent() {
            return annualFeeContent;
        }

        public void setAnnualFeeContent(String annualFeeContent) {
            this.annualFeeContent = annualFeeContent;
        }

        public String getExpireDateContent() {
            return expireDateContent;
        }

        public void setExpireDateContent(String expireDateContent) {
            this.expireDateContent = expireDateContent;
        }

        public String getFeeDateContent() {
            return feeDateContent;
        }

        public void setFeeDateContent(String feeDateContent) {
            this.feeDateContent = feeDateContent;
        }

        public Integer getPayTypeContent() {
            return payTypeContent;
        }

        public void setPayTypeContent(Integer payTypeContent) {
            this.payTypeContent = payTypeContent;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.setMemberTypeTitle(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_MEMBER_TYPE_TITLE));
            dataList.setMemberTypeContent(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_MEMBER_TYPE_CONTENT));
            dataList.setMemberPerpoidTitle(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_MEMBER_PERPOID_TITLE));
            dataList.setMemberPerpoidContent(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_MEMBER_PERPOID_CONTENT));

            dataList.setArrearageTitle(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_ARREARAGE_TITLE));
            dataList.setArrearageContent(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_ARREARAGE_CONTENT));
            dataList.setAnnualFeeTitle(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_ANNUAL_FEE_TITLE));
            dataList.setFeeDateTitle(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_FEE_DATE_TITLE));
            dataList.setFeeTitle(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_FEE_TITLE));
            dataList.setExpireDateTitle(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_EXPIRE_DATE_TITLE));
            dataList.setPayTypeTitle(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_PAY_TYPE_TITLE));


            List<MessageItem> memberTypeList = new ArrayList<>();
            JSONArray jaTypeList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PURCHASE_HISTORY_MESSAGE_LIST);
            for (int i = 0; i < jaTypeList.length(); i++) {

                JSONObject joType = jaTypeList.getJSONObject(i);
                MessageItem listItem = new MessageItem();
                listItem.setFeeDateContent(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_FEE_DATE_CONTENT));
                listItem.setAnnualFeeContent(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_ANNUAL_FEE_CONTENT));
                listItem.setExpireDateContent(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_EXPIRE_DATE_CONTENT));
                listItem.setPayTypeContent(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_PURCHASE_HISTORY_PAY_TYPE_CONTENT));


                memberTypeList.add(listItem);
            }
            dataList.setMessageList(memberTypeList);

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
