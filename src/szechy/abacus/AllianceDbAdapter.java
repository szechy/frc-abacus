package szechy.abacus;

import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.content.ContentValues;

public class AllianceDbAdapter extends TeamItemDbAdapter {
	private SQLiteDatabase database;
	private String[] allColumns = { AbstractDbAdapter.COLUMN_ALLIANCE_NUMBER,
			AbstractDbAdapter.COLUMN_ALLIANCE_CAPTAIN, AbstractDbAdapter.COLUMN_ALLIANCE_PICK_1,
			AbstractDbAdapter.COLUMN_ALLIANCE_PICK_2, AbstractDbAdapter.COLUMN_ALLIANCE_PICK_3};
	
	public AllianceDbAdapter(Context ctx){
		super(ctx);
	}
	
	//should return the perfecto'd alliance
	public void createAlliance(Alliance dat){
		ContentValues values = new ContentValues();
		values.put(AbstractDbAdapter.COLUMN_ALLIANCE_NUMBER, dat.getNumber());
		values.put(AbstractDbAdapter.COLUMN_ALLIANCE_CAPTAIN, dat.getCaptain().getNumber());
		values.put(AbstractDbAdapter.COLUMN_ALLIANCE_PICK_1, dat.getFirstPick().getNumber());
		values.put(AbstractDbAdapter.COLUMN_ALLIANCE_PICK_2, dat.getSecondPick().getNumber());
		values.put(AbstractDbAdapter.COLUMN_ALLIANCE_PICK_3, dat.getThirdPick().getNumber());
		
		long insertId = database.insert(AbstractDbAdapter.TABLE_ALLIANCES, null, values);
		//find a way to return an alliance
	}
	
	public Alliance getAlliance(int seed){
		Cursor cursor = database.query(AbstractDbAdapter.TABLE_ALLIANCES, null, 
				AbstractDbAdapter.COLUMN_ALLIANCE_NUMBER + "=" + seed,
				null, null, null, null);
		cursor.moveToFirst();
		return cursorToAlliance(cursor);
	}
	
	public Alliance updateAlliance(Alliance alliance){
		ContentValues values = new ContentValues();
		values.put(AbstractDbAdapter.COLUMN_ALLIANCE_CAPTAIN, alliance.getCaptain().getNumber());
		values.put(AbstractDbAdapter.COLUMN_ALLIANCE_PICK_1, alliance.getFirstPick().getNumber());
		values.put(AbstractDbAdapter.COLUMN_ALLIANCE_PICK_2, alliance.getSecondPick().getNumber());
		values.put(AbstractDbAdapter.COLUMN_ALLIANCE_PICK_3, alliance.getThirdPick().getNumber());
		database.update(AbstractDbAdapter.TABLE_ALLIANCES, values, AbstractDbAdapter.COLUMN_ALLIANCE_NUMBER + "=" + alliance.getNumber(), null);
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
			Cursor cursor = database.query(AbstractDbAdapter.TABLE_ALLIANCES, null, 
					AbstractDbAdapter.COLUMN_ALLIANCE_NUMBER + "=" + i,
					null, null, null, null);
			cursor.moveToFirst();
			Alliance alliance = cursorToAlliance(cursor);
			megaconcat += alliance.getAllianceAsString() + "\n";
			Log.d("getAlliancesAsString", megaconcat);
		}
		
		return megaconcat;
	}
	
	//These two methods directly copied from TeamItemDbAdapter, not sure how to access them otherwise.
	//They've been moved up to the superclass - AbstractDbAdapter
	/*public TeamItem getTeamItem(int teamNumber){
		Cursor cursor = mDb.query(AbstractDbAdapter.TABLE_TEAMS, null, AbstractDbAdapter.COLUMN_NUMBER + "=" + String.valueOf(teamNumber),
				null, null, null, null);
		cursor.moveToFirst();
		return cursorToTeamItem(cursor);
	}
	
	private TeamItem cursorToTeamItem(Cursor cursor){
		TeamItem team = new TeamItem(0, "", "", "");
		if(cursor.getCount() != 0)
			team = new TeamItem(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
						cursor.getString(3));
		return team;
	}*/
	
}
