package cn.situne.itee.fragment.teetime;

import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.android.volley.VolleyError;
import com.squareup.timessquare.CalendarPickerView;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.utils.UtilsIsSerialDate;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.administration.ChooseDateFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonLogin;
import cn.situne.itee.manager.jsonentity.JsonTeeTimeCalendar;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectYearPopupWindow;

public class TeeTimeCalendarFragment extends BaseFragment {

    private boolean isEdit;

    private CalendarPickerView calendarPickerView;

    private RelativeLayout rlCalendarViewContainer;

    private LinearLayout llTeeTimeBtnContainer;

    private IteeButton btnTeeTime;

    private IteeButton btnSegmentTime;

    private GridView gridView;

    private ArrayList<com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus> yearDateStatuses = new ArrayList<>();

    private String courseId;

    private ArrayList<com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus> yearDateStatusesGet = new ArrayList<>();

    private String selectedYear;

    private SelectYearPopupWindow selectYearPopupWindow;

    private String editOrAdd;

    private List<Date> dateList;

    private List<Date> allDateList;

    private List<String> teeTimeSettingList;

    private List<String> allTeeTimeSettingList;

    private static boolean isLeapYear(int year) {
        if (year % 100 == 0) {
            if (year % 400 == 0) {
                return true;
            }
        } else {
            if (year % 4 == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_tee_times_calendar;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.app_name;
    }

    @Override
    protected void initControls(View rootView) {


        rlCalendarViewContainer = (RelativeLayout) rootView.findViewById(R.id.rl_calendar_view);

        gridView = (GridView) rootView.findViewById(R.id.calendar_description_gridview);

        llTeeTimeBtnContainer = (LinearLayout) rootView.findViewById(R.id.ll_tee_time_btn_container);


        initGridView();

    }

    @Override
    protected void setDefaultValueOfControls() {


        if (JsonLogin.BOOKING_TYPE_2.equals(AppUtils.getBookingType(getBaseActivity()))){
            IteeTextView tvText  = new IteeTextView(getBaseActivity());
            llTeeTimeBtnContainer.addView(tvText);
            LinearLayout.LayoutParams tvTextParams = (LinearLayout.LayoutParams) tvText.getLayoutParams();
            tvTextParams.width =  LinearLayout.LayoutParams.MATCH_PARENT;
            tvTextParams.height = (int) (getScreenHeight() * 0.1);
            tvText.setLayoutParams(tvTextParams);
            tvText.setText(getString(R.string.advanced_setting));
            tvText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            tvText.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
            tvText.setPadding(0, 0, getActualWidthOnThisDevice(20), 0);
            tvText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();

                    bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeCalendarFragment.class.getName());
                    bundle.putString("selectedYear", getTvTitle().getText().toString());
                    push(SegmentASetingList.class, bundle);
                }
            });
            llTeeTimeBtnContainer.setVisibility(View.GONE);


