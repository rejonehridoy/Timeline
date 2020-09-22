package com.example.timeline;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import es.dmoral.toasty.Toasty;

public class ActivityInfoAdapter extends RecyclerView.Adapter<ActivityInfoAdapter.ActivityInfoViewHolder> implements Filterable {

    private ArrayList<ActivityInfo> activityInfos;
    private ArrayList<ActivityInfo> activityInfosFull;
    private OnItemClickListener mListener;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ActivityInfoViewHolder extends RecyclerView.ViewHolder {

        public TextView activity_title;         //First Row
        public TextView category;               // Second Row first column
        public TextView result;                 // Second Row second column (hidden)
        public TextView thirdItem;              // Second Row third column/second column in case second column is hidden
        public TextView date;                   // Third Row first Column
        public TextView status_result;          // Third Row second column

        public ActivityInfoViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            activity_title = itemView.findViewById(R.id.history_item_activity_title);
            category = itemView.findViewById(R.id.history_item_category);
            result = itemView.findViewById(R.id.history_item_result);
            thirdItem = itemView.findViewById(R.id.history_item_thirdField);
            date = itemView.findViewById(R.id.history_item_date);
            status_result = itemView.findViewById(R.id.history_item_status_review_result);

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

    public ActivityInfoAdapter(ArrayList<ActivityInfo> activityInfos) {
        this.activityInfos = activityInfos;
        activityInfosFull = new ArrayList<>(activityInfos);       //add
    }

    @NonNull
    @Override
    public ActivityInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item, parent, false);
        ActivityInfoViewHolder evh = new ActivityInfoViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityInfoViewHolder holder, int position) {
        set_all_info_in_textView(holder, position);
    }

    @Override
    public int getItemCount() {
        return activityInfos.size();
    }

    @Override
    public Filter getFilter() {
        return activityFilter;
    }

    private Filter activityFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ActivityInfo> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(activityInfosFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ActivityInfo info : activityInfosFull) {
                    if (info.getName().toLowerCase().contains(filterPattern) ||
                            info.getCategory().toLowerCase().contains(filterPattern) ||
                            info.getActivityType().toLowerCase().contains(filterPattern) ||
                            info.getDate().contains(filterPattern) ||
                            info.getTime().contains(filterPattern) ||
                            info.getYourTeam().toLowerCase().contains(filterPattern) ||
                            info.getOpponentTeam().toLowerCase().contains(filterPattern) ||
                            info.getPlace().toLowerCase().contains(filterPattern) ||
                            info.getLanguage().toLowerCase().contains(filterPattern) ||
                            info.getNotes().toLowerCase().contains(filterPattern) ||
                            info.getStatus().toLowerCase().contains(filterPattern) ||
                            info.getResult().toLowerCase().contains(filterPattern) ||
                            info.getYear().contains(filterPattern)) {
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
            activityInfos.clear();
            activityInfos.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    private void set_all_info_in_textView(@NonNull ActivityInfoViewHolder holder, int position)  {
        ActivityInfo currentItem = activityInfos.get(position);
        holder.activity_title.setText("You were " + currentItem.getActivityType().toLowerCase() + " " + currentItem.getName());

        if (currentItem.getDate().equals(get_current_Date())){
            holder.date.setText("Today at "+currentItem.getTime());
        }else{
            holder.date.setText(convert_date_format(currentItem.getDate())+" at "+currentItem.getTime());
        }


        holder.result.setVisibility(View.GONE);

        if (currentItem.getActivityType().toLowerCase().equals("watching")) {
            holder.category.setText("Category : " + currentItem.getCategory());
            if (currentItem.getReview().isEmpty()) {
                holder.status_result.setText("Review : N/A");
            } else {
                holder.status_result.setText("Review : " + currentItem.getReview());
            }

            if (currentItem.getCategory().equals("Movie")) {
                holder.thirdItem.setText("Language : " + currentItem.getLanguage());

            } else if (currentItem.getCategory().equals("TV Series") || currentItem.getCategory().equals("Drama")) {
                holder.thirdItem.setText("Sea/Epi : " + currentItem.getYourTeam() + "/" + currentItem.getOpponentTeam());

            } else {
                // for You tube videos
                holder.thirdItem.setText(convert_duration_into_hourMinutes(currentItem.getDuration()));

            }

        } else if (currentItem.getActivityType().toLowerCase().equals("eating")) {
            if (currentItem.getPlace().isEmpty()) {
                holder.category.setText("Place : N/A");
            } else {
                holder.category.setText("at " + currentItem.getPlace());
            }
            if (currentItem.getPrice().isEmpty()) {
                holder.thirdItem.setText("Price : N/A");
            } else {
                holder.thirdItem.setText("Price : " + currentItem.getPrice());
            }
            if (currentItem.getReview().isEmpty()) {
                holder.status_result.setText("Review : N/A");
            } else {
                holder.status_result.setText("Review : " + currentItem.getReview());
            }


        } else if (currentItem.getActivityType().toLowerCase().equals("playing")) {
            if (currentItem.getName().toLowerCase().equals("fifa") || currentItem.getName().toLowerCase().equals("football")){
                if (currentItem.getYourTeam().isEmpty()){
                    if (currentItem.getCategory().isEmpty()){
                        holder.category.setText("Category : N/A");
                    }else{
                        holder.category.setText("Category : "+currentItem.getCategory());
                    }

                }else{
                    holder.category.setText(currentItem.getYourTeam());
                }

                if (!currentItem.getResult().isEmpty()){
                    holder.result.setVisibility(View.VISIBLE);
                    holder.result.setText(currentItem.getResult());
                }

                if (currentItem.getOpponentTeam().isEmpty()){
                    if (!currentItem.getDuration().isEmpty()){
                        holder.thirdItem.setText(convert_duration_into_hourMinutes(currentItem.getDuration()));

                    }else{
                        holder.thirdItem.setText("Duration : N/A");
                    }
                }else{
                    holder.thirdItem.setText(currentItem.getOpponentTeam());
                }
                if (!currentItem.getPlace().isEmpty()){
                    holder.status_result.setText("Place : "+currentItem.getPlace());
                }else{
                    holder.status_result.setText("Place : N/A");
                }



            }else if (currentItem.getName().toLowerCase().equals("cricket")){
                if (!currentItem.getYourTeam().isEmpty()){
                    holder.category.setText(currentItem.getYourTeam());
                }else{
                    holder.category.setText("Your team : N/A");
                }

                if (!currentItem.getOpponentTeam().isEmpty()){
                    holder.thirdItem.setText(currentItem.getOpponentTeam());
                }else{
                    holder.thirdItem.setText("Opponent : N/A");
                }

                if (!currentItem.getStatus().isEmpty()){
                    holder.status_result.setText("Status : "+currentItem.getStatus());
                }else{
                    holder.status_result.setText("Status : N/A");
                }

            }else {
                if (!currentItem.getCategory().isEmpty()){
                    holder.category.setText("Category : "+currentItem.getCategory());
                }else{
                    holder.category.setText("Category : N/A");
                }

                if (!currentItem.getOpponentTeam().isEmpty()){
                    holder.thirdItem.setText("Opponent : "+currentItem.getOpponentTeam());
                }else if (!currentItem.getDuration().isEmpty()){
                    holder.thirdItem.setText(convert_duration_into_hourMinutes(currentItem.getDuration()));
                }else if (!currentItem.getPlace().isEmpty()){
                    holder.thirdItem.setText("Place : "+currentItem.getPlace());
                }else{
                    holder.thirdItem.setText("Duration : N/A");
                }

            }

        } else if (currentItem.getActivityType().toLowerCase().equals("working")) {
            if (!currentItem.getCategory().isEmpty()){
                holder.category.setText("Category : "+currentItem.getCategory());
            }else{
                holder.category.setText("Category : N/A");
            }

            if (!currentItem.getDuration().isEmpty()){
                holder.thirdItem.setText(convert_duration_into_hourMinutes(currentItem.getDuration()));
            }

            if (!currentItem.getStatus().isEmpty()){
                holder.status_result.setText("Status : "+currentItem.getStatus());
            }else{
                holder.status_result.setText("Status : N/A");
            }

        } else if (currentItem.getActivityType().toLowerCase().equals("solving")) {
            if (!currentItem.getCategory().isEmpty()){
                holder.category.setText("Difficulty : "+currentItem.getCategory());
            }else{
                holder.category.setText("Difficulty : N/A");
            }

            holder.thirdItem.setText(convert_duration_into_hourMinutes(currentItem.getDuration()));

            if (!currentItem.getResult().isEmpty()){
                holder.status_result.setText("Result : "+currentItem.getResult());
            }else{
                holder.status_result.setText("Result : N/A");
            }

        } else if (currentItem.getActivityType().toLowerCase().equals("attending")) {
            if (!currentItem.getPlace().isEmpty()){
                holder.category.setText("at "+currentItem.getPlace());
            }else{
                holder.category.setText("Place : N/A");
            }

            if (!currentItem.getCategory().isEmpty()){
                holder.thirdItem.setText("Category : "+currentItem.getCategory());
            }else{
                holder.thirdItem.setText("Category : N/A");
            }

            if (!currentItem.getStatus().isEmpty()){
                holder.status_result.setText("Status : "+currentItem.getStatus());
            }else{
                holder.status_result.setText("Status : N/A");
            }

        } else if (currentItem.getActivityType().toLowerCase().equals("reading")) {
            if (!currentItem.getCategory().isEmpty()){
                holder.category.setText("Category : "+currentItem.getCategory());
            }else{
                holder.category.setText("Category : N/A");
            }

            holder.thirdItem.setText(convert_duration_into_hourMinutes(currentItem.getDuration()));

            if (!currentItem.getStatus().isEmpty()){
                holder.status_result.setText("Status : "+currentItem.getStatus());
            }else if (!currentItem.getReview().isEmpty()){
                holder.status_result.setText("Review : "+currentItem.getResult());
            }else{
                holder.status_result.setText("Status : N/A");
            }

        } else if (currentItem.getActivityType().toLowerCase().equals("writing")) {
            if (!currentItem.getCategory().isEmpty()){
                holder.category.setText("Category : "+currentItem.getCategory());
            }else{
                holder.category.setText("Category : N/A");
            }

            if(!currentItem.getLanguage().isEmpty()){
                holder.thirdItem.setText("Language : "+currentItem.getLanguage());
            }else{
                holder.thirdItem.setText(convert_duration_into_hourMinutes(currentItem.getDuration()));
            }

            if (!currentItem.getStatus().isEmpty()){
                holder.status_result.setText("Status :" + currentItem.getStatus());
            }else{
                holder.status_result.setText("Status : N/A");
            }

        } else if (currentItem.getActivityType().toLowerCase().equals("buying")) {
            if (!currentItem.getCategory().isEmpty()){
                holder.category.setText("Category : "+currentItem.getCategory());
            }else{
                holder.category.setText("Category : N/A");
            }

            if (!currentItem.getPrice().isEmpty()){
                holder.thirdItem.setText("Price : "+currentItem.getPrice());
            }else{
                holder.thirdItem.setText("Price : N/A");
            }

            if (!currentItem.getPlace().isEmpty()){
                holder.status_result.setText("from "+currentItem.getPlace());
            }else{
                holder.status_result.setText("Place : N/A");
            }

        } else if (currentItem.getActivityType().toLowerCase().equals("wasting time")) {
            if (!currentItem.getPrice().isEmpty()){
                holder.thirdItem.setText("Price : "+currentItem.getPrice());
            }else{
                holder.thirdItem.setText("Price : N/A");
            }

            holder.thirdItem.setText(convert_duration_into_hourMinutes(currentItem.getDuration()));

            if (!currentItem.getResult().isEmpty()){
                holder.status_result.setText("Result : "+currentItem.getResult());
            }else{
                holder.status_result.setText("Result : N/A");
            }

        }
    }
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
    private String convert_date_format(String localDate){
        try {
            DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            DateFormat targetFormat1 = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat targetFormat2 = new SimpleDateFormat("MMMM dd");
            Date date = originalFormat.parse(localDate);
            if (localDate.contains(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
                return targetFormat2.format(date);
            }else{
                return targetFormat1.format(date);
            }


        }catch (Exception e){
            Log.d("ERROR",e.getMessage());
        }

         return localDate;
    }

    private String convert_duration_into_hourMinutes(String minutes) {
        if (!minutes.isEmpty() && !minutes.equals("0")) {
            int input_min = Integer.parseInt(minutes);
            int hour = input_min / 60;
            int minute = input_min - (hour * 60);
            if (hour == 0) {
                return "from " + minute + " minutes";
            } else if (minute ==0){
               return "from "+ hour + " hours";
            }else{
                return "from " + hour + " hours " + minute + " minutes";
            }
        } else {
            return "Duration : N/A";
        }

    }
}
