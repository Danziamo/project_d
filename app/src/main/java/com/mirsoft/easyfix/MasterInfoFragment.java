package com.mirsoft.easyfix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.fragments.BaseFragment;
import com.mirsoft.easyfix.networking.api.OrderApi;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.networking.models.ApproveMasterOrder;
import com.mirsoft.easyfix.networking.models.NOrder;
import com.mirsoft.easyfix.views.RoundedTransformation;
import com.squareup.picasso.Picasso;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MasterInfoFragment extends BaseFragment {

    AppCompatEditText etClientAddress;
    AppCompatEditText etClientPhone;
    AppCompatEditText etClientDescription;
    private RatingBar mRaringBar;

    private final static String ARG_MODE = "ARG_MODE";
    private int mode;

    private User master;
    private Order mainOrder;

    public MasterInfoFragment() {
    }

    public static MasterInfoFragment newInstance(int mode) {
        MasterInfoFragment fragment = new MasterInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getInt(ARG_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_master_info, container, false);

        master = (User)getActivity().getIntent().getSerializableExtra("MASTER");
        mode = getActivity().getIntent().getIntExtra(ARG_MODE, 0);
        mainOrder = (Order)getActivity().getIntent().getSerializableExtra("ORDER");

        etClientPhone = (AppCompatEditText)view.findViewById(R.id.et_client_phone);
        etClientAddress = (AppCompatEditText)view.findViewById(R.id.et_client_address);
        etClientDescription = (AppCompatEditText)view.findViewById(R.id.et_client_description);

        AppCompatEditText etLastName = (AppCompatEditText)view.findViewById(R.id.et_last_name);
        AppCompatEditText etFirstName = (AppCompatEditText)view.findViewById(R.id.et_first_name);
        AppCompatRatingBar ratingBar = (AppCompatRatingBar)view.findViewById(R.id.ratingBar);
        TextView tvFeedbacks = (TextView)view.findViewById(R.id.tvFeedbacks);
        AppCompatEditText etPhone = (AppCompatEditText)view.findViewById(R.id.et_phone);
        ImageView ivProfileInfo = (ImageView)view.findViewById(R.id.profile_photo);

        ivProfileInfo.requestFocus();

        etLastName.setText(master.getLastName());
        etFirstName.setText(master.getFirstName());
        ratingBar.setRating(master.getRating());
        tvFeedbacks.setText(master.getReviewsCount() + " отзывов");
        etPhone.setText(master.getPhone());

        etClientAddress.requestFocus();

        RoundedTransformation transformation = new RoundedTransformation(10, 5);
        Picasso.with(getActivity())
                .load("https://scontent.xx.fbcdn.net/hphotos-xtf1/v/t1.0-9/10464122_669886999770868_7199669825191714119_n.jpg?oh=3d8b1edf292f4fef440b870a243a864e&oe=565BAFD9")
                .resize(150, 150)
                .centerCrop()
                .placeholder(R.drawable.no_avatar)
                .error(R.drawable.no_avatar)
                .transform(transformation)
                .into(ivProfileInfo);

        AppCompatButton btnSubmit = (AppCompatButton)view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createOrderRequest();
            }
        });

        tvFeedbacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),CommentActivity.class));
            }
        });

        if (mode == Constants.PENDING_MASTERS_LIST) {
            btnSubmit.setText(getActivity().getResources().getString(R.string.take_master));
            etLastName.setEnabled(false);
            etFirstName.setEnabled(false);
            etPhone.setEnabled(false);
        }

        return view;
    }

    private void createOrderRequest() {

        String address = etClientAddress.getText().toString();
        String phone = etClientPhone.getText().toString();
        String description = etClientDescription.getText().toString();

        showProgress(true, "Подождите...", "Выполняется запрос");
        Settings settings = new Settings(getActivity());
        if (mode == Constants.SPECIALTY_MASTER_LIST) {
            NOrder order = new NOrder();
            order.address = address;
            order.description = description;
            order.contractor = master.getId();
            RestClient.getOrderService(false).createOrder(order, settings.getUserId(), new Callback<Order>() {
                @Override
                public void success(Order order, Response response) {
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (mode == Constants.PENDING_MASTERS_LIST) {
            ApproveMasterOrder order = new ApproveMasterOrder();
            order.contractor = master.getId();
            RestClient.getOrderService(false).setMaster(order, settings.getUserId(), mainOrder.getId(), new Callback<Object>() {
                @Override
                public void success(Object o, Response response) {
                    Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    Toast.makeText(getActivity(), "Failure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
