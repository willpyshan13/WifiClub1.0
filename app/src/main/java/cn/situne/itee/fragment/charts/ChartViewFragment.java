/**
 * Project Name: itee
 * File Name:	 ChartViewFragment.java
 * Package Name: cn.situne.itee.fragment.charts
 * Date:		 2015-08-18
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.charts;

import android.app.ActionBar;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.PopChartMethodMenuView;
import cn.situne.itee.view.TeeTimeCalendarPopupWindowView;

/**
 * ClassName:ChartViewFragment <br/>
 * Function: char view. <br/>
 * Date: 2015-08-18 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class ChartViewFragment extends BaseFragment {
    private ViewPager vpChartView;
    private ChartPagerAdapter pagerAdapter;
    private ArrayList<BaseFragment> fragmentList;
    private TeeTimeCalendarPopupWindowView calendarPopupWindowView;
    private PopChartMethodMenuView menuWindow;

    private AppUtils.NoDoubleClickListener menuItemOnClick;
    private IteeTextView tvSelectedMethod;

    private String selectedDate;
    private String selectedMethod;
    private String startDate;
    private String endDate;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_chart_view;
    }

    @Override
    public int getTitleResourceId() {
       return R.string.chart_view_title;
    }

    @Override
    protected void initControls(View rootView) {
        vpChartView = (ViewPager) rootView.findViewById(R.id.vp_chart_view);
        String b = "";
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        menuItemOnClick = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                menuWindow.dismiss();
                switch (v.getId()) {
                    case R.id.ll_year:
                        selectedMethod = Constants.CHART_SELECT_METHOD_YEAR;
                        break;
                    case R.id.ll_month:
                        selectedMethod = Constants.CHART_SELECT_METHOD_MONTH;
                        break;
                    case R.id.ll_week:
                        selectedMethod = Constants.CHART_SELECT_METHOD_WEEK;
                        break;
                    case R.id.ll_day:
                        selectedMethod = Constants.CHART_SELECT_METHOD_DAY;
                        break;
                    default:
                        break;
                }
                changeSelectedMethod();
                calcDatePeriod(selectedDate);
                getTvTitle().setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(selectedDate, mContext));
                generateCharts();
            }
        };
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
//        setTeeTimeActionBar();
        setChartActionBar();
        if (StringUtils.isEmpty(selectedDate)) {
            selectedDate = AppUtils.getTodaySlash();
        }
        getTvTitle().setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(selectedDate, mContext));
        getTvTitle().setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            //region getTvTitle().setOnClickListener
            @Override
            public void noDoubleClick(View v) {
                // show calendar
                calendarPopupWindowView = new TeeTimeCalendarPopupWindowView(ChartViewFragment.this, getTvRight(), selectedDate,false);
                calendarPopupWindowView.setDateClickListener(new TeeTimeCalendarPopupWindowView.DateClickListener() {
                    @Override
                    public void onDateClick(String date) {
                        if (date != null && !date.equals(selectedDate)) {
                            selectedDate = date;
                            calcDatePeriod(selectedDate);
                            getTvTitle().setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(selectedDate, mContext));
                            generateCharts();
                        }
                    }
                });
            }
            //endregion
        });

        getTvRight().setBackgroundResource(R.drawable.icon_over_flow);
        getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            //region getTvRight().setOnClickListener
            @Override
            public void noDoubleClick(View v) {
                //实例化SelectPicPopupWindow
                menuWindow = new PopChartMethodMenuView(getActivity(), menuItemOnClick);
                //显示窗口
                ActionBar actionBar = getActivity().getActionBar();
                if (actionBar != null) {
                    menuWindow.showAsDropDown(actionBar.getCustomView(), getScreenWidth(), 0);
                } else {
                    menuWindow.showAsDropDown(getTvRight(), 0, 0);
                }
            }
            //endregion
        });

        RelativeLayout parent = (RelativeLayout) getTvRight().getParent();
        tvSelectedMethod = new IteeTextView(this);
        tvSelectedMethod.setTextColor(Color.WHITE);
        parent.addView(tvSelectedMethod);

        LayoutUtils.setLeftOfView(tvSelectedMethod, getTvRight(), 10, mContext);
        LayoutUtils.setCenterVertical(tvSelectedMethod);
        changeSelectedMethod();
    }

    @Override
    protected void executeOnceOnCreate() {
        super.executeOnceOnCreate();
        selectedDate = AppUtils.getTodaySlash();
        calcDatePeriod(selectedDate);
        getTvTitle().setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(selectedDate, mContext));
        generateCharts();
    }

    private void generateCharts() {
        fragmentList = new ArrayList<>();

        ChartCustomerAnalysisFragment chartCustomerAnalysisFragment = new ChartCustomerAnalysisFragment();
        chartCustomerAnalysisFragment.setDatePeriod(startDate, endDate ,selectedMethod);
        chartCustomerAnalysisFragment.setSelectedMethod(selectedMethod);
        fragmentList.add(chartCustomerAnalysisFragment);

        ChartProductSellingFragment chartProductSellingFragment = new ChartProductSellingFragment();
        chartProductSellingFragment.setDatePeriod(startDate, endDate,selectedMethod);
        fragmentList.add(chartProductSellingFragment);

        ChartCustomerConsumeAnalysisFragment chartCustomerConsumeAnalysisFragment = new ChartCustomerConsumeAnalysisFragment();
        chartCustomerConsumeAnalysisFragment.setDatePeriod(startDate, endDate,selectedMethod);
        fragmentList.add(chartCustomerConsumeAnalysisFragment);

        pagerAdapter = new ChartPagerAdapter(getChildFragmentManager(), fragmentList);
        vpChartView.setAdapter(pagerAdapter);
        pagerAdapter.notifyDataSetChanged();
    }

    private void calcDatePeriod(String date) {
        if (StringUtils.isEmpty(selectedMethod)) {
            selectedMethod = Constants.CHART_SELECT_METHOD_DEFAULT;
        }
        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();
        Date currentDate = DateUtils.getDateFromAPIYearMonthDay(date);
        startCalendar.setTime(currentDate);
        endCalendar.setTime(currentDate);
        if (Constants.CHART_SELECT_METHOD_YEAR.equals(selectedMethod)) {
            startCalendar.set(Calendar.MONTH, Calendar.JANUARY);
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);
            endCalendar.set(Calendar.YEAR, startCalendar.get(Calendar.YEAR) + 1);
            endCalendar.set(Calendar.MONTH, Calendar.JANUARY);
            endCalendar.set(Calendar.DAY_OF_MONTH, 1);
        } else if (Constants.CHART_SELECT_METHOD_MONTH.equals(selectedMethod)) {
            startCalendar.set(Calendar.DAY_OF_MONTH, 1);
            endCalendar.set(Calendar.MONTH, startCalendar.get(Calendar.MONTH) + 1);
            endCalendar.set(Calendar.DAY_OF_MONTH, 1);
        } else if (Constants.CHART_SELECT_METHOD_WEEK.equals(selectedMethod)) {

            String currentFirstDayOfWeek = AppUtils.getCurrentFirstDayOfWeek(mContext);
            if (Constants.FIRST_DAY_MON.equals(currentFirstDayOfWeek)) {
                startCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            } else {
                startCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            }
            endCalendar.set(Calendar.DAY_OF_YEAR, startCalendar.get(Calendar.DAY_OF_YEAR) + 7);
        } else {
            endCalendar.set(Calendar.DAY_OF_YEAR, startCalendar.get(Calendar.DAY_OF_YEAR) + 1);
        }

        startDate = DateUtils.getAPIYearMonthDay(startCalendar.getTime());
        endDate = DateUtils.getAPIYearMonthDay(endCalendar.getTime());

        Utils.log("startDate : " + startDate);
        Utils.log("endDate : " + endDate);
    }

    private void changeSelectedMethod() {
        if (StringUtils.isEmpty(selectedMethod)) {
            selectedMethod = Constants.CHART_SELECT_METHOD_DEFAULT;
        }
        if (Constants.CHART_SELECT_METHOD_YEAR.equals(selectedMethod)) {
            tvSelectedMethod.setText(R.string.common_year);
        } else if (Constants.CHART_SELECT_METHOD_MONTH.equals(selectedMethod)) {
            tvSelectedMethod.setText(R.string.common_month);
        } else if (Constants.CHART_SELECT_METHOD_WEEK.equals(selectedMethod)) {
            tvSelectedMethod.setText(R.string.common_week);
        } else {
            tvSelectedMethod.setText(R.string.common_day);
        }
    }

    class ChartPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<BaseFragment> list;
        private FragmentManager fm;
        public ChartPagerAdapter(FragmentManager fm, ArrayList<BaseFragment> list) {
            super(fm);
            this.fm = fm;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int arg0) {
            return list.get(arg0);
        }
    }
}