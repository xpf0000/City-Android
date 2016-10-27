package citycircle.com.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.ClipboardManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.MyViews.DragImageView;
import citycircle.com.R;
import citycircle.com.Utils.AsyncImageLoader;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by admins on 2015/11/21.
 */
public class PhotoNewsIn extends Activity implements View.OnClickListener {
    String url, urlinfo, id;
    String webtxt, description, path, titles,comurl,comstr;
    List<String> list = new ArrayList<>();
    List<String> imglist = new ArrayList<>();
    ViewPager viewpage;
    TextView page, comtent, title;
    DisplayMetrics dm;
    private ImageView[] mImageViews;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    ImageView share, back,collect;
    AsyncImageLoader ImageLoaders;
    Button collected;
    Button submit;
    PopupWindow popupWindow;
    View popView;
    EditText myviptxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photonewin);
        id = getIntent().getStringExtra("id");
        url = GlobalVariables.urlstr + "News.getPicArticle&id=" + id;
        intview();
        getWEbciew(0);
    }

    public void intview() {
        collect=(ImageView)findViewById(R.id.collect);
        collect.setOnClickListener(this);
        submit=(Button)findViewById(R.id.submit);
        submit.setOnClickListener(this);
        collected=(Button)findViewById(R.id.collected);
        viewpage = (ViewPager) findViewById(R.id.viewpager);
        page = (TextView) findViewById(R.id.page);
        comtent = (TextView) findViewById(R.id.content);
        title = (TextView) findViewById(R.id.title);
        share = (ImageView) findViewById(R.id.share);
        share.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        dm = new DisplayMetrics();
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(PhotoNewsIn.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options = ImageUtils.setnoOptions();
        collected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = PreferencesUtils.getInt(PhotoNewsIn.this, "land");
                if (a == 0) {
                    Intent intent = new Intent();
                    intent.setClass(PhotoNewsIn.this, Logn.class);
                    PhotoNewsIn.this.startActivity(intent);
                } else {
                    showpop();
                    popView.setVisibility(View.GONE);
                    myviptxt.setFocusable(true);
                    myviptxt.setFocusableInTouchMode(true);
                    myviptxt.requestFocus();
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask()
                                   {
                                       public void run()
                                       {
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
        viewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                if (position >= list.size()) {
//                    page.setText((1) + "/" + (imglist.size()));
//
//                } else {
                    page.setText((position + 1) + "/" + (imglist.size()));
//
//                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void setImageview() {
        ImageLoaders = new AsyncImageLoader();
        mImageViews = new ImageView[imglist.size()];
        for (int i = 0; i < mImageViews.length; i++) {
            final DragImageView dragImageView = new DragImageView(this);
            mImageViews[i] = dragImageView;
            dragImageView.setmActivity(this, dragImageView);// 注入Activity.
//            dragImageView.setImageResource(R.mipmap.ic_launcher);
            dragImageView.getgetViewTreeObserver();
            String url = imglist.get(i);
//            Bitmap bitmap=ImageLoader.loadImageSync(url);
//            dragImageView.setImageBitmap(bitmap);
            Bitmap bitmap = ImageLoaders.loadBitmap(dragImageView,
                    url, new AsyncImageLoader.ImageCallBack() {

                        @SuppressWarnings("deprecation")
                        @Override
                        public void imageLoad(ImageView imageView, Bitmap bitmap) {
                            dragImageView.setImageBitmap(bitmap);
                        }

                    });
            if (bitmap == null) {
                dragImageView.setImageResource(R.mipmap.nopic);
            } else {
                dragImageView.setImageBitmap(bitmap);
            }
//            ImageLoader.displayImage(url, dragImageView, options,
//                    animateFirstListener);
        }
        viewpage.setAdapter(new MyAdapter());
    }

    public void getWEbciew(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type==0){
                    urlinfo = httpRequest.doGet(url);
                    if (urlinfo.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }else {
                    comstr=httpRequest.doGet(comurl);
                    if (comstr.equals("网络超时")) {
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
                    getinfostr(urlinfo);
//                    Utils utils = new Utils();
//                    list = utils.getImageurl(webtxt, "lt=\"\" />");
//                    for (int i = 0; i < list.size(); i++) {
//                        String jieguo = webtxt.substring(webtxt.indexOf("src=\"") + 5, webtxt.indexOf("\" a"));
//
//                    }
                    setImageview();
                    title.setText(titles);
                    comtent.setText(description);
                    page.setText((1) + "/" + (imglist.size()));
                    path = imglist.get(0);
                    break;
                case 2:
                    Toast.makeText(PhotoNewsIn.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(PhotoNewsIn.this, "暂无内容", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    JSONObject jsonObject3 = JSON.parseObject(comstr);
                    JSONObject jsonObject4 = jsonObject3.getJSONObject("data");
                    int b = jsonObject4.getIntValue("code");
                    if (b == 0) {
                        Toast.makeText(PhotoNewsIn.this, "评论成功", Toast.LENGTH_SHORT).show();
                        popupWindow.dismiss();
                        View view = getWindow().peekDecorView();
                        if (view != null) {
                            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputmanger.hideSoftInputFromWindow(
                                    view.getWindowToken(), 0);
                        }
                        collected.setText("");
                    }else {
                        Toast.makeText(PhotoNewsIn.this, "评论失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    public void getinfostr(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                titles = jsonObject2.getString("title");
                description = jsonObject2.getString("description");
//                String strs=jsonObject2.getString("content");
                JSONArray jsonArray1 = jsonObject2.getJSONArray("content");
                for (int j = 0; j < jsonArray1.size(); j++) {
                    JSONObject jsonObject3=jsonArray1.getJSONObject(j);
                    String path = jsonObject3.getString("src");
                    imglist.add(path);
                }
            }
        } else {
            handler.sendEmptyMessage(3);
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
                        ClipboardManager clip = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                        clip.setText("http://192.168.2.113/city/pic_info.html?id="+id);
                        Toast.makeText(PhotoNewsIn.this,"已经复制到粘贴板",Toast.LENGTH_SHORT).show();
                    }
                };
                oks.setCustomerLogo(logo,logo,"复制链接",listener);
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(titles);
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                oks.setTitleUrl("http://192.168.2.113/city/pic_info.html?id=" + id);
                // text是分享文本，所有平台都需要这个字段
                oks.setText(description);
                oks.setImageUrl(GlobalVariables.imgurl + path);
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://192.168.2.113/city/pic_info.html?id=" + id);
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
                // site是分享此内容的网站名称，仅在QQ空间使用
                oks.setSite(getString(R.string.app_name));
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setSiteUrl("http://192.168.2.113/city/pic_info.html?id=" + id);

// 启动分享GUI
                oks.show(this);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.submit:
                Intent intents = new Intent();
                intents.setClass(PhotoNewsIn.this, CommentList.class);
                intents.putExtra("id", id);
                PhotoNewsIn.this.startActivity(intents);
                break;
            case R.id.collect:
                Intent intent=new Intent();
                intent.setClass(PhotoNewsIn.this,CommentList.class);
                intent.putExtra("id",id);
                PhotoNewsIn.this.startActivity(intent);
                break;
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
                    Toast.makeText(PhotoNewsIn.this, "内容不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    String username = PreferencesUtils.getString(PhotoNewsIn.this, "username");
                    try {
                        comurl = GlobalVariables.urlstr + "Comment.insert&did=" + id + "&username=" + username + "&content=" + URLEncoder.encode(myviptxt.getText().toString().trim(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    getWEbciew(1);
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
    /**
     * @author zhaopengpei
     */
    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mImageViews.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView(mImageViews[position]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mImageViews[position]);
            return mImageViews[position];
        }


    }

}
