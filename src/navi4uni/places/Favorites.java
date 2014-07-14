package navi4uni.places;

import java.io.Serializable;
import java.util.ArrayList;


public class Favorites implements Serializable {
	private static final long serialVersionUID = 1L;
	public static ArrayList<String> favoriteList = new ArrayList<String>();

	public static ArrayList<String> getFavoriteList() {
		return favoriteList;
	}
	
	public static void setFavoriteList(ArrayList<String> fav) {
		favoriteList = fav;
	}
	
	public static void addToFavorites(String name) {
		for (NaviMarker m : NaviMarker.getMarkerList()) {
			if (m.getName().equals(name)) { // czy istnieje taki marker
				if (favoriteList.contains(name) == false) { // czy juz nie jest
															// dodany do
															// ulubionych
					favoriteList.add(name);
				}
			}
		}

	}

	public static void removeFromFavorites(String name) {
		if (favoriteList.contains(name)) {
			favoriteList.remove(name);
		}
	}

	public static boolean checkIfFavorite(String name) {
		if (favoriteList.contains(name) == true)
			return true;
		else
			return false;
	}

}
