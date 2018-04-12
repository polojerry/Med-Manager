package com.example.canister.med_manager.database;

/**
 * Created by Canister on 4/11/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import static com.example.canister.med_manager.database.MedicineContract.MedicineEntry.CONTENT_URI;
import static com.example.canister.med_manager.database.MedicineContract.MedicineEntry.CONTENT_URI_INTAKE;

/**
 * Inserts Medicine Intake data into the database
 */
public class InsertMedicineIntake extends AppCompatActivity {

    static Context context;
    public static  void insertMedicineIntake(Context context,String medicineName, String medicineDescription, String dateTime){

    String mMedicineName = medicineName;
    String mMedicineDescription = medicineDescription;
    String mMedicineIntakeDateTime = dateTime;

    //creating content value object where column name are key

        ContentValues values = new ContentValues();
        values.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_INTAKE_NAME, mMedicineName);
        values.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_INTAKE_DESCRIPTION, mMedicineDescription);
        values.put(MedicineContract.MedicineEntry.COLUMN_MEDICINE_INTAKE_DATE, mMedicineIntakeDateTime);

        //inserting the values into the database
        Uri uri = context.getContentResolver().insert(CONTENT_URI_INTAKE,values );

    }
}
