package navi4uni.places;

public class BuildingMarker extends NaviMarker {

	private String department;
	private String directions;
	private String photo;

	public BuildingMarker(String name, String snippet, String lat, String lon,
			String tag, String openHours, String address, String description,
			String phone, String mail, String department, String directions,
			String photo, String www) {
		super(name, snippet, lat, lon, tag, openHours, address, description,
				phone, mail, www);
		// TODO Auto-generated constructor stub
		this.department = department;
		this.directions = directions;
		this.photo = photo;
	}
	
	public BuildingMarker(){
		super();
		this.department = null;
		this.directions = null;
		this.phone = null;
	}

	public void addBuildingToList(){
		markerList.add(this);
	}
	
	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getDirections() {
		return directions;
	}

	public void setDirections(String directions) {
		this.directions = directions;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}
	
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append(super.toString());
		str.append(this.department + "\n");
		str.append(this.directions + "\n");
		str.append(this.photo + "\n");
		return str.toString();
		
	}
}