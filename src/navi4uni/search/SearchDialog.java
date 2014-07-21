package navi4uni.search;

import java.util.ArrayList;

import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import navi4uni.gui.MainActivity;
import navi4uni.gui.R;
import navi4uni.map.MapFragment;
import navi4uni.places.NaviMarker;
import navi4uni.settings.SettingsItems;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchDialog {

	private Context context;
	private EditText searchText;
	private View dialogView;
	private LayoutInflater inflater;
	private FrameLayout frameView;
	private ImageView searchIcon;
	private static ListView listView;
	private static SearchAdapter adapter;
	public static ArrayList<NaviMarker> searchList;

	public SearchDialog(Context context) {
		this.context = context;

	}

	public void showDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.context).setTitle(MainActivity.context
				.getString(R.string.searchForPlace));

		builder.setNegativeButton(
				MainActivity.context.getString(R.string.cancel),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
					}
				});

		frameView = new FrameLayout(MainActivity.context);
		builder.setView(frameView);

		final AlertDialog alertDialog = builder.create();
		inflater = alertDialog.getLayoutInflater();
		dialogView = inflater.inflate(R.layout.search_dialog, frameView);
		searchText = (EditText) dialogView.findViewById(R.id.searchText);
		searchIcon = (ImageView) dialogView.findViewById(R.id.searchButton);
		searchIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				String search = searchText.getText().toString();
				if (!search.isEmpty()) {
					searchList = MapFragment.searchByParameter(search,
							MapFragment.currentMarker);

					adapter = new SearchAdapter(MainActivity.context);
					listView.setAdapter(adapter);
				} else {
					Toast.makeText(
							context,
							MainActivity.context
									.getString(R.string.searchForPlace),
							Toast.LENGTH_LONG).show();
				}

			}
		});
		listView = (ListView) dialogView.findViewById(R.id.searchList);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int position,
					long arg3) {
				NaviMarker temp = NaviMarker.getMarkerFromListByName(searchList
						.get(position).getName());
				MapFragment.setFocusOnLatLng(temp.getLatLng(),
						MapFragment.mMap, true);
				alertDialog.cancel();
				//stawiamy na tej mapie nowy marker - na wypadek, gdyby byl ukryty i otwieramy chmurke
				Marker marker = MapFragment.mMap.addMarker(temp.getMarker());
				marker.showInfoWindow();
			}

		});

		alertDialog.show();

	}

}
