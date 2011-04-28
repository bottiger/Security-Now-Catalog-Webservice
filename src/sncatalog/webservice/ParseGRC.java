package sncatalog.webservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.jasper.tagplugins.jstl.core.Url;
import java.util.regex.*;


import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

@SuppressWarnings("serial")
public class ParseGRC extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.getWriter().println("Scanning....\n");
		URL url = new URL("http://www.grc.com/securitynow.htm");
		
		URLConnection conn = url.openConnection();
    	BufferedReader in = new BufferedReader(
                            	new InputStreamReader(
                            		conn.getInputStream()));
    	String inputLine;
    	
    	int episodeNumber = 0;
    	String episodeDate = null;
    	String episodeTitle;
    	Text episodeDesc;
    	int episodeLength = 0;
    	
    	while ((inputLine = in.readLine()) != null) {
    		Pattern p = Pattern.compile("<a name=\"(\\d+)\"></a>"); // Search for episode number
    		Matcher m = p.matcher (inputLine);
    		if (m.find())
    			episodeNumber = Integer.parseInt(m.group(1)); //resp.getWriter().println(m.group(1) + "\n");
    		
    		if (episodeNumber > 0 && episodeNumber < 26) {
    			String test = "foo";
    			String bar = test;
    		}
    		
    		Pattern dateAndTime = Pattern.compile("<font size=1>Episode&nbsp;#(\\d+) \\| (.{11}) \\| (\\d+) min.</font></td></tr></table><table width=\"85%\" bgcolor=\"#999999\" border=0 cellpadding=1 cellspacing=0><tr><td>");
    		Matcher m2 = dateAndTime.matcher (inputLine);
    		
    		if (m2.find()) {
    			episodeDate = m2.group(2);
    			episodeLength = Integer.parseInt(m2.group(3));
    			
    			//resp.getWriter().println(" -> " + episodeDate + " -> " + episodeLength + "\n");
    		}
    		
    		Pattern titleAndDesc = Pattern.compile("<table width=\"100%\" bgcolor=\"#F8F8F8\" border=0 cellpadding=0 cellspacing=0><tr>" +
    				"<td><table width=\"100%\" border=0 cellpadding=0 cellspacing=10><tr><td colspan=6><font size=1><font size=2>" +
    				"<b>([^<]+)</b></font><br><img src=\"/image/transpixel.gif\" width=1 height=4 border=0><br>([^<]+)</font></td></tr><tr><td>" +
    				"(.)+");
    		Matcher m3 = titleAndDesc.matcher (inputLine);
    		
    		if (m3.find()) {
    			episodeTitle = m3.group(1);
    			episodeDesc = new Text(m3.group(2));
    			Text transscript = Transscripts.get(episodeNumber);
    			
    			//resp.getWriter().println(" -> " + episodeTitle + " -> " + episodeDesc + "\n");
    			String urlNumber;
    			if (episodeNumber < 10) 
    				urlNumber = "00" + Integer.toString(episodeNumber);
    			else if (episodeNumber < 100) 
    				urlNumber = "0" + Integer.toString(episodeNumber);
    			else
    				urlNumber = Integer.toString(episodeNumber);
    			
    			Episode e = new Episode(episodeNumber, episodeTitle, "https://www.grc.com/sn/sn-"+urlNumber+".mp3", episodeDate, episodeDesc, transscript, episodeLength);
    			e.save();
    			resp.getWriter().println(urlNumber + " saved!\n");
    		}
    	}
    	in.close();
		
	}
}
