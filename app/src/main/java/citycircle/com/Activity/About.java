package citycircle.com.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import citycircle.com.R;
import util.XAPPUtil;
import util.XNetUtil;

/**
 * Created by admins on 2015/12/7.
 */
public class About extends Activity {
    ImageView back;
    TextView txt;
    TextView versionname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        txt=(TextView)findViewById(R.id.txt);
        versionname=(TextView)findViewById(R.id.versionname);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(About.this,Statement.class);
                About.this.startActivity(intent);
            }
        });

        versionname.setText("怀府网 v"+XAPPUtil.getAppVersionName(this));
    }

    private int count = 0;
    public void doClick(View v)
    {
        count += 1;
        if(count == 5)
        {
            XNetUtil.debug = true;
        }
        else
        {
            XNetUtil.debug = false;
        }

    }
}
