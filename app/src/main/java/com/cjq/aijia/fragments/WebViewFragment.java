package com.cjq.aijia.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.HttpAuthHandler;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.cjq.aijia.CommonData;
import com.cjq.aijia.R;
import com.cjq.aijia.activity.CommonWebViewActivity;
import com.cjq.aijia.entity.EventJumpIndex;
import com.cjq.aijia.entity.EventWebChange;
import com.cjq.aijia.entity.EventWebRefresh;
import com.cjq.aijia.util.JsInterface;
import com.cjq.aijia.util.SaveTool;
import com.cjq.aijia.util.ToastUtil;
import com.ypy.eventbus.EventBus;

import java.io.File;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by CJQ on 2015/11/12.
 */
public class WebViewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, TextView.OnEditorActionListener, View.OnClickListener {

    private static Fragment INSTANCE;
    private String url_origin = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private Uri mUri;
    private ValueCallback<Uri> mUploadMsg;

    public static Fragment getInstance() {
        if (INSTANCE == null)
            INSTANCE = new WebViewFragment();
        return INSTANCE;
    }

    @InjectView(R.id.main_web)
    WebView webView;
    @InjectView(R.id.web_refresh)
    SwipeRefreshLayout refreshLayout;
    @InjectView(R.id.fragment_web_title)
    TextView title;
    @InjectView(R.id.fragment_web_logo)
    View logo;
    @InjectView(R.id.fragment_web_search)
    View search;
    @InjectView(R.id.fragment_web_search_text)
    EditText searchText;

    String url = CommonData.INDEX_URL;

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    private void upload1(ValueCallback<Uri[]> filePathCallback) {
        mFilePathCallback = filePathCallback;
        getPic();
    }

