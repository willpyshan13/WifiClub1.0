package cn.situne.itee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SlidingMenuItem;

public class SlidingMenuAdapter extends ArrayAdapter<SlidingMenuItem> {

    private View currentSelectedView;
    private int currentSelectedIdx;
    private Context mContext;

    private String configFlag;
    public SlidingMenuAdapter(Context context,String configFlag) {
        super(context, 0);
        mContext = context;
        this.configFlag = configFlag;
        currentSelectedIdx = -1;
    }



    public View getView(int position, View convertView, ViewGroup parent) {
        SlidingMenuItem smi = getItem(position);
        IteeTextView tvMenuText;
        ImageView ivMark;
        if (convertView == null) {
            if (smi.getMenuTextResourceId() == R.string.menu_separator) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_sliding_menu_separator, null);
                ImageView ivSeparator = (ImageView) convertView.findViewById(R.id.iv_menu_separator);
                LinearLayout.LayoutParams paramsIvSeparator = (LinearLayout.LayoutParams) ivSeparator.getLayoutParams();
                paramsIvSeparator.height = 10;
                paramsIvSeparator.width = ViewGroup.LayoutParams.MATCH_PARENT;
                ivSeparator.setLayoutParams(paramsIvSeparator);
            } else {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row_sliding_menu, null);
                tvMenuText = (IteeTextView) convertView.findViewById(R.id.tv_menu_text);
                tvMenuText.setText(smi.getMenuTextResourceId());

                RelativeLayout.LayoutParams paramsTvMenuText = (RelativeLayout.LayoutParams) tvMenuText.getLayoutParams();
                paramsTvMenuText.height = smi.getRowHeight();
                tvMenuText.setLayoutParams(paramsTvMenuText);

                ivMark = (ImageView) convertView.findViewById(R.id.iv_menu_selected_mark);
                RelativeLayout.LayoutParams paramsIvMark = (RelativeLayout.LayoutParams) ivMark.getLayoutParams();
                paramsIvMark.height = smi.getRowHeight();
                ivMark.setLayoutParams(paramsIvMark);

