package cn.situne.itee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonCourseListInfo;
import cn.situne.itee.view.HorizontalListView;
import cn.situne.itee.view.IteeTextView;

public class CourseTitleListAdapter extends BaseAdapter {

    private CourseColorListAdapter littleColorAdapter;
    private CourseListAdapter littleAdapter;
    private CourseColorTeeAdapter colorTeeAdapter;

    private Context context = null;

    private View.OnClickListener listener;

    private int parOne;
    private int parTwo;
    private int parThree;
    private int parFour;
    private int parFive;
    private int parSix;
    private int parSeven;
    private int parEight;
    private int parNine;

    private int HoleNoOne;
    private int HoleNOTwo;
    private int HoleNOThree;
    private int HoleNOFour;
    private int HoleNOFive;
    private int HoleNOSix;
    private int HoleNOSeven;
    private int HoleNOEight;
    private int HoleNONine;
    private String tvYard;

    private List<JsonCourseListInfo.DataList> dataList;

    private BaseFragment mFragment;

    public CourseTitleListAdapter(List<JsonCourseListInfo.DataList> dataList, Context c, View.OnClickListener listener, BaseFragment baseFragment) {
        this.context = c;
        this.dataList = dataList;
        this.listener = listener;
        this.mFragment = baseFragment;
    }

    @Override
    public int getCount() {
        return dataList.size();
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
            convertView = mInflater.inflate(R.layout.show_course_message_out_item, null);

            viewHolder.outManyColorLvOne = (ListView) convertView.findViewById(R.id.outManyColorLvOne);
            viewHolder.OutHoParHdLvOne = (ListView) convertView.findViewById(R.id.OutHoParHdLvOne);
            viewHolder.rlContainer = (RelativeLayout) convertView.findViewById(R.id.rl_container);
            viewHolder.tvCourseName = new IteeTextView(context);
            viewHolder.tvPar = new IteeTextView(context);
            viewHolder.tvYard = new IteeTextView(context);
            viewHolder.svTee = new HorizontalListView(context, null);
            viewHolder.ivJump = new ImageView(context);
            viewHolder.ivSeparatorLine = (ImageView) convertView.findViewById(R.id.iv_separator_line);
            viewHolder.tvTee = new IteeTextView(context);

            LinearLayout.LayoutParams paramsRlContainer = (LinearLayout.LayoutParams) viewHolder.rlContainer.getLayoutParams();
            paramsRlContainer.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsRlContainer.height = mFragment.getActualHeightOnThisDevice(90);
            viewHolder.rlContainer.setLayoutParams(paramsRlContainer);

            viewHolder.rlContainer.addView(viewHolder.tvCourseName);
            RelativeLayout.LayoutParams paramsTvCourseName = (RelativeLayout.LayoutParams) viewHolder.tvCourseName.getLayoutParams();
            paramsTvCourseName.width = mFragment.getActualWidthOnThisDevice(110);
            paramsTvCourseName.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvCourseName.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsTvCourseName.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramsTvCourseName.leftMargin = mFragment.getActualWidthOnThisDevice(20);
            viewHolder.tvCourseName.setLayoutParams(paramsTvCourseName);
            viewHolder.tvCourseName.setEllipsize(TextUtils.TruncateAt.END);

            viewHolder.rlContainer.addView(viewHolder.ivJump);
            RelativeLayout.LayoutParams paramsIvJump = (RelativeLayout.LayoutParams) viewHolder.ivJump.getLayoutParams();
            paramsIvJump.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsIvJump.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsIvJump.addRule(RelativeLayout.CENTER_VERTICAL);
            paramsIvJump.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsIvJump.rightMargin = mFragment.getActualWidthOnThisDevice(40);
            viewHolder.ivJump.setLayoutParams(paramsIvJump);


            viewHolder.rlContainer.addView(viewHolder.tvPar);
            RelativeLayout.LayoutParams paramsTvPar = (RelativeLayout.LayoutParams) viewHolder.tvPar.getLayoutParams();
            paramsTvPar.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvPar.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvPar.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsTvPar.rightMargin = mFragment.getActualWidthOnThisDevice(80);
            viewHolder.tvPar.setLayoutParams(paramsTvPar);

            viewHolder.rlContainer.addView(viewHolder.tvYard);
            RelativeLayout.LayoutParams paramsTvYard = (RelativeLayout.LayoutParams) viewHolder.tvYard.getLayoutParams();
            paramsTvYard.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvYard.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvYard.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramsTvYard.topMargin = mFragment.getActualHeightOnThisDevice(40);
            paramsTvYard.rightMargin = mFragment.getActualWidthOnThisDevice(80);
            viewHolder.tvYard.setLayoutParams(paramsTvYard);
            viewHolder.tvYard.setId(View.generateViewId());

            viewHolder.rlContainer.addView(viewHolder.svTee);
            RelativeLayout.LayoutParams paramsSvTee = (RelativeLayout.LayoutParams) viewHolder.svTee.getLayoutParams();
            paramsSvTee.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.50);
            paramsSvTee.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsSvTee.addRule(RelativeLayout.LEFT_OF, viewHolder.tvYard.getId());
            paramsSvTee.topMargin = mFragment.getActualHeightOnThisDevice(40);
            paramsSvTee.leftMargin = mFragment.getActualWidthOnThisDevice(80);
            viewHolder.svTee.setLayoutParams(paramsSvTee);

