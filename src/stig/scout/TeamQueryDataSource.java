package stig.scout;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TeamQueryDataSource {

	private SQLiteDatabase database;
	private TeamSQLiteHelper dbHelper;
	private String[] columns = {};
	private TeamDataSource dsTeams;
	
	public TeamQueryDataSource(Context context, TeamDataSource datDsTeams){
		dbHelper = new TeamSQLiteHelper(context);
		dsTeams = datDsTeams;
	}
	
	public void open() throws SQLException{
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public void addTeamQuery(TeamQuery newList){
		ContentValues values = new ContentValues();
		values.put(TeamSQLiteHelper.COLUMN_LIST_NAME, newList.getName());
		values.put(TeamSQLiteHelper.COLUMN_LIST_NUMBERS, newList.getNumbersAsString());
		long insertId = database.insert(TeamSQLiteHelper.TABLE_TEAM_LISTS, null, values);
	}
	
	public TeamQuery getTeamQuery(String name){
		Cursor cursor = database.query(TeamSQLiteHelper.TABLE_TEAM_LISTS, null, TeamSQLiteHelper.COLUMN_LIST_NAME + "=" + name,
			null, null, null, null);
		cursor.moveToFirst();
		return cursorToTeamQuery(cursor);
	}
	
	private TeamQuery cursorToTeamQuery(Cursor cursor){
		String[] teamNumbers = cursor.getString(1).split(" ");
		int [] numbers = new int[teamNumbers.length-1];
		int i = 0;
		for(String datTeam: teamNumbers){
			numbers[i] = Integer.parseInt(datTeam);
			i++;
		}
		
		List<TeamItem> teamList = null;
		for(int team: numbers){
			teamList.add(dsTeams.getTeamItem(team));
		}
		
		return new TeamQuery(cursor.getString(0), teamList);
	}

	@SuppressWarnings("null")
	public ArrayList<TeamQuery> getAllTeamQueries(){
		ArrayList<TeamQuery> datList = null;
		Cursor cursor = database.query(TeamSQLiteHelper.TABLE_TEAM_LISTS, 
				null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			datList.add(cursorToTeamQuery(cursor));			cursor.moveToNext();
		}
		
		return datList;
	}
}