package com.mirsoft.easyfixmaster;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mirsoft.easyfixmaster.api.OrderApi;
import com.mirsoft.easyfixmaster.models.Order;
import com.mirsoft.easyfixmaster.service.ServiceGenerator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class OrderDetailActivityFragment extends Fragment {
    private MapView mMapView;
    private GoogleMap mGoogleMap;

    public OrderDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        TextView tvDescription = (TextView)view.findViewById(R.id.tvDescription);
        final TextView tvPhone = (TextView)view.findViewById(R.id.tvPhone);
        TextView tvAddress = (TextView)view.findViewById(R.id.tvAddress);

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        final Order order = (Order)bundle.getSerializable("ORDER");

        tvDescription.setText(order.getDescription());
        tvPhone.setText(order.getPhone());
        tvPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if(event.getRawX() >= (tvPhone.getRight() - tvPhone.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + order.getClient().getPhone()));
                        startActivity(callIntent);
                        return true;
                    }
                }
                return false;
            }
        });
        tvAddress.setText(order.getAddress());

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGoogleMap = mMapView.getMap();
        //mGoogleMap.setPadding(0, 0, 100, 0);
        if (order.getLatLng() != null) {
            mGoogleMap.addMarker(new MarkerOptions().position(order.getLatLng()));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(order.getLatLng(), 15));
        }

        Button btnSubmit = (Button)view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings settings = new Settings(getActivity());
                OrderApi api = ServiceGenerator.createService(OrderApi.class, settings);
                api.postRequest(true, settings.getUserId(), order.getId(), new Callback<Object>() {
                    @Override
                    public void success(Object o, Response response) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }
}
