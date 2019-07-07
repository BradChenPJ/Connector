package net.sensing;

import org.json.JSONObject;

public class datastream {
	
	String datastream;
	JSONObject datastream1;
	JSONObject sensor;
	JSONObject observedProperty;
	String datastreamCut;
	
	datastream(String datastream){
		
		this.datastream = datastream;
		JSONObject datastreamMetadata = new JSONObject(datastream); //拿到body內容放進JSONObject
		JSONObject datastream1M2m = datastreamMetadata.getJSONObject("m2m:cin");  //m2m:cin的value是JSONObject
		//把String抓出來再轉成Object
		String datastreamCon = datastream1M2m.getString("con");
		JSONObject datastreamConJson = new JSONObject(datastreamCon);
		//個別取出Datastream, Sensor, ObservedProperty的JSONObject
		datastream1 = datastreamConJson.getJSONObject("Datastream");
		sensor = datastreamConJson.getJSONObject("Sensor");
		observedProperty = datastreamConJson.getJSONObject("ObservedProperty");
		//拿掉外面的中括號
		/*datastreamCut = datastream1.substring(1, datastream1.length()-1);*/
	}
	
	

}
