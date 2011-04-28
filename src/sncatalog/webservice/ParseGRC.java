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

import java.util.logging.Logger;
import java.util.regex.*;


import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

import java.util.Date;

@SuppressWarnings("serial")
public class ParseGRC extends HttpServlet {
	
	private long tic = 0;
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		final Logger log = Logger.getLogger(ParseGRC.class.getName());
		
		resp.getWriter().println("Scanning....\n");
		URL url = new URL("https://www.grc.com/securitynow.htm");
		
		URLConnection conn = url.openConnection();
    	BufferedReader in = new BufferedReader(
                            	new InputStreamReader(
                            		conn.getInputStream()));
    	String inputLine;
    	
    	int episodeNumber = 0;
    	String episodeDate = null;
    	String episodeTitle = "";
    	String episodeDesc = null;;
    	String transscript = null;
    	int episodeLength = 0;
    	boolean saveNow = false;
    	
    	long startTime = 0;
    	
    	while ((inputLine = in.readLine()) != null) {
    		
    		
    		Pattern p = Pattern.compile("<a name=\"(\\d+)\"></a>"); // Search for episode number
    		Matcher m = p.matcher (inputLine);
    		if (m.find())
    			episodeNumber = Integer.parseInt(m.group(1)); //resp.getWriter().println(m.group(1) + "\n");
    		
    		String test = null;
    		if (episodeNumber == 211) {
    			test = "foo";
    		}

    		if ((episodeNumber != 0 && 
    				!EpisodeDataStore.episodeExist(episodeNumber)) ||
    				test == "foo")
    		{
    			// Try to match all parameters in one line
    			Pattern allStyle = Pattern.compile("<font size=1>Episode&nbsp;#(\\d+) \\| (.{11}) \\| (\\d+) min." +
    					"</font></td></tr></table><table width=\"85%\" bgcolor=\"#999999\" border=0 " +
    					"cellpadding=1 cellspacing=0><tr><td><table width=\"100%\" bgcolor=\"#F8F8F8\" " +
    					"border=0 cellpadding=0 cellspacing=0><tr><td><table width=\"100%\" border=0 " +
    					"cellpadding=0 cellspacing=10><tr><td colspan=6><font size=1><font size=2>" +
    					"<b>([^<][^/]+)</b></font><br><img src=\"/image/transpixel.gif\" " +
    			"width=1 height=4 border=0><br>([^<][^/]+)</font></td></tr><tr>");
    			Matcher m4 = allStyle.matcher (inputLine);

    			if (m4.find()) {
    				episodeDate = m4.group(2);
    				episodeLength = Integer.parseInt(m4.group(3));
    				episodeTitle = m4.group(4);
    				episodeDesc = m4.group(5);
    				transscript = Transscripts.get(episodeNumber);
    				saveNow = true;
    			} else {



    				Pattern dateAndTime = Pattern.compile("<font size=1>Episode&nbsp;#(\\d+) \\| (.{11}) \\| (\\d+) min.</font></td></tr></table><table width=\"85%\" bgcolor=\"#999999\" border=0 cellpadding=1 cellspacing=0><tr><td>");
    				Matcher m2 = dateAndTime.matcher (inputLine);

    				if (m2.find()) {
    					episodeDate = m2.group(2);
    					episodeLength = Integer.parseInt(m2.group(3));

    					//resp.getWriter().println(" -> " + episodeDate + " -> " + episodeLength + "\n");
    				}
    				//toc();
    				Pattern titleAndDesc = Pattern.compile("<table width=\"100%\" bgcolor=\"#F8F8F8\" border=0 cellpadding=0 cellspacing=0><tr>" +
    						"<td><table width=\"100%\" border=0 cellpadding=0 cellspacing=10><tr><td colspan=6><font size=1><font size=2>" +
    				"<b>([^<][^/]+)</b></font><br><img src=\"/image/transpixel.gif\" width=1 height=4 border=0><br>([^</]+)<(.)+");
    				Matcher m3 = titleAndDesc.matcher (inputLine);

    				if (m3.find()) {
    					episodeTitle = m3.group(1);
    					episodeDesc = m3.group(2);
    					tic();
    					transscript = Transscripts.get(episodeNumber);
    					toc();
    					saveNow = true;
    				}
    			}

    			if (saveNow) {
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
    				System.out.println("Saved episode: " + urlNumber);
    				saveNow = false;
    				resp.getWriter().println(urlNumber + " saved!<br>\n");
    			}

    			//toc();
    		}
    	}
    	in.close();
		
	}
	
	private void tic() {
		this.tic = new Date().getTime();
	}
	
	private void toc() {
		long endTime = new Date().getTime();
		long loopTime = endTime-this.tic;
		if (loopTime > 100)
			System.out.println("loop time: " + Long.toString(loopTime));
	}
	
}
