package citycircle.com.OA.OAAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;

/**
 * Created by admins on 2015/11/5.
 */
public class SubscrAdapter extends BaseExpandableListAdapter {
    private Context context;
    ArrayList<HashMap<String, String>> groupArray ;
    ArrayList<ArrayList<HashMap<String, String>>> childArray;
    LayoutInflater inflater;
    Handler handler=new Handler();
    public SubscrAdapter(Activity a, ArrayList<HashMap<String, String>> groupArray,
                         ArrayList<ArrayList<HashMap<String, String>>> childArray, Context context, Handler handler) {
        this.groupArray = groupArray;
        this.childArray = childArray;
        this.context = context;
        this.handler=handler;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childArray.get(groupPosition).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.subchild_item, null);
        TextView txtstub=(TextView)convertView.findViewById(R.id.txtstub);
        TextView fristw=(TextView)convertView.findViewById(R.id.fristw);
        txtstub.setText(childArray.get(groupPosition).get(childPosition).get("truename"));
        fristw.setText(childArray.get(groupPosition).get(childPosition).get("truename").substring(0, 1));
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return groupArray.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return groupArray.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(context)
                .inflate(R.layout.subgroup, null);
        ImageView imageView = (ImageView) layout.findViewById(R.id.groupview);
        TextView name = (TextView) layout.findViewById(R.id.name);
        if (isExpanded) {
            imageView.setImageResource(R.drawable.class_arrow);
        } else {
            imageView.setImageResource(R.drawable.class_left);
        }
        name.setText(groupArray.get(groupPosition).get("title"));
        return layout;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }
}
