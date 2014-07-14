package navi4uni.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import navi4uni.gui.R;
import navi4uni.places.Tags;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class FilterAdapter extends BaseAdapter {

	LayoutInflater inflater;
	private List<String> stringsList;
	private ViewHolder holder;
	
	
	public FilterAdapter(Context context, Set<String> tagSet ){
		inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		try{
			if(!FilterFragment.tagList.isEmpty()){
				
			}
			
		}catch(Exception e){
			stringsList = new ArrayList(tagSet);
			FilterFragment.tagList = new ArrayList<Tags>();
			addToList();
		}
		
		
	}
	
	public void addToList(){
		for(int i = 0 ; i < stringsList.size(); i++){
			
			Tags t = new Tags(stringsList.get(i));
			if(t.getName().equals("Budynek")){
				t.setSelected(true);
			}
			FilterFragment.tagList.add(t);

		}
	}
	
	
	@Override
	public int getCount() {
		return FilterFragment.tagList.size();
	}

	@Override
	public Object getItem(int position) {
		return FilterFragment.tagList.get(position);
	}

	@Override
	public long getItemId(int id) {
		return id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = inflater.inflate(R.layout.filter_view, null);
		holder = new ViewHolder();
		
		holder.text = (CheckBox) convertView.findViewById(R.id.tagCheckBox);
		holder.position = position;
		convertView.setTag(holder);
		
		holder.text.setOnClickListener( new View.OnClickListener() {  
		     public void onClick(View v) {  
		    	 CheckBox cb = (CheckBox) v;
		    	 int p = ViewHolder.onClick(v);
		    	 FilterFragment.tagList.get(p).setSelected(cb.isChecked());
		     }  
		}); 
		
		holder.text.setText(FilterFragment.tagList.get(position).getName());
		if(FilterFragment.tagList.get(position).isSelected()){
			holder.text.setChecked(true);
		}
		holder.getTextView().setTextColor(Color.BLACK);
		return convertView;

	}
	
	private static class ViewHolder {
		CheckBox text;
		int position;

		public TextView getTextView(){
			return text;
		}

		public static int onClick(View v) {

			ViewHolder vh = getViewHolder(v);
	        return vh.position; 
	    }
		
		public static ViewHolder getViewHolder(View v)
	    {
	        if(v.getTag() == null)
	        {
	            return getViewHolder((View)v.getParent());
	        }
	        return (ViewHolder)v.getTag();
	    }
		
	}
	

}






