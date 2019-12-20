package me.milliways.detail;

import me.milliways.R;
import me.milliways.data.PlaceRepository;
import me.milliways.entity.Place;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class PlaceDetail extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.place_detail);
		
		Place place = PlaceRepository.getPlaceById(getIntent().getIntExtra("placeId", 0));

		TextView title = ((TextView) findViewById(R.id.place_title));
		title.setText(place.title);
		TextView address = ((TextView) findViewById(R.id.place_address));
		address.setText(place.street + ", " + place.number);
	}
}
