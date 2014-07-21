package navi4uni.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import com.google.android.gms.maps.model.LatLng;

import navi4uni.calendar.CalendarDialog;
import navi4uni.calendar.CalendarFragment;
import navi4uni.calendar.NavigationDrawerRightForCalendar;
import navi4uni.favourite.FavouriteDialog;
import navi4uni.filter.FilterDialog;
import navi4uni.map.MapFragment;
import navi4uni.netLayer.XMLFileDownloader;
import navi4uni.places.CampusXmlInterpteter;
import navi4uni.places.Favorites;
import navi4uni.places.NaviMarker;
import navi4uni.search.SearchDialog;
import navi4uni.settings.SettingsFragment;
import storage.SaveData;
import storage.Tuple;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Telephony.Mms;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class MainActivity extends ActionBarActivity {

	private enum DrawerElementsLeft {
		MAP, CALENDAR, SETTINGS
	};

	private enum DrawerElementsRight {
		MAP, CALENDAR
	};

	private enum DrawerElementsRightFiltr {
		SEARCH, FILTR, FAVOURITE
	};
	
	private enum DrawerElementsRightCalendar {
		SEARCH, ADDEVENT, PLAN, LIST
	};


	public static FragmentManager fragmentManager;
	public static DrawerLayout mDrawerlayout;
	public static ListView mDrawerListLeft, mDrawerListRight;
	private ActionBarDrawerToggle mDrawerToggle;
	public static ImageButton imgLeftMenu, imgRightMenu;
	private Activity activity;
	public static Context context;
	public static CampusXmlInterpteter campus;
	public static SaveData storage = new SaveData();
	public static int currentPosition = 0;
	public static XMLFileDownloader xmlDownloader = new XMLFileDownloader(
			MainActivity.context);;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerListLeft = (ListView) findViewById(R.id.drawer_list_left);
		mDrawerListRight = (ListView) findViewById(R.id.drawer_list_right);
		imgLeftMenu = (ImageButton) findViewById(R.id.searchButton);
		imgRightMenu = (ImageButton) findViewById(R.id.imgRightMenu);

		activity = this;

		context = this;
		campus = new CampusXmlInterpteter(context);

		// wczytywanie pliku jesli istnieje
		try {
			Tuple stored = storage.GetDataFromLocalStorage();
			NaviMarker.markerList.addAll(stored.markerList);
			// NaviMarker.images.putAll(stored.images);
			Iterator<NaviMarker> itr = NaviMarker.markerList.iterator();
			while (itr.hasNext()) {
				NaviMarker a = itr.next();
				Log.i("Markery Z PLIKU!!!!", "Marker:" + a.getName() + " tag: "
						+ a.getTag());
			}
			for (String s : stored.favoriteList) {
				Log.i("Odczyt fav", s);
			}
			NaviMarker.tagSet.addAll(stored.tagSet);
			if(stored.dPosition != null){
				NaviMarker.defaultPosition = stored.dPosition;
			}
			Favorites.setFavoriteList(stored.favoriteList);
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		refreshMap(true);

		mDrawerlayout.setDrawerListener(mDrawerToggle);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.header, null);

		imgLeftMenu = (ImageButton) v.findViewById(R.id.searchButton);
		imgRightMenu = (ImageButton) v.findViewById(R.id.imgRightMenu);

		getSupportActionBar().setDisplayShowTitleEnabled(false);
		getSupportActionBar().setDisplayUseLogoEnabled(false);
		getSupportActionBar().setDisplayShowCustomEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				new ColorDrawable(Color.parseColor("#00a2c8")));
		getSupportActionBar().setIcon(
				new ColorDrawable(getResources().getColor(
						android.R.color.transparent)));
		getSupportActionBar().setCustomView(v);

		imgLeftMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				if (mDrawerlayout.isDrawerOpen(mDrawerListRight)) {
					mDrawerlayout.closeDrawer(mDrawerListRight);
				}
				if (mDrawerlayout.isDrawerOpen(mDrawerListLeft)) {
					mDrawerlayout.closeDrawer(mDrawerListLeft);
				}
				if (!mDrawerlayout.isDrawerOpen(mDrawerListLeft)) {
					mDrawerlayout.openDrawer(mDrawerListLeft);
				}
			}

		});

		imgRightMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if(currentPosition != 2){
					if (mDrawerlayout.isDrawerOpen(mDrawerListLeft)) {
						mDrawerlayout.closeDrawer(mDrawerListLeft);
					}
					if (mDrawerlayout.isDrawerOpen(mDrawerListRight)) {
						mDrawerlayout.closeDrawer(mDrawerListRight);
					}
					if (!mDrawerlayout.isDrawerOpen(mDrawerListRight)) {
						mDrawerlayout.openDrawer(mDrawerListRight);
					}
				}else{
					if (mDrawerlayout.isDrawerOpen(mDrawerListLeft)) {
						mDrawerlayout.closeDrawer(mDrawerListLeft);
					}
					if (mDrawerlayout.isDrawerOpen(mDrawerListRight)) {
						mDrawerlayout.closeDrawer(mDrawerListRight);
					}
					if (!mDrawerlayout.isDrawerOpen(mDrawerListRight)) {
						mDrawerlayout.closeDrawer(mDrawerListRight);
					}
				}
			}
		});

		new NavigationDrawerLeft(this.getApplicationContext(), mDrawerListLeft)
				.fillLeftList();
		new NavigationDrawerRight(this.getApplicationContext(),
				mDrawerListRight).fillRightList();

		changeFragmentLeft(0);

		mDrawerListLeft.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				changeFragmentLeft(position);
			}
		});

		mDrawerListRight.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				changeFragmentRight(position);

			}
		});
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("WORKAROUND_FOR_BUG_19917_KEY",
				"WORKAROUND_FOR_BUG_19917_VALUE");
		Tuple store = new Tuple(NaviMarker.markerList, NaviMarker.images,
				Favorites.getFavoriteList(), NaviMarker.defaultPosition,
				NaviMarker.tagSet);
		try {
			storage.SaveDataOnLocalStorage(store);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onSaveInstanceState(outState);
	}

	private void changeFragmentLeft(int position) {
		
		MapFragment.saveCameraPosition();
		
		fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();

		switch (DrawerElementsLeft.values()[position]) {

		case MAP: {
			imgRightMenu.setVisibility(View.VISIBLE);
			currentPosition = 0;
			destroyMapFragment();
			setRightDrawer(position);
			transaction.replace(R.id.Frame_Layout, new MapFragment());
			break;
		}

		case CALENDAR: {
			imgRightMenu.setVisibility(View.VISIBLE);
			currentPosition = 1;
			setRightDrawer(position);
			transaction.replace(R.id.Frame_Layout, new CalendarFragment());
			break;
		}

		case SETTINGS: {
			imgRightMenu.setVisibility(View.GONE);
			currentPosition = 2;
			setRightDrawer(position);
			transaction.replace(R.id.Frame_Layout, new SettingsFragment());
			break;
		}
		
		}
		transaction.commit();
		mDrawerlayout.closeDrawer(mDrawerListLeft);

	}

	private void changeFragmentRight(int position) {
		
		
		switch (DrawerElementsRight.values()[currentPosition]) {
		
		case MAP: {
			switch (DrawerElementsRightFiltr.values()[position]) {

			case SEARCH:{
				new SearchDialog(context).showDialog();
				
				
				break;
			}
			
			case FILTR: {

				try {
					if (!FilterDialog.tagList.isEmpty()) {
						FilterDialog.showDialog();

					}
				} catch (Exception e) {
					new FilterDialog(this, NaviMarker.tagSet);
					FilterDialog.showDialog();
				}

				break;
			}
			case FAVOURITE: {

				FavouriteDialog fav = new FavouriteDialog();
				fav.showDialog();

				break;
			}

			default: {
				break;
			}

			}
			break;
		}
		case CALENDAR: {
			switch (DrawerElementsRightCalendar.values()[position]) {

			case SEARCH: {

				CalendarDialog dialog = new CalendarDialog();
				dialog.showSearchDialog();

				break;
			}
			case ADDEVENT: {

				CalendarDialog dialog = new CalendarDialog();
				dialog.setFromDrawerRight(true);
				dialog.showCalendarDialog();

				break;
			}
			case PLAN: {

				CalendarDialog dialog = new CalendarDialog();
				dialog.showDayPlan();

				break;
			}
			case LIST: {

				FavouriteDialog fav = new FavouriteDialog();
				fav.showDialog();

				break;
			}
			default: {
				break;
			}

			}
			break;
		}
		}

		// transaction.commit();
		mDrawerlayout.closeDrawer(mDrawerListRight);
	}

	private void destroyMapFragment() {
		if (MapFragment.mMap != null) {
			MainActivity.fragmentManager
					.beginTransaction()
					.remove(MainActivity.fragmentManager
							.findFragmentById(R.id.location_map)).commit();
			MapFragment.mMap = null;
		}
	}

	private void setRightDrawer(int position) {

		switch (position) {

		case 0: {
			mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mDrawerListRight);
			new NavigationDrawerRight(this.getApplicationContext(),
					mDrawerListRight).fillRightList();
			break;
		}

		case 1: {
			mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, mDrawerListRight);
			new NavigationDrawerRightForCalendar(this.getApplicationContext(),
					mDrawerListRight).fillRightCalendarList();
			break;
		}
		
		default:{
			mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, mDrawerListRight);
		}

		}
	}

	public static void refreshMap(boolean links) { 

		try {
			if (links == true) {
				campus.parseLinksXML(xmlDownloader
						.download("http://masuk.drl.pl/xmlfile/links2.xml")); // linki wczytuja sie do atrybutu klasy 
																				//CampusXmlInterpteter
			}
			campus.getCampusNames(); // pobranie listy dostepnych campusow
			String timeStamp = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
					.format(Calendar.getInstance().getTime());
			context.getSharedPreferences("DATE", Context.MODE_PRIVATE).edit()
					.putString("currentDate", timeStamp).apply();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

	}
	


}