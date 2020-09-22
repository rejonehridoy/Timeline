package com.example.timeline;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FeedbackInfoAdapter extends RecyclerView.Adapter<FeedbackInfoAdapter.FeedbackInfoViewHolder> implements Filterable {
    private ArrayList<FeedbackInfo> feedbackInfos;
    private ArrayList<FeedbackInfo> feedbackInfosFull;
    private FeedbackInfoAdapter.OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(FeedbackInfoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class FeedbackInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView subjet, message, date, username;

        public FeedbackInfoViewHolder(View itemView, final FeedbackInfoAdapter.OnItemClickListener listener) {
            super(itemView);
            subjet = itemView.findViewById(R.id.feedback_item_subject);
            message = itemView.findViewById(R.id.feedback_item_message);
            date = itemView.findViewById(R.id.feedback_item_date);
            username = itemView.findViewById(R.id.feedback_item_username);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }

            });
        }
    }

    public FeedbackInfoAdapter(ArrayList<FeedbackInfo> feedbacksinfo) {
        this.feedbackInfos = feedbacksinfo;
        feedbackInfosFull = new ArrayList<>(feedbackInfos);       //add
    }

    @NonNull
    @Override
    public FeedbackInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_item, parent, false);
        FeedbackInfoAdapter.FeedbackInfoViewHolder evh = new FeedbackInfoAdapter.FeedbackInfoViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackInfoViewHolder holder, int position) {
        FeedbackInfo currentItem = feedbackInfos.get(position);
        holder.subjet.setText(currentItem.getSubject());
        holder.message.setText(currentItem.getMessage());
        holder.username.setText(currentItem.getUserName());
        holder.date.setText(convert_Date_format(currentItem.getDate()));
    }

    @Override
    public int getItemCount() {
        return feedbackInfos.size();
    }

    @Override
    public Filter getFilter() {
        return FeedbackFilter;
    }

    private Filter FeedbackFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<FeedbackInfo> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(feedbackInfosFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (FeedbackInfo info : feedbackInfosFull) {
                    if (info.getUserName().toLowerCase().contains(filterPattern) ||
                            info.getFullName().toLowerCase().contains(filterPattern) ||
                            info.getEmail().toLowerCase().contains(filterPattern) ||
                            info.getSubject().toLowerCase().contains(filterPattern) ||
                            info.getMessage().toLowerCase().contains(filterPattern) ||
                            info.getAppVersion().toLowerCase().contains(filterPattern) ||
                            info.getDate().toLowerCase().contains(filterPattern) ||
                            info.getUserName().toLowerCase().contains(filterPattern) ||
                            info.getReply().toLowerCase().contains(filterPattern)) {
                        filterList.add(info);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filterList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            feedbackInfos.clear();
            feedbackInfos.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    private String convert_Date_format(String DateTime) {
        // 22-08-2020 Wed 05:34 PM
        if (!DateTime.isEmpty()) {
            String localDate = DateTime.substring(0, 10);
            String localTime = DateTime.substring(14, 23);
            String day = DateTime.substring(10, 14);
            if (localDate.equals(get_current_Date())) {
                return "Today at " + localTime;
            }
            try {
                DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                DateFormat targetFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat targetFormat2 = new SimpleDateFormat("MMMM dd");
                DateFormat targetFormat3 = new SimpleDateFormat("MMMM dd, yyyy");
                Date date = originalFormat.parse(localDate);
                if (localDate.contains(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))) {
                    return targetFormat2.format(date) + "," + day + " at " + localTime;
                } else {
                    return targetFormat3.format(date) + "," + day + " at " + localTime;
                }


            } catch (Exception e) {
                Log.d("ERROR", e.getMessage());
            }
        }


        return DateTime;
    }

    public String get_current_Date() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
}
