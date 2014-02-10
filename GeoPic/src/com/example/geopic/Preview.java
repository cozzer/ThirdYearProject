package com.example.geopic;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Preview extends Activity {
	
	final static int CAMERA = 1;
	final static int GALLERY = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		System.out.println("TRYING TO CREATE");
		Intent intent = getIntent();
		
		ImageView imagePreview = (ImageView) findViewById(R.id.imageThumbNail);
		Button button1 = (Button) findViewById(R.id.redoBtn);
		
		int switchCase = intent.getIntExtra("type", 0);
		switch(switchCase){
		
			case CAMERA:
				System.out.println("CAMERA");
				Bitmap imageBitmap = intent.getParcelableExtra("bitmap");
				imagePreview.setImageBitmap(imageBitmap);
			
				button1.setText("Retake Photo");
				break;
				

			case GALLERY:
				System.out.println("GALLERY");
				Uri imageUri = intent.getParcelableExtra("imageUri");
				imagePreview.setImageURI(imageUri);
				button1.setText("Select Other");
				break;
				
			default:
				System.out.println("Nothing happened");
				break;
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preview, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void chooseLocation(View view){
		
		Intent intent = new Intent(this, LocationActivity.class);
		startActivity(intent);
	}

}
