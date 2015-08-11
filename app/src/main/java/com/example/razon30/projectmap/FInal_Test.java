package com.example.razon30.projectmap;

import android.location.Location;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    //for search
    //Tag used to cancel the request
    public static String tag_json_obj = "json_obj_req";
    AutoCompleteTextView autoCompleteTextView;
    public final String dropDownURL = "https://maps.googleapis.com/maps/api/place/autocomplete/json?key=AIzaSyAwJMcBAzDt2ij8YKUKLgtZJKIZVOJpbAA";
    public final String searchURL = "https://maps.googleapis.com/maps/api/geocode/json?address=";
    ArrayList<String> searchList = new ArrayList<String>();
    ImageView searchImage;
    private VolleySingleton volleySingleton;
    // private ImageLoader imageLoader;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final__test);
        settingNavigationAndToolBarTest();

        fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id
                .map_final));
        map = fragment.getMap();
        volleySingleton = VolleySingleton.getsInstance();
        requestQueue = volleySingleton.getmRequestQueue();

        searchImage = (ImageView) findViewById(R.id.searchButton);
        searchImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteTextView.getText().toString() != "") {
                    //spaces are replaced by + for url
                    char a[] = autoCompleteTextView.getText().toString().toCharArray();
                    int i = 0;
                    for (char c : a) {
                        if (c == ' ')
                            a[i] = '+';
                        i++;
                    }
                    searchRequest(new String(a));
                }
            }
        });

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                autoComplete(s.toString());
                ArrayAdapter adapter = new ArrayAdapter(getBaseContext(), android.R.layout.simple_list_item_1, searchList);
                autoCompleteTextView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

        });

        autoCompleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoCompleteTextView.getText().toString() != "") {
                    //spaces are replaced by + for url
                    char a[] = autoCompleteTextView.getText().toString().toCharArray();
                    int i = 0;
                    for (char c : a) {
                        if (c == ' ')
                            a[i] = '+';
                        i++;
                    }
                    searchRequest(new String(a));
                }
            }
        });

        //worksOnSearch();

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
//        mLocationRequest.setInterval(INTERVAL);
//        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
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

    public void autoComplete(String input) {
        String tempURL = dropDownURL;
        tempURL = tempURL + "&input=" + input;

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                tempURL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray prediction = response.getJSONArray("predictions");
                            for (int i = 0; i < prediction.length(); i++) {
                                JSONObject place = prediction.getJSONObject(i);
                                String name = place.getString("description");
                                searchList.add(new String(name));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjReq);

        // Adding request to request queue

    }

    public void searchRequest(String address) {
        String tempURL = searchURL + address;
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                tempURL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray result = response.getJSONArray("results");
                            JSONObject place = result.getJSONObject(0);
                            JSONObject geometry = place.getJSONObject("geometry");
                            JSONObject location = geometry.getJSONObject("location");
                            Double lat = location.getDouble("lat");
                            Double lng = location.getDouble("lng");
                            Location loc = new Location("");
                            loc.setLatitude(lat);
                            loc.setLongitude(lng);
                            setMarkerOnLocation(loc);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        requestQueue.add(jsonObjReq);

//        AppController appController = AppController.getInstance();
//
//        // Adding request to request queue
//        appController.addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    private  void setMarkerOnLocation(Location location){
        LatLng myCoordinates = new LatLng(location.getLatitude(), location.getLongitude());
        final CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(myCoordinates)      // Sets the center of the map to Mountain View
                .zoom(17)                   // Sets the zoom
                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   //
        map.addMarker(new MarkerOptions().position(myCoordinates).title("Marker"));
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
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
