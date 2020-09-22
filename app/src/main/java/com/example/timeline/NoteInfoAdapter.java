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

public class NoteInfoAdapter extends RecyclerView.Adapter<NoteInfoAdapter.NoteInfoViewHolder>  implements Filterable {
    private ArrayList<NoteInfo> noteInfos;
    private ArrayList<NoteInfo> noteInfosFull;
    private NoteInfoAdapter.OnItemClickListener mListener;




    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(NoteInfoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class NoteInfoViewHolder extends RecyclerView.ViewHolder {

        public TextView subject;
        public TextView description;
        public TextView date;


        public NoteInfoViewHolder(View itemView, final NoteInfoAdapter.OnItemClickListener listener) {
            super(itemView);
            subject = itemView.findViewById(R.id.note_item_subject);
            description = itemView.findViewById(R.id.note_item_description);
            date = itemView.findViewById(R.id.note_item_date);


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

    public NoteInfoAdapter(ArrayList<NoteInfo> noteInfos) {
        this.noteInfos = noteInfos;
        noteInfosFull = new ArrayList<>(noteInfos);       //add
    }

    @NonNull
    @Override
    public NoteInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_item, parent, false);
        NoteInfoAdapter.NoteInfoViewHolder evh = new NoteInfoAdapter.NoteInfoViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull NoteInfoViewHolder holder, int position) {
            NoteInfo currentItem = noteInfos.get(position);
            holder.subject.setText(currentItem.getSubject());
            holder.description.setText(currentItem.getDescription());
            if (currentItem.getModifiedDate() == null || currentItem.getModifiedDate().isEmpty()){
                holder.date.setText("Created: "+convert_createdDate_format(currentItem.getDate(),currentItem.getTime(),currentItem.getDayOfWeek()));
            }else{
                holder.date.setText("Edited: "+convert_ModifiedDate_format(currentItem.getModifiedDate()));
            }
    }

    @Override
    public int getItemCount() {
        return noteInfos.size();
    }
    @Override
    public Filter getFilter() {
        return NoteFilter;
    }

    private Filter NoteFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<NoteInfo> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0) {
                filterList.addAll(noteInfosFull);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (NoteInfo info : noteInfosFull) {
                    if (info.getSubject().toLowerCase().contains(filterPattern) ||
                            info.getDescription().toLowerCase().contains(filterPattern) ||
                            info.getDate().toLowerCase().contains(filterPattern) ||
                            info.getDayOfWeek().toLowerCase().contains(filterPattern) ||
                            info.getModifiedDate().toLowerCase().contains(filterPattern)) {
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
            noteInfos.clear();
            noteInfos.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };


    private String convert_ModifiedDate_format(String DateTime){
        //format july 22,2020, wed at 05:29 PM
        // 22-08-2020 Wed 05:34 PM
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
                return targetFormat2.format(date)+", "+day+" at "+localTime;
            }else{
                return targetFormat3.format(date)+", "+day+ " at "+localTime;
            }


        }catch (Exception e){
            Log.d("ERROR",e.getMessage());
        }

        return DateTime;
    }

    private String convert_createdDate_format(String localDate,String localTime,String localDay){
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
                return targetFormat2.format(date)+", "+localDay+" at "+localTime;
            }else{
                return targetFormat3.format(date)+", "+localDay+ " at "+localTime;
            }


        }catch (Exception e){
            Log.d("ERROR",e.getMessage());
        }

        return localDate+ " "+ localDay+" "+localTime;

    }
    private String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
}
