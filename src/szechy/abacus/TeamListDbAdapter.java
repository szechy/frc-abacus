package szechy.abacus;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class TeamListDbAdapter extends TeamItemDbAdapter{

	private SQLiteDatabase database;
	private String[] columns = {};
	
	public TeamListDbAdapter(Context ctx){
		super(ctx);
	}
	
	public void addTeamList(TeamList newList){
		ContentValues values = new ContentValues();
		values.put(AbstractDbAdapter.COLUMN_LIST_NAME, newList.getName());
		values.put(AbstractDbAdapter.COLUMN_LIST_NUMBERS, newList.getNumbersAsString());
		Log.d("TeamListSQL", "Added teamlist '" + newList.getName() + "'");
		long insertId = database.insert(AbstractDbAdapter.TABLE_TEAM_LISTS, null, values);
		Log.d("TeamListSQL", "definitely added " + insertId);
	}
	
	public TeamList updateTeamList(TeamList newList){
		ContentValues values = new ContentValues();
		values.put(AbstractDbAdapter.COLUMN_LIST_NUMBERS, newList.getNumbersAsString());
		Log.d("TLds", AbstractDbAdapter.COLUMN_LIST_NAME + "=" + newList.getName());
		//database.replace(AbstractDbAdapter.TABLE_TEAM_LISTS, AbstractDbAdapter.COLUMN_LIST_NAME+"="+newList.getName(), values);
		database.update(AbstractDbAdapter.TABLE_TEAM_LISTS, values, AbstractDbAdapter.COLUMN_LIST_NAME + "='" + newList.getName() + "'", null);
		Log.d("newList", newList.getNumbersAsString());
		return getTeamList(newList.getName());
	}
	
	public TeamList getTeamList(String name){
		//Cursor cursor = database.query(AbstractDbAdapter.TABLE_TEAM_LISTS, null, AbstractDbAdapter.COLUMN_LIST_NAME + "=" + name,
			//null, null, null, null);
		Cursor cursor = database.query(AbstractDbAdapter.TABLE_TEAM_LISTS, null, AbstractDbAdapter.COLUMN_LIST_NAME + "='" + name + "'",
				null, null, null, null);
		cursor.moveToFirst();
		TeamList returnThis = cursorToTeamList(cursor);
		Log.d("giving out Teamlist", returnThis.getName() + " " + cursor.getCount() + "\n" + returnThis.getNumbersAsString());
		return returnThis;
	}
	
	/*public TeamList getTeamList(int key){
		Cursor cursor = database.query(AbstractDbAdapter.TABLE_TEAM_LISTS,
		cursor.moveToFirst();
		TeamList returnThis = cursorToTeamList(cursor);
		Log.d("giving out Teamlist", returnThis.getName() + " " + cursor.getCount() + "\n" + returnThis.getNumbersAsString());
		return returnThis;
	}*/
	
	private TeamList cursorToTeamList(Cursor cursor){
		String[] teamNumbers = cursor.getString(1).split(" ");
		int [] numbers = new int[teamNumbers.length];
		int i = 0;
		//Log.d(cursor.getString(0), cursor.getString(1));
		for(String datTeam: teamNumbers){
			numbers[i] = Integer.parseInt(datTeam);
			i++;
		}
		
		ArrayList<TeamItem> teamList = new ArrayList<TeamItem>();
		for(int team: numbers){
			//Log.d("Team", " " + team);
			teamList.add(getTeamItem(team));
		}
		
		return new TeamList(cursor.getString(0), teamList);
	}

	@SuppressWarnings("null")
	public ArrayList<TeamList> getAllTeamLists(){
		ArrayList<TeamList> datList = null;
		Cursor cursor = database.query(AbstractDbAdapter.TABLE_TEAM_LISTS, 
				null, null, null, null, null, null);
		cursor.moveToFirst();
		while(!cursor.isAfterLast()){
			datList.add(cursorToTeamList(cursor));			
			cursor.moveToNext();
		}
		
		return datList;
	}
}