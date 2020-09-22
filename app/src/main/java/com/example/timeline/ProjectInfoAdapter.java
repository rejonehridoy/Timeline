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

public class ProjectInfoAdapter extends RecyclerView.Adapter<ProjectInfoAdapter.ProjectInfoViewHolder> implements Filterable {
    private ArrayList<ProjectInfo> projectInfos;
    private ArrayList<ProjectInfo> projectInfosFull;
    private ProjectInfoAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ProjectInfoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ProjectInfoViewHolder extends RecyclerView.ViewHolder {

        public TextView project_name;
        public TextView project_description;
        public TextView project_category;
        public TextView project_status;
        public TextView project_date;
        public TextView project_createdBy;

        public ProjectInfoViewHolder(View itemView, final ProjectInfoAdapter.OnItemClickListener listener) {
            super(itemView);

            project_name = itemView.findViewById(R.id.project_item_name);
            project_description = itemView.findViewById(R.id.project_item_description);
            project_category = itemView.findViewById(R.id.project_item_category);
            project_status = itemView.findViewById(R.id.project_item_status);
            project_date = itemView.findViewById(R.id.project_item_date);
            project_createdBy = itemView.findViewById(R.id.project_item_created);

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
    public ProjectInfoAdapter(ArrayList<ProjectInfo> projectInfos){
        this.projectInfos = projectInfos;
        projectInfosFull = new ArrayList<>(projectInfos);       //add
    }

    @NonNull
    @Override
    public ProjectInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        ProjectInfoAdapter.ProjectInfoViewHolder evh = new ProjectInfoAdapter.ProjectInfoViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectInfoViewHolder holder, int position) {
        ProjectInfo currentItem = projectInfos.get(position);
        holder.project_name.setText(currentItem.getName());
        holder.project_description.setText(currentItem.getDescription());
        holder.project_category.setText("Category : "+currentItem.getCategory());
        holder.project_status.setText("Status : "+currentItem.getStatus());

        if (currentItem.getStatus() != null && !currentItem.getStatus().isEmpty()){
            if (currentItem.getStatus().toLowerCase().equals("not started")){
                holder.project_createdBy.setText("Created by: "+currentItem.getCreatedUser());
                holder.project_date.setText(convert_date_format(currentItem.getCreatedDate()));
            }else if (currentItem.getStatus().toLowerCase().equals("in progress")){
                if (currentItem.getModifiedDate() != null && !currentItem.getModifiedDate().isEmpty()){
                    holder.project_date.setText(convert_date_format(currentItem.getModifiedDate()));
                    holder.project_createdBy.setText("Modified by: "+currentItem.getModifiedUser());
                }else{
                    holder.project_createdBy.setText("Created by: "+currentItem.getCreatedUser());
                    holder.project_date.setText(convert_date_format(currentItem.getStartingDate()));
                }

            }else if (currentItem.getStatus().toLowerCase().equals("completed")){
                if (currentItem.getModifiedUser() != null && !currentItem.getModifiedUser().isEmpty()){
                    holder.project_createdBy.setText("Modified by: "+currentItem.getModifiedUser());
                }else{
                    holder.project_createdBy.setText("Modified by: "+currentItem.getCreatedUser());
                }
                holder.project_date.setText(convert_date_format(currentItem.getEndDate()));
            }else if (currentItem.getStatus().toLowerCase().equals("cancelled") || currentItem.getStatus().toLowerCase().equals("incompleted")){
                if (currentItem.getModifiedDate() != null && !currentItem.getModifiedDate().isEmpty()){
                    holder.project_date.setText(convert_date_format(currentItem.getModifiedDate()));
                    holder.project_createdBy.setText("Modified by: "+currentItem.getModifiedUser());
                }else{
                    holder.project_createdBy.setText("Created by: "+currentItem.getCreatedUser());
                    holder.project_date.setText(convert_date_format(currentItem.getCreatedDate()));
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return projectInfos.size();
    }

    @Override
    public Filter getFilter() {
        return projectFilter;
    }

    private Filter projectFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ProjectInfo> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filterList.addAll(projectInfosFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ProjectInfo info : projectInfosFull){
                    if (info.getName().toLowerCase().contains(filterPattern) ||
                           info.getCategory().toLowerCase().contains(filterPattern) ||
                            info.getDescription().toLowerCase().contains(filterPattern) ||
                            info.getPlatform().toLowerCase().contains(filterPattern) ||
                            info.getFramework().toLowerCase().contains(filterPattern) ||
                            info.getRequirement().toLowerCase().contains(filterPattern) ||
                            info.getPrerequisite().toLowerCase().contains(filterPattern) ||
                            info.getStatus().toLowerCase().contains(filterPattern) ||
                            info.getModifiedUser().toLowerCase().contains(filterPattern) ||
                            info.getCreatedUser().toLowerCase().contains(filterPattern) ||
                            info.getCreatedDate().contains(filterPattern) ||
                            info.getModifiedDate().contains(filterPattern)){
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
            projectInfos.clear();
            projectInfos.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };

    private String convert_date_format(String DateTime){
        //format july 22,2020, wed at 05:29 PM
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

    private String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
}
