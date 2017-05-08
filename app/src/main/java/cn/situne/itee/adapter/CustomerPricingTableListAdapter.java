package cn.situne.itee.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonLifeMember;
import cn.situne.itee.view.IteeTextView;


public class CustomerPricingTableListAdapter extends BaseAdapter {

    List<JsonLifeMember.MemberPricing> memberPricing;
    /**
     * 上下文对象
     */
    private BaseFragment baseFragment = null;
    private View.OnClickListener deleteButtonListener;
    private String memberTypeTypeId;
    private int mRightWidth = 0;
    private View.OnClickListener itemOnclickListener;
    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public CustomerPricingTableListAdapter(BaseFragment ctx, List<JsonLifeMember.MemberPricing> memberPricing,
                                           int rightWidth, String memberTypeTypeId) {
        baseFragment = ctx;
        this.memberPricing = memberPricing;
        mRightWidth = rightWidth;
        this.memberTypeTypeId = memberTypeTypeId;

    }

    public void setDeleteButtonListener(View.OnClickListener deleteButtonListener) {
        this.deleteButtonListener = deleteButtonListener;
    }

    public void setItemOnclickListener(View.OnClickListener itemOnclickListener) {
        this.itemOnclickListener = itemOnclickListener;
    }

    @Override
    public int getCount() {
        return memberPricing.size();
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
        Context mContext = baseFragment.getActivity();

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_of_fragment_life_member, parent, false);
            holder = new ViewHolder();

            holder.rlContainer = (RelativeLayout) convertView.findViewById(R.id.rl_container);
            holder.memberItemLeft = (RelativeLayout) convertView.findViewById(R.id.member_item_left);
            holder.memberItemRight = (LinearLayout) convertView.findViewById(R.id.member_item_right);
            holder.itemRightDelete = (IteeTextView) convertView.findViewById(R.id.item_right_delete);

            holder.tvOpenTime = new IteeTextView(mContext);
            holder.tvOpenTime.setId(View.generateViewId());

            holder.tvStartDate = new IteeTextView(mContext);
            holder.tvStartDate.setId(View.generateViewId());

            holder.tvEndDate = new IteeTextView(mContext);
            holder.tvEndDate.setId(View.generateViewId());

            holder.tvSeparation2 = new IteeTextView(mContext);
            holder.tvSeparation2.setId(View.generateViewId());

            holder.tvMember = new IteeTextView(mContext);
            holder.tvMember.setId(View.generateViewId());

            holder.tvMemberValue = new IteeTextView(mContext);
            holder.tvMemberValue.setId(View.generateViewId());

            holder.tvGuest = new IteeTextView(mContext);
            holder.tvGuest.setId(View.generateViewId());

            holder.tvGuestValue = new IteeTextView(mContext);
            holder.tvGuestValue.setId(View.generateViewId());

            holder.ivRightArrow = new ImageView(mContext);
            holder.ivRightArrow.setId(View.generateViewId());

            holder.tvProductName = new IteeTextView(mContext);
            holder.tvProductName.setId(View.generateViewId());

            holder.rlContainer.addView(holder.ivRightArrow);
            RelativeLayout.LayoutParams paramsRight = (RelativeLayout.LayoutParams) holder.ivRightArrow.getLayoutParams();
            paramsRight.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsRight.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsRight.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
            paramsRight.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsRight.addRule(RelativeLayout.CENTER_VERTICAL);
            holder.ivRightArrow.setLayoutParams(paramsRight);

