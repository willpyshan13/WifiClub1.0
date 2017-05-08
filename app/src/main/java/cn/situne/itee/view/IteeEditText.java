/**
 * Project Name: itee
 * File Name:	 IteeBaseEditText.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-03-30
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.EditText;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:IteeBaseEditText <br/>
 * Function: edit text of current project. <br/>
 * Date: 2015-03-30 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeEditText extends EditText {

    protected BaseFragment mFragment;

    public IteeEditText(Context context) {
        super(context);
        setSingleLine();

        setTextColor(context.getResources().getColor(R.color.common_black));
        setTextSize(Constants.FONT_SIZE_NORMAL);
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setBackground(null);
    }

    public IteeEditText(BaseFragment mFragment) {
        super(mFragment.getActivity());
        this.mFragment = mFragment;
        setSingleLine();
        setTextColor(mFragment.getColor(R.color.common_black));
        setTextSize(Constants.FONT_SIZE_NORMAL);
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setBackground(null);
    }

    public IteeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        setSingleLine();
        setTextColor(context.getResources().getColor(R.color.common_black));
        setTextSize(Constants.FONT_SIZE_NORMAL);
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        setBackground(null);
    }

    /**
     * return the value
     *
     * @return the value
     */
    public String getValue() {
        return getText().toString();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setTextColor(getResources().getColor(R.color.common_black));
        } else {
            setTextColor(getResources().getColor(R.color.common_gray));
        }
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
//        }else if (size == Constants.FONT_SIZE_15) {
//            super.setTextSize(TypedValue.COMPLEX_UNIT_PX, sizes[DensityUtil.FONT_SIZE_15]);
//        } else {
//            super.setTextSize(size);
//        }
//    }
}