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
 * Created by luochao on 12/1/15.
 */
public class JsonTeeTimeBookingSearch  extends BaseJsonObject implements Serializable {
    public JsonTeeTimeBookingSearch(JSONObject jsonObject) {
        super(jsonObject);
    }


    private List<BookingItem> dataList;

    public List<BookingItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<BookingItem> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new ArrayList<>();

        try {
            JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);

            for (int i = 0; i < jsonArray.length(); i++) {

                BookingItem item = new BookingItem();
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                item.setBookingId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.COMMON_BOOKING_ID));
                item.setKeyWord(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.COMMON_KEY_WORD));
                dataList.add(item);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }


    public static class BookingItem implements Serializable {

        private String bookingId;
        private String keyWord;

        public String getBookingId() {
            return bookingId;
        }

        public void setBookingId(String bookingId) {
            this.bookingId = bookingId;
        }

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }
    }
}
