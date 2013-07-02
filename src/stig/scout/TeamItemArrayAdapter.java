package stig.scout;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TeamItemArrayAdapter extends ArrayAdapter<TeamItem> {
	private final Context context;
	private TeamItem[] values;
	
	public TeamItemArrayAdapter(Context context, TeamItem[] values){
		super(context, R.layout.team_item, values);
		this.context = context;
		this.values = values;
		Log.d("TeamArrayAdapter", "" + values.length);
	}
	
	public void setTeamItemArray(TeamItem[] newValues){
		values = newValues;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.team_item, parent, false);
		TextView teamNumber = (TextView)rowView.findViewById(R.id.teamNumber);
		TextView teamName = (TextView)rowView.findViewById(R.id.teamName);
		TextView teamHomeTown = (TextView)rowView.findViewById(R.id.teamHometown);
		
		Log.d("TeamItemArrayAdapter", "" + 2*position + "   " + (2*position+1));
		
		teamNumber.setText(Integer.toString(values[position].getNumber()));
		teamName.setText(values[position].getName());
		teamHomeTown.setText(values[position].getHometown());
		
		return rowView;
	}
}
