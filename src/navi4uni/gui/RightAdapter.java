package navi4uni.gui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class RightAdapter extends BaseAdapter {

	LayoutInflater inflater;
	ArrayList<String> dataArrayReft;
	ViewHolder holder;
	Context context;
	
	public RightAdapter(Context context,  ArrayList<String> dataArrayReft){
		inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.dataArrayReft = dataArrayReft;
		this.context=context;
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
		
		
		if(dataArrayReft.get(position).equals(context.getString(R.string.search))){
			
			holder.iv.setImageResource(R.drawable.filtracja);
			holder.text.setText(dataArrayReft.get(position));
			holder.getTextView().setTextColor(Color.BLACK);
			
			
		}else if(dataArrayReft.get(position).equals(context.getString(R.string.filter))){

			holder.iv.setImageResource(R.drawable.filtracja2);
			holder.text.setText(dataArrayReft.get(position));
			holder.getTextView().setTextColor(Color.BLACK);
			
			
		}else if(dataArrayReft.get(position).equals(context.getString(R.string.favourite))){
			
			holder.iv.setImageResource(R.drawable.ulubione);
			holder.text.setText(dataArrayReft.get(position));
			holder.getTextView().setTextColor(Color.BLACK);
			
		}

		return convertView;
	}
	
	
	private class ViewHolder {
		TextView text;
		TextView address;
		ImageView iv;
		EditText search;

		public TextView getTextView(){
			return text;
		}
		
		public TextView getAddressView(){
			return address;
		}
		
	}

	

}