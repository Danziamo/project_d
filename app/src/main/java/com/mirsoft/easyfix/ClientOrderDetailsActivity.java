package com.mirsoft.easyfix;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class ClientOrderDetailsActivity extends AppCompatActivity {

    public Toolbar create_order_toolbar;
    public Spinner servicesSpinner;
    public AppCompatRatingBar ratingBar;
    public EditText orderAddress;
    public EditText orderPhone;
    public EditText orderDescription;
    public TextView mastersRequests;
    public TextView orderNotification;
    public Button orderBtnChange;
    public Button orderBtnCancel;
    public Button orderBtnLocate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        create_order_toolbar = (Toolbar)findViewById(R.id.order_create_toolbar);
        setSupportActionBar(create_order_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

       // servicesSpinner = (Spinner)findViewById(R.id.services_spinner);
//        ratingBar = (AppCompatRatingBar)findViewById(R.id.llratingbar);
//        orderAddress = (EditText)findViewById(R.id.etAddress);
//        orderPhone = (EditText)findViewById(R.id.etPhone);
//        orderDescription = (EditText)findViewById(R.id.etDescription);
//        mastersRequests = (TextView)findViewById(R.id.request_from_masters);
//        orderNotification = (TextView)findViewById(R.id.order_notification);
//        orderBtnChange = (Button)findViewById(R.id.btnChange);
//        orderBtnCancel = (Button)findViewById(R.id.btnCancel);
//        orderBtnLocate = (Button)findViewById(R.id.btnLocate);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CreateBasicOrderFragment())
                    .commit();
        }
    }

    public void onBackButtonClicked(){
        super.onBackPressed();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

            }
        }
    }*/
}
