/**
 * Project Name: itee
 * File Name:  SelectRechargeTypePopupWindow.java
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
import android.widget.TextView;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.view.IteeMoneyEditText;

import static android.widget.LinearLayout.LayoutParams;

/**
 * ClassName:SelectRechargeTypePopupWindow <br/>
 * Function: select recharge type popup window. <br/>
 * Date: 2015-01-20 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectRechargeTypePopupWindow extends BasePopWindow {

    private View menuView;
    private IteeMoneyEditText etChargeNum;
    private TextView tvCash, tvVouchers, tvCard, tvThird, tvCurrency, tvTitle;
    private BaseFragment mFragment;
    private Integer memberId;

    /**
     * recharge
     *
     * @param mFragment fragment
     */
    public SelectRechargeTypePopupWindow(final BaseFragment mFragment, Integer paraMemberId) {
        super(mFragment.getActivity());
        this.mFragment = mFragment;
        this.memberId = paraMemberId;
        LayoutInflater inflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_recharge_type, null);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        etChargeNum = (IteeMoneyEditText) menuView.findViewById(R.id.et_charge_num);
        etChargeNum.setBackgroundResource(R.drawable.textview_corner);

        tvTitle = (TextView) menuView.findViewById(R.id.tv_hour);
        tvCurrency = (TextView) menuView.findViewById(R.id.tv_currency);

        tvCurrency.setText(AppUtils.getCurrentCurrency(mFragment.getActivity()));
        tvCurrency.setTextColor(mFragment.getActivity().getResources().getColor(R.color.common_black));
        tvCash = (TextView) menuView.findViewById(R.id.tv_recharge_type_cash);
        tvVouchers = (TextView) menuView.findViewById(R.id.tv_recharge_type_vouchers);
        tvCard = (TextView) menuView.findViewById(R.id.tv_recharge_type_credit_card);
        tvThird = (TextView) menuView.findViewById(R.id.tv_recharge_type_Third_party);

        tvTitle.setPadding(20, 0, 20, 0);
        tvTitle.setTextSize(Constants.FONT_SIZE_LARGER);
        tvTitle.setSingleLine(false);

        tvCash.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvVouchers.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvCard.setTextSize(Constants.FONT_SIZE_NORMAL);
        tvThird.setTextSize(Constants.FONT_SIZE_NORMAL);

        tvCash.setGravity(Gravity.CENTER);
        tvVouchers.setGravity(Gravity.CENTER);
        tvCard.setGravity(Gravity.CENTER);
        tvThird.setGravity(Gravity.CENTER);

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

                if (Utils.isStringNullOrEmpty(etChargeNum.getValue()) ||
                        Constants.STR_0.equals(etChargeNum.getValue())) {
                    Utils.showShortToast(mFragment.getActivity(), mFragment.getString(R.string.error_mes00002));
                    return;
                }

                dismiss();

                switch (v.getId()) {
                    case R.id.tv_recharge_type_cash:
                        SelectRechargeConfirmPopupWindow menuWindow
                                = new SelectRechargeConfirmPopupWindow(SelectRechargeTypePopupWindow.this.mFragment,
                                etChargeNum.getValue(), "1", memberId);
                        menuWindow.showAtLocation(getContentView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        tvCash.requestFocus();
                        break;
                    case R.id.tv_recharge_type_vouchers:
                        SelectRechargeConfirmPopupWindow menuWindow1
                                = new SelectRechargeConfirmPopupWindow(SelectRechargeTypePopupWindow.this.mFragment,
                                etChargeNum.getValue(), "2", memberId);
                        menuWindow1.showAtLocation(getContentView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;
                    case R.id.tv_recharge_type_credit_card:
                        SelectRechargeConfirmPopupWindow menuWindow2
                                = new SelectRechargeConfirmPopupWindow(SelectRechargeTypePopupWindow.this.mFragment,
                                etChargeNum.getValue(), "3", memberId);
                        menuWindow2.showAtLocation(getContentView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        break;
                    case R.id.tv_recharge_type_Third_party:
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
