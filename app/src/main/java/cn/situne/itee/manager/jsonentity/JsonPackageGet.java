/**
 * Project Name: itee
 * File Name:  JsonPackageGet.java
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

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonPackageGet <br/>
 * Function: To get package. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonPackageGet extends BaseJsonObject implements Serializable {

    private int page;
    private ArrayList<PackageData> packageList;
    private String greenId;

    public String getGreenId() {
        return greenId;
    }

    public void setGreenId(String greenId) {
        this.greenId = greenId;
    }

    public ArrayList<PackageData> getPackageList() {
        return packageList;
    }

    public void setPackageList(ArrayList<PackageData> packageList) {
        this.packageList = packageList;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public JsonPackageGet(JSONObject jsonObject) {
        super(jsonObject);
    }


    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        packageList = new ArrayList<>();

        try {
            JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            setPage(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.COMMON_PAGE));
            setGreenId(Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.COMMON_GREEN_ID));

            JSONArray arrPackageList = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.SHOPS_PACKAGE_LIST);
            for (int i = 0; i < arrPackageList.length(); i++) {
                JSONObject jsPackageObject = arrPackageList.getJSONObject(i);
                PackageData packageData = new PackageData();
                packageData.setPackageCode(Utils.getStringFromJsonObjectWithKey(jsPackageObject, JsonKey.SHOPS_PACKAGE_CODE));
                packageData.setPackageId(Utils.getIntegerFromJsonObjectWithKey(jsPackageObject, JsonKey.SHOPS_PACKAGE_PACKAGE_ID));
                packageData.setPackageName(Utils.getStringFromJsonObjectWithKey(jsPackageObject, JsonKey.SHOPS_PACKAGE_PACKAGE_NAME));
                packageData.setPackagePrice(Utils.getDoubleFromJsonObjectWithKey(jsPackageObject, JsonKey.SHOPS_PACKAGE_PRICE));
                packageData.setPackageQty(Utils.getIntegerFromJsonObjectWithKey(jsPackageObject, JsonKey.SHOPS_PACKAGE_QTY));
                packageData.setUnlimitedFlag(Utils.getStringFromJsonObjectWithKey(jsPackageObject, JsonKey.SHOPS_UNLIMITED_FLAG));
                ArrayList<DataItem> productList = new ArrayList<>();
                JSONArray jsProductArray = Utils.getArrayFromJsonObjectWithKey(jsPackageObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST);
                for (int j = 0; j < jsProductArray.length(); j++) {
                    JSONObject jsProductObject = jsProductArray.getJSONObject(j);
                    DataItem item = new DataItem();
                    item.setId(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_ID));
                    item.setNumber(Utils.getIntegerFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_NUMBER));
                    item.setPdName(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PD_NAME));
                    item.setPrice(Utils.getDoubleFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PRICE));
                    item.setProductAttr(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PRODUCT_ATTR));
                    item.setProductAttrId(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PRODUCT_ATTR_ID));
                    item.setProductId(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_PRODUCT_ID));
                    item.setType(Utils.getIntegerFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_TYPE));
                    item.setAttriStatus(Utils.getIntegerFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_ATTR_STATUS));
                    item.setDiscountPrice(Utils.getDoubleFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_DISCOUNT_PRICE));

                    item.setTypeTagId(Utils.getStringFromJsonObjectWithKey(jsProductObject, JsonKey.SHOPS_PACKAGE_PRODUCT_LIST_TYPE_TAG_ID));


                    productList.add(item);

                }
                packageData.setJsProductItem(jsProductArray);
                packageData.setProductList(productList);
                packageList.add(packageData);
            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }


    }


    public class PackageData implements Serializable {

        private int packageId;
        private String packageName;
        private double packagePrice;
        private int packageQty;
        private String packageCode;
        private String unlimitedFlag;
        private ArrayList<DataItem> productList;

        private JSONArray jsProductItem;

        public JSONArray getJsProductItem() {
            return jsProductItem;
        }

        public String getUnlimitedFlag() {
            return unlimitedFlag;
        }

        public void setUnlimitedFlag(String unlimitedFlag) {
            this.unlimitedFlag = unlimitedFlag;
        }

        public void setJsProductItem(JSONArray jsProductItem) {
            this.jsProductItem = jsProductItem;
        }

        public int getPackageId() {
            return packageId;
        }

        public void setPackageId(int packageId) {
            this.packageId = packageId;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public double getPackagePrice() {
            return packagePrice;
        }

        public void setPackagePrice(double packagePrice) {
            this.packagePrice = packagePrice;
        }

        public int getPackageQty() {
            return packageQty;
        }

        public void setPackageQty(int packageQty) {
            this.packageQty = packageQty;
        }

        public String getPackageCode() {
            return packageCode;
        }

        public void setPackageCode(String packageCode) {
            this.packageCode = packageCode;
        }

        public ArrayList<DataItem> getProductList() {
            return productList;
        }

        public void setProductList(ArrayList<DataItem> productList) {
            this.productList = productList;
        }
    }


    public static class DataItem implements Serializable {
        private String id;
        private double price;
        private String productAttrId;
        private String productId;
        private String productAttr;
        private Integer attriStatus;
        private int number;
        private int type;

        private double minPrice;
        private double maxPrice;

        private double discountPrice;

        private String typeTagId;//shop ID

        public Integer getAttriStatus() {
            return attriStatus;
        }

        public void setAttriStatus(Integer attriStatus) {
            this.attriStatus = attriStatus;
        }

        public String getTypeTagId() {
            return typeTagId;
        }

        public void setTypeTagId(String typeTagId) {
            this.typeTagId = typeTagId;
        }

        public double getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(double discountPrice) {
            this.discountPrice = discountPrice;
        }

        public double getMinPrice() {
            return minPrice;
        }

        public void setMinPrice(double minPrice) {
            this.minPrice = minPrice;
        }

        public double getMaxPrice() {
            return maxPrice;
        }

        public void setMaxPrice(double maxPrice) {
            this.maxPrice = maxPrice;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getProductAttrId() {
            return productAttrId;
        }

        public void setProductAttrId(String productAttrId) {
            this.productAttrId = productAttrId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getProductAttr() {
            return productAttr;
        }

        public void setProductAttr(String productAttr) {
            this.productAttr = productAttr;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getPdName() {
            return pdName;
        }

        public void setPdName(String pdName) {
            this.pdName = pdName;
        }

        private String pdName;


    }
}





