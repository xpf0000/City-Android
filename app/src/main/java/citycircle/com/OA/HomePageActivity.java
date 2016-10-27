package citycircle.com.OA;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import citycircle.com.R;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/6.
 */
public class HomePageActivity extends Activity implements View.OnClickListener {
    TextView messages, DOC, meeting, phonenumber,register;
    ImageView loginback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.oahome);
        intview();
    }

    void intview() {
        register=(TextView)findViewById(R.id.register);
        register.setOnClickListener(this);
        loginback = (ImageView) findViewById(R.id.loginback);
        loginback.setOnClickListener(this);
        messages = (TextView) findViewById(R.id.messages);
        DOC = (TextView) findViewById(R.id.DOC);
        phonenumber = (TextView) findViewById(R.id.phonenumber);
        meeting = (TextView) findViewById(R.id.meeting);
        messages.setOnClickListener(this);
        DOC.setOnClickListener(this);
        meeting.setOnClickListener(this);
        phonenumber.setOnClickListener(this);
        loginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        int a = PreferencesUtils.getInt(HomePageActivity.this, "oaland");
        if (a == 0) {
            intent.setClass(HomePageActivity.this, LandActivity.class);
            HomePageActivity.this.startActivity(intent);
        } else {
            switch (v.getId()) {
                case R.id.messages:
                    intent.setClass(HomePageActivity.this, MessageActivity.class);
                    HomePageActivity.this.startActivity(intent);
                    break;
                case R.id.phonenumber:
                    intent.setClass(HomePageActivity.this, ContactsActivity.class);
                    HomePageActivity.this.startActivity(intent);
                    break;
                case R.id.meeting:
                    intent.setClass(HomePageActivity.this, OaUserinfo.class);
                    HomePageActivity.this.startActivity(intent);
                    break;
                case R.id.DOC:
                    intent.setClass(HomePageActivity.this, DocunmentActivity.class);
                    HomePageActivity.this.startActivity(intent);
                    break;
                case R.id.register:
                    intent.setClass(HomePageActivity.this, Documents.class);
                    HomePageActivity.this.startActivity(intent);
                    break;
            }
        }
    }
}
