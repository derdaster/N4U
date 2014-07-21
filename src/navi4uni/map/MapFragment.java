package navi4uni.map;

/*	CLASS DESCRIPTION:
 * 	MapFragment class is fragment view of application Navi4Uni. It contains, show and edit Google Map (API v2) which is main 
 * 	content. Map object shows view of area and full extensions from google e.g. GooglePlaces, 
 * 	the view can be changed dynamically by attribute - CameraPosition (latitude, longitude, zoom, tilt).
 * 	For locally custromize map view google share Markers - its a pins, which can be placed on map 
 * 	with attributes (name, snippet, position, photo). In Navi4Uni, Google's Markers are expanded
 * 	by additional data @see BuildingMarker and PlacesMarker classes.
 *  Class MapFragment is used to storage map by composition and provided method to manage of Markers.
 * 
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import navi4uni.gui.MainActivity;
import navi4uni.gui.R;
import navi4uni.places.BuildingMarker;
import navi4uni.places.FavoriteListener;
import navi4uni.places.Favorites;
import navi4uni.places.NaviMarker;
import navi4uni.places.PlacesMarker;
import storage.Tuple;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;


public class MapFragment extends Fragment implements LocationListener, LocationSource {

	private static View view;
	public static GoogleMap mMap = null;
	public static LatLng currentCameraPosition = null;
	// atrybuty dla kodu okreslajacego aktualna pozycje
	private static LocationManager locationManager;
	private static boolean locationManagerIsEnabled = false;
	private static boolean GPSManagerIsEnabled = false;
	private static OnLocationChangedListener locationChangedListener;

	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;

	public static ArrayList<NaviMarker> currentMarker;
	public static ImageView favIcon;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle) 
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (container == null) {
			return null;
		}
		try {
			view = (RelativeLayout) inflater.inflate(R.layout.fragment_main,
					container, false);
		} catch (Exception e) {
			Log.e("", "", e);
		}
		
		setUpMapIfNeeded();
		
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);
		if(locationManager != null){
			GPSManagerIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            locationManagerIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
			if(GPSManagerIsEnabled)
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						MIN_TIME, MIN_DISTANCE, this);
			else if(locationManagerIsEnabled)
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000L, 10F, this);
		}
			
		try{
			if(!currentMarker.isEmpty()){
				fillMap(currentMarker);
			}
		}catch(Exception e){
			fillMap(NaviMarker.markerList);
		}
		
		//Default position form XML
		if(NaviMarker.defaultPosition != null && MapFragment.currentCameraPosition == null){
			MapFragment.setFocusOnLatLng(NaviMarker.defaultPosition, MapFragment.mMap, true);
		}
		else if (currentCameraPosition != null){
			MapFragment.setFocusOnLatLng(MapFragment.currentCameraPosition, MapFragment.mMap, false);
		}
		
		addMarkerDialogListener();		
		
		return view;
	}
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		setUpMapIfNeeded();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (mMap != null) {
			try {
				MainActivity.fragmentManager
						.beginTransaction()
						.remove(MainActivity.fragmentManager
								.findFragmentById(R.id.location_map)).commit();
			} catch (Exception e) {
				Log.e("", "", e);
			}
			mMap = null;
		}
	}
	
	/** Method assignes map from resources if current mMap reference is empty.
	 * 
	 *  Quality : q1 (verified code by other developer)
	 */
	public void setUpMapIfNeeded() {
		if (mMap == null) {
			mMap = ((SupportMapFragment) MainActivity.fragmentManager
					.findFragmentById(R.id.location_map)).getMap();
			mMap.setMyLocationEnabled(true);
			mMap.setLocationSource(this);
		}
	}

	/* set Focus on position passed by parameter, it cases change map view to required position with animation
	 * @param position - position to be focus.
	 * map - reference to map, which will be focused to position
	 * 
	 * Quality: q2 (fixed code accordingly to the developer remarks). 
	 */
	public static void setFocusOnLatLng(LatLng position, GoogleMap map, boolean animate ){
		CameraUpdate cameraUpdate;
		cameraUpdate = CameraUpdateFactory.newCameraPosition
				(new CameraPosition(position, 17, 0, 0)); 
		if(animate)
			map.animateCamera(cameraUpdate);
		else
			map.moveCamera(cameraUpdate);
	}
	
	/* Method generate window of dialog with data of markers. It add Listerner to MarkerInfoWindow.
	 * If user choose marker on map, application will show information window contains filds from Marker class. 
	 * 
	 * Quality : q2 (fixed code accordingly to the developer remarks)
	 */
	private void addMarkerDialogListener() {
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
        public void onInfoWindowClick(Marker m) {
                    NaviMarker temp = null;
            for(NaviMarker nm: NaviMarker.markerList){
                    if(nm.getName().equals(m.getTitle())) {
                            Log.i("logi","ZNALEZIONO: "+nm.getName());
                            temp = nm;
                    }
            }

            Log.i("logi", "Wcisnieto infoWindows markera: " + m.getTitle());
           
            if (temp instanceof BuildingMarker) {
                    Log.i("logi", "TYP: BUILDINGMARKER");                  
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.buildingmarker_dialog);
                            dialog.setTitle(temp.getName());
                            
                            // Ustawianie danych
                            TextView buildingDescription = (TextView) dialog.findViewById(R.id.building_description);
                            buildingDescription.setText(temp.getDescription()); 
                            TextView buildingOpenHours = (TextView) dialog.findViewById(R.id.building_openhours);
                            if(temp.getOpenHours().equals(" "))
                            {
                                    buildingOpenHours.setHeight(0);
                                    buildingOpenHours.setWillNotDraw(true);
                            }
                            else {
                                    buildingOpenHours.setText("Godziny otwarcia: " + temp.getOpenHours());                                                 
                            }
                            TextView buildingAddress = (TextView) dialog.findViewById(R.id.building_address);
                            if(temp.getAddress().equals(" "))
                            {
                                    buildingAddress.setHeight(0);
                                    buildingAddress.setWillNotDraw(true);
                            }
                            else {
                                    buildingAddress.setText("Adress: " + temp.getAddress());                               
                            }
                            TextView buildingPhone = (TextView) dialog.findViewById(R.id.building_phone);
                            if(temp.getPhone().equals(" "))
                            {
                                    buildingPhone.setHeight(0);
                                    buildingPhone.setWillNotDraw(true);
                            }
                            else {
                                    buildingPhone.setText("Numer telefonu: " + temp.getPhone());
                                    Linkify.addLinks(buildingPhone, Linkify.PHONE_NUMBERS);
                            }
                            TextView buildingWww = (TextView) dialog.findViewById(R.id.building_www);
                            if(temp.getWww().equals(" "))
                            {
                                    buildingWww.setHeight(0);
                                    buildingWww.setWillNotDraw(true);

                            }
                            else {
                                    buildingWww.setText("Strona internetowa: " + temp.getWww());
                                    Linkify.addLinks(buildingWww, Linkify.WEB_URLS);
                            }
                            TextView buildingMail = (TextView) dialog.findViewById(R.id.building_mail);
                            if(temp.getMail().equals(" "))
                            {
                                    buildingMail.setHeight(0);
                                    buildingMail.setWillNotDraw(true);
                            }
                            else {
                                    buildingMail.setText("Adres e-mail: " + temp.getMail());
                                    Linkify.addLinks(buildingMail, Linkify.EMAIL_ADDRESSES);                       
                            }                      
                            TextView buildingDepartment = (TextView) dialog.findViewById(R.id.building_department);
                            if(((BuildingMarker) temp).getDepartment().equals(" "))
                            {
                                    buildingDepartment.setHeight(0);
                                    buildingDepartment.setWillNotDraw(true);
                            }
                            else {
                                    buildingDepartment.setText("Wydział: " + ((BuildingMarker)temp).getDepartment());                             
                            }
                            TextView buildingDirections = (TextView) dialog.findViewById(R.id.building_directions);
                            if(((BuildingMarker) temp).getDirections().equals(" "))
                            {
                                    buildingDirections.setHeight(0);
                                    buildingDirections.setWillNotDraw(true);
                            }
                            else {
                                    buildingDirections.setText("Wskazówki dojazdu: " + ((BuildingMarker)temp).getDirections());                           
                            }
                            ImageView image = (ImageView) dialog.findViewById(R.id.building_photo);
                            Bitmap img = NaviMarker.images.get(temp.getName());
                            if(img != null)
                            	image.setImageBitmap(img);						//pobrane zdjecie
                            else
                            	image.setImageResource(R.drawable.logo_pwr);	//defaultowo logo PWr
                            

                            favIcon = (ImageView) dialog.findViewById(R.id.favorite);
                            if(Favorites.checkIfFavorite(temp.getName())){
                            	favIcon.setImageResource(R.drawable.ulubionetrue);
                            } else {
                            	favIcon.setImageResource(R.drawable.ulubione);
                            }
                            		
                            favIcon.setOnClickListener(new FavoriteListener(temp));
                            
                            Button showEventsButton = (Button) dialog.findViewById(R.id.dialogShowEventsButton);
                            showEventsButton.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    	Toast.makeText(getActivity().getApplicationContext(), "Wydarzenia w tym budynku",
                                    			   Toast.LENGTH_SHORT).show();
                                    }
                            });
                            
                            Button addEventButton = (Button) dialog.findViewById(R.id.addEventButton);
                            addEventButton.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    	Toast.makeText(getActivity().getApplicationContext(), "Dodanie nowego wydarzenie do budynku",
                                    			   Toast.LENGTH_SHORT).show();
                                    }
                            });
                            
                            Button dialogButton = (Button) dialog.findViewById(R.id.dialogBuildingButtonOK);
                            dialogButton.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                            dialog.dismiss();
                                            try {
												MainActivity.storage.SaveDataOnLocalStorage(new Tuple(NaviMarker.markerList, NaviMarker.images, Favorites.getFavoriteList(),
														NaviMarker.defaultPosition, NaviMarker.tagSet));
											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
                                    }
                            });
                            dialog.show();
            }
            else if (temp instanceof PlacesMarker) {
                    Log.i("logi", "TYP: PLACESMARKER");                    
                            final Dialog dialog = new Dialog(getActivity());
                            dialog.setContentView(R.layout.placemarker_dialog);
                            dialog.setTitle(temp.getName());                       
                            // Ustawianie danych

                            TextView placeDescription = (TextView) dialog.findViewById(R.id.place_description);
                            placeDescription.setText(temp.getDescription());   
                            
                            TextView placeOpenHours = (TextView) dialog.findViewById(R.id.place_openhours);
                            if(temp.getOpenHours().equals(" ")){
                                    placeOpenHours.setHeight(0);
                                    placeOpenHours.setWillNotDraw(true);
                            }
                            else {
                                    placeOpenHours.setText("Godziny Otwarcia: " + temp.getOpenHours());
                            }
                            TextView placeAddress = (TextView) dialog.findViewById(R.id.place_address);
                            if(temp.getAddress().equals(" "))
                            {
                                    placeAddress.setHeight(0);
                                    placeAddress.setWillNotDraw(true);
                            }
                            else {
                                    placeAddress.setText("Adress miejsca: " + temp.getAddress());    
                            }
                            TextView placePhone = (TextView) dialog.findViewById(R.id.place_phone);
                            if(temp.getPhone().equals(" ")){
                                    placePhone.setHeight(0);
                                    placePhone.setWillNotDraw(true);
                            }
                            else {
                                    placePhone.setText("Numer telefonu: " + temp.getPhone());
                                    Linkify.addLinks(placePhone, Linkify.PHONE_NUMBERS);
                            }
                            TextView placeWww = (TextView) dialog.findViewById(R.id.place_www);
                            if(temp.getWww().equals(" ")) {
                                    placeWww.setHeight(0);
                                    placeWww.setWillNotDraw(true);
                            }
                            else {
                                    placeWww.setText("Strona internetowa: " + temp.getWww());
                                    Linkify.addLinks(placeWww, Linkify.WEB_URLS);
                            }
                            TextView placeMail = (TextView) dialog.findViewById(R.id.place_mail);
                            if(temp.getMail().equals(" ")) {
                                    placeMail.setHeight(0);
                                    placeMail.setWillNotDraw(true);
                            }
                            else {
                                    placeMail.setText("Adres e-mail: " + temp.getMail());
                                    Linkify.addLinks(placeMail, Linkify.EMAIL_ADDRESSES);                          
                            }
                            TextView placeBuilding = (TextView) dialog.findViewById(R.id.place_building);
                            if(((PlacesMarker) temp).getBuilding().equals(" ")) {
                                    placeBuilding.setHeight(0);
                                    placeBuilding.setWillNotDraw(true);
                            }
                            else {
                                    placeBuilding.setText("Budynek: " + ((PlacesMarker)temp).getBuilding());                       
                            }
                            TextView placeRoom = (TextView) dialog.findViewById(R.id.place_room);
                            if(((PlacesMarker) temp).getRoom().equals(" ")) {
                                    placeRoom.setHeight(0);
                                    placeRoom.setWillNotDraw(true);
                            }
                            else {
                                    placeRoom.setText("Sala: " + ((PlacesMarker)temp).getRoom());      
                            }
                            TextView placeFloor = (TextView) dialog.findViewById(R.id.place_floor);
                            if(((PlacesMarker) temp).getFloor().equals(" ")) {
                                    placeFloor.setHeight(0);
                                    placeFloor.setWillNotDraw(true);
                            }
                            else {
                                    placeFloor.setText("Piętro: " + ((PlacesMarker)temp).getFloor());  
                            }
                            
                            favIcon = (ImageView) dialog.findViewById(R.id.favorite);
                            if(Favorites.checkIfFavorite(temp.getName())){
                            	favIcon.setImageResource(R.drawable.ulubionetrue);
                            } else {
                            	favIcon.setImageResource(R.drawable.ulubione);
                            }
                            favIcon.setOnClickListener(new FavoriteListener(temp));
                            
                            Button showEventsButton = (Button) dialog.findViewById(R.id.dialogShowEventsButton);
                            showEventsButton.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    	Toast.makeText(getActivity().getApplicationContext(), "Wydarzenia w tym miejscu",
                                    			   Toast.LENGTH_SHORT).show();
                                    }
                            });
                            
                            Button addEventButton = (Button) dialog.findViewById(R.id.addEventButton);
                            addEventButton.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                    	Toast.makeText(getActivity().getApplicationContext(), "Dodanie nowego wydarzenie do miejsca",
                                    			   Toast.LENGTH_SHORT).show();
                                    }
                            });
                            
                            Button dialogButton = (Button) dialog.findViewById(R.id.dialogPlaceButtonOK);
                            dialogButton.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                            dialog.dismiss();
                                            try {
												MainActivity.storage.SaveDataOnLocalStorage(new Tuple(NaviMarker.markerList, NaviMarker.images, 
														Favorites.getFavoriteList(), NaviMarker.defaultPosition,
														NaviMarker.tagSet));
											} catch (FileNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (IOException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
                                    }
                            });
                            dialog.show(); 
            }
            }
        }  
        );
    }
	

	/* fillMap operation puts markers on map in mMap reference. 
	 * After action user can see definied markers on Map with predefinied icons.
	 * 
	 * @param markers - List of markers which will be put on map.
	 * 
	 * Quality: q2 (fixed code accordingly to the developer remarks)
	 */
	public static void fillMap(ArrayList<NaviMarker> markers) {
		Iterator<NaviMarker> itr = markers.iterator();
		while (itr.hasNext()) {
			NaviMarker a = itr.next();
			Log.i("Markery", "Marker:" + a.getName() + " tag: " + a.getTag());
			setMarkerIcons(a);		//ustawia odpowiednie ikonki danemu markerowi (brak - defaultowa)
			mMap.addMarker(a.getMarker());
		}
	}
	
	

	
	 /* Set the right icon from the resources on the given marker.
	 * Operation use static set tagSet from NaviMarker class to 
	 * compare with name of files contains icon graphics. 
	 * 
	 * @param nm - NaviMarker object to assign icon         
	 */
	public static void setMarkerIcons(NaviMarker nm) {
		Activity activity = (Activity)MainActivity.context;
		for(String tag : NaviMarker.tagSet) {
			Log.i("TAG", tag);
			if(tag.equalsIgnoreCase(nm.getTag())){
				Log.i("FOUND", tag + " = " + nm.getTag());
				int id = MainActivity.context.getResources().getIdentifier(tag.toLowerCase(), "drawable", activity.getPackageName());
				Log.i("ID", Integer.toString(id));
				if(id != 0){		//id bedzie rowne zero jak nie znajdzie odpowiedniego pliku
					nm.getMarker().icon(BitmapDescriptorFactory.fromResource(id));
				}
				break;
			}
		}
		
	}
														
	/**
	 * Set on Map Markers, which have same tag name as passed by parameter 
	 * e.g.(tag1;tag2;tag3). Method fills currentMarker container. 
	 * Tag param can have multiple tags separated witch semicolon.
	 * @param tag = String with Markers tag name
	 * @param markers - ArrayList to be searched
	 * 
	 * Quality q3 (tested (Unit))
	 */
	public static void setMarkersWithTag(String tag, ArrayList<NaviMarker> markers) {
		//mMap.clear();
		if(tag != null && markers!=null){
			currentMarker = new ArrayList<NaviMarker>();
			String[] tags = tag.split(";");
			for(String t : tags){
				Iterator<NaviMarker> itr = markers.iterator();
				while (itr.hasNext()) {
					NaviMarker a = itr.next();
					if (a.getTag().equals(t)) {
	
						currentMarker.add(a);
						Log.i("setMarkersWithTag", "dodano marker");
					}
				}
			}
		}
	}

	/* Find markers with match to phrase in any field. @see NaviMarker.searchPhrase
	 * and returns container with matched markers.
	 * @param phrase - String phrase to be find
	 * @param markers - ArrayList to be searched
	 * 
	 * Quality : q3 (tested (Unit))
	 */
	public static ArrayList<NaviMarker> searchByParameter(String phrase, ArrayList<NaviMarker> markers) {
        ArrayList<NaviMarker> temp = new ArrayList<NaviMarker>();
        Iterator<NaviMarker> itr;
        try{
                itr = markers.iterator();
        }catch(Exception e){
                markers = NaviMarker.markerList;
                itr = markers.iterator();
        }
        while (itr.hasNext()) {
                NaviMarker a = itr.next();
                if(phrase != null){
	                if (a.searchPhraseIgnoringCase(phrase)) {
	                        temp.add(a);
	                }
                }
        }
        return temp;
}
	public static ArrayList<String> searchNamesByParameter(String phrase, ArrayList<NaviMarker> markers) {
        ArrayList<String> temp = new ArrayList<String>();
        Iterator<NaviMarker> itr;
        try{
                itr = markers.iterator();
        }catch(Exception e){
                markers = NaviMarker.markerList;
                itr = markers.iterator();
        }
        while (itr.hasNext()) {
                NaviMarker a = itr.next();
                if(phrase != null){
	                if (a.searchPhraseIgnoringCase(phrase)) {
	                        temp.add(a.getName());
	                }
                }
        }
        return temp;
}
	// INTERFACE LocationListener
	@Override
	public void onLocationChanged(Location location) {
		LatLng latLng = new LatLng(location.getLatitude(),
				location.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 60, 0));
		mMap.animateCamera(cameraUpdate);

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
		Toast.makeText(this.getActivity().getApplicationContext(),R.string.turnOnLocalization, Toast.LENGTH_LONG).show();
	}
	
	public static void clearAllMarkers(){
		mMap.clear();
	}



	@Override
	public void activate(OnLocationChangedListener listener) {
		locationChangedListener = listener;
	}



	@Override
	public void deactivate() {
		locationChangedListener = null;
	}

}
