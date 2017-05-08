/**
 * Project Name: itee
 * File Name:	 JsonProductSearch.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-08-11
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonProductSearch <br/>
 * Function: entity of productSearch. <br/>
 * Date: 2015-08-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonProductSearch extends BaseJsonObject {

    private ArrayList<SearchProduct> dataList;

    public JsonProductSearch(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        if (jsonObj != null) {
            Utils.log(jsonObj.toString());
            try {
                dataList = new ArrayList<>();
                JSONArray arrDataList = jsonObj.getJSONArray(JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    SearchProduct searchProduct = new SearchProduct();
                    JSONObject joProduct = arrDataList.getJSONObject(i);
                    searchProduct.id = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.PRODUCT_SEARCH_ID);
                    searchProduct.name = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.PRODUCT_SEARCH_NAME);
                    searchProduct.qty = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.PRODUCT_SEARCH_QTY);
                    searchProduct.code = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.PRODUCT_SEARCH_CODE);
                    searchProduct.price = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.PRODUCT_SEARCH_PRICE);
                    searchProduct.type = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.PRODUCT_SEARCH_TYPE);
                    searchProduct.salesTypeId = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.PRODUCT_SEARCH_SALES_TYPE_ID);
                    searchProduct.attrStatus = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.PRODUCT_SEARCH_ATTR_STATUS);
                    if (joProduct.has(JsonKey.PRODUCT_SEARCH_PRODUCT_LIST)) {
                        JSONArray arrSubProduct = joProduct.getJSONArray(JsonKey.PRODUCT_SEARCH_PRODUCT_LIST);
                        for (int j = 0; j < arrSubProduct.length(); j++) {
                            JSONObject joSubProduct = arrSubProduct.getJSONObject(j);
                            SearchProduct.SearchSubProduct subProduct = new SearchProduct.SearchSubProduct();
                            subProduct.id = Utils.getStringFromJsonObjectWithKey(joSubProduct, JsonKey.PRODUCT_SEARCH_PRODUCT_LIST_ID);
                            subProduct.productId = Utils.getStringFromJsonObjectWithKey(joSubProduct, JsonKey.PRODUCT_SEARCH_PRODUCT_LIST_PRODUCT_ID);
                            subProduct.productName = Utils.getStringFromJsonObjectWithKey(joSubProduct, JsonKey.PRODUCT_SEARCH_PRODUCT_LIST_PRODUCT_NAME);
                            subProduct.productAttrId = Utils.getStringFromJsonObjectWithKey(joSubProduct, JsonKey.PRODUCT_SEARCH_PRODUCT_LIST_ATTR_ID);
                            subProduct.productAttr = Utils.getStringFromJsonObjectWithKey(joSubProduct, JsonKey.PRODUCT_SEARCH_PRODUCT_LIST_ATTR_NAME);
                            subProduct.qty = Utils.getStringFromJsonObjectWithKey(joSubProduct, JsonKey.PRODUCT_SEARCH_PRODUCT_LIST_QTY);
                            subProduct.number = Utils.getStringFromJsonObjectWithKey(joSubProduct, JsonKey.PRODUCT_SEARCH_PRODUCT_LIST_NUMBER);
                            subProduct.price = Utils.getStringFromJsonObjectWithKey(joSubProduct, JsonKey.PRODUCT_SEARCH_PRODUCT_LIST_PRICE);
                            subProduct.discountPrice = Utils.getStringFromJsonObjectWithKey(joSubProduct, JsonKey.PRODUCT_SEARCH_PRODUCT_LIST_DISCOUNT_PRICE);
                            subProduct.attrStatus = Utils.getStringFromJsonObjectWithKey(joSubProduct, JsonKey.PRODUCT_SEARCH_PRODUCT_LIST_ATTR_STATUS);
                            searchProduct.getProductList().add(subProduct);
                        }
                    }

                    dataList.add(searchProduct);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<SearchProduct> getDataList() {
        return dataList;
    }

    public static class SearchProduct {
        private String id;
        private String name;
        private String qty;
        private String code;
        private String price;
        private String type;
        private String salesTypeId;
        private String attrStatus;
        private ArrayList<SearchSubProduct> productList;

        private String attrId;

        public SearchProduct() {
            productList = new ArrayList<>();
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getQty() {
            return qty;
        }

        public String getCode() {
            return code;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPrice() {
            return price;
        }

        public String getType() {
            return type;
        }

        public String getSalesTypeId() {
            return salesTypeId;
        }

        public String getAttrId() {
            return attrId;
        }

        public void setAttrId(String attrId) {
            this.attrId = attrId;
        }

        public String getAttrStatus() {
            return attrStatus;
        }

        public ArrayList<SearchSubProduct> getProductList() {
            return productList;
        }

        public static class SearchSubProduct {
            private String id;
            private String productId;
            private String productName;
            private String productAttr;
            private String productAttrId;
            private String price;
            private String qty;
            private String number;
            private String discountPrice;
            private String attrStatus;

            public String getId() {
                return id;
            }

            public String getQty() {
                return qty;
            }

            public String getProductId() {
                return productId;
            }

            public String getProductName() {
                return productName;
            }

            public String getProductAttr() {
                return productAttr;
            }

            public String getProductAttrId() {
                return productAttrId;
            }

            public String getPrice() {
                return price;
            }

            public String getDiscountPrice() {
                return discountPrice;
            }

            public String getAttrStatus() {
                return attrStatus;
            }

            public String getNumber() {
                return number;
            }

            public void setProductAttrId(String productAttrId) {
                this.productAttrId = productAttrId;
            }
        }

    }
}