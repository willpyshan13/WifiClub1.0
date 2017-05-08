package cn.situne.itee.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonAgentsPricingListGet;
import cn.situne.itee.view.IteeTextView;


public class AgentPricingTableListAdapter extends BaseAdapter {

    List<JsonAgentsPricingListGet.PricingItem> agentsPricingList;
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private View.OnClickListener listener;
    private int mRightWidth = 0;
    private BaseFragment mFragment;
    private View.OnClickListener itemClickListener;

    public AgentPricingTableListAdapter(Context ctx, List<JsonAgentsPricingListGet.PricingItem> agentsPricingList,
                                        int rightWidth, View.OnClickListener listener, BaseFragment fragment) {
        mContext = ctx;
        this.agentsPricingList = agentsPricingList;
        mRightWidth = rightWidth;
        mFragment = fragment;
        this.listener = listener;
    }

    public void setItemClickListener(OnClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return agentsPricingList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final JsonAgentsPricingListGet.PricingItem agentsPricingListItem = this.agentsPricingList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.agent_message_delete_item, parent, false);
            holder = new ViewHolder();
            holder.memberItemLeft = (RelativeLayout) convertView.findViewById(R.id.member_item_left);
            holder.memberItemRight = (LinearLayout) convertView.findViewById(R.id.member_item_right);
            holder.tvTime = (IteeTextView) convertView.findViewById(R.id.tv_time);
            holder.tvDate = (IteeTextView) convertView.findViewById(R.id.tv_date);
            holder.tvMoney = (IteeTextView) convertView.findViewById(R.id.tv_money);
            holder.tvProduct = (IteeTextView) convertView.findViewById(R.id.tv_color);
            holder.ivRightIcon = (ImageView) convertView.findViewById(R.id.iv_right_icon);
            holder.ivRightIcon.setImageResource(R.drawable.segment_icon_right_arrow);
            holder.itemRightDelete = (IteeTextView) convertView.findViewById(R.id.item_right_delete);

            holder.tvTime.setTextSize(Constants.FONT_SIZE_LARGER);
            holder.tvMoney.setTextSize(Constants.FONT_SIZE_LARGER);
            holder.tvDate.setTextSize(Constants.FONT_SIZE_SMALLER);
            holder.tvProduct.setTextSize(Constants.FONT_SIZE_SMALLER);

            holder.tvMoney.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            holder.tvProduct.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            holder.tvMoney.setSingleLine();
            holder.tvMoney.setSingleLine(true);
            holder.tvMoney.setEllipsize(TextUtils.TruncateAt.END);

            holder.tvProduct.setSingleLine();
            holder.tvProduct.setSingleLine(true);
            holder.tvProduct.setEllipsize(TextUtils.TruncateAt.END);

            holder.ivRightIcon.setId(View.generateViewId());
            LayoutUtils.setLayoutHeight(holder.memberItemLeft, 120, mContext);
            LayoutUtils.setWidthAndHeight(holder.tvTime, DensityUtil.getActualWidthOnThisDevice(320, mContext), 60);
            LayoutUtils.setWidthAndHeight(holder.tvMoney, DensityUtil.getActualWidthOnThisDevice(320, mContext), 60);
            LayoutUtils.setWidthAndHeight(holder.tvProduct, (int) (DensityUtil.getScreenWidth(mFragment.getActivity()) * 0.3), 60);
            LayoutUtils.setWidthAndHeight(holder.tvDate, (int) (DensityUtil.getScreenWidth(mFragment.getActivity()) * 0.7), 60);

            LayoutUtils.setRightArrow(holder.ivRightIcon, mContext);
            LayoutUtils.setLeftTopViewOfTwoCell(holder.tvTime, mContext);
            LayoutUtils.setLeftBottomViewOfTwoCell(holder.tvDate, mContext);
            LayoutUtils.setRightTopViewOfTwoCell(holder.tvMoney, holder.ivRightIcon);
            LayoutUtils.setRightBottomViewOfTwoCell(holder.tvProduct, holder.ivRightIcon);

            holder.itemRightDelete.setGravity(Gravity.CENTER);

            AppUtils.addBottomSeparatorLine(holder.memberItemLeft, mFragment);
            holder.itemRightDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag(agentsPricingListItem.getMainId());
                    listener.onClick(view);
                }
            });
            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        holder.memberItemLeft.setOnClickListener(itemClickListener);
        holder.memberItemLeft.setTag(position);

        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        holder.memberItemLeft.setLayoutParams(lp1);
        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.memberItemRight.setLayoutParams(lp2);

//        if (StringUtils.EMPTY.equals(agentsPricingListItem.getAgentDateStartTime())) {
//            holder.tvTime.setText(StringUtils.EMPTY);
//        } else {
//            //holder.tvTime.setText(agentsPricingListItem.getTime());
//        }
//        holder.tvTime.setTextColor(mContext.getResources().getColor(R.color.common_deep_blue));

//        if (StringUtils.EMPTY.equals(agentsPricingListItem.getDate())) {
//            holder.tvDate.setText(StringUtils.EMPTY);
//        } else {
//            ArrayList<String> dateList = AppUtils.changeString2List(agentsPricingListItem.getDate(), Constants.STR_COMMA);
//            String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);
//            holder.tvDate.setText(etTitle);
//        }

      //  holder.tvMoney.setText(AppUtils.getCurrentCurrency(mContext) + agentsPricingListItem.getSumPrizeMoney());
        holder.tvDate.setTextColor(mContext.getResources().getColor(R.color.common_gray));
        holder.tvMoney.setTextColor(mContext.getResources().getColor(R.color.common_black));
        holder.tvProduct.setTextColor(mContext.getResources().getColor(R.color.common_gray));
        //holder.tvProduct.setText(agentsPricingListItem.getProductNames());

        return convertView;
    }

    class ViewHolder {
        RelativeLayout memberItemLeft;
        LinearLayout memberItemRight;
        IteeTextView tvTime;
        IteeTextView tvDate;
        IteeTextView tvMoney;
        IteeTextView tvProduct;
        ImageView ivRightIcon;
        IteeTextView itemRightDelete;
    }
}
