package stig.scout;

import com.actionbarsherlock.app.SherlockActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class TeamDetailActivity extends SherlockActivity{
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_team_detail);
		
		Bundle extras = getIntent().getExtras();
		String teamId = extras.getString("teamID");
		
		TextView number = (TextView) findViewById(R.id.number);
		TextView name = (TextView)findViewById(R.id.name);
		TextView hometown = (TextView)findViewById(R.id.hometown);
		TextView sponsors = (TextView)findViewById(R.id.sponsors);
		
		String[] splitArray = teamId.split("\t");
		number.setText(splitArray[0]);
		name.setText(splitArray[1]);
		hometown.setText(splitArray[2]);
		sponsors.setText(splitArray[3]);
		
		setTitle(splitArray[1]);
		
	}

	protected void onStart(){
		super.onStart();
		Log.d("TeamDetailActivity", "pastOnStart");
	}


	protected void onResume(){
		super.onResume();
	}

	protected void onPause(){
		super.onPause();
	}

	protected void onStop(){
		super.onStop();
	}

	protected void onDestroy(){
		super.onDestroy();
	}
}