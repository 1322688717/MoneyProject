/*
 * Copyright (C)  Justson(https://github.com/Justson/AgentWeb)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.just.agentweb;


import android.content.Context;
import android.os.Build;
import android.view.View;

import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * @author cenxiaozhong
 * @update 4.0.0 ,WebDefaultSettingsManager rename to AbsAgentWebSettings
 * @since 1.0.0
 */

public abstract class AbsAgentWebSettings implements IAgentWebSettings, WebListenerManager {
    private WebSettings mWebSettings;
    private static final String TAG = AbsAgentWebSettings.class.getSimpleName();
    public static final String USERAGENT_UC = " UCBrowser/11.6.4.950 ";
    public static final String USERAGENT_QQ_BROWSER = " MQQBrowser/8.0 ";
    public static final String USERAGENT_AGENTWEB = AgentWebConfig.AGENTWEB_VERSION;
    protected AgentWeb mAgentWeb;

    public static AbsAgentWebSettings getInstance() {
        return new AgentWebSettingsImpl();
    }

    public AbsAgentWebSettings() {
    }

    final void bindAgentWeb(AgentWeb agentWeb) {
        this.mAgentWeb = agentWeb;
        this.bindAgentWebSupport(agentWeb);
    }

    protected abstract void bindAgentWebSupport(AgentWeb agentWeb);

    @Override
    public IAgentWebSettings toSetting(WebView webView) {
        settings(webView);
        return this;
    }

    private void settings(WebView webView) {
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSavePassword(false);
        if (AgentWebUtils.checkNetwork(webView.getContext())) {
            //??????cache-control???????????????
            mWebSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            //?????????????????????????????????????????????
            mWebSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        //-------???????????????????????????
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            //??????5.0?????????http???https??????????????????
//            mWebSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
        //-------???????????????????????????


        mWebSettings.setTextZoom(100);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setSupportMultipleWindows(false);
        // ??????????????????????????????  ??????http or https
        mWebSettings.setBlockNetworkImage(false);
        // ????????????????????????html  file??????
        mWebSettings.setAllowFileAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            // ?????? file url ????????? Javascript ??????????????????????????? .????????????
            mWebSettings.setAllowFileAccessFromFileURLs(false);
            // ???????????? file url ????????? Javascript ??????????????????????????????????????????????????? http???https ???????????????
            mWebSettings.setAllowUniversalAccessFromFileURLs(false);
        }
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        } else {
            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        }
        mWebSettings.setLoadWithOverviewMode(false);
        mWebSettings.setUseWideViewPort(false);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setNeedInitialFocus(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");//??????????????????
        mWebSettings.setDefaultFontSize(16);
        mWebSettings.setMinimumFontSize(12);//?????? WebView ??????????????????????????????????????? 8
        mWebSettings.setGeolocationEnabled(true);
        String dir = AgentWebConfig.getCachePath(webView.getContext());
        LogUtils.i(TAG, "dir:" + dir + "   appcache:" + AgentWebConfig.getCachePath(webView.getContext()));
        //?????????????????????  api19 ????????????,??????????????? webkit ?????????
        mWebSettings.setGeolocationDatabasePath(dir);
        mWebSettings.setDatabasePath(dir);
        mWebSettings.setAppCachePath(dir);
        //?????????????????????
        mWebSettings.setAppCacheMaxSize(Long.MAX_VALUE);
        mWebSettings.setUserAgentString(getWebSettings()
                .getUserAgentString()
                .concat(USERAGENT_AGENTWEB)
                .concat(USERAGENT_UC)
        );
        LogUtils.i(TAG, "UserAgentString : " + mWebSettings.getUserAgentString());
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            // ??????9.0???????????????????????????????????????????????????????????????????????????
//            // ?????? https://blog.csdn.net/lvshuchangyin/article/details/89446629
//            Context context = webView.getContext();
//            String processName = ProcessUtils.getCurrentProcessName(context);
//            if (!context.getApplicationContext().getPackageName().equals(processName)) {
//                WebView.setDataDirectorySuffix(processName);
//            }
//        }
    }

    @Override
    public WebSettings getWebSettings() {
        return mWebSettings;
    }

    @Override
    public WebListenerManager setWebChromeClient(WebView webview, WebChromeClient webChromeClient) {
        webview.setWebChromeClient(webChromeClient);
        return this;
    }

    @Override
    public WebListenerManager setWebViewClient(WebView webView, WebViewClient webViewClient) {
        webView.setWebViewClient(webViewClient);
        return this;
    }

    @Override
    public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
        webView.setDownloadListener(downloadListener);
        return this;
    }

}
