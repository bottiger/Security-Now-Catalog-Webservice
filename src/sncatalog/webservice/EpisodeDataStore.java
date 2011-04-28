package sncatalog.webservice;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Text;

public class EpisodeDataStore {
	
	public static boolean storeEpisode(Entity e) {
		if (!episodeExist(e)) {
			datastore().put(e);
			return true;
		}
		return false;
	}
	
	public static MobileEpisode getEpisode(int number) {
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
		
		MobileEpisode me = new MobileEpisode(episodeNumber.intValue(), 
				title, link, pubDate, description.toString(), 
				transscript.toString(), duration.intValue());
		
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
