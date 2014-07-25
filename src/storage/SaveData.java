package storage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.nio.ReadOnlyBufferException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.android.gms.maps.model.LatLng;

import navi4uni.gui.MainActivity;
import navi4uni.places.Favorites;
import navi4uni.places.NaviMarker;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class SaveData {
	
	
	/** Tuple object contains marker List and Image list, data is serialized into file path
	 * @param t - Object tuple from Places package
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void SaveDataOnLocalStorage(Tuple t) throws FileNotFoundException, IOException{
		Boolean position = false;
//		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("MapContent.bin")));	//application local
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(Environment.getExternalStorageDirectory()	//zapis do karty SD (tymczasowo do podlagania wynikow)
                + File.separator + "markerlist.bin")));	//sdCard
		ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream(new File(Environment.getExternalStorageDirectory()	//zapis do karty SD (tymczasowo do podlagania wynikow)
                + File.separator + "favlist.bin")));	//sdCard
		
		//for (NaviMarker a : t.markerLigist){
		//out.writeObject(a);
		out.writeObject(t.markerList);
		out.writeObject(t.tagSet);
		if(t.dPosition != null){
			position = true;
			out.writeObject(position);
			out.writeObject(t.dPosition.latitude);
			out.writeObject(t.dPosition.longitude);
						
		}
		else	out.writeObject(position);
		
		for(String s : t.favoriteList) {
			Log.i("Zapis fav", s);
		}
		
		out2.writeObject(t.favoriteList);
		//}
		out.flush();
		out.close();
		out2.flush();
		out2.close();
		//saveImageCollection(t.images);
	}
	
	/** Method read and deserialize data from file in file path 
	 * @return Tuple object
	 * @throws StreamCorruptedException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Tuple GetDataFromLocalStorage() throws StreamCorruptedException, FileNotFoundException, IOException, ClassNotFoundException{
	            ObjectInputStream in = new ObjectInputStream(new FileInputStream(Environment.getExternalStorageDirectory()	//zapis do karty SD (tymczasowo do podlagania wynikow)
	                    + File.separator + "markerlist.bin"));	//sdCard));
	            ObjectInputStream in2 = new ObjectInputStream(new FileInputStream(Environment.getExternalStorageDirectory()	//zapis do karty SD (tymczasowo do podlagania wynikow)
	                    + File.separator + "favlist.bin"));	//sdCard));
	            Tuple t = new Tuple();
	            
	            Object o = in.readObject();
	            t.markerList = (ArrayList<NaviMarker>)o;
	            o = in.readObject();
	            t.tagSet = (Set<String>) o;
	            o = in.readObject();
	            Boolean position = (Boolean)o;
	            if(position.booleanValue() == true){
	            	o = in.readObject();
	            	double lat = ((Double)o).doubleValue();
	            	o = in.readObject();
	            	double longi = ((Double)o).doubleValue();
	            	t.dPosition = new LatLng(lat, longi);
	            }
	            
	            for (NaviMarker a : t.markerList){
	            	Log.i("Przepatrzałka", "|"+a.getName()+"|");	            	
	            }
	            o = in2.readObject();
	            t.favoriteList = (ArrayList<String>)o;
	            for (String s : t.favoriteList){
	            	Log.i("Przepatrzałka z pliku (F)", s);	            	
	            }
	            in.close();
	            in2.close();
	            //t.images = scanDirLocalForImages();
	            return t;
	}
	
	
	
	public void saveImageCollection(HashMap<String, Bitmap> image){
		Iterator<Entry<String,Bitmap>> itr = image.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<String, Bitmap> temp = itr.next();
			MainActivity.storage.saveImageFile(temp.getValue(), temp.getKey());
		}
	}
	
	
	private void saveImageFile(Bitmap img, String name){
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		img.compress(Bitmap.CompressFormat.PNG, 40, bytes);

		File f = new File(name+".jpg"); // Zapis do domyslnego folderu appki
		
		//File f = new File(Environment.getExternalStorageDirectory()	//zapis do karty SD (tymczasowo do podlagania wynikow)
          //      + File.separator + name+".png");
		
		try {
			f.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream fo = null;
		try {
			fo = new FileOutputStream(f);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			fo.write(bytes.toByteArray());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		try {
			fo.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private HashMap<String, Bitmap> scanDirLocalForImages() throws StreamCorruptedException, FileNotFoundException, IOException{
		ArrayList<File> all = new ArrayList<File>();
		getFilesInLocalDir(new File("."), all);
		System.out.print("ZNALEZIONO:");
	    System.out.println(all);
	    
	    
	    HashMap<String, Bitmap> images = new HashMap<String, Bitmap>();
	    for(File f : all){
           FileInputStream fis = new FileInputStream(f);
           byte[] image = new byte[(int)f.length()];
           fis.read(image);
           Bitmap bitmap = new BitmapFactory().decodeByteArray(image, 0, image.length);
           images.put(f.getName(), bitmap);
	    }
	    return images;
	}
	
	private void getFilesInLocalDir(File f, ArrayList<File> files){
		File[] children = f.listFiles();
	    if (children != null) {
	        for (File child : children) {
	            files.add(child);
	            getFilesInLocalDir(child, files);
	        }
	    }
		
	}
}
