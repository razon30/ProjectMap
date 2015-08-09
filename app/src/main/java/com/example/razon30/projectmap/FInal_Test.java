package com.example.razon30.projectmap;

import android.location.Location;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FInal_Test extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    //navigation and tootlbar
    NavigationDrawerFragment navigationDrawerFragment;
    private Toolbar toolbar;


    //for current position
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;

    private GoogleMap map;
    private SupportMapFragment fragment;


    ArrayList<LatLng> listLatLng = new ArrayList<LatLng>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__test);
        settingNavigationAndToolBarTest();

        fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id
                .map_final));
        map = fragment.getMap();


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
        updateUI();


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


    //for navigation and toolbar
    private void settingNavigationAndToolBarTest() {
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager()
                .findFragmentById(R.id.drawer_fragment_final);
        navigationDrawerFragment.setup(R.id.drawer_fragment_final, (DrawerLayout) findViewById(R.id
                .drawer_layout_final), toolbar);
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
        map.clear();
        updateUI();
    }

    private void updateUI() {
        Log.d(TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            double lat = mCurrentLocation.getLatitude();
            double lng = mCurrentLocation.getLongitude();


            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                    .title("Marker"));


            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();

         LatLngBounds latlngBounds = LatLngBounds.builder().include(new LatLng(lat,lng)).build();
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height,
                    100));

            map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()),
                    15));

//            if (listLatLng != null && listLatLng.size()>0){
//                listLatLng.clear();
//
//                showAround();
//
//                Toast.makeText(this, lat + " list has  ," + lng, Toast.LENGTH_LONG).show();
//
//            }else if (listLatLng==null || listLatLng.size()==0){
//                listLatLng.clear();
//
//               showAround();
//                Toast.makeText(this, lat + "  list empty ," + lng, Toast.LENGTH_LONG).show();
//            }


            Toast.makeText(this, lat + "   ," + lng, Toast.LENGTH_LONG).show();

        } else {
            Log.d(TAG, "location is null ...............");
        }
    }

    private void showAround() {
        LatLng place1 = new LatLng(23.774473, 90.365422);
        LatLng place2 = new LatLng(23.755407, 90.368951);
        LatLng place3 = new LatLng(23.765407, 90.367951);
        LatLng place4 = new LatLng(23.745407, 90.369951);

        listLatLng.add(place1);
        listLatLng.add(place2);
        listLatLng.add(place3);
        listLatLng.add(place4);

        for (int i=0;i<listLatLng.size();i++){

            LatLng place = listLatLng.get(i);
            double lat = place.latitude;
            double lng = place.longitude;
            map.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                    .title("Marker"));

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_final__test, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
