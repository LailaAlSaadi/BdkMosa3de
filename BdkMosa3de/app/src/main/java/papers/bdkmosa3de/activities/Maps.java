package papers.bdkmosa3de.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import papers.bdkmosa3de.R;

public class Maps extends FragmentActivity {

    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;

    private FrameLayout frameLayout;
    private FrameLayout circleFrameLayout;
    private TextView textView;
    private Button chooseLocation;

    private ProgressBar progress;
    private SupportMapFragment mapFragment;
    private GoogleMap mGoogleMap;
    LocationManager lm;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case LOCATION_REQUEST:
                if (canAccessLocation()) {
                    updateCurrentLocation();
                } else {
                    Toast.makeText(getApplicationContext(), "Enable Gps", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean canAccessLocation() {
        return (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hasPermission(String perm) {
        return (PackageManager.PERMISSION_GRANTED == checkSelfPermission(perm));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        if (!canAccessLocation()) {
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST);
        }

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        initViews();
    }

    private void initViews() {

        frameLayout = findViewById(R.id.map_container);

        circleFrameLayout = frameLayout.findViewById(R.id.pin_view_circle);
        textView = circleFrameLayout.findViewById(R.id.textView);
        progress = circleFrameLayout.findViewById(R.id.profile_loader);

        chooseLocation = findViewById(R.id.loc_selected);
        chooseLocation.setOnClickListener(v -> {
            Intent i = new Intent(new Intent(Maps.this, OrderActivity_.class));
            CameraPosition cameraPosition = mGoogleMap.getCameraPosition();
            i.putExtra("loc", cameraPosition.target.latitude + "," + cameraPosition.target.longitude);  // insert your extras here
            startActivityForResult(i, 0);
        });
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(googleMap -> {
            mGoogleMap = googleMap;

            mGoogleMap.setOnCameraMoveStartedListener(i -> {
                textView.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
            });


            mGoogleMap.setOnCameraIdleListener(() -> {

            });
        });

        MapsInitializer.initialize(getApplicationContext());
        updateCurrentLocation();
        moveMapCamera();


    }

    public void updateCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);// TODO: Consider calling
        }
        lm.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 0, 0, new LocationListener() {
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }

                    @Override
                    public void onLocationChanged(final Location location) {
                        CameraUpdate center = CameraUpdateFactory
                                .newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

                        mGoogleMap.moveCamera(center);
                        mGoogleMap.animateCamera(zoom);

                    }
                });
    }

    private void moveMapCamera() {
        if (mGoogleMap == null) {
            return;
        }

        CameraUpdate center = CameraUpdateFactory
                .newLatLng(new LatLng(31.944375, 35.927123));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);

        mGoogleMap.moveCamera(center);
        mGoogleMap.animateCamera(zoom);
    }
}