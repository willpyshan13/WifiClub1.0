/**
 * Project Name: itee
 * File Name:  JsonSigningGuest.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-01-28
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
 * ClassName:JsonSigningGuest <br/>
 * Function: To guest signing. <br/>
 * Date: 2015-01-28 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */

public class JsonSigningGuest extends BaseJsonObject implements Serializable {
    public JsonSigningGuest(JSONObject jsonObject) {
        super(jsonObject);
    }

    public ArrayList<Member> dataList;

    public ArrayList<Member> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Member> dataList) {
        this.dataList = dataList;
    }

    public static class Member implements Serializable {
        public String memberName;
        public Integer memberId;
        public String memberNo;
        public String phone;
        public String nameSort;

        public String signNumber;

        public String getNameSort() {
            return nameSort;
        }

        public void setNameSort(String nameSort) {
            this.nameSort = nameSort;
        }

        public String getSignNumber() {
            return signNumber;
        }

        public void setSignNumber(String signNumber) {
            this.signNumber = signNumber;
        }


        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
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

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }


    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList();

        try {
            Utils.log(jsonObj.toString());
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < joDataList.length(); i++) {

                JSONObject jsonObject = joDataList.getJSONObject(i);
                JsonSigningGuest.Member member = new Member();
                member.memberName = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NAME);
                member.memberId = Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_ID);
                member.memberNo = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_NO);
                member.phone = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_SIGNINGGUEST_MEMBER_PHONE);
                member.signNumber = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_SIGNINGGUEST_SIGN_NUMBER);
                member.nameSort = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_SIGNINGGUEST_NAME_SORT);
                dataList.add(member);
            }


        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }
}
