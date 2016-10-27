package citycircle.com.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2015/11/30.
 */
public class UpPassword extends Activity implements OnClickListener {
    EditText number, password,surepassword;
    Button submit;
    String url, urlstr;
    ImageView back;
    String smurl, smurlstr, smyurl, smyanstr;
    String username;
    TimerTask task;
    private int recLen = 60;
    Timer timer = new Timer();
    String mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uppassword);
        intview();
        username = PreferencesUtils.getString(UpPassword.this, "username");
        mobile=PreferencesUtils.getString(UpPassword.this, "mobile");
        url = GlobalVariables.urlstr + "User.updatePass2";
    }

    public void intview() {
        surepassword = (EditText) findViewById(R.id.surepassword);
        number = (EditText) findViewById(R.id.number);
        back = (ImageView) findViewById(R.id.back);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    public void getuser(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                if (type == 0) {
                    urlstr = httpRequest.doGet(url);
                    if (url.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } else if (type==2){
                    smyanstr= httpRequest.doGet(smyurl);
                    if (smyanstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(5);
                    }
                }else {
                    smurlstr = httpRequest.doGet(smurl);
                    if (smurlstr.equals("网络超时")) {
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
                    JSONObject jsonObject = JSON.parseObject(urlstr);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    if (jsonObject1.getIntValue("code") == 0) {
                        Toast.makeText(UpPassword.this, "修改成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(UpPassword.this, "修改失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(UpPassword.this, "网络似乎出问题了", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(UpPassword.this, "未知", Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    JSONObject jsonObject3 = JSON.parseObject(smurlstr);
                    JSONObject jsonObject2 = jsonObject3.getJSONObject("data");
                    int b = jsonObject2.getIntValue("code");
                    if (b == 0) {
                        Toast.makeText(UpPassword.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UpPassword.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 5:
                    JSONObject jsonObject4 = JSON.parseObject(smurlstr);
                    JSONObject jsonObject5 = jsonObject4.getJSONObject("data");
                    int c = jsonObject5.getIntValue("code");
                    if (c == 0) {
                        url = GlobalVariables.urlstr + "User.updatePass&mobile=" + mobile + "&password=" + password.getText().toString()+"&code="+number.getText().toString();
                        getuser(0);
                    } else {
                        Toast.makeText(UpPassword.this, "验证失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.submit:
//                if (name.getText().toString().length()==0){
//                    Toast.makeText(UpPassword.this,"请输入原密码",Toast.LENGTH_SHORT).show();
//                }else
                if (number.getText().toString().length() == 0) {
                    Toast.makeText(UpPassword.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().trim().length() == 0) {
                    Toast.makeText(UpPassword.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().trim().equals(surepassword.getText().toString().trim())){
                    Toast.makeText(UpPassword.this,"两次密码不一致",Toast.LENGTH_SHORT).show();
                }
                else {
                    url = GlobalVariables.urlstr + "User.updatePass2&mobile=" + mobile + "&oldpass=" + number.getText().toString()+"&newpass="+password.getText().toString();
//                    String username = PreferencesUtils.getString(UpPassword.this, "username");
//
                    getuser(0);
                }
                break;
        }
    }
}
