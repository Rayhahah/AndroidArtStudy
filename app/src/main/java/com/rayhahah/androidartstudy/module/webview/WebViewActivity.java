package com.rayhahah.androidartstudy.module.webview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.rayhahah.androidartstudy.R;

import java.io.File;
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


        //设置硬件加速
        mWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        设置webview的滚动条
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        /**
         * 初始化webview的设置
         */
        initWebSetting();

        // 先载入JS代码
        // 格式规定为:file:///android_asset/文件名.html
//        mWebView.loadUrl("http://114.55.110.201:9191/kitweb/chunkupload/upload_example.html");
//        mWebView.loadUrl("http://rayhahah.s1.natapp.cc/");
        mWebView.loadUrl("file:///android_asset/demo.html");

        // 通过addJavascriptInterface()将Java对象映射到JS对象
        //参数1：Javascript对象名
        //参数2：Java对象名
        //AndroidtoJS类对象映射到js的androidObj对象
        mWebView.addJavascriptInterface(new AndroidToJs(), "androidObj");
        /**
         * 处理各种通知 & 请求事件
         */
        mWebView.setWebViewClient(new MyWebViewClient());

        /**
         * WebChromeClient 主要帮助处理Javascript对话框、网站图标、网站title、加载进度等
         */
        mWebView.setWebChromeClient(new MyChromeClient());
    }

    /**
     * WebView中的配置
     */
    private void initWebSetting() {
        WebSettings webSettings = mWebView.getSettings();
        // 设置与Js交互的权限
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        webSettings.setJavaScriptEnabled(true);


        //设置自适应屏幕，两者合用
        //将图片调整到适合webview的大小
        webSettings.setUseWideViewPort(true);
        // 缩放至屏幕的大小
        webSettings.setLoadWithOverviewMode(true);

        //缩放操作
        //支持缩放，默认为true。是下面那个的前提。
        webSettings.setSupportZoom(true);
        //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setBuiltInZoomControls(true);
        //隐藏原生的缩放控件
        webSettings.setDisplayZoomControls(false);


        //其他细节操作
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        //开启 database storage API 功能
        webSettings.setDatabaseEnabled(true);
        //开启 Application Caches 功能
        webSettings.setAppCacheEnabled(true);
        //设置  Application Caches 缓存目录
        String appCachePath = getFilesDir().getAbsolutePath() + File.separator + "WEB_CACHE";
        webSettings.setAppCachePath(appCachePath);

        //设置可以访问文件
        webSettings.setAllowFileAccess(true);
        //支持通过JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //设置编码格式
        webSettings.setDefaultTextEncodingName("utf-8");
        //设置默认的字体大小，默认为16，有效值区间在1-72之间。
        webSettings.setDefaultFontSize(18);

        //优化设置
        // 是否自动加载图片
        webSettings.setLoadsImagesAutomatically(true);
        // 禁止加载网络图片
        webSettings.setBlockNetworkImage(false);
        // 禁止加载所有网络资源
        webSettings.setBlockNetworkLoads(false);
        //允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http/https 等源。
        // 这个设置在 JELLY_BEAN 以前的版本默认是允许，在 JELLY_BEAN 及以后的版本中默认是禁止的。
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        // 用户是否需要通过手势播放媒体(不会自动播放)，默认值 true
        webSettings.setMediaPlaybackRequiresUserGesture(true);
        //设置是否可以定位，默认true
        webSettings.setGeolocationEnabled(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // 是否在离开屏幕时光栅化(会增加内存消耗)，默认值 false
            webSettings.setOffscreenPreRaster(false);
        }

        //同时支持http和https协议
        if (Build.VERSION.SDK_INT >= 21) {
            // 5.0以上允许加载http和https混合的页面(5.0以下默认允许，5.0+默认禁止)
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

    }

    @Override
    protected void onDestroy() {

        //如此才能真正清除，不会导致内存泄漏
        if (mWebView != null) {
            //加载空内容
            mWebView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            mWebView.clearHistory();
            mWebView.removeAllViews();
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
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


    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void evaluateClick(View view) {
        /**
         * 不会刷新页面，效率更好
         * 4.4以后才可以使用
         */
        mWebView.evaluateJavascript("javascript:schemeNext()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //js的返回值

            }
        });

    }

    /**
     * 相关清楚操作
     */
    public void clear() {
        //清除网页访问留下的缓存
//由于内核缓存是全局的因此这个方法不仅仅针对webview而是针对整个应用程序.
        mWebView.clearCache(true);

//清除当前webview访问的历史记录
//只会webview访问历史记录里的所有记录除了当前访问记录
        mWebView.clearHistory();

//这个api仅仅清除自动完成填充的表单数据，并不会清除WebView存储到本地的数据
        mWebView.clearFormData();
    }

}
