package com.mirsoft.easyfixmaster;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mirsoft.easyfixmaster.models.Order;
import com.mirsoft.easyfixmaster.models.Specialty;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private HashMap<Marker, Order> mOrderMarkerMap;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MapFragment newInstance(String param1, String param2) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

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

        // adding marker

        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.setOnInfoWindowClickListener(this);
        displayOnMap();
        // Perform any camera updates here

        return view;
    }

    private void displayOnMap() {
        mOrderMarkerMap = new HashMap<>();
        ArrayList<Order>orderList = getData();
        for (int i = 0; i < orderList.size(); ++i) {
            Order order = orderList.get(i);
            Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(order.getLatLng()).title(order.getSpecialty().getName()));
            mOrderMarkerMap.put(marker, order);
        }
    }

    private ArrayList<Order> getData() {
        ArrayList<Order> list = new ArrayList<>();
        Specialty plomber = new Specialty(1, "плотник", "plumber", "slave");
        Specialty electric = new Specialty(2, "электрик", "electricity", null);
        Specialty repair = new Specialty(3, "ремонт", "repair", null);
        Specialty decorator = new Specialty(4, "декоратор", "decorator", null);

        list.add(new Order(1, "+996551221445", "tam", "to", plomber, 42.874329, 74.614806));
        list.add(new Order(2, "+996551221445","tut", "eto", electric, 42.870167, 74.620386));
        list.add(new Order(3, "+996551221445","kak", "tak", repair, 42.866095, 74.616691));
        list.add(new Order(4, "+996551221445","nu", "tak", decorator, 42.859435, 74.614321));
        list.add(new Order(5, "+996551221445","nu", "tak", decorator, 42.856546, 74.619347));
        list.add(new Order(7, "+996551221445","nu", "tak", plomber, 42.852071, 74.616321));
        list.add(new Order(11, "+996551221445","nu", "tak", repair, 42.848198, 74.618504));
        list.add(new Order(23, "+996551221445","nu", "tak", decorator, 42.847889, 74.625330));
        list.add(new Order(41, "+996551221445","nu", "tak", decorator, 42.844580, 74.629891));
        list.add(new Order(11, "+996551221445","nu", "tak", electric, 42.840480, 74.618191));
        list.add(new Order(55, "+996551221445","nu", "tak", electric, 42.837118, 74.614747));
        return list;
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
