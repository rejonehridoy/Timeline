package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdminTaskDetails extends AppCompatActivity {
    private TextInputEditText etName,etSerial,etCategory,etStatus,etPriority,etDescription,etSolution,etDeadline,etAssigning
            ,etPoints,etIssues,etLink,etComments,etModifiedDate,etModifiedUser,etCreatedDate,etCreatedUser;
    private String str_tid,str_pid,str_name,str_serial,str_catrgory,str_status,str_priority,str_description,str_soluton,str_deadline,str_assigning,str_points,
            str_issues,str_link,str_createdDate,str_createdUser,str_modifiedDate,str_modifiedUser,str_comments,str_modifiedVersion;
    private Button btnCopy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_task_details);

        find_viewBy_id();
        get_info_from_intent();

        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (str_link != null && !str_link.isEmpty()) {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Link", str_link);
                    clipboard.setPrimaryClip(clip);

                    Uri webpage = Uri.parse(str_link);
                    Intent webintent = new Intent(Intent.ACTION_VIEW,webpage);
                    startActivity(webintent);
                }else{
                    Toasty.error(getApplicationContext(),"Link empty",Toasty.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void find_viewBy_id(){

        etName = findViewById(R.id.ATD_name_input);
        etSerial = findViewById(R.id.ATD_serial_input);
        etCategory = findViewById(R.id.ATD_category_input);
        etStatus = findViewById(R.id.ATD_status_input);
        etPriority = findViewById(R.id.ATD_priority_input);
        etDescription = findViewById(R.id.ATD_description_input);
        etSolution = findViewById(R.id.ATD_solution_input);
        etDeadline = findViewById(R.id.ATD_deadline_input);
        etAssigning = findViewById(R.id.ATD_assigningTo_input);
        etPoints = findViewById(R.id.ATD_points_input);
        etIssues = findViewById(R.id.ATD_issues_input);
        etLink = findViewById(R.id.ATD_link_input);
        etCategory = findViewById(R.id.ATD_category_input);
        etComments = findViewById(R.id.ATD_comments_input);
        etModifiedDate = findViewById(R.id.ATD_modifiedDate_input);
        etModifiedUser = findViewById(R.id.ATD_modifiedUser_input);
        etCreatedDate = findViewById(R.id.ATD_createdDate_input);
        etCreatedUser = findViewById(R.id.ATD_createdUser_input);
        btnCopy = findViewById(R.id.ATD_btnCopy);
    }

    private void get_info_from_intent(){
        Intent intent = getIntent();
        str_pid = intent.getStringExtra("Pid");
        str_tid = intent.getStringExtra("Tid");
        str_name = intent.getStringExtra("name");
        str_serial = intent.getStringExtra("serial");
        str_catrgory = intent.getStringExtra("category");
        str_description = intent.getStringExtra("description");
        str_soluton = intent.getStringExtra("solution");
        str_deadline = intent.getStringExtra("deadline");
        str_assigning = intent.getStringExtra("assigner");
        str_points = intent.getStringExtra("points");
        str_issues = intent.getStringExtra("issues");
        str_link = intent.getStringExtra("link");
        str_priority = intent.getStringExtra("priority");
        str_status = intent.getStringExtra("status");
        str_modifiedDate = intent.getStringExtra("modifiedDate");
        str_modifiedUser = intent.getStringExtra("modifiedUser");
        str_createdDate = intent.getStringExtra("createdDate");
        str_createdUser = intent.getStringExtra("createdUser");
        str_comments = intent.getStringExtra("comments");

        set_value_into_textBoxes();

    }
    private void set_value_into_textBoxes(){
        if (str_name != null && !str_name.isEmpty()){
            etName.setText(str_name);
        }
        if (str_serial != null && !str_serial.isEmpty()){
            etSerial.setText(str_serial);
        }
        if (str_catrgory != null && !str_catrgory.isEmpty()){
            etCategory.setText(str_catrgory);
        }
        if (str_description != null && !str_description.isEmpty()){
            etDescription.setText(str_description);
        }
        if (str_soluton != null && !str_soluton.isEmpty()){
            etSolution.setText(str_soluton);
        }
        if (str_deadline != null && !str_deadline.isEmpty()){
            etDeadline.setText(str_deadline);
        }
        if (str_assigning != null && !str_assigning.isEmpty()){
            etAssigning.setText(str_assigning);
        }
        if (str_points != null && !str_points.isEmpty()){
            etPoints.setText(str_points);
        }
        if (str_issues != null && !str_issues.isEmpty()){
            etIssues.setText(str_issues);
        }
        if (str_link != null && !str_link.isEmpty()){
            etLink.setText(str_link);
        }
        if (str_priority != null && !str_priority.isEmpty()){
            etPriority.setText(str_priority);
        }
        if (str_status != null && !str_status.isEmpty()){
            etStatus.setText(str_status);
        }
        if (str_modifiedDate != null && !str_modifiedDate.isEmpty()){
            etModifiedDate.setText(convert_Date_format(str_modifiedDate));
        }
        if (str_modifiedUser != null && !str_modifiedUser.isEmpty()){
            etModifiedUser.setText(str_modifiedUser);
        }
        if (str_createdDate != null && !str_createdDate.isEmpty()){
            etCreatedDate.setText(convert_Date_format(str_createdDate));
        }
        if (str_createdUser != null && !str_createdUser.isEmpty()){
            etCreatedUser.setText(str_createdUser);
        }
        if (str_comments != null && !str_comments.isEmpty()){
            etComments.setText(str_comments);
        }
    }
    public String get_current_DateOnly(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
    private String convert_Date_format(String DateTime){
        //format july 22,2020, wed at 05:29 PM
        // 22-08-2020 Wed 05:34 PM
        if (!DateTime.isEmpty()){
            String localDate = DateTime.substring(0,10);
            String localTime = DateTime.substring(14,23);
            String day = DateTime.substring(10,14);
            if (localDate.equals(get_current_DateOnly())){
                return "Today at "+localTime;
            }
            try {
                DateFormat originalFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                DateFormat targetFormat1 = new SimpleDateFormat("dd-MM-yyyy");
                DateFormat targetFormat2 = new SimpleDateFormat("MMMM dd");
                DateFormat targetFormat3 = new SimpleDateFormat("MMMM dd, yyyy");
                Date date = originalFormat.parse(localDate);
                if (localDate.contains(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)))){
                    return targetFormat2.format(date)+","+day+" at "+localTime;
                }else{
                    return targetFormat3.format(date)+","+day+" at "+localTime;
                }


            }catch (Exception e){
                Log.d("ERROR",e.getMessage());
            }
        }


        return DateTime;
    }
}
