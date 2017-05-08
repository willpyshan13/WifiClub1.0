package com.squareup.timessquare;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liaojian on 2015/1/28.
 */
public class JsonTeeTimeCalendar implements Serializable {

    public DataList dataList;

    public DataList getDataList() {
        return dataList;
    }

    public void setDataList(DataList dataList) {
        this.dataList = dataList;
    }

    public static class DataList {

        public String courseId;
        public List<DateStatus> dateStatus;

        public String getCourseId() {
            return courseId;
        }

        public void setCourseId(String courseId) {
            this.courseId = courseId;
        }

        public List<DateStatus> getDateStatus() {
            return dateStatus;
        }

        public void setDateStatus(List<DateStatus> dateStatus) {
            this.dateStatus = dateStatus;
        }

        public static class DateStatus {
            public String date;
            public List<StatusList> statusList;

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public List<StatusList> getStatusList() {
                return statusList;
            }

            public void setStatusList(List<StatusList> statusList) {
                this.statusList = statusList;
            }

            public static class StatusList {
                public String day;
                public String teeTimeSetting;
                public String block;
                public String nineHoles;
                public String threeTeeStart;
                public String memberOnly;
                public String booking;
                public String primeTime;
                public String twoTeeStart;

                public String getNineHoles() {
                    return nineHoles;
                }

                public void setNineHoles(String nineHoles) {
                    this.nineHoles = nineHoles;
                }

                public String getThreeTeeStart() {
                    return threeTeeStart;
                }

                public void setThreeTeeStart(String threeTeeStart) {
                    this.threeTeeStart = threeTeeStart;
                }

                public String getDay() {
                    return day;
                }

                public void setDay(String day) {
                    this.day = day;
                }

                public String getTeeTimeSetting() {
                    return teeTimeSetting;
                }

                public void setTeeTimeSetting(String teeTimeSetting) {
                    this.teeTimeSetting = teeTimeSetting;
                }

                public String getBlock() {
                    return block;
                }

                public void setBlock(String block) {
                    this.block = block;
                }

                public String getMemberOnly() {
                    return memberOnly;
                }

                public void setMemberOnly(String memberOnly) {
                    this.memberOnly = memberOnly;
                }

                public String getBooking() {
                    return booking;
                }

                public void setBooking(String booking) {
                    this.booking = booking;
                }

                public String getPrimeTime() {
                    return primeTime;
                }

                public void setPrimeTime(String primeTime) {
                    this.primeTime = primeTime;
                }

                public String getTwoTeeStart() {
                    return twoTeeStart;
                }

                public void setTwoTeeStart(String twoTeeStart) {
                    this.twoTeeStart = twoTeeStart;
                }
            }
        }


    }


}
