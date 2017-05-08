package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/25/15.
 */
public class JsonCheckInGetData  extends BaseJsonObject {
    public JsonCheckInGetData(JSONObject jsonObject) {
        super(jsonObject);
    }

    private String bookingId;

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                setBookingId(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.COMMON_BOOKING_ID));
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

}
