package szechy.abacus;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TeamItemArrayAdapter extends ArrayAdapter<TeamItem> {
	private final Context context;
	private ArrayList<TeamItem> values;
	private final boolean normal;
	private final String name;

	public TeamItemArrayAdapter(Context context, ArrayList<TeamItem> values, boolean normalList, String name) {
		super(context, R.layout.team_item, values);
		this.context = context;
		this.values = values;
		Log.d("TeamArrayAdapter", "" + this.values.size());
		normal = normalList;
		this.name = name;
	}

	public void setTeamItemArray(TeamItem newValues){
		values = new ArrayList<TeamItem>(Arrays.asList(newValues));
	}
	
	public TeamList getTeamList(){
		return new TeamList(name, values);
	}
	
	public void removeTeam(TeamItem team){
		try
		{
			Log.d("removing team", team.getNumber() + " " + team.getName());
			values.remove(values.indexOf(team));
		}
		catch(ArrayIndexOutOfBoundsException e){
			Log.d("removing team", "failed to remove team " + team.getNumber());
		}
		notifyDataSetChanged();
	}
	
	public void addTeam(TeamItem team){
		//Log.d("adding team", team.getNumber() + " " + team.getName());
		int i = 0;
		for(i = 0; (i < values.size()) && values.get(i).getNumber() < team.getNumber(); i++)
		{		
		}
		values.add(i, team);
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)
				context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView;
		if(normal)
		{
			rowView = inflater.inflate(R.layout.team_item, parent, false);
			TextView teamNumber = (TextView)rowView.findViewById(R.id.teamNumber);
			TextView teamName = (TextView)rowView.findViewById(R.id.teamName);
			TextView teamHometown = (TextView)rowView.findViewById(R.id.teamHometown);
			teamNumber.setText(Integer.toString(values.get(position).getNumber()));
			teamName.setText(values.get(position).getName());
			teamHometown.setText(values.get(position).getHometown());
		}
		else 
		{
			rowView = inflater.inflate(R.layout.elimination_mode_team_item, parent, false);
			TextView teamNumber = (TextView)rowView.findViewById(R.id.teamNumberEliminations);
			TextView teamName = (TextView)rowView.findViewById(R.id.teamNameEliminations);
			teamNumber.setText(Integer.toString(values.get(position).getNumber()));
			teamName.setText(values.get(position).getName());
		}
		
		return rowView;
	}
}