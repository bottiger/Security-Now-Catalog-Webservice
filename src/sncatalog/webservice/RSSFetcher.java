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
	
	public List<Entity> episodeFetch(int number) throws Exception {
		Element root = this.feed.getRootElement();
        //return new LinkedList();
		return treeWalk(root, number);
	}
	
	public List<Entity> episodeFetch() throws Exception {
		return this.episodeFetch(0);
	}
	
	private List<Entity> treeWalk(Element element, int count) {
		List<Entity> elements = new LinkedList<Entity>();
		Entity last_inserted = null;
		
        for ( int i = 0, size = element.nodeCount(); i < size; i++ ) {
            Node node = element.node(i);
            if ( node instanceof Element ) {
            		elements.addAll((treeWalk( (Element) node, count )));
            } else {
            
                // do something....
            	if (element.getName() == "item" &&
            		(last_inserted == null ||
            		element.valueOf("guid") != last_inserted.getProperty("guid"))) {
            		Element e = element;
            		String test = e.valueOf("description");
            		//output += element.valueOf("guid") + "\n";
            		Entity ep = new Entity("Episode");
            		
            		String comment = e.valueOf("comment");
            		int episode_number = Integer.parseInt(comment.substring(comment.length()-3));
            		
            		// FIXME will fail before ep100
            		URL url;
            		String output_data = "";
					try {
						url = new URL("http://www.grc.com/sn/sn-" + episode_number + ".txt");
					
						URLConnection conn = url.openConnection();
                    	BufferedReader in = new BufferedReader(
                                            	new InputStreamReader(
                                            		conn.getInputStream()));
                    	String inputLine;
                    	while ((inputLine = in.readLine()) != null) 
                    	output_data = inputLine;
                    	in.close();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
            		Text transscript = new Text(output_data);
            		
            		ep.setProperty("title", e.valueOf("title"));
            		ep.setProperty("link", e.valueOf("link"));
            		ep.setProperty("description", new Text(e.valueOf("description")));
            		ep.setProperty("author", e.valueOf("author"));
            		ep.setProperty("pubDate", e.valueOf("pubDate"));
            		ep.setProperty("category", e.valueOf("category"));
            		ep.setProperty("comment", e.valueOf("comment"));
            		ep.setProperty("enclosure", e.valueOf("enclosure"));
            		ep.setProperty("guid", e.valueOf("guid"));
            		ep.setProperty("subtitle", e.valueOf("subtitle"));
            		ep.setProperty("summary", e.valueOf("summary"));
            		ep.setProperty("duratation", e.valueOf("duratation"));
            		ep.setProperty("transscript", transscript);
            		
            		elements.add(ep);
            		last_inserted = ep;
            	}
            }
        }
        return elements;
    }
}
