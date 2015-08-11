package com.example.razon30.projectmap;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by razon30 on 10-08-15.
 */
public class VolleySingleton {

    private static VolleySingleton sInstance = null;
    private RequestQueue mRequestQueue;


    private VolleySingleton () {
        mRequestQueue = Volley.newRequestQueue(MyApplication.getAppContext());

    }

    public static  VolleySingleton getsInstance(){

        if (sInstance==null)
        {
            sInstance = new VolleySingleton();
        }
        return sInstance;

    }

    public RequestQueue getmRequestQueue(){

        return mRequestQueue;
    }

}
