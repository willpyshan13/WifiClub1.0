package cn.situne.itee.adapter;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.SegmentTimesDetailFragment;
import cn.situne.itee.manager.jsonentity.JsonDoEditSegmentTime;
import cn.situne.itee.view.IteeTextView;

public class SegmentTimeSettingAdapter extends BaseExpandableListAdapter {

    public static final int TWO_TEE_START_ID = 1;
    public static final int NINE_HOLES_ONLY_ID = 2;
    public static final int BLOCK_TIMES_ID = 3;
    public static final int MEMBER_ONLY_ID = 4;
    public static final int THREE_TEE_START_ID = 5;
    public static final int PRIME_TIME_ID = 6;

    JsonDoEditSegmentTime.DataList data;
    private BaseFragment baseFragment;
    View.OnClickListener deleteDayListener;

    public SegmentTimeSettingAdapter(BaseFragment context, JsonDoEditSegmentTime.DataList data,
                                     View.OnClickListener deleteDayListener) {
        this.data = data;
        this.baseFragment = context;
        this.deleteDayListener = deleteDayListener;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.getSegmentTimesList().get(groupPosition).getSegmentTimesList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildViewHolder childViewHolder;
        if (convertView == null) {
            childViewHolder = new ChildViewHolder();
            childViewHolder.ll = new LinearLayout(baseFragment.getActivity());
            childViewHolder.ll.setPadding(0, 0, 0, 0);
            childViewHolder.ll.setBackgroundColor(baseFragment.getColor(R.color.segment_time_item));
            convertView = childViewHolder.ll;
            childViewHolder.rlForChild = new RelativeLayout(baseFragment.getActivity());

            childViewHolder.tvTime = new IteeTextView(baseFragment);
            childViewHolder.tvContent = new IteeTextView(baseFragment);
            childViewHolder.tvType = new IteeTextView(baseFragment);
            childViewHolder.ivRightIcon = new ImageView(baseFragment.getActivity());
            childViewHolder.ivLine = new View(baseFragment.getActivity());


            childViewHolder.tvTime.setId(View.generateViewId());
            childViewHolder.tvTime.setTextColor(baseFragment.getColor(R.color.common_black));
            childViewHolder.tvTime.setTextSize(Constants.FONT_SIZE_SMALLER);

            childViewHolder.tvContent.setId(View.generateViewId());
            childViewHolder.tvContent.setTextColor(baseFragment.getColor(R.color.common_blue));
            childViewHolder.tvContent.setSingleLine();
            childViewHolder.tvContent.setMaxEms(14);
            childViewHolder.tvContent.setEllipsize(TextUtils.TruncateAt.END);
            childViewHolder.tvContent.setTextSize(Constants.FONT_SIZE_SMALLER);

            childViewHolder.tvType.setId(View.generateViewId());
            childViewHolder.tvType.setTextColor(baseFragment.getColor(R.color.common_black));
            childViewHolder.tvType.setGravity(Gravity.CENTER_VERTICAL);

            childViewHolder.ivRightIcon.setId(View.generateViewId());
            childViewHolder.ivRightIcon.setImageResource(R.drawable.icon_right_arrow);

            childViewHolder.ivLine.setId(View.generateViewId());
            childViewHolder.ivLine.setBackgroundColor(baseFragment.getColor(R.color.common_separator_gray));

            childViewHolder.ll.addView(childViewHolder.rlForChild);
            LinearLayout.LayoutParams paramsRlForChild = (LinearLayout.LayoutParams) childViewHolder.rlForChild.getLayoutParams();
            paramsRlForChild.width = LinearLayout.LayoutParams.MATCH_PARENT;
            paramsRlForChild.height = baseFragment.getActualHeightOnThisDevice(80);
            paramsRlForChild.gravity = RelativeLayout.CENTER_VERTICAL;
            childViewHolder.rlForChild.setLayoutParams(paramsRlForChild);

            childViewHolder.rlForChild.addView(childViewHolder.ivRightIcon);
            RelativeLayout.LayoutParams paramsIvRightIcon = (RelativeLayout.LayoutParams) childViewHolder.ivRightIcon.getLayoutParams();
            paramsIvRightIcon.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsIvRightIcon.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            DensityUtil.getActualWidthOnThisDevice(40, baseFragment.getActivity());
            paramsIvRightIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsIvRightIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            childViewHolder.ivRightIcon.setLayoutParams(paramsIvRightIcon);

            childViewHolder.rlForChild.addView(childViewHolder.tvType);
            RelativeLayout.LayoutParams paramsTvType = (RelativeLayout.LayoutParams) childViewHolder.tvType.getLayoutParams();
            paramsTvType.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvType.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            paramsTvType.addRule(RelativeLayout.LEFT_OF, childViewHolder.ivRightIcon.getId());
            paramsTvType.setMargins(0, 0, 10, 0);
            childViewHolder.tvType.setLayoutParams(paramsTvType);

            childViewHolder.rlForChild.addView(childViewHolder.tvTime);
            RelativeLayout.LayoutParams paramsTvTime = (RelativeLayout.LayoutParams) childViewHolder.tvTime.getLayoutParams();
            paramsTvTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvTime.height = baseFragment.getActualHeightOnThisDevice(40);
            paramsTvTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            paramsTvTime.setMargins(20, 0, 0, 0);
            childViewHolder.tvTime.setLayoutParams(paramsTvTime);

            childViewHolder.rlForChild.addView(childViewHolder.tvContent);
            RelativeLayout.LayoutParams paramsTvContent = (RelativeLayout.LayoutParams) childViewHolder.tvContent.getLayoutParams();
            paramsTvContent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvContent.height = baseFragment.getActualHeightOnThisDevice(40);
            paramsTvContent.addRule(RelativeLayout.BELOW, childViewHolder.tvTime.getId());
            paramsTvContent.setMargins(20, 0, 0, 0);
            childViewHolder.tvContent.setLayoutParams(paramsTvContent);

            childViewHolder.rlForChild.addView(childViewHolder.ivLine);
            RelativeLayout.LayoutParams paramsIvLine = (RelativeLayout.LayoutParams) childViewHolder.ivLine.getLayoutParams();
            paramsIvLine.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            paramsIvLine.height = 1;
            paramsIvLine.addRule(RelativeLayout.BELOW, childViewHolder.tvContent.getId());
            paramsIvLine.setMargins(0, 0, 0, 0);
            childViewHolder.ivLine.setLayoutParams(paramsIvLine);

            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }

        childViewHolder.tvTime.setTextSize(Constants.FONT_SIZE_SMALLER);
        childViewHolder.tvContent.setTextSize(Constants.FONT_SIZE_SMALLER);
        childViewHolder.tvType.setGravity(Gravity.CENTER_VERTICAL);

        JsonDoEditSegmentTime.SegmentTime segmentTime
                = data.getSegmentTimesList().get(groupPosition).getSegmentTimesList().get(childPosition);

        String transferStartTime = segmentTime.getTransferStartTime().substring(0, 5);
        String transferEndTime = segmentTime.getTransferEndTime().substring(0, 5);
        childViewHolder.tvTime.setText(transferStartTime + " -- " + transferEndTime);

        String content = segmentTime.getInfo();

        int categoryId = segmentTime.getCategoryId();

        String categoryName = StringUtils.EMPTY;

        switch (categoryId) {
            case TWO_TEE_START_ID:
                categoryName = baseFragment.getString(R.string.segment_times_two_tee_start);
                break;
            case NINE_HOLES_ONLY_ID:
                categoryName = baseFragment.getString(R.string.segment_times_nine_holes_only);
                break;
            case BLOCK_TIMES_ID:
                categoryName = baseFragment.getString(R.string.segment_times_block_times);
                break;
            case MEMBER_ONLY_ID:
                categoryName = baseFragment.getString(R.string.segment_times_member_only);
                break;
            case THREE_TEE_START_ID:
                categoryName = baseFragment.getString(R.string.segment_times_three_tee_start);
                break;
            case PRIME_TIME_ID:
                categoryName = baseFragment.getString(R.string.segment_times_prime_time);
                break;
            default:
                break;
        }

        childViewHolder.tvContent.setText(content);

        childViewHolder.tvType.setText(categoryName);

        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return data.getSegmentTimesList().get(groupPosition).getSegmentTimesList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.getSegmentTimesList().get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return data.getSegmentTimesList().size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(baseFragment.getActivity()).inflate(R.layout.item_of_segment_list, null);
            holder.rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_item);
            holder.rlDelete = (RelativeLayout) convertView.findViewById(R.id.rl_delete);

