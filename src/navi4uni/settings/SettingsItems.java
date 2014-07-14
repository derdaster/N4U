package navi4uni.settings;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import navi4uni.gui.MainActivity;
import navi4uni.gui.R;
import navi4uni.map.MapFragment;
import navi4uni.places.CampusXmlInterpteter;
import navi4uni.places.NaviMarker;
import navi4uni.places.universityTuple;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class SettingsItems {

	private String mainName;
	private String additionalInformation;
	private boolean isClickable;
	private static ProgressDialog dialog;
	private static Handler handler;
	private static String selected;
	private static universityTuple uni;

	public SettingsItems(String mainName, String additionalInformation,
			boolean isClickable) {
		this.mainName = mainName;
		this.additionalInformation = additionalInformation;
		this.isClickable = isClickable;

	}

	public String getMainName() {
		return mainName;
	}

	public String getAdditionalInformation() {
		return additionalInformation;
	}

	public boolean isClickable() {
		return isClickable;
	}

	public static void onClick(int position) {

		SettingsItems item = SettingsFragment.settingsList.get(position);

		if (item.isClickable) {

			switch (position) {

			case 1: {
				new RefreshMap().execute();
				break;
			}
			case 2: {
				showDialog();
				break;
			}

			}

		}
	}

	private static void showDialog() {

		ArrayList<String> stringList = new ArrayList<String>(
				new CampusXmlInterpteter(MainActivity.context).getCampusLinks()
						.keySet());

		final CharSequence[] items = MainActivity.campus
				.getCampusLinks()
				.keySet()
				.toArray(
						new CharSequence[MainActivity.campus.getCampusLinks()
								.size()]);
		stringList.toArray(new CharSequence[stringList.size()]);

		AlertDialog.Builder builder = new AlertDialog.Builder(
				MainActivity.context);
		builder.setTitle("Wybierz kampus");
		builder.setItems(items, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int item) {
				selected = items[item].toString();
				uni = MainActivity.campus.getCampusLinks().get(selected);

				if (uni != null) {

					SettingsItems.dialog = ProgressDialog.show(
							MainActivity.context, null, "Proszę czekać..");

					handler = new Handler() {
						public void handleMessage(Message msg) {
							if (msg.what == 0) {
								MainActivity.currentPosition = 0;
								MainActivity.imgRightMenu.setVisibility(View.VISIBLE);
								MainActivity.mDrawerlayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, MainActivity.mDrawerListRight);
								FragmentTransaction transaction = MainActivity.fragmentManager.beginTransaction(); 
								transaction.replace(R.id.Frame_Layout, new MapFragment());
								SettingsItems.dialog.dismiss();
								transaction.commit();
							}
						}
					};
					Thread thread = new Thread() {
						@Override
						public void run() {
							try {

								NaviMarker.markerList = MainActivity.campus.parseCampusXMLfromDoc(
										MainActivity.xmlDownloader.download(uni
												.getLink()), selected);
								NaviMarker.setDefaultPositions(
										MainActivity.campus.getCampusLinks()
												.get(selected).getClat(),
										MainActivity.campus.getCampusLinks()
												.get(selected).getClong());
								MainActivity.campus.downloadImages();

							} catch (MalformedURLException e) {
								e.printStackTrace();
							} catch (InterruptedException e) {
								e.printStackTrace();
							} catch (ExecutionException e) {
								e.printStackTrace();
							}
							handler.sendEmptyMessage(0);
						}
					};
					thread.start();

				}

			}
		});
		AlertDialog alert = builder.create();
		alert.show();

	}

}
