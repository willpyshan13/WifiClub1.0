/**
 * Project Name: itee
 * File Name:  JsonAgentsPricingGet.java
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

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonAgentsPricingGet <br/>
 * Function: Get Json agents pricing. <br/>
 * Date: 2015-03-21 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */
public class JsonAgentsPricingGet extends BaseJsonObject implements Serializable {

    private List<PricingTime> agentDataList;

    private String timeAct;
    private String timeNumber;
    private String timeQty;


    private String type;
    private String typeId;
    private String dateInfo;
    private String timeInfo;

    private String dateDesc;

    public String getTimeQty() {
        return timeQty;
    }

    public void setTimeQty(String timeQty) {
        this.timeQty = timeQty;
    }

    public String getTimeNumber() {
        return timeNumber;
    }

    public void setTimeNumber(String timeNumber) {
        this.timeNumber = timeNumber;
    }

    public String getTimeAct() {
        return timeAct;
    }

    public void setTimeAct(String timeAct) {
        this.timeAct = timeAct;
    }

    public String getDateDesc() {
        return dateDesc;
    }

    public void setDateDesc(String dateDesc) {
        this.dateDesc = dateDesc;
    }

    public List<PricingTime> getAgentDataList() {
        return agentDataList;
    }

    public void setAgentDataList(List<PricingTime> agentDataList) {
        this.agentDataList = agentDataList;
    }

    public ArrayList<JsonCommonProduct> getPricingDataList() {
        return pricingDataList;
    }

    public void setPricingDataList(ArrayList<JsonCommonProduct> pricingDataList) {
        this.pricingDataList = pricingDataList;
    }

    private ArrayList<JsonCommonProduct> pricingDataList;


    public JsonAgentsPricingGet(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        agentDataList = new ArrayList<>();
        pricingDataList = new ArrayList<>();
        try {
            JSONObject dataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            setDateDesc(Utils.getStringFromJsonObjectWithKey(dataList, JsonKey.PRICING_DATE_DESC));

            setTimeAct(Utils.getStringFromJsonObjectWithKey(dataList, JsonKey.PRICING_TIMES_ACT));
            setTimeNumber(Utils.getStringFromJsonObjectWithKey(dataList, JsonKey.PRICING_TIMES_NUMBER));
            setTimeQty(Utils.getStringFromJsonObjectWithKey(dataList, JsonKey.PRICING_GUESTS_QTY));



            JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(dataList, JsonKey.PRICING_PRICING_DATA);
            JSONArray jsTimeInfoArray = Utils.getArrayFromJsonObjectWithKey(dataList, JsonKey.PRICING_TIME_INFO);
            for (int x = 0; x < jsTimeInfoArray.length(); x++) {
                try {
                    JSONObject jsonMemberDate = jsTimeInfoArray.getJSONObject(x);
                    PricingTime memberData = new PricingTime();
                    memberData.setStartTime(Utils.getStringFromJsonObjectWithKey(jsonMemberDate, JsonKey.PRICING_START_TIME));
                    memberData.setEndTime(Utils.getStringFromJsonObjectWithKey(jsonMemberDate, JsonKey.PRICING_END_TIME));
                    agentDataList.add(memberData);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
            }


                    //pricing Data
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject itemObject = jsonArray.getJSONObject(i);
                String packageId = Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.AGENT_PRICING_DATA_PACKAGE_ID);
                JsonCommonProduct item = new JsonCommonProduct();

                item.setPackageId(packageId);
                item.setProductId(Utils.getIntegerFromJsonObjectWithKey(itemObject, JsonKey.EVENT_PRODUCT_ID));
                item.setProductOriginalCost(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_PRODUCT_PRICE));
                item.setProductName(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.EVENT_PRODUCT_NAME));
                item.setProductAttr(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.COMMON_ATTRIBUTE_ID));
                item.setPricingId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_ID));
                item.setGuestProductDiscount(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_GUEST_DISCOUNT));
                item.setTypeTagId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.COMMON_TYPE_TAG_ID));
                item.setGuestDiscountType(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
                if (!Constants.STR_0.equals(packageId)){
                    ArrayList< JsonCommonProduct.PackageItem > packageItemList = new ArrayList<JsonCommonProduct.PackageItem >();
                    JSONArray jsPackageList = Utils.getArrayFromJsonObjectWithKey(itemObject, JsonKey.PRICING_PRODUCT_LIST);

                    for (int x = 0; x < jsPackageList.length(); x++) {
                        JSONObject jsPackageItem = jsPackageList.getJSONObject(x);
                        JsonCommonProduct.PackageItem packageItem = new JsonCommonProduct.PackageItem();
                        packageItem.setProductId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.EVENT_PRODUCT_ID));
                        packageItem.setPrice(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_PRICE));
                        packageItem.setPricingId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_ID));
                        packageItem.setGuestProductDiscount(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_DISCOUNT));
                        packageItem.setGuestProductDiscountType(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
                        packageItem.setProductName(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.EVENT_PRODUCT_NAME));
                        packageItem.setProductAttr(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.COMMON_ATTRIBUTE_ID));
                        packageItemList.add(packageItem);
                    }

                    item.setPackageItemList(packageItemList);
                }



                pricingDataList.add(item);
            }


        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

//    public class AgentData implements Serializable {
//
//        private int agentTimeId;
//        private String agentStartTime;
//
//        public int getAgentTimeId() {
//            return agentTimeId;
//        }
//
//        public void setAgentTimeId(int agentTimeId) {
//            this.agentTimeId = agentTimeId;
//        }
//
//        public String getAgentStartTime() {
//            return agentStartTime;
//        }
//
//        public void setAgentStartTime(String agentStartTime) {
//            this.agentStartTime = agentStartTime;
//        }
//
//        public String getAgent_endTime() {
//            return agent_endTime;
//        }
//
//        public void setAgent_endTime(String agent_endTime) {
//            this.agent_endTime = agent_endTime;
//        }
//
//        private String agent_endTime;
//    }



    public static class PricingTime implements Serializable {

        private int timeId;
        private String startTime;
        private String endTime;

        public int getTimeId() {
            return timeId;
        }

        public void setTimeId(int timeId) {
            this.timeId = timeId;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }
    }


}
