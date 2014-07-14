package navi4uni.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import navi4uni.gui.MainActivity;
import navi4uni.gui.R;
import navi4uni.map.MapFragment;
import navi4uni.places.NaviMarker;
import navi4uni.places.Tags;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;


public class FilterDialog {

	private static ArrayList<String> stringsList;
	private Context context;
	private static ArrayList seletedTags;
	public static ArrayList<Tags> tagList;
	
	public FilterDialog(Context context, Set<String> tagSet){
		this.context = context;
		stringsList = new ArrayList(tagSet);
		FilterDialog.tagList = new ArrayList<Tags>();
		addToList();
	}
	
	private void addToList(){
		for(int i = 0 ; i < stringsList.size(); i++){
			
			Tags t = new Tags(stringsList.get(i));
			t.setSelected(true);
			FilterDialog.tagList.add(t);

		}
	}
	
	public static void showDialog(){
		
		AlertDialog dialog;
		CharSequence[] items = stringsList.toArray(new CharSequence[stringsList.size()]);
		
		boolean[] checkedValues = new boolean[tagList.size()];
		for(int i=0 ; i<tagList.size(); i++){
			if(tagList.get(i).isSelected()){
				checkedValues[i] = true;
			}
		}
		seletedTags = new ArrayList();
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.context);
		builder.setTitle("Filtr obiektów..");
		builder.setMultiChoiceItems(items, checkedValues,
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int indexSelected, boolean isChecked) {
						if (isChecked) {
							tagList.get(indexSelected).setSelected(true);
						} else{
							tagList.get(indexSelected).setSelected(false);
						}
					}
				})
				.setPositiveButton("Pokaż",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								String tags = "";
								for(int i = 0 ; i < tagList.size(); i++){
									if(tagList.get(i).isSelected()){
										tags += tagList.get(i).getName() + ";";
									}
								}
								if(!tags.isEmpty()){
									navi4uni.map.MapFragment.setMarkersWithTag(tags, NaviMarker.markerList);
									MapFragment.clearAllMarkers();
									MapFragment.fillMap(MapFragment.currentMarker);

								}else{
									Toast.makeText(MainActivity.context, "Wszystkie pola puste", Toast.LENGTH_SHORT).show();
								}
							}
						})
				.setNegativeButton("Anuluj",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								
							}
				});

		dialog = builder.create();
		dialog.show();
		
		
	}
	
	
}
