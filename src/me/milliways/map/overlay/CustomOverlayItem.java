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

import com.google.android.maps.GeoPoint;
import com.google.android.maps.OverlayItem;

public class CustomOverlayItem extends OverlayItem {

	protected String mImageURL;
	private String title;
	private Integer placeId;
	
	public CustomOverlayItem(GeoPoint point, String title, String snippet, String imageURL, Integer placeId) {
		super(point, title, snippet);
		this.mImageURL = imageURL;
		this.title = title;
		this.placeId = placeId;
	}
	
	public CustomOverlayItem(GeoPoint point, String title, String snippet, String imageURL) {
		super(point, title, snippet);
		this.mImageURL = imageURL;
		this.title = title;
	}
	
	public Integer getPlaceId() {
		return placeId;
	}



	public void setPlaceId(Integer placeId) {
		this.placeId = placeId;
	}



	public String getImageURL() {
		return mImageURL;
	}

	public void setImageURL(String imageURL) {
		this.mImageURL = imageURL;
	}

	@Override
	public String toString() {
		return "CustomOverlayItem [title=" + title + "]";
	}
}
