package citycircle.com.OA;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.robin.lazy.cache.CacheLoaderManager;

import citycircle.com.MyViews.MyDialog;
import citycircle.com.R;
import citycircle.com.Utils.Emailtest;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by admins on 2016/1/7.
 */
public class EdUserinfo extends Activity implements View.OnClickListener {
    String emails, qqs, numbers, adresss, url, urlstr, username, userid, tel;
    EditText dianhua, qq, email, zhuzhi;
    ImageView addmessages,addmessagetback,tijiao;
    Emailtest emailtest;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eduserlayout);
        dialog = MyDialog.createLoadingDialog(EdUserinfo.this, "正在操作...");
        emails = getIntent().getStringExtra("email");
        qqs = getIntent().getStringExtra("qq");
        numbers = getIntent().getStringExtra("number");
        adresss = getIntent().getStringExtra("adress");

        userid = APPDataCache.OAUser.getOauid();
        username = APPDataCache.OAUser.getOausername();

        intview();
    }

    private void intview() {
        addmessagetback=(ImageView)findViewById(R.id.addmessagetback);
        tijiao = (ImageView) findViewById(R.id.tijiao);
        tijiao.setOnClickListener(this);
        dianhua = (EditText) findViewById(R.id.dianhua);
        qq = (EditText) findViewById(R.id.qq);
        email = (EditText) findViewById(R.id.email);
        zhuzhi = (EditText) findViewById(R.id.zhuzhi);
        addmessagetback.setOnClickListener(this);
        dianhua.setText(numbers);
        qq.setText(qqs);
        email.setText(emails);
        zhuzhi.setText(adresss);
    }

    private void getUserinfo() {
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
                    JSONObject jsonObject= JSON.parseObject(urlstr);
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    if (jsonObject1.getIntValue("code")==0){
                        Toast.makeText(EdUserinfo.this, "修改成功", Toast.LENGTH_SHORT).show();

                        APPDataCache.OAUser.setOamobile(dianhua.getText().toString().trim());
                        APPDataCache.OAUser.setAddress(zhuzhi.getText().toString().trim());
                        APPDataCache.OAUser.setQq(qq.getText().toString().trim());
                        APPDataCache.OAUser.setEmail(email.getText().toString().trim());


                        dialog.dismiss();
                        finish();
                    }else {
                        dialog.dismiss();
                        Toast.makeText(EdUserinfo.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    dialog.dismiss();
                    Toast.makeText(EdUserinfo.this, "网络似乎有问题啦", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tijiao:
                if (dianhua.getText().toString().trim().length() == 0 || !emailtest.checkphone(dianhua.getText().toString().trim())) {
                    Toast.makeText(EdUserinfo.this, "请输入正确的电话号码", Toast.LENGTH_SHORT).show();

                } else if (qq.getText().toString().trim().length() == 0) {
                    Toast.makeText(EdUserinfo.this, "QQ不能为空", Toast.LENGTH_SHORT).show();
                } else if (email.getText().toString().trim().length() == 0 || !emailtest.checkEmail(email.getText().toString().trim())) {
                    Toast.makeText(EdUserinfo.this, "请输入正确的邮箱", Toast.LENGTH_SHORT).show();
                } else if (zhuzhi.getText().toString().trim().length() == 0) {
                    Toast.makeText(EdUserinfo.this, "住址不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                    tel = APPDataCache.OAUser.getOatel();
                    url = GlobalVariables.oaurlstr + "User.userEdit&username=" + username + "&uid=" + userid + "&tel=" + tel + "&mobile=" + dianhua.getText().toString() + "&qq=" + qq.getText().toString().trim() + "&email=" + email.getText().toString().trim() + "&address=" + zhuzhi.getText().toString().trim();
                    getUserinfo();
                }
                break;
            case R.id.addmessagetback:
                finish();
                break;
        }
    }
}
