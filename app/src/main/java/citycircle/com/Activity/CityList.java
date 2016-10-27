package citycircle.com.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;

import java.util.ArrayList;
import java.util.HashMap;

import citycircle.com.MyAppService.LocationApplication;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;

/**
 * Created by admins on 2015/12/17.
 */
public class CityList extends Activity implements OnGetPoiSearchResultListener, OnGetSuggestionResultListener {
    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private LocationClient mLocClient;
    LocationApplication location = null;
    String City, Street, District;
    ListView list;
    HashMap<String, String> hashMap;
    ArrayList<HashMap<String, String>> array = new ArrayList<HashMap<String, String>>();
    CityListAdapter adapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citylist);
        list = (ListView) findViewById(R.id.list);
        back = (ImageView) findViewById(R.id.back);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        location = (LocationApplication) getApplication();
        mLocClient = ((LocationApplication) getApplication()).mLocationClient;
        City = ((LocationApplication) getApplication()).city;
        Street = ((LocationApplication) getApplication()).getStreet;
        District = ((LocationApplication) getApplication()).District;
        nearbySearch();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    GlobalVariables.city = "";
                    finish();
                }else {
                    GlobalVariables.city=array.get(position).get("name");
                    finish();
                }
            }
        });


//        mPoiSearch.searchInCity((new PoiCitySearchOption())
//                .city(District)
//                .keyword(Street)
//                .pageNum(0));
    }

    @Override
    public void onGetPoiResult(PoiResult result) {
        if (result == null
                || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(CityList.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
//            System.out.println("result:" + result.getAllPoi());
            hashMap = new HashMap<>();
            hashMap.put("name", "不显示");
            hashMap.put("city", "");
            array.add(hashMap);
            hashMap = new HashMap<>();
            hashMap.put("name", District);
            hashMap.put("city", "");
            array.add(hashMap);
            for (int i = 0; i < result.getAllPoi().size(); i++) {
                PoiInfo poiInfo = result.getAllPoi().get(i);
                hashMap = new HashMap<>();
//                System.out.println("poiInfo.address:" + poiInfo.address);
                hashMap.put("name", poiInfo.name);
                hashMap.put("city", poiInfo.address);
                array.add(hashMap);
            }
            getlist();
            return;
        }
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult result) {
        if (result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(CityList.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(CityList.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void nearbySearch() {
        double longitude, latitude;
        longitude = ((LocationApplication) getApplication()).longitude;
        latitude = ((LocationApplication) getApplication()).latitude;
        PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption();
        nearbySearchOption.location(new LatLng(latitude, longitude));
        nearbySearchOption.keyword("小区");
        nearbySearchOption.radius(1000);// 检索半径，单位是米
        nearbySearchOption.pageNum(0);
        mPoiSearch.searchNearby(nearbySearchOption);// 发起附近检索请求
    }

    public void getlist() {
        adapter = new CityListAdapter(array, CityList.this);
        list.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }
}
