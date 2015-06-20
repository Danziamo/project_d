package com.mirsoft.easyfixmaster.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mirsoft.easyfixmaster.R;
import com.mirsoft.easyfixmaster.utils.RecyclerViewSimpleDivider;
import com.mirsoft.easyfixmaster.adapters.OrderAdapter;
import com.mirsoft.easyfixmaster.models.Order;
import com.mirsoft.easyfixmaster.models.Specialty;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends Fragment {
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
     * @return A new instance of fragment TestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TestFragment() {
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
        View view = inflater.inflate(R.layout.fragment_test, container, false);

        RecyclerView rv = (RecyclerView)view.findViewById(R.id.rvOrders);
        rv.addItemDecoration(new RecyclerViewSimpleDivider(getActivity()));
        rv.setHasFixedSize(true);
        rv.setAdapter(new OrderAdapter(getData(), R.layout.list_item_order, getActivity()));
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    public ArrayList<Order> getData() {
        ArrayList<Order> list = new ArrayList<>();
        Specialty plomber = new Specialty(1, "плотник", "plumber", "slave");
        Specialty electric = new Specialty(2, "электрик", "electricity", null);
        Specialty repair = new Specialty(3, "ремонт", "repair", null);
        Specialty decorator = new Specialty(4, "декоратор", "decorator", null);
        list.add(new Order(1, "tam", "to", plomber));
        list.add(new Order(2, "tut", "eto", electric));
        list.add(new Order(3, "kak", "tak", repair));
        list.add(new Order(4, "nu", "tak", decorator));
        list.add(new Order(5, "nu", "tak", decorator));
        list.add(new Order(7, "nu", "tak", plomber));
        list.add(new Order(11, "nu", "tak", repair));
        list.add(new Order(23, "nu", "tak", decorator));
        list.add(new Order(41, "nu", "tak", decorator));
        list.add(new Order(11, "nu", "tak", electric));
        list.add(new Order(55, "nu", "tak", electric));

        list.add(new Order(1, "tam", "to", plomber));
        list.add(new Order(2, "tut", "eto", electric));
        list.add(new Order(3, "kak", "tak", repair));
        list.add(new Order(4, "nu", "tak", decorator));
        list.add(new Order(5, "nu", "tak", decorator));
        list.add(new Order(7, "nu", "tak", plomber));
        list.add(new Order(11, "nu", "tak", repair));
        list.add(new Order(23, "nu", "tak", decorator));
        list.add(new Order(41, "nu", "tak", decorator));
        list.add(new Order(11, "nu", "tak", electric));
        list.add(new Order(55, "nu", "tak", electric));
        list.add(new Order(1, "tam", "to", plomber));
        list.add(new Order(2, "tut", "eto", electric));
        list.add(new Order(3, "kak", "tak", repair));
        list.add(new Order(4, "nu", "tak", decorator));
        list.add(new Order(5, "nu", "tak", decorator));
        list.add(new Order(7, "nu", "tak", plomber));
        list.add(new Order(11, "nu", "tak", repair));
        list.add(new Order(23, "nu", "tak", decorator));
        list.add(new Order(41, "nu", "tak", decorator));
        list.add(new Order(11, "nu", "tak", electric));
        list.add(new Order(55, "nu", "tak", electric));
        list.add(new Order(1, "tam", "to", plomber));
        list.add(new Order(2, "tut", "eto", electric));
        list.add(new Order(3, "kak", "tak", repair));
        list.add(new Order(4, "nu", "tak", decorator));
        list.add(new Order(5, "nu", "tak", decorator));
        list.add(new Order(7, "nu", "tak", plomber));
        list.add(new Order(11, "nu", "tak", repair));
        list.add(new Order(23, "nu", "tak", decorator));
        list.add(new Order(41, "nu", "tak", decorator));
        list.add(new Order(11, "nu", "tak", electric));
        list.add(new Order(55, "nu", "tak", electric));
        list.add(new Order(1, "tam", "to", plomber));
        list.add(new Order(2, "tut", "eto", electric));
        list.add(new Order(3, "kak", "tak", repair));
        list.add(new Order(4, "nu", "tak", decorator));
        list.add(new Order(5, "nu", "tak", decorator));
        list.add(new Order(7, "nu", "tak", plomber));
        list.add(new Order(11, "nu", "tak", repair));
        list.add(new Order(23, "nu", "tak", decorator));
        list.add(new Order(41, "nu", "tak", decorator));
        list.add(new Order(11, "nu", "tak", electric));
        list.add(new Order(55, "nu", "tak", electric));
        list.add(new Order(1, "tam", "to", plomber));
        list.add(new Order(2, "tut", "eto", electric));
        list.add(new Order(3, "kak", "tak", repair));
        list.add(new Order(4, "nu", "tak", decorator));
        list.add(new Order(5, "nu", "tak", decorator));
        list.add(new Order(7, "nu", "tak", plomber));
        list.add(new Order(11, "nu", "tak", repair));
        list.add(new Order(23, "nu", "tak", decorator));
        list.add(new Order(41, "nu", "tak", decorator));
        list.add(new Order(11, "nu", "tak", electric));
        list.add(new Order(55, "nu", "tak", electric));
        list.add(new Order(1, "tam", "to", plomber));
        list.add(new Order(2, "tut", "eto", electric));
        list.add(new Order(3, "kak", "tak", repair));
        list.add(new Order(4, "nu", "tak", decorator));
        list.add(new Order(5, "nu", "tak", decorator));
        list.add(new Order(7, "nu", "tak", plomber));
        list.add(new Order(11, "nu", "tak", repair));
        list.add(new Order(23, "nu", "tak", decorator));
        list.add(new Order(41, "nu", "tak", decorator));
        list.add(new Order(11, "nu", "tak", electric));
        list.add(new Order(55, "nu", "tak", electric));
        list.add(new Order(1, "tam", "to", plomber));
        list.add(new Order(2, "tut", "eto", electric));
        list.add(new Order(3, "kak", "tak", repair));
        list.add(new Order(4, "nu", "tak", decorator));
        list.add(new Order(5, "nu", "tak", decorator));
        list.add(new Order(7, "nu", "tak", plomber));
        list.add(new Order(11, "nu", "tak", repair));
        list.add(new Order(23, "nu", "tak", decorator));
        list.add(new Order(41, "nu", "tak", decorator));
        list.add(new Order(11, "nu", "tak", electric));
        list.add(new Order(55, "nu", "tak", electric));
        list.add(new Order(1, "tam", "to", plomber));
        list.add(new Order(2, "tut", "eto", electric));
        list.add(new Order(3, "kak", "tak", repair));
        list.add(new Order(4, "nu", "tak", decorator));
        list.add(new Order(5, "nu", "tak", decorator));
        list.add(new Order(7, "nu", "tak", plomber));
        list.add(new Order(11, "nu", "tak", repair));
        list.add(new Order(23, "nu", "tak", decorator));
        list.add(new Order(41, "nu", "tak", decorator));
        list.add(new Order(11, "nu", "tak", electric));
        list.add(new Order(55, "nu", "tak", electric));



        return list;
    }
}
