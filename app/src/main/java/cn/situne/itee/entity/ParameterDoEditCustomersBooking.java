package cn.situne.itee.entity;

import java.io.Serializable;
import java.util.List;


public class ParameterDoEditCustomersBooking implements Serializable {

    private static final long serialVersionUID = -1529846374946979809L;


    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }

    public Integer courseId;
    public Integer bookingUserId;
    public String bookingName;
    public String bookingTel;


    private List<Booking> bookingList;


    public static class Booking {

        public String bookingNo;
        public String bookingDate;
        public String signIn;
        public String customerName;
        public Integer customerId;
        public String bookingSort;
        public List<Integer> goodsList;
        public List<BookingAreaItem> bookingArea;


        public static class BookingAreaItem {
            public Integer courseAreaId;
            public String bookingTime;
        }

//
//        public String coustomerNo;
//        public String coustomerType;
//
//        public String courseAreaId;
//        public String bookingTime;
//        public String transferTime;
//        public String transferAreaId;

    }

}
