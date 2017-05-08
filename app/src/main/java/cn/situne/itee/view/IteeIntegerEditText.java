/**
 * Project Name: itee
 * File Name:	 IteeIntegerEditText.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-04-29
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view;

import android.content.Context;
import android.text.InputType;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:IteeIntegerEditText <br/>
 * Function:  <br/>
 * Date: 2015-04-29 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeIntegerEditText extends IteeEditText {

    public IteeIntegerEditText(Context mContext) {
        super(mContext);
        initView(mContext);
    }

    public IteeIntegerEditText(BaseFragment mFragment) {
        super(mFragment);
        initView(mFragment.getActivity());
    }

    private void initView(Context mContext) {
        setSingleLine();
        setTextColor(mContext.getResources().getColor(R.color.common_black));
        setTextSize(Constants.FONT_SIZE_NORMAL);
        setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_NUMBER);
        setBackground(null);
        addTextChangedListener(new AppUtils.EditViewIntegerWatcher(this));
        setOnFocusChangeListener(new AppUtils.AddIntegerFocusListener());
    }
}