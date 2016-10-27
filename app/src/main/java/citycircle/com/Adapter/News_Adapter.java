package citycircle.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Activity.CityClassList;
import citycircle.com.Activity.CityPhotos;
import citycircle.com.Activity.Logn;
import citycircle.com.MyViews.MyGridView;
import citycircle.com.MyViews.MyListView;
import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.ImageUtils;
import citycircle.com.Utils.PreferencesUtils;
import citycircle.com.Utils.Timechange;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by admins on 2015/11/23.
 */
public class News_Adapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    com.nostra13.universalimageloader.core.ImageLoader ImageLoader;
    DisplayImageOptions options;
    citycircle.com.Utils.ImageUtils ImageUtils;
    ImageLoadingListener animateFirstListener;
    Handler handler = new Handler();
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> comhashMap;
    ArrayList<HashMap<String, String>> comarray = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> comarrays = new ArrayList<HashMap<String, String>>();
    newPhotoAdapter newPhotoAdapter;
    DateUtils dateUtils;

    public News_Adapter(ArrayList<HashMap<String, String>> abscure_list,
                        Context context, Handler handler) {
        this.abscure_list = abscure_list;
        this.context = context;
        ImageUtils = new ImageUtils();
        this.handler = handler;
        ImageLoader = ImageLoader.getInstance();
        ImageLoader.init(ImageLoaderConfiguration.createDefault(context));
        animateFirstListener = new ImageUtils.AnimateFirstDisplayListener();
        dateUtils =new DateUtils();
    }
    @Override
    public int getCount() {
        return abscure_list.size();
    }

    @Override
    public Object getItem(int position) {
        return abscure_list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final getItemView getItemView = new getItemView();
        array = new ArrayList<HashMap<String, String>>();
        comarray=new ArrayList<HashMap<String, String>>();
        convertView = LayoutInflater.from(context).inflate(R.layout.news_item, null);
        getItemView.usename = (TextView) convertView.findViewById(R.id.usename);
        getItemView.time = (TextView) convertView.findViewById(R.id.time);
        getItemView.title = (TextView) convertView.findViewById(R.id.title);
        getItemView.zhankai = (TextView) convertView.findViewById(R.id.zhankai);
        getItemView.share = (Button) convertView.findViewById(R.id.share);
        getItemView.comment = (Button) convertView.findViewById(R.id.comment);
        getItemView.collect = (Button) convertView.findViewById(R.id.collect);
        getItemView.content = (TextView) convertView.findViewById(R.id.content);
        getItemView.myListView=(MyListView)convertView.findViewById(R.id.commentlist);
        getItemView.uesrhead=(ImageView)convertView.findViewById(R.id.uesrhead);
        getItemView.myGridView = (MyGridView) convertView.findViewById(R.id.photogrid);
        getItemView.zans=(TextView)convertView.findViewById(R.id.zans);
        getItemView.number=(TextView)convertView.findViewById(R.id.number);
        getItemView.zanlay=(LinearLayout)convertView.findViewById(R.id.zanlay);
        getItemView.linelay=(LinearLayout)convertView.findViewById(R.id.linelay);
        String url=abscure_list.get(position).get("headimage");
        options=ImageUtils.setCirclelmageOptions();
        ImageLoader.displayImage(url, getItemView.uesrhead, options,
                animateFirstListener);
        Timechange timechange=new Timechange();
        String time= timechange.Time(dateUtils.getDateToStringss(Long.parseLong(abscure_list.get(position).get("create_time"))));
        getItemView.time.setText(time);
        getItemView.content.setText(abscure_list.get(position).get("content"));
        getItemView.content.post(new Runnable() {
            @Override
            public void run() {
                if ( getItemView.content.getLineCount()>4){
                    getItemView.zhankai.setVisibility(View.VISIBLE);
                }else {
                    getItemView.zhankai.setVisibility(View.GONE);
                }
            }
        });
        getItemView.usename.setText(abscure_list.get(position).get("nickname"));
        getItemView.title.setText(abscure_list.get(position).get("title"));
        if (abscure_list.get(position).get("orzan").equals("0")){
            getItemView.collect.setBackgroundResource(R.mipmap.btn_unlike_normal);
        }else {
            getItemView.collect.setBackgroundResource(R.mipmap.btn_like_normal);
        }
        if (abscure_list.get(position).get("sex").equals("0")){
            Drawable drawable= context.getResources().getDrawable(R.mipmap.woman);
            getItemView.usename.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        }else {
            Drawable drawable= context.getResources().getDrawable(R.mipmap.man);
            getItemView.usename.setCompoundDrawablesWithIntrinsicBounds(null,null,drawable,null);
        }
        getItemView.zhankai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getItemView.zhankai.getText().equals("展开")){
                    getItemView.content.setMaxLines(100);
                    getItemView.content.setText(abscure_list.get(position).get("content"));
                    getItemView.zhankai.setText("收起");
                }else {
                    getItemView.zhankai.setText("展开");
                    getItemView.content.setMaxLines(4);
                    getItemView.content.setText(abscure_list.get(position).get("content"));
                }
            }
        });
