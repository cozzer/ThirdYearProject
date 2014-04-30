package com.example.geopic;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image);
		setTitle("GeoPic");
		
		Intent i = getIntent();
		ImageView imageview = (ImageView) findViewById(R.id.fullImage);
		String path = i.getStringExtra("Path");
		String title = i.getStringExtra("Title");
		
		UrlImageViewHelper.setUrlDrawable(imageview, "http://kanga-sc2g10.ecs.soton.ac.uk" + path);
	    TextView titleText = (TextView) findViewById(R.id.titleText);;
	    titleText.setText(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
