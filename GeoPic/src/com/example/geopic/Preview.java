package com.example.geopic;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class Preview extends Activity {

	final static int CAMERA = 1;
	final static int GALLERY = 2;
	Uri imageUri = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview);
		Intent intent = getIntent();

		ImageView imagePreview = (ImageView) findViewById(R.id.previewImage);
		Button button1 = (Button) findViewById(R.id.redoBtn);

		int switchCase = intent.getIntExtra("type", 0);

		imageUri = intent.getParcelableExtra("imageUri");
		setGalleryImage(imageUri, imagePreview);


		switch(switchCase){

		case CAMERA:
			button1.setText("Retake Photo");
			button1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					reTakePhoto();
				}
			});

			break;


		case GALLERY:
			button1.setText("Select Other");
			button1.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					reChoosePhoto();
				}
			});
			break;

		default:
			System.out.println("Nothing happened");
			break;
		}
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//		getMenuInflater().inflate(R.menu.setting_menu, menu);
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
		intent.putExtra("imageUri", imageUri);
		startActivity(intent);
	}

	public void reTakePhoto(){


		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
			startActivityForResult(takePictureIntent, CAMERA);	

		}
	}

	public void reChoosePhoto(){

		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		ImageView imagePreview = (ImageView) findViewById(R.id.previewImage);

		switch(requestCode) {

		// Deal with the result from the camera
		case CAMERA:
			if(resultCode == RESULT_OK){

				setGalleryImage(imageUri, imagePreview);
				Toast toast = Toast.makeText(context, "Image Updated!", duration);
				toast.show();


			}else {

				Toast toast = Toast.makeText(context, "No Picture taken!", duration);
				toast.show();
			}
			break;

			// Deal with the result from the selected Gallery App	
		case GALLERY:
			if(resultCode == RESULT_OK){

				imageUri = data.getData();
				setGalleryImage(imageUri, imagePreview);
				Toast toast = Toast.makeText(context, "Image Updated!", duration);
				toast.show();


			} else {

				Toast toast = Toast.makeText(context, "No Picture chosen!", duration);
				toast.show();
			}
			break;
		}
	}

	private void setGalleryImage(Uri imageUri, ImageView imagePreview){


		if(Build.VERSION.SDK_INT < 19){

			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String resolverImagePath = cursor.getString(columnIndex);

			Bitmap selectedImage = BitmapFactory.decodeFile(resolverImagePath);
			imagePreview.setImageBitmap(selectedImage);



		}else {

			ParcelFileDescriptor parcelFileDescriptor;
			try {
				parcelFileDescriptor = getContentResolver().openFileDescriptor(imageUri, "r");
				FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
				Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
				parcelFileDescriptor.close();
				imagePreview.setImageBitmap(image);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
