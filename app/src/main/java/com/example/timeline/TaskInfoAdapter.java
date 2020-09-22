package com.example.timeline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskInfoAdapter extends RecyclerView.Adapter<TaskInfoAdapter.TaskInfoViewHolder> implements Filterable {
    private ArrayList<TaskInfo> taskInfos;
    private ArrayList<TaskInfo> taskInfosFull;
    private TaskInfoAdapter.OnItemClickListener mListener;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(TaskInfoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class TaskInfoViewHolder extends RecyclerView.ViewHolder {

        public TextView task_name,task_description,task_priority,task_status;

        public TaskInfoViewHolder(View itemView, final TaskInfoAdapter.OnItemClickListener listener) {
            super(itemView);

            task_name = itemView.findViewById(R.id.task_item_name);
            task_description = itemView.findViewById(R.id.task_item_description);
            task_priority = itemView.findViewById(R.id.task_item_priority);
            task_status = itemView.findViewById(R.id.task_item_status);

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
    public TaskInfoAdapter(ArrayList<TaskInfo> taskInfos){
        this.taskInfos = taskInfos;
        taskInfosFull = new ArrayList<>(taskInfos);       //add
    }

    @NonNull
    @Override
    public TaskInfoAdapter.TaskInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_item, parent, false);
        TaskInfoAdapter.TaskInfoViewHolder evh = new TaskInfoAdapter.TaskInfoViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull TaskInfoAdapter.TaskInfoViewHolder holder, int position) {
        TaskInfo currentItem = taskInfos.get(position);
        if (currentItem.getName() != null && !currentItem.getName().isEmpty()){
            if (!currentItem.getSerial().isEmpty()){
                holder.task_name.setText(currentItem.getSerial()+". "+currentItem.getName());
            }
            else{
                holder.task_name.setText(currentItem.getName());
            }
        }
        if (currentItem.getDescription() != null && !currentItem.getDescription().isEmpty()){
            holder.task_description.setText(currentItem.getDescription());
        }
        if (currentItem.getPriority() != null && !currentItem.getPriority().isEmpty()){
            holder.task_priority.setText("Priority : "+currentItem.getPriority());
        }
        if (currentItem.getStatus() != null && !currentItem.getStatus().isEmpty()){
            holder.task_status.setText("Status : "+currentItem.getStatus());
        }

    }

    @Override
    public int getItemCount() {
        return taskInfos.size();
    }

    @Override
    public Filter getFilter() {
        return taskFilter;
    }

    private Filter taskFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<TaskInfo> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filterList.addAll(taskInfosFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (TaskInfo info : taskInfosFull){
                    if (info.getName().toLowerCase().contains(filterPattern) ||
                            info.getSerial().toLowerCase().contains(filterPattern) ||
                            info.getCategory().toLowerCase().contains(filterPattern) ||
                            info.getDescription().toLowerCase().contains(filterPattern) ||
                            info.getSolution().toLowerCase().contains(filterPattern) ||
                            info.getDeadline().toLowerCase().contains(filterPattern) ||
                            info.getAssigner().toLowerCase().contains(filterPattern) ||
                            info.getPoints().contains(filterPattern) ||
                            info.getIssues().toLowerCase().contains(filterPattern) ||
                            info.getModifiedUser().toLowerCase().contains(filterPattern) ||
                            info.getCreatedUser().toLowerCase().contains(filterPattern) ||
                            info.getPriority().toLowerCase().contains(filterPattern) ||
                            info.getStatus().toLowerCase().contains(filterPattern) ||
                            info.getComments().toLowerCase().contains(filterPattern)){
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
            taskInfos.clear();
            taskInfos.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
