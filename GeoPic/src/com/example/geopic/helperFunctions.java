package com.example.geopic;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class helperFunctions {


	private String urlString;
	private HttpURLConnection con = null;
	public String content = "";
	

	public helperFunctions(String url){
		this.urlString = url;
	}

	public String getContent(){
		try {
			URL url = new URL(urlString);
			con = (HttpURLConnection) url.openConnection();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Woops");
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("error");
			return null;
		}

		BufferedReader reader = null;
		String line = "";

		try {
			reader = new BufferedReader (new InputStreamReader(con.getInputStream()));
			while((line = reader.readLine()) != null){
				content += line;
				line = reader.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("this error");
			return null;
		}
		
		try {
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("or this error");
			return null;
		}
	
		return content;
	}

	
	public ArrayList formatContent(){
		
		ArrayList result = new ArrayList<ImageEntry>();
		content = content.substring(1, content.length()-2);
		String[] images = content.split("]");
		
		for (int i = 0; i < images.length; i++) {
			String temp = images[i];
			
			temp = temp.substring(temp.indexOf("\""));
					
			String[] params = temp.split("\", \"");

			result.add(new ImageEntry(params[0].substring(1), params[1], params[2].substring(0, params[2].length()-1)));
		}
		
		
		
		return result;
	}




}
