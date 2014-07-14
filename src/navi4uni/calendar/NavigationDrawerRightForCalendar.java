package navi4uni.calendar;

import java.util.ArrayList;

import navi4uni.gui.R;
import android.content.Context;
import android.widget.ListView;


public class NavigationDrawerRightForCalendar {

	private ArrayList<String> dataArrayRight = new ArrayList<String>();
	private Context context;
	private RightCalendarAdapter adapter;
	private ListView mDrawerListRight;
	
	
	public NavigationDrawerRightForCalendar(Context context, ListView mDrawerListRight){
		this.context = context;
		this.mDrawerListRight = mDrawerListRight;
	}
	
	
	public void refreshListView(){
		
		adapter = new RightCalendarAdapter(context, dataArrayRight);
		mDrawerListRight.setAdapter(adapter);
		
	}
	
	
	
	public void fillRightCalendarList() {

		dataArrayRight.clear();
		
		dataArrayRight.add(context.getString(R.string.search));
		dataArrayRight.add(context.getString(R.string.addEvent));
		dataArrayRight.add(context.getString(R.string.dailyPlan));
		//dataArrayRight.add(context.getString(R.string.list));
		refreshListView();
	}
	
	
	public ArrayList<String> getLeftRight(){
		return dataArrayRight;
	}
		
}
