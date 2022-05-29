package com.iotait.schoolapp.ui.homepage.ui.detailsnews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.ads.AdRequest;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ActivityDetailsNewsWebViewBinding;

public class DetailsNewsWebView extends AppCompatActivity {

    private ActivityDetailsNewsWebViewBinding webViewBinding;
    private String link;
    private String TAG = "DETAILS_NEWS_WEB_VIEW";

    private AdRequest adRequest;
    private String premium_type="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webViewBinding= DataBindingUtil.setContentView(this, R.layout.activity_details_news_web_view);
        adRequest = new AdRequest.Builder().build();
        if (!TextUtils.isEmpty(getIntent().getStringExtra("link"))) {
            link = getIntent().getStringExtra("link");
        }
        webViewBinding.spinKit.setVisibility(View.VISIBLE);
        webViewBinding.includeToolbar.toolbar.setTitle("");
        setSupportActionBar(webViewBinding.includeToolbar.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        webViewBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        WebSettings webSettings = webViewBinding.webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webViewBinding.webview.setWebViewClient(new myWebClient());
        webViewBinding.webview.setWebChromeClient(new WebChromeClient());
        webViewBinding.webview.setVerticalScrollBarEnabled(true);
        webViewBinding.webview.setHorizontalScrollBarEnabled(true);


        //improve webView performance
        webViewBinding.webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webViewBinding.webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webViewBinding.webview.getSettings().setAppCacheEnabled(true);
        webViewBinding.webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            webViewBinding.webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            webViewBinding.webview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webViewBinding.webview, true);
        } else {
            CookieManager.getInstance().setAcceptCookie(true);
        }
        loadNews(link);
        webViewBinding.webview.setDownloadListener(new DownloadListener() {
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {

                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }});
    }

    private void loadNews(final String link) {
        webViewBinding.webview.loadUrl(link);
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            webViewBinding.spinKit.setVisibility(View.VISIBLE);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            webViewBinding.spinKit.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            try {
                if (url != null && url.startsWith("whatsapp://")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.whatsapp");
                    startActivity(sendIntent);
                    return true;
                } else if (url != null && url.startsWith("fb://")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.facebook");
                    startActivity(sendIntent);
                    return true;
                } else if (url != null && url.startsWith("twitter://")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.twitter");
                    startActivity(sendIntent);
                    return true;
                } else if (url != null && url.startsWith("https://m.facebook.com/dialog/send?")) {

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, link);
                    sendIntent.setType("text/plain");
                    sendIntent.setPackage("com.facebook.orca");
                    startActivity(sendIntent);
                    return true;


                } else if (url != null && url.startsWith("sms")) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"));
                    sendIntent.putExtra("sms_body", link);
                    startActivity(sendIntent);

                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }


        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            webView.loadUrl("file:///android_asset/error.html");
//            if (!ConnectionChecker.isConnected(DetailsNewsWebView.this)) {
//                showInternetDialog();
//            }
        }

    }


    @Override
    public void onBackPressed() {
        if (webViewBinding.webview.isFocused() && webViewBinding.webview.canGoBack()) {
            webViewBinding.webview.goBack();
        } else
            super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
