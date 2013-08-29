package szechy.abacus;

public class TeamItem {

	private final int number;
	private final String name;
	private final String hometown;
	private final String sponsors;
	
	public TeamItem(int teamNumber, String teamName, String teamHometown, String teamSponsors){
		number = teamNumber;
		name = teamName;
		hometown = teamHometown;
		sponsors = teamSponsors;
	}

	public int getNumber(){
		return number;
	}
	
	public String getName(){
		return name;
	}
	
	public String getHometown(){
		return hometown;
	}
	
	public String getSponsors(){
		return sponsors;
	}
	
	public String getTeamInfo(){
		return (Integer.toString(number) + "\t" + name + "\t" + hometown + "\t" + 
				sponsors);
	}
}
