package com.mirsoft.easyfix.fragments;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.common.OrderType;
import com.mirsoft.easyfix.networking.api.OrderApi;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.networking.models.CommonOrder;
import com.mirsoft.easyfix.networking.models.StatusOrder;
import com.mirsoft.easyfix.utils.HelperUtils;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class OrderDetailActivityFragment extends Fragment {
    private MapView mMapView;
    private GoogleMap mGoogleMap;

    Order order;

    Button btnSubmit;
    Button btnFinish;
    Button btnCancel;

    public OrderDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        EditText tvDescription = (EditText)view.findViewById(R.id.tvDescription);
        final EditText tvPhone = (EditText)view.findViewById(R.id.tvPhone);
        EditText tvAddress = (EditText)view.findViewById(R.id.tvAddress);

        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        order = (Order)bundle.getSerializable("ORDER");
        Settings settings = new Settings(getActivity());
        Toast.makeText(getActivity(), String.valueOf(order.getId()), Toast.LENGTH_SHORT).show();

        tvDescription.setText(order.getDescription());
        tvPhone.setText(order.getPhone());
        tvPhone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (tvPhone.getRight() - tvPhone.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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
            int resId = HelperUtils.getResIdFromSpecialtySlug(order.getSpecialty().getSlug());
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(order.getLatLng())
                    .title(order.getSpecialty().getName())
                    .icon(BitmapDescriptorFactory.fromResource(resId)));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(order.getLatLng(), 15));
        }

        btnSubmit = (Button)view.findViewById(R.id.btnSubmitOrder);
        btnFinish = (Button)view.findViewById(R.id.btnFinishOrder);
        btnCancel = (Button)view.findViewById(R.id.btnCancelOrder);

        if(order.getStatus() == OrderType.NEW && order.getContractor() == null){
            btnSubmit.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);

        } else if (HelperUtils.isNewOrPending(order.getStatus()) && order.getContractor().getId() == settings.getUserId()) {
            btnSubmit.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
        } else if (order.getStatus() == OrderType.ACTIVE) {
            btnFinish.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
        } else {
            btnFinish.setVisibility(View.GONE);
            btnCancel.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings settings = new Settings(getActivity());
                if (order.getContractor() == null) {
                    RestClient.getOrderService(true).postRequest(true, settings.getUserId(), order.getId(), new Callback<Object>() {
                        @Override
                        public void success(Object o, Response response) {
                            if (getActivity() != null)
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            if (getActivity() != null)
                                Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    StatusOrder sorder = new StatusOrder();
                    sorder.status = OrderType.ACTIVE;
                    RestClient.getOrderService(true).updateOrderStatus(sorder, settings.getUserId(), order.getId(), new Callback<Order>() {
                        @Override
                        public void success(Order order, Response response) {
                            if (getActivity() != null)
                                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            if (getActivity() != null)
                                Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings settings = new Settings(getActivity());
                StatusOrder sorder = new StatusOrder();
                sorder.status = OrderType.FINISHED;
                RestClient.getOrderService(true).updateOrderStatus(sorder, settings.getUserId(), order.getId(), new Callback<Order>() {
                    @Override
                    public void success(Order order, Response response) {
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
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
