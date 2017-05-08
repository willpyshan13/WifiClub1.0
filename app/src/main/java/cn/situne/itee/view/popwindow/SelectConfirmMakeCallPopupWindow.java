/**
 * Project Name: itee
 * File Name:  SelectConfirmMakeCallPopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.situne.itee.R;
import cn.situne.itee.activity.MainActivity;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;

import static android.widget.LinearLayout.LayoutParams;

/**
 * ClassName:SelectCaddiePopupWindow <br/>
 * Function: confirm of make a phone call popup window. <br/>
 * Date: 2015-01-26 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectConfirmMakeCallPopupWindow extends BasePopWindow {

    private View menuView;

    /**
     * confirm
     *
     * @param mFragment     base fragment
     * @param clickListener listener
     */
    public SelectConfirmMakeCallPopupWindow(final BaseFragment mFragment, final TeeTimeAddFragment.OnPopupWindowWheelClickListener clickListener, final String telNum) {
        super(mFragment.getActivity());
        LayoutInflater inflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.select_save_or_not_popup_window, null);
        TextView save = (TextView) menuView.findViewById(R.id.bt_course_out);
        TextView cancel = (TextView) menuView.findViewById(R.id.bt_course_in);
        save.setText(mFragment.getResources().getString(R.string.msg_confirm_call) + telNum);
        cancel.setText(mFragment.getResources().getString(R.string.common_cancel));
        save.setGravity(Gravity.CENTER);
        cancel.setGravity(Gravity.CENTER);
        save.setTextSize(Constants.FONT_SIZE_LARGER);
        cancel.setTextSize(Constants.FONT_SIZE_LARGER);
        LinearLayout.LayoutParams paramsRLMemberName = (LinearLayout.LayoutParams) save.getLayoutParams();

        paramsRLMemberName.width = LinearLayout.LayoutParams.MATCH_PARENT;
        paramsRLMemberName.height = (int) (Utils.getHeight(mFragment.getActivity()) * 0.08f);
        save.setLayoutParams(paramsRLMemberName);
        cancel.setLayoutParams(paramsRLMemberName);

        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        setHideListener(menuView);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity mainActivity = (MainActivity) mFragment.getActivity();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNum));
                mainActivity.startActivity(intent);
                dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

}
