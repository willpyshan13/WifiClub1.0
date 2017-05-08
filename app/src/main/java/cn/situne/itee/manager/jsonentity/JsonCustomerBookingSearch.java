/**
 * Project Name: itee
 * File Name:  JsonCustomerBookingSearch.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-01-28
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonCustomerBookingSearch <br/>
 * Function: To searchCustomer Booking. <br/>
 * Date: 2015-01-28 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonCustomerBookingSearch extends BaseJsonObject implements Serializable {
    public ArrayList<Member> dataList;
    public JsonCustomerBookingSearch(JSONObject jsonObject) {
        super(jsonObject);
    }
    public ArrayList<Member> getDataList() {
        return dataList;
    }
    public void setDataList(ArrayList<Member> dataList) {
        this.dataList = dataList;
    }
    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        Utils.log(jsonObj.toString());
        dataList = new ArrayList<>();
        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < joDataList.length(); i++) {

                JSONObject jsonObject = joDataList.getJSONObject(i);
                JsonCustomerBookingSearch.Member member = new Member();
                member.setMemberName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NAME));
                member.setMemberId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_ID));
                member.setMemberNo(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NO));
                member.setMemberTel(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_COUSTOMERBOOKINGSEARCH_MEMBER_TEL));
                member.setMemberBirth(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_COUSTOMERBOOKINGSEARCH_MEMBER_BIRTH));
                member.setZipCode(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_COUSTOMERBOOKINGSEARCH_ZIP_CODE));
                member.setMemberPic(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_COUSTOMERBOOKINGSEARCH_MEMBER_PIC));
                member.setMemberWeek(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_COUSTOMER_BOOKING_SEARCH_MEMBER_WEEK));
                member.setMemberWeekFlag(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_COUSTOMER_BOOKING_SEARCH_MEMBER_WEEK_FLAG));
                member.setEndDateFlag(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_COUSTOMER_BOOKING_SEARCH_END_FLAG));
                member.setIndex(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_CUSTOMERS_INDEX));
                member.setMemberType(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PLAYER_PROFILE_MEMBER_TYPE));
                member.setKeyWord(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.COMMON_KEY_WORD));
                dataList.add(member);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public static class Member implements Serializable {
        private Integer memberId;
        private String memberNo;
        private String memberName;
        private String memberBirth;
        private String memberTel;
        private String zipCode;
        private String memberPic;
        private String memberWeek;
        private String endDateFlag;
        private String memberWeekFlag;
        private String showTitle;
        private String memberType;

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }

        private String keyWord;

        private String index;

        private boolean jAgent;

        public String getKeyWord() {
            return keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public boolean isjAgent() {
            return jAgent;
        }

        public void setjAgent(boolean jAgent) {
            this.jAgent = jAgent;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getShowTitle() {
            return showTitle;
        }

        public void setShowTitle(String showTitle) {
            this.showTitle = showTitle;
        }

        public String getMemberWeekFlag() {
            return memberWeekFlag;
        }

        public void setMemberWeekFlag(String memberWeekFlag) {
            this.memberWeekFlag = memberWeekFlag;
        }
        public String getMemberWeek() {
            return memberWeek;
        }

        public void setMemberWeek(String memberWeek) {
            this.memberWeek = memberWeek;
        }

        public String getMemberPic() {
            return memberPic;
        }

        public void setMemberPic(String memberPic) {
            this.memberPic = memberPic;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public Integer getMemberId() {
            return memberId;
        }

        public void setMemberId(Integer memberId) {
            this.memberId = memberId;
        }

        public String getMemberNo() {
            return memberNo;
        }

        public void setMemberNo(String memberNo) {
            this.memberNo = memberNo;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberBirth() {
            return memberBirth;
        }

        public void setMemberBirth(String memberBirth) {
            this.memberBirth = memberBirth;
        }

        public String getMemberTel() {
            return memberTel;
        }

        public void setMemberTel(String memberTel) {
            this.memberTel = memberTel;
        }

        public String getEndDateFlag() {
            return endDateFlag;
        }

        public void setEndDateFlag(String endDateFlag) {
            this.endDateFlag = endDateFlag;
        }
    }
}
