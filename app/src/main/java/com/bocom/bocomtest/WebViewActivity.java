package com.bocom.bocomtest;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by tedyuen on 07/04/2017.
 */

public class WebViewActivity extends AppCompatActivity {

    WebView mWebView;
    ImageView mIvBottom1;
    ImageView mIvBottom2;
    LinearLayout ll_bottom_1;
    LinearLayout ll_bottom_2;
    TextView tv_bottom_1;
    TextView tv_bottom_2;

    private int index=0;
    String url = "http://120.26.64.180:9090/";

    private JsHandler jsHandler;

    public void sendMessage(int index){
        Message msg = new Message();
        //给message对象赋值
        msg.what = index;
        //发送message值给Handler接收
        mHandler.sendMessage(msg);
    }

    protected Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setPage(1);
                    break;
                case 2:
                    setPage(2);
                    break;

            }
        }
    };


    public void setPage(int temp){
        if(index!=temp){
            switch (temp){
                case 1:
                    mIvBottom1.setImageLevel(1);
                    mIvBottom2.setImageLevel(2);
                    tv_bottom_1.setTextColor(getResources().getColor(R.color.bottom_focus));
                    tv_bottom_2.setTextColor(getResources().getColor(R.color.bottom_grey));
                    index=1;
                    break;
                case 2:
                    mIvBottom1.setImageLevel(2);
                    mIvBottom2.setImageLevel(1);
                    tv_bottom_1.setTextColor(getResources().getColor(R.color.bottom_grey));
                    tv_bottom_2.setTextColor(getResources().getColor(R.color.bottom_focus));
                    index=2;
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.wv_view);
        mIvBottom1 = (ImageView) findViewById(R.id.iv_bottom_1);
        mIvBottom2 = (ImageView) findViewById(R.id.iv_bottom_2);
        ll_bottom_1 = (LinearLayout) findViewById(R.id.ll_bottom_1);
        ll_bottom_2 = (LinearLayout) findViewById(R.id.ll_bottom_2);
        tv_bottom_1 = (TextView) findViewById(R.id.tv_bottom_1);
        tv_bottom_2 = (TextView) findViewById(R.id.tv_bottom_2);


        setPage(1);

        setUpViewComponent();

        ll_bottom_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:window.goPageOne();");
                setPage(1);
            }
        });
        ll_bottom_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mWebView.loadUrl("javascript:window.goPageTwo();");
                setPage(2);
            }
        });

    }

    private void setUpViewComponent(){
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setSupportMultipleWindows(true);
        mWebSettings.setLoadsImagesAutomatically(true);

        //启用数据库
        mWebSettings.setDatabaseEnabled(true);
        String dir = getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        mWebSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        mWebSettings.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因



        // h5 localStorge
        mWebView.getSettings().setDomStorageEnabled(true);
        mWebView.getSettings().setAppCacheMaxSize(1024 * 1024 * 8);
        String appCachePath = this.getCacheDir().getAbsolutePath();
        mWebView.getSettings().setAppCachePath(appCachePath);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAppCacheEnabled(true);
        // h5 localStorge

        mWebView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) { //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                if(!StringHelper.isEmpty(script)){
//                    System.out.println("webview:script:==> "+script);
//                    mWebView.loadUrl(script);
//                }
//                mWebView.loadUrl("javascript:$(\".tipbar\").hide();$(\".wrap\").css('padding-bottom', '0');");

            }
        });

//        String url = "http://120.26.64.180:9090/test.html";
//        mWebView.postUrl(url, EncodingUtils.getBytes(param, "base64"));
        jsHandler = JsHandler.getInstance(this);
        mWebView.addJavascriptInterface(jsHandler, "BocomApp");

        mWebView.postUrl(url,null);
//        showCloseWebView(false);
    }


    private void goBack(){
        if(mWebView.canGoBack()){
//            showCloseWebView(true);
            mWebView.goBack();// 返回前一个页面
        }else{
            finish();
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                goBack();
                return true;
            }
        }

        return super.dispatchKeyEvent(event);
    }

}
