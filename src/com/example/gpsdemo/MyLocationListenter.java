package com.example.gpsdemo;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.TextView;

public class MyLocationListenter implements LocationListener {

	private MainActivity activity;
	private Location lastLocation;
	
	TextView currentX;
	TextView currentY;
	TextView accuracy;
	TextView getTimestamp;
	TextView updatedTimestamp;

	public MyLocationListenter(MainActivity context) {
		this.activity = context;
		
		currentX = (TextView) activity.findViewById(R.id.currentLocationX);
		currentY = (TextView) activity.findViewById(R.id.currentLocationY);
		accuracy = (TextView) activity.findViewById(R.id.accuracy);
		getTimestamp = (TextView) activity.findViewById(R.id.txtGetTimeStamp);
		updatedTimestamp = (TextView) activity.findViewById(R.id.txtUpdateTimestamp);
	}

	@Override
	public void onLocationChanged(Location currentlocation) {
		if (lastLocation != null){
			if (isBetterLocation(currentlocation, lastLocation)){
				updateLocationDataToView(currentlocation);
			}
		} else {
			updateLocationDataToView(currentlocation);
		}
		lastLocation = currentlocation;
	}

	private void updateLocationDataToView(Location currentlocation) {
		currentX.setText("X: " + String.valueOf(currentlocation.getLatitude()));
		currentY.setText("Y: " + String.valueOf(currentlocation.getLongitude()));
		accuracy.setText("accuracy: " + String.valueOf(currentlocation.getAccuracy()) +" m");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onProviderDisabled(String provider) {

	}
	
	private boolean isBetterLocation(Location location, Location currentBestLocation) {
	    if (currentBestLocation == null) {
	        // A new location is always better than no location
	        return true;
	    }

	    // Check whether the new location fix is newer or older
	    long timeDelta = location.getTime() - currentBestLocation.getTime();
	    boolean isSignificantlyNewer = timeDelta > MainActivity.minTimeInMS;
	    boolean isSignificantlyOlder = timeDelta < -MainActivity.minTimeInMS;
	    boolean isNewer = timeDelta > 0;

	    // If it's been more than two minutes since the current location, use the new location
	    // because the user has likely moved
	    if (isSignificantlyNewer) {
	        return true;
	    // If the new location is more than two minutes older, it must be worse
	    } else if (isSignificantlyOlder) {
	        return false;
	    }

	    // Check whether the new location fix is more or less accurate
	    int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
	    boolean isLessAccurate = accuracyDelta > 0;
	    boolean isMoreAccurate = accuracyDelta < 0;
	    boolean isSignificantlyLessAccurate = accuracyDelta > 200;

	    // Check if the old and new location are from the same provider
	    boolean isFromSameProvider = isSameProvider(location.getProvider(),
	            currentBestLocation.getProvider());

	    // Determine location quality using a combination of timeliness and accuracy
	    if (isMoreAccurate) {
	        return true;
	    } else if (isNewer && !isLessAccurate) {
	        return true;
	    } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
	        return true;
	    }
	    return false;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
	    if (provider1 == null) {
	      return provider2 == null;
	    }
	    return provider1.equals(provider2);
	}

}
