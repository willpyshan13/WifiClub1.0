/**
 * Project Name: itee
 * File Name:	 IteeButton.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-07-19
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.Button;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;

/**
 * ClassName:IteeButton <br/>
 * Function: subclass of Button. <br/>
 * Date: 2015-07-19 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeButton extends Button {
    public IteeButton(Context context) {
        super(context);
    }

    public IteeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IteeButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void setTextSize(float size) {
        int[] sizes = AppUtils.getFontSize(getContext());
        if (size == Constants.FONT_SIZE_NORMAL) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizes[DensityUtil.FONT_SIZE_INDEX_NORMAL]);
        } else if (size == Constants.FONT_SIZE_SMALLER) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizes[DensityUtil.FONT_SIZE_INDEX_SMALLER]);
        } else if (size == Constants.FONT_SIZE_MORE_SMALLER) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizes[DensityUtil.FONT_SIZE_INDEX_MORE_SMALLER]);
        } else if (size == Constants.FONT_SIZE_SMALLEST) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizes[DensityUtil.FONT_SIZE_INDEX_SMALLEST]);
        } else if (size == Constants.FONT_SIZE_LARGER) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizes[DensityUtil.FONT_SIZE_INDEX_LARGER]);
        } else if (size == Constants.FONT_SIZE_MORE_LARGER) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizes[DensityUtil.FONT_SIZE_INDEX_LARGEST]);
        }else if (size == Constants.FONT_SIZE_15) {
            super.setTextSize(TypedValue.COMPLEX_UNIT_SP, sizes[DensityUtil.FONT_SIZE_15]);
        }

        else {
            super.setTextSize(size);
        }
    }
//    @Override
//    public void setTextSize(float size) {
//        int[] sizes = AppUtils.getFontSize(getContext());
//        if (size == Constants.FONT_SIZE_NORMAL) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_INDEX_NORMAL]);
//        } else if (size == Constants.FONT_SIZE_SMALLER) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_INDEX_SMALLER]);
//        } else if (size == Constants.FONT_SIZE_MORE_SMALLER) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_INDEX_SMALLEST]);
//        } else if (size == Constants.FONT_SIZE_LARGER) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_INDEX_LARGER]);
//        } else if (size == Constants.FONT_SIZE_MORE_LARGER) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_INDEX_LARGEST]);
//        } else if (size == Constants.FONT_SIZE_LOGIN) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, 55);
//        } else if (size == Constants.FONT_SIZE_15) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_15]);
//        }else {
//            super.setTextSize(size);
//        }
//    }
}