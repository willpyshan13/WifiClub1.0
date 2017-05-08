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
 * Created by luochao on 12/2/15.
 */
public class JsonBookingDetailListGet extends BaseJsonObject implements Serializable {

    private List<BookingDetailItem> dataList;

    public List<BookingDetailItem> getDataList() {
        return dataList;
    }

    public void setDataList(List<BookingDetailItem> dataList) {
        this.dataList = dataList;
    }

    public JsonBookingDetailListGet(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
            Utils.log(jsonObj.toString());

            try {
                JSONArray jsonArray = jsonObj.getJSONArray(JsonKey.COMMON_DATA_LIST);
                dataList = new ArrayList<>();

                for (int i = 0; i < jsonArray.length(); i++) {
                    BookingDetailItem  item = new BookingDetailItem();
                    JSONObject jsItem = jsonArray.getJSONObject(i);

                    item.setId(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_ID));
                    item.setName(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_NAME));
                    item.setBookingNo(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_NO));
                    item.setBkdTime(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_BKD_TIME));
                    item.setCaddieNo(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_CADDIE_NO));
                    item.setMemberType(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_APPOINTMENT_MEMBER_TYPE));
                    item.setSts(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_STS));
                    item.setMemberGender(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_MEMBER_GENDER));

                    item.setCurrentHole(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_CURRENT_HOLE));
                    item.setCheckInStatus(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_CHECK_IN_STATUS));
                    item.setAppointmentMid(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_APPOINTMENT_MID));

                    item.setDate(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_DATE));
                    item.setBookingColor(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_BOOKING_COLOR));
                    item.setPayStatus(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_PAY_STATUS));
                    item.setAppointmentGoodsStatus(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_APPOINTMENT_GOODS_STATUS));


                    item.setAppointmentGoods(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_APPOINTMENT_GOODS));
                    item.setAppointmentTime(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_APPOINTMENT_GOODS_STATUS));
                    item.setMemberGender(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_MEMBER_GENDER));
                    item.setName(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_NAME));
                    item.setCurrentHoleStatus(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_CURRENT_HOLE_STATUS));
                    item.setBookingNo(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.TEE_TIME_SEARCH_BOOKING_NO));

                    item.setDepositStatus(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.APPOINTMENT_DEPOSIT_STATUS));


                    item.setPayId(Utils.getStringFromJsonObjectWithKey(jsItem, JsonKey.PLAYER_PURCHASE_HISTORY_PAY_ID));
                    item.setPayDate(Utils.getStringFromJsonObjectWithKey(jsItem, "pay_time"));

                    dataList.add(item);
                }


            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }

    }

    public static class BookingDetailItem implements Serializable {

        private String caddieNo;

        private String bagNo;
        private String bookingNo;
        private String bkdTime;
        private String memberType;
        private String sts;
        private String name;
        private String id;


        private String memberGender;

        private String depositStatus;
        private String currentHole;
        private String checkInStatus;
        private String appointmentMid;
        private String date;
        private String bookingColor;
        private String payStatus;
        private String appointmentGoodsStatus;
        private String appointmentMemberType;
        private String appointmentGoods;
        private String appointmentTime;
        private String currentHoleStatus;



        private String payId;
        private String payDate;

        public String getPayId() {
            return payId;
        }

        public void setPayId(String payId) {
            this.payId = payId;
        }

        public String getPayDate() {
            return payDate;
        }

        public void setPayDate(String payDate) {
            this.payDate = payDate;
        }

        public String getDepositStatus() {
            return depositStatus;
        }

        public void setDepositStatus(String depositStatus) {
            this.depositStatus = depositStatus;
        }

        public String getCurrentHole() {
            return currentHole;
        }

        public void setCurrentHole(String currentHole) {
            this.currentHole = currentHole;
        }

        public String getCheckInStatus() {
            return checkInStatus;
        }

        public void setCheckInStatus(String checkInStatus) {
            this.checkInStatus = checkInStatus;
        }

        public String getAppointmentMid() {
            return appointmentMid;
        }

        public void setAppointmentMid(String appointmentMid) {
            this.appointmentMid = appointmentMid;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getBookingColor() {
            return bookingColor;
        }

        public void setBookingColor(String bookingColor) {
            this.bookingColor = bookingColor;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public String getAppointmentGoodsStatus() {
            return appointmentGoodsStatus;
        }

        public void setAppointmentGoodsStatus(String appointmentGoodsStatus) {
            this.appointmentGoodsStatus = appointmentGoodsStatus;
        }

        public String getAppointmentMemberType() {
            return appointmentMemberType;
        }

        public void setAppointmentMemberType(String appointmentMemberType) {
            this.appointmentMemberType = appointmentMemberType;
        }

        public String getAppointmentGoods() {
            return appointmentGoods;
        }

        public void setAppointmentGoods(String appointmentGoods) {
            this.appointmentGoods = appointmentGoods;
        }

        public String getAppointmentTime() {
            return appointmentTime;
        }

        public void setAppointmentTime(String appointmentTime) {
            this.appointmentTime = appointmentTime;
        }

        public String getCurrentHoleStatus() {
            return currentHoleStatus;
        }

        public void setCurrentHoleStatus(String currentHoleStatus) {
            this.currentHoleStatus = currentHoleStatus;
        }

        public String getMemberGender() {
            return memberGender;
        }

        public void setMemberGender(String memberGender) {
            this.memberGender = memberGender;
        }

        public String getCaddieNo() {
            return caddieNo;
        }

        public void setCaddieNo(String caddieNo) {
            this.caddieNo = caddieNo;
        }

        public String getBagNo() {
            return bagNo;
        }

        public void setBagNo(String bagNo) {
            this.bagNo = bagNo;
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public String getBkdTime() {
            return bkdTime;
        }

        public void setBkdTime(String bkdTime) {
            this.bkdTime = bkdTime;
        }

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }

        public String getSts() {
            return sts;
        }

        public void setSts(String sts) {
            this.sts = sts;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
