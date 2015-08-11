package com.mirsoft.easyfix.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mirsoft.easyfix.CommentActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.Settings;
import com.mirsoft.easyfix.networking.api.UserApi;
import com.mirsoft.easyfix.models.User;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.views.RoundedTransformation;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by mbt on 7/27/15.
 */
public class ProfileInfoFragment extends BaseFragment implements View.OnClickListener{

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
    private Button btnSubmit;
    private ImageView ivProfileInfo;

    private boolean isEditable = false;


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
        ivProfileInfo = (ImageView) view.findViewById(R.id.profile_photo);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(this);

        RoundedTransformation transformation = new RoundedTransformation(10, 5);

        Picasso.with(getActivity())
                .load("https://scontent.xx.fbcdn.net/hphotos-xtf1/v/t1.0-9/10464122_669886999770868_7199669825191714119_n.jpg?oh=3d8b1edf292f4fef440b870a243a864e&oe=565BAFD9")
                .resize(150, 150)
                .centerCrop()
                .placeholder(R.drawable.no_avatar)
                .error(R.drawable.no_avatar)
                .transform(transformation)
                .into(ivProfileInfo);

        ivProfileInfo.setOnClickListener(this);
        registerForContextMenu(ivProfileInfo);

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

        tvFeedbacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CommentActivity.class));
            }
        });

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
        if(isEditable){
            return; //@TODO change or hide action bar image if editable
        }
        etLastName.setEnabled(true);
        if(etLastName.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        etFirstName.setEnabled(true);
        etPassword.setEnabled(true);
        btnSubmit.setVisibility(View.VISIBLE);
        isEditable = true;
    }

    public void disableViews(){
        etLastName.setEnabled(false);
        etFirstName.setEnabled(false);
        etPassword.setEnabled(false);
        btnSubmit.setVisibility(View.GONE);
        isEditable = false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btnSubmit){
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
        }else if (id == R.id.profile_photo){
            getActivity().openContextMenu(ivProfileInfo);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_profile_photo, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.option_choose_from_gallery: loadImageFromGallery(); return true;
            case R.id.option_take_photo: captureImage(); return true;
            default: return super.onContextItemSelected(item);
        }
    }


    // upload photo





    String imagePath;
    Uri imageUri;
    File imageFile;

    private static int RESULT_CHOOSE_FROM_GALLERY = 1;
    private static int RESULT_CAPTURE_IMAGE = 2;

    private void loadImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_CHOOSE_FROM_GALLERY);
    }

    private void captureImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageUri = Uri.fromFile(getJpgImageFile());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, RESULT_CAPTURE_IMAGE);
    }

    private File getPngImageFile(){
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            return File.createTempFile("test", ".png", storageDir);
        }catch (Exception e) {
            return new File(Environment.getExternalStorageDirectory(), "test.png");
        }
    }

    private File getJpgImageFile(){
        return new File(Environment.getExternalStorageDirectory(), "test.jpg");
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        try {
//            return File.createTempFile("test", ".jpg", storageDir);
//        }catch (Exception e) {
//            return new File(Environment.getExternalStorageDirectory(), "test.jpg");
//        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{

            if(requestCode == RESULT_CHOOSE_FROM_GALLERY ) {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    imagePath = cursor.getString(columnIndex);

                    showProgress(true, "Upload start", "Upload start");
                    convertImageToByte();

                } else {
                    Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
            }else if(requestCode == RESULT_CAPTURE_IMAGE){
                if(resultCode == Activity.RESULT_OK){
                    imagePath = imageUri.getPath();
                    showProgress(true, "Upload start", "Upload start");
                    convertImageToByte();
                }else{
                    Toast.makeText(getActivity(), "User cancelled image capture", Toast.LENGTH_LONG).show();
                }
            }
        }catch (Exception e){
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void convertImageToByte(){
        new AsyncTask<Void, Void, String>(){
            @Override
            protected String doInBackground(Void... params) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] imageBytes = stream.toByteArray();

                try {
                    imageFile = getPngImageFile();

                    FileOutputStream fos = new FileOutputStream(imageFile);
                    fos.write(imageBytes);
                    fos.flush();
                    fos.close();
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Something wrong, try again", Toast.LENGTH_LONG).show();
                }

                return "";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                triggerUpload();
            }
        }.execute();
    }

    private void triggerUpload(){
        try {
            uploadImage();
        }catch (Exception e){
            Toast.makeText(getActivity(), "Upload error" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void uploadImage() throws Exception{

        tmpChangeImage();

        hideProgress();

//        new AsyncTask<Void, Void, Boolean>(){
//
//            @Override
//            protected Boolean doInBackground(Void... params) {
//                try {
//                    ImageUploadClient.upload(imageFile);
//                    return true;
//                }catch (Exception e){
//                    Log.d("UPLOAD_ERROR", e.toString());
//                    return false;
//                }
//            }
//
//            @Override
//            protected void onPostExecute(Boolean result) {
//                super.onPostExecute(result);
//                if(result == false){
//                    Toast.makeText(getActivity(), "Upload Error", Toast.LENGTH_LONG).show();
//                }else{
//                    Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_LONG).show();
//                }
//                hideProgress();
//            }
//        }.execute();
    }

    //@TODO
    private void tmpChangeImage(){
        RoundedTransformation transformation = new RoundedTransformation(10, 5);

        String url = "file://" + imageFile.getAbsolutePath();

        Picasso.with(getActivity())
                .load(url)
                .resize(150, 150)
                .centerCrop()
                .placeholder(R.drawable.no_avatar)
                .error(R.drawable.no_avatar)
                .transform(transformation)
                .into(ivProfileInfo);
    }

    //end upload photo
}
