package com.example.geopic;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class HomeActivity extends Activity {

	static final int REQUEST_IMAGE_CAPTURE = 1; // The request code
	static final int GALLERY_REQUEST_CODE = 2;

	final static int CAMERA = 1;
	final static int GALLERY = 2;

	final static String url = "http://kanga-sc2g10.ecs.soton.ac.uk/django/generateData";

	String mCurrentPhotoPath;
	helperFunctions content = null;
	ArrayList<ImageEntry> images;
	File photoFile = null;

	public Context context = null;

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

		// Setting thread policy
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		if(Build.VERSION.SDK_INT >= 19){
			Button pickButton = (Button)findViewById(R.id.choosePicBtn);
			pickButton.setVisibility(Button.INVISIBLE);
		}

		content = new helperFunctions(url);
		content.getContent();

		images = content.formatContent();

		final ListView listview = (ListView) findViewById(R.id.listview);


		MyAdapter adapter = new MyAdapter(this, images);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position,
					long id) {
				ImageEntry one = (ImageEntry) listview.getItemAtPosition(position);
				fullScreen(view, one);
			}

		});


	}

	public void fullScreen(View view, ImageEntry image){
		Intent intent = new Intent(this, ImageActivity.class);
		intent.putExtra("Path", image.getPath());
		intent.putExtra("Title", image.getTitle());
		startActivity(intent);

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
			Toast.makeText(getApplicationContext(), "At Home Page", Toast.LENGTH_SHORT).show();
			return true;

		case R.id.action_logout:

			SharedPreferences sharedPref = this.getSharedPreferences("com.example.geopic", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString("Username", "");
			editor.putString("Password", "");
			editor.putString("SavedPoints", "");
			editor.commit();
			
			Toast.makeText(getApplicationContext(), "Logged Out", Toast.LENGTH_SHORT).show();
			
			Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	public void takePic(View view){

		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			try {
				photoFile = createImageFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (photoFile != null){
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
				System.out.println(Uri.fromFile(photoFile));
				startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);	
			}
		}
	}

	public void pickPic(View view){

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Intent intent = new Intent(this, Preview.class);
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		switch(requestCode) {

		// Deal with the result from the camera
		case REQUEST_IMAGE_CAPTURE:
			if(resultCode == RESULT_OK){

				intent.putExtra("imageUri", Uri.fromFile(photoFile));
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

				Uri selectedImageUri = data.getData();
				intent.putExtra("imageUri", selectedImageUri);
				intent.putExtra("type", GALLERY);
				startActivity(intent);

			} else {

				Toast toast = Toast.makeText(context, "No Picture chosen!", duration);
				toast.show();
			}
			break;
		}
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";

		File file = new File(Environment.getExternalStorageDirectory(), File.separator + "GeoPic/");

		if(!file.exists()){
			file.mkdir();
		}

		File image = File.createTempFile(
				imageFileName,  /* prefix */
				".jpg",         /* suffix */
				file      /* directory */
				);

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

}