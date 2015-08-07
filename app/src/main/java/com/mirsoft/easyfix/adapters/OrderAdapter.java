package com.mirsoft.easyfix.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mirsoft.easyfix.ClientOrderDetailsActivity;
import com.mirsoft.easyfix.OrderDetailActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.common.Constants;
import com.mirsoft.easyfix.common.OrderType;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.utils.Singleton;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private int mode;
    private ArrayList<Order> items;
    private int itemLayout;
    private final Context mContext;
    Singleton singleton;

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderAdapter(ArrayList<Order> items, int layout, Context context, int mode) {
        this.items = items;
        this.itemLayout = layout;
        this.mContext = context;
        this.mode = mode;
        singleton = Singleton.getInstance(context);
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image;
        public TextView mAddressView;
        public TextView mReasonView;

        public ViewHolder(final View v) {
            super(v);
            image = (ImageView) itemView.findViewById(R.id.imageOrder);
            mAddressView = (TextView) itemView.findViewById(R.id.tvAddress);
            mReasonView = (TextView) itemView.findViewById(R.id.tvReason);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mode == Constants.CLIENT_ORDER_ADAPTER_MODE_NEW) {
                        Order order = (Order) v.getTag();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ORDER", order);
                        Intent intent = new Intent(mContext, OrderDetailActivity.class);
                        intent.putExtra("bundle", bundle);
                        mContext.startActivity(intent);
                    } else if (singleton.isClientMode) {
                        Intent intent = new Intent(mContext, ClientOrderDetailsActivity.class);
                        intent.putExtra("activityMode", "checkOrder");
                        mContext.startActivity(intent);
                        singleton.indexSelectedOrder = getLayoutPosition();
                        singleton.clientSelectedOrder = items.get(getLayoutPosition() - 1);
                    } else {
                        Order order = (Order) v.getTag();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("ORDER", order);
                        Intent intent = new Intent(mContext, OrderDetailActivity.class);
                        intent.putExtra("bundle", bundle);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
      /*  v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (singleton.isClientMode) {
                    Intent intent = new Intent(mContext, ClientOrderDetailsActivity.class);
                    intent.putExtra("activityMode", "checkOrder");
                    mContext.startActivity(intent);
                    singleton.indexSelectedOrder = get;
                    Toast.makeText(mContext,"  " + position,Toast.LENGTH_SHORT).show();
                } else {
                    Order order = (Order) v.getTag();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("ORDER", order);
                    Intent intent = new Intent(mContext, OrderDetailActivity.class);
                    intent.putExtra("bundle", bundle);
                    mContext.startActivity(intent);
                }

            }
        });*/
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        Order item = items.get(position);
        holder.mAddressView.setText(item.getAddress());
        if (mode == Constants.CLIENT_ORDER_ADAPTER_MODE_NEW) {
            holder.mReasonView.setText(item.getDescription());
        } else if (mode == Constants.CLIENT_ORDER_ADAPTER_MODE_ACTIVE) {
            holder.mReasonView.setTextColor(Color.rgb(255, 0, 0));
            if (item.getStatus() == OrderType.ACTIVE)
                holder.mReasonView.setText("Статус: Мастер взял ваш заказ");
            if (item.getStatus() == OrderType.NEW && item.getContractor() != null)
                holder.mReasonView.setText("Статус: Мастер не подтвердил");
            if (item.getStatus() == OrderType.NEW && item.getContractor() == null)
                holder.mReasonView.setText("Статус: Вы не подтвердили");
            if (item.getStatus() == OrderType.FINISHED)
                holder.mReasonView.setText("");
        } else {
            holder.mReasonView.setTextColor(Color.rgb(255, 0, 0));
            if (item.getStatus() == OrderType.ACTIVE)
                holder.mReasonView.setText("Статус: Клиент вас ждет");
            if (item.getStatus() == OrderType.NEW)
                holder.mReasonView.setText("Статус: Вы не подтвердили");
            if (item.getStatus() == OrderType.NEW && item.getContractor() == null)
                holder.mReasonView.setText("Статус: Клиент не подтвердили");
            if (item.getStatus() == OrderType.FINISHED)
                holder.mReasonView.setText("");
        }

        if (item.getSpecialty() != null)
            switch (item.getSpecialty().getId()%5) {
                case 1:
                    holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.plumbing));
                    break;
                case 2:
                    holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.electricity));
                    break;
                case 3:
                    holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.repairing));
                    break;
                case 4:
                    holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.decoration));
                    break;
            }
        else
            holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.foto));
        holder.itemView.setTag(item);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setDataset(ArrayList<Order> dataset) {
        items = dataset;
        // This isn't working
        notifyItemRangeInserted(0, items.size());
        notifyDataSetChanged();
    }
}
