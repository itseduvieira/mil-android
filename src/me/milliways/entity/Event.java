package me.milliways.entity;


public class Event {
	public Integer id;
	public String title;
	public String shortDescription;
	public String imageUrl;
	public Integer place;

	@Override
	public String toString() {
		return "Event [title=" + title + ", place=" + place + "]";
	}
	
}
