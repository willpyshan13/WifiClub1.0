/**
 * Project Name: itee
 * File Name:	 IteeRedDeleteButton.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-08-10
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;

/**
 * ClassName:IteeRedDeleteButton <br/>
 * Function: Delete button. <br/>
 * Date: 2015-08-10 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeRedDeleteButton extends IteeButton {

    public IteeRedDeleteButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public IteeRedDeleteButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public IteeRedDeleteButton(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setBackgroundResource(R.drawable.bg_common_delete_round);
        setTextColor(Color.RED);
        setText(R.string.common_delete);
        setTextSize(Constants.FONT_SIZE_NORMAL);
    }
}