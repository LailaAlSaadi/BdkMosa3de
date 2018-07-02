package papers.bdkmosa3de.activities;

import android.Manifest.permission;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import papers.bdkmosa3de.R;
import papers.bdkmosa3de.common.ItemDao;
import papers.bdkmosa3de.model.Order;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static org.springframework.util.StringUtils.isEmpty;

@EActivity(R.layout.activity_order)
public class OrderActivity extends AppCompatActivity
        implements
        View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener

{

    ItemDao itemDao = new ItemDao();

    @ViewById(R.id.areas)
    public Spinner areasSpinner;

    @ViewById(R.id.in_det)
    public EditText detailsEditText;

    @ViewById(R.id.service_title)
    TextView serviceTitle;

    String locationPoints = "";
    TextView txtDate, txtFromTime, txtToTime, txtLocMap;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Dialog dialog;

    @ViewById(R.id.locMethod)
    RadioGroup locMethodRG;

    @ViewById(R.id.onmap)
    RadioButton onmapLoc;

    boolean onMap;
    boolean fromArea;

    @AfterViews
    public void preMap() {

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initViews();
    }

    @AfterViews
    protected void pre() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        int key = intent.getIntExtra("key", 0);

        serviceTitle.setText(getString(MainActivity.desc[key]));

        txtDate = findViewById(R.id.in_date);
        txtFromTime = findViewById(R.id.from_time);
        txtToTime = findViewById(R.id.to_time);
        txtLocMap = findViewById(R.id.locMap);

        txtDate.setOnClickListener(this);
        txtFromTime.setOnClickListener(this);
        txtToTime.setOnClickListener(this);
        txtLocMap.setOnClickListener(this);

        onmapLoc.setChecked(true);
        areasSpinner.setEnabled(false);
        txtLocMap.setEnabled(true);
    }


    @AfterViews
    public void locRadio() {
        locMethodRG.setOnCheckedChangeListener((radioGroup, checkedId) -> {
            int selectedId = radioGroup.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(selectedId);
            onMap = radioButton.getText().equals(getString(R.string.on_map));
            fromArea = radioButton.getText().equals(getString(R.string.from_areas));

            if (onMap) {
                areasSpinner.setEnabled(false);
                txtLocMap.setEnabled(true);
            } else if (fromArea) {
                txtLocMap.setEnabled(false);
                areasSpinner.setEnabled(true);
            }
        });
    }


    @Click
    public void orderNow() {
        if (!isValidInput()) return;
        Order order = new Order();

        if (onMap) {
            order.setArea(null);
            order.setLocation(locationPoints);
        } else if (fromArea){
            order.setArea(areasSpinner.getSelectedItem().toString());
            order.setLocation(null);
        }


        order.setServiceType(serviceTitle.getText().toString());
        order.setDate(txtDate.getText().toString());
        order.setFromTime(txtFromTime.getText().toString());
        order.setToTime(txtToTime.getText().toString());
        order.setDetails(detailsEditText.getText().toString());
        order.setUid(UUID.randomUUID().toString());
        itemDao.addOrder(order);
        Toast.makeText(getApplicationContext(), getString(R.string.order_done), Toast.LENGTH_LONG).show();
        (new Handler()).postDelayed(this::finish, 1000);

    }

    private boolean isValidInput() {
        if (isEmpty(detailsEditText.getText().toString())
                || (isEmpty(txtLocMap.getText().toString())
                && "...".equals(areasSpinner.getSelectedItem().toString()))) {
            Toast.makeText(this, getString(R.string.please_complete), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == txtDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == txtFromTime) {

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> txtFromTime.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == txtToTime) {

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> txtToTime.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
            timePickerDialog.show();
        }


        if (v == txtLocMap) {
            dialog.show();
        }


    }


    private FrameLayout frameLayout;
    private FrameLayout circleFrameLayout;
    private TextView textView;
    private Button chooseLocation;

    private ProgressBar progress;
    private SupportMapFragment mapFragment;
    private GoogleMap mGoogleMap;
    LocationManager lm;

    private void initViews() {
        EnableGPSAutoMatically();

        dialog = new Dialog(this);

        dialog.setContentView(R.layout.activity_maps);
        dialog.setTitle("Game Paused");

        frameLayout = dialog.findViewById(R.id.map_container);

        circleFrameLayout = frameLayout.findViewById(R.id.pin_view_circle);
        textView = circleFrameLayout.findViewById(R.id.textView);
        progress = circleFrameLayout.findViewById(R.id.profile_loader);

        chooseLocation = dialog.findViewById(R.id.loc_selected);
        chooseLocation.setOnClickListener((View v) -> {
            dialog.hide();
            Geocoder gc = new Geocoder(OrderActivity.this);
            if (Geocoder.isPresent()) {
                try {
                    CameraPosition cameraPosition = mGoogleMap.getCameraPosition();

                    List<Address> list = gc.getFromLocation(cameraPosition.target.latitude, cameraPosition.target.longitude, 1);

                    Address address = list.get(0);
                    StringBuffer str = new StringBuffer();
                    str.append(String.format(
                            "%s  %s, %s", address.getMaxAddressLineIndex() > 0 ?
                                    address.getAddressLine(0) : "",
                            address.getLocality(),
                            address.getFeatureName()));

                    txtLocMap.setText(str);
                    locationPoints = cameraPosition.target.latitude + "," + cameraPosition.target.longitude;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            EnableGPSAutoMatically();
            updateCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    public void updateCurrentLocation() {


        if (ActivityCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{ACCESS_COARSE_LOCATION, permission.ACCESS_FINE_LOCATION}, 1);
                EnableGPSAutoMatically();
            }
            return;
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
                        CameraUpdate zoom = CameraUpdateFactory.zoomTo(18);

                        mGoogleMap.moveCamera(center);
                        mGoogleMap.animateCamera(zoom);

                    }
                });
    }

    private void EnableGPSAutoMatically() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            builder.setAlwaysShow(true); // this is the key ingredient

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(result1 -> {
                final Status status = result1.getStatus();
                final LocationSettingsStates state = result1
                        .getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("xxxxxx", "Success");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("xxxxxx", "GPS is not on");
                        try {
                            status.startResolutionForResult(OrderActivity.this, 1000);

                        } catch (IntentSender.SendIntentException e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("xxxxxx", "Setting change not allowed");
                        break;
                }
            });
        }
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

    private void toast(String message) {
        try {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
