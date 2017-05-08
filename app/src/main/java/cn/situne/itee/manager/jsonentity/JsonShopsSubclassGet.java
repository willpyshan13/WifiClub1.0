/**
 * Project Name: itee
 * File Name:  JsonShopsSubclassGet.java
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
 * ClassName:JsonShopsSubclassGet <br/>
 * Function: To get shops subclass. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonShopsSubclassGet extends BaseJsonObject implements Serializable {

    private ArrayList<ProductTypeDetail> dataList;

    public ArrayList<ProductTypeDetail> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<ProductTypeDetail> dataList) {
        this.dataList = dataList;
    }

    public JsonShopsSubclassGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new ArrayList<>();
        try {
            JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
            for (int i = 0; i < joDataList.length(); i++) {
                JSONObject jsonObject = joDataList.getJSONObject(i);
                ProductTypeDetail bean = new ProductTypeDetail();
                bean.setPraId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_ID));
                bean.setPraParentId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_PARENT_ID));
                bean.setPraLevel(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_LEVEL));
                bean.setPraPdId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_PD_ID));
                bean.setPraName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_NAME));
                ArrayList<ProductTypeDetail> items = new ArrayList<>();
                if (jsonObject.has(JsonKey.SHOPS_SUB_CLASS)) {
                    JSONArray joSubclass = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_SUB_CLASS);
                    setChildItem(joSubclass, items);
                }
                bean.setItems(items);
                dataList.add(bean);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
    }

    private void setChildItem(JSONArray joSubclass, List<ProductTypeDetail> items) {
        try {

            for (int i = 0; i < joSubclass.length(); i++) {
                JSONObject jsonObject = joSubclass.getJSONObject(i);
                ProductTypeDetail item = new ProductTypeDetail();
                item.setPraId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_ID));
                item.setPraParentId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_PARENT_ID));
                item.setPraLevel(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_LEVEL));
                item.setPraPdId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_PD_ID));
                item.setPraName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_NAME));
                ArrayList<ProductTypeDetail> childItems = new ArrayList<>();
                if (jsonObject.has(JsonKey.SHOPS_SUB_CLASS)) {
                    JSONArray joItemSubclass = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_SUB_CLASS);
                    setChildItem(joItemSubclass, childItems);
                }
                item.setItems(childItems);
                items.add(item);
            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    public class ProductTypeDetail implements Serializable {
        private int praId;
        private int praParentId;
        private int praLevel;
        private int praPdId;
        private String praName;
        private boolean isSelected;

        private ArrayList<ProductTypeDetail> items;

        public ArrayList<ProductTypeDetail> getItems() {
            return items;
        }

        public void setItems(ArrayList<ProductTypeDetail> items) {
            this.items = items;
        }

        public int getPraId() {
            return praId;
        }

        public void setPraId(int praId) {
            this.praId = praId;
        }

        public int getPraParentId() {
            return praParentId;
        }

        public void setPraParentId(int praParentId) {
            this.praParentId = praParentId;
        }

        public int getPraLevel() {
            return praLevel;
        }

        public void setPraLevel(int praLevel) {
            this.praLevel = praLevel;
        }

        public int getPraPdId() {
            return praPdId;
        }

        public void setPraPdId(int praPdId) {
            this.praPdId = praPdId;
        }

        public String getPraName() {
            return praName;
        }

        public void setPraName(String praName) {
            this.praName = praName;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }
    }
}





