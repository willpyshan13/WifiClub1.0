/**
 * Project Name: itee
 * File Name:  JsonPurchaseHistoryGet.java
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

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonPurchaseHistoryGet <br/>
 * Function: To get purchase history. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonPurchaseHistoryGet extends BaseJsonObject implements Serializable {

    private ArrayList<DataListItem> dataList;

    public JsonPurchaseHistoryGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    public ArrayList<DataListItem> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<DataListItem> dataList) {
        this.dataList = dataList;
    }

    public class DataListItem implements Serializable {

        private String payId;
        private String time;
        private String amount;
        private Integer payStatus;
        private String refundFlag;

        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
            this.payId = payId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

        public String getRefundFlag() {
            return refundFlag;
        }

        public void setRefundFlag(String refundFlag) {
            this.refundFlag = refundFlag;
        }
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList<>();
        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);

            for (int i = 0; i < joDataList.length(); i++) {

                JSONObject joDataListItem = joDataList.getJSONObject(i);
                DataListItem dataListItem = new DataListItem();
                dataListItem.setPayId(Utils.getStringFromJsonObjectWithKey(joDataListItem, JsonKey.PLAYER_PURCHASE_HISTORY_PAY_ID));
                dataListItem.setTime(Utils.getStringFromJsonObjectWithKey(joDataListItem, JsonKey.PLAYER_PURCHASE_HISTORY_TIME));
                dataListItem.setAmount(Utils.getStringFromJsonObjectWithKey(joDataListItem, JsonKey.PLAYER_PURCHASE_HISTORY_AMOUNT));
                dataListItem.setPayStatus(Utils.getIntegerFromJsonObjectWithKey(joDataListItem, JsonKey.PLAYER_PURCHASE_HISTORY_PAY_STATUS));
                dataListItem.setRefundFlag(Utils.getStringFromJsonObjectWithKey(joDataListItem, JsonKey.PLAYER_PURCHASE_HISTORY_REFUND_FLAG));
                dataList.add(dataListItem);
            }


        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
