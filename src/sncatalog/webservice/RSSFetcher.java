package sncatalog.webservice;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

import java.net.HttpURLConnection;
import java.net.URL;

public class RSSFetcher {
	
	private String url;
	private Document feed;
	
	public RSSFetcher(String Url){
		this.url = Url;
		SAXReader reader = new SAXReader();
        try {
			this.feed = reader.read(Url);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String rawFetch() throws Exception {
		return this.feed.asXML();
    }
	
	public List<Episode> fetchEpisode(int number) throws Exception {
		Element root = this.feed.getRootElement();
        //return new LinkedList();
		return treeWalk(root, number);
	}
	
	public List<Episode> episodeFetch() throws Exception {
		return this.fetchEpisode(0);
	}
	
	private List<Episode> treeWalk(Element element, int count) {
		List<Episode> elements = new LinkedList<Episode>();
		Episode last_inserted = null;
		
        for ( int i = 0, size = element.nodeCount(); i < size; i++ ) {
            Node node = element.node(i);
            if ( node instanceof Element ) {
            		elements.addAll((treeWalk( (Element) node, count )));
            } else {
            	
            	try {
            	String comment = element.valueOf("comments");
        		int episode_number = Integer.parseInt(comment.substring(comment.length()-3), comment.length()); // comment.length()-3), comment.length()
            	
            
                // do something....
            	if (element.getName() == "item" &&
            		(last_inserted == null ||
            		((Object)episode_number != (last_inserted.getEpisode())))) {
            		Element e = element;
            		
            		Transscript transscript = new Transscript(episode_number);
            		
            		Episode episode = new Episode(episode_number, 
            								e.valueOf("title"), 
            								e.valueOf("link"), 
            								e.valueOf("pubDate"), 
            								e.valueOf("duratation"), 
            								transscript, 
            								Integer.parseInt(e.valueOf("duratation"))
            								);
            		
            		elements.add(episode);
            		last_inserted = episode;
            	}
            	
            	
                } catch (Exception e) {
            		// no comment tag
            	}
            }
        }
        return elements;
    }
}
