/**
 * Project Name: itee
 * File Name:  PlayerRefundFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-28
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.administration;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.baoyz.actionsheet.ActionSheet;

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
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonDateSetting;
import cn.situne.itee.view.IteeTextView;
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
public class DateSettingFragment extends BaseFragment {

    private RelativeLayout rl_date_setting_year;
    private RelativeLayout rl_date_setting_sun;
    private RelativeLayout rl_date_setting_mon;
    private RelativeLayout rl_date_setting_tue;
    private RelativeLayout rl_date_setting_wed;
    private RelativeLayout rl_date_setting_thu;
    private RelativeLayout rl_date_setting_fri;
    private RelativeLayout rl_date_setting_sat;
    private RelativeLayout rl_date_setting_generate;
    private RelativeLayout rl_date_setting_advanced;

    private LinearLayout ll_all;


    private IteeTextView tvYear;
    private IteeTextView tvYearValue;
    private IteeTextView tvSun;
    private IteeTextView tvSunValue;
    private IteeTextView tvMon;
    private IteeTextView tvMonValue;
    private IteeTextView tvTue;
    private IteeTextView tvTueValue;
    private IteeTextView tvWed;
    private IteeTextView tvWedValue;
    private IteeTextView tvThu;
    private IteeTextView tvThuValue;
    private IteeTextView tvFri;
    private IteeTextView tvFriValue;
    private IteeTextView tvSat;
    private IteeTextView tvSatValue;
    private IteeTextView tvGenerate;
    private IteeTextView tvAdvanced;


    private SelectYearPopupWindow selectYearPopupWindow;

    private int nowClick;
    private JsonDateSetting jsonDateSetting;
    private List<String> paramList;


    @Override
    protected int getFragmentId() {
        return R.layout.fragment_date_setting;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.add_title_out;
    }

    @Override
    protected void initControls(View rootView) {

        ll_all = (LinearLayout) rootView.findViewById(R.id.ll_all);

        rl_date_setting_year = (RelativeLayout) rootView.findViewById(R.id.rl_date_setting_year);

        rl_date_setting_sun = new RelativeLayout(mContext);
        rl_date_setting_mon = new RelativeLayout(mContext);
        rl_date_setting_tue = new RelativeLayout(mContext);
        rl_date_setting_wed = new RelativeLayout(mContext);
        rl_date_setting_thu = new RelativeLayout(mContext);
        rl_date_setting_fri = new RelativeLayout(mContext);
        rl_date_setting_sat = new RelativeLayout(mContext);


        rl_date_setting_generate = (RelativeLayout) rootView.findViewById(R.id.rl_date_setting_generate);
        rl_date_setting_advanced = (RelativeLayout) rootView.findViewById(R.id.rl_date_setting_advanced);

        tvYear = new IteeTextView(getBaseActivity());
        tvYearValue = new IteeTextView(getBaseActivity());


        tvSun = new IteeTextView(getBaseActivity());
        tvSunValue = new IteeTextView(getBaseActivity());


        tvMon = new IteeTextView(getBaseActivity());
        tvMonValue = new IteeTextView(getBaseActivity());


        tvTue = new IteeTextView(getBaseActivity());
        tvTueValue = new IteeTextView(getBaseActivity());


        tvWed = new IteeTextView(getBaseActivity());
        tvWedValue = new IteeTextView(getBaseActivity());

        tvThu = new IteeTextView(getBaseActivity());
        tvThuValue = new IteeTextView(getBaseActivity());

        tvFri = new IteeTextView(getBaseActivity());
        tvFriValue = new IteeTextView(getBaseActivity());


        tvSat = new IteeTextView(getBaseActivity());
        tvSatValue = new IteeTextView(getBaseActivity());

        tvGenerate = new IteeTextView(getBaseActivity());
        tvAdvanced = new IteeTextView(getBaseActivity());
        paramList = new ArrayList<>();
        paramList.add("1");
        paramList.add("2");
        paramList.add("2");
        paramList.add("2");
        paramList.add("2");
        paramList.add("2");
        paramList.add("1");
    }

    @Override
    protected void setDefaultValueOfControls() {
        tvYear.setText(getString(R.string.common_year));

        tvSun.setText(getString(R.string.calendar_sun));
        tvMon.setText(getString(R.string.calendar_mon));
        tvTue.setText(getString(R.string.calendar_tue));
        tvWed.setText(getString(R.string.calendar_wed));
        tvThu.setText(getString(R.string.calendar_thu));
        tvFri.setText(getString(R.string.calendar_fri));
        tvSat.setText(getString(R.string.calendar_sat));
        tvAdvanced.setText(getString(R.string.administration_date_advanced_setting));
        if (jsonDateSetting == null) {
            tvYearValue.setText(String.valueOf(Utils.getCurrentYear()));
            tvSunValue.setText(getString(R.string.pricing_table_holiday));
            tvMonValue.setText(getString(R.string.pricing_table_weekday));
            tvTueValue.setText(getString(R.string.pricing_table_weekday));
            tvWedValue.setText(getString(R.string.pricing_table_weekday));
            tvThuValue.setText(getString(R.string.pricing_table_weekday));
            tvFriValue.setText(getString(R.string.pricing_table_weekday));
            tvSatValue.setText(getString(R.string.pricing_table_holiday));
            tvGenerate.setText(getString(R.string.administration_date_setting_generate));
        } else {

            JsonDateSetting.DateSetting dateSetting = jsonDateSetting.getDataList();
            tvYearValue.setText(dateSetting.getYear());
            Utils.log(dateSetting.getSun());
            if (Constants.STR_1.equals(dateSetting.getSun())) {
                tvSunValue.setText(getString(R.string.pricing_table_holiday));
                paramList.set(0, "1");
            } else {
                tvSunValue.setText(getString(R.string.pricing_table_weekday));
                paramList.set(0, "2");
            }
            if (Constants.STR_1.equals(dateSetting.getMon())) {
                tvMonValue.setText(getString(R.string.pricing_table_holiday));
                paramList.set(1, "1");
            } else {
                tvMonValue.setText(getString(R.string.pricing_table_weekday));
                paramList.set(1, "2");
            }

            if (Constants.STR_1.equals(dateSetting.getTue())) {
                tvTueValue.setText(getString(R.string.pricing_table_holiday));
                paramList.set(2, "1");
            } else {
                tvTueValue.setText(getString(R.string.pricing_table_weekday));
                paramList.set(2, "2");
            }


            if (Constants.STR_1.equals(dateSetting.getWed())) {
                tvWedValue.setText(getString(R.string.pricing_table_holiday));
                paramList.set(3, "1");
            } else {
                tvWedValue.setText(getString(R.string.pricing_table_weekday));
                paramList.set(3, "2");
            }

            if (Constants.STR_1.equals(dateSetting.getThu())) {
                tvThuValue.setText(getString(R.string.pricing_table_holiday));
                paramList.set(4, "1");
            } else {
                tvThuValue.setText(getString(R.string.pricing_table_weekday));
                paramList.set(4, "2");
            }


            if (Constants.STR_1.equals(dateSetting.getFri())) {
                tvFriValue.setText(getString(R.string.pricing_table_holiday));
                paramList.set(5, "1");
            } else {
                tvFriValue.setText(getString(R.string.pricing_table_weekday));
                paramList.set(5, "2");
            }

            if (Constants.STR_1.equals(dateSetting.getSat())) {
                tvSatValue.setText(getString(R.string.pricing_table_holiday));
                paramList.set(6, "1");
            } else {
                tvSatValue.setText(getString(R.string.pricing_table_weekday));
                paramList.set(6, "2");
            }

        }


    }

    private void resetLLAll() {

        ll_all.removeAllViews();
        if (AppUtils.getCurrentFirstDayOfWeek(mContext).equals("SUN")) {
            ll_all.addView(rl_date_setting_sun);
            AppUtils.addTopSeparatorLine(rl_date_setting_sun, mContext);
            ll_all.addView(rl_date_setting_mon);
            ll_all.addView(rl_date_setting_tue);
            ll_all.addView(rl_date_setting_wed);
            ll_all.addView(rl_date_setting_thu);
            ll_all.addView(rl_date_setting_fri);
            ll_all.addView(rl_date_setting_sat);
        } else {
            ll_all.addView(rl_date_setting_mon);
            AppUtils.addTopSeparatorLine(rl_date_setting_mon, mContext);
            ll_all.addView(rl_date_setting_tue);
            ll_all.addView(rl_date_setting_wed);
            ll_all.addView(rl_date_setting_thu);
            ll_all.addView(rl_date_setting_fri);
            ll_all.addView(rl_date_setting_sat);
            ll_all.addView(rl_date_setting_sun);
        }
    }

    @Override
    protected void setListenersOfControls() {


        final ActionSheet.ActionSheetListener actionSheetListenerAddress = new ActionSheet.ActionSheetListener() {
            @Override
            public void onDismiss(ActionSheet actionSheet, boolean isCancel) {
            }

            @Override
            public void onDismissWithCancelButton(ActionSheet actionSheet) {
            }

            @Override
            public void onOtherButtonClick(ActionSheet actionSheet, int index) {

                switch (index) {
                    case 0:
                        paramList.set(nowClick, "2");
                        switch (nowClick) {
                            case 0:
                                tvSunValue.setText(getString(R.string.pricing_table_weekday));
                                break;
                            case 1:
                                tvMonValue.setText(getString(R.string.pricing_table_weekday));
                                break;
                            case 2:
                                tvTueValue.setText(getString(R.string.pricing_table_weekday));
                                break;
                            case 3:
                                tvWedValue.setText(getString(R.string.pricing_table_weekday));
                                break;
                            case 4:
                                tvThuValue.setText(getString(R.string.pricing_table_weekday));
                                break;
                            case 5:
                                tvFriValue.setText(getString(R.string.pricing_table_weekday));
                                break;
                            case 6:
                                tvSatValue.setText(getString(R.string.pricing_table_weekday));
                                break;

                        }
                        break;
                    case 1:
                        paramList.set(nowClick, "1");
                        Utils.log(nowClick + "");
                        switch (nowClick) {
                            case 0:
                                tvSunValue.setText(getString(R.string.pricing_table_holiday));
                                break;
                            case 1:
                                tvMonValue.setText(getString(R.string.pricing_table_holiday));
                                break;
                            case 2:
                                tvTueValue.setText(getString(R.string.pricing_table_holiday));
                                break;
                            case 3:
                                tvWedValue.setText(getString(R.string.pricing_table_holiday));
                                break;
                            case 4:
                                tvThuValue.setText(getString(R.string.pricing_table_holiday));
                                break;
                            case 5:
                                tvFriValue.setText(getString(R.string.pricing_table_holiday));
                                break;
                            case 6:
                                tvSatValue.setText(getString(R.string.pricing_table_holiday));
                                break;

                        }

                        break;

                }

                actionSheet.dismiss();
            }
        };


        final View.OnClickListener onClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                nowClick = (int) v.getTag();
                getBaseActivity().setTheme(com.baoyz.actionsheet.R.style.ActionSheetStyleIOS7);
                String[] tTags = new String[]{getString(R.string.pricing_table_weekday)
                        , getString(R.string.pricing_table_holiday)};

                ActionSheet.createBuilder(getActivity(), getFragmentManager())
                        .setCancelButtonTitle(getString(R.string.common_cancel))
                        .setOtherButtonTitles(tTags)
                        .setCancelButtonHidden(true)
                        .setCancelableOnTouchOutside(true).setListener(actionSheetListenerAddress).show();

            }
        };

        rl_date_setting_sun.setTag(0);
        rl_date_setting_sun.setOnClickListener(onClickListener);
        rl_date_setting_mon.setTag(1);
        rl_date_setting_mon.setOnClickListener(onClickListener);
        rl_date_setting_tue.setTag(2);
        rl_date_setting_tue.setOnClickListener(onClickListener);
        rl_date_setting_wed.setTag(3);
        rl_date_setting_wed.setOnClickListener(onClickListener);
        rl_date_setting_thu.setTag(4);
        rl_date_setting_thu.setOnClickListener(onClickListener);
        rl_date_setting_fri.setTag(5);
        rl_date_setting_fri.setOnClickListener(onClickListener);
        rl_date_setting_sat.setTag(6);
        rl_date_setting_sat.setOnClickListener(onClickListener);

        rl_date_setting_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // select year
                selectYearPopupWindow = new SelectYearPopupWindow(getActivity(), null,0);
//                selectYearPopupWindow.setYear(Utils.getCurrentYear(), 5000, Integer.valueOf(tvYearValue.getText().toString()).intValue());
                selectYearPopupWindow.showAtLocation(getRootView(),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                selectYearPopupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = selectYearPopupWindow.wheelViewYear.getCurrentItem();
                        String selectedYear = selectYearPopupWindow.wheelViewYear.getAdapter().getItem(index);
                        tvYearValue.setText(selectedYear);
                        selectYearPopupWindow.dismiss();
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


        rl_date_setting_advanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                push(AdvancedSettingFragment.class, null);
            }
        });
        rl_date_setting_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netLinkEditAccount();
            }
        });
    }

    @Override
    protected void setLayoutOfControls() {


        LinearLayout.LayoutParams relativeLayout = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(101));
        LinearLayout.LayoutParams relativeLayout150 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(140));

        rl_date_setting_year.setLayoutParams(relativeLayout);
        rl_date_setting_sun.setLayoutParams(relativeLayout);
        rl_date_setting_mon.setLayoutParams(relativeLayout);
        rl_date_setting_tue.setLayoutParams(relativeLayout);
        rl_date_setting_wed.setLayoutParams(relativeLayout);
        rl_date_setting_thu.setLayoutParams(relativeLayout);
        rl_date_setting_fri.setLayoutParams(relativeLayout);
        rl_date_setting_sat.setLayoutParams(relativeLayout);

        rl_date_setting_sun.setBackgroundColor(getColor(R.color.common_white));
        rl_date_setting_mon.setBackgroundColor(getColor(R.color.common_white));
        rl_date_setting_tue.setBackgroundColor(getColor(R.color.common_white));
        rl_date_setting_thu.setBackgroundColor(getColor(R.color.common_white));
        rl_date_setting_wed.setBackgroundColor(getColor(R.color.common_white));
        rl_date_setting_fri.setBackgroundColor(getColor(R.color.common_white));
        rl_date_setting_sat.setBackgroundColor(getColor(R.color.common_white));
        rl_date_setting_year.setBackgroundColor(getColor(R.color.common_white));
        rl_date_setting_generate.setBackgroundColor(getColor(R.color.common_white));
        rl_date_setting_advanced.setBackgroundColor(getColor(R.color.common_white));
        rl_date_setting_generate.setLayoutParams(relativeLayout150);
        rl_date_setting_advanced.setLayoutParams(relativeLayout);


        RelativeLayout.LayoutParams relativeLayoutArrow = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayoutArrow.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        relativeLayoutArrow.rightMargin = getActualWidthOnThisDevice(20);

        RelativeLayout.LayoutParams relativeLayoutTitle = new RelativeLayout.LayoutParams(getActualHeightOnThisDevice(100), ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayoutTitle.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        relativeLayoutTitle.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        relativeLayoutTitle.leftMargin = getActualWidthOnThisDevice(20);

        RelativeLayout.LayoutParams relativeLayoutTitle1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayoutTitle1.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        relativeLayoutTitle1.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        relativeLayoutTitle1.leftMargin = getActualWidthOnThisDevice(20);

        RelativeLayout.LayoutParams relativeLayoutGenerate = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        relativeLayoutGenerate.addRule(RelativeLayout.CENTER_IN_PARENT);
        relativeLayoutGenerate.setMargins(getActualWidthOnThisDevice(20), getActualWidthOnThisDevice(10), getActualWidthOnThisDevice(20), getActualWidthOnThisDevice(10));

        rl_date_setting_generate.addView(tvGenerate);
        tvGenerate.setLayoutParams(relativeLayoutGenerate);
        AppUtils.addBottomSeparatorLine(rl_date_setting_generate, mContext);
        AppUtils.addTopSeparatorLine(rl_date_setting_generate, mContext);


        ImageView tvArrow8 = new ImageView(getBaseActivity());

        rl_date_setting_advanced.addView(tvAdvanced);
        rl_date_setting_advanced.addView(tvArrow8);
        AppUtils.addBottomSeparatorLine(rl_date_setting_advanced, mContext);

        tvArrow8.setLayoutParams(relativeLayoutArrow);
        tvArrow8.setId(View.generateViewId());
        tvArrow8.setImageResource(R.drawable.icon_right_arrow);
        tvAdvanced.setLayoutParams(relativeLayoutTitle1);


        ImageView tvArrow7 = new ImageView(getBaseActivity());

        rl_date_setting_fri.addView(tvFri);
        rl_date_setting_fri.addView(tvArrow7);
        rl_date_setting_fri.addView(tvFriValue);
        AppUtils.addBottomSeparatorLine(rl_date_setting_fri, mContext);

        tvArrow7.setLayoutParams(relativeLayoutArrow);
        tvArrow7.setId(View.generateViewId());
        tvArrow7.setImageResource(R.drawable.icon_right_arrow);

        tvFri.setLayoutParams(relativeLayoutTitle);

        RelativeLayout.LayoutParams paramtvFriValue = (RelativeLayout.LayoutParams) tvFriValue.getLayoutParams();
        paramtvFriValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramtvFriValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramtvFriValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramtvFriValue.addRule(RelativeLayout.LEFT_OF, tvArrow7.getId());
        tvFriValue.setLayoutParams(paramtvFriValue);


        ImageView tvArrow6 = new ImageView(getBaseActivity());

        rl_date_setting_sat.addView(tvSat);
        rl_date_setting_sat.addView(tvArrow6);
        rl_date_setting_sat.addView(tvSatValue);
        AppUtils.addBottomSeparatorLine(rl_date_setting_sat, mContext);

        tvArrow6.setLayoutParams(relativeLayoutArrow);
        tvArrow6.setId(View.generateViewId());
        tvArrow6.setImageResource(R.drawable.icon_right_arrow);

        tvSat.setLayoutParams(relativeLayoutTitle);

        RelativeLayout.LayoutParams paramtvSatValue = (RelativeLayout.LayoutParams) tvSatValue.getLayoutParams();
        paramtvSatValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramtvSatValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramtvSatValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramtvSatValue.addRule(RelativeLayout.LEFT_OF, tvArrow6.getId());
        tvSatValue.setLayoutParams(paramtvSatValue);
//
        ImageView tvArrow5 = new ImageView(getBaseActivity());

        rl_date_setting_thu.addView(tvThu);
        rl_date_setting_thu.addView(tvArrow5);
        rl_date_setting_thu.addView(tvThuValue);
        AppUtils.addBottomSeparatorLine(rl_date_setting_thu, mContext);

        tvArrow5.setLayoutParams(relativeLayoutArrow);
        tvArrow5.setId(View.generateViewId());
        tvArrow5.setImageResource(R.drawable.icon_right_arrow);

        tvThu.setLayoutParams(relativeLayoutTitle);

        RelativeLayout.LayoutParams paramtvThuValue = (RelativeLayout.LayoutParams) tvThuValue.getLayoutParams();
        paramtvThuValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramtvThuValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramtvThuValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramtvThuValue.addRule(RelativeLayout.LEFT_OF, tvArrow5.getId());
        tvThuValue.setLayoutParams(paramtvThuValue);

        ImageView tvArrow4 = new ImageView(getBaseActivity());

        rl_date_setting_wed.addView(tvWed);
        rl_date_setting_wed.addView(tvArrow4);
        rl_date_setting_wed.addView(tvWedValue);
        AppUtils.addBottomSeparatorLine(rl_date_setting_wed, mContext);

        tvArrow4.setLayoutParams(relativeLayoutArrow);
        tvArrow4.setId(View.generateViewId());
        tvArrow4.setImageResource(R.drawable.icon_right_arrow);

        tvWed.setLayoutParams(relativeLayoutTitle);

        RelativeLayout.LayoutParams paramtvWedValue = (RelativeLayout.LayoutParams) tvWedValue.getLayoutParams();
        paramtvWedValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramtvWedValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramtvWedValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramtvWedValue.addRule(RelativeLayout.LEFT_OF, tvArrow4.getId());
        tvWedValue.setLayoutParams(paramtvWedValue);


        ImageView tvArrow2 = new ImageView(getBaseActivity());

        rl_date_setting_tue.addView(tvTue);
        rl_date_setting_tue.addView(tvArrow2);
        rl_date_setting_tue.addView(tvTueValue);

        AppUtils.addBottomSeparatorLine(rl_date_setting_tue, mContext);

        tvArrow2.setLayoutParams(relativeLayoutArrow);
        tvArrow2.setId(View.generateViewId());
        tvArrow2.setImageResource(R.drawable.icon_right_arrow);

        tvTue.setLayoutParams(relativeLayoutTitle);

        RelativeLayout.LayoutParams paramTueValue = (RelativeLayout.LayoutParams) tvTueValue.getLayoutParams();
        paramTueValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramTueValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramTueValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramTueValue.addRule(RelativeLayout.LEFT_OF, tvArrow2.getId());
        tvTueValue.setLayoutParams(paramTueValue);
//


        ImageView tvArrow = new ImageView(getBaseActivity());

        rl_date_setting_year.addView(tvYear);
        rl_date_setting_year.addView(tvArrow);
        rl_date_setting_year.addView(tvYearValue);
        AppUtils.addBottomSeparatorLine(rl_date_setting_year, mContext);

        tvArrow.setLayoutParams(relativeLayoutArrow);
        tvArrow.setId(View.generateViewId());
        tvArrow.setImageResource(R.drawable.icon_right_arrow);

        tvYear.setLayoutParams(relativeLayoutTitle);

        RelativeLayout.LayoutParams paramYearValue = (RelativeLayout.LayoutParams) tvYearValue.getLayoutParams();
        paramYearValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramYearValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramYearValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramYearValue.addRule(RelativeLayout.LEFT_OF, tvArrow.getId());
        tvYearValue.setLayoutParams(paramYearValue);

        ImageView tvArrow3 = new ImageView(getBaseActivity());

        rl_date_setting_sun.addView(tvSun);
        rl_date_setting_sun.addView(tvArrow3);
        rl_date_setting_sun.addView(tvSunValue);
        AppUtils.addBottomSeparatorLine(rl_date_setting_sun, mContext);
        tvArrow3.setLayoutParams(relativeLayoutArrow);
        tvArrow3.setId(View.generateViewId());
        tvArrow3.setImageResource(R.drawable.icon_right_arrow);

        tvSun.setLayoutParams(relativeLayoutTitle);

        RelativeLayout.LayoutParams paramtvSunValue = (RelativeLayout.LayoutParams) tvSunValue.getLayoutParams();
        paramtvSunValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramtvSunValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramtvSunValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramtvSunValue.addRule(RelativeLayout.LEFT_OF, tvArrow3.getId());
        tvSunValue.setLayoutParams(paramtvSunValue);


//
        ImageView tvArrow1 = new ImageView(getBaseActivity());

        rl_date_setting_mon.addView(tvMon);
        rl_date_setting_mon.addView(tvArrow1);
        rl_date_setting_mon.addView(tvMonValue);

        AppUtils.addBottomSeparatorLine(rl_date_setting_mon, mContext);

        tvArrow1.setLayoutParams(relativeLayoutArrow);
        tvArrow1.setId(View.generateViewId());
        tvArrow1.setImageResource(R.drawable.icon_right_arrow);

        tvMon.setLayoutParams(relativeLayoutTitle);

        RelativeLayout.LayoutParams paramMonValue = (RelativeLayout.LayoutParams) tvMonValue.getLayoutParams();
        paramMonValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramMonValue.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramMonValue.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramMonValue.addRule(RelativeLayout.LEFT_OF, tvArrow1.getId());
        tvMonValue.setLayoutParams(paramMonValue);
        resetLLAll();
    }

    @Override
    protected void setPropertyOfControls() {
        // tvGenerate.setBackgroundColor(getColor(R.color.common_wanted_green));
        tvGenerate.setTextColor(getColor(R.color.common_white));
        tvGenerate.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
        tvGenerate.setGravity(Gravity.CENTER);
        tvGenerate.setBackgroundResource(R.drawable.bg_green_btn);
        tvAdvanced.setTextColor(getColor(R.color.common_blue));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.administration_date_setting));
        getTvLeftTitle().setId(View.generateViewId());

    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        netLinkGetAccount(String.valueOf(Utils.getCurrentYear()));
    }


    private void netLinkGetAccount(String currentYear) {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put("year", currentYear);
        HttpManager<JsonDateSetting> hh = new HttpManager<JsonDateSetting>(DateSettingFragment.this) {
            @Override
            public void onJsonSuccess(JsonDateSetting jo) {
                jsonDateSetting = jo;
                setDefaultValueOfControls();
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        Utils.log(ApiManager.getUrlWithNetApi(ApiManager.HttpApi.XDEVELOPX0235, mContext));
        hh.startGet(DateSettingFragment.this.getBaseActivity().getApplication(), ApiManager.HttpApi.XDEVELOPX0235, params);
    }


    private void netLinkEditAccount() {


        Map<String, String> params = new HashMap<>();

        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put("weekstr", getPriceList());
        Utils.log(getPriceList());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(DateSettingFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    doBack();
                } else {
                    Utils.showShortToast(mContext, msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        Utils.log(ApiManager.getUrlWithNetApi(ApiManager.HttpApi.XDEVELOPX0235, mContext));
        hh.start(getActivity(), ApiManager.HttpApi.XDEVELOPX0235, params);
    }


    private String getPriceList() {

        Map<String, String> priceItem = new HashMap<>();
        for (int i = 0; i < 7; i++) {
            String param = paramList.get(i);
            switch (i) {
                case 0:
                    priceItem.put("SUN", param);
                    break;
                case 1:
                    priceItem.put("MON", param);
                    break;
                case 2:
                    priceItem.put("TUE", param);
                    break;
                case 3:
                    priceItem.put("WED", param);
                    break;
                case 4:
                    priceItem.put("THU", param);
                    break;
                case 5:
                    priceItem.put("FRI", param);
                    break;
                case 6:
                    priceItem.put("SAT", param);
                    break;

            }


        }
        JSONObject priceItemObj = new JSONObject(priceItem);
        JSONObject obj = new JSONObject();

        try {
            obj.put("year", tvYearValue.getText().toString());
            obj.put("week", priceItemObj);
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }

        return obj.toString();
    }
}
