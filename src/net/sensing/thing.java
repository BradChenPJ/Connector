package net.sensing;

import org.json.JSONArray;
import org.json.JSONObject;

public class thing {
	
	String thing1;
	String thingCut;
	String thingString;
	String thingCon;
	thing(String thing){
		
		thing1 = thing;
		JSONObject thingMetadata = new JSONObject(thing1); 
		JSONObject thingM2m = thingMetadata.getJSONObject("m2m:cin");
		thingCon = thingM2m.getString("con");
		
		/*thingCut = thingCon.substring(1, thingCon.length()-1); */   //¥h±¼ÀY§À{}
	};

}
