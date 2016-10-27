package citycircle.com.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import citycircle.com.R;
import citycircle.com.Utils.DateUtils;

/**
 * Created by admins on 2016/6/27.
 */
public class MessageContent extends Activity {
    TextView titile,titiles,time,content;
    ImageView back;
    String titi_le,times,contents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_content);
        titi_le=getIntent().getStringExtra("title");
        times=getIntent().getStringExtra("times");
        contents=getIntent().getStringExtra("contents");
        intview();

    }
    private void intview(){
        titile=(TextView)findViewById(R.id.titile);
        titiles=(TextView)findViewById(R.id.titiles);
        time=(TextView)findViewById(R.id.time);
        content=(TextView)findViewById(R.id.content);
        back=(ImageView)findViewById(R.id.back);
        titile.setText(titi_le);
        titiles.setText(titi_le);
        content.setText(contents);
        time.setText(DateUtils.getDateToStrings(Long.parseLong(times)));
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
