package navi4uni.favourite;

import java.util.ArrayList;

import com.google.android.gms.maps.model.Marker;

import navi4uni.map.MapFragment;
import navi4uni.places.NaviMarker;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

public class FavoritDialogListener implements android.content.DialogInterface.OnClickListener {

	private CharSequence[] items;

	public FavoritDialogListener(CharSequence[] items) {
		this.items = items;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		Log.i("ULUBIONE", "Click on: " + items[which]);
		ArrayList<NaviMarker> list = (ArrayList<NaviMarker>) NaviMarker.getMarkerList();
		for(NaviMarker m : list) {
			if(m.getName().equals(items[which])) {	//szuamy w liscie markera o tej nazwie
				MapFragment.setFocusOnLatLng(m.getLatLng(), MapFragment.mMap, true );
				//stawiamy na tej mapie nowy marker - na wypadek, gdyby byl ukryty i otwieramy chmurke
				Marker marker = MapFragment.mMap.addMarker(m.getMarker());
				marker.showInfoWindow();
			}
		}
	}
}