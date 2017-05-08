/**
 * Project Name: itee
 * File Name:	 IteeQtyEditText.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-07-24
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Context;
import android.text.InputType;

import org.apache.commons.lang3.StringUtils;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:IteeQtyEditText <br/>
 * Function:  <br/>
 * Date: 2015-07-24 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeQtyEditText extends IteeEditText {

    public IteeQtyEditText(Context mContext) {
        super(mContext);
        initView(mContext);
    }

    public IteeQtyEditText(BaseFragment mFragment) {
        super(mFragment);
        initView(mFragment.getActivity());
    }

    private void initView(Context mContext) {
        setSingleLine();
        setTextColor(mContext.getResources().getColor(R.color.common_black));
        setTextSize(Constants.FONT_SIZE_NORMAL);
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_NUMBER);
        setBackground(null);
        AppUtils.EditViewIntegerWatcher editViewIntegerWatcher = new AppUtils.EditViewIntegerWatcher(this);
        editViewIntegerWatcher.setIsQty(true);
        addTextChangedListener(editViewIntegerWatcher);
        setOnFocusChangeListener(new AppUtils.AddIntegerFocusListener());
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled) {
            super.setEnabled(true);
        }
        if (StringUtils.isNotEmpty(getValue())) {
            if (Integer.valueOf(getValue()) == 0) {
                if (enabled) {
                    setTextColor(getResources().getColor(R.color.common_red));
                } else {
                    setTextColor(getResources().getColor(R.color.common_gray));
                }
            } else {
                if (enabled) {
                    setTextColor(getResources().getColor(R.color.common_black));
                } else {
                    setTextColor(getResources().getColor(R.color.common_gray));
                }
            }
        } else {
            if (enabled) {
                setTextColor(getResources().getColor(R.color.common_black));
            } else {
                setTextColor(getResources().getColor(R.color.common_gray));
            }
        }
        if (!enabled) {
            super.setEnabled(false);
        }
    }
}