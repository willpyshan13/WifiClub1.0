
/**
 * Project Name: itee
 * File Name:	 IteeGreenMoneyEditText.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-06-10
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Context;
import android.util.AttributeSet;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;

/**
 * ClassName:IteeMoneyEditText <br/>
 * Function: Edit text for money. 05-2-2 <br/>
 * Date:  05-2-2  <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class IteeGreenMoneyEditText extends IteeMoneyEditText {
    public IteeGreenMoneyEditText(BaseFragment mFragment) {
        super(mFragment);
    }

    public IteeGreenMoneyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public String getNullValue() {
        String res = getText().toString();
        String currentCurrency = AppUtils.getCurrentCurrency(getContext());
        if (res.contains(currentCurrency)) {
            res = res.replace(currentCurrency, Constants.STR_EMPTY).trim();
        }
        if (Utils.isStringNullOrEmpty(res)) res = null;
        return res;
    }


    public void setValue(String value) {
        super.setValue(value);
        if (value.equals("-1")) {
            setText(AppUtils.getCurrentCurrency(getContext()));
        }
    }

}
