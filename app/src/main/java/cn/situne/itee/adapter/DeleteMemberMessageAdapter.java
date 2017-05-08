package cn.situne.itee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.entity.DeleteMemberMessage;
import cn.situne.itee.view.IteeTextView;


public class DeleteMemberMessageAdapter extends BaseAdapter {
    /**
     * 上下文对象
     */
    private Context mContext = null;
    private List<DeleteMemberMessage> data;

    private int mRightWidth = 0;
    /**
     * 单击事件监听器
     */
    private onRightItemClickListener mListener = null;

    public DeleteMemberMessageAdapter(Context ctx, List<DeleteMemberMessage> data, int rightWidth) {
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
        DeleteMemberMessage deleteMemberMessage = data.get(position);

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.de_select_member_item, parent, false);
            holder = new ViewHolder();
            holder.member_item_left = (RelativeLayout) convertView.findViewById(R.id.member_item_left);
            holder.member_item_right = (LinearLayout) convertView.findViewById(R.id.member_item_right);

            holder.tv_member_name = (IteeTextView) convertView.findViewById(R.id.tv_member_name);
            holder.tv_code = (IteeTextView) convertView.findViewById(R.id.tv_code);
            holder.tv_birth = (IteeTextView) convertView.findViewById(R.id.tv_birth);
            holder.tv_birth_code = (IteeTextView) convertView.findViewById(R.id.tv_birth_code);
            holder.tv_tel = (IteeTextView) convertView.findViewById(R.id.tv_tel);
            holder.tv_tel_code = (IteeTextView) convertView.findViewById(R.id.tv_tel_code);
            holder.tv_zip = (IteeTextView) convertView.findViewById(R.id.tv_zip);
            holder.tv_zip_code = (IteeTextView) convertView.findViewById(R.id.tv_zip_code);
            holder.item_right_delete = (IteeTextView) convertView.findViewById(R.id.item_right_delete);
            convertView.setTag(holder);
        } else {// 有直接获得ViewHolder
            holder = (ViewHolder) convertView.getTag();
        }

        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        holder.member_item_left.setLayoutParams(lp1);
        LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
        holder.member_item_right.setLayoutParams(lp2);

//
        holder.tv_member_name.setText(deleteMemberMessage.getMemberName());
        holder.tv_code.setText(deleteMemberMessage.getMemberCode());
        holder.tv_birth.setText(deleteMemberMessage.getBirthday());
        holder.tv_birth_code.setText(deleteMemberMessage.getBirthdayCode());
        holder.tv_tel.setText(deleteMemberMessage.getTel());
        holder.tv_tel_code.setText(deleteMemberMessage.getTelCode());
        holder.tv_zip.setText(deleteMemberMessage.getZip());
        holder.tv_zip_code.setText(deleteMemberMessage.getZipCode());


        holder.member_item_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
        RelativeLayout member_item_left;
        LinearLayout member_item_right;
        IteeTextView tv_member_name;
        IteeTextView tv_code;
        IteeTextView tv_birth;
        IteeTextView tv_birth_code;
        IteeTextView tv_tel;
        IteeTextView tv_tel_code;
        IteeTextView tv_zip;
        IteeTextView tv_zip_code;

        IteeTextView item_right_delete;
    }
}
