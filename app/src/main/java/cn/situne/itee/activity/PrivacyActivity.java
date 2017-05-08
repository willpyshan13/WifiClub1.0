package cn.situne.itee.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

import cn.situne.itee.R;

public class PrivacyActivity extends BaseActivity{

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_privacy);
//    }
WebView webView;

    protected  void initControls(){
webView = (WebView)findViewById(R.id.webView);
        try {
            webView.loadUrl("file:///android_asset/privacy.html");
        }catch (Exception ex)
        {

        }
    }

    protected void setDefaultValueOfControls(){
        getTvLeftTitle().setText("服务协议和隐私政策");
    }

    protected  void setListenersOfControls(){

    }

    protected  void setLayoutOfControls(){

    }

    protected void setPropertyOfControls(){

    }

    protected  int getLayoutId(){
            return R.layout.activity_privacy;
    }

}
