package cn.situne.itee.entity;

import java.io.Serializable;

/**
 * Created by yangym on 2015/2/2.
 */
public class PositionInformationEntity implements Serializable {
    // private Integer positionState;//0：未选中；1：选中状态； 2：已被预约；
    private String memberName;//会员姓名
    private Integer memberId;//会员ID

    private String appointmentOrderNo;

    private String memberType;//会员类型M(Members)/W(WalkIn)/G(Guest)/A(Agent)
    private Integer memberGender;//会员性别0：未知（黑色）；1：男（蓝色）；2：女（红色）
    private Integer payStatus;//结账状态 0：未结账；1：已结账（黑色圆圈F）
    private Integer checkInStatus;//0:未开卡； 1：已开卡
    private Integer depositStatus;//0：没有定金； 1：有定金
    private String appointmentGoods;//预约商品列表
    private String bookingNo;//预约id
    private Integer appointmentGoodsStatus;//0：未付款； 1：已付款
    private Integer currentHole;//当前洞号
    private Integer currentHoleStatus;//当前洞号状态 0:黑色圆圈洞号；1:红色圆圈洞号;2:黄色圆圈洞号

    private boolean checkOutStatus;
    private Integer bookingColor;
    private String lookFlag;
    private String sameDayFlag;

    private String selfFlag;

    public String getSelfFlag() {
        return selfFlag;
    }

    public void setSelfFlag(String selfFlag) {
        this.selfFlag = selfFlag;
    }

    public Integer getBookingColor() {
        return bookingColor;
    }

    public void setBookingColor(Integer bookingColor) {
        this.bookingColor = bookingColor;
    }

    public boolean isCheckOutStatus() {
        return checkOutStatus;
    }

    public void setCheckOutStatus(boolean checkOutStatus) {
        this.checkOutStatus = checkOutStatus;
    }

    public String getAppointmentOrderNo() {
        return appointmentOrderNo;
    }

    public void setAppointmentOrderNo(String appointmentOrderNo) {
        this.appointmentOrderNo = appointmentOrderNo;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public Integer getMemberGender() {
        return memberGender;
    }

    public void setMemberGender(Integer memberGender) {
        this.memberGender = memberGender;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getCheckInStatus() {
        return checkInStatus;
    }

    public void setCheckInStatus(Integer checkInStatus) {
        this.checkInStatus = checkInStatus;
    }

    public Integer getDepositStatus() {
        return depositStatus;
    }

    public void setDepositStatus(Integer depositStatus) {
        this.depositStatus = depositStatus;
    }

    public String getAppointmentGoods() {
        return appointmentGoods;
    }

    public void setAppointmentGoods(String appointmentGoods) {
        this.appointmentGoods = appointmentGoods;
    }

    public Integer getAppointmentGoodsStatus() {
        return appointmentGoodsStatus;
    }

    public void setAppointmentGoodsStatus(Integer appointmentGoodsStatus) {
        this.appointmentGoodsStatus = appointmentGoodsStatus;
    }

    public Integer getCurrentHole() {
        return currentHole;
    }

    public void setCurrentHole(Integer currentHole) {
        this.currentHole = currentHole;
    }

    public Integer getCurrentHoleStatus() {
        return currentHoleStatus;
    }

    public void setCurrentHoleStatus(Integer currentHoleStatus) {
        this.currentHoleStatus = currentHoleStatus;
    }

    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public String getLookFlag() {
        return lookFlag;
    }

    public void setLookFlag(String lookFlag) {
        this.lookFlag = lookFlag;
    }

    public String getSameDayFlag() {
        return sameDayFlag;
    }

    public void setSameDayFlag(String sameDayFlag) {
        this.sameDayFlag = sameDayFlag;
    }
}
