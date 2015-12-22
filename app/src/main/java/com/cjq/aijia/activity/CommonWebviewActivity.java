package com.cjq.aijia.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.cjq.aijia.BaseActivity;
import com.cjq.aijia.R;
import com.cjq.aijia.entity.EventJumpIndex;
import com.cjq.aijia.util.JsInterface;
import com.cjq.aijia.util.SaveTool;
import com.cjq.aijia.util.ToastUtil;
import com.ypy.eventbus.EventBus;

import java.io.File;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CommonWebViewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    @InjectView(R.id.common_title)
    TextView title;
    @InjectView(R.id.common_web)
    WebView web;
    @InjectView(R.id.common_back)
    View back;
    @InjectView(R.id.common_close)
    View close;
    @InjectView(R.id.common_refresh)
    SwipeRefreshLayout refreshLayout;

//    @InjectView(R.id.activity_common_title_bar)
//    Toolbar titleBar;

    public static final String EXTRA_URL = "url";
    public static final String EXTRA_TITLE = "title";
    private String url;
    private boolean flag;
    private ValueCallback<Uri[]> mFilePathCallback;
    private Uri mUri;
    private ValueCallback<Uri> mUploadMsg;
    private boolean titleFlag;

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_webview);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        String urlS = intent.getStringExtra(EXTRA_URL);
        final String titleText = intent.getStringExtra(EXTRA_TITLE);
        titleFlag = "".equals(titleText);
//        refreshLayout.setEnabled(true);
        dealURL(urlS);

        WebSettings webSettings = web.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setAllowFileAccess(true);
//        webSettings.setBuiltInZoomControls(true);

        web.addJavascriptInterface(new JsInterface() {
            @JavascriptInterface
            public void login_request() {
                SaveTool.clear(CommonWebViewActivity.this);
                EventBus.getDefault().post(new EventJumpIndex().setNum(3));
                finish();
            }
        }, "app");

        web.setWebChromeClient(new WebChromeClient() {
                                   @Override
                                   public boolean onConsoleMessage(ConsoleMessage consoleMessage) {

                                       System.out.println(consoleMessage.message());

                                       return super.onConsoleMessage(consoleMessage);
                                   }

                                   @Override
                                   public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                                       return super.onJsAlert(view, url, message, result);
                                   }

                                   @Override
                                   public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
                                       return super.onJsBeforeUnload(view, url, message, result);
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
                                   public boolean onJsTimeout() {
                                       return super.onJsTimeout();
                                   }

                                   @Override
                                   public void onProgressChanged(WebView view, int newProgress) {
                                       super.onProgressChanged(view, newProgress);
                                   }

                                   public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                                       upload(uploadMsg);
                                   }

                                   public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                                       upload1(filePathCallback);
                                       return true;
                                   }
                               }

        );

        web.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (refreshLayout.isRefreshing())
                    refreshLayout.setRefreshing(false);
//                if(flag){
//                    refreshLayout.setVisibility(View.GONE);
//                }
                CommonWebViewActivity.this.title.setText(view.getTitle());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("cart_list")) {
                    EventBus.getDefault().post(new EventJumpIndex(2));
                    finish();
                }

                if (!url.contains("key")) {
                    dealURL(url);
                    view.loadUrl(CommonWebViewActivity.this.url);
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                refreshLayout.setVisibility(View.VISIBLE);
//                flag=false;
                view.stopLoading();
                view.loadUrl("file:///android_asset/net_err_hint.html");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                view.stopLoading();
                view.loadUrl("file:///android_asset/net_err_hint.html");
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                view.stopLoading();
                view.loadUrl("file:///android_asset/net_err_hint.html");
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                view.stopLoading();
                view.loadUrl("file:///android_asset/net_err_hint.html");
                super.onReceivedError(view, request, error);
            }
        });

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color), getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(this);

//        titleBar.setTitle(titleText);
        title.setText(titleText);

        web.loadUrl(url);
        back.setOnClickListener(this);
        close.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.common_web_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.common_web_close) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void upload1(ValueCallback<Uri[]> filePathCallback) {
        mFilePathCallback = filePathCallback;
        getPic();
    }

    private void getPic() {
        new AlertDialog.Builder(CommonWebViewActivity.this).setItems(new String[]{"拍照", "从相册选择"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Intent test = new Intent("android.media.action.IMAGE_CAPTURE");

                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + getPackageName() + "/", new Date().getTime() + ".png");

                            mUri = Uri.fromFile(file);

                            if (!file.getParentFile().exists()) {
                                file.getParentFile().mkdirs();
                            }
                            Uri fileUri = Uri.fromFile(file);
                            test.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(test, 0);
                        } else {
                            ToastUtil.showToast(CommonWebViewActivity.this, "没有SD卡");
                        }
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                        break;
                }
            }
        }).show();
    }

    private void upload(ValueCallback<Uri> filePathCallback) {
        mUploadMsg = filePathCallback;
        getPic();
    }

    private final void dealURL(String urlS) {
        try {
            if (urlS.contains("?")) {
                if (!urlS.contains("key")) {
                    url = urlS + "&key=" + SaveTool.getKey(this);
                } else {
                    url = urlS.replaceFirst("key=\\w+&", "key=" + SaveTool.getKey(this) + "&");
                }
            } else {
                url = urlS + "?key=" + SaveTool.getKey(this);
            }
        } catch (Exception e) {
            url = urlS;
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    if (mFilePathCallback != null)
                        mFilePathCallback.onReceiveValue(new Uri[]{uri});
                    if (mUploadMsg != null)
                        mUploadMsg.onReceiveValue(uri);
                    mFilePathCallback = null;
                    mUploadMsg = null;
                } else {
                    if (mFilePathCallback != null)
                        mFilePathCallback.onReceiveValue(new Uri[]{});
                    if (mUploadMsg != null)
                        mUploadMsg.onReceiveValue(null);
                    mFilePathCallback = null;
                    mUploadMsg = null;
                }
                break;

            case 0:
                if (resultCode == RESULT_OK) {
                    if (mFilePathCallback != null)
                        mFilePathCallback.onReceiveValue(new Uri[]{mUri});
                    if (mUploadMsg != null)
                        mUploadMsg.onReceiveValue(mUri);
                    mFilePathCallback = null;
                    mUri = null;
                    mUploadMsg = null;
                } else {
                    if (mFilePathCallback != null)
                        mFilePathCallback.onReceiveValue(new Uri[]{});
                    if (mUploadMsg != null)
                        mUploadMsg.onReceiveValue(null);
                    mFilePathCallback = null;
                    mUploadMsg = null;
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public final void onRefresh() {
//        dealURL(url);
//        web.loadUrl(url);
        web.reload();
//        flag=true;
    }

    public final static void startCommonWeb(Context context, String title, String url) {
        Intent intent = new Intent(context, CommonWebViewActivity.class);
        intent.putExtra(CommonWebViewActivity.EXTRA_TITLE, title);
        intent.putExtra(CommonWebViewActivity.EXTRA_URL, url);
        context.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (web.canGoBack()) {
                web.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.common_close:
                finish();
                break;
            case R.id.common_back:
                if (web.canGoBack()) {
                    web.goBack();
                } else {
                    finish();
                }
                break;
        }
    }
}
