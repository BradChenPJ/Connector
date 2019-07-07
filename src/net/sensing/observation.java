package net.sensing;

import org.json.JSONArray;
import org.json.JSONObject;

public class observation {
	
	String observation1;
	String FOIURL;
	String observationObjectCut;
	String observationObjString;
	
	observation(String observation){
		
		observation1 = observation;  //http�qoM2M��X�Ӫ����(String)
		JSONObject observationMetadata = new JSONObject(observation1);
		JSONObject observationM2m = observationMetadata.getJSONObject("m2m:cin");
		String observationCt = observationM2m.getString("ct");  //���Xobservation���Х߮ɶ�
		String observationCon = observationM2m.getString("con"); //��String��X�ӦA�নJSONArray
		observationCon = observationCon.replaceAll("\r|\n", "");  //��Ŧ�Ÿ��R���A��������|���Ŧ�Ÿ�
		JSONObject observationObject = new JSONObject(observationCon);
		FOIURL = observationObject.getString("FOIResource");            //���XFOI URL
		observationObject.remove("FOIResource");
		//��Х߮ɶ� 20180831T160829 �ܦ�2018-08-31T16:22:00.000Z �Φ�
		String[] arr = new String[5];
		arr[0] = observationCt.substring(0, 4);
		arr[1] = observationCt.substring(4, 6);
		arr[2] = observationCt.substring(6, 11);
		arr[3] = observationCt.substring(11, 13);
		arr[4] = observationCt.substring(13, 15);
		observationObject.put("phenomenonTime", arr[0]+"-"+arr[1]+"-"+arr[2]+":"+arr[3]+":"+arr[4]+".000Z");  //��Х߮ɶ��[�iObservation object��
		observationObject.remove("resultTime"); //�R���쥻��String null, ��J�u����null
		observationObject.put("resultTime",JSONObject.NULL);	
		observationObjString = observationObject.toString();
		
		
		
		//�����~�������A��
		/*observationObjectCut = observationObject.toString().substring(1,observationObject.toString().length()-1);*/
	}

}
