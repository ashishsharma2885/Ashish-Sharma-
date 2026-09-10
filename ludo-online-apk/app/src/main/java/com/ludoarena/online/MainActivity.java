package com.ludoarena.online;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    private static final String START_PAGE = "file:///android_asset/launcher.html";
    private WebView webView;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            webView = new WebView(this);
        } catch (Throwable error) {
            TextView fallback = new TextView(this);
            fallback.setText("Ludo Arena could not start because Android System WebView is unavailable. Please update Android System WebView or Chrome and open the app again.");
            fallback.setTextColor(Color.WHITE);
            fallback.setBackgroundColor(Color.rgb(6, 26, 63));
            fallback.setPadding(32, 64, 32, 32);
            fallback.setTextSize(16f);
            setContentView(fallback);
            return;
        }

        webView.setBackgroundColor(Color.rgb(6, 26, 63));
        webView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        webView.setWebChromeClient(new WebChromeClient());

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setMediaPlaybackRequiresUserGesture(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(false);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return handleUrl(view, request.getUrl());
            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return handleUrl(view, Uri.parse(url));
            }

            private boolean handleUrl(WebView view, Uri uri) {
                String scheme = uri.getScheme();
                if (scheme == null) return true;
                if (scheme.equalsIgnoreCase("https") || scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("file")) {
                    view.loadUrl(uri.toString());
                    return true;
                }
                Toast.makeText(MainActivity.this, "Unsupported link", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && request.isForMainFrame()) {
                    Toast.makeText(MainActivity.this, "Could not connect to the Ludo server. Check the server URL and internet connection.", Toast.LENGTH_LONG).show();
                }
            }
        });

        setContentView(webView);

        if (savedInstanceState != null) {
            webView.restoreState(savedInstanceState);
        } else {
            webView.loadUrl(START_PAGE);
        }
    }

    @Override
    public void onBackPressed() {
        if (webView == null) {
            super.onBackPressed();
            return;
        }
        String current = webView.getUrl();
        if (current != null && !current.startsWith("file:///android_asset/")) {
            webView.loadUrl(START_PAGE);
        } else if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (webView != null) webView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        if (webView != null) webView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.stopLoading();
            webView.loadUrl("about:blank");
            webView.clearHistory();
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }
}
