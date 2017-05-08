/**
 * Project Name: itee
 * File Name:  SelectTransferTimePopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-20
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.widget.wheel.SelectTextWheelAdapter;
import cn.situne.itee.common.widget.wheel.SelectTimeOnWheelChangedListener;
import cn.situne.itee.common.widget.wheel.SelectTimeWheelView;
import cn.situne.itee.common.widget.wheel.SelectTransferTimeWheelAdapter;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.manager.jsonentity.JsonChangeTransferArea;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.TeeTimeAddTitleView;

/**
 * ClassName:SelectTransferTimePopupWindow <br/>
 * Function: select transfer time popup window. <br/>
 * Date: 2015-01-20 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectTransferTimePopupWindow extends BasePopWindow {

    private String changeDate;

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    private View menuView;
    private SelectTimeWheelView wheelViewHour, wheelViewMin;
    private Button btnOk, btnCancel;
    private BaseFragment mFragment;
    private TeeTimeAddTitleView.OnTwoWheelClickListener clickListener;
    private JsonChangeTransferArea.DataList dataList;
    private String mDate;
    private List<JsonBookingDetailList.DataListItem.BookingAreaListItem> areaList;
    private JsonBookingDetailList.DataListItem.BookingAreaListItem bookingAreaListItem;
    private Integer mCourseAreaId;
    private Integer index;

    public SelectTransferTimePopupWindow(BaseFragment mFragment,
                                         JsonBookingDetailList.DataListItem.BookingAreaListItem bookingAreaListItem, String date,
                                         List<JsonBookingDetailList.DataListItem.BookingAreaListItem> areaList, Integer courseAreaId, Integer index,
                                         TeeTimeAddTitleView.OnTwoWheelClickListener clickListener) {

        super(mFragment.getActivity());
        this.mFragment = mFragment;
        this.clickListener = clickListener;
        this.bookingAreaListItem = bookingAreaListItem;
        this.areaList = areaList;
        this.mDate = date;
        this.index = index;
        this.mCourseAreaId = courseAreaId;
        LayoutInflater inflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_transfer_time_inout, null);

        setContentView(menuView);
        setHideListener(menuView);
        formatViews();
        netLinkChangeTransferArea();
    }

    private String oldDate;
    private String newDate;
    private String beforeTime;
    private TeeTimeAddTitleView.ChangeTimeListener clickOk;
    List<JsonBookingDetailList.DataListItem.BookingListItem> bookingList;

    public SelectTransferTimePopupWindow(BaseFragment mFragment,
                                         String date,
                                         List<JsonBookingDetailList.DataListItem.BookingListItem> bookingList, Integer courseAreaId,
                                         TeeTimeAddTitleView.ChangeTimeListener clickOk,String oldDate,String newDate,String beforeTime) {

        super(mFragment.getActivity());

        this.bookingAreaListItem = bookingAreaListItem;
        this.index = index;
        this.areaList = areaList;

        this.mDate = date;
        this.mFragment = mFragment;
        this.clickOk = clickOk;
        this.oldDate = oldDate;
        this.newDate = newDate;
        this.bookingList = bookingList;

        this.beforeTime = beforeTime;
        this.mCourseAreaId = courseAreaId;
        LayoutInflater inflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_transfer_time_inout, null);
        IteeTextView tvTitle = (IteeTextView)menuView.findViewById(R.id.tv_hour);
        tvTitle.setText(R.string.reservation_time);


        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);
        btnCancel.setTextColor(mFragment.getColor(R.color.common_blue));
        btnCancel.setText(mFragment.getString(R.string.common_cancel));

        IteeTextView tv_min = (IteeTextView)menuView.findViewById(R.id.tv_min);
        tv_min.setVisibility(View.GONE);
        setContentView(menuView);
        setHideListener(menuView);
        formatViews();
        netLinkChangeTransferAreaAndBookingDate();
    }


    private String getJsonStr(){
        JSONObject array = new JSONObject();


        try {
            array.put("player_num",bookingList.size());

            JSONArray  jaInfo= new JSONArray();
        for (JsonBookingDetailList.DataListItem.BookingListItem item:bookingList){

            JSONObject jItem = new JSONObject();
            jItem.put("member_flag",item.getCustomerType());
            jItem.put("member_id",item.getCustomerId());
            jaInfo.put(jItem);
        }

            array.put("player_info",jaInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return array.toString();


    }



    private void initChangeView(){

        int wheelViewHourId = 0;
        int wheelViewMinId = 0;

        wheelViewHour = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_inout);




        final List<String> dataSource = new ArrayList<>();
        for (int i = 0; i < dataList.bookingList.size(); i++) {

            JsonChangeTransferArea.TransferTimeItem t = dataList.bookingList.get(i);
            if (bookingAreaListItem != null) {
                if (t.areaId.equals(String.valueOf(bookingAreaListItem.getCourseAreaId()))) {
                    wheelViewHourId = i;
                }
            }
            dataSource.add(t.areaType);
        }


        wheelViewHour.setAdapter(new SelectTextWheelAdapter(dataSource, 5));
        wheelViewHour.setCyclic(false);
        wheelViewHour.setCurrentItem(wheelViewHourId);

        wheelViewMin = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_time);
        JsonChangeTransferArea.TransferTimeItem transferTimeItem = dataList.bookingList.get(wheelViewHour.getCurrentItem());
        for (int i = 0; i < transferTimeItem.timeList.size(); i++) {
//            if (bookingAreaListItem != null) {
//                String compare1 = bookingAreaListItem.getBookingTime();
//                if (bookingAreaListItem.getBookingTime().length() < 6) {
//                    compare1 = bookingAreaListItem.getBookingTime() + ":00";
//                }

                String compare2 = transferTimeItem.timeList.get(i);
                if (transferTimeItem.timeList.get(i).length() < 6) {
                    compare2 = transferTimeItem.timeList.get(i) + ":00";
                }

                if (compare2.equals(dataList.defaultItem.time)) {
                    wheelViewMinId = i;
                }
           // }

        }

        //change hh:MM:ss to hh:mm

        if (dataList.bookingList != null && dataList.bookingList.size() > 0) {
            for (int j = 0; j < dataList.bookingList.size(); j++) {
                JsonChangeTransferArea.TransferTimeItem transferTimeItem1 = dataList.bookingList.get(j);
                for (int i = 0; i < transferTimeItem1.timeList.size(); i++) {
                    String tt = transferTimeItem1.timeList.get(i);
                    transferTimeItem1.timeList.set(i, tt.substring(0, 5));
                }
            }
        }
        wheelViewMin.setAdapter(new SelectTransferTimeWheelAdapter(transferTimeItem));

        wheelViewMin.setCyclic(false);
        wheelViewMin.setCurrentItem(wheelViewMinId);



        wheelViewHour.addChangingListener(new SelectTimeOnWheelChangedListener() {
                                              @Override
                                              public void onChanged(SelectTimeWheelView wheel, int oldValue, int newValue) {
                                                  wheelViewMin.setAdapter(new SelectTransferTimeWheelAdapter(dataList.bookingList.get(newValue)));
                                              }
                                          }
        );

        btnOk.setHeight(30);
        btnCancel.setHeight(30);
        btnOk.setOnClickListener(new View.OnClickListener()

                                 {
                                     @Override
                                     public void onClick(View v) {
                                         if (clickOk!=null){

                                             int position = wheelViewMin.getCurrentItem();
                                             if (dataList.bookingList.get(0).timeList!=null&&dataList.bookingList.get(0).timeList.size()>0){
                                                 clickOk.clickOk(wheelViewMin.getAdapter().getItem(wheelViewMin.getCurrentItem()),
                                                         changeDate,dataList.bookingList.get(0).transferTimeList.get(position),dataList.bookingList.get(0).transferAreaIdList.get(position),dataList.bookingList.get(0).transferAreaNameList.get(position));
                                             }else{
                                                 dismiss();
                                             }

                                         }

                                     }
                                 }

        );
        btnCancel.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View v) {
                                            // clickListener.onTransferTimeReturn("delete_one", null);
                                             dismiss();
                                         }
                                     }

        );

    }

    private void initView() {

        int wheelViewHourId = 0;
        int wheelViewMinId = 0;

        wheelViewHour = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_inout);

        final List<String> dataSource = new ArrayList<>();
        for (int i = 0; i < dataList.bookingList.size(); i++) {

            JsonChangeTransferArea.TransferTimeItem t = dataList.bookingList.get(i);
            if (bookingAreaListItem != null) {
                if (t.areaId.equals(String.valueOf(bookingAreaListItem.getCourseAreaId()))) {
                    wheelViewHourId = i;
                }
            }
            dataSource.add(t.areaType);
        }


        wheelViewHour.setAdapter(new SelectTextWheelAdapter(dataSource, 5));
        wheelViewHour.setCyclic(false);
        wheelViewHour.setCurrentItem(wheelViewHourId);

        wheelViewMin = (SelectTimeWheelView) menuView.findViewById(R.id.wheel_time);
        JsonChangeTransferArea.TransferTimeItem transferTimeItem = dataList.bookingList.get(wheelViewHour.getCurrentItem());
        for (int i = 0; i < transferTimeItem.timeList.size(); i++) {
            if (bookingAreaListItem != null) {
                String compare1 = bookingAreaListItem.getBookingTime();
                if (bookingAreaListItem.getBookingTime().length() < 6) {
                    compare1 = bookingAreaListItem.getBookingTime() + ":00";
                }

                String compare2 = transferTimeItem.timeList.get(i);
                if (transferTimeItem.timeList.get(i).length() < 6) {
                    compare2 = transferTimeItem.timeList.get(i) + ":00";
                }

                if (compare2.equals(compare1)) {
                    wheelViewMinId = i;
                }
            }

        }

        //change hh:MM:ss to hh:mm

        if (dataList.bookingList != null && dataList.bookingList.size() > 0) {
            for (int j = 0; j < dataList.bookingList.size(); j++) {
                JsonChangeTransferArea.TransferTimeItem transferTimeItem1 = dataList.bookingList.get(j);
                for (int i = 0; i < transferTimeItem1.timeList.size(); i++) {
                    String tt = transferTimeItem1.timeList.get(i);
                    transferTimeItem1.timeList.set(i, tt.substring(0, 5));
                }
            }
        }
        wheelViewMin.setAdapter(new SelectTransferTimeWheelAdapter(transferTimeItem));
        wheelViewMin.setCyclic(false);
        wheelViewMin.setCurrentItem(wheelViewMinId);

        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);

        wheelViewHour.addChangingListener(new SelectTimeOnWheelChangedListener() {
                                              @Override
                                              public void onChanged(SelectTimeWheelView wheel, int oldValue, int newValue) {
                                                  wheelViewMin.setAdapter(new SelectTransferTimeWheelAdapter(dataList.bookingList.get(newValue)));
                                              }
                                          }
        );

        btnOk.setHeight(30);
        btnCancel.setHeight(30);
        btnOk.setOnClickListener(new View.OnClickListener()

                                 {
                                     @Override
                                     public void onClick(View v) {
                                         clickListener.onTransferTimeReturn(wheelViewMin.getAdapter().getItem(wheelViewMin.getCurrentItem()),
                                                 dataList.bookingList.get(wheelViewHour.getCurrentItem()));
                                     }
                                 }

        );
        btnCancel.setOnClickListener(new View.OnClickListener()

                                     {
                                         @Override
                                         public void onClick(View v) {
                                             clickListener.onTransferTimeReturn("delete_one", null);
                                         }
                                     }

        );
    }


    public void netLinkChangeTransferArea() {

        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mFragment.getActivity()));
        params.put(ApiKey.TEE_TIME_DATE, mDate);
        params.put(ApiKey.COURSE_ID, AppUtils.getCurrentCourseId(mFragment.getActivity()));
        params.put(ApiKey.TEE_TIME_COURSE_ID, AppUtils.getCurrentCourseId(mFragment.getActivity()));
        params.put(ApiKey.TEE_TIME_AREA_ID, String.valueOf(mCourseAreaId));
        Integer temp = index - 2;
        if (temp < 0) {
            temp = 0;
        }
        params.put(ApiKey.TEE_TIME_TIME, areaList.get(temp).getBookingTime());


        HttpManager<JsonChangeTransferArea> hh = new HttpManager<JsonChangeTransferArea>(mFragment) {

            @Override
            public void onJsonSuccess(JsonChangeTransferArea jo) {
                int tokenStatus = jo.getTokenStatus();

                if (tokenStatus == 1) {
                    dataList = jo.getDataList();
                    initView();
                } else {
                    Utils.showShortToast(mFragment.getActivity(), jo.getReturnInfo());
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(mFragment.getActivity(), ApiManager.HttpApi.ChangeTransferArea, params);
    }


    public void netLinkChangeTransferAreaAndBookingDate() {

        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mFragment.getActivity()));
        params.put(ApiKey.TEE_TIME_AREA_ID, String.valueOf(mCourseAreaId));


        params.put("booking_old_date", oldDate);
        params.put("booking_new_date", newDate);



        params.put(ApiKey.COURSE_ID, AppUtils.getCurrentCourseId(mFragment.getActivity()));
        params.put(ApiKey.TEE_TIME_COURSE_ID, AppUtils.getCurrentCourseId(mFragment.getActivity()));

        params.put("list", getJsonStr());

//        Integer temp = index - 2;
//        if (temp < 0) {
//            temp = 0;
//        }
        params.put("booking_time", beforeTime);


        HttpManager<JsonChangeTransferArea> hh = new HttpManager<JsonChangeTransferArea>(mFragment) {

            @Override
            public void onJsonSuccess(JsonChangeTransferArea jo) {
                int tokenStatus = jo.getTokenStatus();

                if (tokenStatus == 1) {
                    dataList = jo.getDataList();
                    initChangeView();
                } else {
                    Utils.showShortToast(mFragment.getActivity(), jo.getReturnInfo());
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(mFragment.getActivity(), ApiManager.HttpApi.ChangeReservationTime, params);
    }

}
