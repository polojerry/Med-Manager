 package com.example.canister.med_manager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.canister.med_manager.database.MedicineContract.MedicineEntry;
import com.example.canister.med_manager.database.MedicineDbHelper;
import com.example.canister.med_manager.reminder.NotificationUtils;
import com.example.canister.med_manager.reminder.ReminderTasks;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.example.canister.med_manager.database.MedicineContract.MedicineEntry.CONTENT_URI;

 public class AddMedicineActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

     /*Boolean for checking if Edit Text for user input are empty*/
     Boolean mCheckEditText;

     //views to get data from the user
     private EditText mName;
     private EditText mDescription;
     private EditText mFrequency;
     private TextView mStartDate;
     private TextView mEndDate;

     //String to store the date
     private String startDate=null;
     private String endDate=null;

     //instantiating subclass of SQLiteOpenHelper to access our database
     private MedicineDbHelper mDbHelper;

     static Context mContext;


     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //finding the views to get data from
        mName = (EditText) findViewById(R.id.edit_medicine_name);
        mDescription = (EditText) findViewById(R.id.edit_medicine_description);
        mFrequency = (EditText) findViewById(R.id.edit_medicine_frequency);
        mStartDate = (TextView) findViewById(R.id.text_medicine_start_date);
        mEndDate = (TextView) findViewById(R.id.text_medicine_end_date);

        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDatePicker();
            }
        });


        mEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userDatePicker();
            }
        });

    }

     public void userDatePicker() {
         DialogFragment datePicker = new DatePickerFragment();
         datePicker.show(getSupportFragmentManager(),"date Picker");
     }

     @Override
     public void onDateSet(DatePicker datePicker, int year, int month, int day) {

        final String YEAR = year +"" ;
        final String MONTH = (month+1)+"";
        final String DAY =day +"" ;

        final String SEP = "-" ;

        final String DATESELECTED = YEAR+SEP+MONTH+SEP+DAY;

        if(mStartDate.length()<1){
            mStartDate.setText(DATESELECTED);
        }else {
            mEndDate.setText(DATESELECTED);
        }

     }

    /**
     * Gets User Inputs and Inserts it Into the Database
     * */
     private void insertMedicine() {

         /*Checks if all data are entered*/
         CheckEditTextIsEmptyOrNot();

         if (!mCheckEditText){
             Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            /*Stops method from executing further*/
             return;
         }

         //Read the user data and trim to remove spaces
         String nameString = mName.getText().toString().trim();
         String descriptionString = mDescription.getText().toString().trim();
         String frequencyString = mFrequency.getText().toString().trim();
         String startDateString = mStartDate.getText().toString().trim();
         String endDateString = mEndDate.getText().toString().trim();

         //converting the frequency String to Integer
         int frequency = Integer.parseInt(frequencyString);

         //if edit text are empty, we break from the function and create a Toast
         if (nameString.length() == 0 || descriptionString.length() == 0|| frequencyString.length() == 0
                 || startDateString.length() == 0|| endDateString.length() == 0){
             Toast.makeText(this, "Please fill all the required fields", Toast.LENGTH_SHORT).show();
             return;
         }

         //creating content value object where column name are key
         ContentValues values = new ContentValues();
         values.put(MedicineEntry.COLUMN_MEDICINE_NAME, nameString);
         values.put(MedicineEntry.COLUMN_MEDICINE_DESCRIPTION, descriptionString);
         values.put(MedicineEntry.COLUMN_MEDICINE_FREQUENCY, frequency);
         values.put(MedicineEntry.COLUMN_MEDICINE_START_DATE, startDateString);
         values.put(MedicineEntry.COLUMN_MEDICINE_END_DATE, endDateString);

         //inserting the values into the database
         Uri uri = getContentResolver().insert(CONTENT_URI,values );

         //Shows a toast if the data was inserted successfully
         if(uri != null){
             Toast.makeText(this, "Medicine Inserted",Toast.LENGTH_SHORT).show();

         }
         finish();
     }
     public void CheckEditTextIsEmptyOrNot() {
         //Read the user data and trim to remove spaces
         String nameString = mName.getText().toString().trim();
         String descriptionString = mDescription.getText().toString().trim();
         String frequencyString = mFrequency.getText().toString().trim();
         String startDateString = mStartDate.getText().toString().trim();
         String endDateString = mEndDate.getText().toString().trim();

         // Checking whether EditText value is empty or not.
         if (TextUtils.isEmpty(nameString) || TextUtils.isEmpty(descriptionString) || TextUtils.isEmpty(descriptionString)
                 || TextUtils.isEmpty(descriptionString)|| TextUtils.isEmpty(frequencyString)
                 || TextUtils.isEmpty(startDateString)|| TextUtils.isEmpty(endDateString)) {

             // If any of EditText is empty then set variable value as False.
             mCheckEditText = false;

         } else {

             // If any of EditText is filled then set variable value as True.
             mCheckEditText = true;
         }
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.menu_add_medicine,menu);
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {

        int menuItemThatWasSelected = item.getItemId();

        switch (menuItemThatWasSelected){
            case R.id.action_insert:
                insertMedicine();
                finish();
                break;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
        }
        return super.onOptionsItemSelected(item);
     }

 }
