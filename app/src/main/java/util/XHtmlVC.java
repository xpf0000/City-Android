package util;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.google.gson.internal.LinkedTreeMap;
import com.handmark.pulltorefresh.library.extras.PullToRefreshWebView2;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Activity.NewsInfoActivity;
import citycircle.com.Activity.PhotoLook;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.hfb.GoodsCenter;
import model.GoodsModel;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/27.
 */

public class XHtmlVC extends BaseActivity {

    private PullToRefreshWebView2 web;
    private WebSettings mWebSettings;
    Handler handlers = new Handler();

    @Override
    protected void setupUi() {
        setContentView(R.layout.xhtmlvc);

        web = (PullToRefreshWebView2)findViewById(R.id.web);

        WebView webView = web.getRefreshableView();

        // 设置支持JavaScript等
        mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                if (progress == 100) {

                }
            }

        });
        webView.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void runAndroidMethod(final String str) {
                handlers.post(new Runnable() {

                    @Override
                    public void run() {
                        XNetUtil.APPPrintln("js str: "+str);
                        JSONObject obj = JSON.parseObject(str);
                        handleMsg(obj);
                    }
                });

            }

        }, "android");

        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");

        setPageTitle(title);

        webView.loadUrl(url);
    }

    private void handleMsg(JSONObject obj)
    {
        String type=obj.getString("type");
        String msg=obj.getString("msg");
        if(type.equals("1") && msg.equals("兑换商品"))
        {
            String uid = APPDataCache.User.getUid();
            String uname = APPDataCache.User.getUsername();
            String id = obj.getString("id");
            Bundle bundle = new Bundle();
            bundle.putString("url","file:///android_asset/duihuaninfo.html?id="+id+"&uid="+uid+"&uname="+uname);
            bundle.putString("title","兑换详情");
            pushVC(XHtmlVC.class,bundle);

        }

        if(type.equals("2") && msg.equals("开始兑换商品"))
        {
            XActivityindicator.create(mContext).show();
        }

        if(type.equals("2") && msg.equals("商品兑换成功"))
        {
            final String id=obj.getString("id");
            SVProgressHUD hud = XActivityindicator.create(mContext);
            hud.showSuccessWithStatus("兑换成功");
            hud.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(SVProgressHUD hud) {

                    Bundle bundle = new Bundle();
                    bundle.putString("url","file:///android_asset/duihuansuccess.html?id="+id);
                    bundle.putString("title","兑换详情");
                    pushVC(XHtmlVC.class,bundle);

                }
            });
        }

        if(type.equals("2") && msg.equals("商品兑换失败"))
        {
            String info=obj.getString("info");
            XActivityindicator.create(mContext).showErrorWithStatus(info);
        }

        if(type.equals("3") && msg.equals("跳转怀府币商城"))
        {
            pushVC(GoodsCenter.class);
        }
    }




    @Override
    protected void setupData() {

    }

}
