/**
 * Project Name: itee
 * File Name:  JsonReturnData.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.manager.jsonentity;

import java.io.Serializable;

/**
 * ClassName:JsonReturnData <br/>
 * Function: To set return data. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class JsonReturnData implements Serializable {

    private Integer returnCode;
    private String returnInfo;
    private String addId;

    public String getAddId() {
        return addId;
    }

    public void setAddId(String addId) {
        this.addId = addId;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }
}
