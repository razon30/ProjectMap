package com.example.razon30.projectmap;

import android.annotation.TargetApi;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    NavigationDrawerFragment navigationDrawerFragment;
    private Toolbar toolbar;

    //for path direction
    private static final LatLng SHAMOLY = new LatLng(23.774473, 90.365422);
    private static final LatLng LALMATIA = new LatLng(23.755407, 90.368951);
    // private static final LatLng FRANKFURT = new LatLng(23.111772, 71.682632);
    private GoogleMap map;
    private SupportMapFragment fragment;
    private LatLngBounds latlngBounds;
    private Button bNavigation;
    private Polyline newPolyline;
    // private boolean isTravelingToParis = false;
    private int width, height;

    //for schedular
    private static final int JOB_ID = 100;
    private static final long POLL_FREQUENCY = 28800000;
    private JobScheduler mJobScheduler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        settingNavigationAndToolBar();


        getSreenDimanstions();
        fragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
        map = fragment.getMap();

       // ServiceJOb serviceJOb = new ServiceJOb(3);


        mJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        //set an initial delay with a Handler so that the data loading by the JobScheduler does not clash with the loading inside the Fragment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //schedule the job after the delay has been elapsed
              //  buildJob();
            }
        }, 1);

        findDirections(SHAMOLY.latitude, SHAMOLY.longitude, LALMATIA.latitude, LALMATIA.longitude,
                GMapV2Direction.MODE_DRIVING);

//        bNavigation = (Button) findViewById(R.id.bNavigation);
//        bNavigation.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                if (!isTravelingToParis) {
//                    isTravelingToParis = true;
//                    findDirections( AMSTERDAM.latitude, AMSTERDAM.longitude,PARIS.latitude, PARIS.longitude, GMapV2Direction.MODE_DRIVING );
//                }
//                else
//                {
//                    isTravelingToParis = false;
//                    findDirections( AMSTERDAM.latitude, AMSTERDAM.longitude, FRANKFURT.latitude, FRANKFURT.longitude, GMapV2Direction.MODE_DRIVING );
//                }
//            }
//        });


//        placeRecycler = (RecyclerView) findViewById(R.id.recycler);
//        placeRecycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//        adapter = new RecyclerAdapter(MainActivity.this,listPlaces);
//        placeRecycler.setAdapter(adapter);


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void buildJob() {

        //attach the job ID and the name of the Service that will work in the background
        JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, ServiceJOb
                .class));
        //set periodic polling that needs net connection and works across device reboots



        builder.setPeriodic(8000)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());


    }

    @Override
    protected void onResume() {

        super.onResume();
        latlngBounds = createLatLngBoundsObject(SHAMOLY, LALMATIA);
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));

    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {
        PolylineOptions rectLine = new PolylineOptions().width(5).color(Color.RED);

        for (int i = 0; i < directionPoints.size(); i++) {
            rectLine.add(directionPoints.get(i));
        }
        if (newPolyline != null) {
            newPolyline.remove();
        }
        newPolyline = map.addPolyline(rectLine);
        latlngBounds = createLatLngBoundsObject(SHAMOLY, LALMATIA);
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
//        if (isTravelingToParis)
//        {
//            latlngBounds = createLatLngBoundsObject(AMSTERDAM, PARIS);
//            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
//        }
//        else
//        {
//            latlngBounds = createLatLngBoundsObject(AMSTERDAM, FRANKFURT);
//            map.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBounds, width, height, 150));
//        }

    }

    private void getSreenDimanstions() {
        Display display = getWindowManager().getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }

    private LatLngBounds createLatLngBoundsObject(LatLng firstLocation, LatLng secondLocation) {
        if (firstLocation != null && secondLocation != null) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(firstLocation).include(secondLocation);

            return builder.build();
        }
        return null;
    }

    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String mode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
        asyncTask.execute(map);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    }

    private void settingNavigationAndToolBar() {

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        navigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.drawer_fragment);
        navigationDrawerFragment.setup(R.id.drawer_fragment, (DrawerLayout) findViewById(R.id
                .drawer_layout), toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
             Intent intent = new Intent(MainActivity.this,SimpleDirection.class);
             startActivity(intent);
        }

        if (id == R.id.final_test_activity) {
            Intent intent = new Intent(MainActivity.this,FInal_Test.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
