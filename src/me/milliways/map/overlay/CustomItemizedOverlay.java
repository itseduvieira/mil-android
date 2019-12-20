/***
 * Copyright (c) 2011 readyState Software Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package me.milliways.map.overlay;

import java.util.ArrayList;

import me.milliways.detail.PlaceDetail;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.android.maps.MapView;
import com.google.android.maps.OverlayItem;
import com.readystatesoftware.mapviewballoons.BalloonItemizedOverlay;
import com.readystatesoftware.mapviewballoons.BalloonOverlayView;

public class CustomItemizedOverlay<Item extends OverlayItem> extends BalloonItemizedOverlay<CustomOverlayItem> {

	private ArrayList<CustomOverlayItem> myOverlays = new ArrayList<CustomOverlayItem>();
	private Context context;
	private final String key;
	
	public CustomItemizedOverlay(String key, Drawable defaultMarker, MapView mapView) {
		super(boundCenter(defaultMarker), mapView);
		this.key = key;
		this.context = mapView.getContext();
		populate();
	}

	public void addOverlay(CustomOverlayItem overlay) {
	    myOverlays.add(overlay);
	    populate();
	}
	
	public void clear() {
		myOverlays.clear();
	}

	@Override
	protected CustomOverlayItem createItem(int i) {
		return myOverlays.get(i);
	}

	@Override
	public int size() {
		return myOverlays.size();
	}

	@Override
	protected boolean onBalloonTap(int index, CustomOverlayItem item) {
		Intent details = new Intent(context, PlaceDetail.class);
		details.putExtra("placeId", item.getPlaceId());
		context.startActivity(details);
		return true;
	}

	@Override
	protected BalloonOverlayView<CustomOverlayItem> createBalloonOverlayView() {
		return new CustomBalloonOverlayView<CustomOverlayItem>(getMapView().getContext(), getBalloonBottomOffset());
	}
	
	@Override
	public String toString() {
		return "CustomItemizedOverlay [key=" + key + ", myOverlays=" + myOverlays + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomItemizedOverlay other = (CustomItemizedOverlay) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
}
