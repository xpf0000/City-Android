package citycircle.com.OA.OAAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;

/**
 * Created by admins on 2016/1/26.
 */
public class MyDocuments extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    public MyDocuments(ArrayList<HashMap<String, String>> abscure_list,
                        Context context) {
        this.abscure_list = abscure_list;
        this.context = context;

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
        getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.document_item, null);
        getItemView.name=(TextView)convertView.findViewById(R.id.name);
        getItemView.type=(ImageView)convertView.findViewById(R.id.type);
        getItemView.dowm=(Button)convertView.findViewById(R.id.dowm);
        getItemView.del=(Button)convertView.findViewById(R.id.del);
        getItemView.dowm.setVisibility(View.GONE);
        getItemView.name.setText(abscure_list.get(position).get("name"));
        String name=abscure_list.get(position).get("name");
        if (name.trim().toLowerCase().endsWith(".doc")){
            getItemView.type.setImageResource(R.drawable.icon_list_doc);
        }else if (name.trim().toLowerCase().endsWith(".ppt")){
            getItemView.type.setImageResource(R.drawable.icon_list_ppt);
        }else if (name.trim().toLowerCase().endsWith(".xls")){
            getItemView.type.setImageResource(R.drawable.icon_list_excel);
        }else if (name.trim().toLowerCase().endsWith(".txt")){
            getItemView.type.setImageResource(R.drawable.icon_list_txtfile);
        }else if (name.trim().toLowerCase().endsWith(".pdf")){
            getItemView.type.setImageResource(R.drawable.icon_list_pdf);
        }else {
            getItemView.type.setImageResource(R.drawable.icon_list_unknown);
        }
        getItemView.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(abscure_list.get(position).get("path"));
                abscure_list.remove(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }
    private class getItemView {
        TextView name;
        ImageView type;
        Button dowm,del;
    }
    private void delete(String path){
        File file=new File(path);
        if (file.exists()){
            if (file.isFile()){
                file.delete();
            }
        }else {
            Toast.makeText(context,"文件不存在",Toast.LENGTH_SHORT).show();
        }

    }
}
