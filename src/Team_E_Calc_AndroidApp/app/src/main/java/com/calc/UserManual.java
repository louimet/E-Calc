/**
 * Written by Team-E for COMP 5541, calculator project
 * Winter 2016
 *
 * This class handles the secondary view of the user manual which is stored as an html file in
 * the assets folder
 */
package com.calc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class UserManual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__manual);
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        WebSettings settings = webView.getSettings();

        settings.setDomStorageEnabled(true);
        webView.loadUrl("file:///android_asset/www/eternity_manual.html");
    }
}
