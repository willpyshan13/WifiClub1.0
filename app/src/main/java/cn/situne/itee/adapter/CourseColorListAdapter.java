package cn.situne.itee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.manager.jsonentity.JsonCourseListInfo;
import cn.situne.itee.view.IteeTextView;

public class CourseColorListAdapter extends BaseAdapter {

    List<JsonCourseListInfo.DataList.HoleDataItem> dataList;
    Map<String, List<Object>> dataMap;
    List<HashMap<String, Object>> mapList;
    private Context context = null;
    private List<Object> nameList;

    public CourseColorListAdapter(List<JsonCourseListInfo.DataList.HoleDataItem> dataList, Context c) {
        this.context = c;
        this.dataList = dataList;
        dataMap = new HashMap<>();
        mapList = new ArrayList<>();
        getData();

    }

    private void getData() {
        nameList = new ArrayList<>();
        for (int i = 0; i < dataList.get(0).getTeeDataList().size(); i++) {
            nameList.add(dataList.get(0).getTeeDataList().get(i).getTeeName());
        }
        dataMap.put("nameList", nameList);


        List<Object> holeOneList = new ArrayList<>();
        for (int i = 0; i < dataList.get(0).getTeeDataList().size(); i++) {
            holeOneList.add(dataList.get(0).getTeeDataList().get(i).getTeeYard());
        }
        dataMap.put("holeOneList", holeOneList);


        List<Object> holeTwoList = new ArrayList<>();
        for (int i = 0; i < dataList.get(1).getTeeDataList().size(); i++) {


            holeTwoList.add(dataList.get(1).getTeeDataList().get(i).getTeeYard());
        }
        dataMap.put("holeTwoList", holeTwoList);


        List<Object> holeThreeList = new ArrayList<>();
        for (int i = 0; i < dataList.get(2).getTeeDataList().size(); i++) {


            holeThreeList.add(dataList.get(2).getTeeDataList().get(i).getTeeYard());
        }
        dataMap.put("holeThreeList", holeThreeList);


        List<Object> holeFourList = new ArrayList<>();
        for (int i = 0; i < dataList.get(3).getTeeDataList().size(); i++) {


            holeFourList.add(dataList.get(3).getTeeDataList().get(i).getTeeYard());
        }
        dataMap.put("holeFourList", holeFourList);

        List<Object> holeFiveList = new ArrayList<>();
        for (int i = 0; i < dataList.get(4).getTeeDataList().size(); i++) {


            holeFiveList.add(dataList.get(4).getTeeDataList().get(i).getTeeYard());
        }
        dataMap.put("holeFiveList", holeFiveList);

        List<Object> holeSixList = new ArrayList<>();
        for (int i = 0; i < dataList.get(5).getTeeDataList().size(); i++) {


            holeSixList.add(dataList.get(5).getTeeDataList().get(i).getTeeYard());
        }
        dataMap.put("holeSixList", holeSixList);


        List<Object> holeSevenList = new ArrayList<>();
        for (int i = 0; i < dataList.get(6).getTeeDataList().size(); i++) {

            holeSevenList.add(dataList.get(6).getTeeDataList().get(i).getTeeYard());
        }
        dataMap.put("holeSevenList", holeSevenList);


        List<Object> holeEightList = new ArrayList<>();
        for (int i = 0; i < dataList.get(7).getTeeDataList().size(); i++) {

            holeEightList.add(dataList.get(7).getTeeDataList().get(i).getTeeYard());
        }
        dataMap.put("holeEightList", holeEightList);

        List<Object> holeNineList = new ArrayList<>();
        for (int i = 0; i < dataList.get(8).getTeeDataList().size(); i++) {

            holeNineList.add(dataList.get(8).getTeeDataList().get(i).getTeeYard());
        }
        dataMap.put("holeNineList", holeNineList);


        for (int i = 0; i < nameList.size(); i++) {


            HashMap<String, Object> map = new HashMap<>();

            map.put("name", nameList.get(i));
            map.put("holeOne", dataMap.get("holeOneList").get(i).toString());
            map.put("holeTwo", dataMap.get("holeTwoList").get(i).toString());
            map.put("holeThree", dataMap.get("holeThreeList").get(i).toString());
            map.put("holeFour", dataMap.get("holeFourList").get(i).toString());
            map.put("holeFive", dataMap.get("holeFiveList").get(i).toString());
            map.put("holeSix", dataMap.get("holeSixList").get(i).toString());
            map.put("holeSeven", dataMap.get("holeSevenList").get(i).toString());
            map.put("holeEight", dataMap.get("holeEightList").get(i).toString());
            map.put("holeNine", dataMap.get("holeNineList").get(i).toString());

            mapList.add(map);
        }


    }


