/**
 * Project Name: itee
 * File Name:  JsonForgotPassword.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-05
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * ClassName:JsonForgotPassword <br/>
 * Function: To set ForgotPassword. <br/>
 * Date: 2015-03-05 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonForgotPassword extends BaseJsonObject implements Serializable {
    public JsonForgotPassword(JSONObject jsonObject) {
        super(jsonObject);
    }
}
