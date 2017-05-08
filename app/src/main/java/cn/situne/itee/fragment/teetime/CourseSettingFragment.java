/**
 * Project Name: itee
 * File Name:	 CourseSettingFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-01-29
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCourseList;
import cn.situne.itee.manager.jsonentity.JsonEditCourseData;
import cn.situne.itee.manager.jsonentity.JsonEditHoleData;
import cn.situne.itee.manager.jsonentity.JsonTransferTime;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeRedDeleteButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectChangeCoursePopupWindow;
import cn.situne.itee.view.popwindow.SelectOutOrInPopupWindow;
import cn.situne.itee.view.popwindow.SelectTimePopupWindow;

/**
 * ClassName:CourseSettingFragment <br/>
 * Function: tee time course setting edit. <br/>
 * UI:  03-7-2
 * Date: 2015-01-22 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CourseSettingFragment extends BaseFragment {

    private static final String STR_PAR = "Par";

    private RelativeLayout rlCourse;
    private RelativeLayout rlHoles;
    private RelativeLayout rlTransferCourse;
    private RelativeLayout rlTransferTime;
    private RelativeLayout rlTee;
    private RelativeLayout rlHoleOne;
    private RelativeLayout rlHoleTwo;
    private RelativeLayout rlHoleThree;
    private RelativeLayout rlHoleFour;
    private RelativeLayout rlHoleFive;
    private RelativeLayout rlHoleSix;
    private RelativeLayout rlHoleSeven;
    private RelativeLayout rlHoleEight;
    private RelativeLayout rlHoleNine;
    private RelativeLayout rlCourseDelete;

    private SelectTimePopupWindow popupWindow;
    private SelectChangeCoursePopupWindow changeCoursePopupWindow;
    private SelectOutOrInPopupWindow selectOutOrInPopupWindow;

    private String parOne;
    private String parTwo;
    private String parThree;
    private String parFour;
    private String parFive;
    private String parSix;
    private String parSeven;
    private String parEight;
    private String parNine;

    private String holeOne;
    private String holeTwo;
    private String holeThree;
    private String holeFour;
    private String holeFive;
    private String holeSix;
    private String holeSeven;
    private String holeEight;
    private String holeNine;
    private String courseAreaType;
    private String courseAreaId;

    private String unit;

    private IteeTextView tvAddHoleOneKey;
    private IteeTextView tvAddHoleOneValue;
    private IteeTextView tvAddHoleTwoKey;
    private IteeTextView tvAddHoleTwoValue;
    private IteeTextView tvAddHoleThreeKey;
    private IteeTextView tvAddHoleThreeValue;
    private IteeTextView tvAddHoleFourKey;
    private IteeTextView tvAddHoleFourValue;
    private IteeTextView tvAddHoleFiveKey;
    private IteeTextView tvAddHoleFiveValue;
    private IteeTextView tvAddHoleSixKey;
    private IteeTextView tvAddHoleSixValue;
    private IteeTextView tvAddHoleSevenKey;
    private IteeTextView tvAddHoleSevenValue;
    private IteeTextView tvAddHoleEightKey;
    private IteeTextView tvAddHoleEightValue;
    private IteeTextView tvAddNineKey;
    private IteeTextView tvAddNineValue;

    private IteeTextView[] tvParValues;

    private IteeRedDeleteButton btCourseDelete;

    private IteeTextView tvAddTCourseKey;
    private IteeTextView tvAddTCourseValue;

    private IteeTextView tvAddHoleVKey;
    private IteeEditText etAddHoleValue;

    private IteeTextView tvAddTTimeKey;
    private IteeTextView tvAddTransferTimeValue;
    private IteeTextView tvCalculate;

    private IteeTextView tvAddCourseKey;
    private IteeEditText etAddCourseValue;

    private IteeTextView tvTeeKey;
    private ImageView ivTeeIcon;

    private String tTime;
    private String titleMinute;
    private int titleMinutes;

    private String courseAreType;

    private ArrayList<JsonCourseList.DataList> dataList;
    private ArrayList<HashMap<String, String>> courseAreaTypeList;
    public String courseType;
    public String courseArId;

    private int calculate;

    private AppUtils.NoDoubleClickListener noDoubleClickListener;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_new_course_message;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        changeCoursePopupWindow = new SelectChangeCoursePopupWindow(getActivity(), null);
        selectOutOrInPopupWindow = new SelectOutOrInPopupWindow(getActivity(), null);

        rlCourse = (RelativeLayout) rootView.findViewById(R.id.rl_course);
        rlHoles = (RelativeLayout) rootView.findViewById(R.id.rl_holes);
        rlTransferCourse = (RelativeLayout) rootView.findViewById(R.id.rl_transfer_course);
        rlTransferTime = (RelativeLayout) rootView.findViewById(R.id.rl_transfer_time);
        rlTee = (RelativeLayout) rootView.findViewById(R.id.rl_tee);
        rlHoleOne = (RelativeLayout) rootView.findViewById(R.id.rl_holeOne);
        rlHoleTwo = (RelativeLayout) rootView.findViewById(R.id.rl_holeTwo);
        rlHoleThree = (RelativeLayout) rootView.findViewById(R.id.rl_holeThree);
        rlHoleFour = (RelativeLayout) rootView.findViewById(R.id.rl_holeFour);
        rlHoleFive = (RelativeLayout) rootView.findViewById(R.id.rl_holeFive);
        rlHoleSix = (RelativeLayout) rootView.findViewById(R.id.rl_holeSix);
        rlHoleSeven = (RelativeLayout) rootView.findViewById(R.id.rl_holeSeven);
        rlHoleEight = (RelativeLayout) rootView.findViewById(R.id.rl_holeEight);
        rlHoleNine = (RelativeLayout) rootView.findViewById(R.id.rl_holeNine);
        rlCourseDelete = (RelativeLayout) rootView.findViewById(R.id.rl_delete);

        btCourseDelete = new IteeRedDeleteButton(getActivity());
        tvAddHoleOneKey = new IteeTextView(this);
        tvAddHoleOneValue = new IteeTextView(this);
        tvAddHoleTwoKey = new IteeTextView(this);
        tvAddHoleTwoValue = new IteeTextView(this);
        tvAddHoleThreeKey = new IteeTextView(this);
        tvAddHoleThreeValue = new IteeTextView(this);
        tvAddHoleFourKey = new IteeTextView(this);
        tvAddHoleFourValue = new IteeTextView(this);
        tvAddHoleFiveKey = new IteeTextView(this);
        tvAddHoleFiveValue = new IteeTextView(this);
        tvAddHoleSixKey = new IteeTextView(this);
        tvAddHoleSixValue = new IteeTextView(this);
        tvAddHoleSevenKey = new IteeTextView(this);
        tvAddHoleSevenValue = new IteeTextView(this);
        tvAddHoleEightKey = new IteeTextView(this);
        tvAddHoleEightValue = new IteeTextView(this);
        tvAddNineKey = new IteeTextView(this);
        tvAddNineValue = new IteeTextView(this);

        tvParValues = new IteeTextView[]{
                tvAddHoleOneValue,
                tvAddHoleTwoValue,
                tvAddHoleThreeValue,
                tvAddHoleFourValue,
                tvAddHoleFiveValue,
                tvAddHoleSixValue,
                tvAddHoleSevenValue,
                tvAddHoleEightValue,
                tvAddNineValue
        };

        tvAddTCourseKey = new IteeTextView(this);
        tvAddTCourseValue = new IteeTextView(this);
        tvAddHoleVKey = new IteeTextView(this);
        etAddHoleValue = new IteeEditText(this);
        tvAddTTimeKey = new IteeTextView(this);
        tvAddTransferTimeValue = new IteeTextView(this);
        tvCalculate = new IteeTextView(this);
        tvAddCourseKey = new IteeTextView(this);
        etAddCourseValue = new IteeEditText(this);
        tvTeeKey = new IteeTextView(this);
        ivTeeIcon = new ImageView(getActivity());
    }


    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

        noDoubleClickListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                keyBoardDisappear();
                updateCourseMessage();
            }
        };

        rlTransferCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (courseAreaTypeList != null && courseAreaTypeList.size() > 0) {
                    Utils.hideKeyboard(getActivity());
                    changeCoursePopupWindow.showAtLocation(CourseSettingFragment.this.getRootView()
                            .findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    changeCoursePopupWindow.rlOkAndCancelContainer.setVisibility(View.GONE);
                    changeCoursePopupWindow.getBtnOk().setVisibility(View.GONE);
                    changeCoursePopupWindow.getBtnCancel().setVisibility(View.GONE);
                }

                if (courseAreaTypeList != null && courseAreaTypeList.size() == 1) {
                    showCourseArea();
                } else if (courseAreaTypeList != null && courseAreaTypeList.size() >= 2) {
                    showCourseArea();
                }


            }
        });

        rlTransferTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Utils.hideKeyboard(getActivity());
                String editTextTime = tvAddTransferTimeValue.getText().toString();
                if (!TextUtils.isEmpty(editTextTime)) {
                    String[] times = editTextTime.split(Constants.STR_H);
                    popupWindow = new SelectTimePopupWindow(getActivity(), times, 1);
                } else {
                    popupWindow = new SelectTimePopupWindow(getActivity(), null);
                }
                popupWindow.showAtLocation(CourseSettingFragment.this.getRootView()
                                .findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View buttonView) {
                        String formatTime = String.format(Constants.TIME_FORMAT_DHD, popupWindow.wheelViewHour.getCurrentItem(), popupWindow.wheelViewMin
                                .getCurrentItem());
                        tvAddTransferTimeValue.setText(formatTime);
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

        rlTee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push(CourseTeesFragment.class);
            }
        });

        rlHoleOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeOne);
                bundle.putString("holePar", parOne);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("unit", unit);
                bundle.putString("sign", "1");
                push(CourseHoleSettingFragment.class, bundle);
            }
        });

        rlHoleTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeTwo);
                bundle.putString("holePar", parTwo);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("unit", unit);
                bundle.putString("sign", "2");
                push(CourseHoleSettingFragment.class, bundle);
            }
        });

        rlHoleThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeThree);
                bundle.putString("holePar", parThree);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("unit", unit);
                bundle.putString("sign", "3");
                push(CourseHoleSettingFragment.class, bundle);
            }
        });

        rlHoleFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeFour);
                bundle.putString("holePar", parFour);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("unit", unit);
                bundle.putString("sign", "4");
                push(CourseHoleSettingFragment.class, bundle);
            }
        });

        rlHoleFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeFive);
                bundle.putString("holePar", parFive);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("unit", unit);
                bundle.putString("sign", "5");
                push(CourseHoleSettingFragment.class, bundle);
            }
        });

        rlHoleSix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeSix);
                bundle.putString("holePar", parSix);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("unit", unit);
                bundle.putString("sign", "6");
                push(CourseHoleSettingFragment.class, bundle);
            }
        });

        rlHoleSeven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeSeven);
                bundle.putString("holePar", parSeven);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("unit", unit);
                bundle.putString("sign", "7");
                push(CourseHoleSettingFragment.class, bundle);
            }
        });

        rlHoleEight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeEight);
                bundle.putString("holePar", parEight);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("unit", unit);
                bundle.putString("sign", "8");
                push(CourseHoleSettingFragment.class, bundle);
            }
        });

        rlHoleNine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeNine);
                bundle.putString("holePar", parNine);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("unit", unit);
                bundle.putString("sign", "9");
                push(CourseHoleSettingFragment.class, bundle);
            }
        });

        tvAddCourseKey.setText(R.string.course_course);

        etAddCourseValue.setWidth((int) (getScreenWidth() * 0.15));

        etAddCourseValue.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                return false;
            }
        });

        tvCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> params = new HashMap<>();
                params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
                params.put(ApiKey.TEE_TIME_COURSE_AREA_ID, courseAreaId);

                HttpManager<JsonTransferTime> hh = new HttpManager<JsonTransferTime>(CourseSettingFragment.this) {

                    @Override
                    public void onJsonSuccess(JsonTransferTime jo) {
                        String transferTime = jo.getDataList().transferTime;
                        tvAddTransferTimeValue.setText(transferTime);
                        Integer returnCode = jo.getReturnCode();
                        String msg = jo.getReturnInfo();
                        if (returnCode == Constants.RETURN_CODE_20149_COURSE_HOLE) {
                            Utils.showShortToast(getActivity(), msg);
                        }
                    }

                    @Override
                    public void onJsonError(VolleyError error) {
                        Utils.showShortToast(getActivity(), String.valueOf(error));
                    }
                };
                hh.startGet(getActivity(), ApiManager.HttpApi.TransferTimeGet, params);
//                if (calculate == 1) {
//                    Map<String, String> params = new HashMap<>();
//                    params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
//                    params.put(ApiKey.TEE_TIME_COURSE_AREA_ID, courseAreaId);
//
//                    HttpManager<JsonTransferTime> hh = new HttpManager<JsonTransferTime>(CourseSettingFragment.this) {
//
//                        @Override
//                        public void onJsonSuccess(JsonTransferTime jo) {
//                            String transferTime = jo.getDataList().transferTime;
//                            tvAddTransferTimeValue.setText(transferTime);
//                            Integer returnCode = jo.getReturnCode();
//                            String msg = jo.getReturnInfo();
//                            if (returnCode == Constants.RETURN_CODE_20149_COURSE_HOLE) {
//                                Utils.showShortToast(getActivity(), msg);
//                            }
//                        }
//
//                        @Override
//                        public void onJsonError(VolleyError error) {
//                            Utils.showShortToast(getActivity(), String.valueOf(error));
//                        }
//                    };
//                    hh.startGet(getActivity(), ApiManager.HttpApi.TransferTimeGet, params);
//                }
            }
        });

        btCourseDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AppUtils.showDeleteAlert(CourseSettingFragment.this, new AppUtils.DeleteConfirmListener() {
                    @Override
                    public void onClickDelete() {
                        Map<String, String> params = new HashMap<>();
                        params.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
                        params.put(ApiKey.COURSE_AREA_ID, courseAreaId);

                        HttpManager<JsonEditHoleData> hh = new HttpManager<JsonEditHoleData>(CourseSettingFragment.this) {

                            @Override
                            public void onJsonSuccess(JsonEditHoleData jo) {
                                Integer returnCode = jo.getReturnCode();
                                String msg = jo.getReturnInfo();

                                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                                    setOnBackListener(null);
                                    getBaseActivity().doBack();
                                } else {
                                    Utils.showShortToast(getActivity(), msg);
                                }
                            }

                            @Override
                            public void onJsonError(VolleyError error) {

                            }
                        };
                        hh.start(getActivity(), ApiManager.HttpApi.DoDelCourseData, params);
                    }
                });

            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

        LinearLayout.LayoutParams paramsCourse = (LinearLayout.LayoutParams) rlCourse.getLayoutParams();
        paramsCourse.height = getActualHeightOnThisDevice(100);
        rlCourse.setLayoutParams(paramsCourse);

        rlCourse.addView(tvAddCourseKey);
        RelativeLayout.LayoutParams paramsTvAddCourseKey = (RelativeLayout.LayoutParams) tvAddCourseKey.getLayoutParams();
        paramsTvAddCourseKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddCourseKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddCourseKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvAddCourseKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvAddCourseKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddCourseKey.setLayoutParams(paramsTvAddCourseKey);

        rlCourse.addView(etAddCourseValue);
        RelativeLayout.LayoutParams paramsTvAddCourseValue = (RelativeLayout.LayoutParams) etAddCourseValue.getLayoutParams();
        paramsTvAddCourseValue.width = getActualWidthOnThisDevice(500);
        paramsTvAddCourseValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddCourseValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvAddCourseValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvAddCourseValue.rightMargin = getActualWidthOnThisDevice(20);
        etAddCourseValue.setLayoutParams(paramsTvAddCourseValue);


        LinearLayout.LayoutParams paramsHoles = (LinearLayout.LayoutParams) rlHoles.getLayoutParams();
        paramsHoles.height = getActualHeightOnThisDevice(100);
        rlHoles.setLayoutParams(paramsHoles);

        rlHoles.addView(tvAddHoleVKey);
        RelativeLayout.LayoutParams paramsTvAddHoleVKey = (RelativeLayout.LayoutParams) tvAddHoleVKey.getLayoutParams();
        paramsTvAddHoleVKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddHoleVKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddHoleVKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvAddHoleVKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvAddHoleVKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddHoleVKey.setLayoutParams(paramsTvAddHoleVKey);

        rlHoles.addView(etAddHoleValue);
        RelativeLayout.LayoutParams paramsTvAddHoleValue = (RelativeLayout.LayoutParams) etAddHoleValue.getLayoutParams();
        paramsTvAddHoleValue.width = getActualWidthOnThisDevice(200);
        paramsTvAddHoleValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddHoleValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvAddHoleValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvAddHoleValue.rightMargin = getActualWidthOnThisDevice(20);
        etAddHoleValue.setLayoutParams(paramsTvAddHoleValue);

        LinearLayout.LayoutParams paramsTransferCourse = (LinearLayout.LayoutParams) rlTransferCourse.getLayoutParams();
        paramsTransferCourse.height = getActualHeightOnThisDevice(100);
        rlTransferCourse.setLayoutParams(paramsTransferCourse);

        rlTransferCourse.addView(tvAddTCourseKey);
        RelativeLayout.LayoutParams paramsTvAddTCourseKey = (RelativeLayout.LayoutParams) tvAddTCourseKey.getLayoutParams();
        paramsTvAddTCourseKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddTCourseKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddTCourseKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvAddTCourseKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvAddTCourseKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddTCourseKey.setLayoutParams(paramsTvAddTCourseKey);

        rlTransferCourse.addView(tvAddTCourseValue);
        RelativeLayout.LayoutParams paramsTvAddTCourseValue = (RelativeLayout.LayoutParams) tvAddTCourseValue.getLayoutParams();
        paramsTvAddTCourseValue.width = getActualWidthOnThisDevice(360);
        paramsTvAddTCourseValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddTCourseValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvAddTCourseValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvAddTCourseValue.rightMargin = getActualWidthOnThisDevice(40);
        tvAddTCourseValue.setLayoutParams(paramsTvAddTCourseValue);

        LinearLayout.LayoutParams paramsTransferTime = (LinearLayout.LayoutParams) rlTransferTime.getLayoutParams();
        paramsTransferTime.height = getActualHeightOnThisDevice(100);
        rlTransferTime.setLayoutParams(paramsTransferTime);

        rlTransferTime.addView(tvAddTTimeKey);
        RelativeLayout.LayoutParams paramsTvAddTTimeKey = (RelativeLayout.LayoutParams) tvAddTTimeKey.getLayoutParams();
        paramsTvAddTTimeKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddTTimeKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddTTimeKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvAddTTimeKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvAddTTimeKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddTTimeKey.setLayoutParams(paramsTvAddTTimeKey);

        rlTransferTime.addView(tvCalculate);
        RelativeLayout.LayoutParams paramsTvCalculate = (RelativeLayout.LayoutParams) tvCalculate.getLayoutParams();
        paramsTvCalculate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCalculate.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCalculate.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvCalculate.setMargins(getActualHeightOnThisDevice(250), 10, 0, 10);
        tvCalculate.setLayoutParams(paramsTvCalculate);

        rlTransferTime.addView(tvAddTransferTimeValue);
        RelativeLayout.LayoutParams paramsTvAddTTimeValue = (RelativeLayout.LayoutParams) tvAddTransferTimeValue.getLayoutParams();
        paramsTvAddTTimeValue.width = getActualWidthOnThisDevice(200);
        paramsTvAddTTimeValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddTTimeValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvAddTTimeValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvAddTTimeValue.rightMargin = getActualWidthOnThisDevice(40);
        tvAddTransferTimeValue.setLayoutParams(paramsTvAddTTimeValue);

        LinearLayout.LayoutParams paramsTee = (LinearLayout.LayoutParams) rlTee.getLayoutParams();
        paramsTee.height = getActualHeightOnThisDevice(100);
        rlTee.setLayoutParams(paramsTee);

        rlTee.addView(tvTeeKey);
        RelativeLayout.LayoutParams paramsTeeKey = (RelativeLayout.LayoutParams) tvTeeKey.getLayoutParams();
        paramsTeeKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTeeKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTeeKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTeeKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTeeKey.leftMargin = getActualWidthOnThisDevice(20);
        tvTeeKey.setLayoutParams(paramsTeeKey);

        rlTee.addView(ivTeeIcon);
        RelativeLayout.LayoutParams paramsIvTeeIcon = (RelativeLayout.LayoutParams) ivTeeIcon.getLayoutParams();
        paramsIvTeeIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvTeeIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvTeeIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvTeeIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsIvTeeIcon.rightMargin = getActualWidthOnThisDevice(20);
        ivTeeIcon.setLayoutParams(paramsIvTeeIcon);

        LinearLayout.LayoutParams paramsHoleOne = (LinearLayout.LayoutParams) rlHoleOne.getLayoutParams();
        paramsHoleOne.height = getActualHeightOnThisDevice(100);
        paramsHoleOne.topMargin = 25;
        rlHoleOne.setLayoutParams(paramsHoleOne);

        rlHoleOne.addView(tvAddHoleOneKey);
        RelativeLayout.LayoutParams paramsAddHoleOneKey = (RelativeLayout.LayoutParams) tvAddHoleOneKey.getLayoutParams();
        paramsAddHoleOneKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleOneKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleOneKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleOneKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsAddHoleOneKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddHoleOneKey.setLayoutParams(paramsAddHoleOneKey);

        rlHoleOne.addView(tvAddHoleOneValue);
        RelativeLayout.LayoutParams paramsTvAddHoleOneValue = (RelativeLayout.LayoutParams) tvAddHoleOneValue.getLayoutParams();
        paramsTvAddHoleOneValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddHoleOneValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddHoleOneValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvAddHoleOneValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvAddHoleOneValue.rightMargin = getActualWidthOnThisDevice(80);
        tvAddHoleOneValue.setLayoutParams(paramsTvAddHoleOneValue);

        RelativeLayout.LayoutParams icon1Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon1Params.rightMargin = getActualWidthOnThisDevice(20);
        icon1Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon1Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon1 = new ImageView(getBaseActivity());
        icon1.setBackgroundResource(R.drawable.icon_right_arrow);
        icon1.setLayoutParams(icon1Params);
        rlHoleOne.addView(icon1);



        LinearLayout.LayoutParams paramsHoleTwo = (LinearLayout.LayoutParams) rlHoleTwo.getLayoutParams();
        paramsHoleTwo.height = getActualHeightOnThisDevice(100);
        rlHoleTwo.setLayoutParams(paramsHoleTwo);

        rlHoleTwo.addView(tvAddHoleTwoKey);
        RelativeLayout.LayoutParams paramsAddHoleTwoKey = (RelativeLayout.LayoutParams) tvAddHoleTwoKey.getLayoutParams();
        paramsAddHoleTwoKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleTwoKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleTwoKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleTwoKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsAddHoleTwoKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddHoleTwoKey.setLayoutParams(paramsAddHoleTwoKey);

        rlHoleTwo.addView(tvAddHoleTwoValue);
        RelativeLayout.LayoutParams paramsTvAddHoleTwoValue = (RelativeLayout.LayoutParams) tvAddHoleTwoValue.getLayoutParams();
        paramsTvAddHoleTwoValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddHoleTwoValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAddHoleTwoValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvAddHoleTwoValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvAddHoleTwoValue.rightMargin = getActualWidthOnThisDevice(80);
        tvAddHoleTwoValue.setLayoutParams(paramsTvAddHoleTwoValue);


        RelativeLayout.LayoutParams icon2Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon2Params.rightMargin = getActualWidthOnThisDevice(20);
        icon2Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon2Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon2 = new ImageView(getBaseActivity());
        icon2.setBackgroundResource(R.drawable.icon_right_arrow);
        icon2.setLayoutParams(icon2Params);
        rlHoleTwo.addView(icon2);


        LinearLayout.LayoutParams paramsHoleThree = (LinearLayout.LayoutParams) rlHoleThree.getLayoutParams();
        paramsHoleThree.height = getActualHeightOnThisDevice(100);
        rlHoleThree.setLayoutParams(paramsHoleThree);

        rlHoleThree.addView(tvAddHoleThreeKey);
        RelativeLayout.LayoutParams paramsAddHoleThreeKey = (RelativeLayout.LayoutParams) tvAddHoleThreeKey.getLayoutParams();
        paramsAddHoleThreeKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleThreeKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleThreeKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleThreeKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsAddHoleThreeKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddHoleThreeKey.setLayoutParams(paramsAddHoleThreeKey);

        rlHoleThree.addView(tvAddHoleThreeValue);
        RelativeLayout.LayoutParams paramsAddHoleThreeValue = (RelativeLayout.LayoutParams) tvAddHoleThreeValue.getLayoutParams();
        paramsAddHoleThreeValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleThreeValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleThreeValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleThreeValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsAddHoleThreeValue.rightMargin = getActualWidthOnThisDevice(80);
        tvAddHoleThreeValue.setLayoutParams(paramsAddHoleThreeValue);


        RelativeLayout.LayoutParams icon3Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon3Params.rightMargin = getActualWidthOnThisDevice(20);
        icon3Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon3Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon3 = new ImageView(getBaseActivity());
        icon3.setBackgroundResource(R.drawable.icon_right_arrow);
        icon3.setLayoutParams(icon3Params);
        rlHoleThree.addView(icon3);


        LinearLayout.LayoutParams paramsHoleFour = (LinearLayout.LayoutParams) rlHoleFour.getLayoutParams();
        paramsHoleFour.height = getActualHeightOnThisDevice(100);
        rlHoleFour.setLayoutParams(paramsHoleFour);

        rlHoleFour.addView(tvAddHoleFourKey);
        RelativeLayout.LayoutParams paramsAddHoleFourKey = (RelativeLayout.LayoutParams) tvAddHoleFourKey.getLayoutParams();
        paramsAddHoleFourKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleFourKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleFourKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleFourKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsAddHoleFourKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddHoleFourKey.setLayoutParams(paramsAddHoleFourKey);

        rlHoleFour.addView(tvAddHoleFourValue);
        RelativeLayout.LayoutParams paramsAddHoleFourValue = (RelativeLayout.LayoutParams) tvAddHoleFourValue.getLayoutParams();
        paramsAddHoleFourValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleFourValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleFourValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleFourValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsAddHoleFourValue.rightMargin = getActualWidthOnThisDevice(80);
        tvAddHoleFourValue.setLayoutParams(paramsAddHoleFourValue);

        RelativeLayout.LayoutParams icon4Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon4Params.rightMargin = getActualWidthOnThisDevice(20);
        icon4Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon4Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon4 = new ImageView(getBaseActivity());
        icon4.setBackgroundResource(R.drawable.icon_right_arrow);
        icon4.setLayoutParams(icon4Params);
        rlHoleFour.addView(icon4);




        LinearLayout.LayoutParams paramsHoleFive = (LinearLayout.LayoutParams) rlHoleFive.getLayoutParams();
        paramsHoleFive.height = getActualHeightOnThisDevice(100);
        rlHoleFive.setLayoutParams(paramsHoleFive);

        rlHoleFive.addView(tvAddHoleFiveKey);
        RelativeLayout.LayoutParams paramsAddHoleFiveKey = (RelativeLayout.LayoutParams) tvAddHoleFiveKey.getLayoutParams();
        paramsAddHoleFiveKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleFiveKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleFiveKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleFiveKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsAddHoleFiveKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddHoleFiveKey.setLayoutParams(paramsAddHoleFiveKey);

        rlHoleFive.addView(tvAddHoleFiveValue);
        RelativeLayout.LayoutParams paramsAddHoleFiveValue = (RelativeLayout.LayoutParams) tvAddHoleFiveValue.getLayoutParams();
        paramsAddHoleFiveValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleFiveValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleFiveValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleFiveValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsAddHoleFiveValue.rightMargin = getActualWidthOnThisDevice(80);
        tvAddHoleFiveValue.setLayoutParams(paramsAddHoleFiveValue);


        RelativeLayout.LayoutParams icon5Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon5Params.rightMargin = getActualWidthOnThisDevice(20);
        icon5Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon5Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon5 = new ImageView(getBaseActivity());
        icon5.setBackgroundResource(R.drawable.icon_right_arrow);
        icon5.setLayoutParams(icon5Params);
        rlHoleFive.addView(icon5);




        LinearLayout.LayoutParams paramsHoleSix = (LinearLayout.LayoutParams) rlHoleSix.getLayoutParams();
        paramsHoleSix.height = getActualHeightOnThisDevice(100);
        rlHoleSix.setLayoutParams(paramsHoleSix);

        rlHoleSix.addView(tvAddHoleSixKey);
        RelativeLayout.LayoutParams paramsAddHoleSixKey = (RelativeLayout.LayoutParams) tvAddHoleSixKey.getLayoutParams();
        paramsAddHoleSixKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleSixKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleSixKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleSixKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsAddHoleSixKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddHoleSixKey.setLayoutParams(paramsAddHoleSixKey);

        rlHoleSix.addView(tvAddHoleSixValue);
        RelativeLayout.LayoutParams paramsAddHoleSixValue = (RelativeLayout.LayoutParams) tvAddHoleSixValue.getLayoutParams();
        paramsAddHoleSixValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleSixValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleSixValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleSixValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsAddHoleSixValue.rightMargin = getActualWidthOnThisDevice(80);
        tvAddHoleSixValue.setLayoutParams(paramsAddHoleSixValue);


        RelativeLayout.LayoutParams icon6Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon6Params.rightMargin = getActualWidthOnThisDevice(20);
        icon6Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon6Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon6 = new ImageView(getBaseActivity());
        icon6.setBackgroundResource(R.drawable.icon_right_arrow);
        icon6.setLayoutParams(icon6Params);
        rlHoleSix.addView(icon6);


        LinearLayout.LayoutParams paramsHoleSeven = (LinearLayout.LayoutParams) rlHoleSeven.getLayoutParams();
        paramsHoleSeven.height = getActualHeightOnThisDevice(100);
        rlHoleSeven.setLayoutParams(paramsHoleSeven);

        rlHoleSeven.addView(tvAddHoleSevenKey);
        RelativeLayout.LayoutParams paramsAddHoleSevenKey = (RelativeLayout.LayoutParams) tvAddHoleSevenKey.getLayoutParams();
        paramsAddHoleSevenKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleSevenKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleSevenKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleSevenKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsAddHoleSevenKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddHoleSevenKey.setLayoutParams(paramsAddHoleSevenKey);

        rlHoleSeven.addView(tvAddHoleSevenValue);
        RelativeLayout.LayoutParams paramsAddHoleSevenValue = (RelativeLayout.LayoutParams) tvAddHoleSevenValue.getLayoutParams();
        paramsAddHoleSevenValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleSevenValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleSevenValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleSevenValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsAddHoleSevenValue.rightMargin = getActualWidthOnThisDevice(80);
        tvAddHoleSevenValue.setLayoutParams(paramsAddHoleSevenValue);

        RelativeLayout.LayoutParams icon7Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon7Params.rightMargin = getActualWidthOnThisDevice(20);
        icon7Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon7Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon7 = new ImageView(getBaseActivity());
        icon7.setBackgroundResource(R.drawable.icon_right_arrow);
        icon7.setLayoutParams(icon7Params);
        rlHoleSeven.addView(icon7);




        LinearLayout.LayoutParams paramsHoleEight = (LinearLayout.LayoutParams) rlHoleEight.getLayoutParams();
        paramsHoleEight.height = getActualHeightOnThisDevice(100);
        rlHoleEight.setLayoutParams(paramsHoleEight);

        rlHoleEight.addView(tvAddHoleEightKey);
        RelativeLayout.LayoutParams paramsAddHoleEightKey = (RelativeLayout.LayoutParams) tvAddHoleEightKey.getLayoutParams();
        paramsAddHoleEightKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleEightKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleEightKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleEightKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsAddHoleEightKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddHoleEightKey.setLayoutParams(paramsAddHoleEightKey);

        rlHoleEight.addView(tvAddHoleEightValue);
        RelativeLayout.LayoutParams paramsAddHoleEightValue = (RelativeLayout.LayoutParams) tvAddHoleEightValue.getLayoutParams();
        paramsAddHoleEightValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleEightValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddHoleEightValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddHoleEightValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsAddHoleEightValue.rightMargin = getActualWidthOnThisDevice(80);
        tvAddHoleEightValue.setLayoutParams(paramsAddHoleEightValue);




        RelativeLayout.LayoutParams icon8Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon8Params.rightMargin = getActualWidthOnThisDevice(20);
        icon8Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon8Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon8 = new ImageView(getBaseActivity());
        icon8.setBackgroundResource(R.drawable.icon_right_arrow);
        icon8.setLayoutParams(icon8Params);
        rlHoleEight.addView(icon8);


        LinearLayout.LayoutParams paramsHoleNine = (LinearLayout.LayoutParams) rlHoleNine.getLayoutParams();
        paramsHoleNine.height = getActualHeightOnThisDevice(100);
        rlHoleNine.setLayoutParams(paramsHoleNine);

        rlHoleNine.addView(tvAddNineKey);
        RelativeLayout.LayoutParams paramsAddNineKey = (RelativeLayout.LayoutParams) tvAddNineKey.getLayoutParams();
        paramsAddNineKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddNineKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddNineKey.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddNineKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsAddNineKey.leftMargin = getActualWidthOnThisDevice(20);
        tvAddNineKey.setLayoutParams(paramsAddNineKey);

        rlHoleNine.addView(tvAddNineValue);
        RelativeLayout.LayoutParams paramsAddNineValue = (RelativeLayout.LayoutParams) tvAddNineValue.getLayoutParams();
        paramsAddNineValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddNineValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddNineValue.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsAddNineValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsAddNineValue.rightMargin = getActualWidthOnThisDevice(80);
        tvAddNineValue.setLayoutParams(paramsAddNineValue);


        RelativeLayout.LayoutParams icon9Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon9Params.rightMargin = getActualWidthOnThisDevice(20);
        icon9Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon9Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon9 = new ImageView(getBaseActivity());
        icon9.setBackgroundResource(R.drawable.icon_right_arrow);
        icon9.setLayoutParams(icon9Params);
        rlHoleNine.addView(icon9);

        LinearLayout.LayoutParams paramsCourseDelete = (LinearLayout.LayoutParams) rlCourseDelete.getLayoutParams();
        paramsCourseDelete.height = getActualHeightOnThisDevice(100);
        paramsCourseDelete.topMargin = 15;
        rlCourseDelete.setLayoutParams(paramsCourseDelete);

        rlCourseDelete.addView(btCourseDelete);
        RelativeLayout.LayoutParams paramsDeleteCourse = (RelativeLayout.LayoutParams) btCourseDelete.getLayoutParams();
        paramsDeleteCourse.width = (int) (getScreenWidth() * 0.9);
        paramsDeleteCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsDeleteCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsDeleteCourse.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        btCourseDelete.setLayoutParams(paramsDeleteCourse);
    }

    @Override
    protected void setPropertyOfControls() {

        tvAddCourseKey.setText(R.string.add_course);
        tvAddCourseKey.setTextColor(getColor(R.color.common_black));
        tvAddCourseKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        etAddCourseValue.setTextColor(getColor(R.color.common_gray));
        etAddCourseValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etAddCourseValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        tvAddHoleVKey.setText(R.string.add_holes);
        tvAddHoleVKey.setTextColor(getColor(R.color.common_black));
        tvAddHoleVKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        etAddHoleValue.setTextColor(getColor(R.color.common_gray));
        etAddHoleValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etAddHoleValue.setBackground(null);
        etAddHoleValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

        tvAddTCourseKey.setText(R.string.add_transfer_course);
        tvAddTCourseKey.setTextColor(getColor(R.color.common_black));
        tvAddTCourseKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddTCourseValue.setTextColor(getColor(R.color.common_black));
        tvAddTCourseValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAddTCourseValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        tvAddTCourseValue.setBackground(null);


        tvAddTTimeKey.setText(R.string.add_transfer_time);
        tvAddTTimeKey.setTextColor(getColor(R.color.common_black));
        tvAddTTimeKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddTransferTimeValue.setTextColor(getColor(R.color.common_gray));
        tvAddTransferTimeValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvAddTransferTimeValue.setBackground(null);
        tvAddTransferTimeValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

//        tvCalculate.setText(R.string.add_calculate);
//        tvCalculate.setBackgroundResource(R.drawable.btn_blue_frame);
//        tvCalculate.setGravity(Gravity.CENTER);
//        tvCalculate.setTextColor(getColor(R.color.common_blue));
        //tvCalculate.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvTeeKey.setText(R.string.add_tee);
        tvTeeKey.setTextColor(getColor(R.color.common_black));
        tvTeeKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        ivTeeIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        btCourseDelete.setId(View.generateViewId());

        tvAddHoleVKey.setText(R.string.add_holes);
        etAddHoleValue.setEnabled(false);
        etAddHoleValue.setTextColor(getColor(R.color.common_black));

        tvAddTCourseKey.setText(R.string.add_transfer_course);
        tvAddTCourseValue.setText(R.string.add_in);

        tvAddTTimeKey.setText(R.string.add_transfer_time);

        tvAddTransferTimeValue.setEnabled(false);
        tvAddTransferTimeValue.setTextColor(getColor(R.color.common_black));
        tvCalculate.setText(R.string.add_calculate);
        tvCalculate.setBackgroundResource(R.drawable.btn_blue_frame);
        tvCalculate.setGravity(Gravity.CENTER);
        tvCalculate.setTextColor(getColor(R.color.common_blue));

        tvTeeKey.setText(R.string.add_tee);

        ivTeeIcon.setBackgroundResource(R.drawable.icon_right_arrow);

        tvAddHoleOneKey.setTextColor(getColor(R.color.common_black));
        tvAddHoleOneKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleOneValue.setTextColor(getColor(R.color.common_black));
        tvAddHoleOneValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleTwoKey.setTextColor(getColor(R.color.common_black));
        tvAddHoleTwoKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleTwoValue.setTextColor(getColor(R.color.common_black));
        tvAddHoleTwoValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleThreeKey.setTextColor(getColor(R.color.common_black));
        tvAddHoleThreeKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleThreeValue.setTextColor(getColor(R.color.common_black));
        tvAddHoleThreeValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleFourKey.setTextColor(getColor(R.color.common_black));
        tvAddHoleFourKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleFourValue.setTextColor(getColor(R.color.common_black));
        tvAddHoleFourValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleFiveKey.setTextColor(getColor(R.color.common_black));
        tvAddHoleFiveKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleFiveValue.setTextColor(getColor(R.color.common_black));
        tvAddHoleFiveValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleSixKey.setTextColor(getColor(R.color.common_black));
        tvAddHoleSixKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleSixValue.setTextColor(getColor(R.color.common_black));
        tvAddHoleSixValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleSevenKey.setTextColor(getColor(R.color.common_black));
        tvAddHoleSevenKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleSevenValue.setTextColor(getColor(R.color.common_black));
        tvAddHoleSevenValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleEightKey.setTextColor(getColor(R.color.common_black));
        tvAddHoleEightKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddHoleEightValue.setTextColor(getColor(R.color.common_black));
        tvAddHoleEightValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddNineKey.setTextColor(getColor(R.color.common_black));
        tvAddNineKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvAddNineValue.setTextColor(getColor(R.color.common_black));
        tvAddNineValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        courseAreaType = (String) getArguments().get(TransKey.COURSE_AREA_TYPE);
        courseAreaId = (String) getArguments().get(TransKey.COURSE_AREA_TYPE_ID);

        etAddCourseValue.setGravity(Gravity.END);
        etAddCourseValue.setSingleLine();
        etAddCourseValue.setBackground(null);
        etAddCourseValue.setEnabled(true);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(courseAreaType);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_ok);

        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {

                if (titleMinute != null && courseAreType != null) {
                    if (!etAddCourseValue.getText().toString().equals(courseAreaType)
                            || !tvAddTransferTimeValue.getText().toString().equals(titleMinute)
                            || !tvAddTCourseValue.getText().toString().equals(courseAreType)) {
                        selectOutOrInPopupWindow.showAtLocation(CourseSettingFragment.this.getRootView().
                                findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        backSave();
                    } else {
                        setOnBackListener(null);
                        getBaseActivity().doBack();
                    }

                } else {
                    setOnBackListener(null);
                    getBaseActivity().doBack();
                }


                return false;
            }
        });

        getTvRight().setOnClickListener(noDoubleClickListener);
    }

    @Override
    protected void executeOnceOnCreate() {

        parOne = (String) getArguments().get("parOne");
        parTwo = (String) getArguments().get("parTwo");
        parThree = (String) getArguments().get("parThree");
        parFour = (String) getArguments().get("parFour");
        parFive = (String) getArguments().get("parFive");
        parSix = (String) getArguments().get("parSix");
        parSeven = (String) getArguments().get("parSeven");
        parEight = (String) getArguments().get("parEight");
        parNine = (String) getArguments().get("parNight");

        String[] parStrValues = new String[]{
                parOne,
                parTwo,
                parThree,
                parFour,
                parFive,
                parSix,
                parSeven,
                parEight,
                parNine
        };

        holeOne = (String) getArguments().get("holeOne");
        holeTwo = (String) getArguments().get("holeTwo");
        holeThree = (String) getArguments().get("holeThree");
        holeFour = (String) getArguments().get("holeFour");
        holeFive = (String) getArguments().get("holeFive");
        holeSix = (String) getArguments().get("holeSix");
        holeSeven = (String) getArguments().get("holeSeven");
        holeEight = (String) getArguments().get("holeEight");
        holeNine = (String) getArguments().get("holeNine");
        unit = (String) getArguments().get("unit");

        etAddCourseValue.setText(courseAreaType);
        tvAddHoleOneKey.setText("Hole " + holeOne);
        tvAddHoleTwoKey.setText("Hole " + holeTwo);
        tvAddHoleThreeKey.setText("Hole " + holeThree);
        tvAddHoleFourKey.setText("Hole " + holeFour);
        tvAddHoleFiveKey.setText("Hole " + holeFive);
        tvAddHoleSixKey.setText("Hole " + holeSix);
        tvAddHoleSevenKey.setText("Hole " + holeSeven);
        tvAddHoleEightKey.setText("Hole " + holeEight);
        tvAddNineKey.setText("Hole " + holeNine);

        for (int i = 0; i < tvParValues.length; i++) {
            String parStrValue = parStrValues[i];
            if (Utils.isStringNotNullOrEmpty(parStrValue) && !Constants.STR_0.equals(parStrValue)) {
                tvParValues[i].setText(STR_PAR + Constants.STR_SPACE + parStrValue);
            } else {
                tvParValues[i].setText(Constants.STR_EMPTY);
            }
        }


        changeParText(tvAddHoleOneValue);
        changeParText(tvAddHoleTwoValue);
        changeParText(tvAddHoleThreeValue);
        changeParText(tvAddHoleFourValue);
        changeParText(tvAddHoleFiveValue);
        changeParText(tvAddHoleSixValue);
        changeParText(tvAddHoleSevenValue);
        changeParText(tvAddHoleEightValue);
        changeParText(tvAddNineValue);

        getCourseList();
    }

    private void getCourseList() {
        Map<String, String> param = new HashMap<>();
        param.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
        param.put(ApiKey.COURSE_USER_ID, AppUtils.getCurrentUserId(getActivity()));

        HttpManager<JsonCourseList> h = new HttpManager<JsonCourseList>(CourseSettingFragment.this) {

            @Override
            public void onJsonSuccess(JsonCourseList jo) {

                dataList = jo.getDataList();
                courseAreaTypeList = new ArrayList<>();
                for (int i = 0; i < dataList.size(); i++) {
                    String courseAreaType = dataList.get(i).getShowCourseAreaType();
                    int courseAreaId = dataList.get(i).getShowCourseAreaId();
                    HashMap<String, String> map = new HashMap<>();
                    map.put(TransKey.COURSE_AREA_TYPE, courseAreaType);
                    map.put(TransKey.COURSE_AREA_TYPE_ID, String.valueOf(courseAreaId));
                    courseAreaTypeList.add(map);
                }

                if (courseAreaTypeList != null && courseAreaTypeList.size() > 0) {
                    courseArId = courseAreaTypeList.get(0).get(TransKey.COURSE_AREA_TYPE_ID);
                } else {
                    courseArId = null;
                }


                getEditCourseData();
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        h.start(getActivity(), ApiManager.HttpApi.CourseData, param);
    }

    private void getEditCourseData() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COURSE_AREA_ID, courseAreaId);
        Utils.log("courseAreaId : " + courseAreaId);
        Utils.log("courseArId : " + courseArId);

        HttpManager<JsonEditCourseData> hh = new HttpManager<JsonEditCourseData>(CourseSettingFragment.this) {

            @Override
            public void onJsonSuccess(JsonEditCourseData jo) {

                JsonEditCourseData.DataList data = jo.getDataList();
                Integer hole = data.getHoles();
                courseArId = String.valueOf(data.getTransferCourseId());
                calculate = data.getTransferCalculate();
                courseAreType = data.getTransferCourseType();
                tTime = data.getTransferTime();
                if (tTime != null && !tTime.equals(Constants.STR_EMPTY)) {
                    int returnTime = Integer.parseInt(tTime);
                    int hour = returnTime / Constants.MINUTES_PER_HOUR;
                    int minute = returnTime % Constants.MINUTES_PER_HOUR;
                    titleMinute = hour + "h" + minute;
                    tvAddTransferTimeValue.setText(titleMinute);
                }

                etAddHoleValue.setText(String.valueOf(hole));

                tvAddTCourseValue.setText(courseAreType);
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.EditCourseData, params);
    }

    private void changeParText(IteeTextView textView){

        if (textView.getText().toString().equals("Par " + Constants.STR_0))
            textView.setText(Constants.STR_EMPTY);

    }
    @Override
    protected void reShowWithBackValue() {

        if (getReturnValues() != null) {
            Bundle params = getReturnValues();
            String sign = params.getString("sign");
            String par = params.getString("par");
            if (Utils.isStringNotNullOrEmpty(par) && !Constants.STR_0.equals(par)) {
                if (sign.equals("1")) {
                    parOne = par;
                    tvAddHoleOneValue.setText(STR_PAR + Constants.STR_SPACE + par);
                }
                if (sign.equals("2")) {
                    parTwo = par;
                    tvAddHoleTwoValue.setText(STR_PAR + Constants.STR_SPACE + par);
                }
                if (sign.equals("3")) {
                    parThree = par;
                    tvAddHoleThreeValue.setText(STR_PAR + Constants.STR_SPACE + par);
                }
                if (sign.equals("4")) {
                    parFour = par;
                    tvAddHoleFourValue.setText(STR_PAR + Constants.STR_SPACE + par);
                }
                if (sign.equals("5")) {
                    parFive = par;
                    tvAddHoleFiveValue.setText(STR_PAR + Constants.STR_SPACE + par);
                }
                if (sign.equals("6")) {
                    parSix = par;
                    tvAddHoleSixValue.setText(STR_PAR + Constants.STR_SPACE + par);
                }
                if (sign.equals("7")) {
                    parSeven = par;
                    tvAddHoleSevenValue.setText(STR_PAR + Constants.STR_SPACE + par);
                }
                if (sign.equals("8")) {
                    parEight = par;
                    tvAddHoleEightValue.setText(STR_PAR + Constants.STR_SPACE + par);
                }
                if (sign.equals("9")) {
                    parNine = par;
                    tvAddNineValue.setText(STR_PAR + Constants.STR_SPACE + par);
                }
                getCourseList();
            }

        }
    }

    public void updateCourseMessage() {

        String editTextTime = tvAddTransferTimeValue.getText().toString();
        if (!TextUtils.isEmpty(editTextTime)) {
            String a[] = editTextTime.split("h");
            int hour = Integer.parseInt(a[0]);
            int minute = Integer.parseInt(a[1]);
            titleMinutes = hour * 60 + minute;
        }

        String courseValue = etAddCourseValue.getText().toString();

        if (!TextUtils.isEmpty(courseValue)) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.COURSE_AREA_TYPE, courseValue);
            params.put(ApiKey.COURSE_AREA_ID, courseAreaId);
            params.put(ApiKey.COURSE_HOLES, "9");
            params.put(ApiKey.COURSE_TRANSFER_COURSE_ID, courseArId);
            params.put(ApiKey.COURSE_TRANSFER_TIME, String.valueOf(titleMinutes));


            HttpManager<JsonEditCourseData> hh = new HttpManager<JsonEditCourseData>(CourseSettingFragment.this) {

                @Override
                public void onJsonSuccess(JsonEditCourseData jo) {
                    Integer returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                        setOnBackListener(null);
                        getBaseActivity().doBackWithRefresh();
                    } else {
                        Utils.showShortToast(getActivity(), msg);
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {

                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.EditDoCourseData, params);
        } else {
            Utils.showShortToast(getActivity(), R.string.course_name_is_not_null);
        }
    }

    public void backSave() {
        selectOutOrInPopupWindow.btFirstValue.setText(R.string.msg_save_change);
        selectOutOrInPopupWindow.btSecondValue.setText(R.string.common_cancel);
        selectOutOrInPopupWindow.btThirdValue.setVisibility(View.GONE);
        selectOutOrInPopupWindow.btFirstValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOutOrInPopupWindow.dismiss();
                setOnBackListener(null);
                updateCourseMessage();
            }
        });
        selectOutOrInPopupWindow.btSecondValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOutOrInPopupWindow.dismiss();
                setOnBackListener(null);
                getBaseActivity().doBack();
            }
        });
    }

    public void keyBoardDisappear() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etAddCourseValue.getWindowToken(), 0);
    }

    public void showCourseArea() {
        SelectChangeCoursePopupWindow.ChangeCourseAdapter adapter
                = new SelectChangeCoursePopupWindow.ChangeCourseAdapter(getActivity(), courseAreaTypeList);
        changeCoursePopupWindow.getLvPopupWindow().setAdapter(adapter);
        if (courseAreaTypeList.size() < 6) {
            LayoutUtils.setListViewHeightBasedOnChildrenWithTimes(changeCoursePopupWindow.getLvPopupWindow(), courseAreaTypeList.size());
        } else {
            LayoutUtils.setListViewHeightBasedOnChildrenWithTimes(changeCoursePopupWindow.getLvPopupWindow(), 6);
        }
        changeCoursePopupWindow.getLvPopupWindow().setFocusable(true);
        changeCoursePopupWindow.getLvPopupWindow().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                courseType = dataList.get(i).getShowCourseAreaType();
                courseArId = dataList.get(i).getShowCourseAreaId().toString();
                tvAddTCourseValue.setText(courseType);
                changeCoursePopupWindow.dismiss();
            }
        });
    }

}
