/**
 * Project Name: itee
 * File Name:	 JsonAgentsProductGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-03-23
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
 * ClassName:JsonAgentsProductGet <br/>
 * Function: Get the entity of api agentsProduct. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonAgentsProductGet extends BaseJsonObject implements Serializable {


    private ArrayList<AgentProductShop> dataList;

    public JsonAgentsProductGet(JSONObject jsonObject) {
        super(jsonObject);
        dataList = new ArrayList<>();
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new ArrayList<>();
            try {
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    JSONObject joShop = arrDataList.getJSONObject(i);
                    AgentProductShop agentProductShop = new AgentProductShop();
                    agentProductShop.setShopId(Utils.getIntegerFromJsonObjectWithKey(joShop, JsonKey.COMMON_SHOP_ID));
                    agentProductShop.setShopName(Utils.getStringFromJsonObjectWithKey(joShop, JsonKey.COMMON_SHOP_NAME));
                    try {
                        JSONArray arrProductList = Utils.getArrayFromJsonObjectWithKey(joShop, JsonKey.AGENT_PRODUCT_LIST);
                        for (int j = 0; j < arrProductList.length(); j++) {
                            JSONObject joProduct = arrProductList.getJSONObject(j);
                            AgentProductShop.ProductInfo productInfo = new AgentProductShop.ProductInfo();
                            productInfo.setProductId(Utils.getIntegerFromJsonObjectWithKey(joProduct, JsonKey.COMMON_PRODUCT_ID));
                            productInfo.setProductName(Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.COMMON_PRODUCT_NAME));
                            productInfo.setProductPrice(Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.COMMON_PRODUCT_PRICE));
                            productInfo.setProductMoneyType(Utils.getStringFromJsonObjectWithKey(joProduct, JsonKey.COMMON_PRODUCT_MONEY_TYPE));
                            productInfo.setProductChildren(Utils.getIntegerFromJsonObjectWithKey(joProduct, JsonKey.COMMON_PRODUCT_CHILDREN));
                            agentProductShop.getAgentProductList().add(productInfo);
                        }
                    } catch (JSONException e) {
                        Utils.log(e.getMessage());
                    }
                    dataList.add(agentProductShop);
                }

            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public ArrayList<AgentProductShop> getDataList() {
        return dataList;
    }

    public static class AgentProductShop implements Serializable {

        public AgentProductShop() {
            productInfoList = new ArrayList<>();
        }

        private Integer shopId;
        private String shopName;
        private ArrayList<ProductInfo> productInfoList;

        public Integer getShopId() {
            return shopId;
        }

        public void setShopId(Integer shopId) {
            this.shopId = shopId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public ArrayList<ProductInfo> getAgentProductList() {
            return productInfoList;
        }

        public static class ProductInfo implements Serializable {
            private Integer productId;
            private String productName;
            private String productPrice;
            private String productMoneyType;
            private Integer productChildren;

            public Integer getProductId() {
                return productId;
            }

            public void setProductId(Integer productId) {
                this.productId = productId;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getProductPrice() {
                return productPrice;
            }

            public void setProductPrice(String productPrice) {
                this.productPrice = productPrice;
            }

            public String getProductMoneyType() {
                return productMoneyType;
            }

            public void setProductMoneyType(String productMoneyType) {
                this.productMoneyType = productMoneyType;
            }

            public Integer getProductChildren() {
                return productChildren;
            }

            public void setProductChildren(Integer productChildren) {
                this.productChildren = productChildren;
            }
        }
    }


}