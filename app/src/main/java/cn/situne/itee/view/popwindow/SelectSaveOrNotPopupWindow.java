/**
 * Project Name: itee
 * File Name:  SelectSaveOrNotPopupWindow.java
 * Package Name: cn.situne.itee.common.widget.wheel
 * Date:   2015-01-20
 * Copyright (coffee) 2015, itee@kenes.com.cn All Rights Reserved.
 */
package cn.situne.itee.view.popwindow;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.TeeTimeAddFragment;

/**
 * ClassName:SelectSaveOrNotPopupWindow <br/>
 * Function: save confirm popup window. <br/>
 * Date: 2015-01-20 <br/>
 *
 * @author songyuanbin
 * @version 1.0
 * @see
 */
public class SelectSaveOrNotPopupWindow extends BasePopWindow {

    private View menuView;
    private TextView save;
    private TextView cancel;
    private Integer mPosition, mParentPosition;
    private BaseFragment mFragment;

    /**
     * confirm
     *
     * @param style 1：have change  2：confirm 3:confirm delete 4: select male female
     */
    public SelectSaveOrNotPopupWindow(final BaseFragment mFragment, final TeeTimeAddFragment.OnPopupWindowWheelClickListener itemclick, final int style, Integer parentPosition, Integer position,String upMes,String downMes) {
        super(mFragment.getActivity());
        this.mFragment = mFragment;
        this.mPosition = position;
        this.mParentPosition = parentPosition;
        LayoutInflater inflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.select_save_or_not_popup_window, null);
        save = (TextView) menuView.findViewById(R.id.bt_course_out);
        cancel = (TextView) menuView.findViewById(R.id.bt_course_in);

        if (style == 1) {
            save.setText(mFragment.getString(R.string.msg_save_change));

            cancel.setText(mFragment.getString(R.string.common_cancel));
        } else if (style == 2) {
            save.setText(mFragment.getString(R.string.common_confirm));

            cancel.setText(mFragment.getString(R.string.common_no));
        } else if (style == 3) {

            save.setText(mFragment.getString(R.string.common_delete));
            cancel.setText(mFragment.getString(R.string.common_cancel));
        } else if (style == 4) {

            save.setText(mFragment.getString(R.string.common_male));
            cancel.setText(mFragment.getString(R.string.common_female));
        } else if (style == 5) {

            save.setText(upMes);
            cancel.setText(downMes);
        }






        save.setGravity(Gravity.CENTER);
        cancel.setGravity(Gravity.CENTER);

        save.setTextSize(Constants.FONT_SIZE_LARGER);
        save.setLines(2);
        save.setSingleLine(false);
        cancel.setTextSize(Constants.FONT_SIZE_LARGER);
        LinearLayout.LayoutParams paramsRLMemberName = (LinearLayout.LayoutParams) save.getLayoutParams();

        paramsRLMemberName.width = LinearLayout.LayoutParams.MATCH_PARENT;
        paramsRLMemberName.height = (int) (Utils.getHeight(mFragment.getActivity()) * 0.08f);
        save.setLayoutParams(paramsRLMemberName);
        cancel.setLayoutParams(paramsRLMemberName);


        //设置SelectPicPopupWindow的View
        setContentView(menuView);
        setHideListener(menuView);

        formatViews();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (style == 1) {
                    itemclick.onSaveClick("savechange", StringUtils.EMPTY);
                }
                if (style == 2) {
                    itemclick.onSaveClick(StringUtils.EMPTY, StringUtils.EMPTY);
                }
                if (style == 3) {
                    itemclick.onSaveClick("deleteOrder", String.valueOf(mParentPosition) + "," + String.valueOf(mPosition));
                }
                if (style == 4) {
                    itemclick.onSaveClick(StringUtils.EMPTY, Constants.MALE);
                }


                if (style == 5) {
                    itemclick.onSaveClick(StringUtils.EMPTY, Constants.MALE);
                }


            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (style == 1) {
                    itemclick.onSaveClick("cancel", StringUtils.EMPTY);
                }
                if (style == 4) {
                    itemclick.onSaveClick(StringUtils.EMPTY, Constants.FEMALE);
                }

            }
        });


    }

    public SelectSaveOrNotPopupWindow(final BaseFragment mFragment, final TeeTimeAddFragment.OnPopupWindowWheelClickListener itemclick, final int style, Integer parentPosition, Integer position) {
        super(mFragment.getActivity());
        this.mFragment = mFragment;
        this.mPosition = position;
        this.mParentPosition = parentPosition;
        LayoutInflater inflater = (LayoutInflater) mFragment.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        menuView = inflater.inflate(R.layout.select_save_or_not_popup_window, null);
        save = (TextView) menuView.findViewById(R.id.bt_course_out);
        cancel = (TextView) menuView.findViewById(R.id.bt_course_in);

        if (style == 1) {
            save.setText(mFragment.getString(R.string.msg_save_change));

            cancel.setText(mFragment.getString(R.string.common_cancel));
        } else if (style == 2) {
            save.setText(mFragment.getString(R.string.common_confirm));

            cancel.setText(mFragment.getString(R.string.common_no));
        } else if (style == 3) {

            save.setText(mFragment.getString(R.string.common_delete));
            cancel.setText(mFragment.getString(R.string.common_cancel));
        } else if (style == 4) {

            save.setText(mFragment.getString(R.string.common_male));
            cancel.setText(mFragment.getString(R.string.common_female));
        }






        save.setGravity(Gravity.CENTER);
        cancel.setGravity(Gravity.CENTER);

        save.setTextSize(Constants.FONT_SIZE_LARGER);
        cancel.setTextSize(Constants.FONT_SIZE_LARGER);
        LinearLayout.LayoutParams paramsRLMemberName = (LinearLayout.LayoutParams) save.getLayoutParams();

        paramsRLMemberName.width = LinearLayout.LayoutParams.MATCH_PARENT;
        paramsRLMemberName.height = (int) (Utils.getHeight(mFragment.getActivity()) * 0.08f);
        save.setLayoutParams(paramsRLMemberName);
        cancel.setLayoutParams(paramsRLMemberName);


        //设置SelectPicPopupWindow的View
        setContentView(menuView);
        setHideListener(menuView);

        formatViews();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (style == 1) {
                    itemclick.onSaveClick("savechange", StringUtils.EMPTY);
                }
                if (style == 2) {
                    itemclick.onSaveClick(StringUtils.EMPTY, StringUtils.EMPTY);
                }
                if (style == 3) {
                    itemclick.onSaveClick("deleteOrder", String.valueOf(mParentPosition) + "," + String.valueOf(mPosition));
                }
                if (style == 4) {
                    itemclick.onSaveClick(StringUtils.EMPTY, Constants.MALE);
                }
                dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (style == 1) {
                    itemclick.onSaveClick("cancel", StringUtils.EMPTY);
                }
                if (style == 4) {
                    itemclick.onSaveClick(StringUtils.EMPTY, Constants.FEMALE);
                }
                dismiss();
            }
        });


    }
}
