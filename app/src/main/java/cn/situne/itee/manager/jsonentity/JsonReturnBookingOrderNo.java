/**
 * Project Name: itee
 * File Name:  JsonNormalShop.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-23
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
 * ClassName:JsonNormalShop <br/>
 * Function: To set normal shop. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */

public class JsonReturnBookingOrderNo extends BaseJsonObject implements Serializable {


    private Member dataList;
    private String pricingDisplay;
    private ArrayList<String>bookingList;

    public ArrayList<String> getBookingList() {
        return bookingList;
    }

    public String getPricingDisplay() {
        return pricingDisplay;
    }

    public void setPricingDisplay(String pricingDisplay) {
        this.pricingDisplay = pricingDisplay;
    }

    public JsonReturnBookingOrderNo(JSONObject jsonObject) {
        super(jsonObject);
    }

    public Member getDataList() {
        return dataList;
    }

    public void setDataList(Member dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        bookingList = new ArrayList<>();

        try {
            JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList = new Member();
            dataList.setBookingOrderNo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_BOOKING_ORDER_NO));

            setPricingDisplay(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_TABLE_PRICING_DISPLAY));


           JSONArray jsBookingNo = jsonObj.getJSONArray(JsonKey.MAIN_BOOKING_LIST);

            for (int i = 0;i<jsBookingNo.length();i++){
                JSONObject jsBooking = jsBookingNo.getJSONObject(i);
                bookingList.add(Utils.getStringFromJsonObjectWithKey(jsBooking, JsonKey.SHOPPING_BOOKING_NO));
            }

//            a.ge


        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    public class Member implements Serializable {

        public String getBookingOrderNo() {
            return bookingOrderNo;
        }

        public void setBookingOrderNo(String bookingOrderNo) {
            this.bookingOrderNo = bookingOrderNo;
        }

        private String bookingOrderNo;


    }

}





