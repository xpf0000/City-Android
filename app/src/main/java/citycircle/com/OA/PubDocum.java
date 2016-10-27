package citycircle.com.OA;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;

import citycircle.com.OA.OAAdapter.DocumentAdapter;
import citycircle.com.OA.entities.FileInfo;
import citycircle.com.OA.service.DownloadService;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/26.
 */
public class PubDocum extends Fragment {
    View view, footview;
    ListView doculist;
    String url, urlstr;
    String username,uid;
    DocumentAdapter adapter;
    FileInfo fileInfo ;
    TextView foottxt;
    private ArrayList<FileInfo> mFileInfoList = new ArrayList<>();
    int page = 1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.document_lay, container, false);
        username= PreferencesUtils.getString(getActivity(), "oausername");
        uid = PreferencesUtils.getString(getActivity(), "oauid");
        url= GlobalVariables.oaurlstr+"Files.getList&username="+username+"&uid="+uid+"&page="+page;
        intview();
        setDoculist();
        getdoulist();
        return view;
    }

    private void intview() {
        footview = LayoutInflater.from(getActivity()).inflate(
                R.layout.list_footview, null);
        foottxt = (TextView) footview.findViewById(R.id.foottxt);
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        filter.addAction(DownloadService.ACTION_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);
        doculist = (ListView) view.findViewById(R.id.doculist);
        doculist.addFooterView(footview,null,false);
        doculist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            page++;
                            url= GlobalVariables.oaurlstr+"Files.getList&username="+username+"&uid="+uid+"&page="+page;
                            getdoulist();
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void getdoulist() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setArraylist(urlstr);
                    adapter.notifyDataSetChanged();
                    break;
                case 2:
                    Toast.makeText(getActivity(),"网络似乎有问题了",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    foottxt.setText("已加载全部");
                    break;

            }
        }
    };
    private void setArraylist(String str){
        JSONObject jsonObject= JSON.parseObject(str);
        JSONObject jsonObject1=jsonObject.getJSONObject("data");
        int a=jsonObject1.getIntValue("code");
        if (a==0){
            JSONArray jsonArray=jsonObject1.getJSONArray("info");
            for (int i=0;i<jsonArray.size();i++){
                JSONObject jsonObject2=jsonArray.getJSONObject(i);
                String url=jsonObject2.getString("url")== null ? ""
                        : jsonObject2.getString("url");
                String name=jsonObject2.getString("name")== null ? ""
                        : jsonObject2.getString("name");
                String docuid=jsonObject2.getString("id")== null ? ""
                        : jsonObject2.getString("id");
                fileInfo = new FileInfo(i,url, name, 0, 0,docuid);
//                hashmap=new HashMap<>();
//                hashmap.put("id",jsonObject2.getString("id")== null ? ""
//                        : jsonObject2.getString("id"));
//                hashmap.put("name",jsonObject2.getString("name")== null ? ""
//                        : jsonObject2.getString("name"));
//                hashmap.put("url",jsonObject2.getString("url")== null ? ""
//                        : jsonObject2.getString("url"));
//                arraylist.add(hashmap);
                mFileInfoList.add(fileInfo);
            }
        }else {
            if (page==1){

            }else {
                page--;
            }
            handler.sendEmptyMessage(3);
        }
    }

    public void setDoculist() {
        if (mFileInfoList.size() < 10) {
            foottxt.setText("已加载全部");
        }
        adapter=new DocumentAdapter(getActivity(),mFileInfoList,0,handler);
        doculist.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (DownloadService.ACTION_UPDATE.equals(intent.getAction()))
            {
                int finised = intent.getIntExtra("finished", 0);
                int id = intent.getIntExtra("id", 0);
                adapter.updateProgress(id, finised);
                Log.i("mReceiver", id + "-finised = " + finised);
            }
            else if (DownloadService.ACTION_FINISHED.equals(intent.getAction()))
            {
                // 下载结束
                FileInfo fileInfo = (FileInfo) intent.getSerializableExtra("fileInfo");
                adapter.updateProgress(fileInfo.getId(), 100);
                Toast.makeText(getActivity(),
                        mFileInfoList.get(fileInfo.getId()).getFileName() + "下载完毕",
                        Toast.LENGTH_SHORT).show();

            }
        }
    };
}
