package com.example.razon30.projectmap;

import android.annotation.TargetApi;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polyline;

/**
 * Created by razon30 on 08-08-15.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ServiceJOb extends JobService {

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

    int m;



    @Override
    public boolean onStartJob(JobParameters params) {

        Toast.makeText(this,"Job running "+m,Toast.LENGTH_SHORT).show();


        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
