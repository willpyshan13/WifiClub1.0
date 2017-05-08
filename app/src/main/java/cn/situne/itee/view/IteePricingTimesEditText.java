package cn.situne.itee.view;

import android.content.Context;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.AppUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;

/**
 * Created by luochao on 10/8/15.
 */
public class IteePricingTimesEditText extends IteeEditText {

    public IteePricingTimesEditText(BaseFragment mFragment) {
        super(mFragment);
        initView(mFragment.getActivity());
    }

    private void initView(Context mContext) {
        setSingleLine();
        setTextColor(mContext.getResources().getColor(R.color.common_black));
        setTextSize(Constants.FONT_SIZE_NORMAL);
        setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        setBackground(null);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                String str = charSequence.toString();
                if (Constants.STR_DOT.equals(str)){
                    setText("0.");
                    setSelection(2);
                }


                Utils.log(str.indexOf(Constants.STR_DOT)+"");


                if (str.indexOf(Constants.STR_DOT)!=-1&&(str.length()-str.indexOf(Constants.STR_DOT))>2){
                    setText(str.substring(0,str.length()-1));
                    setSelection(str.length()-1);
                }






            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        addTextChangedListener(new AppUtils.EditViewIntegerWatcher(this));
//        setOnFocusChangeListener(new AppUtils.AddIntegerFocusListener());
    }
}
