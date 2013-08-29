package szechy.abacus;

import java.util.List;

public class Alliance {
	private final int number;
	private TeamItem captain;
	private TeamItem firstPick;
	private TeamItem secondPick;
	private final boolean fourTeamMode;
	private TeamItem thirdPick;
	
	public Alliance(boolean mode){
		number = 0;
		captain = new TeamItem(0, "f", "f", "f");
		firstPick = new TeamItem(0, "f", "f", "f");
		secondPick = new TeamItem(0, "f", "f", "f");
		thirdPick = new TeamItem(0, "f", "f", "f");
		fourTeamMode = mode;
	}
	
	public Alliance(int allianceNumber, boolean mode){
		number = allianceNumber;
		captain = new TeamItem(0, "f", "f", "f");
		firstPick = new TeamItem(0, "f", "f", "f");
		secondPick = new TeamItem(0, "f", "f", "f");
		thirdPick = new TeamItem(0, "f", "f", "f");
		fourTeamMode = mode;
	}
	
	public Alliance(int allianceNumber, TeamItem capt, TeamItem pickOne, TeamItem pickTwo){
		number = allianceNumber;
		captain = capt;
		firstPick = pickOne;
		secondPick = pickTwo;
		fourTeamMode = false;
	}
	
	public Alliance(int allianceNumber, TeamItem capt, TeamItem pickOne, TeamItem pickTwo, TeamItem pickThree){
		number = allianceNumber;
		captain = capt;
		firstPick = pickOne;
		secondPick = pickTwo;
		thirdPick = pickThree;
		fourTeamMode = true;
	}
	
	public Alliance(int allianceNumber, TeamItem[] teams){
		number = allianceNumber;
		captain = teams[0];
		firstPick = teams[1];
		secondPick = teams[2];
		if(teams.length > 3)
		{
			thirdPick = teams[3];
			fourTeamMode = true;
		}
		else
			fourTeamMode = false;
		
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
	
	public TeamItem getThirdPick(){
		return thirdPick;
	}
	
	public String getAllianceAsString(){
		String concat = "";
		if(number == 1)
			concat += "1st ";
		else if (number == 2)
			concat += "2nd ";
		else if (number == 3)
			concat += "3rd ";
		else
			concat += (number + "th ");
		
		if(captain.getNumber() != 0)
			concat += captain.getNumber() + " ";
		if(firstPick.getNumber() != 0)
			concat += firstPick.getNumber() + " ";
		if(secondPick.getNumber() != 0)
			concat += secondPick.getNumber() + " ";
		if(thirdPick.getNumber() != 0)
			concat += thirdPick.getNumber();
		
		return concat;
	}
	
	public boolean addTeam(TeamItem team){
		if(captain.getNumber() == 0)
			captain = team;
		else if(firstPick.getNumber() == 0)
			firstPick = team;
		else if(secondPick.getNumber() == 0)
			secondPick = team;
		else if(fourTeamMode & thirdPick.getNumber() == 0)
				thirdPick = team;
		else
			return false;
		return true;
	}
	
	public TeamItem removeLastTeam(){
		TeamItem removed = new TeamItem(0, "", "", "");
		boolean checkedForFour = true;
		if(fourTeamMode)
		{
			if(thirdPick.getNumber() != 0)
			{
				removed = thirdPick;
				thirdPick = new TeamItem(0, "", "", "");
				checkedForFour = false;
			}
		}
		
		if(checkedForFour)
		{
			if(secondPick.getNumber() != 0)
			{
				removed = secondPick;
				secondPick = new TeamItem(0, "", "", "");
			}
			else if (firstPick.getNumber() != 0)
			{	
				removed = firstPick;
				firstPick = new TeamItem(0, "", "", "");
			}
			else if (captain.getNumber() != 0)
			{	
				removed = captain;
				captain = new TeamItem(0, "", "", "");
			}
		}
		return removed;
	}
	
	public int numberOfTeams(){
		int number = 4;
		if(captain.getNumber() != 0)
			number--;
		if (firstPick.getNumber() != 0)
			number--;
		if (secondPick.getNumber() != 0)
			number--;
		if (thirdPick.getNumber() != 0)
			number--;
		
		return number;
	}
	
	@SuppressWarnings("null")
	public List<TeamItem> getTeamList(){
		List<TeamItem> teamList = null;
		teamList.add(captain);
		teamList.add(firstPick);
		teamList.add(secondPick);
		if(fourTeamMode)
			teamList.add(thirdPick);
		return teamList;
	}
	
	
}
