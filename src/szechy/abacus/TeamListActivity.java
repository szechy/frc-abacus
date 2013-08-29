package szechy.abacus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class TeamListActivity extends SherlockActivity {

	private TeamItemDataSource datasource;
	private TeamListDataSource listDatasource;
	int teamSelected = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState){
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_team_list);
    	
    	setTitle("Michigan Team List");
    	
    	datasource = new TeamItemDataSource(this);
    	datasource.open();
    	
    	listDatasource = new TeamListDataSource(this, datasource);
    	listDatasource.open();
    	
		SharedPreferences settings = getPreferences(MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		
		Log.d("Rebuild SQL database?", String.valueOf(settings.getBoolean("dbExists", false)));
		
		if(!settings.getBoolean("dbExists", false)){
			datasource.upgrade(2);
	    	//parse all of the teams into TeamItems
	    	InputStream inputStream = this.getResources().openRawResource(R.raw.irilist);
	    	BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
	    	String strRead;
	    	try {
				while((strRead=reader.readLine())!=null){
					String[] splitArray = strRead.split("\t");
					datasource.createTeamItem(new TeamItem(Integer.parseInt(splitArray[1]), splitArray[7],
							splitArray[4], splitArray[2]));
					//Log.d("TeamListActivity", splitArray[4]);
				}
			} catch (NumberFormatException e) {
				Log.d("TeamListActivity", "Whoops, NumberFormatException on the team numbers");
			} catch (IOException e) {
				Log.d("TeamListActivity", "Whoops, IOException on the team handling");
			}
	    	editor.putBoolean("dbExists", true);
	    	editor.commit();
		}
		
    	final ListView listview = (ListView)findViewById(R.id.teamlist);
    	ArrayList<TeamItem> valuesPre = new ArrayList<TeamItem>();
    	
    	TeamItem[] values = {new TeamItem(33, "Killer Bees", "Auburn Hills", "Chrysler and Co.")};
    	//values = valuesPre.toArray(values);
    	valuesPre = datasource.getAllTeamItems();
    	values = (TeamItem[]) valuesPre.toArray();
    	TeamItemArrayAdapter adapter = new TeamItemArrayAdapter(getApplicationContext(), valuesPre, true, "main");
    	listview.setAdapter(adapter);
    	
    	listview.setOnItemClickListener(new OnItemClickListener() {
    		@Override
    		public void onItemClick(AdapterView<?> parent, final View view, int position,
    				long id) {
    			TextView theNumber = (TextView)view.findViewById(R.id.teamNumber);
    			Intent i = new Intent(getApplicationContext(), TeamItemActivity.class);
    			i.putExtra("teamID", datasource.getTeamItem(Integer.parseInt((String)theNumber.getText())).getTeamInfo());
    			startActivity(i);
    		}
    	});
    	
    	//setup SlidingMenu
    	SlidingMenu menu = new SlidingMenu(this);
    	menu.setMode(SlidingMenu.LEFT);
    	menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
    	menu.setMenu(R.layout.menu_frame);
    	menu.setBehindOffset(300);
    	menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
    	
    	final ListView menuListView = (ListView)findViewById(R.id.menuListView);
    	//Some default TeamList items
    	ArrayList<TeamItem> einstein = new ArrayList<TeamItem>();
    	einstein.add(datasource.getTeamItem(33));
    	einstein.add(datasource.getTeamItem(148));
    	einstein.add(datasource.getTeamItem(303));
    	einstein.add(datasource.getTeamItem(469));
    	einstein.add(datasource.getTeamItem(862));
    	einstein.add(datasource.getTeamItem(1241));
    	einstein.add(datasource.getTeamItem(1477));
    	einstein.add(datasource.getTeamItem(1640));
    	einstein.add(datasource.getTeamItem(3476));
    	ArrayList<TeamItem> ifi = new ArrayList<TeamItem>();
    	ifi.add(datasource.getTeamItem(111));
    	ifi.add(datasource.getTeamItem(148));
    	ifi.add(datasource.getTeamItem(1114));
    	ifi.add(datasource.getTeamItem(2337));
    	ArrayList<TeamItem> nasa = new ArrayList<TeamItem>();
    	nasa.add(datasource.getTeamItem(71));
    	nasa.add(datasource.getTeamItem(116));
    	nasa.add(datasource.getTeamItem(118));
    	nasa.add(datasource.getTeamItem(696));
    	nasa.add(datasource.getTeamItem(1538));
    	nasa.add(datasource.getTeamItem(1592));
    	nasa.add(datasource.getTeamItem(1902));
    	nasa.add(datasource.getTeamItem(2252));
    	nasa.add(datasource.getTeamItem(2337));
    	nasa.add(datasource.getTeamItem(3947));
    	
    	ArrayList<TeamList> menuList = new ArrayList<TeamList>();
    	menuList.addAll(listDatasource.getAllTeamLists());
    	menuList.add(new TeamList("FIRSTInMichigan", valuesPre));
    	menuList.add(new TeamList("Team IFI", ifi));
    	menuList.add(new TeamList("Team NASA", nasa));
    	menuList.add(new TeamList("Canadians", new ArrayList<TeamItem>()));
    	menuList.add(new TeamList("Don't Mess With Texas", new ArrayList<TeamItem>()));
    	menuList.add(new TeamList("Einstein Field", einstein));
    	
    	final SlidingMenuListArrayAdapter menuAdapter = new SlidingMenuListArrayAdapter(this, menuList.toArray(new TeamList[menuList.size()]));
    	menuListView.setAdapter(menuAdapter);
    	menuListView.setOnItemClickListener(new OnItemClickListener(){
    		@Override
    		public void onItemClick(AdapterView<?> parent, final View view, int position, long id){
    			final TeamItemArrayAdapter newAdapter = new TeamItemArrayAdapter(getApplicationContext(), 
    					menuAdapter.getTeamQueries()[position].getTeams(), true, "main");
    			listview.setAdapter(newAdapter);
    		}
    	});
    	
    }
    	
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.list_activity, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.menu_create_team_list:
	            Toast.makeText(this, "Nice job you clicked on 'new teamlist' :)", Toast.LENGTH_SHORT).show();
	            return true;
            case R.id.menu_go_to_eliminations_mode:
                Toast.makeText(this, "Go into eliminations mode!", Toast.LENGTH_SHORT).show();
                return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
	
    protected void onStart(){
    	super.onStart();
    	Log.d("in team list activity", "pastOnStart");
    }
    
    protected void onResume(){
    	datasource.open();
    	super.onResume();
    }

    protected void onPause(){
    	datasource.close();
    	super.onPause();
    }

    protected void onStop(){
    	super.onStop();
    }

    protected void onDestroy(){
    	super.onDestroy();
    }
}

