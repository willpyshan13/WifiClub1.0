/**
 * Project Name: itee
 * File Name:  JsonAppointmentDetail.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-01-27
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
 * ClassName:JsonAppointmentDetail <br/>
 * Function: Get Appointment Detail. <br/>
 * Date: 2015-01-27 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonAppointmentDetail extends BaseJsonObject implements Serializable {

    private ArrayList<Data> dataList;

    public JsonAppointmentDetail(JSONObject jsonObject) {
        super(jsonObject);
    }

    public ArrayList<Data> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Data> dataList) {
        this.dataList = dataList;
    }

    public class Data implements Serializable {
        private String memberName;
        private String goodsName;
        private String goodsAttribute;
        private Integer categoryId;

        public String getMemberName() {
            return memberName;
        }

        public void setMemberName(String memberName) {
            this.memberName = memberName;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getGoodsAttribute() {
            return goodsAttribute;
        }

        public void setGoodsAttribute(String goodsAttribute) {
            this.goodsAttribute = goodsAttribute;
        }

        public Integer getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(Integer categoryId) {
            this.categoryId = categoryId;
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {

            ArrayList<Data> myDataList = new ArrayList<>();
            JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < arrDataList.length(); i++) {
                JSONObject joData = arrDataList.getJSONObject(i);
                Data data = new Data();
                data.setGoodsAttribute(Utils.getStringFromJsonObjectWithKey(joData, JsonKey.APPOINTMENT_DETAIL_GOODS_ATTRIBUTE));
                data.setGoodsName(Utils.getStringFromJsonObjectWithKey(joData, JsonKey.APPOINTMENT_DETAIL_GOODS_NAME));
                data.setMemberName(Utils.getStringFromJsonObjectWithKey(joData, JsonKey.APPOINTMENT_DETAIL_MEMBER_NAME));
                data.setCategoryId(Utils.getIntegerFromJsonObjectWithKey(joData, JsonKey.APPOINTMENT_DETAIL_CATEGORY_ID));
                myDataList.add(data);
            }

            setDataList(myDataList);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }
}