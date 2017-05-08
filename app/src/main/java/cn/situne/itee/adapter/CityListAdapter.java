package cn.situne.itee.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.view.IteeTextView;


public class CityListAdapter extends BaseAdapter {
    private final static String KEY_NAME = "name";
    private final static String KEY_COUNTRY = "country";
    private ArrayList<HashMap<String, Object>> dataSource;
    private Context context;
    private AlphabetIndexer indexer;
    private Map<String, Object> city;

    /**
     * @param entity  entity
     * @param context context
     * @param indexer indexer
     */
    public CityListAdapter(ArrayList<HashMap<String, Object>> entity,
                           Context context,
                           AlphabetIndexer indexer) {
        super();
        this.dataSource = entity;
        this.context = context;
        this.indexer = indexer;
    }

    @Override
    public int getCount() {
        return dataSource.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        Log.e("aaa", "aaaaaa");
        return dataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        city = dataSource.get(position);
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_row_city, null);
            holder = new ViewHolder();
            holder.tvLetter = (IteeTextView) convertView.findViewById(R.id.tvLetter);
            holder.tvChineseName = (IteeTextView) convertView.findViewById(R.id.tv_city_chinese_name);
            holder.tvEnglishName = (IteeTextView) convertView.findViewById(R.id.tv_city_english_name);
            holder.rlItem = (RelativeLayout) convertView.findViewById(R.id.rl_member_item_all);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvEnglishName.setText((CharSequence) city.get(KEY_NAME));
        holder.tvChineseName.setText((CharSequence) city.get(KEY_COUNTRY));

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

        return convertView;
    }

    class ViewHolder {
        IteeTextView tvLetter;
        IteeTextView tvEnglishName;
        IteeTextView tvChineseName;
        RelativeLayout rlItem;
    }

}
