package citycircle.com.Property;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import citycircle.com.R;

/**
 * Created by admins on 2016/3/22.
 */
public class Messagecontent extends Activity {
    TextView textView;
    String content;
    ImageView loginback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messagecontent);
        content=getIntent().getStringExtra("content");
        textView=(TextView)findViewById(R.id.textView);
        textView.setText(content);
        loginback=(ImageView)findViewById(R.id.loginback);
        loginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
