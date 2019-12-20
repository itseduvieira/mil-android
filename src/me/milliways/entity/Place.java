package me.milliways.entity;

import java.util.ArrayList;
import java.util.List;

import me.milliways.R;

import com.google.android.maps.GeoPoint;

public class Place {
	public Integer id;
	public String title;
	public String street;
	public String number;
	public String city;
	public String state;
	public Double latitude;
	public Double longitude;
	public String imageUrl;
	public List<Event> events = new ArrayList<Event>();

	public Place.Type type;
	
	public enum Type {
		BAR(R.string.bar, 1), MOVIE(R.string.movie, 2), THEATER(R.string.theater, 3), NIGHT(R.string.night, 4);
		
		private Integer localizedNameId;
		
		private Integer id;
		
		Type(Integer localizedNameId, Integer id) {
			this.localizedNameId = localizedNameId;
			this.id = id;
		}
		
		public Integer getLocalizedNameId() {
			return localizedNameId;
		}
		
		public Integer getId() {
			return this.id;
		}
		
		public static Type parse(Integer id) {
			for(Type type : Type.values()) {
				if(type.getId() == id) {
					return type;
				}
			}
			
			throw new IllegalArgumentException("Id not exists");
		}
	}
	
	public GeoPoint getGeoPoint() {
		Double geoLat = latitude * 1E6;
		Double geoLng = longitude * 1E6;
		return new GeoPoint(geoLat.intValue(), geoLng.intValue());
	}

	@Override
	public String toString() {
		return "Place [type=" + type + ", title=" + title + ", street=" + street + 
				", number=" + number + ", city=" + city + ", state=" + state + ", events=" + events + "]";
	}
}