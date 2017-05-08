package cn.situne.itee.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.activity.MainActivity;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.entity.IncomingCall;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.view.IteeTextView;


public class TeeTimeIncomingAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private List<IncomingCall> data;

    private int mRightWidth = 0;

    public TeeTimeIncomingAdapter(Context ctx, List<IncomingCall> data, int rightWidth) {
        mContext = ctx;
        this.data = data;
        mRightWidth = rightWidth;
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
        final IncomingCall incomingCall = data.get(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_row_member_tel, parent, false);
            holder = new ViewHolder();
            holder.item_left = (LinearLayout) convertView.findViewById(R.id.item_left);
            holder.item_right = (LinearLayout) convertView.findViewById(R.id.item_right);

            holder.tv_title = (IteeTextView) convertView.findViewById(R.id.tv_title);
            holder.tv_msg = (IteeTextView) convertView.findViewById(R.id.tv_price);
            holder.tv_time = (IteeTextView) convertView.findViewById(R.id.tv_time);

            holder.item_right_add = (IteeTextView) convertView.findViewById(R.id.item_right_refund);
            holder.item_right_call = (IteeTextView) convertView.findViewById(R.id.item_right_call);

            holder.item_right_add.setGravity(Gravity.CENTER);
            holder.item_right_call.setGravity(Gravity.CENTER);

            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        holder.item_left.setLayoutParams(lp1);
        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.item_right.setLayoutParams(lp2);

//
        holder.tv_title.setText(incomingCall.getName());
        holder.tv_msg.setText(incomingCall.getTel());
        if (incomingCall.getTime().length() > 16) {
            holder.tv_time.setText(incomingCall.getTime().substring(10, 16));
        } else {
            holder.tv_time.setText(incomingCall.getTime());
        }


        holder.item_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEdit, mContext)
                        && AppUtils.getAuth(AppUtils.AuthControl.AuthControlProfileEditNonmember, mContext);
                if (hasPermission) {
                    MainActivity mainActivity = (MainActivity) mContext;
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.COMMON_FRAGMENT_MODE, BaseEditFragment.FragmentMode.FragmentModeAdd.value());
                    bundle.putString(TransKey.TEE_TIME_MEMBER_NAME, incomingCall.getName());
                    bundle.putString(TransKey.TEE_TIME_MEMBER_TEL, incomingCall.getTel());
                    mainActivity.pushFragment(PlayerInfoEditFragment.class, bundle);
                } else {
                    AppUtils.showHaveNoPermission(mContext);
                }
            }
        });
        holder.item_right_call.setTag(incomingCall.getTel());
        holder.item_right_call.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + v.getTag()));
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        LinearLayout item_left;
        LinearLayout item_right;
        IteeTextView tv_title;
        IteeTextView tv_msg;
        IteeTextView tv_time;
        IteeTextView item_right_add;
        IteeTextView item_right_call;
    }
}
