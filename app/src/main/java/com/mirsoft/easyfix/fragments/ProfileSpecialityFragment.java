package com.mirsoft.easyfix.fragments;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mirsoft.easyfix.ProfileActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.adapters.views.MySpecialityAdapter;
import com.mirsoft.easyfix.api.UserApi;
import com.mirsoft.easyfix.models.Specialty;
import com.mirsoft.easyfix.models.UserSpecialty;
import com.mirsoft.easyfix.networking.RestClient;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mbt on 7/28/15.
 */
public class ProfileSpecialityFragment extends BaseFragment implements View.OnClickListener{

    private int userId;
    private ArrayList<Specialty> specialtyList;
    private ArrayList<UserSpecialty> userSpecialtyList;
    private MySpecialityAdapter mySpecialityAdapter;

    private LinearLayout llProfileSpecialityContent;
    private ProgressBar progressBar;
    private RecyclerView rvUserSpecialties;
    private Button btnSubmit;


    public ProfileSpecialityFragment() {
    }

    public static ProfileSpecialityFragment newInstance(){
        return new ProfileSpecialityFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_speciality, container, false);

        llProfileSpecialityContent = (LinearLayout)view.findViewById(R.id.llProfileSpecialityContent);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        rvUserSpecialties = (RecyclerView)view.findViewById(R.id.rvUserSpecialties);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

       // FloatingActionButton btnAdd = (FloatingActionButton) view.findViewById(R.id.btnAdd);
        ((ProfileActivity)getActivity()).addNewProffestionBtn.setOnClickListener(this);

        Settings settings = new Settings(getActivity());
        userId = settings.getUserId();

        RestClient.getSpecialtyApi(false).getSpecialties(new Callback<ArrayList<Specialty>>() {
            @Override
            public void success(ArrayList<Specialty> specialties, Response response) {
                specialtyList = specialties;
                RestClient.createService(UserApi.class, true).getSpecialties(userId, new Callback<ArrayList< UserSpecialty>>(){

                    @Override
                    public void success(ArrayList<UserSpecialty> userSpecialties, Response response) {
                        userSpecialtyList = userSpecialties;
                        updateViews();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        showError(error);
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), "Error fetching specialties from server", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void updateViewsForEdit() {
        mySpecialityAdapter.setEditable(true);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    private void updateViews(){
        llProfileSpecialityContent.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        mySpecialityAdapter = new MySpecialityAdapter(userSpecialtyList, specialtyList, R.layout.list_item_profile_speciality);
        rvUserSpecialties.setAdapter(mySpecialityAdapter);
        rvUserSpecialties.setItemAnimator(new DefaultItemAnimator());
        rvUserSpecialties.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void saveUserSpecialities(){
        userSpecialtyList = mySpecialityAdapter.getItems();
        mySpecialityAdapter.setEditable(false);
        btnSubmit.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.btnAdd){
         //   if(mySpecialityAdapter.getItemCount() < 3) {
                mySpecialityAdapter.addItem(new UserSpecialty());
                btnSubmit.setVisibility(View.VISIBLE);
         //   }
        }else if(id == R.id.btnSubmit){

        }
    }
}
