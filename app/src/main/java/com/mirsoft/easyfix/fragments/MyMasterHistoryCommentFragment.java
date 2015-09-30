package com.mirsoft.easyfix.fragments;

import android.graphics.ComposePathEffect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mirsoft.easyfix.ClientOrderDetailsActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.models.Comment;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.models.Review;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.models.UserSpecialty;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.networking.api.UserApi;
import com.mirsoft.easyfix.utils.HelperUtils;
import com.mirsoft.easyfix.utils.Singleton;
import com.mirsoft.easyfix.views.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyMasterHistoryCommentFragment extends BaseFragment {

    View view;

    private Integer[] list = new Integer[]{2};

    private int userId;
    private String userPassword;
    private User mUser;

    private ProgressBar progressBar;
    private LinearLayout llProfileInfoContent;

    private EditText etLastName;
    private EditText etFirstName;
    private RatingBar ratingBar;
    private TextView tvFeedbacks;
    private EditText etPhone;
    private EditText etProfession;
    private EditText etLicense;
    private EditText etExperiance;

    private Button btnComment;
    private ImageView ivProfileInfo;

    Singleton singleton;

    Settings settings;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_master_history_comment, container, false);
        singleton = Singleton.getInstance(getActivity());

        progressBar = (ProgressBar) view.findViewById(R.id.history_progress_bar);
        llProfileInfoContent = (LinearLayout) view.findViewById(R.id.linearLayout_history);

        etLastName = (EditText) view.findViewById(R.id.et_last_name);
        etFirstName = (EditText) view.findViewById(R.id.et_first_name);
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        tvFeedbacks = (TextView) view.findViewById(R.id.tvFeedbacks);

        etProfession = (EditText) view.findViewById(R.id.et_profession);
        etLicense = (EditText) view.findViewById(R.id.et_license);
        etExperiance = (EditText) view.findViewById(R.id.et_experience);
        ivProfileInfo = (ImageView) view.findViewById(R.id.profile_photo);
        btnComment = (Button)view.findViewById(R.id.btnCommet);

        settings = new Settings(getActivity());

        showProgress(true, "Ожидайте", "Загружаю данные");

        getSpecialtyOptions(singleton.clientSelectedOrder);

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.comment_custom_layout, null);

                new MaterialDialog.Builder(getActivity())
                        .customView(dialogView, false)
                        .positiveText(R.string.submit)
                        .negativeText(R.string.btn_cancel)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                AppCompatEditText edit = (AppCompatEditText) dialogView.findViewById(R.id.et_comment);
                                sendComment(edit.getText().toString());
                            }

                            @Override
                            public void onNegative(MaterialDialog dialog) {
                                super.onNegative(dialog);
                            }
                        })
                        .show();
            }
        });


        ((ClientOrderDetailsActivity)getActivity()).create_order_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        ((ClientOrderDetailsActivity)getActivity()).create_order_toolbar.setTitle(getResources().getString(R.string.my_order_master_history));

        return view;
    }

    public void sendComment(String text){

     //   EditText commentText = (EditText)(getActivity().getLayoutInflater().inflate(R.layout.comment_custom_layout,null)).findViewById(R.id.it_comment);

        Comment comment = new Comment();
        comment.setUser(singleton.clientSelectedOrder.getClient().getId());
        comment.setRating(ratingBar.getRating());
        comment.setDescription(text);

        RestClient.getOrderService(false).addComment(comment, singleton.clientSelectedOrder.getId(),new Callback<Object>() {
            @Override
            public void success(Object object, Response response) {
                Toast.makeText(getActivity(),"Коммент отправлен",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getActivity(),"Failure : send comment",Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        });
    }
//
//    private void getUpdatedOrder(){
//        RestClient.getOrderService(false).getById(settings.getUserId(),singleton.clientSelectedOrder.getId(), new Callback<Order>() {
//            @Override
//            public void success(Order order, Response response) {
//                updateCommonViews(order);
//                getSpecialtyOptions(order);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Toast.makeText(getActivity(), "Error:loading updated information", Toast.LENGTH_SHORT);
//                error.printStackTrace();
//            }
//        });
//    }

    private void getSpecialtyOptions(final Order order){
        RestClient.getUserService(false).getSpecialties(singleton.clientSelectedOrder.getContractor().getId(), new Callback<ArrayList<UserSpecialty>>() {
            @Override
            public void success(ArrayList<UserSpecialty> userSpecialties, Response response) {
                hideProgress();
                updateCommonViews(order);
                updateView(userSpecialties, order);
            }

            @Override
            public void failure(RetrofitError error) {
                hideProgress();
                Toast.makeText(getActivity(), "Error:loading master information", Toast.LENGTH_SHORT);
                error.printStackTrace();
            }
        });
    }

    private void updateCommonViews(Order order){
        llProfileInfoContent.setVisibility(View.VISIBLE);
        etLastName.setText(order.getContractor().getLastName());
        etFirstName.setText(order.getContractor().getFirstName());
        ratingBar.setRating(order.getContractor().getRating());
        tvFeedbacks.setText(getResources().getQuantityString(R.plurals.feedback_count, order.getContractor().getReviewsCount(), order.getContractor().getReviewsCount()));
        etPhone.setText(order.getContractor().getPhone());

        RoundedTransformation transformation = new RoundedTransformation(10, 5);

        HelperUtils.getUserPhotoRequestCreator(getActivity(), order.getContractor().getPicture())
                .resize(150, 150)
                .transform(transformation)
                .into(ivProfileInfo);
    }

    private void updateView(ArrayList<UserSpecialty> specialties,Order order){
        for(int i = 0; i < specialties.size();i++)
        {
            if(order.getSpecialty().getId() == specialties.get(i).getId()){
                for(int j = 0; j < singleton.specialtyList.size();j++){
                    if(specialties.get(i).getId() == singleton.specialtyList.get(j).getId())
                        etProfession.setText(singleton.specialtyList.get(j).getName());
                }
                if(specialties.get(i).isCertified()) {
                    etLicense.setText("Лицензия");
                    etLicense.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.check, 0);
                }
                else {
                    etLicense.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.false_lic, 0);
                    etLicense.setText("Лицензия");
                }
                etExperiance.setText("6 лет");
            }
        }
    }

}
