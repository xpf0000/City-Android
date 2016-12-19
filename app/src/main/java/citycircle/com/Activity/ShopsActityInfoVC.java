package citycircle.com.Activity;

import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import model.ActityModel;
import util.BaseActivity;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPService;
import static citycircle.com.MyAppService.LocationApplication.SW;

/**
 * Created by X on 2016/12/19.
 */

public class ShopsActityInfoVC extends BaseActivity {

    ImageView banner;
    TextView views;
    TextView name,time,tel,address;
    WebView webview;

    String id = "";

    @Override
    protected void setupUi() {
        setContentView(R.layout.shopsactity);
        setPageTitle("详情");

        id = getIntent().getStringExtra("id");

        webview=(WebView)findViewById(R.id.webview);
        webview.setVerticalScrollBarEnabled(false); //垂直不显示
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().getJavaScriptEnabled();
        webview.setWebChromeClient(new WebChromeClient());
        views=(TextView)findViewById(R.id.views);

        banner = (ImageView) findViewById(R.id.banner);

        name = (TextView) findViewById(R.id.name);
        time = (TextView) findViewById(R.id.time);
        tel = (TextView) findViewById(R.id.tel);
        address = (TextView) findViewById(R.id.address);

        ViewGroup.LayoutParams layoutParams = banner.getLayoutParams();

        int w = SW;
        int h = (int)(w*7.0/16.0);

        layoutParams.height = h;
        banner.setLayoutParams(layoutParams);

        getData();

    }


    private void getData()
    {
        XNetUtil.Handle(APPService.discountGetArticle(id), new XNetUtil.OnHttpResult<List<ActityModel>>() {
            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<ActityModel> models) {

                if(models.size() > 0)
                {
                    ActityModel m = models.get(0);

                    name.setText(m.getTitle());

                    DateUtils dateUtils = new DateUtils();
                    String start = dateUtils.getDateToStrings(Long.parseLong(m.getS_time()));
                    String end = dateUtils.getDateToStrings(Long.parseLong(m.getE_time()));

                    time.setText(start + "至" + end);
                    views.setText("浏览: "+m.getView());

                    tel.setText(m.getTel());
                    address.setText(m.getAddress());
                    ImageLoader.getInstance().displayImage(m.getUrl(), banner);

                    String info = "<html>\r\n\t"
                            + "<head>\r\n"
                            + "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\"/>"
                            + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0\" />"
                            + "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
                            + "<style>\r\n\t "
                            + "body {background-color: #FFFFFF}\r\n"
                            + "table {border-right:1px dashed #D2D2D2;border-bottom:1px dashed #D2D2D2} \r\n\t "
                            + "table td{border-left:1px dashed #D2D2D2;border-top:1px dashed #D2D2D2} \r\n\t"
                            + "img {width:100%}\r\n" + "</style>\r\n\t"
                            + "</head>\r\n" + "<body style=\"width:[width]\">\r\n"
                            + m.getContent() + "\r\n</body>" + "</html>";

                    //info = info.replace("#FFFFFF","#F0F0F0");

                    webview.loadDataWithBaseURL(null,info , "text/html", "utf-8", null);

                }

            }
        });
    }

    @Override
    protected void setupData() {

    }


}
