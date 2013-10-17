package szechy.abacus;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TeamItemDbAdapter extends AbstractDbAdapter {
	
	/*public static final String TABLE_TEAMS = "teams";
	public static final String COLUMN_NUMBER = "_id";
	public static final String COLUMN_NAME = "teamname";
	public static final String COLUMN_HOMETOWN = "hometown";
	public static final String COLUMN_SPONSORS = "sponsors";*/
	
	private String[] allColumns = { AbstractDbAdapter.COLUMN_ALLIANCE_NUMBER,
			AbstractDbAdapter.COLUMN_ALLIANCE_CAPTAIN, AbstractDbAdapter.COLUMN_ALLIANCE_PICK_1,
			AbstractDbAdapter.COLUMN_ALLIANCE_PICK_2, AbstractDbAdapter.COLUMN_ALLIANCE_PICK_3};
	
	int teamLength = 0;
	
	public TeamItemDbAdapter(Context ctx){
		super(ctx);
	}
	
	//From here on out, all CRUD - create replace upload delete
	
	public TeamItem createTeamItem(TeamItem team){
		teamLength++;
		ContentValues values = new ContentValues();
		values.put(AbstractDbAdapter.COLUMN_NAME, team.getName());
		values.put(AbstractDbAdapter.COLUMN_NUMBER, team.getNumber());
		values.put(AbstractDbAdapter.COLUMN_HOMETOWN, team.getHometown());
		values.put(AbstractDbAdapter.COLUMN_SPONSORS, team.getSponsors());
		
		long insertId = mDb.insert(AbstractDbAdapter.TABLE_TEAMS, null, values);
		
		Cursor cursor = mDb.query(AbstractDbAdapter.TABLE_TEAMS, allColumns, 
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
		Cursor cursor = mDb.query(AbstractDbAdapter.TABLE_TEAMS, 
				null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			datList.add(cursorToTeamItem(cursor));
			cursor.moveToNext();
		}
		
		return datList;
	}
	
	public TeamItem getTeamItem(int teamNumber){
		Cursor cursor = mDb.query(AbstractDbAdapter.TABLE_TEAMS, null, AbstractDbAdapter.COLUMN_NUMBER + "=" + String.valueOf(teamNumber),
				null, null, null, null);
		cursor.moveToFirst();
		return cursorToTeamItem(cursor);
	}

	
}
