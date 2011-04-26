package sncatalog.webservice;

import java.io.IOException;
import javax.servlet.http.*;

@SuppressWarnings("serial")
public class Security_Now_CatalogServlet extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String snRssFeed = "http://leoville.tv/podcasts/sn.xml";
		
		resp.setContentType("text/plain");
		//resp.getWriter().println("Hello, world");
		try {
			resp.getWriter().println(RSSFetcher.fetch(snRssFeed));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
