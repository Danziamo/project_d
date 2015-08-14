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
    public AppCompatRatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        create_order_toolbar = (Toolbar)findViewById(R.id.order_create_toolbar);
        setSupportActionBar(create_order_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CreateBasicOrderFragment())
                    .commit();
        }
    }

    public void onBackButtonClicked(){
        super.onBackPressed();
    }

}