                if (currentSelectedIdx == -1) {

                    if (Constants.STR_0.equals(this.configFlag)){

                        if (smi.getMenuId() == Constants.MENU_ID_TEE_TIMES) {
                            ImageView ivSelectedMark = (ImageView) convertView.findViewById(R.id.iv_menu_selected_mark);
                            ivSelectedMark.setBackgroundColor(convertView.getResources().getColor(R.color.menu_selected_mark));
                            convertView.setBackgroundResource(R.drawable.menu_item_selected_bg);
                            currentSelectedIdx = 0;
                            currentSelectedView = convertView;
                        }
                    }else{

                        if (smi.getMenuId() == Constants.MENU_ID_ADMINISTRATION) {
                            ImageView ivSelectedMark = (ImageView) convertView.findViewById(R.id.iv_menu_selected_mark);
                            ivSelectedMark.setBackgroundColor(convertView.getResources().getColor(R.color.menu_selected_mark));
                            convertView.setBackgroundResource(R.drawable.menu_item_selected_bg);
                            currentSelectedIdx = 0;
                            currentSelectedView = convertView;
                        }

                    }


                }
            }
        }
        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        SlidingMenuItem smi = getItem(position);
        return smi.getMenuTextResourceId() != R.string.menu_separator;
    }

    public void configMenuItems() {
        int rowHeight = DensityUtil.getActualHeightOnThisDevice(88, mContext);

        int[] menus = new int[]{
                Constants.MENU_ID_TEE_TIMES,
                Constants.MENU_ID_EVENTS,
                Constants.MENU_ID_SHOPS,
                Constants.MENU_ID_CUSTOMERS,
                Constants.MENU_ID_AGENTS,
                Constants.MENU_ID_STAFF,
                Constants.MENU_ID_CHARTS,
                Constants.MENU_ID_ADMINISTRATION,
                Constants.MENU_ID_SCAN_QR_CODE};
        for (int i = 0; i < menus.length; i++) {
            if (!AppUtils.isAgent(mContext)) {
                if (i != 0 && i % 3 == 0) {
                    SlidingMenuItem itemSeparatorShopsAndCustomers = new SlidingMenuItem(0, rowHeight / 4, R.string.menu_separator);
                    add(itemSeparatorShopsAndCustomers);
                }
            }

            boolean isShow = true;
            if (Constants.MENU_ID_SHOPS == menus[i]) {
                isShow = AppUtils.getAuth(AppUtils.AuthControl.AuthControlMenuControlShops, mContext) && !AppUtils.isAgent(mContext);
            }
            if (Constants.MENU_ID_EVENTS == menus[i]) {
                isShow = AppUtils.getAuth(AppUtils.AuthControl.AuthControlMenuEvents, mContext) && !AppUtils.isAgent(mContext);
            }
            if (Constants.MENU_ID_AGENTS == menus[i]) {
                isShow = AppUtils.getAuth(AppUtils.AuthControl.AuthControlMenuAgents, mContext) && !AppUtils.isAgent(mContext);
            }
            if (Constants.MENU_ID_STAFF == menus[i]) {
                isShow = AppUtils.getAuth(AppUtils.AuthControl.AuthControlMenuControlStaffs, mContext) && !AppUtils.isAgent(mContext);
            }
            if (Constants.MENU_ID_ADMINISTRATION == menus[i]) {
                isShow = AppUtils.getAuth(AppUtils.AuthControl.AuthControlMenuAdministration, mContext) && !AppUtils.isAgent(mContext);
            }
            if (Constants.MENU_ID_CUSTOMERS == menus[i]) {
                isShow = !AppUtils.isAgent(mContext);
            }
            //fix syb.
            if (Constants.MENU_ID_CHARTS == menus[i]) {
                isShow = !AppUtils.isAgent(mContext);
            }
            if (Constants.MENU_ID_SCAN_QR_CODE == menus[i]) {
                isShow = !AppUtils.isAgent(mContext);
            }
            if (isShow) {
                SlidingMenuItem smi = new SlidingMenuItem(menus[i], rowHeight, getMenuStringId(menus[i]));
                add(smi);
            }
        }
    }

    private int getMenuStringId(int menuId) {
        int res = -1;

        switch (menuId) {
            case Constants.MENU_ID_TEE_TIMES:
                res = R.string.menu_tee_times;
                break;
            case Constants.MENU_ID_EVENTS:
                res = R.string.menu_events;
                break;
            case Constants.MENU_ID_SHOPS:
                res = R.string.menu_shops;
                break;
            case Constants.MENU_ID_CUSTOMERS:
                res = R.string.menu_customers;
                break;
            case Constants.MENU_ID_AGENTS:
                res = R.string.menu_agents;
                break;
            case Constants.MENU_ID_STAFF:
                res = R.string.menu_staff;
                break;
            case Constants.MENU_ID_NEWS:
                res = R.string.menu_news;
                break;
            case Constants.MENU_ID_CHARTS:
                res = R.string.menu_charts;
                break;
            case Constants.MENU_ID_ADMINISTRATION:
                res = R.string.menu_administration;
                break;
            case Constants.MENU_ID_SCAN_QR_CODE:
                res = R.string.menu_scan_qr_code;
                break;
        }
        return res;
    }

    public int getCurrentSelectedIdx() {
        return currentSelectedIdx;
    }

    public void setCurrentSelectedIdx(int currentSelectedIdx) {
        this.currentSelectedIdx = currentSelectedIdx;
    }

    public View getCurrentSelectedView() {
        return currentSelectedView;
    }

    public void setCurrentSelectedView(View currentSelectedView) {
        this.currentSelectedView = currentSelectedView;
    }
}

