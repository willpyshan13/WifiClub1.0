package cn.situne.itee.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.manager.jsonentity.JsonSigningGuest;
import cn.situne.itee.view.IteeTextView;

public class MemberWithIndexAdapter extends BaseAdapter {

    private List<JsonSigningGuest.Member> dataSource;
    private BaseFragment mBaseFragment;
    private AlphabetIndexer indexer;
    private int fromAdd;
    private String mNowSignType;
    private int mParentPosition;

    private Map<String, Integer> map;

    public MemberWithIndexAdapter(List<JsonSigningGuest.Member> entity,
                                  String nowSignType, BaseFragment mBaseFragment,
                                  AlphabetIndexer indexer, int parentPosition, int fromAdd) {
        this.dataSource = entity;
        this.mBaseFragment = mBaseFragment;
        this.indexer = indexer;
        this.fromAdd = fromAdd;
        this.mParentPosition = parentPosition;
        this.mNowSignType = nowSignType;
    }

    public void setMap(Map<String, Integer> map) {
        this.map = map;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public JsonSigningGuest.Member getItem(int position) {
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        JsonSigningGuest.Member member = dataSource.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mBaseFragment.getActivity()).inflate(R.layout.list_row_member, null);
            holder = new ViewHolder();
            holder.tvLetter = (IteeTextView) convertView.findViewById(R.id.tvLetter);
            holder.tvMemberCode = (IteeTextView) convertView.findViewById(R.id.tv_member_code);
            holder.tvMemberRole = (IteeTextView) convertView.findViewById(R.id.tv_member_role);
            holder.tvMemberName = (IteeTextView) convertView.findViewById(R.id.tv_member_name);
            holder.rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_member_item_all);

            holder.tvMemberCode.setTextSize(Constants.FONT_SIZE_SMALLER);
            holder.tvMemberRole.setTextSize(Constants.FONT_SIZE_SMALLER);
            holder.tvMemberName.setTextSize(Constants.FONT_SIZE_SMALLER);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvMemberName.setText(member.getMemberName());
        holder.tvMemberRole.setText(member.getMemberNo());
        holder.tvMemberCode.setText(member.getPhone());

        holder.tvLetter.setVisibility(View.VISIBLE);
        int section = indexer.getSectionForPosition(position);
        int pos = indexer.getPositionForSection(section);
        if (pos == position) {
            holder.tvLetter.setVisibility(View.VISIBLE);
            String[] args = (String[]) indexer.getSections();
            holder.tvLetter.setText(args[indexer.getSectionForPosition(position)]);
        } else {
            holder.tvLetter.setVisibility(View.GONE);
        }
        holder.rlItem.setTag(member);
        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                JsonSigningGuest.Member tempMember = (JsonSigningGuest.Member) v.getTag();
                if (fromAdd != -1) {

                    //can not overstep Member's signNumber
                    int signNumber = 0;
                    if (Utils.isStringNotNullOrEmpty(tempMember.getSignNumber())) {
                        signNumber = Integer.valueOf(tempMember.getSignNumber());
                    }
                    int count = 0;
                    if (map != null && tempMember.getMemberId() != null && map.get(String.valueOf(tempMember.getMemberId())) != null) {
                        count = map.get(String.valueOf(tempMember.getMemberId()));
                    }

                    if (Constants.STR_FLAG_YES.equals(mNowSignType) && count >= signNumber) {
                        AppUtils.showMessageWithOkButton(mBaseFragment, mBaseFragment.getString(R.string.error_mes00005));
                    } else {

                        //客户绑定人员
                        bundle.putString("fromFlag", "itemMemberDetail");
                        bundle.putString("signType", mNowSignType);
                        bundle.putInt("position", fromAdd);

                        bundle.putInt("parentPosition", mParentPosition);
                        bundle.putSerializable("member", tempMember);
                        mBaseFragment.getBaseActivity().doBackWithReturnValue(bundle, TeeTimeAddFragment.class);
                    }
                } else {
                    //返回客户名字
                    bundle.putString("fromFlag", "memberName");
                    bundle.putSerializable("member", tempMember);


                    bundle.putString("signType", mNowSignType);
                    mBaseFragment.getBaseActivity().doBackWithReturnValue(bundle, TeeTimeAddFragment.class);
                }

            }
        });
        return convertView;
    }

    class ViewHolder {
        IteeTextView tvLetter;
        IteeTextView tvMemberName;
        IteeTextView tvMemberCode;
        IteeTextView tvMemberRole;
        RelativeLayout rlItem;
    }
}
