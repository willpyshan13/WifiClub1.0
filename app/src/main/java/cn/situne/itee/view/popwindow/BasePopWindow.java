/**
 * Project Name: itee
 * File Name:	 BasePopWindow.java
 * Package Name: cn.situne.itee.view.popwindow
 * Date:		 2015-07-01
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import cn.situne.itee.R;

/**
 * ClassName:BasePopWindow <br/>
 * Function: BasePopWindow. <br/>
 * Date: 2015-07-01 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class BasePopWindow extends PopupWindow {

    public BasePopWindow(Context mContext) {
        super(mContext);
    }

    protected void formatViews() {
        //设置SelectPicPopupWindow弹出窗体的宽
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);
    }

    protected void setHideListener(final View menuView) {
        if (menuView != null) {
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
        }
    }
}  