package cn.situne.itee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.situne.itee.R;
import cn.situne.itee.common.utils.Utils;

/**
 * HorizontalListViewAdapter
 *
 * @author song
 */

public class NewsListItemAdapter extends BaseAdapter {

    private Context context;
    private int actionBarHeight;

    public NewsListItemAdapter(Context con, int actionBarHeight) {
        this.context = con;
        this.actionBarHeight = actionBarHeight;
        mInflater = LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return 2;
    }

    private LayoutInflater mInflater;

    @Override
    public Object getItem(int position) {
        return position;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(R.layout.news_list_item, null);
        convertView.setMinimumHeight((int) ((Utils.getHeight(context) - actionBarHeight) * 0.5f));
//        ViewGroup.LayoutParams paramsIncomingTitle =  convertView.getLayoutParams();
//        paramsIncomingTitle.height = (int) ( Utils.getActualHeightOnThisDevice(context) * 0.5f);
//        convertView.setLayoutParams(paramsIncomingTitle);
        return convertView;
    }


}