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
            String id = obj.getString("id");
            Bundle bundle = new Bundle();
            bundle.putString("url","file:///android_asset/duihuaninfo.html?id="+id);
            bundle.putString("title","兑换详情");
            pushVC(XHtmlVC.class,bundle);

        }

        if(type.equals("2") && msg.equals("兑换商品"))
        {
            String id = obj.getString("id");

            doDH(id);

        }

        if(type.equals("3") && msg.equals("跳转怀府币商城"))
        {
            pushVC(GoodsCenter.class);
        }
    }

    private boolean running = false;
    public void doDH(String id) {

        if(running){return;}
        running = true;

        String uid = APPDataCache.User.getUid();
        String uname = APPDataCache.User.getUsername();

        XActivityindicator.create(mContext).show();

        XNetUtil.Handle(APPService.jifenAddDH(uid,uname,id), "兑换成功", "兑换失败", new XNetUtil.OnHttpResult<Boolean>() {
            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
                running = false;
            }

            @Override
            public void onSuccess(Boolean aBoolean) {
                running = false;
                if(aBoolean)
                {
                    web.getRefreshableView().loadUrl("javascript:reload()");
                }
            }
        });

    }


    @Override
    protected void setupData() {

    }

}
