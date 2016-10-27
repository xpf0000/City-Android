package citycircle.com.Activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.ClipboardManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.readystatesoftware.viewbadger.BadgeView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.Adapter.AboutAdapter;
import citycircle.com.MyViews.MyDialog;
import citycircle.com.MyViews.MyListView;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by admins on 2015/11/19.
 */
public class NewsInfoActivity extends Activity implements View.OnClickListener {
    WebView myview;
    String url, urlinfo, id;
    String webtxt, description, path, addurl, addstr, imgurl, adoutnews, aboutstr;
    ImageView share, back, collect, addimg, collects,comment;
    Button collected;
//    Button submit;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    LinearLayout add;
    MyListView aboutnews;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, Object>> collarray = new ArrayList<HashMap<String, Object>>();
    AboutAdapter adapter;
    PopupWindow popupWindow;
    View popView;
    EditText myviptxt;
    Handler handlers = new Handler();
    String username;
    Dialog dialog;
    BadgeView badge ;
    private View xCustomView;
    private FrameLayout video_fullView;// 全屏时视频加载view
    private WebChromeClient.CustomViewCallback xCustomViewCallback;
    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsinfo);
        dialog= MyDialog.createLoadingDialog(NewsInfoActivity.this, "加载中...");
        dialog.show();
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        add = (LinearLayout) findViewById(R.id.add);
        video_fullView = (FrameLayout) findViewById(R.id.video_fullView);
        aboutnews = (MyListView) findViewById(R.id.aboutnews);
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        addimg = (ImageView) findViewById(R.id.addimg);
        addimg.setOnClickListener(this);
        collected = (Button) findViewById(R.id.collected);
        collect = (ImageView) findViewById(R.id.collect);
        comment=(ImageView)findViewById(R.id.comment) ;
        badge = new BadgeView(NewsInfoActivity.this, comment);
        comment.setOnClickListener(this);
        collect.setOnClickListener(this);
//        submit = (Button) findViewById(R.id.submit);
//        submit.setOnClickListener(this);
        myview = (WebView) findViewById(R.id.webview);
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        collects = (ImageView) findViewById(R.id.collects);
        collects.setOnClickListener(this);
        myview.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings ws = myview.getSettings();
        ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
        // ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);// 排版适应屏幕

        ws.setUseWideViewPort(true);// 可任意比例缩放
        ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。

        ws.setSavePassword(true);
        ws.setSaveFormData(true);// 保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);// 启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);// 新加
        myview.setWebViewClient(new HelloWebViewClient());
        id = getIntent().getStringExtra("id");
        webtxt = getIntent().getStringExtra("title");
        description = getIntent().getStringExtra("description");
        path = getIntent().getStringExtra("url");
       try {
           JSONArray jsonArray = JSON.parseArray(path);
           for (int i = 0; i < jsonArray.size(); i++) {
               JSONObject jsonObject = jsonArray.getJSONObject(i);
               path = jsonObject.getString("url");
           }
       }catch (Exception e){

       }
//        url = GlobalVariables.urlstr + "News.getArticle&id=71";
        url = "http://101.201.169.38/city/news_info.php?id=" + id + "&type=1";
//        url="http://www.bilibili.com/video/av6150055/";
//        url="http://123.57.28.170/zxd/city/android.html";
        addurl = GlobalVariables.urlstr + "News.getArticle&id=" + id;
        myview.loadUrl(url);
        getcollext();
