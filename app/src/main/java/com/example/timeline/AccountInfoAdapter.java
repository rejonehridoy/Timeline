package com.example.timeline;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AccountInfoAdapter extends RecyclerView.Adapter<AccountInfoAdapter.AccountInfoViewHolder> implements Filterable {
    private ArrayList<AccountInfo> accountInfos;
    private ArrayList<AccountInfo> accountInfosFull;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class AccountInfoViewHolder extends RecyclerView.ViewHolder {
        public ImageView account_logo;
        public TextView account_name;
        public TextView account_email;

        public AccountInfoViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            account_logo = itemView.findViewById(R.id.account_info_logo);
            account_name = itemView.findViewById(R.id.account_info_name);
            account_email = itemView.findViewById(R.id.account_info_email);

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

    public AccountInfoAdapter(ArrayList<AccountInfo> accountInfos){
        this.accountInfos = accountInfos;
        accountInfosFull = new ArrayList<>(accountInfos);       //add
    }

    @NonNull
    @Override
    public AccountInfoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_info_item, parent, false);
        AccountInfoViewHolder evh = new AccountInfoViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull AccountInfoViewHolder holder, int position) {
        AccountInfo currentItem = accountInfos.get(position);
        holder.account_logo.setImageResource(convert_image_logo_to_int(currentItem.getAccountLogo()));
        holder.account_name.setText(currentItem.getAccountName() + " : "+currentItem.getAccountUserName());
        if (currentItem.getAccountEmail() == null || currentItem.getAccountEmail().isEmpty()){
            holder.account_email.setText(currentItem.getAccountPhone());
        }else{
            holder.account_email.setText(currentItem.getAccountEmail());
        }


    }

    @Override
    public int getItemCount() {
        return accountInfos.size();
    }

    //add
    @Override
    public Filter getFilter() {
        return accountFilter;
    }

    private Filter accountFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<AccountInfo> filterList = new ArrayList<>();

            if (charSequence == null || charSequence.length() == 0){
                filterList.addAll(accountInfosFull);
            }else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for (AccountInfo info : accountInfosFull){
                    if (info.getAccountName().toLowerCase().contains(filterPattern) ||
                        info.getAccountEmail().toLowerCase().contains(filterPattern) ||
                        info.getAccountUserName().toLowerCase().contains(filterPattern) ||
                        info.getAccountPhone().contains(filterPattern)){
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
            accountInfos.clear();
            accountInfos.addAll((List)filterResults.values);
            notifyDataSetChanged();
        }
    };


    private int convert_image_logo_to_int(String acc){
        if (acc.equals("Facebook")) return R.drawable.acc_facebook;
        else if (acc.equals("Twitter")) return R.drawable.acc_twitter;
        else if (acc.equals("Instagram")) return R.drawable.acc_instagram;
        else if (acc.equals("Skype")) return R.drawable.acc_skype;
        else if (acc.equals("Zoom")) return R.drawable.acc_zoom;
        else if (acc.equals("Gmail")) return R.drawable.acc_gmail_original;
        else if (acc.equals("Yahoo")) return R.drawable.acc_yahoo;
        else if (acc.equals("Outlook")) return R.drawable.acc_outlook;
        else if (acc.equals("Coursera")) return R.drawable.acc_coursera;
        else if (acc.equals("Discord")) return R.drawable.acc_discord;
        else if (acc.equals("Github")) return R.drawable.acc_github;
        else if (acc.equals("Linkedin")) return R.drawable.acc_linkedin;
        else if (acc.equals("Netflix")) return R.drawable.acc_netflix;
        else if (acc.equals("Overleaf")) return R.drawable.acc_overleaf;
        else if (acc.equals("WordPress")) return R.drawable.acc_wordpress;
        else if (acc.equals("Dropbox")) return R.drawable.acc_dropbox;
        else if (acc.equals("Amazon")) return R.drawable.acc_amazon;
        else if (acc.equals("Google+")) return R.drawable.acc_google_plus;
        else if (acc.equals("Bing")) return R.drawable.acc_bing;
        else if (acc.equals("Quora")) return R.drawable.acc_quora;
        else if (acc.equals("Behance")) return R.drawable.acc_behance;
        else if (acc.equals("Uber")) return R.drawable.acc_uber;
        else if (acc.equals("Epic Games")) return R.drawable.acc_epic;
        else if (acc.equals("Origin")) return R.drawable.acc_origin;
        else if (acc.equals("Steam")) return R.drawable.acc_steam;
        else if (acc.equals("Snapchat")) return R.drawable.acc_snapchat;
        else if (acc.equals("Spotify")) return R.drawable.acc_spotify;
        else if (acc.equals("Tiktok")) return R.drawable.acc_tiktok;
        else if (acc.equals("WhatsApp")) return R.drawable.acc_whatsapp;
        else if (acc.equals("Line")) return R.drawable.acc_line;
        else if (acc.equals("Viber")) return R.drawable.acc_viber;
        else if (acc.equals("Wechat")) return R.drawable.acc_wechat;
        else if (acc.equals("UVA")) return R.drawable.acc_uva;
        else if (acc.equals("Hacker Rank")) return R.drawable.acc_hackerrank;
        else if (acc.equals("Code Forces")) return R.drawable.acc_codeforces;
        else if (acc.equals("Mozila Firefox")) return R.drawable.acc_firefox;
        else if (acc.equals("Buffer")) return R.drawable.acc_buffer;
        else if (acc.equals("Flickr")) return R.drawable.acc_flickr;
        else if (acc.equals("Hi5")) return R.drawable.acc_hi5;
        else if (acc.equals("HowCast")) return R.drawable.acc_howcast;
        else if (acc.equals("KickStarter")) return R.drawable.acc_kickstarter;
        else if (acc.equals("plaxo")) return R.drawable.acc_plaxo;
        else if (acc.equals("Reddit")) return R.drawable.acc_reddit;
        else if (acc.equals("Tumblr")) return R.drawable.acc_tumblr;
        else return R.drawable.acc_mail;
    }
}
