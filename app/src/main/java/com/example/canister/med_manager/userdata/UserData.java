package com.example.canister.med_manager.userdata;

/**
 * Created by Canister on 3/29/2018.
 */

public class UserData {

    private String mUserName;
    private String mUserEmail;
    private String mPhotoUrl;


    public UserData(String mUserName, String mUserEmail, String mPhotoUrl) {
        this.mUserName = mUserName;
        this.mUserEmail = mUserEmail;
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getmUserName() {
        return mUserName;
    }
    public String getmUserEmail() {
        return mUserEmail;
    }
    public String getmPhotoUrl() {
        return mPhotoUrl;
    }
}

