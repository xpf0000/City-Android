package citycircle.com.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.AsyncImageLoader;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;

/**
 * Created by admins on 2015/12/5.
 */
public class CityPhotos extends FragmentActivity {
    String url, urlstr, id;
    int pos;
    ViewPager viewpage;
    private ImageView[] mImageViews;
    HashMap<String, String> phashMap;
    ArrayList<HashMap<String, String>> parray = new ArrayList<HashMap<String, String>>();
    AsyncImageLoader ImageLoaders;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photolook);
        id=getIntent().getStringExtra("id");
        pos=getIntent().getIntExtra("pos", 0);
        url = GlobalVariables.urlstr + "Quan.getArticle&id=" + id;
        viewpage = (ViewPager) findViewById(R.id.viewpager);
        geturlstr();
//        viewpage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                float DownX = 0;
//                float DownY = 0;
//                switch (event.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                            DownX = event.getX();//float DownX
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            DownY = event.getX();//X轴距离
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            DownY = event.getX();//X轴距离
//                           if (DownY-DownX<700){
//                               finish();
//                           }
//                            break;
//                }
//                return true;
//            }
//        });
    }
//    void setImageview() {
//        ImageLoaders = new AsyncImageLoader();
//        mImageViews = new ImageView[parray.size()];
//        for (int i = 0; i < mImageViews.length; i++) {
//            final DragImageView dragImageView = new DragImageView(this);
//            mImageViews[i] = dragImageView;
//            dragImageView.setmActivity(this, dragImageView);// 注入Activity.
////            dragImageView.setImageResource(R.mipmap.ic_launcher);
//            dragImageView.getgetViewTreeObserver();
//            dragImageView.setOnTouchListener(new View.OnTouchListener() {
//                float DownX = 0;
//                float DownY = 0;
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    switch (event.getAction()) {
//                        case MotionEvent.ACTION_DOWN:
//                            DownX = event.getX();//float DownX
//                            break;
//                        case MotionEvent.ACTION_MOVE:
//                            DownY = event.getX();//X轴距离
//                            break;
//                        case MotionEvent.ACTION_UP:
//                            DownY = event.getX();//X轴距离
//                            finish();
//                            break;
//                    }
//                    return true;
//                }
//            });
//            String url = parray.get(i).get("path");
////            Bitmap bitmap=ImageLoader.loadImageSync(url);
////            dragImageView.setImageBitmap(bitmap);
//            Bitmap bitmap = ImageLoaders.loadBitmap(dragImageView,
//                    url, new AsyncImageLoader.ImageCallBack() {
//
//                        @SuppressWarnings("deprecation")
//                        @Override
//                        public void imageLoad(ImageView imageView, Bitmap bitmap) {
//                            dragImageView.setImageBitmap(bitmap);
//                        }
//
//                    });
//            if (bitmap == null) {
//                dragImageView.setImageResource(R.mipmap.nopic);
//            } else {
//                dragImageView.setImageBitmap(bitmap);
//            }
////            ImageLoader.displayImage(url, dragImageView, options,
////                    animateFirstListener);
//        }
//
//        viewpage.setAdapter(new MyAdapter());
//        viewpage.setCurrentItem(pos);
//    }
   public void geturlstr(){
       new Thread(){
           @Override
           public void run() {
               super.run();
               HttpRequest httpRequest=new HttpRequest();
               urlstr=httpRequest.doGet(url);
               if (urlstr.equals("网络超时")) {
                   handler.sendEmptyMessage(2);
               } else {
                   handler.sendEmptyMessage(1);

               }
           }
       }.start();

    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    getArray(urlstr);
                    ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), parray);
                    viewpage.setAdapter(mAdapter);
                    viewpage.setCurrentItem(pos);
//                    setImageview();
                    break;
                case 2:
                    break;
            }
        }
    };
    public void getArray(String str) {
        JSONObject jsonObject = JSON.parseObject(str);
        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
        int a = jsonObject1.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject1.getJSONArray("info");
            JSONObject jsonObject3 = jsonArray.getJSONObject(0);
            JSONArray jsonArray1 = jsonObject3.getJSONArray("picList");
            parray = new ArrayList<HashMap<String, String>>();
            for (int j = 0; j < jsonArray1.size(); j++) {
                JSONObject jsonObject4 = jsonArray1.getJSONObject(j);
                phashMap = new HashMap<>();
                phashMap.put("path", jsonObject4.getString("url") == null ? "" : jsonObject4.getString("url"));
                parray.add(phashMap);
            }
        }
    }
    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public ArrayList<HashMap<String, String>> fileList;

        public ImagePagerAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.size();
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList.get(position).get("path");
            return ImageDetailFragment.newInstance(url);
        }

    }
//    public class MyAdapter extends PagerAdapter {
//
//        @Override
//        public int getCount() {
//            return mImageViews.length;
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == arg1;
//        }
//
//        @Override
//        public void destroyItem(View container, int position, Object object) {
//            ((ViewPager) container).removeView(mImageViews[position]);
//
//        }
//
//        /**
//         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
//         */
//        @Override
//        public Object instantiateItem(View container, int position) {
//            ((ViewPager) container).addView(mImageViews[position]);
//            return mImageViews[position];
//        }
//    }
}
