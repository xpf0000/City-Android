package citycircle.com.OA.OAAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import citycircle.com.OA.DocumentContent;
import citycircle.com.R;

public class aAdapter extends BaseAdapter {
	Context context;
	List<DocumentContent.A> list;

	public aAdapter(Context context, List<DocumentContent.A> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.fujianitem,
					null);
		}
		TextView name = (TextView) view.findViewById(R.id.textView1);
		name.setText(list.get(position).getName());
		return view;
	}


}
