package com.example.razon30.projectmap;

/**
 * Created by razon30 on 06-08-15.
 */
public class sample {




}


//MainActivity


//public class MainActivity extends ActionBarActivity {
//
//    ArrayList<Place> placeArrayList = new ArrayList<Place>();
//
//    //Tag used to cancel the request
//    String tag_json_obj = "json_obj_req";
//    String URL = "https://maps.googleapis.com/maps/api/place/search/json?key=AIzaSyAwJMcBAzDt2ij8YKUKLgtZJKIZVOJpbAA";
//    ProgressDialog pDialog;
//    String location = "&location=23.74460,90.37280";
//    String types = "&types=hospital";
//    String radius = "&radius=500";
//    String photoReference;
//    Double rating;
//    ListView listView;
//    ArrayAdapter<Place> adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        listView = (ListView) findViewById(R.id.listView);
//
//        adapter = new placeAdapter();
//
//        pDialog = new ProgressDialog(this);
//        pDialog.setMessage("Loading...");
//        pDialog.setCancelable(false);
//
//
//        makeJsonObjectRequest();
//    }
//
//    private void makeJsonObjectRequest() {
//        showDialog();
//        URL = URL + location;
//        URL = URL + types;
//        URL = URL + radius;
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
//                URL, null,
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray results = response.getJSONArray("results");
//                            for (int i = 0; i < results.length(); i++) {
//                                JSONObject place = results.getJSONObject(i);
//                                JSONObject geometry = place.getJSONObject("geometry");
//                                JSONObject location = geometry.getJSONObject("location");
//                                Double lat = location.getDouble("lat");
//                                Double lng = location.getDouble("lng");
//                                String icon = place.getString("icon");
//                                String name = place.getString("name");
//                                if (place.has("photos")) {
//                                    JSONArray photos = place.getJSONArray("photos");
//                                    for (int j = 0; j < photos.length(); j++) {
//                                        JSONObject photo = photos.getJSONObject(j);
//                                        photoReference = photo.getString("photo_reference");
//                                    }
//                                }
//                                else
//                                    photoReference="";
//                                if (place.has("rating")) {
//                                    rating = place.getDouble("rating");
//                                }
//                                else
//                                    rating=3.0;
//                                String vicinity = place.getString("vicinity");
//
//                                placeArrayList.add(new Place(new Location(lat, lng), icon, name, "photoReference", rating, vicinity));
//                            }
//
//                            listView.setAdapter(adapter);
//                        } catch (JSONException e) {
//                            Toast.makeText(getBaseContext(), "error", Toast.LENGTH_LONG).show();
//                            e.printStackTrace();
//                        }
//                        pDialog.hide();
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                pDialog.hide();
//            }
//        });
//
//        // Adding request to request queue
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }
//
//    private void showDialog() {
//        if (!pDialog.isShowing())
//            pDialog.show();
//    }
//
//    private void hideDialog() {
//        if (pDialog.isShowing())
//            pDialog.dismiss();
//    }
//
//    private class placeAdapter extends ArrayAdapter<Place> {
//        public placeAdapter() {
//
//            super(MainActivity.this, R.layout.listview_row, placeArrayList);
//        }
//
//        @Override
//        public View getView(int position, View view, ViewGroup parent) {
//            if (view == null)
//                view = getLayoutInflater().inflate(R.layout.listview_row, parent, false);
//            Place current = placeArrayList.get(position);
//
//            ImageView imageView = (ImageView) view.findViewById(R.id.icon);
//            Picasso.with(getContext()).load(current.getIcon()).into(imageView);
//
//            TextView placeName = (TextView) view.findViewById(R.id.placeName);
//            placeName.setText(current.getName());
//
//            RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
//            double temp=current.getRating();
//            ratingBar.setRating((float) temp);
//
//            return view;
//        }
//    }
//}



//place
//package com.example.troublesome.googleplacejsonparsing;
//
//public class Location {
//    Double lat;
//    Double lng;
//
//    Location(){
//
//    }
//
//    Location(Double lat, Double lng){
//        this.lat=lat;
//        this.lng=lng;
//    }
//
//    public void setLat(Double lat) {
//        this.lat = lat;
//    }
//
//    public void setLng(Double lng) {
//        this.lng = lng;
//    }
//
//    public Double getLat() {
//        return lat;
//    }
//
//    public Double getLng() {
//        return lng;
//    }
//}



//package com.example.troublesome.googleplacejsonparsing;
//
//public class Place {
//    Location location;
//    String icon;
//    String name;
//    String photoReference;
//    Double rating;
//    String vicinity;
//
//    Place(Location location, String icon, String name, String photoReference, Double rating, String vicinity){
//        this.location=location;
//        this.icon=icon;
//        this.name=name;
//        this.photoReference=photoReference;
//        this.rating=rating;
//        this.vicinity=vicinity;
//    }
//
//    public Location getLocation() {
//        return location;
//    }
//
//    public String getIcon() {
//        return icon;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getPhotoReference() {
//        return photoReference;
//    }
//
//    public Double getRating() {
//        return rating;
//    }
//
//    public String getVicinity() {
//        return vicinity;
//    }
//}






