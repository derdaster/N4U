package navi4uni.calendar;

import java.util.ArrayList;

import navi4uni.gui.R;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CalendarView;

import android.widget.ListView;

import android.widget.CalendarView.OnDateChangeListener;

public class CalendarFragment extends Fragment {

	private View view;
	public int nowy;
	private static Long currentDate;
	boolean from;

	ListView list;
	EventListAdapter adapter;
	ArrayList<CalendarEvent> CustomListViewValuesArr = new ArrayList<CalendarEvent>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		view = inflater.inflate(R.layout.main_calendar_view, container, false);
		view.setClickable(true);

		final CalendarView calendarView = (CalendarView) view
				.findViewById(R.id.mainCalendarView);
		currentDate = calendarView.getDate();
		FragmentManager ft = getFragmentManager();
		final CalendarDialog cDialog = new CalendarDialog(ft, view);

		calendarView.setOnDateChangeListener(new OnDateChangeListener() {

			@Override
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {

				if (calendarView.getDate() != currentDate) {

					currentDate = calendarView.getDate();
					FragmentManager ft = getFragmentManager();
					cDialog.setData(year, month, dayOfMonth, view);
					cDialog.setDataEnd(0, 0, 0, view);

					cDialog.showEventListDialog();
				}

			}

		});

		return view;

	}

}
