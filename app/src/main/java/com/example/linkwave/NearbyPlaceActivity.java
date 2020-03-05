package com.example.linkwave;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class NearbyPlaceActivity extends AppCompatActivity {
/*
    {
        "error_message" : "You have exceeded your daily request quota for this API. If you did not set a custom daily request quota, verify your project has an active billing account: http://g.co/dev/maps-no-account",
            "html_attributions" : [],
        "results" : [],
        "status" : "OVER_QUERY_LIMIT"
    }
        */


    String PLACE_TYPE = "";
    ProgressDialog progress;
    TextView NearByPlaceTextView;
    TextView mTitle;
    LocationRequest mLocationRequest;
    FusedLocationProviderClient mFusedLocationProviderClient;
    LocationCallback mLocationCallback;
    RequestQueue myRequestQueueGetPlace;
    GetNearestLocationValue GetNearPlaceJsonValue;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    public static String UserLatitude = "";
    public static String UserLongitude = "";
    public static String UserDataRaduis = "300";
    public static List<PlaceInformation> AllPlaceInformation;

    SharedPreferences sharedPrefs ;
    SharedPreferences.Editor sharedPrefseditor;

    public boolean HAVE_LOCATION = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nearby_place);

        HAVE_LOCATION = false;
        AllPlaceInformation = new ArrayList<>();
        myRequestQueueGetPlace = Volley.newRequestQueue(this);

        mFusedLocationProviderClient = new FusedLocationProviderClient(this);
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(10);
        mLocationRequest.setFastestInterval(5);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        NearByPlaceTextView = (TextView) findViewById(R.id.NearbyPlaceTextBox);

        mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                Log.i("My_Project_NearBy","onLocationResult Is called : "+locationResult.toString());
                Log.i("My_Project","onLocationResult Is called getAccuracy : "+locationResult.getLastLocation().getAccuracy()+"");
                Log.i("My_Project","onLocationResult Is called getProvider : "+locationResult.getLastLocation().getProvider()+"");
                UserLatitude = locationResult.getLastLocation().getLatitude()+"";
                UserLongitude = locationResult.getLastLocation().getLongitude()+"";
                mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);

                sharedPrefs = getSharedPreferences("LOCATION_INFO", MODE_PRIVATE);
                if (sharedPrefs != null && sharedPrefs.contains("lat")
                        && sharedPrefs.contains("lon")
                        && !sharedPrefs.getString("lat","" ).isEmpty()
                        && !sharedPrefs.getString("lon","" ).isEmpty()
                ){

                    String lat = sharedPrefs.getString("lat","" );
                    String lon = sharedPrefs.getString("lon","" );
                    double distent = distance_new(
                        Double.parseDouble(NearbyPlaceActivity.UserLatitude),
                        Double.parseDouble(NearbyPlaceActivity.UserLongitude),
                        Double.parseDouble(lat),
                        Double.parseDouble(lon),
                        'K'
                    );
                    Log.i("My_Project_Hav", ""+distent);
                    if (distent <= 0.1000){
                        HAVE_LOCATION = true;
                        Log.i("My_Project_Hav","true ");

                    }
                }else{
                    sharedPrefseditor = sharedPrefs.edit();
                    sharedPrefseditor.putString("lat", UserLatitude);
                    sharedPrefseditor.putString("lon", UserLongitude);
                    sharedPrefseditor.putString("hospital", "");
                    sharedPrefseditor.putString("police", "");
                    sharedPrefseditor.putString("restaurant", "");
                    sharedPrefseditor.commit();
                    Log.i("My_Project_setShr","true");
                }

                try {
                    Intent intentGetPlace = getIntent();
                    PLACE_TYPE = intentGetPlace.getExtras().getString("NearestPlace_Type");
                    if(PLACE_TYPE.equals("RESTAURANTS")){
                        NearbyPlaceActivity.UserDataRaduis = "500";
                        GetNearPlaceJsonValue = new GetNearestLocationValue();
                        URL url = new URL(getRestudenturl( NearbyPlaceActivity.UserLatitude, NearbyPlaceActivity.UserLongitude, NearbyPlaceActivity.UserDataRaduis));
                        if (HAVE_LOCATION){
                            String restaurant = sharedPrefs.getString("restaurant","" );
                            if (!restaurant.isEmpty()){
                                listViewLayoutOffline(restaurant);
                            }else {
                                GetNearPlaceJsonValue.execute(url);
                            }
                        }
                        else {
                            GetNearPlaceJsonValue.execute(url);
                        }

                    }
                    else if(PLACE_TYPE.equals("HOSPITALS")){
                        NearbyPlaceActivity.UserDataRaduis = "500";
                        GetNearPlaceJsonValue = new GetNearestLocationValue();
                        URL url = new URL(getHospitalurl( NearbyPlaceActivity.UserLatitude, NearbyPlaceActivity.UserLongitude, NearbyPlaceActivity.UserDataRaduis));
                        if (HAVE_LOCATION){
                            String restaurant = sharedPrefs.getString("hospital","" );
                            if (!restaurant.isEmpty()){
                                listViewLayoutOffline(restaurant);
                            }else {
                                GetNearPlaceJsonValue.execute(url);
                            }
                        }
                        else {
                            GetNearPlaceJsonValue.execute(url);
                        }
                    }
                    else if(PLACE_TYPE.equals("POLICES")){
                        NearbyPlaceActivity.UserDataRaduis = "500";
                        GetNearPlaceJsonValue = new GetNearestLocationValue();
                        URL url = new URL(getPolicesurl( NearbyPlaceActivity.UserLatitude, NearbyPlaceActivity.UserLongitude, NearbyPlaceActivity.UserDataRaduis));
                        if (HAVE_LOCATION){
                            String restaurant = sharedPrefs.getString("police","" );
                            if (!restaurant.isEmpty()){
                                listViewLayoutOffline(restaurant);
                            }else {
                                GetNearPlaceJsonValue.execute(url);
                            }
                        }
                        else {
                            GetNearPlaceJsonValue.execute(url);
                        }
                    }



                }catch (Exception e){
                    Log.i("My_Project",e.getMessage());
                }

                super.onLocationResult(locationResult);
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                Log.i("My_Project","onLocationAvailability is called : "+locationAvailability.toString());
                if (!locationAvailability.isLocationAvailable()){
                    mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
                    Intent it = new Intent(NearbyPlaceActivity.this,ErrorActivity.class);
                    it.putExtra("Error_Title", "Location Not Available");
                    it.putExtra("Error_Des", "Sorry , Location is not turned on properly . Please open your gps in high Accuracy .");
                    startActivity(it);
                    finish();
                }

                super.onLocationAvailability(locationAvailability);
            }

        };
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        mFusedLocationProviderClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback,
                null
        );

        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        Intent intent = getIntent();
        PLACE_TYPE = intent.getExtras().getString("NearestPlace_Type");
        NearestPlaceType();
        Toolbar myToolbar = (Toolbar) findViewById(R.id.Main_toobar);
        mTitle = (TextView) myToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(""+PLACE_TYPE);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

      //  setSupportActionBar(myToolbar);
     //   getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     //   getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public void onBackPressed() {
        if (mFusedLocationProviderClient != null){
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
//        Intent it = new Intent(NearbyPlaceActivity.this,FirstPageActivity.class);
//        startActivity(it);
        finish();
        GetNearPlaceJsonValue.cancel(true);
        HAVE_LOCATION = false;
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {

        Intent it = new Intent(NearbyPlaceActivity.this,FirstPageActivity.class);
        startActivity(it);
        finish();
        GetNearPlaceJsonValue.cancel(true);

        return super.onSupportNavigateUp();
    }

    void listViewLayoutOffline(String s){
        try {
            Log.i("My_Project_Offline",s);

            JSONObject root = new JSONObject(s);

            if (root.getString("status").equals("OK")) {
                progress.dismiss();


                List<PlaceInformation> listPlace = new ArrayList<>();
                JSONArray result = root.getJSONArray("results");

                for (int i = 0;i<result.length();i++){
                    PlaceInformation place = new PlaceInformation();
                    JSONObject getResultValue = result.getJSONObject(i);
                    JSONObject geometry = getResultValue.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    //JSONArray photos = getResultValue.getJSONArray("photos");
                    //JSONObject photos_0 = photos.getJSONObject(0);



                    place.setLat(location.getString("lat"));
                    place.setLng(location.getString("lng"));
                    place.setId(getResultValue.getString("id"));
                    place.setPlaceid(getResultValue.getString("place_id"));
                    place.setName(getResultValue.getString("name"));
                    place.setVicinity(getResultValue.getString("vicinity"));

                    place.setRating("0.0");
                    //if(photos_0.has("photo_reference")) {
                    //     place.setImage_reference(photos_0.getString("photo_reference"));
                    // }
                    place.setImage_reference("");
                    place.setReference(getResultValue.getString("reference"));

                       /* double distent = distance(
                                Double.parseDouble(NearbyPlaceActivity.UserLatitude),
                                Double.parseDouble(place.getLat()),
                                Double.parseDouble(NearbyPlaceActivity.UserLongitude),
                                Double.parseDouble(place.getLng()),
                                0.0,
                                1.0
                        );
                        */
                    double distent = distance_new(
                            Double.parseDouble(NearbyPlaceActivity.UserLatitude),
                            Double.parseDouble(NearbyPlaceActivity.UserLongitude),
                            Double.parseDouble(place.getLat()),
                            Double.parseDouble(place.getLng()),
                            'K'
                    );
                    place.setCalculateDistence(new DecimalFormat("##.####").format(distent)+"");
                    Log.i("My_Project",place.toString());

                    listPlace.add(place);

                }


                recyclerView = (RecyclerView) findViewById(R.id.RecycleViewofNearPlace);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(NearbyPlaceActivity.this));

                mAdapter = new IndevidualPlaceAdapter(listPlace,NearbyPlaceActivity.this);
                recyclerView.setAdapter(mAdapter);
                AllPlaceInformation = listPlace;

            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    void NearestPlaceType(){
        if (PLACE_TYPE.equals("RESTAURANTS")){
            NearByPlaceTextView.setText(PLACE_TYPE);
        }
        else if(PLACE_TYPE.equals("HOSPITALS")){
            NearByPlaceTextView.setText(PLACE_TYPE);
        }
        else if(PLACE_TYPE.equals("POLICES")){
            NearByPlaceTextView.setText(PLACE_TYPE);
        }
        else {
            Intent it = new Intent(NearbyPlaceActivity.this,ErrorActivity.class);
            it.putExtra("Error_Title", "Nearest Place Type");
            it.putExtra("Error_Des", "Did not find any place of this type .");
            startActivity(it);
            finish();
        }
    }


    String getRestudenturl(String lat ,String lon , String radius){
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lon+"&radius="+radius+"&type=restaurant&key=AIzaSyAgQeCY7M40CSLSZ5XQj7wxQos_dRyPHSA";
        return url;
    }
    String getHospitalurl(String lat ,String lon , String radius){
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lon+"&radius="+radius+"&type=hospital&key=AIzaSyAgQeCY7M40CSLSZ5XQj7wxQos_dRyPHSA";
        return url;
    }
    String getPolicesurl(String lat ,String lon , String radius){
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+lat+","+lon+"&radius="+radius+"&type=police&key=AIzaSyAgQeCY7M40CSLSZ5XQj7wxQos_dRyPHSA";
        return url;
    }

    private String makeHttpRequest(URL url) throws IOException {


        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            jsonResponse = readFromStream(inputStream);
        } catch (IOException e) {
            // TODO: Handle the exception
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }



    public static double distance(double lat1, double lat2, double lon1, double lon2, double el1, double el2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        double height = el1 - el2;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }


    private double distance_new(double lat1, double lon1, double lat2, double lon2, char unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == 'K') {
            dist = dist * 1.609344;
        } else if (unit == 'N') {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts decimal degrees to radians             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    /*::  This function converts radians to decimal degrees             :*/
    /*:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    private class GetNearestLocationValue extends AsyncTask<URL, Void, String > {

        public  String jsonValue = "";
        public GetNearestLocationValue() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {

            try {
                Log.i("My_Project",s);

                if(PLACE_TYPE.equals("RESTAURANTS")){
                    sharedPrefseditor = sharedPrefs.edit();
                    sharedPrefseditor.putString("lat", UserLatitude);
                    sharedPrefseditor.putString("lon", UserLongitude);
                    sharedPrefseditor.putString("hospital", sharedPrefs.getString("hospital","" ));
                    sharedPrefseditor.putString("police", sharedPrefs.getString("police","" ));
                    sharedPrefseditor.putString("restaurant", s);
                    sharedPrefseditor.commit();
                }
                else if(PLACE_TYPE.equals("HOSPITALS")){
                    sharedPrefseditor = sharedPrefs.edit();
                    sharedPrefseditor.putString("lat", UserLatitude);
                    sharedPrefseditor.putString("lon", UserLongitude);
                    sharedPrefseditor.putString("hospital", s);
                    sharedPrefseditor.putString("police", sharedPrefs.getString("police","" ));
                    sharedPrefseditor.putString("restaurant", sharedPrefs.getString("restaurant","" ));
                    sharedPrefseditor.commit();
                }
                else if(PLACE_TYPE.equals("POLICES")){
                    sharedPrefseditor = sharedPrefs.edit();
                    sharedPrefseditor.putString("lat", UserLatitude);
                    sharedPrefseditor.putString("lon", UserLongitude);
                    sharedPrefseditor.putString("hospital", sharedPrefs.getString("hospital","" ));
                    sharedPrefseditor.putString("police", s);
                    sharedPrefseditor.putString("restaurant", sharedPrefs.getString("restaurant","" ));
                    sharedPrefseditor.commit();
                }


                JSONObject root = new JSONObject(s);

                if (root.getString("status").equals("OK")) {
                    progress.dismiss();


                    List<PlaceInformation> listPlace = new ArrayList<>();
                    JSONArray result = root.getJSONArray("results");

                    for (int i = 0;i<result.length();i++){
                        PlaceInformation place = new PlaceInformation();
                        JSONObject getResultValue = result.getJSONObject(i);
                        JSONObject geometry = getResultValue.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        //JSONArray photos = getResultValue.getJSONArray("photos");
                        //JSONObject photos_0 = photos.getJSONObject(0);



                        place.setLat(location.getString("lat"));
                        place.setLng(location.getString("lng"));
                        place.setId(getResultValue.getString("id"));
                        place.setPlaceid(getResultValue.getString("place_id"));
                        place.setName(getResultValue.getString("name"));
                        place.setVicinity(getResultValue.getString("vicinity"));

                        place.setRating("0.0");
                        //if(photos_0.has("photo_reference")) {
                       //     place.setImage_reference(photos_0.getString("photo_reference"));
                       // }
                        place.setImage_reference("");
                        place.setReference(getResultValue.getString("reference"));

                       /* double distent = distance(
                                Double.parseDouble(NearbyPlaceActivity.UserLatitude),
                                Double.parseDouble(place.getLat()),
                                Double.parseDouble(NearbyPlaceActivity.UserLongitude),
                                Double.parseDouble(place.getLng()),
                                0.0,
                                1.0
                        );
                        */
                        double distent = distance_new(
                                Double.parseDouble(NearbyPlaceActivity.UserLatitude),
                                Double.parseDouble(NearbyPlaceActivity.UserLongitude),
                                Double.parseDouble(place.getLat()),
                                Double.parseDouble(place.getLng()),
                                'K'
                        );
                        place.setCalculateDistence(new DecimalFormat("##.####").format(distent)+"");
                        Log.i("My_Project",place.toString());

                        listPlace.add(place);

                    }


                    recyclerView = (RecyclerView) findViewById(R.id.RecycleViewofNearPlace);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(NearbyPlaceActivity.this));

                    mAdapter = new IndevidualPlaceAdapter(listPlace,NearbyPlaceActivity.this);
                    recyclerView.setAdapter(mAdapter);
                    AllPlaceInformation = listPlace;
                    GetNearPlaceJsonValue.cancel(true);
                }
                else if (root.getString("status").equals("ZERO_RESULTS")){

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (PLACE_TYPE.equals("RESTAURANTS")){
                                    GetNearPlaceJsonValue = new GetNearestLocationValue();
                                    int red = (int) Double.parseDouble(NearbyPlaceActivity.UserDataRaduis) + 100;
                                    NearbyPlaceActivity.UserDataRaduis = red+"";
                                    URL url = new URL(getRestudenturl(NearbyPlaceActivity.UserLatitude,NearbyPlaceActivity.UserLongitude,NearbyPlaceActivity.UserDataRaduis));
                                    GetNearPlaceJsonValue.execute(url);
                                }
                                else if(PLACE_TYPE.equals("HOSPITALS")){
                                    GetNearPlaceJsonValue = new GetNearestLocationValue();
                                    int red = (int) Double.parseDouble(NearbyPlaceActivity.UserDataRaduis) + 500;
                                    NearbyPlaceActivity.UserDataRaduis = red+"";
                                    URL url = new URL(getHospitalurl(NearbyPlaceActivity.UserLatitude,NearbyPlaceActivity.UserLongitude,NearbyPlaceActivity.UserDataRaduis));
                                    GetNearPlaceJsonValue.execute(url);
                                }
                                else if(PLACE_TYPE.equals("POLICES")){
                                    GetNearPlaceJsonValue = new GetNearestLocationValue();
                                    int red = (int) Double.parseDouble(NearbyPlaceActivity.UserDataRaduis) + 1000;
                                    NearbyPlaceActivity.UserDataRaduis = red+"";
                                    URL url = new URL(getPolicesurl(NearbyPlaceActivity.UserLatitude,NearbyPlaceActivity.UserLongitude,NearbyPlaceActivity.UserDataRaduis));
                                    GetNearPlaceJsonValue.execute(url);
                                }



                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }
                    },3000);


                }
                else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (PLACE_TYPE.equals("RESTAURANTS")){
                                    GetNearPlaceJsonValue = new GetNearestLocationValue();
                                    URL url = new URL(getRestudenturl(NearbyPlaceActivity.UserLatitude,NearbyPlaceActivity.UserLongitude,NearbyPlaceActivity.UserDataRaduis));
                                    GetNearPlaceJsonValue.execute(url);
                                }
                                else if(PLACE_TYPE.equals("HOSPITALS")){
                                    GetNearPlaceJsonValue = new GetNearestLocationValue();
                                    URL url = new URL(getHospitalurl(NearbyPlaceActivity.UserLatitude,NearbyPlaceActivity.UserLongitude,NearbyPlaceActivity.UserDataRaduis));
                                    GetNearPlaceJsonValue.execute(url);
                                }
                                else if(PLACE_TYPE.equals("POLICES")){
                                    GetNearPlaceJsonValue = new GetNearestLocationValue();
                                    URL url = new URL(getPolicesurl(NearbyPlaceActivity.UserLatitude,NearbyPlaceActivity.UserLongitude,NearbyPlaceActivity.UserDataRaduis));
                                    GetNearPlaceJsonValue.execute(url);
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    },3000);

                }

            }catch (Exception e){
                    e.printStackTrace();
            }

            super.onPostExecute(s);
        }
        @Override
        protected String doInBackground(URL... urls) {
            String url = urls[0].toString();

          try {
              Log.i("My_project",url);
              jsonValue =  makeHttpRequest(urls[0]);
          }catch (Exception e){
              e.printStackTrace();
          }
            return jsonValue;

        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


    }

}
