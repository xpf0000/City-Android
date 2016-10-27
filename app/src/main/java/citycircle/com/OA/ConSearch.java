package citycircle.com.OA;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import citycircle.com.OA.OAAdapter.CityNameMod;
import citycircle.com.OA.OAAdapter.SortAdapter;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;

/**
 * Created by admins on 2016/1/6.
 */
public class ConSearch extends Activity {
    ListView serachlist;
    EditText earch;
    String key, dwid;
    public static List<CityNameMod> SearourceDateList = new ArrayList<>();
    public static List<CityNameMod> SourceDateList;
    CityNameMod cityNameMod = new CityNameMod();
    private SortAdapter adapter;
    ImageView loginback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consearch);
        SourceDateList = GlobalVariables.SourceDateList;
        intview();
    }

    private void intview() {
        loginback=(ImageView)findViewById(R.id.loginback);
        serachlist = (ListView) findViewById(R.id.serachlist);
        earch = (EditText) findViewById(R.id.earch);
        loginback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        serachlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("getAddress", SearourceDateList.get(position).getAddress());
                intent.putExtra("getEmail", SearourceDateList.get(position).getEmail());
                intent.putExtra("getId", SearourceDateList.get(position).getId());
                intent.putExtra("getMobile", SearourceDateList.get(position).getMobile());
                intent.putExtra("getQq", SearourceDateList.get(position).getQq());
                intent.putExtra("getTel", SearourceDateList.get(position).getTel());
                intent.putExtra("getTruename", SearourceDateList.get(position).getTruename());
                intent.setClass(ConSearch.this, TelMessage.class);
                ConSearch.this.startActivity(intent);
            }
        });
        earch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (earch.getText().toString().trim().length() == 0) {

                } else {
                    SearourceDateList.clear();
                    setArray(earch.getText().toString().trim());
                    adapter = new SortAdapter(ConSearch.this, SearourceDateList);
                    serachlist.setAdapter(adapter);
                }
            }
        });
    }

    private void setArray(String key) {
        for (int i = 0; i < SourceDateList.size(); i++) {
            if (SourceDateList.get(i).getTruename().contains(key) || SourceDateList.get(i).getSortLetters().contains(key) || SourceDateList.get(i).getSortLetters().toLowerCase().contains(key) || SourceDateList.get(i).getMobile().contains(key)) {
                String a = SourceDateList.get(i).getTruename();
                System.out.println(a.contains(key));
                cityNameMod.setQq(SourceDateList.get(i).getQq());
                cityNameMod.setMobile(SourceDateList.get(i).getMobile());
                cityNameMod.setTel(SourceDateList.get(i).getTel());
                cityNameMod.setTruename(SourceDateList.get(i).getTruename());
                cityNameMod.setId(SourceDateList.get(i).getId());
                cityNameMod.setEmail(SourceDateList.get(i).getEmail());
                cityNameMod.setAddress(SourceDateList.get(i).getAddress());
                cityNameMod.setSortLetters(SourceDateList.get(i).getSortLetters());
                SearourceDateList.add(cityNameMod);
            }
        }

    }
}
