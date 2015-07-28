package com.mirsoft.easyfix.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mirsoft.easyfix.ClientOrderDetailsActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.TabsActivity;
import com.mirsoft.easyfix.adapters.MasterAdapter;
import com.mirsoft.easyfix.api.UserApi;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.utils.RecyclerViewSimpleDivider;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MasterListFragment extends Fragment {

    private RecyclerView rvMaster;
    private MasterAdapter rvAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_master_list, container, false);

        rvMaster = (RecyclerView)view.findViewById(R.id.rvMasters);
        rvMaster.addItemDecoration(new RecyclerViewSimpleDivider(getActivity()));
        rvMaster.setHasFixedSize(true);
        rvAdapter = new MasterAdapter(new ArrayList<User>(), R.layout.list_item_master, getActivity());
        rvMaster.setAdapter(rvAdapter);
        rvMaster.setItemAnimator(new DefaultItemAnimator());
        rvMaster.setLayoutManager(new LinearLayoutManager(getActivity()));

        FloatingActionButton btnCreate = (FloatingActionButton)getActivity().findViewById(R.id.btnSwitch);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClientOrderDetailsActivity.class);
                startActivity(intent);
            }
        });
        getMastersList();

        return view;
    }

    private void getMastersList() {
        UserApi api = RestClient.createService(UserApi.class);
        api.getAllByQuery("plumber", new Callback<ArrayList<User>>() {
            @Override
            public void success(ArrayList<User> users, Response response) {
                showList(users);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void showList(ArrayList<User> users) {
        rvAdapter.setDataset(users);
    }
}
