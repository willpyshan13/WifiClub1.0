/**
 * Project Name: itee
 * File Name:	 ChartProductSellingFragment.java
 * Package Name: cn.situne.itee.fragment.charts
 * Date:		 2015-08-05
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.charts;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.LinearLayout;

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
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonProductSaleAnalysis;
import cn.situne.itee.view.chart.IChartGenerate;
import cn.situne.itee.view.chart.PieChartView;

/**
 * ClassName:ChartProductSellingFragment <br/>
 * Function: Product selling chart. <br/>
 * Date: 2015-08-05 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ChartProductSellingFragment extends BaseFragment implements IChartGenerate {

    private LinearLayout llContainer;

    private boolean isChanged = true;

    private String beginDate;
    private String endDate;
    private String selectedMethod;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_chart_product_selling;
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
            getProductSellingAnalysis();
        }
    }

    private void getProductSellingAnalysis() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_BEGIN_DATE, beginDate);
        params.put(ApiKey.COMMON_END_DATE, endDate);
        params.put(ApiKey.COMMON_US_ID, "1");

        Utils.log("beginDate:" + beginDate);
        Utils.log("endDate:" + endDate);

        HttpManager<JsonProductSaleAnalysis> hh = new HttpManager<JsonProductSaleAnalysis>(true) {
            @Override
            public void onJsonSuccess(JsonProductSaleAnalysis jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    String totalTitle = MessageFormat.format(getString(R.string.chart_title_03),
                            getString(R.string.chart_total_revenue), jo.getTotalIn());
                    SpannableStringBuilder ssbTotalTitle = new SpannableStringBuilder(totalTitle);
                    int totalBlueIdx = totalTitle.indexOf(jo.getTotalIn());
                    ssbTotalTitle.setSpan(new ForegroundColorSpan(getColor(R.color.chart_02_blue_font)), totalBlueIdx, totalTitle.length(),
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    LinkedList<PieData> totalChartData = new LinkedList<>();
                    for (int i = 0; i < jo.getTotalList().size(); i++) {
                        JsonProductSaleAnalysis.TotalItem totalItem = jo.getTotalList().get(i);
                        PieData pieData = new PieData();
                        pieData.setKey(totalItem.getTotalType());

                        String valueAndPercent = MessageFormat.format(getString(R.string.chart_label_format_03),
                                totalItem.getTotalAmt(), totalItem.getTotalPercent() + Constants.STR_SYMBOL_PERCENT);

                        String label = totalItem.getTotalType() + Constants.STR_LINE_BREAK + valueAndPercent;
                        pieData.setLabel(label);
                        double percent = 0;
                        if (StringUtils.isNotEmpty(totalItem.getTotalPercent())) {
                            percent = Double.valueOf(totalItem.getTotalPercent());
                        }

                        pieData.setPercentage(percent);
                        pieData.setSliceColor(AppUtils.getColorFromRGBString(totalItem.getTotalTypeColor()));
                        totalChartData.add(pieData);
                    }

                    View viSeparator = AppUtils.getSeparatorView(getActivity(), DensityUtil.getActualHeightOnThisDevice(20, mContext));
                    llContainer.addView(viSeparator);

                    generatePie(ssbTotalTitle, totalChartData);

                    for (int i = 0; i < jo.getTotalSubDataList().size(); i++) {
                        JsonProductSaleAnalysis.TotalSubItem subItem = jo.getTotalSubDataList().get(i);
                        String totalSubTypeAmt = subItem.getTotalSubTypeAmt();
                        if (StringUtils.isEmpty(totalSubTypeAmt) || Constants.STR_0.equals(totalSubTypeAmt)) {
                            continue;
                        }
                        String subTitle = subItem.getTotalSubType() + Constants.STR_COLON + subItem.getTotalSubTypeAmt();
                        SpannableStringBuilder ssbSubTitle = new SpannableStringBuilder(subTitle);
                        int subBlueIdx = subTitle.indexOf(subItem.getTotalSubTypeAmt());
                        ssbSubTitle.setSpan(new ForegroundColorSpan(getColor(R.color.chart_02_blue_font)), subBlueIdx, subTitle.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                        LinkedList<PieData> subChartData = new LinkedList<>();
                        for (JsonProductSaleAnalysis.TotalSubItem.TotalSubItemTypeData typeData : subItem.getTypeList()) {

                            PieData pieData = new PieData();
                            pieData.setKey(typeData.getMemberType());
                            String label = typeData.getMemberType() + Constants.STR_LINE_BREAK
                                    + typeData.getMemberTypeAmt() + Constants.STR_SPACE
                                    + typeData.getMemberTypePercent() + Constants.STR_SYMBOL_PERCENT;
                            pieData.setLabel(label);
                            double percent = 0;
                            if (StringUtils.isNotEmpty(typeData.getMemberTypePercent())) {
                                percent = Double.valueOf(typeData.getMemberTypePercent());
                            }

                            pieData.setPercentage(percent);
                            pieData.setSliceColor(AppUtils.getColorFromRGBString(typeData.getMemberTypeColor()));
                            subChartData.add(pieData);
                        }
                        generatePie(ssbSubTitle, subChartData);
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
        hh.startGet(getActivity(), ApiManager.HttpApi.ProductSaleAnalysis, params);
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

    @Override
    public void setDatePeriod(String startDate, String endDate ,String selectedMethod) {
        this.beginDate = startDate;
        this.endDate = endDate;
        this.selectedMethod = selectedMethod;
        isChanged = true;
    }
}