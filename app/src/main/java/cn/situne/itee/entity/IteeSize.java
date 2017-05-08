/**
 * Project Name: itee
 * File Name:	 IteeSize.java
 * Package Name: cn.situne.itee.entity
 * Date:		 2015-06-10
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.entity;

/**
 * ClassName:IteeSize <br/>
 * Function: FIXME. <br/>
 * Date: 2015-06-10 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeSize {
    private int width;
    private int height;

    public IteeSize() {

    }

    public IteeSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}  