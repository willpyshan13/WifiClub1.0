/**
 * Project Name: itee
 * File Name:  JsonBookingGoodsList.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonBookingGoodsList <br/>
 * Function: Set Booking Goods List. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */
public class JsonBookingGoodsList extends BaseJsonObject implements Serializable {

    private static final long serialVersionUID = -1529846374946979809L;

    public JsonBookingGoodsList(JSONObject jsonObject) {
        super(jsonObject);
    }

    private DataList dataList;

    public static class DataList implements Serializable {

        private List<CategoryListItem> categoryList;

        private List<GoodsListItem> goodsList;
        private String bookingFee;

        public String getBookingFee() {
            return bookingFee;
        }

        public void setBookingFee(String bookingFee) {
            this.bookingFee = bookingFee;
        }

        public List<CategoryListItem> getCategoryList() {
            return categoryList;
        }

        public void setCategoryList(List<CategoryListItem> categoryList) {
            this.categoryList = categoryList;
        }

        public List<GoodsListItem> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListItem> goodsList) {
            this.goodsList = goodsList;
        }

        public static class CategoryListItem implements Serializable {
            private Integer attrId;
            private String attrName;
            private Integer attrParentId;
            private Integer attrLevel;

            public Integer getAttrId() {
                return attrId;
            }

            public void setAttrId(Integer attrId) {
                this.attrId = attrId;
            }

            public String getAttrName() {
                return attrName;
            }

            public void setAttrName(String attrName) {
                this.attrName = attrName;
            }

            public Integer getAttrParentId() {
                return attrParentId;
            }

            public void setAttrParentId(Integer attrParentId) {
                this.attrParentId = attrParentId;
            }

            public Integer getAttrLevel() {
                return attrLevel;
            }

            public void setAttrLevel(Integer attrLevel) {
                this.attrLevel = attrLevel;
            }
        }

        public static class GoodsListItem implements Serializable {
            private Integer goodsId;
            private String goodsName;
            private Integer goodsAttrId;
            private String goodsPrice;
            private String caddieNo;

            public String getCaddieNo() {
                return caddieNo;
            }

            public void setCaddieNo(String caddieNo) {
                this.caddieNo = caddieNo;
            }

            public Integer getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(Integer goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public Integer getGoodsAttrId() {
                return goodsAttrId;
            }

            public void setGoodsAttrId(Integer goodsAttrId) {
                this.goodsAttrId = goodsAttrId;
            }

            public String getGoodsPrice() {
                return goodsPrice;
            }

            public void setGoodsPrice(String goodsPrice) {
                this.goodsPrice = goodsPrice;
            }
        }
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        dataList = new DataList();
        Log.e("syb",jsonObj.toString());
        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);

            dataList.categoryList = new ArrayList<>();
            JSONArray arrLockList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_CATEGORY_LIST);
            for (int i = 0; i < arrLockList.length(); i++) {
                JSONObject joLockListItem = arrLockList.getJSONObject(i);
                DataList.CategoryListItem lli = new DataList.CategoryListItem();
                lli.setAttrId(Utils.getIntegerFromJsonObjectWithKey(joLockListItem, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_ATTR_ID));
                lli.setAttrName(Utils.getStringFromJsonObjectWithKey(joLockListItem, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_ATTR_NAME));
                lli.setAttrParentId(Utils.getIntegerFromJsonObjectWithKey(joLockListItem, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_ATTR_PARENT_ID));
                lli.setAttrLevel(Utils.getIntegerFromJsonObjectWithKey(joLockListItem, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_ATTR_LEVEL));
                dataList.categoryList.add(lli);
            }

            dataList.goodsList = new ArrayList<>();
            JSONArray arrBookingList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_GOODS_LIST);
            for (int i = 0; i < arrBookingList.length(); i++) {
                JSONObject joBookingListItem = arrBookingList.getJSONObject(i);
                DataList.GoodsListItem bli = new DataList.GoodsListItem();

                bli.setGoodsId(Utils.getIntegerFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_GOODS_ID));
                bli.setGoodsName(Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_GOODS_NAME));
                bli.setGoodsAttrId(Utils.getIntegerFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_GOODS_ATTR_ID));
                bli.setGoodsPrice(Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_GOODS_PRICE));
                bli.setCaddieNo(Utils.getStringFromJsonObjectWithKey(joBookingListItem, JsonKey
                        .TEE_TIME_BOOKING_GOODS_LIST_GOODS_CADDIE_NO));
                dataList.goodsList.add(bli);
            }
            dataList.setBookingFee(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.TEE_TIME_BOOKING_GOODS_LIST_BOOKING_FEE));


        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }
}
