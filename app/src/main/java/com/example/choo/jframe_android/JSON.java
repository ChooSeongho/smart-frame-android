package com.example.choo.jframe_android;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.google.gson.Gson;

public class JSON {
	DataList dataList;
	JSONObject obj;
	JSONArray jsonArray = new JSONArray();
	
	// �޾ƿ� JSON �����͸� GSON���� �Ľ�
	public DataList parseJSONFromGSON(String data){
		dataList = new DataList();
//		JsonReader reader = new JsonReader(new StringReader(data));
//		reader.setLenient(true);
		dataList = new Gson().fromJson(data, dataList.getClass());
		return dataList;
	}
	
	// JSON �迭 �����
	public String makeJSONArray(int count, String type, String BLUMAC, String name, String image){
		JSONArray array = new JSONArray();
		obj = new JSONObject();
		for(int i = 0; i<count; i++){
			array.add(makeJSON(type, BLUMAC, name, image));
		}
		obj.put("list", array);
		return obj.toString();
	}
	
	public JSONObject makeJSON(String type, String BLUMAC, String name, String image){
		JSONObject json = new JSONObject();
		json.put("type", type);
		json.put("BLUMAC", BLUMAC);
		json.put("name", name);
		json.put("imageFile", image);
		return json;
	}

	public void addJSONArray(String type, String BLUMAC, String name, String image){
		jsonArray.add(makeJSON(type, BLUMAC, name, image));
	}

	public String getJSONArray(){
		obj = new JSONObject();
		obj.put("list", jsonArray);
		return obj.toString();
	}
	
	
}
