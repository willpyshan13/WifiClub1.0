
/**
 * Project Name: itee
 * File Name:	 DeleteMemberMessage.java
 * Package Name: cn.situne.itee.entity
 * Date:		 2015-01-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.entity;

/**
 * ClassName:DeleteMemberMessage <br/>
 * Function: DeleteMemberMessageAdapter 中 的实体类. <br/>

 * Date: 2015-01-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class DeleteMemberMessage {

    private String memberName;
    private String memberCode;
    private String birthday;
    private String birthdayCode;
    private String tel;
    private String telCode;
    private String zip;
    private String zipCode;

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberCode() {
        return memberCode;
    }

    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBirthdayCode() {
        return birthdayCode;
    }

    public void setBirthdayCode(String birthdayCode) {
        this.birthdayCode = birthdayCode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getTelCode() {
        return telCode;
    }

    public void setTelCode(String telCode) {
        this.telCode = telCode;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }
}
