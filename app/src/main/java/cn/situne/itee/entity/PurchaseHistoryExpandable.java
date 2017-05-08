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
import java.util.List;

import cn.situne.itee.manager.jsonentity.JsonPurchaseHistoryDetailRecord;

/**
 * ClassName:PurchaseHistoryExpandable <br/>
 * Function: . <br/>
 * Date: 2015-04-22 <br/>
 *
 * @author Administrator
 * @version 1.0
 * @see
 */
public class PurchaseHistoryExpandable implements Serializable {

    private String id;
    private String name;
    private String price;
    private Integer count;
    private Integer type;

    private List<JsonPurchaseHistoryDetailRecord.GoodListItem> goodList;

    public List<JsonPurchaseHistoryDetailRecord.GoodListItem> getGoodList() {
        return goodList;
    }

    public void setGoodList(List<JsonPurchaseHistoryDetailRecord.GoodListItem> goodList) {
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
}

