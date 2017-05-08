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
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.manager.jsonentity.JsonAgentListGet;
import cn.situne.itee.view.IteeTextView;


public class AgentsListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<JsonAgentListGet.Agent> agentList;

    private int mRightWidth;

    private OnClickListener deleteListener;
    private OnClickListener listenerJump;

    public AgentsListAdapter(Context mContext, ArrayList<JsonAgentListGet.Agent> agentList, int rightWidth) {
        this.mContext = mContext;
        this.agentList = agentList;
        mRightWidth = rightWidth;
    }

    @Override
    public int getCount() {
        if (agentList != null) {
            return agentList.size() + 1;
        }
        return 0;
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


        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_agents, parent, false);//
            holder = new ViewHolder();

            holder.tvAgentName = new IteeTextView(mContext);
            holder.ivImage = new ImageView(mContext);
            holder.rlMemberItemLeft = (RelativeLayout) convertView.findViewById(R.id.member_item_left);

            holder.tvAgentName.setTextSize(Constants.FONT_SIZE_LARGER);

            LinearLayout.LayoutParams paramItemLeft = (LinearLayout.LayoutParams) holder.rlMemberItemLeft.getLayoutParams();
            paramItemLeft.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            paramItemLeft.height = DensityUtil.getActualHeightOnThisDevice(100, mContext);
            holder.rlMemberItemLeft.setLayoutParams(paramItemLeft);

            holder.rlMemberItemLeft.addView(holder.tvAgentName);
            RelativeLayout.LayoutParams paramsTvAgent = (RelativeLayout.LayoutParams) holder.tvAgentName.getLayoutParams();
            paramsTvAgent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvAgent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvAgent.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramsTvAgent.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsTvAgent.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
            holder.tvAgentName.setLayoutParams(paramsTvAgent);

            holder.rlMemberItemLeft.addView(holder.ivImage);
            RelativeLayout.LayoutParams paramsIvImage = (RelativeLayout.LayoutParams) holder.ivImage.getLayoutParams();
            paramsIvImage.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsIvImage.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsIvImage.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsIvImage.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsIvImage.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
            holder.ivImage.setLayoutParams(paramsIvImage);

            holder.rlMemberItemRight = (LinearLayout) convertView.findViewById(R.id.member_item_right);
            holder.ivImage.setImageResource(R.drawable.icon_black);
            holder.tvItemRightDelete = (IteeTextView) convertView.findViewById(R.id.item_right_delete);
            holder.tvItemRightDelete.setGravity(Gravity.CENTER);

            holder.viSeparator = convertView.findViewById(R.id.vi_separator);



            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            ListView.LayoutParams layoutParams
                    = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1);
            convertView.setLayoutParams(layoutParams);
            holder.viSeparator.setVisibility(View.GONE);
        } else {

            ListView.LayoutParams layoutParams
                    = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    DensityUtil.getActualHeightOnThisDevice(100, mContext));
            convertView.setLayoutParams(layoutParams);

            holder.viSeparator.setVisibility(View.VISIBLE);

            final JsonAgentListGet.Agent agent = agentList.get(position - 1);

            LinearLayout.LayoutParams paramItemLeft = (LinearLayout.LayoutParams) holder.rlMemberItemLeft.getLayoutParams();
            paramItemLeft.width = LinearLayout.LayoutParams.WRAP_CONTENT;
            paramItemLeft.height = DensityUtil.getActualHeightOnThisDevice(100, mContext);
            holder.rlMemberItemLeft.setLayoutParams(paramItemLeft);

            holder.tvItemRightDelete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag(agent.getAgentId());
                    if (getDeleteListener() != null) {
                        getDeleteListener().onClick(view);
                    }
                }
            });

            holder.rlMemberItemLeft.setTag(position - 1);
            holder.rlMemberItemLeft.setOnClickListener(getListenerJump());

            LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT);
            holder.rlMemberItemLeft.setLayoutParams(lp1);
            LayoutParams lp2 = new LayoutParams(mRightWidth, LayoutParams.MATCH_PARENT);
            holder.rlMemberItemRight.setLayoutParams(lp2);
            holder.tvAgentName.setText(agent.agentName);
            holder.tvAgentName.setTextColor(mContext.getResources().getColor(R.color.common_black));
            holder.tvAgentName.setTextSize(Constants.FONT_SIZE_15);
        }

        return convertView;
    }

    public OnClickListener getDeleteListener() {
        return deleteListener;
    }

    public void setDeleteListener(OnClickListener deleteListener) {
        this.deleteListener = deleteListener;
    }

    public OnClickListener getListenerJump() {
        return listenerJump;
    }

    public void setListenerJump(OnClickListener listenerJump) {
        this.listenerJump = listenerJump;
    }

    static class ViewHolder {
        RelativeLayout rlMemberItemLeft;
        LinearLayout rlMemberItemRight;
        IteeTextView tvAgentName;
        ImageView ivImage;
        IteeTextView tvItemRightDelete;
        View viSeparator;
    }
}
