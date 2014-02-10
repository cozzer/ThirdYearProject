package com.example.geopic;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.geopic.MESSAGE";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

	}

	/* Do login and replace the view */
	public void loginHome(View view){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		this.finish();

		// Do the login
		// If successful then show the next view 

	}

	public void loginRegister(View view){

		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
		this.finish();
	}


}
