/**
 * Project Name: itee
 * File Name:  JsonCustomersGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

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
 * ClassName:JsonCustomersGet <br/>
 * Function: To get customers. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */

public class JsonCustomersGet extends BaseJsonObject implements Serializable {

    public JsonCustomersGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    private CustomerList customerList;

    public CustomerList getCustomerList() {
        return customerList;
    }

    public void setCustomerList(CustomerList customerList) {
        this.customerList = customerList;
    }

    public static class CustomerList implements Serializable {

        private Integer nonMemberId;
        private String nonMemberName;
        private Integer nonMemberUum;
        public List<MemberListItem> memberList;

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

        public Integer getNonMemberUum() {
            return nonMemberUum;
        }

        public void setNonMemberUum(Integer nonMemberUum) {
            this.nonMemberUum = nonMemberUum;
        }

        public List<MemberListItem> getMemberList() {
            return memberList;
        }

        public void setMemberList(List<MemberListItem> memberList) {
            this.memberList = memberList;
        }

        public static class MemberListItem implements Serializable {
            private Integer memberId;
            private String memberName;
            private Integer memberUum;

            public Integer getMemberId() {
                return memberId;
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

            public Integer getMemberUum() {
                return memberUum;
            }

            public void setMemberUum(Integer memberUum) {
                this.memberUum = memberUum;
            }
        }

    }

    /*private DataList dataList;

    public static class DataList {
        public String startTime;
        public String endTime;
        public Integer timeInterval;
        public Integer eachGroupNum;

        public List<CourseAreaTypeItem> courseAreaTypeList;

        public List<LockListItem> lockList;

        public List<BookingListItem> bookingList;

        public static class CourseAreaTypeItem {
            public String areaId;
            public String areaName;
            public Integer lockStatus;
        }

        public static class LockListItem {
            public String lockAreaType;
            public String lockTime;
        }

        public static class BookingListItem {
            public String bookingArea;
            public Integer bookingAreaId;

            public Map<String, List<BookingDetailItem>> bookingDetailMap;

            public static class BookingDetailItem {
                public String bookingTime;
                public Integer bookingStatus;
                public Integer bookingSort;
            }
        }*/
    //}

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        customerList = new CustomerList();

        try {
            JSONObject joCustomerList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            customerList.nonMemberId = Utils.getIntegerFromJsonObjectWithKey(joCustomerList, JsonKey.CUSTOMER_NON_MEMBER_ID);
            customerList.nonMemberName = Utils.getStringFromJsonObjectWithKey(joCustomerList, JsonKey.CUSTOMER_NON_MEMBER_NAME);
            customerList.nonMemberUum = Utils.getIntegerFromJsonObjectWithKey(joCustomerList, JsonKey.CUSTOMER_NON_MEMBER_NUM);

            customerList.memberList = new ArrayList<>();
            JSONArray arrMemberList = Utils.getArrayFromJsonObjectWithKey(joCustomerList, JsonKey.CUSTOMER_MEMBER);
            for (int i = 0; i < arrMemberList.length(); i++) {
                JSONObject joMemberList = arrMemberList.getJSONObject(i);

                CustomerList.MemberListItem mi = new CustomerList.MemberListItem();
                mi.memberId = Utils.getIntegerFromJsonObjectWithKey(joMemberList, JsonKey.CUSTOMER_MEMBER_TYPE_ID);
                mi.memberName = Utils.getStringFromJsonObjectWithKey(joMemberList, JsonKey.CUSTOMER_MEMBER_NAME);
                mi.memberUum = Utils.getIntegerFromJsonObjectWithKey(joMemberList, JsonKey.CUSTOMER_MEMBER_NUM);
                customerList.memberList.add(mi);

            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }


}
