package com.calc;

import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.friketrin.calc.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

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
        //webView.loadUrl("http://www.encs.concordia.ca/~f_oreill/eternity_user_manual_filt.html");
        webView.loadUrl("file:///android_asset/www/eternity_manual.html");
        /*String htmlFilename = "doc/eternity_user_manual_filt.html";
        AssetManager mgr = getBaseContext().getAssets();
        try {
            InputStream in = mgr.open(htmlFilename, AssetManager.ACCESS_BUFFER);
            String htmlContentInStringFormat = StreamToString(in);
            in.close();
            webView.loadDataWithBaseURL("file:///android_assets/doc/", htmlContentInStringFormat, null, "utf-8", null);

        } catch (IOException e) {
            e.printStackTrace();
        }*/
        /*String doc="<iframe src='http://docs.google.com/viewer?url=file:///android_assets/doc/eternity.pdf'"+
        "width='100%' height='100%'style='border: none;'></iframe>";
        WebView wv = webView;
        wv.getSettings().setJavaScriptEnabled(true);
        //wv.getSettings().setPluginsEnabled(true);
        wv.getSettings().setAllowFileAccess(true);
        wv.loadUrl(doc);*/
    }

    public static String StreamToString(InputStream in) throws IOException {
        if(in == null) {
            return "";
        }
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
        }
        return writer.toString();
    }

}
