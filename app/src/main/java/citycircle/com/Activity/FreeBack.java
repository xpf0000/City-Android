package citycircle.com.Activity;

import android.app.Activity;
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

import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;

/**
 * Created by admins on 2015/12/7.
 */
public class FreeBack extends Activity {
    ImageView back;
    EditText feedback_num, feedback_txt;
    Button submit;
    String url, urlstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feeback);
        back = (ImageView) findViewById(R.id.back);
        submit = (Button) findViewById(R.id.submit);
        feedback_num = (EditText) findViewById(R.id.feedback_num);
        feedback_txt = (EditText) findViewById(R.id.feedback_txt);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedback_num.getText().toString().length() == 0) {
                    Toast.makeText(FreeBack.this, "请输入联系方式", Toast.LENGTH_SHORT).show();

                } else if (feedback_txt.getText().toString().length() == 0) {
                    Toast.makeText(FreeBack.this, "请输入反馈意见", Toast.LENGTH_SHORT).show();

                } else {
//                    Toast.makeText(FreeBack.this, "反馈成功！", Toast.LENGTH_SHORT).show();
                  url=  GlobalVariables.urlstr+"User.feedAdd&user="+feedback_num.getText().toString().trim()+"&content="+feedback_txt.getText().toString().trim();
                    sendfee();
                }
            }
        });
    }

    public void sendfee() {
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
                        Toast.makeText(FreeBack.this, "提交成功,谢谢反馈", Toast.LENGTH_SHORT).show();
                        finish();
                    }else {
                        Toast.makeText(FreeBack.this, "提交失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    Toast.makeText(FreeBack.this, "网络超时", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    break;
            }
        }
    };
}