    @Override
    public int getCount() {
        return nameList.size();
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
        //Map<String,>

        viewHolder.messageTv.setText(((mapList.get(position).get("name"))).toString());
        viewHolder.messageOneTv.setText(((mapList.get(position).get("holeOne"))).toString());
        viewHolder.messageTwoTv.setText(((mapList.get(position).get("holeTwo"))).toString());
        viewHolder.messageThreeTv.setText(((mapList.get(position).get("holeThree"))).toString());
        viewHolder.messageFourTv.setText(((mapList.get(position).get("holeFour"))).toString());
        viewHolder.messageFiveTv.setText(((mapList.get(position).get("holeFive"))).toString());
        viewHolder.messageSixTv.setText(((mapList.get(position).get("holeSix"))).toString());
        viewHolder.messageSevenTv.setText(((mapList.get(position).get("holeSeven"))).toString());
        viewHolder.messageEightTv.setText(((mapList.get(position).get("holeEight"))).toString());
        viewHolder.messageNineTv.setText(((mapList.get(position).get("holeNine"))).toString());

        viewHolder.messageTv.setSingleLine();
        viewHolder.messageOneTv.setSingleLine();
        viewHolder.messageTwoTv.setSingleLine();
        viewHolder.messageThreeTv.setSingleLine();
        viewHolder.messageFourTv.setSingleLine();
        viewHolder.messageFiveTv.setSingleLine();
        viewHolder.messageSixTv.setSingleLine();
        viewHolder.messageSevenTv.setSingleLine();
        viewHolder.messageEightTv.setSingleLine();
        viewHolder.messageNineTv.setSingleLine();

        viewHolder.messageTv.setTextSize(Constants.FONT_SIZE_SMALLER);
        viewHolder.messageOneTv.setTextSize(Constants.FONT_SIZE_SMALLER);
        viewHolder.messageTwoTv.setTextSize(Constants.FONT_SIZE_SMALLER);
        viewHolder.messageThreeTv.setTextSize(Constants.FONT_SIZE_SMALLER);
        viewHolder.messageFourTv.setTextSize(Constants.FONT_SIZE_SMALLER);
        viewHolder.messageFiveTv.setTextSize(Constants.FONT_SIZE_SMALLER);
        viewHolder.messageSixTv.setTextSize(Constants.FONT_SIZE_SMALLER);
        viewHolder.messageSevenTv.setTextSize(Constants.FONT_SIZE_SMALLER);
        viewHolder.messageEightTv.setTextSize(Constants.FONT_SIZE_SMALLER);
        viewHolder.messageNineTv.setTextSize(Constants.FONT_SIZE_SMALLER);

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


        if (position % 2 != 0) {
            viewHolder.messageTv.setTextColor(Color.BLUE);
            viewHolder.messageOneTv.setTextColor(Color.BLUE);
            viewHolder.messageTwoTv.setTextColor(Color.BLUE);
            viewHolder.messageThreeTv.setTextColor(Color.BLUE);
            viewHolder.messageFourTv.setTextColor(Color.BLUE);
            viewHolder.messageFiveTv.setTextColor(Color.BLUE);
            viewHolder.messageSixTv.setTextColor(Color.BLUE);
            viewHolder.messageSevenTv.setTextColor(Color.BLUE);
            viewHolder.messageEightTv.setTextColor(Color.BLUE);
            viewHolder.messageNineTv.setTextColor(Color.BLUE);
        }


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
