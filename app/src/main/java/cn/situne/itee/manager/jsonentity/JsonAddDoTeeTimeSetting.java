/**
 * Project Name: itee
 * File Name:  JsonAddDoTeeTimeSetting.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * ClassName:JsonAddDoTeeTimeSetting <br/>
 * Function: add tee time setting. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonAddDoTeeTimeSetting extends BaseJsonObject implements Serializable {
    public JsonAddDoTeeTimeSetting(JSONObject jsonObject) {
        super(jsonObject);
    }
}
