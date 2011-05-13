import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class DeserializeTester {

	/**
	 * @param args
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		URL url = new URL("http://localhost:8888/lite-episode/new");
		ArrayList test = (ArrayList) getRemoteObject(url);
		String foo = "bat";

	}
	
	private static Object getRemoteObject(URL url) throws IOException, ClassNotFoundException {
		URLConnection conn = url.openConnection();
		BufferedReader in = new BufferedReader(
				new InputStreamReader(
						conn.getInputStream()));
		String inputLine;
		String serializedObject = "";
		while ((inputLine = in.readLine()) != null) {
			serializedObject += inputLine;
		}
		
		return (Object)sncatalog.shared.Serializer.deserialize(serializedObject);
		
	}

}
