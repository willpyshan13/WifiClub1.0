/**
 * Project Name: itee
 * File Name:  SelectSharePopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-19
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.view.HorizontalListView;
import cn.situne.itee.view.HorizontalListViewAdapter;

/**
 * ClassName:SelectSharePopupWindow <br/>
 * Function: select share type popup window. <br/>
 * Date: 2015-01-29 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectSharePopupWindow extends BasePopWindow {

    private View menuView;
    private Context mContext;

    public SelectSharePopupWindow(Context context) {
        super(context);
        this.mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_share, null);
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);

        formatViews();

        setHideListener(menuView);
        initView();
    }

    private void initView() {
        HorizontalListView tvHorizontalListView = (HorizontalListView) menuView.findViewById(R.id.pop_horizontal_listview);
        List<ResolveInfo> dataList = getShareApps(mContext);
        HorizontalListViewAdapter adapter = new HorizontalListViewAdapter(mContext, dataList);
        tvHorizontalListView.setAdapter(adapter);
    }

    public List<ResolveInfo> getShareApps(Context context) {
        Intent intent = new Intent(Intent.ACTION_SEND, null);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("text/plain");
        PackageManager pManager = context.getPackageManager();
        return pManager.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
    }

}
