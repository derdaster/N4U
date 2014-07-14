package navi4uni.calendar;

import android.R.string;

public class CalendarEvent {
	protected String title="";
	protected String eventLocation="";
	protected String description="";
	private String dateInString="";
	protected long startMilis;
	protected long endMilis;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEventLocation() {
		return eventLocation;
	}
	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}
	public long getStartMilis() {
		return startMilis;
	}
	public void setStartMilis(long startMilis) {
		this.startMilis = startMilis;
	}
	public long getEndMilis() {
		return endMilis;
	}
	public void setEndMilis(long endMilis) {
		this.endMilis = endMilis;
	}

	public String getDateInString() {
		return dateInString;
	}
	public void setDateInString(String dateInString) {
		this.dateInString = dateInString;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	
	

}