    private void getPic() {
        new AlertDialog.Builder(getActivity()).setItems(new String[]{"拍照", "从相册选择"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            Intent test = new Intent("android.media.action.IMAGE_CAPTURE");

                            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + getActivity().getPackageName() + "/", new Date().getTime() + ".png");

                            mUri = Uri.fromFile(file);

                            if (!file.getParentFile().exists()) {
                                file.getParentFile().mkdirs();
                            }
                            Uri fileUri = Uri.fromFile(file);
                            test.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                            startActivityForResult(test, 0);
                        } else {
                            ToastUtil.showToast(getActivity(), "没有SD卡");
                        }
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                        break;
                }
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (mFilePathCallback != null)
                    mFilePathCallback.onReceiveValue(new Uri[]{});
                if (mUploadMsg != null)
                    mUploadMsg.onReceiveValue(null);
                mFilePathCallback = null;
                mUploadMsg = null;
            }
        }).show();
    }

    private void upload(ValueCallback<Uri> filePathCallback) {
        mUploadMsg = filePathCallback;
        getPic();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web, container, false);
        EventBus.getDefault().register(this);
        ButterKnife.inject(this, view);
        dealURL(url);
        webView.loadUrl(url);
        webView.setVerticalScrollBarEnabled(false);
        webView.setHorizontalScrollBarEnabled(false);
        WebSettings webSettings = webView.getSettings();

        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsInterface() {
            @JavascriptInterface
            public void login_request() {
                SaveTool.clear(getActivity());
                EventBus.getDefault().post(new EventJumpIndex().setNum(3));
            }
        }, "app");
        webView.setWebChromeClient(new WebChromeClient() {


            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                upload(uploadMsg);
            }

            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                upload1(filePathCallback);
                return true;
            }
        });

        webView.setWebViewClient(new WebViewClient() {
                                     @Override
                                     public void onPageFinished(WebView view, String url) {
                                         super.onPageFinished(view, url);
                                         if (refreshLayout.isRefreshing()) {
                                             refreshLayout.setRefreshing(false);
                                             EventBus.getDefault().post(new EventWebRefresh());
                                         }
                                     }

                                     @Override
                                     public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                                         super.onReceivedError(view, request, error);
                                         view.stopLoading();
                                         view.loadUrl("file:///android_asset/net_err_hint.html");
                                     }

                                     @Override
                                     public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                                         super.onReceivedHttpError(view, request, errorResponse);
                                         view.stopLoading();
                                         view.loadUrl("file:///android_asset/net_err_hint.html");
                                     }

                                     @Override
                                     public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                                         view.stopLoading();
                                         view.loadUrl("file:///android_asset/net_err_hint.html");
                                         super.onReceivedHttpAuthRequest(view, handler, host, realm);
                                     }

                                     @Override
                                     public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                         super.onReceivedError(view, errorCode, description, failingUrl);
                                         view.stopLoading();
                                         view.loadUrl("file:///android_asset/net_err_hint.html");
                                     }

                                     @Override
                                     public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                         CommonWebViewActivity.startCommonWeb(getActivity(), "", url);
                                         return true;
                                     }
                                 }
        );

        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_color), getResources().getColor(R.color.colorAccent));
        refreshLayout.setOnRefreshListener(this);

        title.setText("爱家商城");

        searchText.setOnEditorActionListener(this);
        search.setOnClickListener(this);

        searchText.setVisibility(View.VISIBLE);
        title.setVisibility(View.GONE);
        search.setVisibility(View.VISIBLE);
        logo.setVisibility(View.VISIBLE);
        url_origin = CommonData.INDEX_URL;
        dealURL(CommonData.INDEX_URL);
        webView.stopLoading();
        webView.loadUrl(url);

        return view;
    }

    public void onEventMainThread(EventWebChange webChange) {
        if (!webChange.getUrl().equals(url_origin)) {
            if (CommonData.INDEX_URL.equals(webChange.getUrl())) {
                searchText.setVisibility(View.VISIBLE);
                title.setVisibility(View.GONE);
                search.setVisibility(View.VISIBLE);
                logo.setVisibility(View.VISIBLE);
                url_origin = webChange.getUrl();
                dealURL(webChange.getUrl());
                webView.stopLoading();
                webView.loadUrl(url);
            } else {
                searchText.setVisibility(View.GONE);
                title.setVisibility(View.VISIBLE);
                search.setVisibility(View.GONE);
                logo.setVisibility(View.GONE);
                title.setText(webChange.getName());
                url_origin = webChange.getUrl();
                dealURL(webChange.getUrl());
                webView.stopLoading();
                webView.loadUrl(url);
            }
        } else {
            webView.stopLoading();
            webView.reload();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();

                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor =getActivity().managedQuery(uri, proj, null, null, null);
                    int column_index = cursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    File file = new File(path);
                    Uri uri_special = Uri.fromFile(file);

                    if (mFilePathCallback != null)
                        mFilePathCallback.onReceiveValue(new Uri[]{uri_special});
                    if (mUploadMsg != null)
                        mUploadMsg.onReceiveValue(uri_special);
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
                if (resultCode == Activity.RESULT_OK) {
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
//        webView.loadUrl(url);
        webView.reload();
    }

    private final void dealURL(String urlS) {
        try {
            if (urlS.contains("?")) {
                if (!urlS.contains("key")) {
                    url = urlS + "&key=" + SaveTool.getKey(getActivity());
                }
//                else {
//                    url = urlS.replaceFirst("key=\\w+&", "key=" + SaveTool.getKey(getActivity()) + "&");
//                }
            } else {
                url = urlS + "?key=" + SaveTool.getKey(getActivity());
            }
        } catch (Exception e) {
            url = urlS;
            e.printStackTrace();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == KeyEvent.KEYCODE_SEARCH && event.getAction() == KeyEvent.ACTION_DOWN) {
            doSearch();
            return true;
        }
        return false;
    }

    private void doSearch() {
        String keyWord = searchText.getText().toString();
        String url = CommonData.SEARCH_URL + "?keyword=" + android.text.TextUtils.htmlEncode(keyWord);
        CommonWebViewActivity.startCommonWeb(getActivity(), "搜索结果", url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_web_search:
                doSearch();
                break;
        }
    }
}
