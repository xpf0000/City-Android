package citycircle.com.card;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Activity.Logn;
import citycircle.com.Activity.ShopInfo;
import citycircle.com.Activity.VipCardConInfo;
import citycircle.com.JsonMordel.VipInfo;
import citycircle.com.MyViews.CallPhonePop;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;
import okhttp3.Call;

/**
 * Created by X on 2016/11/8.
 */

public class CardGetedInfo extends Activity implements View.OnClickListener {

    LinearLayout yu,jifen;
    CardView cardView;
    TextView shopinfo, cardtype, titile, callphone,
            adress, shengyu, info, number,shengyutitle,nowjifen;
    ImageView back, logo;
    VipInfo vipInfo;
    String url, username, id, shopid, addurl;
    List<VipInfo.DataBean.InfoBean> list = new ArrayList<VipInfo.DataBean.InfoBean>();
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    LinearLayout shengyulay;
    int a;
    Button btn_lq;
    ScrollView slay;
    CallPhonePop callPhonePop;
    WebView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardgetedinfo);
        a = PreferencesUtils.getInt(this, "land");
        id = getIntent().getStringExtra("id");
        username = PreferencesUtils.getString(this, "userid");
        url = GlobalVariables.urlstr + "Hyk.getArticleYLQ&id=" + id + "&uid=" + username;
        intview();
        slay.setVisibility(View.GONE);
        getjson();
    }

    private void intview() {
        callPhonePop = new CallPhonePop();
        slay=(ScrollView)findViewById(R.id.slay);
        content = (WebView) findViewById(R.id.content);
        content.setVerticalScrollBarEnabled(false); //垂直不显示
        content.getSettings().setJavaScriptEnabled(true);
        content.getSettings().getJavaScriptEnabled();
        content.setWebChromeClient(new WebChromeClient());
        btn_lq = (Button) findViewById(R.id.btn_lq);
        info = (TextView) findViewById(R.id.info);
        shengyulay = (LinearLayout) findViewById(R.id.shengyulay);
        shengyu = (TextView) findViewById(R.id.shengyu);
        cardView = (CardView) findViewById(R.id.cardview);
        number = (TextView) findViewById(R.id.number);
        shopinfo = (TextView) findViewById(R.id.shopinfo);
        cardtype = (TextView) findViewById(R.id.cardtype);
        titile = (TextView) findViewById(R.id.titile);
        shengyutitle = (TextView) findViewById(R.id.shengyutitle);
        nowjifen = (TextView) findViewById(R.id.nowjifen);
        callphone = (TextView) findViewById(R.id.callphone);
        callphone.setOnClickListener(this);

        yu = (LinearLayout) findViewById(R.id.vipcardinfo_yu);
        jifen = (LinearLayout) findViewById(R.id.vipcardinfo_jifen);

        adress = (TextView) findViewById(R.id.adress);
        back = (ImageView) findViewById(R.id.back);
        logo = (ImageView) findViewById(R.id.logo);
        shopinfo.setOnClickListener(this);
        btn_lq.setOnClickListener(this);
        back.setOnClickListener(this);
        shengyulay.setOnClickListener(this);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();


    }

    private void getjson() {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(CardGetedInfo.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                vipInfo = JSON.parseObject(response, VipInfo.class);
                list.clear();
                if (vipInfo.getData().getCode() == 0) {
                    list.addAll(vipInfo.getData().getInfo());
                    for (int i = 0; i < list.size(); i++) {
                        shopid = list.get(i).getShopid();
                        cardtype.setText(list.get(i).getType());
                        titile.setText(list.get(i).getShopname());
                        String strs="电话:" + list.get(i).getTel();
                        callphone.setText(strs);

                        adress.setText("地址:" + list.get(i).getAddress());
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
                                + list.get(i).getInfo() + "\r\n</body>" + "</html>";
                        content.loadDataWithBaseURL(null,infos , "text/html", "utf-8", null);

                        String color = list.get(i).getColor().replace("#","");
                        color = "#"+color;

                        try {
                            cardView.setCardBackgroundColor(Color.parseColor(color));
                        }catch (Exception e){
                            e.printStackTrace();
                            cardView.setCardBackgroundColor(Color.parseColor("#232323"));
                        }
                        options = ImageUtils.setCirclelmageOptions();
                        ImageLoader.displayImage(list.get(i).getLogo(), logo, options, animateFirstListener);

                        if(list.get(i).getType().equals("打折卡") || list.get(i).getType().equals("积分卡"))
                        {
                            btn_lq.setVisibility(View.GONE);
                        }
                        else
                        {
                            btn_lq.setVisibility(View.VISIBLE);
                        }

                        shengyulay.setVisibility(View.VISIBLE);

                        number.setText("NO."+list.get(i).getId());

                        nowjifen.setText(list.get(i).getJifen());

                        if (cardtype.getText().equals("打折卡")) {
                            shengyutitle.setText("当前折扣:");
                            String str = "" + list.get(i).getValues();
                            shengyu.setText(str);
                        } else if (cardtype.getText().equals("计次卡")) {
                            shengyutitle.setText("剩余次数:");
                            String str = "" + list.get(i).getValues();
                            shengyu.setText(str);
                        } else if (cardtype.getText().equals("充值卡")) {
                            String str = "" + list.get(i).getValues();
                            shengyu.setText(str);
                        } else {
                            yu.setVisibility(View.GONE);
                            String str = "" + list.get(i).getValues();
                            nowjifen.setText(str);
                        }
                    }
                    slay.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(CardGetedInfo.this, R.string.nomore, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getCard() {
        OkHttpUtils.get().url(addurl).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(CardGetedInfo.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = JSON.parseObject(response);
                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                if (jsonObject1.getIntValue("code") == 0) {
                    JSONObject jsonObject2=jsonObject1.getJSONObject("info");
                    id=jsonObject2.getString("id");
                    username=jsonObject2.getString("uid");
                    url = GlobalVariables.urlstr + "Hyk.getArticleYLQ&id=" + id + "&uid=" + username;
                    getjson();
                    Toast.makeText(CardGetedInfo.this, "领取成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.setAction("com.servicedemo4");
                    intent.putExtra("getmeeage", "12");
                    CardGetedInfo.this.sendBroadcast(intent);
                } else {
                    Toast.makeText(CardGetedInfo.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.shopinfo:
                Intent intent = new Intent();
                intent.putExtra("id", shopid);
                intent.putExtra("shopname", titile.getText());
                intent.setClass(CardGetedInfo.this, ShopInfo.class);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
            case R.id.btn_lq:
//                Toast.makeText(this,"领取",Toast.LENGTH_SHORT).show();
                int a = PreferencesUtils.getInt(CardGetedInfo.this, "land");
                if (a == 0) {
                    Intent intent1 = new Intent();
                    intent1.setClass(CardGetedInfo.this, Logn.class);
                    CardGetedInfo.this.startActivity(intent1);
                } else {

                    Intent intent1 = new Intent();
                    intent1.putExtra("id", shopid);
                    intent1.putExtra("sname", titile.getText());

                    if(cardtype.getText().toString().equals("充值卡"))
                    {
                        intent1.setClass(CardGetedInfo.this, CardDoCZ1.class);
                    }
                    else
                    {
                        intent1.setClass(CardGetedInfo.this, CardDoCZ2.class);
                    }

                    CardGetedInfo.this.startActivity(intent1);

                }

                break;
            case R.id.shengyulay:
                Intent intent1 = new Intent();
                intent1.putExtra("id",id);
                intent1.setClass(CardGetedInfo.this, XiaofeiRecord.class);
                CardGetedInfo.this.startActivity(intent1);
                break;
            case R.id.callphone:
                callPhonePop.showpop(CardGetedInfo.this, list.get(0).getTel());
                break;
        }
    }
}
