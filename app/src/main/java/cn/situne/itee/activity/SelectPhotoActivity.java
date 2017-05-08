package cn.situne.itee.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.situne.itee.R;
import cn.situne.itee.common.constant.Constants;
import cn.situne.itee.common.utils.ImageUtils;
import cn.situne.itee.common.utils.Utils;
import cn.situne.itee.entity.IteeSize;
import cn.situne.itee.view.ClipImage.ClipImageLayout;
import cn.situne.itee.view.IteeButton;

public class SelectPhotoActivity extends Activity implements OnClickListener {

    public static final int REQUEST_CODE_SET = 46548;
    public final static int CAMERA_RETURN = 22;
    public final static int FILE_RETURN = 23;
    private IteeButton btnCamera, btnImageFile, btn_cancel;
    private LinearLayout buttonContainer;
    private ClipImageLayout clipImageLayout;
    private RelativeLayout rlContainer;
    private String mPhotoPath;
    private File mPhotoFile;
    private OnClickListener onClickListener;

    private View waitingView;

    private static int getPowerOfTwoForSampleRatio(double ratio) {
        int k = Integer.highestOneBit((int) Math.floor(ratio));
        if (k == 0) {
            return 1;
        } else {
            return k;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rlContainer = (RelativeLayout) findViewById(R.id.rl_container);
        clipImageLayout = new ClipImageLayout(getBaseContext(), null);

        clipImageLayout.setBackgroundColor(Color.WHITE);

        rlContainer.addView(clipImageLayout);

        onClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = clipImageLayout.clip();
                byte[] data = ImageUtils.changeToByteArray(bitmap);


                Utils.log("bitmap.getWidth()) = "+bitmap.getWidth());
                Utils.log("bitmap.getHeight() = "+bitmap.getHeight());
                Utils.log("data.length"+data.length);

                if (bitmap!=null){

                    clipImageLayout.getmZoomImageView().setBackground(null);
                    bitmap.recycle();
                    bitmap = null;
                }

                Intent intent = getIntent();
                intent.putExtra("bitmap", data);
                setResult(RESULT_OK, intent);
                finish();
            }
        };

