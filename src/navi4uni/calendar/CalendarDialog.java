package navi4uni.calendar;

import java.util.ArrayList;
import java.util.Calendar;

import navi4uni.gui.MainActivity;
import navi4uni.gui.R;
import navi4uni.map.MapFragment;
import navi4uni.places.NaviMarker;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.support.v4.app.Fragment;

public class CalendarDialog {
	private EventListAdapter adapter;
	private ListView list;
	private static View view;
	private static View dialogLayout;
	private ArrayList<CalendarEvent> CustomListViewValuesArr = new ArrayList<CalendarEvent>();
	private static int mainYear = 0;
	private static int mainMonth = 0;
	private static int mainDay = 0;
	private static int secYear = 0;
	private static int secMonth = 0;
	private static int secDay = 0;
	public static boolean from;
	String search = "";
	static FragmentManager manager;
	private boolean fromDrawerRight = false;

	public CalendarDialog() {

	}

	public CalendarDialog(FragmentManager ft, View v) {

		manager = ft;
		view = v;
	}

	public CalendarDialog(int y, int m, int d, View v, FragmentManager ft) {
		mainYear = y;
		mainMonth = m;
		mainDay = d;
		view = v;
		manager = ft;
	}

	public void setData(int y, int m, int d, View v) {
		mainYear = y;
		mainMonth = m;
		mainDay = d;
		view = v;
	}

	public void setDataEnd(int y, int m, int d, View v) {
		secYear = y;
		secMonth = m;
		secDay = d;
		view = v;
	}

	public static void setSearchCriteria(int y, int m, int d) {

		if (from) {
			mainYear = y;
			mainMonth = m;
			mainDay = d;
		} else {
			secYear = y;
			secMonth = m;
			secDay = d;
		}
	}
	
	public void setFromDrawerRight(boolean setter){
		this.fromDrawerRight=setter;
	}

	public static FragmentManager getManager() {
		return manager;
	}

