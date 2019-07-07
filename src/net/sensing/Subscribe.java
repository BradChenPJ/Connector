package net.sensing;

public class Subscribe {
	String setSub(String resourceName, String url){
		String content = "{\"m2m:sub\":{\"rn\":\""+resourceName+"\",\"nu\":\""+url+"\",\"nct\":2}}";
		return content;
	}

}
