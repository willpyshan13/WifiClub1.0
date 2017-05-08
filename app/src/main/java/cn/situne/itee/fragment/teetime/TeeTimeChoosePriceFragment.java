package cn.situne.itee.fragment.teetime;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.widget.wheel.BasePopFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.shopping.ShoppingPaymentFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.manager.jsonentity.JsonChoosePricingTableData;
import cn.situne.itee.manager.jsonentity.JsonNfcCheckCardForBookingNoGet;
import cn.situne.itee.manager.jsonentity.JsonShoppingPaymentGet;
import cn.situne.itee.manager.jsonentity.JsonShoppingPurchaseAddReturn;
import cn.situne.itee.manager.jsonentity.JsonTeeTimePriceTableData;
import cn.situne.itee.view.IteeCheckBox;
import cn.situne.itee.view.IteeIntegerEditText;
import cn.situne.itee.view.IteeNumberEditView;
import cn.situne.itee.view.IteePricingTimesEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.ShoppingPurchaseItem;
import cn.situne.itee.view.SwipeLinearLayout;
import cn.situne.itee.view.popwindow.NfcSelectMemberPopup;
import cn.situne.itee.view.popwindow.SelectYearPopupWindow;

/**
 * Created by luochao on 9/15/15.
 */
public class TeeTimeChoosePriceFragment extends BaseFragment {
    public static final String JUMP_FRAGMENT_6_2 = "1";
    public static final String JUMP_FRAGMENT_CHECK_IN = "2";
    private String jumpFragment;
    // private ListView listView;
    private String bookingNo;
    private String bookingNoDetailed;
    private String fromPage;
    private LinearLayout mBody;
    private ArrayList<PlayerLayout> playerLayoutList;

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_tee_time_choose_price;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        mBody = (LinearLayout) rootView.findViewById(R.id.body);
        checkBoxList = new ArrayList<>();
        playerLayoutList = new ArrayList<>();
        Bundle bundle = getArguments();
        if (bundle != null) {
            bookingNo = bundle.getString(TransKey.SHOPPING_BOOKING_NO, Constants.STR_EMPTY);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE);
            bookingNoDetailed = bundle.getString(TransKey.SHOPPING_DETAIL_BOOKING_NO, Constants.STR_EMPTY);
            jumpFragment = bundle.getString(TransKey.COMMON_JUMP);
        }
        isFirst = true;
        getPriceTable();
    }

    private boolean isFirst;

    @Override
    protected void executeEveryOnCreate() {
        if (isFirst) {
            isFirst = false;
        } else {
            Bundle bundle = new Bundle();

            bundle.putString("fromFlag", "refresh");
            bundle.putString("TeeTimeChoosePriceFragment", Constants.STR_1);
            try {
                doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.executeEveryOnCreate();
    }

    class PlayerLayout extends LinearLayout {

        private ArrayList<PricingLayout> pricingLayoutList;

        private String bookNo;

        public String getBookNo() {
            return bookNo;
        }

        public void setBookNo(String bookNo) {
            this.bookNo = bookNo;
        }

        public PlayerLayout(Context context) {
            super(context);
        }

        public ArrayList<PricingLayout> getPricingLayoutList() {
            return pricingLayoutList;
        }

        public void setPricingLayoutList(ArrayList<PricingLayout> pricingLayoutList) {
            this.pricingLayoutList = pricingLayoutList;
        }
    }

    private void refreshBody(ArrayList<JsonChoosePricingTableData.Player> dataList) {
        mBody.removeAllViews();

        for (JsonChoosePricingTableData.Player player : dataList) {
            if (player.getPricingList() != null && player.getPricingList().size() > 0)
                mBody.addView(getItemView(player));
        }
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

    }

    @Override
    protected void setLayoutOfControls() {

    }

    @Override
    protected void setPropertyOfControls() {

    }

    private boolean doCheck() {
        for (PlayerLayout playerLayout : playerLayoutList) {
            JSONObject jsonObject = new JSONObject();
            for (PricingLayout pp : playerLayout.getPricingLayoutList()) {
                if (pp.getCheckBox().getChecked() && pp.getEdTimes().getVisibility() == View.VISIBLE) {
                    if (Constants.STR_EMPTY.equals(pp.getEdTimes().getText().toString()))
                        return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(R.string.pricing_choose_price);
        getTvRight().setBackground(null);
        getTvRight().setText(R.string.common_ok);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (doCheck()) {
                    pricingTablePost();
                } else {
                    Utils.showShortToast(getBaseActivity(), AppUtils.generateNotNullMessage(getBaseActivity(), R.string.pricing_table_times));
                }
            }
        });


        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {

                Bundle bundle = new Bundle();

                bundle.putString("fromFlag", "refresh");
                bundle.putString("TeeTimeChoosePriceFragment", Constants.STR_1);

                try {
                    doBackWithReturnValue(bundle, (Class<? extends BaseFragment>) Class.forName(fromPage));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

    }


    private void openCards() {

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
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(TeeTimeChoosePriceFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                if (returnCode == Constants.RETURN_CODE_20301) {

                    if (JUMP_FRAGMENT_6_2.equals(jumpFragment)) {
                        Bundle bundle = new Bundle();
                        bundle.putString(TransKey.SHOPPING_BOOKING_NO, bookingNo);

                        bundle.putString("needJump", Constants.STR_1);
                        bundle.putString(TransKey.COMMON_FROM_PAGE, fromPage);
                        push(ShoppingPaymentFragment.class, bundle);
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

        hh.start(getActivity(), ApiManager.HttpApi.xcheckin, params);

    }


    private void pricingTablePost() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        JSONArray jsArray = new JSONArray();
        try {

            for (PlayerLayout playerLayout : playerLayoutList) {
                JSONObject jsonObject = new JSONObject();

                for (PricingLayout pricingLayout : playerLayout.getPricingLayoutList()) {
                    if (pricingLayout.getCheckBox().getChecked()) {
                        jsonObject.put("booking_no", playerLayout.getBookNo());
                        jsonObject.put("pm_id", pricingLayout.getPricingId());
                        jsonObject.put("times", pricingLayout.getEdTimes().getText().toString());
                    }

                }


                jsArray.put(jsonObject);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        params.put(ApiKey.COMMON_DATA_LIST, jsArray.toString());
        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(TeeTimeChoosePriceFragment.this) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {


                    if (JUMP_FRAGMENT_CHECK_IN.equals(jumpFragment)) {
                        Bundle bundle = getArguments();
                        bundle.putString(TransKey.COMMON_FROM_PAGE, TeeTimeChoosePriceFragment.class.getName());
                        push(TeeTimeCheckInFragment.class, bundle);
                    } else {
                        openCards();

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

        hh.start(getActivity(), ApiManager.HttpApi.X0425, params);


    }


    public void netLinkTurnOnCard(String bookingNo) {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        //  params.put(ApiKey.BOOKING_STATUS, String.valueOf(booking.getBookingStatus()));
        HttpManager<JsonBookingDetailList> hh = new HttpManager<JsonBookingDetailList>(TeeTimeChoosePriceFragment.this) {

            @Override
            public void onJsonSuccess(JsonBookingDetailList jo) {
                int returnCode = jo.getReturnCode();

                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {


//                    if (Tee)
//                    Bundle bundle = new Bundle();
//                    if (Constants.STR_0.equals(nfcCard)){
//                        AppUtils.saveSellCaddie(getBaseActivity(), false);
//
//                    }else{
//                        AppUtils.saveSellCaddie(getBaseActivity(), true);
//                    }
//                    bundle.putString("fromFlag", "refresh");
//                    getBaseActivity().doBackWithReturnValue(bundle, TeeTimeAddFragment.class);
//                    Utils.showShortToast(getActivity(), jo.getReturnInfo());
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.TurnOnCard, params);

    }


    class PricingLayout extends LinearLayout {
        private IteeCheckBox checkBox;
        private IteePricingTimesEditText edTimes;
        private String pricingId;

        public String getPricingId() {
            return pricingId;
        }

        public void setPricingId(String pricingId) {
            this.pricingId = pricingId;
        }

        public IteeCheckBox getCheckBox() {
            return checkBox;
        }

        public void setCheckBox(IteeCheckBox checkBox) {
            this.checkBox = checkBox;
        }

        public IteePricingTimesEditText getEdTimes() {
            return edTimes;
        }

        public void setEdTimes(IteePricingTimesEditText edTimes) {
            this.edTimes = edTimes;
        }

        public PricingLayout(Context context) {
            super(context);
        }

        public PricingLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public PricingLayout(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }
    }


    private PlayerLayout getItemView(JsonChoosePricingTableData.Player player) {
        PlayerLayout linearLayout = new PlayerLayout(getBaseActivity());
        linearLayout.addView(getTitle(player));
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBookNo(player.getBookingNo());

        ArrayList<PricingLayout> pricingLayoutList = new ArrayList<>();
        int i = 0;
        for (JsonChoosePricingTableData.Pricing pricing : player.getPricingList()) {
            PricingLayout pricingLayout = getPricingTableView(pricing, player.getBookingNo(), i, player.getUser(), player.getCheckType());
            linearLayout.addView(pricingLayout);
            pricingLayoutList.add(pricingLayout);
            i++;
        }
        linearLayout.setPricingLayoutList(pricingLayoutList);

        playerLayoutList.add(linearLayout);
        return linearLayout;


    }

    private RelativeLayout getTitle(JsonChoosePricingTableData.Player player) {
        RelativeLayout rlTitleLayout = new RelativeLayout(mContext);
        IteeTextView titleName = new IteeTextView(mContext);
        titleName.setTextSize(Constants.FONT_SIZE_MORE_LARGER);
        IteeTextView titleRight1 = new IteeTextView(mContext);
        titleRight1.setId(View.generateViewId());
        IteeTextView titleRight2 = new IteeTextView(mContext);
        RelativeLayout.LayoutParams rlTitleLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        RelativeLayout.LayoutParams titleNameParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(300), RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams titleRight1Params = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(300), getActualHeightOnThisDevice(50));
        titleRight1Params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        RelativeLayout.LayoutParams titleRight2arams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(300), getActualHeightOnThisDevice(50));
        titleRight2arams.addRule(RelativeLayout.BELOW, titleRight1.getId());

        titleRight2arams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlTitleLayout.setLayoutParams(rlTitleLayoutParams);
        titleNameParams.leftMargin = getActualWidthOnThisDevice(40);
        titleName.setLayoutParams(titleNameParams);
        titleRight1.setLayoutParams(titleRight1Params);
        titleRight1Params.rightMargin = getActualWidthOnThisDevice(40);
        titleRight2arams.rightMargin = getActualWidthOnThisDevice(40);
        titleRight2.setLayoutParams(titleRight2arams);
        titleName.setGravity(Gravity.CENTER_VERTICAL);

        titleRight1.setGravity(Gravity.BOTTOM | Gravity.RIGHT);

        titleRight2.setGravity(Gravity.TOP | Gravity.RIGHT);


        rlTitleLayout.addView(titleName);
        rlTitleLayout.addView(titleRight1);
        rlTitleLayout.addView(titleRight2);


        titleName.setText(player.getName());

        titleRight1.setText(player.getMemberName());
        titleRight2.setText(player.getMemberCard());

        AppUtils.addBottomSeparatorLine(rlTitleLayout, getBaseActivity());

        return rlTitleLayout;

    }

    private PricingLayout getPricingTableView(JsonChoosePricingTableData.Pricing pricing, String bookingNo, int position, String user, String checkType) {

        PricingLayout linearLayout = new PricingLayout(getBaseActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPricingId(pricing.getId());
        linearLayout.addView(getPricingTableTitle(pricing, bookingNo, position, linearLayout, checkType));

        for (JsonChoosePricingTableData.PricingData pricingData : pricing.getPricingDataList()) {
            if (Constants.STR_0.equals(pricingData.getPackageId())) {

                linearLayout.addView(getPricingTableProductItem(pricingData, false, user, pricing.getType()));
            } else {

                linearLayout.addView(getPackageLayout(pricingData, user, pricing.getType()));
            }

        }
        return linearLayout;

    }


    private LinearLayout getPackageLayout(JsonChoosePricingTableData.PricingData pricingData, String user, String type) {
        LinearLayout linearLayout = new LinearLayout(getBaseActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(getPricingTableProductItem(pricingData, true, user, type));

        for (JsonChoosePricingTableData.PricingData packagePricingData : pricingData.getPackageProductList()) {
            RelativeLayout packageItem = getPricingTableProductItem(packagePricingData, false, user, type);
            packageItem.setBackgroundColor(getColor(R.color.common_gray));

            linearLayout.addView(packageItem);
        }
        return linearLayout;
    }

    private RelativeLayout getPricingTableProductItem(JsonChoosePricingTableData.PricingData pricingData, boolean isPackage, String user, String type) {

        RelativeLayout rlTitleLayout = new RelativeLayout(mContext);
        IteeTextView titleName = new IteeTextView(mContext);
        titleName.setMaxWidth(getActualWidthOnThisDevice(300));
        titleName.setId(View.generateViewId());
        IteeTextView tvRate = new IteeTextView(mContext);
        tvRate.setId(View.generateViewId());
        IteeTextView titleRight2 = new IteeTextView(mContext);
        RelativeLayout.LayoutParams rlTitleLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        RelativeLayout.LayoutParams titleNameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        RelativeLayout.LayoutParams tvRateParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        tvRateParams.addRule(RelativeLayout.RIGHT_OF, titleName.getId());
        RelativeLayout.LayoutParams titleRight2arams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        titleRight2arams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        rlTitleLayout.setLayoutParams(rlTitleLayoutParams);
        titleNameParams.leftMargin = getActualWidthOnThisDevice(100);
        titleName.setLayoutParams(titleNameParams);
        tvRate.setLayoutParams(tvRateParams);
        titleRight2.setLayoutParams(titleRight2arams);
        titleRight2arams.rightMargin = getActualWidthOnThisDevice(40);
        titleName.setGravity(Gravity.CENTER_VERTICAL);


        tvRate.setTextColor(getColor(R.color.common_red));

        titleRight2.setGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        rlTitleLayout.addView(titleName);
        rlTitleLayout.addView(tvRate);
        rlTitleLayout.addView(titleRight2);
        titleName.setText(pricingData.getProductName());

        int userInt = Integer.parseInt(user);

        if (!Constants.STR_1.equals(type) || userInt <= 0) {
            if (Constants.MONEY_DISCOUNT_MONEY.equals(pricingData.getGuestDiscountType())) {
                tvRate.setText(Constants.STR_DOUBLE_SPACE + AppUtils.getCurrentCurrency(getBaseActivity()) + Constants.STR_SEPARATOR + pricingData.getGuestDiscount());

            } else {
                tvRate.setText(Constants.STR_DOUBLE_SPACE + pricingData.getGuestDiscount() + Constants.STR_SYMBOL_PERCENT + Constants.STR_OFF);

            }
            titleRight2.setText(AppUtils.getCurrentCurrency(getBaseActivity()) + pricingData.getGuestPrice());
        } else {

            if (Constants.MONEY_DISCOUNT_MONEY.equals(pricingData.getMemberDiscountType())) {
                tvRate.setText(Constants.STR_DOUBLE_SPACE + AppUtils.getCurrentCurrency(getBaseActivity()) + Constants.STR_SEPARATOR + pricingData.getMemberDiscount());

            } else {
                tvRate.setText(Constants.STR_DOUBLE_SPACE + pricingData.getMemberDiscount() + Constants.STR_SYMBOL_PERCENT + Constants.STR_OFF);

            }
            titleRight2.setText(AppUtils.getCurrentCurrency(getBaseActivity()) + pricingData.getMemberPrice());
        }


        if (isPackage) {
            tvRate.setVisibility(View.GONE);


        }


        AppUtils.addBottomSeparatorLine(rlTitleLayout, getBaseActivity());

        return rlTitleLayout;

    }

    ArrayList<IteeCheckBox> checkBoxList;


    private void checkCheckBox(IteeCheckBox iteeCheckBox) {

        for (IteeCheckBox c : checkBoxList) {
            if (!c.equals(iteeCheckBox) && c.getTag().equals(iteeCheckBox.getTag())) {


                c.setChecked(false);

            }

        }


    }

    private RelativeLayout getPricingTableTitle(JsonChoosePricingTableData.Pricing pricing, String bookingNo, int position, PricingLayout linearLayout, String checkType) {

        RelativeLayout.LayoutParams llProductLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, getActualHeightOnThisDevice(100));
        RelativeLayout rlProductLayout = new RelativeLayout(mContext);
        rlProductLayout.setLayoutParams(llProductLayoutParams);


        RelativeLayout.LayoutParams productRadioParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        productRadioParams.addRule(RelativeLayout.CENTER_VERTICAL);
        final IteeCheckBox productRadio = new IteeCheckBox(TeeTimeChoosePriceFragment.this, getActualHeightOnThisDevice(50), R.drawable.icon_shops_green_unselected, R.drawable.icon_shop_green_selected);
        productRadio.setLayoutParams(productRadioParams);
        linearLayout.setCheckBox(productRadio);
        checkBoxList.add(productRadio);
        productRadio.setId(View.generateViewId());
        if (position == 0) {
            productRadio.setChecked(true);
        }
        productRadio.setTag(bookingNo);

        productRadio.setmPurchaseItemListener(new ShoppingPaymentFragment.PurchaseItemListener() {
            @Override
            public void clickEditLayoutOk(int editType, int num, ShoppingPurchaseItem.RowItem item) {

            }

            @Override
            public void clickItemCheckBox(boolean checked, ShoppingPurchaseItem.RowItem rowItem) {

                if (checked) {
                    checkCheckBox(productRadio);

                } else {
                    productRadio.setChecked(true);

                }

            }

            @Override
            public void gotoShop(String bookingNo, String playerName) {

            }

            @Override
            public void delProductItem(String id, ShoppingPurchaseItem.RowItem rowItem, ShoppingPurchaseItem player) {

            }

            @Override
            public void gotoChoosePackage(String bookingNo, String playerName) {

            }

            @Override
            public void swipeScroll(SwipeLinearLayout layout) {

            }

            @Override
            public void deleteTimes(String TimesId, String times, ShoppingPurchaseItem.RowItem rowItem, ShoppingPurchaseItem player) {

            }

            @Override
            public void showItem(int i, ShoppingPurchaseItem.EditLayout e) {

            }

            @Override
            public void closeItem() {
            }
        });
        productRadioParams.leftMargin = getActualWidthOnThisDevice(40);


        RelativeLayout.LayoutParams productNameParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(500), LinearLayout.LayoutParams.MATCH_PARENT);
        productNameParams.addRule(RelativeLayout.RIGHT_OF, productRadio.getId());
        IteeTextView productName = new IteeTextView(mContext);
        productName.setTextColor(getColor(R.color.common_blue));
        productName.setLayoutParams(productNameParams);


        RelativeLayout.LayoutParams edTimesParams = new RelativeLayout.LayoutParams(getActualWidthOnThisDevice(150), getActualHeightOnThisDevice(70));
        IteePricingTimesEditText edTimes = new IteePricingTimesEditText(TeeTimeChoosePriceFragment.this);
        edTimesParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        edTimesParams.topMargin = getActualHeightOnThisDevice(15);
        edTimesParams.rightMargin = getActualWidthOnThisDevice(40);
        edTimes.setLayoutParams(edTimesParams);
        edTimes.setText(Constants.STR_1);
        edTimes.setGravity(Gravity.CENTER);
        edTimes.setBackgroundResource(R.drawable.textview_corner);
//        edTimes.setBackgroundColor(getColor(R.color.common_white));


        rlProductLayout.addView(productRadio);
        rlProductLayout.addView(productName);
        rlProductLayout.addView(edTimes);
        rlProductLayout.setGravity(Gravity.CENTER_VERTICAL);

        if (Constants.STR_0.equals(pricing.getTimesAct())) {
            edTimes.setVisibility(View.GONE);

        }

        if ("1".equals(checkType)) {
            edTimes.setVisibility(View.GONE);
            edTimes.setText("0");
        }
        linearLayout.setEdTimes(edTimes);

        productName.setText(getString(R.string.customers_pricing_table));

        AppUtils.addBottomSeparatorLine(rlProductLayout, getBaseActivity());
        return rlProductLayout;
    }

    private ArrayList<JsonChoosePricingTableData.Player> dataList;


    //api
    private ArrayList<String> bookingList;

    private void getPriceTable() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        if (!Constants.STR_EMPTY.equals(bookingNo))
            params.put(ApiKey.BOOKING_ORDER_NO, bookingNo);
        if (!Constants.STR_EMPTY.equals(bookingNoDetailed))
            params.put(ApiKey.SHOPPING_BOOKING_NO, bookingNoDetailed);


        HttpManager<JsonChoosePricingTableData> hh = new HttpManager<JsonChoosePricingTableData>(this) {
            @Override
            public void onJsonSuccess(JsonChoosePricingTableData jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20301) {

                    dataList = jo.getDataList();
                    bookingList = jo.getBookingList();
                    refreshBody(jo.getDataList());

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
        hh.startGet(getActivity(), ApiManager.HttpApi.X0425, params);


    }


}
