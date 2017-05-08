/**
 * Project Name: itee
 * File Name:  SegmentTimesDetailFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:   2015-03-22
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.teetime;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonDetailSegmentTime;

/**
 * ClassName:SegmentTimesDetailFragment <br/>
 * Function: SegmentTimesDetailFragment. <br/>
 * Date: 2015-03-22 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class SegmentTimesDetailFragment extends BaseFragment {


    private String date;
    private ViewPager vpSegmentCoursePager;
    private SegmentTimeCoursePagerAdapter vpAdapter;
    private ArrayList<SegmentTimesDetailItemFragment> fragments;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_detail_segment_times;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD, Locale.getDefault());
        Bundle bundle = getArguments();
        if (bundle != null) {
            Date d = (Date) bundle.getSerializable("date");
            if (d != null) {
                date = format.format(d);
            } else {
                date = AppUtils.getToday();
                Utils.showShortToast(getActivity(), "error date");
            }
        }
    }

    @Override
    protected void initControls(View rootView) {

        fragments = new ArrayList<>();

        vpSegmentCoursePager = (ViewPager) rootView.findViewById(R.id.vp_segment_course_pager);
        vpAdapter = new SegmentTimeCoursePagerAdapter(getChildFragmentManager(), fragments);
    }

    @Override
    protected void setDefaultValueOfControls() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COURSE_ID, AppUtils.getCurrentCourseId(getActivity()));
        params.put(ApiKey.ADMINISTRATION_DATE, date);

        HttpManager<JsonDetailSegmentTime> hh = new HttpManager<JsonDetailSegmentTime>(SegmentTimesDetailFragment.this) {

            @Override
            public void onJsonSuccess(JsonDetailSegmentTime jo) {
                Integer returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    initData(jo);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.DetailSegmentTime, params);
    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        vpSegmentCoursePager.setAdapter(vpAdapter);
        vpAdapter.notifyDataSetChanged();
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();

        String segmentTime = getString(R.string.title_activity_segment_times);
        String title = segmentTime + Constants.STR_SPACE + date;
        SpannableString ss = new SpannableString(title);
        ss.setSpan(new ForegroundColorSpan(getColor(R.color.common_white)), 0, segmentTime.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(1.f), 0, segmentTime.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(getColor(R.color.menu_selected_mark)), segmentTime.length(), title.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        ss.setSpan(new RelativeSizeSpan(0.8f), segmentTime.length(), title.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        getTvLeftTitle().setText(ss);

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getTvLeftTitle().getLayoutParams();
        layoutParams.width = getActualWidthOnThisDevice(500);
        getTvLeftTitle().setLayoutParams(layoutParams);

    }

    private void initData(JsonDetailSegmentTime jo) {

        int[] colors = new int[]{R.color.bg_green_of_1, R.color.bg_green_of_2, R.color.bg_green_of_3,
                R.color.bg_purple_of_1, R.color.bg_purple_of_2, R.color.bg_purple_of_3,
                R.color.bg_blue_of_1, R.color.bg_blue_of_2, R.color.bg_blue_of_3};

        HashMap<String, Integer> colorHashMap = new HashMap<>();

        ArrayList<JsonDetailSegmentTime.CourseArea> courseAreas = jo.getDataList().getCourseAreaList();
        LinkedHashMap<String, JsonDetailSegmentTime.DetailSegmentCourse> detailsSegmentMap = jo.getDetailsSegmentMap();
        SegmentTimesDetailItemFragment fragment = new SegmentTimesDetailItemFragment();
        for (int i = 0; i < courseAreas.size(); i++) {
            String key = courseAreas.get(i).getCourseAreaId();
            colorHashMap.put(key, colors[i % colors.length]);
            if (i % 3 == 0) {
                fragment = new SegmentTimesDetailItemFragment();
                fragment.setColorMap(colorHashMap);
                fragments.add(fragment);
            }
            JsonDetailSegmentTime.DetailSegmentCourse detailSegmentCourse = detailsSegmentMap.get(key);
            if (i % 3 == 0) {
                fragment.setLeftCourse(detailSegmentCourse);
            } else if (i % 3 == 1) {
                fragment.setMiddleCourse(detailSegmentCourse);
            } else {
                fragment.setRightCourse(detailSegmentCourse);
            }
           // fragment.setCurrentPage(i / 3 + 1);
        }

        vpAdapter.notifyDataSetChanged();
    }

    class SegmentTimeCoursePagerAdapter extends FragmentStatePagerAdapter {

        ArrayList<SegmentTimesDetailItemFragment> list;
        //private FragmentManager fm;

        public SegmentTimeCoursePagerAdapter(FragmentManager fm, ArrayList<SegmentTimesDetailItemFragment> list) {
            super(fm);
           // this.fm = fm;
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
