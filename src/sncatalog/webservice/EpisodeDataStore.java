package sncatalog.webservice;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sncatalog.shared.MobileEpisode;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;

public class EpisodeDataStore {
	
	private static final int defaultAmount = 10;
	
	public static boolean storeEpisode(Episode episode) {
		Entity e = episode.toEntity();
		if (!episodeExist(e)) {
			datastore().put(e);
			return true;
		}
		return false;
	}
	
	public static ArrayList<MobileEpisode> getNew() {
		return EpisodeDataStore.getNew(defaultAmount, false);
	}
	
	public static ArrayList<MobileEpisode> getNew(boolean lite) {
		return EpisodeDataStore.getNew(defaultAmount, lite);
	}
	
	public static ArrayList<MobileEpisode> getNew(int amount, boolean lite) {
		ArrayList<MobileEpisode> mobileEpisodes = new ArrayList<MobileEpisode>();
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Episode");
		q.addSort("episode", SortDirection.DESCENDING);
		
		PreparedQuery pq = datastore.prepare(q);
		
		for (Entity e: pq.asIterable(FetchOptions.Builder.withLimit(amount))) {
			
			Text de = new Text("");
			Text tr = new Text("");
			
			if (!lite) {
				de = (Text) e.getProperty("description");
				tr = (Text) e.getProperty("transscript");
			}
			
			mobileEpisodes.add(MobileEpisodeWrapper.get(e, de.getValue(), tr.getValue()));
		}
		return mobileEpisodes;
	}
	
	public static Episode getEpisode(Long number) {
		return EpisodeDataStore.getEpisode(number.intValue());
	}
	
	public static Episode getEpisode(int number) {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

		// The Query interface assembles a query
		Query q = new Query("Episode");
		q.addFilter("episode", Query.FilterOperator.EQUAL, number);
		
		PreparedQuery pq = datastore.prepare(q);
		
		Entity e = pq.asSingleEntity();
		
		Long episodeNumber = (Long) e.getProperty("episode");
		String title = (String) e.getProperty("title");
		String link = (String) e.getProperty("link");
		Text description = (Text) e.getProperty("description");
		Text transscript = (Text) e.getProperty("transscript");
		Date pubDate = (Date) e.getProperty("pubDate");
		Long duration = (Long) e.getProperty("duration");
		
		//Date date = new Date(pubDate*1000);
		
		Episode me = new Episode(episodeNumber.intValue(), 
				title, link, pubDate, description.getValue(), 
				new Transscript(transscript.getValue()), duration.intValue());
		
		return me;
	}
	
	private static DatastoreService datastore() {
		return DatastoreServiceFactory.getDatastoreService();
	}
	
	public static Boolean episodeExist(int number) {
		FetchOptions fo = FetchOptions.Builder.withLimit(1);
		Query q = new Query("Episode");
		q.addFilter("episode", Query.FilterOperator.EQUAL, number);
		PreparedQuery pq = datastore().prepare(q);
		return pq.countEntities(fo) > 0;
	}
	
	public static Boolean episodeExist(Entity e) {
		FetchOptions fo = FetchOptions.Builder.withLimit(1);
		Query q = new Query("Episode");
		q.addFilter("episode", Query.FilterOperator.EQUAL, e.getProperty("episode"));
		PreparedQuery pq = datastore().prepare(q);
		return pq.countEntities(fo) > 0;
		//return episodeExist(((Integer) e.getProperty("episode")).intValue());
	}

}
