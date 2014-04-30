package com.example.geopic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		
		// TODO Application name may need changing
		SharedPreferences sharedPref = this.getSharedPreferences("com.example.geopic", Context.MODE_PRIVATE);

		String username = sharedPref.getString("Username", "");
		String password = sharedPref.getString("Password", "");
		
		if(!username.equals("") && !password.equals("")){
			
			TextView userText = (TextView) findViewById(R.id.userText);
			TextView passwordText = (TextView) findViewById(R.id.passText);
			
			userText.setText(username);
			passwordText.setText(password);
			
		}

	}

	/* Do login and replace the view */
	public void loginHome(View view){

		TextView userText = (TextView) findViewById(R.id.userText);
		TextView password = (TextView) findViewById(R.id.passText);

		if((userText.getText().toString() + "").equals("") | (password.getText().toString() + "").equals("")){


			// TODO Make toast work
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "One or more not filled out", Toast.LENGTH_LONG);
			toast.show();
		}else {


			String userString = userText.getText().toString();
			String passString = password.getText().toString();

			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpPost httpPost = new HttpPost("http://kanga-sc2g10.ecs.soton.ac.uk/django/appLogin");
			// Creating post parameters
			List<NameValuePair> postList = new ArrayList<NameValuePair>(2);
			postList.add(new BasicNameValuePair("email", userString));
			postList.add(new BasicNameValuePair("password", passString));


			try {
				httpPost.setEntity(new UrlEncodedFormEntity(postList));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			HttpResponse response = null;
			String contents = null;
			try {
				response = httpClient.execute(httpPost);
				Log.d("Http Response", response.toString());
				BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				contents = reader.readLine();
				reader.close();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if(contents.equals("success")){
				// TODO App Name may need changing
				SharedPreferences sharedPref = this.getSharedPreferences("com.example.geopic", Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.putString("Username", userString);
				editor.putString("Password", passString);
				editor.commit();
				
				
				Intent intent = new Intent(this, HomeActivity.class);
				startActivity(intent);
				this.finish();

			}else {
				Context context = getApplicationContext();
				Toast toast = Toast.makeText(context, "Couldn't Log you In", Toast.LENGTH_LONG);
				toast.show();

			}
		}
	}

	public void loginRegister(View view){

		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
		this.finish();
	}


}
