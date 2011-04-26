package sncatalog.webservice;
import java.net.*;
import java.io.*;

public class RSSFetcher {
	public static String fetch(String Url) throws Exception {
		String output = "";
		
		URL rssFeed = new URL(Url);
        URLConnection yc = rssFeed.openConnection();
        BufferedReader in = new BufferedReader(
                                new InputStreamReader(
                                yc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null) 
            //System.out.println(inputLine);
        	output += inputLine;
        in.close();
        
        return output;
    }
}
