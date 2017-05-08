/**
 * Project Name: itee
 * File Name:  JsonMemberGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-21
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
 * ClassName:JsonMemberGet <br/>
 * Function: To get member. <br/>
 * Date: 2015-03-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonMemberGet extends BaseJsonObject implements Serializable {

    public JsonMemberGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    private ArrayList<CustomerList> dataList;

    public ArrayList<CustomerList> getDataList() {
        return dataList;
    }

    public static class CustomerList implements Serializable {

        private Integer nonMemberId;
        private String nonMemberName;
        private Integer nonMemberNum;
        public ArrayList<MemberListItem> memberList;

        public Integer getNonMemberId() {
            return nonMemberId;
        }

        public void setNonMemberId(Integer nonMemberId) {
            this.nonMemberId = nonMemberId;
        }

        public String getNonMemberName() {
            return nonMemberName;
        }

        public void setNonMemberName(String nonMemberName) {
            this.nonMemberName = nonMemberName;
        }

        public Integer getNonMemberNum() {
            return nonMemberNum;
        }

        public void setNonMemberNum(Integer nonMemberNum) {
            this.nonMemberNum = nonMemberNum;
        }

        public ArrayList<MemberListItem> getMemberList() {
            return memberList;
        }

        public static class MemberListItem implements Serializable {
            private Integer memberTypeId;
            private String memberTypeName;
            private Integer memberNum;

            public Integer getMemberTypeId() {
                return memberTypeId;
            }

            public void setMemberTypeId(Integer memberTypeId) {
                this.memberTypeId = memberTypeId;
            }

            public String getMemberTypeName() {
                return memberTypeName;
            }

            public void setMemberTypeName(String memberTypeName) {
                this.memberTypeName = memberTypeName;
            }

            public Integer getMemberNum() {
                return memberNum;
            }

            public void setMemberNum(Integer memberNum) {
                this.memberNum = memberNum;
            }
        }

    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new ArrayList<>();
        try {
            JSONObject joCustomerList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            CustomerList customerList = new CustomerList();
            customerList.setNonMemberId(Utils.getIntegerFromJsonObjectWithKey(joCustomerList, JsonKey.CUSTOMER_NON_MEMBER_ID));
            customerList.setNonMemberName(Utils.getStringFromJsonObjectWithKey(joCustomerList, JsonKey.CUSTOMER_NON_MEMBER_NAME));
            customerList.setNonMemberNum(Utils.getIntegerFromJsonObjectWithKey(joCustomerList, JsonKey.CUSTOMER_NON_MEMBER_NUM));

            customerList.memberList = new ArrayList<>();
            if (joCustomerList.has(JsonKey.CUSTOMER_MEMBER)) {
                JSONArray arrMemberList = Utils.getArrayFromJsonObjectWithKey(joCustomerList, JsonKey.CUSTOMER_MEMBER);
                for (int i = 0; i < arrMemberList.length(); i++) {
                    try {
                        JSONObject joMemberList = arrMemberList.getJSONObject(i);
                        CustomerList.MemberListItem mi = new CustomerList.MemberListItem();
                        mi.setMemberTypeId(Utils.getIntegerFromJsonObjectWithKey(joMemberList, JsonKey.CUSTOMER_MEMBER_TYPE_ID));
                        mi.setMemberTypeName(Utils.getStringFromJsonObjectWithKey(joMemberList, JsonKey.CUSTOMER_MEMBER_NAME));
                        mi.setMemberNum(Utils.getIntegerFromJsonObjectWithKey(joMemberList, JsonKey.CUSTOMER_MEMBER_NUM));
                        customerList.memberList.add(mi);
                    } catch (JSONException e) {
                        Utils.log(e.getMessage());
                    }
                }

            }


            dataList.add(customerList);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}