	public void showEventListDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.context).setTitle("Lista wydarzeń");
		builder.setPositiveButton("Dodaj",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						/*
						 * Tutaj masz obsługę jak nacisniesz przycisk dodaj w
						 * oknie dialogu.
						 */

						showCalendarDialog();

					}
				});
		builder.setNegativeButton("Anuluj",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/*
						 * Tu masz miejsce co się stanie jak nacisniesz anuluj
						 */

						// getEvents(view);
						dialog.cancel();
					}
				});
		final FrameLayout frameView = new FrameLayout(MainActivity.context);
		builder.setView(frameView);

		final AlertDialog alertDialog = builder.create();
		LayoutInflater inflater = alertDialog.getLayoutInflater();

		View dialoglayout = inflater.inflate(R.layout.calendar_events_list,
				frameView);

		list = (ListView) dialoglayout.findViewById(R.id.list); // List defined
																// in XML ( See
																// Below )

		adapter = new EventListAdapter(MainActivity.context,
				CustomListViewValuesArr);
		adapter.getEvents(mainYear, mainMonth, mainDay, secYear, secMonth,
				secDay, search, frameView, CustomListViewValuesArr);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view2,
					int position, long id) {
				/*String temp=((TextView) view.findViewById(R.id.event_building)).getText().toString();
				Toast.makeText(view.getContext(),
						"Click ListItem Number " + temp, Toast.LENGTH_LONG)
						.show();
				ArrayList<NaviMarker> list = (ArrayList<NaviMarker>) NaviMarker.getMarkerList();
				for(NaviMarker m : list) {
					//if(m.getName().equals(items[which])) 
					{	//szuamy w liscie markera o tej nazwie
						//MapFragment.setFocusOnLatLng(m.getLatLng(), MapFragment.mMap );
						Log.i("ULUBIONE", "Click on: " + m.getName());
						
					}
				}*/

				
			}
		});
		alertDialog.show();

	}

	public void showCalendarDialog() {

		/*
		 * Zrezygnowałem z przeskakiwania do innego fragmentu bo wydaje mi się
		 * że tak jest jakoś przyjemniej jak co to poprostu wywołaj twoja
		 * metodę z fragmentem. Nic ci nie kasowałem także możesz wrócić.
		 */
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.context).setTitle("Dodaj wydarzenie..");

		builder.setPositiveButton("Dodaj",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						/*
						 * Tutaj masz obsługę jak nacisniesz przycisk dodaj w
						 * oknie dialogu.
						 */

						addEvent(view);

					}
				});

		builder.setNegativeButton("Anuluj",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/*
						 * Tu masz miejsce co się stanie jak nacisniesz anuluj
						 */

						// getEvents(view);
						if (!fromDrawerRight)
							showEventListDialog();
					}
				});

		final FrameLayout frameView = new FrameLayout(MainActivity.context);
		builder.setView(frameView);

		final AlertDialog alertDialog = builder.create();
		LayoutInflater inflater = alertDialog.getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.calendar, frameView);

		// setDialogListeners(dialoglayout);

		EditText mEditInit = (EditText) dialoglayout
				.findViewById(R.id.editText4);
		mEditInit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				from = true;
				DialogFragment newFragment = new TimePickerFragment();

				newFragment.show(manager, "timePicker");
			}

		});

		mEditInit = (EditText) dialoglayout.findViewById(R.id.editText5);
		mEditInit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				from = true;
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(manager, "datePicker");
			}

		});

		mEditInit = (EditText) dialoglayout.findViewById(R.id.editText6);
		mEditInit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				from = false;
				DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(manager, "timePicker");

			}

		});

		mEditInit = (EditText) dialoglayout.findViewById(R.id.editText7);
		mEditInit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				from = false;
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(manager, "datePicker");
			}

		});

		dialogLayout = dialoglayout;
		alertDialog.show();

		Log.e("event", Integer.toString(mainYear));
		Log.e("event", Integer.toString(mainMonth));
		Log.e("event", Integer.toString(mainDay));

		Calendar c = Calendar.getInstance();
		if (mainYear != 0) {
			c.set(Calendar.YEAR, mainYear);
			c.set(Calendar.MONTH, mainMonth);
			c.set(Calendar.DAY_OF_MONTH, mainDay);
		}

		EditText e;
		e = (EditText) dialogLayout.findViewById(R.id.editText5);
		e.setText(DateFormat.format("EE, dd-MM-yy", c));
		e = (EditText) dialogLayout.findViewById(R.id.editText7);
		e.setText(DateFormat.format("EE, dd-MM-yy", c));
		c = Calendar.getInstance();
		e = (EditText) dialogLayout.findViewById(R.id.editText4);
		e.setText(DateFormat.format("k:mm", c));
		c.add(Calendar.HOUR_OF_DAY, 1);
		e = (EditText) dialogLayout.findViewById(R.id.editText6);
		e.setText(DateFormat.format("k:mm", c));
	}

	public void addEvent(View v) {

		CalendarAPI calendar = new CalendarAPI(v.getContext());
		EditText e = (EditText) dialogLayout.findViewById(R.id.editText3);
		calendar.setDescription(e.getText().toString());
		e = (EditText) dialogLayout.findViewById(R.id.searchText);
		calendar.setTitle(e.getText().toString());
		e = (EditText) dialogLayout.findViewById(R.id.editText2);
		calendar.setEventLocation(e.getText().toString());
		e = (EditText) dialogLayout.findViewById(R.id.editText5);
		String dateInString = e.getText().toString();
		e = (EditText) dialogLayout.findViewById(R.id.editText4);
		String timeInString = e.getText().toString();
		calendar.setStartEndMilis(dateInString, timeInString, true);
		e = (EditText) dialogLayout.findViewById(R.id.editText7);
		dateInString = e.getText().toString();
		e = (EditText) dialogLayout.findViewById(R.id.editText6);
		timeInString = e.getText().toString();
		calendar.setStartEndMilis(dateInString, timeInString, false);
		e = (EditText) dialogLayout.findViewById(R.id.editText8);

		if (!e.getText().toString().isEmpty())
			calendar.setReminderMinutes(Integer.valueOf(e.getText().toString()));

		CheckBox c = (CheckBox) dialogLayout.findViewById(R.id.checkBox1);
		calendar.setAllDay(c.isChecked());
		calendar.addCal2();

	}

	public void showSearchDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.context).setTitle("Szukaj");

		builder.setPositiveButton("Szukaj",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						/*
						 * Tutaj masz obsługę jak nacisniesz przycisk dodaj w
						 * oknie dialogu.
						 */
						EditText e = (EditText) dialogLayout
								.findViewById(R.id.searchText);
						search = e.getText().toString();
						
						Toast toast = Toast.makeText(MainActivity.context, "Wyszukuje wszystie wydarzenia, brak kryterium" , Toast.LENGTH_LONG);
						toast.show();
						showEventListDialog();

					}
				});

		builder.setNegativeButton("Anuluj",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						/*
						 * Tu masz miejsce co się stanie jak nacisniesz anuluj
						 */

						// getEvents(view);

						dialog.cancel();
					}
				});

		final FrameLayout frameView = new FrameLayout(MainActivity.context);
		builder.setView(frameView);

		final AlertDialog alertDialog = builder.create();
		LayoutInflater inflater = alertDialog.getLayoutInflater();
		View dialoglayout = inflater.inflate(R.layout.search_events, frameView);

		// setDialogListeners(dialoglayout);

		EditText mEditInit = (EditText) dialoglayout
				.findViewById(R.id.editText4);
		mEditInit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				from = true;
				DialogFragment newFragment = new TimePickerFragment();

				newFragment.show(manager, "timePicker");
			}

		});

		mEditInit = (EditText) dialoglayout.findViewById(R.id.editText5);
		mEditInit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				from = true;
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(manager, "datePicker");
			}

		});

		mEditInit = (EditText) dialoglayout.findViewById(R.id.editText6);
		mEditInit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				from = false;
				DialogFragment newFragment = new TimePickerFragment();
				newFragment.show(manager, "timePicker");

			}

		});

		mEditInit = (EditText) dialoglayout.findViewById(R.id.editText7);
		mEditInit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				from = false;
				DialogFragment newFragment = new DatePickerFragment();
				newFragment.show(manager, "datePicker");
			}

		});

		dialogLayout = dialoglayout;
		alertDialog.show();

		Log.e("event", Integer.toString(mainYear));
		Log.e("event", Integer.toString(mainMonth));
		Log.e("event", Integer.toString(mainDay));
		Calendar c = Calendar.getInstance();
		if (mainYear != 0) {
			c.set(Calendar.YEAR, mainYear);
			c.set(Calendar.MONTH, mainMonth);
			c.set(Calendar.DAY_OF_MONTH, mainDay);
			secYear = mainYear;
			secMonth = mainMonth;
			secDay = mainDay;
		} else {
			mainYear = c.get(Calendar.YEAR);
			mainMonth = c.get(Calendar.MONTH);
			mainDay = c.get(Calendar.DAY_OF_MONTH);
			secYear = mainYear;
			secMonth = mainMonth;
			secDay = mainDay;
		}

		EditText e;
		e = (EditText) dialogLayout.findViewById(R.id.editText5);
		e.setText(DateFormat.format("EE, dd-MM-yy", c));
		c = Calendar.getInstance();
		e = (EditText) dialogLayout.findViewById(R.id.editText4);
		e.setText(DateFormat.format("k:mm", c));
		c.add(Calendar.HOUR_OF_DAY, 1);
		if (c.get(Calendar.HOUR_OF_DAY) == 0) {
			c.add(Calendar.DAY_OF_MONTH, 1);
			if (c.get(Calendar.DAY_OF_MONTH) == 1)
				c.add(Calendar.DAY_OF_MONTH, 1);
		}
		e = (EditText) dialogLayout.findViewById(R.id.editText6);
		e.setText(DateFormat.format("k:mm", c));
		e = (EditText) dialogLayout.findViewById(R.id.editText7);
		e.setText(DateFormat.format("EE, dd-MM-yy", c));
	}

	public void showDayPlan() {
		Calendar c = Calendar.getInstance();
		mainYear = c.get(Calendar.YEAR);
		mainMonth = c.get(Calendar.MONTH);
		mainDay = c.get(Calendar.DAY_OF_MONTH);
		secYear = mainYear;
		secMonth = mainMonth;
		secDay = mainDay;
		showEventListDialog();

	}

	public static class TimePickerFragment extends DialogFragment implements
			TimePickerDialog.OnTimeSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);
			// Log.e("event", "nowy kalendarz");
			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			// Do something with the time chosen by the user
			Calendar c = Calendar.getInstance();
			c.set(Calendar.HOUR_OF_DAY, hourOfDay);
			c.set(Calendar.MINUTE, minute);

			EditText e;
			if (from)
				e = (EditText) dialogLayout.findViewById(R.id.editText4);
			else
				e = (EditText) dialogLayout.findViewById(R.id.editText6);
			e.setText(DateFormat.format("k:mm", c));
			// e.setText("value",EditText.BufferType.EDITABLE);

		}

	}

	public static class DatePickerFragment extends DialogFragment implements
			DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) {
			// Do something with the date chosen by the user
			Calendar c = Calendar.getInstance();
			c.set(Calendar.YEAR, year);
			c.set(Calendar.MONTH, month);
			c.set(Calendar.DAY_OF_MONTH, day);

			EditText e;
			if (from) {
				e = (EditText) dialogLayout.findViewById(R.id.editText5);

			} else {
				e = (EditText) dialogLayout.findViewById(R.id.editText7);
			}
			CalendarDialog.setSearchCriteria(year, month, day);
			e.setText(DateFormat.format("EE, dd-MM-yy", c));

		}
	}
}
