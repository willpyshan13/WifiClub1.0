/**
 * Project Name: itee
 * File Name:  SegmentTimeEditPopupWindow.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-03-25
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.SegmentTimesCourseSelectAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonDoEditSegmentTime;
import cn.situne.itee.manager.jsonentity.JsonLogin;
import cn.situne.itee.view.popwindow.SegmentTimesSelectCoursePopupWindow;
import cn.situne.itee.view.popwindow.SelectTimePopupWindow;

/**
 * ClassName:SegmentTimeEditPopupWindow <br/>
 * Function: SegmentTimeEditPopupWindow. <br/>
 * Date: 2015-03-25 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class SegmentTimeEditOrDeletePopupWindow extends FragmentActivity {

    private static final int TWO_TEE_START = 1;
    private static final int NINE_HOLES_ONLY = 2;
    private static final int BLOCK_TIMES = 3;
    private static final int MEMBER_ONLY = 4;
    private static final int THREE_TEE_START = 5;
    private static final int PRIME_TIME = 6;

    private static final String COURSE_AREA_ID = "courseAreaId";
    private static final String COURSE_AREA_NAME = "courseAreaName";

    private static final String MEMBER_TYPE = "memberType";
    private static final String TYPE_ID = "typeId";
    public IteeTextView tvPopStartTimeContent;
    public IteeTextView tvPopEndTimeContent;
    public IteeButton btnPopCommit;
    public IteeButton btnPopDelete;
    private LinearLayout popLayout;
    private RelativeLayout rlPopTitle;
    private RelativeLayout rlStartTime;
    private RelativeLayout rlEndTime;
    private RelativeLayout rlSegmentCourse;
    private RelativeLayout rlCourseLine;
    private RelativeLayout rlCourseListLine;
    private LinearLayout llTwoTeeStartTransfer;
    private RelativeLayout rlTwoTeeStartFirstBtnContainer;
    private RelativeLayout rlTwoTeeStartSecondBtnContainer;
    private LinearLayout llThreeTeeStartTransfer;
    private RelativeLayout rlThreeTeeStartFirstBtnContainer;
    private RelativeLayout rlThreeTeeStartSecondBtnContainer;
    private RelativeLayout rlThreeTeeStartThirdBtnContainer;
    private RelativeLayout rlSegmentMemberType;
    private RelativeLayout rlSegmentAllReserved;
    private RelativeLayout rlSegmentPrimeTime;
    private RelativeLayout rlOfBtn;
    private IteeTextView tvPopTitleTime;
    private IteeTextView tvPopTitleType;
    private IteeTextView tvPopStartTime;
    private IteeTextView tvPopEndTime;
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
    private IteeTextView tvCourse;
    private CheckBox cbCourse;
    private IteeTextView tvAllReserved;
    private CheckSwitchButton swAllReserved;
    private IteeTextView tvMemberType;
    private CheckBox cbMemberType;
    private IteeTextView tvPrimeTime;
    private CheckSwitchButton swThreeToReserve;
    private ImageView ivLine;
    private AppUtils.NoDoubleClickListener commitNoDoubleClickListener;
    private AppUtils.NoDoubleClickListener deleteNoDoubleClickListener;


    private SelectTimePopupWindow popupWindow;
    private SegmentTimesSelectCoursePopupWindow segmentTimesSelectCoursePopupWindow;
    private JsonDoEditSegmentTime.DataList dataList = new JsonDoEditSegmentTime.DataList();
    private int groupPosition;
    private int childPosition;
    //修改时需提供的参数
    private String segmentId;
    private int categoryId;
    private String editDate;
    private String editStartTime;
    private String editEndTime;
    private String transferCourse;
    private int editAllReserved;
    private String editMember;
    private int editThreeToReserve;

    private String multiSegmentIds;

    //修改时默认显示的数据
    private String titleTime;
    private String titleType;
    private String startTime;
    private String endTime;

    private int layoutIndex = -1;

    private List<JsonDoEditSegmentTime.TransferName> transferNameList = new ArrayList<JsonDoEditSegmentTime.TransferName>();

    private int allReserved;
    private int threeToReserve;

    //显示course列表
    private ListView courseListView;
    //course area list
    private List<JsonDoEditSegmentTime.CourseArea> courseAreaList;
    //course area 的大小
    private int courseSize;
    //area list
    private List<JsonDoEditSegmentTime.Area> areaList;
    //通过areaList获得area的id集合
    private ArrayList<Integer> areaIdList;
    //通过courseAreaList获得map集合
    private ArrayList<HashMap<String, Object>> courseAreaMapList;

    //记录course选中状态
    private ArrayList<Boolean> courseCheckedList;

    //记录course初始选中状态
    private ArrayList<Boolean> courseCheckedListInit;

    //已选course的id集合
    private ArrayList<Integer> checkedCourseIdList;

    //得到已选course area的map集合
    private ArrayList<HashMap<String, Object>> checkedCourseMapList;

    //course area id
    private ArrayList<Integer> courseAreaIdList;

    private CourseCheckedChangeListener courseCheckedChangeListener = new CourseCheckedChangeListener();
    private SegmentTimeCourseCheckAdapter courseCheckAdapter;


    //显示member type列表
    private ListView memberTypeListView;
    //member type list集合
    private List<JsonDoEditSegmentTime.MemberTypeList> memberTypeListList;
    //member type list的size
    private int memberTypeListSize;
    //member type
    private List<JsonDoEditSegmentTime.MemberType> memberTypeList;
    //通过memberTypeList获得area的id集合
    private ArrayList<Integer> memberTypeIdList;
    //通过memberTypeList获得member type的id集合
    private ArrayList<Integer> memberTypeId;
    //通过memberTypeListList获得map集合
    private ArrayList<HashMap<String, Object>> memberTypeListMapList;

    //记录member type选中状态
    private ArrayList<Boolean> memberTypeCheckedList;

    //记录member type初始选中状态
    private ArrayList<Boolean> memberTypeCheckedListInit;

    //已选member type的id集合
    private ArrayList<Integer> checkedMemberTypeIdList;
    //已选member type的name集合
    private ArrayList<String> checkedMemberTypeList;

    //得到已选member type list的map集合
    private ArrayList<HashMap<String, Object>> checkedMemberTypeMapList;

    //member type id
    private ArrayList<Integer> memberTypeListIdList;


    private MemberTypeCheckedChangeListener memberTypeCheckedChangeListener = new MemberTypeCheckedChangeListener();
    private SegmentTimeMemberTypeCheckAdapter memberTypeCheckAdapter;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.edit_or_delete_segment_time_popup_window);
        Bundle resultBundle = getIntent().getExtras();

        dataList = (JsonDoEditSegmentTime.DataList) resultBundle.getSerializable("data");
        groupPosition = resultBundle.getInt("groupPosition");
        childPosition = resultBundle.getInt("childPosition");

        JsonDoEditSegmentTime.SegmentTime currentSegmentTime = null;
        if (groupPosition == -1 && childPosition == -1) {
            titleTime = resultBundle.getString(TransKey.SEGMENT_TIME_MULTI_DATE_TITLE);
            multiSegmentIds = resultBundle.getString(TransKey.SEGMENT_TIME_IDS);
            layoutIndex = resultBundle.getInt(TransKey.SEGMENT_TIME_LAYOUT_INDEX, -1);
            String firstId;
            if (multiSegmentIds.contains(Constants.STR_COMMA)) {
                firstId = multiSegmentIds.split(Constants.STR_COMMA)[0];
            } else {
                firstId = multiSegmentIds;
            }

            for (JsonDoEditSegmentTime.SegmentTimes segmentTimes : dataList.getSegmentTimesList()) {
                for (JsonDoEditSegmentTime.SegmentTime segmentTime : segmentTimes.getSegmentTimesList()) {
                    if (firstId.equals(String.valueOf(segmentTime.getSegmentTimeId()))) {
                        currentSegmentTime = segmentTime;
                        break;
                    }
                }
            }
        } else {
            titleTime = dataList.getSegmentTimesList().get(groupPosition).getDate();
            currentSegmentTime = dataList.getSegmentTimesList().get(groupPosition).getSegmentTimesList().get(childPosition);
        }

        if (currentSegmentTime != null) {
            categoryId = currentSegmentTime.getCategoryId();
            startTime = currentSegmentTime.getTransferStartTime();
            endTime = currentSegmentTime.getTransferEndTime();

            courseAreaList = dataList.getCourseAreaList();
            courseAreaMapList = new ArrayList<>();
            courseSize = courseAreaList.size();
            int courseAreaId;
            String courseAreaName;
            for (int i = 0; i < courseSize; i++) {
                HashMap<String, Object> map = new HashMap<>();
                courseAreaId = courseAreaList.get(i).getAreaId();
                courseAreaName = courseAreaList.get(i).getAreaName();
                map.put(COURSE_AREA_ID, courseAreaId);
                map.put(COURSE_AREA_NAME, courseAreaName);
                courseAreaMapList.add(map);
            }


            memberTypeListList = dataList.getMemberTypeList();
            memberTypeListMapList = new ArrayList<>();
            memberTypeListSize = memberTypeListList.size();
            int typeId;
            String memberTypeName;
            for (int i = 0; i < memberTypeListSize; i++) {
                HashMap<String, Object> map = new HashMap<>();
                typeId = memberTypeListList.get(i).getTypeId();
                memberTypeName = memberTypeListList.get(i).getMemberType();
                map.put(TYPE_ID, typeId);
                map.put(MEMBER_TYPE, memberTypeName);
                memberTypeListMapList.add(map);
            }


            transferNameList = currentSegmentTime.getTransferNameList();
            areaList = currentSegmentTime.getAreaList();
            memberTypeList = currentSegmentTime.getMemberTypeList();
            segmentId = currentSegmentTime.getSegmentTimeId().toString();
            categoryId = currentSegmentTime.getCategoryId();


            if (currentSegmentTime.getAllReserveMember() != null) {

                allReserved = currentSegmentTime.getAllReserveMember();
            } else {
                allReserved = 0;
            }

            if (currentSegmentTime.getThreeToReserve() != null) {
                threeToReserve = currentSegmentTime.getThreeToReserve();
            } else {
                threeToReserve = 0;
            }


            //初始化布局
            initLayout();

            //初始化数据
            initData();

            //根据categoryId设置相应布局是否可见
            initVisibility();

            //设置监听
            setListener();
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    protected int getWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    protected int getHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        return DensityUtil.dip2px(this, dpValue);
    }

    /**
     *
     */
    protected void initLayout() {

        popLayout = (LinearLayout) this.findViewById(R.id.pop_layout);

        rlPopTitle = (RelativeLayout) this.findViewById(R.id.rl_pop_title);


        rlStartTime = (RelativeLayout) this.findViewById(R.id.rl_start_time);
        rlEndTime = (RelativeLayout) this.findViewById(R.id.rl_end_time);

        rlSegmentCourse = (RelativeLayout) this.findViewById(R.id.rl_segment_course);

        rlCourseLine = (RelativeLayout) this.findViewById(R.id.rl_course_line);

        rlCourseListLine = (RelativeLayout) this.findViewById(R.id.rl_course_list_line);

        courseListView = (ListView) this.findViewById(R.id.course_list_view);

        memberTypeListView = (ListView) this.findViewById(R.id.member_type_list_view);


        llTwoTeeStartTransfer = (LinearLayout) this.findViewById(R.id.ll_two_tee_start_btn);

        rlTwoTeeStartFirstBtnContainer = (RelativeLayout) this.findViewById(R.id.rl_two_tee_start_course_first);

        rlTwoTeeStartSecondBtnContainer = (RelativeLayout) this.findViewById(R.id.rl_two_tee_start_course_second);


        llThreeTeeStartTransfer = (LinearLayout) this.findViewById(R.id.ll_three_tee_start_btn);

        rlThreeTeeStartFirstBtnContainer = (RelativeLayout) this.findViewById(R.id.rl_three_tee_start_course_first);

        rlThreeTeeStartSecondBtnContainer = (RelativeLayout) this.findViewById(R.id.rl_three_tee_start_course_second);

        rlThreeTeeStartThirdBtnContainer = (RelativeLayout) this.findViewById(R.id.rl_three_tee_start_course_third);


        rlSegmentAllReserved = (RelativeLayout) this.findViewById(R.id.rl_segment_all_reserved);
        rlSegmentMemberType = (RelativeLayout) this.findViewById(R.id.rl_segment_member_type);

        rlSegmentPrimeTime = (RelativeLayout) this.findViewById(R.id.rl_segment_prime_time);

        rlOfBtn = (RelativeLayout) this.findViewById(R.id.rl_of_btn);


        tvPopTitleTime = new IteeTextView(this);
        tvPopTitleType = new IteeTextView(this);

        tvPopStartTime = new IteeTextView(this);
        tvPopStartTimeContent = new IteeTextView(this);
        tvPopEndTime = new IteeTextView(this);
        tvPopEndTimeContent = new IteeTextView(this);

        btnTwoTeeStartFirstCourse = new IteeButton(this);
        btnTwoTeeStartFirstTransfer = new IteeButton(this);
        btnTwoTeeStartSecondCourse = new IteeButton(this);
        btnTwoTeeStartSecondTransfer = new IteeButton(this);

        btnThreeTeeStartFirstCourse = new IteeButton(this);
        btnThreeTeeStartFirstTransfer = new IteeButton(this);
        btnThreeTeeStartSecondCourse = new IteeButton(this);
        btnThreeTeeStartSecondTransfer = new IteeButton(this);
        btnThreeTeeStartThirdCourse = new IteeButton(this);
        btnThreeTeeStartThirdTransfer = new IteeButton(this);

        tvCourse = new IteeTextView(this);
        ivTwoTeeStartFirstArrow = new ImageView(this);
        ivTwoTeeStartSecondArrow = new ImageView(this);


        ivThreeTeeStartFirstArrow = new ImageView(this);
        ivThreeTeeStartSecondArrow = new ImageView(this);
        ivThreeTeeStartThirdArrow = new ImageView(this);

        cbCourse = new CheckBox(this);


        tvAllReserved = new IteeTextView(this);
        swAllReserved = new CheckSwitchButton(this);
        tvMemberType = new IteeTextView(this);

        cbMemberType = new CheckBox(this);

        tvPrimeTime = new IteeTextView(this);
        swThreeToReserve = new CheckSwitchButton(this);

        ivLine = new ImageView(this);
        btnPopCommit = new IteeButton(this);
        btnPopDelete = new IteeButton(this);


        rlPopTitle.addView(tvPopTitleTime);
        RelativeLayout.LayoutParams paramsTvPopTitleTime = (RelativeLayout.LayoutParams) tvPopTitleTime.getLayoutParams();
        paramsTvPopTitleTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopTitleTime.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopTitleTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvPopTitleTime.setMargins(20, 0, 0, 0);
        tvPopTitleTime.setLayoutParams(paramsTvPopTitleTime);

        rlPopTitle.addView(tvPopTitleType);
        RelativeLayout.LayoutParams paramsTvPopTitleType = (RelativeLayout.LayoutParams) tvPopTitleType.getLayoutParams();
        paramsTvPopTitleType.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopTitleType.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopTitleType.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvPopTitleType.setMargins(0, 0, 20, 0);
        tvPopTitleType.setLayoutParams(paramsTvPopTitleType);

        rlStartTime.addView(tvPopStartTime);
        RelativeLayout.LayoutParams paramsTvPopStartTime = (RelativeLayout.LayoutParams) tvPopStartTime.getLayoutParams();
        paramsTvPopStartTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopStartTime.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopStartTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvPopStartTime.setMargins(20, 0, 0, 0);
        tvPopStartTime.setLayoutParams(paramsTvPopStartTime);

        rlStartTime.addView(tvPopStartTimeContent);
        RelativeLayout.LayoutParams paramsTvPopStartTimeContent = (RelativeLayout.LayoutParams) tvPopStartTimeContent.getLayoutParams();
        paramsTvPopStartTimeContent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopStartTimeContent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopStartTimeContent.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvPopStartTimeContent.setMargins(0, 0, 20, 0);
        tvPopStartTimeContent.setLayoutParams(paramsTvPopStartTimeContent);

        rlEndTime.addView(tvPopEndTime);
        RelativeLayout.LayoutParams paramsTvPopEndTime = (RelativeLayout.LayoutParams) tvPopEndTime.getLayoutParams();
        paramsTvPopEndTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopEndTime.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopEndTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvPopEndTime.setMargins(20, 0, 0, 0);
        tvPopEndTime.setLayoutParams(paramsTvPopEndTime);

        rlEndTime.addView(tvPopEndTimeContent);
        RelativeLayout.LayoutParams paramsTvPopEndTimeContent = (RelativeLayout.LayoutParams) tvPopEndTimeContent.getLayoutParams();
        paramsTvPopEndTimeContent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopEndTimeContent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPopEndTimeContent.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvPopEndTimeContent.setMargins(0, 0, 20, 0);
        tvPopEndTimeContent.setLayoutParams(paramsTvPopEndTimeContent);

        rlTwoTeeStartFirstBtnContainer.addView(btnTwoTeeStartFirstCourse);
        RelativeLayout.LayoutParams paramsBtnTwoTeeStartFirstCourse = (RelativeLayout.LayoutParams) btnTwoTeeStartFirstCourse.getLayoutParams();
        paramsBtnTwoTeeStartFirstCourse.width = (int) (getWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnTwoTeeStartFirstCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsBtnTwoTeeStartFirstCourse.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);

        btnTwoTeeStartFirstCourse.setLayoutParams(paramsBtnTwoTeeStartFirstCourse);

        rlTwoTeeStartFirstBtnContainer.addView(btnTwoTeeStartFirstTransfer);
        RelativeLayout.LayoutParams paramsBtnTwoTeeStartFirstTransfer = (RelativeLayout.LayoutParams) btnTwoTeeStartFirstTransfer.getLayoutParams();
        paramsBtnTwoTeeStartFirstTransfer.width = (int) (getWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnTwoTeeStartFirstTransfer.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsBtnTwoTeeStartFirstTransfer.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        btnTwoTeeStartFirstTransfer.setLayoutParams(paramsBtnTwoTeeStartFirstTransfer);

        rlTwoTeeStartFirstBtnContainer.addView(ivTwoTeeStartFirstArrow);
        RelativeLayout.LayoutParams paramsIvTwoTeeStartFirstArrow = (RelativeLayout.LayoutParams) ivTwoTeeStartFirstArrow.getLayoutParams();
        paramsIvTwoTeeStartFirstArrow.width = dp2px(40);
        paramsIvTwoTeeStartFirstArrow.height = dp2px(40);
        paramsIvTwoTeeStartFirstArrow.addRule(RelativeLayout.LEFT_OF, btnTwoTeeStartFirstCourse.getId());
        paramsIvTwoTeeStartFirstArrow.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        ivTwoTeeStartFirstArrow.setLayoutParams(paramsIvTwoTeeStartFirstArrow);


        rlTwoTeeStartSecondBtnContainer.addView(btnTwoTeeStartSecondCourse);
        RelativeLayout.LayoutParams paramsBtnTwoTeeStartSecondCourse = (RelativeLayout.LayoutParams) btnTwoTeeStartSecondCourse.getLayoutParams();
        paramsBtnTwoTeeStartSecondCourse.width = (int) (getWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnTwoTeeStartSecondCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsBtnTwoTeeStartSecondCourse.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        btnTwoTeeStartSecondCourse.setLayoutParams(paramsBtnTwoTeeStartSecondCourse);

        rlTwoTeeStartSecondBtnContainer.addView(btnTwoTeeStartSecondTransfer);
        RelativeLayout.LayoutParams paramsBtnTwoTeeStartSecondTransfer = (RelativeLayout.LayoutParams) btnTwoTeeStartSecondTransfer.getLayoutParams();
        paramsBtnTwoTeeStartSecondTransfer.width = (int) (getWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnTwoTeeStartSecondTransfer.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsBtnTwoTeeStartSecondTransfer.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        btnTwoTeeStartSecondTransfer.setLayoutParams(paramsBtnTwoTeeStartSecondTransfer);

        rlTwoTeeStartSecondBtnContainer.addView(ivTwoTeeStartSecondArrow);
        RelativeLayout.LayoutParams paramsIvTwoTeeStartSecondArrow = (RelativeLayout.LayoutParams) ivTwoTeeStartSecondArrow.getLayoutParams();
        paramsIvTwoTeeStartSecondArrow.width = dp2px(40);
        paramsIvTwoTeeStartSecondArrow.height = dp2px(40);
        paramsIvTwoTeeStartSecondArrow.addRule(RelativeLayout.LEFT_OF, btnTwoTeeStartSecondCourse.getId());
        paramsIvTwoTeeStartSecondArrow.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        ivTwoTeeStartSecondArrow.setLayoutParams(paramsIvTwoTeeStartSecondArrow);


        rlThreeTeeStartFirstBtnContainer.addView(btnThreeTeeStartFirstCourse);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartFirstCourse = (RelativeLayout.LayoutParams) btnThreeTeeStartFirstCourse.getLayoutParams();
        paramsBtnThreeTeeStartFirstCourse.width = (int) (getWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnThreeTeeStartFirstCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsBtnThreeTeeStartFirstCourse.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        btnTwoTeeStartFirstCourse.setLayoutParams(paramsBtnThreeTeeStartFirstCourse);

        rlThreeTeeStartFirstBtnContainer.addView(btnThreeTeeStartFirstTransfer);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartFirstTransfer = (RelativeLayout.LayoutParams) btnThreeTeeStartFirstTransfer.getLayoutParams();
        paramsBtnThreeTeeStartFirstTransfer.width = (int) (getWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnThreeTeeStartFirstTransfer.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsBtnThreeTeeStartFirstTransfer.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        btnThreeTeeStartFirstTransfer.setLayoutParams(paramsBtnThreeTeeStartFirstTransfer);

        rlThreeTeeStartFirstBtnContainer.addView(ivThreeTeeStartFirstArrow);
        RelativeLayout.LayoutParams paramsIvThreeTeeStartFirstArrow = (RelativeLayout.LayoutParams) ivThreeTeeStartFirstArrow.getLayoutParams();
        paramsIvThreeTeeStartFirstArrow.width = dp2px(40);
        paramsIvThreeTeeStartFirstArrow.height = dp2px(40);
        paramsIvThreeTeeStartFirstArrow.addRule(RelativeLayout.LEFT_OF, btnThreeTeeStartFirstCourse.getId());
        paramsIvThreeTeeStartFirstArrow.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        ivThreeTeeStartFirstArrow.setLayoutParams(paramsIvThreeTeeStartFirstArrow);


        rlThreeTeeStartSecondBtnContainer.addView(btnThreeTeeStartSecondCourse);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartSecondCourse = (RelativeLayout.LayoutParams) btnThreeTeeStartSecondCourse.getLayoutParams();
        paramsBtnThreeTeeStartSecondCourse.width = (int) (getWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnThreeTeeStartSecondCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsBtnThreeTeeStartSecondCourse.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        btnThreeTeeStartSecondCourse.setLayoutParams(paramsBtnThreeTeeStartSecondCourse);

        rlThreeTeeStartSecondBtnContainer.addView(btnThreeTeeStartSecondTransfer);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartSecondTransfer = (RelativeLayout.LayoutParams) btnThreeTeeStartSecondTransfer.getLayoutParams();
        paramsBtnThreeTeeStartSecondTransfer.width = (int) (getWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnThreeTeeStartSecondTransfer.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsBtnThreeTeeStartSecondTransfer.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        btnThreeTeeStartSecondTransfer.setLayoutParams(paramsBtnThreeTeeStartSecondTransfer);

        rlThreeTeeStartSecondBtnContainer.addView(ivThreeTeeStartSecondArrow);
        RelativeLayout.LayoutParams paramsIvThreeTeeStartSecondArrow = (RelativeLayout.LayoutParams) ivThreeTeeStartSecondArrow.getLayoutParams();
        paramsIvThreeTeeStartSecondArrow.width = dp2px(40);
        paramsIvThreeTeeStartSecondArrow.height = dp2px(40);
        paramsIvThreeTeeStartSecondArrow.addRule(RelativeLayout.LEFT_OF, btnThreeTeeStartSecondCourse.getId());
        paramsIvThreeTeeStartSecondArrow.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        ivThreeTeeStartSecondArrow.setLayoutParams(paramsIvThreeTeeStartSecondArrow);


        rlThreeTeeStartThirdBtnContainer.addView(btnThreeTeeStartThirdCourse);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartThirdCourse = (RelativeLayout.LayoutParams) btnThreeTeeStartThirdCourse.getLayoutParams();
        paramsBtnThreeTeeStartThirdCourse.width = (int) (getWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnThreeTeeStartThirdCourse.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsBtnThreeTeeStartThirdCourse.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        btnThreeTeeStartThirdCourse.setLayoutParams(paramsBtnThreeTeeStartThirdCourse);

        rlThreeTeeStartThirdBtnContainer.addView(btnThreeTeeStartThirdTransfer);
        RelativeLayout.LayoutParams paramsBtnThreeTeeStartThirdTransfer = (RelativeLayout.LayoutParams) btnThreeTeeStartThirdTransfer.getLayoutParams();
        paramsBtnThreeTeeStartThirdTransfer.width = (int) (getWidth() * 0.8);
        paramsBtnTwoTeeStartFirstCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsBtnThreeTeeStartThirdTransfer.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        paramsBtnThreeTeeStartThirdTransfer.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        btnThreeTeeStartThirdTransfer.setLayoutParams(paramsBtnThreeTeeStartThirdTransfer);

        rlThreeTeeStartThirdBtnContainer.addView(ivThreeTeeStartThirdArrow);
        RelativeLayout.LayoutParams paramsIvThreeTeeStartThirdArrow = (RelativeLayout.LayoutParams) ivThreeTeeStartThirdArrow.getLayoutParams();
        paramsIvThreeTeeStartThirdArrow.width = dp2px(40);
        paramsIvThreeTeeStartThirdArrow.height = dp2px(40);
        paramsIvThreeTeeStartThirdArrow.addRule(RelativeLayout.LEFT_OF, btnThreeTeeStartThirdCourse.getId());
        paramsIvThreeTeeStartThirdArrow.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        ivThreeTeeStartThirdArrow.setLayoutParams(paramsIvThreeTeeStartThirdArrow);

        rlSegmentCourse.addView(tvCourse);
        RelativeLayout.LayoutParams paramsTvCourse = (RelativeLayout.LayoutParams) tvCourse.getLayoutParams();
        paramsTvCourse.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvCourse.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvCourse.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvCourse.setMargins(20, 0, 0, 0);
        tvCourse.setLayoutParams(paramsTvCourse);

        rlSegmentCourse.addView(cbCourse);
        RelativeLayout.LayoutParams paramsCbCourse = (RelativeLayout.LayoutParams) cbCourse.getLayoutParams();
        paramsCbCourse.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsCbCourse.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsCbCourse.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsCbCourse.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsCbCourse.setMargins(0, 0, 20, 0);
        cbCourse.setLayoutParams(paramsCbCourse);


        rlSegmentMemberType.addView(tvMemberType);
        RelativeLayout.LayoutParams paramsTvMemberType = (RelativeLayout.LayoutParams) tvMemberType.getLayoutParams();
        paramsTvMemberType.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvMemberType.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvMemberType.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvMemberType.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvMemberType.setMargins(20, 0, 0, 0);
        tvMemberType.setLayoutParams(paramsTvMemberType);


        rlSegmentAllReserved.addView(tvAllReserved);
        RelativeLayout.LayoutParams paramsTvAllReserved = (RelativeLayout.LayoutParams) tvAllReserved.getLayoutParams();
        paramsTvAllReserved.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAllReserved.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvAllReserved.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvAllReserved.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvAllReserved.setMargins(20, 0, 0, 0);
        tvAllReserved.setLayoutParams(paramsTvAllReserved);

        rlSegmentAllReserved.addView(swAllReserved);
        RelativeLayout.LayoutParams paramsSwAllReserved = (RelativeLayout.LayoutParams) swAllReserved.getLayoutParams();
        paramsSwAllReserved.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwAllReserved.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwAllReserved.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsSwAllReserved.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsSwAllReserved.setMargins(0, 0, 20, 0);
        swAllReserved.setLayoutParams(paramsSwAllReserved);


        rlSegmentMemberType.addView(cbMemberType);
        RelativeLayout.LayoutParams paramsCbMemberType = (RelativeLayout.LayoutParams) cbMemberType.getLayoutParams();
        paramsCbMemberType.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsCbMemberType.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsCbMemberType.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsCbMemberType.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsCbMemberType.setMargins(0, 0, 20, 0);
        cbMemberType.setLayoutParams(paramsCbMemberType);


        rlSegmentPrimeTime.addView(tvPrimeTime);
        RelativeLayout.LayoutParams paramsTvPrimeTime = (RelativeLayout.LayoutParams) tvPrimeTime.getLayoutParams();
        paramsTvPrimeTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPrimeTime.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvPrimeTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvPrimeTime.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvPrimeTime.setMargins(20, 20, 0, 20);
        tvPrimeTime.setLayoutParams(paramsTvPrimeTime);

        rlSegmentPrimeTime.addView(swThreeToReserve);
        RelativeLayout.LayoutParams paramsSwPrimeTime = (RelativeLayout.LayoutParams) swThreeToReserve.getLayoutParams();
        paramsSwPrimeTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsSwPrimeTime.height = (int) (getHeight() * 0.06);
        paramsSwPrimeTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsSwPrimeTime.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsSwPrimeTime.setMargins(0, 10, 20, 10);
        swThreeToReserve.setLayoutParams(paramsSwPrimeTime);

        rlOfBtn.addView(ivLine);
        RelativeLayout.LayoutParams paramsIvLine = (RelativeLayout.LayoutParams) ivLine.getLayoutParams();
        paramsIvLine.width = DensityUtil.dip2px(this, 1);
        paramsIvLine.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsIvLine.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        ivLine.setLayoutParams(paramsIvLine);

        rlOfBtn.addView(btnPopCommit);
        RelativeLayout.LayoutParams paramsBtnPopCommit = (RelativeLayout.LayoutParams) btnPopCommit.getLayoutParams();
        paramsBtnPopCommit.width = (int) (getWidth() * 0.5);
        paramsBtnPopCommit.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsBtnPopCommit.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        btnPopCommit.setLayoutParams(paramsBtnPopCommit);

        rlOfBtn.addView(btnPopDelete);
        RelativeLayout.LayoutParams paramsBtnPopCancel = (RelativeLayout.LayoutParams) btnPopDelete.getLayoutParams();
        paramsBtnPopCancel.width = (int) (getWidth() * 0.5);
        paramsBtnPopCancel.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsBtnPopCancel.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        btnPopDelete.setLayoutParams(paramsBtnPopCancel);

    }

    /**
     *
     */
    protected void initData() {


        btnTwoTeeStartFirstCourse.setBackgroundResource(R.drawable.btn_course_check_bg);
        btnTwoTeeStartFirstTransfer.setBackgroundResource(R.drawable.btn_course_check_bg);
        btnTwoTeeStartSecondCourse.setBackgroundResource(R.drawable.btn_course_check_bg);
        btnTwoTeeStartSecondTransfer.setBackgroundResource(R.drawable.btn_course_check_bg);

        btnThreeTeeStartFirstCourse.setBackgroundResource(R.drawable.btn_course_check_bg);
        btnThreeTeeStartFirstTransfer.setBackgroundResource(R.drawable.btn_course_check_bg);
        btnThreeTeeStartSecondCourse.setBackgroundResource(R.drawable.btn_course_check_bg);
        btnThreeTeeStartSecondTransfer.setBackgroundResource(R.drawable.btn_course_check_bg);
        btnThreeTeeStartThirdCourse.setBackgroundResource(R.drawable.btn_course_check_bg);
        btnThreeTeeStartThirdTransfer.setBackgroundResource(R.drawable.btn_course_check_bg);


        tvPopTitleTime.setId(View.generateViewId());
        tvPopTitleTime.setText(titleTime);
        tvPopTitleTime.setTextColor(this.getResources().getColor(R.color.common_black));

        tvPopTitleType.setId(View.generateViewId());
        tvPopTitleType.setTextColor(getResources().getColor(R.color.common_black));

        tvPopStartTime.setId(View.generateViewId());
        tvPopStartTime.setText(getResources().getString(R.string.segment_times_start_time));
        tvPopStartTime.setTextColor(this.getResources().getColor(R.color.common_black));

        tvPopStartTimeContent.setId(View.generateViewId());
        tvPopStartTimeContent.setText(startTime.substring(0, 5));
        tvPopStartTimeContent.setTextColor(this.getResources().getColor(R.color.common_separator_gray));

        tvPopEndTime.setId(View.generateViewId());
        tvPopEndTime.setText(getResources().getString(R.string.segment_times_end_time));
        tvPopEndTime.setTextColor(this.getResources().getColor(R.color.common_black));

        tvPopEndTimeContent.setId(View.generateViewId());
        tvPopEndTimeContent.setText(endTime.substring(0, 5));
        tvPopEndTimeContent.setTextColor(this.getResources().getColor(R.color.common_separator_gray));


        tvCourse.setId(View.generateViewId());
        tvCourse.setText(getResources().getString(R.string.segment_times_course));
        tvCourse.setTextColor(this.getResources().getColor(R.color.common_black));
        cbCourse.setBackground(null);
        cbCourse.setButtonDrawable(getResources().getDrawable(R.drawable.black_checkbox));


        tvAllReserved.setText(getResources().getString(R.string.segment_times_all_reserved));
        tvAllReserved.setTextColor(this.getResources().getColor(R.color.common_black));

        tvMemberType.setText(getResources().getString(R.string.segment_times_member_type));
        tvMemberType.setTextColor(this.getResources().getColor(R.color.common_black));
        cbMemberType.setBackground(null);
        cbMemberType.setButtonDrawable(getResources().getDrawable(R.drawable.black_checkbox));


        tvPrimeTime.setText(getResources().getString(R.string.segment_times_three_to_reserve));
        tvPrimeTime.setTextColor(this.getResources().getColor(R.color.common_black));


        ivLine.setId(View.generateViewId());
        ivLine.setBackgroundColor(this.getResources().getColor(R.color.common_blue));

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(1, this.getResources().getColor(R.color.common_blue)); // 边框粗细及颜色
        drawable.setColor(this.getResources().getColor(R.color.common_white));


        btnPopCommit.setId(View.generateViewId());
        btnPopCommit.setText(getResources().getString(R.string.common_ok));
        btnPopCommit.setTextColor(this.getResources().getColor(R.color.common_blue));
        btnPopCommit.setBackground(drawable); // 设置背景（效果就是有边框及底色）

        btnPopDelete.setId(View.generateViewId());
        btnPopDelete.setText(getResources().getString(R.string.common_delete));
        btnPopDelete.setTextColor(getResources().getColor(R.color.common_red));
        btnPopDelete.setBackground(drawable);

        switch (categoryId) {

            case TWO_TEE_START:
                titleType = getResources().getString(R.string.segment_times_two_tee_start);

                if (transferNameList.size() == 2) {

                    btnTwoTeeStartFirstCourse.setTag(transferNameList.get(0).getFrom().getId());
                    btnTwoTeeStartFirstCourse.setText(transferNameList.get(0).getFrom().getName());

                    btnTwoTeeStartFirstTransfer.setTag(transferNameList.get(0).getTo().getId());
                    btnTwoTeeStartFirstTransfer.setText(transferNameList.get(0).getTo().getName());

                    btnTwoTeeStartSecondCourse.setTag(transferNameList.get(1).getFrom().getId());
                    btnTwoTeeStartSecondCourse.setText(transferNameList.get(1).getFrom().getName());

                    btnTwoTeeStartSecondTransfer.setTag(transferNameList.get(1).getTo().getId());
                    btnTwoTeeStartSecondTransfer.setText(transferNameList.get(1).getTo().getName());
                }

                break;
            case NINE_HOLES_ONLY:
                titleType = getResources().getString(R.string.segment_times_nine_holes_only);
                initCourseCheckedStatus();


                break;
            case BLOCK_TIMES:
                titleType = getResources().getString(R.string.segment_times_block_times);
                initCourseCheckedStatus();

                break;
            case MEMBER_ONLY:
                titleType = getResources().getString(R.string.segment_times_member_only);
                initCourseCheckedStatus();


                if (allReserved == Constants.SWITCH_RIGHT) {
                    swAllReserved.setChecked(true);
                } else if (allReserved == Constants.SWITCH_LEFT) {
                    swAllReserved.setChecked(false);
                }

                initMemberTypeCheckedStatus();


                break;
            case THREE_TEE_START:
                titleType = getResources().getString(R.string.segment_times_three_tee_start);
                if (transferNameList.size() == 3) {
                    btnThreeTeeStartFirstCourse.setTag(transferNameList.get(0).getFrom().getId());
                    btnThreeTeeStartFirstCourse.setText(transferNameList.get(0).getFrom().getName());

                    btnThreeTeeStartFirstTransfer.setTag(transferNameList.get(0).getTo().getId());
                    btnThreeTeeStartFirstTransfer.setText(transferNameList.get(0).getTo().getName());

                    btnThreeTeeStartSecondCourse.setTag(transferNameList.get(1).getFrom().getId());
                    btnThreeTeeStartSecondCourse.setText(transferNameList.get(1).getFrom().getName());

                    btnThreeTeeStartSecondTransfer.setTag(transferNameList.get(1).getTo().getId());
                    btnThreeTeeStartSecondTransfer.setText(transferNameList.get(1).getTo().getName());

                    btnThreeTeeStartThirdCourse.setTag(transferNameList.get(2).getFrom().getId());
                    btnThreeTeeStartThirdCourse.setText(transferNameList.get(2).getFrom().getName());

                    btnThreeTeeStartThirdTransfer.setTag(transferNameList.get(2).getTo().getId());
                    btnThreeTeeStartThirdTransfer.setText(transferNameList.get(2).getTo().getName());
                }


                break;
            case PRIME_TIME:
                titleType = getResources().getString(R.string.segment_times_prime_time);
                initCourseCheckedStatus();

                if (threeToReserve == Constants.SWITCH_LEFT) {
                    swThreeToReserve.setChecked(false);
                } else {
                    swThreeToReserve.setChecked(true);
                }


                break;

            default:

                break;
        }

        tvPopTitleType.setText(titleType);


    }

    /**
     * 根据类型设置该类型属性可见
     */
    protected void initVisibility() {
        switch (categoryId) {
            case TWO_TEE_START:
                rlPopTitle.setVisibility(View.VISIBLE);

                rlSegmentCourse.setVisibility(View.GONE);
                courseListView.setVisibility(View.GONE);
                rlCourseLine.setVisibility(View.GONE);
                rlCourseListLine.setVisibility(View.GONE);

                llTwoTeeStartTransfer.setVisibility(View.VISIBLE);
                llThreeTeeStartTransfer.setVisibility(View.GONE);

                rlStartTime.setVisibility(View.VISIBLE);
                rlEndTime.setVisibility(View.VISIBLE);

                rlSegmentAllReserved.setVisibility(View.GONE);
                rlSegmentMemberType.setVisibility(View.GONE);
                memberTypeListView.setVisibility(View.GONE);
                rlSegmentPrimeTime.setVisibility(View.GONE);

                rlOfBtn.setVisibility(View.VISIBLE);
                break;
            case NINE_HOLES_ONLY:
                rlPopTitle.setVisibility(View.VISIBLE);


                rlSegmentCourse.setVisibility(View.VISIBLE);

                courseListView.setVisibility(View.VISIBLE);

                rlCourseLine.setVisibility(View.VISIBLE);
                rlCourseListLine.setVisibility(View.VISIBLE);

                llTwoTeeStartTransfer.setVisibility(View.GONE);
                llThreeTeeStartTransfer.setVisibility(View.GONE);


                rlStartTime.setVisibility(View.VISIBLE);
                rlEndTime.setVisibility(View.VISIBLE);

                rlSegmentAllReserved.setVisibility(View.GONE);
                rlSegmentMemberType.setVisibility(View.GONE);
                memberTypeListView.setVisibility(View.GONE);

                rlSegmentPrimeTime.setVisibility(View.GONE);

                rlOfBtn.setVisibility(View.VISIBLE);
                break;
            case BLOCK_TIMES:
                rlPopTitle.setVisibility(View.VISIBLE);

                rlSegmentCourse.setVisibility(View.VISIBLE);
                courseListView.setVisibility(View.VISIBLE);
                rlCourseLine.setVisibility(View.VISIBLE);
                rlCourseListLine.setVisibility(View.VISIBLE);

                llTwoTeeStartTransfer.setVisibility(View.GONE);
                llThreeTeeStartTransfer.setVisibility(View.GONE);
                rlStartTime.setVisibility(View.VISIBLE);
                rlEndTime.setVisibility(View.VISIBLE);

                rlSegmentAllReserved.setVisibility(View.GONE);
                rlSegmentMemberType.setVisibility(View.GONE);
                memberTypeListView.setVisibility(View.GONE);
                rlSegmentPrimeTime.setVisibility(View.GONE);

                rlOfBtn.setVisibility(View.VISIBLE);
                break;
            case MEMBER_ONLY:
                rlPopTitle.setVisibility(View.VISIBLE);


                rlSegmentCourse.setVisibility(View.VISIBLE);
                courseListView.setVisibility(View.VISIBLE);
                rlCourseLine.setVisibility(View.VISIBLE);
                rlCourseListLine.setVisibility(View.VISIBLE);
                llTwoTeeStartTransfer.setVisibility(View.GONE);
                llThreeTeeStartTransfer.setVisibility(View.GONE);

                rlStartTime.setVisibility(View.VISIBLE);
                rlEndTime.setVisibility(View.VISIBLE);

                rlSegmentAllReserved.setVisibility(View.VISIBLE);
                rlSegmentMemberType.setVisibility(View.VISIBLE);
                memberTypeListView.setVisibility(View.VISIBLE);

                rlSegmentPrimeTime.setVisibility(View.GONE);

                rlOfBtn.setVisibility(View.VISIBLE);
                break;
            case THREE_TEE_START:
                rlPopTitle.setVisibility(View.VISIBLE);


                rlSegmentCourse.setVisibility(View.GONE);
                courseListView.setVisibility(View.GONE);
                rlCourseLine.setVisibility(View.GONE);
                rlCourseListLine.setVisibility(View.GONE);

                llTwoTeeStartTransfer.setVisibility(View.GONE);
                llThreeTeeStartTransfer.setVisibility(View.VISIBLE);
                rlStartTime.setVisibility(View.VISIBLE);
                rlEndTime.setVisibility(View.VISIBLE);

                rlSegmentAllReserved.setVisibility(View.GONE);
                rlSegmentMemberType.setVisibility(View.GONE);
                memberTypeListView.setVisibility(View.GONE);

                rlSegmentPrimeTime.setVisibility(View.GONE);

                rlOfBtn.setVisibility(View.VISIBLE);
                break;
            case PRIME_TIME:
                rlPopTitle.setVisibility(View.VISIBLE);


                rlSegmentCourse.setVisibility(View.VISIBLE);
                courseListView.setVisibility(View.VISIBLE);
                rlCourseLine.setVisibility(View.VISIBLE);
                rlCourseListLine.setVisibility(View.VISIBLE);

                llTwoTeeStartTransfer.setVisibility(View.GONE);
                llThreeTeeStartTransfer.setVisibility(View.GONE);
                rlStartTime.setVisibility(View.VISIBLE);
                rlEndTime.setVisibility(View.VISIBLE);

                rlSegmentAllReserved.setVisibility(View.GONE);
                rlSegmentMemberType.setVisibility(View.GONE);
                memberTypeListView.setVisibility(View.GONE);
                rlSegmentPrimeTime.setVisibility(View.VISIBLE);

                rlOfBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    protected void setListener() {
        //添加选择窗口范围监听可以优先获取触点，即不再执行onTouchEvent()函数，点击其他地方时执行onTouchEvent()函数销毁Activity
        popLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        //two tee start
        btnTwoTeeStartFirstCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(SegmentTimeEditOrDeletePopupWindow.this, courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnTwoTeeStartFirstTransfer.getTag() != null && btnTwoTeeStartFirstTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnTwoTeeStartSecondCourse.getTag() != null && btnTwoTeeStartSecondCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

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


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(SegmentTimeEditOrDeletePopupWindow.this, courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnTwoTeeStartFirstCourse.getTag() != null && btnTwoTeeStartFirstCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnTwoTeeStartSecondTransfer.getTag() != null && btnTwoTeeStartSecondTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

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


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(SegmentTimeEditOrDeletePopupWindow.this, courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnTwoTeeStartSecondTransfer.getTag() != null && btnTwoTeeStartSecondTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnTwoTeeStartFirstCourse.getTag() != null && btnTwoTeeStartFirstCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

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


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(SegmentTimeEditOrDeletePopupWindow.this, courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnTwoTeeStartSecondCourse.getTag() != null && btnTwoTeeStartSecondCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnTwoTeeStartFirstTransfer.getTag() != null && btnTwoTeeStartFirstTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

                        } else {
                            btnTwoTeeStartSecondTransfer.setTag(selectedId);
                            btnTwoTeeStartSecondTransfer.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }

                    }
                });
            }
        });


        //three tee start

        btnThreeTeeStartFirstCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(SegmentTimeEditOrDeletePopupWindow.this, courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartFirstTransfer.getTag() != null && btnThreeTeeStartFirstTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartSecondCourse.getTag() != null && btnThreeTeeStartSecondCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartThirdCourse.getTag() != null && btnThreeTeeStartThirdCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

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


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(SegmentTimeEditOrDeletePopupWindow.this, courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartFirstCourse.getTag() != null && btnThreeTeeStartFirstCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartSecondTransfer.getTag() != null && btnThreeTeeStartSecondTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartThirdTransfer.getTag() != null && btnThreeTeeStartThirdTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

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


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(SegmentTimeEditOrDeletePopupWindow.this, courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartSecondTransfer.getTag() != null && btnThreeTeeStartSecondTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartFirstCourse.getTag() != null && btnThreeTeeStartFirstCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartThirdCourse.getTag() != null && btnThreeTeeStartThirdCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

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


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(SegmentTimeEditOrDeletePopupWindow.this, courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartSecondCourse.getTag() != null && btnThreeTeeStartSecondCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartFirstTransfer.getTag() != null && btnThreeTeeStartFirstTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartThirdTransfer.getTag() != null && btnThreeTeeStartThirdTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

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


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(SegmentTimeEditOrDeletePopupWindow.this, courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartThirdTransfer.getTag() != null && btnThreeTeeStartThirdTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartFirstCourse.getTag() != null && btnThreeTeeStartFirstCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartSecondCourse.getTag() != null && btnThreeTeeStartSecondCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

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


                SegmentTimesCourseSelectAdapter segmentTimesCourseAdapter = new SegmentTimesCourseSelectAdapter(SegmentTimeEditOrDeletePopupWindow.this, courseAreaMapList);

                segmentTimesSelectCoursePopupWindow = new SegmentTimesSelectCoursePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, null);

                segmentTimesSelectCoursePopupWindow.transferListView.setAdapter(segmentTimesCourseAdapter);
                segmentTimesSelectCoursePopupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_courseWindow), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                segmentTimesSelectCoursePopupWindow.transferListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object selectedId = courseAreaMapList.get(i).get(COURSE_AREA_ID);

                        if (btnThreeTeeStartThirdCourse.getTag() != null && btnThreeTeeStartThirdCourse.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_cannot_choose_itself));

                        } else if (btnThreeTeeStartFirstTransfer.getTag() != null && btnThreeTeeStartFirstTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

                        } else if (btnThreeTeeStartSecondTransfer.getTag() != null && btnThreeTeeStartSecondTransfer.getTag().equals(selectedId)) {
                            segmentTimesSelectCoursePopupWindow.dismiss();
                            Utils.showShortToast(SegmentTimeEditOrDeletePopupWindow.this, getResources().getString(R.string.segment_times_area_has_been_chose));

                        } else {
                            btnThreeTeeStartThirdTransfer.setTag(selectedId);
                            btnThreeTeeStartThirdTransfer.setText((String) (courseAreaMapList.get(i).get(COURSE_AREA_NAME)));
                            segmentTimesSelectCoursePopupWindow.dismiss();
                        }

                    }
                });
            }
        });


        //start time setting
        tvPopStartTimeContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String str = tvPopStartTimeContent.getText().toString();
                String[] times = str.split(Constants.STR_COLON);
                popupWindow = new SelectTimePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, times, 1);

                popupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_window), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String formatTime = String.format(Constants.STRING_FORMAT_02D_02D, popupWindow.wheelViewHour.getCurrentItem(), popupWindow.wheelViewMin.getCurrentItem());
                        tvPopStartTimeContent.setText(formatTime);

                        popupWindow.dismiss();
                    }
                });
                popupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });
            }
        });


        //end time setting
        tvPopEndTimeContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String str = tvPopStartTimeContent.getText().toString();
                String[] times = str.split(Constants.STR_COLON);
                popupWindow = new SelectTimePopupWindow(SegmentTimeEditOrDeletePopupWindow.this, times, 1);

                popupWindow.showAtLocation(SegmentTimeEditOrDeletePopupWindow.this.findViewById(R.id.popup_window), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String formatTime = String.format(Constants.STRING_FORMAT_02D_02D, popupWindow.wheelViewHour.getCurrentItem(), popupWindow.wheelViewMin.getCurrentItem());
                        tvPopEndTimeContent.setText(formatTime);

                        popupWindow.dismiss();
                    }
                });
                popupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popupWindow.dismiss();
                    }
                });

            }
        });


        cbCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                courseCheckAdapter = new SegmentTimeCourseCheckAdapter(courseAreaMapList, cbCourse.isChecked());
                courseListView.setAdapter(courseCheckAdapter);
                LayoutUtils.setListViewHeightBasedOnChildrenWithTimes(courseListView, 2);


            }
        });


        cbMemberType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                memberTypeCheckAdapter = new SegmentTimeMemberTypeCheckAdapter(memberTypeListMapList, cbMemberType.isChecked());
                memberTypeListView.setAdapter(memberTypeCheckAdapter);
                LayoutUtils.setListViewHeightBasedOnChildrenWithTimes(memberTypeListView, 3);


            }
        });


        swAllReserved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editAllReserved = Constants.SWITCH_RIGHT;
                } else {
                    editAllReserved = Constants.SWITCH_LEFT;
                }
            }
        });


        swThreeToReserve.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    editThreeToReserve = Constants.SWITCH_RIGHT;
                } else {
                    editThreeToReserve = Constants.SWITCH_LEFT;
                }
            }
        });


        commitNoDoubleClickListener = new AppUtils.NoDoubleClickListener(this) {
            @Override
            public void noDoubleClick(View v) {


                editDate = titleTime.replace(Constants.STR_SLASH, Constants.STR_SEPARATOR);
                editStartTime = tvPopStartTimeContent.getText().toString();
                editEndTime = tvPopEndTimeContent.getText().toString();
                editThreeToReserve = Constants.SWITCH_LEFT;//Prime time 用
                editMember = StringUtils.EMPTY;//Member only 用
                editAllReserved = Constants.SWITCH_LEFT;//Member only 用


                if (categoryId == TWO_TEE_START && twoTeeStartBtnTagCheck() && timeCheck()) {
                    String twoTeeStartFirstCourseId = btnTwoTeeStartFirstCourse.getTag().toString();
                    String twoTeeStartFirstTransferId = btnTwoTeeStartFirstTransfer.getTag().toString();
                    String twoTeeStartSecondCourseId = btnTwoTeeStartSecondCourse.getTag().toString();
                    String twoTeeStartSecondTransferId = btnTwoTeeStartSecondTransfer.getTag().toString();

                    transferCourse = "[{\"from\":{\"id\":" + twoTeeStartFirstCourseId + "},\"to\":{\"id\":" + twoTeeStartFirstTransferId + "}},{\"from\":{\"id\":" + twoTeeStartSecondCourseId + "},\"to\":{\"id\":" + twoTeeStartSecondTransferId + "}}]";


                } else if (categoryId == NINE_HOLES_ONLY && courseAreaCheck() && timeCheck()) {
                    getTransferCourse();


                } else if (categoryId == BLOCK_TIMES && courseAreaCheck() && timeCheck()) {
                    getTransferCourse();


                } else if (categoryId == MEMBER_ONLY && courseAreaCheck() && timeCheck() && memberTypeCheck()) {

                    getTransferCourse();
                    getMember();

                    if (swAllReserved.isChecked()) {
                        editAllReserved = Constants.SWITCH_RIGHT;
                    } else {
                        editAllReserved = Constants.SWITCH_LEFT;
                    }


                } else if (categoryId == THREE_TEE_START && threeTeeStartBtnTagCheck() && timeCheck()) {
                    String threeTeeStartFirstCourseId = btnThreeTeeStartFirstCourse.getTag().toString();
                    String threeTeeStartFirstTransferId = btnThreeTeeStartFirstTransfer.getTag().toString();
                    String threeTeeStartSecondCourseId = btnThreeTeeStartSecondCourse.getTag().toString();
                    String threeTeeStartSecondTransferId = btnThreeTeeStartSecondTransfer.getTag().toString();
                    String threeTeeStartThirdCourseId = btnThreeTeeStartThirdCourse.getTag().toString();
                    String threeTeeStartThirdTransferId = btnThreeTeeStartThirdTransfer.getTag().toString();
                    transferCourse = "[{\"from\":{\"id\":" + threeTeeStartFirstCourseId + "},\"to\":{\"id\":" + threeTeeStartFirstTransferId
                            + "}},{\"from\":{\"id\":" + threeTeeStartSecondCourseId + "},\"to\":{\"id\":"
                            + threeTeeStartSecondTransferId + "}},{\"from\":{\"id\":"
                            + threeTeeStartThirdCourseId + "},\"to\":{\"id\":" + threeTeeStartThirdTransferId + "}}]";


                } else if (categoryId == PRIME_TIME && courseAreaCheck() && timeCheck()) {

                    getTransferCourse();

                    if (swThreeToReserve.isChecked()) {
                        editThreeToReserve = Constants.SWITCH_RIGHT;
                    } else {
                        editThreeToReserve = Constants.SWITCH_LEFT;
                    }


                }
                editSegmentTime();
            }
        };

        //edit commit
        btnPopCommit.setOnClickListener(commitNoDoubleClickListener);


        deleteNoDoubleClickListener = new AppUtils.NoDoubleClickListener(this) {
            @Override
            public void noDoubleClick(View v) {
                JsonLogin jl = (JsonLogin) Utils.readFromSP(SegmentTimeEditOrDeletePopupWindow.this, Constants.KEY_SP_LOGIN_INFO);
                Map<String, String> params = new HashMap<>();
                params.put(ApiKey.COMMON_TOKEN, jl.getToken());
                params.put(ApiKey.COURSE_ID, jl.getCourseId());
                if (Utils.isStringNotNullOrEmpty(multiSegmentIds)) {
                    params.put(ApiKey.SEGMENT_TIME_SEGMENT_ID, multiSegmentIds);
                } else {
                    params.put(ApiKey.SEGMENT_TIME_SEGMENT_ID, segmentId);
                }
                params.put(ApiKey.SEGMENT_TIME_DATE, Constants.STR_EMPTY);
                HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(SegmentTimeEditOrDeletePopupWindow.this) {
                    @Override
                    public void onJsonSuccess(BaseJsonObject jo) {
                        Integer returnCode = jo.getReturnCode();
                        String returnInfo = jo.getReturnInfo();
                        if (returnCode.equals(Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY)) {
                            Toast.makeText(SegmentTimeEditOrDeletePopupWindow.this, returnInfo, Toast.LENGTH_LONG).show();
                        }
                        //数据是使用Intent返回
                        Intent intent = new Intent();
                        //把返回数据存入Intent
                        intent.putExtra(TransKey.COMMON_RESULT, TransKey.COMMON_RESULT_DELETED);
                        intent.putExtra(TransKey.SEGMENT_TIME_GROUP_POSITION, groupPosition);
                        intent.putExtra(TransKey.SEGMENT_TIME_CHILD_POSITION, childPosition);
                        intent.putExtra(TransKey.SEGMENT_TIME_LAYOUT_INDEX, layoutIndex);
                        //设置返回数据
                        SegmentTimeEditOrDeletePopupWindow.this.setResult(RESULT_OK, intent);
                        finish();
                    }

                    @Override
                    public void onJsonError(VolleyError error) {

                    }
                };

                hh.start(SegmentTimeEditOrDeletePopupWindow.this, ApiManager.HttpApi.DelSegmentTime, params);
            }
        };

        //delete segment times
        btnPopDelete.setOnClickListener(deleteNoDoubleClickListener);
    }

    private boolean twoTeeStartBtnTagCheck() {
        if (btnTwoTeeStartFirstCourse.getTag() != null && btnTwoTeeStartFirstTransfer.getTag() != null && btnTwoTeeStartSecondCourse.getTag() != null && btnTwoTeeStartSecondTransfer.getTag() != null) {
            return true;
        } else {
            Utils.showShortToast(this, getResources().getString(R.string.segment_times_area_input));
            return false;
        }

    }

    private boolean memberTypeCheck() {

        checkedMemberTypeIdList = new ArrayList<>();
        checkedMemberTypeList = new ArrayList<>();

        checkedMemberTypeMapList = new ArrayList<>();


        for (int i = 0; i < memberTypeCheckedList.size(); i++) {
            if (memberTypeCheckedList.get(i)) {

                HashMap<String, Object> map = new HashMap<>();
                map.put(COURSE_AREA_ID, memberTypeListMapList.get(i).get(TYPE_ID));
                map.put(COURSE_AREA_NAME, memberTypeListMapList.get(i).get(MEMBER_TYPE));
                checkedMemberTypeMapList.add(map);


                checkedMemberTypeIdList.add((Integer) memberTypeListMapList.get(i).get(TYPE_ID));
                checkedMemberTypeList.add((String) memberTypeListMapList.get(i).get(MEMBER_TYPE));
            }
        }


        if (checkedMemberTypeIdList.size() > 0) {
            return true;
        } else {
            Utils.showShortToast(this, getResources().getString(R.string.segment_times_member_type_chose));
            return false;
        }

    }

    private void getTransferCourse() {


        if (checkedCourseIdList.size() == 1) {

            transferCourse = "[{\"from\":{\"id\":" + checkedCourseIdList.get(0) + "},\"to\":null}]";

        } else if (checkedCourseIdList.size() >= 2) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < checkedCourseIdList.size() - 1; i++) {
                sb.append("{\"from\":{\"id\":")
                        .append(checkedCourseIdList.get(i))
                        .append("},\"to\":null},");
            }
            sb.append("{\"from\":{\"id\":")
                    .append(checkedCourseIdList.get(checkedCourseIdList.size() - 1))
                    .append("},\"to\":null}]");

            transferCourse = sb.toString();
        }
    }

    private void getMember() {

        if (checkedMemberTypeIdList.size() == 1) {

            editMember = "[{\"type\":" + checkedMemberTypeIdList.get(0) + ",\"name\":\"" + checkedMemberTypeList.get(0) + "\"}]";

        } else if (checkedMemberTypeIdList.size() >= 2) {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < checkedMemberTypeIdList.size() - 1; i++) {
                sb.append("{\"type\":")
                        .append(checkedMemberTypeIdList.get(i))
                        .append(",\"name\":\"")
                        .append(checkedMemberTypeList.get(i))
                        .append("\"},");
            }
            sb.append("{\"type\":")
                    .append(checkedMemberTypeIdList.get(checkedMemberTypeIdList.size() - 1))
                    .append(",\"name\":\"")
                    .append(checkedMemberTypeList.get(checkedMemberTypeList.size() - 1))
                    .append("\"}]");

            editMember = sb.toString();
        }
    }

    private boolean threeTeeStartBtnTagCheck() {
        if (btnThreeTeeStartFirstCourse.getTag() != null && btnThreeTeeStartFirstTransfer.getTag() != null && btnThreeTeeStartSecondCourse.getTag() != null && btnThreeTeeStartSecondTransfer.getTag() != null && btnThreeTeeStartThirdCourse.getTag() != null && btnThreeTeeStartThirdTransfer
                .getTag() != null) {
            return true;
        } else {
            Utils.showShortToast(this, getResources().getString(R.string.segment_times_member_type_chose));
            return false;
        }

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
            Utils.showShortToast(this, getResources().getString(R.string.segment_times_area_chose));
            return false;
        }

    }

    private boolean timeCheck() {


        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMAT_HHMM, Locale.getDefault());

        if (!Utils.isStringNotNullOrEmpty(startTime)) {
            Utils.showShortToast(this, getResources().getString(R.string.segment_times_start_time_input));
            return false;
        } else if (!Utils.isStringNotNullOrEmpty(endTime)) {
            Utils.showShortToast(this, getResources().getString(R.string.segment_times_end_time_input));
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
                Utils.showShortToast(this, getResources().getString(R.string.segment_times_time_input));
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    private void initCourseCheckedStatus() {
        courseCheckedListInit = new ArrayList<>();
        courseAreaIdList = new ArrayList<>();
        areaIdList = new ArrayList<>();


        for (int i = 0; i < areaList.size(); i++) {
            areaIdList.add(areaList.get(i).getId());
        }

        for (int i = 0; i < courseAreaMapList.size(); i++) {
            courseAreaIdList.add((Integer) courseAreaMapList.get(i).get(COURSE_AREA_ID));
        }

        for (int i = 0; i < courseAreaIdList.size(); i++) {
            if (areaIdList.contains(courseAreaIdList.get(i))) {
                courseCheckedListInit.add(true);
            } else {
                courseCheckedListInit.add(false);
            }
        }


        courseCheckAdapter = new SegmentTimeCourseCheckAdapter(courseCheckedListInit);
        courseListView.setAdapter(courseCheckAdapter);
        LayoutUtils.setListViewHeightBasedOnChildrenWithTimes(courseListView, 2);
    }

    private void initMemberTypeCheckedStatus() {
        memberTypeCheckedListInit = new ArrayList<>();
        memberTypeListIdList = new ArrayList<>();
        memberTypeIdList = new ArrayList<>();


        for (int i = 0; i < memberTypeList.size(); i++) {
            memberTypeIdList.add(memberTypeList.get(i).getId());
        }

        for (int i = 0; i < memberTypeListMapList.size(); i++) {
            memberTypeListIdList.add((Integer) memberTypeListMapList.get(i).get(TYPE_ID));

        }

        for (int i = 0; i < memberTypeListIdList.size(); i++) {
            if (memberTypeIdList.contains(memberTypeListIdList.get(i))) {
                memberTypeCheckedListInit.add(true);
            } else {
                memberTypeCheckedListInit.add(false);
            }
        }


        memberTypeCheckAdapter = new SegmentTimeMemberTypeCheckAdapter(memberTypeCheckedListInit);
        memberTypeListView.setAdapter(memberTypeCheckAdapter);
        LayoutUtils.setListViewHeightBasedOnChildrenWithTimes(memberTypeListView, 3);
    }

    private void editSegmentTime() {
        JsonLogin jl = (JsonLogin) Utils.readFromSP(SegmentTimeEditOrDeletePopupWindow.this, Constants.KEY_SP_LOGIN_INFO);
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, jl.getToken());
        if (Utils.isStringNotNullOrEmpty(multiSegmentIds)) {
            params.put(ApiKey.SEGMENT_TIME_SEGMENT_ID, multiSegmentIds);
        } else {
            params.put(ApiKey.SEGMENT_TIME_SEGMENT_ID, segmentId);
        }
        params.put(ApiKey.SEGMENT_TIME_DATE, editDate);
        params.put(ApiKey.TEE_TIME_CATEGORY_ID, String.valueOf(categoryId));
        params.put(ApiKey.SEGMENT_TIME_START_TIME, editStartTime + Constants.TIME_ADD_SS);
        params.put(ApiKey.SEGMENT_TIME_END_TIME, editEndTime + Constants.TIME_ADD_SS);
        params.put(ApiKey.SEGMENT_TIME_TRANSFER_COURSE, transferCourse);
        params.put(ApiKey.SEGMENT_TIME_MEMBER, editMember);
        params.put(ApiKey.SEGMENT_TIME_THREE_TO_RESERVE, String.valueOf(editThreeToReserve));
        params.put(ApiKey.SEGMENT_TIME_ALL_RESERVE_MEMBER, String.valueOf(editAllReserved));
        Log.i("--edit segment times", params.toString());
        HttpManager<JsonDoEditSegmentTime> hh = new HttpManager<JsonDoEditSegmentTime>(this) {

            @Override
            public void onJsonSuccess(JsonDoEditSegmentTime jo) {

                String returnInfo = jo.getReturnInfo();
                Toast.makeText(SegmentTimeEditOrDeletePopupWindow.this, returnInfo, Toast.LENGTH_LONG).show();

                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                intent.putExtra("result", "edited");
                intent.putExtra(TransKey.SEGMENT_TIME_LAYOUT_INDEX, layoutIndex);
                intent.putExtra(TransKey.SEGMENT_TIME_TRANSFER_COURSE, transferCourse);
                intent.putExtra(TransKey.SEGMENT_TIME_START_TIME, startTime);
                intent.putExtra(TransKey.SEGMENT_TIME_END_TIME, endTime);
                intent.putExtra(TransKey.SEGMENT_TIME_EDIT_ID, segmentId);

                //设置返回数据
                SegmentTimeEditOrDeletePopupWindow.this.setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };

        hh.start(SegmentTimeEditOrDeletePopupWindow.this, ApiManager.HttpApi.EditSegmentTime, params);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //数据是使用Intent返回
        Intent intent = new Intent();
        //把返回数据存入Intent
        intent.putExtra("result", "nothing");
        //设置返回数据
        SegmentTimeEditOrDeletePopupWindow.this.setResult(RESULT_OK, intent);

        finish();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, @NonNull KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //数据是使用Intent返回
            Intent intent = new Intent();
            //把返回数据存入Intent
            intent.putExtra("result", "nothing");
            //设置返回数据
            SegmentTimeEditOrDeletePopupWindow.this.setResult(RESULT_OK, intent);

            finish();
            return true;
        } else {
            return false;
        }
    }

    public class SegmentTimeCourseCheckAdapter extends BaseAdapter {

        private int listSize;


        public SegmentTimeCourseCheckAdapter(ArrayList<Boolean> isCheckList) {
            this.listSize = isCheckList.size();
            courseCheckedList = new ArrayList<>(listSize);
            for (int i = 0; i < isCheckList.size(); i++) {
                courseCheckedList.add(isCheckList.get(i));
            }
        }

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
                        .from(SegmentTimeEditOrDeletePopupWindow.this);
                convertView = inflater.inflate(R.layout.item_of_segment_times_course_check, null);
                holder = new ViewHolder();
                holder.content = (IteeTextView) convertView
                        .findViewById(R.id.tv_course_area_name);
                holder.checkBox = (CheckBox) convertView
                        .findViewById(R.id.cb_course_area_name);

                holder.checkBox.setOnCheckedChangeListener(courseCheckedChangeListener);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.content.setText((String) courseAreaMapList.get(position).get(COURSE_AREA_NAME));
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


        public SegmentTimeMemberTypeCheckAdapter(ArrayList<Boolean> isCheckList) {
            this.listSize = isCheckList.size();
            memberTypeCheckedList = new ArrayList<>(listSize);
            for (int i = 0; i < isCheckList.size(); i++) {
                memberTypeCheckedList.add(isCheckList.get(i));
            }
        }

        public SegmentTimeMemberTypeCheckAdapter(ArrayList<HashMap<String, Object>> arrayList, boolean isCheck) {
            this.listSize = arrayList.size();
            memberTypeCheckedList = new ArrayList<>(listSize);
            for (int i = 0; i < arrayList.size(); i++) {
                memberTypeCheckedList.add(isCheck);
            }
        }

        @Override
        public int getCount() {
            return memberTypeListMapList.size();
        }

        @Override
        public Object getItem(int position) {
            return memberTypeListMapList.get(position);
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
                        .from(SegmentTimeEditOrDeletePopupWindow.this);
                convertView = inflater.inflate(R.layout.item_of_segment_times_member_type_check, null);
                holder = new ViewHolder();
                holder.content = (IteeTextView) convertView
                        .findViewById(R.id.tv_member_type);
                holder.checkBox = (CheckBox) convertView
                        .findViewById(R.id.cb_member_type);

                holder.checkBox.setOnCheckedChangeListener(memberTypeCheckedChangeListener);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.content.setText((String) memberTypeListMapList.get(position).get(MEMBER_TYPE));
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

