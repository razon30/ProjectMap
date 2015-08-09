package com.example.razon30.projectmap;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import android.app.Dialog;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class SimpleDirection extends AppCompatActivity implements LocationListener , OnMapReadyCallback{

    GoogleMap mGoogleMap;
    ArrayList<LatLng> mMarkerPoints;
    double mLatitude = 0;
    double mLongitude = 0;


    double source_lat = 0;
    double source_long = 0;
    double dest_lat =0;
    double dest_long = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_direction);

        Intent intent = getIntent();
        String place = intent.getStringExtra("place");
        String[] split = place.split(",");
        dest_lat = Double.parseDouble(split[0]);
        dest_long = Double.parseDouble(split[1]);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Initializing
            mMarkerPoints = new ArrayList<LatLng>();

            // Getting reference to SupportMapFragment of the activity_main
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

            // Getting Map for the SupportMapFragment
            mGoogleMap = fm.getMap();



//            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(23.774473, 90.365422))
//                    .title("Marker"));
//            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(23.755407, 90.368951)).title("Marker"));
//            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(23.765407, 90.367951))
//                    .title("Marker"));
//            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(23.745407, 90.369951))
//                    .title("Marker"));

            // Enable MyLocation Button in the Map
            mGoogleMap.setMyLocationEnabled(true);


            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location From GPS
            Location location = locationManager.getLastKnownLocation(provider);


            LatLng startPoint = new LatLng(dest_lat, dest_long);
            LatLng dest;

            if (location != null) {
                onLocationChanged(location);
                source_lat = location.getLatitude();
                source_long = location.getLongitude();


                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(dest_lat,dest_long))
                        .title("Marker").icon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_RED)));



                dest = new LatLng(source_lat,source_long);

                // Getting URL to the Google Directions API
                String url = getDirectionsUrl(startPoint, dest);

                DownloadTask downloadTask = new DownloadTask();

                // Start downloading json data from Google Directions API
                downloadTask.execute(url);

            }

            locationManager.requestLocationUpdates(provider, 1000, 1, this);

//            // Setting onclick event listener for the map
//            mGoogleMap.setOnMapClickListener(new OnMapClickListener() {
//
//                @Override
//                public void onMapClick(LatLng point) {
//
//                    // Already map contain destination location
//                    if (mMarkerPoints.size() > 1) {
//
//                        FragmentManager fm = getSupportFragmentManager();
//                        mMarkerPoints.clear();
//                        mGoogleMap.clear();
//                        LatLng startPoint = new LatLng(mLatitude, mLongitude);
//
//                       // mMarkerPoints.add(1,startPoint);
//                        // draw the marker at the current position
//                        drawMarker(startPoint);
//                    }
//
//                    // draws the marker at the currently touched location
//                    drawMarker(point);
//
//                    // Checks, whether start and end locations are captured
//                    if (mMarkerPoints.size() >= 2) {
//                        LatLng origin = mMarkerPoints.get(0);
//                        LatLng dest = mMarkerPoints.get(1);
//
//                        // Getting URL to the Google Directions API
//                        String url = getDirectionsUrl(origin, dest);
//
//                        DownloadTask downloadTask = new DownloadTask();
//
//                        // Start downloading json data from Google Directions API
//                        downloadTask.execute(url);
//                    }
//                }
//            });
        }
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("exception url download", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(23.774473, 90.365422)).title("Marker"));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(23.755407, 90.368951)).title("Marker"));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(23.765407, 90.367951))
                .title("Marker"));
        mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(23.745407, 90.369951))
                .title("Marker"));
    }

    /**
     * A class to download data from Google Directions URL
     */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Directions in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(2);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            mGoogleMap.addPolyline(lineOptions);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_simple_direction, menu);
        return true;
    }

    private void drawMarker(LatLng point) {
        mMarkerPoints.add(point);

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();

        // Setting the position of the marker
        options.position(point);

        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
        if (mMarkerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (mMarkerPoints.size() == 2) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        // Add new marker to the Google Map Android API V2
        mGoogleMap.addMarker(options);
    }

    @Override
    public void onLocationChanged(Location location) {
        // Draw the marker, if destination location is not set
        if (mMarkerPoints.size() < 2) {

            mLatitude = location.getLatitude();
            mLongitude = location.getLongitude();
            LatLng point = new LatLng(mLatitude, mLongitude);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
            //  mGoogleMap.moveCamera(CameraUpdateFactory.);
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            drawMarker(point);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}