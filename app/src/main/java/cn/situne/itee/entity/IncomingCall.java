
/**
 * Project Name: itee
 * File Name:	 IncomingCall.java
 * Package Name: cn.situne.itee.entity
 * Date:		 2015-01-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.entity;

import java.io.Serializable;

/**
 * ClassName:IncomingCall <br/>
 * Function: 返回通话纪录 <br/>

 * Date: 2015-01-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IncomingCall implements Serializable{

    private String name;
    private String tel;
    private String time;
    private String duration;

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
