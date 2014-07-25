package storage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.google.android.gms.maps.model.LatLng;

import navi4uni.places.NaviMarker;
import android.graphics.Bitmap;

public class Tuple implements Serializable{
	private static final long serialVersionUID = 1L;
	public HashMap<String, NaviMarker> markerList;
	public HashMap<String, Bitmap> images;
	public ArrayList<String> favoriteList;
	public LatLng dPosition = null;
	public Set<String> tagSet ;
	
	public Tuple(HashMap<String,NaviMarker> _markerList, HashMap<String, Bitmap> _images, ArrayList<String> _favoriteList, 
			LatLng position, Set<String> tags ){
		this.markerList = _markerList;
		this.images = _images;
		this.favoriteList = _favoriteList;
		this.dPosition = position;
		this.tagSet = tags;
	}
	
	public Tuple(){}
}

