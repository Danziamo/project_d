package com.mirsoft.easyfix.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mirsoft.easyfix.ClientOrderDetailsActivity;
import com.mirsoft.easyfix.OrderDetailActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.TabsActivity;
import com.mirsoft.easyfix.adapters.OrderAdapter;
import com.mirsoft.easyfix.adapters.OrdersAdapter;
import com.mirsoft.easyfix.common.OrderType;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.utils.RecyclerViewSimpleDivider;
import com.mirsoft.easyfix.utils.Singleton;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class NewOrdersFragment extends Fragment{
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private Bundle mBundle;

    private HashMap<Marker, Order> mOrderMarkerMap;

    ListView orderListView;
    OrdersAdapter ordersAdapter;
    private OrderAdapter mOrderAdapter;

    private ViewFlipper flipper;
    Singleton dc;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       //getActivity().registerReceiver(new UpdateDateReciever(), new IntentFilter("update"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_orders, container, false);
        dc = Singleton.getInstance(getActivity());

        dc.currentSelectedTabPage = 0;

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

      //  ((TabsActivity)getActivity()).setBottomLinearLayoutState(true);

        flipper = (ViewFlipper)view.findViewById(R.id.view_flipper);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mGoogleMap = mMapView.getMap();
     //   mGoogleMap.setPadding(0, 0, 100, 0);

     //    adding marker

        mGoogleMap.setMyLocationEnabled(true);
     //   mGoogleMap.setOnInfoWindowClickListener(this);
        /*displayOnMap();*/
        getData();
        // Perform any camera updates here

      /*  rv = (RecyclerView)view.findViewById(R.id.rvOrders);
        rv.addItemDecoration(new RecyclerViewSimpleDivider(getActivity()));
        rv.setHasFixedSize(true);*/

        orderListView = (ListView)view.findViewById(R.id.listOrders);

        ArrayList<Order> testList = new ArrayList<>();
        Order order = new Order();

        testList.add(order);testList.add(order);testList.add(order);testList.add(order);
        testList.add(order);testList.add(order);testList.add(order);testList.add(order);
        testList.add(order);testList.add(order);testList.add(order);testList.add(order);

     /*   mOrderAdapter = new OrderAdapter(testList, R.layout.list_item_order, getActivity());
        rv.setAdapter(mOrderAdapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));*/
        ordersAdapter = new OrdersAdapter(getActivity(), R.layout.list_item_order,testList);
        orderListView.setAdapter(ordersAdapter);


//        FloatingActionButton btnSwitch = (FloatingActionButton) getActivity().findViewById(R.id.btnSwitch);
//        btnSwitch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), ClientOrderDetailsActivity.class);
//                startActivity(intent);
//            }
//        });

    //    ((TabsActivity)getActivity()).mapButton.setEnabled(false);

     //   setBottomLinearLayoutState();
        setBottomButtonsListeners();

       // Toast.makeText(getActivity(),"Orders",Toast.LENGTH_SHORT).show();

        return view;
    }

    public void setBottomButtonsListeners(){
        ((TabsActivity)getActivity()).mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.setVisibility(View.VISIBLE);
                orderListView.setVisibility(View.GONE);
                toRight();

                ((TabsActivity)getActivity()).mapButton.setEnabled(false);
                ((TabsActivity)getActivity()).ordersListButton.setEnabled(true);
            }
        });
        ((TabsActivity)getActivity()).ordersListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMapView.setVisibility(View.GONE);
                orderListView.setVisibility(View.VISIBLE);
                toLeft();

                ((TabsActivity) getActivity()).mapButton.setEnabled(true);
                ((TabsActivity) getActivity()).ordersListButton.setEnabled(false);
            }
        });
    }

    /*private void displayOnMap() {
        mOrderMarkerMap = new HashMap<>();
        mGoogleMap.clear();
        ArrayList<Order> orderList = getData();
        for (int i = 0; i < orderList.size(); ++i) {
            Order order = orderList.get(i);
            LatLng position = order.getLatLng();
            if (position != null) {
                Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(order.getLatLng()).title(order.getSpecialty().getName()));
                mOrderMarkerMap.put(marker, order);
            }
        }
    }*/

    private void getData() {
        final Settings settings = new Settings(getActivity());
        RestClient.getOrderService(true).getByUserId(settings.getUserId(), new Callback<ArrayList<Order>>() {
            @Override
            public void success(ArrayList<Order> orders, Response response) {
                ArrayList<Order> displayList = new ArrayList<Order>();
                int userId = settings.getUserId();
                mOrderMarkerMap = new HashMap<>();
                mGoogleMap.clear();
                for (int i = 0; i < orders.size(); ++i) {
                    Order order = orders.get(i);
                    if (userId == order.getClient().getId()) continue;
                    LatLng position = order.getLatLng();
                    displayList.add(order);
                    if (position != null) {
                        Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(order.getLatLng()).title(order.getSpecialty().getName()));
                        mOrderMarkerMap.put(marker, order);
                    }
                }
                ordersAdapter = new OrdersAdapter(getActivity(), R.layout.list_item_order, displayList);
                orderListView.setAdapter(ordersAdapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Fail", Toast.LENGTH_LONG).show(); //crash
            }
        });
    }

    /*private ArrayList<Order> getData() {
        return ((TabsActivity)getActivity()).getNewOrders();
    }*/

    public class UpdateDateReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*mOrderAdapter.setDataset(getData());
            displayOnMap();*/
        }
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

//    @Override
//    public void onInfoWindowClick(Marker marker) {
//        Order order = mOrderMarkerMap.get(marker);
//        Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("ORDER", order);
//        intent.putExtra("bundle", bundle);
//        getActivity().startActivity(intent);
//    }
}
