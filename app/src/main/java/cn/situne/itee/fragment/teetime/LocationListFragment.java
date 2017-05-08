/**
 * Project Name: itee
 * File Name:	 LocationListFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-01-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.teetime;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.LocationChoiceAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.BookingTimeEntity;
import cn.situne.itee.entity.LocationChoiceAdapterEntity;
import cn.situne.itee.entity.PositionInformationEntity;
import cn.situne.itee.entity.TimeArea;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.quick.SearchBookingFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonAppointment;
import cn.situne.itee.manager.jsonentity.JsonAppointmentDetail;
import cn.situne.itee.view.AppointmentDetailPopupWindowView;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.TeeTimeCalendarPopupWindowView;

/**
 * ClassName:LocationListFragment <br/>
 * Function: booking page. <br/>
 * UI: 03-3
 * Date: 2015-01-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class LocationListFragment extends BaseFragment {

    private static final int DEFAULT_DOWN_HOUR = 24;
    private static final int DEFAULT_UP_HOUR = 0;
    private int selectedNum;
    private PullToRefreshListView refreshListViewLocation;
    private LocationChoiceAdapter adapterLocationChoice;
    private View.OnClickListener listenerOk;
    private View.OnClickListener listenerSelectDate;
    private int tvSelectedDateId;
    private String title;
    private String currentDate;
    private int currentCourseAreaId;
    private String startTime;
    private String endTime;
    // interval
    private int gapTime = 0;
    //位置数量
    private int positionNumber = 1;
    private int defaultStartHour;
    private int nowStartHour;
    private int nowEndHour;
    private int currentPullDownHour = DEFAULT_DOWN_HOUR;
    private int currentPullUpHour = DEFAULT_UP_HOUR;

    private String[] showTimes;

    private LinkedList<LocationChoiceAdapterEntity> arrAdministration = new LinkedList<>();

    private boolean isCreate;
    private boolean isLoading;

    private IteeTextView tvRight;

    private boolean isFirstCourse;

    private int intervalHour = 2;
    private boolean maybeShowTimes(String times){
        for (String showTime:showTimes){
            if (times.equals(showTime))return true;
        }
        return false;
    }

    private String[] priorityOfSegmentTime = new String[]{
            Constants.SEGMENT_TIME_PRIME_TIME_ID,
            Constants.SEGMENT_TIME_TRANSFER_TIME_BLOCK_ID,
            Constants.SEGMENT_TIME_THREE_TEE_START_ID,
            Constants.SEGMENT_TIME_TWO_TEE_START_ID,
            Constants.SEGMENT_TIME_NINE_HOLES_ONLY_ID,
            Constants.SEGMENT_TIME_NORMAL_ID,
            Constants.SEGMENT_TIME_PRIME_DISCOUNT_ID,
            Constants.SEGMENT_TIME_MEMBER_ONLY_ID,
            Constants.SEGMENT_TIME_EVENT_TIME_ID,
            Constants.SEGMENT_TIME_BLOCK_TIMES_ID};

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_location_choice;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        rootView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);
        isCreate = true;
        arrAdministration = new LinkedList<>();
        refreshListViewLocation = (PullToRefreshListView) rootView.findViewById(R.id.lv_location_choice);
        adapterLocationChoice = new LocationChoiceAdapter(this, arrAdministration, positionNumber);
        adapterLocationChoice.setIsFirstCourse(isFirstCourse);
    }

    private void setSelectedNum(int selectedNum) {
        if (selectedNum > 0) {
            tvRight.setVisibility(View.VISIBLE);
            tvRight.setText(String.format(getString(R.string.common_ok) + " (%d)", selectedNum));
        } else {
            tvRight.setText(String.format(getString(R.string.common_ok) + " (%d)", selectedNum));
            tvRight.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        refreshListViewLocation.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                new LoadingMoreBackgroundTask().execute(false);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new LoadingMoreBackgroundTask().execute(true);
            }
        });

        //搜索事件
        adapterLocationChoice.setPushSearchFragmentListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Bundle bundle = new Bundle();
                Bundle bundle = getArguments();
                bundle.putString("selectDate", currentDate);
                bundle.putString("fragmentSource", SearchBookingFragment.FRAGMENT_SOURCE_3);
                bundle.putString("fragmentType", SearchBookingFragment.FRAGMENT_TYPE_1);
                push(SearchBookingFragment.class, bundle);
            }
        });

        View.OnClickListener listenerAdd = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNum = adapterLocationChoice.getSelectedTimeList().size();
                setSelectedNum(selectedNum);
            }
        };

        View.OnClickListener listenerShow = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String bookingNo = (String) v.getTag();
                Map<String, String> params = new HashMap<>();
                params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
                params.put(ApiKey.ADMINISTRATION_BOOKING_NO, bookingNo);

                HttpManager<JsonAppointmentDetail> hh = new HttpManager<JsonAppointmentDetail>(LocationListFragment.this) {
                    @Override
                    public void onJsonSuccess(JsonAppointmentDetail jo) {
                        Utils.debug(jo.toString());
                        new AppointmentDetailPopupWindowView(getActivity(), refreshListViewLocation, jo.getDataList());
                    }

                    @Override
                    public void onJsonError(VolleyError error) {

                    }
                };
                hh.start(getActivity(), ApiManager.HttpApi.AppointmentDetail, params);
            }
        };

        //region 右上角确认按钮事件
        listenerOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapterLocationChoice.getIsAllowCommit()) {
                    ArrayList<String> errorTimes = new ArrayList<>();
                    //SimpleDateFormat smf = new SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault());
                    SimpleDateFormat smf = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());  //改为当日可预约所有时间
                    String timeStamp = smf.format(new Date());

                    boolean isAgentInEventTime = false;
                    for (String base64Str : adapterLocationChoice.getSelectedTimeList()) {
                        BookingTimeEntity bookingTimeEntity = (BookingTimeEntity) Utils.getObjectFromString(base64Str);
                        //String bookTime = currentDate + Constants.STR_SPACE + bookingTimeEntity.getBookingTime();
                        if (timeStamp.compareTo(currentDate) > 0) {//改为当日可预约所有时间
                        //if (timeStamp.compareTo(bookTime) >= 0) {
                            errorTimes.add(bookingTimeEntity.getBookingTime());
                        }
                        if (Constants.SEGMENT_TIME_EVENT_TIME_ID.equals(bookingTimeEntity.getSegmentTypeId())
                                && AppUtils.isAgent(getActivity())) {
                            isAgentInEventTime = true;
                            break;
                        }
                    }

                    if (Utils.isListNotNullOrEmpty(errorTimes)) {
                        Utils.showShortToast(getActivity(), R.string.msg_booking_reserve_the_time_past);
                                for (LocationChoiceAdapterEntity data:     arrAdministration){
                                    ArrayList<Integer>tmp = new ArrayList<>();
                                  for (Integer ii :data.getPositionStates()){
                                      tmp.add(0);
                                    }
                                    data.setPositionStates(tmp);
                                }
                        setSelectedNum(0);
                        adapterLocationChoice.getBookingTimeEntities().clear();
                        adapterLocationChoice.notifyDataSetChanged();
                    } else if (isAgentInEventTime) {
                        Utils.showShortToast(getActivity(), R.string.msg_agent_can_not_book_in_event_time);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putInt(TransKey.SELECTED_NUM, selectedNum);
                        bundle.putBoolean(TransKey.TEE_TIME_IS_ADD, true);
                        bundle.putInt(ApiKey.ADMINISTRATION_COURSE_AREA_ID, currentCourseAreaId);
                        bundle.putString(ApiKey.ADMINISTRATION_DATE, currentDate);
                        bundle.putString(TransKey.TEE_TIME_TITLE, title);
                        bundle.putStringArrayList(ApiKey.ADMINISTRATION_TIME_START, adapterLocationChoice.getSelectedTimeList());
                        bundle.putString(TransKey.COMMON_FROM_PAGE, LocationListFragment.class.getName());
                        push(TeeTimeAddFragment.class, bundle);
                    }
                } else {
                    Toast.makeText(getActivity(), R.string.segment_times_three_to_reserve, Toast.LENGTH_LONG).show();
                }
            }
        };
        //endregion

        listenerSelectDate = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // show calendar
                final TeeTimeCalendarPopupWindowView calendarPopupWindowView = new TeeTimeCalendarPopupWindowView(LocationListFragment.this, getTvRight(), currentDate,false);
                calendarPopupWindowView.setDateClickListener(new TeeTimeCalendarPopupWindowView.DateClickListener() {
                    @Override
                    public void onDateClick(String date) {
                        Utils.log("location : " + date);
                        if (date != null && !date.equals(currentDate)) {
                            Bundle bundle = new Bundle();
                            currentDate = date;
                            bundle.putString(TransKey.SELECTED_DATE, currentDate);
                            doBackWithReturnValue(bundle);
                        }
                    }
                });
            }
        };
        adapterLocationChoice.setListenerAdd(listenerAdd);
        adapterLocationChoice.setListenerShow(listenerShow);
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        refreshListViewLocation.setAdapter(adapterLocationChoice);
        refreshListViewLocation.setMode(PullToRefreshBase.Mode.BOTH);

        ILoadingLayout headerLayoutProxy = refreshListViewLocation.getLoadingLayoutProxy(true, false);
        ILoadingLayout footerLayoutProxy = refreshListViewLocation.getLoadingLayoutProxy(false, true);

        headerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_down));
        headerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_down));

        footerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_loading_more));
        footerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_loading_more));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();  //此方法位于BaseFragment.java内，用于初始化本页所用的ActionBar

        getTvLeftTitle().setText(title);
        getTvLeftTitle().setEllipsize(TextUtils.TruncateAt.END);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getTvLeftTitle().getLayoutParams();
        layoutParams.width = getActualWidthOnThisDevice(110);
//        layoutParams.height = getActualHeightOnThisDevice(50);
        getTvLeftTitle().setLayoutParams(layoutParams);

        RelativeLayout rlActionBarMenu = (RelativeLayout) getBaseActivity().getSupportActionBar().getCustomView();
        IteeTextView tvSelectedDate = (IteeTextView) rlActionBarMenu.findViewById(tvSelectedDateId);
        if (tvSelectedDate == null) {
            tvSelectedDate = new IteeTextView(getActivity());
            tvSelectedDateId = View.generateViewId();
            tvSelectedDate.setId(tvSelectedDateId);
        }
        tvSelectedDate.setTextSize(Constants.FONT_SIZE_LARGER);
        tvSelectedDate.setTextColor(getColor(R.color.common_white));
        rlActionBarMenu.addView(tvSelectedDate);

        RelativeLayout.LayoutParams paramsTvSelectedDate = (RelativeLayout.LayoutParams) tvSelectedDate.getLayoutParams();
        paramsTvSelectedDate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvSelectedDate.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvSelectedDate.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvSelectedDate.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);

        tvSelectedDate.setLayoutParams(paramsTvSelectedDate);
        tvSelectedDate.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(currentDate, getActivity()));

        getTvRight().setOnClickListener(listenerOk);
        tvSelectedDate.setOnClickListener(listenerSelectDate);

        ImageView ivUnderLine = new ImageView(getActivity());
        ivUnderLine.setBackgroundColor(getColor(R.color.common_white));
        rlActionBarMenu.addView(ivUnderLine);

        RelativeLayout.LayoutParams paramsIvUnderLine = (RelativeLayout.LayoutParams) ivUnderLine.getLayoutParams();
        paramsIvUnderLine.width = (int) (getScreenWidth() * 0.3);
        paramsIvUnderLine.height = 2;
        paramsIvUnderLine.topMargin = DensityUtil.getActualHeightOnThisDevice(10, mContext);
        paramsIvUnderLine.addRule(RelativeLayout.BELOW, tvSelectedDate.getId());
        paramsIvUnderLine.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        ivUnderLine.setLayoutParams(paramsIvUnderLine);

        tvRight = getTvRight();
        tvRight.setVisibility(View.INVISIBLE);
        selectedNum = adapterLocationChoice.getSelectedTimeList().size();
        setSelectedNum(selectedNum);

        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.SELECTED_DATE, currentDate);
                doBackWithReturnValue(bundle, TeeTimePageListFragment.class);//
                return false;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            title = bundle.getString(TransKey.COURSE_AREA_TYPE);
            currentDate = bundle.getString(TransKey.SELECTED_DATE);
            currentCourseAreaId = bundle.getInt(TransKey.COURSE_AREA_TYPE_ID);
            gapTime = bundle.getInt(TransKey.INTERVAL_TIME);
            startTime = bundle.getString(TransKey.START_TIME);
            endTime = bundle.getString(TransKey.END_TIME);
            defaultStartHour = bundle.getInt(TransKey.CURRENT_HOUR);
            positionNumber = bundle.getInt(TransKey.EACH_GROUP_NUM);
            isFirstCourse = bundle.getBoolean(TransKey.IS_FIRST_COURSE);
            if (currentDate == null) {
                currentDate = AppUtils.getTodaySlash();
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        Utils.log("-----------------");
        Utils.log("defaultStartHour : " + defaultStartHour);
        Utils.log("nowStartHour : " + nowStartHour);
        Utils.log("nowEndHour : " + nowEndHour);
        Utils.log("currentPullUpHour : " + currentPullUpHour);
        Utils.log("currentPullDownHour : " + currentPullDownHour);
        Utils.log("-----------------");
        refreshPage();
    }

    @Override
    protected void executeOnceOnCreate() {
        super.executeOnceOnCreate();
//        getBookingData(true, true);
    }

    @Override
    protected void reShowWithBackValue() {
//        Bundle bundle = getReturnValues();
//        if (bundle != null && bundle.getBoolean(TransKey.COMMON_REFRESH)) {
//            String startHour = getHHMMSSStringFromHour(defaultStartHour);
//            String endHour = getHHMMSSStringFromHour(nowEndHour);
//
//            Utils.log("reshow startHour:" + startHour);
//            Utils.log("reshow endHour:" + endHour);
//
//            isCreate = true;
//            getBookingData(true, startHour, endHour, true);
//        }
    }

    public void refreshPage() {
        if (currentPullUpHour != DEFAULT_UP_HOUR && currentPullDownHour != DEFAULT_DOWN_HOUR
                && nowStartHour != 0 && nowEndHour != 0) {
            String startHour = getHHMMSSStringFromHour(currentPullDownHour);
            String endHour = getHHMMSSStringFromHour(currentPullUpHour);
            getBookingData(true, startHour, endHour, true);
        } else {
            getBookingData(true, true);
        }
    }

    private void getBookingData(final boolean isRefresh, boolean isBottom) {

        if (isRefresh) {
            setSelectedNum(0);
            adapterLocationChoice.getBookingTimeEntities().clear();
            nowStartHour = defaultStartHour;
        }

        if (isBottom) {
            int endHourOfToday = AppUtils.getHourFromHHMMSS(endTime);
            if (isRefresh) {
                if (gapTime > 30) {
                    nowEndHour = nowStartHour + 24;
                } else if (gapTime > 25) {
                    nowEndHour = nowStartHour + 7;
                } else if (gapTime > 21) {
                    nowEndHour = nowStartHour + 6;
                } else if (gapTime > 17) {
                    nowEndHour = nowStartHour + 5;
                } else if (gapTime > 12) {
                    nowEndHour = nowStartHour + 4;
                } else if (gapTime > 8) {
                    nowEndHour = nowStartHour + 3;
                } else if (gapTime > 4) {
                    nowEndHour = nowStartHour + 2;
                } else {
                    nowEndHour = nowStartHour + 1;
                }
            } else {
                nowEndHour = nowStartHour + intervalHour;
            }
            if (nowEndHour > endHourOfToday + 1) {
                nowEndHour = endHourOfToday + 1;
            }
        } else {
            nowStartHour = nowStartHour - intervalHour;
            int startHourOfToday = AppUtils.getHourFromHHMMSS(startTime);
            if (nowStartHour < startHourOfToday) {
                nowStartHour = startHourOfToday;
            }
            nowEndHour = nowStartHour + intervalHour;
            if (nowEndHour > currentPullDownHour) {
                nowEndHour = currentPullDownHour;
            }
        }

        String startHour = getHHMMSSStringFromHour(nowStartHour);
        String endHour = getHHMMSSStringFromHour(nowEndHour);

        Utils.log("startHour : " + startHour);
        Utils.log("endHour : " + endHour);

        getBookingData(isRefresh, startHour, endHour, isBottom);
        isCreate = false;

    }

    private void getBookingData(final boolean isRefresh, String startHour, String endHour, final boolean isBottom) {
        if (isRefresh) {
            setSelectedNum(0);
            adapterLocationChoice.getBookingTimeEntities().clear();
        }

        nowStartHour = AppUtils.getHourFromHHMMSS(startHour);
        nowEndHour = AppUtils.getHourFromHHMMSS(endHour);
        if (nowEndHour == 0) {
            nowEndHour = 24;
        }
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.ADMINISTRATION_COURSE_AREA_ID, String.valueOf(currentCourseAreaId));
        params.put(ApiKey.ADMINISTRATION_DATE, currentDate);
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.ADMINISTRATION_TIME_START, String.valueOf(startHour));
        params.put(ApiKey.ADMINISTRATION_TIME_END, String.valueOf(endHour));
        params.put("time_interval", String.valueOf(gapTime));//55886322244

        HttpManager<JsonAppointment> hh = new HttpManager<JsonAppointment>(LocationListFragment.this) {
            @Override
            public void onJsonSuccess(JsonAppointment jo) {
                if (isRefresh) {
                    arrAdministration.clear();
                    adapterLocationChoice.getBookingTimeEntities().clear();
                }
                Utils.log("start : " + nowStartHour + "\nend : " + nowEndHour);
                timeStatus = jo.getTimeStatus();
                showTimes = jo.getBookingTimes().split(Constants.STR_COMMA);
                generateAdministrationList(jo, nowStartHour, nowEndHour, isBottom);
                initData(isRefresh);
                if (isRefresh) {
                    currentPullDownHour = Math.min(nowStartHour, currentPullDownHour);
                    currentPullUpHour = Math.max(nowEndHour, currentPullUpHour);
                } else {
                    currentPullDownHour = Math.min(nowStartHour, currentPullDownHour);
                    currentPullUpHour = Math.max(nowEndHour, currentPullUpHour);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                nowStartHour -= intervalHour;
                initData(isRefresh);
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.Appointment, params, isCreate);
    }

    private String timeStatus;

    // 1 means normal ,0 means now time is later than end time, -1 means is loading
    private Integer loadingMore(Boolean isBottom) {
        Integer res;
        if (!isLoading) {
            isLoading = true;
            if (isBottom) {
                Utils.log("nowEndHour : " + nowEndHour);
                int endHour = AppUtils.getHourFromHHMMSS(endTime);
                if (nowEndHour <= endHour - intervalHour) {
                    nowStartHour = currentPullUpHour;
                    getBookingData(false, true);
                    res = 1;
                } else {
                    res = 0;
                    isLoading = false;
                }
            } else {
                int startHour = AppUtils.getHourFromHHMMSS(startTime);
                if (nowStartHour > startHour) {
                    nowStartHour = currentPullDownHour;
                    getBookingData(false, false);
                    res = 1;
                } else {
                    res = 0;
                    isLoading = false;
                }
            }
        } else {
            res = -1;
        }
        return res;
    }

    private void generateAdministrationList(JsonAppointment jo, int startHour, int endHour, boolean isBottom) {
        if (jo.getDataList() != null) {
            List<JsonAppointment.SegmentTime> segmentTimeList = jo.getDataList().getSegmentTimeList();
            Map<String, ArrayList<JsonAppointment.Appointment>> appointmentMap = jo.getAppointmentMap();

            int sizeOfCurrentHour = 0;
            for (int i = startHour; i < endHour; i++) {
                sizeOfCurrentHour += AppUtils.getAvailableTimePointInCurrentHour(gapTime, startTime, i);
            }

            int startMinute = AppUtils.getStartMinuteInCurrentHour(gapTime, startTime, startHour);
            String exactStartDate = Utils.getHHMMSSTimeStringWithHourMinute(startHour, startMinute);
            long millionSecondsOfStartDate = Utils.getMillionSecondsFromHHMMSS(exactStartDate);

            for (int i = 0; i < sizeOfCurrentHour; i++) {
                String currentTime = Utils.getHHMMSSFromMillionSecondsWithType(millionSecondsOfStartDate, Constants.TIME_FORMAT_HHMMSS);
                if (currentTime.compareTo(endTime) > 0) {
                    break;
                }

                String showTimeWithHourMinute = Utils.getHHMMSSFromMillionSecondsWithType(millionSecondsOfStartDate, Constants.TIME_FORMAT_HHMM);
                millionSecondsOfStartDate = millionSecondsOfStartDate + gapTime * Constants.MINUTES_PER_HOUR * 1000;
                ArrayList<JsonAppointment.Appointment> arrAppointment = appointmentMap.get(currentTime);

                // outside
                LocationChoiceAdapterEntity myEntity = new LocationChoiceAdapterEntity();
                myEntity.setTime(showTimeWithHourMinute);
                myEntity.setPositionNumber(positionNumber);
//                myEntity.setSegmentTimeType(Constants.SEGMENT_TIME_NORMAL_ID);
                int appointmentNumber = 0;  //ysc 已预约的数量
                if (arrAppointment != null) {
                    appointmentNumber = arrAppointment.size();
                }
                myEntity.setAppointmentNumber(appointmentNumber);
                ArrayList<Integer> positionStates = new ArrayList<>();
                for (int n = 0; n < positionNumber; n++) {
                    positionStates.add(0);
                }
                myEntity.setPositionStates(positionStates);

                for (int j = 0; j < segmentTimeList.size(); j++) {
                    JsonAppointment.SegmentTime segmentTime = segmentTimeList.get(j);
                    TimeArea timeArea = new TimeArea();
                    String temp = segmentTime.getTime();
                    if (Utils.isStringNotNullOrEmpty(temp)) {
                        String[] s = temp.split(Constants.STR_SEPARATOR);
                        timeArea.setStartTime(s[0]);
                        timeArea.setEndTime(s[1]);
                        if (Constants.SEGMENT_TIME_TRANSFER_TIME_BLOCK_ID.equals(segmentTime.getSegmentTimeTypeId())) {
                            if (segmentTime.isOpenFlg()) {
                                if (Utils.isTransferBlockTimeInArea(currentTime, timeArea)) {
                                    Integer transferTime = segmentTime.getTransferTime();
                                    myEntity.setTransferTime(transferTime);
                                    myEntity.setSegmentTimeType(segmentTime.getSegmentTimeType());
                                    if (Utils.isStringNotNullOrEmpty(myEntity.getSegmentTimeTypeId())) {
                                        myEntity.setSegmentTimeTypeId(getHighPriority(myEntity.getSegmentTimeTypeId(), segmentTime.getSegmentTimeTypeId()));
                                    } else {
                                        myEntity.setSegmentTimeTypeId(segmentTime.getSegmentTimeTypeId());
                                    }
                                    myEntity.setMemberTypeId(segmentTime.getMemberTypeId());
                                }
                            } else {
                                if (Utils.isTimeInArea(currentTime, timeArea)) {
                                    Integer transferTime = segmentTime.getTransferTime();
                                    myEntity.setTransferTime(transferTime);
                                    myEntity.setSegmentTimeType(segmentTime.getSegmentTimeType());
                                    if (Utils.isStringNotNullOrEmpty(myEntity.getSegmentTimeTypeId())) {
                                        myEntity.setSegmentTimeTypeId(getHighPriority(myEntity.getSegmentTimeTypeId(), segmentTime.getSegmentTimeTypeId()));
                                    } else {
                                        myEntity.setSegmentTimeTypeId(segmentTime.getSegmentTimeTypeId());
                                    }
                                    myEntity.setMemberTypeId(segmentTime.getMemberTypeId());
                                }
                            }
                        } else if (Constants.SEGMENT_TIME_PRIME_TIME_ID.equals(segmentTime.getSegmentTimeTypeId())) {
                            if (Utils.isTimeInArea(currentTime, timeArea)) {
                                myEntity.setPrimeTime(true);
                                Integer transferTime = segmentTime.getTransferTime();
                                myEntity.setTransferTime(transferTime);
                                myEntity.setSegmentTimeType(segmentTime.getSegmentTimeType());
                                if (Utils.isStringNotNullOrEmpty(myEntity.getSegmentTimeTypeId())) {
                                    myEntity.setSegmentTimeTypeId(getHighPriority(myEntity.getSegmentTimeTypeId(), segmentTime.getSegmentTimeTypeId()));
                                } else {
                                    myEntity.setSegmentTimeTypeId(segmentTime.getSegmentTimeTypeId());
                                }
                                myEntity.setMemberTypeId(segmentTime.getMemberTypeId());
                                myEntity.setSegmentTimeSetting(segmentTime.getSegmentTimeSetting());
                            }
                        } else {
                            if (Utils.isTimeInArea(currentTime, timeArea)) {
                                Integer transferTime = segmentTime.getTransferTime();
                                myEntity.setTransferTime(transferTime);
                                myEntity.setSegmentTimeType(segmentTime.getSegmentTimeType());
                                if (Utils.isStringNotNullOrEmpty(myEntity.getSegmentTimeTypeId())) {
                                    myEntity.setSegmentTimeTypeId(getHighPriority(myEntity.getSegmentTimeTypeId(), segmentTime.getSegmentTimeTypeId()));
                                } else {
                                    myEntity.setSegmentTimeTypeId(segmentTime.getSegmentTimeTypeId());
                                }
                                myEntity.setMemberTypeId(segmentTime.getMemberTypeId());
                            }
                        }
                    }
                }

                // inside
                ArrayList<PositionInformationEntity> positionInformationList = new ArrayList<>();

                if (arrAppointment != null && arrAppointment.size() > 0) {
                    for (int j = 0; j < arrAppointment.size(); j++) {

                        JsonAppointment.Appointment appointment = arrAppointment.get(j);
                        PositionInformationEntity positionInformation = new PositionInformationEntity();
                        if (j < myEntity.getPositionStates().size()) {
                            myEntity.getPositionStates().set(j, 2);
                        }
                        positionInformation.setMemberName(appointment.getAppointmentMName());
                        positionInformation.setMemberId(appointment.getAppointmentMid());
                        positionInformation.setMemberType(appointment.getAppointmentMemberType());
                        positionInformation.setMemberGender(appointment.getMemberGender());
                        positionInformation.setPayStatus(appointment.getPayStatus());
                        positionInformation.setCheckInStatus(appointment.getCheckInStatus());
                        positionInformation.setDepositStatus(appointment.getDepositStatus());
                        positionInformation.setAppointmentOrderNo(appointment.getAppointmentOrderNo());
                        positionInformation.setLookFlag(appointment.getLookFlag());
                        positionInformation.setSameDayFlag(appointment.getSameDayFlag());
                        positionInformation.setAppointmentGoods(appointment.getAppointmentGoods());
                        positionInformation.setAppointmentGoodsStatus(appointment.getAppointmentGoodsStatus());
                        positionInformation.setCurrentHole(appointment.getCurrentHole());
                        positionInformation.setCurrentHoleStatus(appointment.getCurrentHoleStatus());
                        positionInformation.setBookingNo(appointment.getBookingNo());
                        positionInformation.setBookingColor(appointment.getBookingColor());
                        positionInformation.setSelfFlag(appointment.getSelfFlag());
                        positionInformationList.add(positionInformation);
                    }
                }
                myEntity.setPositionInformationList(positionInformationList);
                if (Constants.STR_0.equals(timeStatus)){
                    if (isBottom) {
                        arrAdministration.add(myEntity);
                    } else {
                        arrAdministration.add(i, myEntity);
                    }
                }else{
                    if (maybeShowTimes(currentTime)){
                        if (isBottom) {
                            arrAdministration.add(myEntity);
                        } else {
                            arrAdministration.add(i, myEntity);
                        }
                     }
                }
            }
        }
    }

    private String getHighPriority(String oldValue, String newValue) {
        int oldIndex = -1;
        int newIndex = -1;
        for (int i = 0; i < priorityOfSegmentTime.length; i++) {
            if (priorityOfSegmentTime[i].equals(oldValue)) {
                oldIndex = i;
            }
            if (priorityOfSegmentTime[i].equals(newValue)) {
                newIndex = i;
            }
        }
        return newIndex > oldIndex ? newValue : oldValue;
    }

    private void initData(boolean isRefresh) {
        isLoading = false;
        if (Utils.isListNotNullOrEmpty(arrAdministration)) {
            adapterLocationChoice.notifyDataSetChanged();
            if (isRefresh) {
                refreshListViewLocation.getRefreshableView().setSelection(arrAdministration.size() > 2 ? 2 : 0);
            }
        }
        refreshListViewLocation.onRefreshComplete();
//        selectedNum = adapterLocationChoice.getSelectedTimeList().size();
//        setSelectedNum(selectedNum);
        if(getArguments().containsKey(TransKey.BOOKING_ORDER_NO))  //包含BOOKING_ORDER_NO，则是从搜索页面过来的
        {
            String bookingNumber = getArguments().getString(TransKey.BOOKING_ORDER_NO);
            int showIndex = 0;
            for(int i=0;i<arrAdministration.size();i++){
               LocationChoiceAdapterEntity locationChoiceAdapterEntity = arrAdministration.get(i);
                for(int j=0;j<locationChoiceAdapterEntity.getPositionInformationList().size(); j++){
                    PositionInformationEntity entity=locationChoiceAdapterEntity.getPositionInformationList().get(j);
                    if(entity.getBookingNo().equals(bookingNumber))
                    {
                        showIndex = i;
                        break;
                    }
                }
            }
            refreshListViewLocation.getRefreshableView().setSelection(showIndex);
//            adapterLocationChoice.
        }
    }

    private String getHHMMSSStringFromHour(int hour) {
        return Utils.getHHMMSSTimeStringWithHourMinute(hour, 0);
    }

    private class LoadingMoreBackgroundTask extends AsyncTask<Boolean, Void, Integer> {
        @Override
        protected Integer doInBackground(Boolean... params) {
            return loadingMore(params[0]);
        }

        @Override
        protected void onPostExecute(Integer state) {
            if (state != 1) {
                // update adapterLocationChoice
                adapterLocationChoice.notifyDataSetChanged();
                // call on RefreshListView to hide header and notify the listView, refreshing is done
                refreshListViewLocation.onRefreshComplete();
                if (state == 0) {
                    Utils.showShortToast(getActivity(), R.string.msg_loaded);
                }
            }
        }
    }
}

