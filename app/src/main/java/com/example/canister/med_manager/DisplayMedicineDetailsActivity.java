package com.example.canister.med_manager;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayMedicineDetailsActivity extends AppCompatActivity {

    TextView mName;
    TextView mDescription;
    TextView mFrequency;
    TextView mStartDate;
    TextView mEndDate;

    //Tags to be used during on click intent
    public String TAG_NAME = "NAME";
    public String TAG_DESCRIPTION = "DESCRIPTION";
    public String TAG_FREQUENCY = "FREQUENCY";
    public String TAG_START_DATE = "START_DATE";
    public String TAG_END_DATE = "END_DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_medicine_details);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mName = (TextView) findViewById(R.id.text_medicine_details_name);
        mDescription = (TextView) findViewById(R.id.text_medicine_description);
        mFrequency = (TextView) findViewById(R.id.text_medicine_details_frequency);
        mStartDate = (TextView) findViewById(R.id.text_medicine_details_start_date);
        mEndDate = (TextView) findViewById(R.id.text_medicine_details_end_date);

        Intent getDetailsFromDisplayMedicineActivity = getIntent();
        Bundle dataFromDisplayMedicineActivity = getDetailsFromDisplayMedicineActivity.getExtras();

        String name = (String) dataFromDisplayMedicineActivity.get(TAG_NAME);
        String description =(String) dataFromDisplayMedicineActivity.get(TAG_DESCRIPTION);
        String frequency = (String) dataFromDisplayMedicineActivity.get(TAG_FREQUENCY);
        String startDate = (String) dataFromDisplayMedicineActivity.get(TAG_START_DATE);
        String endDate = (String) dataFromDisplayMedicineActivity.get(TAG_END_DATE);

        mName.setText(name);
        mDescription.setText(description);
        mFrequency.setText(frequency);
        mStartDate.setText(startDate);
        mEndDate.setText(endDate);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }
}
