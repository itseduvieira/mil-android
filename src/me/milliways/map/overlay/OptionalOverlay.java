package me.milliways.map.overlay;

import java.util.List;

import me.milliways.data.PlaceRepository;
import me.milliways.data.RegionCriteria;
import me.milliways.entity.Place;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.google.android.maps.MapView;

public class OptionalOverlay {
	
	private Boolean isShow;
	private final Place.Type type;
	private final MapView mapView;
	private final Button toggle;
	private final Drawable imageOn;
	private final Drawable imageOff;
	private final Drawable imageMap;
	
	public static OptionalOverlay buildNewOverlay(final Place.Type type, final MapView mapView, final Button toggle, 
			final Drawable imageOn, final Drawable imageOff, final Drawable imageMap) {
		return new OptionalOverlay(type, mapView, toggle, imageOn, imageOff, imageMap);
	}
	
	private OptionalOverlay(final Place.Type type, final MapView mapView, final Button toggle, 
			final Drawable imageOn, final Drawable imageOff, final Drawable imageMap) {
		isShow = true;
		
		this.type = type;
		this.mapView = mapView;
		this.toggle = toggle;
		this.imageOn = imageOn;
		this.imageOff = imageOff;
		this.imageMap = imageMap;
		
		refreshDataAndView();
		
		toggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				isShow = !isShow;
				
				refreshDataAndView();
			}
		});
	}
	
	public void refreshDataAndView() {
		if (isShow) {
			fillOverlay();
		} else {
			removeOverlay();
		}
		
		toggle.postInvalidate();
		mapView.postInvalidate();
	}
	
	@SuppressWarnings("unchecked")
	private void fillOverlay() {
		isShow = true;
		
		List<Place> places = PlaceRepository.getPlaces(RegionCriteria.getCurrentRegion(), type);
		CustomItemizedOverlay<CustomOverlayItem> example = new CustomItemizedOverlay<CustomOverlayItem>(type.toString(), imageMap, mapView);
		CustomItemizedOverlay<CustomOverlayItem> customOverlays = example;
		
		if(!mapView.getOverlays().contains(customOverlays)) {
			mapView.getOverlays().add(customOverlays);
		} else {
			customOverlays = (CustomItemizedOverlay<CustomOverlayItem>) mapView.getOverlays().get(mapView.getOverlays().indexOf(customOverlays));
		}
		
		customOverlays.clear();
		
		for (Place place : places) {
			customOverlays.addOverlay(convertToOverlayItem(place));
		}
		
		toggle.setBackgroundDrawable(imageOn);
	}
	
	private void removeOverlay() {
		CustomItemizedOverlay<CustomOverlayItem> customOverlays = new CustomItemizedOverlay<CustomOverlayItem>(type.toString(), imageMap, mapView);
		mapView.getOverlays().remove(customOverlays);

		toggle.setBackgroundDrawable(imageOff);
	}
	
	private CustomOverlayItem convertToOverlayItem(Place place) {
		return new CustomOverlayItem(place.getGeoPoint(), place.title, place.street + ", " + place.number, place.imageUrl, place.id);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OptionalOverlay other = (OptionalOverlay) obj;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "OptionalOverlay [type=" + type + "]";
	}
}
