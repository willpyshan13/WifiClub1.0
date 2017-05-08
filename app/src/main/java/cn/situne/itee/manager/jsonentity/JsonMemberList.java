/**
 * Project Name: itee
 * File Name:	 JsonMemberList.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-26
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonMemberList <br/>
 * Function: entity of api memberList Get. <br/>
 * Date: 2015-03-26 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonMemberList extends BaseJsonObject implements Serializable {

    public JsonMemberList(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new ArrayList<>();
            JSONArray arrMemberList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < arrMemberList.length(); i++) {
                    try {
                        JSONObject joMember = arrMemberList.getJSONObject(i);
                        Member member = new Member();
                        member.memberId = Utils.getIntegerFromJsonObjectWithKey(joMember, JsonKey.CUSTOMER_11_MEMBER_ID);
                        member.memberName = Utils.getStringFromJsonObjectWithKey(joMember, JsonKey.CUSTOMER_11_MEMBER_NAME);
                        member.memberCard = Utils.getStringFromJsonObjectWithKey(joMember, JsonKey.CUSTOMER_11_MEMBER_CARD);
                        member.telName = Utils.getStringFromJsonObjectWithKey(joMember, JsonKey.CUSTOMER_11_TEL_NAME);
                        member.telNumber = Utils.getStringFromJsonObjectWithKey(joMember, JsonKey.CUSTOMER_11_TEL_NUMBER);
                        member.zipCode = Utils.getStringFromJsonObjectWithKey(joMember, JsonKey.CUSTOMER_11_ZIPCODE);
                        member.memberBirth = Utils.getStringFromJsonObjectWithKey(joMember, JsonKey.CUSTOMER_11_MEMBER_BIRTH);
                        member.memberPhoto = Utils.getStringFromJsonObjectWithKey(joMember, JsonKey.CUSTOMER_11_MEMBER_PHOTO);
                        dataList.add(member);
                    } catch (JSONException e) {
                        Utils.log(e.getMessage());
                    }
                }
        }
    }

    private ArrayList<Member> dataList;

    public ArrayList<Member> getDataList() {
        return dataList;
    }

    public static class Member implements Serializable {
        private int memberId;
        private String memberPhoto;
        private String memberName;
        private String memberCard;
        private String telName;
        private String telNumber;
        private String zipCode;
        private String memberBirth;

        public int getMemberId() {
            return memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public String getMemberCard() {
            return memberCard;
        }

        public String getTelName() {
            return telName;
        }

        public String getTelNumber() {
            return telNumber;
        }

        public String getZipCode() {
            return zipCode;
        }

        public String getMemberBirth() {
            return memberBirth;
        }

        public String getMemberPhoto() {
            return memberPhoto;
        }
    }

}