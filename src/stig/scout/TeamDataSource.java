package stig.scout;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TeamDataSource {
	
	//Database fields
	private SQLiteDatabase database;
	private TeamSQLiteHelper dbHelper;
	private String[] allColumns = { TeamSQLiteHelper.COLUMN_NUMBER, 
			TeamSQLiteHelper.COLUMN_NAME, TeamSQLiteHelper.COLUMN_HOMETOWN,
			TeamSQLiteHelper.COLUMN_SPONSORS};
	
	int teamLength = 0;
	
	public TeamDataSource(Context context){
		dbHelper = new TeamSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public TeamItem createTeamItem(TeamItem team){
		teamLength++;
		ContentValues values = new ContentValues();
		values.put(TeamSQLiteHelper.COLUMN_NAME, team.getName());
		values.put(TeamSQLiteHelper.COLUMN_NUMBER, team.getNumber());
		values.put(TeamSQLiteHelper.COLUMN_HOMETOWN, team.getHometown());
		values.put(TeamSQLiteHelper.COLUMN_SPONSORS, team.getSponsors());
		
		long insertId = database.insert(TeamSQLiteHelper.TABLE_TEAMS, null, values);
		
		Cursor cursor = database.query(TeamSQLiteHelper.TABLE_TEAMS, allColumns, 
				null, null, null, null, null);
		cursor.moveToFirst();
		cursor.move(teamLength-1);
		TeamItem newTeam = cursorToTeamItem(cursor);
		cursor.close();
		
		Log.d("Adding team to SQL", newTeam.getTeamInfo());
		
		return newTeam;
	}
	
	private TeamItem cursorToTeamItem(Cursor cursor){
		TeamItem team = new TeamItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
				cursor.getString(3));
		return team;
	}
	
	public ArrayList<TeamItem> getAllTeamItems(){
		Log.d("TeamDatSource", "in getAllTeamItems");
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
