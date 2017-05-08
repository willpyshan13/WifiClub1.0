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
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerPastBookingFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.manager.jsonentity.JsonChangeTransferArea;
import cn.situne.itee.view.popwindow.SelectTransferTime9HoleConfirmPopupWindow;
import cn.situne.itee.view.popwindow.SelectTransferTimePopupWindow;

/**
 * ClassName:CourseHoleSettingFragment <br/>
 * Function: Title view of course in TeeTimeAddFragment. <br/>
 * UI:  04-1
 * Date: 2015-01-20 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class TeeTimeAddTitleView extends LinearLayout {

    private List<TeeTimeAddWithGoodDetailView> goodsList;
    public List<TeeTimeAddWithGoodDetailView> getGoodsList() {
        return goodsList;
    }
    public void setGoodsList(List<TeeTimeAddWithGoodDetailView> goodsList) {
        this.goodsList = goodsList;
    }

    public LinearLayout itemDetail;
    public int position;
    public ArrayList<SwipeLinearLayout> swipeLinearLayoutList;
    private BaseFragment mFragment;
    private LayoutInflater mInflater;
    private LinearLayout hsvGoodItemCenter;
    private List<JsonBookingDetailList.DataListItem.BookingAreaListItem> areaList;
    private List<JsonBookingDetailList.DataListItem.BookingListItem> bookingList;
    private Button buttonAddTransferTime;
    private List<JsonBookingDetailList.DataListItem.ListItem> list;
    private int mRightWidth;
    private IteeTextView tvDateFlag, tvDate;
    private JsonBookingDetailList dataParameter;
    private String mDate;
    private String mDateFlag = StringUtils.EMPTY;
    private Integer mTypeId;

    private ChangeBookingTimeListener changeBookingTimeListener;

    public ChangeBookingTimeListener getChangeBookingTimeListener() {
        return changeBookingTimeListener;
    }

    public void setChangeBookingTimeListener(ChangeBookingTimeListener changeBookingTimeListener) {
        this.changeBookingTimeListener = changeBookingTimeListener;
    }

    public Integer getmTypeId() {
        return mTypeId;
    }

    public void setmTypeId(Integer mTypeId) {
        this.mTypeId = mTypeId;
    }

    private Integer mCourseAreaId;

    public Integer getmCourseAreaId() {
        return mCourseAreaId;
    }

    public void setmCourseAreaId(Integer mCourseAreaId) {
        this.mCourseAreaId = mCourseAreaId;
    }

    private String mFromPage;
    private boolean isCanEditTransferTime = true;

    private TeeTimeAddFragment.OnPopupWindowWheelClickListener listener;
    private SwipeLinearLayout.AfterShowRightListener afterShowRightListener;

    public TeeTimeAddTitleView(BaseFragment mFragment, JsonBookingDetailList dataParameter,
                               List<JsonBookingDetailList.DataListItem.ListItem> list,
                               int mRightWidth, int position, Integer courseAreaId,
                               TeeTimeAddFragment.OnPopupWindowWheelClickListener listener,
                               Integer typeId, String fromPage, SwipeLinearLayout.AfterShowRightListener afterShowRightListener) {
        super(mFragment.getActivity());
        this.afterShowRightListener = afterShowRightListener;
        goodsList = new ArrayList<>();
        this.mFragment = mFragment;
        this.mDate = dataParameter.getDataList().getBookingDate();
        this.mRightWidth = mRightWidth;
        this.listener = listener;
        this.list = list;
        this.position = position;
        this.dataParameter = dataParameter;
        mFromPage = fromPage;
        this.mCourseAreaId = courseAreaId;
        mTypeId = typeId;
        if (Constants.SEGMENT_TIME_MEMBER_ONLY_ID.equals(String.valueOf(mTypeId))) {
            mDateFlag = "M";
        } else if (Constants.SEGMENT_TIME_EVENT_TIME_ID.equals(String.valueOf(mTypeId))) {
            mDateFlag = "E";
        } else if (Constants.SEGMENT_TIME_PRIME_DISCOUNT_ID.equals(String.valueOf(mTypeId))) {
            mDateFlag = "%";
        }
        mInflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
    }

    public TeeTimeAddTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private SelectTransferTimePopupWindow menuWindow;

    public TeeTimeAddTitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private TeeTimeCalendarPopupWindowView calendarPopupWindowView;

    private void initView() {
        swipeLinearLayoutList = new ArrayList<>();
        mInflater.inflate(R.layout.rl_good_item_title, this);

        itemDetail = (LinearLayout) this.findViewById(R.id.ls_sub);
        tvDateFlag = (IteeTextView) this.findViewById(R.id.date_flag);
        tvDate = (IteeTextView) this.findViewById(R.id.date);
        list.get(position).setTypeTimeName(mTypeId);
        areaList = list.get(position).getAreaList();
        bookingList = list.get(position).getBookingList();

        tvDate.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(mDate, mFragment.getActivity()));
        list.get(position).setBookingDate(mDate);

        tvDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkCheckIn()) {
                    calendarPopupWindowView = new TeeTimeCalendarPopupWindowView(mFragment, mFragment.getRootView(), mDate, true);
                    calendarPopupWindowView.setIsBlock(true);
                    calendarPopupWindowView.setDateClickListener(new TeeTimeCalendarPopupWindowView.DateClickListener() {
                        @Override
                        public void onDateClick(final String date) {
                            menuWindow = new SelectTransferTimePopupWindow(mFragment, date, bookingList, mCourseAreaId, new ChangeTimeListener() {
                                @Override
                                public void clickOk(String a, String changeDate, String startTime, String id, String name) {
                                    for (TeeTimeAddWithGoodDetailView goods : goodsList) {
                                        goods.setBookingDate(changeDate);
                                        goods.initImage();
                                        if (Utils.isStringNotNullOrEmpty(goods.getBooking().getSignId())) {
                                            goods.btn_add_people_other.setText(mFragment.getString(R.string.walk_in));
                                            if (Utils.isStringNotNullOrEmpty(goods.getBooking().getCustomerNo())) {
                                                goods.btn_add_people_other.setText(goods.getBooking().getCustomerNo());
                                            }
                                            goods.getBooking().setSignId(null);
                                            goods.getBooking().setSignNo(null);
                                            goods.getBooking().setSignType(null);
                                            goods.btn_add_people_index.setText(Constants.STR_EMPTY);
                                            goods.btn_add_people_index
                                                    .setBackground(mFragment.getDrawable(R.drawable.bg_btn_change_user));
                                        }
                                    }
                                    mDate = changeDate;
                                    list.get(position).setBookingDate(changeDate);
                                    tvDate.setText(changeDate);
                                    hsvGoodItemCenter.removeAllViews();

                                    JsonBookingDetailList.DataListItem.BookingAreaListItem item =
                                            areaList.get(0);

                                    areaList.clear();
                                    areaList.add(item);
                                    areaList.get(0).setBookingTime(a);

                                    if (Utils.isStringNotNullOrEmpty(id)) {
                                        JsonBookingDetailList.DataListItem.BookingAreaListItem tr = new JsonBookingDetailList.DataListItem.BookingAreaListItem();
                                        tr.setCourseAreaId(Integer.parseInt(id));
                                        tr.setBookingTime(startTime);
                                        tr.setCourseAreaType(name);
                                        areaList.add(tr);
                                    }
                                    for (int i = 0; i < areaList.size(); i++) {
                                        TeeTimeTitleInOrOutView view = new TeeTimeTitleInOrOutView(mFragment, areaList, mCourseAreaId, hsvGoodItemCenter, TeeTimeAddTitleView.this);
                                        view.setDateOut(areaList.get(i).getCourseAreaType());
                                        if (Utils.isStringNotNullOrEmpty(areaList.get(i).getBookingTime())) {
                                            view.setDateOutValue(areaList.get(i).getBookingTime().substring(0, 5));
                                        }
                                        view.setTag(0);
                                        hsvGoodItemCenter.addView(view);//1612456
                                    }
                                    menuWindow.dismiss();
                                    if (changeBookingTimeListener != null) {
                                        changeBookingTimeListener.change();
                                    }
                                }
                            }, tvDate.getText().toString(), date, areaList.get(0).getBookingTime());
                            menuWindow.setChangeDate(date);
                            menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        }


                    });
                }
            }
        });
        tvDateFlag.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvDate.setTextSize(Constants.FONT_SIZE_SMALLER);

        buttonAddTransferTime = (Button) this.findViewById(R.id.btn_gooditem_add);
        hsvGoodItemCenter = (LinearLayout) this.findViewById(R.id.hsv_gooditem_center);

//        afterShowRightListener = new SwipeLinearLayout.AfterShowRightListener() {
//            @Override
//            public void doAfterShowRight(SwipeLinearLayout swipeLinearLayout) {
//                for (SwipeLinearLayout sll : swipeLinearLayoutList) {
//                    if (swipeLinearLayout != sll) {
//                        sll.hideRight();
//                    }
//                }
//            }
//        };

        //将预约信息填入每一行的Item（TeeTimeAddWithGoodDetailView）
        for (int i = 0; i < bookingList.size(); i++) {
            TeeTimeAddWithGoodDetailView teeTimeAddListItemDetailView
                    = new TeeTimeAddWithGoodDetailView(bookingList, mFragment, dataParameter, mRightWidth, position, i, listener);
            teeTimeAddListItemDetailView.setBookingDate(mDate);
            teeTimeAddListItemDetailView.setTag(i);
            teeTimeAddListItemDetailView.setCourseAreaId(mCourseAreaId);
            itemDetail.addView(teeTimeAddListItemDetailView);
            goodsList.add(teeTimeAddListItemDetailView);
            teeTimeAddListItemDetailView.setAfterShowRightListener(afterShowRightListener);
            swipeLinearLayoutList.add(teeTimeAddListItemDetailView);
        }
        tvDateFlag.setText(mDateFlag);
        for (int i = 0; i < areaList.size(); i++) {
            JsonBookingDetailList.DataListItem.BookingAreaListItem item = areaList.get(i);
            TeeTimeTitleInOrOutView view = new TeeTimeTitleInOrOutView(mFragment, areaList, mCourseAreaId, hsvGoodItemCenter, this);
            view.setDateOut(item.getCourseAreaType());
            if (Utils.isStringNotNullOrEmpty(item.getBookingTime())) {
                view.setDateOutValue(item.getBookingTime().substring(0, 5));
            }
            view.setTag(i);

            //except the first transfer time , other can edit.
            if (i != 0) {
                view.setBookingAreaListItem(item);
                if (Constants.STR_0.equals(item.getBookingSignStatus())) {
                    view.setClickAble(false);
                } else {
                    view.setClickAble(true);
                }
                view.setDate(mDate);
            }
            hsvGoodItemCenter.addView(view);
        }
        buttonAddTransferTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanEditTransferTime) {
                    if (!PlayerPastBookingFragment.class.getName().equals(mFromPage)) {
                        if (!Constants.STR_1.equals(dataParameter.getDataList().getBookingStatus())) {
                            netLinkChangeTransferArea();
                        }
                    }
                }
            }
        });
    }

    private boolean checkCheckIn() {

        for (JsonBookingDetailList.DataListItem.BookingListItem item : bookingList) {

            if (item.getBookingStatus() != null && item.getBookingStatus() == 1) {
                return true;
            }
        }
        return false;
    }

    public void resetHsvGoodItemCenterViewTag(int tag) {
        areaList.remove(tag);
        if (hsvGoodItemCenter != null) {
            for (int i = 0; i < hsvGoodItemCenter.getChildCount(); i++) {
                TeeTimeTitleInOrOutView nn = (TeeTimeTitleInOrOutView) hsvGoodItemCenter.getChildAt(i);
                nn.setTag(i);
            }
        }
    }

    public void netLinkChangeTransferArea() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mFragment.getActivity()));
        params.put(ApiKey.TEE_TIME_DATE, mDate);
        params.put(ApiKey.TEE_TIME_COURSE_ID, AppUtils.getCurrentCourseId(mFragment.getActivity()));
        params.put(ApiKey.TEE_TIME_AREA_ID, String.valueOf(areaList.get(areaList.size() - 1).getCourseAreaId()));
        params.put(ApiKey.TEE_TIME_TIME, areaList.get(areaList.size() - 1).getBookingTime());
        HttpManager<JsonChangeTransferArea> hh = new HttpManager<JsonChangeTransferArea>(mFragment) {

            @Override
            public void onJsonSuccess(JsonChangeTransferArea jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    final JsonChangeTransferArea.DefaultItem defaultTransferTimeItem = jo.getDataList().defaultItem;
                    // user make confirm.
                    if (defaultTransferTimeItem.holeOnly == 1) {
                        SelectTransferTime9HoleConfirmPopupWindow.SaveConfirmListener listener = new SelectTransferTime9HoleConfirmPopupWindow.SaveConfirmListener() {
                            @Override
                            public void onClickSave() {
                                JsonBookingDetailList.DataListItem.BookingAreaListItem bookingAreaListItem = new JsonBookingDetailList.DataListItem.BookingAreaListItem();
                                if (Utils.isStringNotNullOrEmpty(defaultTransferTimeItem.areaId)) {
                                    bookingAreaListItem.setBookingTime(defaultTransferTimeItem.time);
                                    bookingAreaListItem.setCourseAreaType(defaultTransferTimeItem.areaType);
                                    bookingAreaListItem.setCourseAreaId(Integer.valueOf(defaultTransferTimeItem.areaId));
                                    bookingAreaListItem.setTransferFlag(Constants.STR_1);
                                    areaList.add(bookingAreaListItem);
                                    TeeTimeTitleInOrOutView view = new TeeTimeTitleInOrOutView(mFragment, areaList, mCourseAreaId, hsvGoodItemCenter, TeeTimeAddTitleView.this);
                                    view.setTag(areaList.size() - 1);
                                    view.setDateOut(defaultTransferTimeItem.areaType);
                                    if (defaultTransferTimeItem.time.length() > 0) {
                                        view.setDateOutValue(defaultTransferTimeItem.time.substring(0, 5));
                                    }
                                    view.setBookingAreaListItem(bookingAreaListItem);
                                    view.setClickAble(true);
                                    view.setDate(mDate);
                                    hsvGoodItemCenter.addView(view);
                                }
                            }
                            @Override
                            public void onClickCancel() {
                            }
                        };
                        SelectTransferTime9HoleConfirmPopupWindow savePopupWindow = new SelectTransferTime9HoleConfirmPopupWindow(mFragment
                                , mFragment.getString(R.string.error_mes00006), listener);
                        savePopupWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    } else {
                        JsonBookingDetailList.DataListItem.BookingAreaListItem bookingAreaListItem = new JsonBookingDetailList.DataListItem.BookingAreaListItem();
                        if (Utils.isStringNotNullOrEmpty(defaultTransferTimeItem.areaId)) {
                            bookingAreaListItem.setBookingTime(defaultTransferTimeItem.time);
                            bookingAreaListItem.setCourseAreaType(defaultTransferTimeItem.areaType);
                            bookingAreaListItem.setCourseAreaId(Integer.valueOf(defaultTransferTimeItem.areaId));
                            bookingAreaListItem.setTransferFlag(Constants.STR_1);
                            areaList.add(bookingAreaListItem);
                            TeeTimeTitleInOrOutView view = new TeeTimeTitleInOrOutView(mFragment, areaList, mCourseAreaId, hsvGoodItemCenter, TeeTimeAddTitleView.this);
                            view.setTag(areaList.size() - 1);
                            view.setDateOut(defaultTransferTimeItem.areaType);
                            if (defaultTransferTimeItem.time.length() > 0) {
                                view.setDateOutValue(defaultTransferTimeItem.time.substring(0, 5));
                            }
                            view.setBookingAreaListItem(bookingAreaListItem);
                            view.setClickAble(true);
                            view.setDate(mDate);
                            hsvGoodItemCenter.addView(view);
                        }
                    }

                }
            }
            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(mFragment.getActivity(), ApiManager.HttpApi.ChangeTransferArea, params);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public List<JsonBookingDetailList.DataListItem.BookingListItem> getBookingList() {
        return bookingList;
    }

    public SwipeLinearLayout.AfterShowRightListener getAfterShowRightListener() {
        return afterShowRightListener;
    }

    public void setIsCanEditTransferTime(boolean isCanEditTransferTime) {
        this.isCanEditTransferTime = isCanEditTransferTime;
    }

    public interface OnTwoWheelClickListener {
        void onTransferTimeReturn(String a, JsonChangeTransferArea.TransferTimeItem b);
    }

    public interface ChangeTimeListener {
        void clickOk(String a, String changeDate, String startTime, String id, String name);
    }

    public interface ChangeBookingTimeListener {
        void change();
    }
}