        RelativeLayout.LayoutParams paramsTvCity = (RelativeLayout.LayoutParams) clipImageLayout.getLayoutParams();
        paramsTvCity.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsTvCity.height = RelativeLayout.LayoutParams.MATCH_PARENT;
        clipImageLayout.setLayoutParams(paramsTvCity);
        clipImageLayout.ok.setOnClickListener(onClickListener);
        clipImageLayout.cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                clipImageLayout.setBitmap(null);
                finish();
            }
        });

        buttonContainer = new LinearLayout(this);
        buttonContainer.setOrientation(LinearLayout.VERTICAL);

        rlContainer.addView(buttonContainer);
        RelativeLayout.LayoutParams paramsBtnContainer = (RelativeLayout.LayoutParams) buttonContainer.getLayoutParams();
        paramsBtnContainer.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsBtnContainer.height = (int) (Utils.getWidth(this) * 0.3f);
        paramsBtnContainer.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        buttonContainer.setLayoutParams(paramsBtnContainer);


        btnCamera = new IteeButton(this);
        btnCamera.setText(getString(R.string.player_camera));
        btnCamera.setId(View.generateViewId());
        btnCamera.setBackgroundColor(getResources().getColor(R.color.common_white));
        btnCamera.setTextColor(getResources().getColor(R.color.common_blue));
        btnCamera.setTextSize(Constants.FONT_SIZE_NORMAL);
        btnImageFile = new IteeButton(this);
        btnImageFile.setText(getString(R.string.player_file));
        btnImageFile.setId(View.generateViewId());
        btnImageFile.setBackgroundColor(getResources().getColor(R.color.common_white));
        btnImageFile.setTextColor(getResources().getColor(R.color.common_blue));
        btnImageFile.setTextSize(Constants.FONT_SIZE_NORMAL);
        buttonContainer.addView(btnCamera);
        buttonContainer.addView(btnImageFile);

        LinearLayout.LayoutParams paramsBtnCamera = (LinearLayout.LayoutParams) btnCamera.getLayoutParams();
        paramsBtnCamera.width = RelativeLayout.LayoutParams.MATCH_PARENT;
        paramsBtnCamera.height = (int) (Utils.getWidth(this) * 0.15f);
        paramsBtnCamera.setMargins(0, 1, 0, 0);
        btnCamera.setLayoutParams(paramsBtnCamera);
        btnImageFile.setLayoutParams(paramsBtnCamera);

        btnCamera.setOnClickListener(this);
        btnImageFile.setOnClickListener(this);
        clipImageLayout.setVisibility(View.GONE);

        rlContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (mPhotoPath != null) {
            CompressTask compressTask = new CompressTask(requestCode, data);
            compressTask.execute();
        }
    }

    public void onClick(View v) {
        mPhotoPath = Environment.getExternalStorageDirectory() + File.separator + getPhotoFileName();
        if (v.getId() == btnCamera.getId()) {

            try {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                mPhotoFile = new File(mPhotoPath);
                if (mPhotoFile.exists()) {
                    mPhotoFile.delete();
                }
                mPhotoFile.createNewFile();
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(mPhotoFile));
                startActivityForResult(intent, CAMERA_RETURN);

            } catch (Exception e) {
                Utils.log(e.getMessage());
            }

        }
        if (v.getId() == btnImageFile.getId()) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, FILE_RETURN);
        }


    }

    /**
     * random picturn name.
     *
     * @return
     */
    private String getPhotoFileName() {
        return "IMG_iTee.jpg";
//        Date date = new Date(System.currentTimeMillis());
//        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd", Locale.getDefault());
//        return dateFormat.format(date) + ".jpg";
    }

    private Bitmap getThumbnail(Uri uri, IteeSize size) throws IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }
        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ?
                onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        int unit = onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth ? size.getHeight() : size.getWidth();

        if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) {
            if (onlyBoundsOptions.outHeight > size.getHeight()) {
                size.setWidth((int) (onlyBoundsOptions.outWidth * 1.0 / onlyBoundsOptions.outHeight * size.getHeight()));
            } else {
                size.setWidth(onlyBoundsOptions.outWidth);
                size.setHeight(onlyBoundsOptions.outHeight);
            }
        } else {
            if (onlyBoundsOptions.outWidth > size.getWidth()) {
                size.setHeight((int) (onlyBoundsOptions.outHeight * 1.0 / onlyBoundsOptions.outWidth * size.getWidth()));
            } else {
                size.setWidth(onlyBoundsOptions.outWidth);
                size.setHeight(onlyBoundsOptions.outHeight);
            }
        }

        double ratio = (originalSize > unit) ? (originalSize / unit) : 1.0;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = getPowerOfTwoForSampleRatio(ratio);
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    private Bitmap getThumbnailFromFile(String path, IteeSize size) throws IOException {
        File file = new File(path);
        InputStream input = new FileInputStream(file);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        if ((onlyBoundsOptions.outWidth == -1) || (onlyBoundsOptions.outHeight == -1)) {
            return null;
        }
        int originalSize = (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) ?
                onlyBoundsOptions.outHeight : onlyBoundsOptions.outWidth;

        int unit = onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth ? size.getHeight() : size.getWidth();

        if (onlyBoundsOptions.outHeight > onlyBoundsOptions.outWidth) {
            if (onlyBoundsOptions.outHeight > size.getHeight()) {
                size.setWidth((int) (onlyBoundsOptions.outWidth * 1.0 / onlyBoundsOptions.outHeight * size.getHeight()));
            } else {
                size.setWidth(onlyBoundsOptions.outWidth);
                size.setHeight(onlyBoundsOptions.outHeight);
            }
        } else {
            if (onlyBoundsOptions.outWidth > size.getWidth()) {
                size.setHeight((int) (onlyBoundsOptions.outHeight * 1.0 / onlyBoundsOptions.outWidth * size.getWidth()));
            } else {
                size.setWidth(onlyBoundsOptions.outWidth);
                size.setHeight(onlyBoundsOptions.outHeight);
            }
        }

        double ratio = (originalSize > unit) ? (originalSize / unit) : 1.0;
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = 10;
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = new FileInputStream(file);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        return bitmap;
    }

    class CompressTask extends AsyncTask<Bitmap, String, Bitmap> {

        private int requestCode;
        private Intent data;

        public CompressTask(int requestCode, Intent data) {
            this.requestCode = requestCode;
            this.data = data;
        }

        @Override
        protected void onPreExecute() {
            buttonContainer.setVisibility(View.GONE);
            LayoutInflater inflater = LayoutInflater.from(SelectPhotoActivity.this);
            waitingView = inflater.inflate(R.layout.view_common_waiting, null);
            rlContainer.addView(waitingView);

            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) waitingView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            waitingView.setLayoutParams(layoutParams);
        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            Bitmap image = null;
            Bitmap imageTemp = null;
            IteeSize size = new IteeSize();
            int hh = getResources().getDisplayMetrics().heightPixels;
            int ww = getResources().getDisplayMetrics().widthPixels;
            size.setWidth(ww);
            size.setHeight(hh);
            int degree = 0;
            if (requestCode == CAMERA_RETURN) {
                try {
                    image = getThumbnailFromFile(mPhotoPath, size);
                } catch (Exception e) {
                    Utils.log(e.getMessage());
                }

            } else {
                if (data.getData() != null) {
                    Uri mImageCaptureUri = data.getData();

                    degree = ImageUtils.getOrientation(SelectPhotoActivity.this, mImageCaptureUri);

                    try {
                        image = getThumbnail(mImageCaptureUri, size);
                    } catch (Exception e) {
                        Utils.log(e.getMessage());
                    }
                }
            }
            imageTemp = ImageUtils.comp(image, size, degree);

            if (image!=null){

                image.recycle();
                image = null;
            }
            return imageTemp;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                clipImageLayout.setVisibility(View.VISIBLE);
                clipImageLayout.setBitmap(bitmap);
            }
            rlContainer.removeView(waitingView);
        }
    }
}