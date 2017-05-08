/**
 * Project Name: itee
 * File Name:	 CourseTeesFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-01-20
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonTeeNameList;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectOutOrInPopupWindow;

/**
 * ClassName:CourseTeesFragment <br/>
 * Function: hole setting. <br/>
 * UI:  03-7-3
 * Date: 2015-01-20 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CourseTeesFragment extends BaseFragment {

    private LinearLayout llCourseTeeListContainer;
    private RelativeLayout rlAddTeeContainer;

    private IteeTextView tvAddTee;

    private ArrayList<JsonTeeNameList.DataList> listCaddieTee;
    private ArrayList<JsonTeeNameList.DataList> listDeletedTee;

    private ArrayList<LayoutCourseTee> listTeeContainer;
    private ArrayList<LayoutCourseTee> listAddTeeContainer;

    private boolean isEdit;
    private boolean isAdd;

    private List<JsonTeeNameList.DataList> dataList;
    private SelectOutOrInPopupWindow selectOutOrInPopupWindow;
    private AppUtils.NoDoubleClickListener noDoubleClickListener;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_course_tee;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            isAdd = bundle.getBoolean(TransKey.COURSE_IS_ADD);
        }

        selectOutOrInPopupWindow = new SelectOutOrInPopupWindow(getActivity(), null);
        llCourseTeeListContainer = (LinearLayout) rootView.findViewById(R.id.ll_course_tee_container);
        rlAddTeeContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_tee_container);
        tvAddTee = new IteeTextView(getActivity());

        listCaddieTee = new ArrayList<>();
        listTeeContainer = new ArrayList<>();
        listDeletedTee = new ArrayList<>();
        listAddTeeContainer = new ArrayList<>();
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

        noDoubleClickListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                if (isEdit) {
                    Utils.hideKeyboard(getBaseActivity());
                    if (doCheck()) {
                        putCaddieTee();
                    }
                } else {
                    isEdit = true;
                    getTvRight().setBackground(null);
                    getTvRight().setText(R.string.common_save);
                    change2EditState();
                }
            }
        };

        tvAddTee.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isEdit) {
                    JsonTeeNameList.DataList tee = new JsonTeeNameList.DataList();
                    addLayoutCaddieLevel(tee);
                }
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

        LinearLayout.LayoutParams rlAddLevelContainerLayoutParams = (LinearLayout.LayoutParams) rlAddTeeContainer.getLayoutParams();
        rlAddLevelContainerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rlAddLevelContainerLayoutParams.height = Constants.ROW_HEIGHT;
        rlAddTeeContainer.setLayoutParams(rlAddLevelContainerLayoutParams);

        rlAddTeeContainer.addView(tvAddTee);

        RelativeLayout.LayoutParams tvAddLevelLayoutParams = (RelativeLayout.LayoutParams) tvAddTee.getLayoutParams();
        tvAddLevelLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        tvAddLevelLayoutParams.width = (int) (getScreenWidth() * 0.5);
        tvAddLevelLayoutParams.leftMargin = 40;
        tvAddTee.setLayoutParams(tvAddLevelLayoutParams);
    }

    @Override
    protected void setPropertyOfControls() {

        tvAddTee.setText(R.string.course_add_tee);
        tvAddTee.setTextSize(Constants.FONT_SIZE_LARGER);
        tvAddTee.setTextColor(getColor(R.color.common_fleet_blue));
        tvAddTee.setGravity(Gravity.CENTER_VERTICAL);

        getCaddieLevel();
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.course_tee);
        if (isEdit) {
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_save);
        } else {
            getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
            getTvRight().setText(Constants.STR_EMPTY);
        }
        getTvRight().setOnClickListener(noDoubleClickListener);

        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {

                if ((getEditCourseTeeString() + getAddCourseTeeString()).contentEquals(getBeginCourseTeeString())) {
                    setOnBackListener(null);
                    getBaseActivity().doBack();
                } else {
                    selectOutOrInPopupWindow.showAtLocation(CourseTeesFragment.this.getRootView().findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    backSave();
                }
                return false;
            }
        });
    }

    private void getCaddieLevel() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        HttpManager<JsonTeeNameList> hh = new HttpManager<JsonTeeNameList>(CourseTeesFragment.this) {

            @Override
            public void onJsonSuccess(JsonTeeNameList jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    listCaddieTee.addAll(jo.getDataList());
                    dataList = jo.getDataList();
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
        hh.startGet(getBaseActivity(), ApiManager.HttpApi.TeeNameListGet, params);
    }

    private void putCaddieTee() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.COURSE_TEE_CODE, getTeeString());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CourseTeesFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getBaseActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    if (isAdd) {
                        setOnBackListener(null);
                        doBack();
                        doBack();
                    } else {
                        setOnBackListener(null);
                        doBack();
                    }

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
        hh.start(getBaseActivity(), ApiManager.HttpApi.TeeNameEditPost, params);
    }

    private void putBackCaddieTee() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.COURSE_TEE_CODE, getTeeString());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CourseTeesFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getBaseActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    setOnBackListener(null);
                    getBaseActivity().doBack();

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
        hh.start(getBaseActivity(), ApiManager.HttpApi.TeeNameEditPost, params);
    }

    private void initLevels() {
        for (int i = 0; i < listCaddieTee.size(); i++) {

            JsonTeeNameList.DataList level = listCaddieTee.get(i);

            final LayoutCourseTee rlLevelContainer = addLayoutCaddieLevel(level);

            listTeeContainer.add(rlLevelContainer);
        }
    }

    private LayoutCourseTee addLayoutCaddieLevel(final JsonTeeNameList.DataList level) {
        final LayoutCourseTee rlLevelContainer = new LayoutCourseTee(getActivity());

        llCourseTeeListContainer.addView(rlLevelContainer);

        LinearLayout.LayoutParams paramsLlLevelContainer = (LinearLayout.LayoutParams) rlLevelContainer.getLayoutParams();
        paramsLlLevelContainer.width = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsLlLevelContainer.height = Constants.ROW_HEIGHT;
        rlLevelContainer.setLayoutParams(paramsLlLevelContainer);

        ImageView ivDelete = rlLevelContainer.getIvDelete();
        ivDelete.setImageResource(R.drawable.icon_delete);
        ivDelete.setTag(level.getTeeId());
        ivDelete.setVisibility(View.INVISIBLE);
        ivDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppUtils.showDeleteAlert(CourseTeesFragment.this, new AppUtils.DeleteConfirmListener() {
                    @Override
                    public void onClickDelete() {
                        llCourseTeeListContainer.removeView(rlLevelContainer);
                        listTeeContainer.remove(rlLevelContainer);
                        listDeletedTee.add(level);
                        listCaddieTee.remove(level);
                        listAddTeeContainer.remove(rlLevelContainer);
                    }
                });
            }
        });

        RelativeLayout.LayoutParams paramsIvDelete = (RelativeLayout.LayoutParams) ivDelete.getLayoutParams();
        paramsIvDelete.width = 40;
        paramsIvDelete.height = 40;
        paramsIvDelete.leftMargin = 40;
        paramsIvDelete.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsIvDelete.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivDelete.setLayoutParams(paramsIvDelete);

        EditText etLevelValue = rlLevelContainer.getEtTeeNameValue();
        etLevelValue.setText(level.getTeeName());
        etLevelValue.setEnabled(false);
        etLevelValue.setBackground(null);
        etLevelValue.setPadding(0, 6, 0, 0);
        etLevelValue.setSingleLine();
        etLevelValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);

        RelativeLayout.LayoutParams paramsEtLevelValue = (RelativeLayout.LayoutParams) etLevelValue.getLayoutParams();
        paramsEtLevelValue.width = (int) (getScreenWidth() * 0.5);
        paramsEtLevelValue.height = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsEtLevelValue.leftMargin = 20;
        paramsEtLevelValue.addRule(RelativeLayout.RIGHT_OF, ivDelete.getId());
        paramsEtLevelValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        etLevelValue.setLayoutParams(paramsEtLevelValue);

        ImageView ivSeparator = new ImageView(getActivity());
        ivSeparator.setBackgroundColor(getColor(R.color.common_separator_gray));
        rlLevelContainer.addView(ivSeparator);

        RelativeLayout.LayoutParams ivSeparatorLayoutParams = (RelativeLayout.LayoutParams) ivSeparator.getLayoutParams();
        ivSeparatorLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        ivSeparatorLayoutParams.height = 1;
        ivSeparatorLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, LAYOUT_TRUE);
        ivSeparator.setLayoutParams(ivSeparatorLayoutParams);

        // add new level
        if (level.getTeeId() == 0) {
            etLevelValue.setEnabled(true);
            ivDelete.setVisibility(View.VISIBLE);
            listAddTeeContainer.add(rlLevelContainer);
        }
        return rlLevelContainer;
    }

    private void change2EditState() {
        for (LayoutCourseTee layoutCourseTee : listTeeContainer) {
            ImageView ivDelete = layoutCourseTee.getIvDelete();
            EditText etTeeValue = layoutCourseTee.getEtTeeNameValue();
            ivDelete.setVisibility(View.VISIBLE);
            etTeeValue.setEnabled(true);
        }
    }

    private String getBeginCourseTeeString() {
        JSONArray jsonArray = new JSONArray();
        if (dataList != null) {
            for (int j = 0; j < dataList.size(); j++) {
                Integer teeId = dataList.get(j).getTeeId();
                String teeName = dataList.get(j).getTeeName();
                Map<String, Object> map = new HashMap<>();
                map.put(JsonKey.EDIT_HOLE_TEE_ID, teeId);
                map.put(JsonKey.EDIT_HOLE_TEE_NAME, teeName);
                JSONObject object = new JSONObject(map);
                jsonArray.put(object);

            }
        }
        return jsonArray.toString();
    }

    private String getEditCourseTeeString() {
        JSONArray arrEdit = new JSONArray();
        for (int i = 0; i < listTeeContainer.size(); i++) {
            try {
                LayoutCourseTee layoutCourseTee = listTeeContainer.get(i);
                ImageView ivDelete = layoutCourseTee.getIvDelete();
                EditText etLevelValue = layoutCourseTee.getEtTeeNameValue();
                int levelId = (int) ivDelete.getTag();
                JSONObject jo = new JSONObject();
                if (levelId != 0) {
                    jo.put(JsonKey.EDIT_HOLE_TEE_ID, levelId);
                    jo.put(JsonKey.EDIT_HOLE_TEE_NAME, etLevelValue.getText().toString());
                    arrEdit.put(jo);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
        return arrEdit.toString();
    }

    public String getAddCourseTeeString() {
        JSONArray arrAdd = new JSONArray();
        if (listAddTeeContainer.size() == 0) {
            return StringUtils.EMPTY;
        } else {
            for (int i = 0; i < listAddTeeContainer.size(); i++) {
                LayoutCourseTee layoutCourseTee = listAddTeeContainer.get(i);
                ImageView ivDelete = layoutCourseTee.getIvDelete();
                EditText etLevelValue = layoutCourseTee.getEtTeeNameValue();

                int levelId = (int) ivDelete.getTag();
                if (levelId == 0) {
                    JSONObject jo = new JSONObject();
                    try {
                        jo.put(JsonKey.EDIT_HOLE_TEE_NAME, etLevelValue.getText().toString());
                        arrAdd.put(jo);
                    } catch (JSONException e) {
                        Utils.log(e.getMessage());
                    }
                }
            }
        }

        return arrAdd.toString();
    }

    private String getTeeString() {
        JSONArray res = new JSONArray();
        JSONArray arrEdit = new JSONArray();
        JSONArray arrAdd = new JSONArray();
        JSONObject joDel = new JSONObject();
        StringBuilder sbDelete = new StringBuilder();
        for (int i = 0; i < listDeletedTee.size(); i++) {
            JsonTeeNameList.DataList Tee = listDeletedTee.get(i);
            if (Tee.getTeeId() != 0) {
                if (i != 0) {
                    sbDelete.append(Constants.STR_COMMA);
                }
                sbDelete.append(Tee.getTeeId());
            }
        }
        for (int i = 0; i < listTeeContainer.size(); i++) {
            try {
                LayoutCourseTee layoutCourseTee = listTeeContainer.get(i);
                ImageView ivDelete = layoutCourseTee.getIvDelete();
                EditText etLevelValue = layoutCourseTee.getEtTeeNameValue();
                int levelId = (int) ivDelete.getTag();
                JSONObject jo = new JSONObject();
                if (levelId != 0) {
                    jo.put(JsonKey.EDIT_HOLE_TEE_ID, levelId);
                    jo.put(JsonKey.EDIT_HOLE_TEE_NAME, etLevelValue.getText().toString());
                    arrEdit.put(jo);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
        for (int i = 0; i < listAddTeeContainer.size(); i++) {
            LayoutCourseTee layoutCourseTee = listAddTeeContainer.get(i);
            ImageView ivDelete = layoutCourseTee.getIvDelete();
            EditText etLevelValue = layoutCourseTee.getEtTeeNameValue();

            int levelId = (int) ivDelete.getTag();
            if (levelId == 0) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put(JsonKey.EDIT_HOLE_TEE_NAME, etLevelValue.getText().toString());
                    arrAdd.put(jo);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
            }
        }

        try {
            JSONObject jo = new JSONObject();
            joDel.put(JsonKey.EDIT_HOLE_TEE_ID, sbDelete.toString());
            jo.put("del", joDel);
            jo.put("add", arrAdd);
            jo.put("edit", arrEdit);
            res.put(jo);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return res.toString();
    }

    private boolean doCheck() {
        boolean res = true;
        for (LayoutCourseTee layoutCourseTee : listTeeContainer) {
            EditText etTeeValue = layoutCourseTee.getEtTeeNameValue();
            if (Utils.isStringNullOrEmpty(etTeeValue.getText().toString())) {
                res = false;
                Utils.showShortToast(getActivity(), R.string.course_fill_tee_name);
            }
        }

        for (LayoutCourseTee layoutCourseTee : listAddTeeContainer) {
            EditText etTeeValue = layoutCourseTee.getEtTeeNameValue();
            if (Utils.isStringNullOrEmpty(etTeeValue.getText().toString())) {
                res = false;
                Utils.showShortToast(getActivity(), R.string.course_fill_tee_name);
            }
        }

        return res;
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
                if (doCheck()) {
                    putBackCaddieTee();
                }

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

    class LayoutCourseTee extends RelativeLayout {

        private ImageView ivDelete;
        private EditText etTeeNameValue;

        public LayoutCourseTee(Context mContext) {
            super(mContext);

            ivDelete = new ImageView(mContext);
            etTeeNameValue = new EditText(mContext);

            ivDelete.setId(View.generateViewId());
            etTeeNameValue.setId(View.generateViewId());

            this.addView(ivDelete);
            this.addView(etTeeNameValue);
        }

        public ImageView getIvDelete() {
            return ivDelete;
        }

        public EditText getEtTeeNameValue() {
            return etTeeNameValue;
        }

    }

}
