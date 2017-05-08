/**
 * Project Name: itee
 * File Name:	 JsonChooseProduct.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-04-02
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonChooseProduct <br/>
 * Function: entity of api chooseProduct. <br/>
 * Date: 2015-04-02 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
@SuppressWarnings("unused")
public class JsonChooseProduct extends BaseJsonObject implements Serializable {

    private static final long serialVersionUID = 7377740596296526768L;

    public JsonChooseProduct(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        if (jsonObj != null) {
            salesTypeList = new ArrayList<>();
            Utils.log(jsonObj.toString());
            try {
                JSONObject joDataList = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);

                greenId = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SHOPS_GREEN_ID);
                caddieId = Utils.getStringFromJsonObjectWithKey(joDataList, JsonKey.SHOPS_CADDIE_ID);
                setDataStatus(Utils.getIntegerFromJsonObjectWithKey(joDataList, JsonKey.SHOPS_DATA_STATUS));

                JSONArray arrSalesType = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.SHOPS_CHOOSE_PRODUCT_SALES_TYPE);
                for (int i = 0; i < arrSalesType.length(); i++) {
                    JSONObject joSalesType = arrSalesType.getJSONObject(i);
                    SalesType salesType = new SalesType();
                    salesType.name = Utils.getStringFromJsonObjectWithKey(joSalesType, JsonKey.SHOPS_CHOOSE_PRODUCT_NAME);
                    salesType.salesTypeId = Utils.getStringFromJsonObjectWithKey(joSalesType, JsonKey.SHOPS_CHOOSE_PRODUCT_SALES_TYPE_ID);
                    if (JsonKey.SHOPS_CHOOSE_PRODUCT_RENTAL_PRODUCT.equals(salesType.name)) {
                        salesType.isRental = true;
                    } else if (JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT.equals(salesType.name)) {
                        salesType.isPackage = true;
                    } else if (JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT.equals(salesType.name)) {
                        salesType.isPromote = true;
                    }
                    JSONArray arrSalesProducts = Utils.getArrayFromJsonObjectWithKey(joSalesType, JsonKey.SHOPS_CHOOSE_PRODUCT_LIST);
                    for (int j = 0; j < arrSalesProducts.length(); j++) {
                        JSONObject joSalesProduct = arrSalesProducts.getJSONObject(j);
                        SalesType.SalesTypeProduct product = new SalesType.SalesTypeProduct();
                        if (JsonKey.SHOPS_CHOOSE_PRODUCT_RENTAL_PRODUCT.equals(salesType.name)) {
                            product.id = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_RENTAL_PRODUCT_PD_ID);
                            product.imgId = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_RENTAL_PRODUCT_PD_PIC_ID);
                            product.name = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_RENTAL_PRODUCT_PRODUCT_NAME);
                            String sort = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_RENTAL_SORT);
                            if (Constants.RENTAL_PRODUCT_TYPE_CADDIE.equals(sort)) {
                                product.name = Constants.RENTAL_PRODUCT_NAME_CADDIE + product.name;
                                product.isCaddie = true;
                            }
                            product.price = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PRODUCT_PRICE);
                            product.attrId = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_RENTAL_PRODUCT_ATTR_ID);
                            product.attrCount = Utils.getIntegerFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_ATTR_COUNT);
                            product.attriStatus = Utils.getIntegerFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPPING_ATTRI_STATUS);
                            product.qty = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_QTY);
                            product.propertyPriceStatus = Utils.getIntegerFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PROPERTY_PRICE_STATUS);
                            product.unlimitedFlag = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPPING_UNLIMITED_FLAG);
                            if (product.propertyPriceStatus == 1) {
                                String maxPrice = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_MAX_PRICE);
                                String minPrice = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_MIN_PRICE);
                                product.price = minPrice + Constants.STR_WAVE + maxPrice;
                            }

                        } else if (JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT.equals(salesType.name)) {
                            product.id = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_PACKAGE_ID);
                            product.name = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_PACKAGE_NAME);
                            product.qty = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_QTY);
                            product.code = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_CODE);
                            product.price = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_PRICE);
                            product.unlimitedFlag = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPPING_UNLIMITED_FLAG);
                            JSONArray arrProductItem = Utils.getArrayFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_LIST);
                            for (int k = 0; k < arrProductItem.length(); k++) {
                                JSONObject joProductItem = arrProductItem.getJSONObject(k);
                                SalesType.SalesTypeProduct.SalesTypeSubProduct subProduct = new SalesType.SalesTypeProduct.SalesTypeSubProduct();
                                subProduct.id = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_LIST_ID);
                                subProduct.productId = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_LIST_PRODUCT_ID);
                                subProduct.productName = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_LIST_PD_NAME);
                                subProduct.productAttr = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_LIST_PRODUCT_ATTR);
                                subProduct.price = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_LIST_PRICE);
                                subProduct.number = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_LIST_NUMBER);
                                subProduct.type = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_LIST_TYPE);
                                subProduct.setAttriStatus(Utils.getIntegerFromJsonObjectWithKey(joProductItem, JsonKey.SHOPPING_ATTRI_STATUS));
                                subProduct.setProductAttrId(Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_LIST_PRODUCT_ATTR_ID));
                                product.subProductList.add(subProduct);
                            }
                        } else if (JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT.equals(salesType.name)) {
                            product.id = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_PROMOTE_ID);
                            product.imgPath = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_PROMOTE_IMG);
                            product.name = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_PROMOTE_NAME);
                            product.startDate = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_START_DATE);
                            product.endDate = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_END_DATE);
                            product.qty = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_QTY);
                            product.code = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_CODE);
                            product.price = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_PROMOTE_PRICE);
                            product.attriStatus = Utils.getIntegerFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPPING_ATTRI_STATUS);
                            product.unlimitedFlag = Utils.getStringFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPPING_UNLIMITED_FLAG);
                            JSONArray arrProductItem = Utils.getArrayFromJsonObjectWithKey(joSalesProduct, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_LIST);
                            for (int k = 0; k < arrProductItem.length(); k++) {
                                JSONObject joProductItem = arrProductItem.getJSONObject(k);
                                SalesType.SalesTypeProduct.SalesTypeSubProduct subProduct = new SalesType.SalesTypeProduct.SalesTypeSubProduct();
                                subProduct.id = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_LIST_ID);
                                subProduct.productId = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_LIST_PRODUCT_ID);
                                subProduct.productName = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_LIST_PD_NAME);
                                subProduct.productAttr = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_LIST_PRODUCT_ATTR);
                                subProduct.price = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_LIST_PRICE);
                                subProduct.promotePrice = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_LIST_PROMOTE_PRICE);
                                subProduct.type = Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PROMOTE_PRODUCT_LIST_TYPE);
                                subProduct.setProductAttrId(Utils.getStringFromJsonObjectWithKey(joProductItem, JsonKey.SHOPS_CHOOSE_PRODUCT_PACKAGE_PRODUCT_LIST_PRODUCT_ATTR_ID));
                                product.subProductList.add(subProduct);
                            }
                        }
                        salesType.salesTypeList.add(product);
                    }
                    salesTypeList.add(salesType);
                }

                String promoteName = Constants.STR_EMPTY;
                String packageName = Constants.STR_EMPTY;
                String rentalName = Constants.STR_EMPTY;
                JSONArray arrProductType = Utils.getArrayFromJsonObjectWithKey(joDataList, JsonKey.SHOPS_CHOOSE_PRODUCT_TYPE_TAG);
                for (int i = 0; i < arrProductType.length(); i++) {
                    JSONObject joProductType = arrProductType.getJSONObject(i);
                    String typeId = Utils.getStringFromJsonObjectWithKey(joProductType, JsonKey.SHOPS_CHOOSE_PRODUCT_TYPE_ID);
                    if (Constants.SHOP_TYPE_RENTAL.equals(typeId)) {
                        rentalName = Utils.getStringFromJsonObjectWithKey(joProductType, JsonKey.SHOPS_CHOOSE_PRODUCT_TYPE_NAME);
                        continue;
                    } else if (Constants.SHOP_TYPE_PROMOTE.equals(typeId)) {
                        promoteName = Utils.getStringFromJsonObjectWithKey(joProductType, JsonKey.SHOPS_CHOOSE_PRODUCT_TYPE_NAME);
                        continue;
                    } else if (Constants.SHOP_TYPE_PACKAGE.equals(typeId)) {
                        packageName = Utils.getStringFromJsonObjectWithKey(joProductType, JsonKey.SHOPS_CHOOSE_PRODUCT_TYPE_NAME);
                        continue;
                    }
                    SalesType salesType = new SalesType();
                    salesType.salesTypeId = typeId;
                    salesType.name = Utils.getStringFromJsonObjectWithKey(joProductType, JsonKey.SHOPS_CHOOSE_PRODUCT_TYPE_NAME);
                    salesTypeList.add(salesType);
                }

                for (int j = 0; j < salesTypeList.size(); j++) {
                    SalesType salesType = salesTypeList.get(j);
                    if (Constants.SHOP_TYPE_RENTAL.equals(salesType.getSalesTypeId())) {
                        salesType.name = rentalName;
                    } else if (Constants.SHOP_TYPE_PROMOTE.equals(salesType.getSalesTypeId())) {
                        salesType.name = promoteName;
                    } else if (Constants.SHOP_TYPE_PACKAGE.equals(salesType.getSalesTypeId())) {
                        salesType.name = packageName;
                    }
                }

            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
    }

    private String greenId;
    private String caddieId;
    private ArrayList<SalesType> salesTypeList;

    private int dataStatus;

    public ArrayList<SalesType> getSalesTypeList() {
        return salesTypeList;
    }

    public String getGreenId() {
        return greenId;
    }

    public String getCaddieId() {
        return caddieId;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public static class SalesType implements Serializable {

        private static final long serialVersionUID = 6392717042604694586L;

        public SalesType() {
            salesTypeList = new ArrayList<>();
        }

        private String salesTypeId;
        private String name;
        private boolean isRental;
        private boolean isPromote;
        private boolean isPackage;
        private ArrayList<SalesTypeProduct> salesTypeList;

        public String getSalesTypeId() {
            return salesTypeId;
        }

        public String getName() {
            return name;
        }

        public boolean isRental() {
            return isRental;
        }

        public boolean isPromote() {
            return isPromote;
        }

        public boolean isPackage() {
            return isPackage;
        }

        public ArrayList<SalesTypeProduct> getSalesTypeList() {
            return salesTypeList;
        }


        public static class SalesTypeProduct implements Serializable {

            public SalesTypeProduct() {
                subProductList = new ArrayList<>();
            }

            public SalesTypeProduct(SalesTypeProduct another) {
                id = another.getId();
                name = another.getName();
                attrId = another.getAttrId();
                qty = another.getQty();
                code = another.getCode();
                price = another.getPrice();
                startDate = another.getStartDate();
                endDate = another.getEndDate();
                imgPath = another.getImgPath();
                imgId = another.getImgId();
                attrCount = another.getAttrCount();
                attriStatus = another.getAttriStatus();
                unlimitedFlag =another.getUnlimitedFlag();
                propertyPriceStatus = another.getPropertyPriceStatus();
                isCaddie = another.isCaddie();
                subProductList = new ArrayList<>();
                subProductList.addAll(another.getSubProductList());
            }

            private String id;
            private String name;

            private String attrId;

            private String qty;
            private String code;
            private String price;

            private String startDate;
            private String endDate;

            private String imgPath;
            private String imgId;

            private boolean isCaddie;

            private int attrCount;
            private int propertyPriceStatus;

            private ArrayList<SalesTypeSubProduct> subProductList;

            //song add.
            private Integer attriStatus;
            private String unlimitedFlag;

            public String getUnlimitedFlag() {
                return unlimitedFlag;
            }

            public void setUnlimitedFlag(String unlimitedFlag) {
                this.unlimitedFlag = unlimitedFlag;
            }

            public Integer getAttriStatus() {
                return attriStatus;
            }

            public void setAttriStatus(Integer attriStatus) {
                this.attriStatus = attriStatus;
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

            public String getPrice() {
                return price;
            }

            public String getStartDate() {
                return startDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public String getImgPath() {
                return imgPath;
            }

            public String getImgId() {
                return imgId;
            }

            public int getAttrCount() {
                return attrCount;
            }

            public int getPropertyPriceStatus() {
                return propertyPriceStatus;
            }

            public ArrayList<SalesTypeSubProduct> getSubProductList() {
                return subProductList;
            }

            public void setId(String id) {
                this.id = id;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setQty(String qty) {
                this.qty = qty;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public void setImgPath(String imgPath) {
                this.imgPath = imgPath;
            }

            public void setImgId(String imgId) {
                this.imgId = imgId;
            }

            public void setAttrCount(int attrCount) {
                this.attrCount = attrCount;
            }

            public void setPropertyPriceStatus(int propertyPriceStatus) {
                this.propertyPriceStatus = propertyPriceStatus;
            }

            public String getAttrId() {
                return attrId;
            }

            public void setAttrId(String attrId) {
                this.attrId = attrId;
            }

            public boolean isCaddie() {
                return isCaddie;
            }

            public static class SalesTypeSubProduct {

                private String id;
                private String productId;
                private String productName;
                private String productAttr;
                private String number;
                private String price;
                private String promotePrice;
                private String type;


                //song add.
                private Integer attriStatus;

                public Integer getAttriStatus() {
                    return attriStatus;
                }


                private String productAttrId;

                public String getProductAttrId() {
                    return productAttrId;
                }

                public void setProductAttrId(String productAttrId) {
                    this.productAttrId = productAttrId;
                }


                public void setAttriStatus(Integer attriStatus) {
                    this.attriStatus = attriStatus;
                }


                public String getId() {
                    return id;
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

                public String getNumber() {
                    return number;
                }

                public String getPrice() {
                    return price;
                }

                public String getPromotePrice() {
                    return promotePrice;
                }

                public String getType() {
                    return type;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public void setProductId(String productId) {
                    this.productId = productId;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
                }

                public void setProductAttr(String productAttr) {
                    this.productAttr = productAttr;
                }

                public void setNumber(String number) {
                    this.number = number;
                }

                public void setPrice(String price) {
                    this.price = price;
                }

                public void setPromotePrice(String promotePrice) {
                    this.promotePrice = promotePrice;
                }

                public void setType(String type) {
                    this.type = type;
                }
            }
        }
    }
}