package com.mirsoft.easyfix;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.api.OrderApi;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.ServiceGenerator;
import com.mirsoft.easyfix.networking.models.NOrder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MasterInfoFragment extends Fragment {

    MaterialEditText etPhone;
    MaterialEditText etFullName;
    MaterialEditText etClientAddress;
    MaterialEditText etClientPhone;
    MaterialEditText etClientDescription;
    private RatingBar mRaringBar;

    private User master;

    public MasterInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_info, container, false);

        master = (User)getActivity().getIntent().getSerializableExtra("MASTER");

        mRaringBar = (RatingBar)view.findViewById(R.id.rating_master);
        addListenerOnRatingBar();
        etPhone = (MaterialEditText)view.findViewById(R.id.etPhone);
        etFullName = (MaterialEditText)view.findViewById(R.id.etFullName);
        etClientAddress = (MaterialEditText)view.findViewById(R.id.etClientAddress);
        etClientPhone = (MaterialEditText)view.findViewById(R.id.etClientPhone);
        etClientDescription = (MaterialEditText)view.findViewById(R.id.etClientDescription);

        etPhone.setText(master.getPhone());
        etFullName.setText(master.getFullName());

        AppCompatButton btnSubmit = (AppCompatButton)view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrderRequest();
            }
        });

        return view;
    }

    private void addListenerOnRatingBar() {
    }


    private void createOrderRequest() {

        String address = etClientAddress.getText().toString();
        String phone = etClientPhone.getText().toString();
        String desctiption = etClientDescription.getText().toString();

        Settings settings = new Settings(getActivity());
        NOrder order = new NOrder();
        order.address = address;
        order.description = desctiption;
        order.contractor = master.getId();

        OrderApi api = ServiceGenerator.createService(OrderApi.class);
        api.createOrder(order, settings.getUserId(), new Callback<Order>() {
            @Override
            public void success(Order order, Response response) {
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }
}
