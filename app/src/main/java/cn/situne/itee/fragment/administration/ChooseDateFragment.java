/**
 * Project Name: itee
 * File Name:  PlayerRefundFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-28
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.administration;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.squareup.timessquare.ChooseCalendarPickerView;
import com.squareup.timessquare.CustomJsonAdvancedSetting;

import org.apache.commons.lang3.StringUtils;

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
import cn.situne.itee.adapter.MultiChoiceAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.SegmentATimeSettingFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonAdvancedSetting;
import cn.situne.itee.manager.jsonentity.JsonSpecialDay;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.MultiChoicePopupWindow;
import cn.situne.itee.view.popwindow.SelectYearPopupWindow;

/**
 * ClassName:PlayerRefundFragment <br/>
 * Function: member's refund  fragment. <br/>
 * UI:  04-8-6
 * Date: 2015-03-28 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class ChooseDateFragment extends BaseFragment {
    private RelativeLayout rlContainer;
    private String tvYearString;
    private Date nowClickDate;

    private JsonAdvancedSetting jsonAdvancedSetting;

    private CustomJsonAdvancedSetting jsonCalendar;


    private ChooseCalendarPickerView calendarPickerView;
    private SelectYearPopupWindow selectYearPopupWindow;
    private MultiChoicePopupWindow currentPopWindow;
    private MultiChoicePopupWindow multiChoicePopupWindow;

    private ArrayList<JsonSpecialDay.SpecialDay> dataSepcialDateList;

    private HashMap<String, Object> needUpdateList = new HashMap<>();


    private String selectedDates;

    private String selectedDatesFrom;

    private String fromPage;

    private boolean isTeeTimeSetting;

    @Override
    protected int getFragmentId() {
        return R.layout.choose_date;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.choose_date_title;
    }

    @Override
    protected void initControls(View rootView) {
        Bundle bundle = getArguments();

        if (bundle != null) {
            selectedDatesFrom = bundle.getString(TransKey.SELECTED_DATE);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);

            isTeeTimeSetting = bundle.getBoolean("isTeeTimeSetting",false);
            Utils.log("ChooseDateFragment = "+selectedDatesFrom);
        }

        if (Utils.isStringNotNullOrEmpty(selectedDatesFrom)){
            tvYearString = selectedDatesFrom.substring(0,4);

        }

        rlContainer = (RelativeLayout) getRootView().findViewById(R.id.rl_content_container);

        calendarPickerView = new ChooseCalendarPickerView(getBaseActivity(), null);
//        reloadCalendar(null);
    }

    @Override
    protected void reShowWithBackValue() {

        Bundle bundle = getReturnValues();
        String isOk = bundle.getString("isOk",Constants.STR_EMPTY);
        if (Utils.isStringNotNullOrEmpty(isOk)){
            try {
                doBackWithReturnValue(bundle,(Class<? extends BaseFragment>) Class.forName(fromPage));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void reloadCalendar(String selectedYear) {
        Calendar today = Calendar.getInstance();
        final Calendar startTime = Calendar.getInstance();
        startTime.set(today.get(Calendar.YEAR), 0, 1);
        final Calendar endTime = Calendar.getInstance();
        endTime.set(today.get(Calendar.YEAR) + 1, 0, 1);
        jsonCalendar = new CustomJsonAdvancedSetting();
        if (jsonAdvancedSetting != null) {
            List<HashMap<String, CustomJsonAdvancedSetting.DaySetting>> arrayList = new ArrayList<>();
            List<HashMap<String, JsonAdvancedSetting.DaySetting>> hostJson = jsonAdvancedSetting.getDataList();
            for (int j = 0; j < hostJson.size(); j++) {
                HashMap<String, CustomJsonAdvancedSetting.DaySetting> toMap = new HashMap<>();
                HashMap<String, JsonAdvancedSetting.DaySetting> hostHashMap = hostJson.get(j);
                for (Map.Entry<String, JsonAdvancedSetting.DaySetting> entry : hostHashMap.entrySet()) {
                    CustomJsonAdvancedSetting.DaySetting advancedSetting =
                            new CustomJsonAdvancedSetting.DaySetting();
                    JsonAdvancedSetting.DaySetting hostAdvancedSetting = entry.getValue();
                    advancedSetting.setCdDate(hostAdvancedSetting.getCdDate());
                    advancedSetting.setCdTypeFlag(hostAdvancedSetting.getCdTypeFlag());
                    advancedSetting.setCdType(hostAdvancedSetting.getCdType());
                    advancedSetting.setCdId(hostAdvancedSetting.getCdId());
                    List<CustomJsonAdvancedSetting.CdTypeArrItem> toListTypeArr = new ArrayList<>();
                    List<CustomJsonAdvancedSetting.CdTypeArrItem> toListTypeFlagArr = new ArrayList<>();
                    if (hostAdvancedSetting.getCdTypeArr() != null) {
                        for (int k = 0; k < hostAdvancedSetting.getCdTypeArr().size(); k++) {
                            CustomJsonAdvancedSetting.CdTypeArrItem toCdTypeArrItem = new CustomJsonAdvancedSetting.CdTypeArrItem();
                            JsonAdvancedSetting.CdTypeArrItem hostCd = hostAdvancedSetting.getCdTypeArr().get(k);
                            toCdTypeArrItem.setFirstName(hostCd.getFirstName());
                            toCdTypeArrItem.setId(hostCd.getId());
                            toCdTypeArrItem.setName(hostCd.getName());
                            toListTypeArr.add(toCdTypeArrItem);
                        }
                    }
                    if (hostAdvancedSetting.getCdTypeFlagArr() != null) {
                        for (int k = 0; k < hostAdvancedSetting.getCdTypeFlagArr().size(); k++) {
                            CustomJsonAdvancedSetting.CdTypeArrItem toCdTypeArrItem = new CustomJsonAdvancedSetting.CdTypeArrItem();
                            JsonAdvancedSetting.CdTypeArrItem hostCd = hostAdvancedSetting.getCdTypeFlagArr().get(k);
                            toCdTypeArrItem.setFirstName(hostCd.getFirstName());
                            toCdTypeArrItem.setId(hostCd.getId());
                            toCdTypeArrItem.setName(hostCd.getName());
                            toListTypeFlagArr.add(toCdTypeArrItem);
                        }
                    }
                    advancedSetting.setCdTypeArr(toListTypeArr);
                    advancedSetting.setCdTypeFlagArr(toListTypeFlagArr);
                    toMap.put(entry.getKey(), advancedSetting);
                }
                arrayList.add(toMap);
            }

            jsonCalendar.setDataList(arrayList);
        }


        if (Utils.isStringNotNullOrEmpty(selectedYear)) {
            startTime.set(Integer.valueOf(selectedYear).intValue(), 0, 1);
            endTime.set(Integer.valueOf(selectedYear).intValue() + 1, 0, 1);
        }
        if (Constants.FIRST_DAY_SUN.equals(AppUtils.getCurrentFirstDayOfWeek(getBaseActivity()))) {
            //将星期日作为一个星期中的第一天
            calendarPickerView.init(startTime.getTime(), endTime.getTime(), Locale.getDefault(), jsonCalendar, 0)
                    .inMode(ChooseCalendarPickerView.SelectionMode.MULTIPLE);
        } else {
            //将星期一作为一个星期中的第一天
            calendarPickerView.init(startTime.getTime(), endTime.getTime(), Locale.getDefault(), jsonCalendar, 1)
                    .inMode(ChooseCalendarPickerView.SelectionMode.MULTIPLE);
        }

        calendarPickerView.setQuickSelectedClickListener(new ChooseCalendarPickerView.QuickSelectedClickListener() {
            @Override
            public void onQuickSelect(Date date) {
                nowClickDate = date;
                netLinkGetSpecialDate();

            }
        });


        calendarPickerView.scrollToDate(new Date());
        rlContainer.removeAllViews();
        rlContainer.addView(calendarPickerView);
        LayoutUtils.setWidthAndHeight(calendarPickerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if (StringUtils.isNotEmpty(selectedDatesFrom)) {
            if (selectedDatesFrom.contains(Constants.STR_COMMA)) {
                String[] dates = selectedDatesFrom.split(Constants.STR_COMMA);
                for (String dateStr : dates) {
                    Date d = DateUtils.getDateFromAPIYearMonthDay(dateStr);
                    calendarPickerView.selectDate(d);
                }
            } else {
                Date d = DateUtils.getDateFromAPIYearMonthDay(selectedDatesFrom);
                calendarPickerView.selectDate(d);
            }

            selectedDatesFrom = "";
        }
    }

    @Override
    protected void setDefaultValueOfControls() {
        if (jsonAdvancedSetting != null) {

            reloadCalendar(tvYearString);
        }
    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {


        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        tvYear.setLayoutParams(layoutParams2);
//        tvYear.setSingleLine();
//        tvYear.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
//        tvYear.getPaint().setAntiAlias(true);
//        tvYear.setTextColor(getColor(R.color.common_black));
//        tvYear.setText(String.valueOf(Utils.getCurrentYear()));
//        tvYear.setTextSize(Constants.FONT_SIZE_LARGER);
//        rlTop.addView(tvYear);

    }

    @Override
    protected void setPropertyOfControls() {
        if (getTvLeftTitle() != null) {
            getTvLeftTitle().setText(R.string.title_select_date);

        }
        if (getTvRight() != null) {
            getTvRight().setText(R.string.common_ok);
            getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(getBaseActivity()) {
                @Override
                public void noDoubleClick(View v) {
                }
            });
        }
        if (getTvLeftTitle() != null) {
            getTvLeftTitle().setText(R.string.title_select_date);

        }
        if (getTvRight() != null) {
            getTvRight().setText(R.string.common_ok);
            getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(getBaseActivity()) {
                @Override
                public void noDoubleClick(View v) {
                }
            });
        }
    }



    @Override
    protected void configActionBar() {

        setCalendarOnlyYearActionBar();

if (Utils.isStringNotNullOrEmpty(tvYearString)){
    setTitle(tvYearString);


}else{
    setTitle(String.valueOf(Utils.getCurrentYear()));
    tvYearString = String.valueOf(Utils.getCurrentYear());

}






        getTvLeftTitle().setText(getString(R.string.choose_date_title));
        getTvLeftTitle().setId(View.generateViewId());

        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_ok);

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getTvRight().setBackground(null);
                getTvRight().setText(R.string.common_ok);
                List<Date> dates = calendarPickerView.getSelectedDates();
                StringBuilder stringBuilder = new StringBuilder();
                for (Date d : dates) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(Constants.STR_COMMA);
                    }
                    String dateStr = DateUtils.getAPIYearMonthDay(d);
                    stringBuilder.append(dateStr);
                }
                selectedDates = stringBuilder.toString();

                if (calendarPickerView != null) {
                    calendarPickerView.clearOldSelections();
                    calendarPickerView.clearHighlightedDates();
                }
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.SELECTED_DATE, selectedDates);

                bundle.putString(TransKey.COMMON_FROM_PAGE, ChooseDateFragment.class.getName());
                if (isTeeTimeSetting) {

                    //  if (Utils.isStringNotNullOrEmpty(selectedDates)){
                    push(SegmentATimeSettingFragment.class, bundle);
//                    }else{
//                        Utils.showLongToast(getBaseActivity(),getString(R.string.choose_date_check));
//                    }


                } else {

                    Utils.log("doback select Datas = " + selectedDates);
                    try {
                        doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        getTvTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // select year
                int year = 0;
                try {
                    year = Integer.parseInt(tvYearString);
                }catch (NumberFormatException e){

                }

                selectYearPopupWindow = new SelectYearPopupWindow(getBaseActivity(), null,year);
//                selectYearPopupWindow.setYear(Utils.getCurrentYear(), 5000, Integer.valueOf(tvYear.getText().toString()));
                selectYearPopupWindow.showAtLocation(getRootView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                selectYearPopupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = selectYearPopupWindow.wheelViewYear.getCurrentItem();
                        String selectedYear = selectYearPopupWindow.wheelViewYear.getAdapter().getItem(index);
                        tvYearString = selectedYear;
                        setTitle(selectedYear);
                        selectYearPopupWindow.dismiss();
                        reloadCalendar(selectedYear);

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

    @Override
    protected void executeOnceOnCreate() {
        netLinkGetAccount(String.valueOf(Utils.getCurrentYear()));
    }


    private void netLinkGetAccount(String currentYear) {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put("year", currentYear);
        HttpManager<JsonAdvancedSetting> hh = new HttpManager<JsonAdvancedSetting>(ChooseDateFragment.this) {
            @Override
            public void onJsonSuccess(JsonAdvancedSetting jo) {
                jsonAdvancedSetting = jo;
                setDefaultValueOfControls();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(ChooseDateFragment.this.getBaseActivity().getApplication(), ApiManager.HttpApi.XDEVELOPX0236, params);
    }


    private void netLinkGetSpecialDate() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        HttpManager<JsonSpecialDay> hh = new HttpManager<JsonSpecialDay>(ChooseDateFragment.this) {
            @Override
            public void onJsonSuccess(JsonSpecialDay jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataSepcialDateList = jo.getDataList();


                    final ArrayList<HashMap<String, Object>> courseAreaTypeList = new ArrayList<>();
                    final HashMap<String, HashMap<String, Object>> selectHasmap = new HashMap<>();
                    HashMap<String, Object> mapHoliday = new HashMap<>();
                    mapHoliday.put(TransKey.EVENTS_COURSE_AREA_ID, "H");
                    mapHoliday.put(TransKey.EVENTS_COURSE_AREA_NAME, getString(R.string.pricing_table_holiday));
                    mapHoliday.put(TransKey.EVENTS_COURSE_AREA_NAME_FIRST, "H");
                    HashMap<String, Object> mapWeekday = new HashMap<>();
                    mapWeekday.put(TransKey.EVENTS_COURSE_AREA_ID, "W");
                    mapWeekday.put(TransKey.EVENTS_COURSE_AREA_NAME, getString(R.string.pricing_table_weekday));
                    mapWeekday.put(TransKey.EVENTS_COURSE_AREA_NAME_FIRST, "W");
                    courseAreaTypeList.add(mapHoliday);
                    courseAreaTypeList.add(mapWeekday);
                    for (int i = 0; i < dataSepcialDateList.size(); i++) {
                        String courseId = dataSepcialDateList.get(i).getCdtId();
                        String courseName = dataSepcialDateList.get(i).getCdtName();
                        String courseNameFirst = dataSepcialDateList.get(i).getCdtFirstName();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put(TransKey.EVENTS_COURSE_AREA_ID, courseId);
                        map.put(TransKey.EVENTS_COURSE_AREA_NAME, courseName);
                        map.put(TransKey.EVENTS_COURSE_AREA_NAME_FIRST, courseNameFirst);
                        courseAreaTypeList.add(map);
                    }


                    MultiChoiceAdapter multiChoiceAdapter = new MultiChoiceAdapter(getActivity(), courseAreaTypeList);

                    multiChoicePopupWindow = new MultiChoicePopupWindow(getActivity(), null, courseAreaTypeList
                            .size());

                    multiChoicePopupWindow.lvPopupWindow.setAdapter(multiChoiceAdapter);
                    multiChoicePopupWindow.lvPopupWindow.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                    //set choice

//                    List<String> choiceIndex = new ArrayList<>();
//                    Calendar canlender = Calendar.getInstance();
//                    canlender.setTime(nowClickDate);
//                    int day_of_month = canlender.get(Calendar.DAY_OF_MONTH);
//                    HashMap<String, CustomJsonAdvancedSetting.DaySetting> dataList = jsonCalendar.getDataList().get(canlender.get(Calendar.MONTH));
//                    if (dataList != null) {
//                        String dayString = null;
//                        if (day_of_month < 10) {
//                            dayString = "0" + day_of_month;
//                        } else {
//                            dayString = String.valueOf(day_of_month);
//                        }
//                        CustomJsonAdvancedSetting.DaySetting data = dataList.get(dayString);
//                        if (AppUtils.getYMD(data.getCdDate()).equals(nowClickDate)) {
//                            if (data.getCdTypeFlagArr() != null) {
//                                for (int i = 0; i < data.getCdTypeFlagArr().size(); i++) {
//                                    CustomJsonAdvancedSetting.CdTypeArrItem cdTypeArrItem = data.getCdTypeFlagArr().get(i);
//                                    if ("1".equals(cdTypeArrItem.getId())) {
//                                        choiceIndex.add("0");
//                                    }
//                                    if ("2".equals(cdTypeArrItem.getId())) {
//                                        choiceIndex.add("1");
//                                    }
//                                }
//
//                            }
//                            if (data.getCdTypeArr() != null) {
//                                for (int i = 0; i < data.getCdTypeArr().size(); i++) {
//                                    CustomJsonAdvancedSetting.CdTypeArrItem cdTypeArrItem = data.getCdTypeArr().get(i);
//                                    for (int j = 0; j < courseAreaTypeList.size(); j++) {
//                                        HashMap<String, Object> mapss = courseAreaTypeList.get(j);
//                                        String sourceID = (String) mapss.get(TransKey.EVENTS_COURSE_AREA_ID);
//                                        if (sourceID.equals(cdTypeArrItem.getId())) {
//                                            choiceIndex.add(String.valueOf(j));
//                                        }
//
//                                    }
//                                }
//
//                            }
//
//                        }
//                    }
//                    for (int i = 0; i < choiceIndex.size(); i++) {
//                        String index = choiceIndex.get(i);
//                        multiChoicePopupWindow.lvPopupWindow.setItemChecked(Integer.valueOf(index), true);
//                    }

                    multiChoicePopupWindow.btn_ok.setOnClickListener(
                            new View.OnClickListener()

                            {
                                @Override
                                public void onClick(View view) {
                                    for (int i = 0; i < courseAreaTypeList.size(); i++) {
                                        if (multiChoicePopupWindow.lvPopupWindow.isItemChecked(i)) {
                                            if (selectHasmap.containsKey(i)) {
                                                selectHasmap.remove(i);
                                            } else {
                                                HashMap<String, Object> map = courseAreaTypeList.get(i);
                                                selectHasmap.put(String.valueOf(i), map);
                                            }
                                        }
                                    }

//                                    if (calendarPickerView != null) {
//                                        calendarPickerView.clearOldSelections();
//                                        calendarPickerView.clearHighlightedDates();
//                                    }

                                    Calendar canlender = Calendar.getInstance();
                                    canlender.setTime(nowClickDate);
                                    int year = canlender.get(Calendar.YEAR);
                                    int month = (canlender.get(Calendar.MONTH)) ;
                                    int day_of_month = canlender.get(Calendar.DAY_OF_MONTH);
                                    calendarPickerView.clearSelectionsWithMonth(month);

                                    if (jsonCalendar.getDataList()!=null&&jsonCalendar.getDataList().size()>0){
                                        HashMap<String, CustomJsonAdvancedSetting.DaySetting> dataList = jsonCalendar.getDataList().get(canlender.get(Calendar.MONTH));
                                        if (dataList != null) {

                                            for (int i = 1; i < 32; i++) {
                                                String dayString = null;
                                                if (i < 10) {
                                                    dayString = "0" + i;
                                                } else {
                                                    dayString = String.valueOf(i);
                                                }
                                                if (dataList.containsKey(dayString)) {
                                                    CustomJsonAdvancedSetting.DaySetting data = dataList.get(dayString);


                                                    if (selectHasmap.size() > 0) {

                                                        for (Map.Entry<String, HashMap<String, Object>> entry : selectHasmap.entrySet()) {

                                                            HashMap<String, Object> map = entry.getValue();
                                                            String id = (String) map.get(TransKey.EVENTS_COURSE_AREA_ID);


                                                            for (int k = 0; k < data.getCdTypeFlagArr().size(); k++) {
                                                                CustomJsonAdvancedSetting.CdTypeArrItem dataItem = data.getCdTypeFlagArr().get(k);
                                                                if ("H".equals(id) && dataItem.getId().equals("1")) {
                                                                    needUpdateList.put(data.getCdDate(), "1");
                                                                }

                                                                if ("W".equals(id) && dataItem.getId().equals("2")) {
                                                                    needUpdateList.put(data.getCdDate(), "1");
                                                                }
                                                            }

                                                            for (int k = 0; k < data.getCdTypeArr().size(); k++) {
                                                                CustomJsonAdvancedSetting.CdTypeArrItem dataItem = data.getCdTypeArr().get(k);
                                                                if (dataItem.getId().equals(id)) {
                                                                    needUpdateList.put(data.getCdDate(), "1");
                                                                }
                                                            }
                                                        }
                                                    }

                                                }
                                            }


                                        }

                                    }else{

                                        Utils.showShortToast(getBaseActivity(),getString(R.string.advanced_setting_check));

                                    }


                                    //set choose
                                    for (String key : needUpdateList.keySet()) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                        try {
                                            Date date = sdf.parse(key);
                                            calendarPickerView.selectDate(date);
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    selectHasmap.clear();
                                    needUpdateList.clear();
                                    multiChoicePopupWindow.dismiss();
                                }
                            }

                    );

                    multiChoicePopupWindow.btn_cancel.setOnClickListener(
                            new View.OnClickListener()

                            {
                                @Override
                                public void onClick(View view) {

                                    multiChoicePopupWindow.dismiss();
                                }
                            }

                    );

                    multiChoicePopupWindow.showAtLocation(ChooseDateFragment.this.

                            getRootView(), Gravity

                            .BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    if (currentPopWindow != null)

                    {
                        currentPopWindow.dismiss();
                    }

                    currentPopWindow = multiChoicePopupWindow;
                } else

                {
                    Utils.showShortToast(mContext, msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(

                getActivity(), ApiManager

                        .HttpApi.XDEVELOPX02361, params);
    }

}
