package citycircle.com.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;
import citycircle.com.Utils.AsyncImageLoader;
import citycircle.com.Utils.GlobalVariables;

/**
 * Created by admins on 2015/12/4.
 */
public class PhotoLook extends FragmentActivity {
    ViewPager viewpage;
    private ImageView[] mImageViews;
    ImageView back;
    AsyncImageLoader ImageLoaders;
    ArrayList<HashMap<String, String>> parray;
    LinearLayout lool;
    int pos;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photolook);
        pos = getIntent().getIntExtra("pos", 0);
        parray = GlobalVariables.parrays;
        viewpage = (ViewPager) findViewById(R.id.viewpager);
        lool = (LinearLayout) findViewById(R.id.lool);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), parray);
        viewpage.setAdapter(mAdapter);
        viewpage.setCurrentItem(pos);
//        setImageview();
//        lool.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
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
//    void setImageview() {
//        ImageUtils = new ImageUtils();
//        ImageLoader = ImageLoader.getInstance();
//        ImageLoader.init(ImageLoaderConfiguration.createDefault(this));
//        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
//        ImageLoaders = new AsyncImageLoader();
//        options=ImageUtils.setOptions();
//        mImageViews = new ImageView[parray.size()];
//        for (int i = 0; i < mImageViews.length; i++) {
////            final DragImageView dragImageView = new DragImageView(this);
//            final ImageView dragImageView=new ImageView(this);
//            mImageViews[i] = dragImageView;
////            dragImageView.setmActivity(this, dragImageView);// 注入Activity.
////            dragImageView.setImageResource(R.mipmap.ic_launcher);
////            dragImageView.getgetViewTreeObserver();
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
//                             DownY = event.getX();//X轴距离
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
////            Bitmap bitmap = ImageLoaders.loadBitmap(dragImageView,
////                    url, new AsyncImageLoader.ImageCallBack() {
////
////                        @SuppressWarnings("deprecation")
////                        @Override
////                        public void imageLoad(ImageView imageView, Bitmap bitmap) {
////                            dragImageView.setImageBitmap(bitmap);
////                        }
////
////                    });
////            if (bitmap == null) {
////                dragImageView.setImageResource(R.mipmap.nopic);
////            } else {
////                dragImageView.setImageBitmap(bitmap);
////            }
//            ImageLoader.displayImage(url, dragImageView, options,
//                    animateFirstListener);
//        }
//
//        viewpage.setAdapter(new MyAdapter());
//        viewpage.setCurrentItem(pos);
//    }
//
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
