package sncatalog.webservice;
import java.util.Date;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

import sncatalog.shared.MobileEpisode;

public class MobileEpisodeWrapper {

	public static MobileEpisode get(int episode, String title, String link, 
			Date pubDate, String description, String transscript,
			int duration) {
		return new MobileEpisode(episode, title, link, pubDate, description, transscript, duration);
	}
	
	public static MobileEpisode get(Entity e, String description, String transscript) {
		return new MobileEpisode(getIntValue((Long) e.getProperty("episode")), 
				(String) e.getProperty("title"), 
				(String) e.getProperty("link"), 
				(Date) e.getProperty("pubDate"), 
				description, 
				transscript, 
				getIntValue((Long) e.getProperty("duration")));
	}
	
	public static MobileEpisode get(Entity e) {
		return get(e, "", "");
	}
	
	private static int getIntValue(Long l) {
		return l.intValue();
	}

}
