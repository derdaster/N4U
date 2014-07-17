package navi4uni.calendar;

import java.util.ArrayList;

import navi4uni.map.MapFragment;
import navi4uni.places.NaviMarker;

import android.app.Notification.Style;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;


public class BuildingAutoAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> mData;

    public BuildingAutoAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        mData = new ArrayList<String>();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int index) {
        return mData.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                
                if(constraint != null) {
                    // A class that queries a web API, parses the data and returns an ArrayList<Style>
                   
                    try {
                        
                        mData=MapFragment.searchNamesByParameter(constraint.toString(),
    							MapFragment.currentMarker);
                       
                        
                    }
                    catch(Exception e) {
                        Log.e("myException", e.getMessage());
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = mData;
                    filterResults.count = mData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {
                notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }
}