package szechy.abacus;

import java.util.ArrayList;
import java.util.List;

public class TeamList {

	private final String name;
	private final ArrayList<TeamItem> teams;
	
	public TeamList(String displayName, ArrayList<TeamItem> teamsImported){
		name = displayName;
		teams = teamsImported;
	}
	
	public String getName(){
		return name;
	}
	
	public ArrayList<TeamItem> getTeams(){
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
		return listOfNumbers.substring(1, listOfNumbers.length());
	}
	
	public TeamItem getTeam(int number){
		for(TeamItem team: teams.toArray(new TeamItem[1])){
			if(team.getNumber() == number)
				return team;
		}
		
		return null;
	}
	
	public void removeTeam(TeamItem deleteTeam){
		teams.remove(teams.indexOf(deleteTeam));
	}
}