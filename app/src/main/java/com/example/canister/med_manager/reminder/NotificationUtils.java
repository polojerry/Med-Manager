package com.example.canister.med_manager.reminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.canister.med_manager.DisplayMedicineActivity;
import com.example.canister.med_manager.R;

/**
 * Created by Canister on 4/11/2018.
 */

public class NotificationUtils {

    public static String REMINDER_TITLE = "";
    public static String REMINDER_DETAILS = "";

    //This notification ID can be used to access our notification
   private static final int MEDICINE_REMINDER_NOTIFICATION_ID = 1000;

    //This pending intent id is used to uniquely reference the pending intent
    private static final int MEDICINE_REMINDER_PENDING_INTENT_ID = 1001;

    //This notification channel id is used to link notifications to this channel
    private static final String MEDICINE_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";

    private static final int ACTION_TAKE_MEDICINE_PENDING_INTENT_ID = 0;
    private static final int ACTION_IGNORE_PENDING_INTENT_ID = 1;

    private static PendingIntent contentIntent(Context context){
        //An intent that opens up the DisplayMedicineActivity
        Intent startActivityIntent = new Intent(context, DisplayMedicineActivity.class);

        // The FLAG_UPDATE_CURRENT makes sure that if the intent is created again, it makes sure to
        // keep the intent but update the data
        return PendingIntent.getActivity(
                context,
                MEDICINE_REMINDER_PENDING_INTENT_ID,
                startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    // This method is to decode a bitmap needed for the notification.
    private static Bitmap myNotificationIcon(Context context) {
        Resources res = context.getResources();
        Bitmap notificationIcon = BitmapFactory.decodeResource(res, R.drawable.ic_pill_24px);
        return notificationIcon;
    }

    //Method to clear all notifications
    public static void clearAllNotifications(Context context) {
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    //method to dismiss
    private static NotificationCompat.Action ignoreReminderAction(Context context) {
        Intent ignoreReminderIntent = new Intent(context, MedicineReminderIntentService.class);
        ignoreReminderIntent.setAction(ReminderTasks.ACTION_DISMISS_NOTIFICATION);
        PendingIntent ignoreReminderPendingIntent = PendingIntent.getService(
                context,
                ACTION_IGNORE_PENDING_INTENT_ID,
                ignoreReminderIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //Action to dismiss
        NotificationCompat.Action ignoreReminderAction = new NotificationCompat.Action(R.drawable.ic_cancel_black_24px,
                "Not Now.",
                ignoreReminderPendingIntent);
        return ignoreReminderAction;
    }

    //Method to take medicine
    private static NotificationCompat.Action takeMedicationAction(Context context) {
        Intent insertMedicineIntakeIntent = new Intent(context, MedicineReminderIntentService.class);
        insertMedicineIntakeIntent.setAction(ReminderTasks.ACTION_INSERT_MEDICINE_INTAKE_DATA);

        PendingIntent insertMedicineIntake = PendingIntent.getService(
                context,
                ACTION_TAKE_MEDICINE_PENDING_INTENT_ID,
                insertMedicineIntakeIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Action tookMedicineAction = new NotificationCompat.Action(R.drawable.ic_pill_24px,
                "I Took It!",
                insertMedicineIntake);
        return tookMedicineAction;
    }

    public static void remindUserNotification(Context context) {
        //Get the NotificationManager using context.getSystemService
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        // A notification channel for Android O devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    MEDICINE_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,MEDICINE_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.ic_pill)
                .setLargeIcon(myNotificationIcon(context))
                .setContentTitle(REMINDER_TITLE)
                .setContentText(REMINDER_DETAILS)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .addAction(takeMedicationAction(context))
                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);


        // A notification channel for Android O devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        // Calls  the notification by calling notify on the NotificationManager.
        notificationManager.notify(MEDICINE_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
    }
}
