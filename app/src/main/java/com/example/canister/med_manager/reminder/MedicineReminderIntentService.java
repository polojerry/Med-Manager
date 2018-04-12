package com.example.canister.med_manager.reminder;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by Canister on 4/11/2018.
 */

public class MedicineReminderIntentService extends IntentService {

    Context context;
    //Tags to be used during  intent
    public String TAG_NAME = "NAME";
    public String TAG_DESCRIPTION = "DESCRIPTION";
    public String TAG_DATE = "DATE";
    public String TAG_FREQUENCY = "FREQUENCY";

    public MedicineReminderIntentService() {
        super("MedicineReminderIntentService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String action = intent.getAction();

        Bundle dataFromIntent = intent.getExtras();
        String name = dataFromIntent.get(TAG_NAME).toString();
        String description = dataFromIntent.get(TAG_DESCRIPTION).toString();
        String stringFrequency = dataFromIntent.get(TAG_FREQUENCY).toString();
        String date = dataFromIntent.get(TAG_DATE).toString();

        int frequency = Integer.valueOf(stringFrequency);

        ReminderTasks.executeTask(context, action,name,description,date);
    }
}
