package navi4uni.netLayer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;


public class ImageDownloader {
	//================================AsyncTask========================
	private class DownloadImg extends AsyncTask<HashMap<String,URL>, Void, HashMap<String,Bitmap>>{
		@Override
		protected HashMap<String,Bitmap> doInBackground(HashMap<String,URL>... params) {
			HashMap<String,Bitmap> images = new HashMap<String,Bitmap>();
			Iterator<Entry<String, URL>> itr = params[0].entrySet().iterator();
			
			while(itr.hasNext()){
				Map.Entry<String, URL> entry = (Entry<String, URL>) itr.next();
				Log.i("ImageDownloader", "Downloaded: "+ entry.getValue());
				if(entry.getValue().toString().trim().length() > 0){
					try{
						images.put(entry.getKey(), getBitmapFromURL(entry.getValue()));
					}catch (IOException e){
						e.printStackTrace();
						Log.e("ImageDownloaderAsyncTask", e.getMessage());
					}
				}
			}
			
			return images;
		}
		
		private Bitmap getBitmapFromURL(URL url) throws IOException{
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream in = connection.getInputStream();
				Bitmap btmp = BitmapFactory.decodeStream(in);
				return btmp;
		}
	}
	//===============================================================	
	
	
	Context context = null;
	
	public ImageDownloader(Context _context){
		this.context = _context;
	}
	
	public HashMap<String, Bitmap> download(HashMap<String,URL> nameUrlPair){
		HashMap<String, Bitmap> downloadedData = new HashMap<String, Bitmap>();
		if(nameUrlPair != null){
			try {
				downloadedData = new DownloadImg().execute(nameUrlPair).get();
			} catch (InterruptedException e) {
				e.printStackTrace();
				Log.e("ImageDownloaderDownloadMethod", e.getMessage());
			} catch (ExecutionException e) {
				e.printStackTrace();
				Log.e("ImageDownloaderDownloadMethod", e.getMessage());
			}
		}
		return downloadedData;
	}
		
}
