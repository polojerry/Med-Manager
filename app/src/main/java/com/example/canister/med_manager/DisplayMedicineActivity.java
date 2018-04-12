package com.example.canister.med_manager;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import com.example.canister.med_manager.database.MedicineContract.MedicineEntry;
import com.example.canister.med_manager.database.MedicineDbHelper;
import com.example.canister.med_manager.recyclerview.MedicineAdapter;
import com.example.canister.med_manager.reminder.NotificationUtils;
import com.example.canister.med_manager.reminder.ReminderUtilities;
import com.example.canister.med_manager.userdata.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

public class DisplayMedicineActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,MedicineAdapter.MedicineItemClickListener {

    private static final int MEDICINE_LOADER_ID = 1;

    MedicineDbHelper mDbHelper = new MedicineDbHelper(this);

    //Tags to be used during on click intent
    public String TAG_NAME = "NAME";
    public String TAG_DESCRIPTION = "DESCRIPTION";
    public String TAG_FREQUENCY = "FREQUENCY";
    public String TAG_START_DATE = "START_DATE";
    public String TAG_END_DATE = "END_DATE";

    //creating a recycler view object
    private RecyclerView mRecyclerView;

    //creating a recycler view adapter object
    private MedicineAdapter mAdapter;

    // Initialize a Cursor, this will hold all the task data
    Cursor mMedicineData = null;

    //Query and load all medicine data in the background using the projetion
    String[] projects = {
            MedicineEntry.COLUMN_MEDICINE_NAME,
            MedicineEntry.COLUMN_MEDICINE_DESCRIPTION,
            MedicineEntry.COLUMN_MEDICINE_FREQUENCY,
            MedicineEntry.COLUMN_MEDICINE_START_DATE,
            MedicineEntry.COLUMN_MEDICINE_END_DATE,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_medicine);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_medicine);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Adapter accepts two parameters context and onClickListener
        mAdapter = new MedicineAdapter(this,this);
        mRecyclerView.setAdapter(mAdapter);

        /*
         Add a touch helper to the RecyclerView to recognize when a user swipes to delete an item.
         An ItemTouchHelper enables touch behavior (like swipe and move) on each ViewHolder,
         and uses callbacks to signal when a user is performing these actions.
         */
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {


            }
        }).attachToRecyclerView(mRecyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openAddMedicineActivity = new Intent(DisplayMedicineActivity.this, AddMedicineActivity.class);
                startActivity(openAddMedicineActivity);
            }
        });

        /*
         Ensure a loader is initialized and active. If the loader doesn't already exist, one is
         created, otherwise the last created loader is re-used.
         */
        getSupportLoaderManager().initLoader(MEDICINE_LOADER_ID, null, this);

        ReminderUtilities.scheduleReminder(this);

    }

    /**
     * This method is called after this activity has been started
     * so this restarts the loader to re-query the underlying data for any changes.
     */
    @Override
    protected void onStart() {
        super.onStart();
        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(MEDICINE_LOADER_ID, null, this);
    }

    /**
     * This method is called after this activity has been paused or restarted.
     * so this restarts the loader to re-query the underlying data for any changes.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // re-queries for all tasks
        getSupportLoaderManager().restartLoader(MEDICINE_LOADER_ID, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater  = getMenuInflater();
        inflater.inflate(R.menu.menu_display_medicine,menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Cursor searchData;
                searchData =searchMedicine(query);
                if (searchData==null){
                    Toast.makeText(DisplayMedicineActivity.this,"No records found!",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(DisplayMedicineActivity.this, mMedicineData.getCount() + " records found!",Toast.LENGTH_LONG).show();
                }
                mAdapter.swapCursor(searchData);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Cursor searchData;
                searchData=searchMedicine(newText);
                if (searchData!=null){
                    mAdapter.swapCursor(searchData);
                }
                return false;
            }
        });

        return true;
    }

    public Cursor searchMedicine(String search){
        //Open connection to read only
        SQLiteDatabase mDb = mDbHelper.getReadableDatabase();
        String selectQuery =  "SELECT  rowid as " +
                MedicineEntry._ID + "," +
                MedicineEntry.COLUMN_MEDICINE_NAME + "," +
                MedicineEntry.COLUMN_MEDICINE_DESCRIPTION + "," +
                MedicineEntry.COLUMN_MEDICINE_FREQUENCY + "," +
                MedicineEntry.COLUMN_MEDICINE_START_DATE + "," +
                MedicineEntry.COLUMN_MEDICINE_END_DATE +
                " FROM " + MedicineEntry.TABLE_NAME +
                " WHERE " +  MedicineEntry.COLUMN_MEDICINE_NAME + "  LIKE  '%" +search + "%' "
                ;

        Cursor cursor = mDb.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor == null) {
            return null;
        } else if (!cursor.moveToFirst()) {
            cursor.close();
            return null;
        }
        return cursor;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.action_profile:
                Intent openProfile = new Intent (this, ProfileActivity.class);
                startActivity(openProfile);
                break;
            case R.id.action_sign_out:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void signOut() {
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        mAuth.signOut();
        Toast.makeText(this, "Signed Out Successfully", Toast.LENGTH_SHORT).show();
        finish();
    }


    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(this) {

            Cursor loadedCursor;
            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (loadedCursor != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(loadedCursor);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Will implement to load data


                //try/catch block to catch any errors in loading data
                try {
                    return getContentResolver().query(MedicineEntry.CONTENT_URI,
                            projects,
                            null,
                            null,
                            null);

                } catch (Exception e) {

                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                loadedCursor = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Update the data that the adapter uses to create ViewHolders
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    /**
     * OnClick to send the clicked item data to the next activity
     * */
    @Override
    public void onMedicineItemClick(String name, String description, String frequency, String startDate, String endDate) {

        //Strings to store data from the onClick
        String mMedicineName = name;
        String mMedicineDescription = description;
        String mMedicineFrequency = frequency;
        String mMedicineStartDate = startDate;
        String mMedicineEndDate = endDate;


        //Intent to open a new activity to dusplay full details and put extra parameters
        Intent openActivityDetails = new Intent(DisplayMedicineActivity.this, DisplayMedicineDetailsActivity.class);

        openActivityDetails.putExtra(TAG_NAME,mMedicineName );
        openActivityDetails.putExtra(TAG_DESCRIPTION,mMedicineDescription );
        openActivityDetails.putExtra(TAG_FREQUENCY,mMedicineFrequency );
        openActivityDetails.putExtra(TAG_START_DATE,mMedicineStartDate );
        openActivityDetails.putExtra(TAG_END_DATE,mMedicineEndDate );

        startActivity(openActivityDetails);


    }
}
