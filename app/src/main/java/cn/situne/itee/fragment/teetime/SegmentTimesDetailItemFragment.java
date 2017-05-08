/**
 * Project Name: itee
 * File Name:	 SegmentTimesDetailItemFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-03-31
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.fragment.teetime;

import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.HashMap;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonDetailSegmentTime;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:SegmentTimesDetailItemFragment <br/>
 * Function: Segment time detail. <br/>
 * Date: 2015-03-31 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class SegmentTimesDetailItemFragment extends BaseFragment {

    private ListView lvSegmentDetails;
    private SegmentTimeDetailAdapter adapter;

    private JsonDetailSegmentTime.DetailSegmentCourse leftCourse;
    private JsonDetailSegmentTime.DetailSegmentCourse middleCourse;
    private JsonDetailSegmentTime.DetailSegmentCourse rightCourse;

    private LinearLayout llCourseNameContainer;

    private IteeTextView tvCourseNameLeft;
    private IteeTextView tvCourseNameMiddle;
    private IteeTextView tvCourseNameRight;

  //  private int currentPage;

    private HashMap<String, Integer> colorMap;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_segment_time_detail_item;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        lvSegmentDetails = (ListView) rootView.findViewById(R.id.lv_segment_details);
        adapter = new SegmentTimeDetailAdapter(this);

        llCourseNameContainer = (LinearLayout) rootView.findViewById(R.id.ll_course_name_container);

        tvCourseNameLeft = (IteeTextView) rootView.findViewById(R.id.tv_course_area_name_left);
        tvCourseNameMiddle = (IteeTextView) rootView.findViewById(R.id.tv_course_area_name_middle);
        tvCourseNameRight = (IteeTextView) rootView.findViewById(R.id.tv_course_area_name_right);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(MATCH_PARENT, getActualHeightOnThisDevice(80));
        llCourseNameContainer.setLayoutParams(layoutParams);
    }

    @Override
    protected void setPropertyOfControls() {
        lvSegmentDetails.setDivider(null);
        lvSegmentDetails.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        if (leftCourse == null) {
            tvCourseNameLeft.setVisibility(View.GONE);
        } else {
            int colorId = colorMap.get(leftCourse.getCourseId());
            tvCourseNameLeft.setBackgroundColor(getColor(colorId));
            tvCourseNameLeft.setText(leftCourse.getCourseName());
            tvCourseNameLeft.setGravity(Gravity.CENTER);
        }
        if (middleCourse == null) {
            tvCourseNameMiddle.setVisibility(View.GONE);
        } else {
            int colorId = colorMap.get(middleCourse.getCourseId());
            tvCourseNameMiddle.setBackgroundColor(getColor(colorId));
            tvCourseNameMiddle.setText(middleCourse.getCourseName());
            tvCourseNameMiddle.setGravity(Gravity.CENTER);
        }
        if (rightCourse == null) {
            tvCourseNameRight.setVisibility(View.GONE);
        } else {
            int colorId = colorMap.get(rightCourse.getCourseId());
            tvCourseNameRight.setBackgroundColor(getColor(colorId));
            tvCourseNameRight.setText(rightCourse.getCourseName());
            tvCourseNameRight.setGravity(Gravity.CENTER);
        }
    }

    @Override
    protected void configActionBar() {

    }

    class SegmentTimeDetailAdapter extends BaseAdapter {

        private BaseFragment mBaseFragment;

        public SegmentTimeDetailAdapter(BaseFragment mBaseFragment) {
            this.mBaseFragment = mBaseFragment;
        }

        @Override
        public int getCount() {
            if (leftCourse != null) {
                return leftCourse.getTimes().size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder vh;
            if (convertView == null) {

                convertView = LayoutInflater.from(mBaseFragment.getActivity()).inflate(R.layout.list_row_segment_time_detail, parent, false);
                vh = new ViewHolder();

                vh.rlLeft = (RelativeLayout) convertView.findViewById(R.id.rl_left);
                vh.rlMiddle = (RelativeLayout) convertView.findViewById(R.id.rl_middle);
                vh.rlRight = (RelativeLayout) convertView.findViewById(R.id.rl_right);

                vh.tvLeftTime = (IteeTextView) convertView.findViewById(R.id.tv_left_time);
                vh.tvMiddleTime = (IteeTextView) convertView.findViewById(R.id.tv_middle_time);
                vh.tvRightTime = (IteeTextView) convertView.findViewById(R.id.tv_right_time);

                vh.llLeftContainer = (LinearLayout) convertView.findViewById(R.id.ll_left_container);
                vh.llMiddleContainer = (LinearLayout) convertView.findViewById(R.id.ll_middle_container);
                vh.llRightContainer = (LinearLayout) convertView.findViewById(R.id.ll_right_container);

                vh.ivSeparatorLeft = (ImageView) convertView.findViewById(R.id.iv_separator_left);
                vh.ivSeparatorMiddle = (ImageView) convertView.findViewById(R.id.iv_separator_middle);
                vh.ivSeparatorRight = (ImageView) convertView.findViewById(R.id.iv_separator_right);

                vh.iv9HoleOnlyLeft = (ImageView) convertView.findViewById(R.id.iv_9_hole_only_left);
                vh.ivBlockTimeLeft = (ImageView) convertView.findViewById(R.id.iv_block_time_left);
                vh.ivMemberOnlyLeft = (ImageView) convertView.findViewById(R.id.iv_member_only_left);
                vh.ivPrimeTimeLeft = (ImageView) convertView.findViewById(R.id.iv_prime_time_left);

                vh.iv9HoleOnlyMiddle = (ImageView) convertView.findViewById(R.id.iv_9_hole_only_middle);
                vh.ivBlockTimeMiddle = (ImageView) convertView.findViewById(R.id.iv_block_time_middle);
                vh.ivMemberOnlyMiddle = (ImageView) convertView.findViewById(R.id.iv_member_only_middle);
                vh.ivPrimeTimeMiddle = (ImageView) convertView.findViewById(R.id.iv_prime_time_middle);

                vh.iv9HoleOnlyRight = (ImageView) convertView.findViewById(R.id.iv_9_hole_only_right);
                vh.ivBlockTimeRight = (ImageView) convertView.findViewById(R.id.iv_block_time_right);
                vh.ivMemberOnlyRight = (ImageView) convertView.findViewById(R.id.iv_member_only_right);
                vh.ivPrimeTimeRight = (ImageView) convertView.findViewById(R.id.iv_prime_time_right);

                LinearLayout.LayoutParams ivLayoutParams = new LinearLayout.LayoutParams(getActualWidthOnThisDevice(20), getActualWidthOnThisDevice(20));

                vh.iv9HoleOnlyLeft.setLayoutParams(ivLayoutParams);
                vh.ivBlockTimeLeft.setLayoutParams(ivLayoutParams);
                vh.ivMemberOnlyLeft.setLayoutParams(ivLayoutParams);
                vh.ivPrimeTimeLeft.setLayoutParams(ivLayoutParams);

                vh.iv9HoleOnlyMiddle.setLayoutParams(ivLayoutParams);
                vh.ivBlockTimeMiddle.setLayoutParams(ivLayoutParams);
                vh.ivMemberOnlyMiddle.setLayoutParams(ivLayoutParams);
                vh.ivPrimeTimeMiddle.setLayoutParams(ivLayoutParams);

                vh.iv9HoleOnlyRight.setLayoutParams(ivLayoutParams);
                vh.ivBlockTimeRight.setLayoutParams(ivLayoutParams);
                vh.ivMemberOnlyRight.setLayoutParams(ivLayoutParams);
                vh.ivPrimeTimeRight.setLayoutParams(ivLayoutParams);

                convertView.setTag(vh);
            } else {
                vh = (ViewHolder) convertView.getTag();
            }

            if (leftCourse == null) {
                vh.rlLeft.setVisibility(View.GONE);
            } else {
                configCell(leftCourse, vh, position);
            }
            if (middleCourse == null) {
                vh.rlMiddle.setVisibility(View.GONE);
            } else {
                configCell(middleCourse, vh, position);
            }
            if (rightCourse == null) {
                vh.rlRight.setVisibility(View.GONE);
            } else {
                configCell(rightCourse, vh, position);
            }
            return convertView;
        }

        private void configCell
                (JsonDetailSegmentTime.DetailSegmentCourse course, ViewHolder viewHolder, int position) {

            RelativeLayout relativeLayout;
            IteeTextView textView;
            LinearLayout llContainer;
            ImageView ivSeparator;

            ImageView iv9HoleOnly;
            ImageView ivBlockTime;
            ImageView ivMemberOnly;
            ImageView ivPrimeTime;

            if (course == leftCourse) {
                relativeLayout = viewHolder.rlLeft;
                textView = viewHolder.tvLeftTime;
                llContainer = viewHolder.llLeftContainer;
                ivSeparator = viewHolder.ivSeparatorLeft;
                iv9HoleOnly = viewHolder.iv9HoleOnlyLeft;
                ivBlockTime = viewHolder.ivBlockTimeLeft;
                ivMemberOnly = viewHolder.ivMemberOnlyLeft;
                ivPrimeTime = viewHolder.ivPrimeTimeLeft;
            } else if (course == middleCourse) {
                relativeLayout = viewHolder.rlMiddle;
                textView = viewHolder.tvMiddleTime;
                llContainer = viewHolder.llMiddleContainer;
                ivSeparator = viewHolder.ivSeparatorMiddle;
                iv9HoleOnly = viewHolder.iv9HoleOnlyMiddle;
                ivBlockTime = viewHolder.ivBlockTimeMiddle;
                ivMemberOnly = viewHolder.ivMemberOnlyMiddle;
                ivPrimeTime = viewHolder.ivPrimeTimeMiddle;
            } else {
                relativeLayout = viewHolder.rlRight;
                textView = viewHolder.tvRightTime;
                llContainer = viewHolder.llRightContainer;
                ivSeparator = viewHolder.ivSeparatorRight;
                iv9HoleOnly = viewHolder.iv9HoleOnlyRight;
                ivBlockTime = viewHolder.ivBlockTimeRight;
                ivMemberOnly = viewHolder.ivMemberOnlyRight;
                ivPrimeTime = viewHolder.ivPrimeTimeRight;
            }

            JsonDetailSegmentTime.DetailSegmentCourse.SegmentDetailTime segmentDetailTime = course.getTimes().get(position);
            textView.setText(removeSecondsString(segmentDetailTime.getTime()));

            llContainer.setVisibility(View.VISIBLE);
            ivSeparator.setVisibility(View.GONE);
            relativeLayout.setBackgroundColor(Color.WHITE);

            ivMemberOnly.setVisibility(View.GONE);
            iv9HoleOnly.setVisibility(View.GONE);
            ivBlockTime.setVisibility(View.GONE);
            ivPrimeTime.setVisibility(View.GONE);

            if (segmentDetailTime.isMemberOnly()) {
                ivMemberOnly.setVisibility(View.VISIBLE);
            }
            if (segmentDetailTime.is9HoleOnly()) {
                iv9HoleOnly.setVisibility(View.VISIBLE);
            }
            if (segmentDetailTime.isBlockTime()) {
                ivBlockTime.setVisibility(View.VISIBLE);
            }
            if (segmentDetailTime.isPrimeTime()) {
                ivPrimeTime.setVisibility(View.VISIBLE);
            }
            String courseId = segmentDetailTime.getCourseId();
            if (Utils.isStringNotNullOrEmpty(courseId)) {
                if (colorMap.containsKey(courseId)) {
                    int colorId = colorMap.get(courseId);
                    relativeLayout.setBackgroundColor(getColor(colorId));
                }
            }
            String transferId = segmentDetailTime.getTransferId();
            if (Utils.isStringNotNullOrEmpty(transferId)) {
                if (colorMap.containsKey(transferId)) {
                    int colorId = colorMap.get(transferId);
                    relativeLayout.setBackgroundColor(getColor(colorId));
                }
            }
        }

        private String removeSecondsString(String timeString) {
            if (timeString != null && timeString.length() == 8) {
                return timeString.substring(0, timeString.length() - 3);
            }
            return Constants.STR_EMPTY;
        }

        class ViewHolder {

            RelativeLayout rlLeft;
            RelativeLayout rlMiddle;
            RelativeLayout rlRight;

            IteeTextView tvLeftTime;
            IteeTextView tvMiddleTime;
            IteeTextView tvRightTime;

            LinearLayout llLeftContainer;
            LinearLayout llMiddleContainer;
            LinearLayout llRightContainer;

            ImageView ivSeparatorLeft;
            ImageView ivSeparatorMiddle;
            ImageView ivSeparatorRight;

            ImageView iv9HoleOnlyLeft;
            ImageView ivBlockTimeLeft;
            ImageView ivMemberOnlyLeft;
            ImageView ivPrimeTimeLeft;

            ImageView iv9HoleOnlyMiddle;
            ImageView ivBlockTimeMiddle;
            ImageView ivMemberOnlyMiddle;
            ImageView ivPrimeTimeMiddle;

            ImageView iv9HoleOnlyRight;
            ImageView ivBlockTimeRight;
            ImageView ivMemberOnlyRight;
            ImageView ivPrimeTimeRight;
        }
    }

    public void setLeftCourse(JsonDetailSegmentTime.DetailSegmentCourse leftCourse) {
        this.leftCourse = leftCourse;
    }

    public void setMiddleCourse(JsonDetailSegmentTime.DetailSegmentCourse middleCourse) {
        this.middleCourse = middleCourse;
    }

    public void setRightCourse(JsonDetailSegmentTime.DetailSegmentCourse rightCourse) {
        this.rightCourse = rightCourse;
    }

//    public void setCurrentPage(int currentPage) {
//        this.currentPage = currentPage;
//    }

    public void setColorMap(HashMap<String, Integer> colorMap) {
        this.colorMap = colorMap;
    }
}