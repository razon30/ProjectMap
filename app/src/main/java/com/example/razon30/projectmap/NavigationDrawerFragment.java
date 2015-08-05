package com.example.razon30.projectmap;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class NavigationDrawerFragment extends Fragment {


    //recyclerview
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ActionBarDrawerToggle mactionbardrawertoggle;
    DrawerLayout mdrawer_layout;
    ArrayList<String> listPlaces = new ArrayList<String>();
    TextView tv;


    //remembering drawer was seen by user or not
    private static final String FIRST_TIME = "first_time";
    private boolean mUserSawDrawer = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String possibleEmail = "My Name";


        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {


                String fullName = account.name;
                possibleEmail = fullName.substring(0, fullName.lastIndexOf("@"));
                ;


            }
        }


        listPlaces.add("Airport");
        listPlaces.add("ATM");
        listPlaces.add("Bank");
        listPlaces.add("Bar");
        listPlaces.add("Book Store");
        listPlaces.add("Bus Station");
        listPlaces.add("Cafe");
        listPlaces.add("Car Rental");
        listPlaces.add("Car Repair");
        listPlaces.add("Car Wash");
        listPlaces.add("Dentist");
        listPlaces.add("Doctor");
        listPlaces.add("Department Store");
        listPlaces.add("Embassy");
        listPlaces.add("Food");
        listPlaces.add("Gas Station");
        listPlaces.add("Gym");
        listPlaces.add("Health");
        listPlaces.add("Hospital");
        listPlaces.add("Hotel");


        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawer_list);
        tv = (TextView) layout.findViewById(R.id.tvName);
        tv.setText(possibleEmail);
        adapter = new RecyclerAdapter(getActivity(), listPlaces, possibleEmail);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTOuchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onCLick(View v, int position) {
                Toast.makeText(getActivity(), "Touched on: " + position, Toast.LENGTH_LONG).show();
                //  mdrawer_layout.closeDrawer(GravityCompat.START);
//                ((MainActivity) getActivity()).onDrawerItemClicked(position - 1);
            }

            @Override
            public void onLongClick(View v, int position) {

                Toast.makeText(getActivity(), "Long Touched on: " + position, Toast.LENGTH_LONG).show();

            }
        }));
        return layout;
    }


//        recyclerView.addOnScrollListener(new HidingScrollListener() {
//            @Override
//            public void onHide() {
//                hideViews();
//            }
//
//            @Override
//            public void onShow() {
//                showViews();
//            }
//        });
//
//
//        return layout;
//    }
//
//
//    private void hideViews() {
//        tv.animate().translationY(-tv.getHeight()).setInterpolator(new
//                AccelerateInterpolator(2));
//
//
//    }
//
//    private void showViews() {
//        tv.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//
//    }


    public void setup(int drawer_fragment, DrawerLayout viewById, final Toolbar toolbar) {

        mdrawer_layout = viewById;
        mactionbardrawertoggle = new ActionBarDrawerToggle(getActivity(), mdrawer_layout, toolbar, R.string.open, R.string.close) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

//            @Override
//            public void onDrawerSlide(View drawerView, float slideOffset) {
//                super.onDrawerSlide(drawerView, slideOffset);
////                ((MainActivity) getActivity()).onDrawerSlide(slideOffset);
////                toolbar.setAlpha(1 - slideOffset / 2);
//            }

        };

        mdrawer_layout.setDrawerListener(mactionbardrawertoggle);

        mdrawer_layout.post(new Runnable() {
            @Override
            public void run() {

                mactionbardrawertoggle.syncState();

            }
        });


        //drawer 1st time or not
        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }

    }

    private void hideDrawer() {
        mdrawer_layout.closeDrawer(GravityCompat.START);
    }

    private void markDrawerSeen() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();

    }

    private void showDrawer() {

        mdrawer_layout.openDrawer(GravityCompat.START);

    }

    private boolean didUserSeeDrawer() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        return mUserSawDrawer;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public static interface ClickListener {

        public void onCLick(View v, int position);

        public void onLongClick(View v, int position);

    }

    static class RecyclerTOuchListener implements RecyclerView.OnItemTouchListener {

        GestureDetector gestureDetector;
        ClickListener clickListener;

        public RecyclerTOuchListener(Context context, final RecyclerView rv, final ClickListener clickListener) {

            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, rv.getChildPosition(child));
                    }

                }
            });


        }


        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {

                clickListener.onCLick(child, rv.getChildPosition(child));

            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


}