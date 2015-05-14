package com.example.edwards2james.goog_lab3;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {
    private GoogleMap googleMap;
    private int mapType = GoogleMap.MAP_TYPE_NORMAL;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpMapIfNeeded();

        googleMap.setMyLocationEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.normal_map:
                mapType = GoogleMap.MAP_TYPE_NORMAL;
                break;

            case R.id.satellite_map:
                mapType = GoogleMap.MAP_TYPE_SATELLITE;
                break;

            case R.id.terrain_map:
                mapType = GoogleMap.MAP_TYPE_TERRAIN;
                break;

            case R.id.hybrid_map:
                mapType = GoogleMap.MAP_TYPE_HYBRID;
                break;

        }

        switch(item.getItemId()) {
            case R.id.traff:
                googleMap.setTrafficEnabled(!googleMap.isTrafficEnabled());
                return true;
            case R.id.indoor:
                googleMap.setIndoorEnabled(!googleMap.isIndoorEnabled());
                return true;
            case R.id.building:
                googleMap.setBuildingsEnabled(!googleMap.isBuildingsEnabled());
                return true;
        }

        googleMap.setMapType(mapType);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save the map type so when we change orientation, the mape type can be restored
        LatLng cameraLatLng = googleMap.getCameraPosition().target;
        float cameraZoom = googleMap.getCameraPosition().zoom;
        outState.putInt("map_type", mapType);
        outState.putDouble("lat", cameraLatLng.latitude);
        outState.putDouble("lng", cameraLatLng.longitude);
        outState.putFloat("zoom", cameraZoom);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #googleMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #googleMap} is not null.
     */
    private void setUpMap() {

        // Enable MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Create a criteria object to retrieve provider
        Criteria criteria = new Criteria();

        // Get the name of the best provider
        String provider = locationManager.getBestProvider(criteria, true);

        // Get Current Location
        Location myLocation = locationManager.getLastKnownLocation(provider);

        // set map type
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Get latitude of the current location
        double latitude = myLocation.getLatitude();

        // Get longitude of the current location
        double longitude = myLocation.getLongitude();

        // Create a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        // Show the current location in Google Map
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                .title("Your location is...").snippet("Lat: " + latitude + " " + "Long: "
                        + longitude));
    }


}
