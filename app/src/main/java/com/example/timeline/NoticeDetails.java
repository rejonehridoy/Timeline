package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class NoticeDetails extends AppCompatActivity {
    private TextInputLayout subject,category,description,eventTime,eventDate,createdDate,creator,tag,repetation,dayofweek,link,modifiedDate;
    private TextInputEditText etSubject,etCategory,etDescription,etEventDate,etEvenetTime,etCreatedDate,etCreator,etTag,etRepetation,etDayofweek,
    etLink,etModifiedDate;
    private String str_id,str_visible,str_subject,str_category,str_description,str_eventDate,str_eventTime,str_createdDate,
        str_creator,str_tag,str_repetation,str_dayofweek,str_link,str_modifiedDate,str_modifier;
    private Button btnCopy;
    private LinearLayout link_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_details);

        find_view_by_id();
        get_data_from_intent();
        set_info_into_textBox();

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // copy to the clipboard function
                if (str_link != null && !str_link.isEmpty()){
//                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
//                    ClipData clip = ClipData.newPlainText("Link",str_link);
//                    clipboard.setPrimaryClip(clip);

                    Uri webpage = Uri.parse(str_link);
                    Intent webintent = new Intent(Intent.ACTION_VIEW,webpage);
                    startActivity(webintent);
                }


            }
        });
    }
    private  void find_view_by_id(){
        subject = findViewById(R.id.ND_subject);
        etSubject = findViewById(R.id.ND_subject_input);
        description = findViewById(R.id.ND_description);
        etDescription = findViewById(R.id.ND_description_input);
        category = findViewById(R.id.ND_category);
        etCategory = findViewById(R.id.ND_category_input);
        etEventDate = findViewById(R.id.ND_eventDate_input);
        eventDate = findViewById(R.id.ND_eventDate);
        eventTime = findViewById(R.id.ND_eventTime);
        etEvenetTime = findViewById(R.id.ND_eventTime_input);
        createdDate = findViewById(R.id.ND_notice_createdDate);
        etCreatedDate = findViewById(R.id.ND_notice_createdDate_input);
        creator = findViewById(R.id.ND_notice_createdBy);
        etCreator = findViewById(R.id.ND_notice_createdBy_input);
        tag = findViewById(R.id.ND_tag);
        etTag = findViewById(R.id.ND_tag_input);
        repetation = findViewById(R.id.ND_repetation);
        etRepetation = findViewById(R.id.ND_repetation_input);
        dayofweek = findViewById(R.id.ND_dayofweek);
        etDayofweek = findViewById(R.id.ND_dayofweek_input);
        link = findViewById(R.id.ND_link);
        etLink = findViewById(R.id.ND_link_input);
        modifiedDate = findViewById(R.id.ND_notice_modifiedDate);
        etModifiedDate = findViewById(R.id.ND_notice_modifiedDate_input);
        link_layout = findViewById(R.id.ND_link_layout);
        btnCopy = findViewById(R.id.ND_btnCopy);

    }
    private void get_data_from_intent(){
        Intent intent = getIntent();
        str_id =intent.getStringExtra("ID");
        str_visible =intent.getStringExtra("visible");
        str_subject =intent.getStringExtra("subject");
        str_category =intent.getStringExtra("category");
        str_description =intent.getStringExtra("description");
        str_eventDate =intent.getStringExtra("eventDate");
        str_eventTime =intent.getStringExtra("eventTime");
        str_createdDate =intent.getStringExtra("createdDate");
        str_creator =intent.getStringExtra("creator");
        str_tag =intent.getStringExtra("tag");
        str_repetation =intent.getStringExtra("repeation");
        str_dayofweek =intent.getStringExtra("dayofweek");
        str_link =intent.getStringExtra("link");
        str_modifiedDate =intent.getStringExtra("modifiedDate");
        str_modifier =intent.getStringExtra("modifier");
    }
    private void set_info_into_textBox(){
        if (str_visible != null && !str_visible.isEmpty() && str_visible.toLowerCase().equals("visible")){
            if (str_subject != null && !str_subject.isEmpty()){
                subject.setVisibility(View.VISIBLE);
                etSubject.setText(str_subject);
            }
            if (str_category != null && !str_category.isEmpty()){
                category.setVisibility(View.VISIBLE);
                etCategory.setText(str_category);
            }
            if (str_description != null && !str_description.isEmpty()){
                description.setVisibility(View.VISIBLE);
                etDescription.setText(str_description);
            }
            if (str_eventDate != null && !str_eventDate.isEmpty()){
                eventDate.setVisibility(View.VISIBLE);
                etEventDate.setText(str_eventDate);
            }
            if (str_eventTime != null && !str_eventTime.isEmpty()){
                eventTime.setVisibility(View.VISIBLE);
                etEvenetTime.setText(str_eventTime);
            }
            if (str_createdDate != null && !str_createdDate.isEmpty()){
                createdDate.setVisibility(View.VISIBLE);
                etCreatedDate.setText(str_createdDate);
            }
            if (str_creator != null && !str_creator.isEmpty()){
                creator.setVisibility(View.VISIBLE);
                etCreator.setText(str_creator);
            }
            if (str_tag != null && !str_tag.isEmpty()){
                tag.setVisibility(View.VISIBLE);
                etTag.setText(str_tag);
            }
            if (str_dayofweek != null && !str_dayofweek.isEmpty()){
                dayofweek.setVisibility(View.VISIBLE);
                etDayofweek.setText(str_dayofweek);
            }
            if (str_link != null && !str_link.isEmpty()){
                link_layout.setVisibility(View.VISIBLE);
                etLink.setText(str_link);
            }
            if (str_modifiedDate != null && !str_modifiedDate.isEmpty()){
                modifiedDate.setVisibility(View.VISIBLE);
                etModifiedDate.setText(str_modifiedDate);
            }
        }

    }
}
