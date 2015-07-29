package com.mirsoft.easyfix.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;
import com.mirsoft.easyfix.ProfileActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.api.UserApi;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.RestClient;
import com.rengwuxian.materialedittext.MaterialEditText;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mbt on 7/27/15.
 */
public class ProfileInfoFragment extends BaseFragment implements View.OnClickListener{

    MaterialDialog dialog;
    MaterialEditText etSpeciality;
    MaterialEditText etLicense;

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
    private EditText etPassword;
    private FlatButton btnSubmit;


    public ProfileInfoFragment(){

    }

    public static ProfileInfoFragment newInstance(){
        return new ProfileInfoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);

        Settings settings = new Settings(getActivity());
        userId = settings.getUserId();
        userPassword = settings.getPassword();

        UserApi api = RestClient.createService(UserApi.class);

        api.getById(userId, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                mUser = user;
                updateViews(user);
            }

            @Override
            public void failure(RetrofitError error) {
                showError(error);
            }
        });

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        llProfileInfoContent = (LinearLayout) view.findViewById(R.id.ll_profile_info_content);
        etLastName = (EditText) view.findViewById(R.id.et_last_name);
        etFirstName = (EditText) view.findViewById(R.id.et_first_name);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        tvFeedbacks = (TextView) view.findViewById(R.id.tvFeedbacks);
        etPhone = (EditText) view.findViewById(R.id.et_phone);
        etPassword = (EditText) view.findViewById(R.id.et_password);
        btnSubmit = (FlatButton) view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);
//        initSpecialityDialog();

//        etSpeciality = (MaterialEditText)view.findViewById(R.id.etSpeciality);
//        etSpeciality.setInputType(InputType.TYPE_NULL);
//
//        etSpeciality.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) showSpecialityDialog(true);
//                else showSpecialityDialog(false);
//            }
//        });

//        etLicense = (MaterialEditText)view.findViewById(R.id.etLicense);



        return view;
    }


    private void updateViews(User user){
        progressBar.setVisibility(View.GONE);
        llProfileInfoContent.setVisibility(View.VISIBLE);
        etLastName.setText(user.getLastName());
        etFirstName.setText(user.getFirstName());
        ratingBar.setRating(user.getRating());
        tvFeedbacks.setText(getResources().getQuantityString(R.plurals.feedback_count, user.getReviewsCount(), user.getReviewsCount()));
        etPhone.setText(user.getPhone());
        etPassword.setText(userPassword);
    }

    @Override
    public void updateViewsForEdit(){
        etLastName.setEnabled(true);
        etFirstName.setEnabled(true);
        etPassword.setEnabled(true);
        btnSubmit.setVisibility(View.VISIBLE);
    }

    public void disableViews(){
        etLastName.setEnabled(false);
        etFirstName.setEnabled(false);
        etPassword.setEnabled(false);
        btnSubmit.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btnSubmit){
            UserApi api = RestClient.createService(UserApi.class);
            mUser.setLastName(etLastName.getText().toString());
            mUser.setFirstName(etFirstName.getText().toString());

            final String newPassword = etPassword.getText().toString();
            mUser.setPassword(newPassword);

            showProgress(true, "Сохранение", "Пожалуйста подождите");


            api.save(mUser.getId(), mUser, new Callback<User>() {
                @Override
                public void success(User user, Response response) {
                    mUser = user;
                    Settings settings = new Settings(getActivity());
                    settings.setPassword(newPassword);
                    userPassword = newPassword;
                    hideProgress();
                    disableViews();
                }

                @Override
                public void failure(RetrofitError error) {
                    hideProgress();
                    showError(error);

                }
            });
        }
    }



//    private void initSpecialityDialog() {
//        String[] specs = new String[] {"Slave", "MegaSlave", "HyperSlave"};
//    }
//
//    private void showSpecialityDialog(final boolean show) {
//        String[] specs = new String[] {"Slave", "MegaSlave", "HyperSlave"};
//        if(show) {
//            MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity())
//                    .title("Opachki")
//                    .items(specs)
//                    .itemsCallbackMultiChoice(list, new MaterialDialog.ListCallbackMultiChoice() {
//                        @Override
//                        public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
//                            /**
//                             * If you use alwaysCallMultiChoiceCallback(), which is discussed below,
//                             * returning false here won't allow the newly selected check box to actually be selected.
//                             * See the limited multi choice dialog example in the sample project for details.
//                             **/
//                            list = which;
//                            etSpeciality.setText(String.valueOf(which.length));
//                            etLicense.requestFocus();
//                            return true;
//                        }
//                    })
//                    .positiveText("Choose");
//            dialog = builder.build();
//            dialog.show();
//        } else {
//            if (dialog != null)
//                dialog.dismiss();
//        }
//
//
//    }
}
