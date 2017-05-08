/**
 * Project Name: itee
 * File Name:  JsonCommonProduct.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-21
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import java.io.Serializable;
import java.util.ArrayList;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonCommonProduct <br/>
 * Function: To set Common Product. <br/>
 * Date: 2015-03-21 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */
public class JsonCommonProduct implements Serializable {

    private String nonMemberName;

    //product  or
    private int productId;//id
    private String productDiscount;//_discount
    private String productNowCost;//now_cost
    private String productName;//name
    private String productOriginalCost;//prize
//    private String productMoneyDefault;//money_default Discount or _discount_money
    // only one layout temp
    private String tempProductMoneyDefault; //only editView temp
    private String tempMoneyEditText;//only editView temp
    private boolean isShowDown;// only editView show or hidden
    //shop
    private int shopId;
    private String pricingId;
    //guest
    private String guestProductDiscount;//_discount
    private String guestProductNowCost;//now_cost
//    private String guestMoneyDefault;//money_default Discount or _discount_money

    private String guestDiscountType;


    //guest temp
    private boolean guestEditViewShow;//editView show or hidden
    private String guestTempProductMoneyDefault; //temp
    private String guestTempMoneyEditText;//temp
    //member
    private String memberProductDiscount;//_discount
    private String memberProductNowCost;//now_cost


    // member temp
    private boolean memberEditViewShow;//editView show or hidden
    private String memberTempProductMoneyDefault; //temp
    private String memberTempMoneyEditText;//temp
    //private String memberMoneyDefault;//money_default Discount or _discount_money
    private String memberDiscountType;

    private int frequenterPricingId;
    private String ftgfId;
    private String packageId;//agents
    private String productAttr;//agents
    private String typeTagId;



    public String getGuestDiscountType() {
        return guestDiscountType;
    }

    public void setGuestDiscountType(String guestDiscountType) {
        this.guestDiscountType = guestDiscountType;
    }

    public String getMemberDiscountType() {
        return memberDiscountType;
    }

    public void setMemberDiscountType(String memberDiscountType) {
        this.memberDiscountType = memberDiscountType;
    }

    private ArrayList<PackageItem> packageItemList;

    public ArrayList<PackageItem> getPackageItemList() {
        return packageItemList;
    }

    public String getNonMemberName() {
        return nonMemberName;
    }

    public void setNonMemberName(String nonMemberName) {
        this.nonMemberName = nonMemberName;
    }

    public void setPackageItemList(ArrayList<PackageItem> packageItemList) {
        this.packageItemList = packageItemList;
    }
    // private ArrayList<ShopsSubProduct> productList;

    public static class PackageItem{
        private String pricingId;
        private String productId;//id
        private String price;//id
        private String productAttr;



        private String productName;
        private String guestProductDiscount;//_discount
        private String guestProductDiscountType;//_discount

        private boolean isMember;
        //member
        private String memberProductDiscount;//_discount
        private String memberProductDiscountType;//_discount


        public String getProductAttr() {
            if (Utils.isStringNullOrEmpty(productAttr))return Constants.STR_0;
            return productAttr;
        }

        public void setProductAttr(String productAttr) {
            this.productAttr = productAttr;
        }

        public boolean isMember() {
            return isMember;
        }

        public void setIsMember(boolean isMember) {
            this.isMember = isMember;
        }

        public String getPricingId() {
            return pricingId;
        }

