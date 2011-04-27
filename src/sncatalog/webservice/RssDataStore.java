package sncatalog.webservice;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

public class RssDataStore {
	
	private static DatastoreService datastore() {
		return DatastoreServiceFactory.getDatastoreService();
	}
	
	public static void storeEpisode(Entity e) {
		datastore().put(e);
	}

}
