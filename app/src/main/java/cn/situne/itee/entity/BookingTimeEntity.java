/**
 * Project Name: itee
 * File Name:	 BookingTimeEntity.java
 * Package Name: cn.situne.itee.entity
 * Date:		 2015-04-16
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.entity;

import java.io.Serializable;

/**
 * ClassName:BookingTimeEntity <br/>
 * Function: FIXME. <br/>
 * Date: 2015-04-16 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class BookingTimeEntity implements Serializable {

    private String bookingTime;
    private String segmentTypeId;
    //add by songyb
    private boolean isPrimeTime;
    private String segmentTimeSetting;

    public BookingTimeEntity() {
    }

    public BookingTimeEntity(String bookingTime, String segmentTypeId, boolean isPrimeTime, String segmentTimeSetting) {
        this.bookingTime = bookingTime;
        this.segmentTypeId = segmentTypeId;
        this.segmentTimeSetting = segmentTimeSetting;
        this.isPrimeTime = isPrimeTime;
    }

    public String getSegmentTimeSetting() {
        return segmentTimeSetting;
    }

    public void setSegmentTimeSetting(String segmentTimeSetting) {
        this.segmentTimeSetting = segmentTimeSetting;
    }

    public boolean isPrimeTime() {
        return isPrimeTime;
    }

    public void setPrimeTime(boolean primeTime) {
        isPrimeTime = primeTime;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getSegmentTypeId() {
        return segmentTypeId;
    }

    public void setSegmentTypeId(String segmentTypeId) {
        this.segmentTypeId = segmentTypeId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else {
            if (o instanceof BookingTimeEntity) {
                BookingTimeEntity bookingTimeEntity = (BookingTimeEntity) o;
                if (bookingTimeEntity.bookingTime != null
                        && bookingTimeEntity.bookingTime.equals(bookingTime)) {
                    if (bookingTimeEntity.segmentTypeId == null && segmentTypeId == null || (segmentTypeId != null && segmentTypeId.equals(bookingTimeEntity.segmentTypeId))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}