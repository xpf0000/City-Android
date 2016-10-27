package citycircle.com.OA;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.OA.OAAdapter.CityNameMod;
import citycircle.com.R;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/6.
 */
public class TelMessage extends Activity implements View.OnClickListener {
    TextView fristw, name, number, qq, email, tel, adress;
    ImageView collect, callphone;
    String getAddress, getEmail, getId, getMobile, getQq, getTel, getTruename;
    ImageView messagetback;
    ArrayList<CityNameMod> arrayList = new ArrayList<CityNameMod>();
    CityNameMod cityNameMod;
    HashMap<String, Object> hashMap;
    String collectstr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.telmessinfo);
        getAddress = getIntent().getStringExtra("getAddress");
        getEmail = getIntent().getStringExtra("getEmail");
        getId = getIntent().getStringExtra("getId");
        getMobile = getIntent().getStringExtra("getMobile");
        getQq = getIntent().getStringExtra("getQq");
        getTel = getIntent().getStringExtra("getTel");
        getTruename = getIntent().getStringExtra("getTruename");
        collectstr = PreferencesUtils.getString(TelMessage.this, "collectphone");
        intview();
    }

    private void intview() {
        collect = (ImageView) findViewById(R.id.collect);
        collect.setOnClickListener(this);
        callphone = (ImageView) findViewById(R.id.callphone);
        callphone.setOnClickListener(this);
        messagetback = (ImageView) findViewById(R.id.messagetback);
        messagetback.setOnClickListener(this);
        fristw = (TextView) findViewById(R.id.fristw);
        name = (TextView) findViewById(R.id.name);
        number = (TextView) findViewById(R.id.number);
        qq = (TextView) findViewById(R.id.qq);
        email = (TextView) findViewById(R.id.email);
        tel = (TextView) findViewById(R.id.tel);
        adress = (TextView) findViewById(R.id.adress);
        fristw.setText(getTruename.substring(0, 1));
        name.setText(getTruename);
        number.setText(getMobile);
        qq.setText("QQ: " + getQq);
        email.setText("E-mail:" + getEmail);
        tel.setText("固话: " + getTel);
        adress.setText("住址: " + getAddress);
        if (iscollcet(collectstr)){
            collect.setImageResource(R.drawable.shou);
        }else {
            collect.setImageResource(R.drawable.ic_action_important);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.messagetback:
                finish();
                break;
            case R.id.callphone:
                if (getMobile.length() > 0) {
                    Uri uri = Uri.parse("tel:" + getMobile);
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    TelMessage.this.startActivity(intent);
                } else {
                    Toast.makeText(TelMessage.this, "电话为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.collect:
                collectstr = PreferencesUtils.getString(TelMessage.this, "collectphone");
                if (iscollcet(collectstr)){
                    remove(collectstr);
                    collect.setImageResource(R.drawable.ic_action_important);
                }else {
                    getcollect(collectstr);
                    collect.setImageResource(R.drawable.shou);
                }

                break;
        }
    }

    private void getcollect(String str) {
        if (str == null) {
            cityNameMod = new CityNameMod();
            cityNameMod.setId(getId);
            cityNameMod.setAddress(getAddress);
            cityNameMod.setTruename(getTruename);
            cityNameMod.setTel(getTel);
            cityNameMod.setQq(getQq);
            cityNameMod.setEmail(getEmail);
            cityNameMod.setMobile(getMobile);
            arrayList.add(cityNameMod);
            hashMap = new HashMap<>();
            hashMap.put("colllist", arrayList);
            String collstr = JSON.toJSONString(hashMap);
            PreferencesUtils.putString(TelMessage.this, "collectphone", collstr);

        } else {
            JSONObject jsonObject = JSON.parseObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("colllist");
            cityNameMod = new CityNameMod();
            cityNameMod.setId(getId);
            cityNameMod.setAddress(getAddress);
            cityNameMod.setTruename(getTruename);
            cityNameMod.setTel(getTel);
            cityNameMod.setQq(getQq);
            cityNameMod.setEmail(getEmail);
            cityNameMod.setMobile(getMobile);
            jsonArray.add(cityNameMod);
            PreferencesUtils.putString(TelMessage.this, "collectphone", jsonObject.toJSONString());
        }
    }

    private boolean iscollcet(String str) {
        boolean iscollect = false;
        if (str == null) {
            iscollect = false;
        } else {
            JSONObject jsonObject = JSON.parseObject(str);
            JSONArray jsonArray = jsonObject.getJSONArray("colllist");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                if (jsonObject1.getString("id").equals(getId)) {
                    iscollect = true;
                    break;
                }
            }
        }
        return iscollect;
    }
    private void remove(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONArray jsonArray = jsonObject.getJSONArray("colllist");
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            if (jsonObject1.getString("id").equals(getId)) {
                jsonArray.remove(i);
            }
        }
        PreferencesUtils.putString(TelMessage.this, "collectphone", jsonObject.toJSONString());
        String s=jsonObject.toJSONString();
        System.out.println(s);
    }

}
