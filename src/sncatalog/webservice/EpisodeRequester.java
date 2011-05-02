package sncatalog.webservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sncatalog.shared.MobileEpisode;
import sncatalog.shared.Serializer;

@SuppressWarnings("serial")
public class EpisodeRequester extends HttpServlet {
	
	static final String HEXES = "0123456789ABCDEF";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		resp.setContentType("text/plain");
		String serializedObject = null;
		
		try {
			int episodeId = Integer.parseInt(req.getParameter("id"));
			
			if (episodeId > 0) {
				MobileEpisode me = EpisodeDataStore.getEpisode(episodeId);
				serializedObject = Serializer.serialize(me);
			}
		} catch (NumberFormatException e) {
			String type = req.getParameter("lite");
			List<MobileEpisode> mes = null;
			if (type.equals("1")) {
				mes = EpisodeDataStore.getNew(true);
			} else {
				mes = EpisodeDataStore.getNew();
			}
			serializedObject = Serializer.serialize(mes);
		}
		resp.getWriter().print(serializedObject);
	}
}
