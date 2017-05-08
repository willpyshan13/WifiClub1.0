/**
 * Project Name: itee
 * File Name:  JsonEditDoTeeTimeSetting.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-12
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * ClassName:JsonEditDoTeeTimeSetting <br/>
 * Function: To edit tee time setting. <br/>
 * Date: 2015-03-12 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonEditDoTeeTimeSetting extends BaseJsonObject implements Serializable {
    public JsonEditDoTeeTimeSetting(JSONObject jsonObject) {
        super(jsonObject);
    }
}
