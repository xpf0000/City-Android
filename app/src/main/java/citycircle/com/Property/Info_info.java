package citycircle.com.Property;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.Toast;

import citycircle.com.R;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/29.
 */
public class Info_info extends Activity {
    ImageView back;
//    TextView title, content;
    String url, urlstr, id, username, uid;
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_info);
        id = getIntent().getStringExtra("id");
//        username = PreferencesUtils.getString(Information.this, "username");
//        uid = PreferencesUtils.getString(Information.this, "userid");
        username = PreferencesUtils.getString(Info_info.this,"username");;
        uid = PreferencesUtils.getString(Info_info.this, "userid");;
//        url = GlobalVariables.urlstr + "Wuye.getNewsContent&uid=" + uid + "&username=" + username + "&id=" + id;
        url="http://101.201.169.38/city/wuyeNotice.html?uid="+uid+"&username="+username+"&id="+id;
        intview();
//        getcntent();
    }

    private void intview() {
        webView=(WebView)findViewById(R.id.webview);
//        title = (TextView) findViewById(R.id.title);
//        content = (TextView) findViewById(R.id.content);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().getJavaScriptEnabled();
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl(url);
    }

    private void getcntent() {
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
//                    JSONObject jsonObject = JSON.parseObject(urlstr);
//                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                    int a = jsonObject.getIntValue("code");
//                    if (a == 0) {
//                        JSONArray jsonArray=jsonObject1.getJSONArray("info");
//                        for (int i=0;i<jsonArray.size();i++){
//                            JSONObject jsonObject2=jsonArray.getJSONObject(i);
//                            title.setText(jsonObject2.getString("title"));
//                            content.setText(jsonObject2.getString("content"));
//                        }
//
//                    } else {
//                        handler.sendEmptyMessage(3);
//                    }
                    break;
                case 2:
                    Toast.makeText(Info_info.this, R.string.intent_error, Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(Info_info.this, "公告已被删除或不存在！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
}
