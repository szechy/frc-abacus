package szechy.abacus;

/*import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import android.content.ContentValues;*/

public class AllianceDataSource {
	/*private SQLiteDatabase database;
	private TeamSQLiteHelper dbHelper;
	private String[] allColumns = { TeamSQLiteHelper.COLUMN_ALLIANCE_NUMBER,
			TeamSQLiteHelper.COLUMN_ALLIANCE_CAPTAIN, TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_1,
			TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_2, TeamSQLiteHelper.COLUMN_ALLIANCE_PICK_3};
	
	public AllianceDataSource(Context context){
		dbHelper = new TeamSQLiteHelper(context);
	}
	
	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}
	
	public void close(){
		dbHelper.close();
	}
	
	public Alliance createAlliance(Alliance dat){
		ContentValues values = new ContentValues();
		values.put(TeamSQLiteHelper.COLUMN_ALLIANCE_NUMBER, dat.getNumber());
	}
	
	public Alliance getAlliance(int seed){
		Cursor cursor = database.query(TeamSQLiteHelper.TABLE_ALLIANCES,
			TeamSQLiteHelper.COLUMN_ALLIANCE_NUMBER + "=" + seed, null,
			null, null, null, null, null);
		cursor.moveToFirst();
		return cursorToAlliance(cursor);
	}
	
	private Alliance cursorToAlliance (Cursor cursor){
			TeamItem captain = TeamItemDataSource.getTeamItem(cursor.getInt(1));
			TeamItem pickOne = TeamItemDataSource.getTeamItem(cursor.getInt(2));
			TeamItem pickTwo = TeamItemDataSource.getTeamItem(cursor.getInt(3));
			TeamItem pickThree = new TeamItem(0, "", "", "");
			if(cursor.getInt(4) != 0)
				pickThree = TeamItemDataSource.getTeamItem(cursor.getInt(4));
			Alliance alliance = new Alliance(cursor.getInt(0), captain, pickOne, pickTwo, pickThree);
		return alliance;
	}
	
	public void upgrade(int upgrade){
		dbHelper.onUpgrade(database, 1, upgrade);
	}*/
}
