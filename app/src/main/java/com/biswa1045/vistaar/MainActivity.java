package com.biswa1045.vistaar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
private WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webview = (WebView) findViewById(R.id.webview2);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl("file:///android_asset/privacypolicy.html");
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(false);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(intent);

        finish();

    }
}