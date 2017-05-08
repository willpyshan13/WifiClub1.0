/**
 * Project Name: itee
 * File Name:	 JsonProductSaleAnalysis.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-08-11
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonProductSaleAnalysis <br/>
 * Function: entity of ProductSaleAnalysis. <br/>
 * Date: 2015-08-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonProductSaleAnalysis extends BaseJsonObject {

    private String totalIn;
    private ArrayList<TotalItem> totalList;
    private ArrayList<TotalSubItem> totalSubDataList;

    public JsonProductSaleAnalysis(JSONObject jsonObject) {
        super(jsonObject);
    }

    public String getTotalIn() {
        return totalIn;
    }

    public ArrayList<TotalItem> getTotalList() {
        return totalList;
    }

    public ArrayList<TotalSubItem> getTotalSubDataList() {
        return totalSubDataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        if (jsonObj != null) {
            Utils.log(jsonObj.toString());
            totalList = new ArrayList<>();
            totalSubDataList = new ArrayList<>();
            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                totalIn = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_IN);
                JSONArray arrTotalList = joDataList.getJSONArray(JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_LIST);
                for (int i = 0; i < arrTotalList.length(); i++) {
                    TotalItem totalItem = new TotalItem();
                    JSONObject joTotalItem = arrTotalList.getJSONObject(i);
                    totalItem.totalType = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_LIST_TOTAL_TYPE);
                    totalItem.totalAmt = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_LIST_TOTAL_AMT);
                    totalItem.totalPercent = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_LIST_TOTAL_PERCENT);
                    totalItem.totalTypeID = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_LIST_TOTAL_TYPE_ID);
                    totalItem.totalTypeColor = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_LIST_TOTAL_TYPE_COLOR);
                    totalList.add(totalItem);
                }

                JSONArray arrTotalSubDataList = joDataList.getJSONArray(JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_SUB_DATA);
                for (int i = 0; i < arrTotalSubDataList.length(); i++) {
                    TotalSubItem subItem = new TotalSubItem();
                    JSONObject joTotalSubData = arrTotalSubDataList.getJSONObject(i);
                    subItem.totalSubType = Utils.getStringFromJsonObjectWithKey(joTotalSubData, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_SUB_TYPE);
                    subItem.totalSubTypeAmt = Utils.getStringFromJsonObjectWithKey(joTotalSubData, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_SUB_TYPE_AMT);
                    JSONArray arrSubTypes = joTotalSubData.getJSONArray(JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_SUB_TYPE_LIST);
                    for (int j = 0; j < arrSubTypes.length(); j++) {
                        JSONObject joSubType = arrSubTypes.getJSONObject(j);
                        TotalSubItem.TotalSubItemTypeData subItemTypeData = new TotalSubItem.TotalSubItemTypeData();
                        subItemTypeData.memberType = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_SUB_TYPE_LIST_MEM_TYPE);
                        subItemTypeData.memberTypeID = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_SUB_TYPE_LIST_MEM_TYPE_ID);
                        subItemTypeData.memberTypeAmt = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_SUB_TYPE_LIST_MEM_TYPE_AMT);
                        subItemTypeData.memberTypePercent = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_SUB_TYPE_LIST_MEM_TYPE_PERCENT);
                        subItemTypeData.memberTypeColor = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.PRODUCT_SALE_ANALYSIS_TOTAL_SUB_TYPE_LIST_MEM_TYPE_COLOR);
                        subItem.getTypeList().add(subItemTypeData);
                    }
                    totalSubDataList.add(subItem);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static class TotalSubItem {
        private String totalSubType;
        private String totalSubTypeAmt;
        private ArrayList<TotalSubItemTypeData> typeList;

        public TotalSubItem() {
            typeList = new ArrayList<>();
        }

        public String getTotalSubType() {
            return totalSubType;
        }

        public String getTotalSubTypeAmt() {
            return totalSubTypeAmt;
        }

        public ArrayList<TotalSubItemTypeData> getTypeList() {
            return typeList;
        }

        public static class TotalSubItemTypeData {
            private String memberType;
            private String memberTypeID;
            private String memberTypeAmt;
            private String memberTypePercent;
            private String memberTypeColor;

            public String getMemberType() {
                return memberType;
            }

            public String getMemberTypeID() {
                return memberTypeID;
            }

            public String getMemberTypeAmt() {
                return memberTypeAmt;
            }

            public String getMemberTypePercent() {
                return memberTypePercent;
            }

            public String getMemberTypeColor() {
                return memberTypeColor;
            }
        }
    }

    public static class TotalItem {
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