//        submit.setText("看评论");
        getWEbciew(1);
        myview.setWebChromeClient(new WebChromeClient() {
            private View xprogressvideo;

            public void onProgressChanged(WebView view, int progress) {

                if (progress == 100) {
//                    getWEbciew(1);
                    dialog.dismiss();
                }
            }
            // 播放网络视频时全屏会被调用的方法
            @Override
            public void onShowCustomView(View view, CustomViewCallback callback) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                myview.setVisibility(View.INVISIBLE);
                // 如果一个视图已经存在，那么立刻终止并新建一个
                if (xCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }
                video_fullView.addView(view);
                xCustomView = view;
                xCustomViewCallback = callback;
                video_fullView.setVisibility(View.VISIBLE);
            }

            // 视频播放退出全屏会被调用的
            @Override
            public void onHideCustomView() {
                if (xCustomView == null)// 不是全屏播放状态
                    return;

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                xCustomView.setVisibility(View.GONE);
                video_fullView.removeView(xCustomView);
                xCustomView = null;
                video_fullView.setVisibility(View.GONE);
                xCustomViewCallback.onCustomViewHidden();
                myview.setVisibility(View.VISIBLE);
            }

            // 视频加载时进程loading
//            @Override
//            public View getVideoLoadingProgressView() {
//                if (xprogressvideo == null) {
//                    LayoutInflater inflater = LayoutInflater
//                            .from(NewsInfoActivity.this);
//                    xprogressvideo = inflater.inflate(
//                            R.layout.video_loading_progress, null);
//                }
//                return xprogressvideo;
//            }

        });
        myview.addJavascriptInterface(new Object() {
            @JavascriptInterface
            public void runAndroidMethod(final String str) {
                handlers.post(new Runnable() {

                    @Override
                    public void run() {
                        String str1 = str;
//                        Toast.makeText(NewsInfoActivity.this, "测试调用java" + str1, Toast.LENGTH_LONG).show();
                        JSONObject jsonObject = JSON.parseObject(str);
                        int a = jsonObject.getIntValue("index");
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        ArrayList<HashMap<String, String>> parray = new ArrayList<HashMap<String, String>>();
                        HashMap<String, String> phashMap;
                        for (int i = 0; i < jsonArray.size(); i++) {
                            phashMap = new HashMap<>();
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            phashMap.put("path", jsonObject1.getString("url") == null ? "" : jsonObject1.getString("url"));
                            parray.add(phashMap);
                        }
                        GlobalVariables.parrays = parray;
                        Intent intent = new Intent();
                        intent.setClass(NewsInfoActivity.this, PhotoLook.class);
                        intent.putExtra("pos", a);
                        NewsInfoActivity.this.startActivity(intent);


                    }
                });

            }

        }, "android");
//        getWEbciew();
        collected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = PreferencesUtils.getInt(NewsInfoActivity.this, "land");
                if (a == 0) {
                    Intent intent = new Intent();
                    intent.setClass(NewsInfoActivity.this, Logn.class);
                    NewsInfoActivity.this.startActivity(intent);
                } else {
                    showpop();
                    popView.setVisibility(View.GONE);
                    myviptxt.setFocusable(true);
                    myviptxt.setFocusableInTouchMode(true);
                    myviptxt.requestFocus();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                                       public void run() {
                                           InputMethodManager inputManager =
                                                   (InputMethodManager) myviptxt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                           inputManager.showSoftInput(myviptxt, 0);
                                       }
                                   },
                            100);
                    popView.setVisibility(View.VISIBLE);
//                    myviptxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//                        @Override
//                        public void onFocusChange(View v, boolean hasFocus) {
//                            if (hasFocus){
//
//                            }else {
//                              popupWindow.dismiss();
//                            }
//                        }
//                    });
                }
            }
        });
        aboutnews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("id", array.get(position).get("id"));
                intent.putExtra("title", array.get(position).get("title"));
                intent.putExtra("description", array.get(position).get("description"));
                intent.putExtra("url", "");
                intent.setClass(NewsInfoActivity.this, NewsInfoActivity.class);
                NewsInfoActivity.this.startActivity(intent);
            }
        });
    }

    public void getWEbciew(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type == 1) {
                    addstr = httpRequest.doGet(addurl);
                    if (addstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(4);
                    }
                } else if (type == 2) {
                    aboutstr = httpRequest.doGet(adoutnews);
                    if (aboutstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(5);
                    }
                } else {
                    urlinfo = httpRequest.doGet(url);
                    if (urlinfo.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }
            }
        }.start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
