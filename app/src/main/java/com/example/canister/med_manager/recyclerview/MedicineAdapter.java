package com.example.canister.med_manager.recyclerview;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.canister.med_manager.R;
import com.example.canister.med_manager.database.MedicineContract.MedicineEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Canister on 4/4/2018.
 */

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {


    //Member Variable for OnClickListener
    final private MedicineItemClickListener mOnClickListener;

    //Member Variable for Cursor
    private Cursor mCursor;

    //Member Variable for Context
    private Context mContext;

    //Interface defining our listener
    public interface MedicineItemClickListener {
        /**
         * Method that takes in 5 parameters that is , name, description, frequency,startDate, endDate
         * to be passed to the new Activity
         * */
        void onMedicineItemClick(String name, String description, String frequency, String startDate, String endDate);
    }
    public MedicineAdapter(Context mContext, MedicineItemClickListener listener) {
        this.mContext = mContext;
        this.mOnClickListener = listener;
    }

    @Override
    public MedicineViewHolder onCreateViewHolder(ViewGroup parent, int viewType)  {

        // Inflate the medicine_layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.card_medicine_list_item, parent, false);

        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MedicineViewHolder holder, int position) {

        //geting the index of each column in the table
        int nameColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_NAME);
        int descriptionColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_DESCRIPTION);
        int frequencyColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_FREQUENCY);
        int startDateColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_START_DATE);
        int endDateColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_END_DATE);

        // get to the right location in the cursor
        mCursor.moveToPosition(position);

        //using the index to get the value of each column
        String mMedicineName = mCursor.getString(nameColumnIndex);
        String mMedicineDescription = mCursor.getString(descriptionColumnIndex);
        int mMedicineFrequency = mCursor.getInt(frequencyColumnIndex);
        String mMedicineStartDate = mCursor.getString(startDateColumnIndex);
        String mMedicineEndDate = mCursor.getString(endDateColumnIndex);

        //Set values
        holder.mName.setText(mMedicineName);
        holder.mDescription.setText(mMedicineDescription);
        holder.mFrequency.setText(String.valueOf(mMedicineFrequency));

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
                return 0;
            }
        return mCursor.getCount();
    }
    /**
     * When data changes and a re-query occurs, this function swaps the old Cursor
     * with a newly updated Cursor (Cursor c) that is passed in.
     */
    public Cursor swapCursor(Cursor c) {
        // check if this cursor is the same as the previous cursor (mCursor)
        if (mCursor == c) {
            return null; // bc nothing has changed
        }
        Cursor temp = mCursor;
        this.mCursor = c; // new cursor value assigned

        //check if this is a valid cursor, then update the cursor
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        public TextView mName;
        public TextView mDescription;
        public TextView mFrequency;
        public TextView mStartDate;
        public TextView mEndDate;

        public MedicineViewHolder(View itemView) {
            super(itemView);

            mName = (TextView) itemView.findViewById(R.id.text_medicine_display_name);
            mDescription = (TextView) itemView.findViewById(R.id.text_medicine_display_description);
            mFrequency = (TextView) itemView.findViewById(R.id.text_medicine_display_frequency);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            //get the position of clicked item
            int clickedPosition = getAdapterPosition();

            // get to the position in the cursor of the clicked item
            mCursor.moveToPosition(clickedPosition);

            //getting the index of each column in the table
            int nameColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_NAME);
            int descriptionColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_DESCRIPTION);
            int frequencyColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_FREQUENCY);
            int startDateColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_START_DATE);
            int endDateColumnIndex = mCursor.getColumnIndex(MedicineEntry.COLUMN_MEDICINE_END_DATE);


            //using the index to get the value of each column
            String mMedicineName = mCursor.getString(nameColumnIndex);
            String mMedicineDescription = mCursor.getString(descriptionColumnIndex);
            int mFrequency = mCursor.getInt(frequencyColumnIndex);
            String mMedicineStartDate = mCursor.getString(startDateColumnIndex);
            String mMedicineEndDate = mCursor.getString(endDateColumnIndex);


            //Converting the frequency integer to String
            String mMedicineFrequency = String.valueOf(mFrequency);


            mOnClickListener.onMedicineItemClick(mMedicineName,mMedicineDescription,mMedicineFrequency,mMedicineStartDate,
                    mMedicineEndDate);
        }
    }
}
