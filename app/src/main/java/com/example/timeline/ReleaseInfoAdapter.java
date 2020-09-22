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

public class ReleaseInfoAdapter extends RecyclerView.Adapter<ReleaseInfoAdapter.ReleaseInfoViewHolder> implements Filterable {
    private ArrayList<ReleaseInfo> releaseInfos;
    private ArrayList<ReleaseInfo> releaseInfosFull;
    private ReleaseInfoAdapter.OnItemClickListener mListener;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(ReleaseInfoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ReleaseInfoViewHolder extends RecyclerView.ViewHolder {
        public TextView version,description,date;

        public ReleaseInfoViewHolder(View itemView, final ReleaseInfoAdapter.OnItemClickListener listener) {
            super(itemView);
            version = itemView.findViewById(R.id.release_item_version);
            description = itemView.findViewById(R.id.release_item_description);
            date = itemView.findViewById(R.id.release_item_date);


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

    public ReleaseInfoAdapter(ArrayList<ReleaseInfo> releaseInfos){
        this.releaseInfos = releaseInfos;
        releaseInfosFull = new ArrayList<>(releaseInfos);       //add
    }

    @NonNull
    @Override
    public ReleaseInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.release_item, parent, false);
        ReleaseInfoAdapter.ReleaseInfoViewHolder evh = new ReleaseInfoAdapter.ReleaseInfoViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ReleaseInfoViewHolder holder, int position) {
        ReleaseInfo currentItem = releaseInfos.get(position);
        if (currentItem.getVersionType() != null && !currentItem.getVersionType().isEmpty()){
            holder.version.setText("Version: "+currentItem.getVersion()+" ("+currentItem.getVersionType()+")");
        }else{
            holder.version.setText("Version: "+currentItem.getVersion());
        }
        if (currentItem.getDescription() !=null && !currentItem.getDescription().isEmpty()){
            holder.description.setText(currentItem.getDescription());
        }else{
            holder.description.setText("N/A");
        }
        if (currentItem.getReleaseDate() != null && !currentItem.getReleaseDate().isEmpty()){
            holder.date.setText("Released Date: "+currentItem.getReleaseDate());
        }else{
            holder.date.setText("Released Date: N/A");
        }
    }

    @Override
    public int getItemCount() {
        return releaseInfos.size();
    }

    @Override
    public Filter getFilter() {
        return ReleaseFilter;
    }

    private Filter ReleaseFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<ReleaseInfo> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filterList.addAll(releaseInfosFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (ReleaseInfo info : releaseInfosFull){
                    if (info.getDescription().toLowerCase().contains(filterPattern) ||
                            info.getVersion().toLowerCase().contains(filterPattern) ||
                            info.getVersionType().toLowerCase().contains(filterPattern) ||
                            info.getBug().toLowerCase().contains(filterPattern) ||
                            info.getReleaseDate().toLowerCase().contains(filterPattern) ||
                            info.getDownloadLink().toLowerCase().contains(filterPattern) ||
                            info.getId().toLowerCase().contains(filterPattern)){
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
            releaseInfos.clear();
            releaseInfos.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };




}
