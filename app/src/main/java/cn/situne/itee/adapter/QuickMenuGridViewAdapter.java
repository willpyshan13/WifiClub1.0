/**
 * Project Name: itee
 * File Name:  QuickMenuGridViewAdapter.java
 * Package Name: cn.situne.itee.adapter
 * Date:   2015-04-24
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ScaleXSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.quick.WelcomeFragment;
import cn.situne.itee.manager.jsonentity.JsonEntry;
import cn.situne.itee.view.IteeTextView;

/**
 * ClassName:QuickMenuGridViewAdapter <br/>
 * Function: QuickMenuGridViewAdapter. <br/>
 * Date: 2015-04-24 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class QuickMenuGridViewAdapter extends BaseAdapter {

    private Context context;

    private ArrayList<Drawable> iconList = new ArrayList<Drawable>();

    private ArrayList<String> contentList = new ArrayList<String>();

    private ArrayList<Boolean> isEdit = new ArrayList<Boolean>();

    private int checkStatus;

    private ArrayList<JsonEntry.EntryList>  entryList;

    public QuickMenuGridViewAdapter(Context context, ArrayList<JsonEntry.EntryList>  entryList, boolean isSingle) {
        this.context = context;
        checkStatus = entryList.get(0).getCheckStatus();

        this.entryList = entryList;

        switch (checkStatus) {
            //未开卡
            case 1:
                contentList.add(context.getResources().getString(R.string.quick_check_in));
                iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_check_in));
                iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_shopping_gray));
                iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_check_out_gray));
                isEdit.add(true);
                isEdit.add(false);
                isEdit.add(false);
                break;
            //已开卡
            case 2:
                contentList.add(context.getResources().getString(R.string.quick_undo_check_in));
                iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_check_in));
                iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_shopping));
                iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_check_out));
                isEdit.add(true);
                isEdit.add(true);
                isEdit.add(true);
                break;

            //已结账
            case 3:
                contentList.add(context.getResources().getString(R.string.quick_undo_check_in));
                iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_check_in_gray));
                iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_shopping_gray));
                iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_check_out_gray));
                isEdit.add(false);
                isEdit.add(false);
                isEdit.add(false);
                break;

            default:
                break;

        }
        contentList.add(context.getResources().getString(R.string.quick_shopping));
        contentList.add(context.getResources().getString(R.string.quick_check_out));

        contentList.add(context.getResources().getString(R.string.quick_recharge));
        contentList.add(context.getResources().getString(R.string.quick_purchase_history));
        contentList.add(context.getResources().getString(R.string.quick_reservations));

        if (isSingle && Utils.isStringNotNullOrEmpty(entryList.get(0).getMemberId()) && Integer.valueOf(entryList.get(0).getMemberId()) > 0) {
            iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_recharge));
            iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_purchase_history));
            iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_reservations));
            isEdit.add(true);
            isEdit.add(true);
            isEdit.add(true);
        } else {
            iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_recharge_gray));
            iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_purchase_history_gray));
            iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_reservations_gray));
            isEdit.add(false);
            isEdit.add(false);
            isEdit.add(false);
        }

        if (Utils.isStringNotNullOrEmpty(entryList.get(0).getMemberId()) && Integer.valueOf(entryList.get(0).getMemberId()) > 0) {
            contentList.add(context.getResources().getString(R.string.quick_customer_info));
        } else {
            contentList.add(context.getResources().getString(R.string.quick_add_a_profile));
        }
        iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_customer_info));
        isEdit.add(true);



        if (entryList.size() == 1){
            if (Constants.NFC_BIND_STATUS_YES.equals(entryList.get(0).getBagCardStatus())){

                if (entryList.get(0).isBagCardIsNowUnBind()){
                    iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_bag_gray));
                    isEdit.add(false);
                }else{
                    iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_bag));
                    isEdit.add(true);
                }
                contentList.add(context.getResources().getString(R.string.quick_bag_unbind));
            }

            if (Constants.NFC_BIND_STATUS_YES.equals(entryList.get(0).getCaddieCardStatus())){


                if (entryList.get(0).isCaddieCardIsNowUnBind()){
                    iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_caddie_gray));
                    isEdit.add(false);
                }else{
                    iconList.add(context.getResources().getDrawable(R.drawable.icon_quick_caddie));
                    isEdit.add(true);
                }

                String caddieStr = context.getResources().getString(R.string.quick_caddie_unbind);

                contentList.add(caddieStr);

            }


        }


    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    @Override
    public int getCount() {
        return iconList.size();
    }

    @Override
    public Object getItem(int position) {
        return iconList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_of_fragment_quick_welcome_gradview, null);
            holder = new ViewHolder();
            holder.icon = (ImageView) convertView.findViewById(R.id.iv_icon_menu);
            holder.content = (IteeTextView) convertView.findViewById(R.id.tv_menu);

            holder.content.setSingleLine(false);
            holder.content.setGravity(Gravity.CENTER);

            ListView.LayoutParams layoutParams
                    = new ListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.getActualHeightOnThisDevice(200, context));

            convertView.setLayoutParams(layoutParams);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.icon.setBackground(iconList.get(position));
        holder.content.setText(contentList.get(position));
        if (!isEdit.get(position)) {
            holder.content.setTextColor(context.getResources().getColor(R.color.common_gray));
        }

        holder.content.setTag(isEdit.get(position));

        if (entryList.size() == 1){
            if (position == 7){
                if (Constants.NFC_BIND_STATUS_YES.equals(entryList.get(0).getBagCardStatus())){
                    convertView.setTag(WelcomeFragment.GRID_VIEW_BAG);
                    String[] bs = this.entryList.get(0).getBagCardInfo().split(Constants.STR_COMMA);
                    String bagStr = context.getResources().getString(R.string.quick_bag_unbind);
                    String bagEnd ="\n"+bs[0]+"\n"+bs[1];

                    if (entryList.get(0).isBagCardIsNowUnBind()){

                        bagEnd = "";
                    }
                    SpannableString spannableString = new SpannableString(bagStr+bagEnd);

                    spannableString.setSpan(new AbsoluteSizeSpan(26), bagStr.length(), spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    holder.content.setText(spannableString);
                }else{
                    convertView.setTag(WelcomeFragment.GRID_VIEW_CADDIE);

                    String caddieStr = context.getResources().getString(R.string.quick_caddie_unbind);
                    String caddieEnd = "\n"+this.entryList.get(0).getCaddieInfo();
                    if (entryList.get(0).isCaddieCardIsNowUnBind()){

                        caddieEnd = "";
                    }
                    SpannableString spannableString = new SpannableString(caddieStr+caddieEnd);
                    spannableString.setSpan(new ForegroundColorSpan(this.context.getResources().getColor(R.color.common_gray)), caddieStr.length(), spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    holder.content.setText(spannableString);


                }
//                if (Constants.NFC_BIND_STATUS_YES.equals(entryList.get(0).getCaddieCardStatus())){
//
//                }
            }

            if (position == 8){
                if (Constants.NFC_BIND_STATUS_YES.equals(entryList.get(0).getCaddieCardStatus())){
                    convertView.setTag(WelcomeFragment.GRID_VIEW_CADDIE);
                    String caddieStr = context.getResources().getString(R.string.quick_caddie_unbind);
                    String caddieEnd = "\n"+this.entryList.get(0).getCaddieInfo();

                    if (entryList.get(0).isCaddieCardIsNowUnBind()){

                        caddieEnd = "";
                    }
                    SpannableString spannableString = new SpannableString(caddieStr+caddieEnd);
                    spannableString.setSpan(new ForegroundColorSpan(this.context.getResources().getColor(R.color.common_gray)), caddieStr.length(),spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    holder.content.setText(spannableString);

                }else{
                    convertView.setTag(WelcomeFragment.GRID_VIEW_BAG);
                    String[] bs = this.entryList.get(0).getBagCardInfo().split(Constants.STR_COMMA);

                    String bagStr = context.getResources().getString(R.string.quick_bag_unbind);
                    String bagEnd ="\n"+bs[0]+"\n"+bs[1];

                    if (entryList.get(0).isBagCardIsNowUnBind()){

                        bagEnd = "";
                    }
                    SpannableString spannableString = new SpannableString(bagStr+bagEnd);

                    spannableString.setSpan(new AbsoluteSizeSpan(26), bagStr.length(), spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                    holder.content.setText(spannableString);

                }
            }


        }

        return convertView;
    }

    class ViewHolder {
        ImageView icon;
        IteeTextView content;
    }
}
