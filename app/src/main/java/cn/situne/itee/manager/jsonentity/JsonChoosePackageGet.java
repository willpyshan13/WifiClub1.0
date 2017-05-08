/**
 * Project Name: itee
 * File Name:	 JsonChoosePackageGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-05-06
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
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
 * ClassName:JsonChoosePackageGet <br/>
 * Function: choosePackage 接口 实体类 <br/>
 * UI:  06-7
 * Date: 2015-05-06 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonChoosePackageGet extends BaseJsonObject implements Serializable {
    private ArrayList<Package> dataList;

    public ArrayList<Package> getDataList() {
        return dataList;
    }

    public void setDataList(ArrayList<Package> dataList) {
        this.dataList = dataList;
    }

    public JsonChoosePackageGet(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            dataList = new ArrayList<Package>();
            try {
                JSONArray arrDataList = Utils.getArrayFromJsonObjectWithKey(jsonObj, JsonKey.COMMON_DATA_LIST);
                for (int i = 0; i < arrDataList.length(); i++) {
                    JSONObject jsPackage = arrDataList.getJSONObject(i);
                    Package productPackage = new Package();
                    productPackage.setPackageId(Utils.getStringFromJsonObjectWithKey(jsPackage, JsonKey.SHOPPING_PACKAGE_ID));
                    productPackage.setPackageName(Utils.getStringFromJsonObjectWithKey(jsPackage, JsonKey.SHOPPING_PACKAGE_NAME));
                    productPackage.setPrice(Utils.getDoubleFromJsonObjectWithKey(jsPackage, JsonKey.SHOPPING_PRICE));


                    JSONArray jsProductList = Utils.getArrayFromJsonObjectWithKey(jsPackage, JsonKey.SHOPPING_PRODUCT_LIST);
                    ArrayList<Product> productArrayList = new ArrayList<Product>();
                    for (int j = 0; j < jsProductList.length(); j++) {

                        JSONObject jsProduct = jsProductList.getJSONObject(j);

                        Product product = new Product();
                        product.setId(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.SHOPPING_ID));
                        product.setNumber(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.SHOPPING_NUMBER));
                        product.setPdName(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.SHOPPING_PD_NAME));
                        product.setPrice(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.SHOPPING_DISCOUNT_PRICE));
                        product.setProductAttr(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.SHOPPING_PRODUCT_ATTR));
                        product.setProductAttrId(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.SHOPPING_PRODUCT_ATTR_ID));
                        product.setProductId(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.SHOPPING_PRODUCT_ID));
                        product.setProductStatus(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.SHOPPING_PRODUCT_STATUS));
                        product.setAttriStatus(Utils.getStringFromJsonObjectWithKey(jsProduct, JsonKey.SHOPPING_ATTRI_STATUS));
                        productArrayList.add(product);
                    }
                    productPackage.setProductArrayList(productArrayList);
                    dataList.add(productPackage);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    public class Package {

        private String packageId;
        private String packageName;
        private double price;
        ArrayList<Product> productArrayList;

        public String getPackageId() {
            return packageId;
        }

        public void setPackageId(String packageId) {
            this.packageId = packageId;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public ArrayList<Product> getProductArrayList() {
            return productArrayList;
        }

        public void setProductArrayList(ArrayList<Product> productArrayList) {
            this.productArrayList = productArrayList;
        }
    }

    public class Product {
        private String id;
        private String productId;
        private String productStatus;
        private String pdName;
        private String productAttr;
        private String productAttrId;
        private String number;

        private String attriStatus;

        public String getAttriStatus() {
            return attriStatus;
        }

        public void setAttriStatus(String attriStatus) {
            this.attriStatus = attriStatus;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductStatus() {
            return productStatus;
        }

        public void setProductStatus(String productStatus) {
            this.productStatus = productStatus;
        }

        public String getPdName() {
            return pdName;
        }

        public void setPdName(String pdName) {
            this.pdName = pdName;
        }

        public String getProductAttr() {
            return productAttr;
        }

        public void setProductAttr(String productAttr) {
            this.productAttr = productAttr;
        }

        public String getProductAttrId() {
            return productAttrId;
        }

        public void setProductAttrId(String productAttrId) {
            this.productAttrId = productAttrId;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        private String price;


    }

}