//        getItemView.collect.setText(abscure_list.get(position).get("zan"));
//        getItemView.comment.setText(abscure_list.get(position).get("comment"));
        String str=abscure_list.get(position).get("picList");
        getItemView.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(context, "ccae6a09a59e");
                String str=abscure_list.get(position).get("picList");
                JSONArray jsonArray= JSON.parseArray(str);
                String url=jsonArray.getJSONObject(0).getString("url");
//                for (int i=0;i<jsonArray.size();i++){
//                    hashMap=new HashMap<>();
//                    JSONObject jsonObject=jsonArray.getJSONObject(i);
//                    hashMap.put("path",jsonObject.getString("url")== null ? "" : jsonObject.getString("url"));
//                    hashMap.put("width",jsonObject.getString("width")== null ? "" : jsonObject.getString("width"));
//                    hashMap.put("height",jsonObject.getString("height")== null ? "" : jsonObject.getString("height"));
//                    array.add(hashMap);
//                }
                final OnekeyShare oks = new OnekeyShare();
                Bitmap logo = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_share_copylink);
                View.OnClickListener listener = new View.OnClickListener() {
                    public void onClick(View v) {
                        ClipboardManager clip = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
                        clip.setText("http://101.201.169.38/city/city_info.html?id="+abscure_list.get(position).get("id"));
                        Toast.makeText(context,"已经复制到粘贴板",Toast.LENGTH_SHORT).show();
                    }
                };
                oks.setCustomerLogo(logo,logo,"复制链接",listener);
                //关闭sso授权
                oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
                //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                oks.setTitle(abscure_list.get(position).get("content"));
                // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
                oks.setTitleUrl("http://wap.huaifuwang.com/city/city_info.html?id="+abscure_list.get(position).get("id"));
                // text是分享文本，所有平台都需要这个字段
                oks.setText(abscure_list.get(position).get("content"));
                oks.setImageUrl(url);
                // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
                // url仅在微信（包括好友和朋友圈）中使用
                oks.setUrl("http://wap.huaifuwang.com/city/city_info.html?id="+abscure_list.get(position).get("id"));
                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
                // site是分享此内容的网站名称，仅在QQ空间使用
                oks.setSite(context.getString(R.string.app_name));
                // siteUrl是分享此内容的网站地址，仅在QQ空间使用
                oks.setSiteUrl("http://wap.huaifuwang.com/city/city_info.html?id="+abscure_list.get(position).get("id"));

// 启动分享GUI
                oks.show(context);
            }
        });
        getItemView.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.putExtra("id", abscure_list.get(position).get("id"));
