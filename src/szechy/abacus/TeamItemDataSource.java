package szechy.abacus;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TeamItemDataSource {
	
	//Database fields
	private SQLiteDatabase database;
	private TeamSQLiteHelper dbHelper;
	private String[] allColumnsTeams = { TeamSQLiteHelper.COLUMN_NUMBER, 
			TeamSQLiteHelper.COLUMN_NAME, TeamSQLiteHelper.COLUMN_HOMETOWN,
			TeamSQLiteHelper.COLUMN_SPONSORS};
	private String[] allColumnsAlliances = { TeamSQLiteHelper.COLUMN_ALLIANCE_NUMBER,
			TeamSQLiteHelper.COLUMN_ALLIANCE_CAPTAIN, TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_1,
			TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_2, TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_3};

	
	int teamLength = 0;
	
	public TeamItemDataSource(Context context){
		dbHelper = new TeamSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	//should return the perfecto'd alliance
	public void createAlliance(Alliance dat){
		ContentValues values = new ContentValues();
		values.put(TeamSQLiteHelper.COLUMN_ALLIANCE_NUMBER, dat.getNumber());
		values.put(TeamSQLiteHelper.COLUMN_ALLIANCE_CAPTAIN, dat.getCaptain().getNumber());
		values.put(TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_1, dat.getFirstPick().getNumber());
		values.put(TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_2, dat.getSecondPick().getNumber());
		values.put(TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_3, dat.getThirdPick().getNumber());
		
		long insertId = database.insert(TeamSQLiteHelper.TABLE_ALLIANCES, null, values);
		//find a way to return an alliance
	}
	
	public Alliance getAlliance(int seed){
		Cursor cursor = database.query(TeamSQLiteHelper.TABLE_ALLIANCES, null, 
				TeamSQLiteHelper.COLUMN_ALLIANCE_NUMBER + "=" + seed,
				null, null, null, null);
		cursor.moveToFirst();
		return cursorToAlliance(cursor);
	}
	
	public Alliance updateAlliance(Alliance alliance){
		ContentValues values = new ContentValues();
		values.put(TeamSQLiteHelper.COLUMN_ALLIANCE_CAPTAIN, alliance.getCaptain().getNumber());
		values.put(TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_1, alliance.getFirstPick().getNumber());
		values.put(TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_2, alliance.getSecondPick().getNumber());
		values.put(TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_3, alliance.getThirdPick().getNumber());
		database.update(TeamSQLiteHelper.TABLE_ALLIANCES, values, TeamSQLiteHelper.COLUMN_ALLIANCE_NUMBER + "=" + alliance.getNumber(), null);
		return getAlliance(alliance.getNumber());
	}
	
	private Alliance cursorToAlliance (Cursor cursor){
		TeamItem captain = getTeamItem(cursor.getInt(1));
		TeamItem pickOne = getTeamItem(cursor.getInt(2));
		TeamItem pickTwo = getTeamItem(cursor.getInt(3));
		TeamItem pickThree = new TeamItem(0, "", "", "");
		if(cursor.getInt(4) != 0)
			pickThree = getTeamItem(cursor.getInt(4));
		Alliance alliance = new Alliance(cursor.getInt(0), captain, pickOne, pickTwo, pickThree);
	return alliance;
	}
	
	public String getAlliancesAsString(){
		String megaconcat = "";
		
		for(int i = 1; i <= 8; i++){
			Cursor cursor = database.query(TeamSQLiteHelper.TABLE_ALLIANCES, null, 
					TeamSQLiteHelper.COLUMN_ALLIANCE_NUMBER + "=" + i,
					null, null, null, null);
			cursor.moveToFirst();
			Alliance alliance = cursorToAlliance(cursor);
			megaconcat += alliance.getAllianceAsString() + "\n";
			Log.d("getAlliancesAsString", megaconcat);
		}
		
		return megaconcat;
	}
	
	public TeamItem createTeamItem(TeamItem team){
		teamLength++;
		ContentValues values = new ContentValues();
		values.put(TeamSQLiteHelper.COLUMN_NAME, team.getName());
		values.put(TeamSQLiteHelper.COLUMN_NUMBER, team.getNumber());
		values.put(TeamSQLiteHelper.COLUMN_HOMETOWN, team.getHometown());
		values.put(TeamSQLiteHelper.COLUMN_SPONSORS, team.getSponsors());
		
		long insertId = database.insert(TeamSQLiteHelper.TABLE_TEAMS, null, values);
		
		Cursor cursor = database.query(TeamSQLiteHelper.TABLE_TEAMS, allColumnsTeams, 
				null, null, null, null, null);
		cursor.moveToFirst();
		cursor.move(teamLength-1);
		TeamItem newTeam = cursorToTeamItem(cursor);
		cursor.close();
		
		Log.d("Adding team to SQL", newTeam.getTeamInfo());
		
		return newTeam;
	}
	
	private TeamItem cursorToTeamItem(Cursor cursor){
		TeamItem team = new TeamItem(0, "", "", "");
		if(cursor.getCount() != 0)
			team = new TeamItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
						cursor.getString(3));
		return team;
	}
	
	public ArrayList<TeamItem> getAllTeamItems(){
		//Log.d("TeamDatSource", "in getAllTeamItems");
		ArrayList<TeamItem> datList = new ArrayList<TeamItem>();
		Cursor cursor = database.query(TeamSQLiteHelper.TABLE_TEAMS, 
				null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			datList.add(cursorToTeamItem(cursor));
			cursor.moveToNext();
		}
		
		return datList;
	}
	
	public void upgrade(int upgrade){
		dbHelper.onUpgrade(database, 1, upgrade);
	}
	
	public TeamItem getTeamItem(int teamNumber){
		Cursor cursor = database.query(TeamSQLiteHelper.TABLE_TEAMS, null, TeamSQLiteHelper.COLUMN_NUMBER + "=" + String.valueOf(teamNumber),
				null, null, null, null);
		cursor.moveToFirst();
		return cursorToTeamItem(cursor);
	}
	
}
