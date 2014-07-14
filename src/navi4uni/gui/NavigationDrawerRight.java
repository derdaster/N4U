package navi4uni.gui;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ListView;

public class NavigationDrawerRight {

	private static ArrayList<String> dataArrayRight ;
	private static Context context;
	private static RightAdapter adapter;
	private static ListView mDrawerListRight;
	
	
	public NavigationDrawerRight(Context context, ListView mDrawerListRight){
		this.context = context;
		this.mDrawerListRight = mDrawerListRight;
		dataArrayRight = new ArrayList<String>();
	}
	
	
	private static void refreshListView(){
				
		adapter = new RightAdapter(context, dataArrayRight);
		mDrawerListRight.setAdapter(adapter);
		
	}
	
	
	
	public void fillRightList() {

		dataArrayRight.clear();
		dataArrayRight.add(context.getString(R.string.search));
		dataArrayRight.add(context.getString(R.string.filter));
		dataArrayRight.add(context.getString(R.string.favourite));
		refreshListView();

	}
	
	public static void addToList(ArrayList<String> listAfterFilter){
		dataArrayRight.addAll(listAfterFilter);
		refreshListView();
	}
	
	
}