package com.mirsoft.easyfix;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.utils.LocationAddress;

public class MapsActivity extends AppCompatActivity {
    private MapView mMapView;
    private GoogleMap mGoogleMap;

    Toolbar toolbar;
    TextView tvAddress;
    Button submit;
    ImageView icMarker;

    private double curlat;
    private double curlng;
    private String curaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        toolbar = (Toolbar)findViewById(R.id.order_create_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
         });

        mMapView = (MapView)findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        tvAddress = (TextView)findViewById(R.id.tvAddress);
        submit = (Button)findViewById(R.id.btnSubmit);
        icMarker = (ImageView) findViewById(R.id.my_marker);
        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGoogleMap = mMapView.getMap();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        if (location != null)
        {
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude()), 15));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }

        mGoogleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            public void onCameraChange(CameraPosition arg0) {
                LatLng cameraPosition = new LatLng(arg0.target.latitude, arg0.target.longitude);
                String displayText = "Поиск адреса...";
                Location cameraLocation = new Location("someprovider");
                cameraLocation.setLatitude(arg0.target.latitude);
                cameraLocation.setLongitude(arg0.target.longitude);
                curlat = arg0.target.latitude;
                curlng = arg0.target.longitude;
                tvAddress.setText(displayText);
                LocationAddress.getAddressFromLocation(arg0.target.latitude, arg0.target.longitude,
                        getApplicationContext(), new GeocoderHandler());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("lat", curlat);
                intent.putExtra("lng", curlng);
                intent.putExtra("address", curaddress);
                setResult(Constants.OK_RESULT_CODE, intent);
                finish();
            }
        });
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            Bundle bundle = message.getData();
            locationAddress = bundle.getString("address");
            tvAddress.setText(locationAddress);
            curaddress = locationAddress;
            if (locationAddress != null && locationAddress.toLowerCase().contains("не удалось")) curaddress = "";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
