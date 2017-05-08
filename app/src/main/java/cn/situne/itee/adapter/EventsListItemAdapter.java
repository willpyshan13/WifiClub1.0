package cn.situne.itee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.manager.jsonentity.JsonEventListGet;
import cn.situne.itee.view.IteeTextView;

/**
 * EventsListItemAdapter
 *
 * @author song
 */

public class EventsListItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<JsonEventListGet.Event> eventList;

    public EventsListItemAdapter(Context con, List<JsonEventListGet.Event> event) {
        this.mContext = con;
        this.eventList = event;
    }

    @Override
    public int getCount() {
        if (eventList != null) {
            return eventList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return eventList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_of_events_list, null);
            holder.llEventsListContainer = (LinearLayout) convertView.findViewById(R.id.ll_events_list);
            holder.rlTopic = (RelativeLayout) convertView.findViewById(R.id.rl_topic_container);
            holder.rlContent = (RelativeLayout) convertView.findViewById(R.id.rl_content_container);

            holder.tvTopic = new IteeTextView(mContext);
            holder.tvTopic.setId(View.generateViewId());

            holder.tvPrice = new IteeTextView(mContext);
            holder.tvPrice.setId(View.generateViewId());

            holder.tvStartDate = new IteeTextView(mContext);
            holder.tvStartDate.setId(View.generateViewId());

            holder.tvSeparationDate = new IteeTextView(mContext);
            holder.tvSeparationDate.setId(View.generateViewId());

            holder.tvEndDate = new IteeTextView(mContext);
            holder.tvEndDate.setId(View.generateViewId());

            holder.tvOpenTime = new IteeTextView(mContext);
            holder.tvOpenTime.setId(View.generateViewId());

            holder.tvSeparationTime = new IteeTextView(mContext);
            holder.tvSeparationTime.setId(View.generateViewId());

            holder.tvCloseTime = new IteeTextView(mContext);
            holder.tvCloseTime.setId(View.generateViewId());

            LayoutUtils.setLayoutHeight(holder.rlTopic, 60, mContext);
            LayoutUtils.setLayoutHeight(holder.rlTopic, 120, mContext);

            holder.rlTopic.addView(holder.tvTopic);
            RelativeLayout.LayoutParams paramsTopic = (RelativeLayout.LayoutParams) holder.tvTopic.getLayoutParams();
            paramsTopic.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            paramsTopic.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            paramsTopic.leftMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
            paramsTopic.rightMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
            paramsTopic.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            holder.tvTopic.setLayoutParams(paramsTopic);

            holder.rlContent.addView(holder.tvPrice);
            RelativeLayout.LayoutParams paramsPrice = (RelativeLayout.LayoutParams) holder.tvPrice.getLayoutParams();
            paramsPrice.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            paramsPrice.height = DensityUtil.getActualHeightOnThisDevice(60, mContext);
            paramsPrice.leftMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
            holder.tvPrice.setLayoutParams(paramsPrice);

            holder.rlContent.addView(holder.tvStartDate);
            RelativeLayout.LayoutParams paramsStartDate = (RelativeLayout.LayoutParams) holder.tvStartDate.getLayoutParams();
            paramsStartDate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsStartDate.height = DensityUtil.getActualHeightOnThisDevice(60, mContext);
            paramsStartDate.leftMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
            paramsStartDate.addRule(RelativeLayout.BELOW, holder.tvPrice.getId());
            holder.tvStartDate.setLayoutParams(paramsStartDate);

            holder.rlContent.addView(holder.tvSeparationDate);
            RelativeLayout.LayoutParams paramsSeparationDate = (RelativeLayout.LayoutParams) holder.tvSeparationDate.getLayoutParams();
            paramsSeparationDate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsSeparationDate.height = DensityUtil.getActualHeightOnThisDevice(60, mContext);
            paramsSeparationDate.leftMargin = DensityUtil.getActualWidthOnThisDevice(5, mContext);
            paramsSeparationDate.addRule(RelativeLayout.RIGHT_OF, holder.tvStartDate.getId());
            paramsSeparationDate.addRule(RelativeLayout.ALIGN_TOP, holder.tvStartDate.getId());
            holder.tvSeparationDate.setLayoutParams(paramsSeparationDate);

            holder.rlContent.addView(holder.tvEndDate);
            RelativeLayout.LayoutParams paramsEndDate = (RelativeLayout.LayoutParams) holder.tvEndDate.getLayoutParams();
            paramsEndDate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsEndDate.height = DensityUtil.getActualHeightOnThisDevice(60, mContext);
            paramsEndDate.leftMargin = DensityUtil.getActualWidthOnThisDevice(5, mContext);
            paramsEndDate.addRule(RelativeLayout.RIGHT_OF, holder.tvSeparationDate.getId());
            paramsEndDate.addRule(RelativeLayout.ALIGN_TOP, holder.tvSeparationDate.getId());
            holder.tvEndDate.setLayoutParams(paramsEndDate);

            holder.rlContent.addView(holder.tvCloseTime);
            RelativeLayout.LayoutParams paramsCloseTime = (RelativeLayout.LayoutParams) holder.tvCloseTime.getLayoutParams();
            paramsCloseTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsCloseTime.height = DensityUtil.getActualHeightOnThisDevice(60, mContext);
            paramsCloseTime.rightMargin = DensityUtil.getActualWidthOnThisDevice(20, mContext);
            paramsCloseTime.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsCloseTime.addRule(RelativeLayout.ALIGN_TOP, holder.tvEndDate.getId());
            holder.tvCloseTime.setLayoutParams(paramsCloseTime);

            holder.rlContent.addView(holder.tvSeparationTime);
            RelativeLayout.LayoutParams paramsSeparationTime = (RelativeLayout.LayoutParams) holder.tvSeparationTime.getLayoutParams();
            paramsSeparationTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsSeparationTime.height = DensityUtil.getActualHeightOnThisDevice(60, mContext);
            paramsSeparationTime.rightMargin = DensityUtil.getActualWidthOnThisDevice(5, mContext);
            paramsSeparationTime.addRule(RelativeLayout.LEFT_OF, holder.tvCloseTime.getId());
            paramsSeparationTime.addRule(RelativeLayout.ALIGN_TOP, holder.tvCloseTime.getId());
            holder.tvSeparationTime.setLayoutParams(paramsSeparationTime);

            holder.rlContent.addView(holder.tvOpenTime);
            RelativeLayout.LayoutParams paramsOpenTime = (RelativeLayout.LayoutParams) holder.tvOpenTime.getLayoutParams();
            paramsOpenTime.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsOpenTime.height = DensityUtil.getActualHeightOnThisDevice(60, mContext);
            paramsOpenTime.rightMargin = DensityUtil.getActualWidthOnThisDevice(5, mContext);
            paramsOpenTime.addRule(RelativeLayout.LEFT_OF, holder.tvSeparationTime.getId());
            paramsOpenTime.addRule(RelativeLayout.ALIGN_TOP, holder.tvSeparationTime.getId());
            holder.tvOpenTime.setLayoutParams(paramsOpenTime);

            holder.tvTopic.setTextColor(mContext.getResources().getColor(R.color.common_blue));
            holder.tvTopic.setTextSize(Constants.FONT_SIZE_LARGER);

            holder.tvStartDate.setTextColor(mContext.getResources().getColor(R.color.common_deep_gray));
            holder.tvEndDate.setTextColor(mContext.getResources().getColor(R.color.common_deep_gray));
            holder.tvSeparationDate.setTextColor(mContext.getResources().getColor(R.color.common_deep_gray));

            holder.tvOpenTime.setTextColor(mContext.getResources().getColor(R.color.common_deep_gray));
            holder.tvCloseTime.setTextColor(mContext.getResources().getColor(R.color.common_deep_gray));
            holder.tvSeparationTime.setTextColor(mContext.getResources().getColor(R.color.common_deep_gray));

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        JsonEventListGet.Event event = eventList.get(position);

        holder.tvTopic.setText(event.getEventName());

        String eventPrice = event.getEventPrize();
        if (StringUtils.isNotEmpty(eventPrice) && !Constants.STR_0.equals(eventPrice)) {
            holder.tvPrice.setText(AppUtils.getCurrentCurrency(mContext) + eventPrice);
        }

        String startDate = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(event.getEventStartDate(), mContext);
        holder.tvStartDate.setText(startDate);

        String endDate = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(event.getEventEndDate(), mContext);
        holder.tvEndDate.setText(endDate);

        String openTime = event.getEventStartTime();
        String closeTime = event.getEventEndTime();

        holder.tvOpenTime.setText(openTime);
        holder.tvCloseTime.setText(closeTime);

        holder.tvSeparationDate.setText(Constants.STR_DOUBLE_SEPARATOR);
        holder.tvSeparationTime.setText(Constants.STR_DOUBLE_SEPARATOR);

        if (StringUtils.isEmpty(openTime) || StringUtils.isEmpty(closeTime)) {
            holder.tvSeparationTime.setVisibility(View.INVISIBLE);
        } else {
            holder.tvSeparationTime.setVisibility(View.VISIBLE);
        }

        if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
            holder.tvSeparationDate.setVisibility(View.INVISIBLE);
        } else {
            holder.tvSeparationDate.setVisibility(View.VISIBLE);
        }

        holder.tvTopic.setTag(position);
        return convertView;
    }

    class ViewHolder {
        LinearLayout llEventsListContainer;
        RelativeLayout rlTopic;
        RelativeLayout rlContent;

        IteeTextView tvTopic;
        IteeTextView tvPrice;
        IteeTextView tvStartDate;
        IteeTextView tvSeparationDate;
        IteeTextView tvEndDate;
        IteeTextView tvOpenTime;
        IteeTextView tvSeparationTime;
        IteeTextView tvCloseTime;
    }


}