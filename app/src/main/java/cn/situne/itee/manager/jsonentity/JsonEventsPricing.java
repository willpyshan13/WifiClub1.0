/**
 * Project Name: itee
 * File Name:  JsonEventsPricing.java
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
 * ClassName:JsonEventsPricing <br/>
 * Function: To set events pricing. <br/>
 * Date: 2015-03-17 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonEventsPricing extends BaseJsonObject implements Serializable {


    private ArrayList<JsonCommonProduct> dataList;
    public ArrayList<JsonCommonProduct> getDataList() {
        return dataList;
    }
    public void setDataList(ArrayList<JsonCommonProduct> dataList) {
        this.dataList = dataList;
    }
    public JsonEventsPricing(JSONObject jsonObject) {
        super(jsonObject);
    }

    private String mainId;

    public String getMainId() {
        return mainId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new ArrayList<>();
        if (jsonObj != null) {
            try {

                JSONArray jsonDataList =    jsonObj.getJSONArray(JsonKey.COMMON_DATA_LIST);

                if (jsonDataList.length()>0){

                    JSONObject jsData = jsonDataList.getJSONObject(0);
                    JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(jsData, JsonKey.PRICING_PRICING_DATA);

                    setMainId(Utils.getStringFromJsonObjectWithKey(jsData, JsonKey.PRICING_MAIN_ID));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject itemObject = jsonArray.getJSONObject(i);
                        JsonCommonProduct item = new JsonCommonProduct();
                        item.setProductId(Utils.getIntegerFromJsonObjectWithKey(itemObject, JsonKey.EVENT_PRODUCT_ID));
                        item.setProductOriginalCost(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_PRODUCT_PRICE));
                        item.setProductName(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.EVENT_PRODUCT_NAME));
                        item.setProductAttr(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.COMMON_ATTRIBUTE_ID));

                      //  item.setPackageId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.AGENT_PRICING_DATA_PACKAGE_ID));
                        item.setPricingId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_ID));
                        item.setTypeTagId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_PRODUCT_SHOP_ID));
                        item.setGuestProductDiscount(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_GUEST_DISCOUNT));
                        item.setGuestDiscountType(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
                        dataList.add(item);
                    }


//                    JSONArray packageArray = Utils.getArrayFromJsonObjectWithKey(jsonDataList, JsonKey.PRICING_PACKAGE_DATA);
//                    for (int i = 0; i < packageArray.length(); i++) {
//                        JSONObject itemObject = packageArray.getJSONObject(i);
//                        JsonCommonProduct item = new JsonCommonProduct();
//                        item.setProductName(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.EVENT_PACKAGE_NAME));
//                        item.setPackageId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.EVENT_PACKAGE_ID));
//                        item.setProductOriginalCost(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.PRICING_COST_PRICE));
//                        item.setTypeTagId(Utils.getStringFromJsonObjectWithKey(itemObject, JsonKey.COMMON_TYPE_TAG_ID));
//                        JSONArray jsPackageList = Utils.getArrayFromJsonObjectWithKey(itemObject, JsonKey.PRICING_PRODUCT_LIST);
//                        ArrayList< JsonCommonProduct.PackageItem > packageItemList = new ArrayList<JsonCommonProduct.PackageItem >();
//                        for (int x = 0; x < jsPackageList.length(); x++) {
//                            JSONObject jsPackageItem = jsPackageList.getJSONObject(x);
//                            JsonCommonProduct.PackageItem packageItem = new JsonCommonProduct.PackageItem();
//                            packageItem.setProductId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.EVENT_PRODUCT_ID));
//                            packageItem.setPrice(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_COST_PRICE));
//                            packageItem.setPricingId(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_ID));
//                            packageItem.setGuestProductDiscount(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_DISCOUNT));
//                            packageItem.setGuestProductDiscountType(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.PRICING_GUEST_DISCOUNT_TYPE));
//                            packageItem.setProductName(Utils.getStringFromJsonObjectWithKey(jsPackageItem, JsonKey.EVENT_PRODUCT_NAME));
//                            packageItemList.add(packageItem);
//                        }
//
//                        item.setPackageItemList(packageItemList);
//                        dataList.add(item);
//                    }
                }



            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }
}




