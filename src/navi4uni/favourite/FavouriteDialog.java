package navi4uni.favourite;

import java.util.ArrayList;

import navi4uni.gui.MainActivity;
import navi4uni.gui.R;
import navi4uni.places.Favorites;
import android.app.AlertDialog;
import android.widget.Toast;

public class FavouriteDialog {

	private ArrayList<String> stringList;

	public FavouriteDialog() {
		stringList = Favorites.favoriteList;
	}

	public void showDialog() {

		CharSequence[] items = stringList.toArray(new CharSequence[stringList.size()]);


		
		if(!stringList.isEmpty()){
			AlertDialog.Builder builder = new AlertDialog.Builder(
					MainActivity.context);
			builder.setTitle(MainActivity.context.getString(R.string.favouriteList));
			builder.setItems(items, new FavoritDialogListener(items));
			AlertDialog alert = builder.create();
			
			alert.show();
		}else{
			Toast.makeText(MainActivity.context, MainActivity.context.getString(R.string.favouriteListEmpty), Toast.LENGTH_SHORT).show();
		}

	}

}
