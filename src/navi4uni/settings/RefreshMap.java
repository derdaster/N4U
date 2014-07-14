package navi4uni.settings;

import navi4uni.gui.MainActivity;
import navi4uni.gui.R;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class RefreshMap {

	private static ProgressDialog dialog;
	private static Handler handler;

	public void execute() {

		showProgressBar();
	}

	public void showProgressBar() {

		dialog = ProgressDialog.show(MainActivity.context, null,
				"Proszę czekać..");
		handler = new Handler() {
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					SettingsFragment.refreshList();
					dialog.dismiss();
				}
			}
		};
		Thread thread = new Thread() {
			@Override
			public void run() {
				MainActivity.refreshMap(true);
				handler.sendEmptyMessage(0);
			}
		};
		thread.start();
	}

}
