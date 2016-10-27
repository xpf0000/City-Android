package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.R;
import citycircle.com.Utils.Emailtest;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.MyhttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/12/11.
 */
public class GetPhone extends Activity implements View.OnClickListener{
    Button submit, getcode;
    ImageView back;
    EditText code, number, password;
    String numbesr, datastr, url, urlstr;
    String smurl, smurlstr;
    Emailtest emtest;
    TimerTask task;
    private int recLen = 60;
    Timer timer = new Timer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.getphone);
        emtest = new Emailtest();
        intview();
    }
    public void intview() {
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        code = (EditText) findViewById(R.id.code);
        getcode = (Button) findViewById(R.id.getcode);
        getcode.setOnClickListener(this);
        number = (EditText) findViewById(R.id.number);
        password = (EditText) findViewById(R.id.password);
    }
    public void getStr(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (type == 0) {
                    MyhttpRequest myhttp = new MyhttpRequest();
                    Object object = myhttp.request(url, datastr, "POST");
                    if (object == null) {
                        handler.sendEmptyMessage(2);
                    } else {
                        urlstr = object.toString();
                        handler.sendEmptyMessage(4);
                    }
                } else {
                    HttpRequest httpRequest = new HttpRequest();
                    smurlstr = httpRequest.doGet(smurl);
                    if (smurlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                }

            }
        }.start();
    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    JSONObject jsonObject3= JSON.parseObject(smurlstr);
                    JSONObject jsonObject1=jsonObject3.getJSONObject("data");
                    int b=jsonObject1.getIntValue("code");
                    if (b==0){
                        Toast.makeText(GetPhone.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(GetPhone.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    JSONObject jsonObject=JSON.parseObject(urlstr);
                    JSONObject jsonObject2=jsonObject.getJSONObject("data");
                    int c=jsonObject2.getIntValue("code");
                    if (c==0){
                        Toast.makeText(GetPhone.this, "绑定成功", Toast.LENGTH_SHORT).show();
                        PreferencesUtils.putString(GetPhone.this, "mobile", number.getText().toString());
                        PreferencesUtils.putInt(GetPhone.this, "land", 1);
                        Intent intent = new Intent();
                        intent.setAction("com.servicedemo4");
                        intent.putExtra("getmeeage", "0");
                        GetPhone.this.sendBroadcast(intent);
                        finish();
                    }else {
                        Toast.makeText(GetPhone.this, "绑定失败", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        }
    };
    public void gettime() {
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        recLen--;
                        getcode.setText(recLen + "后再获取");
                        if (recLen < 0) {
                            getcode.setText("获取验证码");
                            recLen = 60;
                            task.cancel();
                            getcode.setClickable(true);
                        }
                    }
                });
            }
        };
        timer.schedule(task, 1000, 1000);
    }
    @Override
    public void onClick(View v) {
switch (v.getId()){
    case R.id.back:
        finish();
        break;
    case R.id.submit:
        String codes = code.getText().toString();
        if (!(number.getText().toString().equals(numbesr))) {
            Toast.makeText(GetPhone.this, "手机号码与验证手机不符", Toast.LENGTH_SHORT).show();
        } else if (codes.length() == 0) {
            Toast.makeText(GetPhone.this, "请输入验证码！", Toast.LENGTH_SHORT).show();
        }
        if (password.getText().toString().trim().length() == 0) {
            Toast.makeText(GetPhone.this, "请输入密码！", Toast.LENGTH_SHORT).show();
        } else {
            //短信验证
//                    submitVerificationCode("86", numbesr, codes);
            String usernamenew = number.getText().toString();
            String passwords = password.getText().toString();
            url=GlobalVariables.urlstr+"User.openMobileAdd";
            String username= PreferencesUtils.getString(GetPhone.this,"username");
            datastr="&username="+username+"&mobile="+usernamenew+"&password="+passwords+"&code="+codes;
            getStr(0);
        }
        break;
    case R.id.getcode:
        numbesr = number.getText().toString();
        if (numbesr.length() == 0) {
            Toast.makeText(GetPhone.this, "请输入手机号", Toast.LENGTH_SHORT).show();
        } else if (!emtest.checkphone(numbesr)) {
            Toast.makeText(GetPhone.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
        } else {
//                    getVerificationCode("86", numbesr);
            smurl = GlobalVariables.urlstr + "User.smsSend&mobile=" + numbesr + "&type=1";
            getStr(1);
            gettime();
            getcode.setClickable(false);
        }
        break;
}
    }
}
