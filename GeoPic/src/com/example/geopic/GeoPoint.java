package com.example.geopic;

public class GeoPoint {
	
	private double latitude;
	private double longitude;
	
	public GeoPoint(double lat, double lon){
		latitude = lat;
		longitude = lon;
		
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
	
}
