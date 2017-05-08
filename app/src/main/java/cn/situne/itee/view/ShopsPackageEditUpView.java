package cn.situne.itee.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.fragment.BaseEditFragment;
import cn.situne.itee.fragment.BaseFragment;


public class ShopsPackageEditUpView extends LinearLayout {

    private BaseFragment mFragment;
    private IteeEditText edPackageName;
    private IteeQtyEditText etQty;
    private IteeEditText edCode;
    private CheckSwitchButton unlimitedQtyCSBtn;
    private LinearLayout qtyShadeView;
    private String unlimitedFlag ;
    public Button getCodeBtn() {
        return codeBtn;
    }

    public void setCodeBtn(Button codeBtn) {
        this.codeBtn = codeBtn;
    }

    private Button codeBtn;

    public String getUnlimitedFlag() {
        if (unlimitedQtyCSBtn.isChecked()){
           return Constants.UNLIMITED_FLAG_ON;
        }else{
             return Constants.UNLIMITED_FLAG_OFF;
        }
    }

    public void setUnlimitedFlag(String unlimitedFlag) {
        this.unlimitedFlag = unlimitedFlag;
    }

    public ShopsPackageEditUpView(BaseFragment fragment, OnClickListener chooseProductListener,String unlimitedFlag) {
        super(fragment.getActivity());
        this.mFragment = fragment;
        this.unlimitedFlag = unlimitedFlag;
        initView(mFragment.getActivity(), chooseProductListener);

    }
    public void setFragmentMode(BaseEditFragment.FragmentMode fragmentMode) {

        if (fragmentMode == BaseEditFragment.FragmentMode.FragmentModeBrowse) {
            edPackageName.setEnabled(false);
            etQty.setEnabled(false);
            unlimitedQtyCSBtn.setEnabled(false);

            edCode.setEnabled(false);
        } else {
            edPackageName.setEnabled(true);
            etQty.setEnabled(true);
            unlimitedQtyCSBtn.setEnabled(true);
            edCode.setEnabled(true);

        }
    }

    public String getPackageName() {

        return edPackageName.getText().toString();
    }

    public String getPackageQty() {

        return etQty.getText().toString();
    }

    public String getPackageCode() {

        return edCode.getText().toString();
    }

    public void setViewText(String packageName, String packageCode, int packageQty) {
        edPackageName.setText(packageName);
        etQty.setText(String.valueOf(packageQty));
        edCode.setText(packageCode);
    }

    public void setEdCodeText(String packageCode) {
        edCode.setText(packageCode);

    }



    private void initView(Context context, OnClickListener chooseProductListener) {

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams rl1Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
        RelativeLayout rl1 = new RelativeLayout(context);

        RelativeLayout.LayoutParams tv1KeyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        IteeTextView tv1Key = new IteeTextView(context);
        tv1Key.setText(R.string.shop_setting_package_name);
        rl1.addView(tv1Key);
        rl1.setPadding(mFragment.getActualWidthOnThisDevice(40), 0, mFragment.getActualWidthOnThisDevice(40), 0);
        tv1Key.setLayoutParams(tv1KeyParams);
        rl1.setLayoutParams(rl1Params);
        RelativeLayout.LayoutParams edPackageNameParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        edPackageNameParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        edPackageName = new IteeEditText(mFragment);
        edPackageName.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        edPackageName.setLayoutParams(edPackageNameParams);
        edPackageName.setMinimumWidth(mFragment.getActualWidthOnThisDevice(100));
        edPackageName.setHint(mFragment.getString(R.string.shop_setting_package_name));

        rl1.addView(edPackageName);

        LinearLayout.LayoutParams rl2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
        RelativeLayout rl2 = new RelativeLayout(context);

        RelativeLayout.LayoutParams tv2KeyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        tv2KeyParams.leftMargin = mFragment.getActualWidthOnThisDevice(40);
        IteeTextView tv2Key = new IteeTextView(context);
        tv2Key.setLayoutParams(tv2KeyParams);
        tv2Key.setText(R.string.shop_setting_qty);
        rl2.addView(tv2Key);
        rl2.setLayoutParams(rl2Params);
       // rl2.setPadding(mFragment.getActualWidthOnThisDevice(40), 0, mFragment.getActualWidthOnThisDevice(40), 0);

        RelativeLayout.LayoutParams edQtyParams
                = new RelativeLayout.LayoutParams(mFragment.getActualWidthOnThisDevice(300), RelativeLayout.LayoutParams.MATCH_PARENT);
        edQtyParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        edQtyParams.rightMargin = mFragment.getActualWidthOnThisDevice(40);
        etQty = new IteeQtyEditText(mFragment);
        etQty.setLayoutParams(edQtyParams);
        etQty.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        etQty.setMinimumWidth(mFragment.getActualWidthOnThisDevice(40));
        etQty.setHint(mFragment.getString(R.string.shop_setting_qty));
        rl2.addView(etQty);
        RelativeLayout.LayoutParams qtyShadeViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
        qtyShadeView = new LinearLayout(context);
        qtyShadeView.setAlpha(0.8f);
        qtyShadeView.setLayoutParams(qtyShadeViewParams);
        qtyShadeView.setBackgroundColor(mFragment.getColor(R.color.common_gray));
        qtyShadeView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rl2.addView(qtyShadeView);
        qtyShadeView.setVisibility(View.GONE);
        etQty.setEnabled(true);


        RelativeLayout.LayoutParams tv3KeyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams rl3Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
        RelativeLayout rl3 = new RelativeLayout(context);
        rl3.setLayoutParams(rl3Params);
        IteeTextView tv3Key = new IteeTextView(context);
        tv3Key.setText(R.string.shop_setting_code);
        tv3Key.setLayoutParams(tv3KeyParams);
        tv3Key.setId(View.generateViewId());
        rl3.addView(tv3Key);
        RelativeLayout.LayoutParams btnParams = new RelativeLayout.LayoutParams(mFragment.getActualHeightOnThisDevice(50), mFragment.getActualHeightOnThisDevice(50));
        btnParams.addRule(RelativeLayout.RIGHT_OF, tv3Key.getId());
        btnParams.leftMargin = mFragment.getActualWidthOnThisDevice(10);
        btnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        codeBtn = new Button(context);
        codeBtn.setLayoutParams(btnParams);
        codeBtn.setBackgroundResource(R.drawable.icon_shops_code);
        rl3.addView(codeBtn);
        RelativeLayout.LayoutParams edCodeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        edCodeParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        edCode = new IteeEditText(mFragment);
        edCode.setLayoutParams(edCodeParams);
        edCode.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        edCode.setMinWidth(mFragment.getActualWidthOnThisDevice(150));
        edCode.setHint(mFragment.getString(R.string.shop_setting_code));

        rl3.addView(edCode);
        rl3.setPadding(mFragment.getActualWidthOnThisDevice(40), 0, mFragment.getActualWidthOnThisDevice(40), 0);


        RelativeLayout.LayoutParams tv4KeyParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams rl4Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mFragment.getActualHeightOnThisDevice(100));
        RelativeLayout rl4 = new RelativeLayout(context);
        rl4.setOnClickListener(chooseProductListener);
        rl4.setLayoutParams(rl4Params);


        RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(mFragment.getActualHeightOnThisDevice(45), mFragment.getActualHeightOnThisDevice(45));
        iconParams.rightMargin = mFragment.getActualWidthOnThisDevice(0);
        iconParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        iconParams.addRule(RelativeLayout.CENTER_VERTICAL);

        ImageView icon = new ImageView(context);
        icon.setLayoutParams(iconParams);
        icon.setBackgroundResource(R.drawable.icon_right_arrow);


        IteeTextView tv4Key = new IteeTextView(context);
        tv4Key.setText(R.string.shop_setting_choose_product);
        tv4Key.setLayoutParams(tv4KeyParams);
        tv4Key.setId(View.generateViewId());

        rl4.addView(tv4Key);
        rl4.addView(icon);

        // edCode.setText(data.getCode());

        rl4.setPadding(mFragment.getActualWidthOnThisDevice(40), 0, mFragment.getActualWidthOnThisDevice(40), 0);

        unlimitedQtyCSBtn = new CheckSwitchButton(mFragment);


        if (Constants.UNLIMITED_FLAG_OFF.equals(unlimitedFlag)){

            unlimitedQtyCSBtn.setChecked(false);
            etQty.setEnabled(true);
        }else{
            unlimitedQtyCSBtn.setChecked(true);
            qtyShadeView.setVisibility(View.VISIBLE);
            etQty.setEnabled(false);
        }

        unlimitedQtyCSBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    etQty.setEnabled(false);
                    qtyShadeView.setVisibility(View.VISIBLE);
                }else{

                    qtyShadeView.setVisibility(View.GONE);
                    etQty.setEnabled(true);
                }
            }
        });

        ll.addView(rl1);
        ll.addView(AppUtils.getSeparatorLine(mFragment));


        ll.addView(getLineLayoutTypeNewAddCheckSwitchButton(mFragment.getString(R.string.shop_setting_unlimited_qty), unlimitedQtyCSBtn));
        ll.addView(AppUtils.getSeparatorLine(mFragment));
        ll.addView(rl2);
        ll.addView(AppUtils.getSeparatorLine(mFragment));
        ll.addView(rl3);
        ll.addView(AppUtils.getSeparatorLine(mFragment));
        ll.addView(rl4);
        ll.addView(AppUtils.getSeparatorLine(mFragment));

        this.addView(ll);


        rl1.setBackgroundColor(mFragment.getColor(R.color.common_white));
        rl2.setBackgroundColor(mFragment.getColor(R.color.common_white));
        rl3.setBackgroundColor(mFragment.getColor(R.color.common_white));
//        rl4.setBackgroundColor(mFragment.getColor(R.color.common_white));
        rl4.setBackgroundResource(R.drawable.bg_linear_selector_color_white);

//        ItemLayout item = new ItemLayout();
//        item.setEdPrice(edPrice);
//        item.setEdCode(edCode);
//        item.setData(data);
//
//        itemList.add(item);
//        llBody.addView(ll);

    }
    private RelativeLayout getLineLayoutTypeNewAddCheckSwitchButton(String key, CheckSwitchButton csBtn) {
        RelativeLayout res = new RelativeLayout(mFragment.getBaseActivity());
        res.addView(getKeyTextView(key));

        RelativeLayout.LayoutParams csBtnParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, mFragment.getActualHeightOnThisDevice(100));
        csBtnParams.rightMargin = mFragment.getActualWidthOnThisDevice(40);
        csBtnParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        csBtnParams.addRule(RelativeLayout.CENTER_VERTICAL);
        csBtn.setLayoutParams(csBtnParams);
        res.addView(csBtn);
        res.setBackgroundColor(mFragment.getColor(R.color.common_white));
        return res;
    }
    private IteeTextView getKeyTextView(String key) {
        RelativeLayout.LayoutParams leftTextParams
                = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, mFragment.getActualHeightOnThisDevice(100));
        leftTextParams.leftMargin = mFragment.getActualWidthOnThisDevice(40);
        IteeTextView leftText = new IteeTextView(mFragment.getBaseActivity());
        leftText.setText(key);
        leftText.setLayoutParams(leftTextParams);
        return leftText;
    }

}
