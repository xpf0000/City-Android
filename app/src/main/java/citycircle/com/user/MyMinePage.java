package citycircle.com.user;

import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.Activity.Newphoto;
import citycircle.com.R;
import citycircle.com.hfb.GWManageRight;
import citycircle.com.hfb.YGManageLeft;
import util.BaseActivity;
import util.XHorizontalMain;
import util.XHorizontalMenu;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;

/**
 * Created by X on 2016/11/7.
 */

public class MyMinePage extends BaseActivity {

    private XHorizontalMenu menu;
    private XHorizontalMain main;
    private List<XHorizontalMenu.XHorizontalModel> dataArr;

    private MyMinePageLeft left = new MyMinePageLeft();
    private MyMinePageRight right = new MyMinePageRight();

    private RoundedImageView header;
    private TextView name;
    private TextView tel;

    DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.mipmap.home_head)
            .showImageOnFail(R.mipmap.home_head)
            .build();

    @Override
    protected void setupUi() {
        setContentView(R.layout.myminepage);
        setPageTitle("我的主页");

        header = (RoundedImageView)findViewById(R.id.myminepage_head);
        name = (TextView)findViewById(R.id.myminepage_name);
        tel = (TextView)findViewById(R.id.myminepage_tel);

        main = (XHorizontalMain) findViewById(R.id.myminepage_main);
        menu = (XHorizontalMenu) findViewById(R.id.myminepage_menu);
        main.setMenu(menu);

        menu.setLineHeight(2)
                .setNormalTxtSize(16)
                .setSelectedTxtSize(16)
                .setCellInterval(0)
                .setOnePageNum(2)
                .setLineHMaigin(30);

        dataArr = getData();

        menu.setData(dataArr);

        ImageLoader.getInstance().displayImage(APPDataCache.User.getHeadimage(),header,options);
        name.setText(APPDataCache.User.getNickname());

        String m = APPDataCache.User.getMobile();
        if(m.length() > 7)
        {
            this.tel.setText(m.substring(0,3)+"****"+m.substring(7,11));
        }
        else
        {
            tel.setText("暂未绑定手机号");
        }

    }

    @Override
    protected void setupData() {

    }


    private List<XHorizontalMenu.XHorizontalModel> getData() {
        List<XHorizontalMenu.XHorizontalModel> list = new ArrayList<XHorizontalMenu.XHorizontalModel>();

        XHorizontalMenu.XHorizontalModel model = new XHorizontalMenu.XHorizontalModel();
        model.setTitle("动态");
        model.setView(left);
        list.add(model);

        XHorizontalMenu.XHorizontalModel model1 = new XHorizontalMenu.XHorizontalModel();
        model1.setTitle("资料");
        model1.setView(right);
        list.add(model1);

        return list;
    }
}
