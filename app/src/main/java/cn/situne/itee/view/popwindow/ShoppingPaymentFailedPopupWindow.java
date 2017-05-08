package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;

public class ShoppingPaymentFailedPopupWindow extends BasePopWindow {

    public TextView tvTypeHome, tvRepay, tvChooseOtherPayment, tvErrorMes;
    public View mMenuView;
    private LinearLayout llContainer;

    public ShoppingPaymentFailedPopupWindow(Context context, final SelectDatePopupWindow.OnDateSelectClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popw_shopping_payment_failed, null);

        llContainer = (LinearLayout) mMenuView.findViewById(R.id.ll_container);
        tvTypeHome = (TextView) mMenuView.findViewById(R.id.tv_address_type_home);
        tvRepay = (TextView) mMenuView.findViewById(R.id.tv_repay);
        tvChooseOtherPayment = (TextView) mMenuView.findViewById(R.id.tv_choose_other_payment);

        tvRepay.setGravity(Gravity.CENTER);
        tvChooseOtherPayment.setGravity(Gravity.CENTER);

        tvErrorMes = (TextView) mMenuView.findViewById(R.id.tvErrorMes);

        tvErrorMes.setSingleLine(false);

        TextView tvL = (TextView) mMenuView.findViewById(R.id.tv_left);
        TextView tvR = (TextView) mMenuView.findViewById(R.id.tv_right);
        tvL.setText(Constants.STR_BRACKETS_START);
        tvR.setText(Constants.STR_BRACKETS_END);

        tvTypeHome.setTextSize(Constants.FONT_SIZE_LARGER);
        tvRepay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.OnGoodItemClick("2", StringUtils.EMPTY);
                dismiss();
            }
        });

        tvChooseOtherPayment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.OnGoodItemClick("3", StringUtils.EMPTY);
                dismiss();
            }
        });
        mMenuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        this.setContentView(mMenuView);

        formatViews();

        setHideListener(mMenuView);

        setListeners();
    }

    public void setMes(String mes) {
        tvErrorMes.setText(mes);
    }

    private void setListeners() {
        llContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}