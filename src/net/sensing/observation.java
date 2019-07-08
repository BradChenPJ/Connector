package net.sensing;

import org.json.JSONArray;
import org.json.JSONObject;

public class observation {
	
	String observation1;
	String FOIURL;
	String observationObjectCut;
	String observationObjString;
	
	observation(String observation){
		
		observation1 = observation;  //http從oM2M抓出來的資料(String)
		JSONObject observationMetadata = new JSONObject(observation1);
		JSONObject observationM2m = observationMetadata.getJSONObject("m2m:cin");
		String observationCt = observationM2m.getString("ct");  //取出observation的創立時間
		String observationCon = observationM2m.getString("con"); //把String抓出來再轉成JSONArray
		observationCon = observationCon.replaceAll("\r|\n", "");  //把空行符號刪除，不知為何會有空行符號
		JSONObject observationObject = new JSONObject(observationCon);
		FOIURL = observationObject.getString("FOIResource");            //取出FOI URL
		observationObject.remove("FOIResource");
		//把創立時間 20180831T160829 變成2018-08-31T16:22:00.000Z 形式
		String[] arr = new String[5];
		arr[0] = observationCt.substring(0, 4);
		arr[1] = observationCt.substring(4, 6);
		arr[2] = observationCt.substring(6, 11);
		arr[3] = observationCt.substring(11, 13);
		arr[4] = observationCt.substring(13, 15);
		observationObject.put("phenomenonTime", arr[0]+"-"+arr[1]+"-"+arr[2]+":"+arr[3]+":"+arr[4]+".000Z");  //把創立時間加進Observation object裡
		observationObject.remove("resultTime"); //刪除原本的String null, 放入真正的null
		observationObject.put("resultTime",JSONObject.NULL);	
		observationObjString = observationObject.toString();
		
		
		
		//拿掉外面的中括號
		/*observationObjectCut = observationObject.toString().substring(1,observationObject.toString().length()-1);*/
	}

}
