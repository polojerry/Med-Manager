package com.example.canister.med_manager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.canister.med_manager.database.MedicineContract.MedicineEntry;

/**
 * Created by Canister on 3/31/2018.
 */

public class MedicineDbHelper extends SQLiteOpenHelper {

    /*Name of Database File*/
    public static final String DATABASE_NAME = "medicine.db";

    /*Database Version*/
    public static final int DATABASE_VERSION = 1;

    public MedicineDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Creating the Medicine table
        //String that contains the SQL statement to CREATE the Medicine table

        String SQL_CREATE_MEDICINE_TABLE = "CREATE TABLE " + MedicineEntry.TABLE_NAME + " ("
                + MedicineEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MedicineEntry.COLUMN_MEDICINE_NAME + " TEXT NOT NULL,"
                + MedicineEntry.COLUMN_MEDICINE_DESCRIPTION + " TEXT NOT NULL,"
                + MedicineEntry.COLUMN_MEDICINE_FREQUENCY + " INTEGER NOT NULL, "
                + MedicineEntry.COLUMN_MEDICINE_START_DATE + " DATE NOT NULL,"
                + MedicineEntry.COLUMN_MEDICINE_END_DATE + " DATE NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_MEDICINE_TABLE);

        //Creating the Medicine Intake table
        //String that contains the SQL statement to CREATE the Medicine Intake table

        String SQL_CREATE_MEDICINE_INTAKE_TABLE = "CREATE TABLE " + MedicineEntry.MEDDICINE_INTAKE_TABLE_NAME + " ("
                + MedicineEntry._INTAKE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MedicineEntry.COLUMN_MEDICINE_INTAKE_NAME + " TEXT NOT NULL,"
                + MedicineEntry.COLUMN_MEDICINE_INTAKE_DESCRIPTION + " TEXT NOT NULL,"
                + MedicineEntry.COLUMN_MEDICINE_INTAKE_FREQUENCY + " TEXT NOT NULL,"
                + MedicineEntry.COLUMN_MEDICINE_INTAKE_DATE + " DATETIME DEFAULT CURRENT_TIMESTAMP);";

        sqLiteDatabase.execSQL(SQL_CREATE_MEDICINE_INTAKE_TABLE);




    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
