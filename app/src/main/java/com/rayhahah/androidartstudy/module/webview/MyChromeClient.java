package com.rayhahah.androidartstudy.module.webview;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * ┌───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┐ ┌───┬───┬───┐
 * │Esc│ │ F1│ F2│ F3│ F4│ │ F5│ F6│ F7│ F8│ │ F9│F10│F11│F12│ │P/S│S L│P/B│ ┌┐    ┌┐    ┌┐
 * └───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┴───┘ └───┴───┴───┘ └┘    └┘    └┘
 * ┌──┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───────┐┌───┬───┬───┐┌───┬───┬───┬───┐
 * │~`│! 1│@ 2│# 3│$ 4│% 5│^ 6│& 7│* 8│( 9│) 0│_ -│+ =│ BacSp ││Ins│Hom│PUp││N L│ / │ * │ - │
 * ├──┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─────┤├───┼───┼───┤├───┼───┼───┼───┤
 * │Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{ [│} ]│ | \ ││Del│End│PDn││ 7 │ 8 │ 9 │   │
 * ├────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤└───┴───┴───┘├───┼───┼───┤ + │
 * │Caps │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  │             │ 4 │ 5 │ 6 │   │
 * ├─────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────────┤    ┌───┐    ├───┼───┼───┼───┤
 * │Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│  Shift   │    │ ↑ │    │ 1 │ 2 │ 3 │   │
 * ├────┬──┴─┬─┴──┬┴───┴───┴───┴───┴───┴──┬┴───┼───┴┬────┬────┤┌───┼───┼───┐├───┴───┼───┤ E││
 * │Ctrl│Ray │Alt │         Space         │ Alt│code│fuck│Ctrl││ ← │ ↓ │ → ││   0   │ . │←─┘│
 * └────┴────┴────┴───────────────────────┴────┴────┴────┴────┘└───┴───┴───┘└───────┴───┴───┘
 *
 * @author Rayhahah
 * @blog http://rayhahah.com
 * @time 2017/12/20
 * @tips 这个类是Object的子类
 * @fuction
 */

/**
 * WebChromeClient 主要帮助处理Javascript对话框、网站图标、网站title、加载进度等
 */
public class MyChromeClient extends WebChromeClient {
    // 由于设置了弹窗检验调用结果,所以需要支持js对话框
    // webview只是载体，内容的渲染需要使用webviewChromClient类去实现
    // 通过设置WebChromeClient对象处理JavaScript的对话框
    //设置响应js 的Alert()函数

    /**
     * =======================常用=========================================
     */


    // 接收当前页面的加载进度
    //可以比WebViewClient.onPageFinished精准，请使用这个来代替
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
    }

    // 接收文档标题
    @Override
    public void onReceivedTitle(WebView view, String title) {
    }

    // 接收图标(favicon)
    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
    }


    // 显示一个alert对话框
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        AlertDialog.Builder b = new AlertDialog.Builder(view.getContext());
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

    // 显示一个confirm对话框
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
        return false;
    }

    // 显示一个prompt对话框
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        return false;
    }

    /**
     * 接收JavaScript控制台消息
     */
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        Log.e("lzh", consoleMessage.message());
        return true;
    }


    // 显示一个对话框让用户选择是否离开当前页面
    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        return false;
    }

    /**
     * 通知应用打开新窗口
     */
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return false;
    }

    /**
     * 通知应用关闭窗口
     */
    @Override
    public void onCloseWindow(WebView window) {
    }

    /**
     * 请求获取取焦点
     */
    @Override
    public void onRequestFocus(WebView view) {
    }

    /**
     * 为'<input type="file" />'显示文件选择器，返回false使用默认处理
     */
    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
        return false;
    }



    /**
     * =====================不常用==============================
     */

    // 获得所有访问历史项目的列表，用于链接着色。
    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
    }

    // <video /> 控件在未播放时，会展示为一张海报图，HTML中可通过它的'poster'属性来指定。
// 如果未指定'poster'属性，则通过此方法提供一个默认的海报图。
    @Override
    public Bitmap getDefaultVideoPoster() {
        return null;
    }

    // 当全屏的视频正在缓冲时，此方法返回一个占位视图(比如旋转的菊花)。
    @Override
    public View getVideoLoadingProgressView() {
        return null;
    }

    // Android中处理Touch Icon的方案
// http://droidyue.com/blog/2015/01/18/deal-with-touch-icon-in-android/index.html
    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
    }

    // 通知应用当前页进入了全屏模式，此时应用必须显示一个包含网页内容的自定义View
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
    }

    // 通知应用当前页退出了全屏模式，此时应用必须隐藏之前显示的自定义View
    @Override
    public void onHideCustomView() {
    }


    // 指定源的网页内容在没有设置权限状态下尝试使用地理位置API。
    // 从API24开始，此方法只为安全的源(https)调用，非安全的源会被自动拒绝
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
    }

    /**
     * 当前一个调用onGeolocationPermissionsShowPrompt() 取消时，隐藏相关的UI。
     */
    @Override
    public void onGeolocationPermissionsHidePrompt() {
    }


    /**
     * 通知应用网页内容申请访问指定资源的权限(该权限未被授权或拒绝)
     */
    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequest(PermissionRequest request) {
        request.deny();
    }

    /**
     * 通知应用权限的申请被取消，隐藏相关的UI。
     */
    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onPermissionRequestCanceled(PermissionRequest request) {
    }


}
