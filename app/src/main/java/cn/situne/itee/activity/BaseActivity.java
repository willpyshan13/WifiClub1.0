package cn.situne.itee.activity;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.umeng.analytics.MobclickAgent;

import org.codehaus.jackson.annotate.JsonAutoDetect;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.DensityUtil;
import cn.situne.itee.common.utils.NfcUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.quick.WelcomeFragment;
import cn.situne.itee.fragment.shopping.ShoppingPaymentFragment;
import cn.situne.itee.fragment.teetime.TeeTimeCheckInFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.IteeApplication;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonNfcCheckCardGet;
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;

public abstract class BaseActivity extends ActionBarActivity {

    protected static int LAYOUT_TRUE = RelativeLayout.TRUE;
    protected RequestQueue mQueue;
    private IteeTextView tvRight;
    private IteeTextView tvLeftTitle;

    //nfc
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    //  private NdefMessage mNdefPushMessage;
    protected View.OnClickListener onNfcRead;
    private boolean supportNcf;

    //By ysc
    private ActionBarActivity self;

    /**
     * 初始化layout中的控件
     */
    protected abstract void initControls();

    /**
     * 设置控件的初始值
     */
    protected abstract void setDefaultValueOfControls();

    /**
     * 设置控件的Listener
     */
    protected abstract void setListenersOfControls();

    /**
     * 设置控件的Layout
     */
    protected abstract void setLayoutOfControls();

    /**
     * 设置控件的属性
     */
    protected abstract void setPropertyOfControls();

    /**
     * 设置LayoutId
     *
     * @return LayoutId
     */
    protected abstract int getLayoutId();

    //用于增加NFC卡的对话框  by ysc
    AlertDialog.Builder builder;
    private boolean isAddingNfc = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        //nfc  =========Start================================
        supportNcf = true;
        mAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mAdapter == null) {
            supportNcf = false;
        }
        if (supportNcf) {
            resolveIntent(getIntent());
            mPendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        }
        //nfc  =========End===================================

        mQueue = Volley.newRequestQueue(this);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_action_bar));

            LayoutInflater inflater = LayoutInflater.from(this);

            RelativeLayout rlActionBarMenu = (RelativeLayout) inflater.inflate(R.layout.action_bar, null);

            ImageView ivIcon = new ImageView(this);
            ivIcon.setId(View.generateViewId());
            ivIcon.setBackgroundResource(R.drawable.icon_back);
            ivIcon.setScaleType(ImageView.ScaleType.FIT_XY);
            ivIcon.setOnClickListener(getBackListener());
            rlActionBarMenu.addView(ivIcon);

            RelativeLayout.LayoutParams paramsIvIcon = (RelativeLayout.LayoutParams) ivIcon.getLayoutParams();
