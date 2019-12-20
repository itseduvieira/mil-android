package me.milliways.data;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import me.milliways.entity.Event;
import me.milliways.entity.Place;
import me.milliways.map.overlay.OptionalOverlay;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

public class DataSynchronizer {
	public static final String MILLIWAYS_DATA_URL = "http://www.milliways.me/data";
	public static final String EVENTS_DATA = "events";
	public static final String PLACES_DATA = "places";
	
	public static final List<Place> places = new ArrayList<Place>();
	public static final List<Event> events = new ArrayList<Event>();
	
	private List<OptionalOverlay> overlaysForNotification;
	
	private static DataSynchronizer dataSynchronizer;
	
	public static DataSynchronizer getInstance() {
		if(dataSynchronizer == null) {
			dataSynchronizer = new DataSynchronizer();
		}
			
		return dataSynchronizer;
	}
	
	private DataSynchronizer() {
		overlaysForNotification = new ArrayList<OptionalOverlay>();
	}
	
	public void addOverlayNotified(OptionalOverlay overlay) {
		overlaysForNotification.add(overlay);
	}
	
	public void removeOverlayNotified(OptionalOverlay overlay) {
		overlaysForNotification.remove(overlay);
	}
	
	public void refreshData() {
		new Handler().post(new Runnable() {
		    @Override
		    public void run() {
		    	places.clear();
				places.addAll(getPlaces());
				
				events.clear();
				events.addAll(getEvents());
				
				for(OptionalOverlay overlay : overlaysForNotification) {
					overlay.refreshDataAndView();
				}
		    }
	    });
	}
	
	private List<Event> getEvents() {
		List<Event> events = new ArrayList<Event>();
		
		try {
			HttpResponse response = executeRequest(EVENTS_DATA);
			JSONArray jsons = parseEventResponse(parseResponse(response).toString());
			
			for(int i = 0; i < jsons.length(); i++) {
				JSONObject json = jsons.getJSONObject(i);
				Event event = convertJSONToEvent(json);
				events.add(event);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return events;
	}
	
	private List<Place> getPlaces() {
		List<Place> places = new ArrayList<Place>();
		
		try {
			HttpResponse response = executeRequest(PLACES_DATA);
			JSONArray jsons = parsePlaceResponse(parseResponse(response).toString());
			
			for(int i = 0; i < jsons.length(); i++) {
				JSONObject json = jsons.getJSONObject(i); 
				Place place = convertJSONToPlace(json);
				JSONArray jsonsEvents = json.getJSONArray("events");
				for(int j = 0; j < jsonsEvents.length(); j++) {
					place.events.add(convertJSONToEvent(jsonsEvents.getJSONObject(j)));
				}
				places.add(place);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return places;
	}
	
	private Event convertJSONToEvent(JSONObject json) {
		Event event = new Event();
		
		try {
			event.id = json.getInt("id");
			event.title = json.getString("title");
			event.shortDescription = json.getString("short_description");
			event.imageUrl = json.getString("image_url");
			event.place = json.getInt("place"); 
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return event;
	}
	
	private Place convertJSONToPlace(JSONObject json) {
		Place place = new Place();
		
		try {
			place.id = json.getInt("id");
			place.title = json.getString("name");
			place.street = json.getString("street");
			place.number = json.getString("number");
			place.city = json.getString("city");
			place.state = json.getString("state");
			place.latitude = json.getDouble("latitude");
			place.longitude = json.getDouble("longitude");
			place.imageUrl = json.getString("image_url");
			place.type = Place.Type.parse(json.getInt("type"));
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return place;
	}
	
	private HttpResponse executeRequest(String element) {
		HttpResponse response = null;
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet request = new HttpGet(MILLIWAYS_DATA_URL + "/" + element);
			List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			postParameters.add(new BasicNameValuePair("location", "latitude;longitude"));
	        response = httpClient.execute(request);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return response;
	}
	
	private StringBuffer parseResponse(HttpResponse response) {
		StringBuffer stringBuffer = new StringBuffer("");
		
		try {
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(response.getEntity().getContent()));
			   
		    String lineSeparator = System.getProperty("line.separator");
		    String line = "";
		   
		    while ((line = bufferedReader.readLine()) != null) {
		    	stringBuffer.append(line + lineSeparator); 
		    }
		   
		    bufferedReader.close();
		   
		    
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return stringBuffer;
	}
	
	private JSONArray parseEventResponse(String json) {
		JSONArray jsons = null;
		
		try {
			jsons = new JSONObject(json).getJSONObject("result").getJSONArray("events");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		
		return jsons;
	}
	
	private JSONArray parsePlaceResponse(String json) {
		JSONArray jsons = null;
		
		try {
			jsons = new JSONObject(json).getJSONObject("result").getJSONArray("places");
		} catch (JSONException ex) {
			ex.printStackTrace();
		}
		
		return jsons;
	}
}
