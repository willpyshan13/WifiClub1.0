package cn.situne.itee.view.ClipImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.situne.itee.R;
import cn.situne.itee.common.utils.Utils;

/**
 * http://blog.csdn.net/lmj623565791/article/details/39761281
 *
 * @author zhy
 *
 * 选择图片的时候使用
 */
public class ClipImageLayout extends RelativeLayout {

    private ClipZoomImageView mZoomImageView;
    private ClipImageBorderView mClipImageView;

    public TextView ok, cancel;

    /**
     * 这里测试，直接写死了大小，真正使用过程中，可以提取为自定义属性
     */
    private int mHorizontalPadding = 20;

    public ClipImageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mZoomImageView = new ClipZoomImageView(context);
        mClipImageView = new ClipImageBorderView(context);
        ok = new TextView(context);
        cancel = new TextView(context);

        this.addView(mZoomImageView);
        this.addView(mClipImageView);
        this.addView(ok);
        this.addView(cancel);


        RelativeLayout.LayoutParams paramsTvCity = (RelativeLayout.LayoutParams) mZoomImageView.getLayoutParams();
        paramsTvCity.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvCity.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        mZoomImageView.setLayoutParams(paramsTvCity);
        mClipImageView.setLayoutParams(paramsTvCity);


        RelativeLayout.LayoutParams paramsOk = (RelativeLayout.LayoutParams) ok.getLayoutParams();
        paramsOk.width = (int) (Utils.getWidth(context) * 0.2F);
        paramsOk.height = (int) (Utils.getHeight(context) * 0.08F);
        paramsOk.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        paramsOk.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
        ok.setGravity(Gravity.END);
        paramsOk.setMargins(0, 0, 50, 0);
        ok.setLayoutParams(paramsOk);

        RelativeLayout.LayoutParams paramsCancel = (RelativeLayout.LayoutParams) cancel.getLayoutParams();
        paramsCancel.width = (int) (Utils.getWidth(context) * 0.2F);
        paramsCancel.height = (int) (Utils.getHeight(context) * 0.08F);
        paramsCancel.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        paramsCancel.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, TRUE);
        paramsCancel.setMargins(50, 0, 0, 0);
        cancel.setLayoutParams(paramsCancel);
        ok.setTextColor(getResources().getColor(R.color.common_white));
        cancel.setTextColor(getResources().getColor(R.color.common_white));
        ok.setText(getResources().getString(R.string.common_ok));
        cancel.setText(getResources().getString(R.string.common_cancel));


        // 计算padding的px
        mHorizontalPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHorizontalPadding, getResources()
                        .getDisplayMetrics());
//        mZoomImageView.setHorizontalPadding(mHorizontalPadding);
        mClipImageView.setHorizontalPadding(mHorizontalPadding);
    }

    /**
     * 对外公布设置边距的方法,单位为dp
     *
     * @param mHorizontalPadding
     */
    public void setHorizontalPadding(int mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
    }

    public void setBitmap(Bitmap bitmap) {
        mZoomImageView.setImageBitmap(bitmap);
    }

    public ClipZoomImageView getmZoomImageView() {
        return mZoomImageView;
    }

    /**
     * 裁切图片
     *
     * @return
     */
    public Bitmap clip() {
        return mZoomImageView.clip();
    }
}
