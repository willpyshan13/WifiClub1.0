/**
 * Project Name: itee
 * File Name:	 TeeTimeAddWithGoodDetailView.java
 * Package Name: cn.situne.itee.view
 * Date:		 2015-01-20
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */

package cn.situne.itee.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.activity.MainActivity;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.quick.SearchBookingFragment;
import cn.situne.itee.fragment.teetime.RightDrawableOnTouchListener;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.fragment.teetime.TeeTimeCheckInFragment;
import cn.situne.itee.fragment.teetime.TeeTimeChooseCustomerFragment;
import cn.situne.itee.fragment.teetime.TeeTimeChoosePriceFragment;
import cn.situne.itee.fragment.teetime.TeeTimeDepositAddFragment;
import cn.situne.itee.fragment.teetime.TeeTimeMemberClassifySearchFragment;
import cn.situne.itee.fragment.teetime.TeeTimeSigneClassifySearchFragment;
import cn.situne.itee.fragment.teetime.TeeTimeSignedByFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.manager.jsonentity.JsonCustomersBooking;
import cn.situne.itee.manager.jsonentity.JsonSignData;
import cn.situne.itee.manager.jsonentity.jsonCheckPricingData;
import cn.situne.itee.view.popwindow.SelectCaddiePopupWindow;
import cn.situne.itee.view.popwindow.SelectSaveOrNotPopupWindow;
import cn.situne.itee.view.popwindow.SelectShoesPopupWindow;
import cn.situne.itee.view.popwindow.ShoppingDeleteTimesPopupWindow;

