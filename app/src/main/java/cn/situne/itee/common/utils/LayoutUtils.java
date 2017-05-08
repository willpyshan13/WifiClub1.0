/**
 * Project Name: itee
 * File Name:	 LayoutUtils.java
 * Package Name: cn.situne.itee.common.utils
 * Date:		 2015-07-03
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.common.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * ClassName:LayoutUtils <br/>
 * Function: Utility class of setting layout. <br/>
 * Date: 2015-07-03 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class LayoutUtils {

    public static void setCellLeftKeyViewOfRelativeLayout(View leftView, Context mContext) {
        setCellLeftKeyViewOfRelativeLayout(leftView, 40, mContext);
    }

    public static void setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(View leftView, Context mContext) {
        setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(leftView, 20, mContext);
    }
    public static void setCellLeftKeyViewOfRelativeLayoutWithEightyPercent(View leftView, int leftMargin, Context mContext) {
        int currentWidth = DensityUtil.getActualWidthOnThisDevice((float) (getScreenWidth(mContext) * 0.8), mContext);
        int currentHeight = RelativeLayout.LayoutParams.MATCH_PARENT;
        setCellLeftKeyViewOfRelativeLayout(leftView, currentWidth, currentHeight, leftMargin, mContext);
    }

    public static void setCellRightValueViewOfRelativeLayout(View rightValueView, Context mContext) {
        setCellRightValueViewOfRelativeLayout(rightValueView, 40, mContext);
    }

    public static void setCellLeftKeyViewOfRelativeLayout(View leftView, int leftMargin, Context mContext) {
        int currentWidth = DensityUtil.getActualWidthOnThisDevice(getScreenWidth(mContext) / 2, mContext);
        int currentHeight = RelativeLayout.LayoutParams.MATCH_PARENT;
        setCellLeftKeyViewOfRelativeLayout(leftView, currentWidth, currentHeight, leftMargin, mContext);
    }

    public static void setCellLeftSquareViewOfRelativeLayout(View leftView, int width, Context mContext) {
        setCellLeftSquareViewOfRelativeLayout(leftView, width, 20, mContext);
    }

    public static void setCellLeftSquareViewOfRelativeLayout(View leftView, int width, int leftMargin, Context mContext) {
        int currentWidth = DensityUtil.getActualWidthOnThisDevice(width, mContext);
        int currentHeight = DensityUtil.getActualWidthOnThisDevice(width, mContext);
        setCellLeftKeyViewOfRelativeLayout(leftView, currentWidth, currentHeight, leftMargin, mContext);
    }

    private static void setCellLeftKeyViewOfRelativeLayout(View leftView, int width, int height, int leftMargin, Context mContext) {
        if (leftView instanceof TextView) {
            TextView tvLeft = (TextView) leftView;
            tvLeft.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) leftView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(leftMargin, mContext);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        leftView.setLayoutParams(layoutParams);
    }

    public static void setCellLeftWrapViewOfRelativeLayout(View leftView, Context mContext) {
        setCellLeftWrapViewOfRelativeLayout(leftView, 20, mContext);
    }

    public static void setCellLeftWrapViewOfRelativeLayout(View leftView, int leftMargin, Context mContext) {
        if (leftView instanceof TextView) {
            TextView tvLeft = (TextView) leftView;
            tvLeft.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) leftView.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(leftMargin, mContext);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        leftView.setLayoutParams(layoutParams);
    }

    public static void setCellRightValueViewOfRelativeLayout(View rightValueView, int rightMargin, Context mContext) {
        if (rightValueView instanceof TextView) {
            TextView tvRight = (TextView) rightValueView;
            tvRight.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        }
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rightValueView.getLayoutParams();
        layoutParams.width = DensityUtil.getActualWidthOnThisDevice(getScreenWidth(mContext) / 2, mContext);
        layoutParams.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        layoutParams.rightMargin = DensityUtil.getActualWidthOnThisDevice(rightMargin, mContext);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rightValueView.setLayoutParams(layoutParams);
    }

    public static void setRightArrow(View rightArrow, Context mContext) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rightArrow.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.rightMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rightArrow.setLayoutParams(layoutParams);
    }



    public static void setRightArrow(View rightArrow, int rightMargin,Context mContext) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rightArrow.getLayoutParams();
        layoutParams.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.rightMargin = rightMargin;
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        rightArrow.setLayoutParams(layoutParams);
    }

    private static int getScreenWidth(Context mContext) {
        return mContext.getResources().getDisplayMetrics().widthPixels;
    }

    private static int getScreenHeight(Context mContext) {
        return mContext.getResources().getDisplayMetrics().heightPixels;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static void setListViewHeightBasedOnChildren(ExpandableListView listView, int lineNum, int eachLineHeight) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = lineNum * eachLineHeight;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    //item高度的倍数
    public static void setListViewHeightBasedOnChildrenWithTimes(ListView listView, int times) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int itemHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            itemHeight = listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = itemHeight * times
                + (listView.getDividerHeight() * (times - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static void setLinearLayoutHeight(LinearLayout relativeLayout, int height, Context mContext) {
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) relativeLayout.getLayoutParams();
        layoutParams.height = DensityUtil.getActualHeightOnThisDevice(height, mContext);
        layoutParams.topMargin = DensityUtil.getActualHeightOnThisDevice(0, mContext);
        relativeLayout.setLayoutParams(layoutParams);
    }

    public static void setLayoutHeight(RelativeLayout relativeLayout, int height, Context mContext) {
        setLayoutHeightAndTopMargin(relativeLayout, height, 0, mContext);
    }

    public static void setLayoutHeightAndTopMargin(RelativeLayout relativeLayout, int height, int topMargin, Context mContext) {
        relativeLayout.setGravity(Gravity.CENTER_VERTICAL);
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) relativeLayout.getLayoutParams();
        layoutParams.height = DensityUtil.getActualHeightOnThisDevice(height, mContext);
        layoutParams.topMargin = DensityUtil.getActualHeightOnThisDevice(topMargin, mContext);
        relativeLayout.setLayoutParams(layoutParams);
    }

    public static void setViewLayoutHeight(View view, int height, Context mContext) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = DensityUtil.getActualHeightOnThisDevice(height, mContext);
        view.setLayoutParams(layoutParams);
    }

    public static void setMatchParentLayout(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        view.setLayoutParams(layoutParams);
    }

    public static void setLeftOfView(View targetView, View coordinateView, int rightMargin, Context mContext) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        layoutParams.rightMargin = DensityUtil.getActualWidthOnThisDevice(rightMargin, mContext);
        layoutParams.addRule(RelativeLayout.LEFT_OF, coordinateView.getId());
        targetView.setLayoutParams(layoutParams);
    }

    public static void setRightOfView(View targetView, View coordinateView, int leftMargin, Context mContext) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(leftMargin, mContext);
        layoutParams.addRule(RelativeLayout.RIGHT_OF, coordinateView.getId());
        targetView.setLayoutParams(layoutParams);
    }

    public static void setBelowOfView(View targetView, View coordinateView, int topMargin, Context mContext) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(topMargin, mContext);
        layoutParams.addRule(RelativeLayout.BELOW, coordinateView.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_LEFT, coordinateView.getId());
        targetView.setLayoutParams(layoutParams);
    }

    public static void setWidthAndHeight(View targetView, int width, int height) {
        ViewGroup.LayoutParams layoutParams = targetView.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        targetView.setLayoutParams(layoutParams);
    }

    public static void setLeftTopViewOfTwoCell(View targetView, Context mContext) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        targetView.setLayoutParams(layoutParams);
        if (targetView instanceof TextView) {
            TextView textView = (TextView) targetView;
            textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        }
    }

    public static void setLeftBottomViewOfTwoCell(View targetView, Context mContext) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        layoutParams.leftMargin = DensityUtil.getActualWidthOnThisDevice(40, mContext);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        targetView.setLayoutParams(layoutParams);
        if (targetView instanceof TextView) {
            TextView textView = (TextView) targetView;
            textView.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        }
    }

    public static void setRightTopViewOfTwoCell(View targetView, View rightArrowView) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.LEFT_OF, rightArrowView.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        targetView.setLayoutParams(layoutParams);
        if (targetView instanceof TextView) {
            TextView textView = (TextView) targetView;
            textView.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        }
    }

    public static void setRightBottomViewOfTwoCell(View targetView, View rightArrowView) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.LEFT_OF, rightArrowView.getId());
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        targetView.setLayoutParams(layoutParams);
        if (targetView instanceof TextView) {
            TextView textView = (TextView) targetView;
            textView.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        }
    }

    public static void setViewHeightAndAlignParentTop(View targetView, int height, Context mContext) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        layoutParams.height = DensityUtil.getActualHeightOnThisDevice(height, mContext);
        targetView.setLayoutParams(layoutParams);
    }

    public static void setViewHeightAndAlignParentBottom(View targetView, int height, Context mContext) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.height = DensityUtil.getActualHeightOnThisDevice(height, mContext);
        targetView.setLayoutParams(layoutParams);
    }

    public static void setCenterVertical(View targetView) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) targetView.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
        targetView.setLayoutParams(layoutParams);
    }

    public static void setViewWidthAndHeightInRelativeLayout(View view, int width, int height) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }
}