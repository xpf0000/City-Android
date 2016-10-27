package citycircle.com.OA;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.OA.OAAdapter.MyDocuments;
import citycircle.com.OA.service.DownloadService;
import citycircle.com.OA.uitls.Openfile;
import citycircle.com.R;

/**
 * Created by admins on 2016/1/26.
 */
public class UpLoad extends Fragment {
    View view;
    ListView doculist;
    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    HashMap<String, String> hashmap;
    String path;
    MyDocuments adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.document_lay, container, false);
        path = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/citycircle/OADowns";
        intview();
        try {
            getDocument(path);
        }catch (Exception e){

        }
        return view;
    }

    private void intview() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        filter.addAction(DownloadService.ACTION_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);
        doculist = (ListView) view.findViewById(R.id.doculist);
        doculist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path=arraylist.get(position).get("path");
                File f=new File(path);
                Openfile.openFile(getActivity(), f);
            }
        });
    }

    private void getDocument(String path) {
        File file = new File(path);
        File[] subFile = file.listFiles();
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String filename = subFile[iFileLength].getName();
                // 判断是否为MP4结尾
                hashmap = new HashMap<>();
                hashmap.put("name", filename);
                hashmap.put("path", path + "/" + filename);
                arraylist.add(hashmap);
            }
        }
        adapter = new MyDocuments(arraylist, getActivity());
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
            }
            else if (DownloadService.ACTION_FINISHED.equals(intent.getAction()))
            {
                // 下载结束
                arraylist.clear();
                getDocument(path);
            }
        }
    };

}