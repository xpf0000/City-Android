package citycircle.com.OA.uitls;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;

public class StringtoJsontoo {
	public String getJson(Object object) {
		HashMap<String ,Object> hashMap=new HashMap<String, Object>();
		hashMap.put("list", object);
		String myjson=JSON.toJSONString(hashMap);
		return myjson;
		
	}

}
