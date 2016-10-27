package citycircle.com.Property.PropertyAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by admins on 2016/3/22.
 */
public class PronumAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    Context context;

    public PronumAdapter(ArrayList<HashMap<String, String>> array, Context context) {
        this.array = array;
        this.context = context;
    }

    @Override
    public int getCount() {
        return array.size();
    }

    @Override
    public Object getItem(int position) {
        return array.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return convertView;
    }
}
