package citycircle.com.card;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;
import model.ChongzhiModel;
import model.YouhuiquanModel;
import util.BaseActivity;
import util.HttpResult;
import util.PayResult;
import util.XActivityindicator;
import util.XGridView;
import util.XNetUtil;
import util.XNotificationCenter;

import static citycircle.com.MyAppService.LocationApplication.APPDataCache;
import static citycircle.com.MyAppService.LocationApplication.APPService;

/**
 * Created by X on 2016/11/9.
 */

public class CardDoCZ2 extends BaseActivity  {

    private XGridView gview;

    private ChongzhiAdapter adapter;
    private List<ChongzhiModel> dataArr = new ArrayList<>();

    private TextView youhuiquan_tv;
    private TextView name;
    private TextView num;
    private int selectRow = -1;
    private String type = "";
    private String id = "";
    private String cardid = "";
    private String shopid = "";
    private String sname = "";
    private YouhuiquanModel youhuiModel;
    private static final int SDK_PAY_FLAG = 1;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            if(msg.what == SDK_PAY_FLAG)
            {
                PayResult payResult = new PayResult((Map<String, String>) msg.obj);

                XNetUtil.APPPrintln("payResult: "+payResult.toString());

                /**
                 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                 */
                String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                String resultStatus = payResult.getResultStatus();
                // 判断resultStatus 为9000则代表支付成功
                if (TextUtils.equals(resultStatus, "9000")) {
                    // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    XNotificationCenter.getInstance().postNotice("PaySuccess",null);
                    doPop();
                } else {
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    Toast.makeText(mContext, payResult.getMemo(), Toast.LENGTH_SHORT).show();
                }
            }


        };
    };

    public YouhuiquanModel getYouhuiModel() {
        return youhuiModel;
    }

    public void setYouhuiModel(YouhuiquanModel youhuiModel) {
        this.youhuiModel = youhuiModel;
        youhuiquan_tv.setText(youhuiModel.getMoney()+"元");

    }

    public void setSelectRow(int row) {

        this.selectRow = row;
        num.setText(dataArr.get(row).getMoney()+"元");
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XNotificationCenter.getInstance().removeObserver("ChoosedYouhuiquan");
    }

    @Override
    protected void setupUi() {
        setContentView(R.layout.chongzhi2);
        setPageTitle("充值");

        type = getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        shopid = getIntent().getStringExtra("shopid");
        cardid = getIntent().getStringExtra("cardid");
        sname = getIntent().getStringExtra("sname");

        name = (TextView)findViewById(R.id.name);
        num = (TextView)findViewById(R.id.num);
        youhuiquan_tv = (TextView)findViewById(R.id.youhuiquan_tv);
        name.setText(sname);


        gview = (XGridView) findViewById(R.id.gridView);
        gview.setScrollEnable(false);

        gview.setColumnWidth(LocationApplication.SW/2);

        gview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                setSelectRow(i);

            }
        });

        adapter = new ChongzhiAdapter();
        adapter.type = type;
        gview.setAdapter(adapter);
        getData();

        XNotificationCenter.getInstance().addObserver("ChoosedYouhuiquan", new XNotificationCenter.OnNoticeListener() {
            @Override
            public void OnNotice(Object obj) {
                setYouhuiModel((YouhuiquanModel) obj);
            }
        });

    }

    private void getData() {

        XNetUtil.Handle(APPService.hykGetCardProduct(cardid), new XNetUtil.OnHttpResult<List<ChongzhiModel>>() {
            @Override
            public void onError(Throwable e) {

                XNetUtil.APPPrintln(e);
            }

            @Override
            public void onSuccess(List<ChongzhiModel> models) {

                dataArr = models;
                adapter.notifyDataSetChanged();

            }
        });

    }

    @Override
    protected void setupData() {

    }

    public void toChooseYouhuiquan(View v)
    {
        if(selectRow < 0)
        {
            doShowToast("请先选择充值金额");
            return;
        }

        ChongzhiModel m = dataArr.get(selectRow);

        Bundle bundle = new Bundle();
        bundle.putSerializable("ChongzhiModel",m);

        pushVC(ChooseYouhuiquan.class,bundle);
    }

    public void submit(View v)
    {
        if(selectRow < 0)
        {
            doShowToast("请选择充值金额");
            return;
        }

        String uid = APPDataCache.User.getUid();
        String uname = APPDataCache.User.getUsername();
        String mcardid = id;
        String cpid = dataArr.get(selectRow).getId();
        String yhqid = youhuiModel == null ? "" : youhuiModel.getId();

        XActivityindicator.create(mContext).show();

        XNetUtil.HandleReturnAll(APPService.hykPaySign(uid, uname, mcardid, cpid, yhqid), new XNetUtil.OnHttpResult<HttpResult<Object>>() {

            @Override
            public void onError(Throwable e) {
                XActivityindicator.hide();
            }

            @Override
            public void onSuccess(HttpResult<Object> res) {
                XActivityindicator.hide();
                if(res.getData().getCode() == 0)
                {
                    final String str = (String) res.getData().getInfo();

                    XNetUtil.APPPrintln("~~~pay info: "+str);

                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(CardDoCZ2.this);
                            Map<String, String> result = alipay.payV2(str, true);
                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();

                }
                else if(res.getData().getCode() == 2)
                {
                    Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    XNotificationCenter.getInstance().postNotice("PaySuccess",null);
                    doPop();
                }
                else
                {
                    doShowToast(res.getData().getMsg());
                }
            }
        });

    }




    /**
     * 定义ListView适配器MainListViewAdapter
     */
    class ChongzhiAdapter extends BaseAdapter {

        public String type = "充值卡";

        /**
         * 返回item的个数
         */
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return dataArr.size();
        }

        /**
         * 返回item的内容
         */
        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return dataArr.get(position);
        }

        /**
         * 返回item的id
         */
        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        /**
         * 返回item的视图
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ListItemView listItemView;

            // 初始化item view
            if (convertView == null) {
                // 通过LayoutInflater将xml中定义的视图实例化到一个View中
                convertView = LayoutInflater.from(CardDoCZ2.this).inflate(
                        R.layout.chongzhi_cell, null);

                // 实例化一个封装类ListItemView，并实例化它的两个域
                listItemView = new ListItemView();
                listItemView.btn = (RadioButton) convertView
                        .findViewById(R.id.radio);

                // 将ListItemView对象传递给convertView
                convertView.setTag(listItemView);
            } else {
                // 从converView中获取ListItemView对象
                listItemView = (ListItemView) convertView.getTag();
            }

            ChongzhiModel m = dataArr.get(position);
            if(type.equals("充值卡"))
            {
                listItemView.btn.setText(m.getMoney()+"元/"+m.getValue()+"元");
            }
            else
            {
                listItemView.btn.setText(m.getMoney()+"元/"+m.getValue()+"次");
            }

            listItemView.btn.setChecked(selectRow == position);

            // 返回convertView对象
            return convertView;
        }

    }

    /**
     * 封装两个视图组件的类
     */
    class ListItemView {
        RadioButton btn;
    }
}
