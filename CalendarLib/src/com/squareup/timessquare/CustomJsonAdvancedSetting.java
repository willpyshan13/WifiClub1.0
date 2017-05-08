/**
 * Project Name: itee
 * File Name:  JsonAgentListGet.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package com.squareup.timessquare;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


/**
 * ClassName:JsonAgentListGet <br/>
 * Function: Get Json agentsList list information. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class CustomJsonAdvancedSetting implements Serializable {

    private List<HashMap<String, DaySetting>> dataList;

    public List<HashMap<String, DaySetting>> getDataList() {
        return dataList;
    }

    public void setDataList(List<HashMap<String, DaySetting>> dataList) {
        this.dataList = dataList;
    }

    public static class CdTypeArrItem implements Serializable {
        private String id;
        private String firstName;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class DaySetting implements Serializable {

        private String cdDate;
        private String cdId;
        private String cdType;
        //1:holiday 2:weekday
        private String cdTypeFlag;

        private List<CdTypeArrItem> cdTypeArr;
        private List<CdTypeArrItem> cdTypeFlagArr;

        public String getCdDate() {
            return cdDate;
        }

        public void setCdDate(String cdDate) {
            this.cdDate = cdDate;
        }

        public String getCdId() {
            return cdId;
        }

        public void setCdId(String cdId) {
            this.cdId = cdId;
        }

        public String getCdType() {
            return cdType;
        }

        public void setCdType(String cdType) {
            this.cdType = cdType;
        }

        public String getCdTypeFlag() {
            return cdTypeFlag;
        }

        public void setCdTypeFlag(String cdTypeFlag) {
            this.cdTypeFlag = cdTypeFlag;
        }

        public List<CdTypeArrItem> getCdTypeArr() {
            return cdTypeArr;
        }

        public void setCdTypeArr(List<CdTypeArrItem> cdTypeArr) {
            this.cdTypeArr = cdTypeArr;
        }

        public List<CdTypeArrItem> getCdTypeFlagArr() {
            return cdTypeFlagArr;
        }

        public void setCdTypeFlagArr(List<CdTypeArrItem> cdTypeFlagArr) {
            this.cdTypeFlagArr = cdTypeFlagArr;
        }
    }



}
