package util;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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
import java.util.List;

import citycircle.com.Activity.NewsInfoActivity;
import citycircle.com.Activity.PhotoLook;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.hfb.GoodsCenter;
import citycircle.com.hfb.HfbCenter;
import model.GoodsModel;
import model.NewsModel;

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

        XNetUtil.APPPrintln("url: "+url);

        if(url.equals("怀府币规则"))
        {
            getHFBGuize();
        }
        else
        {
            webView.loadUrl(url);
        }

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
            pushVC(HfbCenter.class);
        }

        if(type.equals("4") && msg.equals("积分兑换"))
        {
            XActivityindicator.create(mContext).show();
        }

        if(type.equals("4") && msg.equals("积分兑换成功"))
        {
            final int hfb=obj.getJSONObject("info").getIntValue("hfb");
            final int jifen=obj.getJSONObject("info").getIntValue("jifen");
            final String sname=obj.getJSONObject("info").getString("sname");
            final String time=obj.getJSONObject("info").getString("create_time");

            XActivityindicator.create(mContext).showSuccessWithStatus("兑换成功");
            XActivityindicator.getHud().setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(SVProgressHUD hud) {

                    Bundle bundle = new Bundle();
                    bundle.putString("url","file:///android_asset/dhhfbsuccess.html?hfb="+hfb+
                            "&jifen="+jifen+"&sname="+sname+"&time="+time);
                    bundle.putString("title","兑换详情");
                    pushVC(XHtmlVC.class,bundle);

                }
            });

            XNotificationCenter.getInstance().postNotice("PaySuccess",null);

        }

        if(type.equals("4") && msg.equals("积分兑换失败"))
        {
            String info=obj.getString("info");
            XActivityindicator.create(mContext).showErrorWithStatus(info);
        }

    }

    private void getHFBGuize()
    {
        XNetUtil.Handle(APPService.newsGetArticle(), new XNetUtil.OnHttpResult<List<NewsModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<NewsModel> newsModels) {
                if(newsModels.size() > 0)
                {
                    String str = newsModels.get(0).getContent();

                    String infos = "<html>\r\n\t"
                            + "<head>\r\n"
                            + "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\"/>"
                            + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0\" />"
                            + "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
                            + "<style>\r\n\t "
                            + "table {border-right:1px dashed #D2D2D2;border-bottom:1px dashed #D2D2D2} \r\n\t "
                            + "table td{border-left:1px dashed #D2D2D2;border-top:1px dashed #D2D2D2} \r\n\t"
                            + "img {width:100%}\r\n" + "</style>\r\n\t"
                            + "</head>\r\n" + "<body style=\"width:[width]\">\r\n"
                            + str + "\r\n</body>" + "</html>";
                    web.getRefreshableView().loadDataWithBaseURL(null,infos , "text/html", "utf-8", null);

                }
            }
        });



    }



    @Override
    protected void setupData() {

    }

}
