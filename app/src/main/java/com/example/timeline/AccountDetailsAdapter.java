package com.example.timeline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
//this apapter will use only for account details spinner.....it won't work for recyler view
public class AccountDetailsAdapter extends ArrayAdapter {
    public AccountDetailsAdapter(Context context, ArrayList<AccountDetails> account_list){
        super(context,0,account_list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    private View initView(int position,View convertView,ViewGroup parent){
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.account_spinner_row, parent, false
            );
        }
        ImageView imageViewFlag = convertView.findViewById(R.id.acc_logo);
        TextView textViewName = convertView.findViewById(R.id.acc_name);
        AccountDetails currentItem = (AccountDetails) getItem(position);
        if (currentItem != null) {
            imageViewFlag.setImageResource(currentItem.getAccLogo());
            textViewName.setText(currentItem.getAccName());
        }
        return convertView;
    }
}
