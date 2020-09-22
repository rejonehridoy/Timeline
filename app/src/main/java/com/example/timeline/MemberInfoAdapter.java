package com.example.timeline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MemberInfoAdapter extends RecyclerView.Adapter<MemberInfoAdapter.MemberInfoViewHolder> {
    private ArrayList<MemberInfo> memberInfos;
    private MemberInfoAdapter.OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(MemberInfoAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public static class MemberInfoViewHolder extends RecyclerView.ViewHolder {

        public TextView name,userName,role;

        public MemberInfoViewHolder(View itemView, final MemberInfoAdapter.OnItemClickListener listener) {
            super(itemView);

            name = itemView.findViewById(R.id.member_create_item_fullName);
            userName = itemView.findViewById(R.id.member_create_item_userName);
            role = itemView.findViewById(R.id.member_create_item_role);

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

    public MemberInfoAdapter(ArrayList<MemberInfo> memberInfos){
        this.memberInfos = memberInfos;

    }

    @NonNull
    @Override
    public MemberInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_create_item, parent, false);
        MemberInfoAdapter.MemberInfoViewHolder evh = new MemberInfoAdapter.MemberInfoViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull MemberInfoViewHolder holder, int position) {

        MemberInfo currentItem = memberInfos.get(position);
        holder.name.setText(currentItem.getFullName());
        holder.userName.setText(currentItem.getUserName());
        holder.role.setText(currentItem.getRole());
        holder.itemView.setTag(currentItem);
    }

    @Override
    public int getItemCount() {
        return memberInfos.size();
    }
}
