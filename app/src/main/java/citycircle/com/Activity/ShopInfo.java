package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.JsonMordel.ShopinfoMo;
import citycircle.com.MyViews.CallPhonePop;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.ImageUtils;
import okhttp3.Call;

/**
 * Created by admins on 2016/6/2.
 */
public class ShopInfo extends Activity implements View.OnClickListener {
    ImageView back, shopimg;
    TextView tell_phone, adress, title, vipcard, shophd;
    String url, id, shopname;
    ShopinfoMo shopinfoMo;
    List<ShopinfoMo.DataBean.InfoBean> list = new ArrayList<>();
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    CallPhonePop callPhonePop;
    WebView content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopinfo);
        id = getIntent().getStringExtra("id");
        shopname = getIntent().getStringExtra("shopname");
        url = GlobalVariables.urlstr + "hyk.getshopinfo&id=" + id;
        intview();
        getJson();
    }

    private void intview() {
        callPhonePop = new CallPhonePop();
        content=(WebView)findViewById(R.id.content) ;
        content.setVerticalScrollBarEnabled(false); //垂直不显示
        content.getSettings().setJavaScriptEnabled(true);
        content.getSettings().getJavaScriptEnabled();
        content.setWebChromeClient(new WebChromeClient());
        vipcard = (TextView) findViewById(R.id.vipcard);
        vipcard.setOnClickListener(this);
        shophd = (TextView) findViewById(R.id.shophd);
        shophd.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        back = (ImageView) findViewById(R.id.back);
        shopimg = (ImageView) findViewById(R.id.shopimg);
        tell_phone = (TextView) findViewById(R.id.tell_phone);
        adress = (TextView) findViewById(R.id.adress);
        back.setOnClickListener(this);
        tell_phone.setOnClickListener(this);
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        title.setText(shopname);
    }

    private void getJson() {
        OkHttpUtils.get().url(url).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e) {
                Toast.makeText(ShopInfo.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                shopinfoMo = JSON.parseObject(response, ShopinfoMo.class);
                if (shopinfoMo.getData().getCode() == 0) {
                    list.addAll(shopinfoMo.getData().getInfo());
                    for (int i = 0; i < list.size(); i++) {
                        String strs="电话:" + list.get(i).getTel();
                        SpannableStringBuilder stringBuilders = new SpannableStringBuilder(strs);
                        stringBuilders.setSpan(new ForegroundColorSpan(Color.parseColor("#21adfd")), 3, strs.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        tell_phone.setText(stringBuilders);
                        adress.setText("地址:" + list.get(i).getAddress());
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
                                + list.get(i).getInfo() + "\r\n</body>" + "</html>";
                        content.loadDataWithBaseURL(null,info , "text/html", "utf-8", null);
                        options = ImageUtils.setcenterOptions();
                        ImageLoader.displayImage(list.get(i).getLogo(), shopimg, options, animateFirstListener);
                    }
                } else {
                    Toast.makeText(ShopInfo.this, "获取商家信息失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.putExtra("id", id);
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.vipcard:
                intent.setClass(ShopInfo.this, ShopVipcard.class);
                ShopInfo.this.startActivity(intent);
                break;
            case R.id.shophd:
                intent.setClass(ShopInfo.this, ShopCam.class);
                ShopInfo.this.startActivity(intent);
                break;
            case R.id.tell_phone:
                callPhonePop.showpop(ShopInfo.this, list.get(0).getTel());
                break;
        }
    }
}
