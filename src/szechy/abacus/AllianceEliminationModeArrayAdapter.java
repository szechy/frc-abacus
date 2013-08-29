package szechy.abacus;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AllianceEliminationModeArrayAdapter extends ArrayAdapter<Alliance>{
	
	private final Context context;
	private final Alliance[] alliances;
	private int gridPosition = 0;
	private int round = 0;
	private int position = 0;
	private boolean fourTeam = false;
	
	public AllianceEliminationModeArrayAdapter(Context contextImport, Alliance[] alliancesImport){
		super(contextImport, R.layout.alliance_eliminations_mode_item, alliancesImport);
		context = contextImport;
		alliances = alliancesImport;
	}
	
	//round 0 = captain, 1 = first pick, 2 = second pick, 3 = third pick
	public boolean addTeam(TeamItem newTeam){
		Log.d("EliminationAllianceAdapter", "added new Team on Alliance " + position + " in round " + round);
		//Straight up four teams - IRI format. 1-8 1-8 8-1
		if(round <= 3 & position >= 0)
		{
			alliances[position].addTeam(newTeam);
			if((round == 0) & position <= 7)
			{
				round++;
			}
			else if((round == 1) & position < 7)
			{
				position++;
				round--;
			}
			else if((round == 1) & position == 7)
			{
				round++;
				position = 0;
			}
			else if((round == 2) & position < 7)
				position++;
			else if (round == 2 & position == 7)
				round = 3;
			else if(round == 3 & (position <= 7 & position >= 0))
				position--;
			else
				return false;
			notifyDataSetChanged();
			return true;
		}
		return false;
		
		/* This is normal serpentine... AKA not IRI format, but regular season format! 1-8 8-1
		 * if(round < 3)
		{
			alliances[gridPosition].addTeam(newTeam);
			if(round == 0 & gridPosition < 7)
				round++;
			else if(round == 1 & gridPosition < 7)
			{
				gridPosition++;
				round--;
			}
			else if((round == 0 | round == 1) & gridPosition == 7)
				round++;
			else if(round == 2 & gridPosition > 0){
				gridPosition--;
				Log.d("eliminationmode", "got into special case");
			}
			else
				return false;
			notifyDataSetChanged();
			return true;
		}*/
	}
	
	public TeamItem removeTeam(){
		TeamItem removed = new TeamItem(0, "", "", "");
		if (round == 0)
		{
				if(position != 0)
				{
					round = 1;
					position--;
				}
				
				removed = alliances[position].removeLastTeam();
		}
		else if (round == 1)
		{
			removed = alliances[position].removeLastTeam();
			round = 0;
		}
		else if (round == 2)
		{
			if(position == 0)
			{
				round = 1;
				position = 7;
			}
			else
				position--;

			removed = alliances[position].removeLastTeam();
		}
		else if(round == 3)
		{
			if(position == 7)
				round--;
			else
				position++;
			removed = alliances[position].removeLastTeam();
		}
		Log.d("Alliance adapter", "removed Alliance " + position + " in round " + round);
		notifyDataSetChanged();
		return removed;
	}
	
	public Alliance[] getAlliances(){
		return alliances;
	}
	
	@Override
	public View getView(int viewGridPosition, View convertView, ViewGroup parent){
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.alliance_eliminations_mode_item, parent, false);
		
		switch(viewGridPosition){
			case 0:
				gridPosition = 0;
				break;
			case 1:
				gridPosition = 7;
				break;
			case 2:
				gridPosition = 1;
				break;
			case 3:
				gridPosition = 6;
				break;
			case 4:
				gridPosition = 2;
				break;
			case 5:
				gridPosition = 5;
				break;
			case 6:
				gridPosition = 3;
				break;
			case 7:
				gridPosition = 4;
			default:
				break;
		}
		
		TextView allianceNumber = (TextView)rowView.findViewById(R.id.allianceNumber);
		allianceNumber.setText(Integer.toString(gridPosition+1));
		
		TextView allianceCaptain = (TextView)rowView.findViewById(R.id.allianceCaptain);
		String something = Integer.toString(alliances[gridPosition].getCaptain().getNumber());
		if(alliances[gridPosition].getCaptain().getNumber() != 0)
			allianceCaptain.setText(something);
		else
			allianceCaptain.setText("");
		
		TextView allianceFirstPick = (TextView)rowView.findViewById(R.id.allianceFirstPick);
		something = Integer.toString(alliances[gridPosition].getFirstPick().getNumber());
		if(alliances[gridPosition].getFirstPick().getNumber() != 0)
			allianceFirstPick.setText(something);
		else
			allianceFirstPick.setText("");
		
		TextView allianceSecondPick = (TextView)rowView.findViewById(R.id.allianceSecondPick);
		something = Integer.toString(alliances[gridPosition].getSecondPick().getNumber());
		if(alliances[gridPosition].getSecondPick().getNumber() != 0)
			allianceSecondPick.setText(something);
		else
			allianceSecondPick.setText("");
		
		if(fourTeam){
		
			TextView allianceThirdPick = (TextView)rowView.findViewById(R.id.allianceThirdPick);
			something = Integer.toString(alliances[gridPosition].getThirdPick().getNumber());
			if(alliances[gridPosition].getThirdPick().getNumber() != 0)
				allianceThirdPick.setText(something);
			else
				allianceThirdPick.setText("");
			}
		
		return rowView;
	}
	
	@Override
	public boolean isEnabled(int position){
		return false;
	}
	
	public int getRound(){
		return round;
	}
	
	public int getPosition(){
		return position;
	}
	
	public void setRound(int newOne){
		round = newOne;
	}
	
	public void setPosition(int newOne){
		position = newOne;
	}
	
	public void setFourTeamMode(){
		fourTeam = true;
	}
	
	public void setTraditionalMode(){
		fourTeam = false;
	}

	public Alliance getRecentAlliance(boolean additionNotRemoval) {
		
		int tempPosition =  position;
		
		if(additionNotRemoval)
		{
			if (round == 0)
			{
				if(tempPosition != 0)
					tempPosition--;
			}
			else if (round == 1)
			{
			}
			else if (round == 2)
			{
				if(position == 0)
					tempPosition = 7;
				else
					tempPosition--;

			}
			else if(round == 3)
			{
				if(position == 7)
					round--;
				else
					tempPosition++;
			}
		}
		else
		{
			if((round == 1) & position < 7)
				tempPosition++;
			else if((round == 1) & position == 7)
				tempPosition = 0;
			else if((round == 2) & position < 7)
				tempPosition++;
			else if (round == 2 & position == 7)
				round = 3;
			else if(round == 3 & (position <= 7 & position >= 0))
				tempPosition--;
		}
		
		return alliances[tempPosition];
	}

}