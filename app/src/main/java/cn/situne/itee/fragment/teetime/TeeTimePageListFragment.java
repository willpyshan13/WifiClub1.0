/**
 * Project Name: itee
 * File Name:	 TeeTimePageListFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-01-26
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.teetime;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.android.volley.VolleyError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.TeeTimeListPagerAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.administration.DateSettingFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonMain;
import cn.situne.itee.view.PopTeeTimesMenuView;
import cn.situne.itee.view.TeeTimeCalendarPopupWindowView;
import cn.situne.itee.view.popwindow.IteeYesAndOnPopupWindow;

/**
 * ClassName:TeeTimePageListFragment <br/>
 * Function: main page . <br/>
 * Date: 2015-01-26 <br/>
 * UI:03-1
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class TeeTimePageListFragment extends BaseFragment {
    private IteeYesAndOnPopupWindow settingPopup;
    private static final int COURSE_NUM_PER_PAGE = 2;
    private boolean isAuthCourseData;
    private boolean isAuthTetTimes;
    private String selectedDate;

    private ViewPager myViewPager;
    private TeeTimeListPagerAdapter pagerAdapter;
    private TeeTimeCalendarPopupWindowView calendarPopupWindowView;

    //自定义的弹出框类
    private PopTeeTimesMenuView menuWindow;
    private View.OnClickListener itemsOnClick;

    @Override
    protected void initControls(View rootView) {
        rootView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        myViewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        isAuthCourseData = AppUtils.getAuth(AppUtils.AuthControl.AuthControlEditCourseData, getActivity());
        isAuthTetTimes = AppUtils.getAuth(AppUtils.AuthControl.AuthControlEditTeeTimes, getActivity());
        settingPopup = new IteeYesAndOnPopupWindow(getActivity(), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push(TeeTimeCalendarFragment.class);
            }
        }, TeeTimePageListFragment.this);

        settingPopup.setMessage(getString(R.string.segment_setting_alert_mes));
        settingPopup.getOkBtn().setText(getString(R.string.segment_setting_alert_btn_1));
        settingPopup.getCancelBtn().setText(getString(R.string.segment_setting_alert_btn_2));
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        //为弹出窗口实现监听类
        itemsOnClick = new View.OnClickListener() {

            public void onClick(View v) {
                menuWindow.dismiss();
                switch (v.getId()) {
                    case R.id.ll_course_data:
                        push(CourseListFragment.class);
                        break;
                    case R.id.ll_tee_times:
                        push(TeeTimeCalendarFragment.class);
                        break;
                    case R.id.ll_data_setting:
                        push(DateSettingFragment.class, null);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    @Override
    protected void setLayoutOfControls() {
    }

    @Override
    protected void setPropertyOfControls() {
    }

    @Override
    public int getTitleResourceId() {
        return R.string.tee_time_setting;
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_appointment_browse;
    }

    @Override
    protected void configActionBar() {
        setTeeTimeActionBar();
        Log.e("lc", "title = " + selectedDate);
        setTitle(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(selectedDate, getActivity()));

        if (isAuthCourseData || isAuthTetTimes) {
            getTvRight().setBackgroundResource(R.drawable.icon_over_flow);
            getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
                @Override
                public void noDoubleClick(View v) {
                    //实例化SelectPicPopupWindow
                    menuWindow = new PopTeeTimesMenuView(getActivity(), itemsOnClick);
                    //显示窗口
                    ActionBar actionBar = getActivity().getActionBar();
                    if (actionBar != null) {
                        menuWindow.showAsDropDown(actionBar.getCustomView(), getScreenWidth(), 0);
                    } else {
                        menuWindow.showAsDropDown(getTvRight(), 0, 0);
                    }
                }
            });
        }

        getTvTitle().setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                // show calendar
                calendarPopupWindowView = new TeeTimeCalendarPopupWindowView(TeeTimePageListFragment.this, getTvRight(), selectedDate, false);
                calendarPopupWindowView.setDateClickListener(new TeeTimeCalendarPopupWindowView.DateClickListener() {
                    @Override
                    public void onDateClick(String date) {
                        if (date != null && !date.equals(selectedDate)) {
                            selectedDate = date;
                            generateCells();
                        }
                    }
                });
            }
        });

        if (AppUtils.isAgent(mContext)) {
            getTvRight().setVisibility(View.INVISIBLE);
        } else {
            getTvRight().setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        //modify by songyb 1126 【03-1】从03-3页面返回该页时，该页会刷新两次 fixed
        Bundle bundle = getReturnValues();
        if (bundle == null) {
            generateCells();
        }
    }

    @Override
    protected void executeOnceOnCreate() {
        super.executeOnceOnCreate();
        if (Utils.isStringNullOrEmpty(selectedDate)) {
            selectedDate = AppUtils.getTodaySlash();
        }
    }

    public void doRefresh() {
        generateCells();
    }

    private void generateCells() {
        if (getTvTitle() != null) {
            getTvTitle().setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(selectedDate, getActivity()));
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.COMMON_DATE, Utils.isStringNotNullOrEmpty(selectedDate) ? selectedDate : AppUtils.getToday());

        HttpManager<JsonMain> mainManager = new HttpManager<JsonMain>(TeeTimePageListFragment.this) {
            @Override
            public void onJsonSuccess(JsonMain jo) {
                JsonMain.DataList data = jo.getDataList();
                if (data.getBookingList().size() <= 0) {
                    settingPopup.showAtLocation(getRootView().findViewById(R.id.rl_content_container),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                }

                List<JsonMain.DataList.CourseAreaTypeItem> courseAreaTypeList = data.courseAreaTypeList;
                String startTime = data.startTime;
                String endTime = data.endTime;
                int startHour = 0;
                int endHour = 0;

                if (courseAreaTypeList != null && courseAreaTypeList.size() > 0) {
                    if (Utils.isStringNotNullOrEmpty(startTime) && Utils.isStringNotNullOrEmpty(endTime)) {
                        SimpleDateFormat sdf = new SimpleDateFormat(Constants.TIME_FORMAT_HHMM, Locale.getDefault());
                        try {
                            Date startTimeDate = sdf.parse(startTime);
                            Date endTimeDate = sdf.parse(endTime);
                            Calendar startTimeCalendar = Calendar.getInstance();
                            Calendar endTimeCalendar = Calendar.getInstance();

                            startTimeCalendar.setTime(startTimeDate);
                            endTimeCalendar.setTime(endTimeDate);

                            startHour = startTimeCalendar.get(Calendar.HOUR_OF_DAY);
                            endHour = endTimeCalendar.get(Calendar.HOUR_OF_DAY);
                        } catch (ParseException e) {
                            Utils.log(e.getMessage());
                        }
                    }
                    ArrayList<Fragment> fragmentList = new ArrayList<>();
                    for (int i = 0; i < Math.ceil((double) courseAreaTypeList.size() / COURSE_NUM_PER_PAGE); i++) {
                        JsonMain.DataList.CourseAreaTypeItem leftCourseItem = courseAreaTypeList.get(i * COURSE_NUM_PER_PAGE);
                        JsonMain.DataList.CourseAreaTypeItem rightCourseItem = (i * COURSE_NUM_PER_PAGE + 1) < courseAreaTypeList.size() ? courseAreaTypeList.get(i * COURSE_NUM_PER_PAGE + 1) : null;
                        String leftCourseArea = leftCourseItem.areaName;
                        String rightCourseArea = rightCourseItem == null ? Constants.STR_EMPTY : rightCourseItem.areaName;

                        int leftCourseAreaId = leftCourseItem.areaId;
                        int rightCourseAreaId = rightCourseItem == null ? 0 : rightCourseItem.areaId;

                        boolean isLeftLock = leftCourseItem.lockStatus != null && Constants.COURSE_AREA_LOCK_STATUS_LOCKED == leftCourseItem.lockStatus;
                        boolean isRightLock = rightCourseItem != null && rightCourseItem.lockStatus != null
                                && Constants.COURSE_AREA_LOCK_STATUS_LOCKED == rightCourseItem.lockStatus;

                        TeeTimePageItemFragment itemFragment = new TeeTimePageItemFragment();
                        itemFragment.setLeftCourseArea(leftCourseArea);
                        itemFragment.setRightCourseArea(rightCourseArea);
                        itemFragment.setLeftCourseAreaId(leftCourseAreaId);
                        itemFragment.setRightCourseAreaId(rightCourseAreaId);
                        if (Constants.STR_0.equals(data.getTimeStatus())) {
                            itemFragment.setIsUserShowTime(false);
                        } else {
                            itemFragment.setIsUserShowTime(true);
                        }

                        ArrayList<String> timeList = new ArrayList<>();
                        if (Utils.isStringNotNullOrEmpty(data.getBookingTimes())) {
                            for (String s : data.getBookingTimes().split(Constants.STR_COMMA)) {
                                timeList.add(s);
                            }
                        }
                        itemFragment.setShowTimeList(timeList);
                        itemFragment.setLeftLock(isLeftLock);
                        itemFragment.setRightLock(isRightLock);
                        itemFragment.setStartHour(startHour);
                        itemFragment.setEndHour(endHour);
                        itemFragment.setData(jo);
                        itemFragment.setSelectedDate(selectedDate);
                        itemFragment.setCurrentPage(i);
                        itemFragment.setListFragment(TeeTimePageListFragment.this);
                        fragmentList.add(itemFragment);
                    }
                    if (fragmentList.size() <= 0)
                        Utils.showShortToast(TeeTimePageListFragment.this.getBaseActivity(), "ttt");
                    pagerAdapter = new TeeTimeListPagerAdapter(getChildFragmentManager(), fragmentList);
                    myViewPager.setAdapter(pagerAdapter);
                    pagerAdapter.notifyDataSetChanged();
                } else {
                    push(CourseAddFragment.class);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        mainManager.startGet(getActivity(), ApiManager.HttpApi.BookingBrowse, params);
    }

    @Override
    protected void reShowWithBackValue() {
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            selectedDate = bundle.getString(TransKey.SELECTED_DATE);
            if (selectedDate != null) {
                generateCells();
            }
        }
    }

    private Thread initData = new Thread(new Runnable() {
        @Override
        public void run() {

        }
    });


}
