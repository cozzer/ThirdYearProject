package com.example.geopic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.setting_menu, menu);
		return true;
	}
	
	public void registerHome(View view){
		// Do something with the credentials
		
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		this.finish();
		
	}
	
	public void registerLogin(View view){
		
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		this.finish();
		
		
	}

}
