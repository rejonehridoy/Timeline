package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AdminNoticeDetails extends AppCompatActivity implements View.OnClickListener {
    private SearchableSpinner visible_spinner, repetation_spinner, dayof_week_spinner;
    private TextInputLayout visible, subject, category, description, eventTime, eventDate, createdDate, creator, tag, repetation, dayofweek, link, modifiedDate, modifier;
    private TextInputEditText etVisible, etSubject, etCategory, etDescription, etEventDate, etEvenetTime, etCreatedDate, etCreator, etTag, etRepetation, etDayofweek,
            etLink, etModifiedDate, etModifier;
    private String str_id, str_visible, str_subject, str_category, str_description, str_eventDate, str_eventTime, str_createdDate,
            str_creator, str_tag, str_repetation, str_dayofweek="", str_link, str_modifiedDate, str_modifier;
    private Button btnCopy, btnEdit, btnDelete,btnUpdate;
    private LinearLayout link_layout;
    private String userName,userType,uid,userstatus,userEmail,userFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice_details);

        get_user_info();
        find_view_by_id();
        get_data_from_intent();
        set_info_into_textBox();

        setup_privacy();
        setup_spinner();
    }

    private void find_view_by_id() {
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
        modifier = findViewById(R.id.AND_notice_modifiedby);
        etModifier = findViewById(R.id.AND_notice_modifiedby_input);
        link_layout = findViewById(R.id.ND_link_layout);
        visible = findViewById(R.id.AND_visible);
        etVisible = findViewById(R.id.AND_visible_input);
        btnCopy = findViewById(R.id.ND_btnCopy);
        visible_spinner = findViewById(R.id.AND_visible_spinner);
        repetation_spinner = findViewById(R.id.AND_repetation_spinner);
        dayof_week_spinner = findViewById(R.id.AND_dayofWeek_spinner);
        btnEdit = findViewById(R.id.AND_btnEdit);
        btnDelete = findViewById(R.id.AND_btnDelete);
        btnUpdate = findViewById(R.id.AND_btnUpdate);

        btnUpdate.setOnClickListener(this);
        btnCopy.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnDelete.setOnClickListener(this);


    }

    private void get_data_from_intent() {
        Intent intent = getIntent();
        str_id = intent.getStringExtra("ID");
        str_visible = intent.getStringExtra("visible");
        str_subject = intent.getStringExtra("subject");
        str_category = intent.getStringExtra("category");
        str_description = intent.getStringExtra("description");
        str_eventDate = intent.getStringExtra("eventDate");
        str_eventTime = intent.getStringExtra("eventTime");
        str_createdDate = intent.getStringExtra("createdDate");
        str_creator = intent.getStringExtra("creator");
        str_tag = intent.getStringExtra("tag");
        str_repetation = intent.getStringExtra("repeation");
        str_dayofweek = intent.getStringExtra("dayofweek");
        str_link = intent.getStringExtra("link");
        str_modifiedDate = intent.getStringExtra("modifiedDate");
        str_modifier = intent.getStringExtra("modifier");
    }

    private void set_info_into_textBox() {

        if (str_subject != null && !str_subject.isEmpty()) {
            subject.setVisibility(View.VISIBLE);
            etSubject.setText(str_subject);
        }
        if (str_category != null && !str_category.isEmpty()) {
            category.setVisibility(View.VISIBLE);
            etCategory.setText(str_category);
        }
        if (str_description != null && !str_description.isEmpty()) {
            description.setVisibility(View.VISIBLE);
            etDescription.setText(str_description);
        }
        if (str_eventDate != null && !str_eventDate.isEmpty()) {
            eventDate.setVisibility(View.VISIBLE);
            etEventDate.setText(str_eventDate);
        }
        if (str_eventTime != null && !str_eventTime.isEmpty()) {
            eventTime.setVisibility(View.VISIBLE);
            etEvenetTime.setText(str_eventTime);
        }
        if (str_createdDate != null && !str_createdDate.isEmpty()) {
            createdDate.setVisibility(View.VISIBLE);
            etCreatedDate.setText(str_createdDate);
        }
        if (str_creator != null && !str_creator.isEmpty()) {
            creator.setVisibility(View.VISIBLE);
            etCreator.setText(str_creator);
        }
        if (str_tag != null && !str_tag.isEmpty()) {
            tag.setVisibility(View.VISIBLE);
            etTag.setText(str_tag);
        }
        if (str_dayofweek != null && !str_dayofweek.isEmpty()) {
            dayofweek.setVisibility(View.VISIBLE);
            etDayofweek.setText(str_dayofweek);
        }
        if (str_link != null && !str_link.isEmpty()) {
            link_layout.setVisibility(View.VISIBLE);
            etLink.setText(str_link);
        }
        if (str_modifiedDate != null && !str_modifiedDate.isEmpty()) {
            modifiedDate.setVisibility(View.VISIBLE);
            etModifiedDate.setText(str_modifiedDate);
        }
        if (str_repetation != null && !str_repetation.isEmpty()) {
            repetation.setVisibility(View.VISIBLE);
            etRepetation.setText(str_repetation);
        }
        if (str_visible != null && !str_visible.isEmpty()) {
            visible.setVisibility(View.VISIBLE);
            etVisible.setText(str_visible);
        }
        if (str_modifier != null && !str_modifier.isEmpty()) {
            modifier.setVisibility(View.VISIBLE);
            etModifier.setText(str_modifier);
        }


    }

    private void setup_privacy(){
        if (userType != null && !userType.isEmpty() && str_creator != null && userName != null){
            if (userType.toLowerCase().equals("admin") || userName.equals(str_creator)){
                btnEdit.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.VISIBLE);
            }else{
                btnEdit.setVisibility(View.GONE);
                btnDelete.setVisibility(View.GONE);
            }
        }
    }

    private void setup_editable_mode(){
        // set visible all edit boxes
        subject.setVisibility(View.VISIBLE);
        category.setVisibility(View.VISIBLE);
        description.setVisibility(View.VISIBLE);
        eventDate.setVisibility(View.VISIBLE);
        eventTime.setVisibility(View.VISIBLE);
        createdDate.setVisibility(View.VISIBLE);
        creator.setVisibility(View.VISIBLE);
        tag.setVisibility(View.VISIBLE);
        repetation.setVisibility(View.GONE);
        dayofweek.setVisibility(View.GONE);
        visible.setVisibility(View.GONE);
        link_layout.setVisibility(View.VISIBLE);
        link.setVisibility(View.VISIBLE);
        visible_spinner.setVisibility(View.VISIBLE);
        repetation_spinner.setVisibility(View.VISIBLE);
        dayof_week_spinner.setVisibility(View.GONE);

        // set enabled edit boxes
        repetation_spinner.setEnabled(true);
        dayof_week_spinner.setEnabled(true);
        visible_spinner.setEnabled(true);
        etSubject.setEnabled(true);
        etCategory.setEnabled(true);
        etDescription.setEnabled(true);
        etEventDate.setEnabled(true);
        etEvenetTime.setEnabled(true);
        etTag.setEnabled(true);
        etLink.setEnabled(true);
    }

    private void set_disable_editable_mode(){
        etSubject.setEnabled(false);
        etCategory.setEnabled(false);
        etDescription.setEnabled(false);
        etEventDate.setEnabled(false);
        etEvenetTime.setEnabled(false);
        etTag.setEnabled(false);
        etLink.setEnabled(false);
        repetation_spinner.setEnabled(false);
        dayof_week_spinner.setEnabled(false);
        visible_spinner.setEnabled(false);
    }

    private void setup_spinner(){
        //visible spinner
        String[] visible = {"Visible","Not Visible"};
        ArrayAdapter<String> visible_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,visible);
        visible_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        visible_spinner.setAdapter(visible_adapter);

        //Set Selected item of visible spinner
        int spinnerPosition = 0;
        if (str_visible != null && !str_visible.isEmpty()){
            if (str_visible.toLowerCase().equals("yes") || str_visible.toLowerCase().equals("visible")){
                spinnerPosition = visible_adapter.getPosition("Visible");
            }else if(str_visible.toLowerCase().equals("no") || str_visible.toLowerCase().equals("not visible")){
                spinnerPosition = visible_adapter.getPosition("Not Visible");
            }
        }
        visible_spinner.setSelection(spinnerPosition);


        //repetation spinner
        String[] repetation = getResources().getStringArray(R.array.ANC_repetation_string);

        ArrayAdapter<String> repetation_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,repetation);
        repetation_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repetation_spinner.setAdapter(repetation_adapter);
        //set selected item of repetation spinner
        if (str_repetation != null && !str_repetation.isEmpty()){
            int position = repetation_adapter.getPosition(str_repetation);
            repetation_spinner.setSelection(position);
        }


        // day of week spinner
        String[] dayofweek = getResources().getStringArray(R.array.ANC_dayOfWeek_string);

        ArrayAdapter<String> dayofweek_adater = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,dayofweek);
        dayofweek_adater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayof_week_spinner.setAdapter(dayofweek_adater);
        // set selected item of day of week spinner
        if (str_dayofweek != null && !str_dayofweek.isEmpty()){
            int position = dayofweek_adater.getPosition(str_dayofweek);
            dayof_week_spinner.setSelection(position);
        }


        //set selected item listner
        repetation_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i==0){
                    //Once is selected
                    dayof_week_spinner.setVisibility(View.GONE);

                }else{
                    //weekly is selected
                    dayof_week_spinner.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.AND_btnEdit:
                setup_editable_mode();
                btnEdit.setVisibility(View.GONE);
                btnUpdate.setVisibility(View.VISIBLE);
                //now check constrains and update in the firestore

                break;
            case R.id.AND_btnDelete:
                // show confirmation of alert dialogue
                FirebaseFirestore.getInstance().collection("Notices").document(str_id).delete();
                FirebaseDatabase.getInstance().getReference("Notices").child(str_id).removeValue();
                Toasty.success(getApplicationContext(),"Deleted",Toasty.LENGTH_LONG).show();
                AdminNoticeDetails.this.finish();


                break;
            case R.id.ND_btnCopy:
                // copy to the clipboard function
                if (str_link != null && !str_link.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Link", str_link.trim());
                    clipboard.setPrimaryClip(clip);

                    Toasty.info(AdminNoticeDetails.this, "Copied to the clipboard", Toasty.LENGTH_SHORT).show();
                }

                break;
            case R.id.AND_btnUpdate:
                update_info();
                Toasty.success(getApplicationContext(),"Updated",Toasty.LENGTH_LONG).show();
                set_disable_editable_mode();
                btnUpdate.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);

                break;

        }
    }

    private void update_info(){
        if (!check_validation_subject() | !check_validation_description() | !check_validation_category() | !check_validation_tag()) {
            return;
        }
        if (repetation_spinner == null || repetation_spinner.getSelectedItem() == null || repetation_spinner.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Repetation", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (repetation_spinner.getSelectedItem().toString().toLowerCase().equals("weekly")) {
            if (dayof_week_spinner == null || dayof_week_spinner.getSelectedItem() == null || dayof_week_spinner.getSelectedItem().toString().isEmpty()) {
                Toasty.error(getApplicationContext(), "Select Day of Week", Toasty.LENGTH_SHORT).show();
                return;
            }else{
                str_dayofweek = dayof_week_spinner.getSelectedItem().toString();
            }
        }
        str_subject = subject.getEditText().getText().toString().trim();
        str_category = category.getEditText().getText().toString();
        str_description = description.getEditText().getText().toString();
        str_tag = tag.getEditText().getText().toString();
        str_repetation = repetation_spinner.getSelectedItem().toString();
        str_modifier = userName;
        str_modifiedDate = get_current_Date();
        str_visible = visible_spinner.getSelectedItem().toString();

        if (eventDate !=null && eventDate.getEditText() !=null && !eventDate.getEditText().getText().toString().isEmpty()){
            str_eventDate = eventDate.getEditText().getText().toString();
        }
        if (eventTime !=null && eventTime.getEditText() !=null && !eventTime.getEditText().getText().toString().isEmpty()){
            str_eventTime = eventTime.getEditText().getText().toString();
        }
        if (link !=null && link.getEditText() !=null && !link.getEditText().getText().toString().isEmpty()){
            str_link = link.getEditText().getText().toString();
        }

        //update data into database
        NoticeInfo notice = new NoticeInfo(str_id,str_visible,str_subject,str_category,str_description,str_eventDate,str_eventTime,
                str_tag,str_creator,str_createdDate,str_repetation,str_modifier,str_modifiedDate,str_dayofweek,str_link);

        FirebaseFirestore.getInstance().collection("Notices").document(str_id).set(notice);
        FirebaseDatabase.getInstance().getReference("Notices").child(str_id).setValue(notice);

        etModifiedDate.setText(get_current_Date());
        etModifier.setText(userName);


    }
    private Boolean check_validation_subject() {

        if (subject == null || subject.getEditText() == null || subject.getEditText().getText().toString().isEmpty()) {
            subject.setError("Field cannot be empty");
            return false;
        } else {
            subject.setError(null);
            subject.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean check_validation_category() {

        if (category == null || category.getEditText() == null || category.getEditText().getText().toString().isEmpty()) {
            category.setError("Field cannot be empty");
            return false;
        } else {
            category.setError(null);
            category.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean check_validation_description() {

        if (description == null || description.getEditText() == null || description.getEditText().getText().toString().isEmpty()) {
            description.setError("Field cannot be empty");
            return false;
        } else {
            description.setError(null);
            description.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean check_validation_tag() {

        if (tag == null || tag.getEditText() == null || tag.getEditText().getText().toString().isEmpty()) {
            tag.setError("Field cannot be empty");
            return false;
        } else {
            tag.setError(null);
            tag.setErrorEnabled(false);
            return true;
        }
    }

    private String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }

    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");
            userType = sharedPreferences.getString("userType","");
            userstatus = sharedPreferences.getString("status","");
            //userPassword = sharedPreferences.getString("password","");
            userEmail = sharedPreferences.getString("email","");
            userFullName = sharedPreferences.getString("fullName","");

        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }
}
