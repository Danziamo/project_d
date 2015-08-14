package com.mirsoft.easyfix.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mirsoft.easyfix.OrderDetailActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.TabsActivity;
import com.mirsoft.easyfix.adapters.OrderAdapter;
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.utils.HelperUtils;
import com.mirsoft.easyfix.views.RecyclerViewSimpleDivider;
import com.mirsoft.easyfix.utils.Singleton;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class NewOrdersFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener{
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Bundle mBundle;

    private HashMap<Marker, Order> mOrderMarkerMap;

    private OrderAdapter mOrderAdapter;
    private RecyclerView rv;

    private ViewFlipper flipper;
    Singleton dc;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_orders, container, false);
        dc = Singleton.getInstance(getActivity());

        dc.currentSelectedTabPage = 0;

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        flipper = (ViewFlipper)view.findViewById(R.id.view_flipper);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGoogleMap = mMapView.getMap();
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnInfoWindowClickListener(this);
        /*displayOnMap();*/

        // Perform any camera updates here

        rv = (RecyclerView)view.findViewById(R.id.rvOrders);
        rv.addItemDecoration(new RecyclerViewSimpleDivider(getActivity()));
        rv.setHasFixedSize(true);
        getData();

        mOrderAdapter = new OrderAdapter(new ArrayList<Order>(), R.layout.list_item_order, getActivity(), Constants.CLIENT_ORDER_ADAPTER_MODE_NEW);
        rv.setAdapter(mOrderAdapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        setBottomButtonsListeners();

        return view;
    }

    public void setBottomButtonsListeners(){
        ((TabsActivity)getActivity()).mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.setVisibility(View.VISIBLE);
                rv.setVisibility(View.GONE);
                toRight();

                ((TabsActivity)getActivity()).mapButton.setEnabled(false);
                ((TabsActivity)getActivity()).ordersListButton.setEnabled(true);
            }
        });
        ((TabsActivity)getActivity()).ordersListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.setVisibility(View.GONE);
                rv.setVisibility(View.VISIBLE);
                toLeft();

                ((TabsActivity) getActivity()).mapButton.setEnabled(true);
                ((TabsActivity) getActivity()).ordersListButton.setEnabled(false);
            }
        });
    }

    public void getData() {

        final Settings settings = new Settings(getActivity());
        RestClient.getOrderService(true).getByUserId(settings.getUserId(), new Callback<ArrayList<Order>>() {
            @Override
            public void success(ArrayList<Order> orders, Response response) {
                ArrayList<Order> displayList = new ArrayList<>();
                int userId = settings.getUserId();
                mOrderMarkerMap = new HashMap<>();
                mGoogleMap.clear();
                for (int i = 0; i < orders.size(); ++i) {
                    Order order = orders.get(i);
                    //Skipping orders where contractor is not null
                    if (order.getContractor() != null) continue;

                    LatLng position = order.getLatLng();
                    displayList.add(order);
                    if (position != null) {
                        int resId = HelperUtils.getResIdFromSpecialtySlug(order.getSpecialty().getSlug());
                        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                                .position(order.getLatLng())
                                .title(order.getSpecialty().getName())
                                .icon(BitmapDescriptorFactory.fromResource(resId)));
                        mOrderMarkerMap.put(marker, order);
                    }
                }
                mOrderAdapter.setDataset(displayList);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show(); //crash
            }
        });
    }

    public void toLeft(){
        flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_right));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_to_left));
        flipper.showNext();
    }

    public void toRight(){
        flipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_left));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_to_right));
        flipper.showPrevious();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Order order = mOrderMarkerMap.get(marker);
        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ORDER", order);
        intent.putExtra("bundle", bundle);
        getActivity().startActivity(intent);
    }
}
