package navi4uni.gui;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeftAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<String> dataArrayLeft;
	ViewHolder holder;
	Context context;

	public LeftAdapter(Context context, ArrayList<String> dataArrayLeft) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.dataArrayLeft = dataArrayLeft;
		this.context = context;
	}

	@Override
	public int getCount() {
		return dataArrayLeft.size();
	}

	@Override
	public Object getItem(int position) {
		return dataArrayLeft.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.data, null);
		holder = new ViewHolder();
		holder.text = (TextView) convertView.findViewById(R.id.txtData);
		holder.iv = (ImageView) convertView.findViewById(R.id.imgView);
		convertView.setTag(holder);

		String text = dataArrayLeft.get(position);

		if (text.equals(context.getString(R.string.map))) {
			holder.iv.setImageResource(R.drawable.mapy);
		} else if (text.equals(context.getString(R.string.calendar))) {
			holder.iv.setImageResource(R.drawable.kalendarz);
		} else if (text.equals(context.getString(R.string.settings))) {
			holder.iv.setImageResource(R.drawable.ustawienia);
		}

		holder.text.setText(dataArrayLeft.get(position));
		holder.getTextView().setTextColor(Color.BLACK);
		return convertView;
	}

	private class ViewHolder {
		TextView text;
		ImageView iv;

		public TextView getTextView() {
			return text;
		}

		public ImageView getImageView() {
			return iv;
		}

	}

}
