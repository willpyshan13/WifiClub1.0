package cn.situne.itee.fragment.teetime;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import cn.situne.itee.view.IteeEditText;
import cn.situne.itee.view.IteeTextView;
import cn.situne.itee.view.popwindow.SelectOutOrInPopupWindow;

/**
 * Created by xuyue on 2015/12/09.
 */
public class CourseIndexFragment extends BaseFragment {

    private RelativeLayout rlCourseIndex;
    private IteeTextView etIndexKey;
    private IteeEditText etIndexValue;

    private SelectOutOrInPopupWindow selectOutOrInPopupWindow;

    private AppUtils.NoDoubleClickListener noDoubleClickListener;

    private String courseAreaId;
    private String holeNO;
    private String holePar;
    private String sign;
    private String index;
    @Override
    protected int getFragmentId() {
        return R.layout.fragment_course_index;
    }

    @Override
    public int getTitleResourceId() {
        return TITLE_NONE;
    }

    @Override
    protected void initControls(View rootView) {
        selectOutOrInPopupWindow = new SelectOutOrInPopupWindow(getActivity(), null);
        rlCourseIndex = (RelativeLayout) rootView.findViewById(R.id.rl_course_index);

        etIndexKey = new IteeTextView(this);
        etIndexValue = new IteeEditText(this);
    }

    @Override
    protected void setDefaultValueOfControls() {

    }

    @Override
    protected void setListenersOfControls() {
        noDoubleClickListener = new AppUtils.NoDoubleClickListener(getActivity()) {
            @Override
            public void noDoubleClick(View v) {
                Utils.hideKeyboard(getActivity());
                putCourseHole();
            }
        };
    }

    @Override
    protected void setLayoutOfControls() {
        LinearLayout.LayoutParams paramsIndex = (LinearLayout.LayoutParams) rlCourseIndex.getLayoutParams();
        paramsIndex.height = getActualHeightOnThisDevice(100);
        rlCourseIndex.setLayoutParams(paramsIndex);

        rlCourseIndex.addView(etIndexKey);
        RelativeLayout.LayoutParams paramsTvHoleNoKey = (RelativeLayout.LayoutParams) etIndexKey.getLayoutParams();
        paramsTvHoleNoKey.width = RelativeLayout.LayoutParams.WRAP_CONTENT;
        paramsTvHoleNoKey.height = MATCH_PARENT;
        paramsTvHoleNoKey.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvHoleNoKey.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        paramsTvHoleNoKey.leftMargin = getActualWidthOnThisDevice(40);
        etIndexKey.setLayoutParams(paramsTvHoleNoKey);

        rlCourseIndex.addView(etIndexValue);
        RelativeLayout.LayoutParams paramsTvHoleNoValue = (RelativeLayout.LayoutParams) etIndexValue.getLayoutParams();
        paramsTvHoleNoValue.width = getActualWidthOnThisDevice(360);
        paramsTvHoleNoValue.height = MATCH_PARENT;
        paramsTvHoleNoValue.addRule(RelativeLayout.CENTER_VERTICAL);
        paramsTvHoleNoValue.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
        paramsTvHoleNoValue.rightMargin = getActualWidthOnThisDevice(40);
        etIndexValue.setLayoutParams(paramsTvHoleNoValue);
    }

    @Override
    protected void setPropertyOfControls() {
        courseAreaId = (String) getArguments().get("courseAreaId");
        holeNO = (String) getArguments().get("HoleNO");
        holePar = (String) getArguments().get("holePar");
        sign = (String) getArguments().get("sign");
        index = (String) getArguments().get("index");

        etIndexKey.setText(R.string.add_index);
        etIndexKey.setTextColor(getColor(R.color.common_black));
        etIndexKey.setTextSize(Constants.FONT_SIZE_NORMAL);
        etIndexKey.setBackground(null);
        etIndexKey.setEnabled(false);
        etIndexValue.setHint("Index");

        etIndexValue.setTextSize(Constants.FONT_SIZE_NORMAL);
        etIndexValue.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
        etIndexValue.setBackground(null);
        etIndexValue.setSingleLine();
        etIndexValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        etIndexValue.setPadding(0, 0, 20, 0);
        etIndexValue.setText(index);
    }

    @Override
    protected void configActionBar() {
        setStackedActionBar();
        getTvLeftTitle().setText(getString(R.string.add_yards));
        getTvRight().setText(R.string.common_save);
        getTvRight().setOnClickListener(noDoubleClickListener);
        setOnBackListener(new OnPopBackListener() {
            @Override
            public boolean preDoBack() {
                if(etIndexValue.getText().toString().equals(index)){
                    setOnBackListener(null);
                    getBaseActivity().doBackWithRefresh();
                }else{
                    selectOutOrInPopupWindow.showAtLocation(getRootView().findViewById(R.id.popup_courseWindow),
                            Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    backSave();
                }
                return false;
            }
        });
    }

    @Override
    protected void executeOnceOnCreate() {
        super.executeOnceOnCreate();
    }

    public void backSave() {
        selectOutOrInPopupWindow.btFirstValue.setText(R.string.msg_save_change);
        selectOutOrInPopupWindow.btSecondValue.setText(R.string.common_cancel);
        selectOutOrInPopupWindow.btThirdValue.setVisibility(View.GONE);
        selectOutOrInPopupWindow.btFirstValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOutOrInPopupWindow.dismiss();
                setOnBackListener(null);
                putCourseHole();
            }
        });
        selectOutOrInPopupWindow.btSecondValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectOutOrInPopupWindow.dismiss();
                setOnBackListener(null);
                getBaseActivity().doBackWithRefresh();
            }
        });
    }

    private void putCourseHole() {
        String myIndex = etIndexValue.getText().toString();
        int i = 0;
        try {
            i = Integer.parseInt(myIndex);
        }catch (NumberFormatException e){
            i = 0;

        }
        if(i > 0 && i<=18){
            Map<String, String> params = new HashMap<>();
            params.put(ApiKey.COURSE_TOKEN, AppUtils.getToken(getActivity()));
            params.put(ApiKey.COURSE_HOLE_NO, holeNO);
            params.put(ApiKey.COURSE_AREA_ID, courseAreaId);
            params.put(ApiKey.COURSE_INDEX, myIndex);

            HttpManager<BaseJsonObject> hh = new HttpManager<BaseJsonObject>(CourseIndexFragment.this) {

                @Override
                public void onJsonSuccess(BaseJsonObject jo) {
                    Integer returnCode = jo.getReturnCode();
                    String msg = jo.getReturnInfo();
                    if (returnCode.equals(Constants.RETURN_CODE_20201_MODIFY_SUCCESSFULLY)) {
                        doBackWithRefresh();
                    } else {
                        Utils.showShortToast(getActivity(), msg);
                    }
                }

                @Override
                public void onJsonError(VolleyError error) {

                }
            };
            hh.start(getActivity(), ApiManager.HttpApi.EditdoIndex, params);
        }else{
            Utils.showShortToast(getActivity(), getString(R.string.index_enter));
        }
    }
}

