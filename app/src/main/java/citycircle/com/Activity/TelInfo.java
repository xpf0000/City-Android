package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import citycircle.com.MyViews.CallPhonePop;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.ImageUtils;

/**
 * Created by admins on 2015/11/24.
 */
public class TelInfo extends Activity implements View.OnClickListener {
    ImageView shopimg, back;
    TextView number, adress, distance, title;
    Button callphone;
    String url, urlstr, id, imgstr, num, ur, adr, tit,durl;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    CallPhonePop callPhonePop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telinfo);
        id = getIntent().getStringExtra("id");
        url = GlobalVariables.urlstr + "Tel.getArticle&id=" + id;
        intview();
        getUnfo();
    }

    public void intview() {
        shopimg = (ImageView) findViewById(R.id.shopimg);
        title = (TextView) findViewById(R.id.title);
        number = (TextView) findViewById(R.id.number);
        adress = (TextView) findViewById(R.id.adress);
        distance = (TextView) findViewById(R.id.distance);
        callphone = (Button) findViewById(R.id.callphone);
        back = (ImageView) findViewById(R.id.back);
        callphone.setOnClickListener(this);
        back.setOnClickListener(this);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(TelInfo.this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
    }

    public void getUnfo() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
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
                    getinfo(urlstr);
                    title.setText(tit);
                    number.setText("电话："+num);
                    adress.setText("地址："+adr);
                    distance.setText("网址:"+durl);
                    options = ImageUtils.setnoOptions();
                    ImageLoader.displayImage(ur, shopimg, options,
                            animateFirstListener);
                    break;
                case 2:
                    Toast.makeText(TelInfo.this, "网络似乎有问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(TelInfo.this, "暂无内容", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void getinfo(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                tit = jsonObject2.getString("title");
                num = jsonObject2.getString("tel");
                adr = jsonObject2.getString("address");
                ur = jsonObject2.getString("url");
                durl=jsonObject2.getString("durl");
            }
        } else {
            handler.sendEmptyMessage(3);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.callphone:
//                callPhonePop=new CallPhonePop();
//                callPhonePop.showpop(TelInfo.this,num);
                Uri uri = Uri.parse("tel:" + num);
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                TelInfo.this.startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
