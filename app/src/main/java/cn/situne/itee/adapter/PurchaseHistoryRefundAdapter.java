package cn.situne.itee.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RelativeLayout;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.PurchaseHistoryExpandable;
import cn.situne.itee.manager.jsonentity.JsonPurchaseHistoryDetailRecord;
import cn.situne.itee.view.IteeTextView;

public class PurchaseHistoryRefundAdapter extends BaseExpandableListAdapter {

    public static final int TYPE_GOOD = 0;
    public static final int TYPE_AA = 1;
    public static final int TYPE_PACKAGE = 2;
    public static final int TYPE_REFUND = 3;
    List<PurchaseHistoryExpandable> data;
    Context context;
    private String currency;

    public PurchaseHistoryRefundAdapter(Context context, List<PurchaseHistoryExpandable> data) {

        this.data = data;
        this.context = context;
        currency = AppUtils.getCurrentCurrency(context);

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return data.get(groupPosition).getGoodList().get(childPosition);

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        ChildViewHolder holder;
        if (convertView == null) {

            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_of_purchase_history_child, null);
            holder.textViewName = (IteeTextView) convertView.findViewById(R.id.tv_name);
            holder.textViewPrice = (IteeTextView) convertView.findViewById(R.id.tv_price);
            holder.textViewCount = (IteeTextView) convertView.findViewById(R.id.tv_count);

            RelativeLayout.LayoutParams paramsIvArrow = (RelativeLayout.LayoutParams) holder.textViewName.getLayoutParams();
            paramsIvArrow.width = (int) (Utils.getWidth(context) * 0.4f);
            paramsIvArrow.height = (int) (Utils.getHeight(context) * 0.04f);
            paramsIvArrow.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            paramsIvArrow.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            paramsIvArrow.setMargins(60, 0, 0, 0);
            holder.textViewName.setLayoutParams(paramsIvArrow);
            holder.textViewName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            holder.textViewName.setSingleLine(true);
            holder.textViewName.setSingleLine();
            holder.textViewName.setEllipsize(TextUtils.TruncateAt.END);
            holder.textViewName.setId(View.generateViewId());

            RelativeLayout.LayoutParams paramsTextView = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
            paramsTextView.width = (int) (Utils.getWidth(context) * 0.4f);
            paramsTextView.height = (int) (Utils.getHeight(context) * 0.08f);
            paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsTextView.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsTextView.setMargins(0, 0, 60, 0);
            holder.textViewPrice.setLayoutParams(paramsTextView);
            holder.textViewPrice.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            holder.textViewPrice.setId(View.generateViewId());

            RelativeLayout.LayoutParams paramsIvIcon = (RelativeLayout.LayoutParams) holder.textViewCount.getLayoutParams();
            paramsIvIcon.width = (int) (Utils.getWidth(context) * 0.4f);
            paramsIvIcon.height = (int) (Utils.getHeight(context) * 0.04f);
            paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            paramsIvIcon.addRule(RelativeLayout.BELOW, holder.textViewName.getId());
            paramsIvIcon.setMargins(60, 0, 0, 0);
            holder.textViewCount.setLayoutParams(paramsIvIcon);
            holder.textViewCount.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            convertView.setTag(holder);

        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        JsonPurchaseHistoryDetailRecord.GoodListItem goodListItem = data.get(groupPosition).getGoodList().get(childPosition);
        holder.textViewName.setText(goodListItem.getName());
        holder.textViewPrice.setText(currency + Utils.get2DigitDecimalString(goodListItem.getPrice()));
        holder.textViewCount.setText(Constants.STR_MULTIPLY + String.valueOf(goodListItem.getCount()));
        holder.textViewCount.setTextColor(context.getResources().getColor(R.color.common_blue));
        holder.textViewCount.setTextSize(Constants.FONT_SIZE_SMALLER);
        return convertView;

    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (data.get(groupPosition).getGoodList() == null) {
            return 0;
        } else {
            return data.get(groupPosition).getGoodList().size();
        }


    }

    @Override
    public Object getGroup(int groupPosition) {

        return data.get(groupPosition);

    }

