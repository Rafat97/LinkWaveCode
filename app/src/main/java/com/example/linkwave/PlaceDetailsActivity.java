package com.example.linkwave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;


public class PlaceDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    TextView myTextView;
    TextView myTextViewId;
    TextView myTextViewPlaceName;
    TextView myTextViewPlaceLocation;
    TextView myTextViewPlaceNumber;
    Button call_button;

    ProgressDialog progress;
    GetNearestLocationValue GetNearPlaceJsonValue;

    int positionSelected;
    public  static String CuttentUserPlaceId = "";


    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_details);


        Intent thisIntetn = getIntent();
        positionSelected = thisIntetn.getExtras().getInt("ItemClickedPosition");

/*
        GetNearPlaceJsonValue = new GetNearestLocationValue();
        URL url = null;
        try {
            url = new URL(getDetials(NearbyPlaceActivity.AllPlaceInformation.get(positionSelected).getPlaceid()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        GetNearPlaceJsonValue.execute(url);

        progress = new ProgressDialog(this);
        progress.setMessage("Please Wait ...");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(false);
        progress.setCancelable(false);
        progress.show();
*/

        myTextView = (TextView) findViewById(R.id.PlaceSelectPosition);
        myTextViewId = (TextView) findViewById(R.id.PlaceSelectPlaceId);
        myTextViewPlaceLocation = (TextView) findViewById(R.id.PlaceSelectPlaceLocation);
        myTextViewPlaceName = (TextView) findViewById(R.id.PlaceSelectPlaceName);
        myTextViewPlaceNumber = (TextView) findViewById(R.id.PlaceSelectPhoneNumber);
        call_button=(Button)findViewById(R.id.call_button);

        myTextViewPlaceLocation.setText(NearbyPlaceActivity.AllPlaceInformation.get(positionSelected).getVicinity());
        myTextViewPlaceName.setText(NearbyPlaceActivity.AllPlaceInformation.get(positionSelected).getName());
        myTextViewPlaceNumber.setText(NearbyPlaceActivity.AllPlaceInformation.get(positionSelected).getCalculateDistence()+" km away");



        PlaceInformation pf = NearbyPlaceActivity.AllPlaceInformation.get(positionSelected);
        myTextView.setText("Selected Place Information.");
        myTextViewId.setText(pf.getPlaceid());
        CuttentUserPlaceId = pf.getPlaceid();
     //   PlaceInfo(pf.getPlaceid());

        call_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress = new ProgressDialog(PlaceDetailsActivity.this);
                progress.setMessage("Please Wait ...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(false);
                progress.setCancelable(false);
                progress.show();
                PlaceInfo(NearbyPlaceActivity.AllPlaceInformation.get(positionSelected).getPlaceid());
            }
        });


        Toolbar mytoolbar = (Toolbar) findViewById(R.id.Main_toobar);
        setSupportActionBar(mytoolbar);
      //  getSupportActionBar().setDisplayShowTitleEnabled(false);

      /*  if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        PlaceInformation pf = NearbyPlaceActivity.AllPlaceInformation.get(positionSelected);

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        LatLng Location = new LatLng(Double.parseDouble(pf.getLat()), Double.parseDouble(pf.getLng()));
        mMap.addMarker(new MarkerOptions().position(Location).title(pf.getName()));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Location));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Location, 15.0f));
    }


    @Override
    public void onBackPressed() {
       // this.GetNearPlaceJsonValue.cancel(true);
        super.onBackPressed();
    }

    String getDetials(String Id){
        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid="+Id+"&fields=international_phone_number,formatted_address,rating,formatted_phone_number&key=AIzaSyAgQeCY7M40CSLSZ5XQj7wxQos_dRyPHSA";
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

                JSONObject root = new JSONObject(s);

                if (root.getString("status").equals("OK")) {

                    JSONObject result = root.getJSONObject("result");
                    String Location = "No address found";
                    String Number = "No number found";
                    if (result.has("formatted_address")){
                        Location = result.getString("formatted_address");
                    }
                    if(result.has("international_phone_number")){
                        Number = result.getString("international_phone_number");
                    }

                    myTextViewPlaceLocation.setText(Location);
                    myTextViewPlaceName.setText(NearbyPlaceActivity.AllPlaceInformation.get(positionSelected).getName());
                    myTextViewPlaceNumber.setText(Number);


                    progress.dismiss();
                }
                else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                progress.setMessage("Please Wait Server is busy ...");

                                GetNearPlaceJsonValue = new GetNearestLocationValue();
                                URL url = null;
                                url = new URL(getDetials(NearbyPlaceActivity.AllPlaceInformation.get(positionSelected).getPlaceid()));
                                GetNearPlaceJsonValue.execute(url);

                            }catch (Exception e){
                                e.printStackTrace();
                            }


                        }
                    },10000);

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

    void PlaceInfo(String place){

        String placeId = place;

        Places.initialize(getApplicationContext(),"AIzaSyAWCot7FkeUbf2dSkie1g8oPIb1k-nCvH0");
        PlacesClient placesClient = Places.createClient(this);
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.PHONE_NUMBER,Place.Field.RATING,Place.Field.PRICE_LEVEL);

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, placeFields).build();
/*
        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            Log.i(TAG, "Place found: " + place.getName());
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                int statusCode = apiException.getStatusCode();
                // Handle error with given status code.
                Log.e(TAG, "Place not found: " + exception.getMessage());
                Log.i("PLACE_DETAILS",exception.getMessage());
            }
        });
*/
        placesClient.fetchPlace(request).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();
                Log.i("PLACE_DETAILS",place.toString());
                String number = place.getPhoneNumber();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+number));
                startActivity(intent);
                progress.dismiss();
                return;

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("PLACE_DETAILS",e.getMessage());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        PlaceInfo(CuttentUserPlaceId);



                    }
                }, 5000);
            }
        });


    }




}
