package citycircle.com.Property;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Property.PropertyAdapter.PayAdapter;
import citycircle.com.R;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;
import util.BitmapMo;

/**
 * Created by 飞侠 on 2016/2/23.
 */
public class Payment extends Activity implements View.OnClickListener {
    ImageView back, head;
    ListView paymentlist;
    ArrayList<HashMap<String, Object>> arrayList = new ArrayList<>();
    HashMap<String, Object> hashMap;
    PayAdapter payAdapter;
    View view;
    TextView xiaoquu, adress;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    RelativeLayout relativeLayout;
    BitmapMo bitmapMo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment);
        bitmapMo = new BitmapMo();
        view = LayoutInflater.from(this).inflate(R.layout.payhead, null);
        intview();
    }

    private void intview() {
        relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
        xiaoquu = (TextView) view.findViewById(R.id.xiaoquu);
        adress = (TextView) view.findViewById(R.id.adress);
        head = (ImageView) view.findViewById(R.id.head);
        back = (ImageView) findViewById(R.id.back);
        paymentlist = (ListView) findViewById(R.id.paymentlist);
        back.setOnClickListener(this);
        paymentlist.addHeaderView(view);
        xiaoquu.setText("小区：" +PreferencesUtils.getString(Payment.this,"xiaoqu"));
        adress.setText("地址：" + PreferencesUtils.getString(Payment.this,"housename"));
        ImageUtils = new ImageUtils();
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        options = ImageUtils.setCirclelmageOptions();
        String headurl = PreferencesUtils.getString(this, "headimage");
        ImageLoader.displayImage(headurl, head, options,
                animateFirstListener);
        setArrayList();
        paymentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                } else {
                    Intent intent = new Intent();
                    intent.setClass(Payment.this, paymentInfo.class);
                    intent.putExtra("type", arrayList.get(position - 1).get("type").toString());
                    intent.putExtra("title", arrayList.get(position - 1).get("title").toString());
                    Payment.this.startActivity(intent);
                }

            }
        });
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Drawable drawable = getDrawable(R.mipmap.bg);
//                BitmapDrawable bd = (BitmapDrawable) drawable;
//                Bitmap bitmap = bd.getBitmap();
//                bitmap= bitmapMo.fastblur(Payment.this,bitmap,70);
//                Drawable drawables =new BitmapDrawable(bitmap);
//                relativeLayout.setBackground(drawables);
//            }
//        },300);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
        }
    }

    private void setArrayList() {
        String title[] = new String[]{"电费","水费", "物业费","停车费"};
        String type[] = new String[]{"2","3", "1","4"};
        int drable[] = new int[]{R.mipmap.iconfontdian,R.mipmap.water, R.mipmap.iconfontwuyejiaofei,R.mipmap.car};
        for (int i = 0; i < title.length; i++) {
            hashMap = new HashMap<>();
            hashMap.put("title", title[i]);
            hashMap.put("icon", drable[i]);
            hashMap.put("type", type[i]);
            arrayList.add(hashMap);
        }
        payAdapter = new PayAdapter(arrayList, Payment.this);
        paymentlist.setAdapter(payAdapter);
    }

}
