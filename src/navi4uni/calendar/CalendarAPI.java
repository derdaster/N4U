package navi4uni.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Instances;
import android.provider.CalendarContract.Reminders;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

public class CalendarAPI extends CalendarEvent {

	private Context context;
	private int calendarID = 1;
	private TimeZone timeZone = TimeZone.getDefault();
	private boolean allDay = false;
	private int reminderMinutes = -1;

	public void setCalendarID(int calendar) {
		this.calendarID = calendar;
	}

	public void setStartEndMilis(String dateInString, String timeInString,
			boolean start) {

		SimpleDateFormat formatter = new SimpleDateFormat("EE, dd-MM-yy");
		Date date = new Date();
		try {

			date = formatter.parse(dateInString);

		} catch (ParseException x) {
			x.printStackTrace();
		}

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		date = new Date();
		formatter = new SimpleDateFormat("k:mm");
		try {

			date = formatter.parse(timeInString);

		} catch (ParseException x) {
			x.printStackTrace();
		}
		cal = Calendar.getInstance();
		cal.setTime(date);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);

		cal = Calendar.getInstance();

		cal.set(year, month, day, hour, minute);
		if (start)
			this.startMilis = cal.getTimeInMillis();
		else {
			Log.e("event", "godzina konca" + Integer.toString(hour) + ":"
					+ Integer.toString(minute));
			this.endMilis = cal.getTimeInMillis();
		}

		// problem z godzin¹, podawana jest do kalendarza am/pm
	}

	public void setTimeZone(TimeZone timeZone) {

		this.timeZone = timeZone;
	}

	public void setAllDay(boolean allDay) {
		this.allDay = allDay;
	}



	public void setReminderMinutes(int reminder) {
		this.reminderMinutes = reminder;
	}

	public CalendarAPI(Context context) {
		this.context = context;
		getCalendarID();
	}

	public void addCal2() {

		// Insert Event
		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		// TimeZone timeZone = TimeZone.getDefault();
		values.put(CalendarContract.Events.DTSTART, startMilis);
		values.put(CalendarContract.Events.DTEND, endMilis);
		values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
		values.put(CalendarContract.Events.TITLE, title);
		values.put(CalendarContract.Events.DESCRIPTION, description);
		values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
		values.put(CalendarContract.Events.EVENT_LOCATION, eventLocation);
		if (allDay)
			values.put(CalendarContract.Events.ALL_DAY, 1);
		else
			values.put(CalendarContract.Events.ALL_DAY, 0);
		values.put(CalendarContract.Events.AVAILABILITY,
				CalendarContract.Events.AVAILABILITY_BUSY);
		Uri uri1 = cr.insert(CalendarContract.Events.CONTENT_URI, values);

		// Retrieve ID for new event
		long eventID = Long.parseLong(uri1.getLastPathSegment());
		if (reminderMinutes >= 0)
			setReminder(eventID, context);

	}

	static String getEmail(Context context) {
		AccountManager accountManager = AccountManager.get(context);
		Account account = getAccount(accountManager);

		if (account == null) {
			return null;
		} else {
			return account.name;
		}
	}

	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		}

		return account;
	}

	public void getCalendarID() {

		final String[] EVENT_PROJECTION = new String[] { Calendars._ID, // 0
				Calendars.ACCOUNT_NAME, // 1
				Calendars.CALENDAR_DISPLAY_NAME, // 2
				Calendars.OWNER_ACCOUNT // 3
		};

		// The indices for the projection array above.
		final int PROJECTION_ID_INDEX = 0;
		final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
		final int PROJECTION_DISPLAY_NAME_INDEX = 2;
		final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

		String email = getEmail(context);
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = Calendars.CONTENT_URI;
		String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
				+ Calendars.ACCOUNT_TYPE + " = ?) AND ("
				+ Calendars.OWNER_ACCOUNT + " = ?))";
		String[] selectionArgs = new String[] { email, "com.google", email };

		cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);
		long calID = 0;
		while (cur.moveToNext()) {

			String displayName = null;
			String accountName = null;
			String ownerName = null;

			// Get the field values
			calID = cur.getLong(PROJECTION_ID_INDEX);
			displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
			accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
			ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);
			Log.e("id kalendarza: ", Integer.toString((int) calID));
		}
		calendarID = (int) calID;
	}

	private void setReminder(long eventID, Context context) {

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		values.put(Reminders.MINUTES, reminderMinutes);
		values.put(Reminders.EVENT_ID, eventID);
		values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
		Uri uri = cr.insert(Reminders.CONTENT_URI, values);
	}

	
}
