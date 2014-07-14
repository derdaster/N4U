package navi4uni.gui;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ListView;

public class NavigationDrawerLeft {

	private ArrayList<String> dataArrayLeft = new ArrayList<String>();
	private Context context;
	private LeftAdapter adapter;
	private ListView mDrawerListLeft;
	
	
	public NavigationDrawerLeft(Context context, ListView mDrawerListLeft){
		this.context = context;
		this.mDrawerListLeft = mDrawerListLeft;
	}
	
	
	public void refreshListView(){
		
		adapter = new LeftAdapter(context, dataArrayLeft);
		mDrawerListLeft.setAdapter(adapter);
		
	}
	
	
	
	public void fillLeftList() {

		dataArrayLeft.clear();
		
		dataArrayLeft.add(context.getString(R.string.map));
		dataArrayLeft.add(context.getString(R.string.calendar));
		dataArrayLeft.add(context.getString(R.string.settings));
		refreshListView();
	}
	
	
	public ArrayList<String> getLeftList(){
		return dataArrayLeft;
	}
	
}