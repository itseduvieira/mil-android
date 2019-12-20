package me.milliways.data;

/**
 * A Region is like a Criteria, it's a segment a chunk of Events 
 * on the map. Is useful when we have to limit which events will 
 * be downloaded, based on the actual screen  map image, to show 
 * only the relevant places.
 *  
 * @see EventRepository
 * @author Eduardo Vieira
 */
public class RegionCriteria {
	private Integer id;
	private String description; 
	
	private RegionCriteria(Integer id, String description) {
		this.id = id;
		this.description = description;
	}
	
	public static final RegionCriteria getCurrentRegion() {
		return new RegionCriteria(1, "Praia Grande");
	}

	public Integer getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}
}
