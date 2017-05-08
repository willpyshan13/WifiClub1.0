/**
 * Project Name: itee
 * File Name:  JsonShopsPropertyPriceOrQtyGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonShopsPropertyPriceOrQtyGet <br/>
 * Function: To get shops property price Or Qty. <br/>
 * Date: 2015-04-09 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonShopsPropertyPriceOrQtyGet extends BaseJsonObject implements Serializable {

    private static final long serialVersionUID = -7310371944411123313L;

    private ArrayList<DataItem> dataList;

    public ArrayList<DataItem> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<DataItem> dataList) {
        this.dataList = dataList;
    }

    public JsonShopsPropertyPriceOrQtyGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new ArrayList<>();
        if (jsonObj != null) {
            try {
                JSONArray joDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < joDataList.length(); i++) {
                    JSONObject jsonObject = joDataList.getJSONObject(i);
                    DataItem item = new DataItem();
                    item.setPraId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_ID));
                    item.setPraName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_NAME));
                    if (jsonObject.has(JsonKey.SHOPS_PRA_LEVEL)) {
                        item.setPraLevel(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_LEVEL));

                    }
                    if (jsonObject.has(JsonKey.SHOPS_PRA_QTY)) {
                        item.setPraQty(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_QTY));
                    }
                    if (jsonObject.has(JsonKey.SHOPS_PRA_PRICE)) {
                        item.setPraPrice(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_PRICE));
                    }
                    ArrayList<DataItem> items = new ArrayList<>();
                    if (jsonObject.has(JsonKey.SHOPS_SUB_CLASS)) {
                        JSONArray joSubclass = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_SUB_CLASS);
                        setChildItem(joSubclass, items);
                    }
                    item.setSubClass(items);
                    dataList.add(item);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    private void setChildItem(JSONArray joSubclass, List<DataItem> items) {
        try {
            for (int i = 0; i < joSubclass.length(); i++) {
                JSONObject jsonObject = joSubclass.getJSONObject(i);
                DataItem item = new DataItem();
                item.setPraId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_ID));
                item.setPraName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_NAME));
                if (jsonObject.has(JsonKey.SHOPS_PRA_LEVEL)) {
                    item.setPraLevel(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_LEVEL));
                }
                if (jsonObject.has(JsonKey.SHOPS_PRA_QTY)) {
                    item.setPraQty(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_QTY));
                }
                if (jsonObject.has(JsonKey.SHOPS_PRA_PRICE)) {
                    item.setPraPrice(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_PRA_PRICE));
                }
                if (jsonObject.has(JsonKey.SHOPS_SUB_CLASS)) {
                    List<DataItem> childItems = new ArrayList<>();
                    JSONArray joItemSubclass = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.SHOPS_SUB_CLASS);
                    setChildItem(joItemSubclass, childItems);
                    item.setSubClass(childItems);
                } else {
                    items.add(item);
                    continue;
                }
                items.add(item);
            }

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    public class DataItem implements Serializable {
        private int praId;
        private String praName;
        private String praQty;
        private String praPrice;


        private int praLevel;
        private List<DataItem> subClass;

        public int getPraId() {
            return praId;
        }

        public int getPraLevel() {
            return praLevel;
        }

        public void setPraLevel(int praLevel) {
            this.praLevel = praLevel;
        }
        public void setPraId(int praId) {
            this.praId = praId;
        }

        public String getPraName() {
            return praName;
        }

        public void setPraName(String praName) {
            this.praName = praName;
        }

        public String getPraQty() {
            return praQty;
        }

        public void setPraQty(String praQty) {
            this.praQty = praQty;
        }

        public String getPraPrice() {
            return praPrice;
        }

        public void setPraPrice(String praPrice) {
            this.praPrice = praPrice;
        }

        public List<DataItem> getSubClass() {
            return subClass;
        }

        public void setSubClass(List<DataItem> subClass) {
            this.subClass = subClass;
        }
    }


}