/**
 * ClassName:CourseHoleSettingFragment <br/>
 * Function: Detail view of goods in TeeTimeAddFragment. <br/>
 * UI:  04-1
 * Date: 2015-01-20 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class TeeTimeAddWithGoodDetailView extends SwipeLinearLayout {

    public IteeTextView btn_add_people_other;
    public IteeTextView btn_add_people_index;
    public IteeTextView tvGooditemDeposit, tvGooditemCheckin, tvGooditemDelete, tvGooditemUndoCheckIn;
    public ITeeClearUpEditText etGoodItemName;
    public int position, parentPosition;
    private LayoutInflater mInflater;
    private int mRightViewWidth;
    private LinearLayout item_left;
    private LinearLayout item_right, ll_gooditem_container;
    private BaseFragment mFragment;
    private ImageView leftArrow;
    private int width;
    private int mNowPage;
    private boolean isCanEdit = true;
    private Integer courseAreaId;

    private String upIdStrs;

    public JsonBookingDetailList.DataListItem.BookingListItem getBooking() {
        return booking;
    }

    public Integer getCourseAreaId() {
        return courseAreaId;
    }

    public void setCourseAreaId(Integer courseAreaId) {
        this.courseAreaId = courseAreaId;
    }

    public void setIsCanEdit(boolean isCanEdit) {
        this.isCanEdit = isCanEdit;
    }

    //    private boolean isEditTextCanEdit = true;
    private List<JsonCustomersBooking.CategoryCourseListItem> mCategoryCourseList;
    private JsonBookingDetailList dataParameter;

    private String bookingDate;

    private List<JsonBookingDetailList.DataListItem.BookingListItem> mBookingListItemList;

    //good use
    private List<JsonBookingDetailList.DataListItem.GoodListItem> dataGoodList;

    private JsonBookingDetailList.DataListItem.BookingListItem booking;

    public TextWatcher textWatcher = new TextWatcher() {

        private CharSequence temp;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            TeeTimeAddFragment teeTimeAddFragment = (TeeTimeAddFragment) mFragment;
            teeTimeAddFragment.setBackArrowListener();

            booking.setCustomerName(temp.toString());
//            booking.setCustomerId(null);
//            booking.setCustomerNo(null);
//            booking.setCustomerType(null);
//            if (Constants.STR_1.equals(booking.getSignType())) {
//                btn_add_people_other.setText(mFragment.getString(R.string.tee_time_guest));
//            } else {
//                btn_add_people_other.setText(mFragment.getString(R.string.walk_in));
//            }
        }
    };
    private OnGoodItemClickListener onGoodItemClickListener;
    private TeeTimeAddFragment.OnPopupWindowWheelClickListener parentListener;

    public TeeTimeAddWithGoodDetailView(List<JsonBookingDetailList.DataListItem.BookingListItem> bookingListItemList, BaseFragment mFragment, JsonBookingDetailList dataParameter, int mRightViewWidth, int parentPosition, int position,
                                        TeeTimeAddFragment.OnPopupWindowWheelClickListener listener) {
        super(mFragment.getActivity(), mRightViewWidth);
        this.mFragment = mFragment;
        this.parentListener = listener;
        this.position = position;
        this.parentPosition = parentPosition;
        this.mRightViewWidth = mRightViewWidth;
        this.mCategoryCourseList = dataParameter.getDataList().getCategoryCourseList();
        this.dataParameter = dataParameter;
        this.booking = bookingListItemList.get(position);
        mBookingListItemList = bookingListItemList;
        imgList = new ArrayList<>();
        mInflater = (LayoutInflater) mFragment.getActivity()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        initView();
    }

    private void initView() {
        mInflater.inflate(R.layout.rl_good_item, this);
        this.setPadding(0, 0, -mRightViewWidth, 0);
        setmRightViewWidth(mRightViewWidth);
        if (booking.getBookingStatus() != null) {
            if (booking.getBookingStatus() == 2) {
                isCanEdit = false;
            }
            if (Constants.STR_1.equals(dataParameter.getDataList().getBookingStatus())) {
                isCanEdit = false;
                setmRightViewWidth(0);
            }
        }
        item_left = (LinearLayout) findViewById(R.id.rl_good_item_left);
        item_right = (LinearLayout) findViewById(R.id.ll_good_item_right);
        LayoutParams layoutParams = new LayoutParams(mRightViewWidth, LayoutParams.MATCH_PARENT);
        item_right.setLayoutParams(layoutParams);
        ll_gooditem_container = (LinearLayout) findViewById(R.id.ll_good_item_container);

        width = getResources().getDisplayMetrics().widthPixels;
        LayoutParams layoutParams1 = (LayoutParams) item_left.getLayoutParams();
        layoutParams1.width = width;
        item_left.setLayoutParams(layoutParams1);

        LayoutParams layoutParams2 = (LayoutParams) item_right.getLayoutParams();
        layoutParams2.width = mRightViewWidth;
        item_right.setLayoutParams(layoutParams2);

        btn_add_people_other = (IteeTextView) findViewById(R.id.btn_add_people_other);
        btn_add_people_index = (IteeTextView) findViewById(R.id.btn_add_people_index);

        btn_add_people_other.setGravity(Gravity.CENTER);
        btn_add_people_index.setGravity(Gravity.CENTER);

        //swipe right layout.
        tvGooditemDeposit = (IteeTextView) findViewById(R.id.btn_good_item_deposit);
        //预约点进去和客人列表左滑出现的“开卡、删除”按钮
        tvGooditemCheckin = (IteeTextView) findViewById(R.id.btn_good_item_check_in);
        tvGooditemDelete = (IteeTextView) findViewById(R.id.btn_good_item_delete);
        tvGooditemUndoCheckIn = (IteeTextView) findViewById(R.id.btn_good_item_undo_check_in);
        etGoodItemName = (ITeeClearUpEditText) findViewById(R.id.et_good_item_name);
        LinearLayout p = (LinearLayout) etGoodItemName.getParent();

        etGoodItemName.setIconHeight(mFragment, p.getLayoutParams().height);

        etGoodItemName.setOnTouchListener(new RightDrawableOnTouchListener(etGoodItemName) {
            @Override
            public boolean onDrawableTouch(final MotionEvent event) {
                Utils.hideKeyboard(mFragment.getBaseActivity());
//                Bundle bundle = new Bundle();
//                bundle.putString(TransKey.TEE_TIME_USER_TYPE, Constants.STR_EMPTY);
//                bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE, Constants.STR_1);
//                bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, Constants.STR_EMPTY);
//                bundle.putString(TransKey.COMMON_FROM_PAGE,TeeTimeAddFragment.class.getName());
//                bundle.putString("nowSignType",Constants.STR_1);
//                bundle.putString("notCancel", Constants.STR_1);
//                bundle.putString(TransKey.BOOKING_DATE, bookingDate);
//                bundle.putBoolean("isClickAdd", true);
//                push(TeeTimeMemberClassifySearchFragment.class, bundle);

                etGoodItemName.setText(Constants.STR_EMPTY);
                return false;
            }
        });

        tvGooditemDeposit.setGravity(Gravity.CENTER);
        tvGooditemCheckin.setGravity(Gravity.CENTER);
        tvGooditemDelete.setGravity(Gravity.CENTER);
        tvGooditemUndoCheckIn.setGravity(Gravity.CENTER);

        tvGooditemDeposit.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvGooditemCheckin.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvGooditemDelete.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvGooditemUndoCheckIn.setTextSize(Constants.FONT_SIZE_SMALLER);
        tvGooditemUndoCheckIn.setSingleLine(false);

        initDataOrListener();

        etGoodItemName.setBackgroundResource(R.drawable.btn_change_user_no_content_long);
    }

    private boolean isHasNextPage(int nowPage) {
        int allData = dataGoodList.size();
        int resultInt;
        if (nowPage == 0) {
            resultInt = allData - 4;
            if (resultInt > 0) {
                return true;
            }
        } else {
            resultInt = (allData - 4) - (nowPage * 4);
            if (resultInt > 0) {
                return true;
            }
        }
        return false;
    }

    private void rightArrowView() {
        ImageView rightArrow = new ImageView(mFragment.getActivity());
        ll_gooditem_container.addView(rightArrow);
        rightArrow.setImageResource(R.drawable.icon_rental_item_right_arrow);
        LinearLayout.LayoutParams layoutLeftArrow = (LayoutParams) rightArrow.getLayoutParams();
        layoutLeftArrow.height = DensityUtil.getActualWidthOnThisDevice(100, mFragment.getActivity());
        layoutLeftArrow.width = DensityUtil.getActualWidthOnThisDevice(100, mFragment.getActivity());
        rightArrow.setScaleType(ImageView.ScaleType.FIT_XY);
        rightArrow.setLayoutParams(layoutLeftArrow);
        rightArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGoodPage(++mNowPage);
            }
        });
    }

    private boolean addRightArrow(int nowPage) {
        int allData = dataGoodList.size();
        int resultInt;
        if (nowPage == 0) {
            resultInt = allData - 5;
            if (resultInt > 0) {
                rightArrowView();
            }
        } else {
            resultInt = (allData - 4) - (nowPage * 3);
            if (resultInt > 0) {
                rightArrowView();
            }
        }
        return false;
    }


    public void initImage() {
        for (int i = 0; i < imgList.size(); i++) {
            ImageView imageView = imgList.get(i);

            JsonBookingDetailList.DataListItem.GoodListItem goodListItem = (JsonBookingDetailList.DataListItem.GoodListItem) imageView.getTag();
            goodListItem.setGoodsId(null);
            goodListItem.setGoodsPrice(null);
            goodListItem.setCaddieNo(null);
            goodListItem.setGoodsLastAttr(null);

            switch (goodListItem.getCategoryId()) {
                case 1:
                    imageView.setBackgroundResource(R.drawable.caddie_off);
                    break;
                case 2:
                    imageView.setBackgroundResource(R.drawable.cart_off);
                    break;
                case 3:
                    imageView.setBackgroundResource(R.drawable.club_off);
                    break;
                case 4:
                    imageView.setBackgroundResource(R.drawable.shoes_off);
                    break;
                case 5:
                    imageView.setBackgroundResource(R.drawable.trolley_off);
                    break;
                case 6:
                    imageView.setBackgroundResource(R.drawable.umbrella_off);
                    break;
                case 7:
                    imageView.setBackgroundResource(R.drawable.towel_off);
                    break;
                default:
                    return;
            }
        }
    }

    private List<ImageView> imgList;

    private void goodItemView(final JsonBookingDetailList.DataListItem.GoodListItem goodListItem) {
        ImageView imageView = new ImageView(mFragment.getActivity());
        switch (goodListItem.getCategoryId()) {
            case 1:
                if (goodListItem.getGoodsId() != null) {
                    imageView.setBackgroundResource(R.drawable.caddie_on);
                } else {
                    imageView.setBackgroundResource(R.drawable.caddie_off);
                }
                break;
            case 2:
                if (goodListItem.getGoodsId() != null) {
                    imageView.setBackgroundResource(R.drawable.cart_on);
                } else {
                    imageView.setBackgroundResource(R.drawable.cart_off);
                }
                break;
            case 3:
                if (goodListItem.getGoodsId() != null) {
                    imageView.setBackgroundResource(R.drawable.club_on);
                } else {
                    imageView.setBackgroundResource(R.drawable.club_off);
                }
                break;
            case 4:
                if (goodListItem.getGoodsId() != null) {
                    imageView.setBackgroundResource(R.drawable.shoes_on);
                } else {
                    imageView.setBackgroundResource(R.drawable.shoes_off);
                }
                break;
            case 5:
                if (goodListItem.getGoodsId() != null) {
                    imageView.setBackgroundResource(R.drawable.trolley_on);
                } else {
                    imageView.setBackgroundResource(R.drawable.trolley_off);
                }
                break;
            case 6:
                if (goodListItem.getGoodsId() != null) {
                    imageView.setBackgroundResource(R.drawable.umbrella_on);
                } else {
                    imageView.setBackgroundResource(R.drawable.umbrella_off);
                }
                break;
            case 7:
                if (goodListItem.getGoodsId() != null) {
                    imageView.setBackgroundResource(R.drawable.towel_on);
                } else {
                    imageView.setBackgroundResource(R.drawable.towel_off);
                }
                break;
            default:
                return;
        }

        ll_gooditem_container.addView(imageView);
        imgList.add(imageView);
        imageView.setTag(goodListItem);

        LinearLayout.LayoutParams layoutIm = (LayoutParams) imageView.getLayoutParams();
        layoutIm.height = DensityUtil.dip2px(mFragment.getActivity(), 50);
        layoutIm.width = DensityUtil.dip2px(mFragment.getActivity(), 50);
        imageView.setLayoutParams(layoutIm);
        imageView.setTag(goodListItem);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (booking.getBookingStatus() == null || booking.getBookingStatus() == 0 || booking.getBookingStatus() == 2) {
                    Utils.hideKeyboard(mFragment.getActivity());
                    if (isCanEdit) {
                        JsonBookingDetailList.DataListItem.GoodListItem goodListItem1 = (JsonBookingDetailList.DataListItem.GoodListItem) v.getTag();
                        JsonCustomersBooking.CategoryCourseListItem temp = null;
                        for (int i = 0; i < mCategoryCourseList.size(); i++) {
                            JsonCustomersBooking.CategoryCourseListItem temp1 = mCategoryCourseList.get(i);
                            if (temp1.id.equals(goodListItem1.getCategoryId())) {
                                temp = temp1;
                            }
                        }

                        if (temp != null) {
                            if (temp.status == 1) {
                                if (goodListItem1.getCategoryId() == 1) {
                                    String bookingTime = dataParameter.getDataList().getList().get(parentPosition).getAreaList().get(0).getBookingTime();
                                    String bookingDateTime = bookingDate + Constants.STR_SPACE + bookingTime;
                                    SelectCaddiePopupWindow menuWindow
                                            = new SelectCaddiePopupWindow(mFragment, v, onGoodItemClickListener, bookingDateTime);
                                    menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                                } else {
                                    SelectShoesPopupWindow menuWindow
                                            = new SelectShoesPopupWindow(mFragment, v, String.valueOf(goodListItem1.getCategoryId()), onGoodItemClickListener);
                                    menuWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                                }
                            } else {
                                if (goodListItem1.getGoodsId() == null) {
                                    goodListItem1.setGoodsPrice(temp.price);
                                    goodListItem1.setGoodsId(temp.productId);
                                } else {
                                    goodListItem1.setGoodsPrice(null);
                                    goodListItem1.setGoodsId(null);
                                }

                                switch (goodListItem1.getCategoryId()) {
                                    case 1:
                                        if (goodListItem1.getGoodsId() != null) {
                                            v.setBackgroundResource(R.drawable.caddie_on);
                                        } else {
                                            v.setBackgroundResource(R.drawable.caddie_off);
                                        }
                                        break;
                                    case 2:
                                        if (goodListItem1.getGoodsId() != null) {
                                            v.setBackgroundResource(R.drawable.cart_on);
                                        } else {
                                            v.setBackgroundResource(R.drawable.cart_off);
                                        }
                                        break;
                                    case 3:
                                        if (goodListItem1.getGoodsId() != null) {
                                            v.setBackgroundResource(R.drawable.club_on);
                                        } else {
                                            v.setBackgroundResource(R.drawable.club_off);
                                        }
                                        break;
                                    case 4:
                                        if (goodListItem1.getGoodsId() != null) {
                                            v.setBackgroundResource(R.drawable.shoes_on);
                                        } else {
                                            v.setBackgroundResource(R.drawable.shoes_off);
                                        }
                                        break;
                                    case 5:
                                        if (goodListItem1.getGoodsId() != null) {
                                            v.setBackgroundResource(R.drawable.trolley_on);
                                        } else {
                                            v.setBackgroundResource(R.drawable.trolley_off);
                                        }
                                        break;
                                    case 6:
                                        if (goodListItem1.getGoodsId() != null) {
                                            v.setBackgroundResource(R.drawable.umbrella_on);
                                        } else {
                                            v.setBackgroundResource(R.drawable.umbrella_off);
                                        }
                                        break;
                                    case 7:
                                        if (goodListItem1.getGoodsId() != null) {
                                            v.setBackgroundResource(R.drawable.towel_on);
                                        } else {
                                            v.setBackgroundResource(R.drawable.towel_off);
                                        }
                                        break;
                                    default:
                                        break;
                                }
                                temp.isCheck = !temp.isCheck;
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * user turn on card
     *
     * @param status 1:undo check in 0: check in
     */
    public void netLinkTurnOnCard(final String status, String bdpId, String times) {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mFragment.getActivity()));
        params.put(ApiKey.VERIFY_NO, "1");
        params.put(ApiKey.BOOKING_NO, String.valueOf(booking.getBookingNo()));
        params.put(ApiKey.BOOKING_STATUS, status);
        params.put(ApiKey.SHOPPING_PRICING_TIMES_FLG, booking.getPricingTimesFlg());
        params.put(ApiKey.SHOPPING_PRICING_TIMES, times);
        params.put(ApiKey.SHOPPING_PRICING_BDP_ID, bdpId);

        HttpManager<JsonBookingDetailList> hh = new HttpManager<JsonBookingDetailList>(mFragment) {
            @Override
            public void onJsonSuccess(JsonBookingDetailList jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    TeeTimeAddFragment teeTimeAddFragment = (TeeTimeAddFragment) mFragment;
                    teeTimeAddFragment.refreshView(parentPosition, position);

                    if ("1".equals(status)) {
                        booking.setBookingStatus(0);
                    } else {
                        booking.setBookingStatus(1);
                    }
                } else {
                    Utils.showShortToast(mFragment.getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(mFragment.getActivity(), ApiManager.HttpApi.TurnOnCard, params);
    }

    private void changeGoodPage(int nowPage) {
        mNowPage = nowPage;
        int allData = dataGoodList.size();
        ll_gooditem_container.removeAllViews();

        leftArrow = new ImageView(mFragment.getActivity());
        ll_gooditem_container.addView(leftArrow);
        leftArrow.setImageResource(R.drawable.icon_rental_item_left_arrow);
        LinearLayout.LayoutParams layoutLeftArrow = (LayoutParams) leftArrow.getLayoutParams();
        layoutLeftArrow.height = DensityUtil.getActualWidthOnThisDevice(100, mFragment.getActivity());
        layoutLeftArrow.width = DensityUtil.getActualWidthOnThisDevice(100, mFragment.getActivity());
        leftArrow.setLayoutParams(layoutLeftArrow);

        leftArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGoodPage(--mNowPage);
            }
        });
        if (nowPage == 0) {
            leftArrow.setVisibility(GONE);
        } else {
            leftArrow.setVisibility(VISIBLE);
        }

        if (isHasNextPage(nowPage)) {
            if (nowPage == 0) {
                int tempCount = 4;
                for (int i = 0; i < tempCount; i++) {
                    goodItemView(dataGoodList.get(i));
                }
            } else {
                int tempCount = 3;
                for (int i = 0; i < tempCount; i++) {
                    goodItemView(dataGoodList.get(i + (3 * nowPage) + 1));
                }
            }
        } else {
            if (nowPage == 0) {
                for (int i = 0; i < allData; i++) {
                    goodItemView(dataGoodList.get(i));
                }
            } else {
                int tempCount = allData - 4;
                if (nowPage != 1) {
                    tempCount = tempCount - ((nowPage - 1) * 3);
                }
                for (int i = 0; i < tempCount; i++) {
                    goodItemView(dataGoodList.get(i + (3 * nowPage) + 1));
                }
            }
        }
        addRightArrow(nowPage);
    }

    private void initDataOrListener() {
        etGoodItemName.setHint(getResources().getString(R.string.common_name));
        etGoodItemName.setPadding(20, 0, 0, 0);
        dataGoodList = new ArrayList<>();

        if (mCategoryCourseList != null) {
            for (int i = 0; i < mCategoryCourseList.size(); i++) {
                JsonBookingDetailList.DataListItem.GoodListItem goodListItem = new JsonBookingDetailList.DataListItem.GoodListItem();
                goodListItem.setCategoryId(mCategoryCourseList.get(i).id);
                if (booking.getGoodsList() != null) {
                    for (int j = 0; j < booking.getGoodsList().size(); j++) {
                        JsonBookingDetailList.DataListItem.GoodListItem goodListItem1 = booking.getGoodsList().get(j);
                        if (goodListItem.getCategoryId().intValue() == goodListItem1.getCategoryId().intValue()) {
                            goodListItem.setGoodsId(goodListItem1.getGoodsId());
                            goodListItem.setGoodsLastAttr(goodListItem1.getGoodsLastAttr());
                            goodListItem.setGoodsPrice(goodListItem1.getGoodsPrice());
                            goodListItem.setCaddieNo(goodListItem1.getCaddieNo());
                        }
                    }
                }
                if (goodListItem.getCategoryId() != null && goodListItem.getCategoryId() > 0) {
                    dataGoodList.add(goodListItem);
                }
            }
            booking.setGoodsList(dataGoodList);
        }
        //set  goods layouts
        changeGoodPage(0);

        //set layout by bookingStatus.
        if (booking.getBookingStatus() != null) {
            switch (booking.getBookingStatus()) {
                case 0:
                    tvGooditemCheckin.setVisibility(VISIBLE);
                    tvGooditemDelete.setVisibility(VISIBLE);
                    tvGooditemUndoCheckIn.setVisibility(View.GONE);
                    tvGooditemDeposit.setVisibility(View.GONE);
                    break;
                case 1:
                    tvGooditemCheckin.setVisibility(GONE);
                    tvGooditemDelete.setVisibility(GONE);
                    tvGooditemUndoCheckIn.setVisibility(VISIBLE);
                    tvGooditemDeposit.setVisibility(VISIBLE);
                    break;
                case 2:
                    //check in and payed.can't drag.
                    setmRightViewWidth(0);
                    break;
                default:
                    break;
            }
        }

        if (Utils.isStringNotNullOrEmpty(booking.getCustomerId()) && Utils.isStringNotNullOrEmpty(booking.getCustomerNo())) {
            btn_add_people_other.setText(booking.getCustomerNo());
        } else {
            btn_add_people_other.setText(getResources().getString(R.string.walk_in));
        }
        if (Utils.isStringNotNullOrEmpty(booking.getSignId()) && Constants.STR_1.equals(booking.getSignType())) {
            btn_add_people_other.setText(getResources().getString(R.string.tee_time_guest));
        }
        etGoodItemName.removeTextChangedListener(textWatcher);
        etGoodItemName.setText(booking.getCustomerName());
        etGoodItemName.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etGoodItemName.setId(View.generateViewId());
        if (isCanEdit) {
            etGoodItemName.addTextChangedListener(textWatcher);
        } else {
            etGoodItemName.setEnabled(false);
        }

        if ((Utils.isStringNotNullOrEmpty(booking.getSignId()) && Utils.isStringNotNullOrEmpty(booking.getSignType()))) {
            switch (booking.getSignType()) {
                case "1":
                    btn_add_people_index.setText(booking.getSignNo());
                    break;
                case "2":
                    if (Utils.isStringNullOrEmpty(booking.getSignCode())) {
                        btn_add_people_index.setText(getResources().getString(R.string.tee_time_agent));
                    } else {
                        btn_add_people_index.setText(booking.getSignCode());
                    }
                    break;
                case "3":
                    btn_add_people_index.setText(getResources().getString(R.string.tee_time_event));
                    break;
            }
            btn_add_people_index.setBackgroundResource(R.drawable.btn_change_user_no_content);
        }

        if (AppUtils.isAgent(mFragment.getActivity())) {
            btn_add_people_index.setText(getResources().getString(R.string.tee_time_agent));
            btn_add_people_index.setBackgroundResource(R.drawable.btn_change_user_no_content);
            btn_add_people_index.setEnabled(false);
        }

        btn_add_people_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!AppUtils.isAgent(mFragment.getActivity())) {
                    if (isCanEdit) {
                        //TODO
                        String name = etGoodItemName.getText().toString();
                        MainActivity mainActivity = mFragment.getBaseActivity();
                        Bundle bundle = new Bundle();
                        bundle.putString("searchName", name.trim());
                        bundle.putInt("position", position);
                        bundle.putInt("parentPosition", parentPosition);

                        if (Utils.isStringNotNullOrEmpty(booking.getCustomerId())) {
                            bundle.putString("choiceId", booking.getCustomerId());
                        }
                        if (Utils.isStringNotNullOrEmpty(booking.getCustomerType())) {
                            bundle.putString("nowSignType", booking.getCustomerType());
                        }

                        Utils.hideKeyboard(mainActivity);
                        bundle.putString(TransKey.BOOKING_DATE, bookingDate);
                        bundle.putString(TransKey.COURSE_AREA_ID, String.valueOf(courseAreaId));
                        bundle.putString(TransKey.COURSE_ADD_MEMBER_NAME, etGoodItemName.getText().toString());
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeAddFragment.class.getName());
                        //  mainActivity.pushFragment(TeeTimeMemberListFragment.class, bundle);
                        if (Utils.isStringNotNullOrEmpty(etGoodItemName.getText().toString().replaceAll(" ", "")) && Utils.isStringNullOrEmpty(booking.getCustomerNo())) {
                            bundle.putString(TransKey.TEE_TIME_USER_TYPE, Constants.STR_EMPTY);
                            bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE, Constants.STR_0);
                            bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, Constants.STR_EMPTY);
                            bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeAddFragment.class.getName());

                            if (Utils.isStringNotNullOrEmpty(booking.getCustomerType())) {
                                bundle.putString("nowSignType", booking.getCustomerType());
                            }
                            bundle.putString("searchName", etGoodItemName.getText().toString());
                            bundle.putInt("position", position);
                            if (Utils.isStringNotNullOrEmpty(booking.getCustomerId())) {
                                bundle.putString("choiceId", booking.getCustomerId());
                            }
                            bundle.putInt("parentPosition", parentPosition);
                            bundle.putString("courseAreaId", String.valueOf(courseAreaId));
                            bundle.putString("fragmentSource", SearchBookingFragment.FRAGMENT_SOURCE_1);
                            bundle.putString("fragmentType", SearchBookingFragment.FRAGMENT_TYPE_2);
                            mainActivity.pushFragment(SearchBookingFragment.class, bundle);
                        } else {
                            if (Utils.isStringNullOrEmpty(booking.getCustomerNo())) {
                                mainActivity.pushFragment(TeeTimeChooseCustomerFragment.class, bundle);
                            } else {
                                bundle.putString(TransKey.TEE_TIME_USER_TYPE, Constants.STR_EMPTY);
                                bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE, booking.getCustomerType());
                                bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, Constants.STR_EMPTY);
                                bundle.putString("notCancel", Constants.STR_0);
                                bundle.putString("customerNo", booking.getCustomerId());
                                mainActivity.pushFragment(TeeTimeMemberClassifySearchFragment.class, bundle);
                            }
                        }
                    }
                }
            }
        });

        btn_add_people_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCanEdit) {
                    Utils.hideKeyboard(mFragment.getActivity());
                    MainActivity mainActivity = mFragment.getBaseActivity();
                    TeeTimeAddFragment teeTimeAddFragment = (TeeTimeAddFragment) mFragment;

                    Bundle bundle = new Bundle();
                    bundle.putInt("fromAdd", position);
                    if (Utils.isStringNotNullOrEmpty(booking.getSignId())) {
                        bundle.putString("signId", booking.getSignId());
                    }
                    bundle.putInt("parentPosition", parentPosition);
                    Map<String, Integer> map = teeTimeAddFragment.getMapSelectCount();
                    String mapString = Utils.getStringFromObject(map);
                    bundle.putString("count", mapString);
                    if (Utils.isStringNotNullOrEmpty(booking.getSignType())) {
                        bundle.putString("nowSignType", String.valueOf(booking.getSignType()));
                    }

                    // role is member , sign can't select member.
                    if (Utils.isStringNotNullOrEmpty(booking.getCustomerType()) && Constants.STR_1.equals(booking.getCustomerType())) {
                        bundle.putBoolean(TransKey.TEE_TIME_MEMBER_NO_MEMBER, true);
                    }

                    String bookingTime = dataParameter.getDataList().getList().get(parentPosition).getAreaList().get(0).getBookingTime();
                    String bookingDateTime = bookingDate + Constants.STR_SPACE + bookingTime;
                    Utils.log("bookingDate : " + bookingDateTime);
                    bundle.putString(TransKey.BOOKING_DATE, bookingDateTime);
                    bundle.putString(TransKey.COURSE_AREA_ID, String.valueOf(courseAreaId));
                    bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeAddFragment.class.getName());
                    bundle.putString("addAgent", "1");
                    //mainActivity.pushFragment(TeeTimeMemberListWithIndexFragment.class, bundle);

                    if (Utils.isStringNullOrEmpty(booking.getSignId())) {
                        netLink(bundle);
                    } else {
                        bundle.putString("nowSignType", String.valueOf(booking.getSignType()));
                        bundle.putString("notCancel", Constants.STR_0);
                        mFragment.getBaseActivity().pushFragment(TeeTimeSigneClassifySearchFragment.class, bundle);
                    }
                }
            }
        });

        tvGooditemDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRight();
                MainActivity mainActivity = mFragment.getBaseActivity();
                Utils.hideKeyboard(mainActivity);
                Bundle bundle = new Bundle();
                bundle.putSerializable("booking", booking);
                mainActivity.pushFragment(TeeTimeDepositAddFragment.class, bundle);
            }
        });

        final List<JsonBookingDetailList.DataListItem.ListItem> list = dataParameter.getDataList().getList();

        //开卡事件
        tvGooditemCheckin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                hideRight();
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlCheckInOrUndo, mFragment.getActivity());
                if (hasPermission) {
                    checkPricing(list);
                } else {
                    AppUtils.showHaveNoPermission(mFragment);
                }
            }
        });

        tvGooditemDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectSaveOrNotPopupWindow savePopupWindow
                        = new SelectSaveOrNotPopupWindow(mFragment, parentListener, 3, parentPosition, position);
                savePopupWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });

        if (Utils.isListNotNullOrEmpty(list)) {
            List<JsonBookingDetailList.DataListItem.BookingListItem> bookingList
                    = list.get(parentPosition).getBookingList();
            if (Utils.isListNotNullOrEmpty(bookingList)) {
                String lockFlag = bookingList.get(position).getLockFlag();
                if (Constants.STR_1.equals(lockFlag)) {
                    tvGooditemUndoCheckIn.setEnabled(false);
                    tvGooditemUndoCheckIn.setBackgroundColor(Color.LTGRAY);
                } else {
                    tvGooditemUndoCheckIn.setEnabled(true);
                    tvGooditemUndoCheckIn.setBackgroundColor(mFragment.getColor(R.color.common_wanted_green));
                }
            }
        }

        tvGooditemUndoCheckIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlCheckInOrUndo, mFragment.getActivity());
                if (hasPermission) {
                    if (!Constants.STR_0.equals(booking.getPricingTimesFlg())) {
                        confirmPopWindow = new ShoppingDeleteTimesPopupWindow(mFragment.getActivity(), new OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Utils.hideKeyboard(mFragment.getBaseActivity());
                                if (Utils.isStringNotNullOrEmpty(confirmPopWindow.getTimes())) {
                                    // mPurchaseItemListener.deleteTimes(playerData.getPricingBdpId(),confirmPopWindow.getTimes(), row, ShoppingPurchaseItem.this);
                                    netLinkTurnOnCard(Constants.STR_1, booking.getPricingBdpId(), confirmPopWindow.getTimes());
                                } else {
                                    Utils.showShortToast(mFragment.getBaseActivity(), AppUtils.generateNotNullMessage(mFragment.getBaseActivity(), R.string.pricing_table_times));
                                }
                            }
                        }, mFragment);
                        confirmPopWindow.setMessage(mFragment.getString(R.string.msg_confirm_pay_wether_to_return_times));
                        confirmPopWindow.showAtLocation(mFragment.getRootView().findViewById(R.id.rl_content_container),
                                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    } else {
                        netLinkTurnOnCard(Constants.STR_1, Constants.STR_1, Constants.STR_1);
                    }
                } else {
                    AppUtils.showHaveNoPermission(mFragment);
                }
            }
        });

        onGoodItemClickListener = new OnGoodItemClickListener() {
            @Override
            public void OnGoodItemClick(int position, View v, String content) {
                JsonBookingDetailList.DataListItem.GoodListItem goodListItem
                        = (JsonBookingDetailList.DataListItem.GoodListItem) v.getTag();
                TeeTimeAddFragment teeTimeAddFragment = (TeeTimeAddFragment) mFragment;
                teeTimeAddFragment.setBackArrowListener();
                switch (goodListItem.getCategoryId()) {
                    case 1:
                        v.setBackgroundResource(R.drawable.caddie_on);
                        break;
                    case 2:
                        v.setBackgroundResource(R.drawable.cart_on);
                        break;
                    case 3:
                        v.setBackgroundResource(R.drawable.club_on);
                        break;
                    case 4:
                        v.setBackgroundResource(R.drawable.shoes_on);
                        break;
                    case 5:
                        v.setBackgroundResource(R.drawable.trolley_on);
                        break;
                    case 6:
                        v.setBackgroundResource(R.drawable.umbrella_on);
                        break;
                    case 7:
                        v.setBackgroundResource(R.drawable.towel_on);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private ShoppingDeleteTimesPopupWindow confirmPopWindow;

    public void removeTextWatch() {
        etGoodItemName.removeTextChangedListener(textWatcher);
    }

    public void addTextWatch() {
        etGoodItemName.addTextChangedListener(textWatcher);
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    /**
     * 单击事件监听器
     */
    public interface OnGoodItemClickListener {
        void OnGoodItemClick(int position, View v, String content);
    }

    //开卡实际执行的代码
    private void checkPricing(final List<JsonBookingDetailList.DataListItem.ListItem> list) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mFragment.getBaseActivity()));
        params.put(ApiKey.SHOPPING_BOOKING_NO, booking.getBookingNo());

        HttpManager<jsonCheckPricingData> hh = new HttpManager<jsonCheckPricingData>(mFragment) {
            @Override
            public void onJsonSuccess(jsonCheckPricingData jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    MainActivity mainActivity = mFragment.getBaseActivity();
                    Utils.hideKeyboard(mainActivity);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("booking", booking);

                    StringBuilder customersType = new StringBuilder();
                    customersType.append(btn_add_people_other.getText().toString());
                    if (Utils.isStringNotNullOrEmpty(btn_add_people_index.getText().toString())) {
                        customersType.append(Constants.STR_BRACKETS_START);
                        customersType.append(btn_add_people_index.getText().toString());
                        customersType.append(Constants.STR_BRACKETS_END);
                    }
                    bundle.putString(TransKey.CUSTOMERS_TYPE, customersType.toString());

                    ArrayList<String> areaListTemp = new ArrayList<>();
                    List<JsonBookingDetailList.DataListItem.BookingAreaListItem> areaList
                            = list.get(parentPosition).getAreaList();
                    for (int i = 0; i < areaList.size(); i++) {
                        String areaString = Utils.getStringFromObject(areaList.get(i));
                        areaListTemp.add(areaString);
                    }
                    bundle.putStringArrayList("areaList", areaListTemp);
                    if (Constants.STR_0.equals(jo.getPricingDisplay())) {
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeAddFragment.class.getName());
                        mainActivity.pushFragment(TeeTimeCheckInFragment.class, bundle);
                    } else {
                        bundle.putString(TransKey.SHOPPING_DETAIL_BOOKING_NO, booking.getBookingNo());
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeAddFragment.class.getName());
                        bundle.putString(TransKey.COMMON_JUMP, TeeTimeChoosePriceFragment.JUMP_FRAGMENT_CHECK_IN);
                        mFragment.push(TeeTimeChoosePriceFragment.class, bundle);
                    }
                } else {
                    Utils.showShortToast(mFragment.getBaseActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(mFragment.getBaseActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(mFragment.getBaseActivity(), ApiManager.HttpApi.xcheckpricing, params);
    }

    private void netLink(final Bundle bundle) {
        upIdStrs = Constants.STR_EMPTY;
        for (int i = 0; i < mBookingListItemList.size(); i++) {
            if (Utils.isStringNotNullOrEmpty(mBookingListItemList.get(i).getCustomerId())) {
                if (mBookingListItemList.get(i).getCustomerId() != null && !mBookingListItemList.get(i).getCustomerId().equals(mBookingListItemList.get(position).getCustomerId())) {
                    upIdStrs = upIdStrs + mBookingListItemList.get(i).getCustomerId() + ",";
                }
            }
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mFragment.getBaseActivity()));
        params.put("member_id_string", upIdStrs);
        if (Utils.isStringNotNullOrEmpty(bookingDate)) {
            params.put(ApiKey.TEE_TIME_SIGN_BOOKING_DATE, bookingDate);
        }

        HttpManager<JsonSignData> hh = new HttpManager<JsonSignData>(mFragment) {
            @Override
            public void onJsonSuccess(JsonSignData jo) {
                int tokenStatus = jo.getReturnCode();
                if (tokenStatus == Constants.RETURN_CODE_20301) {
                    if (Utils.isStringNotNullOrEmpty(jo.getMemberName())) {
                        bundle.putString("nowSignType", Constants.STR_1);
                        if (!Utils.isStringNullOrEmpty(booking.getSignId())) {
                            bundle.putString("notCancel", Constants.STR_0);
                        } else {
                            bundle.putString("notCancel", Constants.STR_1);
                        }
                        bundle.putString("selectAgentId", jo.getMemberName());

                        if (Constants.STR_1.equals(mBookingListItemList.get(position).getCustomerType())) {
                            bundle.putString("notCancel", Constants.STR_1);
                            mFragment.getBaseActivity().pushFragment(TeeTimeSignedByFragment.class, bundle);
                        } else {
                            mFragment.getBaseActivity().pushFragment(TeeTimeSigneClassifySearchFragment.class, bundle);
                        }
                    } else {
                        bundle.putString("notCancel", Constants.STR_1);
                        mFragment.getBaseActivity().pushFragment(TeeTimeSignedByFragment.class, bundle);
                    }
                } else if (tokenStatus == Constants.RETURN_CODE_20302) {

                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(mFragment.getBaseActivity(), ApiManager.HttpApi.SigningGuest, params);
    }
}
