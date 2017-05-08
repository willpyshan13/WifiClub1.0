/**
 * Project Name: itee
 * File Name:	 JsonGreenFeeList.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-27
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
 * ClassName:JsonGreenFeeList <br/>
 * Function: entity of api greenFeeList. <br/>
 * Date: 2015-03-27 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonGreenFeeList extends BaseJsonObject implements Serializable {

    public JsonGreenFeeList(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new ArrayList<>();
            try {
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    JSONObject joGreenFee = arrDataList.getJSONObject(i);
                    GreenFee gf = new GreenFee();
                    gf.greenFeeId = Utils.getIntegerFromJsonObjectWithKey(joGreenFee, JsonKey.CUSTOMER_11_GREEN_FEE_ID);
                    gf.greenFeeType = Utils.getIntegerFromJsonObjectWithKey(joGreenFee, JsonKey.CUSTOMER_11_GREEN_FEE_TYPE);
                    gf.areaName = Utils.getStringFromJsonObjectWithKey(joGreenFee, JsonKey.CUSTOMER_11_AREA_NAME);
                    gf.greenFee = Utils.getStringFromJsonObjectWithKey(joGreenFee, JsonKey.CUSTOMER_11_GREEN_FEE);
                    gf.moneyType = Utils.getStringFromJsonObjectWithKey(joGreenFee, JsonKey.CUSTOMER_11_MONEY_TYPE);
                    dataList.add(gf);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    private ArrayList<GreenFee> dataList;

    public ArrayList<GreenFee> getDataList() {
        return dataList;
    }

    public static class GreenFee implements Serializable {
        private int greenFeeId;
        private int greenFeeType;
        private String areaName;
        private String greenFee;
        private String moneyType;

        public int getGreenFeeId() {
            return greenFeeId;
        }

        public int getGreenFeeType() {
            return greenFeeType;
        }

        public String getAreaName() {
            return areaName;
        }

        public String getGreenFee() {
            return greenFee;
        }

        public String getMoneyType() {
            return moneyType;
        }
    }
}