package cn.situne.itee.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.common.widget.wheel.BasePopFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.JsonShoppingAaReturnData;
import cn.situne.itee.manager.jsonentity.JsonShoppingPaymentGet;

/**
 * Created by luochao on 4/18/15.
 */
public class PurchaseAaPopup extends BasePopFragment {


    private AaPopupListener aaPopupListener;

    private LinearLayout row1;
    private LinearLayout row2;
    private LinearLayout row3;

    private BaseFragment mFragment;
    private ArrayList<ShoppingPurchaseItem.RowItem> checkedProductList;

    private ArrayList<ShoppingPurchaseItem> viewItems;

    private ArrayList<Row1Item> row1List;

    private View.OnClickListener selectListener;

    private ArrayList<String> mustBookingNos;

    private double sumMoney;

    private boolean isSamePlayer;

    private String sameWithString;
    private double sameMoney;

    public static Builder createBuilder(BaseFragment mBaseFragment,
                                        FragmentManager fragmentManager) {
        return new Builder(mBaseFragment, fragmentManager);
    }

    public BaseFragment getmFragment() {
        return mFragment;
    }

    public void setmFragment(BaseFragment mFragment) {
        this.mFragment = mFragment;
    }

    public View.OnClickListener getSelectListener() {
        return selectListener;
    }

    public void setSelectListener(View.OnClickListener selectListener) {
        this.selectListener = selectListener;
    }

    public List<ShoppingPurchaseItem.RowItem> getCheckedProductList() {
        return checkedProductList;
    }

    public void setCheckedProductList(ArrayList<ShoppingPurchaseItem.RowItem> checkedProductList) {
        this.checkedProductList = checkedProductList;
    }

    public List<ShoppingPurchaseItem> getViewItems() {
        return viewItems;
    }

    public void setViewItems(ArrayList<ShoppingPurchaseItem> viewItems) {
        this.viewItems = viewItems;
    }

    public AaPopupListener getAaPopupListener() {
        return aaPopupListener;
    }

    public void setAaPopupListener(AaPopupListener aaPopupListener) {
        this.aaPopupListener = aaPopupListener;
    }

    private boolean isExist(String bookingNo) {


        for (String str : mustBookingNos) {

            if (bookingNo.equals(str)) {
                return true;
            }
        }

        return false;
    }

    private boolean isHaveProductOfPlayer(String bookingNo) {

        for (ShoppingPurchaseItem.RowItem productItem : checkedProductList) {

            if (bookingNo.equals(productItem.getBookingNo())) {


                return true;
            }


        }
        return false;
    }

    @Override
    public void createContent(final LinearLayout mParent) {
        row1List = new ArrayList<Row1Item>();
        mustBookingNos = new ArrayList<String>();
        LinearLayout.LayoutParams row1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams row2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams row3Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        row1 = new LinearLayout(getActivity());
        row1.setLayoutParams(row1Params);
        row1.setOrientation(LinearLayout.VERTICAL);


        LinearLayout.LayoutParams row2ScrollViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ScrollView row2ScrollView = new ScrollView(getActivity());
        if (checkedProductList.size() > 5) {

            row2ScrollViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(75 * 5));
        }
        row2ScrollView.setLayoutParams(row2ScrollViewParams);


