package com.example.canister.med_manager.reminder;

import android.content.Context;

import com.example.canister.med_manager.database.InsertMedicineIntake;

/**
 * Created by Canister on 4/11/2018.
 */

public class ReminderTasks{

    public static String ACTION_INSERT_MEDICINE_INTAKE_DATA = "insert-medicine-intake-data";
    public static String ACTION_DISMISS_NOTIFICATION = "dismiss-notification";


    public static void executeTask(Context context, String action, String name, String description, String date){
        if (ACTION_INSERT_MEDICINE_INTAKE_DATA.equals(action)){
            insertMedicineIntakeDetails(context, name, description,date);
        }else if(ACTION_DISMISS_NOTIFICATION.equals(action)){
            NotificationUtils.clearAllNotifications(context);
        }
    }

    private static void insertMedicineIntakeDetails(Context context, String name, String description,String date) {
        InsertMedicineIntake.insertMedicineIntake(context, name, description,date);
        NotificationUtils.clearAllNotifications(context);

    }

}
