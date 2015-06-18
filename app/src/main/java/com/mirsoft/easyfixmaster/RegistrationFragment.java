package com.mirsoft.easyfixmaster;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mirsoft.easyfixmaster.api.UserApi;
import com.mirsoft.easyfixmaster.models.User;
import com.mirsoft.easyfixmaster.service.ServiceGenerator;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private MaterialEditText etlastName;
    private MaterialEditText etfirstName;
    private MaterialEditText etcountry;
    private MaterialEditText etcity;
    private MaterialEditText etphone;
    private MaterialEditText etpassword;
    private Button btnregistration;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RegistrationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        etlastName = (MaterialEditText)view.findViewById(R.id.etLastName);
        etfirstName = (MaterialEditText)view.findViewById(R.id.etFirstName);
        etcountry = (MaterialEditText)view.findViewById(R.id.etCountry);
        etcity = (MaterialEditText)view.findViewById(R.id.etCity);
        etphone = (MaterialEditText)view.findViewById(R.id.etPhone);
        etpassword = (MaterialEditText)view.findViewById(R.id.etPassword);

        btnregistration = (Button)view.findViewById(R.id.btnSubmit);
        btnregistration.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSubmit:
                makeSignUp();
        }
    }

    private void makeSignUp() {
        String firstName = etfirstName.getText().toString();
        String lastName = etlastName.getText().toString();
        String country = etcountry.getText().toString();
        String city = etcity.getText().toString();
        String phone = etphone.getText().toString();
        String password = etpassword.getText().toString();
        boolean isError = false;

        if (country.trim().isEmpty()) {
            etcountry.setError("Не может быть пустым");
            isError = true;
        }

        if (city.trim().isEmpty()) {
            etcity.setError("Не может быть пустым");
            isError = true;
        }

        if (phone.trim().isEmpty()) {
            etfirstName.setError("Не может быть пустым");
            isError = true;
        }

        if (lastName.trim().isEmpty()) {
            etlastName.setError("Не может быть пустым");
            isError = true;
        }

        if (firstName.trim().isEmpty()) {
            etfirstName.setError("Не может быть пустым");
            isError = true;
        }

        if (password.trim().isEmpty()) {
            etpassword.setError("Не может быть пустым");
            isError = true;
        }

        if (isError) return;

        User user = new User();
        user.setPhone(phone);
        user.setFirstName(firstName);
        user.setPassword(password);
        user.setLastName(lastName);
        final Settings settings = new Settings(getActivity());
        UserApi api = ServiceGenerator.createService(UserApi.class, settings);
        api.add(user, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                settings.setAccessToken(user.getToken());
                Intent intent = new Intent(getActivity(), FixNavigationDrawer.class);
                startActivity(intent);
                getActivity().finish();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}
