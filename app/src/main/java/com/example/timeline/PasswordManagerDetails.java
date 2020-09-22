package com.example.timeline;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class PasswordManagerDetails extends AppCompatActivity implements View.OnClickListener {

    private SQLiteDatabase mDatabase;
    private SearchableSpinner account_spinner;
    private ArrayList<AccountDetails> accountDetails;
    private AccountDetailsAdapter adapter;
    private TextInputLayout username, email, phone, priority, notes, password, account_name;
    private TextInputEditText ETusername, ETemail, ETphone, ETpriority, ETnotes, ETpassword, ETcreatedDate, ETmodifiedDate;
    private Button btnEdit, btnCopy, btnDelete, btnCancel,btnUpdate;
    private String uid, userName, userType;
    private int ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_manager_details);
        account_spinner = findViewById(R.id.PMD_account_spinner);
        username = findViewById(R.id.PMD_username);
        ETusername = findViewById(R.id.PMD_username_intput);
        ETusername = findViewById(R.id.PMD_username_intput);
        email = findViewById(R.id.PMD_email);
        ETemail = findViewById(R.id.PMD_email_input);
        phone = findViewById(R.id.PMD_phone);
        ETphone = findViewById(R.id.PMD_phone_input);
        priority = findViewById(R.id.PMD_priority);
        ETpriority = findViewById(R.id.PMD_priority_input);
        notes = findViewById(R.id.PMD_notes);
        ETnotes = findViewById(R.id.PMD_notes_input);
        password = findViewById(R.id.PMD_password);
        ETpassword = findViewById(R.id.PMD_pasword_input);
        ETcreatedDate = findViewById(R.id.PMD_createdDate_input);
        ETmodifiedDate = findViewById(R.id.PMD_modifiedDate_input);
        btnEdit = findViewById(R.id.PMD_btn_edit_submit);
        btnCopy = findViewById(R.id.PMD_btnCopy);
        btnDelete = findViewById(R.id.PMD_btn_delete);
        btnUpdate = findViewById(R.id.PMD_btn_update);
        btnCancel = findViewById(R.id.PMD_btn_cancel);
        account_name = findViewById(R.id.PMD_account_name);
        account_spinner.setEnabled(false);          //in non edited mode spinner should be disabled

        get_user_info();
        TimelineDBHelper dbHelper = new TimelineDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        Intent intent = getIntent();
        ID = intent.getIntExtra("ID", 0);
        if (ID < 0) {
            Toasty.error(PasswordManagerDetails.this, "Invalid Operation Occurred", Toasty.LENGTH_SHORT).show();
            this.finish();
        }


        get_data_from_database();


        btnUpdate.setOnClickListener(this);
        btnCopy.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        //action listner for acount name selection
        account_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                AccountDetails accountDetails = (AccountDetails) adapterView.getItemAtPosition(i);
                String name = accountDetails.getAccName();
                int logo = accountDetails.getAccLogo();

                if (name.equals("Other Account")) {
                    account_name.setVisibility(View.VISIBLE);
                } else {
                    account_name.setVisibility(View.GONE);
                }
                // value will be get in name varible......

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void get_data_from_database() {
        // this method will retrive all data from database from id

        final String SQL_QUERY = "SELECT * FROM " + TimelineDB.PasswordManager.TABLE_NAME + " WHERE " + TimelineDB.PasswordManager._ID + " = " + ID;
        Cursor cursor = mDatabase.rawQuery(SQL_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                String str_userName = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_USERNAME));
                String str_acc_name = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_NAME));
                String str_acc_Logo = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_LOGO));
                String str_acc_userName = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_USERNAME));
                String str_acc_email = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_EMAIL));
                String str_acc_phone = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_PHONE));
                String str_acc_password = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_PASSWORD));
                String str_acc_priority = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_PRIORITY));
                String str_acc_notes = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_NOTES));
                String str_acc_createdDate = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_CREATED_DATE));
                String str_acc_modifiedDate = cursor.getString(cursor.getColumnIndex(TimelineDB.PasswordManager.COLUMN_ACCOUNT_MODIFIED_DATE));

                //setup data
                ETusername.setText(str_acc_userName);
                ETemail.setText(str_acc_email);
                ETphone.setText(str_acc_phone);
                ETpriority.setText(str_acc_priority);
                ETpassword.setText(str_acc_password);
                ETnotes.setText(str_acc_notes);
                ETcreatedDate.setText(create_Date_format(str_acc_createdDate));
                ETmodifiedDate.setText(create_Date_format(str_acc_modifiedDate));
                setup_account_spinner(str_acc_name);
            } while (cursor.moveToNext());
        }

    }


    private void setup_account_spinner(String name) {
        init_spinner_list();

        adapter = new AccountDetailsAdapter(this, accountDetails);
        account_spinner.setAdapter(adapter);

        // get saved item from user
        int position = accountDetails.size() - 1;
        for (int i = 0; i < accountDetails.size(); i++) {
            if (accountDetails.get(i).getAccName().equals(name)) {           // user account selected
                position = i;
            }
        }
        // set selected item
        account_spinner.setSelection(position);

    }

    private void init_spinner_list() {
        accountDetails = new ArrayList<>();
        accountDetails.add(new AccountDetails("Facebook", R.drawable.acc_facebook));
        accountDetails.add(new AccountDetails("Twitter", R.drawable.acc_twitter));
        accountDetails.add(new AccountDetails("Instagram", R.drawable.acc_instagram));
        accountDetails.add(new AccountDetails("Skype", R.drawable.acc_skype));
        accountDetails.add(new AccountDetails("Zoom", R.drawable.acc_zoom));
        accountDetails.add(new AccountDetails("Gmail", R.drawable.acc_gmail_original));
        accountDetails.add(new AccountDetails("Yahoo", R.drawable.acc_yahoo));
        accountDetails.add(new AccountDetails("Outlook", R.drawable.acc_outlook));
        accountDetails.add(new AccountDetails("Coursera", R.drawable.acc_coursera));
        accountDetails.add(new AccountDetails("Discord", R.drawable.acc_discord));
        accountDetails.add(new AccountDetails("Github", R.drawable.acc_github));
        accountDetails.add(new AccountDetails("Linkedin", R.drawable.acc_linkedin));
        accountDetails.add(new AccountDetails("Netflix", R.drawable.acc_netflix));
        accountDetails.add(new AccountDetails("Overleaf", R.drawable.acc_overleaf));
        accountDetails.add(new AccountDetails("WordPress", R.drawable.acc_wordpress));
        accountDetails.add(new AccountDetails("Dropbox", R.drawable.acc_dropbox));
        accountDetails.add(new AccountDetails("Amazon", R.drawable.acc_amazon));
        accountDetails.add(new AccountDetails("Google+", R.drawable.acc_google_plus));
        accountDetails.add(new AccountDetails("Bing", R.drawable.acc_bing));
        accountDetails.add(new AccountDetails("Quora", R.drawable.acc_quora));
        accountDetails.add(new AccountDetails("Behance", R.drawable.acc_behance));
        accountDetails.add(new AccountDetails("Uber", R.drawable.acc_uber));
        accountDetails.add(new AccountDetails("Epic Games", R.drawable.acc_epic));
        accountDetails.add(new AccountDetails("Origin", R.drawable.acc_origin));
        accountDetails.add(new AccountDetails("Steam", R.drawable.acc_steam));
        accountDetails.add(new AccountDetails("Snapchat", R.drawable.acc_snapchat));
        accountDetails.add(new AccountDetails("Spotify", R.drawable.acc_spotify));
        accountDetails.add(new AccountDetails("Tiktok", R.drawable.acc_tiktok));
        accountDetails.add(new AccountDetails("WhatsApp", R.drawable.acc_whatsapp));
        accountDetails.add(new AccountDetails("Line", R.drawable.acc_line));
        accountDetails.add(new AccountDetails("Viber", R.drawable.acc_viber));
        accountDetails.add(new AccountDetails("Wechat", R.drawable.acc_wechat));
        accountDetails.add(new AccountDetails("UVA", R.drawable.acc_uva));
        accountDetails.add(new AccountDetails("Hacker Rank", R.drawable.acc_hackerrank));
        accountDetails.add(new AccountDetails("Code Forces", R.drawable.acc_codeforces));
        accountDetails.add(new AccountDetails("Mozila Firefox", R.drawable.acc_firefox));
        accountDetails.add(new AccountDetails("Buffer", R.drawable.acc_buffer));
        accountDetails.add(new AccountDetails("Flickr", R.drawable.acc_flickr));
        accountDetails.add(new AccountDetails("Hi5", R.drawable.acc_hi5));
        accountDetails.add(new AccountDetails("HowCast", R.drawable.acc_howcast));
        accountDetails.add(new AccountDetails("KickStarter", R.drawable.acc_kickstarter));
        accountDetails.add(new AccountDetails("plaxo", R.drawable.acc_plaxo));
        accountDetails.add(new AccountDetails("Reddit", R.drawable.acc_reddit));
        accountDetails.add(new AccountDetails("Tumblr", R.drawable.acc_tumblr));
        accountDetails.add(new AccountDetails("Other Account", R.drawable.acc_mail));    //44 items

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.PMD_btn_edit_submit:
                    // edited mode
                    Toasty.info(PasswordManagerDetails.this, "You are in edit mode", Toasty.LENGTH_SHORT).show();
                    btnUpdate.setVisibility(View.VISIBLE);
                    account_spinner.setEnabled(true);
                    ETusername.setEnabled(true);
                    ETemail.setEnabled(true);
                    ETphone.setEnabled(true);
                    ETpassword.setEnabled(true);
                    ETnotes.setEnabled(true);
                    ETpriority.setEnabled(true);
                    btnEdit.setVisibility(View.GONE);
                    btnDelete.setVisibility(View.GONE);
                    btnCancel.setVisibility(View.VISIBLE);

                break;
            case R.id.PMD_btn_update:
                // non edited mode/submited mode
                //firstly have to check validation of edit box
                update_info_sqlite();

                break;

            case R.id.PMD_btnCopy:
                // copy to the clipboard function
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Password", password.getEditText().getText().toString());
                clipboard.setPrimaryClip(clip);

                Toasty.info(PasswordManagerDetails.this, "Copied to the clipboard", Toasty.LENGTH_SHORT).show();
                break;
            case R.id.PMD_btn_cancel:
                btnUpdate.setVisibility(View.GONE);
                btnEdit.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.GONE);
                btnDelete.setVisibility(View.VISIBLE);
                account_spinner.setEnabled(false);
                account_name.getEditText().setEnabled(false);
                ETusername.setEnabled(false);
                ETemail.setEnabled(false);
                ETphone.setEnabled(false);
                ETpassword.setEnabled(false);
                ETnotes.setEnabled(false);
                ETpriority.setEnabled(false);
                break;

            case R.id.PMD_btn_delete:
                //confirmation alert dialogue will appear for confirmation
                delete_confiramtion_dialogue();

                break;
        }

    }

    private void update_info_sqlite() {
        if (account_spinner == null || account_spinner.getSelectedItem() == null || account_spinner.getSelectedItem().toString().isEmpty()) {
            Toasty.error(getApplicationContext(), "Select Account", Toasty.LENGTH_SHORT).show();
            return;
        }
        if (account_spinner.getSelectedItem().toString().equals("Other Account")) {
            if (!check_validation_AccountName()) {
                return;
            }
        }
        if (!check_validation_username() | !check_validation_password()) {
            return;
        }
        if (email.getEditText().getText() != null && !email.getEditText().getText().toString().isEmpty() && !check_validation_email()){
            return;
        }
        if (!check_validation_email() && !check_validation_phone()) {
            return;
        }
        String str_acc_name, str_user_name, str_acc_logo, str_acc_email, str_acc_phone, str_acc_password, str_priority, str_notes, str_modifiedDate;
        if (account_spinner.getSelectedItem().toString().equals("Other Account")) {
            str_acc_name = account_name.getEditText().getText().toString();
            str_acc_logo = "Other Account";
        } else {
            str_acc_name = account_spinner.getSelectedItem().toString();
            str_acc_logo = account_spinner.getSelectedItem().toString();
        }
        str_acc_email = email.getEditText().getText().toString();
        str_user_name = username.getEditText().getText().toString();
        str_acc_phone = phone.getEditText().getText().toString();
        str_acc_password = password.getEditText().getText().toString();
        str_priority = priority.getEditText().getText().toString();
        str_notes = notes.getEditText().getText().toString();

        ContentValues cv = new ContentValues();
        cv.put(TimelineDB.PasswordManager.COLUMN_USERNAME, userName);
        cv.put(TimelineDB.PasswordManager.COLUMN_ACCOUNT_NAME, str_acc_name);
        cv.put(TimelineDB.PasswordManager.COLUMN_ACCOUNT_LOGO, str_acc_logo);
        cv.put(TimelineDB.PasswordManager.COLUMN_ACCOUNT_USERNAME, str_user_name);
        cv.put(TimelineDB.PasswordManager.COLUMN_ACCOUNT_EMAIL, str_acc_email);
        cv.put(TimelineDB.PasswordManager.COLUMN_ACCOUNT_PHONE, str_acc_phone);
        cv.put(TimelineDB.PasswordManager.COLUMN_ACCOUNT_PASSWORD, str_acc_password);
        cv.put(TimelineDB.PasswordManager.COLUMN_ACCOUNT_PRIORITY, str_priority);
        cv.put(TimelineDB.PasswordManager.COLUMN_ACCOUNT_NOTES, str_notes);
        cv.put(TimelineDB.PasswordManager.COLUMN_ACCOUNT_MODIFIED_DATE, get_current_Date());

        mDatabase.update(TimelineDB.PasswordManager.TABLE_NAME, cv, TimelineDB.PasswordManager._ID + "=" + ID, null);
        Toasty.success(PasswordManagerDetails.this, "updated", Toasty.LENGTH_SHORT).show();

        btnEdit.setVisibility(View.VISIBLE);
        btnUpdate.setVisibility(View.GONE);
        account_spinner.setEnabled(false);
        ETusername.setEnabled(false);
        ETemail.setEnabled(false);
        ETphone.setEnabled(false);
        ETpassword.setEnabled(false);
        ETnotes.setEnabled(false);
        ETpriority.setEnabled(false);
        btnDelete.setVisibility(View.VISIBLE);
        btnCancel.setVisibility(View.GONE);

    }

    private void delete_confiramtion_dialogue() {

        AlertDialog.Builder alertDialogBuilder;
        alertDialogBuilder = new AlertDialog.Builder(this)
                .setTitle("Delete Account")
                .setMessage("Are you sure to delete this account info? If deleted it can not be undone")
                .setIcon(R.drawable.ic_delete_forever_red)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Yes button clicked
                        //update work completion info in request table
                        delete_info_sqlite();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //No button clicked
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();


    }

    private void delete_info_sqlite() {
        mDatabase.delete(TimelineDB.PasswordManager.TABLE_NAME, TimelineDB.PasswordManager._ID + "=" + ID, null);
        Toasty.success(getApplicationContext(), "Deleted", Toasty.LENGTH_SHORT).show();
        this.finish();
    }

    private Boolean check_validation_username() {

        if (username == null || username.getEditText() == null || username.getEditText().getText().toString().isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean check_validation_password() {

        if (password == null || password.getEditText() == null || password.getEditText().getText().toString().isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean check_validation_AccountName() {

        if (account_name == null || account_name.getEditText() == null || account_name.getEditText().getText().toString().isEmpty()) {
            account_name.setError("Field cannot be empty");
            return false;
        } else {
            account_name.setError(null);
            account_name.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean check_validation_phone() {

        if (phone == null || phone.getEditText() == null || phone.getEditText().getText().toString().isEmpty()) {
            phone.setError("Fillup phone or email");
            return false;
        } else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean check_validation_email() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Fillup email or phone");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    public String get_current_Date() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy EEE hh:mm a");
        //SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return formatter.format(date);

    }
    private String create_Date_format(String DateTime){
        //format july 22,2020, wed at 05:29 PM
        // 22-08-2020 Wed 05:34 PM
        if (!DateTime.isEmpty()){
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

    private void get_user_info() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        Boolean isLoggedin = sharedPreferences.getBoolean("isLoggedin", false);
        if (isLoggedin && sharedPreferences.contains("uid") && sharedPreferences.contains("userName")) {
            uid = sharedPreferences.getString("uid", "");
            userName = sharedPreferences.getString("userName", "");
            userType = sharedPreferences.getString("userType", "");
        } else {
            this.finish();
            Toasty.error(getApplicationContext(), "User didn't log in", Toasty.LENGTH_LONG).show();
        }
    }
}
