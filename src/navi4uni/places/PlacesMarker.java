package navi4uni.places;

import android.text.style.SuperscriptSpan;

public class PlacesMarker extends NaviMarker  {

	private String room;
	private String floor;
	private String building;

	public PlacesMarker(String name, String snippet, String lat, String lon,
			String tag, String openHours, String address, String description,
			String phone, String mail, String room, String floor, String building, String www) {
		super(name, snippet, lat, lon, tag, openHours, address, description,
				phone, mail, www);
		// TODO Auto-generated constructor stub
		this.room = room;
		this.building = building;
	}
	
	public PlacesMarker(){
		super();
		this.room = null;
		this.floor = null;
		this.building = null;
	}

	public void addPlaceToList(){
		markerList.add(this);
	}
	
	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getBuilding() {
		return building;
	}

	public void setBuilding(String building) {
		this.building = building;
	}
	
	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append(super.toString());
		str.append(this.building + "\n");
		str.append(this.floor + "\n");
		str.append(this.room + "\n");
		return str.toString();
	}

}
