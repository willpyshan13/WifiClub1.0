/**
 * Project Name: itee
 * File Name:  TeeTimeAddFragment.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:   2015-03-11
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.teetime;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jackson.map.ser.BeanPropertyFilter;
import org.codehaus.jackson.map.ser.BeanPropertyWriter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.BookingTimeEntity;
import cn.situne.itee.entity.IncomingCall;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.shopping.ShoppingPaymentFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.manager.jsonentity.JsonCheckInGetData;
import cn.situne.itee.manager.jsonentity.JsonCustomerBookingSearch;
import cn.situne.itee.manager.jsonentity.JsonCustomersBooking;
import cn.situne.itee.manager.jsonentity.JsonLogin;
import cn.situne.itee.manager.jsonentity.JsonOrderGetData;
import cn.situne.itee.manager.jsonentity.JsonReturnBookingOrderNo;
import cn.situne.itee.manager.jsonentity.JsonSigningGuest;
import cn.situne.itee.view.ITeeClearUpEditText;
import cn.situne.itee.view.StickyLayout;
import cn.situne.itee.view.SwipeLinearLayout;
import cn.situne.itee.view.TeeTimeAddTitleView;
import cn.situne.itee.view.TeeTimeAddWithGoodDetailView;
import cn.situne.itee.view.popwindow.ConfirmPopWindow;
import cn.situne.itee.view.popwindow.SelectSaveOrNotPopupWindow;

/**
 * ClassName:TeeTimeAddFragment <br/>
 * Function: order detail fragment.<br/>
 * UI:  04-1
 * Date: 2015-03-11 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class TeeTimeAddFragment extends BaseFragment {
    private Boolean isAdd;
    private ArrayList<BookingTimeEntity> bookingTimeEntities;

    //parent parameter
    private int selectedNum;
    private String title;
    private Integer courseAreaId;
    private String bookingDate;
    private ArrayList<String> startDateTime;
    private ImageView ivShop;
    private RelativeLayout rlOrderPeopleAdd;
    private LinearLayout rlOrderPeopleDetail;

    private ITeeClearUpEditText etName;
    private ITeeClearUpEditText etTel;
    private Button nameBtn;
    private Button telBtn;
    private ScrollView scrollView;
    private StickyLayout stickyLayout;
    private String fromPage;
    private JsonBookingDetailList dataParameter;
    private OnPopupWindowWheelClickListener parentListener;
    private String bookingOrderNo;
    private boolean isFromPay;
    private boolean neebChange;

    TextWatcher mTextWatcher = new TextWatcher() {
        private CharSequence temp = null;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            temp = s;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            neebChange = true;
            setBackArrowListener();
            if (String.valueOf(s).equals(String.valueOf(temp))) {
                neebChange = false;
                setOnBackListener(null);
            }
            JsonBookingDetailList.DataListItem fromDataListItem = dataParameter.getDataList();
            fromDataListItem.setBookingName(temp.toString());
            fromDataListItem.setBookingUserId(null);
        }
    };

    TextWatcher mTextWatcherTel = new TextWatcher() {
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
            neebChange = true;
            setBackArrowListener();
            if (String.valueOf(s).equals(String.valueOf(temp))) {
                neebChange = false;
                setOnBackListener(null);
            }
            JsonBookingDetailList.DataListItem fromDataListItem = dataParameter.getDataList();
            fromDataListItem.setBookingTel(temp.toString());
        }
    };

    //ADD by songyb
    private SwipeLinearLayout.AfterShowRightListener afterShowRightListener = new SwipeLinearLayout.AfterShowRightListener() {
        @Override
        public void doAfterShowRight(SwipeLinearLayout swipeLinearLayout) {
            for (int i = 0; i < rlOrderPeopleDetail.getChildCount(); i++) {
                TeeTimeAddTitleView teeTimeAddTitleView = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
                List<TeeTimeAddWithGoodDetailView> swipeLinearLayoutList = teeTimeAddTitleView.getGoodsList();
                for (SwipeLinearLayout sll : swipeLinearLayoutList) {
                    if (swipeLinearLayout != sll) {
                        sll.hideRight();
                    }
                }
            }
        }
    };

    private Map<String, Integer> mapSelectCount;

    public Map<String, Integer> getMapSelectCount() {
        return mapSelectCount;
    }

    public void setSelectedNum(int selectedNum) {
        this.selectedNum = selectedNum;
    }

    /**
     * In create mode : set user data.
     */
    public void initDataParameter(JsonCustomersBooking jo) {
        etName.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    everyEdittextString(etName.getText().toString());
                }
            }
        });

        List<JsonCustomersBooking.CategoryCourseListItem> categoryCourseList = jo.getDataList().getCategoryCourseList();
        dataParameter = new JsonBookingDetailList(null);
        dataParameter.getDataList().setBookingDate(bookingDate);
        dataParameter.getDataList().setCategoryCourseList(categoryCourseList);

        Map<String, ArrayList<String>> stringListMap = new LinkedHashMap<>();
        for (int i = 0; i < startDateTime.size(); i++) {
            ArrayList<String> values = stringListMap.get(startDateTime.get(i));
            if (values == null) {
                values = new ArrayList<>();
                values.add(startDateTime.get(i));
                stringListMap.put(startDateTime.get(i), values);
            } else {
                values.add(startDateTime.get(i));
                stringListMap.put(startDateTime.get(i), values);
            }
        }

        int count = 0;
        List<JsonBookingDetailList.DataListItem.ListItem> list = new ArrayList<>();
        for (Map.Entry<String, ArrayList<String>> entry : stringListMap.entrySet()) {
            String newTime = entry.getKey();
            String headTypeId = null;
            for (int i = 0; i < bookingTimeEntities.size(); i++) {
                BookingTimeEntity bookingTimeEntity = bookingTimeEntities.get(i);
                if (newTime.equals(bookingTimeEntity.getBookingTime())) {
                    headTypeId = bookingTimeEntity.getSegmentTypeId();
                    //add by songyb  黄金时段，必须3人
                    if (bookingTimeEntity.isPrimeTime() && "3".equals(bookingTimeEntity.getSegmentTimeSetting())) {
                        headTypeId = "6";
                    }
                }
            }

            List<String> bookingCount = entry.getValue();
            JsonBookingDetailList.DataListItem.ListItem listItem = new JsonBookingDetailList.DataListItem.ListItem();
            JsonCustomersBooking.BookingListItem bookingListItem = null;

            if (jo.getDataList().getBookingList() != null && jo.getDataList().getBookingList().size() > 0) {
                bookingListItem = jo.getDataList().getBookingList().get(count);
            }

            List<JsonBookingDetailList.DataListItem.BookingListItem> bookingList = new ArrayList<>();
            JsonCustomersBooking.BookingListItem bookingItem = jo.getDataList().getBookingList().get(count);
            for (int i = 0; i < bookingCount.size(); i++) {
                JsonBookingDetailList.DataListItem.BookingListItem itemBooking = new JsonBookingDetailList.DataListItem.BookingListItem();
                List<JsonBookingDetailList.DataListItem.GoodListItem> goodList = new ArrayList<>();
                itemBooking.setSignId(bookingItem.getSignId());
                itemBooking.setSignType(bookingItem.getSignType());
                itemBooking.setGoodsList(goodList);
                bookingList.add(itemBooking);
            }
            listItem.setBookingList(bookingList);
            ArrayList<JsonBookingDetailList.DataListItem.BookingAreaListItem> bookingAreaList = new ArrayList<>();

            JsonBookingDetailList.DataListItem.BookingAreaListItem bookingAreaListItemFirst = new JsonBookingDetailList.DataListItem.BookingAreaListItem();
            bookingAreaListItemFirst.setCourseAreaId(courseAreaId);
            bookingAreaListItemFirst.setCourseAreaType(title);
            bookingAreaListItemFirst.setTransferFlag(Constants.STR_0);
            bookingAreaListItemFirst.setBookingTime(newTime);
            bookingAreaList.add(bookingAreaListItemFirst);

            //if bookingListItem is null ,do add the second transfer time.
            if (bookingListItem != null && bookingListItem.getTransferAreaId() != null && Utils.isStringNotNullOrEmpty(bookingListItem.getTransferAreaType()) && Utils.isStringNotNullOrEmpty(bookingListItem.getTransferTime())) {
                JsonBookingDetailList.DataListItem.BookingAreaListItem bookingAreaListItem = new JsonBookingDetailList.DataListItem.BookingAreaListItem();
                bookingAreaListItem.setCourseAreaId(bookingListItem.getTransferAreaId());
                bookingAreaListItem.setCourseAreaType(bookingListItem.getTransferAreaType());
                bookingAreaListItem.setBookingTime(bookingListItem.getTransferTime());
                bookingAreaListItem.setTransferFlag(bookingListItem.getTransferFlag());
                bookingAreaList.add(bookingAreaListItem);
            }
            listItem.setAreaList(bookingAreaList);
            list.add(listItem);

            //set segmenttypeid is null, set headTypeID default value.
            if (headTypeId == null) {
                headTypeId = "1";
            }

            TeeTimeAddTitleView n1 = new TeeTimeAddTitleView(this, dataParameter, list, 0, count, courseAreaId, parentListener, Integer.valueOf(headTypeId), fromPage, afterShowRightListener);
            n1.setTag(count);
            n1.setChangeBookingTimeListener(new TeeTimeAddTitleView.ChangeBookingTimeListener() {
                @Override
                public void change() {
                    setBackArrowListener();
                }
            });

            rlOrderPeopleDetail.addView(n1);
            count++;
        }
        dataParameter.getDataList().setList(list);
        etName.addTextChangedListener(mTextWatcher);
        etTel.addTextChangedListener(mTextWatcherTel);
    }

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_newteetimes;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        neebChange = false;
        bookingTimeEntities = new ArrayList<>();
        startDateTime = new ArrayList<>();
        mapSelectCount = new HashMap<>();

        Bundle bundle = getArguments();
        if (bundle != null) {
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, Constants.STR_EMPTY);
            isAdd = bundle.getBoolean(TransKey.TEE_TIME_IS_ADD);
            if (isAdd) {
                title = bundle.getString(TransKey.TEE_TIME_TITLE);
                courseAreaId = bundle.getInt(ApiKey.ADMINISTRATION_COURSE_AREA_ID);
                bookingDate = bundle.getString(ApiKey.ADMINISTRATION_DATE);
                ArrayList<String> entityStringList = bundle.getStringArrayList(ApiKey.ADMINISTRATION_TIME_START);
                for (String entityString : entityStringList) {
                    BookingTimeEntity bookingTimeEntity = (BookingTimeEntity) Utils.getObjectFromString(entityString);
                    bookingTimeEntities.add(bookingTimeEntity);
                    startDateTime.add(bookingTimeEntity.getBookingTime());
                }
                setSelectedNum(bundle.getInt(TransKey.SELECTED_NUM));
                for (int i = 0; i < startDateTime.size(); i++) {
                    String temp = startDateTime.get(i);
                    temp = temp + ":00";

                    BookingTimeEntity bookingTimeEntity = bookingTimeEntities.get(i);
                    bookingTimeEntity.setBookingTime(temp);
                    startDateTime.set(i, temp);
                }
                Collections.sort(startDateTime);
            } else {
                bookingOrderNo = bundle.getString(TransKey.BOOKING_ORDER_NO);
                Utils.log("bookingOrderNo : " + bookingOrderNo);
            }
        }

        stickyLayout = (StickyLayout) rootView.findViewById(R.id.sticky_layout);
        scrollView = (ScrollView) rootView.findViewById(R.id.sticky_content);
        rlOrderPeopleAdd = (RelativeLayout) rootView.findViewById(R.id.sticky_header);

        rlOrderPeopleDetail = new LinearLayout(getActivity());
        rlOrderPeopleDetail.setOrientation(LinearLayout.VERTICAL);
        scrollView.addView(rlOrderPeopleDetail);

        etName = new ITeeClearUpEditText(getActivity());
        etTel = new ITeeClearUpEditText(getActivity());

        nameBtn = new Button(getBaseActivity());
        telBtn = new Button(getBaseActivity());

        parentListener = new OnPopupWindowWheelClickListener() {
            @Override
            public void onSaveClick(String a, String b) {
                switch (a) {
                    case "deleteOrder":
                        String[] temp = b.split(",");
                        deleteCustomersBooking(temp[0], temp[1]);
                        break;
                    default:
                        break;
                }
            }
        };
    }

    public void resetEditView() {
        if (dataParameter != null) {
            if (Constants.STR_1.equals(dataParameter.getDataList().getBookingStatus())) {
                // boolean isCanEdit = false;
                getTvRight().setText(Constants.STR_EMPTY);
                getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
                            @Override
                            public void noDoubleClick(View v) {

                            }
                        });

                etName.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                etTel.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                ivShop.setVisibility(View.INVISIBLE);
                ivShop.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
                    @Override
                    public void noDoubleClick(View v) {

                    }
                });
                etName.setEnabled(false);
                etTel.setEnabled(false);
                nameBtn.setEnabled(false);
                telBtn.setEnabled(false);
            }
            //In edit mode : set user data.
            rlOrderPeopleDetail.removeAllViews();
            JsonBookingDetailList.DataListItem fromDataListItem = dataParameter.getDataList();

            // count now  sign number.
            List<JsonBookingDetailList.DataListItem.BookingListItem> bookingListItemList = new ArrayList<>();
            for (int i = 0; i < fromDataListItem.getList().size(); i++) {
                JsonBookingDetailList.DataListItem.ListItem listItem = fromDataListItem.getList().get(i);
                if (listItem.getBookingList() != null && listItem.getBookingList().size() > 0) {
                    bookingListItemList.addAll(listItem.getBookingList());
                }
            }
            mapSelectCount.clear();
            for (int i = 0; i < bookingListItemList.size(); i++) {
                JsonBookingDetailList.DataListItem.BookingListItem bookingListItem = bookingListItemList.get(i);
                if ("1".equals(bookingListItem.getSignType())) {
                    if (!mapSelectCount.containsKey(bookingListItem.getSignId())) {
                        Integer temp = 1;
                        mapSelectCount.put(bookingListItem.getSignId(), temp);
                    } else {
                        mapSelectCount.put(bookingListItem.getSignId(), mapSelectCount.get(bookingListItem.getSignId()) + 1);
                    }
                }
            }
            int count = 0;
            for (int i = 0; i < fromDataListItem.getList().size(); i++) {
                for (int j = 0; j < fromDataListItem.getList().get(i).getBookingList().size(); j++) {
                    count++;
                }
            }
            setSelectedNum(count);
            getTvLeftTitle().setText(String.format("New Tee Times(%d)", count));
            bookingDate = fromDataListItem.getBookingDate();
            etName.setText(fromDataListItem.getBookingName());
            etTel.setText(fromDataListItem.getBookingTel());

            if (fromDataListItem.getList() != null) {
                boolean hasSomeoneCheckOut = false;
                for (int i = 0; i < fromDataListItem.getList().size(); i++) {
                    JsonBookingDetailList.DataListItem.ListItem listItem = fromDataListItem.getList().get(i);
                    TeeTimeAddTitleView n1 =
                            new TeeTimeAddTitleView(
                                    this,
                                    dataParameter,
                                    fromDataListItem.getList(),
                                    getActualWidthOnThisDevice(300),
                                    i,
                                    courseAreaId,
                                    parentListener,
                                    listItem.getTypeTimeName(),
                                    fromPage,
                                    afterShowRightListener);
                    n1.setTag(i);
                    rlOrderPeopleDetail.addView(n1);

                    n1.setChangeBookingTimeListener(new TeeTimeAddTitleView.ChangeBookingTimeListener() {
                        @Override
                        public void change() {
                            setBackArrowListener();
                        }
                    });

                    for (int goodViewIdx = 0; goodViewIdx < n1.itemDetail.getChildCount(); goodViewIdx++) {
                        if (bookingListItemList.get(goodViewIdx).getBookingStatus() != null) {
                            if (bookingListItemList.get(goodViewIdx).getBookingStatus() == 2) {
                                hasSomeoneCheckOut = true;
                            }
                            if (Constants.STR_1.equals(dataParameter.getDataList().getBookingStatus())) {
                                hasSomeoneCheckOut = true;
                            }
                        }
                    }
                }
                if (hasSomeoneCheckOut) {
                    etName.setEnabled(false);
                    etTel.setEnabled(false);
                    nameBtn.setEnabled(false);
                    telBtn.setEnabled(false);
                }
            }
        }
    }

    public void resetEditView(int parentPosition, int position) {
        if (dataParameter != null) {
            if (Constants.STR_1.equals(dataParameter.getDataList().getBookingStatus())) {
                // boolean isCanEdit = false;
                getTvRight().setText(Constants.STR_EMPTY);
                getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
                                                    @Override
                                                    public void noDoubleClick(View v) {

                                                    }
                                                });
                etName.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                etTel.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return false;
                    }
                });
                ivShop.setVisibility(View.INVISIBLE);
                ivShop.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
                    @Override
                    public void noDoubleClick(View v) {

                    }
                });
                etName.setEnabled(false);
                etTel.setEnabled(false);
                nameBtn.setEnabled(false);
                telBtn.setEnabled(false);
            }
            //In edit mode : set user data.
