package cl.genesys.appchofer.utilities;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;


public class GPS extends Service implements LocationListener{
    private final Context mContext;

    // flag for GPS status
    public boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    boolean canGetLocation = false;

    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    String provider; // GPS o NET



    public GPS(Context context) {
        this.mContext = context;
        getLocation();
    }


    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    //private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    // The minimum time between updates in milliseconds
    //private static final long MIN_TIME_BW_UPDATES = 1000; // 1 minute
    private static final long MIN_TIME_BW_UPDATES = 10000; // 10000 = 10 segundos,
    // Declaring a Location Manager
    protected LocationManager locationManager;


    public void onLocationChanged(Location currentLocation) {
        // TODO Auto-generated method stub
        try {
            this.location = currentLocation;
            getLatitude();
            getLongitude();

            /*latitude = location.getLatitude();
            longitude = location.getLongitude();
            provider = location.getProvider();*/
            Log.i("onLocationChanged","onLocationChanged " + latitude + " " + longitude);

        } catch (Exception e) {
            Log.i("onLocationChanged","Error: " + e.getMessage());
        }


    }


    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub
        //Log.i("onLocationChanged","onProviderDisabled");
    }


    public void onProviderEnabled(String arg0) {
        // TODO Auto-generated method stub
        //Log.i("onLocationChanged","onProviderEnabled");
    }


    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
        // TODO Auto-generated method stub
       // Log.i("onLocationChanged","onStatusChanged");
    }


    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        //Log.i("onLocationChanged","onBind");
        return null;
    }

    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }
       /* try{
            latitude = location.getLatitude();
        }catch (Exception e){
            latitude = 0;
        }*/


        return latitude;
    }

    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }
        /*try{
            longitude = location.getLongitude();
        }catch (Exception e){
            longitude = 0;
        }*/

        // return longitude
        return longitude;
    }


    public String getProvider() {
        if(location != null){
            provider = location.getProvider();
        }

        // return longitude
        return provider;
    }


    public Location getLocation() {
        try {
            //Log.i("onLocationChanged","getLocation");
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                latitude =  0;
                longitude = 0;
                provider = "NO GPS-NET";
            } else {
                this.canGetLocation = true;

                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                    //if (location == null) {

                        //Log.d("GPS Enabled", "GPS Enabled");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                provider = location.getProvider();

                            }
                        }
                    locationManager.requestLocationUpdates(
                            LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                   // }
                }

               if (latitude == 0)
                {
                    if (isNetworkEnabled) {

                        //Log.d("Network", "Network");
                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                provider = location.getProvider();
                            }
                        }

                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    }

                }

                //  First get location from Network Provider

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }




    /**
     * Function to check if best network provider
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * */
    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");

        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.delete);

        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     * */

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GPS.this);
        }
    }


}
/*
    // distancia en metros entre 2 localizaciones...
    Location ubicacion1 = new Location("");
    ubicacion1.setLatitude(lat1);
    ubicacion1.setLongitude(lon1);

    Location ubicacion2 = new Location("");
    ubicacion2.setLatitude(lat2);
    ubicacion2.setLongitude(lon2);

    float distanciaEnMetros = ubicacion1.distanceTo(ubicacion2);
*/





