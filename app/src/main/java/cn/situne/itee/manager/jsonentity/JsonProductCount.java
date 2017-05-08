/**
 * Project Name: itee
 * File Name:	 JsonProductCount.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-07-09
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONObject;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonProductCount <br/>
 * Function: FIXME. <br/>
 * Date: 2015-07-09 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonProductCount extends BaseJsonObject {

    private String existFlag;// 1 exist, 0 none

    public String getExistFlag() {
        return existFlag;
    }

    public JsonProductCount(JSONObject jsonObject) {
        super(jsonObject);
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);

        this.existFlag = Utils.getStringFromJsonObjectWithKey(jsonObj, JsonKey.SHOPPING_PRODUCT_COUNT_EXIST_FLAG);
    }
}