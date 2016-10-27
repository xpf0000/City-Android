package citycircle.com.OA;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import citycircle.com.R;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/7.
 */
public class OaUserinfo extends Activity implements View.OnClickListener {
    TextView adress, email, qq, number, bumen, sex, name;
    String adresss, emails, qqs, numbers, bumens, names;
    int sexs;
    LinearLayout tellay,qqlay,emaillay,adresslay,uopass;
    Button logext;
//    PushAgent mPushAgent;
    ImageView messagetback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oauserinfo);
        intview();
//        mPushAgent = PushAgent.getInstance(this);
//        mPushAgent.enable();
//        PushAgent.getInstance(this).onAppStart();
    }

    private void intview() {
        uopass=(LinearLayout)findViewById(R.id.uopass);
        uopass.setOnClickListener(this);
        messagetback=(ImageView)findViewById(R.id.messagetback);
        messagetback.setOnClickListener(this);
        logext = (Button) findViewById(R.id.logext);
        tellay = (LinearLayout) findViewById(R.id.tellay);
        qqlay = (LinearLayout) findViewById(R.id.qqlay);
        emaillay = (LinearLayout) findViewById(R.id.emaillay);
        adresslay = (LinearLayout) findViewById(R.id.adresslay);
        adress = (TextView) findViewById(R.id.adress);
        email = (TextView) findViewById(R.id.email);
        qq = (TextView) findViewById(R.id.qq);
        number = (TextView) findViewById(R.id.number);
        bumen = (TextView) findViewById(R.id.bumen);
        sex = (TextView) findViewById(R.id.sex);
        name = (TextView) findViewById(R.id.name);
        getUserinfo();
        logext.setOnClickListener(this);
        tellay.setOnClickListener(this);
        qqlay.setOnClickListener(this);
        emaillay.setOnClickListener(this);
        adresslay.setOnClickListener(this);
    }

    private void getUserinfo() {
        adresss = PreferencesUtils.getString(OaUserinfo.this, "address");
        emails = PreferencesUtils.getString(OaUserinfo.this, "email");
        qqs = PreferencesUtils.getString(OaUserinfo.this, "qq");
        numbers = PreferencesUtils.getString(OaUserinfo.this, "oamobile");
        bumens = PreferencesUtils.getString(OaUserinfo.this, "bm");
        sexs = PreferencesUtils.getInt(OaUserinfo.this, "oasex");
        names = PreferencesUtils.getString(OaUserinfo.this, "oatruename");
        adress.setText(adresss);
        email.setText(emails);
        qq.setText(qqs);
        number.setText(numbers);
        bumen.setText(bumens);
        if (sexs == 0) {
            sex.setText("女");
        } else {
            sex.setText("男");
        }
        name.setText(names);
    }

    @Override
    public void onClick(View v) {
        Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.logext:
                final String[] tags;
                String jgid=PreferencesUtils.getString(OaUserinfo.this,"jgid");
                String dwid=PreferencesUtils.getString(OaUserinfo.this,"dwid");
                String bmid=PreferencesUtils.getString(OaUserinfo.this,"bmid");
                String uid=PreferencesUtils.getString(OaUserinfo.this,"uid");
                tags = new String[] { jgid, dwid,
                        bmid,uid };
//                settages(tags);
                PreferencesUtils.putInt(OaUserinfo.this,"oaland",0);
                intent.putExtra("type",0);
                intent.setClass(OaUserinfo.this,LandActivity.class);
                OaUserinfo.this.startActivity(intent);
                finish();
                break;
            case R.id.messagetback:
                finish();
                break;
            case R.id.emaillay:
                intent.putExtra("email",emails);
                intent.putExtra("qq",qqs);
                intent.putExtra("number",numbers);
                intent.putExtra("adress",adresss);
                intent.setClass(OaUserinfo.this, EdUserinfo.class);
                OaUserinfo.this.startActivity(intent);
                break;
            case R.id.adresslay:
                intent.putExtra("email",emails);
                intent.putExtra("qq",qqs);
                intent.putExtra("number",numbers);
                intent.putExtra("adress",adresss);
                intent.setClass(OaUserinfo.this,EdUserinfo.class);
                OaUserinfo.this.startActivity(intent);
                break;
            case R.id.qqlay:
                intent.putExtra("email",emails);
                intent.putExtra("qq",qqs);
                intent.putExtra("number",numbers);
                intent.putExtra("adress",adresss);
                intent.setClass(OaUserinfo.this,EdUserinfo.class);
                OaUserinfo.this.startActivity(intent);
                break;
            case R.id.tellay:
                intent.putExtra("email",emails);
                intent.putExtra("qq",qqs);
                intent.putExtra("number",numbers);
                intent.putExtra("adress",adresss);
                intent.setClass(OaUserinfo.this,EdUserinfo.class);
                OaUserinfo.this.startActivity(intent);
                break;
            case R.id.uopass:
                intent.setClass(OaUserinfo.this,UodatePassword.class);
                OaUserinfo.this.startActivity(intent);
                break;
        }
    }
//    void settages(final String[] tags) {
//        new Thread() {
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                super.run();
//                TagManager.Result result = null;
//                try {
//                    mPushAgent.getTagManager().reset().toString();
////                    result = mPushAgent.getTagManager().reset();
//                    System.out.println(result);
//                } catch (Exception e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();
//
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getUserinfo();
    }
}
