/**
 * Project Name: itee
 * File Name:	 CaddiesLevelFragment.java
 * Package Name: cn.situne.itee.fragment.staff
 * Date:		 2015-03-03
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.fragment.staff;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonCaddieLevelGet;

/**
 * ClassName:CaddiesLevelFragment <br/>
 * Function: show level list of caddie <br/>
 * Date: 2015-03-03 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class CaddiesLevelFragment extends BaseFragment {

    private static final int ROW_HEIGHT = 100;

    private LinearLayout llCaddiesLevelListContainer;
    private RelativeLayout rlAddLevelContainer;

    private TextView tvAddLevel;

    private Integer courseId;

    private ArrayList<JsonCaddieLevelGet.Level> listCaddieLevel;
    private ArrayList<JsonCaddieLevelGet.Level> listDeletedLevel;

    private ArrayList<LayoutCaddieLevel> listLevelContainer;
    private ArrayList<LayoutCaddieLevel> listAddLevelContainer;

    private boolean isEdit;

    private AppUtils.NoDoubleClickListener noDoubleClickListener;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_staff_caddies_level;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        llCaddiesLevelListContainer = (LinearLayout) rootView.findViewById(R.id.ll_caddie_level_container);
        rlAddLevelContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_level_container);
        tvAddLevel = new TextView(getActivity());

        listCaddieLevel = new ArrayList<>();
        listLevelContainer = new ArrayList<>();
        listDeletedLevel = new ArrayList<>();
        listAddLevelContainer = new ArrayList<>();
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
                        putCaddieLevel();
                    }
                } else {
                    isEdit = true;
                    getTvRight().setBackground(null);
                    getTvRight().setText(R.string.common_save);
                    change2EditState();
                }
            }
        };

        tvAddLevel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (isEdit) {
                    JsonCaddieLevelGet.Level level = new JsonCaddieLevelGet.Level();
                    addLayoutCaddieLevel(level);
                }
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

        LinearLayout.LayoutParams rlAddLevelContainerLayoutParams = (LinearLayout.LayoutParams) rlAddLevelContainer.getLayoutParams();
        rlAddLevelContainerLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        rlAddLevelContainerLayoutParams.height = ROW_HEIGHT;
        rlAddLevelContainer.setLayoutParams(rlAddLevelContainerLayoutParams);

        rlAddLevelContainer.addView(tvAddLevel);

        RelativeLayout.LayoutParams tvAddLevelLayoutParams = (RelativeLayout.LayoutParams) tvAddLevel.getLayoutParams();
        tvAddLevelLayoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        tvAddLevelLayoutParams.width = (int) (getScreenWidth() * 0.5);
        tvAddLevelLayoutParams.leftMargin = 40;
        tvAddLevel.setLayoutParams(tvAddLevelLayoutParams);
    }

    @Override
    protected void setPropertyOfControls() {

        tvAddLevel.setText(R.string.staff_add_level);
        tvAddLevel.setTextSize(Constants.FONT_SIZE_LARGER);
        tvAddLevel.setTextColor(getColor(R.color.common_fleet_blue));
        tvAddLevel.setGravity(Gravity.CENTER_VERTICAL);

        getCaddieLevel();
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.staff_level);
        if (isEdit) {
            getTvRight().setBackground(null);
            getTvRight().setText(R.string.common_save);
        } else {
            getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
            getTvRight().setText(Constants.STR_EMPTY);
        }
        getTvRight().setOnClickListener(noDoubleClickListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            courseId = bundle.getInt(TransKey.STAFF_COURSE_ID);

        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void getCaddieLevel() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_COURES_ID, String.valueOf(courseId));
        HttpManager<JsonCaddieLevelGet> hh = new HttpManager<JsonCaddieLevelGet>(CaddiesLevelFragment.this) {

            @Override
            public void onJsonSuccess(JsonCaddieLevelGet jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    listCaddieLevel.addAll(jo.getDataList());
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
        hh.startGet(getBaseActivity(), ApiManager.HttpApi.StaffCaddieLevelGet, params);
    }

    private void putCaddieLevel() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_COURES_ID, String.valueOf(courseId));
        params.put(ApiKey.STAFF_AUTH_CODE, getLevelString());

        Log.i("--staff auth code", params.toString());

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CaddiesLevelFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    doBack();
                } else {
                    Utils.showShortToast(getBaseActivity(), msg);
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
        hh.startPut(getBaseActivity(), ApiManager.HttpApi.StaffCaddieLevelPut, params);
    }

    private void initLevels() {
        for (int i = 0; i < listCaddieLevel.size(); i++) {

            JsonCaddieLevelGet.Level level = listCaddieLevel.get(i);

            final LayoutCaddieLevel rlLevelContainer = addLayoutCaddieLevel(level);

            listLevelContainer.add(rlLevelContainer);
        }
    }

    private LayoutCaddieLevel addLayoutCaddieLevel(final JsonCaddieLevelGet.Level level) {
        final LayoutCaddieLevel rlLevelContainer = new LayoutCaddieLevel(getActivity());

        llCaddiesLevelListContainer.addView(rlLevelContainer);

        LinearLayout.LayoutParams paramsLlLevelContainer = (LinearLayout.LayoutParams) rlLevelContainer.getLayoutParams();
        paramsLlLevelContainer.width = ViewGroup.LayoutParams.MATCH_PARENT;
        paramsLlLevelContainer.height = ROW_HEIGHT;
        rlLevelContainer.setLayoutParams(paramsLlLevelContainer);

        ImageView ivDelete = rlLevelContainer.getIvDelete();
        ivDelete.setImageResource(R.drawable.icon_delete);
        ivDelete.setTag(level.getLevId());
        ivDelete.setVisibility(View.INVISIBLE);
        ivDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                llCaddiesLevelListContainer.removeView(rlLevelContainer);
                listLevelContainer.remove(rlLevelContainer);
                if (level.getLevId() > 0) {
                    listDeletedLevel.add(level);
                }
                listAddLevelContainer.remove(rlLevelContainer);
                listCaddieLevel.remove(level);
            }
        });

        RelativeLayout.LayoutParams paramsIvDelete = (RelativeLayout.LayoutParams) ivDelete.getLayoutParams();
        paramsIvDelete.width = 40;
        paramsIvDelete.height = 40;
        paramsIvDelete.leftMargin = 40;
        paramsIvDelete.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsIvDelete.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivDelete.setLayoutParams(paramsIvDelete);

        EditText etLevelValue = rlLevelContainer.getEtLevelValue();
        etLevelValue.setText(level.getLevName());
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
        if (level.getLevId() == 0) {
            etLevelValue.setEnabled(true);
            ivDelete.setVisibility(View.VISIBLE);
            listAddLevelContainer.add(rlLevelContainer);
        }
        return rlLevelContainer;
    }

    private void change2EditState() {
        for (LayoutCaddieLevel layoutCaddieLevel : listLevelContainer) {
            ImageView ivDelete = layoutCaddieLevel.getIvDelete();
            EditText etLevelValue = layoutCaddieLevel.getEtLevelValue();
            ivDelete.setVisibility(View.VISIBLE);
            etLevelValue.setEnabled(true);
        }
    }

    private String getLevelString() {
        JSONArray res = new JSONArray();
        JSONArray arrEdit = new JSONArray();
        JSONArray arrAdd = new JSONArray();
        JSONObject joDel = new JSONObject();
        StringBuilder sbDelete = new StringBuilder();
        for (int i = 0; i < listDeletedLevel.size(); i++) {
            JsonCaddieLevelGet.Level level = listDeletedLevel.get(i);
            if (level.getLevId() != 0) {
                if (i != 0) {
                    sbDelete.append(Constants.STR_COMMA);
                }
                sbDelete.append(level.getLevId());
            }
        }
        for (int i = 0; i < listLevelContainer.size(); i++) {
            try {
                LayoutCaddieLevel layoutCaddieLevel = listLevelContainer.get(i);
                ImageView ivDelete = layoutCaddieLevel.getIvDelete();
                EditText etLevelValue = layoutCaddieLevel.getEtLevelValue();
                int levelId = (int) ivDelete.getTag();
                JSONObject jo = new JSONObject();
                if (levelId != 0) {
                    jo.put(ApiKey.STAFF_CADDIE_LEVEL_ID, levelId);
                    jo.put(ApiKey.STAFF_CADDIE_LEVEL_NAME, etLevelValue.getText().toString());
                    arrEdit.put(jo);
                }
            } catch (JSONException e) {
                Utils.log(e.getMessage());
            }
        }
        for (int i = 0; i < listAddLevelContainer.size(); i++) {
            LayoutCaddieLevel layoutCaddieLevel = listAddLevelContainer.get(i);
            ImageView ivDelete = layoutCaddieLevel.getIvDelete();
            EditText etLevelValue = layoutCaddieLevel.getEtLevelValue();

            int levelId = (int) ivDelete.getTag();
            if (levelId == 0) {
                JSONObject jo = new JSONObject();
                try {
                    jo.put(ApiKey.STAFF_CADDIE_LEVEL_NAME, etLevelValue.getText().toString());
                    arrAdd.put(jo);
                } catch (JSONException e) {
                    Utils.log(e.getMessage());
                }
            }
        }

        try {
            JSONObject jo = new JSONObject();
            joDel.put(ApiKey.STAFF_CADDIE_LEVEL_ID, sbDelete.toString());
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
        for (LayoutCaddieLevel layoutCaddieLevel : listLevelContainer) {
            EditText etLevelValue = layoutCaddieLevel.getEtLevelValue();
            if (Utils.isStringNullOrEmpty(etLevelValue.getText().toString())) {
                Utils.showShortToast(getActivity(), String.format(getString(R.string.common_must_be_filled_in), R.string.staff_level));
                return false;
            }
        }

        for (LayoutCaddieLevel layoutCaddieLevel : listAddLevelContainer) {
            EditText etLevelValue = layoutCaddieLevel.getEtLevelValue();
            if (Utils.isStringNullOrEmpty(etLevelValue.getText().toString())) {
                Utils.showShortToast(getActivity(), String.format(getString(R.string.common_must_be_filled_in), R.string.staff_level));
                return false;
            }
        }
        return true;
    }

    class LayoutCaddieLevel extends RelativeLayout {

        private ImageView ivDelete;
        private EditText etLevelValue;

        public LayoutCaddieLevel(Context mContext) {
            super(mContext);

            ivDelete = new ImageView(mContext);
            etLevelValue = new EditText(mContext);

            ivDelete.setId(View.generateViewId());
            etLevelValue.setId(View.generateViewId());

            this.addView(ivDelete);
            this.addView(etLevelValue);
        }

        public ImageView getIvDelete() {
            return ivDelete;
        }

        public EditText getEtLevelValue() {
            return etLevelValue;
        }

    }
}
