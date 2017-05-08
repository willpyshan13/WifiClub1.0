/**
 * Project Name: itee
 * File Name:	 IteeRadioButton.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-07-19
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.situne.itee.R;

/**
 * ClassName:IteeRadioButton <br/>
 * Function: Radio button. <br/>
 * Date: 2015-07-19 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeRadioButton extends IteeButton {

    private boolean mChecked;

    public IteeRadioButton(Context context) {
        super(context);
        initView();
    }

    public IteeRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IteeRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setBackgroundResource(R.drawable.icon_shops_green_unselected);
        setOnClickListener(null);
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        super.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mChecked = !mChecked;
                if (mChecked) {
                    setBackgroundResource(R.drawable.icon_shop_green_selected);
                } else {
                    setBackgroundResource(R.drawable.icon_shops_green_unselected);
                }
                if (l != null) {
                    l.onClick(v);
                }
            }
        });
    }

    public void doChecked() {
        mChecked = true;
        setBackgroundResource(R.drawable.icon_shop_green_selected);
    }

    public void doUnchecked() {
        mChecked = false;
        setBackgroundResource(R.drawable.icon_shops_green_unselected);
    }

    public boolean isChecked() {
        return mChecked;
    }
}