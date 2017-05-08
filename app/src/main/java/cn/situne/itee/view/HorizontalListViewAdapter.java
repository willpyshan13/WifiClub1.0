package cn.situne.itee.view;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.situne.itee.R;

/**
 * HorizontalListViewAdapter
 *
 * @author song
 */

public class HorizontalListViewAdapter extends BaseAdapter {

    private List<ResolveInfo> dataList;
    private Context context;

    public HorizontalListViewAdapter(Context con, List<ResolveInfo> dataList) {
        this.context = con;
        this.dataList = dataList;
        mInflater = LayoutInflater.from(con);
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    private LayoutInflater mInflater;

    @Override
    public Object getItem(int position) {
        return position;
    }


    private static class ViewHolder {
        private TextView message;
        private ImageView im;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResolveInfo resolveInfo = dataList.get(position);
        ViewHolder vh = null;
        PackageManager packageManager = context.getPackageManager();

        if (convertView == null) {
            vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.horizontallistview_item, null);
            vh.im = (ImageView) convertView.findViewById(R.id.iv_pic);
            vh.message = (TextView) convertView.findViewById(R.id.tv_message);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

//            appInfo.setAppPkgName(resolveInfo.activityInfo.packageName);
////              showLog_I(TAG, "pkg>" + resolveInfo.activityInfo.packageName + ";name>" + resolveInfo.activityInfo.name);
//            appInfo.setAppLauncherClassName(resolveInfo.activityInfo.name);
//
//            appInfo.setAppName(resolveInfo.loadLabel(packageManager).toString());
//            appInfo.setAppIcon(resolveInfo.loadIcon(packageManager));
//            shareAppInfos.add(appInfo);
        vh.im.setImageDrawable(resolveInfo.loadIcon(packageManager));
        vh.message.setText(resolveInfo.loadLabel(packageManager).toString());
        return convertView;
    }


}