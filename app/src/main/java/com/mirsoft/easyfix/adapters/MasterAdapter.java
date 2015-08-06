package com.mirsoft.easyfix.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mirsoft.easyfix.MasterInfoActivity;
import com.mirsoft.easyfix.R;
import com.mirsoft.easyfix.models.User;

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
        public View mView;
        public TextView mFullNameView;
        public TextView mPhoneView;
        public TextView mReviewView;
        public RatingBar mRatingView;
        public ImageButton phoneLogo;
        public ViewHolder(View v) {
            super(v);
            mView = v;
            mFullNameView = (TextView) itemView.findViewById(R.id.tvFullName);
            mPhoneView = (TextView) itemView.findViewById(R.id.tvPhone);
            mReviewView = (TextView) itemView.findViewById(R.id.tvReviews);
            mRatingView = (RatingBar)itemView.findViewById(R.id.ratingBar);
            phoneLogo = (ImageButton)itemView.findViewById(R.id.ibtnCalling);

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

            }
        });
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(mDataset[position]);
        User item = items.get(position);
        holder.mFullNameView.setText(item.getFirstName());
        holder.mReviewView.setText("Отзывов: " + String.valueOf(item.getReviewsCount()));
        holder.mPhoneView.setText(item.getPhone());
        holder.mRatingView.setRating(item.getRating());
        holder.itemView.setTag(item);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MasterInfoActivity.class);
                intent.putExtra("MASTER", items.get(position));
                mContext.startActivity(intent);
            }
        });

        PhoneStateListeners listeners = new PhoneStateListeners();
        TelephonyManager manager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        manager.listen(listeners,PhoneStateListeners.LISTEN_CALL_STATE);

        holder.phoneLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = "tel:"+holder.mPhoneView.getText();
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(tel));
                mContext.startActivity(intent);
            }
        });
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

    public class PhoneStateListeners extends PhoneStateListener {

        private boolean isCalling = false;
        String LOG = "Medical:Phone State";

        @Override
        public  void  onCallStateChanged(int state, String incomingNumber){


            if(TelephonyManager.CALL_STATE_RINGING == state){
                Log.v(LOG, "Ringing number:" + incomingNumber);
            }
            if(TelephonyManager.CALL_STATE_OFFHOOK == state){
                Log.v(LOG, "OFFHOOK");
            }
            if(TelephonyManager.CALL_STATE_IDLE == state){
                Log.v(LOG, "IDLE");

                if(isCalling){
                    Log.v(LOG, "Restart app");

                    Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(
                            mContext.getPackageName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(intent);

                    isCalling = false;
                }
            }
        }


    }
}