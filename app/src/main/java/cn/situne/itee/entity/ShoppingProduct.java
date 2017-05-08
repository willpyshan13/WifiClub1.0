/**
 * Project Name: itee
 * File Name:	 ShoppingProduct.java
 * Package Name: cn.situne.itee.entity
 * Date:		 2015-04-26
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ClassName:ShoppingProduct <br/>
 * Function: Shopping entity of product. <br/>
 * Date: 2015-04-26 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShoppingProduct implements Serializable {

    public ShoppingProduct() {
        productList = new ArrayList<>();
    }

    private String packageId;
    private String promoteId;
    private String productId;
    private String productAttribute;
    private String productName;
    private String attributeName;
    private int productNumber;
    private String productPrice;
    private String promotePrice;
    private boolean isCaddie;

    public boolean isCaddie() {
        return isCaddie;
    }

    public void setIsCaddie(boolean isCaddie) {
        this.isCaddie = isCaddie;
    }

    private ArrayList<ShoppingSubProduct> productList;

    public String getPromoteId() {
        return promoteId;
    }

    public void setPromoteId(String promoteId) {
        this.promoteId = promoteId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductAttribute() {
        return productAttribute;
    }

    public void setProductAttribute(String productAttribute) {
        this.productAttribute = productAttribute;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductList(ArrayList<ShoppingSubProduct> productList) {
        this.productList = productList;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getPromotePrice() {
        return promotePrice;
    }

    public void setPromotePrice(String promotePrice) {
        this.promotePrice = promotePrice;
    }

    public int getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(int productNumber) {
        this.productNumber = productNumber;
    }

    public ArrayList<ShoppingSubProduct> getProductList() {
        return productList;
    }

    public static class ShoppingSubProduct implements Serializable {

        private String id;
        private String productId;
        private String productName;
        private String productAttr;
        private String number;
        private String price;
        private String promotePrice;
        private String type;
        private String productAttrId;

        private String attriStatus;

        private String productStatus;

        public String getAttriStatus() {
            return attriStatus;
        }

        public void setAttriStatus(String attriStatus) {
            this.attriStatus = attriStatus;
        }

        public String getProductAttrId() {
            return productAttrId == null ? "0" : productAttrId;
        }

        public void setProductAttrId(String productAttrId) {
            this.productAttrId = productAttrId;
        }

        public String getProductStatus() {
            return productStatus;
        }

        public void setProductStatus(String productStatus) {
            this.productStatus = productStatus;
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

            return price == null ? "0" : price;
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