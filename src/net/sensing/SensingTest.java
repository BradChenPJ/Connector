package net.sensing;

import org.json.JSONArray;
import org.json.JSONObject;


public class SensingTest {

	public static void main(String[] args) throws Exception {
         
		String dataprofile = "{\"Things\":{\"name\": \"IndoorMonitoring_device\",\"description\": \"CSRSR's sensor\",\"properties\": {\"UID\": \"MY_DEVICE00001\"}},\"Locations\": [{\"name\": \"CSRSR\",\"description\": \"CSRSR\",\"encodingType\": \"application/vnd.geo+json\",\"location\": {\"type\": \"Point\",\"coordinates\": [24.967765,121.187035]}}],\"Datastreams\": [{\"name\": \"sensor1\",\"description\": \"Temperature\",\"observationType\": \"http://www.opengis.net/def/observationType/OGCOM/2.0/OM_Measurement\",\"unitOfMeasurement\": {\"name\": \"degree Celsius\",\"symbol\": \"degree Celsius\",\"definition\": \"http://unitsofmeasure.org/ucum.html#para-30\"},\"Sensor\": {\"name\": \"tempSensor\",\"description\": \"Thermometer\",\"encodingType\": \"text/html\",\"metadata\": \"DHT22\"},\"ObservedProperty\": {\"name\": \"air_temperature\",\"description\": \"Temperature of air in situ.\",\"definition\": \"http://mmisw.org/ont/ioos/parameter/air_temperature\"}},{\"name\": \"sensor2\",\"description\": \"Humidity\",\"observationType\": \"http://www.opengis.net/def/observationType/OGCOM/2.0/OM_Measurement\",\"unitOfMeasurement\": {\"name\": \"Percentage\",\"symbol\": \"%\",\"definition\": \"http://unitsofmeasure.org/ucum.html#para-29\"},\"Sensor\": {\"name\": \"humiSensor\",\"description\": \"Hygrometer\",\"encodingType\": \"text/html\",\"metadata\": \"DHT22\"},\"ObservedProperty\": {\"name\": \"humidity\",\"description\": \"Humidity of air in situ.\",\"definition\": \"http://mmisw.org/ont/ioos/parameter/relative_humidity\"}}]}";
		
		JSONObject Sensing = new JSONObject(dataprofile);
		String thing = Sensing.getJSONObject("Things").toString();
		String thingName = Sensing.getJSONObject("Things").get("name").toString();
		String location = Sensing.getJSONArray("Locations").getJSONObject(0).toString();
		JSONObject locationLocation = Sensing.getJSONArray("Locations").getJSONObject(0).getJSONObject("location");
		JSONObject FOIObject = new JSONObject();                               //宣告FOI Object 
		FOIObject = Sensing.getJSONArray("Locations").getJSONObject(0);
		FOIObject.remove("location");                                          //把location的location換成feature
		FOIObject = FOIObject.put("feature",locationLocation);
		JSONArray datastreamArray = Sensing.getJSONArray("Datastreams");
		int datastreamArrayLen = datastreamArray.length();               //有幾個Datastream
		String[] datastreamName = new String[datastreamArrayLen];        
		for(int i=1; i<datastreamArrayLen+1 ; i++){                        //根據有幾個Datastream創立多少個Datastream resourcename
			datastreamName[i-1] = "Datastream"+ i;
		}
		JSONArray datastreamObj = new JSONArray();
		for(int i=0; i<datastreamArrayLen; i++){
			String name = datastreamArray.getJSONObject(i).get("name").toString();
			String description = datastreamArray.getJSONObject(i).get("description").toString();
			String observationType = datastreamArray.getJSONObject(i).get("observationType").toString();
			JSONObject unit = datastreamArray.getJSONObject(i).getJSONObject("unitOfMeasurement");
			JSONObject sensor = datastreamArray.getJSONObject(i).getJSONObject("Sensor");
			JSONObject observedProperty = datastreamArray.getJSONObject(i).getJSONObject("ObservedProperty");
			JSONObject smallDatastreamObj = new JSONObject();
			JSONObject bigDatastreamObj = new JSONObject();
			smallDatastreamObj.put("name", name).put("description",description).put("observationType", observationType).put("unitOfMeasurement", unit);
			bigDatastreamObj.put("Datastream",smallDatastreamObj ).put("Sensor", sensor).put("ObservedProperty", observedProperty);
			datastreamObj.put(bigDatastreamObj);
			//datastreamObj[i].put("Datastream", name).put("Datastream", description).put("Datastream", observationType).put("Datastream", unit).put("Sensor", sensor).put("ObservedProperty", observedProperty);
		}
		
		devicePostOM2M devicePostOM2M = new devicePostOM2M();
		AE AE = new AE();
		Container container = new Container();
		ContentInstance contentInstance = new ContentInstance();
		Subscribe subscribe = new Subscribe();
		String locationName = "Locations";
		String Datastream_MetadataName = "Datastream_Metadata";
		String FOIName = "FeatureOfInterest";
		String AEURL = "http://192.168.1.100:8686/~/mn-cse";
		String thingConResourceName = "Thing_Metadata";
		String conURL = AEURL +"/mn-name/"+thingName;
		String cinURLThing = conURL+"/"+thingConResourceName;
		String cinURLLocation = conURL+"/"+locationName;
		String cinURLDS_Metadata = conURL+"/"+Datastream_MetadataName;
		String cinURLFOI = conURL+"/"+FOIName;
		String subURL = "http://192.168.1.100:8080/Sensing/service";
		
		String OM2MAEURL = devicePostOM2M.sendPost(AEURL, AE.setAEBody(thingName,"Sensing"), 2);
		String OM2MConURLThing = devicePostOM2M.sendPost(conURL, container.setConBody(thingConResourceName),3);
		String OM2MConURLLocation = devicePostOM2M.sendPost(conURL, container.setConBody(locationName),3);
		String OM2MConURLDS_Metadata = devicePostOM2M.sendPost(conURL, container.setConBody(Datastream_MetadataName),3);
		String OM2MConURLFOI = devicePostOM2M.sendPost(conURL, container.setConBody(FOIName),3);
		String OM2MCinURLThing = devicePostOM2M.sendPost(cinURLThing, contentInstance.setCinBody("Thing_Metadata", thing), 4);
		String OM2MCinURLLocation = devicePostOM2M.sendPost(cinURLLocation, contentInstance.setCinBody("Location", location), 4);
		String[] cinURLSub = new String[datastreamArrayLen];
		for(int i=0; i<datastreamObj.length(); i++){    //幾個datastream就要創幾個
			String OM2MCinURLDS_Metadata = devicePostOM2M.sendPost(cinURLDS_Metadata, contentInstance.setCinBody(datastreamName[i], datastreamObj.getJSONObject(i).toString()), 4);
			String OM2MConURLDS = devicePostOM2M.sendPost(conURL, container.setConBody(datastreamName[i]),3);
			cinURLSub[i] = conURL+"/"+datastreamName[i];
			String OM2MCinURLSub = devicePostOM2M.sendPost(cinURLSub[i], subscribe.setSub("sub", subURL),23);
		}
		String OM2MCinURLFOI = devicePostOM2M.sendPost(cinURLFOI, contentInstance.setCinBody("FOI", FOIObject.toString()), 4);
		//String OM2MAEURLSub = devicePostOM2M.sendPost(CinURLTasking, subscribe.setSub("sub", deviceURL), 23);
		////////////////////////////////////////////////////從oneM2M抓下來Post到STA
		
		/*String getThingURL = cinURLThing+"/Thing_Metadata";
		String getLocationURL = cinURLLocation+"/Location";
		String[] getDS_MetadataURL = new String[datastreamArrayLen];
		for(int i=0;i<datastreamArrayLen;i++){
			getDS_MetadataURL[i] = cinURLDS_Metadata+"/"+datastreamName[i];
		}
		String getFOIURL = cinURLFOI+"/FOI";
		String STAThingURL = "http://localhost:8080/STA3/v1.0/Things";
		String STAObservationURL = "http://localhost:8080/STA3/v1.0/Observations";
		
		GetOM2M getOM2M = new GetOM2M();
		OM2MPostSTA postSTA = new OM2MPostSTA();
		
		thing getOM2MThing = new thing(getOM2M.sendGet(getThingURL));                //個別將thing, location ,datastreams 的 JSONObject 抓下來
		location getOM2MLocation = new location(getOM2M.sendGet(getLocationURL));
		JSONArray DS_MetadataArray = new JSONArray();
		for(int i=0;i<datastreamArrayLen;i++){
			datastream getOM2MDS_Metadata = new datastream(getOM2M.sendGet(getDS_MetadataURL[i]));
			getOM2MDS_Metadata.datastream1.put("Sensor", getOM2MDS_Metadata.sensor).put("ObservedProperty",getOM2MDS_Metadata.observedProperty );
			DS_MetadataArray.put(getOM2MDS_Metadata.datastream1);
			
		}
		JSONObject thingConObj = new JSONObject(getOM2MThing.thingCon);
		JSONObject locationConObj = new JSONObject(getOM2MLocation.locationCon);
		JSONArray locationArray = new JSONArray();
		locationArray.put(locationConObj);
		thingConObj.put("Locations",locationArray).put("Datastreams", DS_MetadataArray);     //將thing, location, datastreams 的 JSONObject存進要post的Jsonobject
		String ThingURL = postSTA.sendPost(STAThingURL, thingConObj.toString()); 
		
		////////////////////////////////////////////傳Observation
		int num = 20;  //observation的值
		int num1 = 25;
		String[] cinURLObservation = new String[datastreamArrayLen];
		for(int i=0; i<datastreamArrayLen ; i++){
			cinURLObservation[i] = conURL+"/"+datastreamName[i];
		}
			String observationProfile = "{\"resultTime\":\"null\",\"result\":\""+ num +"\",\"FOIResource\":\""+ cinURLFOI+"/FOI" +"\"}";
			String datastreamURL = ThingURL+"/Datastreams";
			String OM2MCinURLObservation = devicePostOM2M.sendPost(cinURLObservation[0], contentInstance.setCinBody("Observation", observationProfile), 4);   //Observation之後要改
			observation observation = new observation(getOM2M.sendGet(cinURLObservation[0]+"/Observation"));     //Observation之後要改
			String FOIURL = observation.FOIURL;
			JSONObject FOIMetadata = new JSONObject(getOM2M.sendGet(FOIURL));
			JSONObject FOIM2m = FOIMetadata.getJSONObject("m2m:cin");
			String FOICon = FOIM2m.getString("con");
			JSONObject FOIConObj = new JSONObject(FOICon);
			JSONObject datastreamObject = new JSONObject(getOM2M.sendGet(datastreamURL));   //抓Datastream的ID
			JSONArray STAdatastreamArray = datastreamObject.getJSONArray("value");
			int[] datastreamID = new int[datastreamArrayLen];
			for(int i=0; i<datastreamArrayLen ; i++){
				datastreamID[i] = STAdatastreamArray.getJSONObject(i).getInt("@iot.id");
			}
			JSONObject observationObj = new JSONObject(observation.observationObjString);
			JSONObject datastreamIotid = new JSONObject();   //把@iot.id用JSONObject裝起來
			datastreamIotid.put("@iot.id", datastreamID[0]);
			observationObj.put("FeatureOfInterest", FOIConObj).put("Datastream", datastreamIotid);  //把observation, FOI, datastreamID 嵌在一起
			System.out.println(observationObj);
			
			String observationURL = postSTA.sendPost(STAObservationURL, observationObj.toString());*/
			
			/*String observationProfile1 = "{\"resultTime\":\"null\",\"result\":\""+ num1 +"\",\"FOIResource\":\""+ cinURLFOI+"/FOI" +"\"}";
			String OM2MCinURLObservation2 = devicePostOM2M.sendPost(cinURLObservation[1], contentInstance.setCinBody("Observation", observationProfile), 4);  //Observation之後要改
			observation observation1 = new observation(getOM2M.sendGet(cinURLObservation[1]+"/Observation"));    //Observation之後要改
			String observationURL1 = postSTA.sendPost(STAObservationURL, observation1.observationObj);*/
		
		
	}

}
