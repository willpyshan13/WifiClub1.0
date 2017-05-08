/**
 * Project Name: itee
 * File Name:  MultiChoiceItem.java
 * Package Name: cn.situne.itee.view
 * Date:   2015-03-19
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

import cn.situne.itee.R;

/**
 * ClassName:MultiChoiceItem <br/>
 * Function: MultiChoiceItem. <br/>
 * Date: 2015-03-19 <br/>
 *
 * @author liaojian
 * @version 1.0
 * @see
 */
public class MultiChoiceItem extends LinearLayout implements Checkable {
    private boolean mChecked;

    public MultiChoiceItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        if (checked) {
            this.setBackgroundColor(getResources().getColor(R.color.common_high_light_blue));
        } else {
            this.setBackgroundColor(getResources().getColor(R.color.common_white));
        }

    }

    @Override
    public boolean isChecked() {

        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
