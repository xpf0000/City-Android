package citycircle.com.hfb;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.R;
import util.BaseActivity;
import util.XHorizontalMain;
import util.XHorizontalMenu;

/**
 * Created by X on 16/9/2.
 */
public class YGManageMainVC extends BaseActivity {

    private XHorizontalMenu menu;
    private XHorizontalMain main;
    private List<XHorizontalMenu.XHorizontalModel> dataArr;

    private YGManageLeft left = new YGManageLeft();
    private GWManageRight right = new GWManageRight();

    @Override
    protected void setupUi() {
        setContentView(R.layout.yg_manage_main);
        setPageTitle("财富排行榜");


        main = (XHorizontalMain) findViewById(R.id.yg_manage_viewpager);
        menu = (XHorizontalMenu) findViewById(R.id.XHorizontalList);
        main.setMenu(menu);

        menu.setLineHeight(0)
                .setNormalTxtSize(16)
                .setSelectedTxtSize(16)
                .setCellInterval(0)
                .setOnePageNum(2);

        dataArr = getData();

        menu.setData(dataArr);

    }

    @Override
    protected void setupData() {

    }


    private List<XHorizontalMenu.XHorizontalModel> getData() {
        List<XHorizontalMenu.XHorizontalModel> list = new ArrayList<XHorizontalMenu.XHorizontalModel>();

        XHorizontalMenu.XHorizontalModel model = new XHorizontalMenu.XHorizontalModel();
        model.setTitle("签到达人");
        model.setView(left);
        list.add(model);

        XHorizontalMenu.XHorizontalModel model1 = new XHorizontalMenu.XHorizontalModel();
        model1.setTitle("财富排行");
        model1.setView(right);
        list.add(model1);

        return list;
    }






}
