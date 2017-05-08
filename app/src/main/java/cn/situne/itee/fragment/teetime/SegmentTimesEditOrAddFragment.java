/**
 * Project Name: itee
 * File Name:  SegmentTimesEditOrAddFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:   2015-03-21
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.SegmentTimeEditAdapter;
import cn.situne.itee.adapter.SegmentTimeSettingAdapter;
import cn.situne.itee.adapter.SegmentTimesCourseSelectAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonDoEditSegmentTime;
import cn.situne.itee.view.CheckSwitchButton;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SegmentTimeEditOrDeletePopupWindow;
import cn.situne.itee.view.SegmentTypePopupWindow;
import cn.situne.itee.view.SlideExpandableListView;
import cn.situne.itee.view.popwindow.SegmentTimesSelectCoursePopupWindow;
import cn.situne.itee.view.popwindow.SelectTimePopupWindow;

/**
 * ClassName:SegmentTimesEditOrAddFragment <br/>
 * Function: SegmentTimesEditOrAddFragment. <br/>
 * Date: 2015-03-21 <br/>
 * UI:03-9-2
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class SegmentTimesEditOrAddFragment extends BaseFragment {

    public static final String TWO_TEE_START_ID = "1";
    public static final String NINE_HOLES_ONLY_ID = "2";
    public static final String BLOCK_TIMES_ID = "3";
    public static final String MEMBER_ONLY_ID = "4";
    public static final String THREE_TEE_START_ID = "5";
    public static final String PRIME_TIME_ID = "6";

    private static final String COURSE_AREA_ID = "courseAreaId";
    private static final String COURSE_AREA_NAME = "courseAreaName";

    private static final String MEMBER_TYPE = "memberType";
    private static final String TYPE_ID = "typeId";

    private RelativeLayout rlSegmentTime;
    private RelativeLayout rlSegmentType;
    private RelativeLayout rlSegmentCourse;


    private LinearLayout llTwoTeeStartTransfer;
    private RelativeLayout rlTwoTeeStartFirstBtnContainer;
    private RelativeLayout rlTwoTeeStartSecondBtnContainer;

    private LinearLayout llThreeTeeStartTransfer;
    private RelativeLayout rlThreeTeeStartFirstBtnContainer;
    private RelativeLayout rlThreeTeeStartSecondBtnContainer;
    private RelativeLayout rlThreeTeeStartThirdBtnContainer;

    private RelativeLayout rlSegmentStartTime;
    private RelativeLayout rlSegmentEndTime;
    private RelativeLayout rlSegmentMemberType;
    private RelativeLayout rlSegmentAllReserved;

    private RelativeLayout rlSegmentPrimeTime;
    private View.OnClickListener deleteDayListener;

    private IteeTextView etTime;
    private String timeShow;
    private IteeTextView btnHistorySettings;
    private IteeTextView tvType;
    private ImageView ivTypeArrow;
    private IteeTextView tvCourse;
    private CheckBox cbCourse;

    private IteeButton btnTwoTeeStartFirstCourse;
    private IteeButton btnTwoTeeStartFirstTransfer;
    private IteeButton btnTwoTeeStartSecondCourse;
    private IteeButton btnTwoTeeStartSecondTransfer;

    private IteeButton btnThreeTeeStartFirstCourse;
    private IteeButton btnThreeTeeStartFirstTransfer;
    private IteeButton btnThreeTeeStartSecondCourse;
    private IteeButton btnThreeTeeStartSecondTransfer;
    private IteeButton btnThreeTeeStartThirdCourse;
    private IteeButton btnThreeTeeStartThirdTransfer;

    private ImageView ivTwoTeeStartFirstArrow;
    private ImageView ivTwoTeeStartSecondArrow;

    private ImageView ivThreeTeeStartFirstArrow;
    private ImageView ivThreeTeeStartSecondArrow;
    private ImageView ivThreeTeeStartThirdArrow;

    private IteeTextView tvStartTime;
    private IteeTextView tvStartTimeContent;
    private IteeTextView tvEndTime;
    private IteeTextView tvEndTimeContent;

    private IteeTextView tvAllReserved;
    private CheckSwitchButton swAllReserved;
    private IteeTextView tvMemberType;
    private IteeTextView tvLifeMember;
    private IteeTextView tvFamilyMember;
    private CheckBox cbMemberType;
    private CheckBox cbLifeMember;
    private CheckBox cbFamilyMember;

    private IteeTextView tvSegmentPrimeTime;
    private CheckSwitchButton swThreeToReserve;

    private View.OnClickListener rlSegmentTypeListener;
    private View.OnClickListener itemsOnClick;
    private AppUtils.NoDoubleClickListener noDoubleClickListener;
    private CourseCheckedChangeListener courseCheckedChangeListener = new CourseCheckedChangeListener();

    private MemberTypeCheckedChangeListener memberTypeCheckedChangeListener = new MemberTypeCheckedChangeListener();

    private SegmentTimeCourseCheckAdapter myListViewAdapter;

    private SegmentTimeMemberTypeCheckAdapter memberTypeCheckAdapter;

    //自定义的弹出框类
    private SegmentTypePopupWindow menuWindow;

    private SlideExpandableListView expandListView;
    private SegmentTimeSettingAdapter adapter;
    private SegmentTimeEditAdapter editAdapter;

    private JsonDoEditSegmentTime.DataList editData = new JsonDoEditSegmentTime.DataList();

    private boolean isListShow = false;

    private LinearLayout llSegmentTitle;


    private SelectTimePopupWindow popupWindow;
    private SegmentTimesSelectCoursePopupWindow segmentTimesSelectCoursePopupWindow;

    private String fieldContent;
    private String fieldTime;
    private String category;

    private String segmentTimeDate;
    private String startTime;
    private String endTime;

    private String transferCourse;
    private String memberType;
    private String threeToReserve;
    private String allReserveMember;

    private String initDate;

    private ArrayList<String> dates = new ArrayList<>();

    //course列表
    private ListView courseListView;
    //记录course列表选中状态
    private ArrayList<Boolean> courseCheckedList;
    //course area list
    private ArrayList<JsonDoEditSegmentTime.CourseArea> courseAreaList;
    //course area的size
    private int courseAreaSize;
    //由course area list得到map集合
    private ArrayList<HashMap<String, Object>> courseAreaMapList;
    //已选course的id
    private ArrayList<Integer> checkedCourseIdList;
    //得到已选course area的map集合
    private ArrayList<HashMap<String, Object>> checkedCourseMapList;


    //member type列表
    private ListView memberTypeListView;
    //记录member type列表选中状态
    private ArrayList<Boolean> memberTypeCheckedList;
    //member type list集合
    private List<JsonDoEditSegmentTime.MemberTypeList> memberTypeListList;
    //member type list的size
    private int memberTypeListSize;
    //得到已选member type的map集合
    private ArrayList<HashMap<String, Object>> checkedMemberTypeMapList;
    //已选member type的id
    private ArrayList<Integer> checkedTypeIdList;
    //已选member type的name
    private ArrayList<String> checkedMemberTypeList;


    private ArrayList<SegmentPeriodSettingLayout> periodSettingLayoutArrayList = new ArrayList<>();
    private Handler courseHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int count = 0;

            for (int i = 0; i < courseCheckedList.size(); i++) {
                boolean isChecked = courseCheckedList.get(i);
                if (isChecked) {
                    count++;
                }
            }

            if (count == courseCheckedList.size()) {
                cbCourse.setChecked(true);
            } else {
                cbCourse.setChecked(false);
            }

        }
    };
    private Handler memberTypeHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            int count = 0;

            for (int i = 0; i < memberTypeCheckedList.size(); i++) {
                boolean isChecked = memberTypeCheckedList.get(i);
                if (isChecked) {
                    count++;
                }
            }

            if (count == memberTypeCheckedList.size()) {
                cbMemberType.setChecked(true);
            } else {
                cbMemberType.setChecked(false);
            }

        }
    };

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_edit_or_add_segment_times;
    }

    @Override
    protected void initControls(View rootView) {

        llSegmentTitle = (LinearLayout) rootView.findViewById(R.id.ll_segment_title);

        rlSegmentTime = (RelativeLayout) rootView.findViewById(R.id.rl_segment_time);
        rlSegmentType = (RelativeLayout) rootView.findViewById(R.id.rl_segment_type);

        rlSegmentCourse = (RelativeLayout) rootView.findViewById(R.id.rl_segment_course);

        llTwoTeeStartTransfer = (LinearLayout) rootView.findViewById(R.id.ll_two_tee_start_btn);

        rlTwoTeeStartFirstBtnContainer = (RelativeLayout) rootView.findViewById(R.id.rl_two_tee_start_course_first);

        rlTwoTeeStartSecondBtnContainer = (RelativeLayout) rootView.findViewById(R.id.rl_two_tee_start_course_second);

        llThreeTeeStartTransfer = (LinearLayout) rootView.findViewById(R.id.ll_three_tee_start_btn);

        rlThreeTeeStartFirstBtnContainer = (RelativeLayout) rootView.findViewById(R.id.rl_three_tee_start_course_first);

        rlThreeTeeStartSecondBtnContainer = (RelativeLayout) rootView.findViewById(R.id.rl_three_tee_start_course_second);

        rlThreeTeeStartThirdBtnContainer = (RelativeLayout) rootView.findViewById(R.id.rl_three_tee_start_course_third);

        rlSegmentStartTime = (RelativeLayout) rootView.findViewById(R.id.rl_segment_start_time);
        rlSegmentEndTime = (RelativeLayout) rootView.findViewById(R.id.rl_segment_end_time);

        rlSegmentAllReserved = (RelativeLayout) rootView.findViewById(R.id.rl_segment_all_reserved);
        rlSegmentMemberType = (RelativeLayout) rootView.findViewById(R.id.rl_segment_member_type);
        rlSegmentPrimeTime = (RelativeLayout) rootView.findViewById(R.id.rl_segment_prime_time);

        Context mContext = getActivity();

        etTime = new IteeTextView(this);
        tvType = new IteeTextView(this);

        tvStartTime = new IteeTextView(this);
        tvStartTimeContent = new IteeTextView(this);
        tvEndTime = new IteeTextView(this);
        tvEndTimeContent = new IteeTextView(this);
        tvCourse = new IteeTextView(this);

        cbCourse = new CheckBox(mContext);


        tvAllReserved = new IteeTextView(this);
        swAllReserved = new CheckSwitchButton(this);

        tvMemberType = new IteeTextView(this);
        tvLifeMember = new IteeTextView(this);
        tvFamilyMember = new IteeTextView(this);
        cbMemberType = new CheckBox(mContext);
        cbLifeMember = new CheckBox(mContext);
        cbFamilyMember = new CheckBox(mContext);

        btnHistorySettings = new IteeTextView(mContext);

        btnTwoTeeStartFirstCourse = new IteeButton(mContext);
        btnTwoTeeStartFirstTransfer = new IteeButton(mContext);
        btnTwoTeeStartSecondCourse = new IteeButton(mContext);
        btnTwoTeeStartSecondTransfer = new IteeButton(mContext);

        btnThreeTeeStartFirstCourse = new IteeButton(mContext);
        btnThreeTeeStartFirstTransfer = new IteeButton(mContext);
        btnThreeTeeStartSecondCourse = new IteeButton(mContext);
        btnThreeTeeStartSecondTransfer = new IteeButton(mContext);
        btnThreeTeeStartThirdCourse = new IteeButton(mContext);
        btnThreeTeeStartThirdTransfer = new IteeButton(mContext);

        ivTypeArrow = new ImageView(mContext);

        ivTwoTeeStartFirstArrow = new ImageView(mContext);
        ivTwoTeeStartSecondArrow = new ImageView(mContext);


        ivThreeTeeStartFirstArrow = new ImageView(mContext);
        ivThreeTeeStartSecondArrow = new ImageView(mContext);
        ivThreeTeeStartThirdArrow = new ImageView(mContext);

        tvSegmentPrimeTime = new IteeTextView(this);
        swThreeToReserve = new CheckSwitchButton(this);

        rlSegmentCourse.setVisibility(View.GONE);

        llTwoTeeStartTransfer.setVisibility(View.VISIBLE);
        llThreeTeeStartTransfer.setVisibility(View.GONE);
        llSegmentTitle.setVisibility(View.VISIBLE);
        rlSegmentStartTime.setVisibility(View.VISIBLE);
        rlSegmentEndTime.setVisibility(View.VISIBLE);
        rlSegmentAllReserved.setVisibility(View.GONE);
        rlSegmentMemberType.setVisibility(View.GONE);
        rlSegmentPrimeTime.setVisibility(View.GONE);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        String result = bundle.getString(TransKey.COMMON_RESULT);
        int layoutIndex = bundle.getInt(TransKey.SEGMENT_TIME_LAYOUT_INDEX);
        if (result.equals("edited")) {
            getSegmentTime();
            if (layoutIndex > -1) {
                String editId = bundle.getString(TransKey.SEGMENT_TIME_EDIT_ID);
                if (Utils.isStringNotNullOrEmpty(editId)) {
                    boolean isContain = false;
                    for (SegmentPeriodSettingLayout segmentPeriodSettingLayout : periodSettingLayoutArrayList) {
                        if (segmentPeriodSettingLayout.getSegmentSettingIds().contains(editId)) {
                            isContain = true;
                            break;
                        }
                    }
                    if (isContain) {
                        for (SegmentPeriodSettingLayout segmentPeriodSettingLayout : periodSettingLayoutArrayList) {
                            llSegmentTitle.removeView(segmentPeriodSettingLayout);
                        }
                        periodSettingLayoutArrayList.clear();
                    } else {
                        String transferCourse = bundle.getString(TransKey.SEGMENT_TIME_TRANSFER_COURSE);
                        String startTime = bundle.getString(TransKey.SEGMENT_TIME_START_TIME);
                        String endTime = bundle.getString(TransKey.SEGMENT_TIME_END_TIME);
                        SegmentPeriodSettingLayout segmentPeriodSettingLayout = periodSettingLayoutArrayList.get(layoutIndex);
                        segmentPeriodSettingLayout.tvFieldTime.setText(startTime + " -- " + endTime);
                        segmentPeriodSettingLayout.tvFieldContent.setText(transferCourse);
                    }
                }
            }
        }
        if (result.equals(TransKey.COMMON_RESULT_DELETED)) {
            int groupPosition = bundle.getInt(TransKey.SEGMENT_TIME_GROUP_POSITION, -1);
            int childPosition = bundle.getInt(TransKey.SEGMENT_TIME_CHILD_POSITION, -1);
            if (childPosition > -1 && groupPosition > -1) {
                editData.getSegmentTimesList().get(groupPosition).getSegmentTimesList().remove(childPosition);
                adapter = new SegmentTimeSettingAdapter(SegmentTimesEditOrAddFragment.this, editData, deleteDayListener);
                expandListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                LayoutUtils.setListViewHeightBasedOnChildren(expandListView);
            } else {
                getSegmentTime();
            }
            if (layoutIndex > -1) {
                SegmentPeriodSettingLayout segmentPeriodSettingLayout = periodSettingLayoutArrayList.get(layoutIndex);
                llSegmentTitle.removeView(segmentPeriodSettingLayout);
                periodSettingLayoutArrayList.remove(layoutIndex);
            }
        }
    }

    @Override
    protected void setListenersOfControls() {

        rlSegmentTypeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //实例化SelectPicPopupWindow
                menuWindow = new SegmentTypePopupWindow(getActivity(), itemsOnClick);
                //显示窗口
                menuWindow.showAtLocation(getRootView().findViewById(R.id.sv_segment),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                //根据courseAreaSize的大小来判断 Three tee start 是否显示
                if (courseAreaSize == 1) {
                    menuWindow.btnTwoTeeStart.setVisibility(View.GONE);
                    menuWindow.btnThreeTeeStart.setVisibility(View.GONE);
                } else if (courseAreaSize == 2) {
                    menuWindow.btnThreeTeeStart.setVisibility(View.GONE);
                } else if (courseAreaSize >= 3) {
                    menuWindow.btnThreeTeeStart.setVisibility(View.VISIBLE);
                }
            }
        };
        rlSegmentType.setOnClickListener(rlSegmentTypeListener);


        noDoubleClickListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                startTime = tvStartTimeContent.getText().toString();
                endTime = tvEndTimeContent.getText().toString();
                segmentTimeDate = getDate();

                memberType = StringUtils.EMPTY;
                threeToReserve = StringUtils.EMPTY;
                allReserveMember = StringUtils.EMPTY;

                String typeId = String.valueOf(tvType.getTag());

                if (typeId.equals(TWO_TEE_START_ID) && twoTeeStartBtnTagCheck() && timeCheck()) {
                    category = TWO_TEE_START_ID;
                    String twoTeeStartFirstCourseId = btnTwoTeeStartFirstCourse.getTag().toString();
                    String twoTeeStartFirstTransferId = btnTwoTeeStartFirstTransfer.getTag().toString();
                    String twoTeeStartSecondCourseId = btnTwoTeeStartSecondCourse.getTag().toString();
                    String twoTeeStartSecondTransferId = btnTwoTeeStartSecondTransfer.getTag().toString();

                    transferCourse = "[{\"from\":{\"id\":" + twoTeeStartFirstCourseId + "},\"to\":{\"id\":"
                            + twoTeeStartFirstTransferId + "}},{\"from\":{\"id\":" + twoTeeStartSecondCourseId
                            + "},\"to\":{\"id\":" + twoTeeStartSecondTransferId + "}}]";
                    addSegmentTimes();

                    fieldContent = btnTwoTeeStartFirstCourse.getText().toString() + " to "
                            + btnTwoTeeStartFirstTransfer.getText().toString() + Constants.STR_SPACE
                            + Constants.STR_SPACE + btnTwoTeeStartSecondCourse.getText().toString()
                            + " to " + btnTwoTeeStartSecondTransfer.getText().toString();


                } else if (typeId.equals(NINE_HOLES_ONLY_ID) && courseAreaCheck() && timeCheck()) {
                    category = NINE_HOLES_ONLY_ID;
                    getCourseCheckedId();
                    addSegmentTimes();
                } else if (typeId.equals(BLOCK_TIMES_ID) && courseAreaCheck() && timeCheck()) {
                    category = BLOCK_TIMES_ID;
                    getCourseCheckedId();
                    addSegmentTimes();
                } else if (typeId.equals(MEMBER_ONLY_ID) && courseAreaCheck() && timeCheck() && memberTypeCheck()) {
                    category = MEMBER_ONLY_ID;

                    getCourseCheckedId();

                    if (swAllReserved.isChecked()) {
                        allReserveMember = Constants.STR_1;
                    } else {
                        allReserveMember = Constants.STR_0;
                    }
                    getMemberTypeCheckedId();
                    addSegmentTimes();
                } else if (typeId.equals(THREE_TEE_START_ID) && threeTeeStartBtnTagCheck() && timeCheck()) {
                    category = THREE_TEE_START_ID;
                    String threeTeeStartFirstCourseId = btnThreeTeeStartFirstCourse.getTag().toString();
                    String threeTeeStartFirstTransferId = btnThreeTeeStartFirstTransfer.getTag().toString();
                    String threeTeeStartSecondCourseId = btnThreeTeeStartSecondCourse.getTag().toString();
                    String threeTeeStartSecondTransferId = btnThreeTeeStartSecondTransfer.getTag().toString();
                    String threeTeeStartThirdCourseId = btnThreeTeeStartThirdCourse.getTag().toString();
                    String threeTeeStartThirdTransferId = btnThreeTeeStartThirdTransfer.getTag().toString();
                    transferCourse = "[{\"from\":{\"id\":" + threeTeeStartFirstCourseId + "},\"to\":{\"id\":"
                            + threeTeeStartFirstTransferId + "}},{\"from\":{\"id\":" + threeTeeStartSecondCourseId
                            + "},\"to\":{\"id\":" + threeTeeStartSecondTransferId + "}},{\"from\":{\"id\":"
                            + threeTeeStartThirdCourseId + "},\"to\":{\"id\":" + threeTeeStartThirdTransferId + "}}]";
                    addSegmentTimes();

                    fieldContent = btnThreeTeeStartFirstCourse.getText().toString() + " to "
                            + btnThreeTeeStartFirstTransfer.getText().toString() + Constants.STR_SPACE
                            + Constants.STR_SPACE + btnThreeTeeStartSecondCourse.getText().toString()
                            + " to " + btnThreeTeeStartSecondTransfer.getText().toString() + Constants.STR_SPACE
                            + Constants.STR_SPACE + btnThreeTeeStartThirdCourse.getText().toString() + " to "
                            + btnThreeTeeStartThirdTransfer.getText().toString();
                } else if (typeId.equals(PRIME_TIME_ID) && courseAreaCheck() && timeCheck()) {
                    category = PRIME_TIME_ID;

                    getCourseCheckedId();

                    if (swThreeToReserve.isChecked()) {
                        threeToReserve = Constants.STR_1;
                    } else {
                        threeToReserve = Constants.STR_0;
                    }
                    addSegmentTimes();
                }

            }
        };

        itemsOnClick = new View.OnClickListener() {

            public void onClick(View v) {
                menuWindow.dismiss();
                switch (v.getId()) {
                    case R.id.btn_two_tee_start:
                        tvType.setText(getText(R.string.segment_times_two_tee_start));
                        tvType.setTag(TWO_TEE_START_ID);
                        rlSegmentCourse.setVisibility(View.GONE);
                        courseListView.setVisibility(View.GONE);

                        llTwoTeeStartTransfer.setVisibility(View.VISIBLE);
                        llThreeTeeStartTransfer.setVisibility(View.GONE);
                        llSegmentTitle.setVisibility(View.VISIBLE);
                        rlSegmentStartTime.setVisibility(View.VISIBLE);
                        rlSegmentEndTime.setVisibility(View.VISIBLE);
                        rlSegmentAllReserved.setVisibility(View.GONE);
                        rlSegmentMemberType.setVisibility(View.GONE);
                        memberTypeListView.setVisibility(View.GONE);
                        rlSegmentPrimeTime.setVisibility(View.GONE);


                        btnTwoTeeStartFirstCourse.setTag(null);
                        btnTwoTeeStartFirstCourse.setText(Constants.STR_EMPTY);
                        btnTwoTeeStartFirstTransfer.setTag(null);
                        btnTwoTeeStartFirstTransfer.setText(Constants.STR_EMPTY);
                        btnTwoTeeStartSecondCourse.setTag(null);
                        btnTwoTeeStartSecondCourse.setText(Constants.STR_EMPTY);
                        btnTwoTeeStartSecondTransfer.setTag(null);
                        btnTwoTeeStartSecondTransfer.setText(Constants.STR_EMPTY);

                        break;
                    case R.id.btn_nine_holes_only:
                        tvType.setText(getText(R.string.segment_times_nine_holes_only));
                        tvType.setTag(NINE_HOLES_ONLY_ID);
                        rlSegmentCourse.setVisibility(View.VISIBLE);

                        courseListView.setVisibility(View.VISIBLE);
                        llTwoTeeStartTransfer.setVisibility(View.GONE);
                        llThreeTeeStartTransfer.setVisibility(View.GONE);
                        llSegmentTitle.setVisibility(View.GONE);
                        rlSegmentStartTime.setVisibility(View.VISIBLE);
                        rlSegmentEndTime.setVisibility(View.VISIBLE);
                        rlSegmentAllReserved.setVisibility(View.GONE);
                        rlSegmentMemberType.setVisibility(View.GONE);
                        memberTypeListView.setVisibility(View.GONE);
                        rlSegmentPrimeTime.setVisibility(View.GONE);

                        break;
                    case R.id.btn_member_only:
                        tvType.setText(getText(R.string.segment_times_member_only));
                        tvType.setTag(MEMBER_ONLY_ID);
                        rlSegmentCourse.setVisibility(View.VISIBLE);

                        courseListView.setVisibility(View.VISIBLE);
                        llTwoTeeStartTransfer.setVisibility(View.GONE);
                        llThreeTeeStartTransfer.setVisibility(View.GONE);
                        llSegmentTitle.setVisibility(View.GONE);
                        rlSegmentStartTime.setVisibility(View.VISIBLE);
                        rlSegmentEndTime.setVisibility(View.VISIBLE);
                        rlSegmentAllReserved.setVisibility(View.VISIBLE);
                        rlSegmentMemberType.setVisibility(View.VISIBLE);
                        memberTypeListView.setVisibility(View.VISIBLE);
                        rlSegmentPrimeTime.setVisibility(View.GONE);

                        break;
                    case R.id.btn_block_times:
                        tvType.setText(getText(R.string.segment_times_block_times));
                        tvType.setTag(BLOCK_TIMES_ID);
                        rlSegmentCourse.setVisibility(View.VISIBLE);

                        courseListView.setVisibility(View.VISIBLE);
                        llTwoTeeStartTransfer.setVisibility(View.GONE);
                        llThreeTeeStartTransfer.setVisibility(View.GONE);
                        llSegmentTitle.setVisibility(View.GONE);
                        rlSegmentStartTime.setVisibility(View.VISIBLE);
                        rlSegmentEndTime.setVisibility(View.VISIBLE);
                        rlSegmentAllReserved.setVisibility(View.GONE);
                        rlSegmentMemberType.setVisibility(View.GONE);
                        memberTypeListView.setVisibility(View.GONE);
                        rlSegmentPrimeTime.setVisibility(View.GONE);

                        break;
                    case R.id.btn_three_tee_start:
                        tvType.setText(getText(R.string.segment_times_three_tee_start));
                        tvType.setTag(THREE_TEE_START_ID);
                        rlSegmentCourse.setVisibility(View.GONE);

                        courseListView.setVisibility(View.GONE);
                        llTwoTeeStartTransfer.setVisibility(View.GONE);
                        llThreeTeeStartTransfer.setVisibility(View.VISIBLE);
                        llSegmentTitle.setVisibility(View.VISIBLE);
                        rlSegmentStartTime.setVisibility(View.VISIBLE);
                        rlSegmentEndTime.setVisibility(View.VISIBLE);
                        rlSegmentAllReserved.setVisibility(View.GONE);
                        rlSegmentMemberType.setVisibility(View.GONE);
                        memberTypeListView.setVisibility(View.GONE);
                        rlSegmentPrimeTime.setVisibility(View.GONE);


                        btnThreeTeeStartFirstCourse.setTag(null);
                        btnThreeTeeStartFirstCourse.setText(Constants.STR_EMPTY);
                        btnThreeTeeStartFirstTransfer.setTag(null);
                        btnThreeTeeStartFirstTransfer.setText(Constants.STR_EMPTY);
                        btnThreeTeeStartSecondCourse.setTag(null);
                        btnThreeTeeStartSecondCourse.setText(Constants.STR_EMPTY);
                        btnThreeTeeStartSecondTransfer.setTag(null);
                        btnThreeTeeStartSecondTransfer.setText(Constants.STR_EMPTY);
                        btnThreeTeeStartThirdCourse.setTag(null);
                        btnThreeTeeStartThirdCourse.setText(Constants.STR_EMPTY);
                        btnThreeTeeStartThirdTransfer.setTag(null);
                        btnThreeTeeStartThirdTransfer.setText(Constants.STR_EMPTY);


                        break;
                    case R.id.btn_prime_time:
                        tvType.setText(getText(R.string.segment_times_prime_time));
                        tvType.setTag(PRIME_TIME_ID);
                        rlSegmentCourse.setVisibility(View.VISIBLE);

                        courseListView.setVisibility(View.VISIBLE);
                        llTwoTeeStartTransfer.setVisibility(View.GONE);
                        llThreeTeeStartTransfer.setVisibility(View.GONE);
                        llSegmentTitle.setVisibility(View.GONE);
                        rlSegmentStartTime.setVisibility(View.VISIBLE);
                        rlSegmentEndTime.setVisibility(View.VISIBLE);
                        rlSegmentAllReserved.setVisibility(View.GONE);
                        rlSegmentMemberType.setVisibility(View.GONE);
                        memberTypeListView.setVisibility(View.GONE);
                        rlSegmentPrimeTime.setVisibility(View.VISIBLE);

                        break;
                    default:
                        break;
                }
            }

        };


        expandListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            public void onGroupExpand(int groupPosition) {
                LayoutUtils.setListViewHeightBasedOnChildren(expandListView);
            }

        });

        expandListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                LayoutUtils.setListViewHeightBasedOnChildren(expandListView);
            }
        });


        //给expandListView的子项加监听
        expandListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, final View view,
                                        final int groupPosition, final int childPosition, long id) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), SegmentTimeEditOrDeletePopupWindow.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("data", editData);
                bundle.putInt(TransKey.SEGMENT_TIME_GROUP_POSITION, groupPosition);
                bundle.putInt(TransKey.SEGMENT_TIME_CHILD_POSITION, childPosition);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);

                return true;
            }
        });

        //

        btnHistorySettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isListShow) {
                    btnHistorySettings.setText(getString(R.string.segment_times_expand));
                    isListShow = false;
                    expandListView.setVisibility(View.GONE);

                } else {
                    btnHistorySettings.setText(getString(R.string.segment_times_hide));
                    isListShow = true;
                    expandListView.setVisibility(View.VISIBLE);
                }
            }
        });


        //two tee start的四个场地按钮

        btnTwoTeeStartFirstCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter
                        = new SegmentTimesCourseSelectAdapter(getActivity(), courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(getActivity(), null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnTwoTeeStartFirstTransfer.getTag() != null
                                && btnTwoTeeStartFirstTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnTwoTeeStartSecondCourse.getTag() != null
                                && btnTwoTeeStartSecondCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else {
                            btnTwoTeeStartFirstCourse.setTag(selectedId);
                            btnTwoTeeStartFirstCourse.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }

                    }
                });
            }
        });


        btnTwoTeeStartFirstTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(getActivity(),
                        courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(getActivity(), null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnTwoTeeStartFirstCourse.getTag() != null
                                && btnTwoTeeStartFirstCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_cannot_choose_itself));
                        } else if (btnTwoTeeStartSecondTransfer.getTag() != null
                                && btnTwoTeeStartSecondTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));
                        } else {
                            btnTwoTeeStartFirstTransfer.setTag(selectedId);
                            btnTwoTeeStartFirstTransfer.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }
                    }
                });
            }
        });


        btnTwoTeeStartSecondCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(getActivity(),
                        courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(getActivity(), null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnTwoTeeStartSecondTransfer.getTag() != null
                                && btnTwoTeeStartSecondTransfer.getTag().equals(selectedId)) {

                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnTwoTeeStartFirstCourse.getTag() != null
                                && btnTwoTeeStartFirstCourse.getTag().equals(selectedId)) {

                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else {
                            btnTwoTeeStartSecondCourse.setTag(selectedId);
                            btnTwoTeeStartSecondCourse.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }
                    }
                });
            }
        });


        btnTwoTeeStartSecondTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(getActivity(),
                        courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(getActivity(), null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnTwoTeeStartSecondCourse.getTag() != null
                                && btnTwoTeeStartSecondCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnTwoTeeStartFirstTransfer.getTag() != null
                                && btnTwoTeeStartFirstTransfer.getTag().equals(selectedId)) {

                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));
                        } else {
                            btnTwoTeeStartSecondTransfer.setTag(selectedId);
                            btnTwoTeeStartSecondTransfer.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }
                    }
                });
            }
        });


        //three tee start的六个场地按钮
        btnThreeTeeStartFirstCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(getActivity(),
                        courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(getActivity(), null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartFirstTransfer.getTag() != null
                                && btnThreeTeeStartFirstTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartSecondCourse.getTag() != null
                                && btnThreeTeeStartSecondCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartThirdCourse.getTag() != null
                                && btnThreeTeeStartThirdCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else {
                            btnThreeTeeStartFirstCourse.setTag(selectedId);
                            btnThreeTeeStartFirstCourse.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }

                    }
                });
            }
        });


        btnThreeTeeStartFirstTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(getActivity(),
                        courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(getActivity(), null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartFirstCourse.getTag() != null
                                && btnThreeTeeStartFirstCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartSecondTransfer.getTag() != null
                                && btnThreeTeeStartSecondTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartThirdTransfer.getTag() != null
                                && btnThreeTeeStartThirdTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else {
                            btnThreeTeeStartFirstTransfer.setTag(selectedId);
                            btnThreeTeeStartFirstTransfer.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }

                    }
                });
            }
        });


        btnThreeTeeStartSecondCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(getActivity(),
                        courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(getActivity(), null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartSecondTransfer.getTag() != null
                                && btnThreeTeeStartSecondTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartFirstCourse.getTag() != null
                                && btnThreeTeeStartFirstCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartThirdCourse.getTag() != null
                                && btnThreeTeeStartThirdCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else {
                            btnThreeTeeStartSecondCourse.setTag(selectedId);
                            btnThreeTeeStartSecondCourse.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }

                    }
                });
            }
        });


        btnThreeTeeStartSecondTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(getActivity(),
                        courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(getActivity(), null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartSecondCourse.getTag() != null && btnThreeTeeStartSecondCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartFirstTransfer.getTag() != null && btnThreeTeeStartFirstTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartThirdTransfer.getTag() != null && btnThreeTeeStartThirdTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else {
                            btnThreeTeeStartSecondTransfer.setTag(selectedId);
                            btnThreeTeeStartSecondTransfer.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }

                    }
                });
            }
        });


        btnThreeTeeStartThirdCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(getActivity(),
                        courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(getActivity(), null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartThirdTransfer.getTag() != null && btnThreeTeeStartThirdTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartFirstCourse.getTag() != null && btnThreeTeeStartFirstCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartSecondCourse.getTag() != null && btnThreeTeeStartSecondCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else {
                            btnThreeTeeStartThirdCourse.setTag(selectedId);
                            btnThreeTeeStartThirdCourse.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }

                    }
                });
            }
        });


        btnThreeTeeStartThirdTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(getActivity(),
                        courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(getActivity(), null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartThirdCourse.getTag() != null
                                && btnThreeTeeStartThirdCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartFirstTransfer.getTag() != null
                                && btnThreeTeeStartFirstTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartSecondTransfer.getTag() != null
                                && btnThreeTeeStartSecondTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_has_been_chose));

                        } else {
                            btnThreeTeeStartThirdTransfer.setTag(selectedId);
                            btnThreeTeeStartThirdTransfer.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }

                    }
                });
            }
        });


        cbCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                myListViewAdapter = new SegmentTimeCourseCheckAdapter(courseAreaMapList, cbCourse.isChecked());
                courseListView.setAdapter(myListViewAdapter);
                LayoutUtils.setListViewHeightBasedOnChildren(courseListView);

            }
        });

        cbMemberType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                memberTypeCheckAdapter = new SegmentTimeMemberTypeCheckAdapter(checkedMemberTypeMapList, cbMemberType.isChecked());
                memberTypeListView.setAdapter(memberTypeCheckAdapter);
                LayoutUtils.setListViewHeightBasedOnChildren(memberTypeListView);

            }
        });

    }

    @Override
    protected void setLayoutOfControls() {
        rlSegmentTime.addView(etTime);
        RelativeLayout.LayoutParams paramsEtTime = (RelativeLayout.LayoutParams) etTime.getLayoutParams();
        paramsEtTime.width = getActualWidthOnThisDevice(520);
        paramsEtTime.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEtTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsEtTime.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsEtTime.setMargins(getActualWidthOnThisDevice(40), 5, 0, 0);
        etTime.setLayoutParams(paramsEtTime);


        rlSegmentTime.addView(btnHistorySettings);
        RelativeLayout.LayoutParams paramsBtnHistorySettings = (RelativeLayout.LayoutParams) btnHistorySettings.getLayoutParams();
        paramsBtnHistorySettings.width = getActualWidthOnThisDevice(220);
        paramsBtnHistorySettings.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnHistorySettings.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsBtnHistorySettings.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        btnHistorySettings.setLayoutParams(paramsBtnHistorySettings);
        btnHistorySettings.setGravity(Gravity.END);

        rlSegmentType.addView(tvType);
        rlSegmentType.addView(ivTypeArrow);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvType, mContext);
        LayoutUtils.setRightArrow(ivTypeArrow, mContext);


        rlTwoTeeStartFirstBtnContainer.addView(btnTwoTeeStartFirstCourse);
        RelativeLayout.LayoutParams paramsBtnTwoTeeStartFirstCourse
                = (RelativeLayout.LayoutParams) btnTwoTeeStartFirstCourse.getLayoutParams();
        paramsBtnTwoTeeStartFirstCourse.width = (int) (getScreenWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = dp2px(35);
        paramsBtnTwoTeeStartFirstCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsBtnTwoTeeStartFirstCourse.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        btnTwoTeeStartFirstCourse.setLayoutParams(paramsBtnTwoTeeStartFirstCourse);

        rlTwoTeeStartFirstBtnContainer.addView(btnTwoTeeStartFirstTransfer);
        RelativeLayout.LayoutParams paramsBtnTwoTeeStartFirstTransfer
                = (RelativeLayout.LayoutParams) btnTwoTeeStartFirstTransfer.getLayoutParams();
        paramsBtnTwoTeeStartFirstTransfer.width = (int) (getScreenWidth() * 0.8);
        paramsBtnTwoTeeStartFirstTransfer.height = dp2px(35);
        paramsBtnTwoTeeStartFirstTransfer.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsBtnTwoTeeStartFirstTransfer.addRule(RelativeLayout.BELOW, btnTwoTeeStartFirstCourse.getId());
        paramsBtnTwoTeeStartFirstTransfer.setMargins(0, dp2px(1), 0, 0);
        btnTwoTeeStartFirstTransfer.setLayoutParams(paramsBtnTwoTeeStartFirstTransfer);

        rlTwoTeeStartFirstBtnContainer.addView(ivTwoTeeStartFirstArrow);
        RelativeLayout.LayoutParams paramsIvTwoTeeStartFirstArrow
                = (RelativeLayout.LayoutParams) ivTwoTeeStartFirstArrow.getLayoutParams();
        paramsIvTwoTeeStartFirstArrow.width = dp2px(40);
        paramsIvTwoTeeStartFirstArrow.height = dp2px(40);
        paramsIvTwoTeeStartFirstArrow.addRule(RelativeLayout.LEFT_OF, btnTwoTeeStartFirstCourse.getId());
        paramsIvTwoTeeStartFirstArrow.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivTwoTeeStartFirstArrow.setLayoutParams(paramsIvTwoTeeStartFirstArrow);


        rlTwoTeeStartSecondBtnContainer.addView(btnTwoTeeStartSecondCourse);
        RelativeLayout.LayoutParams paramsBtnTwoTeeStartSecondCourse
                = (RelativeLayout.LayoutParams) btnTwoTeeStartSecondCourse.getLayoutParams();
        paramsBtnTwoTeeStartSecondCourse.width = (int) (getScreenWidth() * 0.8);
        paramsBtnTwoTeeStartSecondCourse.height = dp2px(35);
        paramsBtnTwoTeeStartSecondCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsBtnTwoTeeStartSecondCourse.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        btnTwoTeeStartSecondCourse.setLayoutParams(paramsBtnTwoTeeStartSecondCourse);

        rlTwoTeeStartSecondBtnContainer.addView(btnTwoTeeStartSecondTransfer);
        RelativeLayout.LayoutParams paramsBtnTwoTeeStartSecondTransfer
                = (RelativeLayout.LayoutParams) btnTwoTeeStartSecondTransfer.getLayoutParams();
        paramsBtnTwoTeeStartSecondTransfer.width = (int) (getScreenWidth() * 0.8);
        paramsBtnTwoTeeStartSecondTransfer.height = dp2px(35);
        paramsBtnTwoTeeStartSecondTransfer.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsBtnTwoTeeStartSecondTransfer.addRule(RelativeLayout.BELOW, btnTwoTeeStartSecondCourse.getId());
        paramsBtnTwoTeeStartSecondTransfer.setMargins(0, dp2px(1), 0, 0);
        btnTwoTeeStartSecondTransfer.setLayoutParams(paramsBtnTwoTeeStartSecondTransfer);

        rlTwoTeeStartSecondBtnContainer.addView(ivTwoTeeStartSecondArrow);
        RelativeLayout.LayoutParams paramsIvTwoTeeStartSecondArrow
                = (RelativeLayout.LayoutParams) ivTwoTeeStartSecondArrow.getLayoutParams();
        paramsIvTwoTeeStartSecondArrow.width = dp2px(40);
        paramsIvTwoTeeStartSecondArrow.height = dp2px(40);
        paramsIvTwoTeeStartSecondArrow.addRule(RelativeLayout.LEFT_OF, btnTwoTeeStartSecondCourse.getId());
        paramsIvTwoTeeStartSecondArrow.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivTwoTeeStartSecondArrow.setLayoutParams(paramsIvTwoTeeStartSecondArrow);


        rlThreeTeeStartFirstBtnContainer.addView(btnThreeTeeStartFirstCourse);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartFirstCourse
                = (RelativeLayout.LayoutParams) btnThreeTeeStartFirstCourse.getLayoutParams();
        paramsBtnThreeTeeStartFirstCourse.width = (int) (getScreenWidth() * 0.8);
        paramsBtnThreeTeeStartFirstCourse.height = dp2px(35);
        paramsBtnThreeTeeStartFirstCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsBtnThreeTeeStartFirstCourse.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        btnTwoTeeStartFirstCourse.setLayoutParams(paramsBtnThreeTeeStartFirstCourse);

        rlThreeTeeStartFirstBtnContainer.addView(btnThreeTeeStartFirstTransfer);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartFirstTransfer
                = (RelativeLayout.LayoutParams) btnThreeTeeStartFirstTransfer.getLayoutParams();
        paramsBtnThreeTeeStartFirstTransfer.width = (int) (getScreenWidth() * 0.8);
        paramsBtnThreeTeeStartFirstTransfer.height = dp2px(35);
        paramsBtnThreeTeeStartFirstTransfer.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsBtnThreeTeeStartFirstTransfer.addRule(RelativeLayout.BELOW, btnThreeTeeStartFirstCourse.getId());
        paramsBtnThreeTeeStartFirstTransfer.setMargins(0, dp2px(1), 0, 0);
        btnThreeTeeStartFirstTransfer.setLayoutParams(paramsBtnThreeTeeStartFirstTransfer);

        rlThreeTeeStartFirstBtnContainer.addView(ivThreeTeeStartFirstArrow);
        RelativeLayout.LayoutParams paramsIvThreeTeeStartFirstArrow
                = (RelativeLayout.LayoutParams) ivThreeTeeStartFirstArrow.getLayoutParams();
        paramsIvThreeTeeStartFirstArrow.width = dp2px(40);
        paramsIvThreeTeeStartFirstArrow.height = dp2px(40);
        paramsIvThreeTeeStartFirstArrow.addRule(RelativeLayout.LEFT_OF, btnThreeTeeStartFirstCourse.getId());
        paramsIvThreeTeeStartFirstArrow.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivThreeTeeStartFirstArrow.setLayoutParams(paramsIvThreeTeeStartFirstArrow);


        rlThreeTeeStartSecondBtnContainer.addView(btnThreeTeeStartSecondCourse);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartSecondCourse
                = (RelativeLayout.LayoutParams) btnThreeTeeStartSecondCourse.getLayoutParams();
        paramsBtnThreeTeeStartSecondCourse.width = (int) (getScreenWidth() * 0.8);
        paramsBtnThreeTeeStartSecondCourse.height = dp2px(35);
        paramsBtnThreeTeeStartSecondCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsBtnThreeTeeStartSecondCourse.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        btnThreeTeeStartSecondCourse.setLayoutParams(paramsBtnThreeTeeStartSecondCourse);

        rlThreeTeeStartSecondBtnContainer.addView(btnThreeTeeStartSecondTransfer);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartSecondTransfer
                = (RelativeLayout.LayoutParams) btnThreeTeeStartSecondTransfer.getLayoutParams();
        paramsBtnThreeTeeStartSecondTransfer.width = (int) (getScreenWidth() * 0.8);
        paramsBtnThreeTeeStartSecondTransfer.height = dp2px(35);
        paramsBtnThreeTeeStartSecondTransfer.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsBtnThreeTeeStartSecondTransfer.addRule(RelativeLayout.BELOW, btnThreeTeeStartSecondCourse.getId());
        paramsBtnThreeTeeStartSecondTransfer.setMargins(0, dp2px(1), 0, 0);
        btnThreeTeeStartSecondTransfer.setLayoutParams(paramsBtnThreeTeeStartSecondTransfer);

        rlThreeTeeStartSecondBtnContainer.addView(ivThreeTeeStartSecondArrow);
        RelativeLayout.LayoutParams paramsIvThreeTeeStartSecondArrow
                = (RelativeLayout.LayoutParams) ivThreeTeeStartSecondArrow.getLayoutParams();
        paramsIvThreeTeeStartSecondArrow.width = dp2px(40);
        paramsIvThreeTeeStartSecondArrow.height = dp2px(40);
        paramsIvThreeTeeStartSecondArrow.addRule(RelativeLayout.LEFT_OF, btnThreeTeeStartSecondCourse.getId());
        paramsIvThreeTeeStartSecondArrow.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivThreeTeeStartSecondArrow.setLayoutParams(paramsIvThreeTeeStartSecondArrow);


        rlThreeTeeStartThirdBtnContainer.addView(btnThreeTeeStartThirdCourse);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartThirdCourse
                = (RelativeLayout.LayoutParams) btnThreeTeeStartThirdCourse.getLayoutParams();
        paramsBtnThreeTeeStartThirdCourse.width = (int) (getScreenWidth() * 0.8);
        paramsBtnThreeTeeStartThirdCourse.height = dp2px(35);
        paramsBtnThreeTeeStartThirdCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsBtnThreeTeeStartThirdCourse.addRule(RelativeLayout.ALIGN_PARENT_TOP, LAYOUT_TRUE);
        btnThreeTeeStartThirdCourse.setLayoutParams(paramsBtnThreeTeeStartThirdCourse);

        rlThreeTeeStartThirdBtnContainer.addView(btnThreeTeeStartThirdTransfer);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartThirdTransfer
                = (RelativeLayout.LayoutParams) btnThreeTeeStartThirdTransfer.getLayoutParams();
        paramsBtnThreeTeeStartThirdTransfer.width = (int) (getScreenWidth() * 0.8);
        paramsBtnThreeTeeStartThirdTransfer.height = dp2px(35);
        paramsBtnThreeTeeStartThirdTransfer.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsBtnThreeTeeStartThirdTransfer.addRule(RelativeLayout.BELOW, btnThreeTeeStartThirdCourse.getId());
        paramsBtnThreeTeeStartThirdTransfer.setMargins(0, dp2px(1), 0, 0);
        btnThreeTeeStartThirdTransfer.setLayoutParams(paramsBtnThreeTeeStartThirdTransfer);

        rlThreeTeeStartThirdBtnContainer.addView(ivThreeTeeStartThirdArrow);
        RelativeLayout.LayoutParams paramsIvThreeTeeStartThirdArrow
                = (RelativeLayout.LayoutParams) ivThreeTeeStartThirdArrow.getLayoutParams();
        paramsIvThreeTeeStartThirdArrow.width = dp2px(40);
        paramsIvThreeTeeStartThirdArrow.height = dp2px(40);
        paramsIvThreeTeeStartThirdArrow.addRule(RelativeLayout.LEFT_OF, btnThreeTeeStartThirdCourse.getId());
        paramsIvThreeTeeStartThirdArrow.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivThreeTeeStartThirdArrow.setLayoutParams(paramsIvThreeTeeStartThirdArrow);


        rlSegmentStartTime.addView(tvStartTime);
        rlSegmentStartTime.addView(tvStartTimeContent);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvStartTime, mContext);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvStartTimeContent, mContext);

        rlSegmentEndTime.addView(tvEndTime);
        rlSegmentEndTime.addView(tvEndTimeContent);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvEndTime, mContext);
        LayoutUtils.setCellRightValueViewOfRelativeLayout(tvEndTimeContent, mContext);

        rlSegmentCourse.addView(tvCourse);
        RelativeLayout.LayoutParams paramsTvCourse = (RelativeLayout.LayoutParams) tvCourse.getLayoutParams();
        paramsTvCourse.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCourse.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvCourse.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvCourse.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvCourse.setLayoutParams(paramsTvCourse);


        rlSegmentCourse.addView(cbCourse);
        RelativeLayout.LayoutParams paramsCbCourse = (RelativeLayout.LayoutParams) cbCourse.getLayoutParams();
        paramsCbCourse.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsCbCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsCbCourse.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsCbCourse.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsCbCourse.setMargins(0, 0, getActualWidthOnThisDevice(40), 0);
        cbCourse.setLayoutParams(paramsCbCourse);


        rlSegmentAllReserved.addView(tvAllReserved);
        RelativeLayout.LayoutParams paramsTvAllReserved = (RelativeLayout.LayoutParams) tvAllReserved.getLayoutParams();
        paramsTvAllReserved.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAllReserved.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAllReserved.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvAllReserved.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvAllReserved.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvAllReserved.setLayoutParams(paramsTvAllReserved);

        rlSegmentAllReserved.addView(swAllReserved);
        RelativeLayout.LayoutParams paramsSwAllReserved = (RelativeLayout.LayoutParams) swAllReserved.getLayoutParams();
        paramsSwAllReserved.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwAllReserved.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwAllReserved.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsSwAllReserved.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsSwAllReserved.setMargins(0, 0, 20, 0);
        swAllReserved.setLayoutParams(paramsSwAllReserved);


        rlSegmentMemberType.addView(tvMemberType);
        RelativeLayout.LayoutParams paramsTvMemberType = (RelativeLayout.LayoutParams) tvMemberType.getLayoutParams();
        paramsTvMemberType.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvMemberType.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvMemberType.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvMemberType.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvMemberType.setMargins(getActualWidthOnThisDevice(40), 0, 0, 0);
        tvMemberType.setLayoutParams(paramsTvMemberType);


        rlSegmentMemberType.addView(cbMemberType);
        RelativeLayout.LayoutParams paramsCbMemberType = (RelativeLayout.LayoutParams) cbMemberType.getLayoutParams();
        paramsCbMemberType.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsCbMemberType.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsCbMemberType.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsCbMemberType.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsCbMemberType.setMargins(0, 0, 20, 0);
        cbMemberType.setLayoutParams(paramsCbMemberType);


        rlSegmentPrimeTime.addView(tvSegmentPrimeTime);
        RelativeLayout.LayoutParams paramsTvSegmentPrimeTime = (RelativeLayout.LayoutParams) tvSegmentPrimeTime.getLayoutParams();
        paramsTvSegmentPrimeTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvSegmentPrimeTime.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvSegmentPrimeTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvSegmentPrimeTime.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsTvSegmentPrimeTime.setMargins(20, 0, 0, 0);
        tvSegmentPrimeTime.setLayoutParams(paramsTvSegmentPrimeTime);

        rlSegmentPrimeTime.addView(swThreeToReserve);
        RelativeLayout.LayoutParams paramsIbSegmentPrimeTime = (RelativeLayout.LayoutParams) swThreeToReserve.getLayoutParams();
        paramsIbSegmentPrimeTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIbSegmentPrimeTime.height = dp2px(30);
        paramsIbSegmentPrimeTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIbSegmentPrimeTime.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paramsIbSegmentPrimeTime.setMargins(0, 0, 20, 0);
        swThreeToReserve.setLayoutParams(paramsIbSegmentPrimeTime);

    }

    @Override
    protected void setPropertyOfControls() {

        expandListView = (SlideExpandableListView) getRootView().findViewById(R.id.lv_segment_history_settings);
        expandListView.setVisibility(View.GONE);

        courseListView = (ListView) getRootView().findViewById(R.id.course_list_view);
        courseListView.setVisibility(View.GONE);


        memberTypeListView = (ListView) getRootView().findViewById(R.id.member_type_list_view);
        memberTypeListView.setVisibility(View.GONE);

        deleteDayListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int groupPosition = (Integer) view.getTag();
                String date = editData.getSegmentTimesList().get(groupPosition).getDate();
                String courseId = editData.getCourseId().toString();
                Log.e("DJZ", "groupPosition : " + groupPosition);
                Log.e("DJZ", date);
                Map<String, String> params = new HashMap<>();
                params.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
                params.put(ApiKey.COURSE_ID, courseId);
                params.put(ApiKey.ADMINISTRATION_DATE, date);
                params.put(ApiKey.SEGMENT_TIME_SEGMENT_ID, Constants.STR_EMPTY);
                HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(SegmentTimesEditOrAddFragment.this) {
                    @Override
                    public void onJsonSuccess(BaseJsonObject jo) {
                        Integer returnCode = jo.getReturnCode();
                        String msg = jo.getReturnInfo();
                        if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {

                            getSegmentTime();
                        } else {
                            Utils.showShortToast(getActivity(), msg);
                        }
                    }

                    @Override
                    public void onJsonError(VolleyError error) {

                    }
                };
                hh.start(getActivity(), ApiManager.HttpApi.DelSegmentTime, params);
            }
        };

        LayoutUtils.setListViewHeightBasedOnChildren(expandListView);

        etTime.setId(View.generateViewId());
        etTime.setBackground(null);
        etTime.setSingleLine();
        etTime.setText(timeShow);
        etTime.setHorizontallyScrolling(true);
        etTime.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        etTime.setTextColor(getColor(R.color.common_black));
        etTime.setMovementMethod(ScrollingMovementMethod.getInstance());

        tvType.setTextColor(getColor(R.color.common_black));

        btnHistorySettings.setId(View.generateViewId());
        btnHistorySettings.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
        btnHistorySettings.setText(getString(R.string.segment_times_expand));
        btnHistorySettings.setGravity(Gravity.CENTER);
        btnHistorySettings.setTextColor(getColor(R.color.common_blue));

        tvType.setId(View.generateViewId());
        if (courseAreaSize == 1) {
            tvType.setText(getString(R.string.segment_times_nine_holes_only));
            tvType.setTag(NINE_HOLES_ONLY_ID);
        } else {
            tvType.setText(getString(R.string.segment_times_two_tee_start));
            tvType.setTag(TWO_TEE_START_ID);
        }


        ivTypeArrow.setId(View.generateViewId());
        ivTypeArrow.setImageResource(R.drawable.icon_right_arrow);

        setCourseButtonProperty(btnTwoTeeStartFirstCourse);
        setCourseButtonProperty(btnTwoTeeStartFirstTransfer);

        ivTwoTeeStartFirstArrow.setId(View.generateViewId());
        ivTwoTeeStartFirstArrow.setImageResource(R.drawable.icon_left_arrow);

        setCourseButtonProperty(btnTwoTeeStartSecondCourse);
        setCourseButtonProperty(btnTwoTeeStartSecondTransfer);

        ivTwoTeeStartSecondArrow.setId(View.generateViewId());
        ivTwoTeeStartSecondArrow.setImageResource(R.drawable.icon_left_arrow);

        setCourseButtonProperty(btnThreeTeeStartFirstCourse);
        setCourseButtonProperty(btnThreeTeeStartFirstTransfer);

        ivThreeTeeStartFirstArrow.setId(View.generateViewId());
        ivThreeTeeStartFirstArrow.setImageResource(R.drawable.icon_left_arrow);

        setCourseButtonProperty(btnThreeTeeStartSecondCourse);
        setCourseButtonProperty(btnThreeTeeStartSecondTransfer);

        ivThreeTeeStartSecondArrow.setId(View.generateViewId());
        ivThreeTeeStartSecondArrow.setImageResource(R.drawable.icon_left_arrow);

        setCourseButtonProperty(btnThreeTeeStartThirdCourse);
        setCourseButtonProperty(btnThreeTeeStartThirdTransfer);

        ivThreeTeeStartThirdArrow.setId(View.generateViewId());
        ivThreeTeeStartThirdArrow.setImageResource(R.drawable.icon_left_arrow);

        tvStartTime.setId(View.generateViewId());
        tvStartTime.setText(getString(R.string.segment_times_start_time));
        tvStartTime.setTextColor(getColor(R.color.common_black));

        tvStartTimeContent.setId(View.generateViewId());
        tvStartTimeContent.setTextColor(getColor(R.color.common_gray));
        rlSegmentStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String str = tvStartTimeContent.getText().toString();

                String[] times = str.split(Constants.STR_COLON);

                popupWindow = new SelectTimePopupWindow(getActivity(), times, 1);

                popupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View buttonView) {
                        String formatTime = String.format(Constants.STRING_FORMAT_02D_02D,
                                popupWindow.wheelViewHour.getCurrentItem(), popupWindow.wheelViewMin.getCurrentItem());
                        tvStartTimeContent.setText(formatTime);
                        popupWindow.dismiss();
                    }
                });

                popupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View buttonView) {
                        popupWindow.dismiss();
                    }
                });
            }
        });

        tvEndTime.setId(View.generateViewId());
        tvEndTime.setText(getString(R.string.segment_times_end_time));
        tvEndTime.setTextColor(getColor(R.color.common_black));

        tvEndTimeContent.setId(View.generateViewId());
        tvEndTimeContent.setTextColor(getColor(R.color.common_gray));
        rlSegmentEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                String str = tvEndTimeContent.getText().toString();

                String[] times = str.split(Constants.STR_COLON);

                popupWindow = new SelectTimePopupWindow(getActivity(), times, 1);


                popupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View buttonView) {
                        String formatTime = String.format(Constants.STRING_FORMAT_02D_02D,
                                popupWindow.wheelViewHour.getCurrentItem(), popupWindow.wheelViewMin.getCurrentItem());
                        tvEndTimeContent.setText(formatTime);
                        popupWindow.dismiss();
                    }
                });

                popupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View buttonView) {
                        popupWindow.dismiss();
                    }
                });
            }

        });

        tvCourse.setId(View.generateViewId());
        tvCourse.setText(getString(R.string.segment_times_course));
        tvCourse.setTextColor(getColor(R.color.common_black));

        cbCourse.setBackground(null);
        cbCourse.setButtonDrawable(getResources().getDrawable(R.drawable.black_checkbox));

        tvMemberType.setId(View.generateViewId());
        tvMemberType.setText(getString(R.string.segment_times_member_type));
        tvMemberType.setTextColor(getColor(R.color.common_black));
        cbMemberType.setBackground(null);
        cbMemberType.setButtonDrawable(getResources().getDrawable(R.drawable.black_checkbox));


        tvLifeMember.setId(View.generateViewId());
        tvLifeMember.setText(getString(R.string.segment_times_life_member));
        tvLifeMember.setTextColor(getColor(R.color.common_black));
        cbLifeMember.setBackground(null);
        cbLifeMember.setButtonDrawable(getResources().getDrawable(R.drawable.black_checkbox));


        tvAllReserved.setText(getString(R.string.segment_times_all_reserved));
        tvAllReserved.setTextColor(getColor(R.color.common_black));

        tvFamilyMember.setId(View.generateViewId());
        tvFamilyMember.setText(getString(R.string.segment_times_family_member));
        tvFamilyMember.setTextColor(getColor(R.color.common_black));
        cbFamilyMember.setBackground(null);
        cbFamilyMember.setButtonDrawable(getResources().getDrawable(R.drawable.black_checkbox));


        tvSegmentPrimeTime.setId(View.generateViewId());
        tvSegmentPrimeTime.setText(getString(R.string.segment_times_three_to_reserve));
        tvSegmentPrimeTime.setTextColor(getColor(R.color.common_black));

    }

    private void setCourseButtonProperty(IteeButton btnCourse) {
        btnCourse.setId(View.generateViewId());
        btnCourse.setTextColor(getColor(R.color.common_blue));
        btnCourse.setBackgroundResource(R.drawable.btn_course_check_bg);
        btnCourse.setTag(null);
        btnCourse.setText(Constants.STR_EMPTY);
        btnCourse.setTextSize(Constants.FONT_SIZE_SMALLER);
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    //界面展示所选转场的布局
    public void ShowCutTo(String addId) {

        final SegmentPeriodSettingLayout rlSegmentField = new SegmentPeriodSettingLayout(getActivity());
        periodSettingLayoutArrayList.add(rlSegmentField);

        ImageView ivFieldArrow = new ImageView(getActivity());

        fieldTime = startTime + Constants.STR_SEPARATOR_SPACE + endTime;

        rlSegmentField.tvFieldTime.setText(fieldTime);
        rlSegmentField.tvFieldContent.setText(fieldContent);
        ivFieldArrow.setImageResource(R.drawable.icon_right_arrow);


        if (addId.contains(Constants.STR_COMMA)) {
            String[] ids = addId.split(Constants.STR_COMMA);
            for (String id : ids) {
                rlSegmentField.addSegmentSettingId(id);
            }
        } else {
            rlSegmentField.addSegmentSettingId(addId);
        }

        rlSegmentField.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {

                Intent intent = new Intent();
                intent.setClass(getActivity(), SegmentTimeEditOrDeletePopupWindow.class);
                Bundle bundle = new Bundle();

                bundle.putSerializable("data", editData);
                bundle.putString(TransKey.SEGMENT_TIME_IDS, rlSegmentField.getSegmentSettingIds());
                bundle.putString(TransKey.SEGMENT_TIME_MULTI_DATE_TITLE, etTime.getText().toString());
                bundle.putInt(TransKey.SEGMENT_TIME_GROUP_POSITION, -1);
                bundle.putInt(TransKey.SEGMENT_TIME_CHILD_POSITION, -1);
                bundle.putInt(TransKey.SEGMENT_TIME_LAYOUT_INDEX, periodSettingLayoutArrayList.indexOf(rlSegmentField));
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });

        rlSegmentField.addView(rlSegmentField.tvFieldTime);
        rlSegmentField.tvFieldTime.setTextSize(Constants.FONT_SIZE_15);
        RelativeLayout.LayoutParams paramsFieldTime = (RelativeLayout.LayoutParams) rlSegmentField.tvFieldTime.getLayoutParams();
        paramsFieldTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsFieldTime.height = getActualHeightOnThisDevice(40);
        paramsFieldTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsFieldTime.setMargins(20, 0, 0, 0);
        rlSegmentField.tvFieldTime.setLayoutParams(paramsFieldTime);

        rlSegmentField.addView(rlSegmentField.tvFieldContent);

        rlSegmentField.tvFieldContent.setTextSize(Constants.FONT_SIZE_15);
        RelativeLayout.LayoutParams paramsTvFieldContent
                = (RelativeLayout.LayoutParams) rlSegmentField.tvFieldContent.getLayoutParams();
        paramsTvFieldContent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvFieldContent.height = getActualHeightOnThisDevice(40);
        paramsTvFieldContent.bottomMargin = getActualHeightOnThisDevice(5);
        paramsTvFieldContent.addRule(RelativeLayout.BELOW, rlSegmentField.tvFieldTime.getId());
        paramsTvFieldContent.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsTvFieldContent.setMargins(20, 40, 0, 0);
        rlSegmentField.tvFieldContent.setLayoutParams(paramsTvFieldContent);


        rlSegmentField.addView(ivFieldArrow);
        RelativeLayout.LayoutParams paramsIvFieldArrow = (RelativeLayout.LayoutParams) ivFieldArrow.getLayoutParams();
        paramsIvFieldArrow.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsIvFieldArrow.height = getActualHeightOnThisDevice(80);
        paramsIvFieldArrow.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsIvFieldArrow.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        ivFieldArrow.setLayoutParams(paramsIvFieldArrow);

        llSegmentTitle.addView(rlSegmentField);
        LinearLayout.LayoutParams paramsTitle = (LinearLayout.LayoutParams) rlSegmentField.getLayoutParams();
        paramsTitle.width = LinearLayout.LayoutParams.WRAP_CONTENT;
        paramsTitle.height = getActualHeightOnThisDevice(80);
        llSegmentTitle.setVisibility(View.VISIBLE);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.segment_times_segment_times);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.segment_times_ok);

        getTvRight().setOnClickListener(noDoubleClickListener);

    }

    private boolean twoTeeStartBtnTagCheck() {
        if (btnTwoTeeStartFirstCourse.getTag() != null && btnTwoTeeStartFirstTransfer.getTag() != null
                && btnTwoTeeStartSecondCourse.getTag() != null && btnTwoTeeStartSecondTransfer.getTag() != null) {
            return true;
        } else {
            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_input));
            return false;
        }

    }

    private boolean threeTeeStartBtnTagCheck() {
        if (btnThreeTeeStartFirstCourse.getTag() != null && btnThreeTeeStartFirstTransfer.getTag() != null
                && btnThreeTeeStartSecondCourse.getTag() != null && btnThreeTeeStartSecondTransfer.getTag() != null
                && btnThreeTeeStartThirdCourse.getTag() != null && btnThreeTeeStartThirdTransfer.getTag() != null) {
            return true;
        } else {
            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_input));
            return false;
        }

    }

    private boolean timeCheck() {


        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMAT_HHMM, Locale.getDefault());

        if (!Utils.isStringNotNullOrEmpty(startTime)) {
            Utils.showShortToast(getActivity(), getString(R.string.segment_times_start_time_input));
            return false;
        } else if (!Utils.isStringNotNullOrEmpty(endTime)) {
            Utils.showShortToast(getActivity(), getString(R.string.segment_times_end_time_input));
            return false;
        } else if (Utils.isStringNotNullOrEmpty(startTime) && Utils.isStringNotNullOrEmpty(endTime)) {

            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            try {
                c1.setTime(dateFormat.parse(startTime));
                c2.setTime(dateFormat.parse(endTime));

            } catch (ParseException e) {
                Utils.log(e.getMessage());
            }

            if (c2.compareTo(c1) <= 0) {
                Utils.showShortToast(getActivity(), getString(R.string.segment_times_time_input));
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    protected void setTimeShow() {
        if (dates.size() == 1) {
            timeShow = dates.get(0).replace(Constants.STR_SEPARATOR, Constants.STR_SLASH);
            etTime.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(timeShow, mContext));
        } else {
            timeShow = getEtTimeTitle().replace(Constants.STR_SEPARATOR, Constants.STR_SLASH);
            etTime.setText(timeShow);
        }
    }

    private String getDate() {
        StringBuilder sb = new StringBuilder();

        for (String date : dates) {
            if (sb.length() > 0) {
                sb.append(Constants.STR_COMMA);
            }
            sb.append("{\"date\":\"").append(date).append("\"}");
        }

        return Constants.STR_SQUARE_BRACKETS_START + sb.toString() + Constants.STR_SQUARE_BRACKETS_END;
    }

    private void getCourseCheckedId() {

        if (checkedCourseIdList.size() == 1) {

            transferCourse = "[{\"from\":{\"id\":" + checkedCourseIdList.get(0) + "},\"to\":null}]";
            fieldContent = (String) checkedCourseMapList.get(0).get(COURSE_AREA_NAME);

        } else if (checkedCourseIdList.size() >= 2) {
            StringBuilder sb = new StringBuilder();
            StringBuilder sbField = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < checkedCourseIdList.size() - 1; i++) {
                sb.append("{\"from\":{\"id\":")
                        .append(checkedCourseIdList.get(i))
                        .append("},\"to\":null},");
                sbField.append((String) checkedCourseMapList.get(i).get(COURSE_AREA_NAME));
            }
            sb.append("{\"from\":{\"id\":")
                    .append(checkedCourseIdList.get(checkedCourseIdList.size() - 1))
                    .append("},\"to\":null}]");
            sbField.append((String) checkedCourseMapList.get(checkedCourseMapList.size() - 1).get(COURSE_AREA_NAME));

            transferCourse = sb.toString();
            fieldContent = sbField.toString();
        }
    }

    private void getMemberTypeCheckedId() {
        if (checkedTypeIdList.size() == 1) {

            memberType = "[{\"type\":" + checkedTypeIdList.get(0) + ",\"name\":\"" + checkedMemberTypeList.get(0) + "\"}]";

        } else if (checkedTypeIdList.size() >= 2) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < checkedTypeIdList.size() - 1; i++) {
                sb.append("{\"type\":")
                        .append(checkedTypeIdList.get(i))
                        .append(",\"name\":\"")
                        .append(checkedMemberTypeList.get(i))
                        .append("\"},");
            }
            sb.append("{\"type\":")
                    .append(checkedTypeIdList.get(checkedTypeIdList.size() - 1))
                    .append(",\"name\":\"")
                    .append(checkedMemberTypeList.get(checkedMemberTypeList.size() - 1))
                    .append("\"}]");

            memberType = sb.toString();
        }
    }

    private void addSegmentTimes() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COURSE_ID, AppUtils.getCurrentCourseId(getActivity()));
        params.put(ApiKey.ADMINISTRATION_DATE, segmentTimeDate.replace(Constants.STR_SLASH, Constants.STR_SEPARATOR));
        params.put(ApiKey.TEE_TIME_CATEGORY_ID, category);
        params.put(ApiKey.SEGMENT_TIME_START_TIME, startTime + Constants.TIME_ADD_SS);
        params.put(ApiKey.SEGMENT_TIME_END_TIME, endTime + Constants.TIME_ADD_SS);
        params.put(ApiKey.SEGMENT_TIME_TRANSFER_COURSE, transferCourse);
        params.put(ApiKey.SEGMENT_TIME_MEMBER, memberType);
        params.put(ApiKey.SEGMENT_TIME_THREE_TO_RESERVE, threeToReserve);
        params.put(ApiKey.SEGMENT_TIME_ALL_RESERVE_MEMBER, allReserveMember);

        HttpManager<JsonDoEditSegmentTime> hh = new HttpManager<JsonDoEditSegmentTime>(SegmentTimesEditOrAddFragment.this) {

            @Override
            public void onJsonSuccess(JsonDoEditSegmentTime jo) {

                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY == returnCode) {
                    editData = jo.getDataList();
                    editAdapter = new SegmentTimeEditAdapter(SegmentTimesEditOrAddFragment.this, editData, deleteDayListener);

                    expandListView.initSlideMode(SlideExpandableListView.MOD_RIGHT);
                    expandListView.setAdapter(editAdapter);
                    LayoutUtils.setListViewHeightBasedOnChildren(expandListView);
                    if (Utils.isStringNotNullOrEmpty(editData.getAddId())) {
                        ShowCutTo(editData.getAddId());
                    }

                    if (editData.getSegmentTimesList().size() > 0) {
                        btnHistorySettings.setVisibility(View.VISIBLE);
                    } else {
                        btnHistorySettings.setVisibility(View.INVISIBLE);
                    }

                    //添加成功后清空所有可编辑项
                    cbCourse.setChecked(false);

                    myListViewAdapter = new SegmentTimeCourseCheckAdapter(courseAreaMapList, false);
                    courseListView.setAdapter(myListViewAdapter);
                    LayoutUtils.setListViewHeightBasedOnChildren(courseListView);

                    cbMemberType.setChecked(false);
                    memberTypeCheckAdapter = new SegmentTimeMemberTypeCheckAdapter(checkedMemberTypeMapList, false);
                    memberTypeListView.setAdapter(memberTypeCheckAdapter);
                    LayoutUtils.setListViewHeightBasedOnChildren(memberTypeListView);

                    tvStartTimeContent.setText(Constants.TIME_DEFAULT);
                    tvEndTimeContent.setText(Constants.TIME_DEFAULT);

                    btnTwoTeeStartFirstCourse.setTag(null);
                    btnTwoTeeStartFirstCourse.setText(Constants.STR_EMPTY);

                    btnTwoTeeStartFirstTransfer.setTag(null);
                    btnTwoTeeStartFirstTransfer.setText(Constants.STR_EMPTY);

                    btnTwoTeeStartSecondCourse.setTag(null);
                    btnTwoTeeStartSecondCourse.setText(Constants.STR_EMPTY);

                    btnTwoTeeStartSecondTransfer.setTag(null);
                    btnTwoTeeStartSecondTransfer.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartFirstCourse.setTag(null);
                    btnThreeTeeStartFirstCourse.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartFirstTransfer.setTag(null);
                    btnThreeTeeStartFirstTransfer.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartSecondCourse.setTag(null);
                    btnThreeTeeStartSecondCourse.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartSecondTransfer.setTag(null);
                    btnThreeTeeStartSecondTransfer.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartThirdCourse.setTag(null);
                    btnThreeTeeStartThirdCourse.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartThirdTransfer.setTag(null);
                    btnThreeTeeStartThirdTransfer.setText(Constants.STR_EMPTY);

                    swAllReserved.setChecked(false);
                    swThreeToReserve.setChecked(false);
                } else {
                    editData = jo.getDataList();
                    editAdapter = new SegmentTimeEditAdapter(SegmentTimesEditOrAddFragment.this, editData, deleteDayListener);
                    expandListView.initSlideMode(SlideExpandableListView.MOD_RIGHT);
                    expandListView.setAdapter(editAdapter);
                    LayoutUtils.setListViewHeightBasedOnChildren(expandListView);
                    for (int i = 0; i < editAdapter.getGroupCount(); i++) {
                        expandListView.expandGroup(i);
                    }
                    Utils.showShortToast(getActivity(), msg);
                }

            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };

        hh.start(getActivity(), ApiManager.HttpApi.DoAddSegmentTime, params);
    }

    private boolean courseAreaCheck() {

        checkedCourseIdList = new ArrayList<>();
        checkedCourseMapList = new ArrayList<>();


        for (int i = 0; i < courseCheckedList.size(); i++) {
            if (courseCheckedList.get(i)) {

                HashMap<String, Object> map = new HashMap<>();
                map.put(COURSE_AREA_ID, courseAreaMapList.get(i).get(COURSE_AREA_ID));
                map.put(COURSE_AREA_NAME, courseAreaMapList.get(i).get(COURSE_AREA_NAME));
                checkedCourseMapList.add(map);


                checkedCourseIdList.add((Integer) courseAreaMapList.get(i).get(COURSE_AREA_ID));
            }
        }


        if (checkedCourseIdList.size() > 0) {
            return true;
        } else {
            Utils.showShortToast(getActivity(), getString(R.string.segment_times_area_chose));
            return false;
        }

    }

    private boolean memberTypeCheck() {
        checkedTypeIdList = new ArrayList<>();
        checkedMemberTypeList = new ArrayList<>();


        for (int i = 0; i < memberTypeCheckedList.size(); i++) {
            if (memberTypeCheckedList.get(i)) {
                checkedTypeIdList.add((Integer) checkedMemberTypeMapList.get(i).get(TYPE_ID));
                checkedMemberTypeList.add((String) checkedMemberTypeMapList.get(i).get(MEMBER_TYPE));
            }
        }


        if (checkedTypeIdList.size() > 0) {
            return true;
        } else {
            Utils.showShortToast(getActivity(), getString(R.string.segment_times_member_type_chose));
            return false;
        }

    }

    @Override
    protected void executeOnceOnCreate() {
        Bundle bundle = getArguments();
        dates = bundle.getStringArrayList("dateList");
        initDate = bundle.getString("date");
        setTimeShow();
        getSegmentTime();
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
    }

    private void getSegmentTime() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.COURSE_ID, AppUtils.getCurrentCourseId(getActivity()));
        params.put(ApiKey.ADMINISTRATION_DATE, initDate);

        HttpManager<JsonDoEditSegmentTime> hh = new HttpManager<JsonDoEditSegmentTime>(SegmentTimesEditOrAddFragment.this) {

            @Override
            public void onJsonSuccess(JsonDoEditSegmentTime jo) {
                Integer returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    editData = jo.getDataList();
                    courseAreaList = new ArrayList<>();
                    courseAreaMapList = new ArrayList<>();
                    courseAreaList = editData.getCourseAreaList();

                    courseAreaSize = courseAreaList.size();
                    int courseAreaId;
                    String courseAreaName;
                    for (int i = 0; i < courseAreaSize; i++) {
                        HashMap<String, Object> map = new HashMap<>();
                        courseAreaId = courseAreaList.get(i).getAreaId();
                        courseAreaName = courseAreaList.get(i).getAreaName();
                        map.put(COURSE_AREA_ID, courseAreaId);
                        map.put(COURSE_AREA_NAME, courseAreaName);
                        courseAreaMapList.add(map);
                    }

                    memberTypeListList = new ArrayList<>();
                    checkedMemberTypeMapList = new ArrayList<>();
                    memberTypeListList = editData.getMemberTypeList();

                    memberTypeListSize = memberTypeListList.size();

                    int typeId;
                    String memberTypeName;
                    for (int i = 0; i < memberTypeListSize; i++) {
                        HashMap<String, Object> map = new HashMap<>();
                        typeId = memberTypeListList.get(i).getTypeId();
                        memberTypeName = memberTypeListList.get(i).getMemberType();
                        map.put(TYPE_ID, typeId);
                        map.put(MEMBER_TYPE, memberTypeName);
                        checkedMemberTypeMapList.add(map);
                    }

                    adapter = new SegmentTimeSettingAdapter(SegmentTimesEditOrAddFragment.this, editData, deleteDayListener);
                    expandListView.initSlideMode(SlideExpandableListView.MOD_RIGHT);
                    expandListView.setAdapter(adapter);
                    LayoutUtils.setListViewHeightBasedOnChildren(expandListView);

                    cbCourse.setChecked(false);
                    myListViewAdapter = new SegmentTimeCourseCheckAdapter(courseAreaMapList, false);
                    courseListView.setAdapter(myListViewAdapter);
                    LayoutUtils.setListViewHeightBasedOnChildren(courseListView);

                    cbMemberType.setChecked(false);
                    memberTypeCheckAdapter = new SegmentTimeMemberTypeCheckAdapter(checkedMemberTypeMapList, false);
                    memberTypeListView.setAdapter(memberTypeCheckAdapter);
                    LayoutUtils.setListViewHeightBasedOnChildren(memberTypeListView);

                    if (editData.getSegmentTimesList().size() > 0) {
                        btnHistorySettings.setVisibility(View.VISIBLE);
                    } else {
                        btnHistorySettings.setVisibility(View.INVISIBLE);
                    }


                    tvStartTimeContent.setText(Constants.TIME_DEFAULT);
                    tvEndTimeContent.setText(Constants.TIME_DEFAULT);


                    btnTwoTeeStartFirstCourse.setTag(null);
                    btnTwoTeeStartFirstCourse.setText(Constants.STR_EMPTY);

                    btnTwoTeeStartFirstTransfer.setTag(null);
                    btnTwoTeeStartFirstTransfer.setText(Constants.STR_EMPTY);

                    btnTwoTeeStartSecondCourse.setTag(null);
                    btnTwoTeeStartSecondCourse.setText(Constants.STR_EMPTY);

                    btnTwoTeeStartSecondTransfer.setTag(null);
                    btnTwoTeeStartSecondTransfer.setText(Constants.STR_EMPTY);


                    btnThreeTeeStartFirstCourse.setTag(null);
                    btnThreeTeeStartFirstCourse.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartFirstTransfer.setTag(null);
                    btnThreeTeeStartFirstTransfer.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartSecondCourse.setTag(null);
                    btnThreeTeeStartSecondCourse.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartSecondTransfer.setTag(null);
                    btnThreeTeeStartSecondTransfer.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartThirdCourse.setTag(null);
                    btnThreeTeeStartThirdCourse.setText(Constants.STR_EMPTY);

                    btnThreeTeeStartThirdTransfer.setTag(null);
                    btnThreeTeeStartThirdTransfer.setText(Constants.STR_EMPTY);

                    swAllReserved.setChecked(false);
                    swThreeToReserve.setChecked(false);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        mQueue = Volley.newRequestQueue(getActivity());
        hh.start(getActivity(), ApiManager.HttpApi.SegmentTime, params);
    }

    private String getEtTimeTitle() {
        boolean[] everyWeekDay = new boolean[]{true, true, true, true, true, true, true};
        String[] weekDayNames = new String[]{getString(R.string.calendar_sun)
                , getString(R.string.calendar_mon)
                , getString(R.string.calendar_tue)
                , getString(R.string.calendar_wed)
                , getString(R.string.calendar_thu)
                , getString(R.string.calendar_fri)
                , getString(R.string.calendar_sat)};

        String[] monthNames = new String[]{
                getString(R.string.calendar_jan),
                getString(R.string.calendar_feb),
                getString(R.string.calendar_mar),
                getString(R.string.calendar_apr),
                getString(R.string.calendar_may),
                getString(R.string.calendar_jun),
                getString(R.string.calendar_jul),
                getString(R.string.calendar_aug),
                getString(R.string.calendar_sep),
                getString(R.string.calendar_oct),
                getString(R.string.calendar_nov),
                getString(R.string.calendar_dec)
        };

        ArrayList<ArrayList<String>> weekDaysContainer = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDaysContainer.add(new ArrayList<String>());
        }

        StringBuilder monthString = new StringBuilder();
        StringBuilder weekString = new StringBuilder();
        StringBuilder periodString = new StringBuilder();
        StringBuilder singleString = new StringBuilder();

        Calendar calendar = Calendar.getInstance();
        ArrayList<String> weekDays = new ArrayList<>();
        ArrayList<String> monthDays = new ArrayList<>();
        LinkedList<String> periodDays = new LinkedList<>();

        int start = 0;
        int end = 0;
        int year = calendar.get(Calendar.YEAR);

        calendar.setTime(DateUtils.getDateFromAPIYearMonthDay(dates.get(0)));
        start = calendar.get(Calendar.MONTH);
        calendar.setTime(DateUtils.getDateFromAPIYearMonthDay(dates.get(dates.size() - 1)));
        end = calendar.get(Calendar.MONTH);

        calendar.setFirstDayOfWeek(Calendar.SUNDAY);
        String lastDay = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dates.get(0), mContext);

        for (int month = start; month <= end; month++) {
            monthDays.clear();
            for (int day = 1; day <= 31; day++) {
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                if (calendar.get(Calendar.MONTH) == month) {
                    String dateStr = DateUtils.getAPIYearMonthDay(calendar.getTime());
                    int weekDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    if (everyWeekDay[weekDay]) {
                        if (!dates.contains(dateStr)) {
                            everyWeekDay[weekDay] = false;
                        } else {
                            monthDays.add(dateStr);
                            weekDaysContainer.get(weekDay).add(dateStr);
                        }
                    }
                }
            }

            if (monthDays.size() == AppUtils.getMonthDaySize(year, month + 1)) {
                if (monthString.length() > 0) {
                    monthString.append(Constants.STR_COMMA);
                }
                monthString.append(AppUtils.generateSegmentTimeWholeMonthMessage(this, monthNames[month]));
                continue;
            }

            String choseWeekDay = Constants.STR_EMPTY;
            for (int weekDay = 0; weekDay < everyWeekDay.length; weekDay++) {
                if (everyWeekDay[weekDay]) {
                    if (Utils.isStringNotNullOrEmpty(choseWeekDay)) {
                        choseWeekDay += Constants.STR_COMMA;
                    }
                    choseWeekDay += weekDayNames[weekDay];
                    weekDays.addAll(weekDaysContainer.get(weekDay));
                }
                weekDaysContainer.get(weekDay).clear();
            }
            if (Utils.isStringNotNullOrEmpty(choseWeekDay)) {
                if (weekString.length() > 0) {
                    weekString.append(Constants.STR_COMMA);
                }
                weekString.append(AppUtils.generateSegmentTimeEveryWeekMessage(this, choseWeekDay, monthNames[month]));
            }

            for (int day = 1; day <= 31; day++) {
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                if (calendar.get(Calendar.MONTH) == month) {
                    String dateStr = DateUtils.getAPIYearMonthDay(calendar.getTime());
                    if (dates.contains(dateStr) && !weekDays.contains(dateStr)) {
                        if (periodDays.size() == 0 || lastDay.equals(periodDays.getLast())) {
                            periodDays.add(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dateStr, mContext));
                        } else {
                            generatePeriodString(periodDays, periodString, singleString);
                            periodDays.add(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dateStr, mContext));
                        }
                    }
                    lastDay = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dateStr, mContext);
                }
            }
        }

        if (periodDays.size() > 0) {
            generatePeriodString(periodDays, periodString, singleString);
        }

        String res = monthString.toString();
        if (Utils.isStringNotNullOrEmpty(res) && weekString.length() > 0) {
            res += Constants.STR_COMMA;
        }
        res += weekString.toString().replace(Constants.STR_SEPARATOR, Constants.STR_SLASH);

        if (Utils.isStringNotNullOrEmpty(res) && periodString.length() > 0) {
            res += Constants.STR_COMMA;
        }
        res += periodString.toString().replace(Constants.STR_SEPARATOR, Constants.STR_SLASH);

        if (Utils.isStringNotNullOrEmpty(res) && singleString.length() > 0) {
            res += Constants.STR_COMMA;
        }
        res += singleString.toString().replace(Constants.STR_SEPARATOR, Constants.STR_SLASH);

        return res;
    }

    private void generatePeriodString(LinkedList<String> periodDays, StringBuilder periodString, StringBuilder singleString) {
        if (periodDays.size() == 1) {
            if (singleString.length() > 0) {
                singleString.append(Constants.STR_COMMA);
            }
            singleString.append(periodDays.get(0));
        } else {
            if (periodString.length() > 0) {
                periodString.append(Constants.STR_COMMA);
            }
            periodString.append(periodDays.get(0))
                    .append(Constants.STR_WAVE)
                    .append(periodDays.getLast());
        }
        periodDays.clear();
    }

    class SegmentPeriodSettingLayout extends RelativeLayout {

        IteeTextView tvFieldTime = new IteeTextView(getActivity());
        IteeTextView tvFieldContent = new IteeTextView(getActivity());
        private ArrayList<String> segmentIdList = new ArrayList<>();

        public SegmentPeriodSettingLayout(Context context) {
            super(context);
        }

        public void addSegmentSettingId(String id) {
            segmentIdList.add(id);
        }

        public String getSegmentSettingIds() {
            StringBuilder sb = new StringBuilder();
            for (String id : segmentIdList) {
                if (sb.length() > 0) {
                    sb.append(Constants.STR_COMMA);
                }
                sb.append(id);
            }
            return sb.toString();
        }
    }

    public class SegmentTimeCourseCheckAdapter extends BaseAdapter {

        private int listSize;

        public SegmentTimeCourseCheckAdapter(ArrayList<HashMap<String, Object>> arrayList, boolean isCheck) {
            this.listSize = arrayList.size();
            courseCheckedList = new ArrayList<>(listSize);
            for (int i = 0; i < arrayList.size(); i++) {
                courseCheckedList.add(isCheck);
            }
        }

        @Override
        public int getCount() {
            return courseAreaMapList.size();
        }

        @Override
        public Object getItem(int position) {
            return courseAreaMapList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater
                        .from(getActivity());
                convertView = inflater.inflate(R.layout.item_of_segment_times_course_check, null);
                holder = new ViewHolder();
                holder.content = (IteeTextView) convertView
                        .findViewById(R.id.tv_course_area_name);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_course_area_name);

                holder.checkBox.setOnCheckedChangeListener(courseCheckedChangeListener);

                RelativeLayout.LayoutParams paramsCheckBox = (RelativeLayout.LayoutParams) holder.checkBox.getLayoutParams();
                paramsCheckBox.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                paramsCheckBox.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                paramsCheckBox.rightMargin = getActualWidthOnThisDevice(40);
                paramsCheckBox.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
                paramsCheckBox.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
                holder.checkBox.setLayoutParams(paramsCheckBox);

                holder.checkBox.setBackground(null);
                holder.checkBox.setButtonDrawable(getResources().getDrawable(R.drawable.black_checkbox));

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.content.setText((String) courseAreaMapList.get(position).get(COURSE_AREA_NAME));
            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(holder.content,getBaseActivity());
            holder.checkBox.setId(position);
            if (!courseCheckedList.isEmpty()) {
                boolean isChecked = courseCheckedList.get(position);
                holder.checkBox.setChecked(isChecked);
            }

            return convertView;
        }

        class ViewHolder {
            CheckBox checkBox;
            IteeTextView content;
        }
    }

    private class CourseCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            Message message = new Message();
            Bundle bundle = new Bundle();
            message.setData(bundle);
            courseHandler.sendMessage(message);
            courseCheckedList.set(buttonView.getId(), isChecked);
        }

    }

    public class SegmentTimeMemberTypeCheckAdapter extends BaseAdapter {

        private int listSize;

        public SegmentTimeMemberTypeCheckAdapter(ArrayList<HashMap<String, Object>> arrayList, boolean isCheck) {
            this.listSize = arrayList.size();
            memberTypeCheckedList = new ArrayList<>(listSize);
            for (int i = 0; i < arrayList.size(); i++) {
                memberTypeCheckedList.add(isCheck);
            }
        }

        @Override
        public int getCount() {
            return checkedMemberTypeMapList.size();
        }

        @Override
        public Object getItem(int position) {
            return checkedMemberTypeMapList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.item_of_segment_times_member_type_check, null);
                holder = new ViewHolder();
                holder.content = (IteeTextView) convertView.findViewById(R.id.tv_member_type);
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.cb_member_type);

                holder.checkBox.setOnCheckedChangeListener(memberTypeCheckedChangeListener);

                RelativeLayout.LayoutParams paramsCheckBox = (RelativeLayout.LayoutParams) holder.checkBox.getLayoutParams();
                paramsCheckBox.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
                paramsCheckBox.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                paramsCheckBox.rightMargin = getActualWidthOnThisDevice(40);
                paramsCheckBox.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
                paramsCheckBox.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
                holder.checkBox.setLayoutParams(paramsCheckBox);

                holder.checkBox.setBackground(null);
                holder.checkBox.setButtonDrawable(getResources().getDrawable(R.drawable.black_checkbox));

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.content.setText((String) checkedMemberTypeMapList.get(position).get(MEMBER_TYPE));

            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(holder.content,getBaseActivity());
            holder.checkBox.setId(position);
            if (!memberTypeCheckedList.isEmpty()) {
                boolean isChecked = memberTypeCheckedList.get(position);
                holder.checkBox.setChecked(isChecked);
            }

            return convertView;
        }

        class ViewHolder {
            CheckBox checkBox;
            IteeTextView content;
        }
    }

    private class MemberTypeCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                                     boolean isChecked) {
            Message message = new Message();
            Bundle bundle = new Bundle();
            message.setData(bundle);
            memberTypeHandler.sendMessage(message);
            memberTypeCheckedList.set(buttonView.getId(), isChecked);
        }

    }


}
