package cn.situne.itee.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.manager.jsonentity.JsonCommonProduct;

/**
 * ClassName:PricingTablePackageItemBase <br/>
 * Function: PricingTablePackageItemBase. <br/>
 * Date: 2015-07-21 <br/>
 *
 * @author luochao
 * @version 1.0
 * @see
 */

public class PricingTablePackageItemBase extends LinearLayout{
    public PricingTablePackageItemBase(Context context) {
        super(context);
        mContext = context;
    }



    public JsonCommonProduct getViewData() {
        return viewData;
    }

    public void setViewData(JsonCommonProduct viewData) {
        this.viewData = viewData;
    }

    private JsonCommonProduct viewData;

    private Context mContext;


    public String getMoneyTypeTextString(TextView edReduceRate) {
        String str = edReduceRate.getText().toString();
        if (str.contains(AppUtils.getCurrentCurrency(mContext))) {
            str = str.substring(1, str.length());
        }
        return str;
    }


    public String getSwBtnDiscountType(CheckSwitchButton btn) {
        if (btn.isChecked()) {
            return Constants.MONEY_DISCOUNT_PERCENT;
        }
        return Constants.MONEY_DISCOUNT_MONEY;
    }

    public String getNowCost(String discountType,String originalCost,String editText) {
        double originalCostD = doubleToString(originalCost);
        double discountD = doubleToString(editText);
        if (Constants.MONEY_DISCOUNT_MONEY.equals(discountType)){
            return Utils.get2DigitDecimalString(String.valueOf(originalCostD - discountD));
        }else{
            return Utils.get2DigitDecimalString(String.valueOf(originalCostD - (originalCostD * discountD / 100)));
        }
    }





    public void setMoneyTypeText(TextView v, String value) {
        v.setText(AppUtils.getCurrentCurrency(mContext)  + Constants.STR_SPACE + Utils.get2DigitDecimalString(value));
    }



    public void setPackageSwMaxValue(PricingTablePackageItem.RowLayout row) {

        if (Constants.MONEY_DISCOUNT_PERCENT.equals(getSwBtnDiscountType(row.getEditViewSwCurrency()))){
            row.getEditTextWatcher().setMaxValue((double) Constants.MAX_PERCENT_VALUE);

        }else{

            String originalCost = row.getPackageItem().getPrice();
            double maxValue = 0;
            try {
                maxValue = Double.parseDouble(originalCost);
            } catch (NumberFormatException e) {
                Utils.log(e.getMessage());
            }
            row.getEditTextWatcher().setMaxValue(maxValue);
        }
    }


    public  double doubleToString(String value){
        double res ;
        try {
            res = Double.parseDouble( value);
        }catch (NumberFormatException e){
            res = 0;
        }
        return res;
    }
}