    @Override
    public int getGroupCount() {

        return data.size();

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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_of_purchase_history_group, null);
            holder.textViewName = (IteeTextView) convertView.findViewById(R.id.tv_name);
            holder.textViewPrice = (IteeTextView) convertView.findViewById(R.id.tv_price);
            holder.textViewCount = (IteeTextView) convertView.findViewById(R.id.tv_count);

            RelativeLayout.LayoutParams paramsIvArrow = (RelativeLayout.LayoutParams) holder.textViewName.getLayoutParams();
            paramsIvArrow.width = (int) (Utils.getWidth(context) * 0.4f);
            paramsIvArrow.height = (int) (Utils.getHeight(context) * 0.08f);
            paramsIvArrow.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            paramsIvArrow.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsIvArrow.setMargins(40, 0, 0, 0);
            holder.textViewName.setLayoutParams(paramsIvArrow);
            holder.textViewName.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            holder.textViewName.setSingleLine(true);
            holder.textViewName.setSingleLine();
            holder.textViewName.setEllipsize(TextUtils.TruncateAt.END);
            RelativeLayout.LayoutParams paramsTextView = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
            paramsTextView.width = (int) (Utils.getWidth(context) * 0.4f);
            paramsTextView.height = (int) (Utils.getHeight(context) * 0.04f);
            paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsTextView.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            paramsTextView.setMargins(0, 0, 40, 0);
            holder.textViewPrice.setLayoutParams(paramsTextView);
            holder.textViewPrice.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
            holder.textViewPrice.setId(View.generateViewId());
            holder.textViewPrice.setTextSize(Constants.FONT_SIZE_SMALLER);

            RelativeLayout.LayoutParams paramsIvIcon = (RelativeLayout.LayoutParams) holder.textViewCount.getLayoutParams();
            paramsIvIcon.width = (int) (Utils.getWidth(context) * 0.4f);
            paramsIvIcon.height = (int) (Utils.getHeight(context) * 0.04f);
            paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            paramsIvIcon.addRule(RelativeLayout.BELOW, holder.textViewPrice.getId());
            paramsIvIcon.setMargins(0, 0, 40, 0);
            holder.textViewCount.setLayoutParams(paramsIvIcon);
            holder.textViewCount.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            convertView.setTag(holder);

        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        PurchaseHistoryExpandable expandable = data.get(groupPosition);

        switch (expandable.getType()) {
            case TYPE_GOOD:
                holder.textViewName.setText(expandable.getName());
                holder.textViewPrice.setText(currency + Utils.get2DigitDecimalString(expandable.getPrice()));
                holder.textViewCount.setText(Constants.STR_MULTIPLY + String.valueOf(expandable.getCount()));
                holder.textViewCount.setVisibility(View.VISIBLE);
                break;
            case TYPE_AA:

                holder.textViewName.setText(expandable.getName());
                holder.textViewPrice.setText(currency + expandable.getPrice());
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                layoutParams.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                holder.textViewPrice.setLayoutParams(layoutParams);
                holder.textViewCount.setVisibility(View.GONE);
                break;
            case TYPE_PACKAGE:
                holder.textViewName.setText(expandable.getName());
                holder.textViewPrice.setText(currency + expandable.getPrice());
                RelativeLayout.LayoutParams layoutParams1 = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                layoutParams1.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams1.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                holder.textViewPrice.setLayoutParams(layoutParams1);
                holder.textViewCount.setVisibility(View.GONE);
                break;
            case TYPE_REFUND:
                holder.textViewName.setText(expandable.getName());
                holder.textViewPrice.setText(DateUtils.getCurrentShowYearMonthDayHourMinuteSecondFromApiDateTime(expandable.getPrice(), context));
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) holder.textViewPrice.getLayoutParams();
                layoutParams2.removeRule(RelativeLayout.ALIGN_PARENT_TOP);
                layoutParams2.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
                holder.textViewPrice.setLayoutParams(layoutParams2);
                holder.textViewCount.setVisibility(View.GONE);


                break;
        }

        holder.textViewCount.setTextColor(context.getResources().getColor(R.color.common_blue));
        holder.textViewCount.setTextSize(Constants.FONT_SIZE_SMALLER);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        IteeTextView textViewName;
        IteeTextView textViewPrice;
        IteeTextView textViewCount;
    }

    class ChildViewHolder {
        IteeTextView textViewName;
        IteeTextView textViewPrice;
        IteeTextView textViewCount;
    }

}