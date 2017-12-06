package com.rayhahah.androidartstudy.module.webview;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.rayhahah.androidartstudy.R;

import java.lang.ref.WeakReference;


public class WebViewActivity extends AppCompatActivity {

    private FrameLayout mFrameLayout;
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mFrameLayout = ((FrameLayout) findViewById(R.id.fl_web));
        //使用Activity的弱引用来防止内存泄漏
        WeakReference<Context> webContext = new WeakReference<Context>(this);
        mWebView = new WebView(webContext.get());
        mFrameLayout.addView(mWebView);
        WebSettings webSettings = mWebView.getSettings();


        //设置硬件加速
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        设置webview的滚动条
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
//        设置编码格式
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        //同时支持http和https协议
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//支持通过JS打开新窗口
        webSettings.setSupportZoom(true);//设置是否支持缩放
        webSettings.setBuiltInZoomControls(true);//设置是否显示缩放工具
        webSettings.setDisplayZoomControls(false);//设定缩放控件隐藏
        webSettings.setLoadWithOverviewMode(true);// loads the WebView completely zoomed out
        webSettings.setUseWideViewPort(true); //makes the Webview have a normal viewport (such as a normal desktop browser), while when false the webview will have a viewport constrained to its own dimensions (so if the webview is 50px*50px the viewport will be the same size)
        webSettings.setAllowFileAccess(true); // 允许访问文件
        webSettings.setDefaultFontSize(18);//设置默认的字体大小，默认为16，有效值区间在1-72之间。

        // 设置与Js交互的权限
        webSettings.setJavaScriptEnabled(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
        mWebView.loadUrl("file:///android_asset/demo.html");
//        mWebView.loadUrl("http://114.55.110.201:9191/kitweb/chunkupload/upload_example.html");
//        mWebView.loadUrl("http://rayhahah.s1.natapp.cc/");

        mWebView.setWebViewClient(new WebViewClient() {
            /**
             * WebViewClient 主要帮助WebView处理各种通知、请求事件
             */

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }
        });

        mWebView.addJavascriptInterface(new AndroidToJs(), "androidObj");
        // 由于设置了弹窗检验调用结果,所以需要支持js对话框
        // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
        // 通过设置WebChromeClient对象处理JavaScript的对话框
        //设置响应js 的Alert()函数
        mWebView.setWebChromeClient(new WebChromeClient() {
            /**
             * WebChromeClient 主要帮助处理Javascript对话框、网站图标、网站title、加载进度等
             */

            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.e("lzh", consoleMessage.message());
                return true;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                return super.onJsConfirm(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }

            @Override
            public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                return super.onJsBeforeUnload(view, url, message, result);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
                AlertDialog.Builder b = new AlertDialog.Builder(WebViewActivity.this);
                b.setTitle("Alert");
                b.setMessage(message);
                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        result.confirm();
                    }
                });
                b.setCancelable(false);
                b.create().show();
                return true;
            }

            /**
             * 可以比WebViewClient.onPageFinished精准，请使用这个来代替
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //如此才能真正清除，不会导致内存泄漏
        //W
        mFrameLayout.removeView(mWebView);
        mWebView.removeAllViews();
        mWebView.destroy();
    }

    public void clickTellH5(View view) {
        // 必须另开线程进行JS方法调用(否则无法调用)
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                String id = "5";
                String createBy = "6";
                String liveName = "7";

                // 注意调用的JS方法名要对应上
                // 调用javascript的callJS()方法
                mWebView.loadUrl("javascript:upload_block.uploadParamsFromMobile('"
                        + id + "','"
                        + createBy + "','"
                        + liveName + "')");

            }
        });
    }

    public void clicScheme(View view) {
        // 必须另开线程进行JS方法调用(否则无法调用)
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                // 注意调用的JS方法名要对应上
                // 调用javascript的callJS()方法
                mWebView.loadUrl("javascript:schemeNext()");
            }
        });

    }
}
