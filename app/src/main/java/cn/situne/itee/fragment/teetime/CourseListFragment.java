/**
 * Project Name: itee
 * File Name:	 CourseListFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-01-10
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.teetime;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.CourseTitleListAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonCourseListInfo;

/**
 * ClassName:CourseListFragment <br/>
 * Function: tee time course list. <br/>
 * UI:  03-7-1
 * Date: 2015-01-22 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class CourseListFragment extends BaseFragment {

    private ListView showCourseListView;
    private CourseTitleListAdapter courseTitleListAdapter;
    private View.OnClickListener ivJumpClick;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_show_course_message;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {

        showCourseListView = (ListView) rootView.findViewById(R.id.show_course_listView);
        showCourseListView.setDivider(null);

    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        ivJumpClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tagOne = (String) view.getTag();
                String[] value = tagOne.split(Constants.STR_COMMA);
                String parOne = value[0];
                String parTwo = value[1];
                String parThree = value[2];
                String parFour = value[3];
                String parFive = value[4];
                String parSix = value[5];
                String parSeven = value[6];
                String parEight = value[7];
                String parNight = value[8];
                String holeOne = value[9];
                String holeTwo = value[10];
                String holeThree = value[11];
                String holeFour = value[12];
                String holeFive = value[13];
                String holeSix = value[14];
                String holeSeven = value[15];
                String holeEight = value[16];
                String holeNine = value[17];
                String courseAreaType = value[18];
                String courseAreaIdString = value[19];
                String unit = value[20];

                Bundle bundle = new Bundle();
                bundle.putString("parOne", parOne);
                bundle.putString("parTwo", parTwo);
                bundle.putString("parThree", parThree);
                bundle.putString("parFour", parFour);
                bundle.putString("parFive", parFive);
                bundle.putString("parSix", parSix);
                bundle.putString("parSeven", parSeven);
                bundle.putString("parEight", parEight);
                bundle.putString("parNight", parNight);
                bundle.putString("holeOne", holeOne);
                bundle.putString("holeTwo", holeTwo);
                bundle.putString("holeThree", holeThree);
                bundle.putString("holeFour", holeFour);
                bundle.putString("holeFive", holeFive);
                bundle.putString("holeSix", holeSix);
                bundle.putString("holeSeven", holeSeven);
                bundle.putString("holeEight", holeEight);
                bundle.putString("holeNine", holeNine);
                bundle.putString("courseAreaType", courseAreaType);
                bundle.putString("courseAreaTypeId", courseAreaIdString);
                bundle.putString("unit", unit);
                push(CourseSettingFragment.class, bundle);
            }
        };

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {
        showCourseListView.setDividerHeight(getActualHeightOnThisDevice(20));
        showCourseListView.setBackgroundColor(getColor(R.color.common_light_gray));
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.title_course_data);
        getTvRight().setBackground(null);
        getTvRight().setBackgroundResource(R.drawable.icon_common_add);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push(CourseAddFragment.class);
            }
        });
    }


    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        putCourseData();
    }


    public void putCourseData() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COURSE_USER_ID, AppUtils.getCurrentUserId(getActivity()));

        HttpManager<JsonCourseListInfo> hh = new HttpManager<JsonCourseListInfo>(CourseListFragment.this) {

            @Override
            public void onJsonSuccess(JsonCourseListInfo jo) {

                final List<JsonCourseListInfo.DataList> dataList = jo.getDataList();

                courseTitleListAdapter = new CourseTitleListAdapter(dataList, getActivity(), ivJumpClick, CourseListFragment.this);
                showCourseListView.setAdapter(courseTitleListAdapter);
                courseTitleListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.CourseData, params);
    }
}
