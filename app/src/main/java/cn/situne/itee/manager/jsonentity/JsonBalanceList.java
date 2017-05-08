/**
 * Project Name: itee
 * File Name:  JsonBookingGoodsList.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonBookingGoodsList <br/>
 * Function: Set Booking Goods List. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */
public class JsonBalanceList extends BaseJsonObject implements Serializable {

    private static final long serialVersionUID = -1529846374946979809L;

    public JsonBalanceList(JSONObject jsonObject) {
        super(jsonObject);
    }

    private DataList dataList;

    public static class DataList implements Serializable {

        private String account;

        private Double balance;
        private String customId;

        public String getCustomId() {
            return customId;
        }

        public void setCustomId(String customId) {
            this.customId = customId;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public Double getBalance() {
            return balance;
        }

        public void setBalance(Double balance) {
            this.balance = balance;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);

            dataList = new DataList();
            dataList.setAccount(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SHOPPING_CONFIRM_PAY_ACCOUNT));
            dataList.setBalance(Utils.getDoubleFromJsonObjectWithKey(joDataList, JsonKey.SHOPPING_CONFIRM_PAY_BALANCE));
            dataList.setCustomId(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SHOPPING_CONFIRM_PAY_CUSTOM_ID));
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }
}
