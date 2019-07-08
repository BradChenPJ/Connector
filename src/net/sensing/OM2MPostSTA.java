package net.sensing;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;

public class OM2MPostSTA {
	String postURL;
	String postJsonString;
	
	String sendPost(String postURL, String postJsonString) throws Exception{
		
		this.postURL = postURL;
		this.postJsonString = postJsonString;
		URL url = new URL(postURL);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		/*String json = "{\"name\":\"things\",\"description\":\"123\"}";*/
		
		con.setRequestMethod("POST");
		/*con.setRequestProperty("X-M2M-Origin", "admin:admin");
		con.setRequestProperty("Content-Type","application/json");*/
		con.setRequestProperty("Accept", "application/json;charset=utf-8");
        con.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
		con.setDoInput(true);
		con.setDoOutput(true);
		
		DataOutputStream dos = null;
		dos = new DataOutputStream(con.getOutputStream());
		
		
		dos.write(postJsonString.getBytes(Charset.forName("utf-8")));
		dos.flush();
		dos.close();
		
		int responseCode = con.getResponseCode();
		String id = con.getHeaderField("location");
		System.out.println("\nSebding 'POST' request to URL: " + url);
		
		System.out.println("Response Code:" + responseCode);
		System.out.println("\nlocation: " + id);
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null){
			response.append(inputLine);
		}
		in.close();
		
		return id;
	}

}