            holder.rlContainer.addView(holder.tvOpenTime);
            RelativeLayout.LayoutParams paramsOpenTime = (RelativeLayout.LayoutParams) holder.tvOpenTime.getLayoutParams();
            paramsOpenTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsOpenTime.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsOpenTime.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramsOpenTime.setMargins(baseFragment.getActualWidthOnThisDevice(40), baseFragment.getActualHeightOnThisDevice(20), 0, 0);
            holder.tvOpenTime.setLayoutParams(paramsOpenTime);

//            holder.rlContainer.addView(holder.tvSeparation1);
//            RelativeLayout.LayoutParams paramsSeparation1 = (RelativeLayout.LayoutParams) holder.tvSeparation1.getLayoutParams();
//            paramsSeparation1.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//            paramsSeparation1.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//            paramsSeparation1.addRule(RelativeLayout.RIGHT_OF, holder.tvOpenTime.getId());
//            paramsSeparation1.addRule(RelativeLayout.ALIGN_TOP, holder.tvOpenTime.getId());
//            paramsSeparation1.setMargins(5, 0, 0, 0);
//            holder.tvSeparation1.setLayoutParams(paramsSeparation1);

//            holder.rlContainer.addView(holder.tvCloseTime);
//            RelativeLayout.LayoutParams paramsCloseTime = (RelativeLayout.LayoutParams) holder.tvCloseTime.getLayoutParams();
//            paramsCloseTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
//            paramsCloseTime.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
//            paramsCloseTime.addRule(RelativeLayout.RIGHT_OF, holder.tvSeparation1.getId());
//            paramsCloseTime.addRule(RelativeLayout.ALIGN_TOP, holder.tvSeparation1.getId());
//            paramsCloseTime.setMargins(5, 0, 0, 0);
//            holder.tvCloseTime.setLayoutParams(paramsCloseTime);

            holder.rlContainer.addView(holder.tvMemberValue);
            RelativeLayout.LayoutParams paramsMemberVal = (RelativeLayout.LayoutParams) holder.tvMemberValue.getLayoutParams();
            paramsMemberVal.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsMemberVal.height = baseFragment.getActualHeightOnThisDevice(55);
            paramsMemberVal.addRule(RelativeLayout.LEFT_OF, holder.ivRightArrow.getId());
            paramsMemberVal.addRule(RelativeLayout.ALIGN_TOP, holder.tvOpenTime.getId());
            holder.tvMemberValue.setLayoutParams(paramsMemberVal);

            holder.rlContainer.addView(holder.tvMember);
            RelativeLayout.LayoutParams paramsMember = (RelativeLayout.LayoutParams) holder.tvMember.getLayoutParams();
            paramsMember.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsMember.height = baseFragment.getActualHeightOnThisDevice(55);
            paramsMember.addRule(RelativeLayout.LEFT_OF, holder.tvMemberValue.getId());
            paramsMember.addRule(RelativeLayout.ALIGN_BOTTOM, holder.tvMemberValue.getId());

            holder.tvMember.setLayoutParams(paramsMember);


            holder.rlContainer.addView(holder.tvStartDate);
            RelativeLayout.LayoutParams paramsStartDate = (RelativeLayout.LayoutParams) holder.tvStartDate.getLayoutParams();
            paramsStartDate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsStartDate.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsStartDate.addRule(RelativeLayout.BELOW, holder.tvOpenTime.getId());
            paramsStartDate.addRule(RelativeLayout.ALIGN_LEFT, holder.tvOpenTime.getId());
            paramsStartDate.setMargins(0, 0, 0, 0);
            holder.tvStartDate.setLayoutParams(paramsStartDate);

            holder.rlContainer.addView(holder.tvSeparation2);
            RelativeLayout.LayoutParams paramsSeparation2 = (RelativeLayout.LayoutParams) holder.tvSeparation2.getLayoutParams();
            paramsSeparation2.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsSeparation2.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsSeparation2.addRule(RelativeLayout.RIGHT_OF, holder.tvStartDate.getId());
            paramsSeparation2.addRule(RelativeLayout.ALIGN_TOP, holder.tvStartDate.getId());
            paramsSeparation2.setMargins(5, 0, 0, 0);
            holder.tvSeparation2.setLayoutParams(paramsSeparation2);

            holder.rlContainer.addView(holder.tvEndDate);
            RelativeLayout.LayoutParams paramsEndDate = (RelativeLayout.LayoutParams) holder.tvEndDate.getLayoutParams();
            paramsEndDate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsEndDate.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsEndDate.addRule(RelativeLayout.RIGHT_OF, holder.tvSeparation2.getId());
            paramsEndDate.addRule(RelativeLayout.ALIGN_TOP, holder.tvSeparation2.getId());
            paramsEndDate.setMargins(5, 0, 0, 0);
            holder.tvEndDate.setLayoutParams(paramsEndDate);

            holder.rlContainer.addView(holder.tvGuestValue);
            RelativeLayout.LayoutParams paramsGuestVal = (RelativeLayout.LayoutParams) holder.tvGuestValue.getLayoutParams();
            paramsGuestVal.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsGuestVal.height = baseFragment.getActualHeightOnThisDevice(55);
            paramsGuestVal.addRule(RelativeLayout.LEFT_OF, holder.ivRightArrow.getId());
            paramsGuestVal.addRule(RelativeLayout.ALIGN_TOP, holder.tvEndDate.getId());
            holder.tvGuestValue.setLayoutParams(paramsGuestVal);

