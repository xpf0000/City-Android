package citycircle.com.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;

import citycircle.com.R;

/**
 * Created by admins on 2016/8/12.
 */
public class WebViews extends Activity {
    WebView webs;
    String url;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web);
        webs=(WebView)findViewById(R.id.webs);
        back=(ImageView)findViewById(R.id.back) ;
        url=getIntent().getStringExtra("url");
        webs.setVerticalScrollBarEnabled(false); //垂直不显示
        webs.getSettings().setJavaScriptEnabled(true);
        webs.getSettings().getJavaScriptEnabled();
        webs.setWebChromeClient(new WebChromeClient());
        webs.loadUrl(url);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
