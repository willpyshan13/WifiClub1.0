/**
 * Project Name: itee
 * File Name:	 JsonPromote.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonPromote <br/>
 * Function: entity of promote get. <br/>
 * Date: 2015-03-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonPromote extends BaseJsonObject implements Serializable {

    private ArrayList<Promote> promoteList;
    private int page;

    public ArrayList<Promote> getPromoteList() {
        return promoteList;
    }

    public JsonPromote(JSONObject jsonObject) {
        super(jsonObject);
    }

    public int getPage() {
        return page;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            Utils.log(jsonObj.toString());

            promoteList = new ArrayList<>();

            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                page = Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.COMMON_PAGE);
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.SHOPS_PROMOTE_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    JSONObject joPromote = arrDataList.getJSONObject(i);
                    Promote promote = new Promote();

                    promote.promoteId = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_PROMOTE_ID);
                    promote.caddieFlag = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_CADDIE_FLAG);
                    promote.promoteName = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_PROMOTE_NAME);
                    promote.startDate = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_START_DATE);
                    promote.endDate = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_END_DATE);
                    promote.qty = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_QTY);
                    promote.code = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_CODE);
                    promote.price = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_PRICE);
                    promote.promotePrice = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST_PROMOTE_PRICE);
                    promote.promoteImg = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST_PROMOTE_IMG);
                    promote.attrStatus = Utils.getIntegerFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST_ATTR_STATUS);
                    promote.unlimitedFlag = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_UNLIMITED_FLAG);
                    promote.promoteAttr = Utils.getStringFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_ATTR);

                    JSONArray arrProduct = Utils.getArrayFromJsonObjectWithKey(joPromote, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST);
                    for (int j = 0; j < arrProduct.length(); j++) {
                        JSONObject joProduct = arrProduct.getJSONObject(j);
                        Promote.Product product = new Promote.Product();
                        product.id = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST_ID);
                        product.productId = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST_PRODUCT_ID);
                        product.pdName = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST_PD_NAME);
                        product.productAttr = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST_PRODUCT_ATTR);
                        product.promoteAttrId = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST_PRODUCT_ATTR_ID);
                        product.price = Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST_PRICE);
                        product.type = Utils.getIntegerFromJsonObjectWithKey(joProduct, JsonKey.SHOPS_PROMOTE_PRODUCT_LIST_TYPE);
                        promote.productList.add(product);
                    }

                    promoteList.add(promote);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public static class Promote implements Serializable {

        public Promote() {
            productList = new ArrayList<>();
        }

        private String promoteId;
        private String promoteName;
        private String startDate;
        private String endDate;
        private String qty;
        private String code;
        private String promoteAttr;

        private String promoteImg;
        private String promotePrice;
        private String price;
        private String caddieFlag;
        private Integer attrStatus;
        private ArrayList<Product> productList;


        private String unlimitedFlag;

        public String getCaddieFlag() {
            return caddieFlag;
        }

        public String getUnlimitedFlag() {
            return unlimitedFlag;
        }

        public String getProductAttr() {
            return promoteAttr;
        }

        public static class Product implements Serializable {

            private String id;
            private String productId;
            private String pdName;
            private String productAttr;
            private String price;
            private int type;
            private String promoteAttrId;
            private String promotePrice;

            public String getId() {
                return id;
            }

            public String getProductId() {
                return productId;
            }

            public String getPdName() {
                return pdName;
            }

            public String getProductAttr() {
                return productAttr;
            }

            public String getPrice() {
                return price;
            }

            public String getPromoteAttrId() {
                return promoteAttrId;
            }

            public String getPromotePrice() {
                return promotePrice;
            }

            public int getType() {
                return type;
            }
        }

        public String getPromotePrice() {
            return promotePrice;
        }

        public String getPromoteId() {
            return promoteId;
        }

        public String getPromoteName() {
            return promoteName;
        }

        public String getStartDate() {
            return startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public String getQty() {
            return qty;
        }

        public String getCode() {
            return code;
        }

        public String getPrice() {
            return price;
        }

        public String getPromoteImg() {
            return promoteImg;
        }

        public Integer getAttrStatus() {
            return attrStatus;
        }

        public ArrayList<Product> getProductList() {
            return productList;
        }

    }
}