        LinearLayout.LayoutParams row1ScrollViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ScrollView row1ScrollView = new ScrollView(getActivity());
        if (viewItems.size() > 5) {

            row1ScrollViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(200));
        }
        row1ScrollView.setLayoutParams(row1ScrollViewParams);


        row2 = new LinearLayout(getActivity());
        row2.setLayoutParams(row2Params);
        row2.setOrientation(LinearLayout.VERTICAL);
        sumMoney = 0;

        isSamePlayer = true;

        String checkedBookingNo = checkedProductList.get(0).getBookingNo();
        for (ShoppingPurchaseItem.RowItem productItem : checkedProductList) {

            if (!checkedBookingNo.equals(productItem.getBookingNo())) {

                isSamePlayer = false;
            }

            LinearLayout.LayoutParams showItemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
            RelativeLayout showItem = new RelativeLayout(mFragment.getBaseActivity());
            showItem.setLayoutParams(showItemParams);
            showItem.setBackgroundColor(mFragment.getColor(R.color.common_light_gray));
            RelativeLayout.LayoutParams tvNameParams = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            tvNameParams.leftMargin = mFragment.getActualWidthOnThisDevice(20);
            tvNameParams.addRule(RelativeLayout.CENTER_VERTICAL);
            IteeTextView tvName = new IteeTextView(mFragment.getActivity());

            tvName.setText(productItem.getProData().getProductName());
            tvName.setLayoutParams(tvNameParams);

            RelativeLayout.LayoutParams rightLayoutParams = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(300), LinearLayout.LayoutParams.MATCH_PARENT);
            rightLayoutParams.rightMargin = mFragment.getActualWidthOnThisDevice(20);
            rightLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            LinearLayout rightLayout = new LinearLayout((mFragment.getBaseActivity()));
            rightLayout.setOrientation(LinearLayout.VERTICAL);
            rightLayout.setLayoutParams(rightLayoutParams);
            rightLayout.setGravity(Gravity.END);

            LinearLayout.LayoutParams tvMoneyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            tvMoneyParams.weight = 1;
            TextView tvMoney = new TextView(mFragment.getBaseActivity());
            tvMoney.setLayoutParams(tvMoneyParams);
            tvMoney.setGravity(Gravity.CENTER | Gravity.END);

            tvMoney.setSingleLine(true);
            tvMoney.setSingleLine();
            tvMoney.setEllipsize(TextUtils.TruncateAt.END);


            tvMoney.setText(AppUtils.getCurrentCurrency(mFragment.getActivity()) + productItem.getProData().getDisCountPrice());
            LinearLayout.LayoutParams tvNumParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            tvNumParams.weight = 1;
            TextView tvNum = new TextView(mFragment.getBaseActivity());

            tvNum.setLayoutParams(tvNumParams);
            tvNum.setGravity(Gravity.CENTER | Gravity.END);
            tvNum.setText(Constants.STR_MULTIPLY + productItem.getProData().getQty());

            tvNum.setTextColor(mFragment.getColor(R.color.common_blue));
            rightLayout.addView(tvMoney);
            rightLayout.addView(tvNum);
            showItem.addView(tvName);
            showItem.addView(rightLayout);


            row2.addView(showItem);
            sumMoney += productItem.getProData().getQty() * productItem.getProData().getDisCountPrice();

        }

        double tempMoney = sumMoney;
        double singleMoney = tempMoney / viewItems.size();
        double tempSingleMoney = Double.parseDouble(Utils.get2DigitDecimalStringWithAbandon(singleMoney));


        double lastMoney = sumMoney - tempSingleMoney * (viewItems.size() - 1);
        lastMoney = Double.parseDouble(Utils.get2DigitDecimalString(String.valueOf(lastMoney)));

        int cha = (int) (Double.parseDouble((Utils.get2DigitDecimalString(String.valueOf(lastMoney - tempSingleMoney)))) * 100);
        double[] moneys = new double[viewItems.size()];
        for (int i = 0; i < viewItems.size(); i++) {

            moneys[i] = tempSingleMoney;
            if (i >= (viewItems.size() - cha)) {

                moneys[i] = tempSingleMoney + 0.01;
            }

        }

        int position = 0;
        for (ShoppingPurchaseItem playerItem : viewItems) {
            LinearLayout.LayoutParams row1ItemParams = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, mFragment.getActualHeightOnThisDevice(80));
            Row1Item row1Item = new Row1Item(mFragment.getBaseActivity());
            row1Item.setLayoutParams(row1ItemParams);
            RelativeLayout.LayoutParams playerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            playerParams.addRule(RelativeLayout.CENTER_VERTICAL);
            playerParams.leftMargin = mFragment.getActualWidthOnThisDevice(20);
            IteeCheckBox cbPlayer = new IteeCheckBox(mFragment, mFragment.getActualHeightOnThisDevice(60), R.drawable.blue_check_false, R.drawable.blue_check_true);
            cbPlayer.setLayoutParams(playerParams);
            cbPlayer.setText(playerItem.getPlayerName());
            cbPlayer.setTextColor(mFragment.getColor(R.color.common_blue));
            cbPlayer.setChecked(true);
            if (isHaveProductOfPlayer(playerItem.getBookingNo())) {


                cbPlayer.setEnabled(false);
            }

            row1Item.setBookingNo(playerItem.getBookingNo());

            RelativeLayout.LayoutParams edMoneyParams = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(300), mFragment.getActualHeightOnThisDevice(75));
            edMoneyParams.addRule(RelativeLayout.CENTER_VERTICAL);
            edMoneyParams.rightMargin = mFragment.getActualWidthOnThisDevice(20);
            edMoneyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            IteeEditText edMoney = new IteeEditText(mFragment);
            edMoney.setBackgroundResource(R.drawable.bg_edittext_border);
            edMoney.setGravity(Gravity.END);
            edMoney.setLayoutParams(edMoneyParams);
            edMoney.setSingleLine(true);
            edMoney.setSingleLine();
            edMoney.setEllipsize(TextUtils.TruncateAt.END);
            edMoney.setId(View.generateViewId());
            edMoney.setTextColor(mFragment.getColor(R.color.common_blue));

