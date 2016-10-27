package citycircle.com.MyViews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.Adapter.AllClassdadpter;
import citycircle.com.Adapter.ClassDadpter;
import citycircle.com.R;

/**
 * Created by admins on 2016/6/15.
 */
public class MyClassPopwd {
    View popView;
    PopupWindow popupWindow;
    int positions,getPositions;
    private MyPopwindowsListener mListViewListener;
    LinearLayout back;
    public void showpop(final Context context, final ArrayList<HashMap<String, String>> arrayList, View view) {
        popView = LayoutInflater.from(context).inflate(R.layout.classpop, null);
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);//这个控制PopupWindow内部控件的点击事件
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.update();
        popupWindow.showAsDropDown(view);
        ListView listView = (ListView) popView.findViewById(R.id.classlleft);
        back = (LinearLayout) popView.findViewById(R.id.back);
        final AllClassdadpter allClassdadpter=new AllClassdadpter(arrayList,context);
        listView.setAdapter(allClassdadpter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList.get(getPositions).put("check","false");
                getPositions = position;
                arrayList.get(position).put("check", "true");
                allClassdadpter.notifyDataSetChanged();
                startUrlLoadMore();
                popupWindow.dismiss();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void showpopdrable(Context context, final ArrayList<HashMap<String, Object>> arrayList, View view) {
        popView = LayoutInflater.from(context).inflate(R.layout.classpop, null);
        popupWindow = new PopupWindow(popView, LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setTouchable(true);//这个控制PopupWindow内部控件的点击事件
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.update();
        ListView listViewleft = (ListView) popView.findViewById(R.id.classlist);
        back = (LinearLayout) popView.findViewById(R.id.back);
        final ClassDadpter classDadpter = new ClassDadpter(arrayList, context);
        listViewleft.setAdapter(classDadpter);
        popupWindow.showAsDropDown(view);
        listViewleft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                arrayList.get(positions).put("check",false);
                positions = position;
                arrayList.get(position).put("check", true);
                classDadpter.notifyDataSetChanged();
                startLoadMore();
                popupWindow.dismiss();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    public void setMyPopwindowswListener(MyPopwindowsListener l) {
        mListViewListener = l;
    }

    public interface MyPopwindowsListener {
        public void onRefresh(int position);
    }

    private void startLoadMore() {

        mListViewListener.onRefresh(positions);

    }
    private void startUrlLoadMore() {

        mListViewListener.onRefresh(getPositions);

    }
}
