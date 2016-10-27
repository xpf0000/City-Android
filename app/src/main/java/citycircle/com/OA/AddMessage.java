package citycircle.com.OA;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import citycircle.com.MyViews.MyDialog;
import citycircle.com.OA.OAAdapter.CheckAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;
import citycircle.com.Utils.HttpRequest;
import citycircle.com.Utils.PreferencesUtils;

/**
 * Created by admins on 2016/1/7.
 */
public class AddMessage extends Activity implements View.OnClickListener{
    ImageView addmessagetback, addmessages;
    EditText messagetit, messagcon;
    String url, dataStr,uid,username,apptype,title,content;
    String returnback;
    Dialog dialog;
    String tag="";
    TextView check;
    View popView;
    PopupWindow popupWindow;
    CheckAdapter checkAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmessage);
        dialog = MyDialog.createLoadingDialog(AddMessage.this, "正在请求");
//        tag=PreferencesUtils.getString(AddMessage.this, "jgid");
//        tag=tag+","+PreferencesUtils.getString(AddMessage.this, "dwid");
//        tag=tag+","+PreferencesUtils.getString(AddMessage.this, "bmid");
//        tag=tag+","+PreferencesUtils.getString(AddMessage.this, "oauid");
        url= GlobalVariables.oaurlstr+"News.sendGroupcast&uid="+uid+"&username="+username+"&apptype=0&tag="+tag+"&title="+title+"&content="+content;
       intview();
    }
    private void intview(){
        check=(TextView)findViewById(R.id.check);
        messagetit = (EditText) findViewById(R.id.messagetit);
        messagcon = (EditText) findViewById(R.id.messagcon);
        addmessagetback = (ImageView) findViewById(R.id.addmessagetback);
        addmessages = (ImageView) findViewById(R.id.addmessages);
        messagetit.setOnClickListener(this);
        messagcon.setOnClickListener(this);
        addmessagetback.setOnClickListener(this);
        addmessages.setOnClickListener(this);
        check.setOnClickListener(this);
    }
  private void geturl(){
      new Thread(){
          @Override
          public void run() {
              super.run();
              HttpRequest httpRequest=new HttpRequest();
              returnback= httpRequest.doGet(url);
              if (returnback.equals("网络超时")){
                  handler.sendEmptyMessage(2);
              }else {
                  handler.sendEmptyMessage(1);
              }
          }
      }.start();
  }
    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    dialog.dismiss();
                    JSONObject jsonObject = JSONObject.parseObject(returnback);
                    JSONObject jsonObject1=jsonObject.getJSONObject("data");
                    int a=jsonObject1.getIntValue("code");
                    if (a==0){
                        Toast.makeText(AddMessage.this, "添加成功", Toast.LENGTH_SHORT)
                             .show();
                        finish();
                    }else {
                        Toast.makeText(AddMessage.this,
                                "添加失败," + jsonObject1.getString("msg"),
                                Toast.LENGTH_SHORT).show();
                    }

//                    if (jsonObject.getIntValue("status") == 0) {
//
//                    } else {
//                        Toast.makeText(AddMessage.this,
//                                "添加失败," + jsonObject.getString("statusMsg"),
//                                Toast.LENGTH_SHORT).show();
//                    }
                    break;
                case 2:
                    dialog.dismiss();
                    Toast.makeText(AddMessage.this, "网络超时", Toast.LENGTH_SHORT)
                            .show();
                    break;

                default:
                    break;
            }
        };
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addmessagetback:
                finish();
                break;
            case R.id.addmessages:
                if (messagetit.getText().toString().trim().equals("")
                        || messagcon.getText().toString().trim().equals("")) {
                    Toast.makeText(AddMessage.this, "请输入正确的内容！", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    uid = PreferencesUtils.getString(AddMessage.this, "oauid");
                    username = PreferencesUtils.getString(AddMessage.this, "oausername");
                    title=messagetit.getText().toString().trim();
                    content=messagcon.getText().toString().trim();
                    if (tag.length()==0){
                        Toast.makeText(AddMessage.this,"请选择发送范围",Toast.LENGTH_SHORT).show();
                    }else {
                        url= GlobalVariables.oaurlstr+"News.sendGroupcast&uid="+uid+"&username="+username+"&apptype=0&tag="+tag+"&title="+title+"&content="+content;
                        geturl();
                        dialog.show();
                    }
                }

                break;
            case  R.id.check:
                showpop();
                break;
            default:
                break;
        }
    }
    public void showpop(){
        popView =AddMessage.this.getLayoutInflater().inflate(
                R.layout.checkbox, null);
        WindowManager windowManager = AddMessage.this.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.update();
//        popupWindow.showAtLocation(popView,
//                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        popupWindow.showAsDropDown(check);
        ListView checklist=(ListView)popView.findViewById(R.id.checklist);
        checkAdapter=new CheckAdapter(GlobalVariables.arraylist,AddMessage.this);
        checklist.setAdapter(checkAdapter);
        checklist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                tag=GlobalVariables.arraylist.get(position).get("value");
                url= GlobalVariables.oaurlstr+"News.sendGroupcast&uid="+uid+"&username="+username+"&apptype=0&tag="+tag+"&title="+title+"&content="+content;
                check.setText("选择范围:"+GlobalVariables.arraylist.get(position).get("name"));
                popupWindow.dismiss();
            }
        });
    }
}
