package com.example.geopic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	static final int REQUEST_IMAGE_CAPTURE = 1; // The request code
	static final int GALLERY_REQUEST_CODE = 2;

	final static int CAMERA = 1;
	final static int GALLERY = 2;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
			// Show the Up button in the action bar.
			setupActionBar();
		}

		// Get the message from the intent
		Intent intent = getIntent();
		
		

	}

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void takePic(View view){

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
		}
	}

	public void pickPic(View view){

		Intent choosePictureIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(choosePictureIntent, GALLERY_REQUEST_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Intent intent = new Intent(this, Preview.class);
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_LONG;

		switch(requestCode) {

			// Deal with the result from the camera
			case REQUEST_IMAGE_CAPTURE:
				if(resultCode == RESULT_OK){
					
					Bundle extras = data.getExtras();
					Bitmap imageBitmap = (Bitmap) extras.get("data");
					intent.putExtra("bitmap", imageBitmap);
					intent.putExtra("type", CAMERA);
					startActivity(intent);
					
				}else {

					Toast toast = Toast.makeText(context, "No Picture taken!", duration);
					toast.show();
				}
				break;

			// Deal with the result from the selected Gallery App	
			case GALLERY_REQUEST_CODE:
				if(resultCode == RESULT_OK){
					intent.putExtra("type", GALLERY);
					startActivity(intent);
					
				} else {
					
					Toast toast = Toast.makeText(context, "No Picture chosen!", duration);
					toast.show();
				}
				break;
		}
	}
}