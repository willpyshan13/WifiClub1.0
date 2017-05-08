package cn.situne.itee.view;

import android.os.Parcel;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.StyleSpan;

public class ForgetPasswordTipStyleSpan extends StyleSpan {
    public ForgetPasswordTipStyleSpan(int style) {
        super(style);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public int getSpanTypeId() {
        return super.getSpanTypeId();
    }

    @Override
    public int getStyle() {
        return super.getStyle();
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        ds.setFakeBoldText(true);
        super.updateDrawState(ds);
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        paint.setFakeBoldText(true);
        super.updateMeasureState(paint);
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }
}

