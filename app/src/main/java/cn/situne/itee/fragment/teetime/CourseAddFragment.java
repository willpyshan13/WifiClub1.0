/**
 * Project Name: itee
 * File Name:	 CourseAddFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-01-29
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.teetime;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCourseList;
import cn.situne.itee.manager.jsonentity.JsonEditHoleData;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.AddCoursePopupWindow;
import cn.situne.itee.view.popwindow.SelectChangeCoursePopupWindow;
import cn.situne.itee.view.popwindow.SelectTimePopupWindow;

/**
 * ClassName:CourseAddFragment <br/>
 * Function: add course. <br/>
 * UI:  03-7-2
 * Date: 2015-01-29 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CourseAddFragment extends BaseFragment {
    public String courseType;
    public String courseArId;

    private int titleMinutes;
    private RelativeLayout rlCourse;
    private RelativeLayout rlHoles;
    private RelativeLayout rlTransferCourse;
    private RelativeLayout rlTransferTime;
    private RelativeLayout rlTee;
    private IteeTextView tvCourseKey;
    private IteeEditText etCourseValue;
    private IteeTextView tvHolesKey;
    private IteeTextView tvHolesValue;
    private IteeTextView tvTransferCourseKey;
    private IteeTextView tvTransferCourseValue;
    private IteeTextView tvTransferTimeKey;
    private IteeTextView tvCalculate;
    private IteeTextView tvTransferTimeValue;
    private IteeTextView tvTeeKey;
    private ImageView ivTeeIcon;
    private SelectTimePopupWindow popupWindow;
    private SelectChangeCoursePopupWindow changeCoursePopupWindow;
    private AddCoursePopupWindow addCoursePopupWindow;
    private List<JsonCourseList.DataList> dataList;
    private ArrayList<HashMap<String, String>> courseAreaTypeList;
    private AppUtils.NoDoubleClickListener noDoubleClickListener;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_course_add;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        changeCoursePopupWindow = new SelectChangeCoursePopupWindow(getActivity(), null);
        addCoursePopupWindow = new AddCoursePopupWindow(getActivity(), null);

        rlCourse = (RelativeLayout) rootView.findViewById(R.id.rl_course);
        rlHoles = (RelativeLayout) rootView.findViewById(R.id.rl_holes);
        rlTransferCourse = (RelativeLayout) rootView.findViewById(R.id.rl_transfer_course);
        rlTransferTime = (RelativeLayout) rootView.findViewById(R.id.rl_transfer_time);
        rlTee = (RelativeLayout) rootView.findViewById(R.id.rl_transfer_tee);

        tvCourseKey = new IteeTextView(getActivity());
        etCourseValue = new IteeEditText(this);
        etCourseValue.setHint(getString(R.string.add_course));
        tvHolesKey = new IteeTextView(getActivity());
        tvHolesValue = new IteeTextView(getActivity());
        tvTransferCourseKey = new IteeTextView(getActivity());
        tvTransferCourseValue = new IteeTextView(getActivity());
        tvTransferTimeKey = new IteeTextView(getActivity());
        tvTransferTimeValue = new IteeTextView(getActivity());
        tvCalculate = new IteeTextView(getActivity());
        tvTeeKey = new IteeTextView(getActivity());
        ivTeeIcon = new ImageView(getActivity());

        tvHolesKey.setText(R.string.add_holes);
        tvHolesValue.setText(Constants.HOLE_9_VALUE);
        tvTransferCourseKey.setText(R.string.add_transfer_course);
        tvTransferTimeKey.setText(R.string.add_transfer_time);
    }

    @Override
    protected void setDefaultValueOfControls() {
        LinearLayout.LayoutParams paramsCourse = (LinearLayout.LayoutParams) rlCourse.getLayoutParams();
        paramsCourse.height = getActualHeightOnThisDevice(100);
        rlCourse.setLayoutParams(paramsCourse);

        rlCourse.addView(tvCourseKey);
        RelativeLayout.LayoutParams paramsTvCourseKey = (RelativeLayout.LayoutParams) tvCourseKey.getLayoutParams();
        paramsTvCourseKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCourseKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCourseKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvCourseKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvCourseKey.leftMargin = getActualWidthOnThisDevice(40);
        tvCourseKey.setLayoutParams(paramsTvCourseKey);

        rlCourse.addView(etCourseValue);
        RelativeLayout.LayoutParams paramsTvCourseValue = (RelativeLayout.LayoutParams) etCourseValue.getLayoutParams();
        paramsTvCourseValue.width = getActualWidthOnThisDevice(500);
        paramsTvCourseValue.height = MATCH_PARENT;
        paramsTvCourseValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvCourseValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvCourseValue.rightMargin = getActualWidthOnThisDevice(20);
        etCourseValue.setLayoutParams(paramsTvCourseValue);

        LinearLayout.LayoutParams paramsHoles = (LinearLayout.LayoutParams) rlHoles.getLayoutParams();
        paramsHoles.height = getActualHeightOnThisDevice(100);
        rlHoles.setLayoutParams(paramsHoles);

        rlHoles.addView(tvHolesKey);
        RelativeLayout.LayoutParams paramsTvHolesKey = (RelativeLayout.LayoutParams) tvHolesKey.getLayoutParams();
        paramsTvHolesKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHolesKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHolesKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvHolesKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvHolesKey.leftMargin = getActualWidthOnThisDevice(40);
        tvHolesKey.setLayoutParams(paramsTvHolesKey);

        rlHoles.addView(tvHolesValue);
        RelativeLayout.LayoutParams paramsTvHolesValue = (RelativeLayout.LayoutParams) tvHolesValue.getLayoutParams();
        paramsTvHolesValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHolesValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHolesValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvHolesValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvHolesValue.rightMargin = getActualWidthOnThisDevice(20);
        tvHolesValue.setLayoutParams(paramsTvHolesValue);

        LinearLayout.LayoutParams paramsTransferCourse = (LinearLayout.LayoutParams) rlTransferCourse.getLayoutParams();
        paramsTransferCourse.height = getActualHeightOnThisDevice(100);
        rlTransferCourse.setLayoutParams(paramsTransferCourse);

        rlTransferCourse.addView(tvTransferCourseKey);
        RelativeLayout.LayoutParams paramsTvTransferCourseKey = (RelativeLayout.LayoutParams) tvTransferCourseKey.getLayoutParams();
        paramsTvTransferCourseKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTransferCourseKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTransferCourseKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvTransferCourseKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvTransferCourseKey.leftMargin = getActualWidthOnThisDevice(40);
        tvTransferCourseKey.setLayoutParams(paramsTvTransferCourseKey);

        rlTransferCourse.addView(tvTransferCourseValue);
        RelativeLayout.LayoutParams paramsTvTransferCourseValue = (RelativeLayout.LayoutParams) tvTransferCourseValue.getLayoutParams();
        paramsTvTransferCourseValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTransferCourseValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTransferCourseValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvTransferCourseValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvTransferCourseValue.rightMargin = getActualWidthOnThisDevice(20);
        tvTransferCourseValue.setLayoutParams(paramsTvTransferCourseValue);

        LinearLayout.LayoutParams paramsTransferTime = (LinearLayout.LayoutParams) rlTransferTime.getLayoutParams();
        paramsTransferTime.height = getActualHeightOnThisDevice(100);
        rlTransferTime.setLayoutParams(paramsTransferTime);

        rlTransferTime.addView(tvTransferTimeKey);
        RelativeLayout.LayoutParams paramsTvTransferTimeKey = (RelativeLayout.LayoutParams) tvTransferTimeKey.getLayoutParams();
        paramsTvTransferTimeKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTransferTimeKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTransferTimeKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvTransferTimeKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvTransferTimeKey.leftMargin = getActualWidthOnThisDevice(40);
        tvTransferTimeKey.setLayoutParams(paramsTvTransferTimeKey);

        rlTransferTime.addView(tvCalculate);
        RelativeLayout.LayoutParams paramsTvCalculate = (RelativeLayout.LayoutParams) tvCalculate.getLayoutParams();
        paramsTvCalculate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCalculate.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCalculate.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvCalculate.setMargins(getActualWidthOnThisDevice(250), 10, 0, 10);
        tvCalculate.setLayoutParams(paramsTvCalculate);

        rlTransferTime.addView(tvTransferTimeValue);
        RelativeLayout.LayoutParams paramsTvTransferTimeValue = (RelativeLayout.LayoutParams) tvTransferTimeValue.getLayoutParams();
        paramsTvTransferTimeValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTransferTimeValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvTransferTimeValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvTransferTimeValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvTransferTimeValue.rightMargin = getActualWidthOnThisDevice(20);
        tvTransferTimeValue.setLayoutParams(paramsTvTransferTimeValue);

        LinearLayout.LayoutParams paramsTee = (LinearLayout.LayoutParams) rlTee.getLayoutParams();
        paramsTee.height = getActualHeightOnThisDevice(100);
        rlTee.setLayoutParams(paramsTee);

        rlTee.addView(tvTeeKey);
        RelativeLayout.LayoutParams paramsTeeKey = (RelativeLayout.LayoutParams) tvTeeKey.getLayoutParams();
        paramsTeeKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTeeKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTeeKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTeeKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTeeKey.leftMargin = getActualWidthOnThisDevice(40);
        tvTeeKey.setLayoutParams(paramsTeeKey);

        rlTee.addView(ivTeeIcon);
        RelativeLayout.LayoutParams paramsIvTeeIcon = (RelativeLayout.LayoutParams) ivTeeIcon.getLayoutParams();
        paramsIvTeeIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvTeeIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvTeeIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvTeeIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsIvTeeIcon.rightMargin = getActualWidthOnThisDevice(20);
        ivTeeIcon.setLayoutParams(paramsIvTeeIcon);
    }

    @Override
    protected void setListenersOfControls() {
        noDoubleClickListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (doCheck()) {
                    addCourseMessage();
                }
            }
        };


        rlTee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());
                if (!TextUtils.isEmpty(etCourseValue.getValue()) && !TextUtils.isEmpty(tvHolesValue.getText().toString())) {
                    addCoursePopupWindow.showAtLocation(CourseAddFragment.this.getRootView().findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    addCoursePopupWindow.tvEvent.setText(R.string.common_add);
                    addCoursePopupWindow.tvContent.setText(etCourseValue.getText().toString());

                    addCoursePopupWindow.btn_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addCoursePopupWindow.dismiss();
                            addTeeCourseMessage();
                        }
                    });

                    addCoursePopupWindow.btn_cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addCoursePopupWindow.dismiss();
                        }
                    });
                } else if (TextUtils.isEmpty(etCourseValue.getText().toString())) {
                    AppUtils.showMustBeFilledIn(CourseAddFragment.this, getString(R.string.segment_times_course));
                } else if (TextUtils.isEmpty(tvHolesValue.getText().toString())) {
                    AppUtils.showMustBeFilledIn(CourseAddFragment.this, getString(R.string.add_holes));
                }
            }
        });

        tvCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        rlTransferCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());
                if (courseAreaTypeList != null && courseAreaTypeList.size() > 0) {
                    if (courseAreaTypeList != null && courseAreaTypeList.size() == 1) {
                        changeCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        changeCoursePopupWindow.rlOkAndCancelContainer.setVisibility(View.GONE);
                        changeCoursePopupWindow.getBtnOk().setVisibility(View.GONE);
                        changeCoursePopupWindow.getBtnCancel().setVisibility(View.GONE);
                    } else if (courseAreaTypeList != null && courseAreaTypeList.size() > 1) {
                        changeCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        changeCoursePopupWindow.rlOkAndCancelContainer.setVisibility(View.GONE);
                        changeCoursePopupWindow.getBtnOk().setVisibility(View.GONE);
                        changeCoursePopupWindow.getBtnCancel().setVisibility(View.GONE);
                    }
                    showCourseArea();
                }

            }
        });

        rlTransferTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Utils.hideKeyboard(getActivity());
                String editTextTime = tvTransferTimeValue.getText().toString();
                String[] times = editTextTime.split(Constants.STR_H);
                popupWindow = new SelectTimePopupWindow(getActivity(), times, 1);
                popupWindow.showAtLocation(CourseAddFragment.this.getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View buttonView) {
                        String formatTime = String.format(Constants.TIME_FORMAT_DHD, popupWindow.wheelViewHour.getCurrentItem(),
                                popupWindow.wheelViewMin.getCurrentItem());
                        tvTransferTimeValue.setText(formatTime);
                        popupWindow.dismiss();
                    }
                });

                popupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View buttonView) {
                        popupWindow.dismiss();
                    }
                });
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        tvCourseKey.setText(R.string.add_course);
        tvCourseKey.setTextColor(getColor(R.color.common_black));
        tvCourseKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvHolesKey.setText(R.string.add_holes);
        tvHolesKey.setTextColor(getColor(R.color.common_black));
        tvHolesKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvTransferCourseKey.setText(R.string.add_transfer_course);
        tvTransferCourseKey.setTextColor(getColor(R.color.common_black));
        tvTransferCourseKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvTransferTimeKey.setText(R.string.add_transfer_time);
        tvTransferTimeKey.setTextColor(getColor(R.color.common_black));
        tvTransferTimeKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvTransferTimeValue.setText("2h0");

        tvCalculate.setText(R.string.add_calculate);
        tvCalculate.setBackgroundResource(R.drawable.btn_blue_frame);
        tvCalculate.setGravity(Gravity.CENTER);
        tvCalculate.setTextColor(getColor(R.color.common_blue));

        tvTeeKey.setText(R.string.add_tee);
        tvTeeKey.setTextColor(getColor(R.color.common_black));
        tvTeeKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        ivTeeIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        etCourseValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etCourseValue.setSingleLine();
        etCourseValue.setBackground(null);
        etCourseValue.setEnabled(true);
        etCourseValue.setPadding(0, 0, 0, 0);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();

        getTvLeftTitle().setText(R.string.add_title_add_course);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_ok);
        getTvRight().setOnClickListener(noDoubleClickListener);
    }

    @Override
    protected void executeOnceOnCreate() {
        super.executeOnceOnCreate();
        putCourseMessage();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addCourseMessage() {
        Utils.hideKeyboard(getActivity());
        if (courseArId == null) {
            courseArId = Constants.STR_EMPTY;
        }
        changeMinute();

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COURSE_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.COURSE_ID, AppUtils.getCurrentCourseId(getActivity()));
        params.put(ApiKey.COURSE_AREA_TYPE, etCourseValue.getText().toString());
        params.put(ApiKey.COURSE_HOLES, "9");
        params.put(ApiKey.COURSE_TRANSFER_COURSE_ID, courseArId);
        params.put(ApiKey.COURSE_TRANSFER_TIME, String.valueOf(titleMinutes));

        HttpManager<JsonEditHoleData> hh = new HttpManager<JsonEditHoleData>(CourseAddFragment.this) {
            @Override
            public void onJsonSuccess(JsonEditHoleData jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY == returnCode) {
                    doBackWithRefresh();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.AddCourseData, params);
    }

    public void addTeeCourseMessage() {
        Utils.hideKeyboard(getActivity());
        if (courseArId == null) {
            courseArId = Constants.STR_EMPTY;
        }
        changeMinute();

        if (!TextUtils.isEmpty(etCourseValue.getText().toString())) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.COURSE_USER_ID, AppUtils.getCurrentUserId(getActivity()));
            params.put(ApiKey.COURSE_ID, AppUtils.getCurrentCourseId(getActivity()));
            params.put(ApiKey.COURSE_AREA_TYPE, etCourseValue.getValue());
            params.put(ApiKey.COURSE_HOLES, "9");
            params.put(ApiKey.COURSE_TRANSFER_COURSE_ID, courseArId);
            params.put(ApiKey.COURSE_TRANSFER_TIME, String.valueOf(titleMinutes));

            HttpManager<JsonEditHoleData> hh = new HttpManager<JsonEditHoleData>(CourseAddFragment.this) {
                @Override
                public void onJsonSuccess(JsonEditHoleData jo) {
                    Integer returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();

                    if (Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY == returnCode) {
                        Bundle bundle = new Bundle();
                        bundle.putBoolean(TransKey.COURSE_IS_ADD, true);
                        push(CourseTeesFragment.class, bundle);
                    } else {
                        Utils.showShortToast(getActivity(), msg);
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {

                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.AddCourseData, params);
        } else {
            Utils.showShortToast(getActivity(), R.string.course_name_is_not_null);
        }
    }

    public void putCourseMessage() {
        Map<String, String> param = new HashMap<>();
        param.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
        param.put(ApiKey.COURSE_USER_ID, AppUtils.getCurrentUserId(getActivity()));

        HttpManager<JsonCourseList> h = new HttpManager<JsonCourseList>(CourseAddFragment.this) {
            @Override
            public void onJsonSuccess(JsonCourseList jo) {
                dataList = jo.getDataList();
                courseAreaTypeList = new ArrayList<>();
                for (int i = 0; i < dataList.size(); i++) {
                    String courseAreaType = dataList.get(i).getShowCourseAreaType();
                    int courseAreaId = dataList.get(i).getShowCourseAreaId();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("courseAreaType", courseAreaType);
                    map.put("courseAreaId", String.valueOf(courseAreaId));
                    courseAreaTypeList.add(map);
                }

                if (courseAreaTypeList != null && courseAreaTypeList.size() > 0) {
                    tvTransferCourseValue.setText(courseAreaTypeList.get(0).get("courseAreaType"));
                    courseArId = courseAreaTypeList.get(0).get("courseAreaId");
                } else {
                    tvTransferCourseValue.setText(Constants.STR_EMPTY);
                    courseArId = Constants.STR_EMPTY;
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        h.start(getActivity(), ApiManager.HttpApi.CourseData, param);
    }

    public void showCourseArea() {
        SelectChangeCoursePopupWindow.ChangeCourseAdapter adapter
                = new SelectChangeCoursePopupWindow.ChangeCourseAdapter(getActivity(), courseAreaTypeList);
        changeCoursePopupWindow.getLvPopupWindow().setAdapter(adapter);
//        if (courseAreaTypeList.size() <= 6) {
//            Utility.setListViewHeightBasedOnChildrenWithTimes(changeCoursePopupWindow.lvPopupWindow, 6);
//        } else if (courseAreaTypeList.size() > 6) {
//            Utility.setListViewHeightBasedOnChildrenWithTimes(changeCoursePopupWindow.lvPopupWindow, 6);
//        }
        changeCoursePopupWindow.getLvPopupWindow().setFocusable(true);
        changeCoursePopupWindow.getLvPopupWindow().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                courseType = dataList.get(i).getShowCourseAreaType();
                courseArId = dataList.get(i).getShowCourseAreaId().toString();
                tvTransferCourseValue.setText(courseType);
                changeCoursePopupWindow.dismiss();
            }
        });
    }

    public void changeMinute() {
        String editTextTime = tvTransferTimeValue.getText().toString();
        if (!TextUtils.isEmpty(editTextTime)) {
            String a[] = editTextTime.split("h");
            int hour = Integer.parseInt(a[0]);
            int minute = Integer.parseInt(a[1]);
            titleMinutes = hour * 60 + minute;
        }
    }

    private boolean doCheck() {
        boolean res = true;
        if (Utils.isStringNullOrEmpty(etCourseValue.getValue())) {
            res = false;
            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(this, R.string.add_course));
        }
        return res;
    }
}
