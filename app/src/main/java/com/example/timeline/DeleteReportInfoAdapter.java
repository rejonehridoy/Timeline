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

public class DeleteReportInfoAdapter extends RecyclerView.Adapter<DeleteReportInfoAdapter.DeleteReportInfoViewHolder> implements Filterable {
    private ArrayList<DeleteReportInfo> deleteReportInfos;
    private ArrayList<DeleteReportInfo> deleteReportInfosFull;
    private DeleteReportInfoAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(DeleteReportInfoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class DeleteReportInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView fullname,username,email,reason,date;

        public DeleteReportInfoViewHolder(View itemView, final DeleteReportInfoAdapter.OnItemClickListener listener) {
            super(itemView);
            fullname = itemView.findViewById(R.id.delete_user_item_fullName);
            username = itemView.findViewById(R.id.delete_user_item_userName);
            email = itemView.findViewById(R.id.delete_user_item_email);
            reason = itemView.findViewById(R.id.delete_user_item_reason);
            date = itemView.findViewById(R.id.delete_user_item_date);

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

    public DeleteReportInfoAdapter(ArrayList<DeleteReportInfo> deleteReportInfos){
        this.deleteReportInfos = deleteReportInfos;
        deleteReportInfosFull = new ArrayList<>(deleteReportInfos);       //add
    }


    @NonNull
    @Override
    public DeleteReportInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.delete_user_report_item, parent, false);
        DeleteReportInfoAdapter.DeleteReportInfoViewHolder evh = new DeleteReportInfoAdapter.DeleteReportInfoViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull DeleteReportInfoViewHolder holder, int position) {
        DeleteReportInfo currentItem = deleteReportInfos.get(position);
        holder.fullname.setText(currentItem.getFullName());
        holder.username.setText(currentItem.getUserName());
        holder.email.setText(currentItem.getEmail());
        holder.reason.setText(currentItem.getReason());
        holder.date.setText(currentItem.getDate());
    }

    @Override
    public int getItemCount() {
        return deleteReportInfos.size();
    }
    @Override
    public Filter getFilter() {
        return DeleteUserFilter;
    }
    private Filter DeleteUserFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<DeleteReportInfo> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filterList.addAll(deleteReportInfosFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (DeleteReportInfo info : deleteReportInfosFull){
                    if (info.getUserName().toLowerCase().contains(filterPattern) ||
                            info.getFullName().toLowerCase().contains(filterPattern) ||
                            info.getEmail().toLowerCase().contains(filterPattern) ||
                            info.getPhone().toLowerCase().contains(filterPattern) ||
                            info.getReason().toLowerCase().contains(filterPattern) ||
                            info.getDate().toLowerCase().contains(filterPattern) ||
                            info.getUid().toLowerCase().contains(filterPattern)
                            ){
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
            deleteReportInfos.clear();
            deleteReportInfos.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
