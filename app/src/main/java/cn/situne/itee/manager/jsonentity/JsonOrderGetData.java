package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/23/15.
 */
public class JsonOrderGetData  extends BaseJsonObject implements Serializable {
    private String pricingDisplay;

    public String getPricingDisplay() {
        return pricingDisplay;
    }

    public void setPricingDisplay(String pricingDisplay) {
        this.pricingDisplay = pricingDisplay;
    }

    public JsonOrderGetData(JSONObject jsonObject) {
        super(jsonObject);
    }
    private ArrayList<String> bookingList;

    public ArrayList<String> getBookingList() {
        return bookingList;
    }

    public void setBookingList(ArrayList<String> bookingList) {
        this.bookingList = bookingList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {
            JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            bookingList = new ArrayList<>();
            setPricingDisplay(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_TABLE_PRICING_DISPLAY));
            JSONArray jsBookingNo = jsonObj.getJSONArray(JsonKey.MAIN_BOOKING_LIST);

            for (int i = 0;i<jsBookingNo.length();i++){
                JSONObject jsBooking = jsBookingNo.getJSONObject(i);
                bookingList.add(Utils.getStringFromJsonObjectWithKey(jsBooking, JsonKey.SHOPPING_BOOKING_NO));
            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }
}
