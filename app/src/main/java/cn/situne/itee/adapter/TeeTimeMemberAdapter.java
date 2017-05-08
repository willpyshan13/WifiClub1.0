package cn.situne.itee.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.activity.MainActivity;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.manager.jsonentity.JsonCustomerBookingSearch;
import cn.situne.itee.view.IteeNetworkImageView;
import cn.situne.itee.view.IteeTextView;


public class TeeTimeMemberAdapter extends BaseAdapter {

    /**
     * 上下文对象
     */
    private Context mContext = null;
    private List<JsonCustomerBookingSearch.Member> data;


    private String nowSignType;

    private int mRightWidth = 0;
    private int nowPosition, parentPosition;
    private String choiceId;
    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public TeeTimeMemberAdapter(Context ctx, List<JsonCustomerBookingSearch.Member> data, String choiceId, int nowPosition, int parentPosition, int rightWidth) {
        mContext = ctx;
        this.data = data;
        this.parentPosition = parentPosition;
        this.nowPosition = nowPosition;
        mRightWidth = rightWidth;
        this.choiceId = choiceId;
    }

    public void setNowSignType(String nowSignType) {
        this.nowSignType = nowSignType;
    }

    @Override
    public int getCount() {
        return data.size();
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
        final JsonCustomerBookingSearch.Member member = data.get(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row_newteetimes_member_name, parent, false);
            holder = new ViewHolder();
            holder.itemIcon = (IteeNetworkImageView) convertView.findViewById(R.id.iv_icon);
            holder.itemLeft = (RelativeLayout) convertView.findViewById(R.id.item_left);
            holder.itemRight = (LinearLayout) convertView.findViewById(R.id.item_right);
            holder.itemRightTxt = (IteeTextView) convertView.findViewById(R.id.item_right_txt);
            holder.itemRightAdd = (IteeTextView) convertView.findViewById(R.id.item_right_refund);
            holder.itemRightDelete = (IteeTextView) convertView.findViewById(R.id.item_right_delete);
            holder.tvTelValue = (IteeTextView) convertView.findViewById(R.id.tv_tel_value);
            holder.tvMemberNo = (IteeTextView) convertView.findViewById(R.id.tv_member_no);
            holder.tvMemberName = (IteeTextView) convertView.findViewById(R.id.tv_member_name);
            holder.tvBirthValue = (IteeTextView) convertView.findViewById(R.id.tv_birth_value);
            holder.tvZipCode = (IteeTextView) convertView.findViewById(R.id.tv_zip_code);
            LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(mRightWidth, LinearLayout.LayoutParams.MATCH_PARENT);
            holder.itemRight.setLayoutParams(lp2);
            holder.itemIcon.setDefaultImageResId(R.drawable.member_photo);

//            holder.itemRightTxt;
            holder.tvTelValue.setTextSize(Constants.FONT_SIZE_SMALLER);
            holder.tvMemberNo.setTextSize(Constants.FONT_SIZE_SMALLER);
            holder.tvMemberName.setTextSize(Constants.FONT_SIZE_SMALLER);
            holder.tvBirthValue.setTextSize(Constants.FONT_SIZE_SMALLER);
            holder.tvZipCode.setTextSize(Constants.FONT_SIZE_SMALLER);

            holder.itemRightTxt.setGravity(Gravity.CENTER);

            holder.tvMemberNo.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);

            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        //
        if (choiceId.equals(String.valueOf(member.getMemberId()))) {
            holder.itemRightTxt.setText(R.string.common_undo);
            holder.itemRightTxt.setBackgroundResource(R.drawable.bg_common_delete);
        } else {
            holder.itemRightTxt.setText(R.string.common_select);
            holder.itemRightTxt.setBackgroundResource(R.drawable.bg_common_edit);
        }

        holder.tvTelValue.setText(member.getMemberTel());
        holder.tvMemberNo.setText(member.getMemberNo());
        holder.tvMemberName.setText(member.getMemberName());
        holder.tvBirthValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(member.getMemberBirth(), mContext));
        holder.tvZipCode.setText(member.getZipCode());
        holder.itemRightTxt.setTag(member);
        AppUtils.showNetworkImage(holder.itemIcon, member.getMemberPic());
        //设置默认值
        holder.itemRightTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                JsonCustomerBookingSearch.Member tempMember = (JsonCustomerBookingSearch.Member) v.getTag();
                Utils.log("66666 : " + tempMember.getEndDateFlag());

                Bundle bundle = new Bundle();
                bundle.putString("fromFlag", "memberRole");
                bundle.putSerializable("member", tempMember);
                bundle.putInt("position", nowPosition);
                bundle.putInt("parentPosition", parentPosition);
                bundle.putString("nowSignType", nowSignType);
                bundle.putString(TransKey.END_DATE_FLAG, tempMember.getEndDateFlag());

                if (choiceId.equals(String.valueOf(tempMember.getMemberId()))) {
                    bundle.putString("fromFlag", "memberRole");
                    bundle.putSerializable("member", null);
                    bundle.putInt("position", nowPosition);
                    bundle.putInt("parentPosition", parentPosition);
                }
                ((MainActivity) mContext).doBackWithReturnValue(bundle, TeeTimeAddFragment.class);
                if (mListener != null) {
                    mListener.onRightItemClick(v, position);
                }
            }
        });
        return convertView;
    }

    public interface onRightItemClickListener {
        void onRightItemClick(View v, int position);
    }

    static class ViewHolder {

        IteeNetworkImageView itemIcon;
        RelativeLayout itemLeft;
        LinearLayout itemRight;
        IteeTextView itemRightTxt;
        IteeTextView itemRightAdd;
        IteeTextView itemRightDelete;
        IteeTextView tvTelValue;
        IteeTextView tvMemberNo;
        IteeTextView tvMemberName;
        IteeTextView tvBirthValue;
        IteeTextView tvZipCode;
    }
}
