/**
 * Project Name: itee
 * File Name:  JsonRentalGoodsType.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-23
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
 * ClassName:JsonRentalGoodsType <br/>
 * Function: To set rental goods type. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonRentalGoodsType extends BaseJsonObject implements Serializable {

    private List<RentalGoodsType> dataList;

    public List<RentalGoodsType> getDataList() {
        return dataList;
    }

    public void setDataList(List<RentalGoodsType> dataList) {
        this.dataList = dataList;
    }

    public JsonRentalGoodsType(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new ArrayList();

        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject jsonObject = joDataList.getJSONObject(i);
                RentalGoodsType bean = new RentalGoodsType();
//                bean.setTypeId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_TYPE_ID));
//                bean.setTypeName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_TYPE_NAME));
                bean.setBookingFee(Utils.getDoubleFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_BOOKING_FEE));

                dataList.add(bean);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }

    class RentalGoodsType implements Serializable {

        private int typeId;
        private String typeName;
        private double bookingFee;

        public int getTypeId() {
            return typeId;
        }

        public void setTypeId(int typeId) {
            this.typeId = typeId;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public double getBookingFee() {
            return bookingFee;
        }

        public void setBookingFee(double bookingFee) {
            this.bookingFee = bookingFee;
        }
    }
}