//                    getinfostr(urlinfo);
//                    String check = "style=\"width:.*;\"";
//                    Pattern regex = Pattern.compile(check);
//                    Matcher matcher = regex.matcher(webtxt);
//                    while (matcher.find()) {
//                        webtxt="<html>\r\n\t"
//                                + "<head>\r\n"
//                                +"<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\"/>"
//                                +"<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0\" />"
//                                +"<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
//                                + "<style>\r\n\t "
//                                + "table {border-right:1px dashed #D2D2D2;border-bottom:1px dashed #D2D2D2} \r\n\t "
//                                + "table td{border-left:1px dashed #D2D2D2;border-top:1px dashed #D2D2D2} \r\n\t"
//                                +"img {width:100%; height: auto;}\r\n"
//                                + "</style>\r\n\t"
//                                + "</head>\r\n"
//                                + "<body style=\"width:[width]\">\r\n"+
//                                webtxt.replace(matcher.group(), "")
//                                +"\r\n</body>"
//                                + "</html>";
//                    }
                    JSONObject jsonObject3 = JSON.parseObject(urlinfo);
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("data");
                    int b = jsonObject4.getIntValue("code");
                    if (b == 0) {
                        Toast.makeText(NewsInfoActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        View view = getWindow().peekDecorView();
                        if (view != null) {
                            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputmanger.hideSoftInputFromWindow(
                                    view.getWindowToken(), 0);
                        }
                        getWEbciew(1);
//                        collected.setText("");
                    } else {
                        Toast.makeText(NewsInfoActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                    }
//                    myview.loadDataWithBaseURL(null, webtxt, "text/html", "utf-8", null);
                    break;
                case 2:
                    Toast.makeText(NewsInfoActivity.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(NewsInfoActivity.this, "暂无内容", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    JSONObject jsonObject = JSON.parseObject(addstr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    if (jsonObject1.getIntValue("code") == 0) {
                        JSONArray jsonArray = jsonObject1.getJSONArray("info");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                            imgurl = jsonObject2.getString("comment");
                        }
                    } else {

                    }
                    badge.setText(imgurl);
                    badge.setTextSize(10);
                    badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                    badge.show();
//                    submit.setText(imgurl + "评");
//                    add.setVisibility(View.VISIBLE);
//                    options = ImageUtils.setnoOptions();
//                    ImageLoader.displayImage(imgurl, addimg, options,
//                            animateFirstListener);
//                    getnews(aboutstr);
//                    setAdoutnews();
                    break;
                case 5:
                    JSONObject jsonObject2 = JSON.parseObject(aboutstr);
                    JSONObject jsonObject5 = jsonObject2.getJSONObject("data");
                    if (jsonObject5.getIntValue("code") == 0) {
                        collects.setImageResource(R.mipmap.news_collect_s12x);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("id", id);
                        collarray.add(hashMap);
                        hashMap = new HashMap<>();
                        hashMap.put("list", collarray);
                        String str = JSON.toJSONString(hashMap);
                        PreferencesUtils.putString(NewsInfoActivity.this, "collelist", str);
                        Toast.makeText(NewsInfoActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NewsInfoActivity.this, jsonObject5.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void getnews(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        if (jsonObject1.getIntValue("code") == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                hashMap = new HashMap<>();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                hashMap.put("id", jsonObject2.getString("id") == null ? "" : jsonObject2.getString("id"));
                hashMap.put("title", jsonObject2.getString("title") == null ? "" : jsonObject2.getString("title"));
                hashMap.put("create_time", jsonObject2.getString("create_time") == null ? "" : jsonObject2.getString("create_time"));
                hashMap.put("source", jsonObject2.getString("source") == null ? "" : jsonObject2.getString("source"));
                array.add(hashMap);
            }
        } else {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myview.loadUrl("https://www.baidu.com/");
    }

    public void setAdoutnews() {
        adapter = new AboutAdapter(array, NewsInfoActivity.this);
        aboutnews.setAdapter(adapter);
    }

    public void getinfostr(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                webtxt = jsonObject2.getString("content");
            }
        } else {
            handler.sendEmptyMessage(3);
        }
    }

    public void showpop() {
        popView = getLayoutInflater().inflate(
                R.layout.newcommentpop, null);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
        popupWindow.showAtLocation(popView,
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        myviptxt = (EditText) popView.findViewById(R.id.popedttxt);
        Button back = (Button) popView.findViewById(R.id.back);
        Button update = (Button) popView.findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (myviptxt.getText().toString().length() == 0) {
                    Toast.makeText(NewsInfoActivity.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String username = PreferencesUtils.getString(NewsInfoActivity.this, "username");
                    try {
                        url = GlobalVariables.urlstr + "Comment.insert&did=" + id + "&username=" + username + "&content=" + URLEncoder.encode(myviptxt.getText().toString().trim(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getWEbciew(0);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
    }

    private class HelloWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("news_info.php?")) {

                int a = url.indexOf("=") + 1;
                String id = url.substring(a, url.length());
                Intent intent = new Intent();
                intent.putExtra("id", id);
                intent.putExtra("title", "");
                intent.putExtra("description", "");
                intent.putExtra("url", "");
                intent.setClass(NewsInfoActivity.this, NewsInfoActivity.class);
                NewsInfoActivity.this.startActivity(intent);
            }

            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share:
                ShareSDK.initSDK(this);
                OnekeyShare oks = new OnekeyShare();
                Bitmap logo = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_share_copylink);
                View.OnClickListener listener = new View.OnClickListener() {
                    public void onClick(View v) {
                        ClipboardManager clip = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        clip.setText("http://101.201.169.38/city/news_info.php?id=" + id);
                        Toast.makeText(NewsInfoActivity.this, "已经复制到粘贴板", Toast.LENGTH_SHORT).show();
                    }
                };
                oks.setCustomerLogo(logo, logo, "复制链接", listener);
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                try {
                    if (webtxt.length() == 0) {
                        webtxt = myview.getTitle();
                    }
                } catch (Exception e) {

                }
                oks.setTitle(webtxt);
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                oks.setTitleUrl("http://wap.huaifuwang.com/city/news_info.php?id=" + id + "&type=0");
                // text是分享文本，所有平台都需要这个字段
//                oks.setText(description);
                oks.setImageUrl(path);
//                oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
                        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://wap.huaifuwang.com/city/news_info.php?id=" + id + "&type=0");
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
                // site是分享此内容的网站名称，仅在QQ空间使用
                oks.setSite(getString(R.string.app_name));
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setSiteUrl("http://wap.huaifuwang.com/city/news_info.php?id=" + id + "&type=0");

// 启动分享GUI
                oks.show(this);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.comment:
                Intent intent = new Intent();
                intent.setClass(NewsInfoActivity.this, CommentList.class);
                intent.putExtra("id", id);
                NewsInfoActivity.this.startActivity(intent);
                break;
            case R.id.collects:
                int a = PreferencesUtils.getInt(NewsInfoActivity.this, "land");
                if (a == 0) {
                    Intent intents = new Intent();
                    intents.setClass(NewsInfoActivity.this, Logn.class);
                    NewsInfoActivity.this.startActivity(intents);
                } else {
                    username = PreferencesUtils.getString(NewsInfoActivity.this, "username");
                    adoutnews = GlobalVariables.urlstr + "News.collectAdd&did=" + id + "&username=" + username;
                    getWEbciew(2);
                }
                break;
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getWEbciew(1);
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onPause() {
        super.onPause();
        myview.onPause();
        myview.pauseTimers();
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onResume() {
        super.onResume();
        super.onResume();
        myview.onResume();
        myview.resumeTimers();

        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    private void getcollext() {
        String str = PreferencesUtils.getString(NewsInfoActivity.this, "collelist");
        if (str == null) {

        } else {
            JSONObject jsonObject = JSON.parseObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            for (int i = 0; i < jsonArray.size(); i++) {
                HashMap<String, Object> hashMap = new HashMap<>();
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String ID=jsonObject1.getString("id") == null ? "" : jsonObject1.getString("id");
                    if (ID.equals(id)) {
                        collects.setImageResource(R.mipmap.news_collect_s12x);
                    } else {
                }
                hashMap.put("id", jsonObject1.getString("id")== null ? "" : jsonObject1.getString("id"));
                collarray.add(hashMap);
            }
        }
    }
}
