package sncatalog.webservice;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.datastore.Text;

public class TransscriptUpdate {
	
	private final static int fetchAmount = 3;
	
	public static void update() {
		DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
		Query q = new Query("Episode");
		q.addSort("episode", SortDirection.DESCENDING);
		PreparedQuery pq = datastore.prepare(q);
		
		for (Entity e: pq.asIterable(FetchOptions.Builder.withLimit(fetchAmount))) {
			Long episodeNumber = (Long)e.getProperty("episode");
			Episode episode = (Episode) EpisodeDataStore.getEpisode(episodeNumber);
			if (isTransscript(episode.getTransscript())) {
				Transscript t = new Transscript(episode.getEpisode().intValue());
				if (isTransscript(t.valueOf())) {
					episode.setTransscript(t.valueOf());
					episode.save();
				}
			}
		}
	}
	
	private static boolean isTransscript(String transscript) {
		if (transscript.isEmpty())
			return false;
		return !transscript.substring(0, 8).equals("<!DOCTYPE");
	}

}
