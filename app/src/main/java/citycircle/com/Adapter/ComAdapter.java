package citycircle.com.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcel;
import android.text.ParcelableSpan;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.R;

/**
 * Created by admins on 2015/12/11.
 */
public class ComAdapter extends BaseAdapter {
    ArrayList<HashMap<String, String>> abscure_list;
    Context context;
    public ComAdapter(ArrayList<HashMap<String, String>> abscure_list,
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
        final getItemView getItemView = new getItemView();
        convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
        getItemView.comment=(TextView)convertView.findViewById(R.id.comment);
        SpannableStringBuilder multiWord = new SpannableStringBuilder();
        ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#21ACFC"));
        ForegroundColorSpan span2 = new ForegroundColorSpan(Color.parseColor("#21ACFC"));
//        Intent intent = new Intent(context, OaUserinfo.class);
        String name=abscure_list.get(position).get("nickname");
        String tname=abscure_list.get(position).get("tnickname");
        if (tname.length()==0){
            SpannableString spanString = new SpannableString(name+":"+abscure_list.get(position).get("content"));
            spanString.setSpan(span,0,name.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            getItemView.comment.setText(spanString);
        }else {
            String str1=name+ "回复";
            String str2=tname+":" + abscure_list.get(position).get("content");
            SpannableString spanString = new SpannableString(str1+str2);
            spanString.setSpan(span,0,name.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            spanString.setSpan(span2, str1.length(), str1.length()+tname.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            getItemView.comment.setText(spanString);
        }

//        SpannableString word = new SpannableString(multiWord);

//        getItemView.comment.setMovementMethod(LinkMovementMethod.getInstance());
        return convertView;
    }
    private class getItemView {
        TextView comment;
    }
    private class IntentSpan extends ClickableSpan implements ParcelableSpan{
        private Intent mIntent;
        public IntentSpan(Intent intent) {
            mIntent = intent;
        }

        @Override
        public void onClick(View widget) {
            Context context = widget.getContext();
            context.startActivity( mIntent );
        }

        @Override
        public int getSpanTypeId() {
            return 100;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(Color.parseColor("#00BFFF"));
            ds.setUnderlineText(false);
        }
    }
}
