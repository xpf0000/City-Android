package citycircle.com.Property;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import citycircle.com.R;
import citycircle.com.Utils.DateUtils;

/**
 * Created by admins on 2016/2/24.
 */
public class PayMoney extends Activity implements View.OnClickListener {
    ImageView loginback, typeimg;
    TextView sbiao, bbiao, dian, time, pmoney, smoney, ymoney,ymoneyss;
    Button submit;
    String type, yumoney, snumber, bnumber, create_time, status, unumber, ymoneys, smoneys,ymoneysss;
    LinearLayout lay2,lay3,lay4,lay9;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paymoney);
        type = getIntent().getStringExtra("type");
        yumoney = getIntent().getStringExtra("yumoney");
        snumber = getIntent().getStringExtra("snumber");
        bnumber = getIntent().getStringExtra("bnumber");
        unumber = getIntent().getStringExtra("unumber");
        ymoneys = getIntent().getStringExtra("ymoney");
        smoneys = getIntent().getStringExtra("smoney");
        ymoneysss=getIntent().getStringExtra("price");
        create_time = getIntent().getStringExtra("create_time");
        status = getIntent().getStringExtra("status");
        intview();
    }

    private void intview() {
        lay2=(LinearLayout)findViewById(R.id.lay2);
        lay3=(LinearLayout)findViewById(R.id.lay3);
        lay4=(LinearLayout)findViewById(R.id.lay4);
        lay9=(LinearLayout)findViewById(R.id.lay9);
        loginback = (ImageView) findViewById(R.id.loginback);
//        typeimg=(ImageView)findViewById(R.id.typeimg);
        sbiao = (TextView) findViewById(R.id.sbiao);
        bbiao = (TextView) findViewById(R.id.bbiao);
        time = (TextView) findViewById(R.id.time);
        dian = (TextView) findViewById(R.id.dian);
        pmoney = (TextView) findViewById(R.id.pmoney);
        smoney = (TextView) findViewById(R.id.smoney);
        ymoney = (TextView) findViewById(R.id.ymoney);
        ymoneyss = (TextView) findViewById(R.id.ymoneys);
        ymoneyss.setText(ymoneysss);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        loginback.setOnClickListener(this);
        sbiao.setText(snumber);
        bbiao.setText(bnumber);
        dian.setText(unumber);
        pmoney.setText(ymoneys);
        smoney.setText(smoneys);
        ymoney.setText(yumoney);
        if (type.equals("1") || type.equals("4")) {
            lay2.setVisibility(View.GONE);
            lay3.setVisibility(View.GONE);
            lay4.setVisibility(View.GONE);
            lay9.setVisibility(View.GONE);
        }

        time.setText(DateUtils.getDateToStrings(Long.parseLong(create_time)));
        if (status.equals("1")) {
            submit.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginback:
                finish();
                break;
            case R.id.submit:
                break;
        }
    }
}
