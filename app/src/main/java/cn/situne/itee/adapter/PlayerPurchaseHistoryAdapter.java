package cn.situne.itee.adapter;

import android.content.Context;
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

import java.util.ArrayList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.entity.BaseViewHolder;
import cn.situne.itee.manager.jsonentity.JsonPurchaseHistoryGet;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SwipeListView;


public class PlayerPurchaseHistoryAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private ArrayList<JsonPurchaseHistoryGet.DataListItem> data;

    private int mRightWidth = 0;
    private Integer memberId;
    private OnClickListener onClickListener;

    public PlayerPurchaseHistoryAdapter(Context ctx, JsonPurchaseHistoryGet data, int rightWidth, Integer memberId,
                                        OnClickListener onClickRefundListener) {
        mContext = ctx;
        this.data = data.getDataList();
        this.memberId = memberId;
        mRightWidth = rightWidth;
        onClickListener = onClickRefundListener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        JsonPurchaseHistoryGet.DataListItem dataListItem = data.get(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row_purchase_history, parent, false);
            holder = new ViewHolder();
            holder.itemLeft = (RelativeLayout) convertView.findViewById(R.id.item_left);
            holder.itemRight = (LinearLayout) convertView.findViewById(R.id.item_right);

            holder.tvTitle = (IteeTextView) convertView.findViewById(R.id.tv_title);
            holder.tvPrice = (IteeTextView) convertView.findViewById(R.id.tv_price);
            holder.ivArrow = (ImageView) convertView.findViewById(R.id.iv_arrow);

            holder.itemRightRefund = (IteeTextView) convertView.findViewById(R.id.item_right_refund);

            holder.ivArrow.setId(View.generateViewId());

            LayoutUtils.setCellLeftKeyViewOfRelativeLayout(holder.tvTitle, mContext);
            LayoutUtils.setLeftOfView(holder.tvPrice, holder.ivArrow, 0, mContext);

            holder.itemRightRefund.setGravity(Gravity.CENTER);
            holder.tvPrice.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

            holder.tvTitle.setTextSize(Constants.FONT_SIZE_SMALLER);
            holder.tvPrice.setTextSize(Constants.FONT_SIZE_SMALLER);
            holder.itemRightRefund.setTextSize(Constants.FONT_SIZE_SMALLER);

            LayoutUtils.setRightArrow(holder.ivArrow, mContext);
            holder.itemLeft.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
            holder.itemRight.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        holder.itemLeft.setLayoutParams(lp1);

        holder.tvTitle.setText(DateUtils.getCurrentShowYearMonthDayHourMinuteSecondFromApiDateTime(dataListItem.getTime(), mContext));
        if (dataListItem.getPayStatus() != null && dataListItem.getPayStatus() == 0) {
            holder.tvPrice.setTextColor(mContext.getResources().getColor(R.color.common_red));
            holder.itemRight.setVisibility(View.GONE);
            LayoutParams lp3 = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            holder.itemRight.setLayoutParams(lp3);
            holder.disableright = SwipeListView.DISABLE_RIGHT;
        } else {
            LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
            holder.itemRight.setLayoutParams(lp2);
            holder.disableright = Constants.STR_EMPTY;
        }

        if (Constants.STR_FLAG_YES.equals(dataListItem.getRefundFlag())) {
            holder.disableright = Constants.STR_EMPTY;
        } else {
            holder.disableright = SwipeListView.DISABLE_RIGHT;
        }

        holder.tvPrice.setText(AppUtils.getCurrentCurrency(mContext) + dataListItem.getAmount());

        holder.itemRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        holder.itemRightRefund.setTag(position);
        holder.itemRightRefund.setOnClickListener(onClickListener);
        return convertView;
    }

    class ViewHolder extends BaseViewHolder {
        RelativeLayout itemLeft;
        LinearLayout itemRight;
        IteeTextView tvTitle;
        IteeTextView tvPrice;
        IteeTextView itemRightRefund;
        ImageView ivArrow;
    }
}