//                intent.setClass(context, Cityinfo.class);
//                context.startActivity(intent);
                int a = PreferencesUtils.getInt(context, "land");
                if (a == 0) {
                    Intent intent = new Intent();
                    intent.setClass(context, Logn.class);
                    context.startActivity(intent);
                } else {
                    GlobalVariables.position=position;
                    handler.sendEmptyMessage(7);
                }
            }
        });
        getItemView.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("id",abscure_list.get(position).get("category_id"));
                intent.setClass(context, CityClassList.class);
                context.startActivity(intent);
            }
        });
        getItemView.collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = PreferencesUtils.getInt(context, "land");
                if (a == 0) {
                    Intent intent = new Intent();
                    intent.setClass(context, Logn.class);
                    context.startActivity(intent);
                } else {
                    if (abscure_list.get(position).get("orzan").equals("0")){
                        GlobalVariables.position=position;
                        String str=abscure_list.get(position).get("picList");
                        JSONArray jsonArray= JSON.parseArray(str);
                        for (int i=0;i<jsonArray.size();i++){
                            hashMap=new HashMap<>();
                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                            GlobalVariables.imgurls=jsonObject.getString("url");
                            array.add(hashMap);
                        }
                        handler.sendEmptyMessage(4);
                    }else {
                        Toast.makeText(context,"已经赞过啦",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        getItemView.myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                int a = PreferencesUtils.getInt(context, "land");
                if (a == 0) {
                    Intent intent = new Intent();
                    intent.setClass(context, Logn.class);
                    context.startActivity(intent);
                } else {
                    comarrays.clear();
                    String nickname=PreferencesUtils.getString(context,"nickname");
                    String comstr=abscure_list.get(position).get("commentList");
                    JSONArray jsonArray1=JSON.parseArray(comstr);
                    for (int i=0;i<jsonArray1.size();i++){
                        JSONObject jsonObject=jsonArray1.getJSONObject(i);
                        comhashMap=new HashMap<>();
                        String name = jsonObject.getString("nickname") == null ? "" : jsonObject.getString("nickname");
                        String tnickname=jsonObject.getString("tnickname") == null ? "" : jsonObject.getString("tnickname");
                        comhashMap.put("content", jsonObject.getString("content") == null ? "" : jsonObject.getString("content"));
                        comhashMap.put("id", jsonObject.getString("id") == null ? "" : jsonObject.getString("id"));
                        comhashMap.put("uid", jsonObject.getString("uid") == null ? "" : jsonObject.getString("uid"));
                        comhashMap.put("tnickname",tnickname );
                        comhashMap.put("nickname", name);
                        comarrays.add(comhashMap);
                    }
                    if (comarrays.get(positions).get("nickname").equals(nickname)){

                    }else {
                        GlobalVariables.position=position;
                        GlobalVariables.positions=positions;
                        handler.sendEmptyMessage(9);
                    }

                }
            }
        });
        JSONArray jsonArray= JSON.parseArray(str);
        for (int i=0;i<jsonArray.size();i++){
            hashMap=new HashMap<>();
            JSONObject jsonObject=jsonArray.getJSONObject(i);
            hashMap.put("path",jsonObject.getString("url")== null ? "" : jsonObject.getString("url"));
            hashMap.put("width",jsonObject.getString("width")== null ? "" : jsonObject.getString("width"));
            hashMap.put("height",jsonObject.getString("height")== null ? "" : jsonObject.getString("height"));
            array.add(hashMap);
        }
        if (array.size()==2){
            getItemView.myGridView.setNumColumns(2);
        }else if (array.size()==1){
            getItemView.myGridView.setNumColumns(1);
        } if (array.size()>=3){
            getItemView.myGridView.setNumColumns(3);
        }
        newPhotoAdapter=new newPhotoAdapter(array,context);
        getItemView.myGridView.setAdapter(newPhotoAdapter);
        getItemView.myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int positions, long id) {
                Intent intent=new Intent();
                intent.putExtra("id",abscure_list.get(position).get("id"));
                intent.putExtra("pos",positions);
                intent.setClass(context, CityPhotos.class);
                context.startActivity(intent);
            }
        });
        String comstr=abscure_list.get(position).get("commentList");
        JSONArray jsonArray1=JSON.parseArray(comstr);
        for (int i=0;i<jsonArray1.size();i++){
            JSONObject jsonObject=jsonArray1.getJSONObject(i);
            comhashMap=new HashMap<>();
            String name = jsonObject.getString("nickname") == null ? "" : jsonObject.getString("nickname");
            String tnickname=jsonObject.getString("tnickname") == null ? "" : jsonObject.getString("tnickname");
            comhashMap.put("content", jsonObject.getString("content") == null ? "" : jsonObject.getString("content"));
            comhashMap.put("id", jsonObject.getString("id") == null ? "" : jsonObject.getString("id"));
            comhashMap.put("uid", jsonObject.getString("uid") == null ? "" : jsonObject.getString("uid"));
            comhashMap.put("tnickname",tnickname );
            comhashMap.put("nickname", name);
            comarray.add(comhashMap);
        }
        if (comarray.size()==0){
            getItemView.myListView.setVisibility(View.GONE);
        }else {
            getItemView.myListView.setVisibility(View.VISIBLE);
            ComAdapter adapter=new ComAdapter(comarray,context);
            getItemView.myListView.setAdapter(adapter);
        }

        String zanstr=abscure_list.get(position).get("zanList");
        JSONArray jsonArray2=JSON.parseArray(zanstr);
        String name="";
        if (jsonArray2.size()==0){
            getItemView.linelay.setVisibility(View.GONE);
            getItemView.zanlay.setVisibility(View.GONE);
        }else {
            for (int i=0;i<jsonArray2.size();i++){
                JSONObject jsonObject=jsonArray2.getJSONObject(i);
                if (i==0){
                    name=jsonObject.getString("nickname");
                }else {
                    name=name+","+jsonObject.getString("nickname");
                }

            }
        }
        getItemView.number.setText(abscure_list.get(position).get("zan"));
        getItemView.zans.setText(name);
        return convertView;
    }
    private class getItemView {
        ImageView uesrhead;
        TextView usename, time,title,zans,content,number,zhankai;
        MyGridView myGridView;
        MyListView myListView;
        Button share,comment,collect;
        LinearLayout zanlay,linelay;
    }
}
