/**
 * Project Name: itee
 * File Name:	 TeeTimeAddWithGoodDetailView.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-01-20
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.manager.jsonentity.JsonChangeTransferArea;
import cn.situne.itee.view.popwindow.SelectTransferTimePopupWindow;

/**
 * ClassName:CourseHoleSettingFragment <br/>
 * Function: Transfer time view of course in TeeTimeAddFragment. <br/>
 * UI:  04-1
 * Date: 2015-01-20 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class TeeTimeTitleInOrOutView extends LinearLayout implements View.OnClickListener {

    private BaseFragment mFragment;
    private View view;
    private IteeTextView dateOut;
    private IteeTextView dateOutValue;
    private boolean clickAble;
    private SelectTransferTimePopupWindow menuWindow;
    private String date;
    private List<JsonBookingDetailList.DataListItem.BookingAreaListItem> dataList;
    private LinearLayout parent;
    private Integer mCourseAreaId;
    private TeeTimeAddTitleView parentView;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private JsonBookingDetailList.DataListItem.BookingAreaListItem bookingAreaListItem;

    public void setBookingAreaListItem(JsonBookingDetailList.DataListItem.BookingAreaListItem bookingAreaListItem) {
        this.bookingAreaListItem = bookingAreaListItem;
    }


    public TeeTimeTitleInOrOutView(BaseFragment mFragment, List<JsonBookingDetailList.DataListItem.BookingAreaListItem> dataList,

                                   Integer courseAreaId, LinearLayout parent, TeeTimeAddTitleView parentView) {
        super(mFragment.getActivity(), null);
        this.mFragment = mFragment;
        this.parent = parent;
        this.dataList = dataList;
        this.mCourseAreaId = courseAreaId;
        this.parentView = parentView;
        initView();
    }

    public TeeTimeTitleInOrOutView(Context mFragment, AttributeSet attrs) {
        super(mFragment, attrs, 0);
    }

    public TeeTimeTitleInOrOutView(Context mFragment, AttributeSet attrs, int defStyleAttr) {
        super(mFragment, attrs, defStyleAttr);
    }

    private void initView() {
        LayoutInflater mInflater = (LayoutInflater) mFragment.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.tee_time_title_in_out, this, false);
        dateOut = (IteeTextView) view.findViewById(R.id.date_out);
        dateOut.setTextColor(getResources().getColor(R.color.common_black));
        dateOutValue = (IteeTextView) view.findViewById(R.id.date_out_value);
        addView(view);
        setOnClickListener(this);

        dateOut.setTextSize(Constants.FONT_SIZE_SMALLER);
        dateOutValue.setTextSize(Constants.FONT_SIZE_SMALLER);
    }

    public TextView getDateOutValue() {
        return dateOutValue;
    }

    public void setDateOutValue(String dateOutValue) {
        this.dateOutValue.setText(dateOutValue);
    }

    public TextView getDateOut() {
        return dateOut;
    }

    public void setDateOut(String dateOut) {
        this.dateOut.setText(dateOut);
    }

    @Override
    public void onClick(final View v) {

        if (isClickAble()) {
            TeeTimeAddTitleView.OnTwoWheelClickListener onTwoWheelClickListener = new TeeTimeAddTitleView.OnTwoWheelClickListener() {
                @Override
                public void onTransferTimeReturn(String a, JsonChangeTransferArea.TransferTimeItem entity) {

                    if (!"delete_one".equals(a)) {
                        setDateOut(entity.areaType);
                        setDateOutValue(a.substring(0, 5));
                        bookingAreaListItem.setBookingTime(a);
                        bookingAreaListItem.setCourseAreaType(entity.areaType);
                        bookingAreaListItem.setCourseAreaId(Integer.valueOf(entity.areaId));
                        menuWindow.dismiss();
                    } else {

                        parent.removeView(v);
                        parentView.resetHsvGoodItemCenterViewTag((int) v.getTag());
                        menuWindow.dismiss();
                    }

                }

            };

            Integer index = (Integer) v.getTag();
            Utils.hideKeyboard(mFragment.getActivity());
            menuWindow = new SelectTransferTimePopupWindow(mFragment, bookingAreaListItem, date, dataList, mCourseAreaId, index, onTwoWheelClickListener);

            menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }


    public boolean isClickAble() {
        return clickAble;
    }

    public void setClickAble(boolean clickAble) {
        this.clickAble = clickAble;
    }
}
