/**
 * Project Name: itee
 * File Name:	 IteeMoneyEditText.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-03-30
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view;

import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:IteeMoneyEditText <br/>
 * Function: Edit text for money. <br/>
 * Date: 2015-03-30 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeMoneyEditText extends IteeEditText {

    public IteeMoneyEditText(BaseFragment mFragment) {
        super(mFragment);
        addTextChangedListener(new AppUtils.EditViewMoneyWatcher(this));
        setOnFocusChangeListener(new AppUtils.AddRemoveMoneySymbolListener(mFragment.getActivity()));
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    public IteeMoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        addTextChangedListener(new AppUtils.EditViewMoneyWatcher(this));
        setOnFocusChangeListener(new AppUtils.AddRemoveMoneySymbolListener(context));
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    /**
     * get the value of money without currency symbol
     *
     * @return the value
     */
    @Override
    public String getValue() {
        String res = getText().toString();
        String currentCurrency = AppUtils.getCurrentCurrency(getContext());
        if (res.contains(currentCurrency)) {
            res = res.replace(currentCurrency, Constants.STR_EMPTY).trim();
        }
        if (Utils.isStringNullOrEmpty(res)) res = Constants.STR_0;
        return res;
    }


    public String getEmptyValue() {
        String res = getText().toString();
        String currentCurrency = AppUtils.getCurrentCurrency(getContext());
        if (res.contains(currentCurrency)) {
            res = res.replace(currentCurrency, Constants.STR_EMPTY).trim();
        }
        if (Utils.isStringNullOrEmpty(res)) res = Constants.STR_EMPTY;
        return res;
    }





    public void setValue(String value) {
        if (Utils.isStringNotNullOrEmpty(value)) {
            setText(AppUtils.getCurrentCurrency(getContext()) + value);
        }
    }
}