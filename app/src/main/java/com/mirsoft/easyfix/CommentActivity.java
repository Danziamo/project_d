package com.mirsoft.easyfix;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.mirsoft.easyfix.adapters.ReviewAdapter;
import com.mirsoft.easyfix.models.Review;
import com.mirsoft.easyfix.networking.RestClient;
import com.mirsoft.easyfix.utils.Singleton;
import com.mirsoft.easyfix.views.RecyclerViewSimpleDivider;

import java.util.ArrayList;

public class CommentActivity extends AppCompatActivity {
    Toolbar toolbar;
    Singleton singleton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        toolbar = (Toolbar)findViewById(R.id.order_create_toolbar);
        setSupportActionBar(toolbar);

        singleton = Singleton.getInstance(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        RecyclerView rv = (RecyclerView)findViewById(R.id.rvReviews);
        rv.setHasFixedSize(true);
        rv.setAdapter(new ReviewAdapter(getData(), R.layout.list_item_review, this));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new RecyclerViewSimpleDivider(this));
    }

    private ArrayList<Review> getData() {
        ArrayList<Review> list = new ArrayList<>();

        for (int i = 0; i <= 50; ++i) {
            list.add(new Review(i + 1, "Vasya Pupkin", 3.5f, "Polnoe gavno fadsf aslkdf jahsdkjfhaskdjfhaksdjfh asjd fgjaksdhfashdkfhasdkfjhadskjf hakjsd hfkjasdhfkjasdhfkjadhf kjasdh fkjasdhf jaksdhf kjasdhf " + String.valueOf(i)));
        }

        //RestClient.getOrderService(false).getReviewsByOrderId(singleton.order);

        return list;
    }

}
