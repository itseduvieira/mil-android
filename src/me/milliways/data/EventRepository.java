package me.milliways.data;

import java.util.List;

import me.milliways.entity.Event;
import me.milliways.entity.Place;

public class EventRepository {
	public static List<Event> getEvents(RegionCriteria region) {
		return DataSynchronizer.events;
	}
	
	public static List<Event> getEvents(RegionCriteria region, Place.Type eventType){
		return getEvents(region);
	}
}