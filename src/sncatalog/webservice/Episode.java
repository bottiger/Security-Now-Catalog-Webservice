package sncatalog.webservice;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sncatalog.shared.MobileEpisode;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class Episode{
	
	private MobileEpisode mobileEpisode;

	public Episode(int episode, String title, String link, 
			String pubDate, String description, String transscript,
			int duration) {
		DateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
		Date date = null;
		try {
			date = (Date)formatter.parse(pubDate);
		} catch (ParseException e) {
			e.setStackTrace(null);
		}
		
		this.mobileEpisode = new MobileEpisode(episode, title, link,
				date, description, transscript, duration);
	}
	
	public Entity toEntity() {
		Entity ep = new Entity("Episode");
		
		
		
		ep.setProperty("title", this.mobileEpisode.getTitle());
		ep.setProperty("link", this.mobileEpisode.getLink());
		ep.setProperty("description", new Text(this.mobileEpisode.getDescription()));
		ep.setProperty("transscript", new Text(this.mobileEpisode.getTransscript()));
		ep.setProperty("pubDate", this.mobileEpisode.getPubDate());
		ep.setProperty("duration", this.mobileEpisode.getDuration());
		ep.setProperty("episode", new Long(this.mobileEpisode.getEpisode()));
		
		return ep;
	}
	public boolean save() {
		return EpisodeDataStore.storeEpisode(this.toEntity());
	}

}
