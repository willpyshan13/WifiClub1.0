package cn.situne.itee.common.utils;

import android.content.Context;

import cn.situne.itee.common.constant.Constants;


public class DensityUtil {

    public static final int FONT_SIZE_INDEX_SMALLEST = 0;
    public static final int FONT_SIZE_INDEX_MORE_SMALLER = 1;
    public static final int FONT_SIZE_INDEX_SMALLER = 2;
    public static final int FONT_SIZE_INDEX_NORMAL = 3;
    public static final int FONT_SIZE_INDEX_LARGER = 4;
    public static final int FONT_SIZE_INDEX_LARGEST = 5;

    public static final int FONT_SIZE_15 = 6;

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int getActualWidthOnThisDevice(float pxValue, Context context) {
        return (int) (pxValue / Constants.DESIGN_UI_WIDTH * getScreenWidth(context));
    }

    public static int getActualHeightOnThisDevice(float pxValue, Context context) {
        return (int) (pxValue / Constants.DESIGN_UI_HEIGHT * getScreenHeight(context));
    }

    public static int getScreenWidth(Context context) {
        int res = 0;
        try {
            res = context.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static int getScreenHeight(Context context) {
        int res = 0;
        try {
            res = context.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }
}
