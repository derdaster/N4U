package navi4uni.places;

import java.util.LinkedList;

import navi4uni.gui.R;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class NaviMarkers {



/**
Funkcja która pobiera informacje o budynkach politechniki z XML albo JSON
 * @param  bez parametrów
 * @return      Lista przetworzonych markerów
 */
	
	public LinkedList<MarkerOptions> getMarkers(GoogleMap map)
	{
		LinkedList<MarkerOptions> myList= new LinkedList<MarkerOptions>();
		//Wczytywanie z XML albo JSON'a i dorzucanie do naszej mapy
		LatLng pos = new LatLng(51.103677,17.08492);
		MarkerOptions marker = new MarkerOptions().position(pos).title("T-16").draggable(false).snippet("Akademik").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_pwr));
		myList.add(marker);

		pos = new LatLng(51.103192,17.084641);
		marker = (new MarkerOptions().position(pos).title("T-15").draggable(false).snippet("Akademik").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_pwr)));
		myList.add(marker);
		
		pos = new LatLng(51.103094,17.085429);
		marker = (new MarkerOptions().position(pos).title("T-19").draggable(false).snippet("Akademik").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_pwr)));
		myList.add(marker);
		
		pos = new LatLng(51.103522,17.086481);
		marker = (new MarkerOptions().position(pos).title("T-17").draggable(false).snippet("Akademik").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_pwr)));
		myList.add(marker);
		
		pos = new LatLng(51.104054,17.086336);
		marker = (new MarkerOptions().position(pos).title("T-18/T-22").draggable(false).snippet("Akademik").icon(BitmapDescriptorFactory.fromResource(R.drawable.logo_pwr)));
		myList.add(marker);
		
		for(MarkerOptions m : myList)
		{
			map.addMarker(m); //wrzucam je na mapę
		}
		
		return myList;
	}
}
