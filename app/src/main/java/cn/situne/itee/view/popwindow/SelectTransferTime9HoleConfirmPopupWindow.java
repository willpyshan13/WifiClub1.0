/**
 * Project Name: itee
 * File Name:  SelectRechargeConfirmPopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.fragment.BaseFragment;

import static android.widget.LinearLayout.LayoutParams;

/**
 * ClassName:SelectRechargeConfirmPopupWindow <br/>
 * Function: select recharge confirm popup window. <br/>
 * Date: 2015-02-28 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectTransferTime9HoleConfirmPopupWindow extends PopupWindow {

    private View menuView;
    private Button save;
    private Button cancel;
    private TextView tvMessage;
    private BaseFragment mBaseFragment;
    private Integer memberId;

    private String rechargeMoney;
    private String rechargeType;
    private SaveConfirmListener listener;

    /**
     * recharge
     *
     * @param mBaseFragment
     * @param message
     */
    public SelectTransferTime9HoleConfirmPopupWindow(BaseFragment mBaseFragment, String message, final SaveConfirmListener listener) {
        super(mBaseFragment.getActivity());
        this.mBaseFragment = mBaseFragment;
        this.listener = listener;
        LayoutInflater inflater = (LayoutInflater) mBaseFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_recharge_confirm, null);
        tvMessage = (TextView) menuView.findViewById(R.id.tv_message);
        tvMessage.setText(Html.fromHtml(message));
        tvMessage.setTextSize(Constants.FONT_SIZE_LARGER);

        save = (Button) menuView.findViewById(R.id.btn_ok);
        cancel = (Button) menuView.findViewById(R.id.btn_cancel);
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

        menuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = menuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClickSave();
                dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickCancel();
                dismiss();
            }
        });


    }


    public interface SaveConfirmListener {
        void onClickSave();

        void onClickCancel();
    }
}
