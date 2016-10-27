package citycircle.com.OA;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import citycircle.com.MyViews.MyDialog;
import citycircle.com.OA.OAAdapter.aAdapter;
import citycircle.com.OA.uitls.Openfile;
import citycircle.com.R;
import citycircle.com.Utils.DateUtils;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/8.
 */
public class DocumentContent extends Activity {
    String id, url, urlstr, uid, username;
    TextView title, name, time, content;
    JSONArray annex;
    String gongwenid;
    String result, neirong, Time, Title;
    Dialog dialog;
    ListView listview;
    String s;
    aAdapter adapter;
    String[] file;
    List<A> items = new ArrayList<A>();
    List<Map<String, String>> items1 = new ArrayList<Map<String, String>>();
    ImageView messagetback;
    DateUtils dateUtils;
    Map<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.documentcontent);
        dialog = MyDialog.createLoadingDialog(this, "附件获取中..");
        dialog.show();
        id = getIntent().getStringExtra("id");
        username = PreferencesUtils.getString(DocumentContent.this, "oausername");
        uid = PreferencesUtils.getString(DocumentContent.this, "oauid");
        url = GlobalVariables.oaurlstr + "Document.getArticle&id=" + id + "&uid=" + uid + "&username=" + username;
        title = (TextView) this.findViewById(R.id.meeagetitle);
        time = (TextView) this.findViewById(R.id.meeagetime);
        listview = (ListView) this.findViewById(R.id.fujian);
        content = (TextView) this.findViewById(R.id.messageco);
        messagetback=(ImageView)findViewById(R.id.messagetback);
        messagetback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getcont();
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                dialog.show();
                // imageURL.substring(imageURL.lastIndexOf("/") + 1)
                final String ss = items.get(position).getUrl();
                String file = ss.substring(ss.lastIndexOf("/") + 1);
                String saveDir = Environment.getExternalStorageDirectory()
                        + "/wenjian/";
                File dir = new File(saveDir);
                if (!dir.exists()) {
                    dir.mkdirs(); // 创建文件夹
                }
                File f = new File(saveDir + file);
                if (f.exists()) {
                    System.out.println("判断文件是否存在去打开===" + f.exists());
                    Openfile.openFile(DocumentContent.this, f);
                    dialog.dismiss();
                } else {
                    new Thread() {
                        public void run() {
                            getfujian(ss);
                        }

                        ;
                    }.start();
                }
//
            }
        });
    }

    private void getcont() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                HttpRequest httpRequest = new HttpRequest();
                urlstr = httpRequest.doGet(url);
                if (urlstr.equals("网络超时")) {
                    handler.sendEmptyMessage(0);
                } else {
                    handler.sendEmptyMessage(2);
                }
            }
        }.start();
    }

    private void setarray(String str) {
        dateUtils = new DateUtils();
        JSONObject jo = JSONObject.parseObject(str);
        JSONObject jsonObject = jo.getJSONObject("data");
        int a = jsonObject.getIntValue("code");
        if (a == 0) {
            JSONArray jsonArray = jsonObject.getJSONArray("info");
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                neirong = jsonObject1.getString("content");
                Time = dateUtils.getDateToStringss(jsonObject1.getLongValue("create_time"));
                Title=jsonObject1.getString("title");
                annex = jsonObject1.getJSONArray("fileList");
            }
            if (annex != null) {
                file = new String[annex.size()];
                for (int j = 0; j < annex.size(); j++) {
                    A aa=new A();
                    JSONObject joo = annex.getJSONObject(j);
                    String fs = joo.getString("url") == null ? ""
                            : joo.getString("url");
                    //map = new HashMap<String, String>();
                    aa.setUrl(fs);
                    String n=joo.getString("name") == null ? ""
                            : joo.getString("name");
                    aa.setName(n);
                    items.add(aa);
                    file[j] = fs;

                    System.out.println("items===" + items.size());
                }
            }
            handler.sendEmptyMessage(1);
        }
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            if (msg.what == 0) {
                dialog.dismiss();
                Toast.makeText(DocumentContent.this, "网络超时", Toast.LENGTH_SHORT).show();
            }
            if (msg.what == 1) {
                dialog.dismiss();

                title.setText(Title);
                time.setText(Time);
                content.setText(neirong);
                adapter = new aAdapter(DocumentContent.this, items);
                listview.setAdapter(adapter);

            }
            if (msg.what == 2) {
                setarray(urlstr);
            }
            if (msg.what == 3) {
                dialog.dismiss();
                Toast.makeText(DocumentContent.this, "打开附件失败", Toast.LENGTH_SHORT).show();
            }
        }

    };

    public void getfujian(String uri) {
        String newFilename = uri.substring(uri.lastIndexOf("/") + 1);
        String saveDir = Environment.getExternalStorageDirectory()
                + "/wenjian/";
        File aaa = new File(saveDir + newFilename);
        // 如果目标文件已经存在，则删除。产生覆盖旧文件的效果
        if (aaa.exists()) {
            aaa.delete();
        }
        try {
            // 构造URL
            URL url = new URL(uri);
            // 打开连接
            InputStream is = url.openStream();
            // 1K的数据缓冲
            byte[] bs = new byte[1024];
            // 读取到的数据长度
            int len;
            // 输出的文件流
            OutputStream os = new FileOutputStream(aaa);
            // 开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            // 完毕，关闭所有链接
            os.close();
            is.close();
            dialog.dismiss();
            Openfile.openFile(DocumentContent.this, aaa);

        } catch (Exception e) {
            handler.sendEmptyMessage(3);
            e.printStackTrace();
        }
    }

    public class A {
        private String name;
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
