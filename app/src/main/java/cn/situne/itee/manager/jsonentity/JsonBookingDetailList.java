/**
 * Project Name: itee
 * File Name:  JsonBookingDetailList.java
 * Package Name: cn.situne.itee.manager.jsonentity
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.manager.jsonentity;

import org.codehaus.jackson.map.annotate.JsonFilter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.Utils;

/**
 * ClassName:JsonBookingDetailList <br/>
 * Function: Set Booking Detail List. <br/>
 * Date: 2015-03-11 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class JsonBookingDetailList extends BaseJsonObject implements Serializable {
    private DataListItem dataList;
    public JsonBookingDetailList(JSONObject jsonObject) {
        super(jsonObject);
    }
    public DataListItem getDataList() {
        return dataList;
    }
    public void setDataList(DataListItem dataList) {
        this.dataList = dataList;
    }

    @Override
    public void setJsonValues(JSONObject jsonObj) {
        super.setJsonValues(jsonObj);
        dataList = new DataListItem();
        if (jsonObj != null) {
            Utils.log(jsonObj.toString());
            try {
                JSONObject jsonObject = jsonObj.getJSONObject(JsonKey.COMMON_DATA_LIST);
                dataList.setBookingName(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_NAME));
                dataList.setBookingTel(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_TEL));
                dataList.setBookingUserId(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_USER_ID));
                dataList.setCourseAreaCount(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_COURSE_AREA_COUNT));
                dataList.setBookingDate(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_DATE));
                dataList.setBookingStatus(Utils.getStringFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_STATUS));
                dataList.categoryCourseList = new ArrayList<>();

                JSONArray jsonCategoryCourseList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_CATEGORY_COURSE_LIST);
                for (int j = 0; j < jsonCategoryCourseList.length(); j++) {
                    JSONObject jsonCategoryCourseListItem = jsonCategoryCourseList.getJSONObject(j);
                    JsonCustomersBooking.CategoryCourseListItem categoryCourseListItem = new JsonCustomersBooking.CategoryCourseListItem();
                    categoryCourseListItem.id = Utils.getIntegerFromJsonObjectWithKey(jsonCategoryCourseListItem, JsonKey.TEE_TIME_CATEGORY_COURSE_LIST_ITEM_ID);
                    categoryCourseListItem.status = Utils.getIntegerFromJsonObjectWithKey(jsonCategoryCourseListItem, JsonKey.TEE_TIME_CATEGORY_COURSE_LIST_ITEM_STATUS);
                    categoryCourseListItem.productId = Utils.getIntegerFromJsonObjectWithKey(jsonCategoryCourseListItem, JsonKey.TEE_TIME_CATEGORY_COURSE_LIST_ITEM_PRODUCT_ID);
                    categoryCourseListItem.price = Utils.getStringFromJsonObjectWithKey(jsonCategoryCourseListItem, JsonKey.TEE_TIME_CATEGORY_COURSE_LIST_ITEM_PRICE);
                    dataList.categoryCourseList.add(categoryCourseListItem);
                }
                dataList.setBookingUserType(Utils.getIntegerFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_USER_TYPE));
                List<DataListItem.ListItem> list = new ArrayList<>();

                JSONArray arrList = Utils.getArrayFromJsonObjectWithKey(jsonObject, JsonKey.TEE_TIME_BOOKING_DETAIL_LIST);
                for (int m = 0; m < arrList.length(); m++) {
                    JSONObject jsonListItem = arrList.getJSONObject(m);
                    DataListItem.ListItem listItem = new DataListItem.ListItem();
                    List<DataListItem.BookingAreaListItem> bookingCourseAreaList = new ArrayList<>();
                    listItem.setTypeTimeName(Utils.getIntegerFromJsonObjectWithKey(jsonListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_TYPE_TIME_NAME));
                    listItem.setBookingDate(Utils.getStringFromJsonObjectWithKey(jsonListItem, "booking_date"));

                    JSONArray arrAreaList = Utils.getArrayFromJsonObjectWithKey(jsonListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_AREA_LIST);
                    for (int j = 0; j < arrAreaList.length(); j++) {
                        JSONObject joAreaListItem = arrAreaList.getJSONObject(j);
                        DataListItem.BookingAreaListItem itemArea = new DataListItem.BookingAreaListItem();
                        itemArea.setCourseAreaType(Utils.getStringFromJsonObjectWithKey(joAreaListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_COURSE_AREA_TYPE));
                        itemArea.setCourseAreaId(Utils.getIntegerFromJsonObjectWithKey(joAreaListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_COURSE_AREA_ID));
                        itemArea.setBookingTime(Utils.getStringFromJsonObjectWithKey(joAreaListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_TIME));
                        itemArea.setBookingTimeStatus(Utils.getIntegerFromJsonObjectWithKey(joAreaListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_TIME_STATUS));
                        itemArea.setTransferFlag(Utils.getStringFromJsonObjectWithKey(joAreaListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_TRANSFER_FLAG));
                        itemArea.setBookingSignStatus(Utils.getStringFromJsonObjectWithKey(joAreaListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_SIGN_STATUS));
                        bookingCourseAreaList.add(itemArea);
                    }
                    listItem.setAreaList(bookingCourseAreaList);

                    List<DataListItem.BookingListItem> holeDataItemList = new ArrayList<>();
                    JSONArray arrHoleDataList = Utils.getArrayFromJsonObjectWithKey(jsonListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_LIST);
                    for (int k = 0; k < arrHoleDataList.length(); k++) {
                        JSONObject joHoleListItem = arrHoleDataList.getJSONObject(k);
                        DataListItem.BookingListItem item = new DataListItem.BookingListItem();
                        item.bookingNo = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_NO);
                        item.signId = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_SIGN_ID);
                        item.setSignCode(Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_AGEN_CODE));
                        item.signType = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_SIGN_TYPE);
                        item.signNo = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_SIGN_NO);
                        item.bookingStatus = Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_STATUS);
                        item.customerId = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_CUSTOMER_ID);
                        item.customerNo = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_CUSTOMER_NO);
                        item.customerType = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_CUSTOMER_TYPE);
                        item.customerName = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_CUSTOMER_NAME);
                        item.bookingDeposit = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_DEPOSIT);
                        item.customerPic = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_CUSTOMER_PIC);
                        item.bookingSort = Utils.getIntegerFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_SORT);
                        item.lockFlag = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_LOCK_FLAG);
                        item.primeTime = Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_PRIME_TIME);
                        item.setPricingBdpId(Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOPPING_PRICING_BDP_ID));
                        item.setPricingTimes(Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOPPING_PRICING_TIMES));
                        item.setPricingTimesFlg(Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.SHOPPING_PRICING_TIMES_FLG));
                        item.setUsType(Utils.getStringFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_BOOKING_US_TYPE));

                        ArrayList<DataListItem.GoodListItem> categoryList = new ArrayList<>();
                        JSONArray arrCategoryList = Utils.getArrayFromJsonObjectWithKey(joHoleListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_GOODS_LIST);
                        for (int j = 0; j < arrCategoryList.length(); j++) {
                            JSONObject joAreaListItem = arrCategoryList.getJSONObject(j);
                            DataListItem.GoodListItem categoryListItem = new DataListItem.GoodListItem();
                            categoryListItem.setGoodsId(Utils.getIntegerFromJsonObjectWithKey(joAreaListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_GOODS_ID));
                            categoryListItem.setGoodsLastAttr(Utils.getIntegerFromJsonObjectWithKey(joAreaListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_GOODS_LAST_ATTR));
                            categoryListItem.setCategoryId(Utils.getIntegerFromJsonObjectWithKey(joAreaListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_CATEGORY_ID));
                            categoryListItem.setGoodsPrice(Utils.getStringFromJsonObjectWithKey(joAreaListItem, JsonKey.TEE_TIME_BOOKING_DETAIL_GOODS_PRICE));
                            categoryListItem.setCaddieNo(Utils.getStringFromJsonObjectWithKey(joAreaListItem, JsonKey
                                    .TEE_TIME_BOOKING_DETAIL_GOODS_CADDIE_NO));
                            categoryList.add(categoryListItem);
                        }
                        item.setGoodsList(categoryList);
                        holeDataItemList.add(item);
                    }
                    listItem.setBookingList(holeDataItemList);
                    list.add(listItem);
                }
                dataList.setList(list);
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        } else {
            List<DataListItem.ListItem> itemList = new ArrayList<>();
            dataList.setList(itemList);
        }
    }

    public static class DataListItem implements Serializable {
        private String bookingName;
        private String bookingTel;
        private Integer bookingUserId;
        private Integer bookingUserType;
        private Integer courseAreaCount;
        private String bookingStatus;
        private String bookingDate;
        private List<JsonCustomersBooking.CategoryCourseListItem> categoryCourseList;
        private List<ListItem> list;

        public String getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(String bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        public List<ListItem> getList() {
            return list;
        }

        public void setList(List<ListItem> list) {
            this.list = list;
        }

        public Integer getBookingUserType() {
            return bookingUserType;
        }

        public void setBookingUserType(Integer bookingUserType) {
            this.bookingUserType = bookingUserType;
        }

        public List<JsonCustomersBooking.CategoryCourseListItem> getCategoryCourseList() {
            return categoryCourseList;
        }

        public void setCategoryCourseList(List<JsonCustomersBooking.CategoryCourseListItem> categoryCourseList) {
            this.categoryCourseList = categoryCourseList;
        }

        public String getBookingDate() {
            return bookingDate;
        }

        public void setBookingDate(String bookingDate) {
            this.bookingDate = bookingDate;
        }

        public Integer getCourseAreaCount() {
            return courseAreaCount;
        }

        public void setCourseAreaCount(Integer courseAreaCount) {
            this.courseAreaCount = courseAreaCount;
        }

        public String getBookingName() {
            return bookingName;
        }

        public void setBookingName(String bookingName) {
            this.bookingName = bookingName;
        }

        public String getBookingTel() {
            return bookingTel;
        }

        public void setBookingTel(String bookingTel) {
            this.bookingTel = bookingTel;
        }

        public Integer getBookingUserId() {
            return bookingUserId;
        }

        public void setBookingUserId(Integer bookingUserId) {
            this.bookingUserId = bookingUserId;
        }

        public static class ListItem implements Serializable {
            private String bookingDate;
            public String getBookingDate() {
                return bookingDate;
            }

            public void setBookingDate(String bookingDate) {
                this.bookingDate = bookingDate;
            }

            private Integer typeTimeName;
            private List<BookingListItem> bookingList;
            private List<BookingAreaListItem> AreaList;

            public Integer getTypeTimeName() {
                return typeTimeName;
            }

            public void setTypeTimeName(Integer typeTimeName) {
                this.typeTimeName = typeTimeName;
            }

            public List<BookingListItem> getBookingList() {
                return bookingList;
            }

            public void setBookingList(List<BookingListItem> bookingList) {
                this.bookingList = bookingList;
            }

            public List<BookingAreaListItem> getAreaList() {
                return AreaList;
            }

            public void setAreaList(List<BookingAreaListItem> areaList) {
                AreaList = areaList;
            }
        }

        public static class BookingListItem implements Serializable {
            private String bookingNo;
            private String signId;
            private String signType;
            private String signNo;

            private String signCode;
            public String getSignCode() {
                return signCode;
            }
            public void setSignCode(String signCode) {
                this.signCode = signCode;
            }

            private Integer bookingStatus;
            private String customerId;
            private String customerNo;
            private String customerType;
            private String customerName;
            private String bookingDeposit;
            private String lockFlag;    // 0 enable, 1 disable
            private String primeTime;
            private String customerPic;
            private Integer bookingSort;
            private String pricingTimes;
            private String pricingBdpId;
            private String pricingTimesFlg;

            public String getPricingTimesFlg() {
                return pricingTimesFlg;
            }

            public void setPricingTimesFlg(String pricingTimesFlg) {
                this.pricingTimesFlg = pricingTimesFlg;
            }

            public String getPricingTimes() {
                return pricingTimes;
            }

            public void setPricingTimes(String pricingTimes) {
                this.pricingTimes = pricingTimes;
            }

            public String getPricingBdpId() {
                return pricingBdpId;
            }

            public void setPricingBdpId(String pricingBdpId) {
                this.pricingBdpId = pricingBdpId;
            }

            private String usType;

            public String getUsType() {
                return usType;
            }

            public void setUsType(String usType) {
                this.usType = usType;
            }
            private List<GoodListItem> goodsList;

            public String getCustomerPic() {
                return customerPic;
            }

            public void setCustomerPic(String customerPic) {
                this.customerPic = customerPic;
            }

            public String getSignId() {
                return signId;
            }

            public void setSignId(String signId) {
                this.signId = signId;
            }

            public String getSignType() {
                return signType;
            }

            public void setSignType(String signType) {
                this.signType = signType;
            }

            public String getSignNo() {
                return signNo;
            }

            public void setSignNo(String signNo) {
                this.signNo = signNo;
            }

            public String getBookingNo() {
                return bookingNo;
            }

            public void setBookingNo(String bookingNo) {
                this.bookingNo = bookingNo;
            }

            public Integer getBookingStatus() {
                return bookingStatus;
            }

            public void setBookingStatus(Integer bookingStatus) {
                this.bookingStatus = bookingStatus;
            }

            public String getCustomerId() {
                return customerId;
            }

            public void setCustomerId(String customerId) {
                this.customerId = customerId;
            }

            public String getCustomerNo() {
                return customerNo;
            }

            public void setCustomerNo(String customerNo) {
                this.customerNo = customerNo;
            }

            public String getCustomerType() {
                return customerType;
            }

            public void setCustomerType(String customerType) {
                this.customerType = customerType;
            }

            public String getCustomerName() {
                return customerName;
            }

            public void setCustomerName(String customerName) {
                this.customerName = customerName;
            }

            public String getBookingDeposit() {
                return bookingDeposit;
            }

            public void setBookingDeposit(String bookingDeposit) {
                this.bookingDeposit = bookingDeposit;
            }

            public Integer getBookingSort() {
                return bookingSort;
            }

            public void setBookingSort(Integer bookingSort) {
                this.bookingSort = bookingSort;
            }

            public List<GoodListItem> getGoodsList() {
                return goodsList;
            }

            public void setGoodsList(List<GoodListItem> goodsList) {
                this.goodsList = goodsList;
            }

            public String getLockFlag() {
                return lockFlag;
            }

            public void setLockFlag(String lockFlag) {
                this.lockFlag = lockFlag;
            }

            public String getPrimeTime() {
                return primeTime;
            }

            public void setPrimeTime(String primeTime) {
                this.primeTime = primeTime;
            }
        }

        @JsonFilter("JsonBookingDetailList.DataListItem.GoodListItem")
        public static class GoodListItem implements Serializable {
            private Integer goodsId;
            private Integer categoryId;
            private String goodsPrice;
            private Integer goodsLastAttr;
            private String caddieNo;

            public String getCaddieNo() {
                return caddieNo;
            }

            public void setCaddieNo(String caddieNo) {
                this.caddieNo = caddieNo;
            }

            public String getGoodsPrice() {
                return goodsPrice;
            }

            public void setGoodsPrice(String goodsPrice) {
                this.goodsPrice = goodsPrice;
            }

            public Integer getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(Integer goodsId) {
                this.goodsId = goodsId;
            }

            public Integer getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(Integer categoryId) {
                this.categoryId = categoryId;
            }

            public Integer getGoodsLastAttr() {
                return goodsLastAttr;
            }

            public void setGoodsLastAttr(Integer goodsLastAttr) {
                this.goodsLastAttr = goodsLastAttr;
            }
        }

        public static class BookingAreaListItem implements Serializable {
            private String courseAreaType;
            private Integer courseAreaId;
            private String bookingTime;
            private String bookingSignStatus;
            private Integer bookingTimeStatus;
            private String transferFlag;

            public String getTransferFlag() {
                return transferFlag;
            }

            public void setTransferFlag(String transferFlag) {
                this.transferFlag = transferFlag;
            }

            public String getCourseAreaType() {
                return courseAreaType;
            }

            public void setCourseAreaType(String courseAreaType) {
                this.courseAreaType = courseAreaType;
            }

            public Integer getCourseAreaId() {
                return courseAreaId;
            }

            public void setCourseAreaId(Integer courseAreaId) {
                this.courseAreaId = courseAreaId;
            }

            public String getBookingTime() {
                return bookingTime;
            }

            public void setBookingTime(String bookingTime) {
                this.bookingTime = bookingTime;
            }

            public Integer getBookingTimeStatus() {
                return bookingTimeStatus;
            }

            public void setBookingTimeStatus(Integer bookingTimeStatus) {
                this.bookingTimeStatus = bookingTimeStatus;
            }

            public String getBookingSignStatus() {
                return bookingSignStatus;
            }

            public void setBookingSignStatus(String bookingSignStatus) {
                this.bookingSignStatus = bookingSignStatus;
            }
        }
    }
}
