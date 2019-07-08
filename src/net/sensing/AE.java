package net.sensing;

public class AE {
	String setAEBody(String resourceName, String lbl){
		String AEBody= "{\"m2m:ae\":{\"api\":\""+resourceName+"\",\"rr\":\"false\",\"lbl\":\""+lbl+"\",\"rn\":\""+resourceName+"\"}}";
		return AEBody;
		
	}
	

}
