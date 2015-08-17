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
import com.mirsoft.easyfix.adapters.MySpecialityAdapter;
import com.mirsoft.easyfix.models.SpecialtyDetails;
import com.mirsoft.easyfix.networking.api.UserApi;
import com.mirsoft.easyfix.models.Specialty;
import com.mirsoft.easyfix.models.UserSpecialty;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.utils.Singleton;

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
    private ArrayList<SpecialtyDetails> userSpecialtyList;
    private MySpecialityAdapter mySpecialityAdapter;

    private LinearLayout llProfileSpecialityContent;
    private ProgressBar progressBar;
    private RecyclerView rvUserSpecialties;
    private Button btnSubmit;

    Singleton singleton;

    Settings settings;

    public ProfileSpecialityFragment() {
    }

    public static ProfileSpecialityFragment newInstance(){
        return new ProfileSpecialityFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_speciality, container, false);

        singleton = Singleton.getInstance(getActivity());

        llProfileSpecialityContent = (LinearLayout)view.findViewById(R.id.llProfileSpecialityContent);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        rvUserSpecialties = (RecyclerView)view.findViewById(R.id.rvUserSpecialties);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(this);

        ((ProfileActivity)getActivity()).addNewProffestionBtn.setOnClickListener(this);

        userSpecialtyList = singleton.currentUser.getUserSpecialties() ;
        updateViews();

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

        ArrayList<SpecialtyDetails> filteredList = new ArrayList<>();
        settings = new Settings(getActivity());
        for(int i = 0; i<userSpecialtyList.size(); i++){
            if(userSpecialtyList.get(i).getUserId() == settings.getUserId()) filteredList.add(userSpecialtyList.get(i));
        }

        mySpecialityAdapter = new MySpecialityAdapter(filteredList, singleton.specialtyList, R.layout.list_item_profile_speciality);
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
            if(mySpecialityAdapter.getItemCount() < 3) {
                mySpecialityAdapter.addItem(new SpecialtyDetails());
                btnSubmit.setVisibility(View.VISIBLE);
            }
        }else if(id == R.id.btnSubmit){

        }
    }
}