//          paramsIvIcon.width = 50;
//          paramsIvIcon.height = 50;
//          paramsIvIcon.leftMargin = getWidth(20);
            paramsIvIcon.leftMargin = getWidth(0);//20160121 modify by zfg
            paramsIvIcon.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsIvIcon.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            ivIcon.setLayoutParams(paramsIvIcon);

            ImageView ivSeparator = new ImageView(this);
            ivSeparator.setId(View.generateViewId());
            ivSeparator.setVisibility(View.INVISIBLE);//20160121 by zfg add
            ivSeparator.setImageResource(R.drawable.icon_separator);
            rlActionBarMenu.addView(ivSeparator);

            RelativeLayout.LayoutParams paramsIvSeparator = (RelativeLayout.LayoutParams) ivSeparator.getLayoutParams();
            paramsIvSeparator.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsIvSeparator.height = 50;
            paramsIvSeparator.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsIvSeparator.addRule(RelativeLayout.RIGHT_OF, ivIcon.getId());
            paramsIvSeparator.leftMargin = 5;
            ivSeparator.setLayoutParams(paramsIvSeparator);

            IteeTextView tvTitle = new IteeTextView(this);
            rlActionBarMenu.addView(tvTitle);

            RelativeLayout.LayoutParams paramsTvTitle = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
            paramsTvTitle.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvTitle.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvTitle.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsTvTitle.addRule(RelativeLayout.RIGHT_OF, ivSeparator.getId());
            paramsTvTitle.leftMargin = 15;
            tvTitle.setLayoutParams(paramsTvTitle);

            tvTitle.setTextSize(Constants.FONT_SIZE_LARGER);
            tvTitle.setTextColor(getColor(R.color.common_white));

            tvRight = new IteeTextView(this);
            rlActionBarMenu.addView(tvRight);

            RelativeLayout.LayoutParams paramsTvOk = (RelativeLayout.LayoutParams) tvRight.getLayoutParams();
            paramsTvOk.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvOk.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
            paramsTvOk.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsTvOk.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
            paramsTvOk.rightMargin = (int) (getWidth() * 0.05);
            tvRight.setLayoutParams(paramsTvOk);

            tvRight.setTextSize(Constants.FONT_SIZE_LARGER);
            tvRight.setTextColor(getResources().getColor(R.color.common_white));

            tvLeftTitle = new IteeTextView(this);
            rlActionBarMenu.addView(tvLeftTitle);

            RelativeLayout.LayoutParams paramsTvLeftTitle = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
            paramsTvLeftTitle.width = (int) (getWidth() * 0.8);
            paramsTvLeftTitle.height = RelativeLayout.LayoutParams.MATCH_PARENT;
            paramsTvLeftTitle.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
            paramsTvLeftTitle.addRule(RelativeLayout.RIGHT_OF, ivSeparator.getId());
            tvLeftTitle.setLayoutParams(paramsTvLeftTitle);

            tvLeftTitle.setTextSize(Constants.FONT_SIZE_LARGER);
            tvLeftTitle.setTextColor(getColor(R.color.common_white));

            actionBar.setCustomView(rlActionBarMenu);
        }

        initControls();
        setPropertyOfControls();
        setDefaultValueOfControls();
        setLayoutOfControls();
        setListenersOfControls();

        self = this;
        IteeApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        IteeApplication.getInstance().finishActivity(this);
    }

    protected int getColor(int colorId) {
        return this.getResources().getColor(colorId);
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度
     */
    protected int getWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度
     */
    protected int getHeight() {
        return getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(float dpValue) {
        return DensityUtil.dip2px(this, dpValue);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.push_in_left, R.anim.push_out_right);
    }

    protected View.OnClickListener getBackListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.popActivity(BaseActivity.this, LoginActivity.class, true);
            }
        };
    }


    public IteeTextView getTvRight() {
        return tvRight;
    }


    public IteeTextView getTvLeftTitle() {
        return tvLeftTitle;
    }

    public int getWidth(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_WIDTH * getWidth());
    }

    public int getHeight(float currentPx) {
        return (int) (currentPx / Constants.DESIGN_UI_HEIGHT * getHeight());
    }

    public Fragment getVisibleFragment() {
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }


    public void changeFragment(Class<? extends BaseFragment> clazz) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> list = fragmentManager.getFragments();
        if (list != null && list.size() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        pushFragment(clazz);

    }

    public void pushFragment(Class<? extends BaseFragment> clazz) {
        pushFragment(clazz, null, clazz.getName());
    }

    public void pushFragment(Class<? extends BaseFragment> clazz, Bundle bundle) {
        pushFragment(clazz, bundle, null);
    }

    public void pushFragment(Class<? extends BaseFragment> clazz, Bundle bundle, String fragmentName) {
        Utils.hideKeyboard(this);

        try {
            FragmentManager fragmentManager = this.getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(
                    R.anim.push_in_right,
                    R.anim.push_out_left,
                    R.anim.push_in_right,
                    R.anim.push_out_left);
            BaseFragment baseFragment = clazz.newInstance();
            if (bundle != null) {
                baseFragment.setArguments(bundle);
            }
            fragmentTransaction.replace(R.id.rl_container, baseFragment, clazz.getName());

            if (Utils.isStringNullOrEmpty(fragmentName)) {
                fragmentName = clazz.getName();
            }
            fragmentTransaction.addToBackStack(fragmentName);
            Utils.log("fragmentName : " + fragmentName);
            fragmentTransaction.commit();
        } catch (InstantiationException e) {
            Utils.log(e.getMessage());
        } catch (IllegalAccessException e) {
            Utils.log(e.getMessage());
        }
    }
    //nfc ================================================================================================================

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        if (supportNcf) {
            if (mAdapter != null) {
                if (!mAdapter.isEnabled()) {
                    //showWirelessSettingsDialog();
                }
                mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
                //  mAdapter.enableForegroundNdefPush(this, mNdefPushMessage);
            }

        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

        if (supportNcf) {
            if (mAdapter != null) {
                mAdapter.disableForegroundDispatch(this);
                mAdapter.disableForegroundNdefPush(this);
            }

        }

    }

    //comment by ysc 手机读到NFC卡时调用此方法
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            //Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            //NdefMessage[] msgs;
            //if (rawMsgs != null) {
            //msgs = new NdefMessage[rawMsgs.length];
            //for (int i = 0; i < rawMsgs.length; i++) {
            //msgs[i] = (NdefMessage) rawMsgs[i];
            //}
            //} else {
            // Unknown tag type
            //byte[] empty = new byte[0];
            //byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
            //Parcelable tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            //byte[] payload = dumpTagData(tag).getBytes();
            String nfcid = readIdFromTag(intent);   //读取NFC卡ID
            Utils.log("nfc card" + nfcid);
            Fragment fragment = getVisibleFragment();
            getNfcCardType(nfcid, fragment);
            //NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
            //NdefMessage msg = new NdefMessage(new NdefRecord[] { record });
            //msgs = new NdefMessage[] { msg };
            //}
            // Setup the views
            //buildTagViews(msgs);
        }
    }

    public String readIdFromTag(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String id = ByteArrayToHexString(tag.getId());
        return id;
    }

    private static String ByteArrayToHexString(byte[] inarray) {
        int i, j, in;
        String[] hex = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F"};
        String out = "";
        for (j = 0; j < inarray.length; ++j) {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }


    private String dumpTagData(Parcelable p) {
//        StringBuilder sb = new StringBuilder();
//        Tag tag = (Tag) p;
//        byte[] id = tag.getId();
//        sb.append("Tag ID (hex): ").append(getHex(id)).append("\n");
//        sb.append("Tag ID (dec): ").append(getDec(id)).append("\n");
//        sb.append("ID (reversed): ").append(getReversed(id)).append("\n");
//
//        String prefix = "android.nfc.tech.";
//        sb.append("Technologies: ");
//        for (String tech : tag.getTechList()) {
//            sb.append(tech.substring(prefix.length()));
//            sb.append(", ");
//        }
//        sb.delete(sb.length() - 2, sb.length());
//        for (String tech : tag.getTechList()) {
//            if (tech.equals(MifareClassic.class.getName())) {
//                sb.append('\n');
//                MifareClassic mifareTag = MifareClassic.get(tag);
//                String type = "Unknown";
//                switch (mifareTag.getType()) {
//                    case MifareClassic.TYPE_CLASSIC:
//                        type = "Classic";
//                        break;
//                    case MifareClassic.TYPE_PLUS:
//                        type = "Plus";
//                        break;
//                    case MifareClassic.TYPE_PRO:
//                        type = "Pro";
//                        break;
//                }
//                sb.append("Mifare Classic type: ");
//                sb.append(type);
//                sb.append('\n');
//
//                sb.append("Mifare size: ");
//                sb.append(mifareTag.getSize() + " bytes");
//                sb.append('\n');
//
//                sb.append("Mifare sectors: ");
//                sb.append(mifareTag.getSectorCount());
//                sb.append('\n');
//
//                sb.append("Mifare blocks: ");
//                sb.append(mifareTag.getBlockCount());
//            }
//
//            if (tech.equals(MifareUltralight.class.getName())) {
//                sb.append('\n');
//                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
//                String type = "Unknown";
//                switch (mifareUlTag.getType()) {
//                    case MifareUltralight.TYPE_ULTRALIGHT:
//                        type = "Ultralight";
//                        break;
//                    case MifareUltralight.TYPE_ULTRALIGHT_C:
//                        type = "Ultralight C";
//                        break;
//                }
//                sb.append("Mifare Ultralight type: ");
//                sb.append(type);
//            }
//        }

//        return sb.toString();
        return "4";
    }


    void buildTagViews(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0) {
            return;
        }
        NdefRecord[] records = msgs[0].getRecords();
        for (final NdefRecord record : records) {
            if (NfcUtils.isText(record)) {
                String cardNo = NfcUtils.parse(record);
                Utils.log("nfc card" + cardNo);
                Fragment fragment = getVisibleFragment();
                getNfcCardType(cardNo, fragment);
            }
        }
    }

    @Override
    public void onNewIntent(Intent intent) {
        setIntent(intent);
        if (supportNcf)
            resolveIntent(intent);
    }

    //nfc api
    protected void getNfcCardType(final String cardNo, final Fragment fragment) {
        if (isAddingNfc) {
            Toast.makeText(BaseActivity.this, "正在增加新卡，请稍候再试！", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(this));
        params.put(ApiKey.NFC_CARD_NUMBER, cardNo);
        HttpManager<JsonNfcCheckCardGet> hh = new HttpManager<JsonNfcCheckCardGet>(this) {
            @Override
            public void onJsonSuccess(JsonNfcCheckCardGet jo) {
                int returnCode = jo.getReturnCode();
                String msg = jo.getReturnInfo();

                Utils.log("=========================================check card===============================================================================");
                Utils.log("cardType : " + jo.getCardType());
                Utils.log("cardMessage : " + jo.getCardMessage());
                Utils.log("bookingNo : " + jo.getCardBindBookingNo());
                Utils.log("returnCode : " + returnCode);
                Utils.log("fragment.getClass().getName() " + fragment.getClass().getName());
                Utils.log("========================================================================================================================");

                if (returnCode != Constants.NFC_RETURN_CODE_20604) {
                    //6-2
                    if (ShoppingPaymentFragment.class.getName().equals(fragment.getClass().getName())) {
                        ShoppingPaymentFragment shoppingPaymentFragment = (ShoppingPaymentFragment) fragment;
                        shoppingPaymentFragment.nfcGetData(jo.getCardBindBookingNo());
                    }
                    //add by ysc 开卡界面绑卡
                    else if (TeeTimeCheckInFragment.class.getName().equals(fragment.getClass().getName())) {
                        TeeTimeCheckInFragment teeTimeCheckIn = (TeeTimeCheckInFragment) fragment;
                        String cardCustomerNo = jo.getCardCode();
                        teeTimeCheckIn.writeNfcCardNo(cardCustomerNo);
                    } //end add
                    else {
                        if (WelcomeFragment.class.getName().equals(fragment.getClass().getName())) {
                            WelcomeFragment welcomeFragment = (WelcomeFragment) fragment;
                            welcomeFragment.setNfcCard(cardNo);

                            if (returnCode == Constants.NFC_RETURN_CODE_20602) { //  no bind card
                                if (Constants.NFC_CARD_TYPE_ONE.equals(jo.getCardType())) {
                                    welcomeFragment.nfcOneCardBind(cardNo, jo.getCardType());
                                }
                                if (Constants.NFC_CARD_TYPE_BAG.equals(jo.getCardType()) || Constants.NFC_CARD_TYPE_BAG_2.equals(jo.getCardType())) {
                                    welcomeFragment.nfcBagCardBind(cardNo, jo.getCardType());
                                }
                                if (Constants.NFC_CARD_TYPE_CADDIE.equals(jo.getCardType())) {
                                    welcomeFragment.nfcCaddieCheck(cardNo, jo.getCardType());
                                }
                            } else if (returnCode == Constants.NFC_RETURN_CODE_20603) {//  bind card
                                if ((Constants.NFC_CARD_TYPE_BAG.equals(jo.getCardType()) || Constants.NFC_CARD_TYPE_BAG_2.equals(jo.getCardType())) && welcomeFragment.isBagUnBind()) {
                                    welcomeFragment.oneBagCardUnbindPost(cardNo, jo.getCardType());
                                } else if (Constants.NFC_CARD_TYPE_CADDIE.equals(jo.getCardType()) && welcomeFragment.isaCaddieUnBind()) {
                                    welcomeFragment.unBindCaddie(cardNo, jo.getCardType());
                                } else if (Constants.NFC_CARD_TYPE_ONE.equals(jo.getCardType()) && welcomeFragment.isaCaddieUnBind()) {
                                    welcomeFragment.unBindCaddie(cardNo, jo.getCardType());
                                } else if (Constants.NFC_CARD_TYPE_ONE.equals(jo.getCardType()) && AppUtils.getIsCaddie(getApplicationContext())) {
                                    welcomeFragment.nfcCaddiePhoneCheckOneCardGet(cardNo, jo.getCardType());
                                } else if (Constants.NFC_CARD_TYPE_CADDIE.equals(jo.getCardType())) {
                                    welcomeFragment.nfcCaddieCheck(cardNo, jo.getCardType());
                                } else {
                                    welcomeFragment.refreshLayout(jo.getCardBindBookingNo());
                                }
                            }
                        } else {
                            if (returnCode != Constants.NFC_RETURN_CODE_20602) {
                                Bundle bundle = new Bundle();
                                if (!Constants.NFC_CARD_TYPE_CADDIE.equals(jo.getCardType())) {
                                    if (Constants.NFC_CARD_TYPE_ONE.equals(jo.getCardType()) && AppUtils.getIsCaddie(getApplicationContext())) {
                                        AppUtils.saveSellCaddie(BaseActivity.this, true);
                                        bundle.putBoolean(TransKey.NFC_NO_SET_SELL_CADDIE, true);
                                    }
                                    bundle.putString(TransKey.BOOKING_ORDER_NO, jo.getCardBindBookingNo());
                                    bundle.putString(TransKey.NFC_CARD, cardNo);
                                    bundle.putString(TransKey.NFC_CARD_TYPE, jo.getCardType());
                                    pushFragment(WelcomeFragment.class, bundle);
                                }
                            } else {
                                Utils.showShortToast(getApplication(), msg);
                            }
                        }
                    }
                } else {
//                    Utils.showShortToast(getApplication(), msg);
                    inputNewCard(cardNo);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                NetworkResponse response = error.networkResponse;
                if (response != null) {
                    Utils.debug(response.toString());
                }
                Utils.showShortToast(getApplication(), R.string.msg_common_network_error);
            }
        };
        hh.startGet(this, ApiManager.HttpApi.NfcCheckCardGet, params);
    }



    protected void inputNewCard(final String cardId) {
        isAddingNfc = true;
        builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        LayoutInflater inflater = getLayoutInflater();
        final View layout = inflater.inflate(R.layout.dialog_new_nfc_card, (ViewGroup) findViewById(R.id.dialog));
        TextView tvCardNo = (TextView) layout.findViewById(R.id.tv_cardId);
        tvCardNo.setText("当前卡号为： " + cardId);
        final TextView txtWarnning = (TextView) layout.findViewById(R.id.txtWarnning);
        final IteeEditText tvCard = (IteeEditText) layout.findViewById(R.id.txtCardCode);
        tvCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (txtWarnning.getVisibility() == View.VISIBLE) {
                    txtWarnning.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        builder.setTitle(R.string.nfc_card_not_found).setIcon(
                android.R.drawable.ic_dialog_info).setView(layout);
        builder.setPositiveButton(R.string.common_confirm, new DialogInterface.OnClickListener() {
                    //LayoutInflater inflater = getLayoutInflater();
                    //View layout = inflater.inflate(R.layout.dialog_new_nfc_card, (ViewGroup) findViewById(R.id.dialog));
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        RadioGroup rg = (RadioGroup) layout.findViewById(R.id.radioGroup);
                        RadioButton rb = (RadioButton) layout.findViewById(rg.getCheckedRadioButtonId());
                        String cardType = rb.getText().toString();
                        if (cardType.equals("一卡通")) {
                            cardType = "1";
                        } else if (cardType.equals("球包卡")) {
                            cardType = "2";
                        } else if (cardType.equals("球童卡")) {
                            cardType = "3";
                        }

                        String cardCode = tvCard.getText().toString().trim();
                        //卡号不能为空验证
                        if (cardCode.isEmpty()) {
                            txtWarnning.setVisibility(tvCard.getVisibility());
                            try {
                                Field field = dialog.getClass().getSuperclass().getDeclaredField(
                                        "mShowing");
                                field.setAccessible(true);
                                //   将mShowing变量设为false，表示对话框已关闭
                                field.set(dialog, false);
                                dialog.dismiss();

                            } catch (Exception e) {

                            }
                        }

                        Map<String, String> params = new HashMap<>();
                        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(self));
                        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(self));
                        params.put(ApiKey.NFC_CARD_NUMBER, cardId);
                        params.put(ApiKey.NFC_CARD_CODE, cardCode);
                        params.put(ApiKey.NFC_CARD_TYPE, cardType);
                        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(self) {
                            @Override
                            public void onJsonSuccess(BaseJsonObject jo) {
                                if (jo != null) {
                                    if (jo.getReturnCode().equals("20401")) {
                                        try {
                                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                                            field.setAccessible(true);
                                            //   将mShowing变量设为false，表示对话框已关闭
                                            field.set(dialog, true);
                                            dialog.dismiss();
//                                            isAddingNfc = false;
                                        } catch (Exception e) {
                                            Toast.makeText(self, "Sys:" + e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
//                                        isAddingNfc = false;

                                    } else {
//                                        isAddingNfc = false;
                                        String errMsg = jo.getReturnInfo();
                                        Toast.makeText(self, errMsg, Toast.LENGTH_LONG).show();
                                    }
                                }
                                isAddingNfc = false;
                            }

                            @Override
                            public void onJsonError(VolleyError error) {
                                isAddingNfc = false;
                            }

                        };
                        hh.start(self, ApiManager.HttpApi.NFCADD, params);
                    }
                }
        );

        builder.setNegativeButton(R.string.common_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(layout.getWindowToken(), 0); //强制隐藏键盘

                            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
                            field.setAccessible(true);
                            //   将mShowing变量设为false，表示对话框已关闭
                            field.set(dialog, true);
                            dialog.dismiss();

                        } catch (Exception e) {
                            Toast.makeText(self, "Sys:" + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        isAddingNfc = false;
                    }
                }
        );
        builder.show();

    }

    private void okClick() {

    }


}
