package com.example.gpsdemo;

import android.app.Activity;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	private MyLocationListenter locationListener;
	private TextView gpsLocationStatus, networkLocationStatus;
	
	public static final long minTimeInMS = 1*60*1000; // 1 mins
	public static final float minDistanceInMeter = 10.0f; //10 meters
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gpsLocationStatus = (TextView) this.findViewById(R.id.gpsLocationStats);
		networkLocationStatus = (TextView) this.findViewById(R.id.networkLocationStatus);
		locationListener = new MyLocationListenter(this);
		
		getLocationProvidersStatus();
	}

	private void getLocationProvidersStatus() {
		LocationManager lmanager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
		
		if (lmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
			networkLocationStatus.setText("Available");
		} else {
			networkLocationStatus.setText("Unvailable");
		}
		
		lmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER
				, minTimeInMS, minDistanceInMeter
				, locationListener);
		
		if (lmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
			gpsLocationStatus.setText("Available");
		} else {
			gpsLocationStatus.setText("Unvailable");
		}
		
		lmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER
				, minTimeInMS, minDistanceInMeter
				, locationListener);
		
	}
}
