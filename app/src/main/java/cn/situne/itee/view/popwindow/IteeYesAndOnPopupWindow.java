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
import cn.situne.itee.view.IteePricingTimesEditText;

/**
 * Created by luochao on 12/14/15.
 */
public class IteeYesAndOnPopupWindow extends BasePopWindow {

    private TextView tvMessage;

    public TextView getTvMessageView() {
        return tvMessage;
    }

    public Button getOkBtn(){

        return btnOk;
    }

    public Button getCancelBtn(){

        return btnCancel;
    }

    private Button btnOk, btnCancel;

    private RelativeLayout rlMessageLayout;


    private Context mContext;


    private View menuView;



    public IteeYesAndOnPopupWindow(Context context, View.OnClickListener listener, BaseFragment baseFragment) {
        super(context);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         menuView = inflater.inflate(R.layout.itee_yes_no_pop, null);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        tvMessage = (TextView) menuView.findViewById(R.id.tv_message);
        rlMessageLayout = (RelativeLayout) menuView.findViewById(R.id.rl_message_container);
        LinearLayout.LayoutParams rlMessageLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        rlMessageLayout.setLayoutParams(rlMessageLayoutParams);
        tvMessage.setSingleLine(false);
        tvMessage.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

        RelativeLayout.LayoutParams tvMessageParams = (RelativeLayout.LayoutParams) tvMessage.getLayoutParams();
        tvMessageParams.leftMargin = baseFragment.getActualWidthOnThisDevice(40);
        tvMessageParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tvMessageParams.height = baseFragment.getActualHeightOnThisDevice(100);
        tvMessage.setLayoutParams(tvMessageParams);
        tvMessage.setPadding(0,0,baseFragment.getActualWidthOnThisDevice(40),0);
        btnOk = (Button) menuView.findViewById(R.id.btn_ok);
        btnCancel = (Button) menuView.findViewById(R.id.btn_cancel);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(2, context.getResources().getColor(R.color.common_blue)); // 边框粗细及颜色
        drawable.setColor(Color.WHITE); // 边框内部颜色

        btnOk.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnOk.setHeight(30);

        btnCancel.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        btnCancel.setHeight(30);


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




    public void setOkListener(final View.OnClickListener listener){
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);

            }
        });

    }

    public void setMessage(String message) {
        tvMessage.setText(message);
    }

    public void setView(View view){
        tvMessage.setVisibility(View.GONE);
        rlMessageLayout.addView(view);

    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);

        menuView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.popup_enter));
    }
}
