package com.mirsoft.easyfixmaster.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mirsoft.easyfixmaster.OrderDetailActivity;
import com.mirsoft.easyfixmaster.R;
import com.mirsoft.easyfixmaster.models.Order;
import com.mirsoft.easyfixmaster.models.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private ArrayList<Review> items;
    private int itemLayout;
    private final Context mContext;

    // Provide a suitable constructor (depends on the kind of dataset)
    public ReviewAdapter(ArrayList<Review> items, int layout, Context context) {
        this.items = items;
        this.itemLayout = layout;
        this.mContext = context;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mAuthorView;
        public RatingBar mRatingView;
        public TextView mDescriptionView;
        public ViewHolder(View v) {
            super(v);
            mRatingView = (RatingBar) itemView.findViewById(R.id.ratingBar);
            mAuthorView = (TextView) itemView.findViewById(R.id.tvAuthor);
            mDescriptionView = (TextView) itemView.findViewById(R.id.tvDescription);
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(itemLayout, parent, false);
        // set the view's size, margins, paddings and layout parameters
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tvDescription = (TextView)v.findViewById(R.id.tvDescription);
                if (tvDescription.getMaxLines() == 2) {
                    tvDescription.setMaxLines(0);
                } else {
                    tvDescription.setMaxLines(2);
                }
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
        Review item = items.get(position);
        holder.mAuthorView.setText(item.getAuthor());
        holder.mDescriptionView.setText(item.getDescription());
        holder.mRatingView.setRating(item.getRating());

        holder.itemView.setTag(item);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return items.size();
    }
}

