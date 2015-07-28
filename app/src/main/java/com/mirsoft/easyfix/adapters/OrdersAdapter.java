package com.mirsoft.easyfix.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirsoft.easyfix.OrderDetailActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.models.Order;

import java.util.ArrayList;

/**
 * Created by parviz on 7/28/15.
 */
public class OrdersAdapter extends BaseAdapter {

    private ArrayList<Order> items;
    private int itemLayout;
    Context mContext;

    public OrdersAdapter(Context context,int layout,ArrayList<Order> items){
        this.mContext = context;
        this.itemLayout = layout;
        this.items = items;

    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(itemLayout, viewGroup, false);

       ImageView image = (ImageView) rowView.findViewById(R.id.imageOrder);
       TextView mAddressView = (TextView) rowView.findViewById(R.id.tvAddress);
       TextView mReasonView = (TextView) rowView.findViewById(R.id.tvReason);


        Order item = items.get(i);
//        holder.mAddressView.setText(item.getAddress());
//        holder.mReasonView.setText(item.getDescription());

        mAddressView.setText(item.getAddress());
        mReasonView.setText(item.getDescription());

        if (item.getSpecialty() != null)
            switch (item.getSpecialty().getId()%5) {
                case 1:
                    image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.plumbing));
                    break;
                case 2:
                    image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.electricity));
                    break;
                case 3:
                    image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.repairing));
                    break;
                case 4:
                    image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.decoration));
                    break;
            }
        else
            image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.foto));
        rowView.setTag(item);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order order = (Order) v.getTag();
                Intent intent = new Intent(mContext, OrderDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ORDER", order);
                intent.putExtra("bundle", bundle);
                mContext.startActivity(intent);
            }
        });

        return rowView;
    }
}
