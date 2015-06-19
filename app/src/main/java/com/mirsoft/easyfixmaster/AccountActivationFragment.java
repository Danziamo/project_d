package com.mirsoft.easyfixmaster;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.mirsoft.easyfixmaster.api.SessionApi;
import com.mirsoft.easyfixmaster.models.Session;
import com.mirsoft.easyfixmaster.service.ServiceGenerator;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccountActivationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccountActivationFragment extends Fragment {

    MaterialEditText et_code_sms;
    Button btn_authorization;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccountActivationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccountActivationFragment newInstance(String param1, String param2) {
        AccountActivationFragment fragment = new AccountActivationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AccountActivationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_account_activation, container, false);

        et_code_sms = (MaterialEditText)view.findViewById(R.id.et_code_sms);
        btn_authorization = (Button)view.findViewById(R.id.btn_authorization);
        btn_authorization.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });

        return view;
    }

    private void openDrawer(){
        Intent intent = new Intent(getActivity(), FixNavigationDrawer.class);
        startActivity(intent);
        getActivity().finish();
    }

}
