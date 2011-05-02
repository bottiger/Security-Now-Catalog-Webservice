package sncatalog.shared;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.Serializable;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class MobileEpisode implements Serializable {

	private Long Episode;
	private String Title;
	private String Link;
	private Date pubDate;
	private String Description;
	private String Transscript;
	private Long Duration;
	
	public MobileEpisode(int episode, String title, String link, 
			Date pubDate, String description, String transscript,
			int duration) {
		
		this.Episode = new Long(episode);
		this.Title = title;
		this.Link = link;
		this.Description = description;
		this.Transscript = transscript;
		this.pubDate = pubDate;
		this.Duration = new Long(duration);
	}
	
	public MobileEpisode(Entity e, String description, String transscript) {
		this.Episode = (Long) e.getProperty("episode");
		this.Title = (String) e.getProperty("title");
		this.Link = (String) e.getProperty("link");
		// Cannot do this because android doesn't have a Text object
		//this.Description = (String) e.getProperty("description");
		//this.Transscript = (String) e.getProperty("transscript");
		this.Description = description;
		this.Transscript = transscript;
		this.pubDate = (Date) e.getProperty("pubDate");
		this.Duration = (Long) e.getProperty("duration");
	}
	
	public MobileEpisode(Entity e) {
		this(e, "", "");
	}

	public Long getEpisode() {
		return Episode;
	}

	public String getTitle() {
		return Title;
	}

	public String getLink() {
		return Link;
	}

	public Date getPubDate() {
		return pubDate;
	}

	public String getDescription() {
		return Description;
	}

	public String getTransscript() {
		return Transscript;
	}

	public Long getDuration() {
		return Duration;
	}

	public void setEpisode(Long episode) {
		Episode = episode;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public void setLink(String link) {
		Link = link;
	}

	public void setPubDate(Date pubDate) {
		this.pubDate = pubDate;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public void setTransscript(String transscript) {
		Transscript = transscript;
	}

	public void setDuration(Long duration) {
		Duration = duration;
	}
	
}
