/**
 * Project Name: itee
 * File Name:  JsonNormalShop.java
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
 * ClassName:JsonNormalShop <br/>
 * Function: To set normal shop. <br/>
 * Date: 2015-03-23 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */

public class JsonNormalShop extends BaseJsonObject implements Serializable {

    private NormalShop shop;


    public JsonNormalShop(JSONObject jsonObject) {
        super(jsonObject);
    }

    public NormalShop getShop() {
        return shop;
    }

    public void setShop(NormalShop shop) {
        this.shop = shop;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        try {
            JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
            shop = new NormalShop();
            shop.setProductId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRODUCT_ID));
            shop.setProductName(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.SHOP_05_PRODUCT_NAME));
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

    }

    class NormalShop implements Serializable {
        private int productId;
        private int productName;

        public int getProductId() {
            return productId;
        }

        public void setProductId(int productId) {
            this.productId = productId;
        }

        public int getProductName() {
            return productName;
        }

        public void setProductName(int productName) {
            this.productName = productName;
        }
    }

}