            //gridView.setVisibility(View.GONE);
        }else{
            llTeeTimeBtnContainer.addView(btnTeeTime);
            LinearLayout.LayoutParams paramsBtnTeeTime = (LinearLayout.LayoutParams) btnTeeTime.getLayoutParams();
            paramsBtnTeeTime.width = (int) (getScreenWidth() * 0.5);
            paramsBtnTeeTime.height = (int) (getScreenHeight() * 0.1);
            btnTeeTime.setLayoutParams(paramsBtnTeeTime);

            llTeeTimeBtnContainer.addView(btnSegmentTime);
            LinearLayout.LayoutParams paramsBtnSegmentTime = (LinearLayout.LayoutParams) btnSegmentTime.getLayoutParams();
            paramsBtnSegmentTime.width = (int) (getScreenWidth() * 0.5);
            paramsBtnSegmentTime.height = (int) (getScreenHeight() * 0.1);
            btnSegmentTime.setLayoutParams(paramsBtnSegmentTime);
        }



    }

    @Override
    protected void setListenersOfControls() {


        btnTeeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendarPickerView != null) {
                    dateList = calendarPickerView.getSelectedDates();

                    //have select least one day
                    if (dateList.size() > 0) {
                        int dateSize = dateList.size();

                        List<String> bookingList = new ArrayList<>();

                        for (Date date : dateList) {
                            bookingList.add(getBooking(date));
                        }

                        if (bookingList.contains("1")) {
                            Utils.showShortToast(getActivity(), R.string.tee_times_has_been_reserved);
                        } else {
                            //1.select one day
                            if (dateSize == 1) {
                                if (Utils.isStringNullOrEmpty(getTeeTimeSetting(dateList.get(0)))) {
                                    //not tee time setting
                                    editOrAdd = "allNull";
                                } else {
                                    //has tee time setting
                                    editOrAdd = "allSame";
                                }

                                Bundle bundle = new Bundle();
                                bundle.putString("course_id", courseId);


                                StringBuilder chooseDate = new StringBuilder();

                                for (Date date : dateList) {
                                    chooseDate.append(DateUtils.getAPIYearMonthDay(date));

                                    chooseDate.append(Constants.STR_COMMA);
                                }
                                String chooseStr = chooseDate.toString();

                                if (chooseStr.length() > 0) {
                                    chooseStr = chooseStr.substring(0, chooseStr.length() - 1);

                                }
                                bundle.putString("choose_date", chooseStr);

                                bundle.putSerializable("start_date", dateList.get(0));
                                bundle.putSerializable("end_date", dateList.get(0));
                                bundle.putString("edit_or_add", editOrAdd);
                                push(TeeTimeSettingEditOrAddFragment.class, bundle);

                            }

                            //2.select some days
                            else if (dateSize > 1) {
                                DateUtils.DateCompare c = new DateUtils.DateCompare();
                                Collections.sort(dateList, c);

                                teeTimeSettingList = new ArrayList<>();

                                for (Date date : dateList) {
                                    teeTimeSettingList.add(getTeeTimeSetting(date));
                                }

                                //serial
                                // if (UtilsIsSerialDate.isSerialDate(dateList)) {

                                boolean isSameTeeTimeSetting = true;

                                String firstTeeTimeSetting = teeTimeSettingList.get(0);

                                for (int i = 1; i < teeTimeSettingList.size(); i++) {
                                    if (!firstTeeTimeSetting.equals(teeTimeSettingList.get(i))) {
                                        isSameTeeTimeSetting = false;
                                        break;
                                    }
                                }
                                //tee time setting相同并且不为空
                                if (isSameTeeTimeSetting && Utils.isStringNotNullOrEmpty(firstTeeTimeSetting)) {
                                    editOrAdd = "allSame";
                                }
                                //都没有tee time setting
                                else if (isSameTeeTimeSetting && Utils.isStringNullOrEmpty(firstTeeTimeSetting)) {
                                    editOrAdd = "allNull";
                                }
                                //部分没有tee time setting
                                else {
                                    editOrAdd = "notAll";
                                }


                                Bundle bundle = new Bundle();
                                StringBuilder chooseDate = new StringBuilder();

                                for (Date date : dateList) {
                                    chooseDate.append(DateUtils.getAPIYearMonthDay(date));

                                    chooseDate.append(Constants.STR_COMMA);
                                }
                                String chooseStr = chooseDate.toString();

                                if (chooseStr.length() > 0) {
                                    chooseStr = chooseStr.substring(0, chooseStr.length() - 1);

                                }
                                bundle.putString("choose_date", chooseStr);

                                bundle.putString("course_id", courseId);
                                bundle.putSerializable("start_date", dateList.get(0));
                                bundle.putSerializable("end_date", dateList.get(dateSize - 1));
                                bundle.putString("edit_or_add", editOrAdd);
                                push(TeeTimeSettingEditOrAddFragment.class, bundle);

//                                } else {// not serial
//                                    Utils.showShortToast(getActivity(), getResources().getString(R.string.tee_times_selected_not_serial_dates));
//                                }
                            }
                        }


                    }
                    //not select any day
                    else {
                        Utils.showShortToast(getActivity(), getResources().getString(R.string.tee_times_select_date));
                    }
                }
            }
        });

        btnSegmentTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendarPickerView != null) {
                    dateList = calendarPickerView.getSelectedDates();

                    DateUtils.DateCompare c = new DateUtils.DateCompare();
                    Collections.sort(dateList, c);

                    if (dateList.size() > 0) {
                        ArrayList<String> bookingList = new ArrayList<>();

                        for (Date date : dateList) {
                            bookingList.add(getBooking(date));
                        }

                        if (bookingList.contains("1")) {
                            Utils.showShortToast(getActivity(), R.string.tee_times_has_been_reserved);
                        } else {

                            allDateList = getAllDateBetweenFirstAndLast(dateList);


                            teeTimeSettingList = new ArrayList<>();
                            allTeeTimeSettingList = new ArrayList<>();

                            for (Date date : dateList) {
                                teeTimeSettingList.add(getTeeTimeSetting(date));
                            }

                            for (Date date : allDateList) {
                                allTeeTimeSettingList.add(getTeeTimeSetting(date));
                            }

                            //是否选择了没有设置tee times的日期
                            if (teeTimeSettingList.contains(Constants.STR_EMPTY)) {
                                Utils.showShortToast(getActivity(), getResources().getString(R.string.tee_times_selected_not_tee_times_setting_date));
                            } else {

                                boolean isSameTeeTimeSetting = true;
                                String firstTeeTimeSetting = allTeeTimeSettingList.get(0);


                                for (int i = 1; i < allTeeTimeSettingList.size(); i++) {
                                    if (!firstTeeTimeSetting.equals(allTeeTimeSettingList.get(i))) {
                                        isSameTeeTimeSetting = false;
                                        break;
                                    }
                                }

                                //判断是否在相同的tee times段里
                                if (isSameTeeTimeSetting) {
                                    ArrayList<String> dateStringList = new ArrayList<>();
                                    StringBuilder sb = new StringBuilder("[");

                                    if (dateList.size() == 1) {
                                        String date = DateUtils.getAPIYearMonthDay(dateList.get(0));
                                        dateStringList.add(date);
                                        sb.append("{\"date\":\"")
                                                .append(date)
                                                .append("\"}");
                                    } else {
                                        for (int i = 0; i < dateList.size(); i++) {
                                            String date = DateUtils.getAPIYearMonthDay(dateList.get(i));
                                            dateStringList.add(date);
                                            if (i == 0) {
                                                sb.append("{\"date\":\"")
                                                        .append(date)
                                                        .append("\"}");
                                            } else if (i > 0) {
                                                sb.append(",{\"date\":\"")
                                                        .append(date)
                                                        .append("\"}");
                                            }
                                        }
                                    }
                                    sb.append("]");
                                    for (String dateItem : dateStringList) {
                                        Log.i("--dateItem", dateItem);
                                    }

                                    Bundle bundle = new Bundle();
                                    bundle.putStringArrayList("dateList", dateStringList);
                                    bundle.putString("date", sb.toString());
                                    push(SegmentTimesEditOrAddFragment.class, bundle);
                                } else {
                                    Utils.showShortToast(getActivity(), getResources().getString(R.string.tee_times_selected_different_tee_times));
                                }


                            }
                        }

                    }
                    //not select any day
                    else {
                        Utils.showShortToast(getActivity(), getResources().getString(R.string.tee_times_select_date));
                    }
                }
            }
        });


    }

    protected void getData(final String currentYear) {
        JsonLogin jl = (JsonLogin) Utils.readFromSP(getActivity(), Constants.KEY_SP_LOGIN_INFO);
        if (jl != null) {
            Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.TEE_TIME_CALENDAR_MONTH, currentYear);
        HttpManager<JsonTeeTimeCalendar> hh = new HttpManager<JsonTeeTimeCalendar>(TeeTimeCalendarFragment.this) {

            @Override
            public void onJsonSuccess(JsonTeeTimeCalendar jo) {
                initData(jo, currentYear);
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };

        hh.start(getActivity(), ApiManager.HttpApi.TeeTimeCalendar, params);
    }

    }

    @Override
    protected void setLayoutOfControls() {


    }

    @Override
    protected void setPropertyOfControls() {

        btnTeeTime = new IteeButton(getActivity());
        btnSegmentTime = new IteeButton(getActivity());

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, getColor(R.color.common_blue)); // 边框粗细及颜色
        drawable.setColor(getColor(R.color.common_white)); // 边框内部颜色

        btnTeeTime.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnSegmentTime.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnTeeTime.setText(R.string.tee_times_tee_times);
        btnTeeTime.setTextColor(getColor(R.color.common_blue));
        btnSegmentTime.setText(R.string.tee_times_segment_times);
        btnSegmentTime.setTextColor(getColor(R.color.common_blue));

        btnTeeTime.setTextSize(Constants.FONT_SIZE_LARGER);
        btnSegmentTime.setTextSize(Constants.FONT_SIZE_LARGER);
    }

    @Override
    protected void configActionBar() {

        setCalendarActionBar();
        selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        setTitle(selectedYear);



//        if (isEdit) {
//            getTvRight().setBackground(null);
//            getTvRight().setText(R.string.common_ok);
//            gridView.setVisibility(View.GONE);
//            //
//
//
//        } else {
//            getTvRight().setBackgroundResource(R.drawable.tee_time_calendar_icon_edit);
//            getTvRight().setText(StringUtils.EMPTY);
//            llTeeTimeBtnContainer.setVisibility(View.GONE);
//           // gridView.setVisibility(View.VISIBLE);
//        }

        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
           getTvRight().setText(StringUtils.EMPTY);

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                bundle.putString(TransKey.SELECTED_DATE, Constants.STR_EMPTY);
                bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeCalendarFragment.class.getName());
                bundle.putBoolean("isTeeTimeSetting",true);
                push(ChooseDateFragment.class, bundle);


            }
        });

        ImageView ivShop = new ImageView(getActivity());
        ivShop.setImageResource(R.drawable.tee_time_icon_1);


        ivShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeCalendarFragment.class.getName());
                bundle.putString("selectedYear", getTvTitle().getText().toString());
                push(SegmentASetingList.class, bundle);
            }
        });


        RelativeLayout.LayoutParams ivPackageLayoutParams
                = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(60), getActualWidthOnThisDevice(60));
        ivPackageLayoutParams.addRule(RelativeLayout.LEFT_OF, getTvRight().getId());
        ivPackageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivPackageLayoutParams.rightMargin = getActualWidthOnThisDevice(30);
        ivShop.setLayoutParams(ivPackageLayoutParams);
        RelativeLayout parent = (RelativeLayout) getTvRight().getParent();
