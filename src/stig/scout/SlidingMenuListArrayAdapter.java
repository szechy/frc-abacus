package stig.scout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SlidingMenuListArrayAdapter extends ArrayAdapter<TeamQuery> {
	private final Context context;
	private final TeamQuery[] list;
	
	public SlidingMenuListArrayAdapter(Context context, TeamQuery[] list){
		super(context, android.R.layout.simple_list_item_1, list);
		this.context = context;
		this.list = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
		TextView queryName = (TextView)rowView.findViewById(android.R.id.text1);
		
		queryName.setText(list[position].getName());
		
		return rowView;
	}
	
	public TeamQuery[] getTeamQueries(){
		return list;
	}
	
}
