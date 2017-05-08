/**
 * Project Name: itee
 * File Name:  JsonLifeMember.java
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
 * ClassName:JsonLifeMember <br/>
 * Function: To set LifeMember. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonLifeMember extends BaseJsonObject implements Serializable {

    private List<MemberPricing> memberPricing;

    public List<MemberPricing> getMemberPricing() {
        return memberPricing;
    }

    public static class MemberPricing implements Serializable {

        public Integer memberTimeId;

        public Integer productId;
        public String productEnName;
        public String memberProductPrize;
        public String guestProductPrize;
        public String productCode;
        private String pricingDate;
        private String mainId;
        private String time;


        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getMainId() {
            return mainId;
        }

        public void setMainId(String mainId) {
            this.mainId = mainId;
        }

        public Integer getMemberTimeId() {
            return memberTimeId;
        }

        public void setMemberTimeId(Integer memberTimeId) {
            this.memberTimeId = memberTimeId;
        }

        public String getPricingDate() {
            return pricingDate;
        }

        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
        }

        public String getProductEnName() {
            return productEnName;
        }

        public void setProductEnName(String productEnName) {
            this.productEnName = productEnName;
        }

        public String getMemberProductPrize() {
            return memberProductPrize;
        }

        public void setMemberProductPrize(String memberProductPrize) {
            this.memberProductPrize = memberProductPrize;
        }

        public String getGuestProductPrize() {
            return guestProductPrize;
        }

        public void setGuestProductPrize(String guestProductPrize) {
            this.guestProductPrize = guestProductPrize;
        }

        public String getProductCode() {
            return productCode;
        }

        public void setProductCode(String productCode) {
            this.productCode = productCode;
        }
    }

    public JsonLifeMember(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        memberPricing = new ArrayList<>();
        try {
            JSONArray jsonArray = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                MemberPricing memberPricing = new MemberPricing();
                memberPricing.memberTimeId = Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.CUSTOMER_MEMBER_DATE_MEMBER_TIME_ID);
                memberPricing.setTime(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_TIME));
                memberPricing.productId = Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.CUSTOMER_PRODUCT_ID);
                memberPricing.productEnName = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_PRODUCT_NAME);
                memberPricing.setMainId(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_MAIN_ID));
                memberPricing.memberProductPrize = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.CUSTOMER_MEMBER_PRICE);
                memberPricing.guestProductPrize = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.CUSTOMER_GUEST_PRICE);
                memberPricing.pricingDate = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.PRICING_DATE_DESC);
                memberPricing.productCode = Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.CUSTOMER_PRODUCT_CODE);
                this.memberPricing.add(memberPricing);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

}
