package com.mirsoft.easyfix.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.TabsActivity;
import com.mirsoft.easyfix.adapters.SectionedOrderAdapter;
import com.mirsoft.easyfix.api.OrderApi;
import com.mirsoft.easyfix.common.OrderType;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.utils.RecyclerViewSimpleDivider;
import com.mirsoft.easyfix.adapters.OrderAdapter;
import com.mirsoft.easyfix.models.Order;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserOrderListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserOrderListFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private OrderType mType;
    private String mParam2;

    private RecyclerView rvActive;
    private RecyclerView rvOld;
    private OrderAdapter mOrderAdapterActive;
    private OrderAdapter mOrderAdapterOld;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param type Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserOrderListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserOrderListFragment newInstance(OrderType type, String param2) {
        UserOrderListFragment fragment = new UserOrderListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, type);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public UserOrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = (OrderType)getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //getActivity().registerReceiver(new UpdateDateReciever(), new IntentFilter("update"));
        //getActivity().registerReceiver(new UpdateFinishedDateReciever(), new IntentFilter("updatefinished"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_order_list, container, false);

        rvActive = (RecyclerView)view.findViewById(R.id.rvOrdersActive);
        rvActive.addItemDecoration(new RecyclerViewSimpleDivider(getActivity()));
        rvActive.setHasFixedSize(true);
        mOrderAdapterActive = new OrderAdapter(getData(OrderType.ACTIVE), R.layout.list_item_order, getActivity());
        rvActive.setAdapter(mOrderAdapterActive);
        rvActive.setItemAnimator(new DefaultItemAnimator());
        rvActive.setLayoutManager(new LinearLayoutManager(getActivity()));

        /*rvOld = (RecyclerView)view.findViewById(R.id.rvOrdersOld);
        rvOld.addItemDecoration(new RecyclerViewSimpleDivider(getActivity()));
        rvOld.setHasFixedSize(true);
        mOrderAdapterOld = new OrderAdapter(getData(OrderType.FINISHED), R.layout.list_item_order, getActivity());
        rvOld.setAdapter(mOrderAdapterActive);
        rvOld.setItemAnimator(new DefaultItemAnimator());
        rvOld.setLayoutManager(new LinearLayoutManager(getActivity()));*/

        List<SectionedOrderAdapter.Section> sections = new ArrayList<>();
        sections.add(new SectionedOrderAdapter.Section(0, "Active"));
        //sections.add(new SectionedOrderAdapter.Section(4, "Finished"));

        SectionedOrderAdapter.Section[] dummy = new SectionedOrderAdapter.Section[sections.size()];
        SectionedOrderAdapter mSectionedAdapter = new
                SectionedOrderAdapter(getActivity() ,R.layout.recycledview_section,R.id.section_text, mOrderAdapterActive);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        rvActive.setAdapter(mSectionedAdapter);

        fillData();

        return view;
    }

    private void fillData() {
        Settings settings = new Settings(getActivity());
        OrderApi api = RestClient.createService(OrderApi.class);
        api.getByUserIdAndStatuses(settings.getUserId(), null, new Callback<ArrayList<Order>>() {
            @Override
            public void success(ArrayList<Order> orders, Response response) {
                //ArrayList<Order> activeList =
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public ArrayList<Order> getData(OrderType type) {
        if(type == OrderType.NEW)
            return  ((TabsActivity)getActivity()).getNewOrders();
        if(type == OrderType.ACTIVE)
            return ((TabsActivity)getActivity()).getActiveOrders();
        if(type == OrderType.FINISHED)
            return ((TabsActivity)getActivity()).getFinishedOrders();
        return null;
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

    /*public class UpdateDateReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            mOrderAdapterActive.setDataset(getData(OrderType.ACTIVE));
        }
    }*/
}
