package sncatalog.webservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sncatalog.shared.MobileEpisode;
import sncatalog.shared.Serializer;

@SuppressWarnings("serial")
public class PackRequester extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		String output = "";
		int type = Integer.parseInt(req.getParameter("type"));
		if (type == 1) {
			int id = Integer.parseInt(req.getParameter("id"));
			MobileEpisode me = EpisodeDataStore.getEpisode(id);
			output = Serializer.serialize(EpisodeDataStore.getEpisode(id));
			
			//resp.getWriter().print(me.getTransscript());
		} else if (type == 2) {
			output = Serializer.serialize(EpisodeDataStore.getNew());
		} else {
			output = "Invalid Episode";
		}
		
		resp.setContentType("text/plain");
		resp.getWriter().print(output);
		
	}


	
}