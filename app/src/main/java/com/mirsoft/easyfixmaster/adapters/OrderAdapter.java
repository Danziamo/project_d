package com.mirsoft.easyfixmaster.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mirsoft.easyfixmaster.R;
import com.mirsoft.easyfixmaster.models.Order;

import java.util.List;

/**
 * Created by danta on 6/16/2015.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> items;
    private int itemLayout;

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderAdapter(List<Order> items, int layout) {
        this.items = items;
        this.itemLayout = layout;
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
            image = (ImageView) itemView.findViewById(R.id.image);
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
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}
