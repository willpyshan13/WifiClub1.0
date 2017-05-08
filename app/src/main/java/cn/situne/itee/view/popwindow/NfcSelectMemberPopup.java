package cn.situne.itee.view.popwindow;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.situne.itee.R;
import cn.situne.itee.common.widget.wheel.BasePopFragment;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.quick.WelcomeFragment;
import cn.situne.itee.manager.jsonentity.JsonNfcCheckCardForBookingNoGet;

/**
 * Created by luochao on 9/17/15.
 */
public class NfcSelectMemberPopup extends BasePopFragment {



    private nfcSelectMemberPopupSelectListener selectListener;


    public void setSelectListener(nfcSelectMemberPopupSelectListener selectListener) {
        this.selectListener = selectListener;
    }

    private ArrayList<JsonNfcCheckCardForBookingNoGet.BookingItem> selectMemberDataList;


    public void setSelectMemberDataList(ArrayList<JsonNfcCheckCardForBookingNoGet.BookingItem> selectMemberDataList) {
        this.selectMemberDataList = selectMemberDataList;
    }

    @Override
    public void createContent(LinearLayout mParent) {
        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout body = new LinearLayout(baseFragment.getBaseActivity());
        body.setOrientation(LinearLayout.VERTICAL);
        body.setLayoutParams(bodyParams);
        int i = 0;
        for (JsonNfcCheckCardForBookingNoGet.BookingItem data:selectMemberDataList){
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getHeight(100));
            LinearLayout row = new LinearLayout(getActivity());
            row.setGravity(Gravity.CENTER);
            row.setLayoutParams(rowParams);
            TextView text = new TextView(getActivity());
            row.addView(text);
            text.setTextColor(baseFragment.getColor(R.color.common_blue));
            text.setText(data.getBkdName());
            row.setTag(i);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    int position = (int)v.getTag();
                    selectListener.selectItem(selectMemberDataList.get(position));
                }
            });
            body.addView(row);
            ImageView ivSeparator = new ImageView(baseFragment.getActivity());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(BaseFragment.MATCH_PARENT, baseFragment.getActualHeightOnThisDevice(2));
            ivSeparator.setLayoutParams(layoutParams);
            ivSeparator.setBackgroundColor(baseFragment.getColor(R.color.common_blue));
            body.addView(ivSeparator);
            i++;

        }


        LinearLayout.LayoutParams scrollViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        ScrollView scrollView = new ScrollView(getActivity());
        scrollView.addView(body);

        if (i > 7) {

            scrollViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, baseFragment.getActualHeightOnThisDevice(800));
        }
        scrollView.setLayoutParams(scrollViewParams);


        mParent.addView(scrollView);
    }

    public static Builder createBuilder(BaseFragment mBaseFragment,
                                        FragmentManager fragmentManager) {
        return new Builder(mBaseFragment, fragmentManager);
    }

    public static class Builder extends BasePopFragment.Builder<NfcSelectMemberPopup> {

        private  nfcSelectMemberPopupSelectListener selectListener;

        private ArrayList<JsonNfcCheckCardForBookingNoGet.BookingItem> selectMemberDataList;

        public Builder(BaseFragment mBaseFragment, FragmentManager fragmentManager) {
            super(mBaseFragment, fragmentManager);
            super.setCancelableOnTouchOutside(true);
        }

        @Override
        public Builder setCancelableOnTouchOutside(boolean cancelable) {
            return (Builder) super.setCancelableOnTouchOutside(cancelable);
        }

        @Override
        public Builder setListener(OnDismissedListener listener) {
            return (Builder) super.setListener(listener);
        }


        public Builder setSelectListener(nfcSelectMemberPopupSelectListener selectListener) {
            this.selectListener = selectListener;
            return this;
        }

        public Builder setPlayers(ArrayList<JsonNfcCheckCardForBookingNoGet.BookingItem> selectMemberDataList) {
            this.selectMemberDataList = selectMemberDataList;
            return this;
        }

        public NfcSelectMemberPopup show() {


            NfcSelectMemberPopup fragment = (NfcSelectMemberPopup) Fragment.instantiate(
                    mBaseFragment.getActivity(), NfcSelectMemberPopup.class.getName(), prepareArguments());
            fragment.setDismissedListener(mListener);
            fragment.setBaseFragment(mBaseFragment);
            fragment.setSelectListener(this.selectListener);
            fragment.setSelectMemberDataList(selectMemberDataList);

            fragment.show(mFragmentManager, mTag);
            return fragment;
        }
    }

   public interface nfcSelectMemberPopupSelectListener{

        public void selectItem(JsonNfcCheckCardForBookingNoGet.BookingItem selectItem);
    }
}
