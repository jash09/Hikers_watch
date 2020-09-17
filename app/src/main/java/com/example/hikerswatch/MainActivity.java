package com.example.hikerswatch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.security.Permission;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
              updatelocationInfo(location);
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
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {

            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
             Location lastknownlocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
             if(lastknownlocation!= null){
                 updatelocationInfo(lastknownlocation);

             }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

            startlistening();

        }
    }
    public void startlistening(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    public void updatelocationInfo (Location location){

        TextView latTextView = findViewById(R.id.latTextView);
        TextView lonTextView = findViewById(R.id.lonTextView);
        TextView accTextView = findViewById(R.id.accTextView);
        TextView altTextView = findViewById(R.id.altTextView);
        TextView addTextView = findViewById(R.id.addTextView);

        latTextView.setText("Latitude: " + Double.toString(location.getLatitude()));
        lonTextView.setText("Longitude: " + Double.toString(location.getLongitude()));
        accTextView.setText("Accuracy: " + Double.toString(location.getAccuracy()));
        altTextView.setText("Altitude: " + Double.toString(location.getAltitude()));
        String address=" Could not find address";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> listaddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(listaddresses!=null && listaddresses.size()>0){
                address ="Adress:\n";
                if(listaddresses.get(0).getThoroughfare() != null){

                    address +=listaddresses.get(0).getThoroughfare() +"\n ";

                }
                if(listaddresses.get(0).getLocality() != null){

                    address +=listaddresses.get(0).getLocality() +" \n ";

                }
                if(listaddresses.get(0).getPostalCode() != null){

                    address +=listaddresses.get(0).getPostalCode() +" \n";

                }
                if(listaddresses.get(0).getAdminArea() != null){

                    address +=listaddresses.get(0).getAdminArea() ;

                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        addTextView.setText(address);

    }
}
