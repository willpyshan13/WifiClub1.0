package cn.situne.itee.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import cn.situne.itee.R;
import cn.situne.itee.common.widget.wheel.BasePopFragment;
import cn.situne.itee.fragment.BaseFragment;

/**
 * Created by luochao on 4/16/15.
 */
public class PurchasePlayersPopup extends BasePopFragment {


    private View.OnClickListener selectListener;

    public View.OnClickListener getSelectListener() {
        return selectListener;
    }

    public void setSelectListener(View.OnClickListener selectListener) {
        this.selectListener = selectListener;
    }


    private Map<String, String> players;

    public Map<String, String> getPlayers() {
        return players;
    }

    public void setPlayers(Map<String, String> players) {
        this.players = players;
    }



    @Override
    public void createContent(LinearLayout mParent) {

        Set bookingNos = players.keySet();
        Iterator<String> it = bookingNos.iterator();
        LinearLayout.LayoutParams bodyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout body = new LinearLayout(baseFragment.getBaseActivity());
        body.setOrientation(LinearLayout.VERTICAL);
        body.setLayoutParams(bodyParams);
        int i = 0;
        while (it.hasNext()) {
            String key = it.next();
            LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getHeight(100));
            LinearLayout row = new LinearLayout(getActivity());
            row.setGravity(Gravity.CENTER);
            row.setLayoutParams(rowParams);
            TextView text = new TextView(getActivity());
            row.addView(text);
            text.setTextColor(baseFragment.getColor(R.color.common_blue));
            text.setText(players.get(key));
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    selectListener.onClick(v);
                }
            });
            row.setTag(key);
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

    public static class Builder extends BasePopFragment.Builder<PurchasePlayersPopup> {

        // private SelectedShopsRentalProductType selectedListener;
        private Map<String, String> players;
        private View.OnClickListener selectListener;

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


        public Builder setPlayers(Map<String, String> players) {
            this.players = players;
            return this;
        }


        public Builder setSelectListener(View.OnClickListener selectListener) {
            this.selectListener = selectListener;
            return this;
        }


        public PurchasePlayersPopup show() {


            PurchasePlayersPopup fragment = (PurchasePlayersPopup) Fragment.instantiate(
                    mBaseFragment.getActivity(), PurchasePlayersPopup.class.getName(), prepareArguments());
            fragment.setDismissedListener(mListener);
            fragment.setBaseFragment(mBaseFragment);
            fragment.setPlayers(this.players);
            fragment.setSelectListener(this.selectListener);

            fragment.show(mFragmentManager, mTag);
            return fragment;
        }
    }
}
