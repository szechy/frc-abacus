package stig.scout;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class AllianceEliminationModeArrayAdapter extends ArrayAdapter<Alliance>{
	
	private final Context context;
	private final Alliance[] alliances;
	
	public AllianceEliminationModeArrayAdapter(Context contextImport, Alliance[] alliancesImport){
		super(contextImport, R.layout.elimination_mode_alliance, alliancesImport);
		context = contextImport;
		alliances = alliancesImport;
	}
	
	public void addNewTeam(TeamItem newTeam){
		
	}
	
	public Alliance[] getAlliances(){
		return alliances;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
	}

}
