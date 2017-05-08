/**
 * Project Name: itee
 * File Name:  SelectPayForConfirmPopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-15
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 *
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;

import java.util.HashMap;
import java.util.Map;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.ApiKey;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.manager.ApiManager;
import cn.situne.itee.manager.HttpManager;
import cn.situne.itee.manager.jsonentity.BaseJsonObject;

import static android.widget.LinearLayout.LayoutParams;

/**
 * ClassName:SelectPayForConfirmPopupWindow <br/>
 * Function: select pay confirm popup window. <br/>
 * Date: 2015-01-20 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectPayForConfirmPopupWindow extends PopupWindow {

    private View menuView;
    private Button save;
    private Button cancel;
    private TextView tvMessage;
    private BaseFragment mBaseFragment;
    private Integer paraAgentId;

    private String rechargeMoney;
    private String rechargeType;

    /**
     * recharge
     *
     * @param mBaseFragment
     * @param num           recharge number.
     * @param type          Pay cash 1,Vouchers 2,Credit card 3,Third-Party payment 4
     * @param paraAgentId
     * @auther syb
     */
    public SelectPayForConfirmPopupWindow(BaseFragment mBaseFragment, String num, String type, Integer paraAgentId) {
        super(mBaseFragment.getActivity());
        this.mBaseFragment = mBaseFragment;
        this.paraAgentId = paraAgentId;
        this.rechargeMoney = num;
        this.rechargeType = type;
        LayoutInflater inflater = (LayoutInflater) mBaseFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.popw_recharge_confirm, null);
        tvMessage = (TextView) menuView.findViewById(R.id.tv_message);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.mBaseFragment.getResources().getString(R.string.recharge_confirm_message));
        stringBuffer.append("<font color='red'>");
        stringBuffer.append(" ");
        stringBuffer.append(num);
        stringBuffer.append(" ");
        stringBuffer.append("</font>");
        stringBuffer.append(this.mBaseFragment.getResources().getString(R.string.recharge_confirm_message_by));
        stringBuffer.append(" ");

        switch (type) {

            case "1":
                stringBuffer.append(mBaseFragment.getString(R.string.recharge_type_cash));
                break;
            case "2":
                stringBuffer.append(mBaseFragment.getString(R.string.recharge_type_vouchers));
                break;
            case "3":
                stringBuffer.append(mBaseFragment.getString(R.string.recharge_type_credit_card));
                break;
            case "4":
                stringBuffer.append(mBaseFragment.getString(R.string.recharge_type_Third_party));
                break;
            default:
                break;
        }

        stringBuffer.append("?");

        tvMessage.setText(Html.fromHtml(stringBuffer.toString()));

        save = (Button) menuView.findViewById(R.id.btn_ok);
        cancel = (Button) menuView.findViewById(R.id.btn_cancel);
        //设置SelectPicPopupWindow的View
        this.setContentView(menuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        //this.setAnimationStyle(R.style.PopupAnimation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框

        menuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = menuView.findViewById(R.id.pop_layout).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                netLinkRecharge();
                dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    private void netLinkRecharge() {

        Map<String, String> params = new HashMap<>();
        params.put(ApiKey.COMMON_TOKEN, AppUtils.getToken(mBaseFragment.getActivity()));
        params.put(ApiKey.COMMON_USER_ID, AppUtils.getCurrentUserId(mBaseFragment.getActivity()));
        params.put(ApiKey.AGENT_RECHARGE_RECHARGE_MONEY, rechargeMoney);
        params.put(ApiKey.AGENT_RECHARGE_RECHARGE_TYPE, rechargeType);
        params.put(ApiKey.AGENT_ID, String.valueOf(paraAgentId));

        HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(mBaseFragment) {

            @Override
            public void onJsonSuccess(BaseJsonObject jo) {
                Integer returnCode = jo.getReturnCode();

                String msg = jo.getReturnInfo();
                if (returnCode == Constants.RETURN_CODE_20112_RECHARGE_SUCCESSFULLY) {
                    Utils.showShortToast(mBaseFragment.getActivity(), msg);
                } else {
                    Utils.showShortToast(mBaseFragment.getActivity(), msg);
                }
            }

            @Override
            public void onJsonError(VolleyError error) {

            }
        };
        hh.start(mBaseFragment.getActivity(), ApiManager.HttpApi.AgentRechargePost, params);
    }
}
