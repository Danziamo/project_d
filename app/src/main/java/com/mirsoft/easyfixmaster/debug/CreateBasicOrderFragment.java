package com.mirsoft.easyfixmaster.debug;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.models.Specialty;
import com.mirsoft.easyfix.utils.Singleton;
import com.rengwuxian.materialedittext.MaterialEditText;


public class CreateBasicOrderFragment extends Fragment {

    EditText etadress;
    EditText etPhone;
    EditText etdesciption;
    Button btnLocate;
    private AppCompatRatingBar mRatingbar;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Singleton dc;

    private Spinner spinner;

    public static CreateBasicOrderFragment newInstance(String param1, String param2) {
        CreateBasicOrderFragment fragment = new CreateBasicOrderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CreateBasicOrderFragment() {
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
        View view = inflater.inflate(R.layout.fragment_create_basic_order,container, false);
        dc = Singleton.getInstance(getActivity());

      /*  etadress = (EditText) view.findViewById(R.id.etadress);
        etPhone = (EditText) view.findViewById(R.id.etPhone);
        etdesciption = (EditText) view.findViewById(R.id.etDescription);*/
        mRatingbar = (AppCompatRatingBar) view.findViewById(R.id.llratingbar);
        Button btnLocate = (Button)view.findViewById(R.id.btnLocate);

        Drawable progress = mRatingbar.getProgressDrawable();
        DrawableCompat.setTint(progress, Color.BLACK);

       // AppCompatButton btnLocate = (AppCompatButton)getView().findViewById(R.id.btnLocate);
       /* btnLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        // Inflate the layout for this fragment


        spinner = (Spinner)view.findViewById(R.id.services_spinner);

        ArrayAdapter<Specialty> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, dc.specialtyList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        return view;
    }





}
