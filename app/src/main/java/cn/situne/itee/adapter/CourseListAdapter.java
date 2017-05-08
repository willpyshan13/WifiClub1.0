package cn.situne.itee.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.manager.jsonentity.JsonCourseListInfo;
import cn.situne.itee.view.IteeTextView;

public class CourseListAdapter extends BaseAdapter {

    List<JsonCourseListInfo.DataList.HoleDataItem> dataList;
    private Context context = null;

    public CourseListAdapter(List<JsonCourseListInfo.DataList.HoleDataItem> dataList, Context c) {
        this.context = c;
        this.dataList = dataList;

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            LayoutInflater mInflater = LayoutInflater.from(context);
            convertView = mInflater.inflate(R.layout.show_course_message_in_item, null);

            viewHolder.messageTv = (IteeTextView) convertView.findViewById(R.id.messageTv);
            viewHolder.messageOneTv = (IteeTextView) convertView.findViewById(R.id.messageOneTv);
            viewHolder.messageTwoTv = (IteeTextView) convertView.findViewById(R.id.messageTwoTv);
            viewHolder.messageThreeTv = (IteeTextView) convertView.findViewById(R.id.messageThreeTv);
            viewHolder.messageFourTv = (IteeTextView) convertView.findViewById(R.id.messageFourTv);
            viewHolder.messageFiveTv = (IteeTextView) convertView.findViewById(R.id.messageFiveTv);
            viewHolder.messageSixTv = (IteeTextView) convertView.findViewById(R.id.messageSixTv);
            viewHolder.messageSevenTv = (IteeTextView) convertView.findViewById(R.id.messageSevenTv);
            viewHolder.messageEightTv = (IteeTextView) convertView.findViewById(R.id.messageEightTv);
            viewHolder.messageNineTv = (IteeTextView) convertView.findViewById(R.id.messageNineTv);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        IteeTextView[] textViews = new IteeTextView[]{
                viewHolder.messageOneTv,
                viewHolder.messageTwoTv,
                viewHolder.messageThreeTv,
                viewHolder.messageFourTv,
                viewHolder.messageFiveTv,
                viewHolder.messageSixTv,
                viewHolder.messageSevenTv,
                viewHolder.messageEightTv,
                viewHolder.messageNineTv};

        if (position == 0) {
            viewHolder.messageTv.setText("Hole");
            for (int i = 0; i < dataList.size(); i++) {
                for (int j = 0; j < textViews.length; j++) {
                    Integer holeValue = dataList.get(j).getHoleNO();
                    if (holeValue != null && holeValue != 0) {
                        textViews[j].setVisibility(View.VISIBLE);
                        textViews[j].setText(String.valueOf(holeValue));
                        textViews[j].setTextSize(Constants.FONT_SIZE_SMALLER);
                    } else {
                        textViews[j].setVisibility(View.INVISIBLE);
                    }
                }
            }
        } else {
            viewHolder.messageTv.setText("Par");
            for (int i = 0; i < dataList.size(); i++) {
                for (int j = 0; j < textViews.length; j++) {
                    Integer parValue = dataList.get(j).getPar();
                    if (parValue != null && parValue != 0) {
                        textViews[j].setVisibility(View.VISIBLE);
                        textViews[j].setText(String.valueOf(parValue));
                        textViews[j].setTextSize(Constants.FONT_SIZE_SMALLER);
                    } else {
                        textViews[j].setVisibility(View.INVISIBLE);
                    }
                }
            }
        }

        viewHolder.messageTv.setGravity(Gravity.CENTER);
        viewHolder.messageOneTv.setGravity(Gravity.CENTER);
        viewHolder.messageTwoTv.setGravity(Gravity.CENTER);
        viewHolder.messageThreeTv.setGravity(Gravity.CENTER);
        viewHolder.messageFourTv.setGravity(Gravity.CENTER);
        viewHolder.messageFiveTv.setGravity(Gravity.CENTER);
        viewHolder.messageSixTv.setGravity(Gravity.CENTER);
        viewHolder.messageSevenTv.setGravity(Gravity.CENTER);
        viewHolder.messageEightTv.setGravity(Gravity.CENTER);
        viewHolder.messageNineTv.setGravity(Gravity.CENTER);

        return convertView;
    }

    public class ViewHolder {
        public IteeTextView messageTv;
        public IteeTextView messageOneTv;
        public IteeTextView messageTwoTv;
        public IteeTextView messageThreeTv;
        public IteeTextView messageFourTv;
        public IteeTextView messageFiveTv;
        public IteeTextView messageSixTv;
        public IteeTextView messageSevenTv;
        public IteeTextView messageEightTv;
        public IteeTextView messageNineTv;
    }
}
