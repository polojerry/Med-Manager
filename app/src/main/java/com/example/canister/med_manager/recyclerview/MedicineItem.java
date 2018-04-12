package com.example.canister.med_manager.recyclerview;

/**
 * Created by Canister on 4/4/2018.
 */

/**
 * A class that contains the variables describing the medicine fileds
 * */

public class MedicineItem {

    private String mName;
    private String mDescription;
    private int mFrequency;
    private String mStartDate;
    private String mEndDate;

    public MedicineItem(String mName, String mDescription, int mFrequency, String mStartDate, String mEndDate) {
        this.mName = mName;
        this.mDescription = mDescription;
        this.mFrequency = mFrequency;
        this.mStartDate = mStartDate;
        this.mEndDate = mEndDate;
    }

    public String getmName() {
        return mName;
    }

    public String getmDescription() {
        return mDescription;
    }

    public int getmFrequency() {
        return mFrequency;
    }

    public String getmStartDate() {
        return mStartDate;
    }

    public String getmEndDate() {
        return mEndDate;
    }
}

