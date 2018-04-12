package com.example.canister.med_manager.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Canister on 3/31/2018.
 */

public final class MedicineContract{

    //Authority to gain access to to Content Provider
    public static final String AUTHORITY = "com.example.canister.med_manager";

    //Base Uri = "content://" + <AUTHORITY>
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    //Path for accessing the medicines table
    public static final String PATH_MEDICINE = "medicines";

    //Path for accessing the medicine intake table
    public static final String PATH_MEDICINE_INTAKE = "medicines";

    public MedicineContract() {}

    public static final class MedicineEntry implements BaseColumns{

        //Medicine entry Uri = BASE_CONTENT_URI + PATH_MEDICINE
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDICINE).build();

        //Medicine entry Uri = BASE_CONTENT_URI + PATH_MEDICINE
        public static final Uri CONTENT_URI_INTAKE =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MEDICINE_INTAKE).build();

        //Contract for medicine
        public final static String TABLE_NAME = "medicines";

        public final static String _ID = BaseColumns._ID;
        public final static String  COLUMN_MEDICINE_NAME = "name";
        public final static String  COLUMN_MEDICINE_DESCRIPTION="description";
        public final static String  COLUMN_MEDICINE_FREQUENCY= "frequency";
        public final static String  COLUMN_MEDICINE_START_DATE= "startDate";
        public final static String  COLUMN_MEDICINE_END_DATE= "endDate";


        //Contract for medicine Intake
        //Used to track user intake
        public final static String MEDDICINE_INTAKE_TABLE_NAME = "medicine_intake";

        public final static String _INTAKE_ID = BaseColumns._ID;
        public final static String  COLUMN_MEDICINE_INTAKE_NAME = "name";
        public final static String  COLUMN_MEDICINE_INTAKE_DESCRIPTION="description";
        public final static String  COLUMN_MEDICINE_INTAKE_FREQUENCY="frequency";
        public final static String  COLUMN_MEDICINE_INTAKE_DATE= "intake_date";

    }
}
