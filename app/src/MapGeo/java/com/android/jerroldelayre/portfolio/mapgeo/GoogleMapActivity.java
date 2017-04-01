package com.android.jerroldelayre.portfolio.mapgeo;

import android.content.Intent;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.jerroldelayre.portfolio.R;
import com.android.jerroldelayre.portfolio.mapgeo.googleapis.LocationAPI;
import com.android.jerroldelayre.portfolio.mapgeo.googleapis.SignInAPI;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GoogleMapActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerDragListener,
        LocationAPI.LocationAPIListener {

    private static final String TAG = "GoogleMapActivity";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view) NavigationView mNavigationView;

    private GoogleMap mMap;
    private LocationAPI mLocationApi;
    private Marker mMyLocationMarker;

    private String sDisplayName;
    private String sEmail;
    private Uri uriPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_map);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        //initialize google map
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //initialize locationApi
        mLocationApi = new LocationAPI(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            sDisplayName = bundle.getString(SignInAPI.DISPLAY_NAME);
            sEmail = bundle.getString(SignInAPI.EMAIL);
            uriPhoto = bundle.getParcelable(SignInAPI.PHOTO);
            initNavigationHeader(mNavigationView);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id == R.id.nav_map_normal_mode || id == R.id.nav_map_satellite_mode || id == R.id.nav_map_night_mode) {
            loadGoogleMapStyle(mMap, id);
        }
        /*if (id == R.id.nav_camera) {
            mLocationApi.startIntentService("Malolos, Bulacan");
            // Handle the camera action
        }
        */

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(false);
        loadGoogleMapStyle(googleMap, R.id.nav_map_normal_mode);

        mMap.setOnMarkerDragListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mLocationApi.OnStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mLocationApi.OnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationApi.OnPause();
    }

    @Override
    protected void onStop() {
        mLocationApi.OnStop();
        super.onStop();
    }

    @OnClick(R.id.fab_my_location)
    public void updateCurrentLocation(View view) {
        Log.i("getMyLocation", "here");
        mLocationApi.startLocationUpdate();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if(marker.equals(mMyLocationMarker)) {
            Log.i("GoogleMapActivity", "onMarkerDragEnd called");
            mLocationApi.startIntentServiceWithSelectedLocation(marker.getPosition().latitude, marker.getPosition().longitude);
        }
    }

    /**
     * Set the google map style as standard.
     *
     * @param googleMap The Google Map
     * @param navId The selected navigation id
     *
     * @see <a href="https://mapstyle.withgoogle.com/">Styling Wizard</a>
     */
    private void loadGoogleMapStyle(GoogleMap googleMap, int navId) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = false;
            if (navId == R.id.nav_map_normal_mode) {
                success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.standard_map_no_landmarks));
            } else if (navId == R.id.nav_map_night_mode) {
                success = googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.night_map));
            } else if (navId == R.id.nav_map_satellite_mode) {
                success = true;
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
            if(!success) {
                Log.e("GoogleMapActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("GoogleMapActivity", "Can't find style error.", e);
        }
    }

    @Override
    public void getOutputAddress(String address) {

    }

    @Override
    public void getLatlngAddress(LatLng latLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14.0f));
    }

    @Override
    public void locationUpdate(Location location) {
        if(mMyLocationMarker != null) {
            mMyLocationMarker.remove();
        }

        if(location == null) return;
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMyLocationMarker = mMap.addMarker(new MarkerOptions().position(latLng).draggable(true));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19.0f));

        mLocationApi.startIntentService();
        mLocationApi.stopLocationUpdate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LocationAPI.REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        mLocationApi.startLocationUpdate();
                        break;
                    case RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }
    }

    private void initNavigationHeader(NavigationView navigationView) {
        View view = navigationView.getHeaderView(0);
        TextView mTextViewDisplayName = (TextView) view.findViewById(R.id.tv_display_name);
        TextView mTextViewEmail = (TextView) view.findViewById(R.id.tv_email);
        ImageView mImageViewPhoto = (ImageView) view.findViewById(R.id.iv_profile);

        mTextViewDisplayName.setText(sDisplayName);
        mTextViewEmail.setText(sEmail);
        //mImageViewPhoto.setImageURI(uriPhoto);
        Glide.with(getApplicationContext()).load(uriPhoto)
                .thumbnail(0.5f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageViewPhoto);
    }
}
