package com.example.geopic;

import java.io.IOException;
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
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void registerHome(View view){
		// Do something with the credentials

		TextView firstNameText = (TextView) findViewById(R.id.firstName);
		TextView lasttNameText = (TextView) findViewById(R.id.LastName);
		TextView emailText = (TextView) findViewById(R.id.emailText);
		TextView passwordText = (TextView) findViewById(R.id.password);

		if((firstNameText.getText().toString() + "").equals("") | (lasttNameText.getText().toString() + "").equals("")  |
				(emailText.getText().toString() + "").equals("") | (passwordText.getText().toString() + "").equals("")){
			
			
			// TODO Make toast work
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "One or more Fields not filled out", Toast.LENGTH_LONG);
			toast.show();
		}else {
			
			
			String firstName = firstNameText.getText().toString();
			String lastName = lasttNameText.getText().toString();
			String email = emailText.getText().toString();
			String password = passwordText.getText().toString();

			// Creating HTTP client
			HttpClient httpClient = new DefaultHttpClient();
			// Creating HTTP Post
			HttpPost httpPost = new HttpPost("http://kanga-sc2g10.ecs.soton.ac.uk/django/register");
			// Creating post parameters
			List<NameValuePair> postList = new ArrayList<NameValuePair>(4);
			postList.add(new BasicNameValuePair("first_name", firstName));
			postList.add(new BasicNameValuePair("surname", lastName));
			postList.add(new BasicNameValuePair("email", email));
			postList.add(new BasicNameValuePair("password", password));
			
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(postList));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			HttpResponse response;
			try {
				response = httpClient.execute(httpPost);
				Log.d("Http Response", response.toString());
				System.out.println(response.toString());
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			this.finish();
		} 

	}

	public void registerLogin(View view){

		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		this.finish();


	}

}
