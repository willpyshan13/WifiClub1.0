package cn.situne.itee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.manager.jsonentity.JsonCourseListInfo;
import cn.situne.itee.view.IteeTextView;

public class CourseColorTeeAdapter extends BaseAdapter {

    List<JsonCourseListInfo.DataList.HoleDataItem> dataList;
    Map<String, List<Object>> dataMap;
    List<HashMap<String, Object>> mapList;
    private Context context = null;
    private List<Object> nameList;

    public CourseColorTeeAdapter(List<JsonCourseListInfo.DataList.HoleDataItem> dataList, Context c) {
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
        return nameList.get(position);
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
            convertView = mInflater.inflate(R.layout.show_course_tee_item, null);

            viewHolder.rlTee = (RelativeLayout) convertView.findViewById(R.id.rl_tee);
            viewHolder.tvTee = new IteeTextView(context);

            viewHolder.rlTee.addView(viewHolder.tvTee);
            RelativeLayout.LayoutParams paramsTvTee = (RelativeLayout.LayoutParams) viewHolder.tvTee.getLayoutParams();
            paramsTvTee.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            paramsTvTee.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvTee.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
            viewHolder.tvTee.setLayoutParams(paramsTvTee);
            viewHolder.tvTee.setSingleLine();

            viewHolder.tvTee.setTextSize(Constants.FONT_SIZE_MORE_SMALLER);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        int totalOne = Integer.parseInt((String) mapList.get(position).get("holeOne"));
        int totalTwo = Integer.parseInt((String) mapList.get(position).get("holeTwo"));
        int totalThree = Integer.parseInt((String) mapList.get(position).get("holeThree"));
        int totalFour = Integer.parseInt((String) mapList.get(position).get("holeFour"));
        int totalFive = Integer.parseInt((String) mapList.get(position).get("holeFive"));
        int totalSix = Integer.parseInt((String) mapList.get(position).get("holeSix"));
        int totalSeven = Integer.parseInt((String) mapList.get(position).get("holeSeven"));
        int totalEight = Integer.parseInt((String) mapList.get(position).get("holeEight"));
        int totalNine = Integer.parseInt((String) mapList.get(position).get("holeNine"));
        int totalTee = totalOne + totalTwo + totalThree + totalFour + totalFive + totalSix + totalSeven + totalEight + totalNine;

        if (position % 2 != 0) {
            viewHolder.tvTee.setTextColor(Color.BLUE);
        }

        if (position == nameList.size() - 1) {
            viewHolder.tvTee.setText(String.valueOf(totalTee));
        } else {
            viewHolder.tvTee.setText(String.valueOf(totalTee + "/"));
        }


        return convertView;
    }

    public class ViewHolder {
        public RelativeLayout rlTee;
        public IteeTextView tvTee;

    }
}
