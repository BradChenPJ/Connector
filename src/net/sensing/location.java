package net.sensing;

import org.json.JSONObject;

public class location {
	
	String location;
	String locationCut;
	String locationCon;
	
	location(String location){
		
		this.location = location;
		JSONObject location1Metadata = new JSONObject(location);
		JSONObject locationM2m = location1Metadata.getJSONObject("m2m:cin");
		locationCon = locationM2m.getString("con");
		/*locationCut = locationCon.substring(1, locationCon.length()-1);*/
		
	}

}
