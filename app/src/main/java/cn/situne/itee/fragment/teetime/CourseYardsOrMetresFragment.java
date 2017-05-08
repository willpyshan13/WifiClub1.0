package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonTeeInfoList;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectOutOrInPopupWindow;

/**
 * Created by xuyue on 2015/12/09.
 */
public class CourseYardsOrMetresFragment extends BaseFragment {

    private LinearLayout llCourseTeeListContainer;
    private SelectOutOrInPopupWindow selectOutOrInPopupWindow;

    private AppUtils.NoDoubleClickListener noDoubleClickListener;

    private ArrayList<JsonTeeInfoList.DataList.DataInfoList> listCourseHole;
    private ArrayList<LayoutCourseTee> listTeeContainer;

    private String courseAreaId;
    private String holeNO;
    private String holePar;
    private String sign;
    private String teeUnit;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_course_yards;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        selectOutOrInPopupWindow = new SelectOutOrInPopupWindow(getActivity(), null);
        llCourseTeeListContainer = (LinearLayout) rootView.findViewById(R.id.ll_course_tee_container);

        listCourseHole = new ArrayList<>();
        listTeeContainer = new ArrayList<>();
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        noDoubleClickListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Utils.hideKeyboard(getActivity());
                putCourseHole();
            }
        };
