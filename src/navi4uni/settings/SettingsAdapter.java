package navi4uni.settings;

import navi4uni.gui.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SettingsAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ViewHolder holder;
	
	
	public SettingsAdapter(Context context){
		inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	
	}
	

	@Override
	public int getCount() {
		return SettingsFragment.settingsList.size();
	}

	@Override
	public Object getItem(int position) {
		return SettingsFragment.settingsList.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = inflater.inflate(R.layout.settings_view, null);
		holder = new ViewHolder();
		
		holder.mainName = (TextView) convertView.findViewById(R.id.mainNameOfSettings);
		holder.additionalInformation = (TextView) convertView.findViewById(R.id.additionalInformation);
		convertView.setTag(holder);

		
		holder.mainName.setText(SettingsFragment.settingsList.get(position).getMainName());
		holder.additionalInformation.setText(SettingsFragment.settingsList.get(position).getAdditionalInformation());

		return convertView;
	}
	
	private static class ViewHolder {
		TextView mainName;
		TextView additionalInformation;
	    
	}
	

	
}
