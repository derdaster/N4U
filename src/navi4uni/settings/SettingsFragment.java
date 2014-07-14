package navi4uni.settings;

import java.util.ArrayList;
import navi4uni.gui.MainActivity;
import navi4uni.gui.R;
import navi4uni.places.NaviMarker;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class SettingsFragment extends Fragment {

	private static View view;
	private static ListView listView;
	private static SettingsAdapter adapter;
	public static ArrayList<SettingsItems> settingsList;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		settingsList = new ArrayList<SettingsItems>();
		view = inflater.inflate(R.layout.settings_list, container, false);
		fillList();
        listView=(ListView)view.findViewById(R.id.settings_list);  
        
        adapter = new SettingsAdapter(MainActivity.context);
        listView.setAdapter(adapter);	
		
        listView.setOnItemClickListener(
                new OnItemClickListener(){             	
                	
					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int position, long arg3){						
						
					SettingsItems.onClick(position);
						
		}});
        
		return view;
	}
	
	private static void fillList(){
		
		String date = MainActivity.context.getSharedPreferences("DATE", Context.MODE_PRIVATE)
				.getString("currentDate", null);
		SettingsItems data = new SettingsItems("Data pobrania mapy", date, false);
		settingsList.add(data);
		
		SettingsItems refresh = new SettingsItems("Odśwież mapę", "Pobierz aktualną wersję", true);
		settingsList.add(refresh);
		
		SettingsItems maps = new SettingsItems("Wybierz kampus", "Pobierz nowe obiekty", true);
		settingsList.add(maps);
		
	}
	
	public static void refreshList(){
		settingsList.clear();
		fillList();
		adapter = new SettingsAdapter(MainActivity.context);
        listView.setAdapter(adapter);	
	}
	
	
	
}
