package sncatalog.webservice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheFactory;
import net.sf.jsr107cache.CacheManager;

import sncatalog.shared.MobileEpisode;
import sncatalog.shared.Serializer;

@SuppressWarnings("serial")
public class EpisodeRequester extends HttpServlet {
	
	static final String HEXES = "0123456789ABCDEF";
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		Cache cache = null;

        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
            // ...
        }
		
		resp.setContentType("text/plain");
		String serializedObject = null;
		
		try {
			int episodeId = Integer.parseInt(req.getParameter("id"));
			
			if (episodeId > 0) {
				if (cache.containsKey(episodeId)) {
					serializedObject = (String) cache.get(episodeId);
				} else {
					Episode e = EpisodeDataStore.getEpisode(episodeId);
					serializedObject = Serializer.serialize(e.toMobile());
					cache.put(episodeId, serializedObject);
				}
			}
		} catch (NumberFormatException e) {
			String type = req.getParameter("lite");
			ArrayList<MobileEpisode> mes = null;
			if (type.equals("1")) {
				if (cache.containsKey("lite3")) {
					mes = (ArrayList<MobileEpisode>) cache.get("lite3");
				} else {
					mes = EpisodeDataStore.getNew(true);
					cache.put("lite3", mes);
				}
			} else if (type.equals("2")) {
				if (cache.containsKey("all6")) {
					mes = (ArrayList<MobileEpisode>) cache.get("all6");
				} else {
					mes = EpisodeDataStore.getAll();
					cache.put("all6", mes);
				}
			} else {
				if (cache.containsKey("full2")) {
					mes = (ArrayList<MobileEpisode>) cache.get("full2");
				} else {
					mes = EpisodeDataStore.getNew();
					cache.put("full2", mes);
				}
			}
			serializedObject = Serializer.serialize(mes);
		}
		resp.getWriter().print(serializedObject);
	}
}
