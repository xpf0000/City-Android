package citycircle.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import citycircle.com.Activity.NewsInfoActivity;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;

public class Myviewpageadapater extends PagerAdapter {
	private List<View> mList;
	private Context context;
	ImageView image;
	ArrayList<HashMap<String, String>> imgarray;
	com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
	DisplayImageOptions options;
	ImageUtils ImageUtils;
	ImageLoadingListener animateFirstListener;
	Handler handler=new Handler();

	public Myviewpageadapater(List<View> list, Context context,
							  ArrayList<HashMap<String, String>> imgarray,Handler handler) {
		this.mList = list;
		this.context = context;
		this.imgarray = imgarray;
		ImageUtils = new ImageUtils();
		ImageLoader = ImageLoader.getInstance();
		try{
			ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
		}catch (Exception e){
		}
		animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
		this.handler=handler;
	}

	/**
	 * Return the number of views available.
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	/**
	 * Remove a page for the given position. 滑动过后就销毁 ，销毁当前页的前一个的前一个的页！
	 * instantiateItem(View container, int position) This method was deprecated
	 * in API level . Use instantiateItem(ViewGroup, int)
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		container.removeView(mList.get(position));

	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

	/**
	 * Create the page for the given position.
	 */
	@Override
	public Object instantiateItem(final ViewGroup container, final int position) {
		final int[] b = {0};
		options=ImageUtils.setnoOptions();
		final View view = mList.get(position);
		image = (ImageView) view.findViewById(R.id.viewimage);
		final String url= imgarray.get(position).get("url");
		final int[] a = {PreferencesUtils.getInt(context, "photo")};
//		if (a==1){
			ImageLoader.displayImage(url, image, options,
					animateFirstListener);
//		}else {
//			String urls=  ImageLoader.getDiscCache().get(url).getPath();
//			boolean bloo= ImageUtils.fileIsExists(urls);
//			if (bloo){
//				ImageLoader.displayImage(url, image, options,
//						animateFirstListener);
//			}else {
		image.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						ImageLoader.displayImage(url,image, options,
//								animateFirstListener);
//						image.setClickable(false);
						Intent intent=new Intent();
						intent.putExtra("id",imgarray.get(position).get("id"));
						GlobalVariables.position=position;
						intent.setClass(context, NewsInfoActivity.class);

						if (b[0] ==0){
							handler.sendEmptyMessage(4);
							b[0] =2;
						}else {

						}
						context.startActivity(intent);
					}
				});

////			}
//		}
		container.removeView(mList.get(position));
		container.addView(mList.get(position));
		return mList.get(position);
	}

}
