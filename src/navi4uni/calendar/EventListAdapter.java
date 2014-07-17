package navi4uni.calendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import navi4uni.gui.MainActivity;
import navi4uni.gui.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Resources;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.net.Uri;
import android.provider.CalendarContract.Instances;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

public class EventListAdapter extends BaseAdapter {

	private ArrayList<CalendarEvent> listData;

	private LayoutInflater layoutInflater;

	public EventListAdapter(Context context, ArrayList<CalendarEvent> listData) {
		this.listData = listData;
		layoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}
	public String getLocation(int position){
		return listData.get(position).getEventLocation();
	}
	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = layoutInflater.inflate(
					R.layout.calendar_event_element, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.event_name);
			holder.building = (TextView) convertView
					.findViewById(R.id.event_building);
			holder.description = (TextView) convertView
					.findViewById(R.id.event_description);
			holder.date = (TextView) convertView.findViewById(R.id.event_date);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(listData.get(position).getTitle());
		holder.building.setText(listData.get(position).getEventLocation());
		holder.description.setText(listData.get(position).getDescription());
		holder.date.setText(listData.get(position).getDateInString());

		return convertView;
	}

	static class ViewHolder {
		TextView name;
		TextView building;
		TextView description;
		TextView date;
	}

	public void getEvents(int mainYear, int mainMonth, int mainDay,
			int secYear, int secMonth, int secDay, String search, View view,
			ArrayList<CalendarEvent> CustomListViewValuesArr) {

		CustomListViewValuesArr.clear();

		final String DEBUG_TAG = "MyActivity";
		final String[] INSTANCE_PROJECTION = new String[] { Instances.EVENT_ID, // 0
				Instances.BEGIN, // 1
				Instances.END, // 2
				Instances.TITLE, // 3
				Instances.EVENT_LOCATION, // 4
				Instances.DESCRIPTION // 5
		};

		
		final int PROJECTION_ID_INDEX = 0;
		final int PROJECTION_BEGIN_INDEX = 1;
		final int PROJECTION_END_INDEX = 2;
		final int PROJECTION_TITLE_INDEX = 3;
		final int PROJECTION_LOCATION_INDEX = 4;
		final int PROJECTION_DESCRIPTION_INDEX = 5;

		
		Calendar beginTime = Calendar.getInstance();
		if (mainYear != 0){
			beginTime.set(mainYear, mainMonth, mainDay, 0, 0);
		}
		else
		{
			beginTime.set(Calendar.HOUR_OF_DAY, 0);
			beginTime.set(Calendar.MINUTE, 0);
			
		}
		long startMillis = beginTime.getTimeInMillis();
		Calendar endTime = Calendar.getInstance();
		if (secYear != 0)
			endTime.set(secYear, secMonth, secDay, 23, 59);
		else
			endTime.set(mainYear, mainMonth, mainDay, 23, 59);
		long endMillis = endTime.getTimeInMillis();

		Cursor cur = null;
		ContentResolver cr = view.getContext().getContentResolver();

		
		String selection = Instances.CALENDAR_ID + " = ? AND ( "
				+ Instances.TITLE + " like ? OR " + Instances.EVENT_LOCATION
				+ " like ? OR " + Instances.DESCRIPTION + " like ? )";
		String[] selectionArgs = new String[] { "1", "%" + search + "%",
				"%" + search + "%", "%" + search + "%"};

		
		Uri.Builder builder = Instances.CONTENT_URI.buildUpon();
		
		
		ContentUris.appendId(builder, startMillis);
		ContentUris.appendId(builder, endMillis);
		Log.e(DEBUG_TAG, "Begin date: " + beginTime.getTime().toString());
		Log.e(DEBUG_TAG, "End date: " + endTime.getTime().toString());

		cur = cr.query(builder.build(), INSTANCE_PROJECTION, selection,
				selectionArgs, null);
		Log.e(DEBUG_TAG, cur.toString());
		while (cur.moveToNext()) {

			String title = null;
			String location = null;
			String description = null;
			long eventID = 0;
			long beginVal = 0;
			long endVal = 0;

			// Get the field values
			eventID = cur.getLong(PROJECTION_ID_INDEX);
			Log.e(DEBUG_TAG, "Event ID:  " + Long.toString(eventID));

			beginVal = cur.getLong(PROJECTION_BEGIN_INDEX);
			endVal = cur.getLong(PROJECTION_END_INDEX);
			title = cur.getString(PROJECTION_TITLE_INDEX);
			Log.e(DEBUG_TAG, "Event:  " + title);

			location = cur.getString(PROJECTION_LOCATION_INDEX);
			Log.e(DEBUG_TAG, "Description:  " + description);

			description = cur.getString(PROJECTION_DESCRIPTION_INDEX);
			Log.e(DEBUG_TAG, "Location:  " + location);

			// Do something with the values.
			Calendar beginCalendar = Calendar.getInstance();
			beginCalendar.set(mainYear, mainMonth, mainDay);

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(beginVal);

			// date and time formatters
			SimpleDateFormat formatterDate = new SimpleDateFormat("EE");
			SimpleDateFormat formatterTime = new SimpleDateFormat("k:mm");

			String bDate = formatterDate.format(calendar.getTime());
			String bTime = formatterTime.format(calendar.getTime());

			Log.e(DEBUG_TAG, "Begin date: " + bDate);

			calendar.setTimeInMillis(endVal);
			String eDate = formatterDate.format(calendar.getTime());
			String eTime = formatterTime.format(calendar.getTime());

			Log.e(DEBUG_TAG, "End date: " + eTime);
			String dateString = bDate + " " + bTime + "-" + eTime;
			
			setListData(title, location, description, dateString,
					CustomListViewValuesArr);
		}
	}

	public void setListData(String title, String building, String description,
			String date, ArrayList<CalendarEvent> CustomListViewValuesArr) {

		/******* Firstly take data in model object ******/
		final CalendarEvent sched = new CalendarEvent();
		sched.setTitle(title);
		sched.setEventLocation(building);
		sched.setDescription(description);
		sched.setDateInString(date);

		/******** Take Model Object in ArrayList **********/
		CustomListViewValuesArr.add(sched);

	}

}
