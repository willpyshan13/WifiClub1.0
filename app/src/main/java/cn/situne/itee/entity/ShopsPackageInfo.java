/**
 * Project Name: itee
 * File Name:	 ShopsPackageInfo.java
 * Package Name: cn.situne.itee.entity
 * Date:		 2015-04-14
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ClassName:ShopsPackageInfo <br/>
 * Function: FIXME. <br/>
 * Date: 2015-04-14 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ShopsPackageInfo implements Serializable {

    public ShopsPackageInfo() {
        productList = new ArrayList<>();
    }

    private String packageId;
    private String packageName;
    private String packagePrice;
    private String packageCount;
    private String unlimitedFlag;

    public String getUnlimitedFlag() {
        return unlimitedFlag;
    }

    public void setUnlimitedFlag(String unlimitedFlag) {
        this.unlimitedFlag = unlimitedFlag;
    }

    private ArrayList<ShopsProduct> productList;

    public String getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(String packageCount) {
        this.packageCount = packageCount;
    }

    public void addProductDetail(ShopsProduct shopsProduct) {
        productList.add(shopsProduct);
    }

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

    public String getPackagePrice() {
        return packagePrice;
    }

    public void setPackagePrice(String packagePrice) {
        this.packagePrice = packagePrice;
    }

    public ArrayList<ShopsProduct> getProductList() {
        return productList;
    }
}