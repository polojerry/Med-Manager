package com.example.canister.med_manager.reminder;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.canister.med_manager.database.MedicineContract.MedicineEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Canister on 4/11/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MedicineReminderFirebaseJobService extends JobService {

    //Query and load all medicine data in the background using the projetion
    String[] projects = {
            MedicineEntry.COLUMN_MEDICINE_NAME,
            MedicineEntry.COLUMN_MEDICINE_DESCRIPTION,
            MedicineEntry.COLUMN_MEDICINE_FREQUENCY,
            MedicineEntry.COLUMN_MEDICINE_START_DATE,
            MedicineEntry.COLUMN_MEDICINE_END_DATE,
    };

    private AsyncTask mBackground;
    private Cursor mCursor;


    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(JobParameters job) {

        mBackground = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                //try/catch block to catch any errors in loading data
                try {
                    mCursor = getContentResolver().query(MedicineEntry.CONTENT_URI,
                            projects,
                            null,
                            null,
                            null);

                    //geting the index of each column in the table
                    int nameColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_NAME);
                    int descriptionColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_DESCRIPTION);
                    int frequencyColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_FREQUENCY);
                    int startDateColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_START_DATE);
                    int endDateColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_END_DATE);


                    //using the index to get the value of each column
                    String mMedicineName = mCursor.getString(nameColumnIndex);
                    String mMedicineDescription = mCursor.getString(descriptionColumnIndex);
                    int mMedicineFrequency = mCursor.getInt(frequencyColumnIndex);
                    String mMedicineStartDate = mCursor.getString(startDateColumnIndex);
                    String mMedicineEndDate = mCursor.getString(endDateColumnIndex);

                    NotificationUtils.REMINDER_TITLE = mMedicineName;
                    NotificationUtils.REMINDER_DETAILS = mMedicineDescription;
                    ReminderUtilities.REMINDER_INTERVAL_MINUTES = mMedicineFrequency;

                    Date currentDate = Calendar.getInstance().getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String formattedDate =dateFormat.format(currentDate) ;



                } catch (Exception e) {

                    e.printStackTrace();
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
            }
        };
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }
}