//        getTvRight().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isEdit = !isEdit;
//                if (isEdit) {
//                    getTvRight().setBackground(null);
//                    getTvRight().setText(R.string.common_ok);
//                    gridView.setVisibility(View.GONE);
//                   // llTeeTimeBtnContainer.setVisibility(View.VISIBLE);
//
//                } else {
//                    getTvRight().setBackgroundResource(R.drawable.tee_time_calendar_icon_edit);
//                    getTvRight().setText(StringUtils.EMPTY);
//                    llTeeTimeBtnContainer.setVisibility(View.GONE);
//                   // gridView.setVisibility(View.VISIBLE);
//                    if (calendarPickerView != null) {
//                        calendarPickerView.clearOldSelections();
//                    }
//
//                }
//            }
//        });
        parent.addView(ivShop);
        ivShop.setVisibility(View.GONE);

        parent.invalidate();
        getTvTitle().setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                int year = 0;
                try {
                    year = Integer.parseInt(getTvTitle().getText().toString());
                }catch (NumberFormatException e){

                }

                // select year
                selectYearPopupWindow = new SelectYearPopupWindow(getActivity(), null,year);
                selectYearPopupWindow.showAtLocation(TeeTimeCalendarFragment.this.getRootView().findViewById(R.id.popup_window),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                selectYearPopupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        selectedYear = selectYearPopupWindow.wheelViewYear.getCurrentItem() + Constants.STR_EMPTY;
                        setTitle(selectedYear);
                        yearDateStatuses.clear();

                        getData(selectedYear);
                        calendarPickerView.validateAndUpdate();
                        selectYearPopupWindow.dismiss();
                    }
                });
                selectYearPopupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectYearPopupWindow.dismiss();
                    }
                });
            }
        });

    }

    protected void initCalendar(ArrayList<com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus> yearDateStatuses,
                                String currentYear) {
           final Calendar startTime = Calendar.getInstance();
        startTime.set(Integer.valueOf(currentYear), 0, 1);
        final Calendar endTime = Calendar.getInstance();
        endTime.set(Integer.valueOf(currentYear) + 1, 0, 1);

        Calendar today = Calendar.getInstance();


        if (AppUtils.getCurrentFirstDayOfWeek(getActivity()).equals(Constants.FIRST_DAY_SUN)) {
            //将星期日作为一个星期中的第一天
            calendarPickerView.init(startTime.getTime(), endTime.getTime(), Locale.getDefault(), yearDateStatuses, 0)
                    .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        } else {
            //将星期一作为一个星期中的第一天
            calendarPickerView.init(startTime.getTime(), endTime.getTime(), Locale.getDefault(), yearDateStatuses, 1)
                    .inMode(CalendarPickerView.SelectionMode.MULTIPLE);
        }


        int month = today.get(Calendar.MONTH);
        //定位到当前月
        calendarPickerView.setSelection(month);
        calendarPickerView.setDateBeforeSelectListener(new CalendarPickerView.BeforeSelectDateListener() {
            @Override
            public boolean doBeforeSelect(Date date) {
                if (isEdit) {
                    return true;
                } else {
                    if (isSegmentTimeSetting(date)) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("date", date);
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeCalendarFragment.class.getName());

                        bundle.putSerializable("edit", "1");

                        push(TeeTimeLimitFragment.class, bundle);
                    } else {
                        Utils.showShortToast(getActivity(), getResources().getString(R.string.tee_times_not_segment_times));
                    }
                    return false;
                }
            }
        });


        calendarPickerView.validateAndUpdate();


    }

    protected void initGridView() {
        int[] images = new int[]{R.drawable.icon_calendar_gridview_block_times,
                R.drawable.icon_calendar_gridview_booking_here,
                R.drawable.icon_calendar_gridview_member_only,


                R.drawable.icon_calendar_gridview_prime_time};

        String[] tvDescription = new String[]{getResources().getString(R.string.tee_times_block_times),
                getResources().getString(R.string.tee_times_booking_here),
                getResources().getString(R.string.tee_times_member_only),


                getResources().getString(R.string.tee_times_prime_time)};

        ArrayList<HashMap<String, Object>> lstImageItem = new ArrayList<>();
        for (int i = 0; i < images.length; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("itemImage", images[i]);
            map.put("itemText", tvDescription[i]);
            lstImageItem.add(map);
        }

        SimpleAdapter saImageItems = new SimpleAdapter(getActivity(),
                lstImageItem,// 数据源
                R.layout.item_of_fragment_calendar_gridview,// 显示布局
                new String[]{"itemImage", "itemText"},
                new int[]{R.id.iv_icon_color_description, R.id.tv_color_description});
        gridView.setAdapter(saImageItems);
    }

    //接口返回数据
    protected void initData(JsonTeeTimeCalendar jo, String currentYear) {

        calendarPickerView = new CalendarPickerView(getActivity(), null);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT);
        calendarPickerView.setLayoutParams(layoutParams);

        rlCalendarViewContainer.addView(calendarPickerView);

        JsonTeeTimeCalendar.DataList jsonDataList = jo.getDataList();


        List<JsonTeeTimeCalendar.DataList.DateStatus> dateStatuses = jsonDataList.getDateStatus();
        courseId = jsonDataList.getCourseId().toString();
        for (int i = 0; i < dateStatuses.size(); i++) {

            com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus dateStatus
                    = new com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus();

            dateStatus.date = dateStatuses.get(i).getDate();

            List<JsonTeeTimeCalendar.DataList.DateStatus.StatusList> statusLists = dateStatuses.get(i).getStatusList();

            dateStatus.statusList = new ArrayList<>();

            for (int j = 0; j < statusLists.size(); j++) {
                int flag = 1;
                com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus.StatusList statusList
                        = new com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus.StatusList();

                int day = statusLists.get(j).getDay();
                String teeTimeSetting = statusLists.get(j).getTeeTimeSetting();
                int block = statusLists.get(j).getBlock();
                int nineHoles = statusLists.get(j).getNineHoles();
                int threeTeeStart = statusLists.get(j).getThreeTeeStart();
                int memberOnly = statusLists.get(j).getMemberOnly();
                int booking = statusLists.get(j).getBooking();
                int primeTime = statusLists.get(j).getPrimeTime();
                int twoTeeStart = statusLists.get(j).getTwoTeeStart();

                if (Utils.isStringNotNullOrEmpty(String.valueOf(day))) {
                    statusList.day = String.valueOf(day);
                    statusList.teeTimeSetting = teeTimeSetting;
                    statusList.block = String.valueOf(block);
                    statusList.nineHoles = String.valueOf(nineHoles);
                    statusList.threeTeeStart = String.valueOf(threeTeeStart);
                    statusList.memberOnly = String.valueOf(memberOnly);
                    statusList.booking = String.valueOf(booking);
                    statusList.primeTime = String.valueOf(primeTime);
                    statusList.twoTeeStart = String.valueOf(twoTeeStart);
                } else {
                    statusList.day = String.valueOf(flag);
                    statusList.teeTimeSetting = Constants.STR_EMPTY;
                    statusList.block = Constants.STR_EMPTY;
                    statusList.nineHoles = Constants.STR_EMPTY;
                    statusList.threeTeeStart = Constants.STR_EMPTY;
                    statusList.memberOnly = Constants.STR_EMPTY;
                    statusList.booking = Constants.STR_EMPTY;
                    statusList.primeTime = Constants.STR_EMPTY;
                    statusList.twoTeeStart = Constants.STR_EMPTY;
                }
                dateStatus.statusList.add(statusList);
            }
            yearDateStatuses.add(dateStatus);
        }

        initCalendarData(yearDateStatuses, currentYear);

        initCalendar(yearDateStatusesGet, currentYear);
    }

    protected void initCalendarData(ArrayList<com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus> yearDateStatuses,
                                    String currentYear) {

//        HashMap<Date, String> teeTimeSettings = new HashMap<>();
        yearDateStatusesGet.clear();

        boolean isLeapYear = isLeapYear(Integer.valueOf(currentYear));
        int februaryDay;
        if (isLeapYear) {
            februaryDay = 29;
        } else {
            februaryDay = 28;
        }

        int days[] = {31, februaryDay, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        Date date = UtilsIsSerialDate.getCurrYearFirst(Integer.valueOf(currentYear));


        for (int i = 0; i < days.length; i++) {

            com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus dateStatus
                    = new com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus();

            dateStatus.statusList = new ArrayList<>();

            for (int j = 0; j < days[i]; j++) {

                com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus.StatusList statusList
                        = new com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus.StatusList();

                //初次进入日历
                if (yearDateStatuses.size() == 0) {

                    statusList.day = String.valueOf(j + 1);
                    statusList.teeTimeSetting = StringUtils.EMPTY;
                    statusList.block = "0";
                    statusList.nineHoles = "0";
                    statusList.threeTeeStart = "0";
                    statusList.memberOnly = "0";
                    statusList.booking = "0";
                    statusList.primeTime = "0";
                    statusList.twoTeeStart = "0";


                    //获取数据后
                } else {

                    List<com.squareup.timessquare.JsonTeeTimeCalendar.DataList.DateStatus.StatusList> statusListData
                            = yearDateStatuses.get(i).getStatusList();

                    if (statusListData.size() > 0) {

                        for (int k = 0; k < statusListData.size(); k++) {


                            if (String.valueOf(j + 1).equals(statusListData.get(k).getDay())) {

                                statusList.day = statusListData.get(k).getDay();
                                statusList.teeTimeSetting = statusListData.get(k).getTeeTimeSetting();
                                statusList.block = statusListData.get(k).getBlock();
                                statusList.nineHoles = statusListData.get(k).getNineHoles();
                                statusList.threeTeeStart = statusListData.get(k).getThreeTeeStart();
                                statusList.memberOnly = statusListData.get(k).getMemberOnly();
                                statusList.booking = statusListData.get(k).getBooking();
                                statusList.primeTime = statusListData.get(k).getPrimeTime();
                                statusList.twoTeeStart = statusListData.get(k).getTwoTeeStart();

//                                teeTimeSettings.put(date, statusListData.get(k).getTeeTimeSetting());
                                Calendar calendar = new GregorianCalendar();
                                calendar.setTime(date);
                                calendar.add(Calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
                                date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                            }
                        }

                    } else if (statusListData.size() == 0) {

                        statusList.day = String.valueOf(j + 1);
                        statusList.teeTimeSetting = StringUtils.EMPTY;
                        statusList.block = "0";
                        statusList.nineHoles = "0";
                        statusList.threeTeeStart = "0";
                        statusList.memberOnly = "0";
                        statusList.booking = "0";
                        statusList.primeTime = "0";
                        statusList.twoTeeStart = "0";
//                        teeTimeSettings.put(date, StringUtils.EMPTY);
                        Calendar calendar = new GregorianCalendar();
                        calendar.setTime(date);
                        calendar.add(Calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
                        date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                    }

                }
                dateStatus.statusList.add(statusList);


            }
            yearDateStatusesGet.add(dateStatus);
        }
    }

    private String getTeeTimeSetting(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        String teeTimeSetting = yearDateStatusesGet.get(month).getStatusList().get(day - 1).getTeeTimeSetting();

        if (Utils.isStringNotNullOrEmpty(teeTimeSetting)) {
            return teeTimeSetting;

        }
        return StringUtils.EMPTY;
    }

    private String getBooking(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        String booking = yearDateStatusesGet.get(month).getStatusList().get(day - 1).getBooking();

        if (Utils.isStringNotNullOrEmpty(booking)) {
            return booking;

        }
        return StringUtils.EMPTY;
    }

    private boolean isSegmentTimeSetting(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DATE);

        String teeTimeSetting = yearDateStatusesGet.get(month).getStatusList().get(day - 1).getTeeTimeSetting();
        String twoTeeStart;
        String nineHolesOnly;
        String blockTimes;
        String memberOnly;
        String threeTimeStart;
        String primeTime;

        if (Utils.isStringNotNullOrEmpty(teeTimeSetting)) {

            twoTeeStart = yearDateStatusesGet.get(month).getStatusList().get(day - 1).getTwoTeeStart();
            nineHolesOnly = yearDateStatusesGet.get(month).getStatusList().get(day - 1).getNineHoles();
            threeTimeStart = yearDateStatusesGet.get(month).getStatusList().get(day - 1).getThreeTeeStart();
            blockTimes = yearDateStatusesGet.get(month).getStatusList().get(day - 1).getBlock();
            memberOnly = yearDateStatusesGet.get(month).getStatusList().get(day - 1).getMemberOnly();
            primeTime = yearDateStatusesGet.get(month).getStatusList().get(day - 1).getPrimeTime();
            return !(Constants.STR_0).equals(twoTeeStart)
                    || !(Constants.STR_0).equals(blockTimes)
                    || !(Constants.STR_0).equals(memberOnly)
                    || !(Constants.STR_0).equals(primeTime)
                    || !(Constants.STR_0).equals(nineHolesOnly)
                    || !(Constants.STR_0).equals(threeTimeStart);

        } else {
            return false;
        }


    }

    private List<Date> getAllDateBetweenFirstAndLast(List<Date> dateList) {

        Date firstDate = dateList.get(0);
        Date lastDate = dateList.get(dateList.size() - 1);

        List<Date> allDate = new ArrayList<>();
        allDate.add(firstDate);
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(firstDate);
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(lastDate);

        while (true) {
            startCalendar.add(Calendar.DAY_OF_MONTH, 1);
            if (startCalendar.getTimeInMillis() < endCalendar.getTimeInMillis()) {
                allDate.add(startCalendar.getTime());
            } else {
                break;
            }
        }
        allDate.add(lastDate);

        return allDate;
    }


    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        selectedYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        yearDateStatuses.clear();
        getData(selectedYear);
    }
}
