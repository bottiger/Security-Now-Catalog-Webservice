package sncatalog.webservice;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class Episode{
	
	private int Episode;
	private String Title;
	private String Link;
	private String pubDate;
	private Text Description;
	private Text Transscript;
	private int Duration;

	// FIXME String[] category
	public Episode(int episode, String title, String link, 
			String pubDate, Text description, Text transscript,
			int duration) {
		this.Episode = episode;
		this.Title = title;
		this.Link = link;
		this.Description = description;
		this.Transscript = transscript;
		this.pubDate = pubDate;
		this.Duration = duration;
	}
	
	public Entity toEntity() {
		Entity ep = new Entity("Episode");
		
		ep.setProperty("title", this.Title);
		ep.setProperty("link", this.Link);
		ep.setProperty("description", this.Description);
		ep.setProperty("transscript", this.Transscript);
		ep.setProperty("pubDate", this.pubDate);
		ep.setProperty("duration", this.Duration);
		ep.setProperty("episode", this.Episode);
		
		return ep;
	}
	public boolean save() {
		return RssDataStore.storeEpisode(this.toEntity());
	}

}
