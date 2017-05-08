package cn.situne.itee.adapter;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.PositionInformationEntity;
import cn.situne.itee.fragment.shopping.ShoppingGoodsListFragment;
import cn.situne.itee.fragment.shopping.ShoppingPaymentFragment;
import cn.situne.itee.fragment.teetime.LocationListFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonCheckOut;
import cn.situne.itee.view.IteeTextView;


public class LocationChoiceDetailsAdapter extends BaseAdapter {

    private static final String IS_PAID_ALL = Constants.STR_1;

    private static final String STATUS_F = "F";
    private static final String STATUS_R = "R";

    private static final int BOOKING_COLOR_RED = 2;
    private static final int BOOKING_COLOR_BLUE = 3;

    private LocationListFragment fragment;
    private ArrayList<PositionInformationEntity> positionInformationList;
    private int myIndex = 0;
    private View.OnClickListener listener;
    private View.OnClickListener listenerHideRight;

    public LocationChoiceDetailsAdapter(LocationListFragment fragment, ArrayList<PositionInformationEntity> positionInformationList,
                                        View.OnClickListener listener, View.OnClickListener listenerHideRight) {
        this.fragment = fragment;
        this.positionInformationList = positionInformationList;
        this.listener = listener;
        this.listenerHideRight = listenerHideRight;
    }

    @Override
    public int getCount() {
        return positionInformationList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View childView, ViewGroup viewGroup) {

        ViewHolder myHolder;

        if (childView == null) {
            childView = LayoutInflater.from(fragment.getActivity()).inflate(R.layout.item_of_location_show, null);

            myHolder = new ViewHolder();
            myHolder.setCanSlide("count");

            myHolder.rlRowContainer = (RelativeLayout) childView.findViewById(R.id.rl_row_container);
            myHolder.tvStatus = (IteeTextView) childView.findViewById(R.id.tv_status);
            myHolder.tvUserName = (IteeTextView) childView.findViewById(R.id.tv_user_name);
            myHolder.tvCheckOut = (IteeTextView) childView.findViewById(R.id.tv_check_out);
            myHolder.tvShoppingCart = (IteeTextView) childView.findViewById(R.id.tv_shopping_cart);

            myHolder.llCommodity = (LinearLayout) childView.findViewById(R.id.ll_commodity);
            myHolder.ivCommodityBoy = (ImageView) childView.findViewById(R.id.iv_commodity_boy);
            myHolder.ivCommodityCar = (ImageView) childView.findViewById(R.id.iv_commodity_car);
            myHolder.ivCommodityClub = (ImageView) childView.findViewById(R.id.iv_commodity_club);
            myHolder.ivCommodityShoes = (ImageView) childView.findViewById(R.id.iv_commodity_shoes);
            myHolder.ivCommodityBag = (ImageView) childView.findViewById(R.id.iv_commodity_bag);
            myHolder.ivCommodityTowel = (ImageView) childView.findViewById(R.id.iv_commodity_towel);
            myHolder.ivCommodityUmbrella = (ImageView) childView.findViewById(R.id.iv_commodity_umbrella);
            myHolder.tvSex = (IteeTextView) childView.findViewById(R.id.tv_sex);
            myHolder.tvField = (IteeTextView) childView.findViewById(R.id.tv_field);

            myHolder.tvField.setTextSize(Constants.FONT_SIZE_SMALLER);
            myHolder.tvField.setGravity(Gravity.CENTER);

            RelativeLayout.LayoutParams tvUserNameLayoutParams
                    = (RelativeLayout.LayoutParams) myHolder.tvUserName.getLayoutParams();
            tvUserNameLayoutParams.width = fragment.getActualWidthOnThisDevice(200);
            tvUserNameLayoutParams.height = fragment.getActualHeightOnThisDevice(70);
            myHolder.tvUserName.setLayoutParams(tvUserNameLayoutParams);

            myHolder.tvCheckOut.setGravity(Gravity.CENTER);
            myHolder.tvShoppingCart.setGravity(Gravity.CENTER);

            myHolder.tvCheckOut.setTextSize(Constants.FONT_SIZE_SMALLER);
            myHolder.tvShoppingCart.setTextSize(Constants.FONT_SIZE_SMALLER);

            myHolder.tvUserName.setGravity(Gravity.CENTER_VERTICAL);

            childView.setTag(myHolder);
        } else {
            myHolder = (ViewHolder) childView.getTag();
            myHolder.setCanSlide("count");
        }

        PositionInformationEntity positionInformation = positionInformationList.get(position);
        myHolder.tvUserName.setText(positionInformation.getMemberName());
        myHolder.tvSex.setText(positionInformation.getMemberType());
        if (positionInformation.getMemberGender() == 1) {
            myHolder.tvSex.setTextColor(fragment.getColor(R.color.common_blue));
        } else if (positionInformation.getMemberGender() == 2) {
            myHolder.tvSex.setTextColor(fragment.getColor(R.color.common_red));
        } else {
            myHolder.tvSex.setTextColor(fragment.getColor(R.color.common_black));
        }

        if (BOOKING_COLOR_RED == positionInformation.getBookingColor()) {
            myHolder.tvStatus.setBackgroundColor(fragment.getColor(R.color.common_red));
        }

        if (BOOKING_COLOR_BLUE == positionInformation.getBookingColor()) {
            myHolder.tvStatus.setBackgroundColor(fragment.getColor(R.color.common_blue));
        }

        final PositionInformationEntity finalPositionInformation = positionInformation;
        myHolder.tvShoppingCart.setOnClickListener(new AppUtils.NoDoubleClickListener(fragment.getActivity()) {

            @Override
            public void noDoubleClick(View v) {
                if (Constants.STR_FLAG_YES.equals(finalPositionInformation.getSameDayFlag())) {
                    boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlSelling, fragment.getActivity());
                    if (hasPermission) {
                        if (!finalPositionInformation.isCheckOutStatus()) {
                            Bundle bundle = new Bundle();
                            bundle.putString(TransKey.SHOPPING_BOOKING_NO, finalPositionInformation.getBookingNo());
                            bundle.putString(TransKey.COMMON_FROM_PAGE, LocationListFragment.class.getName());
                            bundle.putInt(TransKey.SHOPPING_PURCHASE_FLAG, ShoppingPaymentFragment.PURCHASE_FLAG_SHOPPING);
                            fragment.push(ShoppingGoodsListFragment.class, bundle);
                        }
                    } else {
                        AppUtils.showHaveNoPermission(fragment.getActivity());
                    }
                } else {
                    Utils.showShortToast(fragment.getActivity(), R.string.msg_error_this_time_has_past);
                }
            }
        });