        public void setPricingId(String pricingId) {
            this.pricingId = pricingId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getGuestProductDiscount() {
            return guestProductDiscount;
        }

        public void setGuestProductDiscount(String guestProductDiscount) {
            this.guestProductDiscount = guestProductDiscount;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getGuestProductDiscountType() {
            return guestProductDiscountType;
        }

        public void setGuestProductDiscountType(String guestProductDiscountType) {
            this.guestProductDiscountType = guestProductDiscountType;
        }

        public String getMemberProductDiscount() {
            return memberProductDiscount;
        }

        public void setMemberProductDiscount(String memberProductDiscount) {
            this.memberProductDiscount = memberProductDiscount;
        }

        public String getMemberProductDiscountType() {
            return memberProductDiscountType;
        }

        public void setMemberProductDiscountType(String memberProductDiscountType) {
            this.memberProductDiscountType = memberProductDiscountType;
        }
    }



    public JsonCommonProduct() {
        packageId = Constants.STR_0;
    }

    public String getTypeTagId() {
        return typeTagId;
    }

    public void setTypeTagId(String typeTagId) {
        this.typeTagId = typeTagId;
    }

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getProductAttr() {
        return productAttr;
    }

    public void setProductAttr(String productAttr) {
        this.productAttr = productAttr;
    }

    public String getGuestTempProductMoneyDefault() {
        return guestTempProductMoneyDefault;
    }

    public void setGuestTempProductMoneyDefault(String guestTempProductMoneyDefault) {
        this.guestTempProductMoneyDefault = guestTempProductMoneyDefault;
    }

    public String getGuestTempMoneyEditText() {
        return guestTempMoneyEditText;
    }

    public void setGuestTempMoneyEditText(String guestTempMoneyEditText) {
        this.guestTempMoneyEditText = guestTempMoneyEditText;
    }

    public String getMemberTempProductMoneyDefault() {
        return memberTempProductMoneyDefault;
    }

    public void setMemberTempProductMoneyDefault(String memberTempProductMoneyDefault) {
        this.memberTempProductMoneyDefault = memberTempProductMoneyDefault;
    }

    public String getMemberTempMoneyEditText() {
        return memberTempMoneyEditText;
    }

    public void setMemberTempMoneyEditText(String memberTempMoneyEditText) {
        this.memberTempMoneyEditText = memberTempMoneyEditText;
    }

    public boolean isGuestEditViewShow() {
        return guestEditViewShow;
    }

    public void setGuestEditViewShow(boolean guestEditViewShow) {
        this.guestEditViewShow = guestEditViewShow;
    }

    public boolean isMemberEditViewShow() {
        return memberEditViewShow;
    }

    public void setMemberEditViewShow(boolean memberEditViewShow) {
        this.memberEditViewShow = memberEditViewShow;
    }

//    public String getMemberMoneyDefault() {
//        return memberMoneyDefault;
//    }
//
//    public void setMemberMoneyDefault(String memberMoneyDefault) {
//        this.memberMoneyDefault = memberMoneyDefault;
//    }



    public int getFrequenterPricingId() {
        return frequenterPricingId;
    }

    public void setFrequenterPricingId(int frequenterPricingId) {
        this.frequenterPricingId = frequenterPricingId;
    }

    public String getGuestProductDiscount() {
        return guestProductDiscount;
    }

    public void setGuestProductDiscount(String guestProductDiscount) {
        this.guestProductDiscount = guestProductDiscount;
    }

    public String getGuestProductNowCost() {
        return guestProductNowCost;
    }

    public void setGuestProductNowCost(String guestProductNowCost) {
        if (Utils.isStringNullOrEmpty(guestProductNowCost)) guestProductNowCost = "0.00";
        this.guestProductNowCost = guestProductNowCost;
    }

    public String getMemberProductDiscount() {
        return memberProductDiscount;
    }

    public void setMemberProductDiscount(String memberProductDiscount) {
        this.memberProductDiscount = memberProductDiscount;
    }

    public String getMemberProductNowCost() {
        return memberProductNowCost;
    }

    public void setMemberProductNowCost(String memberProductNowCost) {
        if (Utils.isStringNullOrEmpty(memberProductNowCost)) memberProductNowCost = "0.00";
        this.memberProductNowCost = memberProductNowCost;
    }

    public String getPricingId() {
        return pricingId;
    }

    public void setPricingId(String pricingId) {
        this.pricingId = pricingId;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getTempMoneyEditText() {
        return tempMoneyEditText;
    }

    public void setTempMoneyEditText(String tempMoneyEditText) {
        this.tempMoneyEditText = tempMoneyEditText;
    }

    public String getTempProductMoneyDefault() {
        return tempProductMoneyDefault;
    }

    public void setTempProductMoneyDefault(String tempProductMoneyDefault) {
        this.tempProductMoneyDefault = tempProductMoneyDefault;
    }

    public boolean isShowDown() {
        return isShowDown;
    }

    public void setShowDown(boolean isShowDown) {
        this.isShowDown = isShowDown;
    }



    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductDiscount() {
        return Utils.isStringNullOrEmpty(productDiscount) ? "0.00" : productDiscount;
    }

    public void setProductDiscount(String productDiscount) {
        if (Utils.isStringNullOrEmpty(productDiscount)) productDiscount = "0.00";
        this.productDiscount = productDiscount;
    }

    public String getProductNowCost() {
        return Utils.isStringNullOrEmpty(productNowCost) ? "0.00" : productNowCost;
    }

    public void setProductNowCost(String productNowCost) {
        if (Utils.isStringNullOrEmpty(productNowCost)) productNowCost = "0.00";
        this.productNowCost = productNowCost;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductOriginalCost() {
        return Utils.isStringNullOrEmpty(productOriginalCost) ? "0.00" : productOriginalCost;
    }

    public void setProductOriginalCost(String productOriginalCost) {

        if (Utils.isStringNullOrEmpty(productOriginalCost)) productOriginalCost = "0.00";
        this.productOriginalCost = productOriginalCost;
    }

    public void setProductDiscount(String productDiscount, String productDiscountMoney, String productMoneyDefault) {
       // this.productMoneyDefault = productMoneyDefault;
        if (Constants.MONEY_DISCOUNT_MONEY.equals(productMoneyDefault)) {
            this.productDiscount = productDiscountMoney;
        } else {
            this.productDiscount = productDiscount;
        }
    }

    public void setMemberProductDiscount(String memberProductDiscount, String memberProductDiscountMoney, String memberMoneyDefault) {
       // this.memberMoneyDefault = memberMoneyDefault;
        if (Constants.MONEY_DISCOUNT_MONEY.equals(memberMoneyDefault)) {
            this.memberProductDiscount = memberProductDiscountMoney;
        } else {
            this.memberProductDiscount = memberProductDiscount;
        }
    }

    public void setGuestProductDiscount(String guestProductDiscount, String guestProductDiscountMoney, String guestMoneyDefault) {
       // this.guestMoneyDefault = guestMoneyDefault;
        if (Constants.MONEY_DISCOUNT_MONEY.equals(guestMoneyDefault)) {
            this.guestProductDiscount = guestProductDiscountMoney;
        } else {
            this.guestProductDiscount = guestProductDiscount;
        }
    }

    public String getFtgfId() {
        return ftgfId;
    }

    public void setFtgfId(String ftgfId) {
        this.ftgfId = ftgfId;
    }
}

