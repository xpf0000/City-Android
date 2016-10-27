package citycircle.com.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by admins on 2015/11/21.
 */
public class DiscountInfo extends Activity implements View.OnClickListener {

    String url, urlinfo, id;
    String imgurl, name, time, tel, adress, addurl, addinfo, username,view,content;
    DateUtils dateUtils;
    ImageView back, saimg, shares, collect;
    TextView names, times, tels, adresss,views,titile,hd,hd2;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
   WebView webview;
    int type;
    @Override
    @SuppressLint({"SetJavaScriptEnabled", "InlinedApi"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discount);
        id = getIntent().getStringExtra("id");
        type=getIntent().getIntExtra("type",0);
        username = PreferencesUtils.getString(DiscountInfo.this, "username");
        dateUtils = new DateUtils();
        url = GlobalVariables.urlstr + "Discount.getArticle&id=" + id;
        addurl = GlobalVariables.urlstr + "Discount.collectAdd&did=" + id + "&username=" + username;
        intview();
        getInfo(0);
    }

    public void intview() {
        titile=(TextView)findViewById(R.id.titile);
        if (type!=0){
            titile.setText("活动详情");
        }
        webview=(WebView)findViewById(R.id.webview);
        webview.setVerticalScrollBarEnabled(false); //垂直不显示
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().getJavaScriptEnabled();
        webview.setWebChromeClient(new WebChromeClient());
        views=(TextView)findViewById(R.id.views);
        shares = (ImageView) findViewById(R.id.shares);
        shares.setOnClickListener(this);
        collect = (ImageView) findViewById(R.id.collects);
        collect.setOnClickListener(this);
        names = (TextView) findViewById(R.id.name);
        hd = (TextView) findViewById(R.id.hd);
        hd2 = (TextView) findViewById(R.id.hd2);
        times = (TextView) findViewById(R.id.time);
        tels = (TextView) findViewById(R.id.tel);
        adresss = (TextView) findViewById(R.id.adress);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        saimg = (ImageView) findViewById(R.id.saimg);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(DiscountInfo.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }

    public void getInfo(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type == 0) {
                    urlinfo = httpRequest.doGet(url);
                    if (urlinfo.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    addinfo = httpRequest.doGet(addurl);
                    if (addinfo.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(4);
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
                    setUrlinfo(urlinfo);
                    if (type!=0){
                        hd.setText("活动:");
                        hd2.setText("活动信息");
                        times.setText("时间: " + time);
                        tels.setText("电话: " + tel);
                        adresss.setText("地址: " + adress);
                    }else {
                        names.setText(" " +name);
                        times.setText("报名时间: " + time);
                        tels.setText("联系电话: " + tel);
                        adresss.setText("联系地址: " + adress);
                    }
                    views.setText("浏览量: "+view);
                    options = ImageUtils.setnoOptions();
                    ImageLoader.displayImage(imgurl, saimg, options,
                            animateFirstListener);
                    if (type!=0){
                        String info = "<html>\r\n\t"
                                + "<head>\r\n"
                                + "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\"/>"
                                + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0\" />"
                                + "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
                                + "<style>\r\n\t "
                                + "table {border-right:1px dashed #D2D2D2;border-bottom:1px dashed #D2D2D2} \r\n\t "
                                + "table td{border-left:1px dashed #D2D2D2;border-top:1px dashed #D2D2D2} \r\n\t"
                                + "img {width:100%}\r\n" + "</style>\r\n\t"
                                + "</head>\r\n" + "<body style=\"width:[width]\">\r\n"
                                + content + "\r\n</body>" + "</html>";
                        webview.loadDataWithBaseURL(null,info , "text/html", "utf-8", null);
                    }else {
                        webview.loadUrl("http://101.201.169.38/city/dis_info_info.php?id=" + id);
                    }
                    break;
                case 2:
                    Toast.makeText(DiscountInfo.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(DiscountInfo.this, "暂无信息", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    JSONObject jsonObject = JSON.parseObject(addinfo);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    int a = jsonObject1.getIntValue("code");
                    if (a == 0) {
                        Toast.makeText(DiscountInfo.this, "收藏成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DiscountInfo.this, "收藏失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void setUrlinfo(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                imgurl = jsonObject2.getString("url");
                name = jsonObject2.getString("title");
                tel = jsonObject2.getString("tel");
                adress = jsonObject2.getString("address");
                view=jsonObject2.getString("view");
                content=jsonObject2.getString("content");
                String start = dateUtils.getDateToStrings(jsonObject2.getLongValue("s_time"));
                String end = dateUtils.getDateToStrings(jsonObject2.getLongValue("e_time"));
                time = start + "至" + end;
            }

        } else {
            handler.sendEmptyMessage(3);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.shares:
                ShareSDK.initSDK(this);
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(name);
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                oks.setTitleUrl("http://123.56.136.21/zxd/city/news_info.php?id=" + id + "&type=0");
                // text是分享文本，所有平台都需要这个字段
                oks.setText(name);
                oks.setImageUrl(imgurl);
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://123.56.136.21/zxd/city/news_info.php?id=" + id + "&type=0");
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
                // site是分享此内容的网站名称，仅在QQ空间使用
                oks.setSite(getString(R.string.app_name));
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setSiteUrl("http://123.56.136.21/zxd/city/news_info.php?id=" + id + "&type=0");

// 启动分享GUI
                oks.show(this);
                break;
            case R.id.collects:
                int a=PreferencesUtils.getInt(DiscountInfo.this,"land");
                if (a==0){
                    Intent intent=new Intent();
                    intent.setClass(DiscountInfo.this,Logn.class);
                    DiscountInfo.this.startActivity(intent);
                }else {
                    getInfo(1);
                }

                break;
        }
    }
}
