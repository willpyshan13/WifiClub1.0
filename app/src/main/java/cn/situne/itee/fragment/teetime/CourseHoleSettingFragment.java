/**
 * Project Name: itee
 * File Name:	 CourseHoleSettingFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-01-20
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonCalculate;
import cn.situne.itee.manager.jsonentity.JsonTeeInfoList;
import cn.situne.itee.manager.jsonentity.JsonTransferTime;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectChangeCoursePopupWindow;
import cn.situne.itee.view.popwindow.SelectOutOrInPopupWindow;

/**
 * ClassName:CourseHoleSettingFragment <br/>
 * Function: hole setting. <br/>
 * UI:  03-7-3
 * Date: 2015-01-20 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CourseHoleSettingFragment extends BaseFragment {

    private RelativeLayout rlHoleNo;
    private RelativeLayout rlPar;
    private RelativeLayout rlYards;
    private RelativeLayout rlIndex;
    private RelativeLayout rlPace;

    private IteeTextView etHoleNoKey;
    private IteeTextView etParKey;
    private IteeTextView etYardsKey;
    private IteeTextView etIndexKey;
    private IteeTextView etPaceKey;
    private IteeTextView tvCalculate;

    private IteeTextView etHoleNoValue;
    private IteeTextView etParValue;
    private IteeTextView etIndexValue;
    private IteeTextView etPaceUnit;
    private IteeEditText etPace;

//    private LinearLayout llCourseTeeListContainer;

    private ArrayList<JsonTeeInfoList.DataList.DataInfoList> listCourseHole;

    private ArrayList<LayoutCourseTee> listTeeContainer;

    private String courseAreaId;
    private String holeNO;
    private String holePar;
    private String sign;
    private String teeUnit;

    private String valuePar;
    private String PaceValue;
    private String IndexValue;

    private String pace;

    private SelectOutOrInPopupWindow selectOutOrInPopupWindow;
    private SelectChangeCoursePopupWindow changeCoursePopupWindow;

    private AppUtils.NoDoubleClickListener noDoubleClickListener;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_course_hole_setting;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        selectOutOrInPopupWindow = new SelectOutOrInPopupWindow(getActivity(), null);
        changeCoursePopupWindow = new SelectChangeCoursePopupWindow(getActivity(), null);

        rlHoleNo = (RelativeLayout) rootView.findViewById(R.id.rl_holeNo);
        rlPar = (RelativeLayout) rootView.findViewById(R.id.rl_par);
        rlYards = (RelativeLayout) rootView.findViewById(R.id.rl_yards);
        rlIndex = (RelativeLayout) rootView.findViewById(R.id.rl_index);
        rlPace = (RelativeLayout) rootView.findViewById(R.id.rl_pace);

        etHoleNoKey = new IteeTextView(this);
        etParKey = new IteeTextView(this);
        etIndexKey = new IteeTextView(this);
        etYardsKey = new IteeTextView(this);
        etPaceKey = new IteeTextView(this);
        tvCalculate = new IteeTextView(this);

        etHoleNoValue = new IteeTextView(this);
        etParValue = new IteeTextView(this);
        etIndexValue = new IteeTextView(this);
        etPaceUnit = new IteeTextView(this);
        etPace = new IteeEditText(this);

        listCourseHole = new ArrayList<>();
        listTeeContainer = new ArrayList<>();

    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

        rlPar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                selectOutOrInPopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                choicePar();
            }
        });

        rlYards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeNO);
                bundle.putString("holePar", holePar);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("sign", "9");
                push(CourseYardsOrMetresFragment.class, bundle);
            }
        });

        rlIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyBoardDisappear();
                Bundle bundle = new Bundle();
                bundle.putString("HoleNO", holeNO);
                bundle.putString("holePar", holePar);
                bundle.putString("courseAreaId", courseAreaId);
                bundle.putString("sign", "9");
                bundle.putString("index", etIndexValue.getText().toString());
                push(CourseIndexFragment.class, bundle);
            }
        });

        noDoubleClickListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Utils.hideKeyboard(getActivity());
                putCourseHole();
            }
        };

        etPace.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (Constants.STR_0.equals(etPace.getValue())) {
                        etPace.setText(Constants.STR_EMPTY);
                    }
                } else {
                    if (Utils.isStringNotNullOrEmpty(etPace.getValue())) {
                        etPaceUnit.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        etParValue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (Constants.STR_0.equals(etParValue.getText())) {
                        etParValue.setText(Constants.STR_EMPTY);
                    }
                }
            }
        });

        tvCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    Map<String, String> params = new HashMap<>();
                params.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
                params.put(ApiKey.COURSE_HOLE_NO, holeNO);
                params.put(ApiKey.COURSE_AREA_ID, courseAreaId);

                    HttpManager<JsonCalculate> hh = new HttpManager<JsonCalculate>(CourseHoleSettingFragment.this) {

                        @Override
                        public void onJsonSuccess(JsonCalculate jo) {
                            String calculate = jo.getDataList().getCalculate();
                            etPace.setText(String.valueOf(calculate));
                            Integer returnCode = jo.getReturnCode();
                            String msg = jo.getReturnInfo();
                            if (returnCode == Constants.RETURN_CODE_20148_COURSE_HOLE || returnCode == Constants.RETURN_CODE_20147_COURSE_HOLE) {
                                Utils.showShortToast(getActivity(), msg);
                            }
                        }

                        @Override
                        public void onJsonError(VolleyError error) {
                            Utils.showShortToast(getActivity(), String.valueOf(error));
                        }
                    };
                    hh.startGet(getActivity(), ApiManager.HttpApi.GetPaceCalculate, params);
            }
        });


    }

    @Override
    protected void setLayoutOfControls() {
        LinearLayout.LayoutParams paramsHoleNo = (LinearLayout.LayoutParams) rlHoleNo.getLayoutParams();
        paramsHoleNo.height = getActualHeightOnThisDevice(100);
        rlHoleNo.setLayoutParams(paramsHoleNo);

        rlHoleNo.addView(etHoleNoKey);
        RelativeLayout.LayoutParams paramsTvHoleNoKey = (RelativeLayout.LayoutParams) etHoleNoKey.getLayoutParams();
        paramsTvHoleNoKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHoleNoKey.height = MATCH_PARENT;
        paramsTvHoleNoKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvHoleNoKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvHoleNoKey.leftMargin = getActualWidthOnThisDevice(40);
        etHoleNoKey.setLayoutParams(paramsTvHoleNoKey);

        rlHoleNo.addView(etHoleNoValue);
        RelativeLayout.LayoutParams paramsTvHoleNoValue = (RelativeLayout.LayoutParams) etHoleNoValue.getLayoutParams();
        paramsTvHoleNoValue.width = getActualWidthOnThisDevice(360);
        paramsTvHoleNoValue.height = MATCH_PARENT;
        paramsTvHoleNoValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvHoleNoValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvHoleNoValue.rightMargin = getActualWidthOnThisDevice(40);
        etHoleNoValue.setLayoutParams(paramsTvHoleNoValue);

        LinearLayout.LayoutParams paramsPar = (LinearLayout.LayoutParams) rlPar.getLayoutParams();
        paramsPar.height = getActualHeightOnThisDevice(100);
        rlPar.setLayoutParams(paramsPar);

        rlPar.addView(etParKey);
        RelativeLayout.LayoutParams paramsTvParKey = (RelativeLayout.LayoutParams) etParKey.getLayoutParams();
        paramsTvParKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvParKey.height = MATCH_PARENT;
        paramsTvParKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvParKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvParKey.leftMargin = getActualWidthOnThisDevice(40);
        etParKey.setLayoutParams(paramsTvParKey);

        rlPar.addView(etParValue);
        RelativeLayout.LayoutParams paramsTvParValue = (RelativeLayout.LayoutParams) etParValue.getLayoutParams();
        paramsTvParValue.width = getActualWidthOnThisDevice(360);
        paramsTvParValue.height = MATCH_PARENT;
        paramsTvParValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvParValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvParValue.rightMargin = getActualWidthOnThisDevice(80);
        etParValue.setLayoutParams(paramsTvParValue);

        RelativeLayout.LayoutParams icon1Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon1Params.rightMargin = getActualWidthOnThisDevice(40);
        icon1Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon1Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon1 = new ImageView(getBaseActivity());
        icon1.setBackgroundResource(R.drawable.icon_right_arrow);
        icon1.setLayoutParams(icon1Params);
        rlPar.addView(icon1);

        LinearLayout.LayoutParams paramsYards = (LinearLayout.LayoutParams) rlYards.getLayoutParams();
        paramsYards.height = getActualHeightOnThisDevice(100);
        rlYards.setLayoutParams(paramsYards);

        rlYards.addView(etYardsKey);
        RelativeLayout.LayoutParams paramsTvYardsKey = (RelativeLayout.LayoutParams) etYardsKey.getLayoutParams();
        paramsTvYardsKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvYardsKey.height = MATCH_PARENT;
        paramsTvYardsKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvYardsKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvYardsKey.leftMargin = getActualWidthOnThisDevice(40);
        etYardsKey.setLayoutParams(paramsTvYardsKey);

        RelativeLayout.LayoutParams icon2Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon2Params.rightMargin = getActualWidthOnThisDevice(40);
        icon2Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon2Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon2 = new ImageView(getBaseActivity());
        icon2.setBackgroundResource(R.drawable.icon_right_arrow);
        icon2.setLayoutParams(icon2Params);
        rlYards.addView(icon2);

        LinearLayout.LayoutParams paramsIndex = (LinearLayout.LayoutParams) rlIndex.getLayoutParams();
        paramsIndex.height = getActualHeightOnThisDevice(100);
        rlIndex.setLayoutParams(paramsIndex);

        rlIndex.addView(etIndexKey);
        RelativeLayout.LayoutParams paramsTvIndexKey = (RelativeLayout.LayoutParams) etIndexKey.getLayoutParams();
        paramsTvIndexKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvIndexKey.height = MATCH_PARENT;
        paramsTvIndexKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvIndexKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvIndexKey.leftMargin = getActualWidthOnThisDevice(40);
        etIndexKey.setLayoutParams(paramsTvIndexKey);

        rlIndex.addView(etIndexValue);
        RelativeLayout.LayoutParams paramsTvIndexValue = (RelativeLayout.LayoutParams) etIndexValue.getLayoutParams();
        paramsTvIndexValue.width = getActualWidthOnThisDevice(360);
        paramsTvIndexValue.height = MATCH_PARENT;
        paramsTvIndexValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvIndexValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvIndexValue.rightMargin = getActualWidthOnThisDevice(80);
        etIndexValue.setLayoutParams(paramsTvIndexValue);

        RelativeLayout.LayoutParams icon3Params = new RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        icon3Params.rightMargin = getActualWidthOnThisDevice(40);
        icon3Params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        icon3Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        ImageView icon3 = new ImageView(getBaseActivity());
        icon3.setBackgroundResource(R.drawable.icon_right_arrow);
        icon3.setLayoutParams(icon3Params);
        rlIndex.addView(icon3);


        LinearLayout.LayoutParams paramsPace = (LinearLayout.LayoutParams) rlPace.getLayoutParams();
        paramsPace.height = getActualHeightOnThisDevice(100);
        rlPace.setLayoutParams(paramsPace);

        rlPace.addView(etPaceKey);
        RelativeLayout.LayoutParams paramsTvPaceKey = (RelativeLayout.LayoutParams) etPaceKey.getLayoutParams();
        paramsTvPaceKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPaceKey.height = MATCH_PARENT;
        paramsTvPaceKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvPaceKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvPaceKey.leftMargin = getActualWidthOnThisDevice(40);
        etPaceKey.setLayoutParams(paramsTvPaceKey);

        rlPace.addView(tvCalculate);
        RelativeLayout.LayoutParams paramsTvCalculate = (RelativeLayout.LayoutParams) tvCalculate.getLayoutParams();
        paramsTvCalculate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCalculate.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCalculate.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvCalculate.setMargins(getActualHeightOnThisDevice(150), 10, 0, 10);
        tvCalculate.setLayoutParams(paramsTvCalculate);

        rlPace.addView(etPaceUnit);
        RelativeLayout.LayoutParams paramsTvPaceValue = (RelativeLayout.LayoutParams) etPaceUnit.getLayoutParams();
        paramsTvPaceValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPaceValue.height = MATCH_PARENT;
        paramsTvPaceValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvPaceValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvPaceValue.rightMargin = getActualWidthOnThisDevice(40);
        etPaceUnit.setLayoutParams(paramsTvPaceValue);
        etPaceUnit.setId(View.generateViewId());

        rlPace.addView(etPace);
        RelativeLayout.LayoutParams paramsEtPace = (RelativeLayout.LayoutParams) etPace.getLayoutParams();
        paramsEtPace.width = getActualWidthOnThisDevice(360);
        paramsEtPace.height = MATCH_PARENT;
        paramsEtPace.addRule(RelativeLayout.CENTER_VERTICAL);
//        paramsEtPace.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsEtPace.addRule(RelativeLayout.LEFT_OF, etPaceUnit.getId());
//        paramsEtPace.rightMargin = getActualWidthOnThisDevice(10);
        etPace.setLayoutParams(paramsEtPace);

        rlPace.bringChildToFront(etPace);
    }

    @Override
    protected void setPropertyOfControls() {

        etHoleNoKey.setText(R.string.course_hole_setting_hole_no);
        etHoleNoKey.setTextColor(getColor(R.color.common_black));
        etHoleNoKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        etHoleNoKey.setBackground(null);
        etHoleNoKey.setEnabled(false);

        etHoleNoValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etHoleNoValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etHoleNoValue.setBackground(null);

        etHoleNoValue.setPadding(0, 0, 20, 0);

        etParKey.setText(R.string.course_setting_par);
        etParKey.setTextColor(getColor(R.color.common_black));
        etParKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        etParKey.setBackground(null);


        etParValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etParValue.setBackground(null);
        etParValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etParValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        etParValue.setPadding(0, 0, 20, 0);

        etYardsKey.setText(R.string.add_yards);
        etYardsKey.setTextColor(getColor(R.color.common_black));
        etYardsKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        etYardsKey.setBackground(null);

        etIndexKey.setText(R.string.add_index);
        etIndexKey.setTextColor(getColor(R.color.common_black));
        etIndexKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        etIndexKey.setBackground(null);


        etIndexValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etIndexValue.setBackground(null);
        etIndexValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etIndexValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        etIndexValue.setPadding(0, 0, 20, 0);
//        etIndexValue.setHint(R.string.add_index);//三通要求去掉2015.12.18

        etPaceKey.setText(R.string.course_hole_setting_pace);
        etPaceKey.setTextColor(getColor(R.color.common_black));
        etPaceKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        etPaceKey.setBackground(null);

        tvCalculate.setText(R.string.add_calculate);
        tvCalculate.setBackgroundResource(R.drawable.btn_blue_frame);
        tvCalculate.setGravity(Gravity.CENTER);
        tvCalculate.setTextColor(getColor(R.color.common_blue));


        etPace.setTextSize(Constants.FONT_SIZE_NORMAL);
        etPace.setBackground(null);
        etPace.setSingleLine();
        etPace.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etPace.setInputType(InputType.TYPE_CLASS_NUMBER);

        etPaceUnit.setText(R.string.common_min);
        etPaceUnit.setTextSize(Constants.FONT_SIZE_NORMAL);
        etPaceUnit.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etPaceUnit.setBackground(null);
        etPaceUnit.setEnabled(false);
        etPaceUnit.setTextColor(getColor(R.color.common_black));
        etPaceUnit.setPadding(0, 0, 20, 0);
        courseAreaId = (String) getArguments().get("courseAreaId");
        holeNO = (String) getArguments().get("HoleNO");
        holePar = (String) getArguments().get("holePar");
        sign = (String) getArguments().get("sign");

        etHoleNoValue.setText(holeNO);
        if (Constants.STR_0.equals(holePar)) {
            etParValue.setText(Constants.STR_EMPTY);
        } else {
            etParValue.setText(holePar);
        }
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.course_hole_setting_hole) + holeNO);
        getTvRight().setText(R.string.common_save);

        getTvRight().setOnClickListener(noDoubleClickListener);


        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {

                valuePar = etParValue.getText().toString();
                PaceValue = etPace.getText().toString();
                if(Constants.STR_0.equals(String.valueOf(valuePar))){
                    valuePar = "";
                }
                if(Constants.STR_0.equals(String.valueOf(holePar))){
                    holePar = "";
                }
                if(Constants.STR_0.equals(String.valueOf(PaceValue))){
                    PaceValue = "";
                }
                if(Constants.STR_0.equals(String.valueOf(pace))){
                    pace = "";
                }

                if (valuePar.equals(holePar) && PaceValue.equals(pace)) {
                    setOnBackListener(null);
                    getBaseActivity().doBack();
                } else {
                    selectOutOrInPopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    backSave();
                }


                return false;
            }
        });


    }

    @Override
    protected void executeOnceOnCreate() {
        super.executeOnceOnCreate();
        getCourseHoleMessage();
    }

    public void keyBoardDisappear() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etParValue.getWindowToken(), 0);
    }

    private void getCourseHoleMessage() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.COURSE_AREA_ID, courseAreaId);
        params.put(ApiKey.COURSE_HOLE_NO, holeNO);
        HttpManager<JsonTeeInfoList> hh = new HttpManager<JsonTeeInfoList>(CourseHoleSettingFragment.this) {

            @Override
            public void onJsonSuccess(JsonTeeInfoList jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {

                    teeUnit = jo.getDataList().teeUnit;
                    pace = jo.getDataList().pace.toString();
                    if (Constants.STR_0.equals(pace)) {
                        etPace.setText(Constants.STR_EMPTY);
                       // etPaceUnit.setVisibility(View.INVISIBLE);
                    } else {
                        etPace.setText(String.valueOf(pace));
                        etPaceUnit.setVisibility(View.VISIBLE);
                    }
                    etIndexValue.setText(jo.getDataList().getIndex().toString());
                    listCourseHole.addAll(jo.getDataList().dataInfoList);
//                    initLevels();
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.TeeInfoListGet, params);

    }

    @Override
    protected void reShowWithBackValue() {
        super.reShowWithBackValue();
        getCourseHoleMessage();
    }

    private void putCourseHole() {
        valuePar = etParValue.getText().toString();
        PaceValue = etPace.getText().toString();
        IndexValue = etIndexValue.getText().toString();
        String teeDate = getTeeString();

        if(null == PaceValue || "".equals(PaceValue)){
            Utils.showShortToast(getActivity(), R.string.please_enter_time);
            return;
        }

        if(!(Integer.parseInt(PaceValue) >= 1 && Integer.parseInt(PaceValue) <= 32767)){
            Utils.showShortToast(getActivity(), R.string.time_is_out_of_range);
            return;
        }

        if (!TextUtils.isEmpty(valuePar)) {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.COURSE_HOLE_NO, holeNO);
            params.put(ApiKey.COURSE_AREA_ID, courseAreaId);
            params.put(ApiKey.COURSE_PAR, valuePar);
            params.put(ApiKey.COURSE_INDEX, IndexValue);
            params.put(ApiKey.COURSE_PACE, PaceValue);
            params.put(ApiKey.COURSE_TEE_DATA, teeDate);

            HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CourseHoleSettingFragment.this) {

                @Override
                public void onJsonSuccess(BaseJsonObject jo) {
                    Integer returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    if (returnCode.equals(Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY)) {
                        Bundle bundle = new Bundle();
                        bundle.putString("sign", sign);
                        bundle.putString("par", etParValue.getText().toString());
                        doBackWithReturnValue(bundle, CourseSettingFragment.class);
                    } else {
                        Utils.showShortToast(getActivity(), msg);
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {

                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.EditDoHoleData, params);

        } else {
            Utils.showShortToast(getActivity(), R.string.par_is_not_null);
        }
    }

//    private void initLevels() {
//        for (int i = 0; i < listCourseHole.size(); i++) {
//
//            JsonTeeInfoList.DataList.DataInfoList level = listCourseHole.get(i);
//
//            final LayoutCourseTee rlLevelContainer = addLayoutCaddieLevel(level);
//
//            listTeeContainer.add(rlLevelContainer);
//        }
//    }

//    private LayoutCourseTee addLayoutCaddieLevel(final JsonTeeInfoList.DataList.DataInfoList tee) {
////        final LayoutCourseTee rlLevelContainer = new LayoutCourseTee(getActivity());
//
////        llCourseTeeListContainer.addView(rlLevelContainer);
//
////        LinearLayout.LayoutParams paramsLlLevelContainer = (LinearLayout.LayoutParams) rlLevelContainer.getLayoutParams();
////        paramsLlLevelContainer.width = ViewGroup.LayoutParams.MATCH_PARENT;
////        paramsLlLevelContainer.height = Constants.ROW_HEIGHT;
////        rlLevelContainer.setLayoutParams(paramsLlLevelContainer);
////
////
////        //场地名字
////        IteeTextView etCourseName = rlLevelContainer.getEdCourseName();
//
//        etCourseName.setText(tee.getTeeName());
//        etCourseName.setBackground(null);
//        etCourseName.setEnabled(false);
//        etCourseName.setGravity(Gravity.START);
//        etCourseName.setTextColor(getColor(R.color.common_black));
//
//        RelativeLayout.LayoutParams paramsEtTee = (RelativeLayout.LayoutParams) etCourseName.getLayoutParams();
//        paramsEtTee.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//        paramsEtTee.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramsEtTee.leftMargin = getActualWidthOnThisDevice(40);
//        paramsEtTee.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
//        paramsEtTee.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        etCourseName.setLayoutParams(paramsEtTee);
//
//        //所对应的码数
//        final IteeEditText etCourseYard = rlLevelContainer.getEtTeeNameValue();
//
//        final IteeTextView etUnit = rlLevelContainer.getEtYard();
//
//        if (Constants.STR_0.equals(String.valueOf(tee.getTeeYard()))) {
//            etCourseYard.setText(Constants.STR_EMPTY);
//           // etUnit.setText(Constants.STR_EMPTY);
//        } else {
//            etCourseYard.setText(String.valueOf(tee.getTeeYard()));
//
//        }
//
//
//        if (Utils.isStringNullOrEmpty(teeUnit)) {
//            etUnit.setText(teeUnit);
//        } else {
//            String unit = teeUnit.substring(0, 1).toUpperCase() + teeUnit.substring(1);
//            etUnit.setText(unit);
//        }
//
//        etCourseYard.setTextColor(getColor(R.color.common_gray));
//        etCourseYard.setInputType(InputType.TYPE_CLASS_NUMBER);
//        etCourseYard.setSingleLine();
//        etCourseYard.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
//        etCourseYard.setBackground(null);
//        etCourseYard.setPadding(0, 0, 40, 0);
//        etCourseYard.setTextColor(getColor(R.color.common_black));
//
//        etCourseYard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                if (b) {
//                    if (Constants.STR_0.equals(etCourseYard.getValue())) {
//                        etCourseYard.setText(Constants.STR_EMPTY);
//                    }
//                } else {
//                    if (Utils.isStringNullOrEmpty(teeUnit)) {
//                        etUnit.setText(teeUnit);
//                    } else {
//                        String unit = teeUnit.substring(0, 1).toUpperCase() + teeUnit.substring(1);
//                        etUnit.setText(unit);
//                    }
//                }
//            }
//        });
//
//        etUnit.setTextColor(getColor(R.color.common_gray));
////        if (Utils.isStringNullOrEmpty(teeUnit)) {
////            etUnit.setText(teeUnit);
////        } else {
////            String unit = teeUnit.substring(0, 1).toUpperCase() + teeUnit.substring(1);
////            etUnit.setText(unit);
////        }
//        etUnit.setTag(tee.getTeeId());
//        etUnit.setBackground(null);
//        etUnit.setTextColor(getColor(R.color.common_black));
//
//        RelativeLayout.LayoutParams paramsEtYard = (RelativeLayout.LayoutParams) etUnit.getLayoutParams();
//        paramsEtYard.width = getActualWidthOnThisDevice(100);
//        paramsEtYard.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramsEtYard.rightMargin = getActualWidthOnThisDevice(40);
//        paramsEtYard.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        paramsEtYard.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        etUnit.setLayoutParams(paramsEtYard);
//
//        RelativeLayout.LayoutParams paramsEtName = (RelativeLayout.LayoutParams) etCourseYard.getLayoutParams();
//        paramsEtName.width = getActualWidthOnThisDevice(360);
//        paramsEtName.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramsEtName.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        paramsEtName.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        paramsEtName.rightMargin = getActualWidthOnThisDevice(140);
//        etCourseYard.setLayoutParams(paramsEtName);
//
//        ImageView ivSeparator = new ImageView(getActivity());
//        ivSeparator.setBackgroundColor(getColor(R.color.common_separator_gray));
//        rlLevelContainer.addView(ivSeparator);
//
//        RelativeLayout.LayoutParams ivSeparatorLayoutParams = (RelativeLayout.LayoutParams) ivSeparator.getLayoutParams();
//        ivSeparatorLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        ivSeparatorLayoutParams.height = 1;
//        ivSeparatorLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, LAYOUT_TRUE);
//        ivSeparator.setLayoutParams(ivSeparatorLayoutParams);
//
//        return rlLevelContainer;
//    }

    private String getTeeString() {
        JSONArray arrEdit = new JSONArray();
        for (int i = 0; i < listTeeContainer.size(); i++) {
            try {
                LayoutCourseTee layoutCourseTee = listTeeContainer.get(i);
                IteeTextView courseName = layoutCourseTee.getEdCourseName();
                IteeTextView yard = layoutCourseTee.getEtYard();
                EditText courseYard = layoutCourseTee.getEtTeeNameValue();


                JSONObject jo = new JSONObject();
                jo.put(JsonKey.EDIT_HOLE_TEE_NAME, courseName.getText().toString());
                jo.put(JsonKey.EDIT_HOLE_TEE_ID, yard.getTag());
                jo.put(JsonKey.EDIT_HOLE_TEE_YARD, courseYard.getText().toString());

                arrEdit.put(jo);
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
        return arrEdit.toString();
    }

    class LayoutCourseTee extends RelativeLayout {

        private IteeTextView edCourseName;
        private IteeEditText etTeeNameValue;
        private IteeTextView etYard;

        public LayoutCourseTee(Context mContext) {
            super(mContext);

            edCourseName = new IteeTextView(mContext);
            etTeeNameValue = new IteeEditText(mContext);
            etYard = new IteeTextView(mContext);


            edCourseName.setId(View.generateViewId());
            etTeeNameValue.setId(View.generateViewId());
            etYard.setId(View.generateViewId());

            this.addView(edCourseName);
            this.addView(etTeeNameValue);
            this.addView(etYard);

            etYard.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
                @Override
                public void noDoubleClick(View v) {
                    etTeeNameValue.requestFocus();
                    Utils.showKeyboard(etTeeNameValue, getActivity());
                }
            });
        }

        public IteeTextView getEdCourseName() {
            return edCourseName;
        }

        public IteeEditText getEtTeeNameValue() {
            return etTeeNameValue;
        }

        public IteeTextView getEtYard() {
            return etYard;
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
                putCourseHole();
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

    public void choicePar(){
        selectOutOrInPopupWindow.btFirstValue.setText("3");
        selectOutOrInPopupWindow.btSecondValue.setText("4");
        selectOutOrInPopupWindow.btThirdValue.setText("5");
        selectOutOrInPopupWindow.btFirstValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOutOrInPopupWindow.dismiss();
                etParValue.setText("3");
            }
        });
        selectOutOrInPopupWindow.btSecondValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOutOrInPopupWindow.dismiss();
                etParValue.setText("4");
            }
        });
        selectOutOrInPopupWindow.btThirdValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOutOrInPopupWindow.dismiss();
                etParValue.setText("5");
            }
        });
    }

}
