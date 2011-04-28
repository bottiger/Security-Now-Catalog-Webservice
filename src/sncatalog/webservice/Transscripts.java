package sncatalog.webservice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import com.google.appengine.api.datastore.Text;

public class Transscripts {
	
	public static String get(int episode_number) {
		URL url;
		String output_data = "";
		try {
			String number;
			if (episode_number < 100) 
				number = "0" + Integer.toString(episode_number);
			else
				number = Integer.toString(episode_number);
				
			url = new URL("https://www.grc.com/sn/sn-" + number + ".txt");
		
			URLConnection conn = url.openConnection();
        	BufferedReader in = new BufferedReader(
                                	new InputStreamReader(
                                		conn.getInputStream()));
        	String inputLine;
        	while ((inputLine = in.readLine()) != null) 
        	output_data += inputLine;
        	in.close();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return output_data;
	}

}
