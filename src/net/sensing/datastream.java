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
		JSONObject datastreamMetadata = new JSONObject(datastream); //����body���e��iJSONObject
		JSONObject datastream1M2m = datastreamMetadata.getJSONObject("m2m:cin");  //m2m:cin��value�OJSONObject
		//��String��X�ӦA�নObject
		String datastreamCon = datastream1M2m.getString("con");
		JSONObject datastreamConJson = new JSONObject(datastreamCon);
		//�ӧO���XDatastream, Sensor, ObservedProperty��JSONObject
		datastream1 = datastreamConJson.getJSONObject("Datastream");
		sensor = datastreamConJson.getJSONObject("Sensor");
		observedProperty = datastreamConJson.getJSONObject("ObservedProperty");
		//�����~�������A��
		/*datastreamCut = datastream1.substring(1, datastream1.length()-1);*/
	}
	
	

}
