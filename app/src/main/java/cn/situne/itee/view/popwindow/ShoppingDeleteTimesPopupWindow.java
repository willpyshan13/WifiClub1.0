package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.view.IteeIntegerEditText;
import cn.situne.itee.view.IteePricingTimesEditText;

/**
 * Created by luochao on 9/30/15.
 */
public class ShoppingDeleteTimesPopupWindow extends BasePopWindow {

    private TextView tvMessage;
    private Button btnOk, btnCancel;

    private RelativeLayout rlMessageLayout;

    private IteePricingTimesEditText addTimes;

    private Context mContext;

    public String getTimes() {

        return addTimes.getText().toString();
    }

    public ShoppingDeleteTimesPopupWindow(Context context, View.OnClickListener listener, BaseFragment baseFragment) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View menuView = inflater.inflate(R.layout.shopping_delete_times, null);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        tvMessage = (TextView) menuView.findViewById(R.id.tv_message);
        rlMessageLayout = (RelativeLayout) menuView.findViewById(R.id.rl_message_container);

        LinearLayout.LayoutParams rlMessageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, baseFragment.getActualHeightOnThisDevice(100));
        rlMessageLayout.setLayoutParams(rlMessageLayoutParams);
        tvMessage.setSingleLine(false);
        tvMessage.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        RelativeLayout.LayoutParams tvMessageParams = (RelativeLayout.LayoutParams) tvMessage.getLayoutParams();
        tvMessageParams.leftMargin = baseFragment.getActualWidthOnThisDevice(40);
        tvMessageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvMessage.setLayoutParams(tvMessageParams);
        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);

        RelativeLayout.LayoutParams addTimesParams = new RelativeLayout.LayoutParams(baseFragment.getActualHeightOnThisDevice(150), baseFragment.getActualHeightOnThisDevice(70));

        addTimesParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        addTimesParams.rightMargin = baseFragment.getActualWidthOnThisDevice(40);
        addTimesParams.addRule(RelativeLayout.CENTER_VERTICAL);
        addTimes = new IteePricingTimesEditText(baseFragment);
        addTimes.setLayoutParams(addTimesParams);
        addTimes.setText(Constants.STR_1);

        addTimes.setGravity(Gravity.CENTER);
        addTimes.setBackgroundResource(R.drawable.textview_corner);

        rlMessageLayout.addView(addTimes);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, context.getResources().getColor(R.color.common_blue)); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色

        btnOk.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnOk.setHeight(30);

        btnCancel.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnCancel.setHeight(30);

        menuView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.popup_enter));
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);

        formatViews();

        setHideListener(menuView);

        setListeners(listener);
    }


    private void setListeners(final View.OnClickListener listener) {
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClick(v);
                InputMethodManager inputManger = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManger.hideSoftInputFromWindow(addTimes.getWindowToken(), 0);
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setMessage(String message) {
        tvMessage.setText(message);
    }
}
