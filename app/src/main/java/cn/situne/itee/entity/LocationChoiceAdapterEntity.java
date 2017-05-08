package cn.situne.itee.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class LocationChoiceAdapterEntity implements Serializable {

    private String time;//时间
    private Integer positionNumber;// 位置数量
    private Integer appointmentNumber;//被预约位置数量
    private ArrayList<Integer> positionStates;//0：未选中；1：选中状态； 2：已被预约；
    //还需要添加位置属性字段，但是由于接口中的数据结构影响软件效率暂时使用单个字段
    private String segmentTimeType; //0：加号; 1:M; 2:锁
    private String segmentTimeTypeId;
    private String segmentTimeSetting;
    private String memberTypeId;
    private Integer transferTime;   //0：灰色； 1：绿色
    private boolean isPrimeTime;

    private String checkInStatus;
    private String payStatus;


    private ArrayList<PositionInformationEntity> positionInformationList;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getPositionNumber() {
        return positionNumber;
    }

    public void setPositionNumber(Integer positionNumber) {
        this.positionNumber = positionNumber;
    }

    public Integer getAppointmentNumber() {
        return appointmentNumber;
    }

    public void setAppointmentNumber(Integer appointmentNumber) {
        this.appointmentNumber = appointmentNumber;
    }

    public ArrayList<PositionInformationEntity> getPositionInformationList() {
        return positionInformationList;
    }

    public void setPositionInformationList(ArrayList<PositionInformationEntity> positionInformationList) {
        this.positionInformationList = positionInformationList;
    }

    public String getSegmentTimeType() {
        return segmentTimeType;
    }

    public void setSegmentTimeType(String segmentTimeType) {
        this.segmentTimeType = segmentTimeType;
    }

    public ArrayList<Integer> getPositionStates() {
        return positionStates;
    }

    public void setPositionStates(ArrayList<Integer> positionStates) {
        this.positionStates = positionStates;
    }

    public String getSegmentTimeTypeId() {
        return segmentTimeTypeId;
    }

    public void setSegmentTimeTypeId(String segmentTimeTypeId) {
        this.segmentTimeTypeId = segmentTimeTypeId;
    }

    public String getSegmentTimeSetting() {
        return segmentTimeSetting;
    }

    public void setSegmentTimeSetting(String segmentTimeSetting) {
        this.segmentTimeSetting = segmentTimeSetting;
    }

    public Integer getTransferTime() {
        return transferTime;
    }

    public void setTransferTime(Integer transferTime) {
        this.transferTime = transferTime;
    }

    public String getMemberTypeId() {
        return memberTypeId;
    }

    public void setMemberTypeId(String memberTypeId) {
        this.memberTypeId = memberTypeId;
    }

    public boolean isPrimeTime() {
        return isPrimeTime;
    }

    public void setPrimeTime(boolean isPrimeTime) {
        this.isPrimeTime = isPrimeTime;
    }

    public String getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(String checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public String getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(String payStatus) {
        this.payStatus = payStatus;
    }
}
