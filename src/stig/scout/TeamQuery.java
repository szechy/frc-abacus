package stig.scout;

import java.util.ArrayList;
import java.util.List;

public class TeamQuery {

	private final String name;
	private final List<TeamItem> teams;
	
	public TeamQuery(String displayName, List<TeamItem> teamsImported){
		name = displayName;
		teams = teamsImported;
	}
	
	public String getName(){
		return name;
	}
	
	public List<TeamItem> getTeams(){
		return teams;
	}
	
	public TeamItem[] getTeamsArray(){
		return teams.toArray(new TeamItem[teams.size()]);
	}
	
	public String getNumbersAsString(){
		String listOfNumbers = "";
		for(TeamItem team: teams){
			listOfNumbers += " " + String.valueOf(team.getNumber());
		}
		return listOfNumbers;
	}
}
