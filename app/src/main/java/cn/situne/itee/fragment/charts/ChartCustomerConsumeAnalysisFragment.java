/**
 * Project Name: itee
 * File Name:	 ChartCustomerConsumeAnalysisFragment.java
 * Package Name: cn.situne.itee.fragment.charts
 * Date:		 2015-08-18
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.charts;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;
import org.xclcharts.chart.PieData;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.shopping.ShoppingConfirmPay_Fragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCustomerConsumeAnalysis;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.chart.IChartGenerate;
import cn.situne.itee.view.chart.PieChartView;

/**
 * ClassName:ChartCustomerConsumeAnalysisFragment <br/>
 * Function: . <br/>
 * Date: 2015-08-18 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ChartCustomerConsumeAnalysisFragment extends BaseFragment implements IChartGenerate {

    private LinearLayout llContainer;

    private boolean isChanged = true;

    private String beginDate;
    private String endDate;

    private String selectedMethod;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_chart_customer_consume_analysis;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        llContainer = (LinearLayout) rootView.findViewById(R.id.ll_content_container);
    }

    @Override
    protected void setDefaultValueOfControls() {
        llContainer.setBackgroundColor(getColor(R.color.chart_separator_gray));
    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {

    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        if (isChanged) {
            isChanged = false;
            getCustomerConsumeAnalysis();
        }
    }

    private void getCustomerConsumeAnalysis() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_BEGIN_DATE, beginDate);
        params.put(ApiKey.COMMON_END_DATE, endDate);
        params.put(ApiKey.COMMON_US_ID, "1");

        HttpManager<JsonCustomerConsumeAnalysis> hh = new HttpManager<JsonCustomerConsumeAnalysis>(true) {
            @Override
            public void onJsonSuccess(JsonCustomerConsumeAnalysis jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    LinearLayout.LayoutParams chartTitleParams = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,getActualHeightOnThisDevice(100));
                    chartTitleParams.leftMargin =DensityUtil.getActualWidthOnThisDevice(10, mContext);
                    chartTitleParams.rightMargin =DensityUtil.getActualWidthOnThisDevice(10, mContext);

                    //region NotUse
                    /*
                    LinearLayout chartTitle = new LinearLayout(ChartCustomerConsumeAnalysisFragment.this.getBaseActivity());
                    chartTitle.setBackgroundColor(getColor(R.color.common_light_gray));

                    chartTitle.setLayoutParams(chartTitleParams);

                    chartTitle.setPadding(DensityUtil.getActualWidthOnThisDevice(20, mContext),0,0,0);

                    LinearLayout.LayoutParams tv1Params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    LinearLayout.LayoutParams tv2Params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
                    LinearLayout.LayoutParams tv3Params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);

                    chartTitle.setOrientation(LinearLayout.HORIZONTAL);
                    TextView tv1 = new TextView(ChartCustomerConsumeAnalysisFragment.this.getBaseActivity());
                    TextView tv2 = new TextView(ChartCustomerConsumeAnalysisFragment.this.getBaseActivity());
                    TextView tv3 = new TextView(ChartCustomerConsumeAnalysisFragment.this.getBaseActivity());

                    tv1.setText(jo.getChangeTypeNum());
                    tv2.setText(getString(R.string.chart_players));
                    tv3.setText(getString(R.string.chart_ide_change_mes));


                    tv1.setGravity(Gravity.CENTER_VERTICAL);
                    tv2.setGravity(Gravity.CENTER_VERTICAL);
                    tv3.setGravity(Gravity.CENTER_VERTICAL);


                    tv1.setTextColor(getColor(R.color.chart_02_blue_font));
                    tv2.setTextColor(getColor(R.color.chart_02_blue_font));
                    tv1.setLayoutParams(tv1Params);
                    tv2.setLayoutParams(tv2Params);
                    tv3.setLayoutParams(tv3Params);

                    chartTitle.addView(tv1);
                    chartTitle.addView(tv2);
                    chartTitle.addView(tv3);
                    if (!Constants.STR_0.equals(jo.getChangeTypeNum())){
                        chartTitle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Bundle bundle = new Bundle();
                                bundle.putString(TransKey.SHOPPING_BEGIN_DATE, beginDate);
                                bundle.putString(TransKey.SHOPPING_END_DATE, endDate);
                                bundle.putString(TransKey.SHOPPING_SELECTED_METHOD, selectedMethod);
                                push(ChartDetailedIdentityChangeFragment.class, bundle);
                            }
                        });
                    }
                    llContainer.addView(chartTitle);*/ // note by zf ,因为身份变更现在是假数据，所以前台先注释掉
                    //endregion

                    View viSeparator = AppUtils.getSeparatorView(getActivity(), DensityUtil.getActualHeightOnThisDevice(20, mContext));
                    llContainer.addView(viSeparator);

                    for (int i = 0; i < jo.getDataList().size(); i++) {
                        JsonCustomerConsumeAnalysis.DataItem dataItem = jo.getDataList().get(i);
//                        if (StringUtils.isEmpty(dataItem.getMemAmt()) || Constants.STR_0.equals(dataItem.getMemAmt())) {
//                            continue;
//                        }
                        String type = MessageFormat.format(getString(R.string.chart_title_04), dataItem.getMemType(), dataItem.getMemPernum());
                        String titleString = type + Constants.STR_DOUBLE_SPACE + getString(R.string.chart_revenue)
                                + Constants.STR_COLON_WITH_SPACE + dataItem.getMemAmt();
                        SpannableStringBuilder ssbTitle = new SpannableStringBuilder(titleString);

                        int numberBlueStartIdx = titleString.indexOf(dataItem.getMemPernum());
                        int numberBlueEndIdx = numberBlueStartIdx + dataItem.getMemPernum().length();
                        int revenueBlueStartIdx = titleString.lastIndexOf(dataItem.getMemAmt());
                        ssbTitle.setSpan(new ForegroundColorSpan(getColor(R.color.chart_02_blue_font)), numberBlueStartIdx, numberBlueEndIdx,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        ssbTitle.setSpan(new ForegroundColorSpan(getColor(R.color.chart_02_blue_font)), revenueBlueStartIdx, titleString.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        LinkedList<PieData> chartData = new LinkedList<>();
                        for (int j = 0; j < dataItem.getTotalList().size(); j++) {
                            JsonCustomerConsumeAnalysis.DataItem.TotalDataItem totalDataItem = dataItem.getTotalList().get(j);

                            PieData pieData = new PieData();
                            pieData.setKey(totalDataItem.getTotalType());

                            String valueAndPercent = MessageFormat.format(getString(R.string.chart_label_format_04),
                                    totalDataItem.getTotalAmt(), totalDataItem.getTotalPercent() + Constants.STR_SYMBOL_PERCENT);
                            String label = totalDataItem.getTotalType() + Constants.STR_LINE_BREAK + valueAndPercent;

                            pieData.setLabel(label);
                            double percent = 0;
                            if (StringUtils.isNotEmpty(totalDataItem.getTotalPercent())) {
                                percent = Double.valueOf(totalDataItem.getTotalPercent());
                            }
                            pieData.setPercentage(percent);
                            pieData.setSliceColor(AppUtils.getColorFromRGBString(totalDataItem.getTotalTypeColor()));
                            chartData.add(pieData);
                        }
                        generatePie(ssbTitle, chartData);
                    }
                    isChanged = false;
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.CustomerConsumeAnalysis, params);
    }

    private void generatePie(CharSequence title, LinkedList<PieData> chartData) {
        int size = (int) (DensityUtil.getScreenWidth(mContext) * 0.8);
        PieChartView pieChartView = new PieChartView(getActivity());
        llContainer.addView(pieChartView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) pieChartView.getLayoutParams();
        layoutParams.height = DensityUtil.getActualHeightOnThisDevice(800, mContext);
        layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
        layoutParams.rightMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
        pieChartView.setLayoutParams(layoutParams);
        pieChartView.setChartSize(size, size);
        pieChartView.setChartData(chartData);
        pieChartView.setTitle(title);

        View viSeparator = AppUtils.getSeparatorView(this, DensityUtil.getActualHeightOnThisDevice(20, mContext));
        llContainer.addView(viSeparator);
    }

    private RelativeLayout generateTotalLayout() {
        RelativeLayout rlTotal = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams layoutParams
                = new RelativeLayout.LayoutParams(MATCH_PARENT, DensityUtil.getActualHeightOnThisDevice(100, mContext));
        rlTotal.setLayoutParams(layoutParams);
        IteeTextView tvCheckInInfo = new IteeTextView(mContext);
        rlTotal.addView(tvCheckInInfo);
        return rlTotal;
    }

    @Override
    public void setDatePeriod(String startDate, String endDate ,String selectedMethod) {
        this.beginDate = startDate;
        this.endDate = endDate;
        this.selectedMethod = selectedMethod;
        isChanged = true;
    }
}