package com.boss.imuno.Utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.boss.imuno.R;
import com.boss.imuno.UI.Main.MainActivity;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class NotificationWorker extends Worker {

    private Context mContext;
    private String CHANNEL_ID = "reminder";

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Calendar c = Calendar.getInstance(Locale.UK);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        int refillDay = Integer.parseInt(Objects.requireNonNull(sharedPref.getString(mContext.getString(R.string.refill_day_shared_preference_key), "")));
        boolean showNotification = sharedPref.getBoolean("notification_preference_key", true);
        int daysLeft = (((refillDay + 7) - dayOfWeek))%7;


        if(daysLeft == 0 && showNotification){
            createNotificationChannel();

            Intent intent = new Intent(mContext, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentTitle(mContext.getString(R.string.notification_reminder_title_label))
                    .setContentText(mContext.getString(R.string.notification_reminder_body_label))
                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);;

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
            int notificationId = 191;
            notificationManager.notify(notificationId, builder.build());

        }

        return Result.success();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = mContext.getString(R.string.channel_name);
            String description = mContext.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
