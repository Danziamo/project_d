package com.mirsoft.easyfixmaster.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mirsoft.easyfixmaster.R;
import com.mirsoft.easyfixmaster.models.Order;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private ArrayList<Order> items;
    private int itemLayout;
    private final Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderAdapter(ArrayList<Order> items, int layout, Context context) {
        this.items = items;
        this.itemLayout = layout;
        this.mContext = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image;
        public TextView mAddressView;
        public TextView mReasonView;
        public ViewHolder(View v) {
            super(v);
            image = (ImageView) itemView.findViewById(R.id.imageOrder);
            mAddressView = (TextView) itemView.findViewById(R.id.tvAddress);
            mReasonView = (TextView) itemView.findViewById(R.id.tvReason);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(itemLayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
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
        holder.mReasonView.setText(item.getDescription());
        switch (item.getSpecialty().getId()%4) {
            case 0:
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.plumbing));
                break;
            case 1:
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.electricity));
                break;
            case 2:
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.repairing));
                break;
            case 3:
                holder.image.setImageDrawable(mContext.getResources().getDrawable(R.drawable.decoration));
                break;
        }
        holder.itemView.setTag(item);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}
