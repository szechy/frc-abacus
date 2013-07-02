package stig.scout;

import java.util.List;

public class Alliance {
	private final int number;
	private TeamItem captain;
	private TeamItem firstPick;
	private TeamItem secondPick;
	
	public Alliance(int allianceNumber, TeamItem capt, TeamItem pickOne, TeamItem pickTwo){
		number = allianceNumber;
		captain = capt;
		firstPick = pickOne;
		secondPick = pickTwo;
	}
	
	public Alliance(int allianceNumber, TeamItem[] teams){
		number = allianceNumber;
		captain = teams[0];
		firstPick = teams[1];
		secondPick = teams[2];
	}
	
	public int getNumber(){
		return number;
	}
	
	public TeamItem getCaptain(){
		return captain;
	}
	
	public TeamItem getFirstPick(){
		return firstPick;
	}
	
	public TeamItem getSecondPick(){
		return secondPick;
	}
	
	public boolean addNewTeam(TeamItem team){
		if(captain == null)
			captain = team;
		else if(firstPick == null)
			firstPick = team;
		else if(secondPick == null)
			secondPick = team;
		else
			return false;
		return true;
	}
	
	public int numberOfTeamsFilledIn(){
		int number = 3;
		if(captain != null)
			number--;
		if (firstPick != null)
			number--;
		if (secondPick != null)
			number--;
		return number;
	}
	
	@SuppressWarnings("null")
	public List<TeamItem> getTeamList(){
		List<TeamItem> teamList = null;
		teamList.add(captain);
		teamList.add(firstPick);
		teamList.add(secondPick);
		return teamList;
	}
	
	
}