            holder.ivArrow = new ImageView(baseFragment.getActivity());
            holder.textView = new IteeTextView(baseFragment);
            holder.ivIcon = new ImageView(baseFragment.getActivity());
            holder.textViewDelete = new IteeTextView(baseFragment);
            holder.ivArrow.setId(View.generateViewId());
            holder.textView.setId(View.generateViewId());
            holder.textView.setTextColor(baseFragment.getColor(R.color.common_black));
            holder.ivIcon.setId(View.generateViewId());
            holder.textViewDelete.setId(View.generateViewId());
            holder.ivIcon.setImageResource(R.drawable.segment_child_icon);
            holder.textViewDelete.setText(baseFragment.getString(R.string.segment_times_delete));
            holder.textViewDelete.setTextColor(baseFragment.getColor(R.color.common_white));

            holder.rlItem.addView(holder.ivArrow);
            RelativeLayout.LayoutParams paramsIvArrow = (RelativeLayout.LayoutParams) holder.ivArrow.getLayoutParams();
            paramsIvArrow.width = baseFragment.getActualWidthOnThisDevice(40);
            paramsIvArrow.height = baseFragment.getActualHeightOnThisDevice(40);
            paramsIvArrow.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            //paramsIvArrow.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsIvArrow.setMargins(10, 20, 0, 20);
            holder.ivArrow.setLayoutParams(paramsIvArrow);

