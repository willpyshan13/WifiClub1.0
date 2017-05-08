/**
 * Project Name: itee
 * File Name:	 IteeMoneyEditText.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-03-30
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */

package cn.situne.itee.view;

import android.content.res.Configuration;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:IteePeriodEditText <br/>
 * Function: Edit text for period of validity. <br/>
 * Date: 2015-04-22 <br/>
 *
 * @author xuyue
 * @version 1.0
 * @see
 */
public class IteePeriodEditText extends IteeEditText {

    public IteePeriodEditText(BaseFragment mFragment) {
        super(mFragment);
        addTextChangedListener(new AppUtils.EditViewMoneyWatcher(this));
        setOnFocusChangeListener(new AppUtils.AddRemoveMSymbolListener(mFragment.getActivity()));
        setRawInputType(Configuration.KEYBOARD_12KEY);
    }

    /**
     * get the value of money without currency symbol
     *
     * @return the value
     */
    @Override
    public String getValue() {
        String res = getText().toString();
        String currentCurrency = AppUtils.getCurrentCurrency(mFragment.getActivity());
        if (res.contains(currentCurrency)) {
            res = res.replace(currentCurrency, Constants.STR_EMPTY).trim();
        }
        return res;
    }

    public void setValue(String value) {
        if (Utils.isStringNotNullOrEmpty(value)) {
            setText(AppUtils.getCurrentCurrency(mFragment.getActivity()) + value);
        }
    }
}