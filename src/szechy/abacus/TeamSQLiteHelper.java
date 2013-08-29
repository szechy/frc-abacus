package szechy.abacus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class TeamSQLiteHelper extends SQLiteOpenHelper {
	
	public static final String TABLE_TEAMS = "teams";
	public static final String COLUMN_NUMBER = "_id";
	public static final String COLUMN_NAME = "teamname";
	public static final String COLUMN_HOMETOWN = "hometown";
	public static final String COLUMN_SPONSORS = "sponsors";
	
	public static final String TABLE_TEAM_LISTS = "team_list";
	public static final String COLUMN_LIST_KEY = "_id";
	public static final String COLUMN_LIST_NAME = "name";
	public static final String COLUMN_LIST_NUMBERS = "team_numbers";
	
	public static final String TABLE_ALLIANCES = "alliances";
	public static final String COLUMN_ALLIANCE_NUMBER = "alliance_name";
	public static final String COLUMN_ALLIANCE_CAPTAIN = "alliance_captain";
	public static final String COLUMN_ALLIANCE_PICK_1 = "alliance_pick_1";
	public static final String COLUMN_ALLIANCE_PICK_2 = "alliance_pick_2";
	public static final String COLUMN_ALLIANCE_PICK_3 = "alliance_pick_3";
	
	public static final String[] TABLE_TEAM_LIST_ALL_COLUMNS = {COLUMN_LIST_KEY, 
		COLUMN_LIST_NAME, COLUMN_LIST_NUMBERS};
	
	/*public static final String TABLE_EVENTS = "events";
	public static final String COLUMN_EVENT_NAME = "name";
	public static final String COLUMN_EVENT_LOCATION = "location";
	public static final String COLUMN_EVENT_DATE = "date";*/
	
	private static final String DATABASE_NAME = "teams.db";
	private static final int DATABASE_VERSION = 1;
	
	//Database creation SQL statement
	private static final String DATABASE_CREATE_1 = "create table "
			+ TABLE_TEAMS + " ( " + COLUMN_NUMBER + " integer, "
			+ COLUMN_NAME + " text not null, " + COLUMN_HOMETOWN + " text not null, "
			+ COLUMN_SPONSORS + " text not null);";
	private static final String DATABASE_CREATE_2 = "create table "
			+ TABLE_TEAM_LISTS + " ( " //+ COLUMN_LIST_KEY + " integer primary key autoincrement, "
			+ COLUMN_LIST_NAME + " text, " + COLUMN_LIST_NUMBERS + " text);";
	/*private static final String DATABASE_CREATE_3 = "create table "
			+ TABLE_EVENTS + "(" + COLUMN_EVENT_NAME + " text not null, "
			+ COLUMN_EVENT_LOCATION + " text not null, "
			+ COLUMN_EVENT_DATE + " text not null);";*/
	
	private static final String DATABASE_CREATE_3 = "create table "
			+ TABLE_ALLIANCES + " ( " + COLUMN_ALLIANCE_NUMBER + " integer, " 
			+ COLUMN_ALLIANCE_CAPTAIN + " integer, " + COLUMN_ALLIANCE_PICK_1
			+ " integer, " + COLUMN_ALLIANCE_PICK_2 + " integer, "
			+ COLUMN_ALLIANCE_PICK_3 + " integer);";
	
	public TeamSQLiteHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		//Log.d("create statement", DATABASE_CREATE_1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase database){
		database.execSQL(DATABASE_CREATE_1);
		database.execSQL(DATABASE_CREATE_2);
		database.execSQL(DATABASE_CREATE_3);
	}
	
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		Log.w(TeamSQLiteHelper.class.getName(), "Upgrading database from version" 
				+ oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAMS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEAM_LISTS);
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALLIANCES);
		onCreate(db);
	}

}
