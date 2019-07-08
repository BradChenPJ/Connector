package net.sensing;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetOM2M {
	String url;
	 String sendGet( String url) throws Exception{
		this.url = url;
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("X-M2M-Origin", "admin:admin");
		con.setRequestProperty("Content-Type","application/json");
		
		int responseCode = con.getResponseCode();
		
		BufferedReader in = new BufferedReader(
				new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		
		while ((inputLine = in.readLine()) != null){
			response.append(inputLine);
		}
		in.close();
		
		
		return response.toString();
	}

}
