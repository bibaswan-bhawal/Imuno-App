package com.boss.imuno.UI.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.boss.imuno.R;
import com.boss.imuno.data.User;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private double targetLevel = 0;
    private double totalAmount = 0;
    private double recommendedAmount = 0;

    public MainRecyclerViewAdapter(Context context){
        mContext = context;
    }

    /** Returns item position as ViewType based on position which is used by @onCreateViewHolder to bind ViewHolders **/
    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return 1;
        } else if(position == 1 || position == 2){
            return 2;
        } else{
            return 3;
        }
    }

    /** Sets Data for Recycler view **/

    public void setViewData(User user){
        targetLevel = user.getTargetLevel();
        totalAmount = user.getTotalAmount();
        recommendedAmount = user.getRecommendedAmount();

        notifyDataSetChanged();
    }

    /** Binds ViewHolders based on position **/
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_main, parent, false);
            view.setFocusable(true);
            return new MainListItemViewHolder(view);
        }else if(viewType == 2){
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_secondary, parent, false);
            view.setFocusable(true);
            return new SecondaryListItemViewHolder(view);
        } else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_tertiary, parent, false);
            return new TertiaryListItemViewHolder(view);
        }
    }

    /** Calls appropriate function to handle view based on position **/
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position == 0){
            initLayoutOne((MainListItemViewHolder) holder);
        }

        if(position == 1 || position == 2){
            initLayoutTwo((SecondaryListItemViewHolder) holder, position);
        }

        if(position == 3){
           initLayoutThree((TertiaryListItemViewHolder) holder);
        }
    }

    /** Handles the display of target amount Card View **/
    private void initLayoutOne(MainListItemViewHolder holder) {
        holder.targetLevelTextView.setText(String.format("%.3f", targetLevel));

    }

    /** Handles the display of total amount and recommended daily dosage**/
    private void initLayoutTwo(SecondaryListItemViewHolder holder, int position) {
        if(position == 1){
            holder.titleTextView.setText(R.string.total_amount_item_label);
            holder.unitTextView.setText(R.string.ng_ml_units_label);
            holder.valueTextView.setText(String.format("%.3f", totalAmount));

        }else if(position == 2){
            holder.titleTextView.setText(R.string.daily_amount_title_label);
            holder.unitTextView.setText(R.string.iu_units_label);
            holder.valueTextView.setText(String.format("%.0f", Math.ceil(recommendedAmount/1000)*1000));
        }
    }

    /** Handles the display of days left CardView **/
    private void initLayoutThree(TertiaryListItemViewHolder holder) {

        Calendar c = Calendar.getInstance(Locale.UK);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        int refillDay = Integer.parseInt(Objects.requireNonNull(sharedPref.getString(mContext.getString(R.string.refill_day_shared_preference_key), "")));
        int daysLeft = (((refillDay + 7) - dayOfWeek))%7;



        if(daysLeft == 0){
            // Hide all TextView except the prefix
            holder.daysLeftPrefixTextView.setText(R.string.take_dose_now_label);
            holder.daysLeftTextView.setVisibility(View.GONE);
            holder.daysLeftSuffixTextView.setVisibility(View.GONE);

            //reset margins of the TextView to be center
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 52, 0, 52);
            holder.daysLeftPrefixTextView.setLayoutParams(lp);

        } else {
            // Set days left till next dose
            holder.daysLeftPrefixTextView.setVisibility(View.VISIBLE);
            holder.daysLeftTextView.setVisibility(View.VISIBLE);
            holder.daysLeftSuffixTextView.setVisibility(View.VISIBLE);

            holder.daysLeftTextView.setText(String.valueOf(daysLeft));
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    /** ViewHolder for target amount Card View in R.layout.list_item_main **/

    public static class MainListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView targetLevelTextView;

        public MainListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            targetLevelTextView = itemView.findViewById(R.id.targetLevelTextView);
        }
    }

    /** ViewHolder for total amount and recommended amount ListItems in R.layout.list_item_secondary **/
    public static class SecondaryListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView;
        public TextView valueTextView;
        public TextView unitTextView;
        public SecondaryListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.titleTextView);
            valueTextView = itemView.findViewById(R.id.valueTextView);
            unitTextView = itemView.findViewById(R.id.unitTextView);
        }
    }

    /** ViewHolder for days left Card View in R.layout.list_item_tertiary **/
    public static class TertiaryListItemViewHolder extends RecyclerView.ViewHolder {

        public TextView daysLeftPrefixTextView;
        public TextView daysLeftTextView;
        public TextView daysLeftSuffixTextView;

        public TertiaryListItemViewHolder(@NonNull View itemView) {
            super(itemView);

            daysLeftPrefixTextView = itemView.findViewById(R.id.daysLeftPrefixTextView);
            daysLeftTextView = itemView.findViewById(R.id.daysLeftTextView);
            daysLeftSuffixTextView = itemView.findViewById(R.id.daysLeftSuffixTextView);

        }
    }
}
