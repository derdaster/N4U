package navi4uni.calendar;

import java.util.ArrayList;

import navi4uni.gui.R;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RightCalendarAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<String> dataArrayReft;
	ViewHolder holder;
	Context context;

	public RightCalendarAdapter(Context context, ArrayList<String> dataArrayReft) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.dataArrayReft = dataArrayReft;
		this.context = context;
	}

	@Override
	public int getCount() {
		return dataArrayReft.size();
	}

	@Override
	public Object getItem(int position) {
		return dataArrayReft.get(position);
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

		String text = dataArrayReft.get(position);

		if (text.equals(context.getString(R.string.search))) {
			holder.iv.setImageResource(R.drawable.filtracja);
		}
		if (text.equals(context.getString(R.string.addEvent))) {
			holder.iv.setImageResource(R.drawable.dodajwydarzenie);
		}
		if (text.equals(context.getString(R.string.dailyPlan))) {
			holder.iv.setImageResource(R.drawable.plandnia);
		}
		/*if (text.equals(context.getString(R.string.list))) {
			holder.iv.setImageResource(R.drawable.listawydarzen);
		}*/

		holder.text.setText(dataArrayReft.get(position));
		holder.getTextView().setTextColor(Color.BLACK);

		return convertView;
	}

	private class ViewHolder {
		TextView text;
		ImageView iv;

		public TextView getTextView() {
			return text;
		}

	}

}