            holder.rlItem.addView(holder.textView);
            RelativeLayout.LayoutParams paramsTextView = (RelativeLayout.LayoutParams) holder.textView.getLayoutParams();
            paramsTextView.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTextView.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTextView.addRule(RelativeLayout.RIGHT_OF, holder.ivArrow.getId());
            //paramsTextView.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsTextView.setMargins(10, 20, 0, 20);
            holder.textView.setLayoutParams(paramsTextView);

            holder.rlItem.addView(holder.ivIcon);
            RelativeLayout.LayoutParams paramsIvIcon = (RelativeLayout.LayoutParams) holder.ivIcon.getLayoutParams();
            paramsIvIcon.width = baseFragment.getActualWidthOnThisDevice(40);
            paramsIvIcon.height = baseFragment.getActualHeightOnThisDevice(40);
            paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            //paramsIvIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsIvIcon.setMargins(0, 20, 20, 20);
            holder.ivIcon.setLayoutParams(paramsIvIcon);

            holder.rlDelete.addView(holder.textViewDelete);
            RelativeLayout.LayoutParams paramsTextViewDelete = (RelativeLayout.LayoutParams) holder.textViewDelete.getLayoutParams();
            paramsTextViewDelete.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTextViewDelete.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTextViewDelete.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsTextViewDelete.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            holder.textViewDelete.setLayoutParams(paramsTextViewDelete);
            convertView.setTag(holder);

        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        if (data.getSegmentTimesList().get(groupPosition).getSegmentTimesList().size() > 0) {
            holder.ivIcon.setVisibility(View.VISIBLE);
        } else {
            holder.ivIcon.setVisibility(View.INVISIBLE);
        }

        holder.textViewDelete.setTag(groupPosition);
        holder.textViewDelete.setOnClickListener(deleteDayListener);

        holder.ivIcon.setOnClickListener(new AppUtils.NoDoubleClickListener(baseFragment.getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Bundle bundle = new Bundle();
                String dateString = data.getSegmentTimesList().get(groupPosition).getDate();
                SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_YYYYMMDD_OBLIQUE, Locale.getDefault());
                try {
                    Date date = sdf.parse(dateString);
                    bundle.putSerializable("date", date);
                    baseFragment.push(SegmentTimesDetailFragment.class, bundle);
                } catch (ParseException e) {
                    Utils.log(e.getMessage());
                }
            }
        });

        if (isExpanded) {
            holder.ivArrow.setImageResource(R.drawable.segment_icon_bottom_arrow);
        } else {
            holder.ivArrow.setImageResource(R.drawable.segment_icon_left_arrow);
        }

        String date = data.getSegmentTimesList().get(groupPosition).getDate();
        holder.textView.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(date, baseFragment.getActivity()));

        return convertView;

    }

    class GroupViewHolder {

        RelativeLayout rlItem;
        RelativeLayout rlDelete;

        ImageView ivArrow;
        IteeTextView textView;
        ImageView ivIcon;
        IteeTextView textViewDelete;
    }

    class ChildViewHolder {

        LinearLayout ll;
        RelativeLayout rlForChild;
        IteeTextView tvTime;
        IteeTextView tvContent;
        IteeTextView tvType;
        ImageView ivRightIcon;
        View ivLine;
    }

    @Override
    public boolean hasStableIds() {

        return true;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}