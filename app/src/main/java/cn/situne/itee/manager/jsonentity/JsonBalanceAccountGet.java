/**
 * Project Name: itee
 * File Name:  JsonBalanceAccountGet.java
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
 * ClassName:JsonBalanceAccountGet <br/>
 * Function: Get balance Account. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonBalanceAccountGet extends BaseJsonObject implements Serializable {

    private DataList dataList;

    public JsonBalanceAccountGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }


    public class DataList implements Serializable {

        private String balanceAccount;
        private String account;
        private String password;
        private String prePay;
        private String overDraft;
        private Integer timeLimit;
        private List<RecordsItem> records;


        public List<RecordsItem> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsItem> records) {
            this.records = records;
        }

        public String getBalanceAccount() {
            return balanceAccount;
        }

        public void setBalanceAccount(String balanceAccount) {
            this.balanceAccount = balanceAccount;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPrePay() {
            return prePay;
        }

        public void setPrePay(String prePay) {
            this.prePay = prePay;
        }

        public String getOverDraft() {
            return overDraft;
        }

        public void setOverDraft(String overDraft) {
            this.overDraft = overDraft;
        }

        public Integer getTimeLimit() {
            return timeLimit;
        }

        public void setTimeLimit(Integer timeLimit) {
            this.timeLimit = timeLimit;
        }


    }


    public class RecordsItem implements Serializable {

        private String date;
        private String amount;
        private Integer payStatus;

        private Integer refundFlg;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public Integer getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(Integer payStatus) {
            this.payStatus = payStatus;
        }

        public Integer getRefundFlg() {
            return refundFlg;
        }

        public void setRefundFlg(Integer refundFlg) {
            this.refundFlg = refundFlg;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.setBalanceAccount(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_BALANCE_ACCOUNT));
            dataList.setAccount(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_BALANCE_ACCOUNT_ACCOUNT));
            dataList.setPassword(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_BALANCE_ACCOUNT_PASSWORD));
            dataList.setPrePay(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_BALANCE_ACCOUNT_PRE_PAY));
            dataList.setOverDraft(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_BALANCE_ACCOUNT_OVERDRAFT));
            dataList.setTimeLimit(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_BALANCE_ACCOUNT_TIME_LIMIT));
            List<RecordsItem> memberTypeList = new ArrayList<>();
            JSONArray jaTypeList = Utils.getArrayFromJsonObjectWithKey
                    (joDataList, JsonKey.PLAYER_BALANCE_ACCOUNT_RECORDS_LIST);
            for (int i = 0; i < jaTypeList.length(); i++) {

                JSONObject joType = jaTypeList.getJSONObject(i);
                RecordsItem listItem = new RecordsItem();
                listItem.setDate(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_BALANCE_ACCOUNT_DATE));
                listItem.setAmount(Utils.getStringFromJsonObjectWithKey(joType, JsonKey.PLAYER_BALANCE_ACCOUNT_AMOUNT));
                listItem.setPayStatus(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_BALANCE_ACCOUNT_PAY_STATUS));
                listItem.setRefundFlg(Utils.getIntegerFromJsonObjectWithKey(joType, JsonKey.PLAYER_BALANCE_ACCOUNT_REFUND_FLG));
                memberTypeList.add(listItem);
            }
            dataList.setRecords(memberTypeList);

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
