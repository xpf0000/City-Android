package util;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.bigkoo.svprogresshud.listener.OnDismissListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.extras.PullToRefreshWebView2;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import citycircle.com.Activity.NewsInfoActivity;
import citycircle.com.Activity.UpPasswords;
import citycircle.com.R;
import citycircle.com.Utils.MyEventBus;
import citycircle.com.hfb.HfbCenter;
import citycircle.com.user.AuthBandPhoneVC;
import model.NewsModel;
import model.UserModel;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;
import static citycircle.com.MyAppService.LocationApplication.context;

/**
 * Created by X on 2016/11/27.
 */

public class XHtmlVC extends BaseActivity {

    private WebView web;
    Handler handlers = new Handler();

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface","NewApi"})
    @Override
    protected void setupUi() {
        setContentView(R.layout.xhtmlvc);

        EventBus.getDefault().register(this);

        web = (WebView)findViewById(R.id.web);
        //web.setMode(PullToRefreshBase.Mode.DISABLED);
        //WebView webView = web;

        // 设置支持JavaScript等
        WebSettings mWebSettings = web.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebSettings.setDomStorageEnabled(false);
        mWebSettings.setDatabaseEnabled(false);
        mWebSettings.setGeolocationEnabled(false);
        mWebSettings.setAppCacheEnabled(false);

        web.setWebViewClient(new WebViewClient(){


            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {


                XNetUtil.APPPrintln("shouldInterceptRequest000 url: "+request.getUrl());

                return super.shouldInterceptRequest(view, request);

            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                XNetUtil.APPPrintln(view);
                XNetUtil.APPPrintln("shouldInterceptRequest111 url: "+url);
                return super.shouldInterceptRequest(view, url);


            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);

                XNetUtil.APPPrintln("onLoadResource url: "+url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                String url = request.getUrl().toString().toLowerCase();

                XNetUtil.APPPrintln("url000: "+url);

                return false;

            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String u) {

                String url = u.toLowerCase();
                XNetUtil.APPPrintln("url111: "+url);

                return false;
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);

                XNetUtil.APPPrintln("request000: "+request.getUrl());
                XNetUtil.APPPrintln("errorResponse000: "+errorResponse.toString());

            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);

                XNetUtil.APPPrintln("request111: "+request.getUrl());
                XNetUtil.APPPrintln("errorResponse111: "+error.toString());

            }
        });

        web.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                if (progress == 100) {

                }
            }

        });
        web.addJavascriptInterface(new Object() {
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
            getHFBGuize("6892");
        }
        else if(url.equals("服务条款"))
        {
            getHFBGuize("6243");
        }
        else
        {
            web.loadUrl(url);
        }

    }

    private static final String APP_CACAHE_DIRNAME ="/webcache";
    /**
     * 清除WebView缓存  在onDestroy调用这个方法就可以了
     */
    public void clearWebViewCache(){

        web.stopLoading();
        web.clearCache(true);
        web.clearHistory();
        web.setWebChromeClient(null);
        web.setWebViewClient(null);
        web.removeJavascriptInterface("android");
        web = null;


        //清理Webview缓存数据库
        try{
            deleteDatabase("webview.db");
            deleteDatabase("webviewCache.db");
        }catch(Exception e) {
            e.printStackTrace();
        }

        //WebView 缓存文件
        File appCacheDir =new File(getFilesDir().getAbsolutePath()+APP_CACAHE_DIRNAME);

        File webviewCacheDir =new File(getCacheDir().getAbsolutePath()+"/webviewCache");

        //删除webview 缓存目录
        if(webviewCacheDir.exists()){
            deleteFile(webviewCacheDir);
        }
        //删除webview 缓存 缓存目录
        if(appCacheDir.exists()){
            deleteFile(appCacheDir);
        }
    }

    /**
     * 递归删除 文件/文件夹
     *
     * @param file
     */
    public void deleteFile(File file) {

        if(file.exists()) {
            if(file.isFile()) {
                file.delete();
            }else if(file.isDirectory()) {
                File files[] = file.listFiles();
                for(int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
            }
            file.delete();
        }else{

        }
    }


    private void handleMsg(JSONObject obj)
    {
        String type=obj.getString("type");
        String msg=obj.getString("msg");

        if(type.equals("0") && msg.equals("跳转签到规则"))
        {
            Bundle bundle = new Bundle();
            bundle.putString("url","file:///android_asset/hfbguize.html?id=6886");
            bundle.putString("title","签到规则");
            pushVC(XHtmlVC.class,bundle);
        }

        if(type.equals("1") && msg.equals("兑换商品"))
        {
            if(XAPPUtil.isNetWorkAvailable(this))
            {
                String uid = APPDataCache.User.getUid();
                String uname = APPDataCache.User.getUsername();
                String id = obj.getString("id");
                Bundle bundle = new Bundle();
                bundle.putString("url","file:///android_asset/duihuaninfo.html?id="+id+"&uid="+uid+"&uname="+uname);
                bundle.putString("title","兑换详情");
                pushVC(XHtmlVC.class,bundle);
            }

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

        if(type.equals("4") && msg.equals("积分兑换失败"))
        {
            String info=obj.getString("info");
            XActivityindicator.create(mContext).showErrorWithStatus(info);
        }

        if(type.equals("5") && msg.equals("跳转注册页面"))
        {
            String bindtype=obj.getString("bindtype");
            String ptype = "";

            switch (bindtype) {
            case "新浪微博":
                ptype = "1";
                break;
            case "微信":
                ptype = "2";
                break;
            case "QQ":
                ptype = "3";
                break;
            default:
                break;
            }

            Bundle bundle = new Bundle();
            bundle.putString("type",ptype);
            pushVC(AuthBandPhoneVC.class,bundle);

        }

        if(type.equals("5") && msg.equals("跳转绑定现有帐号页面"))
        {
            String openid=obj.getString("openid");
            String bindtype=obj.getString("bindtype");
            String ptype = "";

            switch (bindtype) {
                case "新浪微博":
                    ptype = "1";
                    break;
                case "微信":
                    ptype = "2";
                    break;
                case "QQ":
                    ptype = "3";
                    break;
                default:
                    break;
            }

            Bundle bundle = new Bundle();
            bundle.putString("url","file:///android_asset/loginBind.html?openid="+openid+"&type="+ptype);
            bundle.putString("title","登录绑定");
            pushVC(XHtmlVC.class,bundle);

        }

        if(type.equals("6") && msg.equals("跳转找回密码页面"))
        {
            pushVC(UpPasswords.class);
        }

        if(type.equals("6") && msg.equals("绑定登录"))
        {
            String account=obj.getString("account");
            String pass=obj.getString("pass");
            String openid=obj.getString("openid");
            String bindtype=obj.getString("bindtype");

            bindLogin(account,pass,openid,bindtype);

        }

    }

    private void bindLogin(String account,String pass,String openid,String bindtype)
    {
        XActivityindicator.create(mContext).show();

        XNetUtil.Handle(APPService.userOpenBD(openid,bindtype,account,pass), new XNetUtil.OnHttpResult<List<UserModel>>() {
            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onSuccess(List<UserModel> models) {

                if(models.size() > 0)
                {
                    XActivityindicator.hide();
                    UserModel user = models.get(0);
                    APPDataCache.User = user;
                    APPDataCache.User.save();
                    APPDataCache.land = 1;

                    int sex = 0;
                    int houseid = 0;

                    try
                    {
                        sex = Integer.parseInt(user.getSex());

                    }
                    catch (Exception e)
                    {
                        sex = 0;
                    }

                    try
                    {
                        houseid = Integer.parseInt(user.getHouseid());

                    }
                    catch (Exception e)
                    {
                        houseid = 0;
                    }

                    setAccount(user.getUid());

                    APPDataCache.User.registNotice();
                    APPDataCache.User.getUser();
                    APPDataCache.User.getMsgCount();

                    Intent intent = new Intent();
                    intent.setAction("com.servicedemo4");
                    intent.putExtra("getmeeage", "0");
                    XHtmlVC.this.sendBroadcast(intent);
                    Toast.makeText(XHtmlVC.this, "登陆成功", Toast.LENGTH_SHORT).show();

                    EventBus.getDefault().post(
                            new MyEventBus("LoginSuccess"));

                    finish();
                }


            }
        });
    }

    private void setAccount(String username) {
        PushServiceFactory.getCloudPushService().bindAccount(username, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                System.out.println("setAccount:onSuccess");
            }

            @Override
            public void onFailed(String s, String s1) {
                System.out.println("setAccount:onFailed");
            }
        });
    }

    private void getHFBGuize(String id)
    {
        XNetUtil.Handle(APPService.newsGetArticleInfo(id), new XNetUtil.OnHttpResult<List<NewsModel>>() {
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
                    web.loadDataWithBaseURL(null,infos , "text/html", "utf-8", null);

                }
            }
        });



    }



    @Override
    protected void setupData() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        clearWebViewCache();
        XNetUtil.APPPrintln("xhtml vc destrory !!!!!!");
    }

    @Subscribe
    public void getEventmsg(MyEventBus myEventBus) {
        if (myEventBus.getMsg().equals("LoginSuccess")) {
            finish();
        }
    }

}