        final ViewHolder finalMyHolder = myHolder;
        myHolder.tvCheckOut.setOnClickListener(new AppUtils.NoDoubleClickListener(fragment.getActivity()) {

            @Override
            public void noDoubleClick(View v) {

                boolean hasPermission = AppUtils.getAuth(AppUtils.AuthControl.AuthControlCheckOut, fragment.getActivity());
                if (hasPermission) {
                    if (!finalPositionInformation.isCheckOutStatus()) {
                        getCheckOut(finalPositionInformation, finalMyHolder);
                    }
                } else {
                    AppUtils.showHaveNoPermission(fragment);
                }
            }
        });

        myHolder.llCommodity.setVisibility(View.GONE);

        if (positionInformation.getPayStatus() == 1) {// 已结账（已经打完球了）
//            黑色圆圈F;
            myHolder.tvField.setText(STATUS_F);
            myHolder.tvField.setTextColor(fragment.getColor(R.color.common_black));
            myHolder.tvField.setBackgroundResource(R.drawable.bg_black_ring);
            myHolder.tvCheckOut.setEnabled(false);
            myHolder.tvShoppingCart.setEnabled(false);
            myHolder.tvCheckOut.setBackgroundColor(fragment.getColor(R.color.common_light_gray));
            myHolder.tvShoppingCart.setBackgroundResource(R.drawable.bg_gray_stroke);
            myHolder.tvCheckOut.setBackgroundResource(R.drawable.bg_gray_stroke);
        } else {// 未结账
            if (positionInformation.getCurrentHole() != null && positionInformation.getCurrentHole() != 0) {//正在打球
                if (positionInformation.getCurrentHoleStatus() == 0) {
//                    黑色圆圈洞号；
                    myHolder.tvField.setText(String.valueOf(positionInformation.getCurrentHole()));
                    myHolder.tvField.setTextColor(fragment.getColor(R.color.common_black));
                    myHolder.tvField.setBackgroundResource(R.drawable.bg_black_ring);
                } else if (positionInformation.getCurrentHoleStatus() == 1) {
//                    红色圆圈洞号；
                    myHolder.tvField.setText(String.valueOf(positionInformation.getCurrentHole()));
                    myHolder.tvField.setTextColor(fragment.getColor(R.color.common_white));
                    myHolder.tvField.setBackgroundResource(R.drawable.bg_red_circle_location);
                } else if (positionInformation.getCurrentHoleStatus() == 2) {
//                    黄色圆圈洞号；
                    myHolder.tvField.setText(String.valueOf(positionInformation.getCurrentHole()));
                    myHolder.tvField.setTextColor(fragment.getColor(R.color.common_black));
                    myHolder.tvField.setBackgroundResource(R.drawable.bg_yellow_circle_location);
                }
            } else {// 没在打球
                if (positionInformation.getCheckInStatus() == 0) {// 没开卡
                    myHolder.setCanSlide("can");
                    if (positionInformation.getAppointmentGoods().length() > 0) {// 有预约商品
                        myHolder.tvField.setVisibility(View.GONE);
                        myHolder.llCommodity.setVisibility(View.VISIBLE);
                        myHolder.ivCommodityBoy.setVisibility(View.GONE);
                        myHolder.ivCommodityCar.setVisibility(View.GONE);
                        myHolder.ivCommodityClub.setVisibility(View.GONE);
                        myHolder.ivCommodityShoes.setVisibility(View.GONE);
                        myHolder.ivCommodityBag.setVisibility(View.GONE);
                        myHolder.ivCommodityTowel.setVisibility(View.GONE);
                        myHolder.ivCommodityUmbrella.setVisibility(View.GONE);
                        if (positionInformation.getAppointmentGoods().contains("1")) {
                            myHolder.ivCommodityBoy.setVisibility(View.VISIBLE);
                        }
                        if (positionInformation.getAppointmentGoods().contains("2")) {
                            myHolder.ivCommodityCar.setVisibility(View.VISIBLE);
                        }
                        if (positionInformation.getAppointmentGoods().contains("3")) {
                            myHolder.ivCommodityClub.setVisibility(View.VISIBLE);
                        }
                        if (positionInformation.getAppointmentGoods().contains("4")) {
                            myHolder.ivCommodityShoes.setVisibility(View.VISIBLE);
                        }
                        if (positionInformation.getAppointmentGoods().contains("5")) {
                            myHolder.ivCommodityBag.setVisibility(View.VISIBLE);
                        }
                        if (positionInformation.getAppointmentGoods().contains("6")) {
                            myHolder.ivCommodityUmbrella.setVisibility(View.VISIBLE);
                        }
                        if (positionInformation.getAppointmentGoods().contains("7")) {
                            myHolder.ivCommodityTowel.setVisibility(View.VISIBLE);
                        }
                        if (positionInformation.getAppointmentGoodsStatus() == 0) {
//                            蓝色商品；
                            myHolder.llCommodity.setBackgroundResource(R.drawable.bg_light_blue_location);
                        } else {
//                            绿色商品；
                            myHolder.llCommodity.setBackgroundResource(R.drawable.bg_green_location);
                        }
                    } else {// 没预约商品
                        if (positionInformation.getDepositStatus() == 0) {
//                            蓝色圆圈R；
                            myHolder.tvField.setText(STATUS_R);
                            myHolder.tvField.setTextColor(fragment.getColor(R.color.common_blue));
                            myHolder.tvField.setBackgroundResource(R.drawable.bg_blue_ring);
                        } else {
//                            绿色圆圈R；
                            myHolder.tvField.setText(STATUS_R);
                            myHolder.tvField.setTextColor(fragment.getColor(R.color.bg_green_of_2));
                            myHolder.tvField.setBackgroundResource(R.drawable.bg_green_ring);
                        }
                    }
                } else {// 已开卡
//                    黑色对钩；
                    myHolder.tvField.setText(StringUtils.EMPTY);
                    myHolder.tvField.setBackgroundResource(R.drawable.bg_right_ring);
                }
            }


            if (AppUtils.isAgent(fragment.getActivity()) && !Constants.STR_1.equals(positionInformation.getSelfFlag())) {
                myHolder.tvStatus.setEnabled(false);
                myHolder.tvUserName.setEnabled(false);
                myHolder.llCommodity.setEnabled(false);
                myHolder.ivCommodityBoy.setEnabled(false);
                myHolder.ivCommodityCar.setEnabled(false);
                myHolder.ivCommodityClub.setEnabled(false);
                myHolder.ivCommodityShoes.setEnabled(false);
                myHolder.ivCommodityBag.setEnabled(false);
                myHolder.ivCommodityTowel.setEnabled(false);
                myHolder.ivCommodityUmbrella.setEnabled(false);
                myHolder.tvSex.setEnabled(false);
                myHolder.tvField.setEnabled(false);
                myHolder.tvCheckOut.setEnabled(false);
                myHolder.tvShoppingCart.setEnabled(false);
                myHolder.setCanSlide("can");
            }
        }