            holder.rlContainer.addView(holder.tvGuest);
            RelativeLayout.LayoutParams paramsGuest = (RelativeLayout.LayoutParams) holder.tvGuest.getLayoutParams();
            paramsGuest.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsGuest.height = baseFragment.getActualHeightOnThisDevice(55);
            paramsGuest.addRule(RelativeLayout.LEFT_OF, holder.tvGuestValue.getId());
            paramsGuest.addRule(RelativeLayout.ALIGN_BOTTOM, holder.tvGuestValue.getId());

            holder.tvGuest.setLayoutParams(paramsGuest);

            holder.rlContainer.addView(holder.tvProductName);
            RelativeLayout.LayoutParams tvProductNameLayoutParams
                    = (RelativeLayout.LayoutParams) holder.tvProductName.getLayoutParams();
            tvProductNameLayoutParams.addRule(RelativeLayout.BELOW, holder.tvGuest.getId());
            tvProductNameLayoutParams.addRule(RelativeLayout.LEFT_OF, holder.ivRightArrow.getId());
            tvProductNameLayoutParams.width = baseFragment.getActualWidthOnThisDevice(200);
            holder.tvProductName.setLayoutParams(tvProductNameLayoutParams);

            holder.tvProductName.setEllipsize(TextUtils.TruncateAt.END);
            holder.tvProductName.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
            holder.tvProductName.setTextColor(baseFragment.getColor(R.color.common_gray));

            RelativeLayout.LayoutParams tvPriceParams
                    = new RelativeLayout.LayoutParams(baseFragment.getActualWidthOnThisDevice(200), RelativeLayout.LayoutParams.WRAP_CONTENT);
            tvPriceParams.addRule(RelativeLayout.LEFT_OF, holder.ivRightArrow.getId());
            tvPriceParams.setMargins(baseFragment.getActualWidthOnThisDevice(40), baseFragment.getActualHeightOnThisDevice(20), 0, 0);
            holder.tvPrice = new IteeTextView(baseFragment);
            holder.tvPrice.setLayoutParams(tvPriceParams);
            holder.tvPrice.setId(View.generateViewId());

