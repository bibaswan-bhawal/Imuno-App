package com.boss.imuno.Widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.boss.imuno.R;
import com.boss.imuno.UI.Main.MainActivity;

import java.text.BreakIterator;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class RefillWidget extends AppWidgetProvider {

    public static void updateWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_layout);

        Calendar c = Calendar.getInstance(Locale.UK);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        int refillDay = Integer.parseInt(Objects.requireNonNull(sharedPref.getString(context.getString(R.string.refill_day_shared_preference_key), "")));
        int daysLeft = (((refillDay + 7) - dayOfWeek))%7;



        if(daysLeft == 0){
            // Hide all TextView except the prefix
            views.setTextViewText(R.id.daysLeftPrefixTextView, context.getString(R.string.take_dose_now_label));
            views.setViewVisibility(R.id.widgetDaysLeftTextView, View.GONE);
            views.setViewVisibility(R.id.daysLeftSuffixTextView, View.GONE);

            //reset margins of the TextView to be center
            views.setViewPadding(R.id.daysLeftPrefixTextView, 0, 52, 0, 52);

        } else {
            // Set days left till next dose

            views.setViewVisibility(R.id.daysLeftPrefixTextView, View.VISIBLE);
            views.setViewVisibility(R.id.widgetDaysLeftTextView, View.VISIBLE);
            views.setViewVisibility(R.id.daysLeftSuffixTextView, View.VISIBLE);

            views.setTextViewText(R.id.daysLeftPrefixTextView, context.getString(R.string.days_till_dose_prefix_label));
            views.setTextViewText(R.id.widgetDaysLeftTextView, String.valueOf(daysLeft));
        }

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateAllWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds){
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int appWidgetId : appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId);
        }
    }
}
