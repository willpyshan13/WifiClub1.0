/**
 * Project Name: itee
 * File Name:	 LocationChoiceAdapter.java
 * Package Name: cn.situne.itee.fragment.teetime
 * Date:		 2015-01-21
 * Copyright (c) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.LayoutUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.BookingTimeEntity;
import cn.situne.itee.entity.LocationChoiceAdapterEntity;
import cn.situne.itee.entity.PositionInformationEntity;
import cn.situne.itee.fragment.teetime.LocationListFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.SlideListView;

/**
 * ClassName:LocationChoiceAdapter <br/>
 * Function: adapter of booking page. <br/>
 * UI: 03-3
 * Date: 2015-01-21 <br/>
 *
 * @author dengjunzeng
 * @version 1.0
 * @see
 */
public class LocationChoiceAdapter extends BaseAdapter {

    private boolean isFirstCourse;
    private int locationNumber;

    private LocationListFragment fragment;

    private ArrayList<BookingTimeEntity> bookingTimeEntities;
    private ArrayList<SlideListView> slideListViewArrayList = new ArrayList<>();
    private LinkedList<LocationChoiceAdapterEntity> dataSourceList;

    private View.OnClickListener clickListener;
    private View.OnClickListener pushSearchFragmentListener;
    private View.OnClickListener listenerAdd;
    private View.OnClickListener listenerShow;

    public LocationChoiceAdapter(LocationListFragment fragment, LinkedList<LocationChoiceAdapterEntity> list, int locationNumber) {
        this.fragment = fragment;
        dataSourceList = list;
        bookingTimeEntities = new ArrayList<>();
        this.locationNumber = locationNumber;
    }

    public void setIsFirstCourse(boolean isFirstCourse) {
        this.isFirstCourse = isFirstCourse;
    }

