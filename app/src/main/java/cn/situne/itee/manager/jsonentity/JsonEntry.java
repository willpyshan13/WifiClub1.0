/**
 * Project Name: itee
 * File Name:  JsonEntry.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-04-23
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

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
 * ClassName:JsonEntry <br/>
 * Function: JsonEntry. <br/>
 * Date: 2015-04-23 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
@SuppressWarnings("unused")
public class JsonEntry extends BaseJsonObject {

    private ArrayList<EntryList> entryList;

    public ArrayList<EntryList> getEntryList() {
        return entryList;
    }

    public static class EntryList implements Serializable {
        public String memberName;
        public String memberId;
        public String memberType;
        public String photo;
        public Integer signType;
        public String bookingOrderNo;
        public String bookingNo;
        public String infoTime;
        public String infoArea;
        public Integer checkStatus;


        private String bagCardInfo;

        public String getBagCardInfo() {
            return bagCardInfo;
        }

        public void setBagCardInfo(String bagCardInfo) {
            this.bagCardInfo = bagCardInfo;
        }

        private String caddieCardStatus;
        private String bagCardStatus;

        private String caddieInfo;


        private String pricingTimes;

        private String pricingBdpId;
        private String pricingTimesFlg;

        public String getPricingTimes() {
            return pricingTimes;
        }

        public void setPricingTimes(String pricingTimes) {
            this.pricingTimes = pricingTimes;
        }

        public String getPricingBdpId() {
            return pricingBdpId;
        }

        public void setPricingBdpId(String pricingBdpId) {
            this.pricingBdpId = pricingBdpId;
        }

        public String getPricingTimesFlg() {
            return pricingTimesFlg;
        }

        public void setPricingTimesFlg(String pricingTimesFlg) {
            this.pricingTimesFlg = pricingTimesFlg;
        }

        public String getCaddieInfo() {
            return caddieInfo;
        }

        public void setCaddieInfo(String caddieInfo) {
            this.caddieInfo = caddieInfo;
        }

        private boolean caddieCardIsNowUnBind;
        private boolean bagCardIsNowUnBind;

        public boolean isCaddieCardIsNowUnBind() {
            return caddieCardIsNowUnBind;
        }

        public void setCaddieCardIsNowUnBind(boolean caddieCardIsNowUnBind) {
            this.caddieCardIsNowUnBind = caddieCardIsNowUnBind;
        }

        public boolean isBagCardIsNowUnBind() {
            return bagCardIsNowUnBind;
        }

        public void setBagCardIsNowUnBind(boolean bagCardIsNowUnBind) {
            this.bagCardIsNowUnBind = bagCardIsNowUnBind;
        }

        public String getCaddieCardStatus() {
            return caddieCardStatus;
        }

        public void setCaddieCardStatus(String caddieCardStatus) {
            this.caddieCardStatus = caddieCardStatus;
        }

        public String getBagCardStatus() {
            return bagCardStatus;
        }

        public void setBagCardStatus(String bagCardStatus) {
            this.bagCardStatus = bagCardStatus;
        }

        public String getBookingOrderNo() {
            return bookingOrderNo;
        }

        public void setBookingOrderNo(String bookingOrderNo) {
            this.bookingOrderNo = bookingOrderNo;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public Integer getSignType() {
            return signType;
        }

        public void setSignType(Integer signType) {
            this.signType = signType;
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public String getInfoTime() {
            return infoTime;
        }

        public void setInfoTime(String infoTime) {
            this.infoTime = infoTime;
        }

        public String getInfoArea() {
            return infoArea;
        }

        public void setInfoArea(String infoArea) {
            this.infoArea = infoArea;
        }

        public Integer getCheckStatus() {
            return checkStatus;
        }

        public void setCheckStatus(Integer checkStatus) {
            this.checkStatus = checkStatus;
        }
    }

    public JsonEntry(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        entryList = new ArrayList<>();
        try {
            JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                EntryList entry = new EntryList();
                entry.memberName = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_NAME);
                entry.memberId = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_ID);
                entry.memberType = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_TYPE);
                entry.photo = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_PHOTO);
                entry.signType = Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_SIGN_TYPE);
                entry.bookingOrderNo = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_BOOKING_ORDER_NO);
                entry.bookingNo = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_BOOKING_NO);
                entry.infoTime = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_INFO_TIME);
                entry.infoArea = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_INFO_AREA);
                entry.checkStatus = Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.ENTRY_MEMBER_CHECK_STATUS);
                entry.setBagCardStatus(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.NFC_BAG_CARD_STATUS));
                entry.setCaddieCardStatus(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.NFC_CADDIE_CARD_STATUS));
                entry.setCaddieInfo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.NFC_CADDIE_INFO));


                entry.setPricingBdpId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_BDP_ID));
                entry.setPricingTimes(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_TIMES));
                entry.setPricingTimesFlg(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPPING_PRICING_TIMES_FLG));
                entry.setBagCardInfo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.NFC_BAGCARD_INFO));
                entryList.add(entry);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }
}
