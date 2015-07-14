package com.mirsoft.easyfix.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mirsoft.easyfix.OrderDetailActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.models.Order;
import com.mirsoft.easyfix.models.User;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MasterAdapter extends RecyclerView.Adapter<MasterAdapter.ViewHolder> {
    private ArrayList<User> items;
    private int itemLayout;
    private final Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public MasterAdapter(ArrayList<User> items, int layout, Context context) {
        this.items = items;
        this.itemLayout = layout;
        this.mContext = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mFullNameView;
        public TextView mPhoneView;
        public TextView mReviewView;
        public AppCompatRatingBar mRatingView;
        public ViewHolder(View v) {
            super(v);
            mFullNameView = (TextView) itemView.findViewById(R.id.tvFullName);
            mPhoneView = (TextView) itemView.findViewById(R.id.tvPhone);
            mReviewView = (TextView) itemView.findViewById(R.id.tvReviews);
            mRatingView = (AppCompatRatingBar)itemView.findViewById(R.id.ratingBar);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MasterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(itemLayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        User item = items.get(position);
        holder.mFullNameView.setText(item.getFirstName());
        holder.mReviewView.setText(item.getRole());
        holder.mPhoneView.setText(item.getPhone());
        holder.mRatingView.setRating(item.getId()%6);
        holder.itemView.setTag(item);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setDataset(ArrayList<User> dataset) {
        items = dataset;
        // This isn't working
        notifyItemRangeInserted(0, items.size());
        notifyDataSetChanged();
    }
}