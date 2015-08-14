package com.mirsoft.easyfix.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.TabsActivity;
import com.mirsoft.easyfix.adapters.SectionedOrderAdapter;
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.common.OrderType;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.views.RecyclerViewSimpleDivider;
import com.mirsoft.easyfix.adapters.OrderAdapter;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.utils.Singleton;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class UserOrderListFragment extends Fragment {

    private RecyclerView rvActive;
    private OrderAdapter mOrderAdapterActive;
    private int allClientOrdersSize = -1;

    Singleton dc;

    public UserOrderListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_order_list, container, false);
        dc = Singleton.getInstance(getActivity());

        dc.currentSelectedTabPage = 2;

        //  ((TabsActivity)getActivity()).setBottomLinearLayoutState(false);

        ArrayList<Order> testList = new ArrayList<>();
        ArrayList<Order> oldList = new ArrayList<>();
        ArrayList<Order> finalList = new ArrayList<>();

        Order order = new Order();
        testList.add(order);testList.add(order);testList.add(order);testList.add(order);
        oldList.add(order);oldList.add(order);oldList.add(order);oldList.add(order);oldList.add(order);
        oldList.add(order);oldList.add(order);oldList.add(order);oldList.add(order);oldList.add(order);

        for(int i = 0; i < testList.size(); i++){
            finalList.add(0, testList.get(i));
        }

        for(int i = 0; i < oldList.size(); i++){
            finalList.add(0,oldList.get(i));
        }

        rvActive = (RecyclerView)view.findViewById(R.id.rvOrdersActive);
        rvActive.addItemDecoration(new RecyclerViewSimpleDivider(getActivity()));
        rvActive.setHasFixedSize(true);
        // mOrderAdapterActive = new OrderAdapter(getData(OrderType.ACTIVE), R.layout.list_item_order, getActivity());
        //mOrderAdapterActive = new OrderAdapter(finalList,R.layout.list_item_order,getActivity());

        //rvActive.setAdapter(mOrderAdapterActive);
        rvActive.setItemAnimator(new DefaultItemAnimator());
        rvActive.setLayoutManager(new LinearLayoutManager(getActivity()));

        fillDataClient();

       // ((TabsActivity)getActivity()).myMastersButton.setEnabled(false);
        setBottomButtonsListeners();



        return view;
    }

    public ArrayList<Order> copy(ArrayList<Order> ar1, ArrayList<Order> ar2){
        ArrayList<Order> fin = new ArrayList<>();
        for(int i = 0; i < ar1.size(); i++){
            ar2.add(ar1.get(i));
        }
        return fin;
    }

    public void setBottomButtonsListeners(){
        ((TabsActivity)getActivity()).myMastersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"myMastersButton",Toast.LENGTH_SHORT).show();
                fillDataClient();

                dc.isClientMode = true;

                ((TabsActivity)getActivity()).myMastersButton.setEnabled(false);
                ((TabsActivity)getActivity()).myClientsButton.setEnabled(true);
            }
        });
        ((TabsActivity)getActivity()).myClientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"myClientsButton",Toast.LENGTH_SHORT).show();
                fillDataMaster();

                dc.isClientMode = false;

                ((TabsActivity)getActivity()).myMastersButton.setEnabled(true);
                ((TabsActivity)getActivity()).myClientsButton.setEnabled(false);
            }
        });
    }




    private void fillDataClient() {
        final Settings settings = new Settings(getActivity());
        RestClient.getOrderService(false).getByUserId(settings.getUserId(), new Callback<ArrayList<Order>>() {
            @Override
            public void success(ArrayList<Order> orders, Response response) {
                ArrayList<Order> activeOrders = new ArrayList<>();
                ArrayList<Order> finishedOrders = new ArrayList<>();
                ArrayList<Order> allOrders = new ArrayList<>();

                for (int i = 0; i < orders.size(); ++i) {
                    Order tempOrder = orders.get(i);
                    if (tempOrder.getClient().getId() != settings.getUserId()) continue;
                    if (tempOrder.getStatus() != OrderType.FINISHED && tempOrder.getStatus() != OrderType.CANCELLED) {
                        activeOrders.add(tempOrder);
                    } else {
                        finishedOrders.add(tempOrder);
                    }
                }
                allOrders.addAll(activeOrders);
                allOrders.addAll(finishedOrders);

                dc.activeOrdersCount   = activeOrders.size();
                dc.finishedOrdersCount = finishedOrders.size();

                allClientOrdersSize = allOrders.size();

                mOrderAdapterActive = new OrderAdapter(allOrders, R.layout.list_item_order, getActivity(), Constants.CLIENT_ORDER_ADAPTER_MODE_ACTIVE);

                List<SectionedOrderAdapter.Section> sections = new ArrayList<>();
                sections.add(new SectionedOrderAdapter.Section(0, "Active"));
                sections.add(new SectionedOrderAdapter.Section(activeOrders.size(), "Finished"));

                SectionedOrderAdapter.Section[] dummy = new SectionedOrderAdapter.Section[sections.size()];
                SectionedOrderAdapter mSectionedAdapter = new
                        SectionedOrderAdapter(getActivity(), R.layout.recycledview_section, R.id.section_text, mOrderAdapterActive);
                mSectionedAdapter.setSections(sections.toArray(dummy));

                //Apply this adapter to the RecyclerView
                rvActive.setAdapter(mSectionedAdapter);

            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Some network error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void fillDataMaster() {
        final Settings settings = new Settings(getActivity());
        RestClient.getOrderService(true).getByUserId(settings.getUserId(), new Callback<ArrayList<Order>>() {
            @Override
            public void success(ArrayList<Order> orders, Response response) {
                ArrayList<Order> activeOrders = new ArrayList<>();
                ArrayList<Order> finishedOrders = new ArrayList<>();
                ArrayList<Order> allOrders = new ArrayList<>();

                for (int i = 0; i < orders.size(); ++i) {
                    Order tempOrder = orders.get(i);
                    if (tempOrder.getContractor() == null || tempOrder.getContractor().getId() != settings.getUserId()) continue;
                    if (tempOrder.getStatus() != OrderType.FINISHED) {
                        activeOrders.add(tempOrder);
                    } else {
                        finishedOrders.add(tempOrder);
                    }
                }
                allOrders.addAll(activeOrders);
                allOrders.addAll(finishedOrders);

                mOrderAdapterActive = new OrderAdapter(allOrders, R.layout.list_item_order, getActivity(), Constants.MASTER_ORDER_ADAPTER);

                List<SectionedOrderAdapter.Section> sections = new ArrayList<>();
                sections.add(new SectionedOrderAdapter.Section(0, "Active"));
                sections.add(new SectionedOrderAdapter.Section(activeOrders.size(), "Finished"));

                SectionedOrderAdapter.Section[] dummy = new SectionedOrderAdapter.Section[sections.size()];
                SectionedOrderAdapter mSectionedAdapter = new
                        SectionedOrderAdapter(getActivity(), R.layout.recycledview_section, R.id.section_text, mOrderAdapterActive);
                mSectionedAdapter.setSections(sections.toArray(dummy));

                //Apply this adapter to the RecyclerView
                rvActive.setAdapter(mSectionedAdapter);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Some network error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void onResume(){
        super.onResume();
        fillDataClient();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ((TabsActivity)getActivity()).myMastersButton.setEnabled(true);
        ((TabsActivity)getActivity()).myClientsButton.setEnabled(false);
    }

}
