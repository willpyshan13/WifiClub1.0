/**
 * Project Name: itee
 * File Name:  PurchaseHistoryExpandable.java
 * Package Name: cn.situne.itee.entity
 * Date:   2015-04-22
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
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
public class ParamAdvancedSetting implements Serializable {

    private String cdDate;
    private String cdTypeFlag;
    private String cdType;

    public String getCdDate() {
        return cdDate;
    }

    public void setCdDate(String cdDate) {
        this.cdDate = cdDate;
    }

    public String getCdTypeFlag() {
        return cdTypeFlag;
    }

    public void setCdTypeFlag(String cdTypeFlag) {
        this.cdTypeFlag = cdTypeFlag;
    }

    public String getCdType() {
        return cdType;
    }

    public void setCdType(String cdType) {
        this.cdType = cdType;
    }
}

