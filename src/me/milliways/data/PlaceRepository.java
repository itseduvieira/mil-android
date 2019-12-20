package me.milliways.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import me.milliways.entity.Place;

public class PlaceRepository {
	public static List<Place> getPlaces(RegionCriteria region) {
		return DataSynchronizer.places;
	}
	
	public static List<Place> getPlaces(RegionCriteria region, Place.Type eventType){
		List<Place> places = getPlaces(region);
		List<Place> returnPlaces = new ArrayList<Place>();
		Iterator<Place> iterator = places.iterator();
		
		while(iterator.hasNext()) {
			Place place = iterator.next();
			if(place.type.equals(eventType)) {
				returnPlaces.add(place);
			}
		}
			
		return returnPlaces;
	}

	public static Place getPlaceById(Integer id) {
		Place place = null;
		
		for(Place p : DataSynchronizer.places) {
			if(p.id.equals(id)) {
				place = p;
				break;
			}
		}
				
		return place; 
	}
}