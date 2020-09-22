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

public class NoticeInfoAdapter extends RecyclerView.Adapter<NoticeInfoAdapter.NoticeInfoViewHolder> implements Filterable {
    private ArrayList<NoticeInfo> noticeInfos;
    private ArrayList<NoticeInfo> noticeInfosFull;
    private NoticeInfoAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(NoticeInfoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class NoticeInfoViewHolder extends RecyclerView.ViewHolder {

        public TextView notice_subject;
        public TextView notice_date;
        public TextView notice_third;
        public TextView notice_description;

        public NoticeInfoViewHolder(View itemView, final NoticeInfoAdapter.OnItemClickListener listener) {
            super(itemView);

            notice_subject = itemView.findViewById(R.id.notice_item_subject);
            notice_third = itemView.findViewById(R.id.notice_item_third);
            notice_date = itemView.findViewById(R.id.notice_item_date);
            notice_description = itemView.findViewById(R.id.notice_item_description);

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

    public NoticeInfoAdapter(ArrayList<NoticeInfo> noticeInfos){
        this.noticeInfos = noticeInfos;
        noticeInfosFull = new ArrayList<>(noticeInfos);       //add
    }

    @NonNull
    @Override
    public NoticeInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, parent, false);
        NoticeInfoAdapter.NoticeInfoViewHolder evh = new NoticeInfoAdapter.NoticeInfoViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeInfoViewHolder holder, int position) {
        NoticeInfo currentItem = noticeInfos.get(position);
        holder.notice_subject.setText(currentItem.getSubject());
        holder.notice_date.setText(currentItem.getCreatedDate());
        holder.notice_description.setText(currentItem.getDescription());
        if (currentItem.getRepetition().toLowerCase().equals("weekly")){
            holder.notice_third.setText("Day of week : "+currentItem.getDayOfWeek());
        }else{
            holder.notice_third.setText("Tag : "+currentItem.getTag());
        }

    }

    @Override
    public int getItemCount() {
        return noticeInfos.size();
    }

    @Override
    public Filter getFilter() {
        return noticeFilter;
    }

    private Filter noticeFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<NoticeInfo> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filterList.addAll(noticeInfosFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (NoticeInfo info : noticeInfosFull){
                    if (info.getSubject().toLowerCase().contains(filterPattern) ||
                            info.getCategory().toLowerCase().contains(filterPattern) ||
                            info.getCreator().toLowerCase().contains(filterPattern) ||
                            info.getCreatedDate().contains(filterPattern) ||
                            info.getDayOfWeek().toLowerCase().contains(filterPattern) ||
                            info.getEventDate().contains(filterPattern) ||
                            info.getEventTime().toLowerCase().contains(filterPattern) ||
                            info.getDescription().toLowerCase().contains(filterPattern) ||
                            info.getRepetition().toLowerCase().contains(filterPattern) ||
                            info.getTag().toLowerCase().contains(filterPattern)){
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
            noticeInfos.clear();
            noticeInfos.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
