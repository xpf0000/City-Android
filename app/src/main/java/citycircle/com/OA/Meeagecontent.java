package citycircle.com.OA;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/7.
 */
public class Meeagecontent extends Activity {
    TextView meeagetitle, meeagepop, meeagetime, messageco;
    ImageView messagecback;
    String id, url, str;
    String meeagetitlec, meeagepopc, meeagetimec, messagecoc,username,bid;
    DateUtils dateUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meeagecontent);
        id = getIntent().getStringExtra("id");
        intview();
        dateUtils=new DateUtils();
        username= PreferencesUtils.getString(Meeagecontent.this, "oausername");
        bid = PreferencesUtils.getString(Meeagecontent.this, "oauid");
        url= GlobalVariables.oaurlstr+"News.getArticle&id="+id+"&bid="+bid+"&username="+username;
        getmessagecon(url,1);
    }
    private void intview(){
        meeagetitle = (TextView) findViewById(R.id.meeagetitle);
        meeagepop = (TextView) findViewById(R.id.meeagepop);
        meeagetime = (TextView) findViewById(R.id.meeagetime);
        messageco = (TextView) findViewById(R.id.messageco);
        messagecback = (ImageView) findViewById(R.id.messagecback);
        messagecback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();

            }
        });
    }
    void getmessagecon(final String url, final int what) {
        new Thread() {
            public void run() {
                HttpRequest httpRequest = new HttpRequest();
                str = httpRequest.doGet(url);
                if (str.equals("网络超时")) {
                    handler.sendEmptyMessage(3);
                } else {
                    handler.sendEmptyMessage(what);
                }
            };
        }.start();

    }
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    getcontent(str);
                    meeagepop.setText("发布人：" + meeagepopc);
                    meeagetime.setText("发布时间：" + meeagetimec);
                    meeagetitle.setText(meeagetitlec);
                    messageco.setText(messagecoc);
                    break;
                case 2:

                    break;
                case 3:
                    Toast.makeText(Meeagecontent.this, "网络超时", Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }
        };
    };
    void getcontent(String str) {
        JSONObject jsonObject = JSONObject.parseObject(str);
        JSONObject jsonObject1=jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray=jsonObject1.getJSONArray("info");
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                meeagetitlec = jsonObject2.getString("title");
                messagecoc = jsonObject2.getString("content");
                meeagepopc=jsonObject2.getString("truename");
                meeagetimec=dateUtils.getDateToStringsss(jsonObject2.getLongValue("create_time"));
            }

        }

        else {

        }

    }
}
