package com.example.geopic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class LocationActivity extends Activity {

	final static int duration = Toast.LENGTH_SHORT;
	LocationManager lm = null;
	LocationListener listener;
	double lat;
	double lon;
	RadioGroup group;
	GeoPoint current;
	SharedPreferences settings;
	ArrayList<GeoPoint> storedPoints;
	Geocoder geocoder;
	TextView locationText;
	Uri imageUri;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);

		Intent intent = getIntent();
		imageUri = intent.getParcelableExtra("imageUri");

		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);

		lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);		

		locationText = (TextView)findViewById(R.id.location);

		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		lat = location.getLatitude();
		lon = location.getLongitude();
		current = new GeoPoint(lat, lon);

		storedPoints = new ArrayList<GeoPoint>();

		// TODO maybe change name here
		settings = this.getSharedPreferences("com.example.geopic", Context.MODE_PRIVATE);


		// Generate Points
		String pointsString = settings.getString("SavedPoints", "");

		if(!pointsString.equals("")){
			String[] textPoints = pointsString.split(",");
			for (int i = 0; i < textPoints.length; i+=2) {
				storedPoints.add(new GeoPoint(Double.parseDouble(textPoints[i]), Double.parseDouble(textPoints[i+1])));
			}
		}

		int lenght = storedPoints.size();

		for (int i = 1; i <= lenght; i++) {
			String buttonId = "save" + i;
			int resID = getResources().getIdentifier(buttonId, "id", "com.example.geopic");
			RadioButton button = (RadioButton)findViewById(resID);
			button.setVisibility(button.VISIBLE);
		}

		geocoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
			String address = addresses.get(0).getAddressLine(0);
			locationText.setText(address);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		group = (RadioGroup)findViewById(R.id.group1);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				RadioButton button = (RadioButton)findViewById(checkedId);
				String text = button.getText().toString();
				if (text.equals("Current Location")) {
					lat = current.getLatitude();
					lon = current.getLongitude();
				}else {
					int number = Integer.parseInt(text.substring(text.length()-1));
					lat = storedPoints.get(number-1).getLatitude();
					lon = storedPoints.get(number-1).getLongitude();
				}

				try {
					List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
					String address = addresses.get(0).getAddressLine(0);
					locationText.setText(address);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return true;
	}

	public void sendPhoto(View view) throws ClientProtocolException, IOException{

		TextView titleText = (TextView)findViewById(R.id.imagTitle);
		
		if((titleText.getText().toString() + "").equals("")){
			Toast.makeText(getApplicationContext(), "Give the Image a Title", duration).show();

		} else{

			// add to arraylist in shared preferences
			int radioButtonID = group.getCheckedRadioButtonId();
			RadioButton radioButton = (RadioButton)group.findViewById(radioButtonID);

			if(radioButton.getText().toString().equals("Current Location")){
				GeoPoint point = new GeoPoint(lat, lon);
				storedPoints.add(point);
				if(storedPoints.size() == 4){
					storedPoints.remove(0);
				}

				String textPoint = "";
				for (GeoPoint curr : storedPoints) {
					textPoint += curr.getLatitude() + "," + curr.getLongitude() + ",";
				}
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("SavedPoints", textPoint);
				editor.commit();
			}


			if(sendToDjango()){
				Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);

				Context context = getApplicationContext();
				Toast toast = Toast.makeText(context, "Photo Uploaded!", duration);
				toast.show();

			}
		}
	}

	public void cancel(View view){
		Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);

		Context context = getApplicationContext();
		Toast toast = Toast.makeText(context, "Upload Canceled!", duration);
		toast.show();

	}

	@SuppressWarnings("deprecation")
	public boolean sendToDjango() throws ClientProtocolException, IOException{

		TextView titleText = (TextView)findViewById(R.id.imagTitle);
		String username = settings.getString("Username", "");

		HttpClient httpClient = new DefaultHttpClient();
		// Teest
		httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION,  HttpVersion.HTTP_1_1);

		HttpPost post = new HttpPost("http://kanga-sc2g10.ecs.soton.ac.uk/django/appAddImage");
		post.setHeader("enctype","multipart/form-data");

		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		System.out.println(imageUri.toString());
		System.out.println(imageUri.getPath());
		builder.addPart("imageForUpload", new FileBody(getRealPath(imageUri)));

		builder.addTextBody("username", username);
		builder.addTextBody("title", titleText.getText().toString());
		builder.addTextBody("lat", String.valueOf(lat));
		builder.addTextBody("lng", String.valueOf(lon));

		post.setEntity(builder.build());

		HttpResponse response = httpClient.execute(post);
		String contents = EntityUtils.toString(response.getEntity());

		if(contents.equals("Success")){

			return true;

		}else {
			Context context = getApplicationContext();
			Toast toast = Toast.makeText(context, "Couldn't upload that", Toast.LENGTH_LONG);
			toast.show();

			return false;

		}

	}


	public File getRealPath(Uri imageUri){

		if(Build.VERSION.SDK_INT < 19){
			String[] filePathColumn = {MediaStore.Images.Media.DATA};
			Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			String resolverImagePath = cursor.getString(columnIndex);

			File image = new File(resolverImagePath);
			return image;



		}else {
			String path = null;
			if(imageUri.toString().contains("content")){
				String[] projection = {MediaStore.MediaColumns.DATA};
				ContentResolver cr = getApplicationContext().getContentResolver();
				Cursor c = cr.query(imageUri, projection, null, null, null);
				if(c != null){
					try{
						if(c.moveToFirst()){
							path = c.getString(0);
						}
					} finally{
						c.close();
					}
				}

				System.out.println("Path is: " + path);
				return new File(path);
			}
			System.out.println("Didnt resolve");
			File image = new File(imageUri.getPath());
			return image;
		}
	}

}
