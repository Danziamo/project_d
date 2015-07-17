package com.mirsoft.easyfix.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.adapters.ReviewAdapter;
import com.mirsoft.easyfix.models.Review;

import java.util.ArrayList;

public class ReviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReviewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rvReviews);
        rv.setHasFixedSize(true);
        rv.setAdapter(new ReviewAdapter(getData(), R.layout.list_item_review, getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    private ArrayList<Review> getData() {
        ArrayList<Review> list = new ArrayList<>();

        for (int i = 0; i <= 50; ++i) {
            list.add(new Review(i + 1, "Vasya Pupkin", 3.5f, "Polnoe gavno fadsf aslkdf jahsdkjfhaskdjfhaksdjfh asjd fgjaksdhfashdkfhasdkfjhadskjf hakjsd hfkjasdhfkjasdhfkjadhf kjasdh fkjasdhf jaksdhf kjasdhf " + String.valueOf(i)));
        }

        return list;
    }
}