            holder.tvPrice.setEllipsize(TextUtils.TruncateAt.END);
            holder.rlContainer.addView(holder.tvPrice);
            holder.tvPrice.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);

            holder.tvPrice.setTextSize(Constants.FONT_SIZE_SMALLER);

            if (Constants.CUSTOMER_NON_MEMBER.equals(this.memberTypeTypeId)) {
                holder.tvMember.setVisibility(View.GONE);
                holder.tvMemberValue.setVisibility(View.GONE);
                holder.tvGuest.setVisibility(View.GONE);
                holder.tvGuestValue.setVisibility(View.GONE);
                tvProductNameLayoutParams
                        = new RelativeLayout.LayoutParams(baseFragment.getActualWidthOnThisDevice(240), RelativeLayout.LayoutParams.WRAP_CONTENT);
                tvProductNameLayoutParams.addRule(RelativeLayout.BELOW, holder.tvPrice.getId());
                tvProductNameLayoutParams.addRule(RelativeLayout.LEFT_OF, holder.ivRightArrow.getId());

                holder.tvProductName.setLayoutParams(tvProductNameLayoutParams);
                holder.tvProductName.setTextSize(Constants.FONT_SIZE_SMALLER);
            } else {
                holder.tvPrice.setVisibility(View.INVISIBLE);
            }

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        holder.memberItemLeft.setLayoutParams(lp1);

        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.memberItemRight.setLayoutParams(lp2);

        JsonLifeMember.MemberPricing memberPricing = this.memberPricing.get(position);

        holder.tvOpenTime.setText(memberPricing.getTime());

        holder.tvSeparation2.setTextColor(baseFragment.getResources().getColor(R.color.common_gray));

        ArrayList<String> dateList = AppUtils.changeString2List(memberPricing.getPricingDate(), Constants.STR_COMMA);
        String etTitle = AppUtils.getEtTimeTitle(dateList, mContext);
        holder.tvStartDate.setText(etTitle);
        holder.tvStartDate.setTextSize(Constants.FONT_SIZE_SMALLER);
        holder.tvStartDate.setTextColor(baseFragment.getResources().getColor(R.color.common_gray));
        //holder.tvEndDate.setText(memberPricingList.getDateEndDate());
        holder.tvEndDate.setTextSize(Constants.FONT_SIZE_SMALLER);
        holder.tvEndDate.setTextColor(baseFragment.getResources().getColor(R.color.common_gray));

        holder.tvMember.setText(R.string.customers_member);
        holder.tvMember.setTextSize(Constants.FONT_SIZE_SMALLER);

        holder.tvMemberValue.setText(AppUtils.getCurrentCurrency(baseFragment.getBaseActivity()) + memberPricing.getMemberProductPrize());
        holder.tvMemberValue.setTextColor(baseFragment.getResources().getColor(R.color.common_black));
        holder.tvMemberValue.setTextSize(Constants.FONT_SIZE_SMALLER);

        holder.tvGuest.setText(R.string.customers_guest);
        holder.tvGuest.setTextSize(Constants.FONT_SIZE_SMALLER);
        holder.tvGuest.setGravity(Gravity.CENTER);

        holder.tvGuestValue.setText(AppUtils.getCurrentCurrency(baseFragment.getBaseActivity()) + memberPricing.getGuestProductPrize());
        holder.tvGuestValue.setTextColor(baseFragment.getResources().getColor(R.color.common_black));
        holder.tvGuestValue.setTextSize(Constants.FONT_SIZE_SMALLER);
        holder.tvGuestValue.setGravity(Gravity.CENTER);

        holder.tvOpenTime.setTextSize(Constants.FONT_SIZE_LARGER);
        holder.tvProductName.setTextSize(Constants.FONT_SIZE_SMALLER);

        holder.tvGuestValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        holder.tvGuestValue.setSingleLine();
        holder.tvGuestValue.setSingleLine(true);
        holder.tvGuestValue.setEllipsize(TextUtils.TruncateAt.END);

        holder.tvMemberValue.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        holder.tvMemberValue.setSingleLine();
        holder.tvMemberValue.setSingleLine(true);
        holder.tvMemberValue.setEllipsize(TextUtils.TruncateAt.END);


        holder.ivRightArrow.setImageResource(R.drawable.icon_right_arrow);

        holder.itemRightDelete.setTag(memberPricing);
        holder.itemRightDelete.setGravity(Gravity.CENTER);
        holder.itemRightDelete.setOnClickListener(deleteButtonListener);

        holder.memberItemLeft.setTag(memberPricing);
        holder.memberItemLeft.setOnClickListener(itemOnclickListener);

        holder.tvProductName.setText(memberPricing.getProductEnName());


        holder.tvPrice.setText(AppUtils.getCurrentCurrency(baseFragment.getBaseActivity()) + memberPricing.getMemberProductPrize());

        if (Constants.CUSTOMER_NON_MEMBER.equals(this.memberTypeTypeId)) {

            holder.tvPrice.setText(AppUtils.getCurrentCurrency(baseFragment.getBaseActivity()) + memberPricing.getGuestProductPrize());
        }

        holder.tvPrice.setTextColor(baseFragment.getResources().getColor(R.color.common_black));

        holder.tvPrice.setGravity(Gravity.END);

        holder.tvPrice.setSingleLine();
        holder.tvPrice.setEllipsize(TextUtils.TruncateAt.END);
        holder.tvPrice.setTextSize(Constants.FONT_SIZE_SMALLER);

        return convertView;
    }

    public void setOnRightItemClickListener(onRightItemClickListener listener) {
        mListener = listener;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }

    static class ViewHolder {
        RelativeLayout memberItemLeft;
        LinearLayout memberItemRight;
        RelativeLayout rlContainer;

        IteeTextView tvOpenTime;
//        IteeTextView tvSeparation1;
//        IteeTextView tvCloseTime;

        IteeTextView tvStartDate;
        IteeTextView tvSeparation2;
        IteeTextView tvEndDate;

        IteeTextView tvMember;
        IteeTextView tvMemberValue;

        IteeTextView tvGuest;
        IteeTextView tvGuestValue;

        IteeTextView itemRightDelete;
        ImageView ivRightArrow;

        IteeTextView tvProductName;
        IteeTextView tvPrice;
    }
}
