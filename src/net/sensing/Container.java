package net.sensing;

public class Container {
	String setConBody(String resourceName){
		String ConBody = "{\"m2m:cnt\":{\"rn\":\""+resourceName+"\"}}";
		return ConBody;
	}

}