            viewHolder.rlContainer.addView(viewHolder.tvTee);
            RelativeLayout.LayoutParams paramsTvTee = (RelativeLayout.LayoutParams) viewHolder.tvTee.getLayoutParams();
            paramsTvTee.width = mFragment.getActualWidthOnThisDevice(455);
            paramsTvTee.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvTee.addRule(RelativeLayout.LEFT_OF, viewHolder.tvYard.getId());
            paramsTvTee.topMargin = mFragment.getActualHeightOnThisDevice(40);
            paramsTvTee.rightMargin = mFragment.getActualWidthOnThisDevice(20);
            viewHolder.tvTee.setLayoutParams(paramsTvTee);

            viewHolder.ivJump.setBackgroundResource(R.drawable.icon_right_arrow);

            viewHolder.tvTee.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            viewHolder.tvTee.setTextSize(Constants.FONT_SIZE_SMALLER);

//            viewHolder.tvYard.

            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final int p = position;
        viewHolder.rlContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < dataList.get(p).getHoleDataItemList().size(); i++) {
                    parOne = dataList.get(p).getHoleDataItemList().get(0).getPar();
                    parTwo = dataList.get(p).getHoleDataItemList().get(1).getPar();
                    parThree = dataList.get(p).getHoleDataItemList().get(2).getPar();
                    parFour = dataList.get(p).getHoleDataItemList().get(3).getPar();
                    parFive = dataList.get(p).getHoleDataItemList().get(4).getPar();
                    parSix = dataList.get(p).getHoleDataItemList().get(5).getPar();
                    parSeven = dataList.get(p).getHoleDataItemList().get(6).getPar();
                    parEight = dataList.get(p).getHoleDataItemList().get(7).getPar();
                    parNine = dataList.get(p).getHoleDataItemList().get(8).getPar();


                    HoleNoOne = dataList.get(p).getHoleDataItemList().get(0).getHoleNO();
                    HoleNOTwo = dataList.get(p).getHoleDataItemList().get(1).getHoleNO();
                    HoleNOThree = dataList.get(p).getHoleDataItemList().get(2).getHoleNO();
                    HoleNOFour = dataList.get(p).getHoleDataItemList().get(3).getHoleNO();
                    HoleNOFive = dataList.get(p).getHoleDataItemList().get(4).getHoleNO();
                    HoleNOSix = dataList.get(p).getHoleDataItemList().get(5).getHoleNO();
                    HoleNOSeven = dataList.get(p).getHoleDataItemList().get(6).getHoleNO();
                    HoleNOEight = dataList.get(p).getHoleDataItemList().get(7).getHoleNO();
                    HoleNONine = dataList.get(p).getHoleDataItemList().get(8).getHoleNO();

                }

                view.setTag(parOne + Constants.STR_COMMA + parTwo
                                + Constants.STR_COMMA + parThree + Constants.STR_COMMA + parFour
                                + Constants.STR_COMMA + parFive + Constants.STR_COMMA + parSix
                                + Constants.STR_COMMA + parSeven + Constants.STR_COMMA + parEight
                                + Constants.STR_COMMA + parNine + Constants.STR_COMMA + HoleNoOne
                                + Constants.STR_COMMA + HoleNOTwo + Constants.STR_COMMA + HoleNOThree
                                + Constants.STR_COMMA + HoleNOFour + Constants.STR_COMMA + HoleNOFive
                                + Constants.STR_COMMA + HoleNOSix + Constants.STR_COMMA + HoleNOSeven
                                + Constants.STR_COMMA + HoleNOEight + Constants.STR_COMMA + HoleNONine
                                + Constants.STR_COMMA + dataList.get(p).getShowCourseAreaType()
                                + Constants.STR_COMMA + dataList.get(p).getShowCourseAreaId()
                                + Constants.STR_COMMA + tvYard
                );

                listener.onClick(view);

            }
        });


        viewHolder.tvCourseName.setText(dataList.get(position).getShowCourseAreaType());
        viewHolder.tvCourseName.setTextColor(Color.BLUE);
        viewHolder.tvCourseName.setTextSize(Constants.FONT_SIZE_NORMAL);
        viewHolder.tvCourseName.setSingleLine();
        String yard = dataList.get(p).getShowCourseUnit();
        if (Utils.isStringNullOrEmpty(yard)) {
            viewHolder.tvYard.setVisibility(View.INVISIBLE);
        } else {
            tvYard = yard.substring(0, 1).toUpperCase() + yard.substring(1);
            viewHolder.tvYard.setText(tvYard);
            viewHolder.tvYard.setVisibility(View.VISIBLE);
        }

        if (dataList.get(position).getHoleDataItemList().size() == 0) {
            parOne = 0;
            parTwo = 0;
            parThree = 0;
            parFour = 0;
            parFive = 0;
            parSix = 0;
            parSeven = 0;
            parEight = 0;
            parNine = 0;

            HoleNoOne = 0;
            HoleNOTwo = 0;
            HoleNOThree = 0;
            HoleNOFour = 0;
            HoleNOFive = 0;
            HoleNOSix = 0;
            HoleNOSeven = 0;
            HoleNOEight = 0;
            HoleNONine = 0;
        } else {
            parOne = dataList.get(position).getHoleDataItemList().get(0).getPar();
            parTwo = dataList.get(position).getHoleDataItemList().get(1).getPar();
            parThree = dataList.get(position).getHoleDataItemList().get(2).getPar();
            parFour = dataList.get(position).getHoleDataItemList().get(3).getPar();
            parFive = dataList.get(position).getHoleDataItemList().get(4).getPar();
            parSix = dataList.get(position).getHoleDataItemList().get(5).getPar();
            parSeven = dataList.get(position).getHoleDataItemList().get(6).getPar();
            parEight = dataList.get(position).getHoleDataItemList().get(7).getPar();
            parNine = dataList.get(position).getHoleDataItemList().get(8).getPar();
        }

        int parValue = parOne + parTwo + parThree + parFour + parFive + parSix + parSeven + parEight + parNine;
        if (parValue != 0) {
            viewHolder.tvPar.setVisibility(View.VISIBLE);
            viewHolder.tvPar.setText("Par " + String.valueOf(parValue));
        } else {
            viewHolder.tvPar.setVisibility(View.INVISIBLE);
        }

        //item的个数
        JsonCourseListInfo.DataList item = dataList.get(position);
        //获取每个item所对应的Tee
        ArrayList<JsonCourseListInfo.DataList.HoleDataItem> holeDataList = item.getHoleDataItemList();

        if (holeDataList.size() > 0 && holeDataList.get(0).getTeeDataList().size() > 0) {
            viewHolder.ivSeparatorLine.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivSeparatorLine.setVisibility(View.INVISIBLE);
        }

        littleAdapter = new CourseListAdapter(holeDataList, context);
        littleColorAdapter = new CourseColorListAdapter(holeDataList, context);
        colorTeeAdapter = new CourseColorTeeAdapter(holeDataList, context);
        viewHolder.OutHoParHdLvOne.setAdapter(littleAdapter);
        viewHolder.outManyColorLvOne.setAdapter(littleColorAdapter);
        viewHolder.svTee.setAdapter(colorTeeAdapter);
        viewHolder.svTee.setVisibility(View.GONE);

        if (holeDataList.size() > 0) {
            int size = holeDataList.get(0).getTeeDataList().size();
            SpannableStringBuilder res = getYards(holeDataList, size);
            viewHolder.tvTee.setText(res);
        } else {
            viewHolder.tvTee.setText(Constants.STR_EMPTY);
        }

        if (Utils.isStringNullOrEmpty(viewHolder.tvTee.getText().toString())) {
            viewHolder.tvYard.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.tvYard.setVisibility(View.VISIBLE);
        }

        viewHolder.OutHoParHdLvOne.setDivider(null);
        viewHolder.outManyColorLvOne.setDivider(null);

        LayoutUtils.setListViewHeightBasedOnChildren(viewHolder.OutHoParHdLvOne);
        LayoutUtils.setListViewHeightBasedOnChildren(viewHolder.outManyColorLvOne);

        return convertView;
    }

    private String getNonZeroValueWithSpace(int intValue) {
        if (intValue != 0) {
            return String.valueOf(intValue);
        } else {
            return Constants.STR_SPACE;
        }
    }

    private String getNonZeroValueWithEmpty(int intValue) {
        if (intValue != 0) {
            return String.valueOf(intValue);
        } else {
            return Constants.STR_EMPTY;
        }
    }

    private SpannableStringBuilder getYards(ArrayList<JsonCourseListInfo.DataList.HoleDataItem> holeDataList, int size) {
        int[] yards = new int[size];
        for (JsonCourseListInfo.DataList.HoleDataItem item : holeDataList) {
            for (int i = 0; i < item.getTeeDataList().size(); i++) {
                JsonCourseListInfo.DataList.HoleDataItem.TeeDataList teeData = item.getTeeDataList().get(i);
                yards[i] += teeData.getTeeYard();
            }
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (int i = 0; i < size; i++) {
            if (ssb.length() > 0) {
                ssb.append(Constants.STR_SLASH);
            }
            int start = ssb.length();
            ssb.append(String.valueOf(yards[i]));
            if (i % 2 != 0) {
                ssb.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.common_blue)),
                        start, ssb.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return ssb;
    }

    public class ViewHolder {
        public RelativeLayout rlContainer;
        public ListView outManyColorLvOne;
        public ListView OutHoParHdLvOne;
        public IteeTextView tvCourseName;
        public IteeTextView tvPar;
        public IteeTextView tvYard;
        public HorizontalListView svTee;
        public ImageView ivJump;
        public ImageView ivSeparatorLine;

        IteeTextView tvTee;
    }
}
