/**
 * Project Name: itee
 * File Name:	 EventsEditFragment.java
 * Package Name: cn.situne.itee.fragment.events
 * Date:		 2015-03-09
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.events;


import android.content.Context;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.adapter.MultiChoiceAdapter;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.JsonKey;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DateUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.player.PlayerInfoEditFragment;
import cn.situne.itee.fragment.teetime.TeeTimeMemberListWithIndexFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonEventAreaGet;
import cn.situne.itee.manager.jsonentity.JsonEventsEditGet;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeRedDeleteButton;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.ConfirmPopWindow;
import cn.situne.itee.view.popwindow.MultiChoicePopupWindow;
import cn.situne.itee.view.popwindow.SelectDatePopupWindow;
import cn.situne.itee.view.popwindow.SelectTimePopupWindow;

/**
 * ClassName:EventsEditFragment <br/>
 * Function: event info edit. <br/>
 * UI:  09-2/09-2-3/09-2-4/09-2-5
 * Date: 2015-03-09 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class EventsEditFragment extends BaseEditFragment {

    private RelativeLayout rlEventContainer;
    private RelativeLayout rlPriceContainer;
    private RelativeLayout rlAddDateContainer;
    private View viewAddDateContainer;
    private LinearLayout llDeleteContainer;

    private IteeTextView tvEvent;
    private IteeEditText etEventValue;
    private IteeTextView tvPrice;
    private ImageView ivPriceValue;
    private ListView editListView;
    private IteeTextView tvAddDate;
    private IteeRedDeleteButton btnDel;

    private boolean isAdd;
    private Integer eventId;

    private EventsEditAdapter eventsEditAdapter;
    private ArrayList<JsonEventAreaGet.EventArea> eventAreaList;

    private JsonEventsEditGet.EventInfo eventInfo;
    private List<JsonEventsEditGet.EventInfo.DateInfo> dataList;
    private SelectTimePopupWindow popupWindow;
    private MultiChoicePopupWindow multiChoicePopupWindow;
    private SelectDatePopupWindow selectDatePopupWindow;

    private View.OnClickListener listenerPop;
    private View.OnClickListener listenerEdit;
    private View.OnClickListener listenerDelete;
    private SelectDatePopupWindow.OnDateSelectClickListener dateSelectReturn;

    private String token;
    private String userId;

    private StringBuilder stringBufferName;
    private StringBuilder stringBufferId;
    private String strStartTime;
    private String strEndTime;

    private String fromPage;

    private PopupWindow currentPopWindow;
    private String delEventsDateIdStr = Constants.STR_EMPTY;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_events_edit;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.common_edit;
    }

    @Override
    protected void initControls(View rootView) {

        Bundle bundle = getArguments();
        if (bundle != null) {
            isAdd = bundle.getBoolean(TransKey.EVENT_IS_ADD);
            if (isAdd) {
                setFragmentMode(FragmentMode.FragmentModeAdd);
            } else {
                setFragmentMode(FragmentMode.FragmentModeBrowse);
            }
            eventId = bundle.getInt(TransKey.EVENT_EVENT_ID);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, Constants.STR_EMPTY);
        }
        token = AppUtils.getToken(getActivity());
        userId = AppUtils.getCurrentUserId(getActivity());

        rlEventContainer = (RelativeLayout) rootView.findViewById(R.id.rl_event_container);
        rlPriceContainer = (RelativeLayout) rootView.findViewById(R.id.rl_price_container);
        rlAddDateContainer = (RelativeLayout) rootView.findViewById(R.id.rl_add_date_container);
        viewAddDateContainer = rootView.findViewById(R.id.view_add_date_container);
        llDeleteContainer = (LinearLayout) rootView.findViewById(R.id.ll_delete_container);
        editListView = (ListView) rootView.findViewById(R.id.edit_listView);

        eventsEditAdapter = new EventsEditAdapter(getActivity(), dataList, listenerPop, listenerDelete);

        editListView.setAdapter(eventsEditAdapter);
        editListView.setDividerHeight(0);

        tvEvent = new IteeTextView(getActivity());
        etEventValue = new IteeEditText(getActivity());

        etEventValue.setSingleLine();
        etEventValue.setBackground(null);
        etEventValue.setHint(R.string.event_event);

        tvPrice = new IteeTextView(getActivity());
        ivPriceValue = new ImageView(getActivity());

        tvAddDate = new IteeTextView(getActivity());

        btnDel = new IteeRedDeleteButton(getActivity());

        dataList = new ArrayList<>();

        etEventValue.setEnabled(isAdd);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

        listenerDelete = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    int position = (int) view.getTag();
                    if (dataList.get(position).getEventDateId() != null) {
                        delEventsDateIdStr = delEventsDateIdStr + String.valueOf(dataList.get(position)
                                .getEventDateId()) + Constants.STR_COMMA;
                    }

                    dataList.remove(position);
                    eventsEditAdapter.notifyDataSetChanged();
                    LayoutUtils.setListViewHeightBasedOnChildren(editListView);
                }

            }
        };


        listenerPop = new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Calendar c = Calendar.getInstance();
                int curHours = c.get(Calendar.HOUR_OF_DAY);
                int curMinutes = c.get(Calendar.MINUTE);

                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    Utils.hideKeyboard(getActivity());
                    String tag = (String) view.getTag();
                    String[] value = tag.split(Constants.STR_COMMA);
                    int rePosition = Integer.parseInt(value[0]);
                    final int itemPosition = Integer.parseInt(value[1]);
                    if (rePosition == 0) {
                        dateSelectReturn = new SelectDatePopupWindow.OnDateSelectClickListener() {
                            @Override
                            public void OnGoodItemClick(String flag, String content) {
                                switch (flag) {
                                    case Constants.DATE_RETURN:
                                        IteeTextView dateValue = (IteeTextView) view.findViewById(R.id.tv_dateValue);
                                        dateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(content, getActivity()));
                                        dataList.get(itemPosition).eventDate = content;
                                        break;
                                }
                            }
                        };
                        if (currentPopWindow != null) {
                            currentPopWindow.dismiss();
                        }
                        String myData = DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dataList.get(itemPosition).eventDate, mContext);
                        selectDatePopupWindow = new SelectDatePopupWindow(getActivity(), myData, dateSelectReturn);
                        selectDatePopupWindow.showAtLocation(getRootView(), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        currentPopWindow = selectDatePopupWindow;

                    } else if (rePosition == 1) {
                        IteeTextView startTimeValue = (IteeTextView) view.findViewById(R.id.tv_startTimeValue);
                        strStartTime = startTimeValue.getText().toString();
                        if (!TextUtils.isEmpty(strStartTime)) {
                            String[] times = strStartTime.split(Constants.STR_COLON);
                            popupWindow = new SelectTimePopupWindow(getActivity(), times, 1);
                        } else {
                            strStartTime = String.format(Constants.STRING_FORMAT_02D_02D, curHours, curMinutes);
                            String[] times = strStartTime.split(Constants.STR_COLON);
                            popupWindow = new SelectTimePopupWindow(getActivity(), times, 1);
                        }

                        if (currentPopWindow != null) {
                            currentPopWindow.dismiss();
                        }
                        popupWindow.showAtLocation(EventsEditFragment.this.getRootView()
                                .findViewById(R.id.popup_window), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View buttonView) {
                                String formatStartTimeValue = String.format(Constants.STRING_FORMAT_02D_02D, popupWindow.wheelViewHour
                                        .getCurrentItem(), popupWindow.wheelViewMin.getCurrentItem());
                                IteeTextView startTimeValue = (IteeTextView) view.findViewById(R.id.tv_startTimeValue);
                                startTimeValue.setText(formatStartTimeValue);
                                dataList.get(itemPosition).eventStartTime = formatStartTimeValue;
                                popupWindow.dismiss();
                            }
                        });

                        popupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View buttonView) {
                                popupWindow.dismiss();
                            }
                        });
                        currentPopWindow = popupWindow;
                    } else if (rePosition == 2) {
                        IteeTextView endTimeValue = (IteeTextView) view.findViewById(R.id.tv_end_time_value);
                        strEndTime = endTimeValue.getText().toString();
                        if (!TextUtils.isEmpty(strEndTime)) {
                            String[] times = strEndTime.split(Constants.STR_COLON);
                            popupWindow = new SelectTimePopupWindow(getActivity(), times, 1);
                        } else {
                            strEndTime = String.format(Constants.STRING_FORMAT_02D_02D, curHours, curMinutes);
                            String[] times = strEndTime.split(Constants.STR_COLON);
                            popupWindow = new SelectTimePopupWindow(getActivity(), times, 1);
                        }
                        popupWindow.showAtLocation(EventsEditFragment.this.getRootView()
                                .findViewById(R.id.popup_window), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                        popupWindow.btnOk.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View buttonView) {
                                String formatEndTimeValue = String.format(Constants.STRING_FORMAT_02D_02D, popupWindow.wheelViewHour
                                        .getCurrentItem(), popupWindow.wheelViewMin.getCurrentItem());
                                IteeTextView endTimeValue = (IteeTextView) view.findViewById(R.id.tv_end_time_value);
                                endTimeValue.setText(formatEndTimeValue);
                                dataList.get(itemPosition).eventEndTime = formatEndTimeValue;
                                popupWindow.dismiss();
                            }
                        });

                        popupWindow.btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View buttonView) {
                                popupWindow.dismiss();
                            }
                        });
                        if (currentPopWindow != null) {
                            currentPopWindow.dismiss();
                        }
                        currentPopWindow = popupWindow;
                    } else if (rePosition == 3) {

                        final IteeTextView courseValue = (IteeTextView) view.findViewById(R.id.tv_course_value);

                        final String courseAreaId = dataList.get(itemPosition).getCourseAreaId();

                        Map<String, String> params = new HashMap<>();
                        params.put(ApiKey.EVENT_LIST_TOKEN, token);
                        params.put(ApiKey.EVENT_LIST_USER_ID, userId);

                        HttpManager<JsonEventAreaGet> hh = new HttpManager<JsonEventAreaGet>(EventsEditFragment.this) {

                            @Override
                            public void onJsonSuccess(JsonEventAreaGet jo) {
                                eventAreaList = jo.getEvent();

                                final ArrayList<HashMap<String, Object>> courseAreaTypeList = new ArrayList<>();
                                for (int i = 0; i < eventAreaList.size(); i++) {
                                    int courseId = eventAreaList.get(i).getCourseAreaId();
                                    String courseName = eventAreaList.get(i).getCourseArea();
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put(TransKey.EVENTS_COURSE_AREA_ID, courseId);
                                    map.put(TransKey.EVENTS_COURSE_AREA_NAME, courseName);
                                    courseAreaTypeList.add(map);
                                }


                                MultiChoiceAdapter multiChoiceAdapter = new MultiChoiceAdapter(getActivity(), courseAreaTypeList);

                                multiChoicePopupWindow = new MultiChoicePopupWindow(getActivity(), null, courseAreaTypeList
                                        .size());

                                multiChoicePopupWindow.lvPopupWindow.setAdapter(multiChoiceAdapter);
                                multiChoicePopupWindow.lvPopupWindow.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                                multiChoicePopupWindow.lvPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view1, int position,
                                                            long l) {


                                        stringBufferId = new StringBuilder();
                                        stringBufferName = new StringBuilder();
                                        stringBufferName.append(Constants.STR_EMPTY);
                                        long[] ids = multiChoicePopupWindow.lvPopupWindow.getCheckedItemIds();

                                        if (ids.length == 1) {
                                            stringBufferId.append(courseAreaTypeList.get((int) (ids[0]))
                                                    .get(TransKey.EVENTS_COURSE_AREA_ID)
                                                    .toString());
                                            stringBufferName.append(courseAreaTypeList.get((int) (ids[0]))
                                                    .get(TransKey.EVENTS_COURSE_AREA_NAME)
                                                    .toString());
                                        } else if (ids.length > 1) {

                                            for (int i = 0; i < ids.length - 1; i++) {
                                                stringBufferId.append(courseAreaTypeList.get((int) (ids[i]))
                                                        .get(TransKey.EVENTS_COURSE_AREA_ID)
                                                        .toString()).append(Constants.STR_COMMA);
                                                stringBufferName.append(courseAreaTypeList.get((int) (ids[i]))
                                                        .get(TransKey.EVENTS_COURSE_AREA_NAME)
                                                        .toString()).append(Constants.STR_SLASH);
                                            }
                                            stringBufferId.append(courseAreaTypeList.get((int) (ids[ids.length - 1]))
                                                    .get(TransKey.EVENTS_COURSE_AREA_ID)
                                                    .toString());
                                            stringBufferName.append(courseAreaTypeList.get((int) (ids[ids.length - 1]))
                                                    .get(TransKey.EVENTS_COURSE_AREA_NAME)
                                                    .toString());
                                        }


                                    }
                                });

                                multiChoicePopupWindow.btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (stringBufferName != null) {
                                            dataList.get(itemPosition).courseAreaName = stringBufferName.toString();
                                            dataList.get(itemPosition).courseAreaId = stringBufferId.toString();
                                            if (stringBufferName != null) {
                                                courseValue.setText(stringBufferName.toString());
                                            } else {
                                                courseValue.setText(Constants.STR_EMPTY);
                                            }


                                        }
                                        multiChoicePopupWindow.dismiss();
                                    }
                                });

                                multiChoicePopupWindow.btn_cancel.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        multiChoicePopupWindow.dismiss();
                                    }
                                });

                                if (Utils.isStringNotNullOrEmpty(courseAreaId)) {
                                    String[] courseAreaIds = courseAreaId.split(Constants.STR_COMMA);
                                    for (String courseAreaIdString : courseAreaIds) {
                                        for (int j = 0; j < courseAreaTypeList.size(); j++) {
                                            if (courseAreaIdString.equals(courseAreaTypeList.get(j)
                                                    .get(TransKey.EVENTS_COURSE_AREA_ID)
                                                    .toString())) {
                                                multiChoicePopupWindow.lvPopupWindow.setItemChecked(j, true);
                                                break;
                                            }
                                        }
                                    }
                                }

                                multiChoicePopupWindow.showAtLocation(EventsEditFragment.this.getRootView()
                                        .findViewById(R.id.popup_window), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                                if (currentPopWindow != null) {
                                    currentPopWindow.dismiss();
                                }
                                currentPopWindow = multiChoicePopupWindow;
                            }

                            @Override
                            public void onJsonError(VolleyError error) {
                                Utils.showShortToast(getActivity(), String.valueOf(error));
                            }
                        };
                        hh.startGet(getActivity(), ApiManager.HttpApi.EventsArea, params);
                    }
                }
            }
        };


        listenerEdit = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(getActivity());
                // add event
                if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
                    if (isNullCheck()) {
                        if (inputCheck()) {
                            postEventData(false);
                        }
                    } else {
                        Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(getActivity(), R.string.event_event));
                    }
                } else {
                    // edit event
                    btnDel.setVisibility(View.VISIBLE);
                    if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                        if (isNullCheck()) {
                            if (inputCheck()) {
                                putEventData(false);
                            }
                        } else {
                            Utils.showShortToast(getActivity(), AppUtils.generateNotNullMessage(getActivity(), R.string.event_event));
                        }
                    } else {
                        rlPriceContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
                        rlAddDateContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
                        etEventValue.setEnabled(true);
                        btnDel.setEnabled(true);
                        rlAddDateContainer.setVisibility(View.VISIBLE);
                        viewAddDateContainer.setVisibility(View.VISIBLE);
                        editListView.setEnabled(false);

                        setFragmentMode(FragmentMode.FragmentModeEdit);

                        llDeleteContainer.setVisibility(View.VISIBLE);
                        btnDel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                                    AppUtils.showDeleteAlert(EventsEditFragment.this, new AppUtils.DeleteConfirmListener() {
                                        @Override
                                        public void onClickDelete() {
                                            deleteEvent();
                                        }
                                    });
                                }

                            }
                        });
                        changeOkState();
                        eventsEditAdapter.notifyDataSetChanged();
                    }
                }
            }
        };

        View.OnClickListener listenerPricing = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etEventValue.getWindowToken(), 0);
                if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(TransKey.EVENT_EVENT_ID, eventId);
                    push(EventsPricingFragment.class, bundle);
                    Log.d("EventsEditFragment", "�ڴ˴����");
                }

            }
        };
        rlPriceContainer.setOnClickListener(listenerPricing);

        View.OnClickListener listenerAddDate = new View.OnClickListener() {   //新增日期

            @Override
            public void onClick(View v) {
                if (getFragmentMode() != FragmentMode.FragmentModeBrowse) {
                    addDate();
                    LayoutUtils.setListViewHeightBasedOnChildren(editListView);
                }
            }
        };
        rlAddDateContainer.setOnClickListener(listenerAddDate);
    }

    @Override
    protected void setLayoutOfControls() {

        rlEventContainer.addView(tvEvent);
        RelativeLayout.LayoutParams paramsEvent = (RelativeLayout.LayoutParams) tvEvent.getLayoutParams();
        paramsEvent.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEvent.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEvent.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsEvent.addRule(RelativeLayout.CENTER_VERTICAL);
        tvEvent.setLayoutParams(paramsEvent);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvEvent, mContext);

        rlEventContainer.addView(etEventValue);
        RelativeLayout.LayoutParams paramsEventValue = (RelativeLayout.LayoutParams) etEventValue.getLayoutParams();
        paramsEventValue.width = (int) (getScreenWidth() * 0.5);
        paramsEventValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsEventValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsEventValue.addRule(RelativeLayout.CENTER_VERTICAL);
        etEventValue.setLayoutParams(paramsEventValue);

        rlPriceContainer.addView(tvPrice);
        RelativeLayout.LayoutParams paramsPrice = (RelativeLayout.LayoutParams) tvPrice.getLayoutParams();
        paramsPrice.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrice.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPrice.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsPrice.addRule(RelativeLayout.CENTER_VERTICAL);
        tvPrice.setLayoutParams(paramsPrice);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvPrice, mContext);

        rlPriceContainer.addView(ivPriceValue);
//        rlPriceContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);//�۸������Ӵ����¼�
        RelativeLayout.LayoutParams paramsPriceValue = (RelativeLayout.LayoutParams) ivPriceValue.getLayoutParams();
        paramsPriceValue.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPriceValue.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsPriceValue.rightMargin = getActualWidthOnThisDevice(10);
        paramsPriceValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        paramsPriceValue.addRule(RelativeLayout.CENTER_VERTICAL);
        ivPriceValue.setLayoutParams(paramsPriceValue);

        rlAddDateContainer.addView(tvAddDate);

        RelativeLayout.LayoutParams paramsAddDate = (RelativeLayout.LayoutParams) tvAddDate.getLayoutParams();
        paramsAddDate.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddDate.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsAddDate.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        paramsAddDate.addRule(RelativeLayout.CENTER_VERTICAL);
        tvAddDate.setLayoutParams(paramsAddDate);
        LayoutUtils.setCellLeftKeyViewOfRelativeLayout(tvAddDate, mContext);

//        btnDel.setBackgroundResource(R.drawable.bg_linear_selector_color_white);ɾ��
        llDeleteContainer.addView(btnDel);
        llDeleteContainer.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams paramsIvDel = (LinearLayout.LayoutParams) btnDel.getLayoutParams();
        paramsIvDel.width = AppUtils.getLargerButtonWidth(this);
        paramsIvDel.height = AppUtils.getLargerButtonHeight(this);
        paramsIvDel.topMargin = getActualHeightOnThisDevice(30);
        paramsIvDel.bottomMargin = getActualHeightOnThisDevice(30);
        btnDel.setLayoutParams(paramsIvDel);

        if (isAdd) {
            llDeleteContainer.setVisibility(View.GONE);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.edit_item, null);
            ImageView ivDelete = (ImageView) view.findViewById(R.id.iv_delete);
            ivDelete.setVisibility(View.GONE);
        }
    }


    @Override
    protected void setPropertyOfControls() {
        tvEvent.setId(View.generateViewId());
        tvEvent.setText(R.string.event_event);
        tvEvent.setTextSize(Constants.FONT_SIZE_NORMAL);

        etEventValue.setId(View.generateViewId());
        etEventValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etEventValue.setGravity(Gravity.END);
        etEventValue.setSingleLine();

        tvPrice.setId(View.generateViewId());
        tvPrice.setTextSize(Constants.FONT_SIZE_NORMAL);
        if (isAdd) {
            String textPricingTable = getString(R.string.event_pricing_table) + getString(R.string.common_operate_after_adding);
            SpannableStringBuilder ssb = new SpannableStringBuilder(textPricingTable);
            ssb.setSpan(new RelativeSizeSpan(1.f), 0, getString(R.string.event_pricing_table).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ssb.setSpan(new RelativeSizeSpan(0.8f), getString(R.string.event_pricing_table).length(), textPricingTable.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            tvPrice.setText(ssb);
        } else {
            tvPrice.setText(R.string.event_pricing_table);
        }


        ivPriceValue.setId(View.generateViewId());
        ivPriceValue.setBackgroundResource(R.drawable.icon_black);

        if (isAdd) {
            ivPriceValue.setVisibility(View.INVISIBLE);
        }

        tvAddDate.setId(View.generateViewId());
        tvAddDate.setText(R.string.event_add_date);
        tvAddDate.setTextColor(getResources().getColor(R.color.common_blue));
        tvAddDate.setTextSize(Constants.FONT_SIZE_NORMAL);


        btnDel.setGravity(Gravity.CENTER);
        if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
            llDeleteContainer.setVisibility(View.INVISIBLE);
            rlAddDateContainer.setVisibility(View.INVISIBLE);
            viewAddDateContainer.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();

        getTvLeftTitle().setText(R.string.common_edit);

        getTvRight().setText(Constants.STR_EMPTY);
        getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
        getTvRight().setVisibility(View.VISIBLE);
        getTvRight().setOnClickListener(listenerEdit);

        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            getTvRight().setText(R.string.common_ok);
            getTvRight().setBackground(null);

        } else {
            getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
            if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                getTvRight().setText(R.string.common_ok);
                getTvRight().setBackground(null);
            } else {
                btnDel.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void executeOnceOnCreate() {
        // add event
        if (isAdd) {
            addDate();
        } else {
            getEventData();
        }
    }

    private void changeOkState() {
        if (getFragmentMode() == FragmentMode.FragmentModeAdd) {
            getTvRight().setText(R.string.common_save);
            getTvRight().setBackground(null);
        } else {
            if (getFragmentMode() == FragmentMode.FragmentModeEdit) {
                getTvRight().setText(R.string.common_ok);
                getTvRight().setBackground(null);
                eventsEditAdapter = new EventsEditAdapter(getActivity(), dataList, listenerPop, listenerDelete);
                editListView.setAdapter(eventsEditAdapter);
                LayoutUtils.setListViewHeightBasedOnChildren(editListView);
            } else {
                getTvRight().setText(Constants.STR_EMPTY);
                getTvRight().setBackgroundResource(R.drawable.icon_common_edit);
                eventsEditAdapter = new EventsEditAdapter(getActivity(), dataList, listenerPop, listenerDelete);
                editListView.setAdapter(eventsEditAdapter);
                LayoutUtils.setListViewHeightBasedOnChildren(editListView);
            }
        }
    }

    private void getEventData() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.EVENT_LIST_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.EVENT_LIST_USER_ID, AppUtils.getCurrentUserId(getActivity()));
        params.put(ApiKey.EVENTS_EVENTS_ID, String.valueOf(eventId));

        HttpManager<JsonEventsEditGet> hh = new HttpManager<JsonEventsEditGet>(EventsEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonEventsEditGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    if (jo.getEventInfo() != null) {
                        if (jo.getEventInfo().dateInfoList.size() > 0) {
                            dataList.clear();
                            dataList.addAll(jo.getEventInfo().dateInfoList);
                            eventInfo = jo.getEventInfo();
                            etEventValue.setText(eventInfo.eventName);
                            eventsEditAdapter = new EventsEditAdapter(getActivity(), dataList, listenerPop, listenerDelete);

                            editListView.setAdapter(eventsEditAdapter);
                            LayoutUtils.setListViewHeightBasedOnChildren(editListView);
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
        hh.startGet(getActivity(), ApiManager.HttpApi.EventsEditGet, params);
    }

    private void putEventData(boolean isConfirmed) {
        String dateInfo = getDateInfoString();

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.EVENT_LIST_TOKEN, token);
        params.put(ApiKey.EVENT_LIST_USER_ID, userId);
        params.put(ApiKey.EVENTS_EVENTS_ID, String.valueOf(eventId));
        params.put(ApiKey.EVENT_NAME, etEventValue.getText().toString());
        params.put(ApiKey.EVENT_DATE_INFO, dateInfo);
        if (isConfirmed) {
            params.put(ApiKey.EVENT_DATE_FLAG, Constants.STR_1);
        }

        if (delEventsDateIdStr != null && delEventsDateIdStr.length() > 0) {
            delEventsDateIdStr = delEventsDateIdStr.substring(0, delEventsDateIdStr.length() - 1);
        }

        params.put(ApiKey.EVENTS_DEL_EVENTS_DATEID, delEventsDateIdStr);

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(EventsEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode.equals(Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY)) {
                    if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                        doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                    } else {
                        doBackWithRefresh();
                    }
                } else if (Constants.RETURN_CODE_20137_CURRENT_DATE_HAS_BOOKED == returnCode) {
                    AppUtils.NoDoubleClickListener clickListener
                            = new AppUtils.NoDoubleClickListener(getActivity()) {
                        @Override
                        public void noDoubleClick(View v) {
                            putEventData(true);
                        }
                    };
                    ConfirmPopWindow confirmPopWindow = new ConfirmPopWindow(getActivity(), clickListener);
                    confirmPopWindow.setMessage(getString(R.string.msg_this_time_has_been_reserved));
                    confirmPopWindow.showAtLocation(getRootView().findViewById(R.id.rl_content_container),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                Utils.showShortToast(getActivity(), String.valueOf(error));
            }
        };
        hh.startPut(getActivity(), ApiManager.HttpApi.EventsPut, params);
    }

    private String getDateInfoString() {
        JSONArray jsonArray = new JSONArray();
        for (int j = 0; j < dataList.size(); j++) {
            Integer id = dataList.get(j).eventDateId;
            String date = dataList.get(j).eventDate;
            String startTime = dataList.get(j).eventStartTime;
            String endTime = dataList.get(j).eventEndTime;
            String courseAreaId = dataList.get(j).courseAreaId;

            Map<String, Object> map = new HashMap<>();
            if (!isAdd) {
                map.put(JsonKey.EVENT_EVENTS_DATE_ID, id == null ? Constants.STR_EMPTY : id);
            }
            map.put(JsonKey.EVENT_EVENTS_DATE, date);
            map.put(JsonKey.EVENT_EVENTS_START_TIME, startTime);
            map.put(JsonKey.EVENT_EVENTS_END_TIME, endTime);
            map.put(JsonKey.EVENT_EVENTS_COURSE_AREA_ID, courseAreaId);

            JSONObject object = new JSONObject(map);
            jsonArray.put(object);
        }
        return jsonArray.toString();
    }

    private void postEventData(boolean isConfirmed) {
        String dateInfo = getDateInfoString();

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.EVENT_LIST_TOKEN, token);
        params.put(ApiKey.EVENT_LIST_USER_ID, userId);
        params.put(ApiKey.EVENT_NAME, etEventValue.getText().toString());
        params.put(ApiKey.EVENT_DATE_INFO, dateInfo);
        if (isConfirmed) {
            params.put(ApiKey.EVENT_DATE_FLAG, Constants.STR_1);
        }

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(EventsEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY == returnCode) {
                    if (fromPage.equals(TeeTimeMemberListWithIndexFragment.class.getName())) {
                        doBackWithRefresh(TeeTimeMemberListWithIndexFragment.class);
                    } else {

                        if (Utils.isStringNotNullOrEmpty(fromPage)) {
                            Bundle bundle = new Bundle();
                            bundle.putString("addId", jo.getAddId());
                            bundle.putString(TransKey.COMMON_FROM_PAGE, PlayerInfoEditFragment.class.getName());
                            try {
                                doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            doBackWithRefresh();

                        }


                    }
                } else if (Constants.RETURN_CODE_20137_CURRENT_DATE_HAS_BOOKED == returnCode) {
                    AppUtils.NoDoubleClickListener clickListener
                            = new AppUtils.NoDoubleClickListener(getActivity()) {
                        @Override
                        public void noDoubleClick(View v) {
                            postEventData(true);
                        }
                    };
                    ConfirmPopWindow confirmPopWindow = new ConfirmPopWindow(getActivity(), clickListener);
                    confirmPopWindow.setMessage(getString(R.string.msg_this_time_has_been_reserved));
                    confirmPopWindow.showAtLocation(getRootView().findViewById(R.id.rl_content_container),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                Utils.showShortToast(getActivity(), String.valueOf(error));
            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.EventsPost, params);
    }

    private void deleteEvent() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.EVENT_LIST_TOKEN, token);
        params.put(ApiKey.EVENT_LIST_USER_ID, userId);
        params.put(ApiKey.EVENTS_EVENTS_ID, String.valueOf(eventId));

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(EventsEditFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20403_DELETE_SUCCESSFULLY) {
                    getBaseActivity().doBackWithRefresh();
                } else {
                    Utils.showShortToast(getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {
                Utils.showShortToast(getActivity(), String.valueOf(error));
            }
        };
        hh.startDelete(getActivity(), ApiManager.HttpApi.EventsDelete, params);
    }


    private void addDate() {
        JsonEventsEditGet.EventInfo.DateInfo di = new JsonEventsEditGet.EventInfo.DateInfo();
        dataList.add(di);
        eventsEditAdapter = new EventsEditAdapter(getActivity(), dataList, listenerPop, listenerDelete);
        editListView.setAdapter(eventsEditAdapter);
        editListView.setDividerHeight(0);
    }


    private boolean isNullCheck() {
        return Utils.isStringNotNullOrEmpty(etEventValue.getText().toString());
    }


    public boolean inputCheck() {
        if (dataList.size() > 0) {
            int count = 0;
            for (int i = 0; i < dataList.size(); i++) {
                String date = dataList.get(i).getEventDate();
                String startTime = dataList.get(i).getEventStartTime();
                String endTime = dataList.get(i).getEventEndTime();
                String course = dataList.get(i).getCourseAreaId();
                if (itemInputCheck(date, startTime, endTime, course)) {
                    count++;
                } else {
                    break;
                }
            }
            return count == dataList.size();
        } else {
            Utils.showShortToast(getActivity(), "Enter info!");
            return false;
        }
    }


    public boolean itemInputCheck(String date, String startTime, String endTime, String course) {

        SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMAT_HHMM, Locale.getDefault());

        if (Utils.isStringNullOrEmpty(date)) {
            Utils.showShortToast(getActivity(), "Enter date!");
            return false;
        } else if (!Utils.isStringNotNullOrEmpty(startTime)) {
            Utils.showShortToast(getActivity(), getResources().getString(R.string.segment_times_start_time_input));
            return false;
        } else if (!Utils.isStringNotNullOrEmpty(endTime)) {
            Utils.showShortToast(getActivity(), getResources().getString(R.string.segment_times_end_time_input));
            return false;
        } else if (Utils.isStringNullOrEmpty(course)) {
            Utils.showShortToast(getActivity(), "chose course!");
            return false;
        } else if (Utils.isStringNotNullOrEmpty(startTime) && Utils.isStringNotNullOrEmpty(endTime)) {

            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();
            try {
                c1.setTime(dateFormat.parse(startTime));
                c2.setTime(dateFormat.parse(endTime));

            } catch (ParseException e) {
                Utils.log(e.getMessage());
            }

            if (c2.compareTo(c1) <= 0) {
                Utils.showShortToast(getActivity(), getResources().getString(R.string.segment_times_time_input));
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }

    }

    class EventsEditAdapter extends BaseAdapter {

        private Context mContext = null;
        private List<JsonEventsEditGet.EventInfo.DateInfo> dataList;
        private View.OnClickListener listener;
        private View.OnClickListener deleteListener;

        public EventsEditAdapter(Context context, List<JsonEventsEditGet.EventInfo.DateInfo> dataList, View.OnClickListener listener,
                                 View.OnClickListener deleteListener) {
            this.mContext = context;
            this.dataList = dataList;
            this.listener = listener;
            this.deleteListener = deleteListener;
        }

        @Override
        public int getCount() {
            if (dataList != null) {
                return dataList.size();
            }
            return 0;
        }


        @Override
        public Object getItem(int position) {
            return dataList.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                LayoutInflater mInflater = LayoutInflater.from(mContext);
                convertView = mInflater.inflate(R.layout.events_edit_item, null);

                holder.rlDateContainer = (RelativeLayout) convertView.findViewById(R.id.rl_date_container);
                holder.rlStartTimeContainer = (RelativeLayout) convertView.findViewById(R.id.rl_start_time_container);
                holder.rlEndTimeContainer = (RelativeLayout) convertView.findViewById(R.id.rl_end_time_container);
                holder.rlCourseContainer = (RelativeLayout) convertView.findViewById(R.id.rl_course_container);
                holder.dateKey = (IteeTextView) convertView.findViewById(R.id.tv_dateKey);
                holder.dateValue = (IteeTextView) convertView.findViewById(R.id.tv_dateValue);
                holder.startTimeKey = (IteeTextView) convertView.findViewById(R.id.tv_startTimeKey);
                holder.startTimeValue = (IteeTextView) convertView.findViewById(R.id.tv_startTimeValue);
                holder.endTimeKey = (IteeTextView) convertView.findViewById(R.id.tv_end_time_key);
                holder.endTimeValue = (IteeTextView) convertView.findViewById(R.id.tv_end_time_value);
                holder.courseKey = (IteeTextView) convertView.findViewById(R.id.tv_course_key);
                holder.courseValue = (IteeTextView) convertView.findViewById(R.id.tv_course_value);
                holder.courseValue.setSingleLine();
                holder.courseValue.setSingleLine(true);
                holder.courseValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
                holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_delete);

                RelativeLayout.LayoutParams courseValueParams = (RelativeLayout.LayoutParams) holder.courseValue.getLayoutParams();

                LayoutUtils.setLayoutHeight(holder.rlDateContainer, 100, mContext);
                LayoutUtils.setLayoutHeight(holder.rlStartTimeContainer, 100, mContext);
                LayoutUtils.setLayoutHeight(holder.rlEndTimeContainer, 100, mContext);
                LayoutUtils.setLayoutHeight(holder.rlCourseContainer, 100, mContext);

                LayoutUtils.setCellLeftKeyViewOfRelativeLayout(holder.dateKey, mContext);
                LayoutUtils.setCellLeftKeyViewOfRelativeLayout(holder.startTimeKey, mContext);
                LayoutUtils.setCellLeftKeyViewOfRelativeLayout(holder.endTimeKey, mContext);
                LayoutUtils.setCellLeftKeyViewOfRelativeLayout(holder.courseKey, mContext);

                RelativeLayout.LayoutParams ivDeleteLayoutParams = (RelativeLayout.LayoutParams) holder.ivDelete.getLayoutParams();
                ivDeleteLayoutParams.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//                ivDeleteLayoutParams.addRule(RelativeLayout.END_OF, R.id.tv_dateKey);
                holder.ivDelete.setLayoutParams(ivDeleteLayoutParams);

                courseValueParams.width = DensityUtil.getActualWidthOnThisDevice(400, mContext);
                holder.courseValue.setEllipsize(TextUtils.TruncateAt.END);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                holder.rlCourseContainer.setBackgroundResource(R.color.common_white);
                holder.rlDateContainer.setBackgroundResource(R.color.common_white);
                holder.rlEndTimeContainer.setBackgroundResource(R.color.common_white);
                holder.rlStartTimeContainer.setBackgroundResource(R.color.common_white);
            }else {
                holder.rlCourseContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
                holder.rlEndTimeContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
                holder.rlStartTimeContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
                holder.rlDateContainer.setBackgroundResource(R.drawable.bg_linear_selector_color_white);
            }

            JsonEventsEditGet.EventInfo.DateInfo dateInfo = dataList.get(position);

            holder.dateKey.setText(R.string.agents_date);
            holder.dateValue.setText(DateUtils.getCurrentShowYearMonthDayFromApiDateStr(dateInfo.eventDate, mContext));
            holder.startTimeKey.setText(R.string.event_start_time);
            holder.startTimeValue.setText(dateInfo.eventStartTime);
            holder.endTimeKey.setText(R.string.event_end_time);
            holder.endTimeValue.setText(dateInfo.eventEndTime);
            holder.courseKey.setText(R.string.event_course);
            holder.courseValue.setText(dateInfo.courseAreaName);

            holder.dateKey.setTextSize(Constants.FONT_SIZE_NORMAL);
            holder.dateValue.setTextSize(Constants.FONT_SIZE_NORMAL);
            holder.startTimeKey.setTextSize(Constants.FONT_SIZE_NORMAL);
            holder.startTimeValue.setTextSize(Constants.FONT_SIZE_NORMAL);
            holder.endTimeKey.setTextSize(Constants.FONT_SIZE_NORMAL);
            holder.endTimeValue.setTextSize(Constants.FONT_SIZE_NORMAL);
            holder.courseKey.setTextSize(Constants.FONT_SIZE_NORMAL);
            holder.courseValue.setTextSize(Constants.FONT_SIZE_NORMAL);

            holder.rlDateContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag(0 + Constants.STR_COMMA + position);
                    listener.onClick(view);
                }
            });

            holder.rlStartTimeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag(1 + Constants.STR_COMMA + position);
                    listener.onClick(view);
                }
            });

            holder.rlEndTimeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag(2 + Constants.STR_COMMA + position);
                    listener.onClick(view);
                }
            });

            holder.rlCourseContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag(3 + Constants.STR_COMMA + position);
                    listener.onClick(view);
                }
            });

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag(position);
                    deleteListener.onClick(view);
                }
            });

//            if (getFragmentMode() != FragmentMode.FragmentModeEdit) {
          if (getFragmentMode() == FragmentMode.FragmentModeBrowse) {
                holder.ivDelete.setVisibility(View.INVISIBLE);
            } else {
                holder.ivDelete.setVisibility(View.VISIBLE);
                if (position == 0) {
                    holder.ivDelete.setVisibility(View.GONE);
                }
            }

            return convertView;
        }


        class ViewHolder {
            public IteeTextView dateKey;
            public IteeTextView dateValue;
            public IteeTextView startTimeKey;
            public IteeTextView startTimeValue;
            public IteeTextView endTimeKey;
            public IteeTextView endTimeValue;
            public IteeTextView courseKey;
            public IteeTextView courseValue;
            public ImageView ivDelete;
            RelativeLayout rlDateContainer;
            RelativeLayout rlStartTimeContainer;
            RelativeLayout rlEndTimeContainer;
            RelativeLayout rlCourseContainer;
        }
    }

}
