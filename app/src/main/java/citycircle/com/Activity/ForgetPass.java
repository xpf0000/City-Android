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
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import citycircle.com.R;
import citycircle.com.Utils.Emailtest;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;

/**
 * Created by admins on 2015/12/4.
 */
public class ForgetPass extends Activity implements View.OnClickListener {
    String smurl, smurlstr, smyurl, smyanstr;
    EditText code, number;
    Button submit, getcode;
    ImageView back;
    Emailtest emtest;
    String numbers="0";
    private int recLen = 60;
    Timer timer = new Timer();
    TimerTask task;
    int type;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forpass);
        emtest = new Emailtest();
        intview();
        type=getIntent().getIntExtra("type",0);
        if (type==1){
            title.setText("注册");
        }
    }

    public void intview() {
        title=(TextView)findViewById(R.id.title);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        code = (EditText) findViewById(R.id.code);
        getcode = (Button) findViewById(R.id.getcode);
        getcode.setOnClickListener(this);
        number = (EditText) findViewById(R.id.number);
    }

    public void getStr(final int type) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                if (type == 0) {
                    HttpRequest httpRequest = new HttpRequest();
                    smurlstr = httpRequest.doGet(smurl);
                    if (smurlstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    HttpRequest httpRequest = new HttpRequest();
                    smyanstr = httpRequest.doGet(smyurl);
                    if (smyanstr.equals("网络超时")) {
                        handler.sendEmptyMessage(2);
                    } else {
                        handler.sendEmptyMessage(3);
                    }
                }

            }
        }.start();
    }
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
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    JSONObject jsonObject3 = JSON.parseObject(smurlstr);
                    JSONObject jsonObject2 = jsonObject3.getJSONObject("data");
                    int b = jsonObject2.getIntValue("code");
                    if (b == 0) {
                        Toast.makeText(ForgetPass.this, "验证码已发送", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ForgetPass.this, "验证码发送失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(ForgetPass.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    JSONObject jsonObject4 = JSON.parseObject(smurlstr);
                    JSONObject jsonObject5 = jsonObject4.getJSONObject("data");
                    int c = jsonObject5.getIntValue("code");
                    if (c == 0) {
                        Toast.makeText(ForgetPass.this, "验证成功", Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        if (type==1){
                            intent.putExtra("number",numbers);
                            intent.putExtra("code",code.getText().toString());
                            intent.setClass(ForgetPass.this,Register.class);
                            ForgetPass.this.startActivity(intent);
                            finish();
                        }else {
                            intent.putExtra("number",numbers);
                            intent.putExtra("code",code.getText().toString());
                            intent.setClass(ForgetPass.this,UpPasswords.class);
                            ForgetPass.this.startActivity(intent);
                            finish();
                        }

                    } else {
                        Toast.makeText(ForgetPass.this, "验证失败", Toast.LENGTH_SHORT).show();
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
                if (number.getText().toString().length() == 0){
                    Toast.makeText(ForgetPass.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                }else if (!emtest.checkphone(number.getText().toString().trim())){
                    Toast.makeText(ForgetPass.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }else {
                    if (!(numbers.equals(number.getText().toString().trim()))) {
                        Toast.makeText(ForgetPass.this, "与验证手机不符", Toast.LENGTH_SHORT).show();
                    } else {
                        smyurl = GlobalVariables.urlstr+"User.smsVerify&mobile=" + numbers + "&code=" + code.getText().toString();
                        getStr(2);
                    }
                }
                break;
            case R.id.getcode:
                if (number.getText().toString().length() == 0) {
                    Toast.makeText(ForgetPass.this, "请输入手机号", Toast.LENGTH_SHORT).show();
                } else if (!emtest.checkphone(number.getText().toString().trim())) {
                    Toast.makeText(ForgetPass.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                } else {
                    numbers = number.getText().toString().trim();
                    smurl = GlobalVariables.urlstr + "User.smsSend&mobile=" + number.getText().toString().trim() + "&type=1";
                    getStr(0);
                    gettime();
                }
                break;
        }
    }
}
