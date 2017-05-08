package cn.situne.itee.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mining.app.zxing.MipcaActivityCapture;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.view.IteeButton;
import cn.situne.itee.view.IteeTextView;


public class ScanQrCodeActivity extends MipcaActivityCapture {

    private IteeTextView tvLeftTitle;

    @Override
    protected void createView(RelativeLayout parent) {

        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        if (getActionBar() != null) {
            getActionBar().hide();
        }

        RelativeLayout rlActionBar = new RelativeLayout(this);
        rlActionBar.setBackgroundResource(R.drawable.bg_action_bar);

        RelativeLayout.LayoutParams rlActionBarLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, actionBarHeight);
        rlActionBarLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlActionBar.setLayoutParams(rlActionBarLayoutParams);

        setBackArrowAndLeftTitle(rlActionBar);

        tvLeftTitle.setText("Code");

        parent.addView(rlActionBar);
    }

    @Override
    protected void handleCode(String code) {
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(TransKey.QR_CODE, code);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        Utils.hideKeyboard(this);
        finish();
    }

    private int setBackArrow(RelativeLayout parent) {
        RelativeLayout rlIconContainer = new RelativeLayout(this);
        rlIconContainer.setId(View.generateViewId());

        RelativeLayout.LayoutParams rlIconContainerLayoutParams = new RelativeLayout.LayoutParams(getWidth(55), ViewGroup.LayoutParams.MATCH_PARENT);
        rlIconContainerLayoutParams.leftMargin = getWidth(20);
        rlIconContainer.setLayoutParams(rlIconContainerLayoutParams);

        parent.addView(rlIconContainer);

        ImageView ivIcon = new ImageView(this);
        ivIcon.setId(View.generateViewId());
        ivIcon.setBackgroundResource(R.drawable.icon_back);
        ivIcon.setScaleType(ImageView.ScaleType.FIT_XY);
        ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(ScanQrCodeActivity.this);
                finish();
            }
        });

        RelativeLayout.LayoutParams paramsIvIcon = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsIvIcon.width = getWidth(50);
        paramsIvIcon.height = getWidth(50);
        paramsIvIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        ivIcon.setLayoutParams(paramsIvIcon);
        rlIconContainer.addView(ivIcon);

        ImageView ivSeparator = new ImageView(this);
        ivSeparator.setId(View.generateViewId());
        ivSeparator.setImageResource(R.drawable.icon_separator);
        rlIconContainer.addView(ivSeparator);
        ivSeparator.setVisibility(View.INVISIBLE);

        RelativeLayout.LayoutParams paramsIvSeparator = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsIvSeparator.width = getWidth(5);
        paramsIvSeparator.height = getHeight(50);
        paramsIvSeparator.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsIvSeparator.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsIvSeparator.rightMargin = getWidth(5);
        ivSeparator.setLayoutParams(paramsIvSeparator);

        IteeButton btn = new IteeButton(this);
        RelativeLayout.LayoutParams btnLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        btn.setLayoutParams(btnLayoutParams);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setBackground(null);
        rlIconContainer.addView(btn);

        return rlIconContainer.getId();
    }

    private void setBackArrowAndLeftTitle(RelativeLayout parent) {
        int leftLayoutId = setBackArrow(parent);

        tvLeftTitle = new IteeTextView(this);
        parent.addView(tvLeftTitle);

        RelativeLayout.LayoutParams paramsTvLeftTitle = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        paramsTvLeftTitle.width = (int) (getWidth() * 0.5);
        paramsTvLeftTitle.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvLeftTitle.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        paramsTvLeftTitle.addRule(RelativeLayout.RIGHT_OF, leftLayoutId);
        paramsTvLeftTitle.leftMargin = 10;
        tvLeftTitle.setLayoutParams(paramsTvLeftTitle);

        tvLeftTitle.setTextSize(Constants.FONT_SIZE_LARGER);
        tvLeftTitle.setSingleLine();
        tvLeftTitle.setEllipsize(TextUtils.TruncateAt.END);
        tvLeftTitle.setTextColor(getResources().getColor(R.color.common_white));
    }

    public int getWidth(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_WIDTH * (getResources().getDisplayMetrics().widthPixels));
    }

    public int getHeight(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_HEIGHT * (getResources().getDisplayMetrics().heightPixels));
    }
}
