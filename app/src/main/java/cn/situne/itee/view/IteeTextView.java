/**
 * Project Name: itee
 * File Name:	 IteeTextView.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-03-30
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:IteeTextView <br/>
 * Function: custom text view. <br/>
 * Date: 2015-03-30 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeTextView extends TextView {

    public IteeTextView(Context mContext) {
        super(mContext);
        init();
    }

    public IteeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            if ("textSize".equals(attrs.getAttributeName(i))) {
                String sizeValue = attrs.getAttributeValue(i);
                if (StringUtils.isNotEmpty(sizeValue)) {
                    if (sizeValue.contains("sp")) {
                        sizeValue = sizeValue.replace("sp", StringUtils.EMPTY);
                    }
                    setTextSize(Double.valueOf(sizeValue).intValue());
                }
            }
        }

    }

    public IteeTextView(BaseFragment mFragment) {
        super(mFragment.getActivity());
        init();
    }




    public void setE(boolean b){
        if (b){

            setTextColor(Color.WHITE);

        }else{
            setTextColor(Color.GRAY);

        }
        setEnabled(b);

    }
    private void init() {
        setTextSize(Constants.FONT_SIZE_NORMAL);
        setEllipsize(TextUtils.TruncateAt.END);
        setGravity(Gravity.CENTER_VERTICAL);
        setSingleLine();
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
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_INDEX_MORE_SMALLER]);
//        } else if (size == Constants.FONT_SIZE_SMALLEST) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_INDEX_SMALLEST]);
//        } else if (size == Constants.FONT_SIZE_LARGER) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_INDEX_LARGER]);
//        } else if (size == Constants.FONT_SIZE_MORE_LARGER) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_INDEX_LARGEST]);
//        }else if (size == Constants.FONT_SIZE_15) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_15]);
//        }
//
//        else {
//            super.setTextSize(size);
//        }
//    }
}