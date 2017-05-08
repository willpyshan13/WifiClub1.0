/**
 * Project Name: itee
 * File Name:  PlayerSignatureEditFragment.java
 * Package Name: cn.situne.itee.fragment.player
 * Date:   2015-03-28
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.fragment.player;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.VolleyError;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.constant.TransKey;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.ImageUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;
import cn.situne.itee.fragment.teetime.TeeTimeCheckInFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;
import cn.situne.itee.manager.jsonentity.JsonBookingDetailList;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.drawLine.PaintView;

/**
 * ClassName:PlayerSignatureEditFragment <br/>
 * Function: member's signature input fragment. <br/>
 * UI:  04-2-2
 * Date: 2015-03-28 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class PlayerSignatureEditFragment extends BaseFragment {


    private RelativeLayout rlContainer;
    private PaintView paintView;
    private IteeTextView cleanButton;
    private String memberId;
    private String url;
    private String fromPage;
    private String bookingNo;
    private String bookingStatus;
    private String nfcCard;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_player_signature;
    }

    @Override
    public int getTitleResourceId() {
        return R.string.player_signature;
    }


    @Override
    protected void initControls(View rootView) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            memberId = String.valueOf(bundle.getInt("memberId"));
            url = bundle.getString("signature", StringUtils.EMPTY);
            bookingNo = bundle.getString("booking_no", StringUtils.EMPTY);
            bookingStatus = bundle.getString("booking_status", StringUtils.EMPTY);
            fromPage = bundle.getString(TransKey.COMMON_FROM_PAGE, StringUtils.EMPTY);
            nfcCard= bundle.getString(TransKey.NFC_CARD,Constants.STR_0);
        }

        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_content_container);
        paintView = new PaintView(getActivity());
        cleanButton = new IteeTextView(getActivity());

    }

    @Override
    protected void setDefaultValueOfControls() {
        cleanButton.setBackground(getResources().getDrawable(R.drawable.textview_corner_red));
        cleanButton.setText(getString(R.string.common_remove));
        cleanButton.setTextColor(getColor(R.color.common_red));
    }

    @Override
    protected void setListenersOfControls() {
        cleanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                paintView.clearImage();
            }
        });
    }

    @Override
    protected void executeOnceOnCreate() {
        netLinkDownLoadImg();
    }

    @Override
    protected void setLayoutOfControls() {

        rlContainer.addView(paintView);

        RelativeLayout.LayoutParams paramsBirth = (RelativeLayout.LayoutParams) paintView.getLayoutParams();
        paramsBirth.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsBirth.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsBirth.addRule(RelativeLayout.CENTER_VERTICAL, LAYOUT_TRUE);
        paintView.setLayoutParams(paramsBirth);
        rlContainer.addView(cleanButton);

        RelativeLayout.LayoutParams paramsBtn = (RelativeLayout.LayoutParams) cleanButton.getLayoutParams();
        paramsBtn.width = (int) (getScreenWidth() * 0.25f);
        paramsBtn.height = (int) (getScreenHeight() * 0.05f);
        paramsBtn.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, LAYOUT_TRUE);
        paramsBtn.setMargins(0, 10, 30, 0);
        cleanButton.setLayoutParams(paramsBtn);
        cleanButton.setGravity(Gravity.CENTER);
        cleanButton.setTextSize(Constants.FONT_SIZE_NORMAL);
    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.player_signature));
        getTvRight().setText(R.string.common_save);
        getTvRight().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netLinkUploadImg();
            }
        });
    }


    public void netLinkTurnOnCard() {
        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(getActivity()));
        params.put(ApiKey.VERIFY_NO, "1");
        params.put(ApiKey.BOOKING_NO, bookingNo);
        params.put(ApiKey.BOOKING_STATUS, bookingStatus);
        params.put(ApiKey.NFC_CARD_NUMBER, nfcCard);
        HttpManager<JsonBookingDetailList> hh = new HttpManager<JsonBookingDetailList>(PlayerSignatureEditFragment.this) {

            @Override
            public void onJsonSuccess(JsonBookingDetailList jo) {
                int returnCode = jo.getReturnCode();
                Utils.log("returnCode :"+returnCode);
                if (returnCode == Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY) {


                    Bundle bundle = new Bundle();
                    bundle.putString("fromFlag", "refresh");

                    if (Constants.STR_0.equals(nfcCard)){
                        AppUtils.saveSellCaddie(getBaseActivity(), false);

                    }else{
                        AppUtils.saveSellCaddie(getBaseActivity(), true);
                    }

                    doBackWithReturnValue(bundle, TeeTimeAddFragment.class);

                    doBack();
                    Utils.showShortToast(getActivity(), jo.getReturnInfo());
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

                Utils.log("");

            }
        };
        hh.start(getActivity(), ApiManager.HttpApi.TurnOnCard, params);

    }


    private void netLinkUploadImg() {
        Bitmap bitmap = paintView.getmBottomBitmap();

        Bitmap bitmapTemp = ImageUtils.imageZoom(bitmap);
        byte[] b = ImageUtils.convertBitmap2Bytes(bitmapTemp);

        HashMap<String, byte[]> files = new HashMap<>();
        files.put("img", b);
        HashMap<String, String> params = new HashMap<>();
        params.put("token", AppUtils.getToken(mContext));
        params.put("member_id", memberId);

        if (fromPage.equals(TeeTimeCheckInFragment.class.getName())) {
            params.put("type", "4");
            params.put("booking_no", bookingNo);
        }

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(PlayerSignatureEditFragment.this) {
            @Override
            public void onJsonSuccess(BaseJsonObject jo) {

                if (fromPage.equals(TeeTimeCheckInFragment.class.getName())) {

                    netLinkTurnOnCard();
                    //doBackWithRefresh(TeeTimeCheckInFragment.class);
                }

                if (fromPage.equals(PlayerFragment.class.getName())) {
                    doBackWithRefresh(PlayerFragment.class);
                }

                if (fromPage.equals(PlayerInfoEditFragment.class.getName())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(TransKey.COMMON_FROM_PAGE, PlayerSignatureEditFragment.class.getName());
                    doBackWithReturnValue(bundle, PlayerInfoEditFragment.class);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.uploadFile(getActivity(), ApiManager.HttpApi.CommonSignature, files, params);

    }

    private void netLinkDownLoadImg() {
        if (Utils.isStringNotNullOrEmpty(url)) {
            AppUtils.DownImageClient downImage = new AppUtils.DownImageClient(url, new AppUtils.DownImageClient.DownLoadedImageClientListener() {
                @Override
                public void onDownLoadedClient(Bitmap bitmap) {
                    if (bitmap != null) {
                        paintView.setBitmap(bitmap);
                    }
                }
            }, getActivity());
            downImage.setRelativeLayout(rlContainer);
            downImage.execute();
        }

    }
}