        final LinearLayout commodity = myHolder.llCommodity;
        final int p = position;
        commodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commodity.setTag(positionInformationList.get(p).getBookingNo());
                listener.onClick(commodity);
            }
        });
        return childView;
    }

    private void getCheckOut(final PositionInformationEntity finalPositionInformation, final ViewHolder finalMyHolder) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(fragment.getActivity()));
        params.put(ApiKey.BOOKING_NO, finalPositionInformation.getBookingNo());

        HttpManager<JsonCheckOut> hh = new HttpManager<JsonCheckOut>(fragment) {

            @Override
            public void onJsonSuccess(JsonCheckOut jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {
                    //if (IS_PAID_ALL.equals(jo.getCheckStatus())) {
                        //putCheckOut(finalPositionInformation, finalMyHolder);
                    //} else {     //预约界面点结账直接跳入消费界面，不checkout
                        Bundle bundle = new Bundle();
                        bundle.putString(TransKey.SHOPPING_BOOKING_NO, finalPositionInformation.getBookingNo());
                        bundle.putString(TransKey.COMMON_FROM_PAGE, LocationListFragment.class.getName());
                        bundle.putString(TransKey.SAME_DAY_FLAG, finalPositionInformation.getSameDayFlag());
                        //bundle.putInt(TransKey.SHOPPING_PURCHASE_FLAG, ShoppingPaymentFragment.PURCHASE_FLAG_CHECKOUT);
                        bundle.putInt(TransKey.SHOPPING_PURCHASE_FLAG, ShoppingPaymentFragment.PURCHASE_FLAG_SHOPPING);   //改为购物，不直接checkout
                        fragment.push(ShoppingPaymentFragment.class, bundle);
                    //}
                } else {
                    Utils.showShortToast(fragment.getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(fragment.getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(fragment.getActivity(), ApiManager.HttpApi.TeeTimeCheckOutGet, params);
    }


    private void putCheckOut(final PositionInformationEntity finalPositionInformation, final ViewHolder finalMyHolder) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(fragment.getActivity()));
        params.put(ApiKey.BOOKING_NO, finalPositionInformation.getBookingNo());

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(fragment) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(fragment.getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20125_CHECK_OUT_SUCCEEDED) {
                    finalPositionInformation.setCheckOutStatus(true);
                    finalPositionInformation.setCheckInStatus(1);
                    finalMyHolder.tvStatus.setBackgroundColor(fragment.getColor(R.color.common_blue));
                    finalMyHolder.tvCheckOut.setEnabled(false);
                    finalMyHolder.tvShoppingCart.setEnabled(false);
                    finalMyHolder.tvField.setText(STATUS_F);
                    finalMyHolder.tvField.setTextColor(fragment.getColor(R.color.common_black));
                    finalMyHolder.tvField.setBackgroundResource(R.drawable.bg_black_ring);
                } else {
                    finalPositionInformation.setCheckOutStatus(false);
                }
                if (listenerHideRight != null) {
                    listenerHideRight.onClick(null);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(fragment.getActivity(), R.string.msg_common_network_error);
            }
        };
        hh.startPut(fragment.getActivity(), ApiManager.HttpApi.TeeTimeCheckOutPut, params);
    }

    public class ViewHolder {

        RelativeLayout rlRowContainer;

        IteeTextView tvStatus;
        IteeTextView tvUserName;
        IteeTextView tvCheckOut;
        IteeTextView tvShoppingCart;
        LinearLayout llCommodity;
        ImageView ivCommodityBoy;
        ImageView ivCommodityCar;
        ImageView ivCommodityClub;
        ImageView ivCommodityShoes;
        ImageView ivCommodityBag;
        ImageView ivCommodityTowel;
        ImageView ivCommodityUmbrella;

        IteeTextView tvSex;
        IteeTextView tvField;

        private String canSlide;

        public String getCanSlide() {
            return this.canSlide;
        }

        public void setCanSlide(String aa) {
            this.canSlide = aa;
        }

    }
}

