package net.sensing;

import org.json.JSONObject;

public class ContentInstance {
	String setCinBody(String resourceName, String con){
		JSONObject mainObject = new JSONObject();
		JSONObject body = new JSONObject();
		body.put("cnf",resourceName).put("rn",resourceName).put("con", con);
		mainObject.put("m2m:cin",body);
		String a = mainObject.toString();
		return a;
	}

}
