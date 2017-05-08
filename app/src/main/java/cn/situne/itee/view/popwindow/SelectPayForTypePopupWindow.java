/**
 * Project Name: itee
 * File Name:  SelectPayForTypePopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.view.IteeMoneyEditText;
import cn.situne.itee.view.IteeTextView;

import static android.widget.LinearLayout.LayoutParams;

/**
 * ClassName:SelectPayForTypePopupWindow <br/>
 * Function: select pay type popup window. <br/>
 * Date: 2015-01-20 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectPayForTypePopupWindow extends BasePopWindow {

    private View menuView;
    private IteeMoneyEditText etChargeNum;
    private IteeTextView tvCash, tvVouchers, tvCard, tvThird, tvCurrency, tvTitle;
    private BaseFragment mFragment;

    /**
     * recharge
     */
    public SelectPayForTypePopupWindow(BaseFragment mBaseFragment, final Integer paraAgentId) {
        super(mBaseFragment.getActivity());
        this.mFragment = mBaseFragment;
        LayoutInflater inflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_recharge_type, null);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        etChargeNum = (IteeMoneyEditText) menuView.findViewById(R.id.et_charge_num);
        etChargeNum.setBackgroundResource(R.drawable.textview_corner);
        tvCurrency = (IteeTextView) menuView.findViewById(R.id.tv_currency);
        tvTitle = (IteeTextView) menuView.findViewById(R.id.tv_hour);
        tvCash = (IteeTextView) menuView.findViewById(R.id.tv_recharge_type_cash);
        tvVouchers = (IteeTextView) menuView.findViewById(R.id.tv_recharge_type_vouchers);
        tvCard = (IteeTextView) menuView.findViewById(R.id.tv_recharge_type_credit_card);
        tvThird = (IteeTextView) menuView.findViewById(R.id.tv_recharge_type_Third_party);
        tvCurrency.setText(AppUtils.getCurrentCurrency(mFragment.getActivity()));

        tvCash.setGravity(Gravity.CENTER);
        tvVouchers.setGravity(Gravity.CENTER);
        tvCard.setGravity(Gravity.CENTER);
        tvThird.setGravity(Gravity.CENTER);
        tvCurrency.setGravity(Gravity.CENTER);

        tvTitle.setPadding(20, 0, 20, 0);
        tvTitle.setTextSize(Constants.FONT_SIZE_LARGER);


        tvCash.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvVouchers.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvCard.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvThird.setTextSize(Constants.FONT_SIZE_NORMAL);


        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        setHideListener(menuView);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputManger = (InputMethodManager) mFragment.getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManger.hideSoftInputFromWindow(etChargeNum.getWindowToken(), 0);
                dismiss();
                switch (v.getId()) {
                    case R.id.tv_recharge_type_cash:
                        SelectPayForConfirmPopupWindow menuWindow = new SelectPayForConfirmPopupWindow(mFragment, etChargeNum.getValue(), "1", paraAgentId);
                        menuWindow.showAtLocation(getContentView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;
                    case R.id.tv_recharge_type_vouchers:
                        SelectPayForConfirmPopupWindow menuWindow1 = new SelectPayForConfirmPopupWindow(mFragment, etChargeNum.getValue(), "2", paraAgentId);
                        menuWindow1.showAtLocation(getContentView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;
                    case R.id.tv_recharge_type_credit_card:
                        SelectPayForConfirmPopupWindow menuWindow2 = new SelectPayForConfirmPopupWindow(mFragment, etChargeNum.getValue(), "3", paraAgentId);
                        menuWindow2.showAtLocation(getContentView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;
                    case R.id.tv_recharge_type_Third_party:
                        SelectPayForConfirmPopupWindow menuWindow3 = new SelectPayForConfirmPopupWindow(mFragment, etChargeNum.getValue(), "4", paraAgentId);
                        menuWindow3.showAtLocation(getContentView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;
                    default:
                        break;

                }


            }
        };

        tvCash.setOnClickListener(listener);
        tvVouchers.setOnClickListener(listener);
        tvCard.setOnClickListener(listener);
        tvThird.setOnClickListener(listener);
    }

}
