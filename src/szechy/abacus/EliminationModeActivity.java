package szechy.abacus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class EliminationModeActivity extends SherlockActivity {

	private TeamItemDataSource datasource;
	private TeamListDataSource listDatasource;
	private TeamList master;
	TeamItemArrayAdapter available;
	AllianceEliminationModeArrayAdapter allianceAdapter;
	//private TeamList taken;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	private ShareActionProvider mShareActionProvider;
	private Intent shareIntent = new Intent(Intent.ACTION_SEND);
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eliminations_mode);
		
		datasource = new TeamItemDataSource(this);
		datasource.open();
		listDatasource = new TeamListDataSource(this, datasource);
		listDatasource.open();
		
		/*
		 * Some special first-application time stuff		
		 */
		settings = getPreferences(MODE_PRIVATE);
		editor = settings.edit();
		
		Log.d("Don't rebuild SQL database?", String.valueOf(settings.getBoolean("dbExists", false)));
		
		if(!settings.getBoolean("dbExists", false)){
			editor.putInt("position", 0);
			editor.putInt("round", 0);
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
	    	for(int i = 1; i < 9; i++)
	    		datasource.createAlliance(new Alliance(i, true));

	    	listDatasource.addTeamList(new TeamList("iri", datasource.getAllTeamItems()));
	    	
	    	editor.putBoolean("dbExists", true);
	    	editor.commit();
		}
		
		shareIntent.putExtra(Intent.EXTRA_TEXT, "blank");
		
		/*
		 * For when this is not the primary activity
		 * String teamListName = getIntent().getStringExtra("teamListName");
		 * master = listDatasource.getTeamList(getIntent().getStringExtra("teamListName"));  
		 * setTitle(teamListName);
		 * */
		
    	master = listDatasource.getTeamList("iri");
		setTitle("2013 IRI");
		
    	available = new TeamItemArrayAdapter(getApplicationContext(), master.getTeams(), false, "iri");
    	final ListView teamsAvailable = (ListView)findViewById(R.id.teamsAvailable);
    	teamsAvailable.setAdapter(available);
    	Alliance[] alliancesToSend = {datasource.getAlliance(1), datasource.getAlliance(2), datasource.getAlliance(3),
    			datasource.getAlliance(4), datasource.getAlliance(5), datasource.getAlliance(6), datasource.getAlliance(7),
    			datasource.getAlliance(8)};
    	
    	allianceAdapter = new AllianceEliminationModeArrayAdapter(this, alliancesToSend);
    	final GridView alliances = (GridView)findViewById(R.id.alliances);
    	alliances.setAdapter(allianceAdapter);
    	
    	allianceAdapter.setRound(settings.getInt("round", 0));
    	allianceAdapter.setPosition(settings.getInt("position", 0));
    	
    	teamsAvailable.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView theNumber = (TextView)view.findViewById(R.id.teamNumberEliminations);
				TeamItem team = master.getTeam(Integer.parseInt((String)theNumber.getText()));
				if(allianceAdapter.addTeam(team))
				{
					alliances.deferNotifyDataSetChanged();
					alliances.invalidate();
					alliances.postInvalidate();
					available.remove(team);
					available.notifyDataSetChanged();
					editor.putInt("position", allianceAdapter.getPosition());
					editor.putInt("round", allianceAdapter.getRound());
					editor.commit();
					//updateShareIntent();
				}
			}
    	});
	}
	
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.eliminations_mode, menu);
		MenuItem item = menu.findItem(R.id.elims_share);
		/*mShareActionProvider = (ShareActionProvider)item.getActionProvider();
		shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, "-blank-");
		mShareActionProvider.setShareIntent(shareIntent);*/
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.elims_undo:
			TeamItem removed = allianceAdapter.removeTeam();
			if(removed.getNumber() != 0) {
				available.addTeam(removed);
				editor.putInt("position", allianceAdapter.getPosition());
				editor.putInt("round", allianceAdapter.getRound());
				editor.commit();
			}
			return true;
		case R.id.elims_share:
				//updateShareIntent();
				Alliance[] alliances = allianceAdapter.getAlliances();
				for(int i = 0; i < 8; i++){
					datasource.updateAlliance(alliances[i]);
				}
				String construct = new String();
				construct = "2013 IRI Alliances\n";
				construct += datasource.getAlliancesAsString();
				//Toast.makeText(this,  construct, Toast.LENGTH_SHORT).show();
				shareIntent.removeExtra(Intent.ACTION_SEND);
				shareIntent.putExtra(Intent.EXTRA_TEXT, construct);
				shareIntent.setType("text/plain");
				startActivity(shareIntent);
				return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	/*@Override
	public boolean onPrepareOptionsMenu(Menu menu){
		//updateShareIntent();
		return true;
	}*/
	
	private Intent updateShareIntent(){
		Alliance[] alliances = allianceAdapter.getAlliances();
		for(int i = 0; i < 8; i++){
			datasource.updateAlliance(alliances[i]);
		}
		String construct = new String();
		construct = "2013 IRI Alliances\n";
		construct += datasource.getAlliancesAsString();
		//Toast.makeText(this, construct, Toast.LENGTH_SHORT).show();
		shareIntent.removeExtra(Intent.EXTRA_TEXT);
		shareIntent.putExtra(Intent.EXTRA_TEXT, construct);
		mShareActionProvider.setShareIntent(shareIntent);
		return shareIntent;
	}
	
	protected void onStart(){
		super.onStart();
	}
	
	protected void onResume(){
		super.onResume();
		datasource.open();
	}
	
	protected void onPause(){
		super.onPause();
		Alliance[] alliances = allianceAdapter.getAlliances();
		for(int i = 0; i < 8; i++){
			datasource.updateAlliance(alliances[i]);
		}
		listDatasource.updateTeamList(available.getTeamList());
		//updateShareIntent();
		datasource.close();
	}
	
	protected void onStop(){
		super.onStop();
	}
	
	protected void onDestroy(){
		super.onDestroy();
	}
	
}
