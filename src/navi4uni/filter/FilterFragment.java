package navi4uni.filter;

import java.util.ArrayList;
import navi4uni.gui.MainActivity;
import navi4uni.gui.R;
import navi4uni.map.MapFragment;
import navi4uni.places.NaviMarker;
import navi4uni.places.NaviMarkers;
import navi4uni.places.Tags;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

public class FilterFragment extends Fragment{

	private static View view;
	private static ListView listView;
	private static FilterAdapter adapter;
	public static ArrayList<Tags> tagList;

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.filter_list, container, false);
        
        
        listView=(ListView)view.findViewById(R.id.filter_list);   
        adapter = new FilterAdapter(MainActivity.context, NaviMarker.tagSet);
        listView.setAdapter(adapter);	
        refreshList();

        Button myButton = (Button) view.findViewById(R.id.accept_changes);
        myButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String tags = "";
				for(int i = 0 ; i < tagList.size(); i++){
					if(tagList.get(i).isSelected()){
						tags += tagList.get(i).getName() + ";";
					}
				}
				if(!tags.isEmpty()){
					navi4uni.map.MapFragment.setMarkersWithTag(tags, NaviMarker.markerList);
					FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction();
					transaction.replace(R.id.Frame_Layout, new MapFragment());
					transaction.commit();
				}
			}
		});
        
        return view;
	}

	public void refreshList(){
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}

	
}
