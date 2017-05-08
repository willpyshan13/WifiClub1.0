/**
 * Project Name: itee
 * File Name:	 ShopsProduct.java
 * Package Name: cn.situne.itee.entity
 * Date:		 2015-04-09
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.entity;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.Constants;

/**
 * ClassName:ShopsProduct <br/>
 * Function: FIXME. <br/>
 * Date: 2015-04-09 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsProduct implements Serializable {


    private String memberDiscount;
    private String memberDiscountType;
    private String guestDiscount;
    private String guestDiscountType;

    public String getMemberDiscount() {
        return memberDiscount;
    }

    public void setMemberDiscount(String memberDiscount) {
        this.memberDiscount = memberDiscount;
    }

    public String getMemberDiscountType() {
        return memberDiscountType;
    }

    public void setMemberDiscountType(String memberDiscountType) {
        this.memberDiscountType = memberDiscountType;
    }

    public String getGuestDiscount() {
        return guestDiscount;
    }

    public void setGuestDiscount(String guestDiscount) {
        this.guestDiscount = guestDiscount;
    }

    public String getGuestDiscountType() {
        return guestDiscountType;
    }

    public void setGuestDiscountType(String guestDiscountType) {
        this.guestDiscountType = guestDiscountType;
    }

    public void setType(int type) {
        this.type = type;
    }

    private String productId;
    private String attrId;
    private String productName;
    private Integer productNumber;
    private String productPrice;
    private String packageId;
    private int type;
    private String typeTagId;//shopId
    private String parentId;

    private String discountPrice;
    private boolean isCaddie;


    private String unlimitedFlag;
    private String qty;

    public String getUnlimitedFlag() {
        return unlimitedFlag;
    }

    public void setUnlimitedFlag(String unlimitedFlag) {
        this.unlimitedFlag = unlimitedFlag;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public boolean isCaddie() {
        return isCaddie;
    }

    public void setIsCaddie(boolean isCaddie) {
        this.isCaddie = isCaddie;
    }

    private ArrayList<ShopsSubProduct> productList;

    public ArrayList<ShopsSubProduct> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<ShopsSubProduct> productList) {
        this.productList = productList;
    }

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(Integer productNumber) {
        this.productNumber = productNumber;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getAttrId() {
        return attrId;
    }

    public void setAttrId(String attrId) {
        this.attrId = attrId;
    }

    public String getTypeTagId() {
        return typeTagId;
    }

    public void setTypeTagId(String typeTagId) {
        this.typeTagId = typeTagId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPackageId() {
        return packageId == null ? Constants.STR_0 : packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public int getType() {
        if (attrId == null || Constants.STR_0.equals(attrId)) {
            type = 1;
        } else {
            type = 2;
        }
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ShopsProduct) {
            ShopsProduct shopsProduct = (ShopsProduct) o;
            if (productId != null && productId.equals(shopsProduct.getProductId()) && attrId != null && attrId.equals(shopsProduct.getAttrId())) {
                return true;
            }
        }
        return false;
    }

    public static class ShopsSubProduct implements Serializable {

        private String memberDiscount;
        private String memberDiscountType;
        private String guestDiscount;
        private String guestDiscountType;

        public String getMemberDiscount() {
            return memberDiscount;
        }

        public void setMemberDiscount(String memberDiscount) {
            this.memberDiscount = memberDiscount;
        }

        public String getMemberDiscountType() {
            return memberDiscountType;
        }

        public void setMemberDiscountType(String memberDiscountType) {
            this.memberDiscountType = memberDiscountType;
        }

        public String getGuestDiscount() {
            return guestDiscount;
        }

        public void setGuestDiscount(String guestDiscount) {
            this.guestDiscount = guestDiscount;
        }

        public String getGuestDiscountType() {
            return guestDiscountType;
        }

        public void setGuestDiscountType(String guestDiscountType) {
            this.guestDiscountType = guestDiscountType;
        }

        private String id;
        private String productId;
        private String productName;
        private String productPrice;

        private String productAttr;

        public String getProductAttr() {
            return productAttr;
        }

        public void setProductAttr(String productAttr) {
            this.productAttr = productAttr;
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
    }


}