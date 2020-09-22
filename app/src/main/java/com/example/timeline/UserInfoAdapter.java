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

public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.UserInfoViewHolder> implements Filterable {
    private ArrayList<UserDetails> userInfos;
    private ArrayList<UserDetails> userInfosFull;
    private UserInfoAdapter.OnItemClickListener mListener;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(UserInfoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class UserInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView fullname,username,email,gender,phone,status,userType,lastVisit;

        public UserInfoViewHolder(View itemView, final UserInfoAdapter.OnItemClickListener listener) {
            super(itemView);
            fullname = itemView.findViewById(R.id.user_item_fullName);
            username = itemView.findViewById(R.id.user_item_userName);
            email = itemView.findViewById(R.id.user_item_email);
            gender = itemView.findViewById(R.id.user_item_gender);
            phone = itemView.findViewById(R.id.user_item_phone);
            status = itemView.findViewById(R.id.user_item_status);
            userType = itemView.findViewById(R.id.user_item_userType);
            lastVisit = itemView.findViewById(R.id.user_item_lastVisit);

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

    public UserInfoAdapter(ArrayList<UserDetails> userinfos){
        this.userInfos = userinfos;
        userInfosFull = new ArrayList<>(userinfos);       //add
    }

    @NonNull
    @Override
    public UserInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        UserInfoAdapter.UserInfoViewHolder evh = new UserInfoAdapter.UserInfoViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull UserInfoViewHolder holder, int position) {
        UserDetails currentItem = userInfos.get(position);
        holder.fullname.setText(currentItem.getFullName());
        holder.username.setText(currentItem.getUserName());
        holder.email.setText(currentItem.getEmail());
        if (currentItem.getGender() == null || currentItem.getGender().isEmpty()){
            holder.gender.setText("N/A");
        }else{
            holder.gender.setText(currentItem.getGender());
        }
        if (currentItem.getPhone() == null || currentItem.getPhone().isEmpty()){
            holder.phone.setText("Phone: N/A");
        }else{
            holder.phone.setText("Phone: "+currentItem.getPhone());
        }
        if (currentItem.getStatus() == null || currentItem.getStatus().isEmpty()){
            holder.status.setText("Status: N/A");
        }else{
            holder.status.setText("Status: "+currentItem.getStatus());
        }
        if (currentItem.getUserType() == null || currentItem.getUserType().isEmpty()){
            holder.userType.setText("UserType: N/A");
        }else{
            holder.userType.setText("UserType: "+currentItem.getUserType());
        }
        if (currentItem.getLastVisit() == null || currentItem.getLastVisit().isEmpty()){
            holder.lastVisit.setText("Last Visit: N/A");
        }else{
            holder.lastVisit.setText("Last Visit: "+convert_Date_format(currentItem.getLastVisit()));
        }
    }

    @Override
    public int getItemCount() {
        return userInfos.size();
    }

    @Override
    public Filter getFilter() {
        return UserFilter;
    }

    private Filter UserFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<UserDetails> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filterList.addAll(userInfosFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (UserDetails info : userInfosFull){
                    if (info.getUserName().toLowerCase().contains(filterPattern) ||
                            info.getFullName().toLowerCase().contains(filterPattern) ||
                            info.getEmail().toLowerCase().contains(filterPattern) ||
                            info.getPhone().contains(filterPattern) ||
                            info.getUserType().toLowerCase().contains(filterPattern) ||
                            info.getStatus().toLowerCase().contains(filterPattern) ||
                            info.getGender().toLowerCase().contains(filterPattern) ||
                            info.getDateOfBirth().toLowerCase().contains(filterPattern) ||
                            info.getLastVisit().toLowerCase().contains(filterPattern)||
                            info.getCreatedDate().toLowerCase().contains(filterPattern)){
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
            userInfos.clear();
            userInfos.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };


    private String convert_Date_format(String DateTime){
        // 22-08-2020 Wed 05:34 PM
        if (!DateTime.isEmpty()){
            String localDate = DateTime.substring(0,10);
            String localTime = DateTime.substring(14,23);
            String day = DateTime.substring(10,14);
            if (localDate.equals(get_current_Date())){
                return "Today at "+localTime;
            }
            try {
                DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                DateFormat targetFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat targetFormat2 = new SimpleDateFormat("MMMM dd");
                DateFormat targetFormat3 = new SimpleDateFormat("MMMM dd, yyyy");
                Date date = originalFormat.parse(localDate);
                if (localDate.contains(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
                    return targetFormat2.format(date)+","+day+" at "+localTime;
                }else{
                    return targetFormat3.format(date)+","+day+" at "+localTime;
                }


            }catch (Exception e){
                Log.d("ERROR",e.getMessage());
            }
        }


        return DateTime;
    }

    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }

}
