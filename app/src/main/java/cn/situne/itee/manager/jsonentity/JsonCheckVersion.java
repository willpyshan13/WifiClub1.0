/**
 * Project Name: itee
 * File Name:	 JsonCheckVersion.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:		 2015-06-12
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.json.JSONObject;

/**
 * ClassName:JsonCheckVersion <br/>
 * Function: FIXME. <br/>
 * Date: 2015-06-12 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonCheckVersion extends BaseJsonObject {

    private boolean isForceUpdate;
    private String baseUrl;

    public JsonCheckVersion(JSONObject jsonObject) {
        super(jsonObject);
    }

    public boolean isForceUpdate() {
        return isForceUpdate;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}