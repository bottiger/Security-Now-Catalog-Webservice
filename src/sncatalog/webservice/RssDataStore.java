package sncatalog.webservice;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class RssDataStore {
	
	public static boolean storeEpisode(Entity e) {
		if (!episodeExist(e)) {
			datastore().put(e);
			return true;
		}
		return false;
	}
	
	private static DatastoreService datastore() {
		return DatastoreServiceFactory.getDatastoreService();
	}
	
	private static Boolean episodeExist(Entity e) {
		FetchOptions fo = FetchOptions.Builder.withLimit(1);
		Query q = new Query("Episode");
		q.addFilter("episode", Query.FilterOperator.EQUAL, e.getProperty("episode"));
		PreparedQuery pq = datastore().prepare(q);
		return pq.countEntities(fo) > 0;
	}

}