//        bundle.putString(TransKey.COMMON_FROM_PAGE, CourseSettingFragment.class.getName());
//        Bundle bundle = new Bundle();
//        bundle.putString("sign", sign);
//        bundle.putString("par", etParValue.getText().toString());
//        doBackWithReturnValue(bundle, CourseSettingFragment.class);

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        courseAreaId = (String) getArguments().get("courseAreaId");
        holeNO = (String) getArguments().get("HoleNO");
        holePar = (String) getArguments().get("holePar");
        sign = (String) getArguments().get("sign");
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.add_yards));
        getTvRight().setText(R.string.common_save);

        getTvRight().setOnClickListener(noDoubleClickListener);

        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {
                if (!haveChange()) {
                    setOnBackListener(null);
                    getBaseActivity().doBackWithRefresh();
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

    private void getCourseHoleMessage() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.COURSE_AREA_ID, courseAreaId);
        params.put(ApiKey.COURSE_HOLE_NO, holeNO);
        HttpManager<JsonTeeInfoList> hh = new HttpManager<JsonTeeInfoList>(CourseYardsOrMetresFragment.this) {

            @Override
            public void onJsonSuccess(JsonTeeInfoList jo) {
                int returnCode = jo.getReturnCode();
                teeUnit = jo.getDataList().teeUnit;
                if (returnCode == Constants.RETURN_CODE_20301) {

//                    teeUnit = jo.getDataList().teeUnit;
//                    pace = jo.getDataList().pace.toString();
//                    if (Constants.STR_0.equals(pace)) {
//                        etPace.setText(Constants.STR_EMPTY);
//                        // etPaceUnit.setVisibility(View.INVISIBLE);
//                    } else {
//                        etPace.setText(String.valueOf(pace));
//                        etPaceUnit.setVisibility(View.VISIBLE);
//                    }
//                    etIndexValue.setText(jo.getDataList().getIndex().toString());
                    listCourseHole.addAll(jo.getDataList().dataInfoList);
                    initLevels();
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

    private void initLevels() {
        for (int i = 0; i < listCourseHole.size(); i++) {
            JsonTeeInfoList.DataList.DataInfoList level = listCourseHole.get(i);
            final LayoutCourseTee rlLevelContainer = addLayoutCaddieLevel(level);
            listTeeContainer.add(rlLevelContainer);
        }
    }

    private LayoutCourseTee addLayoutCaddieLevel(final JsonTeeInfoList.DataList.DataInfoList tee) {
        final LayoutCourseTee rlLevelContainer = new LayoutCourseTee(getActivity());

        llCourseTeeListContainer.addView(rlLevelContainer);

        LinearLayout.LayoutParams paramsLlLevelContainer = (LinearLayout.LayoutParams) rlLevelContainer.getLayoutParams();
        paramsLlLevelContainer.width = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsLlLevelContainer.height = getActualHeightOnThisDevice(100);
        rlLevelContainer.setLayoutParams(paramsLlLevelContainer);

//        //场地名字
        IteeTextView etCourseName = rlLevelContainer.getEdCourseName();

        etCourseName.setText(tee.getTeeName());
        etCourseName.setBackground(null);
        etCourseName.setEnabled(false);
        etCourseName.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
        etCourseName.setTextColor(getColor(R.color.common_black));

        RelativeLayout.LayoutParams paramsEtTee = (RelativeLayout.LayoutParams) etCourseName.getLayoutParams();
        paramsEtTee.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtTee.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsEtTee.leftMargin = getActualWidthOnThisDevice(40);
        paramsEtTee.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsEtTee.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        etCourseName.setLayoutParams(paramsEtTee);

        //所对应的码数
        final IteeEditText etCourseYard = rlLevelContainer.getEtTeeNameValue();
        String unit = AppUtils.getUnit(getActivity()).substring(0, 1).toUpperCase() + AppUtils.getUnit(getActivity()).substring(1);
        etCourseYard.setHint(unit);
//        if (Utils.isStringNullOrEmpty(teeUnit)) {
//            etCourseYard.setHint(AppUtils.getUnit(getActivity()));
//        } else {
//            String unit = teeUnit.substring(0, 1).toUpperCase() + teeUnit.substring(1);
//            etCourseYard.setHint(unit);
//        }

//        final IteeTextView etUnit = rlLevelContainer.getEtYard();

        if (Constants.STR_0.equals(String.valueOf(tee.getTeeYard()))) {
            etCourseYard.setText(Constants.STR_EMPTY);
           // etUnit.setText(Constants.STR_EMPTY);
        } else {
            etCourseYard.setText(String.valueOf(tee.getTeeYard()));

        }

//        AppUtils.getUnit(getActivity());
//        if (Utils.isStringNullOrEmpty(teeUnit)) {
//            etUnit.setText(teeUnit);
//        } else {
//            String unit = teeUnit.substring(0, 1).toUpperCase() + teeUnit.substring(1);
//            etUnit.setText(unit);
//        }

        etCourseYard.setTextColor(getColor(R.color.common_gray));
        etCourseYard.setInputType(InputType.TYPE_CLASS_NUMBER);
        etCourseYard.setSingleLine();
        etCourseYard.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etCourseYard.setBackground(null);
//        etCourseYard.setPadding(0, 0, 40, 0);
        etCourseYard.setTextColor(getColor(R.color.common_black));

        etCourseYard.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    if (Constants.STR_0.equals(etCourseYard.getValue())) {
                        etCourseYard.setText(Constants.STR_EMPTY);
                    }
                } else {
                    String unit = AppUtils.getUnit(getActivity()).substring(0, 1).toUpperCase() + AppUtils.getUnit(getActivity()).substring(1);
                    etCourseYard.setHint(unit);
//                    if (Utils.isStringNullOrEmpty(teeUnit)) {
//                        etCourseYard.setHint(AppUtils.getUnit(getActivity()));
//                    } else {
//                        String unit = teeUnit.substring(0, 1).toUpperCase() + teeUnit.substring(1);
//                        etCourseYard.setHint(unit);
//                    }
                }
            }
        });

//        etUnit.setTextColor(getColor(R.color.common_gray));
//        if (Utils.isStringNullOrEmpty(teeUnit)) {
//            etUnit.setText(teeUnit);
//        } else {
//            String unit = teeUnit.substring(0, 1).toUpperCase() + teeUnit.substring(1);
//            etUnit.setText(unit);
//        }
//        etUnit.setTag(tee.getTeeId());
//        etUnit.setBackground(null);
//        etUnit.setTextColor(getColor(R.color.common_black));

//        RelativeLayout.LayoutParams paramsEtYard = (RelativeLayout.LayoutParams) etUnit.getLayoutParams();
//        paramsEtYard.width = getActualWidthOnThisDevice(100);
//        paramsEtYard.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//        paramsEtYard.rightMargin = getActualWidthOnThisDevice(40);
//        paramsEtYard.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
//        paramsEtYard.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
//        etUnit.setLayoutParams(paramsEtYard);

        RelativeLayout.LayoutParams paramsEtName = (RelativeLayout.LayoutParams) etCourseYard.getLayoutParams();
        paramsEtName.width = getActualWidthOnThisDevice(360);
        paramsEtName.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsEtName.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsEtName.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsEtName.rightMargin = getActualWidthOnThisDevice(40);
        etCourseYard.setLayoutParams(paramsEtName);

        ImageView ivSeparator = new ImageView(getActivity());
        ivSeparator.setBackgroundColor(getColor(R.color.common_separator_gray));
        rlLevelContainer.addView(ivSeparator);

        RelativeLayout.LayoutParams ivSeparatorLayoutParams = (RelativeLayout.LayoutParams) ivSeparator.getLayoutParams();
        ivSeparatorLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        ivSeparatorLayoutParams.height = 1;
        ivSeparatorLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, LAYOUT_TRUE);
        ivSeparator.setLayoutParams(ivSeparatorLayoutParams);

        return rlLevelContainer;
    }

    class LayoutCourseTee extends RelativeLayout {

        private IteeTextView edCourseName;
        private IteeEditText etTeeNameValue;

        public LayoutCourseTee(Context mContext) {
            super(mContext);

            edCourseName = new IteeTextView(mContext);
            etTeeNameValue = new IteeEditText(mContext);

            edCourseName.setId(View.generateViewId());
            etTeeNameValue.setId(View.generateViewId());

            etTeeNameValue.setHint(getString(R.string.add_yards));

            this.addView(edCourseName);
            this.addView(etTeeNameValue);
        }

        public IteeTextView getEdCourseName() {
            return edCourseName;
        }

        public IteeEditText getEtTeeNameValue() {
            return etTeeNameValue;
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
//                getBaseActivity().doBack();
                getBaseActivity().doBackWithRefresh();
            }
        });
    }

    private String getTeeString() {
        JSONArray arrEdit = new JSONArray();
        for (int i = 0; i < listTeeContainer.size(); i++) {
            try {
                LayoutCourseTee layoutCourseTee = listTeeContainer.get(i);
                IteeTextView courseName = layoutCourseTee.getEdCourseName();
                EditText courseYard = layoutCourseTee.getEtTeeNameValue();

                JSONObject jo = new JSONObject();
                jo.put(JsonKey.EDIT_HOLE_TEE_NAME, courseName.getText().toString());
                jo.put(JsonKey.EDIT_HOLE_TEE_YARD, courseYard.getText().toString());
                jo.put(JsonKey.EDIT_HOLE_TEE_ID, listCourseHole.get(i).getTeeId());

                arrEdit.put(jo);
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
        return arrEdit.toString();
    }

    private boolean haveChange() {
        for (int i = 0; i < listTeeContainer.size(); i++) {
            LayoutCourseTee layoutCourseTee = listTeeContainer.get(i);
            EditText courseYard = layoutCourseTee.getEtTeeNameValue();
            String oldYard = listCourseHole.get(i).getTeeYard().toString();
            String yard = courseYard.getText().toString();
            if(Constants.STR_0.equals(String.valueOf(oldYard))){
                oldYard = "";
            }
            if(Constants.STR_0.equals(String.valueOf(yard))){
                yard = "";
            }
            if (!oldYard.equals(yard)) {
                return true;
            }
        }
        return false;
    }

    private void putCourseHole() {
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.COURSE_HOLE_NO, holeNO);
            params.put(ApiKey.COURSE_AREA_ID, courseAreaId);
            params.put(ApiKey.COURSE_TEE_DATA, getTeeString());

            HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CourseYardsOrMetresFragment.this) {

                @Override
                public void onJsonSuccess(BaseJsonObject jo) {
                    Integer returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    if (returnCode.equals(Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY)) {
                        doBackWithRefresh();
//                        Bundle bundle = new Bundle();
//                        bundle.putString("sign", sign);
//                        bundle.putString("par", etParValue.getText().toString());
//                        doBackWithReturnValue(bundle, CourseSettingFragment.class);
                    } else {
                        Utils.showShortToast(getActivity(), msg);
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {

                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.EditDoYards, params);
    }
}
