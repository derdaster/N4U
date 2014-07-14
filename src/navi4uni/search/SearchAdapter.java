package navi4uni.search;

import navi4uni.gui.R;
import navi4uni.settings.SettingsFragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SearchAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ViewHolder holder;

	public SearchAdapter(Context context) {
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return SearchDialog.searchList.size();
	}

	@Override
	public Object getItem(int position) {
		return SearchDialog.searchList.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		convertView = inflater.inflate(R.layout.search_view, null);
		holder = new ViewHolder();

		holder.mainName = (TextView) convertView
				.findViewById(R.id.mainSearch);
		holder.additionalInformation = (TextView) convertView
				.findViewById(R.id.addressSearch);
		convertView.setTag(holder);

		holder.mainName.setText(SearchDialog.searchList.get(position)
				.getName());
		holder.additionalInformation.setText(SearchDialog.searchList.get(
				position).getAddress());

		return convertView;
	}

	private static class ViewHolder {
		TextView mainName;
		TextView additionalInformation;

	}

}
