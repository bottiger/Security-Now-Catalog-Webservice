package sncatalog.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.appengine.api.datastore.Text;

public class Transscript {
	
	private int episode;
	private String transsscript;
	
	public Transscript(int episode_number) {
		this.episode = episode_number;
	}
	
	public Transscript(String transscript) {
		this.transsscript = transscript;
	}
	 
	public String valueOf() {
		return this.get();
	}
	
	private String get() {
		URL url;
		try {
			String number;
			if (this.episode < 100) 
				number = "0" + Integer.toString(episode);
			else
				number = Integer.toString(episode);
				
			url = new URL("https://www.grc.com/sn/sn-" + number + ".txt");
		
			URLConnection conn = url.openConnection();
        	BufferedReader in = new BufferedReader(
                                	new InputStreamReader(
                                		conn.getInputStream()));
        	String inputLine;
        	while ((inputLine = in.readLine()) != null) 
        	this.transsscript += inputLine;
        	in.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return this.transsscript;
	}

}
