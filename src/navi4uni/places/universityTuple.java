package navi4uni.places;
/*
 * Class is tuple of data contains university name, default position, country
 * and link to xmlFile with description of campus.
 */

public class universityTuple {
	private String name = null;
	private String clong = null;
	private String clat = null;
	private String country = null;
	private String city  = null;
	private String link = null;
	
	public String getLink(){
		return link;
	}
	public void setLink(String link){
		this.link = link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getClong() {
		return clong;
	}
	public void setClong(String clong) {
		this.clong = clong;
	}
	public String getClat() {
		return clat;
	}
	public void setClat(String clat) {
		this.clat = clat;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public String toString(){
		String temp = "Name: " + getName() + "\n" 
				+ "cLong : " + getClong() +"\n"
				+ "cLat : " + getClat() +"\n"
				+ "country : " + getCountry() +"\n"
				+ "city : " + getCity() +"\n"
				+ "Link : " + getLink() +"\n";
		return temp;
	}
	
}
