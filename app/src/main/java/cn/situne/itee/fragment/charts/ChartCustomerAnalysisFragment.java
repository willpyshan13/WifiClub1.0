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
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;
import org.xclcharts.chart.PieData;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCustomerAnalysis;
import cn.situne.itee.view.IteeTextView;
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
public class ChartCustomerAnalysisFragment extends BaseFragment implements IChartGenerate {

    private LinearLayout llContainer;

    private boolean isChanged = true;

    private String beginDate;
    private String endDate;
    private String selectedMethod;

    private String showDiscount="0";

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
            getCustomerAnalysis();
        }
    }

    private void getCustomerAnalysis() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COMMON_BEGIN_DATE, beginDate);
        params.put(ApiKey.COMMON_END_DATE, endDate);
        params.put(ApiKey.COMMON_US_ID, "1");

        HttpManager<JsonCustomerAnalysis> hh = new HttpManager<JsonCustomerAnalysis>(true) {
            @Override
            public void onJsonSuccess(JsonCustomerAnalysis jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    llContainer.removeAllViews();
                    LinearLayout llTotalInfo = generateTotalInfoLayout(jo);
                    llContainer.addView(llTotalInfo);

                    LinearLayout llTypeList = generatePayMethodListLayout(jo.getPayList());
                    llContainer.addView(llTypeList);

                    View viSeparator = AppUtils.getSeparatorView(getActivity(), DensityUtil.getActualHeightOnThisDevice(20, mContext));
                    llContainer.addView(viSeparator);

                    LinkedList<PieData> totalChartData = new LinkedList<>();
                    ArrayList<JsonCustomerAnalysis.TotalItem> totalList = jo.getTotalList();
                    for (int i = 0; i < totalList.size(); i++) {
                        JsonCustomerAnalysis.TotalItem totalItem = totalList.get(i);
                        PieData pieData = new PieData();
                        pieData.setKey(totalItem.getMemType());
                        String label = MessageFormat.format(getString(R.string.chart_label_format_02), totalItem.getMemType(), totalItem.getMemQty()
                                , totalItem.getMemAmt(),
                                totalItem.getMemPercent() + Constants.STR_SYMBOL_PERCENT, Constants.STR_LINE_BREAK);
                        pieData.setLabel(label);

                        double percent = 0;
                        if (StringUtils.isNotEmpty(totalItem.getMemPercent())) {
                            percent = Double.valueOf(totalItem.getMemPercent());
                        }
                        pieData.setPercentage(percent);
                        pieData.setSliceColor(AppUtils.getColorFromRGBString(totalItem.getMemColor()));
                        totalChartData.add(pieData);
                    }
                    generatePie(Constants.STR_EMPTY, totalChartData);
                    LinearLayout totalLayout = generateTotalLayout(jo);
                    llContainer.addView(totalLayout);

                    View totalSeparator = AppUtils.getSeparatorView(getActivity(), DensityUtil.getActualHeightOnThisDevice(20, mContext));
                    llContainer.addView(totalSeparator);
                    for (int i = 0; i < totalList.size(); i++) {
                        JsonCustomerAnalysis.TotalItem totalItem = totalList.get(i);
                        String subTitle = totalItem.getMemType();
                        if (totalItem.getTypeList().size() > 0) {
                            LinkedList<PieData> subChartData = new LinkedList<>();
                            for (JsonCustomerAnalysis.TotalItem.TypeItem typeItem : totalItem.getTypeList()) {
                                PieData pieData = new PieData();
                                pieData.setKey(typeItem.getMemSubType());
                                String label = typeItem.getMemSubType() + Constants.STR_LINE_BREAK
                                        + typeItem.getMemSubTypeAmt() + Constants.STR_SPACE
                                        + typeItem.getMemSubTyPercent() + Constants.STR_SYMBOL_PERCENT;
                                pieData.setLabel(label);
                                double percent = 0;
                                if (StringUtils.isNotEmpty(typeItem.getMemSubTyPercent())) {
                                    percent = Double.valueOf(typeItem.getMemSubTyPercent());
                                }
                                pieData.setPercentage(percent);
                                pieData.setSliceColor(AppUtils.getColorFromRGBString(typeItem.getMemSubTypeColor()));
                                subChartData.add(pieData);
                            }
                            generatePie(subTitle, subChartData);
                        } else {
                            RelativeLayout rlOtherTitle = new RelativeLayout(mContext);
                            IteeTextView tvOtherTitle = new IteeTextView(mContext);
                            rlOtherTitle.addView(tvOtherTitle);
                            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvOtherTitle, 20, mContext);
                            tvOtherTitle.setText(totalItem.getMemType());
                            tvOtherTitle.setTextColor(getColor(R.color.common_black));
                            llContainer.addView(rlOtherTitle);

                            LayoutUtils.setLayoutHeight(rlOtherTitle, 70, mContext);
                            rlOtherTitle.setBackgroundColor(getColor(R.color.common_light_gray));
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rlOtherTitle.getLayoutParams();
                            layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
                            layoutParams.rightMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
                        }
                        LinearLayout otherLayout = generateOtherLayout(totalItem);
                        llContainer.addView(otherLayout);

                        View pieSeparator = AppUtils.getSeparatorView(getActivity(), DensityUtil.getActualHeightOnThisDevice(20, mContext));
                        llContainer.addView(pieSeparator);
                    }
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.startGet(getActivity(), ApiManager.HttpApi.CustomerAnalysis, params);
    }

    private LinearLayout generateOtherLayout(JsonCustomerAnalysis.TotalItem totalItem) {
        LinearLayout llOtherContainer = new LinearLayout(mContext);
        llOtherContainer.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout rlRevenue = new RelativeLayout(mContext);
        llOtherContainer.addView(rlRevenue);
        rlRevenue.setBackgroundColor(getColor(R.color.common_light_gray));
        LayoutUtils.setLayoutHeight(rlRevenue, 50, mContext);

        IteeTextView tvRevenue = new IteeTextView(mContext);
        tvRevenue.setGravity(Gravity.CENTER_VERTICAL);
        tvRevenue.setTextColor(getColor(R.color.common_deep_gray));
        tvRevenue.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        rlRevenue.addView(tvRevenue);

        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvRevenue, 20, mContext);

        String revenueValue = getString(R.string.chart_revenue_from) + Constants.STR_SPACE + totalItem.getMemType()
                + Constants.STR_DOUBLE_SPACE + totalItem.getMemAmt();
        SpannableStringBuilder ssbRevenue = new SpannableStringBuilder(revenueValue);
        int revenueBlueEndIdx = revenueValue.indexOf(totalItem.getMemAmt());
        ssbRevenue.setSpan(new ForegroundColorSpan(getColor(R.color.chart_02_blue_font)), 0, revenueBlueEndIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRevenue.setText(ssbRevenue);

        RelativeLayout rlCheckIn = new RelativeLayout(mContext);
        llOtherContainer.addView(rlCheckIn);
        rlCheckIn.setBackgroundColor(getColor(R.color.common_light_gray));
        LayoutUtils.setLayoutHeight(rlCheckIn, 50, mContext);

        IteeTextView tvCheckIn = new IteeTextView(mContext);
        tvCheckIn.setGravity(Gravity.CENTER_VERTICAL);
        tvCheckIn.setTextColor(getColor(R.color.common_deep_gray));
        tvCheckIn.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        rlCheckIn.addView(tvCheckIn);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvCheckIn, 20, mContext);

        String checkInValue = getString(R.string.chart_check_in) + Constants.STR_DOUBLE_SPACE
                + Constants.STR_DOUBLE_SPACE + totalItem.getMemQty();
        SpannableStringBuilder ssbCheckIn = new SpannableStringBuilder(checkInValue);
        int checkInBlueEndIdx = checkInValue.indexOf(totalItem.getMemQty());
        ssbCheckIn.setSpan(new ForegroundColorSpan(getColor(R.color.chart_02_blue_font)), 0, checkInBlueEndIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCheckIn.setText(ssbCheckIn);

        RelativeLayout rlPerCapita = new RelativeLayout(mContext);
        llOtherContainer.addView(rlPerCapita);
        rlPerCapita.setBackgroundColor(getColor(R.color.chart_02_per_capita_background));
        LayoutUtils.setLayoutHeight(rlPerCapita, 50, mContext);

        IteeTextView tvPerCapita = new IteeTextView(mContext);
        tvPerCapita.setGravity(Gravity.CENTER_VERTICAL);
        tvPerCapita.setTextColor(getColor(R.color.common_deep_gray));
        tvPerCapita.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        rlPerCapita.addView(tvPerCapita);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvPerCapita, 20, mContext);
        tvPerCapita.setText(getString(R.string.chart_per_capita) + Constants.STR_DOUBLE_SPACE + totalItem.getMemAvg());


        int padding = DensityUtil.getActualWidthOnThisDevice(10, mContext);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.leftMargin = padding;
        layoutParams.rightMargin = padding;

        llOtherContainer.setPadding(padding, 0, padding, DensityUtil.getActualHeightOnThisDevice(2, mContext));

        return llOtherContainer;
    }

    private LinearLayout generateTotalLayout(JsonCustomerAnalysis jo) {
        LinearLayout llOtherContainer = new LinearLayout(mContext);
        llOtherContainer.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout rlRevenue = new RelativeLayout(mContext);
        llOtherContainer.addView(rlRevenue);
        rlRevenue.setBackgroundColor(getColor(R.color.common_light_gray));
        LayoutUtils.setLayoutHeight(rlRevenue, 50, mContext);

        IteeTextView tvRevenue = new IteeTextView(mContext);
        tvRevenue.setGravity(Gravity.CENTER_VERTICAL);
        tvRevenue.setTextColor(getColor(R.color.common_deep_gray));
        tvRevenue.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        rlRevenue.addView(tvRevenue);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvRevenue, 20, mContext);

        String revenueValue = getString(R.string.chart_total) + Constants.STR_DOUBLE_SPACE + jo.getTotalPay()
                + Constants.STR_DOUBLE_SPACE;
        SpannableStringBuilder ssbRevenue = new SpannableStringBuilder(revenueValue);
        int revenueBlueEndIdx = revenueValue.indexOf(jo.getTotalPay());
        ssbRevenue.setSpan(new ForegroundColorSpan(getColor(R.color.chart_02_blue_font)), 0, revenueBlueEndIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvRevenue.setText(ssbRevenue);

        RelativeLayout rlBalance = new RelativeLayout(mContext);
        llOtherContainer.addView(rlBalance);
        rlBalance.setBackgroundColor(getColor(R.color.common_light_gray));
        LayoutUtils.setLayoutHeight(rlBalance, 50, mContext);

        IteeTextView tvBalance = new IteeTextView(mContext);
        tvBalance.setGravity(Gravity.CENTER_VERTICAL);
        tvBalance.setTextColor(getColor(R.color.common_deep_gray));
        tvBalance.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        rlBalance.addView(tvBalance);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(tvBalance, 20, mContext);

//        double balance = Double.valueOf(jo.getTotalPay().replace(Constants.STR_COMMA, Constants.STR_EMPTY))
//                - Double.valueOf(jo.getTotalIn().replace(Constants.STR_COMMA, Constants.STR_EMPTY));

String tmp = jo.getTotalPay();

        String valueBalance = Constants.STR_BRACKETS_START
                + getString(R.string.chart_account_receivable) + Constants.STR_SPACE
                + Constants.STR_COLON + Constants.STR_SPACE
                + Utils.get2DigitDecimalString(String.valueOf( Double.parseDouble( tmp.replace(Constants.STR_COMMA, Constants.STR_EMPTY))
                +Double.parseDouble(showDiscount.replace(Constants.STR_COMMA, Constants.STR_EMPTY))))
                + Constants.STR_SPACE + Constants.STR_COMMA + Constants.STR_SPACE
                + getString(R.string.chart_analysis_discount) + Constants.STR_SPACE + Constants.STR_COLON
                + Constants.STR_SPACE + showDiscount
                + Constants.STR_BRACKETS_END;
        tvBalance.setText(valueBalance);

        RelativeLayout rlCheckIn = new RelativeLayout(mContext);
        llOtherContainer.addView(rlCheckIn);
        rlCheckIn.setBackgroundColor(getColor(R.color.common_light_gray));
        LayoutUtils.setLayoutHeight(rlCheckIn, 50, mContext);

        IteeTextView tvCheckIn = new IteeTextView(mContext);
        tvCheckIn.setGravity(Gravity.CENTER_VERTICAL);
        tvCheckIn.setTextColor(getColor(R.color.common_deep_gray));
        tvCheckIn.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        rlCheckIn.addView(tvCheckIn);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvCheckIn, 20, mContext);

        String checkInValue = getString(R.string.chart_check_in) + Constants.STR_DOUBLE_SPACE
                + Constants.STR_DOUBLE_SPACE + jo.getTotalQty();
        SpannableStringBuilder ssbCheckIn = new SpannableStringBuilder(checkInValue);
        int checkInBlueEndIdx = checkInValue.indexOf(jo.getTotalQty());
        ssbCheckIn.setSpan(new ForegroundColorSpan(getColor(R.color.chart_02_blue_font)), 0, checkInBlueEndIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvCheckIn.setText(ssbCheckIn);


        RelativeLayout rlPerCapita = new RelativeLayout(mContext);
        llOtherContainer.addView(rlPerCapita);
        rlPerCapita.setBackgroundColor(getColor(R.color.chart_02_per_capita_background));
        LayoutUtils.setLayoutHeight(rlPerCapita, 50, mContext);

        IteeTextView tvPerCapita = new IteeTextView(mContext);
        tvPerCapita.setGravity(Gravity.CENTER_VERTICAL);
        tvPerCapita.setTextColor(getColor(R.color.common_deep_gray));
        tvPerCapita.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);
        rlPerCapita.addView(tvPerCapita);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvPerCapita, 20, mContext);
        tvPerCapita.setText(getString(R.string.chart_per_capita) + Constants.STR_DOUBLE_SPACE + jo.getTotalAvg());


        int padding = DensityUtil.getActualWidthOnThisDevice(10, mContext);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.leftMargin = padding;
        layoutParams.rightMargin = padding;

        llOtherContainer.setPadding(padding, 0, padding, DensityUtil.getActualHeightOnThisDevice(2, mContext));

        return llOtherContainer;
    }

    private void generatePie(String title, LinkedList<PieData> chartData) {
        int size = (int) (DensityUtil.getScreenWidth(mContext) * 0.8);
        PieChartView pieChartView = new PieChartView(getActivity());
        llContainer.addView(pieChartView);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) pieChartView.getLayoutParams();
        layoutParams.height = DensityUtil.getActualHeightOnThisDevice(1400, mContext);
        layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
        layoutParams.rightMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
        pieChartView.setLayoutParams(layoutParams);
        pieChartView.setChartSize(size, DensityUtil.getActualHeightOnThisDevice(3400, mContext));
        pieChartView.setChartData(chartData);
        pieChartView.setTitle(title);
    }

    @Override
    public void setDatePeriod(String startDate, String endDate ,String selectedMethod) {
        this.beginDate = startDate;
        this.endDate = endDate;
        this.selectedMethod = selectedMethod;
        isChanged = true;
    }


    public void setSelectedMethod(String selectedMethod) {
        this.selectedMethod = selectedMethod;
    }

    private LinearLayout generatePayMethodListLayout(ArrayList<JsonCustomerAnalysis.PayItem> payItemArrayList) {

        LinearLayout llTypeListContainer = new LinearLayout(mContext);
        llTypeListContainer.setBackgroundColor(getColor(R.color.common_light_gray));
        llTypeListContainer.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams paramsLayout = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        paramsLayout.leftMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
        paramsLayout.rightMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
        paramsLayout.bottomMargin = DensityUtil.getActualHeightOnThisDevice(20, mContext);
        llTypeListContainer.setLayoutParams(paramsLayout);

        LinearLayout llRow = null;
        for (int i = 0; i < payItemArrayList.size(); i++) {
            if (i % 3 == 0) {
                llRow = new LinearLayout(mContext);
                llTypeListContainer.addView(llRow);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llRow.getLayoutParams();
                layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
                layoutParams.rightMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
                layoutParams.height = DensityUtil.getActualHeightOnThisDevice(60, mContext);
            }
            JsonCustomerAnalysis.PayItem payItem = payItemArrayList.get(i);

            if ("7".equals(payItem.getPaymentId())){

                showDiscount = payItem.getPaymentAmt();
            }
            IteeTextView tvType = new IteeTextView(mContext);
            tvType.setTextColor(getColor(R.color.common_deep_gray));
            tvType.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
            tvType.setTextSize(Constants.FONT_SIZE_SMALLEST);


            String value = payItem.getPayment() + Constants.STR_DOUBLE_SPACE + payItem.getPaymentAmt();
            SpannableStringBuilder ssbValue = new SpannableStringBuilder(value);
            int blueEndIdx = value.indexOf(payItem.getPaymentAmt());
            ssbValue.setSpan(new ForegroundColorSpan(getColor(R.color.chart_02_blue_font)), 0, blueEndIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvType.setText(ssbValue);

            LinearLayout.LayoutParams layoutParams
                    = new LinearLayout.LayoutParams(0, DensityUtil.getActualHeightOnThisDevice(60, mContext));
            layoutParams.weight = 1;
            tvType.setLayoutParams(layoutParams);
            if (llRow != null) {
                llRow.addView(tvType);
                if (i % 3 != 0 && i == payItemArrayList.size() - 1) {
                    for (int j = 0; j < 3 - llRow.getChildCount(); j++) {
                        IteeTextView tvSpace = new IteeTextView(mContext);
                        LinearLayout.LayoutParams spaceLayoutParams
                                = new LinearLayout.LayoutParams(0, DensityUtil.getActualHeightOnThisDevice(60, mContext));
                        spaceLayoutParams.weight = 1;
                        tvSpace.setLayoutParams(spaceLayoutParams);
                        llRow.addView(tvSpace);
                    }
                }
            }
        }
        LinearLayout llFooter = new LinearLayout(mContext);
        llTypeListContainer.addView(llFooter);
        LayoutUtils.setLinearLayoutHeight(llFooter, 20, mContext);
        return llTypeListContainer;
    }

    private LinearLayout generateTotalInfoLayout(JsonCustomerAnalysis jo) {

        LinearLayout llTotalInfoContainer = new LinearLayout(mContext);
        llTotalInfoContainer.setBackgroundColor(getColor(R.color.common_light_gray));

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
        layoutParams.rightMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
        llTotalInfoContainer.setLayoutParams(layoutParams);

        RelativeLayout rlTotalInfo = new RelativeLayout(mContext);
        IteeTextView tvTotalAmt = new IteeTextView(mContext);

        LinearLayout llCompareResult = new LinearLayout(mContext);
        llCompareResult.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams paramCompareResult = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        llCompareResult.setLayoutParams(paramCompareResult);

        llCompareResult.setGravity(Gravity.CENTER_VERTICAL);

        IteeTextView tvBraceStart = new IteeTextView(mContext);
        IteeTextView tvBraceEnd = new IteeTextView(mContext);
        IteeTextView tvDayOrWeekResult = new IteeTextView(mContext);
        IteeTextView tvMonthResult = new IteeTextView(mContext);
        IteeTextView tvYearResult = new IteeTextView(mContext);

        tvTotalAmt.setId(View.generateViewId());

        tvTotalAmt.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvDayOrWeekResult.setTextSize(Constants.FONT_SIZE_SMALLEST);
        tvMonthResult.setTextSize(Constants.FONT_SIZE_SMALLEST);
        tvYearResult.setTextSize(Constants.FONT_SIZE_SMALLEST);
        tvBraceStart.setTextSize(Constants.FONT_SIZE_SMALLEST);
        tvBraceEnd.setTextSize(Constants.FONT_SIZE_SMALLEST);

        tvBraceStart.setText(Constants.STR_BRACKETS_START);
        tvBraceEnd.setText(Constants.STR_BRACKETS_END);

        String totalAmt = getString(R.string.chart_account_receivable) + Constants.STR_SPACE + Constants.STR_COLON + Constants.STR_SPACE + jo.getTotalIn();
        SpannableStringBuilder ssbValue = new SpannableStringBuilder(totalAmt);
        int blueStartIdx = totalAmt.indexOf(jo.getTotalIn());
        int blueEndIdx = blueStartIdx + jo.getTotalIn().length();
        ssbValue.setSpan(new ForegroundColorSpan(getColor(R.color.chart_02_blue_font)), blueStartIdx, blueEndIdx, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTotalAmt.setText(ssbValue);

        LinearLayout.LayoutParams compareResultParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        tvDayOrWeekResult.setLayoutParams(compareResultParams);
        tvMonthResult.setLayoutParams(compareResultParams);
        tvYearResult.setLayoutParams(compareResultParams);
        tvBraceStart.setLayoutParams(compareResultParams);
        tvBraceEnd.setLayoutParams(compareResultParams);

        tvMonthResult.setText(getString(R.string.chart_last_month) + Constants.STR_COLON);
        tvYearResult.setText(getString(R.string.chart_last_year) + Constants.STR_COLON);

        final int INDEX_YEAR = 2;
        final int INDEX_MONTH = 2;
        final int INDEX_WEEK = 1;
        final int INDEX_DAY = 0;

//        if (Utils.isListNotNullOrEmpty(jo.getCompareList())) {
//            if (Constants.CHART_SELECT_METHOD_YEAR.equals(selectedMethod)) {
//                llCompareResult.addView(tvBraceStart);
//                llCompareResult.addView(tvYearResult);
//                llCompareResult.addView(getSymbolOfInOut(jo.getCompareList().get(INDEX_YEAR).getCompareResult()));
//                llCompareResult.addView(tvBraceEnd);
//            } else if (Constants.CHART_SELECT_METHOD_MONTH.equals(selectedMethod)) {
//                llCompareResult.addView(tvBraceStart);
//                llCompareResult.addView(tvMonthResult);
//                llCompareResult.addView(getSymbolOfInOut(jo.getCompareList().get(INDEX_MONTH).getCompareResult()));
//                llCompareResult.addView(generateSpace());
//                llCompareResult.addView(tvYearResult);
//                llCompareResult.addView(getSymbolOfInOut(jo.getCompareList().get(INDEX_YEAR).getCompareResult()));
//                llCompareResult.addView(tvBraceEnd);
//            } else if (Constants.CHART_SELECT_METHOD_WEEK.equals(selectedMethod)) {
//                tvDayOrWeekResult.setText(getString(R.string.chart_last_week) + Constants.STR_COLON);
//                llCompareResult.addView(tvBraceStart);
//                llCompareResult.addView(tvDayOrWeekResult);
//                llCompareResult.addView(getSymbolOfInOut(jo.getCompareList().get(INDEX_WEEK).getCompareResult()));
//                llCompareResult.addView(generateSpace());
//                llCompareResult.addView(tvMonthResult);
//                llCompareResult.addView(getSymbolOfInOut(jo.getCompareList().get(INDEX_MONTH).getCompareResult()));
//                llCompareResult.addView(generateSpace());
//                llCompareResult.addView(tvYearResult);
//                llCompareResult.addView(getSymbolOfInOut(jo.getCompareList().get(INDEX_YEAR).getCompareResult()));
//                llCompareResult.addView(tvBraceEnd);
//            } else {
//                tvDayOrWeekResult.setText(getString(R.string.chart_yesterday) + Constants.STR_COLON);
//
//                llCompareResult.addView(tvBraceStart);
//                llCompareResult.addView(tvDayOrWeekResult);
//                llCompareResult.addView(getSymbolOfInOut(jo.getCompareList().get(INDEX_DAY).getCompareResult()));
//                llCompareResult.addView(generateSpace());
//                llCompareResult.addView(tvMonthResult);
//                llCompareResult.addView(getSymbolOfInOut(jo.getCompareList().get(INDEX_MONTH).getCompareResult()));
//                llCompareResult.addView(generateSpace());
//                llCompareResult.addView(tvYearResult);
//                llCompareResult.addView(getSymbolOfInOut(jo.getCompareList().get(INDEX_YEAR).getCompareResult()));
//                llCompareResult.addView(tvBraceEnd);
//            }
//        }

        rlTotalInfo.addView(tvTotalAmt);
//        rlTotalInfo.addView(llCompareResult);

        LayoutUtils.setViewWidthAndHeightInRelativeLayout(tvTotalAmt, WRAP_CONTENT, DensityUtil.getActualHeightOnThisDevice(70, mContext));
//        LayoutUtils.setViewWidthAndHeightInRelativeLayout(llCompareResult, WRAP_CONTENT, MATCH_PARENT);

//        LayoutUtils.setBelowOfView(llCompareResult, tvTotalAmt, 2, mContext);

        RelativeLayout.LayoutParams paramsTvTotalAmt = (RelativeLayout.LayoutParams) tvTotalAmt.getLayoutParams();
        paramsTvTotalAmt.leftMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
        paramsTvTotalAmt.width = (int) (DensityUtil.getScreenWidth(mContext) * 0.9);
        tvTotalAmt.setLayoutParams(paramsTvTotalAmt);

        llTotalInfoContainer.addView(rlTotalInfo);

//        LayoutUtils.setLayoutHeight(rlTotalInfo, 120, mContext);
        LayoutUtils.setLayoutHeight(rlTotalInfo, 70, mContext);

        return llTotalInfoContainer;
    }

    private ImageView getSymbolOfInOut(String compareResult) {
        ImageView tvImage = new ImageView(mContext);
        LinearLayout.LayoutParams layoutParams
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 50);
        layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(10, mContext);
        tvImage.setLayoutParams(layoutParams);
        if (Constants.STR_0.equals(compareResult)) {
            tvImage.setImageResource(R.drawable.icon_chart_equal);
        } else if (Constants.STR_1.equals(compareResult)) {
            tvImage.setImageResource(R.drawable.icon_chart_raise);
        } else {
            tvImage.setImageResource(R.drawable.icon_chart_drop);
        }
        return tvImage;
    }

    private IteeTextView generateSpace() {
        IteeTextView tvSpace = new IteeTextView(mContext);
        tvSpace.setText(Constants.STR_DOUBLE_SPACE);
        return tvSpace;
    }
}