package navi4uni.places;

import navi4uni.gui.R;
import navi4uni.map.MapFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class FavoriteListener implements OnClickListener {

	NaviMarker temp;

	public FavoriteListener(NaviMarker temp) {
		this.temp = temp;
	}

	@Override
	public void onClick(View arg0) {
		temp.getName();
		if( Favorites.checkIfFavorite(temp.getName()) ) {
			Favorites.removeFromFavorites(temp.getName());
			MapFragment.favIcon.setImageResource(R.drawable.ulubione);
			Toast.makeText(arg0.getContext(), "Usunieto z ulubionych.", Toast.LENGTH_SHORT).show();
			
		} else {
			Favorites.addToFavorites(temp.getName());
			MapFragment.favIcon.setImageResource(R.drawable.ulubionetrue);
			Toast.makeText(arg0.getContext(), "Dodano do ulubionych.", Toast.LENGTH_SHORT).show();
		}
		

	}

};