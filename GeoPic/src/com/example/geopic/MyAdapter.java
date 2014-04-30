package com.example.geopic;

import java.util.ArrayList;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<ImageEntry> {
	  private final Context context;
	  private final ArrayList<ImageEntry> values;

	  public MyAdapter(Context context, ArrayList<ImageEntry> values) {
	    super(context, R.layout.list_layout, values);
	    this.context = context;
	    this.values = values;
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.list_layout, parent, false);
	    TextView titleText = (TextView) rowView.findViewById(R.id.title);
	    TextView authorText = (TextView) rowView.findViewById(R.id.author);
	    ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
	    titleText.setText(values.get(position).getTitle());
	    authorText.setText(values.get(position).getAuthor());
	    // change the icon for Windows and iPhone
	    UrlImageViewHelper.setUrlDrawable(imageView, "http://kanga-sc2g10.ecs.soton.ac.uk" + values.get(position).getPath());
	    imageView.getLayoutParams().width = 200;


	    return rowView;
	  }
	}