//            if (position == viewItems.size() - 1) {
//                edMoney.setText(Utils.get2DigitDecimalString(String.valueOf(sumMoney - tempSingleMoney * position)));
//            } else {
            edMoney.setText(Utils.get2DigitDecimalString(String.valueOf(moneys[position])));
            // }


            RelativeLayout.LayoutParams tvCurrentCurrencyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            tvCurrentCurrencyParams.addRule(RelativeLayout.CENTER_VERTICAL);
            tvCurrentCurrencyParams.addRule(RelativeLayout.LEFT_OF, edMoney.getId());
            tvCurrentCurrencyParams.rightMargin = mFragment.getActualWidthOnThisDevice(5);
            IteeTextView tvCurrentCurrency = new IteeTextView(mFragment);
            tvCurrentCurrency.setText(AppUtils.getCurrentCurrency(mFragment.getActivity()));
            tvCurrentCurrency.setLayoutParams(tvCurrentCurrencyParams);
            tvCurrentCurrency.setTextColor(mFragment.getColor(R.color.common_blue));

            row1Item.setCbPlayer(cbPlayer);
            row1Item.setEdMoney(edMoney);
            row1Item.addView(tvCurrentCurrency);
            row1Item.setPlayerItem(playerItem);
            row1List.add(row1Item);
            row1.addView(row1Item);
            position++;
        }


        row3 = new LinearLayout(getActivity());
        row3.setLayoutParams(row3Params);
        row3.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout.LayoutParams btnConfirmParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
        btnConfirmParams.weight = 1;

        LinearLayout.LayoutParams btnCancelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
        btnCancelParams.weight = 1;


        LinearLayout.LayoutParams row3ViewParams = new LinearLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(2), LinearLayout.LayoutParams.MATCH_PARENT);

        View row3View = new View(getActivity());
        row3View.setLayoutParams(row3ViewParams);
        row3View.setBackgroundColor(mFragment.getColor(R.color.common_blue));

        IteeButton btnConfirm = new IteeButton(getActivity());
        btnConfirm.setLayoutParams(btnConfirmParams);
        btnConfirm.setTextColor(mFragment.getColor(R.color.common_blue));
        btnConfirm.setText(mFragment.getString(R.string.common_confirm));
        btnConfirm.setBackgroundColor(mFragment.getColor(R.color.common_white));
        IteeButton btnCancel = new IteeButton(getActivity());
        btnCancel.setBackgroundColor(mFragment.getColor(R.color.common_white));
        btnCancel.setTextColor(mFragment.getColor(R.color.common_blue));
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (maybeAa()) {
                    v.setEnabled(false);
                    purchaseAAPost();

                } else {

                    Utils.showShortToast(PurchaseAaPopup.this.getActivity(), mFragment.getString(R.string.shopping_aa_err_mes));
                }

            }
        });

        btnCancel.setText(mFragment.getString(R.string.common_cancel));
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnCancel.setLayoutParams(btnCancelParams);
        row3.addView(btnConfirm);
        row3.addView(row3View);
        row3.addView(btnCancel);

        row1ScrollView.addView(row1);
        mParent.addView(row1ScrollView);

        row2ScrollView.addView(row2);
        mParent.addView(row2ScrollView);

        ImageView ivSeparator = new ImageView(mFragment.getBaseActivity());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(BaseFragment.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(2));
        ivSeparator.setLayoutParams(layoutParams);
        ivSeparator.setBackgroundColor(mFragment.getColor(R.color.common_blue));

        mParent.addView(ivSeparator);
        mParent.addView(row3);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private String getAAListJsonString() {
        JSONArray aaJsArray = new JSONArray();
        try {
            for (Row1Item checkRowItem : row1List) {
                if (checkRowItem.getCbPlayer().getChecked()) {
                    ShoppingPurchaseItem item = checkRowItem.getPlayerItem();
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put(ApiKey.SHOPPING_PURCHASE_BOOKING_NO, item.getBookingNo());
                    jsonObject.put(ApiKey.SHOPPING_PURCHASE_AA_PRICE, String.valueOf(checkRowItem.getEdMoney().getText().toString()));
                    jsonObject.put(ApiKey.SHOPPING_PURCHASE_AA_WITH, getAaWith(checkRowItem.getBookingNo()));
                    jsonObject.put(ApiKey.SHOPPING_PURCHASE_ID, getAaId());

                    jsonObject.put(ApiKey.SHOPPING_PURCHASE_PLAYER, item.getPlayerName());

                    jsonObject.put(ApiKey.SHOPPING_PURCHASE_OWN_PRODUCT, getOwnProducts(checkRowItem.getPlayerItem().getBookingNo()));
                    jsonObject.put(ApiKey.SHOPPING_PURCHASE_OTHERS_PRODUCT, getOthersProducts(checkRowItem.getPlayerItem().getBookingNo()));
                    aaJsArray.put(jsonObject);
                }


            }
        } catch (JSONException e) {
            Utils.log(e.getMessage());
        }
        return aaJsArray.toString();
    }

    private String getAaWith(String bookingNo) {


        StringBuffer sb = new StringBuffer();
//        sb.append(mFragment.getString(R.string.shopping_aa_with));

        sb.append(Constants.STR_SPACE);
        for (Row1Item row1Item : row1List) {

            if (row1Item.getCbPlayer().getChecked() && !bookingNo.equals(row1Item.getBookingNo())) {
                sb.append(row1Item.getPlayerItem().getPlayerName());
                sb.append(Constants.STR_COMMA);
            }

        }


        String res = sb.toString();
        if (res.length() > 0) {

            res = res.substring(0, res.length() - 1);
        }

        if (isSamePlayer) {
            if (bookingNo.equals(checkedProductList.get(0).getBookingNo())) {

                sameWithString = res;
            }

        }

        return res;
    }

    private String getAaId() {

        StringBuffer sb = new StringBuffer();

        for (ShoppingPurchaseItem.RowItem item : checkedProductList) {
            sb.append(String.valueOf(item.getProData().getId()));

            sb.append(Constants.STR_COMMA);
        }
        String res = sb.toString();
        if (res.length() > 0) {

            res = res.substring(0, res.length() - 1);
        }

        return res;
    }


    private JSONArray getOwnProducts(String bookingNo) {
        JSONArray res = new JSONArray();
        for (ShoppingPurchaseItem.RowItem item : checkedProductList) {
            if (bookingNo.equals(item.getBookingNo())) {
                Map<String, String> map = new HashMap<String, String>();
                map.put(ApiKey.SHOPPING_PURCHASE_PD_ID, item.getProData().getProductId());
                map.put(ApiKey.SHOPPING_PURCHASE_QTY, String.valueOf(item.getProData().getQty()));
                map.put(ApiKey.SHOPPING_PURCHASE_PRA_ID, item.getProData().getAttriId());
                res.put(new JSONObject(map));
            }

        }

        return res;


    }

    private JSONArray getOthersProducts(String bookingNo) {
        JSONArray res = new JSONArray();
        for (ShoppingPurchaseItem.RowItem item : checkedProductList) {
            if (!bookingNo.equals(item.getBookingNo())) {
                Map<String, String> map = new HashMap<String, String>();
                map.put(ApiKey.SHOPPING_PURCHASE_PD_ID, item.getProData().getProductId());
                map.put(ApiKey.SHOPPING_PURCHASE_QTY, String.valueOf(item.getProData().getQty()));
                map.put(ApiKey.SHOPPING_PURCHASE_PRA_ID, item.getProData().getAttriId());
                map.put(ApiKey.SHOPPING_PURCHASE_PRICE, String.valueOf(item.getProData().getDisCountPrice()));
                res.put(new JSONObject(map));

            }
        }
        return res;


    }

    private boolean maybeAa() {
        double putSumMoney = 0;
        int size = 0;
        boolean isZ = false;
        for (Row1Item item : row1List) {
            if (item.getCbPlayer().getChecked()) {
                double money = 0;
                try {
                    if (item.getEdMoney().getText().toString().length() > 0) {
                        money = Double.parseDouble(item.getEdMoney().getText().toString());
                        if (money == 0) {
                            isZ = true;
                        }
                    }
                } catch (NumberFormatException e) {


                }
                putSumMoney += money;
                size++;

                if (isSamePlayer) {
                    if (checkedProductList.get(0).getBookingNo().equals(item.getBookingNo())) {

                        sameMoney = money;
                    }


                }

            }
        }

        return !(sumMoney != putSumMoney || size < 2 || isZ);
    }

    private void purchaseAAPost() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));

        params.put(ApiKey.SHOPPING_PURCHASE_AA_LIST, getAAListJsonString());

        HttpManager<JsonShoppingAaReturnData> hh = new HttpManager<JsonShoppingAaReturnData>(mFragment) {

            @Override
            public void onJsonSuccess(JsonShoppingAaReturnData jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();
                Utils.showShortToast(getActivity(), msg);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY || returnCode == Constants.RETURN_CODE_20401_ADD_SUCCESSFULLY) {


                    aaPopupListener.postDone(checkedProductList, jo.getDataList(), sameMoney, sameWithString);
                    dismiss();
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

        hh.start(getActivity(), ApiManager.HttpApi.PurchaseAaPost, params);

    }

    public interface AaPopupListener {


        void postDone(ArrayList<ShoppingPurchaseItem.RowItem> checkedProductList, ArrayList<JsonShoppingPaymentGet.DataItem> returnAaList, double sameMoney, String sameWithString);
    }

    public static class Builder extends BasePopFragment.Builder<PurchasePlayersPopup> {

        private ArrayList<ShoppingPurchaseItem.RowItem> checkedProductList;

        private ArrayList<ShoppingPurchaseItem> viewItems;
        private View.OnClickListener selectListener;
        private AaPopupListener aaPopupListener;


        public Builder(BaseFragment mBaseFragment, FragmentManager fragmentManager) {
            super(mBaseFragment, fragmentManager);
            super.setCancelableOnTouchOutside(true);
        }

        public Builder setCheckedProductList(ArrayList<ShoppingPurchaseItem.RowItem> checkedProductList) {
            this.checkedProductList = checkedProductList;
            return this;
        }

        public Builder setAaPopupListener(AaPopupListener aaPopupListener) {
            this.aaPopupListener = aaPopupListener;
            return this;
        }

        public Builder setViewItems(ArrayList<ShoppingPurchaseItem> viewItems) {
            this.viewItems = viewItems;
            return this;
        }

        @Override
        public Builder setCancelableOnTouchOutside(boolean cancelable) {
            return (Builder) super.setCancelableOnTouchOutside(cancelable);
        }


        @Override
        public Builder setListener(OnDismissedListener listener) {
            return (Builder) super.setListener(listener);
        }

        public Builder setSelectListener(View.OnClickListener selectListener) {
            this.selectListener = selectListener;
            return this;
        }

        public PurchaseAaPopup show() {
            PurchaseAaPopup fragment = (PurchaseAaPopup) Fragment.instantiate(
                    mBaseFragment.getActivity(), PurchaseAaPopup.class.getName(), prepareArguments());
            fragment.setDismissedListener(mListener);
            fragment.setBaseFragment(mBaseFragment);
            fragment.setCheckedProductList(this.checkedProductList);
            fragment.setViewItems(this.viewItems);
            fragment.setSelectListener(selectListener);
            fragment.setmFragment(mBaseFragment);
            fragment.setAaPopupListener(this.aaPopupListener);
            fragment.show(mFragmentManager, mTag);
            return fragment;
        }
    }

    class Row1Item extends RelativeLayout {

        private IteeCheckBox cbPlayer;
        private IteeEditText edMoney;

        private String bookingNo;
        private ShoppingPurchaseItem playerItem;

        public Row1Item(Context context) {
            super(context);
        }

        public String getBookingNo() {
            return bookingNo;
        }

        public void setBookingNo(String bookingNo) {
            this.bookingNo = bookingNo;
        }

        public ShoppingPurchaseItem getPlayerItem() {
            return playerItem;
        }

        public void setPlayerItem(ShoppingPurchaseItem playerItem) {
            this.playerItem = playerItem;
        }

        public IteeCheckBox getCbPlayer() {
            return cbPlayer;
        }

        public void setCbPlayer(IteeCheckBox cbPlayer) {
            this.cbPlayer = cbPlayer;
            this.addView(cbPlayer);
        }

        public IteeEditText getEdMoney() {
            return edMoney;
        }

        public void setEdMoney(IteeEditText edMoney) {
            this.edMoney = edMoney;
            this.addView(edMoney);
        }
    }

}
