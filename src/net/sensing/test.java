package net.sensing;

import org.json.JSONArray;
import org.json.JSONObject;

public class test {

	public static void main(String[] args) throws Exception {
	
		GetOM2M getOM2M = new GetOM2M();
		OM2MPostSTA postSTA = new OM2MPostSTA();
		observation observation = new observation(getOM2M.sendGet("http://127.0.0.1:8282/~/in-cse/in-name/IndoorMonitoring_device/Datastream1/Observation1"));     //cinURLObservation[0]代表Datastream1
		String FOIURL = observation.FOIURL;
		JSONObject FOIMetadata = new JSONObject(getOM2M.sendGet(FOIURL));
		JSONObject FOIM2m = FOIMetadata.getJSONObject("m2m:cin"); 
		String FOICon = FOIM2m.getString("con");
		JSONObject FOIConObj = new JSONObject(FOICon);
		JSONObject STAdatastreamObject = new JSONObject(getOM2M.sendGet("http://localhost:8080/STA3/v1.0/Things(126)/Datastreams"));   //抓Datastream的ID
		JSONArray STAdatastreamArray = STAdatastreamObject.getJSONArray("value");
		int[] datastreamID = new int[STAdatastreamArray.length()];
		for(int i=0; i<STAdatastreamArray.length() ; i++){
			datastreamID[i] = STAdatastreamArray.getJSONObject(i).getInt("@iot.id");
		}
		System.out.println(datastreamID[0]);
		JSONObject observationObj = new JSONObject(observation.observationObjString);
		JSONObject datastreamIotid = new JSONObject();   //把@iot.id用JSONObject裝起來
		datastreamIotid.put("@iot.id", datastreamID[0]); //代表Datastream1
		observationObj.put("FeatureOfInterest", FOIConObj).put("Datastream", datastreamIotid);  //把observation, FOI, datastreamID 嵌在一起
		
		String observationURL = postSTA.sendPost("http://localhost:8080/STA3/v1.0/Observations", observationObj.toString());
		
	}

}
