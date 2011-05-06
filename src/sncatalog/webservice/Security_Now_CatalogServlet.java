package sncatalog.webservice;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.*;
import org.dom4j.Element;

import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class Security_Now_CatalogServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String snRssFeed = "http://leoville.tv/podcasts/sn.xml";
		RSSFetcher rss = new RSSFetcher(snRssFeed);
		
		resp.setContentType("text/plain");
		//resp.getWriter().println("Hello, world");
		
		List<Episode> episodes = null;
		try {
			episodes = rss.fetchEpisode(2);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		String xml ="";
		for (Episode e: episodes) {
			xml += e.getTitle() + "\n==================\n";
			if (EpisodeDataStore.storeEpisode(e))
				resp.getWriter().println("Saved\n");
			else
				resp.getWriter().println("Not Saved\n");
		}
		
		//resp.getWriter().println(xml);
		
	}
}
