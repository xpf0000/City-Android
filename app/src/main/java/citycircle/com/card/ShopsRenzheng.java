package citycircle.com.card;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.List;

import citycircle.com.R;
import model.RenzhengModel;
import model.YouhuiquanModel;
import util.BaseActivity;
import util.XNetUtil;

import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/8.
 */

public class ShopsRenzheng extends BaseActivity {

    private WebView content;
    private TextView time;
    private String id;

    @Override
    protected void setupUi() {
        setContentView(R.layout.renzhenginfo);
        setPageTitle("认证信息");

        id = getIntent().getStringExtra("id");

        time=(TextView)findViewById(R.id.renzheng_time) ;
        content=(WebView)findViewById(R.id.renzheng_info) ;
        content.setVerticalScrollBarEnabled(false); //垂直不显示
        content.getSettings().setJavaScriptEnabled(true);
        content.getSettings().getJavaScriptEnabled();
        content.setWebChromeClient(new WebChromeClient());

        getData();

    }

    private void getData()
    {
        XNetUtil.Handle(APPService.hykGetShopVIPInfo(id), new XNetUtil.OnHttpResult<List<RenzhengModel>>() {
            @Override
            public void onError(Throwable e) {
                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<RenzhengModel> models) {

                if(models.size() > 0)
                {
                    RenzhengModel m = models.get(0);

                    time.setText("于"+m.getVip_time()+"完成怀府网认证，每年怀府网及第三方审核机构都将对其资料进行审核。");

                    String info = "<html>\r\n\t"
                            + "<head>\r\n"
                            + "<meta http-equiv=\"Content-Type\" content=\"application/xhtml+xml; charset=utf-8\"/>"
                            + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0\" />"
                            + "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
                            + "<style>\r\n\t "
                            + "body {background-color: #F0F0F0}\r\n"
                            + "table {border-right:1px dashed #D2D2D2;border-bottom:1px dashed #D2D2D2} \r\n\t "
                            + "table td{border-left:1px dashed #D2D2D2;border-top:1px dashed #D2D2D2} \r\n\t"
                            + "img {width:100%}\r\n" + "</style>\r\n\t"
                            + "</head>\r\n" + "<body style=\"width:[width]\">\r\n"
                            + m.getContent() + "\r\n</body>" + "</html>";

                            info = info.replace("#FFFFFF","#F0F0F0");

                        content.loadDataWithBaseURL(null,info , "text/html", "utf-8", null);

                }

            }
        });
    }

    @Override
    protected void setupData() {

    }
}
