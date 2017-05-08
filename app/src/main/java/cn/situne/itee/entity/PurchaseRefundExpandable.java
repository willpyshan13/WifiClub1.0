/**
 * Project Name: itee
 * File Name:  PurchaseHistoryExpandable.java
 * Package Name: cn.situne.itee.entity
 * Date:   2015-04-22
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.manager.jsonentity.JsonPurchaseHistoryDetailRecord;

/**
 * ClassName:PurchaseHistoryExpandable <br/>
 * Date: 2015-04-22 <br/>
 *
 * @author Administrator
 * @version 1.0
 * @see
 */
public class PurchaseRefundExpandable implements Serializable {


    private String id;
    private String name;
    private String price;
    private Integer count;
    private Integer type;
    private  List<JsonPurchaseHistoryDetailRecord.PricingData> pricingDataList;

    public List<JsonPurchaseHistoryDetailRecord.PricingData> getPricingDataList() {
        return pricingDataList;
    }

    public void setPricingDataList(List<JsonPurchaseHistoryDetailRecord.PricingData> pricingDataList) {
        this.pricingDataList = pricingDataList;
    }

    private Integer refundflag;
    private List<GoodListItem> goodList;
    private ArrayList<JsonPurchaseHistoryDetailRecord.VoucherItem> voucherItems;

    public ArrayList<JsonPurchaseHistoryDetailRecord.VoucherItem> getVoucherItems() {
        return voucherItems;
    }

    public void setVoucherItems(ArrayList<JsonPurchaseHistoryDetailRecord.VoucherItem> voucherItems) {
        this.voucherItems = voucherItems;
    }


    public Integer getRefundflag() {
        return refundflag;
    }

    public void setRefundflag(Integer refundflag) {
        this.refundflag = refundflag;
    }


    public List<GoodListItem> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<GoodListItem> goodList) {
        this.goodList = goodList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public static class GoodListItem implements Serializable {

        private String id;
        private String name;
        private String price;
        private Integer count;
        private boolean check;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
    }
}

