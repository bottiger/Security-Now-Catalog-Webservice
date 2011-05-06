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

	public List<Episode> fetchEpisodes() throws Exception {
		Element root = this.feed.getRootElement();
		return treeWalk(root);
	}

	private List<Episode> treeWalk(Element element) {
		List<Episode> episodes = new LinkedList<Episode>();
		Episode last_inserted = null;

		for ( int i = 0, size = element.nodeCount(); i < size; i++ ) {
			Node node = element.node(i);
			if ( node instanceof Element ) {
				episodes.addAll((treeWalk( (Element) node)));
			} else {

				// do something....
				if (element.getName() == "item") {

					String comment = element.valueOf("comments");
					int episode_number = Integer.parseInt(comment.substring(comment.length()-3, comment.length())); // comment.length()-3), comment.length()
					//int episode_number = 9; // Integer.parseInt
					if (last_inserted == null ||
							(episode_number != (last_inserted.getEpisode()))) {
						Element e = element;

						String title = e.valueOf("title");
						Transscript transscript = new Transscript(episode_number);
						String link = e.valueOf("link");
						String pubDate = e.valueOf("pubDate");
						String description = e.valueOf("itunes:summary");
						
						// parse duration from 1:38:42 into 99 min
						String dur = e.valueOf("itunes:duration");
						String[] times = dur.split(":");
						int duration = 0;
						for (int j = 0; j < 2; j++) // we only want hours:minutes
							duration += Integer.parseInt(times[j]) + (60 * (1-j));
						
						Episode episode = new Episode(episode_number, 
								title, 
								link, 
								pubDate, 
								description, 
								transscript, 
								duration
						);

						episodes.add(episode);
						last_inserted = episode;
					}
				}

			}
		}
		return episodes;
	}
}