//            rlOrderPeopleDetail.removeAllViews();
            JsonBookingDetailList.DataListItem fromDataListItem = dataParameter.getDataList();
            // count now  sign number.
            List<JsonBookingDetailList.DataListItem.BookingListItem> bookingListItemList = new ArrayList<>();
            for (int i = 0; i < fromDataListItem.getList().size(); i++) {
                JsonBookingDetailList.DataListItem.ListItem listItem = fromDataListItem.getList().get(i);
                if (listItem.getBookingList() != null && listItem.getBookingList().size() > 0) {
                    bookingListItemList.addAll(listItem.getBookingList());
                }
            }
            mapSelectCount.clear();
            for (int i = 0; i < bookingListItemList.size(); i++) {
                JsonBookingDetailList.DataListItem.BookingListItem bookingListItem = bookingListItemList.get(i);

                if (Constants.STR_1.equals(bookingListItem.getSignType())) {
                    if (!mapSelectCount.containsKey(bookingListItem.getSignId())) {
                        Integer temp = 1;
                        mapSelectCount.put(bookingListItem.getSignId(), temp);
                    } else {
                        mapSelectCount.put(bookingListItem.getSignId(), mapSelectCount.get(bookingListItem.getSignId()) + 1);
                    }
                }
            }
            int count = 0;
            for (int i = 0; i < fromDataListItem.getList().size(); i++) {
                for (int j = 0; j < fromDataListItem.getList().get(i).getBookingList().size(); j++) {
                    count++;
                }
            }
            setSelectedNum(count);
            getTvLeftTitle().setText(String.format("New Tee Times(%d)", count));

            bookingDate = fromDataListItem.getBookingDate();
            etName.setText(fromDataListItem.getBookingName());
            etTel.setText(fromDataListItem.getBookingTel());

            for (int i = 0; i < rlOrderPeopleDetail.getChildCount(); i++) {
                if (i == parentPosition) {
                    TeeTimeAddTitleView titleView = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
                    titleView.itemDetail.removeViewAt(position);
                    titleView.setmCourseAreaId(courseAreaId);
                    TeeTimeAddWithGoodDetailView teeTimeAddListItemDetailView = new TeeTimeAddWithGoodDetailView(bookingListItemList, this, dataParameter, getActualWidthOnThisDevice(300), parentPosition, position, parentListener);
                    teeTimeAddListItemDetailView.setBookingDate(dataParameter.getDataList().getBookingDate());
                    teeTimeAddListItemDetailView.setTag(i);
                    teeTimeAddListItemDetailView.setCourseAreaId(courseAreaId);

                    titleView.itemDetail.addView(teeTimeAddListItemDetailView, position);
                    titleView.getGoodsList().add(teeTimeAddListItemDetailView);
                    teeTimeAddListItemDetailView.setAfterShowRightListener(afterShowRightListener);
                    titleView.swipeLinearLayoutList.add(teeTimeAddListItemDetailView);
                }
            }
        }
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(TransKey.TEE_TIME_USER_TYPE, Constants.STR_EMPTY);
                bundle.putString(TransKey.TEE_TIME_MEMBER_TYPE, Constants.STR_1);
                bundle.putString(TransKey.TEE_TIME_INDEX_TYPE, Constants.STR_EMPTY);
                bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeAddFragment.class.getName());
                bundle.putString("nowSignType", Constants.STR_1);
                bundle.putString("notCancel", Constants.STR_1);
                bundle.putString(TransKey.BOOKING_DATE, bookingDate);
                bundle.putBoolean("isClickAdd", true);
                push(TeeTimeMemberClassifySearchFragment.class, bundle);
            }
        });

        etName.setOnTouchListener(new RightDrawableOnTouchListener(etName) {
            @Override
            public boolean onDrawableTouch(final MotionEvent event) {
                Utils.hideKeyboard(getActivity());
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
                etName.setText(Constants.STR_EMPTY);
                return false;
            }
        });

        telBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(getActivity());
                push(TeeTimeIncomingAddFragment.class);
            }
        });

        etTel.setOnTouchListener(new RightDrawableOnTouchListener(etTel) {
            @Override
            public boolean onDrawableTouch(final MotionEvent event) {
//                Utils.hideKeyboard(getActivity());
                etTel.setText(Constants.STR_EMPTY);
                return false;
            }
        });

        if (!AppUtils.isAgent(mContext)) {
            stickyLayout.setMaxHeaderHeight(240);
            stickyLayout.setOnGiveUpTouchEventListener(new StickyLayout.OnGiveUpTouchEventListener() {
                @Override
                public boolean giveUpTouchEvent(MotionEvent event) {
                    return scrollView.getScrollY() < 0.5;
                }
            });
        }
    }

    /**
     * 每个EditText 都设置值
     *
     * @param name name
     */
    private void everyEdittextString(String name) {
//        if (everyEditTextString) {
        JsonBookingDetailList.DataListItem fromDataListItem = dataParameter.getDataList();
        fromDataListItem.setBookingName(name);
        int count = 0;
        for (int i = 0; i < rlOrderPeopleDetail.getChildCount(); i++) {
            TeeTimeAddTitleView nn = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
            nn.setmCourseAreaId(courseAreaId);
            for (int j = 0; j < nn.itemDetail.getChildCount(); j++) {
                TeeTimeAddWithGoodDetailView bookingList = (TeeTimeAddWithGoodDetailView) nn.itemDetail.getChildAt(j);
                count++;
                if (StringUtils.EMPTY.equals(bookingList.etGoodItemName.getText().toString())) {
                    bookingList.etGoodItemName.setText(name + count);
                    fromDataListItem.getList().get(i).getBookingList().get(j).setCustomerName(name + count);
                }
            }
        }
//            everyEditTextString = false;
//        }
    }

    public void removeEveryEdittextTexWtch() {
        if (rlOrderPeopleDetail != null) {
            for (int i = 0; i < rlOrderPeopleDetail.getChildCount(); i++) {
                TeeTimeAddTitleView nn = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
                nn.setmCourseAreaId(courseAreaId);
                for (int j = 0; j < nn.itemDetail.getChildCount(); j++) {
                    TeeTimeAddWithGoodDetailView bookingList = (TeeTimeAddWithGoodDetailView) nn.itemDetail.getChildAt(j);
                    bookingList.removeTextWatch();
                }
            }
        }
    }

    public void addEveryEdittextTexWtch() {
        if (rlOrderPeopleDetail != null) {

            for (int i = 0; i < rlOrderPeopleDetail.getChildCount(); i++) {
                TeeTimeAddTitleView nn = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
                nn.setmCourseAreaId(courseAreaId);
                for (int j = 0; j < nn.itemDetail.getChildCount(); j++) {
                    TeeTimeAddWithGoodDetailView bookingList = (TeeTimeAddWithGoodDetailView) nn.itemDetail.getChildAt(j);
                    bookingList.addTextWatch();
                }
            }
        }
    }

    private void addAllMember(Bundle bundle) {
        JsonBookingDetailList.DataListItem fromDataListItem = dataParameter.getDataList();
        for (int i = 0; i < rlOrderPeopleDetail.getChildCount(); i++) {
            TeeTimeAddTitleView nn = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
            nn.setmCourseAreaId(courseAreaId);
            for (int j = 0; j < nn.itemDetail.getChildCount(); j++) {
                TeeTimeAddWithGoodDetailView teeTimeAddListItemDetailView = (TeeTimeAddWithGoodDetailView) nn.itemDetail.getChildAt(j);
                String text = teeTimeAddListItemDetailView.btn_add_people_index.getText().toString();
                if (!getString(R.string.tee_time_event).equals(text)) {
                    teeTimeAddListItemDetailView.btn_add_people_index.setBackgroundResource(R.drawable.btn_change_user_no_content);
                    JsonCustomerBookingSearch.Member member1 = (JsonCustomerBookingSearch.Member) bundle.getSerializable("member");

                    if (Utils.isStringNotNullOrEmpty(member1.getMemberNo())) {
                        teeTimeAddListItemDetailView.btn_add_people_index.setText(member1.getMemberNo());

                    } else {
                        teeTimeAddListItemDetailView.btn_add_people_index.setText(getString(R.string.agents_agent));
                    }

                    String mNowSignType = bundle.getString("signType");
                    JsonBookingDetailList.DataListItem.BookingListItem bookingListItem = fromDataListItem.getList().get(i).getBookingList().get(j);
                    bookingListItem.setSignId(String.valueOf(member1.getMemberId()));
                    bookingListItem.setSignNo(member1.getMemberNo());
                    bookingListItem.setSignType(mNowSignType);
                }
            }
        }
    }

    @Override
    protected void reShowWithBackValue() {
        JsonBookingDetailList.DataListItem fromDataListItem = dataParameter.getDataList();
        Bundle bundle = getReturnValues();
        if (bundle != null) {
            neebChange = true;
            setBackArrowListener();
            String mNowSignType = bundle.getString("signType");
            String choosePrice = bundle.getString("TeeTimeChoosePriceFragment", Constants.STR_EMPTY);
            if (Constants.STR_1.equals(choosePrice)) {
                isAdd = false;
            }
            String flag = bundle.getString("fromFlag", StringUtils.EMPTY);
            switch (flag) {
                case "memberName":
                    JsonCustomerBookingSearch.Member member = (JsonCustomerBookingSearch.Member) bundle.getSerializable("member");
                    etName.removeTextChangedListener(mTextWatcher);
                    etTel.removeTextChangedListener(mTextWatcherTel);
                    etName.setText(member.getMemberName());
                    etTel.setText(member.getMemberTel());
                    fromDataListItem.setBookingName(member.getMemberName());
                    fromDataListItem.setBookingTel(member.getMemberTel());
                    fromDataListItem.setBookingUserId(member.getMemberId());

                    etName.addTextChangedListener(mTextWatcher);
                    etTel.addTextChangedListener(mTextWatcherTel);
                    everyEdittextString(member.getMemberName());

                    if ("2".equals(mNowSignType)) {
                        addAllMember(bundle);
                    }
                    break;
                case "memberTel":
                    IncomingCall incomingCall = (IncomingCall) bundle.getSerializable("memberTel");
                    etName.removeTextChangedListener(mTextWatcher);
                    etTel.removeTextChangedListener(mTextWatcherTel);
                    if (incomingCall != null) {
                        if (incomingCall.getName() != null) {
                            etName.setText(incomingCall.getName());
                        }
                        etTel.setText(incomingCall.getTel());

                        fromDataListItem.setBookingName(incomingCall.getName());
                        fromDataListItem.setBookingTel(incomingCall.getTel());
                    }
                    fromDataListItem.setBookingUserId(null);
                    etName.addTextChangedListener(mTextWatcher);
                    etTel.addTextChangedListener(mTextWatcherTel);
                    break;
                case "itemMemberDetail":
                    JsonSigningGuest.Member member1 = (JsonSigningGuest.Member) bundle.getSerializable("member");
                    int indexMemberDetail = bundle.getInt("position");
                    int parentIndexMemberDetail = bundle.getInt("parentPosition");
                    String unSignFlag = bundle.getString("unSignFlag");

                    for (int i = 0; i < rlOrderPeopleDetail.getChildCount(); i++) {
                        TeeTimeAddTitleView nn = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
                        nn.setmCourseAreaId(courseAreaId);
                        int tagParent = (int) nn.getTag();
                        if (tagParent == parentIndexMemberDetail) {
                            for (int j = 0; j < nn.itemDetail.getChildCount(); j++) {
                                TeeTimeAddWithGoodDetailView teeTimeAddListItemDetailView = (TeeTimeAddWithGoodDetailView) nn.itemDetail.getChildAt(j);
                                int tag = (int) teeTimeAddListItemDetailView.getTag();
                                if (tag == indexMemberDetail) {
                                    if (Utils.isStringNotNullOrEmpty(unSignFlag) && Constants.STR_1.equals(unSignFlag)) {
                                        JsonBookingDetailList.DataListItem.BookingListItem bookinglist = fromDataListItem.getList().get(parentIndexMemberDetail).getBookingList().get(indexMemberDetail);
                                        bookinglist.setSignNo(null);
                                        bookinglist.setSignId(null);
                                        bookinglist.setSignType(null);

                                        teeTimeAddListItemDetailView.btn_add_people_index.setText(StringUtils.EMPTY);
                                        teeTimeAddListItemDetailView.btn_add_people_index.setBackgroundResource(R.drawable.bg_btn_change_user);
                                        if (Utils.isStringNotNullOrEmpty(bookinglist.getCustomerId())) {
                                            teeTimeAddListItemDetailView.btn_add_people_other.setText(bookinglist.getCustomerNo());
                                        } else {
                                            teeTimeAddListItemDetailView.btn_add_people_other.setText(R.string.walk_in);
                                        }
                                        break;
                                    }

                                    switch (mNowSignType) {
                                        case "1":
                                            if (member1 != null) {
                                                teeTimeAddListItemDetailView.btn_add_people_index.setText(member1.getMemberNo());
                                            }
                                            break;
                                        case "2"://itee 11 dev
                                            if (Utils.isStringNotNullOrEmpty(bundle.getString("agentId"))) {
                                                teeTimeAddListItemDetailView.btn_add_people_index.setText(bundle.getString("agentId"));
                                            } else {
                                                teeTimeAddListItemDetailView.btn_add_people_index.setText(getString(R.string.tee_time_agent));
                                            }
                                            break;
                                        case "3":
                                            teeTimeAddListItemDetailView.btn_add_people_index.setText(getString(R.string.tee_time_event));
                                            break;
                                    }

                                    teeTimeAddListItemDetailView.btn_add_people_index.setBackgroundResource(R.drawable.btn_change_user_no_content);
                                    JsonBookingDetailList.DataListItem.BookingListItem bookingListItem = fromDataListItem.getList().get(parentIndexMemberDetail).getBookingList().get(indexMemberDetail);
                                    bookingListItem.setSignId(member1 != null ? String.valueOf(member1.getMemberId()) : StringUtils.EMPTY);
                                    bookingListItem.setSignNo(member1 != null ? member1.getMemberNo() : StringUtils.EMPTY);
                                    bookingListItem.setSignType(mNowSignType);
                                    if (Constants.STR_1.equals(mNowSignType)) {
                                        teeTimeAddListItemDetailView.btn_add_people_other.setText(getString(R.string.tee_time_guest));
                                    } else {
                                        if (Utils.isStringNullOrEmpty(bookingListItem.getCustomerId())) {
                                            teeTimeAddListItemDetailView.btn_add_people_other.setText(R.string.walk_in);
                                        } else {
                                            teeTimeAddListItemDetailView.btn_add_people_other.setText(bookingListItem.getCustomerNo());
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if ("1".equals(mNowSignType)) {
                        // count now  sign number.
                        List<JsonBookingDetailList.DataListItem.BookingListItem> bookingListItemList = new ArrayList<>();
                        for (int i = 0; i < fromDataListItem.getList().size(); i++) {
                            JsonBookingDetailList.DataListItem.ListItem listItem = fromDataListItem.getList().get(i);
                            if (listItem.getBookingList() != null && listItem.getBookingList().size() > 0) {
                                bookingListItemList.addAll(listItem.getBookingList());
                            }
                        }
                        mapSelectCount.clear();
                        for (int i = 0; i < bookingListItemList.size(); i++) {
                            JsonBookingDetailList.DataListItem.BookingListItem bookingListItem = bookingListItemList.get(i);

                            if ("1".equals(bookingListItem.getSignType())) {
                                if (!mapSelectCount.containsKey(bookingListItem.getSignId())) {
                                    Integer temp = 1;
                                    mapSelectCount.put(bookingListItem.getSignId(), temp);
                                } else {
                                    mapSelectCount.put(bookingListItem.getSignId(), mapSelectCount.get(bookingListItem.getSignId()) + 1);
                                }
                            }
                        }
                    }
                    break;
                case "memberRole":
                    JsonCustomerBookingSearch.Member memberRole = (JsonCustomerBookingSearch.Member) bundle.getSerializable("member");
                    int indexCustomer = bundle.getInt("position");
                    int parentIndexCustomer = bundle.getInt("parentPosition");
                    String nowSignTypeRole = bundle.getString("nowSignType");

                    for (int i = 0; i < rlOrderPeopleDetail.getChildCount(); i++) {
                        TeeTimeAddTitleView nn = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
                        if (memberRole != null && Constants.STR_FLAG_YES.equals(memberRole.getEndDateFlag()) && Constants.SEGMENT_TIME_MEMBER_ONLY_ID.equals(String.valueOf(nn.getmTypeId()))) {
                            if (Constants.STR_FLAG_YES.equals(nowSignTypeRole)) {
                                String message = MessageFormat.format(getString(R.string.msg_member_ship_is_overdue_full_fee1), memberRole.getMemberName());
                                AppUtils.showMessageWithOkButton(TeeTimeAddFragment.this, message);
                            }
                        } else {
                            nn.setmCourseAreaId(courseAreaId);
                            int tagParent = (int) nn.getTag();
                            if (tagParent == parentIndexCustomer) {
                                for (int j = 0; j < nn.itemDetail.getChildCount(); j++) {
                                    TeeTimeAddWithGoodDetailView teeTimeAddListItemDetailView = (TeeTimeAddWithGoodDetailView) nn.itemDetail.getChildAt(j);
                                    int tag = (int) teeTimeAddListItemDetailView.getTag();
                                    if (tag == indexCustomer) {
                                        JsonBookingDetailList.DataListItem.BookingListItem bookingListItem = fromDataListItem.getList().get(i).getBookingList().get(j);
                                        if (memberRole == null) {
                                            bookingListItem.setCustomerId(null);
                                            bookingListItem.setCustomerName(null);
                                            bookingListItem.setCustomerNo(null);
                                            bookingListItem.setCustomerPic(null);
                                            bookingListItem.setCustomerType(null);
                                            //test
                                            bookingListItem.setSignId(null);
                                            bookingListItem.setSignNo(null);
                                            bookingListItem.setSignType(null);
                                            removeEveryEdittextTexWtch();
                                            teeTimeAddListItemDetailView.etGoodItemName.setText(Constants.STR_EMPTY);
                                            addEveryEdittextTexWtch();
                                            teeTimeAddListItemDetailView.etGoodItemName.setEnabled(true);
                                            teeTimeAddListItemDetailView.btn_add_people_other.setText(getString(R.string.walk_in));
                                            teeTimeAddListItemDetailView.btn_add_people_index.setBackground(getDrawable(R.drawable.bg_btn_change_user));
                                            teeTimeAddListItemDetailView.btn_add_people_index.setText(Constants.STR_EMPTY);
                                        } else {
                                            teeTimeAddListItemDetailView.btn_add_people_other.setBackgroundResource(R.drawable.btn_change_user_no_content);
                                            removeEveryEdittextTexWtch();
                                            teeTimeAddListItemDetailView.etGoodItemName.setText(memberRole.getMemberName());
                                            addEveryEdittextTexWtch();
                                            bookingListItem.setCustomerId(String.valueOf(memberRole.getMemberId()));
                                            bookingListItem.setCustomerName(memberRole.getMemberName());
                                            bookingListItem.setCustomerNo(memberRole.getMemberNo());
                                            bookingListItem.setCustomerPic(memberRole.getMemberPic());
                                            bookingListItem.setCustomerType(nowSignTypeRole);
                                            String signType = bookingListItem.getSignType();

                                            //member
                                            if (Constants.STR_1.equals(nowSignTypeRole)) {
                                                if (Utils.isStringNotNullOrEmpty(signType) && Constants.STR_1.equals(signType)) {
                                                    teeTimeAddListItemDetailView.btn_add_people_index.setText(Constants.STR_EMPTY);
                                                    teeTimeAddListItemDetailView.btn_add_people_index.
                                                            setBackgroundResource(R.drawable.bg_btn_change_user);
                                                    teeTimeAddListItemDetailView.btn_add_people_other.setText(memberRole.getMemberNo());
                                                    bookingListItem.setSignNo(null);
                                                    bookingListItem.setSignId(null);
                                                    bookingListItem.setSignType(null);
                                                } else {
                                                    teeTimeAddListItemDetailView.btn_add_people_other.setText(memberRole.getMemberNo());
                                                }
                                            }

                                            teeTimeAddListItemDetailView.btn_add_people_other.setText(memberRole.getMemberNo());
                                            if (Constants.STR_FLAG_YES.equals(memberRole.getEndDateFlag())) {
                                                if (Constants.STR_FLAG_YES.equals(nowSignTypeRole)) {
                                                    String message = MessageFormat.format(getString(R.string.msg_member_ship_is_overdue_full_fee), memberRole.getMemberName());
                                                    AppUtils.showMessageWithOkButton(TeeTimeAddFragment.this, message);
                                                }
                                            } else {
                                                //message
                                                if (Constants.STR_FLAG_YES.equals(nowSignTypeRole)) {
//                                                String[] weeks = memberRole.getMemberWeek().split(Constants.STR_COMMA);
//                                                List<String> weekList = new ArrayList<>();
//                                                Collections.addAll(weekList, weeks);
//                                                String week = DateUtils.getWeekOfDate(bookingDate);
//                                                if (!weekList.contains(week)) {
//                                                    AppUtils.showMessageWithOkButton(TeeTimeAddFragment.this,
//                                                            memberRole.getMemberName() + getString(R.string.msg_no_discount_message));
//                                                }
                                                    //fix by syb. BUG 753
                                                    String weeksFlag = memberRole.getMemberWeekFlag();
                                                    if (Constants.STR_FLAG_YES.equals(weeksFlag)) {
                                                        AppUtils.showMessageWithOkButton(TeeTimeAddFragment.this, memberRole.getMemberName() + getString(R.string.msg_no_discount_message));
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "refresh":
                    refreshView();
                    break;
                default:
                    break;
            }
        }
    }

    public void refreshView() {
        etName.removeTextChangedListener(mTextWatcher);
        etTel.removeTextChangedListener(mTextWatcherTel);
        setOnBackListener(null);
        netLinkBookingDetail();
    }

    public void refreshView(int parentPosition, int position) {
        etName.removeTextChangedListener(mTextWatcher);
        etTel.removeTextChangedListener(mTextWatcherTel);
        setOnBackListener(null);
        netLinkBookingDetail(parentPosition, position);
    }

    @Override
    protected void setLayoutOfControls() {
        LinearLayout.LayoutParams paramsRLAdd = (LinearLayout.LayoutParams) rlOrderPeopleAdd.getLayoutParams();
        paramsRLAdd.height = getActualHeightOnThisDevice(200);
        paramsRLAdd.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        rlOrderPeopleAdd.setLayoutParams(paramsRLAdd);
        rlOrderPeopleAdd.setPadding(getActualHeightOnThisDevice(20), getActualHeightOnThisDevice(20), getActualHeightOnThisDevice(20), getActualHeightOnThisDevice(20));

        etName.setId(View.generateViewId());
        rlOrderPeopleAdd.addView(etName);
        RelativeLayout.LayoutParams paramsOrderPeopleAdd = (RelativeLayout.LayoutParams) etName.getLayoutParams();
        paramsOrderPeopleAdd.width = getActualWidthOnThisDevice(580);
        paramsOrderPeopleAdd.height = (int) (getScreenHeight() * 0.07f);
        paramsOrderPeopleAdd.leftMargin = 0;

        RelativeLayout.LayoutParams nameBtnParams = new RelativeLayout.LayoutParams((int) (getScreenHeight() * 0.07f), (int) (getScreenHeight() * 0.07f));
        nameBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        nameBtn.setLayoutParams(nameBtnParams);

        nameBtn.setBackgroundResource(R.drawable.k_add);
        rlOrderPeopleAdd.addView(nameBtn);

        paramsOrderPeopleAdd.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsOrderPeopleAdd.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        etName.setPadding(10, 0, 0, 0);
        etName.setGravity(Gravity.CENTER_VERTICAL);
        etName.setLayoutParams(paramsOrderPeopleAdd);
        etName.setIconHeight(TeeTimeAddFragment.this, paramsOrderPeopleAdd.height);
        etTel.setId(View.generateViewId());
        rlOrderPeopleAdd.addView(etTel);
        RelativeLayout.LayoutParams paramsNameTelAdd = (RelativeLayout.LayoutParams) etTel.getLayoutParams();
        paramsNameTelAdd.width = getActualWidthOnThisDevice(580);
        paramsNameTelAdd.height = (int) (getScreenHeight() * 0.07f);

        paramsNameTelAdd.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsNameTelAdd.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        etTel.setGravity(Gravity.CENTER_VERTICAL);
        etTel.setPadding(10, 0, 0, 0);
        etTel.setLayoutParams(paramsNameTelAdd);
        etTel.setIconHeight(TeeTimeAddFragment.this, paramsOrderPeopleAdd.height);

        RelativeLayout.LayoutParams telBtnParams = new RelativeLayout.LayoutParams((int) (getScreenHeight() * 0.07f), (int) (getScreenHeight() * 0.07f));
        telBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        telBtnParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        telBtn.setLayoutParams(telBtnParams);
        telBtn.setBackgroundResource(R.drawable.k_tel);
        rlOrderPeopleAdd.addView(telBtn);
    }

    @Override
    protected void setPropertyOfControls() {
        etName.setHint(R.string.newteetimes_name_hint);
        Drawable imNameDrawable = getResources().getDrawable(R.drawable.btn_add_green1);
        // etName.setCompoundDrawablesWithIntrinsicBounds(null, null, imNameDrawable, null);
        etName.setBackground(getResources().getDrawable(R.drawable.textview_corner));
        etName.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        etTel.setHint(R.string.newteetimes_tel_hint);
//        Drawable imTelDrawable = getResources().getDrawable(R.drawable.btn_call_blue1);
//        etTel.setCompoundDrawablesWithIntrinsicBounds(null, null, imTelDrawable, null);
        etTel.setBackground(getResources().getDrawable(R.drawable.textview_corner));
        etTel.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_CLASS_PHONE);
    }

    public void setBackArrowListener() {
        neebChange = true;
        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {
                OnPopupWindowWheelClickListener onPopupWindowWheelClickListener = new OnPopupWindowWheelClickListener() {
                    @Override
                    public void onSaveClick(String a, String b) {
                        setOnBackListener(null);
                        if ("savechange".equals(a)) {
                            if (doCheck()) {
                                if (isAdd) {
                                    netLinkDoCustomersBooking(StringUtils.EMPTY);
                                } else {
                                    netLinkDoEditCustomersBooking(StringUtils.EMPTY);
                                }
                                //doBack();
                            } else {
                                Utils.showShortToast(getActivity(), getString(R.string.error_mes00001));
                            }
                        } else {
                            doBack();
                        }
                    }
                };
                SelectSaveOrNotPopupWindow savePopupWindow = new SelectSaveOrNotPopupWindow(TeeTimeAddFragment.this, onPopupWindowWheelClickListener, 1, null, null);
                savePopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_window), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        addEveryEdittextTexWtch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        removeEveryEdittextTexWtch();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        removeEveryEdittextTexWtch();
    }

    @Override
    public void onAfterApi() {
        if (ivShop != null) {
            ivShop.setEnabled(true);
        }
    }

    private boolean shoppingCheck() {
        List<JsonBookingDetailList.DataListItem.ListItem> list = dataParameter.getDataList().getList();
        String date = list.get(0).getBookingDate();

        for (JsonBookingDetailList.DataListItem.ListItem item : list) {
            if (!item.getBookingDate().equals(date)) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(String.format("New Tee Times(%d)", selectedNum));
        getTvRight().setText(getResources().getString(R.string.common_ok));
        getTvRight().setGravity(Gravity.CENTER);
        RelativeLayout parent = (RelativeLayout) getTvRight().getParent();

        ivShop = new ImageView(getActivity());
        ivShop.setImageResource(R.drawable.icon_shopping_cart);

        RelativeLayout.LayoutParams ivPackageLayoutParams = new RelativeLayout.LayoutParams(getActionBarHeight(), getActionBarHeight());
        ivPackageLayoutParams.addRule(RelativeLayout.LEFT_OF, getTvRight().getId());
        ivPackageLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        //ivPackageLayoutParams.rightMargin = getActualWidthOnThisDevice(30);
        ivShop.setLayoutParams(ivPackageLayoutParams);
        ivShop.setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, getActivity());
                if (!hasPermission) {
                    AppUtils.showHaveNoPermission(TeeTimeAddFragment.this);
                    return;
                }
                if (!shoppingCheck()) {
                    Utils.showShortToast(getActivity(), getString(R.string.tee_time_save_reservation));
                    return;
                }
                //if any one checkout you can't shopping good.
                Boolean isJump = true;
                List<JsonBookingDetailList.DataListItem.ListItem> list = dataParameter.getDataList().getList();
                for (int m = 0; m < list.size(); m++) {
                    List<JsonBookingDetailList.DataListItem.BookingListItem> bookingList = list.get(m).getBookingList();
                    for (int n = 0; n < bookingList.size(); n++) {
                        JsonBookingDetailList.DataListItem.BookingListItem bookingListItem = bookingList.get(n);
                        if (bookingListItem.getBookingStatus() != null && bookingListItem.getBookingStatus() == 2) {
                            isJump = false;
                        }
                    }
                }

                if (isJump) {
                    setOnBackListener(null);
                    Utils.hideKeyboard(getActivity());
                    if (doCheck()) {
                        if (isAdd) {
                            netLinkDoCustomersBooking("1");
                        } else {
                            netLinkDoEditCustomersBooking("1");
                        }
                    } else {
                        Utils.showShortToast(getActivity(), getString(R.string.error_mes00001));
                    }
                } else {
                    Utils.showShortToast(mContext, getString(R.string.error_mes00007));
                }
            }
        });
        parent.addView(ivShop);
        parent.invalidate();

        getTvRight().setOnClickListener(new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                setOnBackListener(null);
                Utils.hideKeyboard(getActivity());
                if (doCheck()) {
                    if (isAdd) {
                        netLinkDoCustomersBooking(Constants.STR_EMPTY);
                    } else {
                        netLinkDoEditCustomersBooking(StringUtils.EMPTY);
                    }
                } else {
                    Utils.showShortToast(getActivity(), getString(R.string.error_mes00001));
                }
            }
        });

        if (AppUtils.isAgent(mContext)) {
            ivShop.setVisibility(View.INVISIBLE);
        } else {
            ivShop.setVisibility(View.VISIBLE);
        }
    }

    private boolean doCheck() {
        if (rlOrderPeopleDetail != null) {
            for (int i = 0; i < rlOrderPeopleDetail.getChildCount(); i++) {
                TeeTimeAddTitleView nn = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
                nn.setmCourseAreaId(courseAreaId);
                for (int j = 0; j < nn.itemDetail.getChildCount(); j++) {
                    TeeTimeAddWithGoodDetailView bookingList = (TeeTimeAddWithGoodDetailView) nn.itemDetail.getChildAt(j);
                    if (Utils.isStringNullOrEmpty(bookingList.etGoodItemName.getText().toString())) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void executeOnceOnCreate() {
        if (isAdd) {
            netLinkCustomersBooking();
        } else {
            netLinkBookingDetail();
        }
    }

    @Override
    protected void executeEveryOnCreate() {
        super.executeEveryOnCreate();
        if (!isAdd && isFromPay) {
            isFromPay = false;
            netLinkBookingDetail();
        }
        if (neebChange)
            setBackArrowListener();
    }

    public void netLinkDoCustomersBooking(final String isJump) {
        if (ivShop != null) {
            ivShop.setEnabled(false);
        }

        List<JsonBookingDetailList.DataListItem.ListItem> list = dataParameter.getDataList().getList();
        for (int m = 0; m < list.size(); m++) {
            List<JsonBookingDetailList.DataListItem.BookingListItem> bookingListSave = list.get(m).getBookingList();
//        remove the null value
            for (int i = 0; i < bookingListSave.size(); i++) {
                JsonBookingDetailList.DataListItem.BookingListItem bookingListItemSave = bookingListSave.get(i);
                bookingListItemSave.setBookingSort(i + 1);
                if (Utils.isStringNullOrEmpty(bookingListItemSave.getCustomerName())) {
                    Utils.showShortToast(getActivity(), getString(R.string.error_mes00001));
                    return;
                }
            }
        }

        JsonLogin jsonLogin = AppUtils.getLoginInfo(getBaseActivity());
        //fix by syb.  isAgent login. now set  signId/signType.
        if (AppUtils.isAgent(getBaseActivity())) {
            for (int m = 0; m < list.size(); m++) {
                List<JsonBookingDetailList.DataListItem.BookingListItem> bookingListSave = list.get(m).getBookingList();
                for (int i = 0; i < bookingListSave.size(); i++) {
                    JsonBookingDetailList.DataListItem.BookingListItem bookingListItemSave = bookingListSave.get(i);
                    if (Utils.isStringNullOrEmpty(bookingListItemSave.getSignId())) {
                        bookingListItemSave.setSignId(String.valueOf(jsonLogin.getUserId()));
                        bookingListItemSave.setSignNo(jsonLogin.getUserName());
                        bookingListItemSave.setSignType("2");
                    }
                }
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        FilterProvider filters = new SimpleFilterProvider().addFilter("JsonBookingDetailList.DataListItem.GoodListItem", new BeanPropertyFilter() {
                    @Override
                    public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov, BeanPropertyWriter writer) throws Exception {
                        if (bean instanceof JsonBookingDetailList.DataListItem.GoodListItem) {
                            JsonBookingDetailList.DataListItem.GoodListItem item = (JsonBookingDetailList.DataListItem.GoodListItem) bean;
                            if (item.getGoodsId() != null) {
                                writer.serializeAsField(bean, jgen, prov);
                            }
                        } else {
                            writer.serializeAsField(bean, jgen, prov);
                        }
                    }
                });
        objectMapper.setFilters(filters);

        String jsonValue = null;
        try {
            jsonValue = objectMapper.writeValueAsString(list);
            jsonValue = jsonValue.replace("signIn", "sign_in").replace("bookingDate", "booking_date").replace("bookingStatus", "booking_status").replace("customerId", "customer_id").replace("customerNo", "customer_no").replace("customerType", "customer_type").replace("customerName", "customer_name").replace("customerDeposit", "customer_deposit").replace("bookingSort", "booking_sort").replace("goodsList", "goods_list").replace("bookingAreaList", "booking_area_list").replace("categoryCourseList", "category_course_list").replace("categoryId", "category_id").replace("goodsId", "goods_id").replace("goodsLastAttr", "goods_last_attr").replace("courseAreaType", "course_area_type").replace("courseAreaId", "course_area_id").replace("bookingTime", "booking_time").replace("booking_timeStatus", "booking_time_status").replace("bookingNo", "booking_no").replace("signNo", "sign_no").replace("signId", "sign_id").replace("signType", "sign_type").replace("areaList", "area_list").replace("bookingList", "booking_list").replace("bookingDeposit", "booking_deposit").replace("customerPic", "customer_pic").replace("goodsPrice", "goods_price").replace("typeId", "type_id").replace("typeTimeName", "type_time_name").replace("transferFlag", "transfer_flag").replace("caddieNo", "caddie_no");
        } catch (Exception e) {
            Utils.log(e.getMessage());
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.TEE_TIME_COURSE_ID, AppUtils.getCurrentCourseId(getActivity()));
//        if (Constants.STR_1.equals(isJump)) {
//            params.put(ApiKey.TEE_TIME_BK_FLAG, Constants.STR_1);
//        }
        params.put(ApiKey.TEE_TIME_BOOKING_USER_ID, String.valueOf(dataParameter.getDataList().getBookingUserId()));

        if (dataParameter.getDataList().getBookingName() == null) {
            params.put(ApiKey.TEE_TIME_BOOKING_NAME, Constants.STR_EMPTY);
        } else {
            params.put(ApiKey.TEE_TIME_BOOKING_NAME, dataParameter.getDataList().getBookingName());
        }

        if (dataParameter.getDataList().getBookingTel() == null) {
            params.put(ApiKey.TEE_TIME_BOOKING_TEL, Constants.STR_EMPTY);
        } else {
            params.put(ApiKey.TEE_TIME_BOOKING_TEL, dataParameter.getDataList().getBookingTel());
        }

        if (AppUtils.isAgent(mContext)) {
            JsonLogin loginInfo = AppUtils.getLoginInfo(mContext);
            if (loginInfo != null) {
                params.put(ApiKey.TEE_TIME_BOOKING_USER_ID, String.valueOf(loginInfo.getUserId()));
                params.put(ApiKey.TEE_TIME_BOOKING_NAME, loginInfo.getUserName());
                params.put(ApiKey.TEE_TIME_BOOKING_TEL, loginInfo.getUserTel());
            }
        }
        params.put(ApiKey.TEE_TIME_BOOKING_DATE, bookingDate);
        params.put(ApiKey.TEE_TIME_LIST, jsonValue);

        HttpManager<JsonReturnBookingOrderNo> hh = new HttpManager<JsonReturnBookingOrderNo>(TeeTimeAddFragment.this) {
            @Override
            public void onJsonSuccess(JsonReturnBookingOrderNo jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {
                    if (Constants.STR_1.equals(isJump)) {
                        if (Constants.STR_0.equals(jo.getPricingDisplay())) {
                            bookingOrderNo = jo.getDataList().getBookingOrderNo();
//                            openCards(jo.getBookingList());
                        } else {
                            bookingOrderNo = jo.getDataList().getBookingOrderNo();
                            Bundle bundle = new Bundle();
                            bundle.putString(TransKey.SHOPPING_BOOKING_NO, jo.getDataList().getBookingOrderNo());
                            bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeAddFragment.class.getName());
                            bundle.putString(TransKey.COMMON_JUMP, TeeTimeChoosePriceFragment.JUMP_FRAGMENT_6_2);
                            push(TeeTimeChoosePriceFragment.class, bundle);
                        }
                    } else {
                        if (Utils.isStringNotNullOrEmpty(fromPage)) {
                            try {
                                Bundle bundle = new Bundle();
                                doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            doBackWithRefresh(LocationListFragment.class);
                        }
                    }
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.DocsBooking, params);
    }

    public void netLinkDoEditCustomersBooking(final String isJump) {
        if (ivShop != null) {
            ivShop.setEnabled(false);
        }
        List<JsonBookingDetailList.DataListItem.ListItem> list = dataParameter.getDataList().getList();
        for (int m = 0; m < list.size(); m++) {
            List<JsonBookingDetailList.DataListItem.BookingListItem> bookingListSave = list.get(m).getBookingList();
            // List<JsonBookingDetailList.DataListItem.BookingListItem> paramBookingListSave = new ArrayList<>();
            // remove the null value
            for (int i = 0; i < bookingListSave.size(); i++) {
                JsonBookingDetailList.DataListItem.BookingListItem bookingListItemSave = bookingListSave.get(i);
                bookingListItemSave.setBookingSort(i + 1);
                if (Utils.isStringNullOrEmpty(bookingListItemSave.getCustomerName())) {
                    Utils.showShortToast(getActivity(), getString(R.string.error_mes00001));
                    return;
                }
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        FilterProvider filters = new SimpleFilterProvider().addFilter("JsonBookingDetailList.DataListItem.GoodListItem", new BeanPropertyFilter() {
                    @Override
                    public void serializeAsField(Object bean, JsonGenerator jgen, SerializerProvider prov, BeanPropertyWriter writer) throws Exception {
                        if (bean instanceof JsonBookingDetailList.DataListItem.GoodListItem) {
                            JsonBookingDetailList.DataListItem.GoodListItem item = (JsonBookingDetailList.DataListItem.GoodListItem) bean;
                            if (item.getGoodsId() != null) {
                                writer.serializeAsField(bean, jgen, prov);
                            }
                        } else {
                            writer.serializeAsField(bean, jgen, prov);
                        }
                    }
                });
        objectMapper.setFilters(filters);
        String jsonValue = null;
        try {
            jsonValue = objectMapper.writeValueAsString(list);
            jsonValue = jsonValue.replace("signIn", "sign_in")
                    .replace("bookingStatus", "booking_status")
                    .replace("customerId", "customer_id")
                    .replace("customerNo", "customer_no")
                    .replace("customerType", "customer_type")
                    .replace("customerName", "customer_name")
                    .replace("customerDeposit", "customer_deposit")
                    .replace("bookingSort", "booking_sort")
                    .replace("goodsList", "goods_list")
                    .replace("bookingAreaList", "booking_area_list")
                    .replace("categoryCourseList", "category_course_list")
                    .replace("categoryId", "category_id")
                    .replace("goodsId", "goods_id")
                    .replace("goodsLastAttr", "goods_last_attr").replace("courseAreaType", "course_area_type")
                    .replace("courseAreaId", "course_area_id")
                    .replace("bookingTime", "booking_time").replace("booking_timeStatus", "booking_time_status").replace("bookingNo", "booking_no")
                    .replace("signNo", "sign_no").replace("signId", "sign_id").replace("signType", "sign_type").replace("areaList", "area_list")
                    .replace("bookingList", "booking_list").replace("bookingDeposit", "booking_deposit").replace("customerPic", "customer_pic")
                    .replace("goodsPrice", "goods_price").replace("typeId", "type_id").replace("typeTimeName", "type_time_name")
                    .replace("transferFlag", "transfer_flag").replace("caddieNo", "caddie_no")
                    .replace("bookingDate", "booking_date").replace("bookingSignStatus", "booking_sign_status");
        } catch (Exception e) {
            Utils.log(e.getMessage());
        }

        final Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
//        if (Constants.STR_1.equals(isJump)) {
//            params.put(ApiKey.TEE_TIME_BK_FLAG, "1");
//        }
        if (dataParameter.getDataList().getBookingName() == null) {
            params.put(ApiKey.TEE_TIME_BOOKING_NAME, Constants.STR_EMPTY);
        } else {
            params.put(ApiKey.TEE_TIME_BOOKING_NAME, dataParameter.getDataList().getBookingName());
        }

        if (dataParameter.getDataList().getBookingTel() == null) {
            params.put(ApiKey.TEE_TIME_BOOKING_TEL, Constants.STR_EMPTY);
        } else {
            params.put(ApiKey.TEE_TIME_BOOKING_TEL, dataParameter.getDataList().getBookingTel());
        }

        if (dataParameter.getDataList().getBookingUserId() == null) {
            params.put(ApiKey.TEE_TIME_BOOKING_USER_ID, Constants.STR_EMPTY);
        } else {
            params.put(ApiKey.TEE_TIME_BOOKING_USER_ID, String.valueOf(dataParameter.getDataList().getBookingUserId()));
        }

        if (dataParameter.getDataList().getBookingUserType() == null) {
            params.put(ApiKey.TEE_TIME_BOOKING_USER_TYPE, Constants.STR_EMPTY);
        } else {
            params.put(ApiKey.TEE_TIME_BOOKING_USER_TYPE, String.valueOf(dataParameter.getDataList().getBookingUserType()));
        }

        if (dataParameter.getDataList().getBookingDate() == null) {
            params.put(ApiKey.TEE_TIME_BOOKING_DATE, Constants.STR_EMPTY);
        } else {
            params.put(ApiKey.TEE_TIME_BOOKING_DATE, dataParameter.getDataList().getBookingDate());
        }
        params.put(ApiKey.TEE_TIME_BOOKING_ORDER_NO, bookingOrderNo);
        params.put(ApiKey.TEE_TIME_LIST, jsonValue);

        final HttpManager<JsonOrderGetData> hh = new HttpManager<JsonOrderGetData>(TeeTimeAddFragment.this) {
            @Override
            public void onJsonSuccess(JsonOrderGetData jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {
                    if (Constants.STR_1.equals(isJump)) {
//                        if (Constants.STR_0.equals(jo.getPricingDisplay())) {
//                            openCards(jo.getBookingList());
//                        } else {
                        Bundle bundle = new Bundle();
                        bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingOrderNo);
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeAddFragment.class.getName());
                        bundle.putString(TransKey.COMMON_JUMP, TeeTimeChoosePriceFragment.JUMP_FRAGMENT_6_2);
                        push(TeeTimeChoosePriceFragment.class, bundle);
//                        }
                    } else {
                        if (Utils.isStringNotNullOrEmpty(fromPage)) {
                            try {
                                Bundle bundle = new Bundle();
                                doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            doBackWithRefresh(LocationListFragment.class);
                        }
                    }
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.DoEditCustomersBk, params);
    }

    // 开卡
    private void openCards(ArrayList<String> bookingList) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        JSONArray jsArray = new JSONArray();
        try {
            for (String bookingStr : bookingList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("booking_no", bookingStr);
                jsArray.put(jsonObject);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put(ApiKey.COMMON_DATA_LIST, jsArray.toString());

        HttpManager<JsonCheckInGetData> hh = new HttpManager<JsonCheckInGetData>(TeeTimeAddFragment.this) {
            @Override
            public void onJsonSuccess(JsonCheckInGetData jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    isAdd = false;
                    isFromPay = true;
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.SHOPPING_BOOKING_NO, jo.getBookingId());
                    bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeAddFragment.class.getName());
                    push(ShoppingPaymentFragment.class, bundle);
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.xcheckin, params);
    }

    public void netLinkCustomersBooking() {
        if (ivShop != null) {
            ivShop.setEnabled(false);
        }
        List<Map<String, String>> mapList = new ArrayList<>();
        String oldTime = StringUtils.EMPTY;
        for (int i = 0; i < startDateTime.size(); i++) {
            HashMap<String, String> map = new HashMap<>();
            if (!oldTime.equals(startDateTime.get(i))) {
                map.put("time", startDateTime.get(i));
                oldTime = startDateTime.get(i);
                mapList.add(map);
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonValue = null;
        try {
            jsonValue = objectMapper.writeValueAsString(mapList);
        } catch (Exception e) {
            Utils.log(e.getMessage());
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.TEE_TIME_COURSE_ID, AppUtils.getCurrentCourseId(getActivity()));
        params.put(ApiKey.TEE_TIME_COURSE_AREA_ID, String.valueOf(courseAreaId));
        params.put(ApiKey.TEE_TIME_BOOKING_DATE, bookingDate);
        params.put(ApiKey.TEE_TIME_BOOKING_TIME, jsonValue);

        HttpManager<JsonCustomersBooking> hh = new HttpManager<JsonCustomersBooking>(TeeTimeAddFragment.this) {
            @Override
            public void onJsonSuccess(JsonCustomersBooking jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    initDataParameter(jo);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.Csbooking, params);
    }

    /**
     * In edit mode : get user data.
     */
    public void netLinkBookingDetail(final int parentPosition, final int position) {
        if (ivShop != null) {
            ivShop.setEnabled(false);
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.BOOKING_ORDER_NO, bookingOrderNo);

        HttpManager<JsonBookingDetailList> hh = new HttpManager<JsonBookingDetailList>(TeeTimeAddFragment.this) {
            @Override
            public void onJsonSuccess(JsonBookingDetailList jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataParameter = jo;
                    resetEditView(parentPosition, position);

                    etName.addTextChangedListener(mTextWatcher);
                    etTel.addTextChangedListener(mTextWatcherTel);

                    for (int i = rlOrderPeopleDetail.getChildCount() - 1; i > -1; i--) {
                        TeeTimeAddTitleView nn = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
                        if (nn.getBookingList() == null || nn.getBookingList().size() == 0) {
                            rlOrderPeopleDetail.removeView(nn);
                        }
                        if (nn.getBookingList().size() == 1 && Utils.isStringNullOrEmpty(nn.getBookingList().get(0).getBookingNo())) {
                            rlOrderPeopleDetail.removeView(nn);
                        }
                    }
                    if (rlOrderPeopleDetail.getChildCount() == 0) {
                        doBackWithRefresh(LocationListFragment.class);
                    }

                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.BookingDetail, params);
    }

    //查看
    public void netLinkBookingDetail() {
        if (ivShop != null) {
            ivShop.setEnabled(false);
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.BOOKING_ORDER_NO, bookingOrderNo);

        HttpManager<JsonBookingDetailList> hh = new HttpManager<JsonBookingDetailList>(TeeTimeAddFragment.this) {
            @Override
            public void onJsonSuccess(JsonBookingDetailList jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    dataParameter = jo;
                    if (jo.getDataList().getList().size() > 0) {
                        courseAreaId = jo.getDataList().getList().get(0).getAreaList().get(0).getCourseAreaId();
                    }
                    resetEditView();
                    etName.addTextChangedListener(mTextWatcher);
                    etTel.addTextChangedListener(mTextWatcherTel);

                    for (int i = rlOrderPeopleDetail.getChildCount() - 1; i > -1; i--) {
                        TeeTimeAddTitleView nn = (TeeTimeAddTitleView) rlOrderPeopleDetail.getChildAt(i);
                        nn.setmCourseAreaId(courseAreaId);
                        if (nn.getBookingList() == null || nn.getBookingList().size() == 0) {
                            rlOrderPeopleDetail.removeView(nn);
                        }
                        if (nn.getBookingList().size() == 1
                                && Utils.isStringNullOrEmpty(nn.getBookingList().get(0).getBookingNo())) {
                            rlOrderPeopleDetail.removeView(nn);
                        }
                    }
                    if (rlOrderPeopleDetail.getChildCount() == 0) {
                        doBackWithRefresh(LocationListFragment.class);
                    }
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.BookingDetail, params);
    }

    /**
     * del booking .
     */
    public void deleteCustomersBooking(final String mParentPosition, final String mPosition) {
        JsonBookingDetailList.DataListItem fromDataListItem = dataParameter.getDataList();
        List<JsonBookingDetailList.DataListItem.BookingListItem> bookingList = fromDataListItem.getList().get(Integer.valueOf(mParentPosition)).getBookingList();
        JsonBookingDetailList.DataListItem.BookingListItem booking = bookingList.get(Integer.valueOf(mPosition));

        final StringBuilder sbBookingNo = new StringBuilder();
        StringBuilder sbNames = new StringBuilder();

        AppUtils.NoDoubleClickListener clickListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                netLinkDelBooking(sbBookingNo.toString());
            }
        };
        ConfirmPopWindow confirmPopWindow = new ConfirmPopWindow(getActivity(), clickListener);

        String currentBookingNo = booking.getBookingNo();
        if (Constants.STR_1.equals(booking.getPrimeTime())) {
            String prefixBookingNo = currentBookingNo.split(Constants.STR_SEPARATOR)[0];
            int total = 0;
            for (JsonBookingDetailList.DataListItem.BookingListItem item : bookingList) {
                if (item.getBookingNo().contains(prefixBookingNo)) {
                    if (sbBookingNo.length() > 0) {
                        sbBookingNo.append(Constants.STR_COMMA);
                    }
                    if (sbNames.length() > 0) {
                        sbNames.append(Constants.STR_COMMA);
                    }
                    sbNames.append(item.getCustomerName());
                    sbBookingNo.append(item.getBookingNo());
                    total++;
                }
            }
            if (total > 3 || total == 1) {
                sbBookingNo.setLength(0);
                sbBookingNo.append(currentBookingNo);
                confirmPopWindow.setMessage(getString(R.string.confirm_delete));
                confirmPopWindow.showAtLocation(getRootView().findViewById(R.id.rl_content_container), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            } else if (total == 3) {
                String message = MessageFormat.format(getString(R.string.msg_prime_time_now_will_be_delete), sbNames.toString());
                confirmPopWindow.setMessage(message);
                confirmPopWindow.showAtLocation(getRootView().findViewById(R.id.rl_content_container), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        } else {
            sbBookingNo.setLength(0);
            sbBookingNo.append(currentBookingNo);
            confirmPopWindow.setMessage(getString(R.string.confirm_delete));
            confirmPopWindow.showAtLocation(getRootView().findViewById(R.id.rl_content_container), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        }
    }

    private void netLinkDelBooking(String bookingNo) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.BOOKING_NO, bookingNo);
        HttpManager<JsonBookingDetailList> hh = new HttpManager<JsonBookingDetailList>(TeeTimeAddFragment.this) {
            @Override
            public void onJsonSuccess(JsonBookingDetailList jo) {
                int returnCode = jo.getReturnCode();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    refreshView();
                } else {
                    Utils.showShortToast(getActivity(), jo.getReturnInfo());
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.DoDelBooking, params);
    }

    public interface OnPopupWindowWheelClickListener {
        void onSaveClick(String a, String b);
    }
}
