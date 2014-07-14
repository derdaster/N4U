package navi4uni.places;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.graphics.Bitmap;

public class Tuple implements Serializable{
	private static final long serialVersionUID = 1L;
	public ArrayList<NaviMarker> markerList;
	public HashMap<String, Bitmap> images;
	
	public Tuple(ArrayList<NaviMarker> _markerList, HashMap<String, Bitmap> _images){
		this.markerList = _markerList;
		this.images = _images;
	}
}

