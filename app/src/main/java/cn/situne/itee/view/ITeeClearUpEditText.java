package cn.situne.itee.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.fragment.BaseFragment;
import cn.situne.itee.fragment.teetime.RightDrawableOnTouchListener;

/**
 * Created by luochao on 11/25/15.
 */
public class ITeeClearUpEditText extends IteeEditText {

    BaseFragment mBaseFragment;
    Context mContext;
    Drawable imNameDrawable;
    int iHeight = 124;

    public ITeeClearUpEditText(BaseFragment mFragment) {
        super(mFragment);
    }

    TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (Utils.isStringNotNullOrEmpty(ITeeClearUpEditText.this.getText().toString())) {
                if (imNameDrawable == null) {
                    imNameDrawable = getResources().getDrawable(R.drawable.btn_tel_delete);
                }
                imNameDrawable.setBounds(0, 0, iHeight, iHeight);
                ITeeClearUpEditText.this.setCompoundDrawables(null, null, imNameDrawable, null);
            } else {
                ITeeClearUpEditText.this.setCompoundDrawables(null, null, null, null);
            }
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (Utils.isStringNotNullOrEmpty(ITeeClearUpEditText.this.getText().toString())) {
                if (imNameDrawable == null) {
                    imNameDrawable = getResources().getDrawable(R.drawable.btn_tel_delete);
                }
                imNameDrawable.setBounds(0, 0, iHeight, iHeight);
                ITeeClearUpEditText.this.setCompoundDrawables(null, null, imNameDrawable, null);
                setOnTouchListener(new RightDrawableOnTouchListener(ITeeClearUpEditText.this) {
                    @Override
                    public boolean onDrawableTouch(final MotionEvent event) {


                            ITeeClearUpEditText.this.setText(Constants.STR_EMPTY);

                        return false;
                    }
                });
            } else {
                ITeeClearUpEditText.this.setCompoundDrawables(null, null, null, null);
            }

        }
    };

    public ITeeClearUpEditText(Context context) {
        super(context);
        imNameDrawable = getResources().getDrawable(R.drawable.btn_tel_delete);

        addTextChangedListener(watcher);

    }



    public ITeeClearUpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        imNameDrawable = getResources().getDrawable(R.drawable.btn_tel_delete);

        addTextChangedListener(watcher);
    }

    public void setIconHeight(BaseFragment baseFragment, int h) {
        mBaseFragment = baseFragment;
        iHeight = h - baseFragment.getActualHeightOnThisDevice(10);
        imNameDrawable.setBounds(0, 0, iHeight, iHeight);
        this.setCompoundDrawables(null, null, null, null);
    }
}
