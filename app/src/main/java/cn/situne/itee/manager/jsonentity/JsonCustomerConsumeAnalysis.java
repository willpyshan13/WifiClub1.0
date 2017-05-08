/**
 * Project Name: itee
 * File Name:	 JsonCustomerConsumeAnalysis.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-08-18
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonCustomerConsumeAnalysis <br/>
 * Function: entity of chart 4. <br/>
 * Date: 2015-08-18 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonCustomerConsumeAnalysis extends BaseJsonObject {

    private ArrayList<DataItem> dataList;

    private String changeTypeNum;

    public ArrayList<DataItem> getDataList() {
        return dataList;
    }

    public JsonCustomerConsumeAnalysis(JSONObject jsonObject) {
        super(jsonObject);
    }

    public String getChangeTypeNum() {
        return changeTypeNum;
    }

    public void setChangeTypeNum(String changeTypeNum) {
        this.changeTypeNum = changeTypeNum;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new ArrayList<>();
            Utils.log(jsonObj.toString());
            try {

                JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);

                JSONArray arrDataList = jsonObject.getJSONArray(JsonKey.CHART_DETAIL_LIST);


                setChangeTypeNum( Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.CHART_CHANGE_TYPENUM));
                for (int i = 0; i < arrDataList.length(); i++) {
                    DataItem dataItem = new DataItem();
                    JSONObject joDataItem = arrDataList.getJSONObject(i);

                    dataItem.memType = Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.CUSTOMER_CONSUME_ANALYSIS_MEM_TYPE);
                    dataItem.memTypeId = Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.CUSTOMER_CONSUME_ANALYSIS_MEM_TYPE_ID);
                    dataItem.memAmt = Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.CUSTOMER_CONSUME_ANALYSIS_MEM_AMT);
                    dataItem.memPernum = Utils.getStringFromJsonObjectWithKey(joDataItem, JsonKey.CUSTOMER_CONSUME_ANALYSIS_MEM_PERNUM);

                    JSONArray arrTotalList = joDataItem.getJSONArray(JsonKey.CUSTOMER_CONSUME_ANALYSIS_TOTAL_LIST);
                    for (int j = 0; j < arrTotalList.length(); j++) {
                        DataItem.TotalDataItem totalDataItem = new DataItem.TotalDataItem();
                        JSONObject joTotalItem = arrTotalList.getJSONObject(j);

                        totalDataItem.totalAmt = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_CONSUME_ANALYSIS_TOTAL_LIST_TOTAL_TYPE_AMT);
                        totalDataItem.totalType = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_CONSUME_ANALYSIS_TOTAL_LIST_TOTAL_TYPE);
                        totalDataItem.totalTypeID = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_CONSUME_ANALYSIS_TOTAL_LIST_TOTAL_TYPE_ID);
                        totalDataItem.totalTypeColor = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_CONSUME_ANALYSIS_TOTAL_LIST_TOTAL_TYPE_COLOR);
                        totalDataItem.totalPercent = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_CONSUME_ANALYSIS_TOTAL_LIST_TOTAL_TYPE_PERCENT);
                        dataItem.totalList.add(totalDataItem);
                    }

                    dataList.add(dataItem);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static class DataItem implements Serializable {

        private String memType;
        private String memTypeId;
        private String memPernum;
        private String memAmt;
        private ArrayList<TotalDataItem> totalList = new ArrayList<>();

        public String getMemType() {
            return memType;
        }

        public String getMemTypeId() {
            return memTypeId;
        }

        public String getMemPernum() {
            return memPernum;
        }

        public String getMemAmt() {
            return memAmt;
        }

        public ArrayList<TotalDataItem> getTotalList() {
            return totalList;
        }

        public static class TotalDataItem {
            private String totalType;
            private String totalAmt;
            private String totalPercent;
            private String totalTypeID;
            private String totalTypeColor;

            public String getTotalType() {
                return totalType;
            }

            public String getTotalAmt() {
                return totalAmt;
            }

            public String getTotalPercent() {
                return totalPercent;
            }

            public String getTotalTypeID() {
                return totalTypeID;
            }

            public String getTotalTypeColor() {
                return totalTypeColor;
            }
        }
    }

}