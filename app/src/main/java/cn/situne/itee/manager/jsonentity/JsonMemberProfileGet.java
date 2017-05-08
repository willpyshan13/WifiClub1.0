/**
 * Project Name: itee
 * File Name:  JsonMemberProfileGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonMemberProfileGet <br/>
 * Function: To get member profile. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */

public class JsonMemberProfileGet extends BaseJsonObject implements Serializable {

    private DataList dataList;

    public JsonMemberProfileGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }


    public class DataList implements Serializable {
        private String memberPhoto;
        private Integer memberId;
        private String memberName;
        private String memberTel;
        private String memberEmail;
        private String memberType;
        private Integer memberTypeId;
        private String memberNo;
        private String balancesAccount;
        private Integer balancesAccountCount;
        private Integer purchaseHistoryCount;
        private String signature;
        private String overdueFlag;

        public String getOverdueFlag() {
            return overdueFlag;
        }

        public void setOverdueFlag(String overdueFlag) {
            this.overdueFlag = overdueFlag;
        }

        public String getMemberPhoto() {
            return memberPhoto;
        }

        public void setMemberPhoto(String memberPhoto) {
            this.memberPhoto = memberPhoto;
        }

        public Integer getMemberId() {
            return memberId;
        }

        public Integer getMemberTypeId() {
            return memberTypeId;
        }

        public void setMemberTypeId(Integer memberTypeId) {
            this.memberTypeId = memberTypeId;
        }

        public void setMemberId(Integer memberId) {
            this.memberId = memberId;
        }

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getMemberTel() {
            return memberTel;
        }

        public void setMemberTel(String memberTel) {
            this.memberTel = memberTel;
        }

        public String getMemberEmail() {
            return memberEmail;
        }

        public void setMemberEmail(String memberEmail) {
            this.memberEmail = memberEmail;
        }

        public String getMemberType() {
            return memberType;
        }

        public void setMemberType(String memberType) {
            this.memberType = memberType;
        }

        public String getMemberNo() {
            return memberNo;
        }

        public void setMemberNo(String memberNo) {
            this.memberNo = memberNo;
        }

        public String getBalancesAccount() {
            return balancesAccount;
        }

        public void setBalancesAccount(String balancesAccount) {
            this.balancesAccount = balancesAccount;
        }

        public Integer getBalancesAccountCount() {
            return balancesAccountCount;
        }

        public void setBalancesAccountCount(Integer balancesAccountCount) {
            this.balancesAccountCount = balancesAccountCount;
        }

        public Integer getPurchaseHistoryCount() {
            return purchaseHistoryCount;
        }

        public void setPurchaseHistoryCount(Integer purchaseHistoryCount) {
            this.purchaseHistoryCount = purchaseHistoryCount;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new DataList();
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            dataList.setMemberPhoto(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_MEMBER_PHOTO));
            dataList.setMemberId(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_MEMBER_ID));
            dataList.setMemberName(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_MEMBER_NAME));
            dataList.setMemberTel(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_MEMBER_TEL));
            dataList.setMemberEmail(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_MEMBER_EMAIL));
            dataList.setMemberType(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_MEMBER_TYPE));
            dataList.setMemberNo(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_MEMBER_NO));
            dataList.setBalancesAccount(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_BALANCES_ACCOUNT));
            dataList.setBalancesAccountCount(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_BALANCES_ACCOUNT_COUNT));
            dataList.setPurchaseHistoryCount(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_PURCHASE_HISTORY_COUNT));
            dataList.setSignature(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_SIGNATURE));
            dataList.setMemberTypeId(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_MEMBER_TYPE_ID));

            dataList.setOverdueFlag(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.PLAYER_PROFILE_OVERDUE_FLAG));

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
