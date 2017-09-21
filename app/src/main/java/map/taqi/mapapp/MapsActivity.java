package map.taqi.mapapp;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import map.taqi.mapapp.POJO.Example;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 10000;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;



    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (!isConnected(MapsActivity.this)) buildDialog(MapsActivity.this).show();
        else {
            Toast.makeText(MapsActivity.this, "Welcome!! tap the button to find location", Toast.LENGTH_SHORT).show();

        }


        AdView mAdView;
        Button btnFullscreenAd;

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);


        // Create an icon
        ImageView icon = new ImageView(this);
        icon.setImageResource(R.drawable.actionmenu);


        ImageView icon2 = new ImageView(this);
        icon2.setImageResource(R.drawable.mapmenu);


        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .setPosition(7)
                .build();


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);


        // repeat many times:
        final ImageView Atm = new ImageView(this);
        Atm.setImageResource(R.drawable.atm2);


        ImageView Medical = new ImageView(this);
        Medical.setImageResource(R.drawable.medicl);


        ImageView University = new ImageView(this);
        University.setImageResource(R.drawable.school);

        ImageView Resturants = new ImageView(this);
        Resturants.setImageResource(R.drawable.rest2);

        ImageView Bank = new ImageView(this);
        Bank.setImageResource(R.drawable.bank);

        ImageView PoliceStation = new ImageView(this);
        PoliceStation.setImageResource(R.drawable.police);


        ImageView Airport = new ImageView(this);
        Airport.setImageResource(R.drawable.airport);

        final SubActionButton atmbtn = itemBuilder.setContentView(Atm).build();
        SubActionButton medbtn = itemBuilder.setContentView(Medical).build();
        SubActionButton universitybtn = itemBuilder.setContentView(University).build();
        SubActionButton resturantbtn = itemBuilder.setContentView(Resturants).build();
        SubActionButton bankbtn = itemBuilder.setContentView(Bank).build();
        SubActionButton policestationbtn = itemBuilder.setContentView(PoliceStation).build();
        SubActionButton airportbtn = itemBuilder.setContentView(Airport).build();


        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(atmbtn)
                .addSubActionView(medbtn)
                .addSubActionView(universitybtn)
                .addSubActionView(resturantbtn)
                .addSubActionView(bankbtn)
                .addSubActionView(policestationbtn)
                .addSubActionView(airportbtn)
                .attachTo(actionButton)
                .enableAnimations()
                .setStartAngle(90)
                .setEndAngle(-90)
                .build();

        atmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("atm");
                actionMenu.close(true);

                if (!isConnected(MapsActivity.this))
                    Toast.makeText(getApplicationContext(), "you are not connected!Please turn on the internet!!", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(MapsActivity.this, "Searching Nearby ATM Booths", Toast.LENGTH_SHORT).show();

                }


            }
        });


        medbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("hospital");
                actionMenu.close(true);

                if (!isConnected(MapsActivity.this))
                    Toast.makeText(getApplicationContext(), "you are not connected!Please turn on the internet!!", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(MapsActivity.this, "Searching Nearby hospitals", Toast.LENGTH_SHORT).show();

                }


            }
        });

        universitybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("university");
                actionMenu.close(true);

                if (!isConnected(MapsActivity.this))
                    Toast.makeText(getApplicationContext(), "you are not connected!Please turn on the internet!!", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(MapsActivity.this, "Searching Nearby universities", Toast.LENGTH_SHORT).show();

                }


            }
        });

        resturantbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("restaurant");
                actionMenu.close(true);

                if (!isConnected(MapsActivity.this))
                    Toast.makeText(getApplicationContext(), "you are not connected!Please turn on the internet!!", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(MapsActivity.this, "Searching Nearby restaurants", Toast.LENGTH_SHORT).show();

                }


            }
        });

        bankbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("bank");
                actionMenu.close(true);

                if (!isConnected(MapsActivity.this))
                    Toast.makeText(getApplicationContext(), "you are not connected!Please turn on the internet!!", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(MapsActivity.this, "Searching Nearby bank", Toast.LENGTH_SHORT).show();

                }


            }
        });

        policestationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("police");
                actionMenu.close(true);

                if (!isConnected(MapsActivity.this))
                    Toast.makeText(getApplicationContext(), "you are not connected!Please turn on the internet!!", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(MapsActivity.this, "Searching Nearby police stations", Toast.LENGTH_SHORT).show();

                }


            }
        });


        airportbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                build_retrofit_and_get_response("airport");
                actionMenu.close(true);

                if (!isConnected(MapsActivity.this))
                    Toast.makeText(getApplicationContext(), "you are not connected!Please turn on the internet!!", Toast.LENGTH_LONG).show();
                else {
                    Toast.makeText(MapsActivity.this, "Searching Nearby airport", Toast.LENGTH_SHORT).show();

                }


            }
        });


        FloatingActionButton mapButton = new FloatingActionButton.Builder(this)
                .setContentView(icon2)
                .setPosition(6)
                .build();


        SubActionButton.Builder mapitemBuilder = new SubActionButton.Builder(this);


        // repeat many times:
        ImageView normalMap = new ImageView(this);
        normalMap.setImageResource(R.drawable.submenu);

        ImageView hybridMap = new ImageView(this);
        hybridMap.setImageResource(R.drawable.hybrid);

        ImageView satelliteMap = new ImageView(this);
        satelliteMap.setImageResource(R.drawable.sattelite);

        ImageView terrainMap = new ImageView(this);
        terrainMap.setImageResource(R.drawable.terrain);


        SubActionButton normalMapbtn = mapitemBuilder.setContentView(normalMap).build();
        SubActionButton hybridbtn = mapitemBuilder.setContentView(hybridMap).build();
        SubActionButton satelitebtn = mapitemBuilder.setContentView(satelliteMap).build();
        SubActionButton terrainbtn = mapitemBuilder.setContentView(terrainMap).build();


        //attach the sub buttons to the main button
        final FloatingActionMenu mapMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(normalMapbtn)
                .addSubActionView(hybridbtn)
                .addSubActionView(satelitebtn)
                .addSubActionView(terrainbtn)
                .attachTo(mapButton)
                .enableAnimations()
                .setStartAngle(0)
                .setEndAngle(-90)
                .build();

        normalMapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                mapMenu.close(true);
                Toast.makeText(getApplication(), "Converted to normal map", Toast.LENGTH_LONG).show();
            }
        });


        hybridbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mapMenu.close(true);
                Toast.makeText(getApplication(), "Converted to hybrid map", Toast.LENGTH_LONG).show();

            }
        });

        satelitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                mapMenu.close(true);
                Toast.makeText(getApplication(), "Converted to satellite map", Toast.LENGTH_LONG).show();
            }
        });

        terrainbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                mapMenu.close(true);
                Toast.makeText(getApplication(), "Converted to terrain map", Toast.LENGTH_LONG).show();
            }
        });



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
        } else {

        }

        //show error dialog if Google Play Services not available
        if (!isGooglePlayServicesAvailable()) {
            Log.d("onCreate", "Google Play Services not available. Ending Test case.");
            finish();
        } else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this, result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this.Please turn on GPS & internet");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //finish();
                WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                wifi.setWifiEnabled(true);
            }
        });

        return builder;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }

    private void build_retrofit_and_get_response(String type) {

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getNearbyPlaces(type, latitude + "," + longitude, PROXIMITY_RADIUS);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Response<Example> response, Retrofit retrofit) {

                try {
                    mMap.clear();
                    // This loop will go through all the results and add marker on each location.
                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                        String placeName = response.body().getResults().get(i).getName();
                        String vicinity = response.body().getResults().get(i).getVicinity();
                        MarkerOptions markerOptions = new MarkerOptions();
                        LatLng latLng = new LatLng(lat, lng);
                        // Position of Marker on Map
                        markerOptions.position(latLng);
                        // Adding Title to the Marker
                        markerOptions.title(placeName + " : " + vicinity);
                        // Adding Marker to the Camera.
                        Marker m = mMap.addMarker(markerOptions);
                        // Adding colour to the marker
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        // move map camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));


                    }
                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });

    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
            return;


        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Hi! you are here");

        // Adding colour to the marker
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));

        // Adding Marker to the Map
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f", latitude, longitude));

        Log.d("onLocationChanged", "Exit");

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }


    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_FINE_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSION_REQUEST_COARSE_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        if (mGoogleApiClient == null) {
                            buildGoogleApiClient();
                        }


                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;

            }

            case MY_PERMISSION_REQUEST_COARSE_LOCATION:
                break;


        }
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure to quit??")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MapsActivity.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}


