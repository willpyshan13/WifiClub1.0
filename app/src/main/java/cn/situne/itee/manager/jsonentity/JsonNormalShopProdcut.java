/**
 * Project Name: itee
 * File Name:  JsonNormalShopProdcut.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-23
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonNormalShopProduct <br/>
 * Function: To set normal shop product. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author Luochao
 * @version 1.0
 * @see
 */
public class JsonNormalShopProdcut extends BaseJsonObject implements Serializable {

    private Prodcut prodcut;


    public JsonNormalShopProdcut(JSONObject jsonObject) {
        super(jsonObject);
    }

    public Prodcut getProdcut() {
        return prodcut;
    }

    public void setProdcut(Prodcut prodcut) {
        this.prodcut = prodcut;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {
            JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            prodcut = new Prodcut();
            prodcut.setEnableSubclassStatus(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_ENABLE_SUBCLASS_STATUS));
            prodcut.setPrice(Utils.getDoubleFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRICE));
            prodcut.setQty(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_QTY));
            prodcut.setCode(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_CODE));

        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    class Prodcut implements Serializable {
        private int enableSubclassStatus;
        private double price;
        private int qty;
        private String code;

        public int getEnableSubclassStatus() {
            return enableSubclassStatus;
        }

        public void setEnableSubclassStatus(int enableSubclassStatus) {
            this.enableSubclassStatus = enableSubclassStatus;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getQty() {
            return qty;
        }

        public void setQty(int qty) {
            this.qty = qty;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

}





