/**
 * Project Name: itee
 * File Name:	 JsonCustomerAnalysis.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-08-27
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
 * ClassName:JsonCustomerAnalysis <br/>
 * Function: entity of CustomerAnalysis. <br/>
 * Date: 2015-08-27 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonCustomerAnalysis extends BaseJsonObject {

    private String totalIn;
    private String totalQty;
    private String totalAvg;
    private String totalPay;
    private ArrayList<TotalItem> totalList;
    private ArrayList<PayItem> payList;
    private ArrayList<CompareItem> compareList;

    public JsonCustomerAnalysis(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            totalList = new ArrayList<>();
            payList = new ArrayList<>();
            compareList = new ArrayList<>();

            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);

                totalIn = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_ANALYSIS_TOTAL_IN);
                totalQty = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_ANALYSIS_TOTAL_QTY);
                totalAvg = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_ANALYSIS_TOTAL_AVG);
                totalPay = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.CUSTOMER_ANALYSIS_TOTAL_PAY);

                JSONArray arrCompareList = joDataList.getJSONArray(JsonKey.CUSTOMER_ANALYSIS_COMPARE_LIST);
                for (int i = 0; i < arrCompareList.length(); i++) {
                    CompareItem compareItem = new CompareItem();
                    JSONObject joCompareItem = arrCompareList.getJSONObject(i);
                    compareItem.compareType = Utils.getStringFromJsonObjectWithKey(joCompareItem, JsonKey.CUSTOMER_ANALYSIS_COMPARE_LIST_COMPARE_TYPE);
                    compareItem.compareResult = Utils.getStringFromJsonObjectWithKey(joCompareItem, JsonKey.CUSTOMER_ANALYSIS_COMPARE_LIST_COMPARE_RESULT);
                    compareItem.compareVisible = Utils.getStringFromJsonObjectWithKey(joCompareItem, JsonKey.CUSTOMER_ANALYSIS_COMPARE_LIST_COMPARE_VISIBLE);
                    compareList.add(compareItem);
                }

                JSONArray arrPayList = joDataList.getJSONArray(JsonKey.CUSTOMER_ANALYSIS_PAY_LIST);
                for (int i = 0; i < arrPayList.length(); i++) {
                    PayItem payItem = new PayItem();
                    JSONObject joPayItem = arrPayList.getJSONObject(i);
                    payItem.payment = Utils.getStringFromJsonObjectWithKey(joPayItem, JsonKey.CUSTOMER_ANALYSIS_PAY_LIST_PAYMENT);
                    payItem.paymentId = Utils.getStringFromJsonObjectWithKey(joPayItem, JsonKey.CUSTOMER_ANALYSIS_PAY_LIST_PAYMENT_ID);
                    payItem.paymentAmt = Utils.getStringFromJsonObjectWithKey(joPayItem, JsonKey.CUSTOMER_ANALYSIS_PAY_LIST_PAYMENT_AMT);
                    payList.add(payItem);
                }

                JSONArray arrTotalList = joDataList.getJSONArray(JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST);
                for (int totalIdx = 0; totalIdx < arrTotalList.length(); totalIdx++) {
                    TotalItem totalItem = new TotalItem();
                    JSONObject joTotalItem = arrTotalList.getJSONObject(totalIdx);
                    totalItem.memType = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_MEM_TYPE);
                    totalItem.memTypeId = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_MEM_TYPE_ID);
                    totalItem.memQty = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_MEM_QTY);
                    totalItem.memAmt = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_MEM_AMT);
                    totalItem.memPercent = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_MEM_PERCENT);
                    totalItem.memAvg = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_MEM_AVG);
                    totalItem.memColor = Utils.getStringFromJsonObjectWithKey(joTotalItem, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_MEM_COLOR);

                    if (joTotalItem.has(JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_TYPE_LIST)) {
                        JSONArray arrSubTypeList = joTotalItem.getJSONArray(JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_TYPE_LIST);
                        for (int subIdx = 0; subIdx < arrSubTypeList.length(); subIdx++) {
                            TotalItem.TypeItem typeItem = new TotalItem.TypeItem();
                            JSONObject joSubType = arrSubTypeList.getJSONObject(subIdx);
                            typeItem.memSubType = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_TYPE_LIST_SUB_TYPE);
                            typeItem.memSubTypeId = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_TYPE_LIST_SUB_TYPE_ID);
                            typeItem.memSubTypeQty = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_TYPE_LIST_SUB_TYPE_QTY);
                            typeItem.memSubTyPercent = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_TYPE_LIST_SUB_PERCENT);
                            typeItem.memSubTypeAmt = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_TYPE_LIST_SUB_TYPE_AMT);
                            typeItem.memSubTypeColor = Utils.getStringFromJsonObjectWithKey(joSubType, JsonKey.CUSTOMER_ANALYSIS_TOTAL_LIST_TYPE_LIST_SUB_TYPE_COLOR);
                            totalItem.getTypeList().add(typeItem);
                        }
                    }
                    totalList.add(totalItem);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String getTotalIn() {
        return totalIn;
    }

    public String getTotalQty() {
        return totalQty;
    }

    public String getTotalAvg() {
        return totalAvg;
    }

    public String getTotalPay() {
        return totalPay;
    }

    public ArrayList<TotalItem> getTotalList() {
        return totalList;
    }

    public ArrayList<PayItem> getPayList() {
        return payList;
    }

    public ArrayList<CompareItem> getCompareList() {
        return compareList;
    }

    public static class CompareItem implements Serializable {
        private String compareType;
        private String compareResult;
        private String compareVisible;

        public String getCompareType() {
            return compareType;
        }

        public String getCompareResult() {
            return compareResult;
        }

        public String getCompareVisible() {
            return compareVisible;
        }
    }

    public static class PayItem implements Serializable {
        private String payment;
        private String paymentId;
        private String paymentAmt;

        public String getPayment() {
            return payment;
        }

        public String getPaymentId() {
            return paymentId;
        }

        public String getPaymentAmt() {
            return paymentAmt;
        }
    }

    public static class TotalItem implements Serializable {

        private String memType;
        private String memTypeId;
        private String memQty;
        private String memAmt;
        private String memPercent;
        private String memAvg;
        private String memColor;
        private ArrayList<TypeItem> typeList;

        public TotalItem() {
            typeList = new ArrayList<>();
        }

        public String getMemType() {
            return memType;
        }

        public String getMemTypeId() {
            return memTypeId;
        }

        public String getMemQty() {
            return memQty;
        }

        public String getMemAmt() {
            return memAmt;
        }

        public String getMemPercent() {
            return memPercent;
        }

        public String getMemAvg() {
            return memAvg;
        }

        public String getMemColor() {
            return memColor;
        }

        public ArrayList<TypeItem> getTypeList() {
            return typeList;
        }

        public static class TypeItem implements Serializable {
            private String memSubType;
            private String memSubTypeId;
            private String memSubTypeQty;
            private String memSubTypeAmt;
            private String memSubTyPercent;
            private String memSubTypeColor;

            public String getMemSubType() {
                return memSubType;
            }

            public String getMemSubTypeId() {
                return memSubTypeId;
            }

            public String getMemSubTypeQty() {
                return memSubTypeQty;
            }

            public String getMemSubTypeAmt() {
                return memSubTypeAmt;
            }

            public String getMemSubTyPercent() {
                return memSubTyPercent;
            }

            public String getMemSubTypeColor() {
                return memSubTypeColor;
            }
        }

    }
}