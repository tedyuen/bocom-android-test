package com.bocom.bocomtest;

import android.content.Context;
import android.webkit.JavascriptInterface;

/**
 * Created by tedyuen on 07/04/2017.
 */

public class JsHandler {

    private WebViewActivity mContext;

    private JsHandler(WebViewActivity context){
        this.mContext = context;
    }

    public static JsHandler getInstance(WebViewActivity context) {
        return new JsHandler(context);
    }


    @JavascriptInterface
    public void setPage(int index){
        mContext.sendMessage(index);
    }

}
