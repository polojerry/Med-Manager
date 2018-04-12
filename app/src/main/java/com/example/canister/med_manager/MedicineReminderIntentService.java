package com.example.canister.med_manager;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

/**
 * Created by Canister on 4/10/2018.
 */

public class MedicineReminderIntentService extends IntentService {

    public MedicineReminderIntentService() {
        super("Medicine Intake Reminder Service");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }
}
