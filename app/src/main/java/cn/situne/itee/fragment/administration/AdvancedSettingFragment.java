/**
 * Project Name: itee
 * File Name:  PlayerRefundFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-28
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.administration;

import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.mining.app.zxing.decoding.Intents;
import com.squareup.timessquare.CustomCalendarPickerView;
import com.squareup.timessquare.CustomJsonAdvancedSetting;
import com.squareup.timessquare.JsonTeeTimeCalendar;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import cn.situne.itee.R;
import cn.situne.itee.adapter.MultiChoiceAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.ParamAdvancedSetting;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
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
public class AdvancedSettingFragment extends BaseFragment {
    private RelativeLayout rlContainer;
    private RelativeLayout rlBottom;
    private IteeTextView tvSpecialday;
    private IteeTextView tvComment;
    private boolean isEdit;
    private boolean isFirst = true;
    private Date nowClickDate;
    private String tvYearString;

    private JsonAdvancedSetting jsonAdvancedSetting;

    private CustomJsonAdvancedSetting jsonCalendar;


    private CustomCalendarPickerView calendarPickerView;
    private SelectYearPopupWindow selectYearPopupWindow;
    private MultiChoicePopupWindow currentPopWindow;
    private MultiChoicePopupWindow multiChoicePopupWindow;

    private ArrayList<JsonSpecialDay.SpecialDay> dataSepcialDateList;

    private HashMap<String, ParamAdvancedSetting> needUpdateList = new HashMap<>();


    private String selectedDates;

    @Override
    protected int getFragmentId() {
        return R.layout.advanced_setting;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.advanced_setting_title;
    }

    @Override
    protected void initControls(View rootView) {
        rlContainer = (RelativeLayout) getRootView().findViewById(R.id.rl_content_container);
        rlBottom = (RelativeLayout) getRootView().findViewById(R.id.rl_advanced_setting_bottom);

        calendarPickerView = new CustomCalendarPickerView(getBaseActivity(), null);
        tvComment = new IteeTextView(this);
        tvSpecialday = new IteeTextView(this);
//        reloadCalendar(null);
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
                    .inMode(CustomCalendarPickerView.SelectionMode.SINGLE);
        } else {
            //将星期一作为一个星期中的第一天
            calendarPickerView.init(startTime.getTime(), endTime.getTime(), Locale.getDefault(), jsonCalendar, 1)
                    .inMode(CustomCalendarPickerView.SelectionMode.SINGLE);
        }


        calendarPickerView.setDateBeforeSelectListener(new CustomCalendarPickerView.BeforeSelectDateListener() {
            @Override
            public boolean doBeforeSelect(Date date) {
                if (isEdit) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        calendarPickerView.setOnDateSelectedListener(
                new CustomCalendarPickerView.OnDateSelectedListener() {
                    @Override

                    public void onDateSelected(Date date) {

                        nowClickDate = date;
                        netLinkGetSpecialDate();


                    }

                    @Override
                    public void onDateUnselected(Date date) {

                    }
                }

        );

        calendarPickerView.scrollToDate(new Date());
        rlContainer.removeAllViews();
        rlContainer.addView(calendarPickerView);
        LayoutUtils.setWidthAndHeight(calendarPickerView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void setDefaultValueOfControls() {
        if (jsonAdvancedSetting != null) {

            reloadCalendar(tvYearString);
        }
    }

    @Override
    protected void setListenersOfControls() {
        tvSpecialday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                push(SpecialDayFragment.class, null);
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {
//


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.leftMargin = DensityUtil.getActualHeightOnThisDevice(20, getBaseActivity());
        tvComment.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams1.rightMargin = DensityUtil.getActualHeightOnThisDevice(20, getBaseActivity());
        tvSpecialday.setLayoutParams(layoutParams1);
        tvSpecialday.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvSpecialday.getPaint().setAntiAlias(true);
        tvSpecialday.setTextColor(getColor(R.color.common_blue));
        tvComment.setText(getString(R.string.administration_advanced_setting_comment));
        tvComment.setTextColor(getColor(R.color.common_black));
        tvComment.setSingleLine(false);


        tvSpecialday.setText(getString(R.string.administration_special_title));
        rlBottom.addView(tvComment);
        rlBottom.addView(tvSpecialday);


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
//                    List<Date> dates = calendarPickerView.getSelectedDates();
//                    StringBuilder stringBuilder = new StringBuilder();
//                    for (Date d : dates) {
//                        if (stringBuilder.length() > 0) {
//                            stringBuilder.append(Constants.STR_COMMA);
//                        }
//                        String dateStr = DateUtils.getAPIYearMonthDay(d);
//                        stringBuilder.append(dateStr);
//                    }
//                    selectedDates = stringBuilder.toString();
//                    Intent intent = new Intent();
//                    intent.putExtra(TransKey.SELECTED_DATE, selectedDates);
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();
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
//                    List<Date> dates = calendarPickerView.getSelectedDates();
//                    StringBuilder stringBuilder = new StringBuilder();
//                    for (Date d : dates) {
//                        if (stringBuilder.length() > 0) {
//                            stringBuilder.append(Constants.STR_COMMA);
//                        }
//                        String dateStr = DateUtils.getAPIYearMonthDay(d);
//                        stringBuilder.append(dateStr);
//                    }
//                    selectedDates = stringBuilder.toString();
//                    Intent intent = new Intent();
//                    intent.putExtra(TransKey.SELECTED_DATE, selectedDates);
//                    setResult(Activity.RESULT_OK, intent);
//                    finish();
                }
            });
        }
    }

    @Override
    protected void configActionBar() {
        setCalendarOnlyYearActionBar();
        getTvLeftTitle().setText(getString(R.string.advanced_setting_title));
        getTvLeftTitle().setId(View.generateViewId());
        setTitle(String.valueOf(Utils.getCurrentYear()));
        tvYearString = String.valueOf(Utils.getCurrentYear());
        if (isEdit) {
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_ok);

        } else {
            getTvRight().setBackgroundResource(R.drawable.tee_time_calendar_icon_edit);
            getTvRight().setText(StringUtils.EMPTY);
        }

        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEdit = !isEdit;
                if (isEdit) {
                    getTvRight().setBackground(null);
                    getTvRight().setText(R.string.common_ok);
                    isFirst = false;

                } else {
                    getTvRight().setBackgroundResource(R.drawable.tee_time_calendar_icon_edit);
                    getTvRight().setText(StringUtils.EMPTY);

                    if (!isFirst) {
                        netLinkEditAccount();
                    }

                }
                if (calendarPickerView != null) {
                    calendarPickerView.clearOldSelections();
                    calendarPickerView.clearHighlightedDates();
                }
            }
        });

        getTvTitle().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // select year
                selectYearPopupWindow = new SelectYearPopupWindow(getBaseActivity(), null,0);
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
//                        reloadCalendar(selectedYear);
                        netLinkGetAccount(selectedYear);
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
        HttpManager<JsonAdvancedSetting> hh = new HttpManager<JsonAdvancedSetting>(AdvancedSettingFragment.this) {
            @Override
            public void onJsonSuccess(JsonAdvancedSetting jo) {
                jsonAdvancedSetting = jo;
                setDefaultValueOfControls();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.startGet(AdvancedSettingFragment.this.getBaseActivity().getApplication(), ApiManager.HttpApi.XDEVELOPX0236, params);
    }


    private void netLinkEditAccount() {


        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put("data_list", getPriceList());
        Utils.log(getPriceList());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(AdvancedSettingFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    isFirst = !isFirst;
                    doBack();
                } else {
                    Utils.showShortToast(mContext, msg);
                    isFirst = true;
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                isFirst = true;
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.XDEVELOPX0236, params);
    }

    private String getPriceList() {


        JSONArray array = new JSONArray();

        for (Map.Entry<String, ParamAdvancedSetting> entry : needUpdateList.entrySet()) {
            Map<String, String> priceItem = new HashMap<>();

            ParamAdvancedSetting param = entry.getValue();
            priceItem.put(JsonKey.ADMINISTRATION_ADVANCED_SETTING_CD_DATE, param.getCdDate());

            if (param.getCdType().endsWith(",")) {
                priceItem.put(JsonKey.ADMINISTRATION_ADVANCED_SETTING_CD_TYPE, param.getCdType().substring(0, param.getCdType().length() - 1));
            }
            if (param.getCdTypeFlag().endsWith(",")) {
                priceItem.put(JsonKey.ADMINISTRATION_ADVANCED_SETTING_CD_TYPE_FLAG, param.getCdTypeFlag().substring(0, param.getCdTypeFlag().length() - 1));
            }

            array.put(new JSONObject(priceItem));
        }
        return array.toString();
    }

    private void netLinkGetSpecialDate() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        HttpManager<JsonSpecialDay> hh = new HttpManager<JsonSpecialDay>(AdvancedSettingFragment.this) {
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

                    List<String> choiceIndex = new ArrayList<>();
                    Calendar canlender = Calendar.getInstance();
                    canlender.setTime(nowClickDate);
                    int day_of_month = canlender.get(Calendar.DAY_OF_MONTH);
                    if(jsonCalendar.getDataList() != null && jsonCalendar.getDataList().size() > canlender.get(Calendar.MONTH)){
                        HashMap<String, CustomJsonAdvancedSetting.DaySetting> dataList = jsonCalendar.getDataList().get(canlender.get(Calendar.MONTH));
                        if (dataList != null) {
                            String dayString = null;
                            if (day_of_month < 10) {
                                dayString = "0" + day_of_month;
                            } else {
                                dayString = String.valueOf(day_of_month);
                            }
                            CustomJsonAdvancedSetting.DaySetting data = dataList.get(dayString);
                            if (AppUtils.getYMD(data.getCdDate()).equals(nowClickDate)) {
                                if (data.getCdTypeFlagArr() != null) {
                                    for (int i = 0; i < data.getCdTypeFlagArr().size(); i++) {
                                        CustomJsonAdvancedSetting.CdTypeArrItem cdTypeArrItem = data.getCdTypeFlagArr().get(i);
                                        if ("1".equals(cdTypeArrItem.getId())) {
                                            choiceIndex.add("0");
                                        }
                                        if ("2".equals(cdTypeArrItem.getId())) {
                                            choiceIndex.add("1");
                                        }
                                    }

                                }
                                if (data.getCdTypeArr() != null) {
                                    for (int i = 0; i < data.getCdTypeArr().size(); i++) {
                                        CustomJsonAdvancedSetting.CdTypeArrItem cdTypeArrItem = data.getCdTypeArr().get(i);
                                        for (int j = 0; j < courseAreaTypeList.size(); j++) {
                                            HashMap<String, Object> mapss = courseAreaTypeList.get(j);
                                            String sourceID = (String) mapss.get(TransKey.EVENTS_COURSE_AREA_ID);
                                            if (sourceID.equals(cdTypeArrItem.getId())) {
                                                choiceIndex.add(String.valueOf(j));
                                            }

                                        }
                                    }

                                }

                            }
                        }
                    }

                    for (int i = 0; i < choiceIndex.size(); i++) {
                        String index = choiceIndex.get(i);
                        multiChoicePopupWindow.lvPopupWindow.setItemChecked(Integer.valueOf(index), true);
                    }

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

                                    multiChoicePopupWindow.dismiss();
                                    Calendar canlender = Calendar.getInstance();
                                    canlender.setTime(nowClickDate);
                                    int year = canlender.get(Calendar.YEAR);
                                    int month = (canlender.get(Calendar.MONTH)) + 1;
                                    int day_of_month = canlender.get(Calendar.DAY_OF_MONTH);

                                    if (jsonCalendar.getDataList()!=null&&jsonCalendar.getDataList().size()>0){
                                        HashMap<String, CustomJsonAdvancedSetting.DaySetting> dataList = jsonCalendar.getDataList().get(canlender.get(Calendar.MONTH));
                                        if (dataList != null) {
                                            String dayString = null;
                                            if (day_of_month < 10) {
                                                dayString = "0" + day_of_month;
                                            } else {
                                                dayString = String.valueOf(day_of_month);
                                            }

                                            if (!dataList.containsKey(dayString)) {
                                                CustomJsonAdvancedSetting.DaySetting daySetting = new CustomJsonAdvancedSetting.DaySetting();
                                                daySetting.setCdTypeArr(new ArrayList<CustomJsonAdvancedSetting.CdTypeArrItem>());
                                                daySetting.setCdTypeFlagArr(new ArrayList<CustomJsonAdvancedSetting.CdTypeArrItem>());
                                                dataList.put(dayString, daySetting);
                                            }
                                            CustomJsonAdvancedSetting.DaySetting data = dataList.get(dayString);
                                            if (AppUtils.getYMD(data.getCdDate()).equals(nowClickDate) && selectHasmap.size() > 0) {
                                                List<CustomJsonAdvancedSetting.CdTypeArrItem> cdTypeArr = new ArrayList<CustomJsonAdvancedSetting.CdTypeArrItem>();
                                                List<CustomJsonAdvancedSetting.CdTypeArrItem> cdTypeFlagArr = new ArrayList<CustomJsonAdvancedSetting.CdTypeArrItem>();
                                                StringBuilder stringBuilder = new StringBuilder();
                                                StringBuilder stringBuilderFlag = new StringBuilder();
                                                for (Map.Entry<String, HashMap<String, Object>> entry : selectHasmap.entrySet()) {

                                                    HashMap<String, Object> map = entry.getValue();
                                                    String id = (String) map.get(TransKey.EVENTS_COURSE_AREA_ID);
                                                    String courseName = (String) map.get(TransKey.EVENTS_COURSE_AREA_NAME);
                                                    String courseNameFirst = (String) map.get(TransKey.EVENTS_COURSE_AREA_NAME_FIRST);
                                                    if ("H".equals(id) || "W".equals(id)) {
                                                        CustomJsonAdvancedSetting.CdTypeArrItem cdTypeArrItem = new CustomJsonAdvancedSetting.CdTypeArrItem();
                                                        if ("H".equals(id)) {

                                                            cdTypeArrItem.setId("1");
                                                        } else {
                                                            cdTypeArrItem.setId("2");
                                                        }
                                                        cdTypeArrItem.setName(courseName);
                                                        cdTypeArrItem.setFirstName(courseNameFirst);
                                                        cdTypeFlagArr.add(cdTypeArrItem);
                                                        stringBuilderFlag.append(cdTypeArrItem.getId() + ",");
                                                    } else {
                                                        CustomJsonAdvancedSetting.CdTypeArrItem cdTypeArrItem = new CustomJsonAdvancedSetting.CdTypeArrItem();
                                                        cdTypeArrItem.setId(id);
                                                        cdTypeArrItem.setName(courseName);
                                                        cdTypeArrItem.setFirstName(courseNameFirst);
                                                        cdTypeArr.add(cdTypeArrItem);
                                                        stringBuilder.append(id + ",");
                                                    }

                                                }
                                                data.setCdTypeArr(cdTypeArr);
                                                data.setCdTypeFlagArr(cdTypeFlagArr);

                                                ParamAdvancedSetting paramAdvancedSetting
                                                        = new ParamAdvancedSetting();
                                                paramAdvancedSetting.setCdDate(data.getCdDate());
                                                paramAdvancedSetting.setCdType(stringBuilder.toString());
                                                paramAdvancedSetting.setCdTypeFlag(stringBuilderFlag.toString());
                                                needUpdateList.put(data.getCdDate(), paramAdvancedSetting);

                                            }

                                        }

                                    }else{

                                        Utils.showShortToast(getBaseActivity(),getString(R.string.advanced_setting_check));

                                    }


                                    calendarPickerView.validateAndUpdate();
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

                    multiChoicePopupWindow.showAtLocation(AdvancedSettingFragment.this.

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
