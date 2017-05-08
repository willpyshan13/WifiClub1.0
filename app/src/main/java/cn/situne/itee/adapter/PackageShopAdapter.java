package cn.situne.itee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.view.IteeTextView;


public class PackageShopAdapter extends BaseAdapter {

    ArrayList<HashMap<String, Object>> dataList;
    private Context context = null;

    public PackageShopAdapter(ArrayList<HashMap<String, Object>> dataList, Context c) {
        this.context = c;
        this.dataList = dataList;

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
            convertView = mInflater.inflate(R.layout.package_shop_item, null);

            viewHolder.rlPackage = (RelativeLayout) convertView.findViewById(R.id.rl_package);

            viewHolder.tvPackageKey = new IteeTextView(context);
            viewHolder.tvPackageValue = new IteeTextView(context);
            viewHolder.tvGreenKey = new IteeTextView(context);
            viewHolder.tvGreenValue = new IteeTextView(context);


            LinearLayout.LayoutParams paramPackage = (LinearLayout.LayoutParams) viewHolder.rlPackage.getLayoutParams();
            paramPackage.height = 90;
            viewHolder.rlPackage.setLayoutParams(paramPackage);

            LinearLayout.LayoutParams paramGreen = (LinearLayout.LayoutParams) viewHolder.rlGreen.getLayoutParams();
            paramGreen.height = 40;
            viewHolder.rlGreen.setLayoutParams(paramGreen);


            viewHolder.rlPackage.addView(viewHolder.tvPackageKey);
            RelativeLayout.LayoutParams paramPackageKey = (RelativeLayout.LayoutParams) viewHolder.tvPackageKey.getLayoutParams();
            paramPackageKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramPackageKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramPackageKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramPackageKey.addRule(RelativeLayout.CENTER_VERTICAL);
            paramPackageKey.leftMargin = 40;
            viewHolder.tvPackageKey.setLayoutParams(paramPackageKey);

            viewHolder.rlPackage.addView(viewHolder.tvPackageValue);
            RelativeLayout.LayoutParams paramPackageValue = (RelativeLayout.LayoutParams) viewHolder.tvPackageValue.getLayoutParams();
            paramPackageValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramPackageValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramPackageValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramPackageValue.addRule(RelativeLayout.CENTER_VERTICAL);
            paramPackageValue.rightMargin = 40;
            viewHolder.tvPackageValue.setLayoutParams(paramPackageValue);

            viewHolder.rlGreen.addView(viewHolder.tvGreenKey);
            RelativeLayout.LayoutParams paramTvGreenKey = (RelativeLayout.LayoutParams) viewHolder.tvGreenKey.getLayoutParams();
            paramTvGreenKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramTvGreenKey.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramTvGreenKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            paramTvGreenKey.addRule(RelativeLayout.CENTER_VERTICAL);
            paramTvGreenKey.leftMargin = 40;
            viewHolder.tvGreenKey.setLayoutParams(paramTvGreenKey);

            viewHolder.rlGreen.addView(viewHolder.tvGreenValue);
            RelativeLayout.LayoutParams paramTvGreenValue = (RelativeLayout.LayoutParams) viewHolder.tvGreenValue.getLayoutParams();
            paramTvGreenValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramTvGreenValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramTvGreenValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            paramTvGreenValue.addRule(RelativeLayout.CENTER_VERTICAL);
            paramTvGreenValue.rightMargin = 40;
            viewHolder.tvGreenValue.setLayoutParams(paramTvGreenValue);


            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvPackageKey.setText(dataList.get(position).get("nameKey").toString());
        viewHolder.tvPackageKey.setTextColor(Color.BLACK);
        viewHolder.tvPackageKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        viewHolder.tvPackageValue.setText(dataList.get(position).get("nameValue").toString());
        viewHolder.tvPackageValue.setTextColor(Color.BLACK);
        viewHolder.tvPackageValue.setTextSize(Constants.FONT_SIZE_NORMAL);

        viewHolder.tvGreenKey.setText(dataList.get(position).get("greenKey").toString());
        viewHolder.tvGreenKey.setTextColor(Color.BLACK);
        viewHolder.tvGreenKey.setTextSize(Constants.FONT_SIZE_NORMAL);

        viewHolder.tvGreenValue.setText(dataList.get(position).get("greenValue").toString());
        viewHolder.tvGreenValue.setTextColor(Color.BLACK);
        viewHolder.tvGreenValue.setTextSize(Constants.FONT_SIZE_NORMAL);


        return convertView;
    }

    public class ViewHolder {
        public RelativeLayout rlPackage;
        public RelativeLayout rlGreen;


        public IteeTextView tvPackageKey;
        public IteeTextView tvPackageValue;
        public IteeTextView tvGreenKey;
        public IteeTextView tvGreenValue;

    }
}
