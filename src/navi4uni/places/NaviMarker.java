package navi4uni.places;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.TreeSet;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class NaviMarker implements Serializable {
	private static final long serialVersionUID = 1L;
	transient MarkerOptions marker; // Nazwa, Snippet, Pozycja
	String id;
	String tag;
	String openHours; // ?
	String address; // ?
	String description; // ?
	String phone; // ?
	String mail; // ?
	String www;
	String lon;
	String lat;
	String campusName;

	public static HashMap<String, NaviMarker> markerList = new HashMap<String, NaviMarker>();	//lista wszystkich markerow 
	//public static LinkedList<String> itemNames = new LinkedList<String>();		//zbieramy nazwy do Kalendarza
	public static HashMap<String, Bitmap> images = new HashMap<String, Bitmap>();	//zawiera wszystkie zdjecia budynkow
	public static LatLng defaultPosition = null;
	
	public static Set<String> tagSet = new TreeSet<String>();	//lista przechowuje dziedzine tagów
	
	public NaviMarker(String name, String snippet, String lat, String lon,
			String tag, String openHours, String address, String description,
			String phone, String mail, String www) {
		LatLng pos = new LatLng(Double.parseDouble(lat),
				Double.parseDouble(lon));
		this.marker = new MarkerOptions().title(name).snippet(snippet)
				.position(pos);
		this.tag = tag;
		this.openHours = openHours;
		this.address = address;
		this.description = description;
		this.phone = phone;
		this.mail = mail;
		this.www = www;
	}
	
	public NaviMarker(){
		this.marker = new MarkerOptions();
		this.address = null;
		this.description = null;
		this.lat = null;
		this.lon = null;
		this.mail = null;
		this.openHours = null;
		this.phone = null;
		this.tag = null;
		this.www = null;
	}
	
	public static void setDefaultPositions(String latitude, String longitude){
		NaviMarker.defaultPosition = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
		Log.i("DefaultPosition", NaviMarker.defaultPosition.toString() );
	}
	
	/**
	 * Converted variables long and lat to WGS84 System (google requirement),
	 * set title and  
	 */
	public void configurePositons(){
		LatLng pos = new LatLng(Double.parseDouble(lat),
				Double.parseDouble(lon));
		this.marker.position(pos);
	}

	public static HashMap<String, NaviMarker> getMarkerList() {
		return markerList;
	}

//	public static void setMarkerList() {
//		markerList = new ArrayList<NaviMarker>();
//	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return this.marker.getTitle();
	}

	public void setName(String name) {
		this.marker.title(name);
		//itemNames.add(name);
	}

	public String getSnippet() {
		return this.marker.getSnippet();
	}

	public void setSnippet(String snippet) {
		this.marker.snippet(snippet);
	}

	public LatLng getLatLng() {
		return this.marker.getPosition();
	}

	//Podczas czytania xml dostaje w roznych obiegach dane longitude i latitude, dlatego 
	//robilem to na dwa, a znacznik </building> i </places> wywolaja metode ladujaca dane do MarkerOptions
	public void setLong(String _lon){
		lon = _lon;
	}
	
	public void setLat(String _lat){
		lat = _lat;
	}

	// Podstawowe gettery i settery
	public MarkerOptions getMarker() {
		return marker;
	}

	public void setMarker(MarkerOptions m) {
		this.marker = m;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getOpenHours() {
		return openHours;
	}

	public void setOpenHours(String openHours) {
		this.openHours = openHours;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getWww() {
		return www;
	}

	public void setWww(String www) {
		this.www = www;
	}
	
	public String getCampusName() {
		return campusName;
	}

	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append(this.id + "\n");
		str.append(this.address + "\n");
		str.append(this.description + "\n");
		str.append(this.lat + "\n");
		str.append(this.lon  + "\n");
		str.append(this.mail + "\n");
		str.append(this.openHours + "\n"); //pusty
		str.append(this.phone + "\n");
		str.append(this.tag + "\n"); //pusty
		str.append(this.www + "\n");
		str.append("MarkerOptions : ");
		str.append("Title " + marker.getTitle()+"\n");
		str.append("Snippet: " + marker.getSnippet()+"\n" );
		str.append("MarkerPosition: " + marker.getPosition()+"\n");
		return str.toString();
	}
	
	public boolean searchPhrase(String _phrase){
		if(this.getAddress().contains(_phrase)){
			return true;
		}
		if(this.getDescription().contains(_phrase)){
			return true;
		}
		if(this.getMail().contains(_phrase)){
			return true;
		}
		if(this.getName().contains(_phrase)){
			return true;
		}
		if(this.getOpenHours().contains(_phrase)){
			return true;
		}
		if(this.getPhone().contains(_phrase)){
			return true;
		}
		if(this.getSnippet().contains(_phrase)){
			return true;
		}
		if(this.getTag().contains(_phrase)){
			return true;
		}
		if(this.getWww().contains(_phrase)){
			return true;
		}
		return false;
	}
	
	public boolean searchPhraseIgnoringCase(String _phrase){
		_phrase = _phrase.toLowerCase();
		
		if(this.getAddress().toLowerCase().contains(_phrase)){
			return true;
		}
		if(this.getDescription().toLowerCase().contains(_phrase)){
			return true;
		}
		if(this.getMail().toLowerCase().contains(_phrase)){
			return true;
		}
		if(this.getName().toLowerCase().contains(_phrase)){
			return true;
		}
		if(this.getOpenHours().toLowerCase().contains(_phrase)){
			return true;
		}
		if(this.getPhone().toLowerCase().contains(_phrase)){
			return true;
		}
		if(this.getSnippet().toLowerCase().contains(_phrase)){
			return true;
		}
		if(this.getTag().toLowerCase().contains(_phrase)){
			return true;
		}
		if(this.getWww().toLowerCase().contains(_phrase)){
			return true;
		}
		return false;
	}
	
    private void writeObject(java.io.ObjectOutputStream stream)
            throws IOException {
    	stream.writeObject(this.id);
        stream.writeObject(this.tag);
        stream.writeObject(this.openHours);
        stream.writeObject(this.address);
        stream.writeObject(this.description);
        stream.writeObject(this.phone);
        stream.writeObject(this.mail);
        stream.writeObject(this.www);
        stream.writeObject(this.lon);
        stream.writeObject(this.lat);
        stream.writeObject(this.campusName);
        stream.writeObject(this.marker.getTitle());
        stream.writeObject(this.marker.getSnippet());
    }
    
    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException {
    	 this.id = (String) stream.readObject();
         this.tag = (String) stream.readObject();
         this.openHours = (String) stream.readObject();
         this.address = (String) stream.readObject();
         this.description = (String) stream.readObject();
         this.phone = (String) stream.readObject();
         this.mail = (String) stream.readObject();
         this.www = (String) stream.readObject();
         this.lon = (String) stream.readObject();
         this.lat = (String) stream.readObject();
         this.campusName = (String) stream.readObject();
         String title = (String) stream.readObject();		//marker refactor1
         String snippet = (String) stream.readObject();
         marker = new MarkerOptions();
         marker.snippet(snippet);
         marker.title(title);
         this.configurePositons();
    }
    
	public static NaviMarker getMarkerFromListByName(String name){
		for(NaviMarker nm: markerList.values()) {
			if(nm.getName().equals(name))
				return nm;
		}
		return null;
	}
//	public void SaveDataOnLocalStorage(Tuple t) throws FileNotFoundException, IOException{
////		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("MapContent.bin")));	//application local
//		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(Environment.getExternalStorageDirectory()	//zapis do karty SD (tymczasowo do podlagania wynikow)
//                + File.separator + "tupla.png")));	//sdCard
//		out.writeObject(new Tuple(NaviMarker.markerList, NaviMarker.images));
//		out.flush();
//		out.close();
//	}

}
