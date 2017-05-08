package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * Created by luochao on 9/12/15.
 */
public class JsonNfcCheckCardForBookingNoGet extends BaseJsonObject implements Serializable {
    public JsonNfcCheckCardForBookingNoGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    private ArrayList<BookingItem> bookingItems;

    public ArrayList<BookingItem> getBookingItems() {
        return bookingItems;
    }

    public void setBookingItems(ArrayList<BookingItem> bookingItems) {
        this.bookingItems = bookingItems;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        try {
            JSONArray joDataList = jsonObj.getJSONArray(JsonKey.NFC_RETURN_BOOKING);
            bookingItems = new ArrayList<>();

            for (int i = 0;i<joDataList.length();i++){
                JSONObject jsonObject = joDataList.getJSONObject(i);
                BookingItem bookingItem = new BookingItem();
                bookingItem.setBkdBkId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.NFC_BKD_BK_ID));
                bookingItem.setBkdBkNo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.NFC_BKD_BK_NO));
                bookingItem.setBkdName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.NFC_BKD_NAME));
                bookingItem.setCheckInStatus(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.NFC_BKS_STS));

                bookingItem.setMemberId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.NFC_MEMBER_ID));
                bookingItem.setMemberType(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.NFC_MEMBER_TYPE));

                bookingItems.add(bookingItem);
            }

            setBookingItems(bookingItems);

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public static class BookingItem implements Serializable {

        public String bkdBkNo;
        public String bkdName;
        public String bkdBkId;
        private String checkInStatus;

        public String memberId;
        public String memberType;

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }

        public String getCheckInStatus() {
            return checkInStatus;
        }

        public void setCheckInStatus(String checkInStatus) {
            this.checkInStatus = checkInStatus;
        }

        public String getBkdBkNo() {
            return bkdBkNo;
        }

        public void setBkdBkNo(String bkdBkNo) {
            this.bkdBkNo = bkdBkNo;
        }

        public String getBkdName() {
            return bkdName;
        }

        public void setBkdName(String bkdName) {
            this.bkdName = bkdName;
        }

        public String getBkdBkId() {
            return bkdBkId;
        }

        public void setBkdBkId(String bkdBkId) {
            this.bkdBkId = bkdBkId;
        }
    }

}
