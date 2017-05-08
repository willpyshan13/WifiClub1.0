/**
 * Project Name: itee
 * File Name:	 JsonShoppingAaReturnData.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-05-06
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonShoppingAaReturnData <br/>
 * Function: PurchaseAaPost 实体类 <br/>
 * UI:  06-03
 * Date: 2015-05-06 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonShoppingAaReturnData extends BaseJsonObject implements Serializable {

    private ArrayList<JsonShoppingPaymentGet.DataItem> dataList;//AA
    private String bookingNo;
    private String player;

    public ArrayList<JsonShoppingPaymentGet.DataItem> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<JsonShoppingPaymentGet.DataItem> dataList) {
        this.dataList = dataList;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public JsonShoppingAaReturnData(JSONObject jsonObject) {
        super(jsonObject);
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList<JsonShoppingPaymentGet.DataItem>();
        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);

            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject jsonObject = joDataList.getJSONObject(i);
                JSONArray joAaList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_AA_LIST);
                JsonShoppingPaymentGet.DataItem dataItem = new JsonShoppingPaymentGet.DataItem();


                dataItem.setPlayerName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PLAYER));
                dataItem.setBookingNo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_BOOKING_NO));

                //aaList
                ArrayList<JsonShoppingPaymentGet.FiftyData> fiftyDataList = new ArrayList<JsonShoppingPaymentGet.FiftyData>();
                for (int aaIndex = 0; aaIndex < joAaList.length(); aaIndex++) {
                    JSONObject jsonProObject = joAaList.getJSONObject(aaIndex);

                    String aaCode = Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_AA_CODE);

                    JsonShoppingPaymentGet.FiftyData fiftyData = findFiftyDataOfAaCode(aaCode, fiftyDataList);
                    if (fiftyData == null) {
                        fiftyData = new JsonShoppingPaymentGet.FiftyData();
                        fiftyData.setPlayer(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PLAYER));
                        fiftyData.setPayStatus(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PAY_STATUS));
                        fiftyData.setDiscountPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_DISCOUNT_PRICE));
                        fiftyData.setAACode(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_AA_CODE));
                        fiftyData.setAAWith(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_AA_WITH));
                        fiftyData.setAaTotalPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_AA_TOTAL_PRICE));
                        ArrayList<JsonShoppingPaymentGet.ProData> aaList = new ArrayList<JsonShoppingPaymentGet.ProData>();
                        JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                        proData.setQty(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_QTY));
                        proData.setDisCountPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_DISCOUNT_PRICE));
                        proData.setProductName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRODUCT_NAME));
                        proData.setAttriName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_NAME));
                        proData.setAttriId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_ID));
                        proData.setId(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ID));
                        proData.setOwnerStatus(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_OWNER_STATUS));
                        proData.setProductId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRODUCT_ID));
                        aaList.add(proData);
                        fiftyData.setAaList(aaList);
                        fiftyDataList.add(fiftyData);
                    } else {

                        JsonShoppingPaymentGet.ProData proData = new JsonShoppingPaymentGet.ProData();
                        proData.setQty(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_QTY));
                        proData.setDisCountPrice(Utils.getDoubleFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_DISCOUNT_PRICE));
                        proData.setProductName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRODUCT_NAME));
                        proData.setAttriName(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_NAME));
                        proData.setAttriId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ATTRI_ID));
                        proData.setId(Utils.getIntegerFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_ID));
                        proData.setOwnerStatus(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_OWNER_STATUS));
                        proData.setProductId(Utils.getStringFromJsonObjectWithKey(jsonProObject, JsonKey.SHOPPING_PRODUCT_ID));
                        fiftyData.getAaList().add(proData);

                    }

                }


                dataItem.setFiftyDataList(fiftyDataList);

                dataList.add(dataItem);


            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }

    private JsonShoppingPaymentGet.FiftyData findFiftyDataOfAaCode(String code, List<JsonShoppingPaymentGet.FiftyData> fiftyDataList) {

        for (JsonShoppingPaymentGet.FiftyData fiftyData : fiftyDataList) {
            if (code.equals(fiftyData.getAACode())) {
                return fiftyData;
            }

        }
        return null;

    }

}
