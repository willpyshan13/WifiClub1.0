/**
 * Project Name: itee
 * File Name:  TeeTimeSettingEditOrAddFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:   2015-03-12
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

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
import cn.situne.itee.adapter.TeeTimeSettingTransferCourseAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonAddDoTeeTimeSetting;
import cn.situne.itee.manager.jsonentity.JsonDateSun;
import cn.situne.itee.manager.jsonentity.JsonEditDoTeeTimeSetting;
import cn.situne.itee.manager.jsonentity.JsonTeeTimeSetting;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.DeleteConfirmPopupWindow;
import cn.situne.itee.view.popwindow.SelectTimePopupWindow;

/**
 * ClassName:TeeTimeSettingEditOrAddFragment <br/>
 * Function: edit or add tee time setting. <br/>
 * Date: 2015-03-12 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class TeeTimeSettingEditOrAddFragment extends BaseEditFragment {

    private RelativeLayout rlStartDateContainer;
    private RelativeLayout rlSunriseContainer;
    private RelativeLayout rlSunsetContainer;
    private RelativeLayout rlFirstTeeTimeContainer;
    private RelativeLayout rlFinishTeeTimeContainer;
    private RelativeLayout rlGapTimeContainer;
    private RelativeLayout rlTransferTimeContainer;

    private IteeTextView tvStartDateName;
    private IteeTextView tvStartDateValue;

    private IteeTextView tvSunriseName;
    private IteeTextView tvSunriseValue;

    private IteeTextView tvSunsetName;
    private IteeTextView tvSunsetValue;

    private IteeTextView tvFirstTeeTime;
    private IteeTextView tvFirstTeeTimeValue;

    private IteeTextView tvFinishTeeTime;
    private IteeTextView tvFinishTeeTimeValue;

    private IteeTextView tvGapTime;
    private EditText etGapTimeValue;
    private IteeTextView tvGapTimeUnit;

    private IteeTextView tvTransferTime;

    private SelectTimePopupWindow selectTimePopupWindow;

    private String sunriseTime;
    private String sunsetTime;

    private String courseId;
    private String startDate;
    private String endDate;
    private Date theStartDate;

    private ArrayList<Integer> transferTimeIdList;
    private ArrayList<String> transferTimeNameList;
    private ArrayList<String> transferTimeTimeList;

    private ListView transferCourseListView;
    private ArrayList<HashMap<String, Object>> transferCourseData = new ArrayList<>();

    private String editOrAdd;

    private DeleteConfirmPopupWindow deleteConfirmPopupWindow;

    private StringBuffer sbTransferChanged;

    private AppUtils.NoDoubleClickListener noDoubleClickListener;

    private String minTransferTime;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_tee_time_setting_edit_or_add;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        courseId = bundle.getString("course_id");
        startDate = bundle.getString("choose_date");
        theStartDate = (Date) bundle.getSerializable("start_date");
        endDate = DateUtils.getAPIYearMonthDay((Date) bundle.getSerializable("end_date"));
        editOrAdd = bundle.getString("edit_or_add");
    }

    @Override
    protected void initControls(View rootView) {

        rlStartDateContainer = (RelativeLayout) rootView.findViewById(R.id.rl_start_date);

        rlSunriseContainer = (RelativeLayout) rootView.findViewById(R.id.rl_sunrise);
        rlSunsetContainer = (RelativeLayout) rootView.findViewById(R.id.rl_sunset);
        rlFirstTeeTimeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_first_tee_time);
        rlFinishTeeTimeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_finish_tee_time);
        rlGapTimeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_gap_time);
        rlTransferTimeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_transfer_time);
        transferCourseListView = (ListView) rootView.findViewById(R.id.transfer_course_list_view);

        tvStartDateName = new IteeTextView(this);
        tvStartDateValue = new IteeTextView(this);


        tvSunriseName = new IteeTextView(this);
        tvSunriseValue = new IteeTextView(this);

        tvSunsetName = new IteeTextView(this);
        tvSunsetValue = new IteeTextView(this);

        tvFirstTeeTime = new IteeTextView(this);
        tvFirstTeeTimeValue = new IteeTextView(this);

        tvFinishTeeTime = new IteeTextView(this);
        tvFinishTeeTimeValue = new IteeTextView(this);

        tvGapTime = new IteeTextView(this);
        etGapTimeValue = new EditText(getActivity());
        tvGapTimeUnit = new IteeTextView(this);

        tvTransferTime = new IteeTextView(this);

        transferTimeIdList = new ArrayList<>();
        transferTimeNameList = new ArrayList<>();
        transferTimeTimeList = new ArrayList<>();

    }

    @Override
    protected void setDefaultValueOfControls() {
        tvStartDateName.setText(R.string.pricing_table_choose_date);
        tvStartDateName.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvStartDateName.setTextColor(getColor(R.color.common_black));


        tvSunriseName.setText(R.string.tee_times_sunrise);
        tvSunriseName.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvSunriseName.setTextColor(getColor(R.color.common_black));

        tvSunsetName.setText(R.string.tee_times_sunset);
        tvSunsetName.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvSunsetName.setTextColor(getColor(R.color.common_black));

        tvFirstTeeTime.setText(R.string.tee_times_first_tee_time);
        tvFirstTeeTime.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvFirstTeeTime.setTextColor(getColor(R.color.common_black));

        tvFinishTeeTime.setText(R.string.tee_times_last_tee_time);
        tvFinishTeeTime.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvFinishTeeTime.setTextColor(getColor(R.color.common_black));

        tvGapTime.setText(R.string.tee_times_gap_time);
        tvGapTime.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvGapTime.setTextColor(getColor(R.color.common_black));
        tvGapTimeUnit.setText(Constants.TIME_MIN_UNIT);
        tvGapTimeUnit.setGravity(Gravity.CENTER | Gravity.END);

        etGapTimeValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        etGapTimeValue.setSingleLine();
        etGapTimeValue.setBackground(null);
        etGapTimeValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etGapTimeValue.setTextColor(getColor(R.color.common_black));

        etGapTimeValue.setKeyListener(new NumberKeyListener() {
            @Override
            protected char[] getAcceptedChars() {
                return new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
            }

            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_NUMBER;
            }
        });
        tvTransferTime.setText(R.string.tee_times_transfer_time);
        tvTransferTime.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvTransferTime.setTextColor(getColor(R.color.common_blue));

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COURSE_ID, AppUtils.getCurrentCourseId(getActivity()));

        params.put(ApiKey.TEE_TIME_SETTING_START_DATE, DateUtils.getAPIYearMonthDay(theStartDate));
        params.put(ApiKey.TEE_TIME_SETTING_END_DATE, endDate);

        HttpManager<JsonTeeTimeSetting> hh = new HttpManager<JsonTeeTimeSetting>(TeeTimeSettingEditOrAddFragment.this) {

            @Override
            public void onJsonSuccess(JsonTeeTimeSetting jo) {
                Utils.debug(jo.toString());
                initData(jo);
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        mQueue = Volley.newRequestQueue(getActivity());
        hh.start(getActivity(), ApiManager.HttpApi.EditTeeTimeSetting, params);
    }

    @Override
    protected void setListenersOfControls() {

        noDoubleClickListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (timeCheck()) {
                    //edit tee time setting
                    if (("allSame").equals(editOrAdd)) {

                        deleteConfirmPopupWindow = new DeleteConfirmPopupWindow(getActivity(), null);

                        deleteConfirmPopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_window),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                        deleteConfirmPopupWindow.btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                editTeeTimeSetting();
                                deleteConfirmPopupWindow.dismiss();
                            }
                        });
                        deleteConfirmPopupWindow.btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                deleteConfirmPopupWindow.dismiss();
                            }
                        });

                        //add tee time setting
                    } else if (("allNull").equals(editOrAdd)) {
                        addTeeTimeSetting();
                        //not all same
                    } else if (("notAll").equals(editOrAdd)) {

                        deleteConfirmPopupWindow = new DeleteConfirmPopupWindow(getActivity(), null);

                        deleteConfirmPopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_window),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                        deleteConfirmPopupWindow.btn_ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addTeeTimeSetting();
                                deleteConfirmPopupWindow.dismiss();
                            }
                        });
                        deleteConfirmPopupWindow.btn_cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deleteConfirmPopupWindow.dismiss();
                            }
                        });

                    }
                }
            }
        };

        tvFirstTeeTimeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = tvFirstTeeTimeValue.getText().toString();

                String[] times = str.split(Constants.STR_COLON);

                selectTimePopupWindow = new SelectTimePopupWindow(getActivity(), times, 1);

                selectTimePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_window),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                selectTimePopupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String formatTime
                                = String.format(Constants.STRING_FORMAT_02D_02D, selectTimePopupWindow.wheelViewHour.getCurrentItem(),
                                selectTimePopupWindow.wheelViewMin.getCurrentItem());
                        tvFirstTeeTimeValue.setText(formatTime);
                        selectTimePopupWindow.dismiss();
                    }
                });
                selectTimePopupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectTimePopupWindow.dismiss();
                    }
                });

            }
        });

        tvFinishTeeTimeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = tvFinishTeeTimeValue.getText().toString();

                String[] times = str.split(Constants.STR_COLON);

                selectTimePopupWindow = new SelectTimePopupWindow(getActivity(), times, 1);

                selectTimePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_window),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                selectTimePopupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String formatTime
                                = String.format(Constants.STRING_FORMAT_02D_02D, selectTimePopupWindow.wheelViewHour.getCurrentItem(),
                                selectTimePopupWindow.wheelViewMin.getCurrentItem());
                        tvFinishTeeTimeValue.setText(formatTime);
                        selectTimePopupWindow.dismiss();
                    }
                });
                selectTimePopupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectTimePopupWindow.dismiss();
                    }
                });

            }
        });


        transferCourseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                final IteeTextView tvTime = (IteeTextView) view.findViewById(R.id.tv_transfer_time);

                final String time = tvTime.getText().toString();

                final String[] times = time.split(Constants.STR_H);

                selectTimePopupWindow = new SelectTimePopupWindow(getActivity(), times, 1);

                selectTimePopupWindow.showAtLocation(TeeTimeSettingEditOrAddFragment.this.getRootView().findViewById(R.id.popup_window), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                selectTimePopupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String formatTime = String.format(Constants.TIME_FORMAT_DHD, selectTimePopupWindow.wheelViewHour.getCurrentItem(), selectTimePopupWindow.wheelViewMin.getCurrentItem());
                        tvTime.setText(formatTime);
                        getTransferTimeChanged();
                        selectTimePopupWindow.dismiss();
                    }
                });
                selectTimePopupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getTransferTimeChanged();
                        selectTimePopupWindow.dismiss();
                    }
                });

            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

        int rowHeight = 100;
        Context mContext = getActivity();
        LayoutUtils.setLayoutHeight(rlStartDateContainer, rowHeight, mContext);
//        LayoutUtils.setLayoutHeight(rlEndDateContainer, rowHeight, mContext);
        LayoutUtils.setLayoutHeight(rlSunriseContainer, rowHeight, mContext);
        LayoutUtils.setLayoutHeight(rlSunsetContainer, rowHeight, mContext);
        LayoutUtils.setLayoutHeight(rlFirstTeeTimeContainer, rowHeight, mContext);
        LayoutUtils.setLayoutHeight(rlFinishTeeTimeContainer, rowHeight, mContext);
        LayoutUtils.setLayoutHeight(rlGapTimeContainer, rowHeight, mContext);
        LayoutUtils.setLayoutHeight(rlTransferTimeContainer, rowHeight, mContext);

        rlStartDateContainer.addView(tvStartDateName);
        rlStartDateContainer.addView(tvStartDateValue);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvStartDateName, mContext);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvStartDateValue, mContext);

        RelativeLayout.LayoutParams tvStartDateValueParams = (RelativeLayout.LayoutParams) tvStartDateValue.getLayoutParams();
        tvStartDateValueParams.width = getActualWidthOnThisDevice(400);

        rlSunriseContainer.addView(tvSunriseName);
        rlSunriseContainer.addView(tvSunriseValue);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvSunriseName, mContext);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvSunriseValue, mContext);

        rlSunsetContainer.addView(tvSunsetName);
        rlSunsetContainer.addView(tvSunsetValue);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvSunsetName, mContext);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvSunsetValue, mContext);

        rlFirstTeeTimeContainer.addView(tvFirstTeeTime);
        rlFirstTeeTimeContainer.addView(tvFirstTeeTimeValue);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvFirstTeeTime, mContext);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvFirstTeeTimeValue, mContext);

        rlFinishTeeTimeContainer.addView(tvFinishTeeTime);
        rlFinishTeeTimeContainer.addView(tvFinishTeeTimeValue);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvFinishTeeTime, mContext);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvFinishTeeTimeValue, mContext);


        rlGapTimeContainer.addView(tvGapTime);
        RelativeLayout.LayoutParams paramsTvGapTime = (RelativeLayout.LayoutParams) tvGapTime.getLayoutParams();
        paramsTvGapTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvGapTime.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvGapTime.leftMargin = getActualWidthOnThisDevice(40);
        paramsTvGapTime.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvGapTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        tvGapTime.setLayoutParams(paramsTvGapTime);

        rlGapTimeContainer.addView(tvGapTimeUnit);
        RelativeLayout.LayoutParams paramsTvGapTimeUnit = (RelativeLayout.LayoutParams) tvGapTimeUnit.getLayoutParams();
        paramsTvGapTimeUnit.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvGapTimeUnit.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvGapTimeUnit.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsTvGapTimeUnit.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvGapTimeUnit.rightMargin = getActualWidthOnThisDevice(40);
        tvGapTimeUnit.setLayoutParams(paramsTvGapTimeUnit);
        tvGapTimeUnit.setId(View.generateViewId());

        rlGapTimeContainer.addView(etGapTimeValue);
        RelativeLayout.LayoutParams paramsEtGapTimeValue = (RelativeLayout.LayoutParams) etGapTimeValue.getLayoutParams();
        paramsEtGapTimeValue.width = getActualWidthOnThisDevice(200);
        paramsEtGapTimeValue.height = MATCH_PARENT;
        paramsEtGapTimeValue.addRule(RelativeLayout.LEFT_OF, tvGapTimeUnit.getId());
        etGapTimeValue.setLayoutParams(paramsEtGapTimeValue);

        rlTransferTimeContainer.addView(tvTransferTime);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvTransferTime, 40, mContext);
    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getResources().getString(R.string.tee_times_tee_times_setting));
        getTvRight().setText(R.string.tee_times_ok);

        getTvRight().setOnClickListener(noDoubleClickListener);
    }


    protected void initData(JsonTeeTimeSetting jo) {

        tvStartDateValue.setText(AppUtils.getEtTimeTitle(AppUtils.changeString2List(startDate, Constants.STR_COMMA),
                TeeTimeSettingEditOrAddFragment.this.getBaseActivity()));


        if (jo.getDataList().getGapTime() != null && jo.getDataList().getGapTime() != 0) {
            etGapTimeValue.setText(String.valueOf(jo.getDataList().getGapTime()));
        } else {
            etGapTimeValue.setText(Constants.DEFAULT_GAP_TIME);
        }

        if (jo.getDataList().getTransferTimeList() != null) {
            List<JsonTeeTimeSetting.DataList.TransferTime> transferTimeList = jo.getDataList().getTransferTimeList();

            for (JsonTeeTimeSetting.DataList.TransferTime transferTime : transferTimeList) {
                transferTimeIdList.add(transferTime.getId());
                transferTimeNameList.add(transferTime.getName());
                transferTimeTimeList.add(transferTime.getTime());
            }

            for (int i = 0; i < transferTimeIdList.size(); i++) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("id", transferTimeIdList.get(i));
                map.put("name", transferTimeNameList.get(i));
                map.put("time", transferTimeTimeList.get(i));
                Utils.log("time : " + transferTimeTimeList.get(i));
                if (minTransferTime == null) {
                    minTransferTime = transferTimeTimeList.get(i);
                } else {
                    if (minTransferTime.compareTo(transferTimeTimeList.get(i)) > 0) {
                        minTransferTime = transferTimeTimeList.get(i);
                    }
                }
                transferCourseData.add(map);
            }

            Utils.log("minTransferTime : " + minTransferTime);

            if (transferCourseData != null) {
                TeeTimeSettingTransferCourseAdapter transferCourseAdapter
                        = new TeeTimeSettingTransferCourseAdapter(getActivity(), transferCourseData);
                transferCourseListView.setAdapter(transferCourseAdapter);
                LayoutUtils.setListViewHeightBasedOnChildren(transferCourseListView);
            }

            getSunRiseSetTime(jo.getDataList().getFirstTeeTime(), jo.getDataList().getLastTeeTime());
        }
    }

    protected void editTeeTimeSetting() {
        getTransferTimeChanged();

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COURSE_ID, courseId);


        params.put(ApiKey.TEE_TIME_SETTING_START_DATE, startDate);
        params.put(ApiKey.TEE_TIME_SETTING_END_DATE, endDate);
        params.put(ApiKey.TEE_TIME_SETTING_SUNRISE, tvSunriseValue.getText().toString() + Constants.TIME_ADD_SS);
        params.put(ApiKey.TEE_TIME_SETTING_SUNSET, tvSunsetValue.getText().toString() + Constants.TIME_ADD_SS);
        params.put(ApiKey.TEE_TIME_SETTING_FIRST_TEE_TIME, tvFirstTeeTimeValue.getText().toString() + Constants.TIME_ADD_SS);
        params.put(ApiKey.TEE_TIME_SETTING_FINISH_TEE_TIME, tvFinishTeeTimeValue.getText().toString() + Constants.TIME_ADD_SS);
        params.put(ApiKey.TEE_TIME_SETTING_GAP_TIME, etGapTimeValue.getText().toString());
        params.put(ApiKey.TEE_TIME_SETTING_TRANSFER_TIME, sbTransferChanged.toString());

        Log.i("--edit tee time setting", params.toString());
        HttpManager<JsonEditDoTeeTimeSetting> hh = new HttpManager<JsonEditDoTeeTimeSetting>(TeeTimeSettingEditOrAddFragment.this) {

            @Override
            public void onJsonSuccess(JsonEditDoTeeTimeSetting jo) {

                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                doBack();
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };

        hh.start(getActivity(), ApiManager.HttpApi.EditTeeTime, params);

    }

    protected void addTeeTimeSetting() {

        getTransferTimeChanged();

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COURSE_ID, courseId);


        params.put(ApiKey.TEE_TIME_SETTING_START_DATE, startDate);
        params.put(ApiKey.TEE_TIME_SETTING_END_DATE, endDate);
        // params.put(ApiKey.TEE_TIME_SETTING_END_DATE, tvEndDateValue.getText().toString());
        params.put(ApiKey.TEE_TIME_SETTING_SUNRISE, tvSunriseValue.getText().toString() + Constants.TIME_ADD_SS);
        params.put(ApiKey.TEE_TIME_SETTING_SUNSET, tvSunsetValue.getText().toString() + Constants.TIME_ADD_SS);
        params.put(ApiKey.TEE_TIME_SETTING_FIRST_TEE_TIME, tvFirstTeeTimeValue.getText().toString() + Constants.TIME_ADD_SS);
        params.put(ApiKey.TEE_TIME_SETTING_FINISH_TEE_TIME, tvFinishTeeTimeValue.getText().toString() + Constants.TIME_ADD_SS);
        params.put(ApiKey.TEE_TIME_SETTING_GAP_TIME, etGapTimeValue.getText().toString());
        params.put(ApiKey.TEE_TIME_SETTING_TRANSFER_TIME, sbTransferChanged.toString());

        Log.i("--add tee time setting", params.toString());
        HttpManager<JsonAddDoTeeTimeSetting> hh = new HttpManager<JsonAddDoTeeTimeSetting>(TeeTimeSettingEditOrAddFragment.this) {

            @Override
            public void onJsonSuccess(JsonAddDoTeeTimeSetting jo) {
                doBack();
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.AddTeeTime, params);
    }

    protected void getTransferTimeChanged() {
        if (transferTimeIdList != null) {
            sbTransferChanged = new StringBuffer("[");
            if (transferCourseData.size() > 0) {
                for (int i = 0; i < transferCourseData.size(); i++) {
                    IteeTextView timeView = (IteeTextView) transferCourseListView.getChildAt(i).findViewById(R.id.tv_transfer_time);
                    String id = transferTimeIdList.get(i).toString();
                    String time = timeView.getText().toString();
                    if (i == 0) {
                        sbTransferChanged.append("{\"id\":").append(id).append(",\"time\":\"").append(time).append("\"}");
                    } else if (i > 0) {
                        sbTransferChanged.append(",{\"id\":").append(id).append(",\"time\":\"").append(time).append("\"}");
                    } else if (i == transferCourseData.size() - 1) {
                        sbTransferChanged.append("{\"id\":").append(id).append(",\"time\":\"").append(time).append("\"}");
                    }
                }
            }
            sbTransferChanged.append("]");
        }
    }

    private boolean timeCheck() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMAT_HHMM, Locale.getDefault());
        String startTime = tvFirstTeeTimeValue.getText().toString();
        String endTime = tvFinishTeeTimeValue.getText().toString();

        if (Utils.isStringNotNullOrEmpty(startTime) && Utils.isStringNotNullOrEmpty(endTime)) {

            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            try {
                c1.setTime(dateFormat.parse(startTime));
                c2.setTime(dateFormat.parse(endTime));

            } catch (ParseException e) {
                Utils.log(e.getMessage());
            }

            if (c2.compareTo(c1) <= 0) {
                Utils.showShortToast(getActivity(), getResources().getString(R.string.segment_times_time_input));
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void getSunRiseSetTime(final String firstTeeTime, final String lastTeeTime) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_DATE, DateUtils.getAPIYearMonthDay(theStartDate));

        HttpManager<JsonDateSun> hh = new HttpManager<JsonDateSun>(TeeTimeSettingEditOrAddFragment.this) {

            @Override
            public void onJsonSuccess(JsonDateSun jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20301) {
                    sunriseTime = jo.getSunrise();
                    sunsetTime = jo.getSunset();

                    tvSunriseValue.setText(sunriseTime);
                    tvSunsetValue.setText(sunsetTime);

                    if (Utils.isStringNotNullOrEmpty(firstTeeTime)) {
                        String startTeeTime = firstTeeTime.substring(0, 5);
                        String finishTeeTime = lastTeeTime.substring(0, 5);
                        tvFirstTeeTimeValue.setText(startTeeTime);
                        tvFinishTeeTimeValue.setText(finishTeeTime);
                    } else {
                        tvFirstTeeTimeValue.setText(sunriseTime);

                        if (StringUtils.isNotEmpty(minTransferTime)) {
                            int minuteOfTransferTime = changeHour2Minute(minTransferTime);
                            Date sunriseDateTime = DateUtils.getDateFromFormat(sunriseTime, DateUtils.TIME_FORMAT_HH_MM);
                            Date sunsetDateTime = DateUtils.getDateFromFormat(sunsetTime, DateUtils.TIME_FORMAT_HH_MM);
                            Calendar calendarSunrise = Calendar.getInstance();
                            Calendar calendarSunset = Calendar.getInstance();
                            calendarSunrise.setTime(sunriseDateTime);
                            calendarSunset.setTime(sunsetDateTime);
                            calendarSunset.add(Calendar.MINUTE, -minuteOfTransferTime);
                            if (calendarSunset.after(calendarSunrise)) {
                                String endTime = DateUtils.getStringFromFormat(calendarSunset.getTime(), DateUtils.TIME_FORMAT_HH_MM);
                                tvFinishTeeTimeValue.setText(endTime);
                            } else {
                                tvFinishTeeTimeValue.setText(sunsetTime);
                            }
                        } else {
                            tvFinishTeeTimeValue.setText(sunsetTime);
                        }
                    }
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.GetDateSun, params);
    }

    private int changeHour2Minute(String hourString) {
        int res = 0;
        if (StringUtils.isNotEmpty(hourString)) {
            if (hourString.toLowerCase().contains("h")) {
                String[] hs = hourString.toLowerCase().split("h");
                res += Integer.valueOf(hs[0]) * 60 + Integer.valueOf(hs[1]);
            } else {
                res += Integer.valueOf(hourString);
            }
        }
        return res;
    }
}
