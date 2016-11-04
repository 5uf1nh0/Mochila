package com.example.sufin.mochila;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by sufin on 12/10/2016.
 */

public class WebAppInterface {
    Context mContext;

            WebAppInterface(Context c) {
            mContext = c;
            }
    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
            Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
            }
}
