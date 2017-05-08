/**
 * Project Name: itee
 * File Name:	 TeeTimePageItemFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-01-26
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.teetime;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.TeeTimePageItemAdapter;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonMain;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:TeeTimePageItemFragment <br/>
 * Function: item of main page. <br/>
 * UI:  03-1
 * Date: 2015-01-26 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class TeeTimePageItemFragment extends BaseFragment {

    private String selectedDate;
    // pull list view
    private PullToRefreshListView refreshListViewBookingInfo;

    private String leftCourseArea;
    private String rightCourseArea;

    private int leftCourseAreaId;
    private int rightCourseAreaId;

    private boolean isLeftLock;
    private boolean isRightLock;

    private int startHour;
    private int endHour;
    private int gapTime;
    private int currentPage;

    private IteeTextView tvLeftCourseArea;
    private IteeTextView tvRightCourseArea;

    private ImageView ivLeftCourseLock;
    private ImageView ivRightCourseLock;

    private ArrayList<String> showTimeList;
    public ArrayList<String> getShowTimeList() {
        return showTimeList;
    }

    public void setShowTimeList(ArrayList<String> showTimeList) {
        this.showTimeList = showTimeList;
    }

    private boolean isUserShowTime;
    public boolean isUserShowTime() {
        return isUserShowTime;
    }
    public void setIsUserShowTime(boolean isUserShowTime) {
        this.isUserShowTime = isUserShowTime;
    }

    private JsonMain data;
    private TeeTimePageListFragment listFragment;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_appointment_browse_page;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        refreshListViewBookingInfo = (PullToRefreshListView) rootView.findViewById(R.id.lv_page_list);

        tvLeftCourseArea = (IteeTextView) rootView.findViewById(R.id.tv_left_course_area);
        tvRightCourseArea = (IteeTextView) rootView.findViewById(R.id.tv_right_course_area);

        ivLeftCourseLock = (ImageView) rootView.findViewById(R.id.iv_left_course_lock_status);
        ivRightCourseLock = (ImageView) rootView.findViewById(R.id.iv_right_course_lock_status);
    }

    @Override
    protected void setDefaultValueOfControls() {
        tvLeftCourseArea.setText(getLeftCourseArea());
        tvRightCourseArea.setText(getRightCourseArea());

        if (isLeftLock()) {
            ivLeftCourseLock.setVisibility(View.VISIBLE);
        } else {
            ivLeftCourseLock.setVisibility(View.GONE);
        }

        if (isRightLock()) {
            ivRightCourseLock.setVisibility(View.VISIBLE);
        } else {
            ivRightCourseLock.setVisibility(View.GONE);
        }
    }

    @Override
    protected void setListenersOfControls() {
        refreshListViewBookingInfo.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                listFragment.doRefresh();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });
    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    @SuppressWarnings("unchecked")
    protected void setPropertyOfControls() {
        gapTime = data.getDataList().timeInterval;
        int eachGroupNum = data.getDataList().eachGroupNum;
        String startTime = data.getDataList().startTime;
        String endTime = data.getDataList().endTime;
        if (gapTime > 0) {
            int maxColNum;
            if (Constants.MINUTES_PER_HOUR % gapTime == 0) {
                maxColNum = Constants.MINUTES_PER_HOUR / gapTime;
            } else {
                maxColNum = Constants.MINUTES_PER_HOUR / gapTime + 1;
            }

            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout ll = (LinearLayout) v;
                    Map<String, Object> params = (Map<String, Object>) ll.getTag();
                    Bundle bundle = new Bundle();
                    boolean isFirstCourse = (boolean) params.get(TransKey.IS_FIRST_COURSE);

                    bundle.putString(TransKey.COURSE_AREA_TYPE, (String) params.get(TransKey.COURSE_AREA_TYPE));
                    bundle.putInt(TransKey.CURRENT_HOUR, (Integer) params.get(TransKey.CURRENT_HOUR));
                    bundle.putString(TransKey.SELECTED_DATE, (String) params.get(TransKey.SELECTED_DATE));
                    bundle.putInt(TransKey.COURSE_AREA_TYPE_ID, (Integer) params.get(TransKey.COURSE_AREA_TYPE_ID));
                    bundle.putString(TransKey.START_TIME, (String) params.get(TransKey.START_TIME));
                    bundle.putString(TransKey.END_TIME, (String) params.get(TransKey.END_TIME));
                    bundle.putInt(TransKey.INTERVAL_TIME, gapTime);
                    bundle.putInt(TransKey.EACH_GROUP_NUM, (Integer) params.get(TransKey.EACH_GROUP_NUM));
                    bundle.putBoolean(TransKey.IS_FIRST_COURSE, isFirstCourse);
                    push(LocationListFragment.class, bundle);
                }
            };

            TeeTimePageItemAdapter adapter = new TeeTimePageItemAdapter(getActivity(),isUserShowTime(),showTimeList);
            adapter.setLeftCourseArea(getLeftCourseArea());
            adapter.setRightCourseArea(getRightCourseArea());
            adapter.setLeftCourseAreaId(getLeftCourseAreaId());
            adapter.setRightCourseAreaId(getRightCourseAreaId());
            adapter.setStartHour(getStartHour());
            adapter.setEndHour(getEndHour());
            adapter.setEachGroupNum(eachGroupNum);
            adapter.setGapTime(gapTime);
            adapter.setMaxColumnNum(maxColNum);
            adapter.setStartTime(startTime);
            adapter.setEndTime(endTime);
            adapter.setData(getData());
            adapter.setCurrentPage(getCurrentPage());
            adapter.setSelectedDate(selectedDate);
            adapter.setOnClickListener(listener);
            refreshListViewBookingInfo.setAdapter(adapter);

            tvLeftCourseArea.setTextSize(Constants.FONT_SIZE_LARGER);
            tvRightCourseArea.setTextSize(Constants.FONT_SIZE_LARGER);

            tvLeftCourseArea.setSingleLine();
            tvLeftCourseArea.setEllipsize(TextUtils.TruncateAt.END);
            tvRightCourseArea.setSingleLine();
            tvRightCourseArea.setEllipsize(TextUtils.TruncateAt.END);

            adapter.notifyDataSetChanged();
            int currentHour = Utils.getCurrentHour();
            if (AppUtils.getTodaySlash().equals(selectedDate)) {
                if (currentHour - startHour >= 2) {
                    refreshListViewBookingInfo.getRefreshableView().setSelection(currentHour - startHour - 1);
                }
            }

            refreshListViewBookingInfo.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

            ILoadingLayout headerLayoutProxy = refreshListViewBookingInfo.getLoadingLayoutProxy(true, false);
            ILoadingLayout footerLayoutProxy = refreshListViewBookingInfo.getLoadingLayoutProxy(false, true);

            headerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_down));
            headerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_down));
            headerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_down));

            footerLayoutProxy.setPullLabel(getString(R.string.app_list_header_refresh_loading_more));
            footerLayoutProxy.setRefreshingLabel(getString(R.string.app_list_header_refresh_loading_more));
            footerLayoutProxy.setReleaseLabel(getString(R.string.app_list_header_refresh_loading_more));
        }
    }

    @Override
    protected void configActionBar() {

    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String getLeftCourseArea() {
        return leftCourseArea;
    }

    public void setLeftCourseArea(String leftCourseArea) {
        this.leftCourseArea = leftCourseArea;
    }

    public String getRightCourseArea() {
        return rightCourseArea;
    }

    public void setRightCourseArea(String rightCourseArea) {
        this.rightCourseArea = rightCourseArea;
    }

    public int getStartHour() {
        return startHour;
    }

    public void setStartHour(int startHour) {
        this.startHour = startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public void setEndHour(int endHour) {
        this.endHour = endHour;
    }

    public JsonMain getData() {
        return data;
    }

    public void setData(JsonMain data) {
        this.data = data;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public boolean isRightLock() {
        return isRightLock;
    }

    public void setRightLock(boolean isRightLock) {
        this.isRightLock = isRightLock;
    }

    public boolean isLeftLock() {
        return isLeftLock;
    }

    public void setLeftLock(boolean isLeftLock) {
        this.isLeftLock = isLeftLock;
    }

    public int getRightCourseAreaId() {
        return rightCourseAreaId;
    }

    public void setRightCourseAreaId(int rightCourseAreaId) {
        this.rightCourseAreaId = rightCourseAreaId;
    }

    public int getLeftCourseAreaId() {
        return leftCourseAreaId;
    }

    public void setLeftCourseAreaId(int leftCourseAreaId) {
        this.leftCourseAreaId = leftCourseAreaId;
    }

    public void setListFragment(TeeTimePageListFragment listFragment) {
        this.listFragment = listFragment;
    }
}