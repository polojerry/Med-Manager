package com.example.canister.med_manager.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.canister.med_manager.database.MedicineContract.MedicineEntry;
import static com.example.canister.med_manager.database.MedicineContract.MedicineEntry.TABLE_NAME;

/**
 * Created by Canister on 4/6/2018.
 */

public class MedicineContentProvider extends ContentProvider {


    //Defining constants for all data and single data item
    public static final int MEDICINES = 100;
    public static final int MEDICINES_WITH_ID = 101;

    public static final UriMatcher sUriMatcher = buildUriMatcher();

    //buildUriMatcher to associate URI's with their constants
    public static UriMatcher buildUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //match all data
        uriMatcher.addURI(MedicineContract.AUTHORITY, MedicineContract.PATH_MEDICINE, MEDICINES);
        //match single item
        uriMatcher.addURI(MedicineContract.AUTHORITY, MedicineContract.PATH_MEDICINE + "#", MEDICINES_WITH_ID);

        return uriMatcher;
    }

    // Member variable for a MedicineDbHelper
    private MedicineDbHelper mMedicineDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMedicineDbHelper = new MedicineDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        // gives access to medicines database for querying
        final SQLiteDatabase mDb = mMedicineDbHelper.getReadableDatabase();

        // Uri matcher  variable to return a Cursor
        int match = sUriMatcher.match(uri);

        Cursor cursor;

        //query for the all data
        switch (match) {
            //all data
            case MEDICINES:
                cursor =  mDb.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MEDICINES_WITH_ID:
                selection = MedicineEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = mDb.query(MedicineEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
            default:
                throw new UnsupportedOperationException("Cannot Query Unknown uri: " + uri);
        }

        //Set a notification URI on the Cursor
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        // Return a cursor
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase mDb = mMedicineDbHelper.getWritableDatabase();

        // URI matching code fo identifying match for the medicine directory
        int match = sUriMatcher.match(uri);

        // URI to be returned
        Uri returnUri;

        switch (match) {
            case MEDICINES:
                //Insert new values into the database using ContentValues Inserting values into medicines table
                long newRowId = mDb.insert(TABLE_NAME, null, contentValues);

                if ( newRowId == -1) {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                } else {
                    returnUri = ContentUris.withAppendedId(MedicineContract.MedicineEntry.CONTENT_URI, newRowId);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed
        getContext().getContentResolver().notifyChange(uri, null);

        // Return inserted uri
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        // Get access to the database and write URI matching code to recognize a single item
        final SQLiteDatabase db = mMedicineDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        // Keep track of the number of deleted tasks
        int medicineDeleted; // starts as 0

        // Deletes a single item
        switch (match) {
            // Handle the deletion of a single item
            case MEDICINES_WITH_ID:
                // Get the task ID from the URI path
                String id = uri.getPathSegments().get(1);
                // Use selections/selectionArgs to filter for this ID
                medicineDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (medicineDeleted != 0) {
            // set notification if medicine was deleted
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of medicinesdeleted
        return medicineDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
