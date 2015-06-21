package com.mirsoft.easyfixmaster;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mirsoft.easyfixmaster.models.Order;


/**
 * A placeholder fragment containing a simple view.
 */
public class OrderDetailActivityFragment extends Fragment {

    public OrderDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        TextView tvInfo = (TextView)view.findViewById(R.id.tvInfo);
        Bundle bundle = getActivity().getIntent().getBundleExtra("bundle");
        Order order = (Order)bundle.getSerializable("ORDER");

        tvInfo.setText(order.getAddress() + " " + order.getStatus().toString());

        return view;
    }
}
