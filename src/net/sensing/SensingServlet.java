package net.sensing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sensing.AE;
import net.sensing.Container;
import net.sensing.ContentInstance;
import net.sensing.GetOM2M;
import net.sensing.OM2MPostSTA;
import net.sensing.datastream;
import net.sensing.devicePostOM2M;
import net.sensing.location;
import net.sensing.observation;
import net.sensing.thing;

import org.json.JSONArray;
import org.json.JSONObject;

public class SensingServlet extends HttpServlet{
	int observationNum = 1;
	String OM2MObservationName = null;
	String[] cinURLObservation = null;
	String STAdatastreamURL = null;
	String ThingURL = null;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		try{
			//����n��
			/*String dataprofile = "{\"Things\":{\"name\": \"IndoorMonitoring_device\",\"description\": \"CSRSR's sensor\",\"properties\": {\"UID\": \"MY_DEVICE00001\"}},\"Locations\": [{\"name\": \"CSRSR\",\"description\": \"CSRSR\",\"encodingType\": \"application/vnd.geo+json\",\"location\": {\"type\": \"Point\",\"coordinates\": [24.967765,121.187035]}}],\"Datastreams\": [{\"name\": \"sensor1\",\"description\": \"Temperature\",\"observationType\": \"http://www.opengis.net/def/observationType/OGCOM/2.0/OM_Measurement\",\"unitOfMeasurement\": {\"name\": \"degree Celsius\",\"symbol\": \"degree Celsius\",\"definition\": \"http://unitsofmeasure.org/ucum.html#para-30\"},\"Sensor\": {\"name\": \"tempSensor\",\"description\": \"Thermometer\",\"encodingType\": \"text/html\",\"metadata\": \"DHT22\"},\"ObservedProperty\": {\"name\": \"air_temperature\",\"description\": \"Temperature of air in situ.\",\"definition\": \"http://mmisw.org/ont/ioos/parameter/air_temperature\"}},{\"name\": \"sensor2\",\"description\": \"Humidity\",\"observationType\": \"http://www.opengis.net/def/observationType/OGCOM/2.0/OM_Measurement\",\"unitOfMeasurement\": {\"name\": \"Percentage\",\"symbol\": \"%\",\"definition\": \"http://unitsofmeasure.org/ucum.html#para-29\"},\"Sensor\": {\"name\": \"humiSensor\",\"description\": \"Hygrometer\",\"encodingType\": \"text/html\",\"metadata\": \"DHT22\"},\"ObservedProperty\": {\"name\": \"humidity\",\"description\": \"Humidity of air in situ.\",\"definition\": \"http://mmisw.org/ont/ioos/parameter/relative_humidity\"}}]}";
			JSONObject Sensing = new JSONObject(dataprofile);
			String thing = Sensing.getJSONObject("Things").toString();
			String thingName = Sensing.getJSONObject("Things").get("name").toString();
			String location = Sensing.getJSONArray("Locations").getJSONObject(0).toString();
			JSONObject locationLocation = Sensing.getJSONArray("Locations").getJSONObject(0).getJSONObject("location");
			JSONObject FOIObject = new JSONObject();                               //�ŧiFOI Object 
			FOIObject = Sensing.getJSONArray("Locations").getJSONObject(0);
			FOIObject.remove("location");                                          //��location��location����feature
			FOIObject = FOIObject.put("feature",locationLocation);
			JSONArray datastreamArray = Sensing.getJSONArray("Datastreams");*/
			//int datastreamArrayLen = datastreamArray.length();               //���X��Datastream
			//String[] datastreamName = new String[datastreamArrayLen];        
			
			/*JSONArray datastreamObj = new JSONArray();
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
			}*/
		
			devicePostOM2M devicePostOM2M = new devicePostOM2M();
			AE AE = new AE();
			Container container = new Container();
			ContentInstance contentInstance = new ContentInstance();
			String locationName = "Locations";
			String Datastream_MetadataName = "Datastream_Metadata";
			String FOIName = "FeatureOfInterest";
			String AEURL = "http://140.115.111.188:8282/~/in-cse";   //m
			String conURL = AEURL +"/in-name";  //m  
			String cinURLThing = conURL+"/"+"Thing_Metadata";     
			String cinURLLocation = conURL+"/"+locationName;
			String cinURLDS_Metadata = conURL+"/"+Datastream_MetadataName;
			String cinURLFOI = conURL+"/"+FOIName;
			//
			
			GetOM2M getOM2M = new GetOM2M();
			OM2MPostSTA postSTA = new OM2MPostSTA();
			GetSTA getSTA = new GetSTA();
			String STAThingURL = "http://140.115.111.189:8080/STA/v1.0/Things";   //STA m
			String STAObservationURL = "http://140.115.111.189:8080/STA/v1.0/Observations";  //STA m
			String observationName = "Observation";
			/*Enumeration<String> send = request.getHeaderNames();     //���oHeader��Name
			while(send.hasMoreElements()){
				System.out.println("++++++");
				System.out.println(send.nextElement());
			}
			System.out.println("+++++");
			System.out.println(request.getHeader("accept")+"123");
			System.out.println(request.getHeader("x-m2m-origin"));
			System.out.println(request.getHeader("content-type"));
			System.out.println(request.getHeader("content-length"));
			System.out.println(request.getHeader("host"));
			System.out.println(request.getHeader("connection"));
			System.out.println(request.getHeader("user-agent"));
			System.out.println(request.getHeader("accept-encoding"));
			System.out.println("+++++++++");*/
			String contentType = request.getHeader("content-type");     //���o�ǨӪ�POST��Header�ӧP�_�O�ֶǨӪ�
			String contentLen = request.getHeader("content-length"); 
			int contentLength = Integer.parseInt(contentLen);
			String main = "text/plain;charset=UTF-8";                   //device�Ǫ�
			String sub = "application/json";                            //sub�Ǫ�
			/*System.out.println("*******HEADER*******");
			System.out.println("contentType: "+contentType);
			System.out.println("content-length: "+contentLength);
			System.out.println("*******HEADER*******");*/
			InputStream requestBody = request.getInputStream();
			StringBuilder inputStringBuilder = new StringBuilder();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(requestBody, "UTF-8"));
			String line = bufferedReader.readLine();
			while(line != null) {
				inputStringBuilder.append(line);
				inputStringBuilder.append('\n');
				line = bufferedReader.readLine();
			}
			System.out.println(inputStringBuilder.toString());
			if(contentType.equals(sub) /*&& contentLength>50 && contentLength<100*/ ){  //���F����U��OM2M�Asub�Ǩ�o�A�B�z�qOM2M��U��Ǩ�STA
				/*InputStream requestBody = request.getInputStream();
				StringBuilder inputStringBuilder = new StringBuilder();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(requestBody, "UTF-8"));
				String line = bufferedReader.readLine();
				while(line != null) {
					inputStringBuilder.append(line);
					inputStringBuilder.append('\n');
					line = bufferedReader.readLine();
				}*/
				JSONObject inputObj = new JSONObject(inputStringBuilder.toString());
				System.out.println(inputObj.toString());
				JSONObject sgn = inputObj.getJSONObject("m2m:sgn");
				JSONObject nev = sgn.getJSONObject("m2m:nev");
				JSONObject rep = nev.getJSONObject("m2m:rep");
				if (rep.has("m2m:ae")== true){
					JSONObject ae = rep.getJSONObject("m2m:ae");
					JSONArray lbl = ae.getJSONArray("lbl");
					if(lbl.getString(0).equals("Sensing")){
						String rn = ae.getString("rn");
						conURL = conURL+"/"+rn;
						System.out.println("****"+conURL);
						Thread.sleep(1000);                 //����1����container���͡A�o�˫���~��a��
						//�ഫ�{��
						
					
						/*String OM2MAEURL = devicePostOM2M.sendPost(AEURL, AE.setAEBody(thingName), 2);
						String OM2MConURLThing = devicePostOM2M.sendPost(conURL, container.setConBody(thingName),3);
						String OM2MConURLLocation = devicePostOM2M.sendPost(conURL, container.setConBody(locationName),3);
						String OM2MConURLDS_Metadata = devicePostOM2M.sendPost(conURL, container.setConBody(Datastream_MetadataName),3);
						String OM2MConURLFOI = devicePostOM2M.sendPost(conURL, container.setConBody(FOIName),3);
						String OM2MCinURLThing = devicePostOM2M.sendPost(cinURLThing, contentInstance.setCinBody("Thing_Metadata", thing), 4);
						String OM2MCinURLLocation = devicePostOM2M.sendPost(cinURLLocation, contentInstance.setCinBody("Location", location), 4);
						for(int i=0; i<datastreamObj.length(); i++){    //�X��datastream�N�n�дX��
							String OM2MCinURLDS_Metadata = devicePostOM2M.sendPost(cinURLDS_Metadata, contentInstance.setCinBody(datastreamName[i], datastreamObj.getJSONObject(i).toString()), 4);
							String OM2MConURL = devicePostOM2M.sendPost(conURL, container.setConBody(datastreamName[i]),3);
						}
						String OM2MCinURLFOI = devicePostOM2M.sendPost(cinURLFOI, contentInstance.setCinBody("FOI", FOIObject.toString()), 4);*/
					
						////////////////////////////////////////////////////�qoneM2M��U��Post��STA
						JSONObject DScon = new JSONObject(getOM2M.sendGet(conURL+"/Datastream_Metadata"));
						System.out.println("----"+DScon.toString()+"-----");
						int DSLen = DScon.getJSONObject("m2m:cnt").getInt("cni");   
						System.out.println("++++"+DSLen);
						int datastreamArrayLen = DSLen;
						System.out.println(datastreamArrayLen);
						String [] datastreamName = new String[datastreamArrayLen];
						for(int i=1; i<datastreamArrayLen+1 ; i++){                        //�ھڦ��X��Datastream�Хߦh�֭�Datastream resourcename
							datastreamName[i-1] = "Datastream"+ i;
						}
					
						String getThingURL = conURL+"/Thing_Metadata/Thing_Metadata";
						String getLocationURL = conURL+"/Locations/Location";
						String[] getDS_MetadataURL = new String[datastreamArrayLen];   
						for(int i=0;i<datastreamArrayLen;i++){    
							getDS_MetadataURL[i] = conURL+"/Datastream_Metadata/"+datastreamName[i];  
						}
						String getFOIURL = conURL+"/FeatureOfInterest/FOI";
					
						thing getOM2MThing = new thing(getOM2M.sendGet(getThingURL));                //�ӧO�Nthing, location ,datastreams �� JSONObject ��U��
						location getOM2MLocation = new location(getOM2M.sendGet(getLocationURL));
						JSONArray DS_MetadataArray = new JSONArray();
						for(int i=0;i<datastreamArrayLen;i++){    //
							datastream getOM2MDS_Metadata = new datastream(getOM2M.sendGet(getDS_MetadataURL[i]));
							getOM2MDS_Metadata.datastream1.put("Sensor", getOM2MDS_Metadata.sensor).put("ObservedProperty",getOM2MDS_Metadata.observedProperty );
							DS_MetadataArray.put(getOM2MDS_Metadata.datastream1);
						
						}
						JSONObject thingConObj = new JSONObject(getOM2MThing.thingCon);
						JSONObject locationConObj = new JSONObject(getOM2MLocation.locationCon);
						JSONArray locationArray = new JSONArray();
						locationArray.put(locationConObj);
						thingConObj.put("Locations",locationArray).put("Datastreams", DS_MetadataArray);     //�Nthing, location, datastreams �� JSONObject�s�i�npost��Jsonobject
						ThingURL = postSTA.sendPost(STAThingURL, thingConObj.toString());  //OM2M Post �� STA �� Thing(id)
					}else if (lbl.getString(0).equals("Tasking")){
						String rn = ae.getString("rn");
						String OM2MAEURL = "http://140.115.111.188:8282/~/in-cse/in-name/"+rn;  //m
						Thread.sleep(1000);
						String getThingURL = OM2MAEURL+"/Thing_Metadata/Thing_Metadata";
						String getLocationURL = OM2MAEURL+"/Locations/Location";
						String getTaskingCapaURL = OM2MAEURL+"/TaskingCapability_Metadata/TaskingCapability1";
						String TaskURL = OM2MAEURL+"/TaskingCapability1_Task";
						String STATaskingThing = "http://140.115.111.189:8080/STA/v1.0/Things";  //m
						
						thing getOM2MThing = new thing(getOM2M.sendGet(getThingURL));
						location getOM2MLocation = new location(getOM2M.sendGet(getLocationURL));
						JSONObject getTaskingCapa = new JSONObject(getOM2M.sendGet(getTaskingCapaURL));
						JSONObject TaskingCapaArray = new JSONObject(getTaskingCapa.getJSONObject("m2m:cin").getString("con"));
						
						JSONObject thingObject = new JSONObject(getOM2MThing.thingCon);            //device����ƶǵ�STA
						JSONArray locationArray = new JSONArray();
						JSONObject locationObj = new JSONObject(getOM2MLocation.locationCon);
						locationArray.put(locationObj);
						
						
						String messageBody = TaskingCapaArray.getJSONObject("oneM2MProtocol").getString("messageBody");  //TaskingCapa��messageBody���X��
						TaskingCapaArray.remove("oneM2MProtocol");                                  //�A�R�� 
						JSONObject httpProtocol = new JSONObject("{\"httpMethod\":\"POST\",\"absoluteResourcePath\":\""+TaskURL+"\",\"headers\":[{\"key\":\"X-M2M-Origin\",\"value\":\"admin:admin\"},{\"key\":\"Content-Type\",\"value\":\"application/json;ty=4\"}],\"messageDataType\":\"application/json;ty=4\"}");
						httpProtocol.put("messageBody", messageBody);                               //��messageBody�[�^�h      
						JSONArray httpProtocolArray = new JSONArray();
						httpProtocolArray.put(httpProtocol);
						TaskingCapaArray.put("httpProtocol", httpProtocolArray);                         //��httpProtocol�[�^�h
						JSONArray TaskingCapaArray1 = new JSONArray();                              //��JSONObject��TaskingCapa�ܦ�JSONArray 
						TaskingCapaArray1.put(TaskingCapaArray);
						thingObject.put("Locations", locationArray).put("TaskingCapabilities", TaskingCapaArray1);  //��ӭn�ǵ�STA�����
						String STAString  = thingObject.toString();
						String STATaskingURL = postSTA.sendPost(STATaskingThing, STAString);
					}
				}else if (rep.has("m2m:cin")== true){
					JSONObject cin = rep.getJSONObject("m2m:cin");      //�z�L�^�Ǫ�sub�A��L��pi�A�A�z�Lpi��la(�̷s��cin)
					String pi = cin.getString("pi");
					String piURL = "http://140.115.111.188:8282/~"+pi;
					JSONObject piCon = new JSONObject(getOM2M.sendGet(piURL));
					JSONObject piCnt = piCon.getJSONObject("m2m:cnt");
					System.out.println(piCnt);
					String pirn = piCnt.getString("rn");
					int pirnNum = Integer.parseInt(pirn.substring(10));  //pirnNum�i�H���D�O�ĴX��Datastream
					String observationLA = "http://140.115.111.188:8282/~"+piCnt.getString("la");
					observation observation = new observation(getOM2M.sendGet(observationLA));    
					String FOIURL = observation.FOIURL;
					System.out.println(FOIURL);
					JSONObject FOIMetadata = new JSONObject(getOM2M.sendGet(FOIURL));
					JSONObject FOIM2m = FOIMetadata.getJSONObject("m2m:cin"); 
					String FOICon = FOIM2m.getString("con");
					JSONObject FOIConObj = new JSONObject(FOICon);
					System.out.println(ThingURL+"/Datastreams");					
					JSONObject STAdatastreamObject = new JSONObject(getSTA.sendGet(ThingURL+"/Datastreams"));   //��Datastream��ID			
					System.out.println(STAdatastreamObject.toString());
					JSONArray STAdatastreamArray = STAdatastreamObject.getJSONArray("value");
					int[] datastreamID = new int[STAdatastreamArray.length()];
					for(int i=0; i<STAdatastreamArray.length() ; i++){
						datastreamID[i] = STAdatastreamArray.getJSONObject(i).getInt("@iot.id");
					}
					System.out.println(datastreamID[0]);
					JSONObject observationObj = new JSONObject(observation.observationObjString);
					JSONObject datastreamIotid = new JSONObject();   //��@iot.id��JSONObject�˰_��
					datastreamIotid.put("@iot.id", datastreamID[pirnNum-1]); //pirnNum-1�A�ҥHDatastream1�N�O1-1=0�AdatastreamID[0]�N��datastream1
					observationObj.put("FeatureOfInterest", FOIConObj).put("Datastream", datastreamIotid);  //��observation, FOI, datastreamID �O�b�@�_
					String observationURL = postSTA.sendPost(STAObservationURL, observationObj.toString());
					
				}
				
			
			//response
			
			}else if(contentType.equals(main)){   //��Observation�A�ȿ�J��POST OM2M�A��ڼg�bdevice
				/*InputStream requestBody = request.getInputStream();
				StringBuilder inputStringBuilder = new StringBuilder();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(requestBody, "UTF-8"));
				String line = bufferedReader.readLine();
				while(line != null) {
					inputStringBuilder.append(line);
					inputStringBuilder.append('\n');
					line = bufferedReader.readLine();
				}*/
				String num = inputStringBuilder.toString();  //observation����
				num = num.replace("\n", "");
				cinURLObservation = new String[2];  //datastreamLen=2 �g��
				String[] datastreamName22 = new String[2];
				datastreamName22[0] = "Datastream1";   //�g��
				datastreamName22[1] = "Datastream2";
				for(int i=0; i<2 ; i++){      //2�g��
					cinURLObservation[i] = conURL+"/IndoorMonitoring_device/"+datastreamName22[i];  //URL�n�g��
				}
				String observationProfile = "{\"resultTime\":\"null\",\"result\":\""+ num +"\",\"FOIResource\":\""+ conURL+"/IndoorMonitoring_device/"+FOIName+"/FOI" +"\"}";  //URL�n�g��
				STAdatastreamURL = ThingURL+"/Datastreams";
				OM2MObservationName = observationName + observationNum;   //OM2MObservationName����g�bdevice�n��
				observationNum = observationNum + 1;
				String OM2MCinURLObservation = devicePostOM2M.sendPost(cinURLObservation[0], contentInstance.setCinBody(OM2MObservationName, observationProfile), 4);   //cinURLObservation[0]�N��Datastream1    
				
				
				/*String observationProfile1 = "{\"resultTime\":\"null\",\"result\":\""+ num1 +"\",\"FOIResource\":\""+ cinURLFOI+"/FOI" +"\"}";
				String OM2MCinURLObservation2 = devicePostOM2M.sendPost(cinURLObservation[1], contentInstance.setCinBody("Observation", observationProfile), 4);  //Observation����n��
				observation observation1 = new observation(getOM2M.sendGet(cinURLObservation[1]+"/Observation"));    //Observation����n��
				String observationURL1 = postSTA.sendPost(STAObservationURL, observation1.observationObj);*/
				response.setHeader("Content-Type", "text/plain");
				response.getWriter().write("OK");

				
			}/*else if(contentType.equals(sub) && contentLength>600){
				
			}*/
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
