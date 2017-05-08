/**
 * Project Name: itee
 * File Name:  GreenFragment.java
 * Package Name: cn.situne.itee.fragment.shops
 * Date:   2015-03-05
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.shops;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

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
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.CourseAddFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonAgentsPricingListGet;
import cn.situne.itee.manager.jsonentity.JsonGreenFeeGet;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeGreenMoneyEditText;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:GreenFragment <br/>
 * Function: Green. <br/>
 * Date: 2015-03-05 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class ShopsGreenEditFragment extends BaseFragment {

    private LinearLayout llCourse;
    private LinearLayout llHoles;

    private IteeButton btnCourse;
    private IteeButton btnHoles;

    private List<JsonGreenFeeGet.GreenData> coursePriceList;
    private List<JsonGreenFeeGet.GreenData> holesPriceList;

    private List<IteeGreenMoneyEditText> coursePriceEdTextList;
    private List<IteeGreenMoneyEditText> holesPriceEdTextList;

    private int greenType;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_shops_green;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        llCourse = (LinearLayout) rootView.findViewById(R.id.ll_course);
        llHoles = (LinearLayout) rootView.findViewById(R.id.ll_holes);

        RelativeLayout rlCourse = (RelativeLayout) rootView.findViewById(R.id.rl_course);
        RelativeLayout rlHoles = (RelativeLayout) rootView.findViewById(R.id.rl_holes);

        LinearLayout.LayoutParams rlCourseParams = (LinearLayout.LayoutParams) rlCourse.getLayoutParams();
        LinearLayout.LayoutParams rlHolesParams = (LinearLayout.LayoutParams) rlHoles.getLayoutParams();
        rlCourseParams.height = getActualHeightOnThisDevice(80);
        rlHolesParams.height = getActualHeightOnThisDevice(80);
        rlCourse.setLayoutParams(rlCourseParams);
        rlHoles.setLayoutParams(rlHolesParams);

        coursePriceList = new ArrayList<>();
        holesPriceList = new ArrayList<>();

        btnCourse = (IteeButton) rootView.findViewById(R.id.btn_course);
        btnHoles = (IteeButton) rootView.findViewById(R.id.btn_holes);

        RelativeLayout.LayoutParams btnCourseParams = (RelativeLayout.LayoutParams) btnCourse.getLayoutParams();
        RelativeLayout.LayoutParams btnHolesParams = (RelativeLayout.LayoutParams) btnHoles.getLayoutParams();
        btnCourseParams.width = getActualHeightOnThisDevice(45);
        btnCourseParams.height = getActualHeightOnThisDevice(45);

        btnHolesParams.width = getActualHeightOnThisDevice(45);
        btnHolesParams.height = getActualHeightOnThisDevice(45);
        btnCourse.setTextSize(Constants.FONT_SIZE_NORMAL);
        btnHoles.setTextSize(Constants.FONT_SIZE_NORMAL);

        coursePriceEdTextList = new ArrayList<>();
        holesPriceEdTextList = new ArrayList<>();
        getGreenFeeData();
    }

    @Override
    protected void setDefaultValueOfControls() {

    }


    private void addCourseView() {
        btnCourse.setVisibility(View.VISIBLE);

        if (coursePriceList.size() > 0 && coursePriceList.get(0).getEnableStatus() == Constants.SHOPS_ENABLE_STATUS_YES) {
            btnCourse.setBackgroundResource(R.drawable.icon_shop_green_selected);
            btnHoles.setBackgroundResource(R.drawable.icon_shops_green_unselected);
            greenType = 0;
        }

        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnCourse.setBackgroundResource(R.drawable.icon_shop_green_selected);
                btnHoles.setBackgroundResource(R.drawable.icon_shops_green_unselected);
                greenType = 0;

            }
        });

        llCourse.addView(AppUtils.getSeparatorLine(ShopsGreenEditFragment.this));
        for (JsonGreenFeeGet.GreenData data : coursePriceList) {
            LinearLayout.LayoutParams rlLayoutParams
                    = new LinearLayout.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            RelativeLayout rlLayout = new RelativeLayout(getBaseActivity());
            rlLayout.setLayoutParams(rlLayoutParams);

            RelativeLayout.LayoutParams tvPdNameParams
                    = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(500), RelativeLayout.LayoutParams.WRAP_CONTENT);
            tvPdNameParams.addRule(RelativeLayout.CENTER_VERTICAL);

            IteeTextView tvPdName = new IteeTextView(getActivity());
            tvPdName.setTextColor(getResources().getColor(R.color.common_black));
            tvPdName.setTextSize(Constants.FONT_SIZE_NORMAL);
            tvPdName.setSingleLine();
            tvPdName.setSingleLine(true);
            tvPdName.setEllipsize(TextUtils.TruncateAt.END);
            tvPdName.setLayoutParams(tvPdNameParams);
            tvPdName.setText(data.getName());
            rlLayout.addView(tvPdName);
            rlLayout.setPadding(getActualWidthOnThisDevice(40), 0, getActualWidthOnThisDevice(40), 0);

            RelativeLayout.LayoutParams edPdMoneyParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(320), RelativeLayout.LayoutParams.WRAP_CONTENT);
            edPdMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            edPdMoneyParams.addRule(RelativeLayout.CENTER_VERTICAL);

            IteeGreenMoneyEditText edPdMoney = new IteeGreenMoneyEditText(this);
            edPdMoney.setTag(data.getId());
            edPdMoney.setLayoutParams(edPdMoneyParams);
            edPdMoney.setHint(R.string.shop_setting_price);

            if (Utils.isStringNotNullOrEmpty(data.getPrice()) && !Constants.STR_MINUS_1.equals(data.getPrice())) {
                edPdMoney.setValue(data.getPrice());
            } else {
                edPdMoney.setText(Constants.STR_EMPTY);
            }

            edPdMoney.setGravity(Gravity.END);
            edPdMoney.setTextColor(getColor(R.color.common_gray));
            rlLayout.addView(edPdMoney);
            coursePriceEdTextList.add(edPdMoney);
            llCourse.addView(rlLayout);
            llCourse.addView(AppUtils.getSeparatorLine(ShopsGreenEditFragment.this));
        }

    }

    private void addHolesView() {
        btnHoles.setVisibility(View.VISIBLE);

        if (holesPriceList.size() > 0 && holesPriceList.get(0).getEnableStatus() == Constants.SHOPS_ENABLE_STATUS_YES) {

            btnCourse.setBackgroundResource(R.drawable.icon_shops_green_unselected);
            btnHoles.setBackgroundResource(R.drawable.icon_shop_green_selected);
            greenType = 1;
        }
        btnHoles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnCourse.setBackgroundResource(R.drawable.icon_shops_green_unselected);
                btnHoles.setBackgroundResource(R.drawable.icon_shop_green_selected);
                greenType = 1;
            }
        });
        llHoles.addView(AppUtils.getSeparatorLine(ShopsGreenEditFragment.this));
        for (JsonGreenFeeGet.GreenData data : holesPriceList) {
            LinearLayout.LayoutParams rlLayoutParams = new LinearLayout.LayoutParams(ListView.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
            RelativeLayout rlLayout = new RelativeLayout(getBaseActivity());
            rlLayout.setLayoutParams(rlLayoutParams);
            RelativeLayout.LayoutParams tvPdNameParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(500), RelativeLayout.LayoutParams.WRAP_CONTENT);
            tvPdNameParams.addRule(RelativeLayout.CENTER_VERTICAL);

            IteeTextView tvPdName = new IteeTextView(getActivity());
            tvPdName.setText(R.string.shop_setting_caddie);
            tvPdName.setTextColor(getResources().getColor(R.color.common_black));
            tvPdName.setTextSize(Constants.FONT_SIZE_NORMAL);
            tvPdName.setLayoutParams(tvPdNameParams);
            tvPdName.setText(data.getName());

            tvPdName.setSingleLine();
            tvPdName.setSingleLine(true);
            tvPdName.setEllipsize(TextUtils.TruncateAt.END);
            rlLayout.addView(tvPdName);
            rlLayout.setPadding(getActualWidthOnThisDevice(40), 0, getActualWidthOnThisDevice(40), 0);

            RelativeLayout.LayoutParams edPdMoneyParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(320), RelativeLayout.LayoutParams.WRAP_CONTENT);
            edPdMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            edPdMoneyParams.addRule(RelativeLayout.CENTER_VERTICAL);

            IteeGreenMoneyEditText edPdMoney = new IteeGreenMoneyEditText(this);
            edPdMoney.setTag(data.getId());
            edPdMoney.setLayoutParams(edPdMoneyParams);
            edPdMoney.setGravity(Gravity.END);
            edPdMoney.setTextColor(getColor(R.color.common_gray));
            edPdMoney.setHint(R.string.shop_setting_price);

            if (Utils.isStringNotNullOrEmpty(data.getPrice()) && !Constants.STR_MINUS_1.equals(data.getPrice())) {
                edPdMoney.setValue(data.getPrice());
            } else {
                edPdMoney.setText(Constants.STR_EMPTY);
            }
            rlLayout.addView(edPdMoney);
            holesPriceEdTextList.add(edPdMoney);
            llHoles.addView(rlLayout);
            llHoles.addView(AppUtils.getSeparatorLine(ShopsGreenEditFragment.this));
        }

    }

    @Override
    protected void setListenersOfControls() {

    }


    private String getPriceList() {

        JSONObject jsObj = new JSONObject();

        JSONArray courseArray = new JSONArray();

        for (int i = 0; i < coursePriceEdTextList.size(); i++) {
            Map<String, String> course = new HashMap<>();
            course.put(ApiKey.SHOPS_ID, String.valueOf(coursePriceList.get(i).getId()));
            course.put(ApiKey.SHOPS_NAME, coursePriceList.get(i).getName());
            course.put(ApiKey.SHOPS_PRICE, coursePriceEdTextList.get(i).getNullValue());
            course.put(ApiKey.SHOPS_ENABLE_STATUS, greenType == 1 ? Constants.STR_0 : Constants.STR_1);

            course.put(ApiKey.SHOPS_AREA_ID, coursePriceList.get(i).getAreaId());
            course.put(ApiKey.SHOPS_AREA_TYPE, coursePriceList.get(i).getAreaType());

            JSONObject courseObject = new JSONObject(course);

            courseArray.put(courseObject);
        }
        try {
            jsObj.put(ApiKey.SHOPS_COURSE, courseArray);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

        JSONArray holesArray = new JSONArray();
        for (int i = 0; i < holesPriceEdTextList.size(); i++) {
            Map<String, String> holes = new HashMap<>();
            holes.put(ApiKey.SHOPS_ID, String.valueOf(holesPriceList.get(i).getId()));
            holes.put(ApiKey.SHOPS_NAME, holesPriceList.get(i).getName());
            holes.put(ApiKey.SHOPS_PRICE, holesPriceEdTextList.get(i).getNullValue());
            holes.put(ApiKey.SHOPS_ENABLE_STATUS, greenType == 1 ? Constants.STR_1 : Constants.STR_0);

            holes.put(ApiKey.SHOPS_AREA_ID, holesPriceList.get(i).getAreaId());
            holes.put(ApiKey.SHOPS_AREA_TYPE, holesPriceList.get(i).getAreaType());
            JSONObject holesObject = new JSONObject(holes);
            holesArray.put(holesObject);
        }
        try {
            jsObj.put(ApiKey.SHOPS_HOLES, holesArray);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

        return jsObj.toString();
    }

    private void putGreenFee() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.SHOPS_PRICE_LIST, getPriceList());


        HttpManager<JsonAgentsPricingListGet> hh = new HttpManager<JsonAgentsPricingListGet>(ShopsGreenEditFragment.this) {
            @Override
            public void onJsonSuccess(JsonAgentsPricingListGet jo) {
                Integer returnCode = jo.getReturnCode();

                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    doBack();
                }
                Utils.showShortToast(getActivity(), msg);
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startPut(this.getActivity(), ApiManager.HttpApi.ShopsGreenFeePut, params);


    }

    @Override
    protected void setLayoutOfControls() {


    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.common_green_fee);
        getTvRight().setText(R.string.shop_setting_ok);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String error = getErrorStr();
                if (error != null) {
                    Utils.showLongToast(getActivity(), error);
                } else {
                    putGreenFee();
                }

            }
        });
    }

    private String getErrorStr() {

        if (greenType == 0) {
            int bc = 0;
            if (coursePriceEdTextList.size() > 1) {
                bc = coursePriceEdTextList.size() / 2;
            }

            for (int i = 0; i < bc; i++) {
                if (coursePriceEdTextList.get(i).getValue().equals(Constants.STR_EMPTY)) {
                    return AppUtils.generateNotNullMessage(this, R.string.shop_setting_course);
                }
            }
        } else {
            int bh = 0;
            if (holesPriceEdTextList.size() > 1) {
                bh = holesPriceEdTextList.size() / 2;
            }
            for (int i = 0; i < bh; i++) {
                if (holesPriceEdTextList.get(i).getValue().equals(Constants.STR_EMPTY)) {
                    return AppUtils.generateNotNullMessage(this, R.string.shop_setting_holes);
                }
            }
        }
        return null;
    }

    private void getGreenFeeData() {
        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        HttpManager<JsonGreenFeeGet> hh = new HttpManager<JsonGreenFeeGet>(ShopsGreenEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonGreenFeeGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    coursePriceList = jo.getCourseList();
                    holesPriceList = jo.getHolesList();
                    addCourseView();
                    addHolesView();
                } else if (returnCode == Constants.RETURN_CODE_20302) {
                    push(CourseAddFragment.class);
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(this.getActivity(), ApiManager.HttpApi.ShopsGreenFeeGet, params);


    }
}
