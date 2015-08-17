package com.mirsoft.easyfix.fragments;


import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.adapters.MasterAdapter;
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.networking.api.UserApi;
import com.mirsoft.easyfix.models.Specialty;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.views.RecyclerViewSimpleDivider;
import com.mirsoft.easyfix.utils.Singleton;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class MasterListFragment extends BaseFragment {

    private RecyclerView rvMaster;
    private MasterAdapter rvAdapter;
    private AppCompatSpinner spinner;

    private ArrayList<User> usersList;
    private int mMode;
    private Order order;

    private final static String USERS_KEY = "USERS_KEY";
    private final static String MODE_KEY = "MODE_KEY";
  //  private ArrayList<Specialty> specialtyList;
    Singleton dc;

    MaterialDialog dialog;

    public static MasterListFragment newInstance(ArrayList<User> users, int mode) {
        MasterListFragment fragment = new MasterListFragment();
        Bundle args = new Bundle();
        args.putSerializable(USERS_KEY, users);
        args.putInt(MODE_KEY, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usersList = new ArrayList<>();
        if (getArguments() != null) {
            usersList = (ArrayList<User>) getArguments().getSerializable(USERS_KEY);
            mMode = getArguments().getInt(MODE_KEY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_master_list, container, false);
        dc = Singleton.getInstance(getActivity());

        dc.currentSelectedTabPage = 1;
        order = dc.clientSelectedOrder;

        LinearLayout spinnerLayout = (LinearLayout)view.findViewById(R.id.spinner_layout);
        spinner = (AppCompatSpinner)view.findViewById(R.id.spinner);

        rvMaster = (RecyclerView)view.findViewById(R.id.rvMasters);
        rvMaster.addItemDecoration(new RecyclerViewSimpleDivider(getActivity()));
        rvMaster.setHasFixedSize(true);

        rvAdapter = new MasterAdapter(usersList, R.layout.list_item_master, getActivity(), mMode);

        rvMaster.setAdapter(rvAdapter);
        rvMaster.setItemAnimator(new DefaultItemAnimator());
        rvMaster.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (mMode == Constants.PENDING_MASTERS_LIST) {
            spinnerLayout.setVisibility(View.GONE);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (dc.specialtyList.size() > 0) {
                    dc.selectedSpecialty = dc.specialtyList.get(position);
                    getMastersList(dc.specialtyList.get(position).getSlug());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        getSpecialties();

        return view;
    }

    private void getMastersList(String specialty) {

        showProgress(true, "Загрузка...", "Пожалуйста подождите");

        UserApi api = RestClient.createService(UserApi.class);
        api.getAllByQuery(specialty, new Callback<ArrayList<User>>() {
            @Override
            public void success(ArrayList<User> users, Response response) {
                hideProgress();
                showList(users);
            }

            @Override
            public void failure(RetrofitError error) {
                hideProgress();
            }
        });
    }

    public void getSpecialties() {
        RestClient.getSpecialtyApi(false).getSpecialties(new Callback<ArrayList<Specialty>>() {
            @Override
            public void success(ArrayList<Specialty> specialties, Response response) {
                dc.specialtyList = specialties;
                ArrayAdapter<Specialty> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dc.specialtyList);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                if (specialties.size() > 0)
                    spinner.setSelection(0);
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Error fetching specialties from server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showList(ArrayList<User> users) {
        rvAdapter.setDataset(users);
    }
}
