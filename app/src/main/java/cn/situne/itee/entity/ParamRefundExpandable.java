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

/**
 * ClassName:PurchaseHistoryExpandable <br/>
 * Date: 2015-04-22 <br/>
 *
 * @author Administrator
 * @version 1.0
 * @see
 */
public class ParamRefundExpandable implements Serializable {

    private String id;
    private String total_price;
    private String count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

