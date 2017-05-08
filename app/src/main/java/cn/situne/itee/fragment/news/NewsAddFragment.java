package cn.situne.itee.fragment.news;


import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import cn.situne.itee.R;
import cn.situne.itee.entity.IncomingCall;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.view.IteeButton;

public class NewsAddFragment extends BaseFragment {


    private RelativeLayout rlContainer;
    private IteeButton btnAdd;
    private EditText et_content;

    private List<IncomingCall> dataSource = new ArrayList<>();

    @Override
    protected int getFragmentId() {
        return R.layout.fragment_news_show_item;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        rlContainer = (RelativeLayout) rootView.findViewById(R.id.rl_news_body_container);
        et_content = new EditText(getActivity());
        btnAdd = new IteeButton(getActivity());

    }


    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {

    }


    @Override
    protected void setLayoutOfControls() {


        rlContainer.addView(et_content);

        RelativeLayout.LayoutParams paramsConten = (RelativeLayout.LayoutParams) et_content.getLayoutParams();
        paramsConten.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsConten.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsConten.addRule(RelativeLayout.CENTER_HORIZONTAL, LAYOUT_TRUE);
        paramsConten.addRule(RelativeLayout.BELOW, et_content.getId());
        paramsConten.setMargins(px2dp(40), px2dp(30), 0, 0);
        et_content.setLayoutParams(paramsConten);
        et_content.setId(View.generateViewId());
        et_content.setHint(getResources().getString(R.string.news_add_say_something));
        et_content.setBackgroundColor(getResources().getColor(R.color.common_white));

        rlContainer.addView(btnAdd);

        RelativeLayout.LayoutParams paramsIncomingTitle = (RelativeLayout.LayoutParams) btnAdd.getLayoutParams();
        paramsIncomingTitle.width = (int) (getScreenWidth() * 0.1388f);
        paramsIncomingTitle.height = (int) (getScreenWidth() * 0.1388f);
        paramsIncomingTitle.setMargins(px2dp(40), px2dp(65), 0, 0);
        paramsIncomingTitle.addRule(RelativeLayout.ALIGN_PARENT_LEFT, LAYOUT_TRUE);
        paramsIncomingTitle.addRule(RelativeLayout.BELOW, et_content.getId());
        btnAdd.setLayoutParams(paramsIncomingTitle);

        btnAdd.setBackground(getResources().getDrawable(R.drawable.btn_news_add));

    }

    @Override
    protected void setPropertyOfControls() {

    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        setTitle(getResources().getString(R.string.news_add));
        getTvRight().setVisibility(View.VISIBLE);
        getTvRight().setText(getResources().getString(R.string.common_ok));

    }
}
