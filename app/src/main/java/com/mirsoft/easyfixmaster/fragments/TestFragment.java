package com.mirsoft.easyfixmaster.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mirsoft.easyfixmaster.R;
import com.mirsoft.easyfixmaster.Settings;
import com.mirsoft.easyfixmaster.TabsActivity;
import com.mirsoft.easyfixmaster.api.OrderApi;
import com.mirsoft.easyfixmaster.common.OrderType;
import com.mirsoft.easyfixmaster.service.ServiceGenerator;
import com.mirsoft.easyfixmaster.utils.RecyclerViewSimpleDivider;
import com.mirsoft.easyfixmaster.adapters.OrderAdapter;
import com.mirsoft.easyfixmaster.models.Order;
import com.mirsoft.easyfixmaster.models.Specialty;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        getActivity().registerReceiver(new UpdateDateReciever(), new IntentFilter("fragmentupdater"));
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
        /*ArrayList<Order> list = new ArrayList<>();
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

        return list;*/
    }

    public class UpdateDateReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mOrderAdapter.setDataset(getData());
        }
    }


}
