package com.example.razon30.projectmap;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class Collecting_Data_And_Show_Marker extends AppCompatActivity implements
        com.google.android.gms.location.LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private Toolbar toolbar;

    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 60 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 60;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    private LatLngBounds latlngBounds;

    GoogleMap mGoogleMap;
    ArrayList<LatLng> mMarkerPoints = getPlaces();
    double mLatitude = 0;
    double mLongitude = 0;

    private SupportMapFragment fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collecting__data__and__show__marker);

        toolbar = (Toolbar) findViewById(R.id.app_bar_collectin);
        setSupportActionBar(toolbar);


        // Initializing
        mMarkerPoints = new ArrayList<LatLng>();

        // Getting reference to SupportMapFragment of the activity_main
        fm = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_collecting);

        // Getting Map for the SupportMapFragment
        mGoogleMap = fm.getMap();


//        // Getting Google Play availability status
//        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
//
//        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available
//
//            int requestCode = 10;
//            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
//            dialog.show();
//
//        } else { // Google Play Services are available


        //show error dialog if GoolglePlayServices not available
        if (!isGooglePlayServicesAvailable()) {
            finish();
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mMarkerPoints = getPlaces();


        updateUI(mMarkerPoints);


//            // Getting LocationManager object from System Service LOCATION_SERVICE
//            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//
//            // Creating a criteria object to retrieve provider
//            Criteria criteria = new Criteria();
//
//            // Getting the name of the best provider
//            String provider = locationManager.getBestProvider(criteria, true);
//
//            // Getting Current Location From GPS
//            Location location = locationManager.getLastKnownLocation(provider);
//
//            if (location != null) {
//                onLocationChanged(location);
//                mLatitude = location.getLatitude();
//                mLongitude = location.getLongitude();
//                LatLng point = new LatLng(mLatitude,mLongitude);
//                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLatitude,mLongitude))
//                        .title("Marker").icon(BitmapDescriptorFactory.defaultMarker
//                                (BitmapDescriptorFactory.HUE_GREEN)));
//            }
//
//            locationManager.requestLocationUpdates(provider, 1000, 1, this);
//

        //  drawMarker(location);

    }

    private ArrayList<LatLng> getPlaces() {

        ArrayList<LatLng> list = new ArrayList<LatLng>();

        LatLng place1 = new LatLng(23.774473, 90.365422);
        LatLng place2 = new LatLng(23.755407, 90.368951);
        LatLng place3 = new LatLng(23.765407, 90.367951);
        LatLng place4 = new LatLng(23.745407, 90.369951);

        list.add(place1);
        list.add(place2);
        list.add(place3);
        list.add(place4);

        return list;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mGoogleMap.clear();
        mMarkerPoints.clear();
        mMarkerPoints = getPlaces();
        updateUI(mMarkerPoints);
    }

    private void updateUI(ArrayList<LatLng> mMarkerPoints) {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            double lat = mCurrentLocation.getLatitude();
            double lng = mCurrentLocation.getLongitude();


//            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
//                    15));

//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()))      //
//                    // Sets
//                    // the center of the map to location user
//                    .zoom(17)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
//                    .build();                   // Creates a CameraPosition from the builder
//            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            latlngBounds = createLatLngBoundsObject(mMarkerPoints);
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height,
                    300));


            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                    .title("Marker").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

//            Circle circle = mGoogleMap.addCircle(new CircleOptions().center(new LatLng(lat,
//                    lng)).radius(2000).strokeColor(Color.RED));
//            circle.setVisible(true);
//            getZoomLevel(circle);


            for (int i = 0; i < mMarkerPoints.size(); i++) {

                LatLng place = mMarkerPoints.get(i);
                double lat1 = place.latitude;
                double lng1 = place.longitude;
                mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(lat1, lng1))
                        .title("Marker").icon(BitmapDescriptorFactory.defaultMarker
                                (BitmapDescriptorFactory.HUE_RED)));

            }


            Toast.makeText(this, lat + "   ," + lng, Toast.LENGTH_LONG).show();

        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    private LatLngBounds createLatLngBoundsObject(ArrayList<LatLng> mMarkerPoints) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < mMarkerPoints.size(); i++) {
            builder.include(mMarkerPoints.get(i));

        }
        return builder.build();

    }

//    private int getZoomLevel(Circle circle) {
//
//        int zoomLevel = 0;
//        if (circle != null) {
//            double radius = circle.getRadius();
//            double scale = radius / 500;
//            zoomLevel = (int) (16 - Math.log(scale) / Math.log(2));
//        }
//        return zoomLevel;
//
//
//    }


    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }


//    private void drawMarker(LatLng point) {
//        mMarkerPoints.add(point);
//
//        // Creating MarkerOptions
//        MarkerOptions options = new MarkerOptions();
//
//        // Setting the position of the marker
//        options.position(point);
//
//        /**
//         * For the start location, the color of marker is GREEN and
//         * for the end location, the color of marker is RED.
//         */
//        if (mMarkerPoints.size() == 1) {
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//        } else if (mMarkerPoints.size()>1) {
//            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        }
//
//        // Add new marker to the Google Map Android API V2
//        mGoogleMap.addMarker(options);
//    }

//    @Override
//    public void onLocationChanged(Location location) {
//        // Draw the marker, if destination location is not set
//        if (mMarkerPoints.size() < 2) {
//
//            mLatitude = location.getLatitude();
//            mLongitude = location.getLongitude();
//            LatLng point = new LatLng(mLatitude, mLongitude);
//
//            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
//            //  mGoogleMap.moveCamera(CameraUpdateFactory.);
//            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//
//            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(mLatitude, mLongitude))
//                    .title("Marker").icon(BitmapDescriptorFactory.defaultMarker
//                            (BitmapDescriptorFactory.HUE_GREEN)));
//        }
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_collecting__data__and__show__marker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {


            ArrayList<String> list = new ArrayList<String>();
            list.add("Place1");
            list.add("Place2");
            list.add("Place3");
            list.add("Place4");

            View view1 = getLayoutInflater().inflate(R.layout.place_list, null);
            ListView listView = (ListView) view1.findViewById(R.id.listplace);

            ArrayAdapter adapter = new ArrayAdapter(Collecting_Data_And_Show_Marker.this, android
                    .R.layout.simple_list_item_1, list);

            listView.setAdapter(adapter);

            AlertDialog.Builder builderAlertDialog = new AlertDialog.Builder(
                    Collecting_Data_And_Show_Marker.this);

            builderAlertDialog
                    .setView(view1)
                    .show();

            ArrayList<LatLng> listLat = new ArrayList<LatLng>();
            listLat = getPlaces();

            final ArrayList<LatLng> finalListLat = listLat;
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    LatLng place1 = finalListLat.get(position);

                    String lat2 = String.valueOf(place1.latitude);
                    String long2 = String.valueOf(place1.longitude);

                    String loc = lat2+","+long2;

                    Intent intent = new Intent(Collecting_Data_And_Show_Marker.this,
                            SimpleDirection.class);

                    intent.putExtra("place",loc);
                    startActivity(intent);


                }
            });



        }

        return super.onOptionsItemSelected(item);
    }
}
