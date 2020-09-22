package com.example.timeline;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import com.example.timeline.TimelineDB.PasswordManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddNewPassword extends AppCompatActivity {

    private ArrayList<AccountDetails> accountDetails;
    private SQLiteDatabase mDatabase;
    private AccountDetailsAdapter adapter;
    private Spinner account_spinner;
    private Button btnSubmit;
    private TextInputLayout acc_name,acc_userName,acc_email,acc_phone,acc_password,acc_priority,acc_notes;
    private String uid,userName,userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_password);


        account_spinner = findViewById(R.id.ANP_account_spinner);
        acc_name = findViewById(R.id.ANP_account_name);
        acc_userName = findViewById(R.id.ANP_Username);
        acc_email = findViewById(R.id.ANP_Email);
        acc_phone = findViewById(R.id.ANP_phone);
        acc_password = findViewById(R.id.ANP_password);
        acc_priority = findViewById(R.id.ANP_priority);
        acc_notes = findViewById(R.id.ANP_notes);
        btnSubmit = findViewById(R.id.ANP_btnSubmit);

        get_user_info();
        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
        init_spinner_list();

        adapter = new AccountDetailsAdapter(this,accountDetails);
        account_spinner.setAdapter(adapter);

        //action listner for acount name selection
        account_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AccountDetails accountDetails = (AccountDetails) adapterView.getItemAtPosition(i);
                String name =accountDetails.getAccName();
                if (name.equals("Other Account")){
                    acc_name.setVisibility(View.VISIBLE);
                }
                else{
                    acc_name.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create new account
                create_new_account();
            }
        });
    }

    private void create_new_account(){
        if (account_spinner == null || account_spinner.getSelectedItem() == null || account_spinner.getSelectedItem().toString().isEmpty()){
            Toasty.error(getApplicationContext(),"Select Account",Toasty.LENGTH_SHORT).show();
            return;
        }
        if (account_spinner.getSelectedItem().toString().equals("Other Account")){
            if (!check_validation_AccountName()){
                return;
            }
        }
        if (!check_validation_username() | !check_validation_password()){
            return;
        }
        if (acc_email.getEditText().getText() != null && !acc_email.getEditText().getText().toString().isEmpty() && !check_validation_email()){
            return;
        }
        if (!check_validation_email() && !check_validation_phone()){
            return;
        }

        String str_acc_name,str_user_name,str_acc_logo,str_acc_email,str_acc_phone,str_acc_password,str_priority,str_notes;
        if (account_spinner.getSelectedItem().toString().equals("Other Account")){
            str_acc_name = acc_name.getEditText().getText().toString();
            str_acc_logo = "Other Account";
        }else{
            str_acc_name = account_spinner.getSelectedItem().toString();
            str_acc_logo = account_spinner.getSelectedItem().toString();
        }
        str_acc_email = acc_email.getEditText().getText().toString();
        str_user_name = acc_userName.getEditText().getText().toString();
        str_acc_phone = acc_phone.getEditText().getText().toString();
        str_acc_password = acc_password.getEditText().getText().toString();
        str_priority = acc_priority.getEditText().getText().toString();
        str_notes = acc_notes.getEditText().getText().toString();

        // create account in SQLite
        ContentValues cv = new ContentValues();
        cv.put(PasswordManager.COLUMN_USERNAME, userName);
        cv.put(PasswordManager.COLUMN_ACCOUNT_NAME, str_acc_name);
        cv.put(PasswordManager.COLUMN_ACCOUNT_LOGO, str_acc_logo);
        cv.put(PasswordManager.COLUMN_ACCOUNT_USERNAME, str_user_name);
        cv.put(PasswordManager.COLUMN_ACCOUNT_EMAIL, str_acc_email);
        cv.put(PasswordManager.COLUMN_ACCOUNT_PHONE, str_acc_phone);
        cv.put(PasswordManager.COLUMN_ACCOUNT_PASSWORD, str_acc_password);
        cv.put(PasswordManager.COLUMN_ACCOUNT_PRIORITY, str_priority);
        cv.put(PasswordManager.COLUMN_ACCOUNT_NOTES, str_notes);
        cv.put(PasswordManager.COLUMN_ACCOUNT_CREATED_DATE, get_current_Date());
        cv.put(PasswordManager.COLUMN_ACCOUNT_MODIFIED_DATE, "");
        long id = mDatabase.insert(PasswordManager.TABLE_NAME,null,cv);
        Toasty.success(getApplicationContext(),"Account Created"+id,Toasty.LENGTH_SHORT).show();
        this.finish();
    }

    private void init_spinner_list(){
        accountDetails = new ArrayList<>();
        accountDetails.add(new AccountDetails("Facebook",R.drawable.acc_facebook));
        accountDetails.add(new AccountDetails("Twitter",R.drawable.acc_twitter));
        accountDetails.add(new AccountDetails("Instagram",R.drawable.acc_instagram));
        accountDetails.add(new AccountDetails("Skype",R.drawable.acc_skype));
        accountDetails.add(new AccountDetails("Zoom",R.drawable.acc_zoom));
        accountDetails.add(new AccountDetails("Gmail",R.drawable.acc_gmail_original));
        accountDetails.add(new AccountDetails("Yahoo",R.drawable.acc_yahoo));
        accountDetails.add(new AccountDetails("Outlook",R.drawable.acc_outlook));
        accountDetails.add(new AccountDetails("Coursera",R.drawable.acc_coursera));
        accountDetails.add(new AccountDetails("Discord",R.drawable.acc_discord));
        accountDetails.add(new AccountDetails("Github",R.drawable.acc_github));
        accountDetails.add(new AccountDetails("Linkedin",R.drawable.acc_linkedin));
        accountDetails.add(new AccountDetails("Netflix",R.drawable.acc_netflix));
        accountDetails.add(new AccountDetails("Overleaf",R.drawable.acc_overleaf));
        accountDetails.add(new AccountDetails("WordPress",R.drawable.acc_wordpress));
        accountDetails.add(new AccountDetails("Dropbox",R.drawable.acc_dropbox));
        accountDetails.add(new AccountDetails("Amazon",R.drawable.acc_amazon));
        accountDetails.add(new AccountDetails("Google+",R.drawable.acc_google_plus));
        accountDetails.add(new AccountDetails("Bing",R.drawable.acc_bing));
        accountDetails.add(new AccountDetails("Quora",R.drawable.acc_quora));
        accountDetails.add(new AccountDetails("Behance",R.drawable.acc_behance));
        accountDetails.add(new AccountDetails("Uber",R.drawable.acc_uber));
        accountDetails.add(new AccountDetails("Epic Games",R.drawable.acc_epic));
        accountDetails.add(new AccountDetails("Origin",R.drawable.acc_origin));
        accountDetails.add(new AccountDetails("Steam",R.drawable.acc_steam));
        accountDetails.add(new AccountDetails("Snapchat",R.drawable.acc_snapchat));
        accountDetails.add(new AccountDetails("Spotify",R.drawable.acc_spotify));
        accountDetails.add(new AccountDetails("Tiktok",R.drawable.acc_tiktok));
        accountDetails.add(new AccountDetails("WhatsApp",R.drawable.acc_whatsapp));
        accountDetails.add(new AccountDetails("Line",R.drawable.acc_line));
        accountDetails.add(new AccountDetails("Viber",R.drawable.acc_viber));
        accountDetails.add(new AccountDetails("Wechat",R.drawable.acc_wechat));
        accountDetails.add(new AccountDetails("UVA",R.drawable.acc_uva));
        accountDetails.add(new AccountDetails("Hacker Rank",R.drawable.acc_hackerrank));
        accountDetails.add(new AccountDetails("Code Forces",R.drawable.acc_codeforces));
        accountDetails.add(new AccountDetails("Mozila Firefox",R.drawable.acc_firefox));
        accountDetails.add(new AccountDetails("Buffer",R.drawable.acc_buffer));
        accountDetails.add(new AccountDetails("Flickr",R.drawable.acc_flickr));
        accountDetails.add(new AccountDetails("Hi5",R.drawable.acc_hi5));
        accountDetails.add(new AccountDetails("HowCast",R.drawable.acc_howcast));
        accountDetails.add(new AccountDetails("KickStarter",R.drawable.acc_kickstarter));
        accountDetails.add(new AccountDetails("plaxo",R.drawable.acc_plaxo));
        accountDetails.add(new AccountDetails("Reddit",R.drawable.acc_reddit));
        accountDetails.add(new AccountDetails("Tumblr",R.drawable.acc_tumblr));
        accountDetails.add(new AccountDetails("Other Account",R.drawable.acc_mail));    //45 items

    }

    private Boolean check_validation_username() {

        if (acc_userName == null || acc_userName.getEditText() == null || acc_userName.getEditText().getText().toString().isEmpty()) {
            acc_userName.setError("Field cannot be empty");
            return false;
        } else {
            acc_userName.setError(null);
            acc_userName.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean check_validation_password() {

        if (acc_password == null || acc_password.getEditText() == null || acc_password.getEditText().getText().toString().isEmpty()) {
            acc_password.setError("Field cannot be empty");
            return false;
        } else {
            acc_password.setError(null);
            acc_password.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean check_validation_AccountName() {

        if (acc_name == null || acc_name.getEditText() == null || acc_name.getEditText().getText().toString().isEmpty()) {
            acc_name.setError("Field cannot be empty");
            return false;
        } else {
            acc_name.setError(null);
            acc_name.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean check_validation_phone() {

        if (acc_phone == null || acc_phone.getEditText() == null || acc_phone.getEditText().getText().toString().isEmpty()) {
            acc_phone.setError("Fillup phone or email");
            return false;
        } else {
            acc_phone.setError(null);
            acc_phone.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean check_validation_email(){
        String val = acc_email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            acc_email.setError("Fillup email or phone");
            return false;
        } else if (!val.matches(emailPattern)) {
            acc_email.setError("Invalid email address");
            return false;
        } else {
            acc_email.setError(null);
            acc_email.setErrorEnabled(false);
            return true;
        }
    }

    private void get_user_info(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin",false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid","");
            userName = sharedPreferences.getString("userName","");
            userType = sharedPreferences.getString("userType","");
        }else{
            this.finish();
            Toasty.error(getApplicationContext(),"User didn't log in",Toasty.LENGTH_LONG).show();
        }
    }
    public String get_current_Date(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy EEE hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
}