    @Override
    public int getCount() {
        if (Utils.isListNotNullOrEmpty(dataSourceList)) {
            return dataSourceList.size() + 1;
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return dataSourceList.get(position + 1);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        if (view == null) {
            view = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_of_fragment_location_choice, null);
            holder = new ViewHolder();
            holder.llNullLocation = (LinearLayout) view.findViewById(R.id.ll_null_location);
            holder.llLocations = (LinearLayout) view.findViewById(R.id.ll_locations);
            holder.llLocationDetails = (LinearLayout) view.findViewById(R.id.ll_location_details);
            holder.llSearch = (RelativeLayout) view.findViewById(R.id.ll_search);

            ImageView iconSearch = (ImageView) view.findViewById(R.id.iconSearch);
            iconSearch.getLayoutParams().height = this.fragment.getActionBarHeight() - fragment.getActualHeightOnThisDevice(35);
            iconSearch.getLayoutParams().width = this.fragment.getActionBarHeight() - fragment.getActualHeightOnThisDevice(35);
            holder.tvTime = (IteeTextView) view.findViewById(R.id.tv_time);

            for (int i = 0; i < locationNumber; i++) {
                ImageView imageView = new ImageView(fragment.getActivity());
                holder.llLocations.addView(imageView);
                LinearLayout.LayoutParams paramsImageView = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                paramsImageView.width = 0;
                paramsImageView.height = LinearLayout.LayoutParams.MATCH_PARENT;
                paramsImageView.weight = 1;
                imageView.setLayoutParams(paramsImageView);
                holder.arr.add(imageView);
            }

            holder.lvLocations = (SlideListView) view.findViewById(R.id.lv_locations);
            if (holder.lvLocations != null) {
                slideListViewArrayList.add(holder.lvLocations);
                holder.lvLocations.setSlideListViewListener(new SlideListView.SlideListViewListener() {
                    @Override
                    public void scrollItem(View item) {
                        for (SlideListView slv : slideListViewArrayList) {
                            if (slv.isSlided()) {
                                slv.slideBack();
                            }
                        }
                    }
                });
            }

            holder.etSearch = (IteeTextView) view.findViewById(R.id.et_search);
            holder.ivDivider = (ImageView) view.findViewById(R.id.iv_divider);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (position == 0) {
            holder.llSearch.setVisibility(View.VISIBLE);
            holder.ivDivider.setVisibility(View.GONE);
            holder.llNullLocation.setVisibility(View.GONE);
            holder.llLocationDetails.setVisibility(View.GONE);
            holder.llSearch.setOnClickListener(pushSearchFragmentListener);
        } else {
            if (position == 1) {
                holder.ivDivider.setVisibility(View.GONE);
            } else {
                holder.ivDivider.setVisibility(View.VISIBLE);
            }

            final LocationChoiceAdapterEntity entity = dataSourceList.get(position - 1);
            holder.tvTime.setText(entity.getTime());
            holder.llSearch.setVisibility(View.GONE);
            holder.llNullLocation.setVisibility(View.VISIBLE);

            for (int i = 0; i < locationNumber; i++) {
                final ImageView imageView = holder.arr.get(i);
                imageView.setOnClickListener(null);
                imageView.setOnLongClickListener(null);

                int positionStates = entity.getPositionStates().get(i);
                if (positionStates == 2) {
                    imageView.setImageResource(0);
                    if (i == 0) {
                        imageView.setBackgroundResource(R.drawable.icon_location_left);
                    } else if (i == holder.arr.size() - 1) {
                        imageView.setBackgroundResource(R.drawable.icon_location_right);
                    } else {
                        imageView.setBackgroundResource(R.drawable.icon_location_middle);
                    }
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
                } else {
                    if (Constants.SEGMENT_TIME_MEMBER_ONLY_ID.equals(entity.getSegmentTimeTypeId())) {
                        imageView.setImageResource(R.drawable.icon_m);
                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Utils.showShortToast(fragment.getActivity(), R.string.booking_message_member_only);
                                return false;
                            }
                        });
                    } else if (Constants.SEGMENT_TIME_BLOCK_TIMES_ID.equals(entity.getSegmentTimeTypeId())) {
                        imageView.setImageResource(R.drawable.icon_password);
                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Utils.showShortToast(fragment.getActivity(), R.string.booking_message_block_time);
                                return false;
                            }
                        });
                    } else if (Constants.SEGMENT_TIME_TRANSFER_TIME_BLOCK_ID.equals(entity.getSegmentTimeTypeId())) {
                        imageView.setImageResource(R.drawable.icon_password);
                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Utils.showShortToast(fragment.getActivity(), R.string.booking_message_transfer_time);
                                return false;
                            }
                        });
                    } else if (Constants.SEGMENT_TIME_PRIME_TIME_ID.equals(entity.getSegmentTimeTypeId())) {
                        imageView.setImageResource(R.drawable.icon_prime_time);
                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Utils.showShortToast(fragment.getActivity(), R.string.booking_message_prime_time);
                                return false;
                            }
                        });
                    } else if (Constants.SEGMENT_TIME_EVENT_TIME_ID.equals(entity.getSegmentTimeTypeId())) {
                        imageView.setImageResource(R.drawable.icon_event_time);
                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Utils.showShortToast(fragment.getActivity(), R.string.booking_message_event_time);
                                return false;
                            }
                        });
                    } else if (Constants.SEGMENT_TIME_PRIME_DISCOUNT_ID.equals(entity.getSegmentTimeTypeId())) {
                        imageView.setImageResource(R.drawable.icon_percent);
                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                Utils.showShortToast(fragment.getActivity(), R.string.booking_message_price_discount);
                                return false;
                            }
                        });
                    } else {
                        imageView.setImageResource(R.drawable.icon_location_add);
                        imageView.setOnLongClickListener(null);
                    }

                    // when the course is lock
                    if (Constants.SEGMENT_TIME_BLOCK_TIMES_ID.equals(entity.getSegmentTimeTypeId())) {
                        if (i == 0) {
                            imageView.setBackgroundResource(R.drawable.bg_grey_location_left);
                        } else if (i == holder.arr.size() - 1) {
                            imageView.setBackgroundResource(R.drawable.bg_grey_location_right);
                        } else {
                            imageView.setBackgroundResource(R.drawable.bg_grey_location_middle);
                        }
                    } else if (Constants.SEGMENT_TIME_TRANSFER_TIME_BLOCK_ID.equals(entity.getSegmentTimeTypeId())) {
                        if (i == 0) {
                            imageView.setBackgroundResource(R.drawable.bg_green_location_left);
                        } else if (i == holder.arr.size() - 1) {
                            imageView.setBackgroundResource(R.drawable.bg_green_location_right);
                        } else {
                            imageView.setBackgroundResource(R.drawable.bg_green_location_middle);
                        }
                    } else {
                        if (positionStates == 0) {
                            if (i == 0) {
                                imageView.setBackgroundResource(R.drawable.bg_green_location_left);
                            } else if (i == holder.arr.size() - 1) {
                                imageView.setBackgroundResource(R.drawable.bg_green_location_right);
                            } else {
                                imageView.setBackgroundResource(R.drawable.bg_green_location_middle);
                            }
                        } else if (positionStates == 1) {
                            if (i == 0) {
                                imageView.setBackgroundResource(R.drawable.bg_green_location_left_choice);
                            } else if (i == holder.arr.size() - 1) {
                                imageView.setBackgroundResource(R.drawable.bg_green_location_right_choice);
                            } else {
                                imageView.setBackgroundResource(R.drawable.bg_green_location_middle_choice);
                            }
                        }

                        final int index = i;
                        final int arrSize = holder.arr.size();
                        imageView.setTag(entity);
                        final BookingTimeEntity bookingTimeEntity = new BookingTimeEntity(entity.getTime(), entity.getSegmentTimeTypeId(), entity.isPrimeTime(), entity.getSegmentTimeSetting());
                        clickListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!Constants.SEGMENT_TIME_BLOCK_TIMES_ID.equals(entity.getSegmentTimeTypeId())
                                        && !Constants.SEGMENT_TIME_TRANSFER_TIME_BLOCK_ID.equals(entity.getSegmentTimeTypeId())) {

                                    //  boolean isOneTeeOnly = AppUtils.isOneTeeOnly(fragment.getActivity());
                                    // if (isOneTeeOnly && !isFirstCourse && Utils.isStringNullOrEmpty(entity.getSegmentTimeTypeId())) {

                                    //  if (!isFirstCourse && Utils.isStringNullOrEmpty(entity.getSegmentTimeTypeId())) {
                                    if (false) {
                                        Utils.showShortToast(fragment.getActivity(), R.string.msg_one_tee_only);
                                    } else {
                                        if (entity.getPositionStates().get(index) == 1) {
                                            if (index == 0) {
                                                imageView.setBackgroundResource(R.drawable.bg_green_location_left);
                                            } else if (index == arrSize - 1) {
                                                imageView.setBackgroundResource(R.drawable.bg_green_location_right);
                                            } else {
                                                imageView.setBackgroundResource(R.drawable.bg_green_location_middle);
                                            }
                                            entity.getPositionStates().set(index, 0);
                                            bookingTimeEntities.remove(bookingTimeEntity);

                                        } else if (entity.getPositionStates().get(index) == 0) {
                                            if (index == 0) {
                                                imageView.setBackgroundResource(R.drawable.bg_green_location_left_choice);
                                            } else if (index == arrSize - 1) {
                                                imageView.setBackgroundResource(R.drawable.bg_green_location_right_choice);
                                            } else {
                                                imageView.setBackgroundResource(R.drawable.bg_green_location_middle_choice);
                                            }
                                            entity.getPositionStates().set(index, 1);
                                            bookingTimeEntities.add(bookingTimeEntity);
                                        }
                                        if (getListenerAdd() != null) {
                                            getListenerAdd().onClick(view);
                                        }
                                    }
                                }
                            }
                        };
                        imageView.setOnClickListener(clickListener);
                    }
                }
            }

            final ArrayList<ImageView> arr = holder.arr;
            holder.llNullLocation.setTag(entity);

            holder.llNullLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!Constants.SEGMENT_TIME_BLOCK_TIMES_ID.equals(entity.getSegmentTimeTypeId())
                            && !Constants.SEGMENT_TIME_TRANSFER_TIME_BLOCK_ID.equals(entity.getSegmentTimeTypeId())) {
                        //boolean isOneTeeOnly = AppUtils.isOneTeeOnly(fragment.getActivity());
                        //if (!isFirstCourse && Utils.isStringNullOrEmpty(entity.getSegmentTimeTypeId())) {
                        if (false) {
                            Utils.showShortToast(fragment.getActivity(), R.string.msg_one_tee_only);
                        } else {
                            if (entity.getPositionStates().contains(0)) {
                                for (int i = 0; i < locationNumber; i++) {
                                    BookingTimeEntity bookingTimeEntity = new BookingTimeEntity(entity.getTime(), entity.getSegmentTimeTypeId(), entity.isPrimeTime(), entity.getSegmentTimeSetting());
                                    if (entity.getPositionStates().get(i) == 0) {
                                        if (i == 0) {
                                            arr.get(i).setBackgroundResource(R.drawable.bg_green_location_left_choice);
                                        } else if (i == arr.size() - 1) {
                                            arr.get(i).setBackgroundResource(R.drawable.bg_green_location_right_choice);
                                        } else {
                                            arr.get(i).setBackgroundResource(R.drawable.bg_green_location_middle_choice);
                                        }
                                        entity.getPositionStates().set(i, 1);
                                        bookingTimeEntities.add(bookingTimeEntity);
                                        if (getListenerAdd() != null) {
                                            getListenerAdd().onClick(view);
                                        }
                                    }
                                }
                            } else {
                                for (int i = 0; i < locationNumber; i++) {
                                    BookingTimeEntity bookingTimeEntity = new BookingTimeEntity(entity.getTime(), entity.getSegmentTimeTypeId(), entity.isPrimeTime(), entity.getSegmentTimeSetting());
                                    if (entity.getPositionStates().get(i) == 1) {
                                        if (i == 0) {
                                            arr.get(i).setBackgroundResource(R.drawable.bg_green_location_left);
                                        } else if (i == arr.size() - 1) {
                                            arr.get(i).setBackgroundResource(R.drawable.bg_green_location_right);
                                        } else {
                                            arr.get(i).setBackgroundResource(R.drawable.bg_green_location_middle);
                                        }
                                        entity.getPositionStates().set(i, 0);
                                        bookingTimeEntities.remove(bookingTimeEntity);
                                        if (getListenerAdd() != null) {
                                            getListenerAdd().onClick(view);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            });

            if (entity.getAppointmentNumber() > 0) {
                View.OnClickListener listenerHideRight = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.lvLocations.slideBack();
                    }
                };
                holder.llLocationDetails.setVisibility(View.VISIBLE);
                holder.adapter = new LocationChoiceDetailsAdapter(fragment, entity.getPositionInformationList(),
                        getListenerShow(), listenerHideRight);
                holder.lvLocations.initSlideMode(2);
                holder.lvLocations.setAdapter(holder.adapter);
                LayoutUtils.setListViewHeightBasedOnChildren(holder.lvLocations);

            } else {
                holder.llLocationDetails.setVisibility(View.GONE);
            }

            holder.lvLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    PositionInformationEntity positionInformationEntity = entity.getPositionInformationList().get(position);
                    if (Constants.STR_1.equals(positionInformationEntity.getLookFlag())) {
                        if (AppUtils.isAgent(fragment.getActivity())) {
                            if (Constants.STR_1.equals(positionInformationEntity.getSelfFlag())) {
                                Bundle bundle = new Bundle();
                                bundle.putString(TransKey.BOOKING_ORDER_NO, positionInformationEntity.getAppointmentOrderNo());
                                bundle.putBoolean("isAdd", false);
                                bundle.putString(TransKey.COMMON_FROM_PAGE, LocationListFragment.class.getName());
                                fragment.push(TeeTimeAddFragment.class, bundle);
                            }
                        } else {
                            Bundle bundle = new Bundle();
                            bundle.putString(TransKey.BOOKING_ORDER_NO, positionInformationEntity.getAppointmentOrderNo());
                            bundle.putBoolean("isAdd", false);
                            bundle.putString(TransKey.COMMON_FROM_PAGE, LocationListFragment.class.getName());
                            fragment.push(TeeTimeAddFragment.class, bundle);
                        }
                    } else {
                        AppUtils.showHaveNoPermission(fragment);
                    }
                }
            });
        }
        holder.lvLocations.setSelectionFromTop(1,30);
        return view;
    }

    public View.OnClickListener getPushSearchFragmentListener() {
        return pushSearchFragmentListener;
    }

    public void setPushSearchFragmentListener(View.OnClickListener pushSearchFragmentListener) {
        this.pushSearchFragmentListener = pushSearchFragmentListener;
    }

    public View.OnClickListener getListenerAdd() {
        return listenerAdd;
    }

    public void setListenerAdd(View.OnClickListener listenerAdd) {
        this.listenerAdd = listenerAdd;
    }

    public View.OnClickListener getListenerShow() {
        return listenerShow;
    }

    public void setListenerShow(View.OnClickListener listenerShow) {
        this.listenerShow = listenerShow;
    }

    public ArrayList<String> getSelectedTimeList() {
        ArrayList<String> selectedTimeList = new ArrayList<>();
        for (BookingTimeEntity bookingTimeEntity : bookingTimeEntities) {
            selectedTimeList.add(Utils.getStringFromObject(bookingTimeEntity));
        }
        return selectedTimeList;
    }

    public boolean getIsAllowCommit() {
        HashMap<String, Integer> hashMap = new HashMap<>();
        String bookingTime;
        for (BookingTimeEntity bookingTimeEntity : bookingTimeEntities) {
            bookingTime = bookingTimeEntity.getBookingTime();
            if (hashMap.containsKey(bookingTime)) {
                hashMap.put(bookingTime, hashMap.get(bookingTime) + 1);
            } else {
                hashMap.put(bookingTime, 1);
            }
        }
        for (LocationChoiceAdapterEntity locationChoiceAdapterEntity : dataSourceList) {
            if (hashMap.containsKey(locationChoiceAdapterEntity.getTime())) {
                if (Utils.isStringNotNullOrEmpty(locationChoiceAdapterEntity.getSegmentTimeSetting())
                        && locationChoiceAdapterEntity.isPrimeTime()
                        && locationChoiceAdapterEntity.getAppointmentNumber() < 3
                        && hashMap.get(locationChoiceAdapterEntity.getTime()) < Integer.valueOf(locationChoiceAdapterEntity.getSegmentTimeSetting())) {
                    return false;
                }
            }
        }
        return true;
    }

    public ArrayList<BookingTimeEntity> getBookingTimeEntities() {
        return bookingTimeEntities;
    }

    class ViewHolder {
        LinearLayout llNullLocation;//位置信息
        LinearLayout llLocations;
        IteeTextView tvTime;
        LinearLayout llLocationDetails;//位置详情
        SlideListView lvLocations; //详情列表
        RelativeLayout llSearch;//搜索框
        IteeTextView etSearch;
        ImageView ivDivider;//间隔线
        LocationChoiceDetailsAdapter adapter;
        ArrayList<ImageView> arr = new ArrayList<>();
    }
}
