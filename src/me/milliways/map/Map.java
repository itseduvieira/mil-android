package me.milliways.map;

import java.util.Locale;

import me.milliways.R;
import me.milliways.data.DataSynchronizer;
import me.milliways.data.GeocodingHackAboveAndroid4;
import me.milliways.entity.Place;
import me.milliways.map.overlay.CustomItemizedOverlay;
import me.milliways.map.overlay.CustomOverlayItem;
import me.milliways.map.overlay.OptionalOverlay;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;

public class Map extends MapActivity {
	private static final Integer BASE_ZOOM_VIEW = 14;
	private static final String MY_POSITION_KEY = "currentPosition";
	private static final String BASE_LOCATION_DESCRIPTION = "São Paulo, Brasil";
	
	private CustomItemizedOverlay<OverlayItem> position;
	private MapView mapView;
	private GeoPoint myActualLocation;
	
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		
		setContentView(R.layout.map);
		
		setupUIControls();
		
		setupOptionalOverlays();
		
		setupLocation();
	}
	
	private void animateToLocation(GeoPoint location) {		
		MapController mapController = mapView.getController();
		mapController.animateTo(location);
	}
	
	private void setupOptionalOverlays() {
		MapView mapView = (MapView) findViewById(R.id.mapview);
		
		Place.Type type = Place.Type.MOVIE;
		Button toggle = (Button) findViewById(R.id.toggle_movie);
		Drawable imageOn = getResources().getDrawable(R.drawable.toggle_movie_on);
		Drawable imageOff = getResources().getDrawable(R.drawable.toggle_movie_off);
		Drawable imageMap = getResources().getDrawable(R.drawable.movie_map);
		
		OptionalOverlay movieOverlay = OptionalOverlay.buildNewOverlay(type, mapView, toggle, imageOn, imageOff, imageMap);
		DataSynchronizer.getInstance().addOverlayNotified(movieOverlay);
		
		type = Place.Type.BAR;
		toggle = (Button) findViewById(R.id.toggle_bar);
		imageOn = getResources().getDrawable(R.drawable.toggle_bar_on);
		imageOff = getResources().getDrawable(R.drawable.toggle_bar_off);
		imageMap = getResources().getDrawable(R.drawable.bar_map);
		
		OptionalOverlay barOverlay = OptionalOverlay.buildNewOverlay(type, mapView, toggle, imageOn, imageOff, imageMap);
		DataSynchronizer.getInstance().addOverlayNotified(barOverlay);
		
		type = Place.Type.NIGHT;
		toggle = (Button) findViewById(R.id.toggle_night);
		imageOn = getResources().getDrawable(R.drawable.toggle_night_on);
		imageOff = getResources().getDrawable(R.drawable.toggle_night_off);
		imageMap = getResources().getDrawable(R.drawable.music_map);
		
		OptionalOverlay nightOverlay = OptionalOverlay.buildNewOverlay(type, mapView, toggle, imageOn, imageOff, imageMap);
		DataSynchronizer.getInstance().addOverlayNotified(nightOverlay);
		
		type = Place.Type.THEATER;
		toggle = (Button) findViewById(R.id.toggle_theater);
		imageOn = getResources().getDrawable(R.drawable.toggle_theater_on);
		imageOff = getResources().getDrawable(R.drawable.toggle_theater_off);
		imageMap = getResources().getDrawable(R.drawable.theater_map);
		
		OptionalOverlay theaterOverlay = OptionalOverlay.buildNewOverlay(type, mapView, toggle, imageOn, imageOff, imageMap);
		DataSynchronizer.getInstance().addOverlayNotified(theaterOverlay);
		
		DataSynchronizer.getInstance().refreshData();
	}
	
	private void setupUIControls() {
		mapView = (MapView) findViewById(R.id.mapview);
		
		Button btnListMode = (Button) findViewById(R.id.btn_list_mode);
		btnListMode.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return false;
			}
		});
		
		final ToggleButton btnShowFilters = (ToggleButton) findViewById(R.id.btn_show_filters);
		btnShowFilters.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				findViewById(R.id.filters).setVisibility(LinearLayout.GONE);
				
				if(isChecked) {
					findViewById(R.id.applied_filters).setVisibility(LinearLayout.VISIBLE);
					btnShowFilters.setBackgroundDrawable(getResources().getDrawable(R.drawable.show_filters_on));
				} else {
					findViewById(R.id.applied_filters).setVisibility(LinearLayout.GONE);
					btnShowFilters.setBackgroundDrawable(getResources().getDrawable(R.drawable.show_filters_off));
				}
			}
		});
		
		Button btnRefreshFilters = (Button) findViewById(R.id.btn_refresh_filters);
		btnRefreshFilters.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				findViewById(R.id.applied_filters).setVisibility(LinearLayout.VISIBLE);
				findViewById(R.id.filters).setVisibility(LinearLayout.GONE);
				
				applyFilters();
				
				return false;
			}
		});
		
		Button btnEditFilters = (Button) findViewById(R.id.btn_applied_filters);
		btnEditFilters.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				findViewById(R.id.applied_filters).setVisibility(LinearLayout.GONE);
				findViewById(R.id.filters).setVisibility(LinearLayout.VISIBLE);
				return false;
			}
		});
		
		final AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.filter_city);
		textView.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus) {
					textView.setText(""); 
				}
			}
		});
		
		String[] cities = getResources().getStringArray(R.array.cities_array);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
		textView.setAdapter(adapter);
		
		mapView.setSatellite(false);
		mapView.getController().setZoom(BASE_ZOOM_VIEW);
		
		Button btnZoomIn = (Button) findViewById(R.id.btn_zoom_in_map);
		btnZoomIn.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mapView.getController().zoomIn();
				return false;
			}
		});
		
		Button btnZoomOut = (Button) findViewById(R.id.btn_zoom_out_map);
		btnZoomOut.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mapView.getController().zoomOut();
				return false;
			}
		});
	}
	
	private void applyFilters() {
		try {
			AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.filter_city);
			String inputCity = textView.getText().toString();
			GeoPoint visibleLocation = null;
			
			if(inputCity == null || inputCity.equals("")) {
				visibleLocation = myActualLocation;
				inputCity = BASE_LOCATION_DESCRIPTION;
			} else {
				if(Build.VERSION.SDK_INT < 14) {
					visibleLocation = GeocodingHackAboveAndroid4.getGeoPoint(GeocodingHackAboveAndroid4.getLocationInfo(inputCity));
				} else {
					Geocoder geocoder = new Geocoder(this, Locale.getDefault());
					Address address = geocoder.getFromLocationName(inputCity, 2).get(0);
					Double geoLat = address.getLatitude() * 1E6;
					Double geoLng = address.getLongitude() * 1E6;
					visibleLocation = new GeoPoint(geoLat.intValue(), geoLng.intValue());
				}
			}
			
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
			
			((TextView) findViewById(R.id.my_location_filter)).setText(inputCity);
			
			mapView.getController().setZoom(BASE_ZOOM_VIEW);
			animateToLocation(visibleLocation);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void setupLocation() {
		final LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		
		String provider = locationManager.getBestProvider(criteria, true);
		
		Location location = null;
			
		if(!locationManager.isProviderEnabled(provider)) {
			Toast t = Toast.makeText(Map.this,getResources().getString(R.string.provider_disabled_enable_it).replace("$provider$", provider), Toast.LENGTH_LONG);
            t.show();
		} else {
			location = locationManager.getLastKnownLocation(provider);
		}
		
		if(location == null) {
			provider = LocationManager.NETWORK_PROVIDER;
			location = locationManager.getLastKnownLocation(provider);
		}
		
		if(location != null) {
			Double geoLat = location.getLatitude() * 1E6;
			Double geoLng = location.getLongitude() * 1E6;
			myActualLocation = new GeoPoint(geoLat.intValue(), geoLng.intValue());
			animateToLocation(myActualLocation);
			setupMyLocationOverlay();
		} else {
			Toast t = Toast.makeText(Map.this,getResources().getString(R.string.location_could_not_be_determined), Toast.LENGTH_LONG);
            t.show();
		}
		
		locationManager.requestLocationUpdates(provider, 2000, 10, new LocationListener() {
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
				
			}
			
			@Override
			public void onProviderEnabled(String provider) {
				
			}
			
			@Override
			public void onProviderDisabled(String provider) {
				
			}
			
			@Override
			public void onLocationChanged(Location location) {
				Double geoLat = location.getLatitude() * 1E6;
				Double geoLng = location.getLongitude() * 1E6;
				myActualLocation = new GeoPoint(geoLat.intValue(), geoLng.intValue());
				
				setupMyLocationOverlay();				
			}
		});
	}
	
	private void setupMyLocationOverlay() {
		CustomOverlayItem overlayitem = new CustomOverlayItem(myActualLocation, "Aye Cap'n", "Yar Har!", "");
		final Drawable imHereIcon = this.getResources().getDrawable(R.drawable.imhere);
		
		if (position == null){
			position = new CustomItemizedOverlay<OverlayItem>(MY_POSITION_KEY, imHereIcon, mapView);
			position.addOverlay(overlayitem);
			mapView.getOverlays().add(position);
		} else {
			position.addOverlay(overlayitem);
		}
	}
	
	@Override
	protected boolean isRouteDisplayed() {
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.map_options_menu, menu);
		
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.refresh_data:
        	Toast t = Toast.makeText(Map.this, getResources().getString(R.string.refreshing_data), Toast.LENGTH_LONG);
            t.show();
        	DataSynchronizer.getInstance().refreshData();
        	return true;
        	
        case R.id.i_am_here:
        	animateToLocation(myActualLocation);
        	return true;
        	
        default:
        	return super.onOptionsItemSelected(item);
        }
    }
}
