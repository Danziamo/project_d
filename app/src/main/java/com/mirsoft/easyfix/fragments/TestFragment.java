package com.mirsoft.easyfix.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.TabsActivity;
import com.mirsoft.easyfix.common.OrderType;
import com.mirsoft.easyfix.utils.RecyclerViewSimpleDivider;
import com.mirsoft.easyfix.adapters.OrderAdapter;
import com.mirsoft.easyfix.models.Order;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private OrderType mType;
    private String mParam2;

    private RecyclerView rv;
    private OrderAdapter mOrderAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(OrderType type, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, type);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = (OrderType)getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getActivity().registerReceiver(new UpdateDateReciever(), new IntentFilter("update"));
        getActivity().registerReceiver(new UpdateFinishedDateReciever(), new IntentFilter("updatefinished"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        rv = (RecyclerView)view.findViewById(R.id.rvOrders);
        rv.addItemDecoration(new RecyclerViewSimpleDivider(getActivity()));
        rv.setHasFixedSize(true);
        mOrderAdapter = new OrderAdapter(getData(), R.layout.list_item_order, getActivity());
        rv.setAdapter(mOrderAdapter);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public ArrayList<Order> getData() {

        if(mType == OrderType.NEW)
            return  ((TabsActivity)getActivity()).getNewOrders();
        if(mType == OrderType.ACTIVE)
            return ((TabsActivity)getActivity()).getActiveOrders();
        if(mType == OrderType.FINISHED)
            return ((TabsActivity)getActivity()).getFinishedOrders();
        return null;
    }

    public class UpdateDateReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mOrderAdapter.setDataset(getData());
        }
    }

    public class UpdateFinishedDateReciever extends BroadcastReceiver {
    @Override
        public void onReceive(Context context, Intent intent) {
            mOrderAdapter.setDataset(getData());
        }
    }




}
