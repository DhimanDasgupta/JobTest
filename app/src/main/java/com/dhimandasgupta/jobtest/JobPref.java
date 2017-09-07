package com.dhimandasgupta.jobtest;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by dhimandasgupta on 08/09/17.
 */

public class JobPref {
    public static final String PREF_NAME = "job_pref";

    public static final String IMMEDIATE_START_TIMESTAMP = "immediate_start_timestamp";
    public static final String IMMEDIATE_END_TIMESTAMP = "immediate_end_timestamp";

    public static final String PERIODIC_START_TIMESTAMP = "periodic_start_timestamp";
    public static final String PERIODIC_END_TIMESTAMP = "periodic_end_timestamp";

    public static final String IMMEDIATE_JOB_RUNNING = "immediate_job_running";
    public static final String PERIODIC_JOB_RUNNING = "periodic_job_running";

    private JobPref() {

    }

    public static void saveLastImmediateJobStartTimeStamp(Context context, final long startTime) {
        final Context appContext = context.getApplicationContext();
        final SharedPreferences preferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(IMMEDIATE_START_TIMESTAMP, startTime);

        editor.apply();
    }

    public static void saveLastImmediateJobEndTimeStamp(Context context, final long endTime) {
        final Context appContext = context.getApplicationContext();
        final SharedPreferences preferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(IMMEDIATE_END_TIMESTAMP, endTime);

        editor.apply();
    }

    public static void saveLastPeriodicJobStartTimeStamp(Context context, final long startTime) {
        final Context appContext = context.getApplicationContext();
        final SharedPreferences preferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(PERIODIC_START_TIMESTAMP, startTime);

        editor.apply();
    }

    public static void saveLastPeriodicJobEndTimeStamp(Context context, final long endTime) {
        final Context appContext = context.getApplicationContext();
        final SharedPreferences preferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        editor.putLong(PERIODIC_END_TIMESTAMP, endTime);

        editor.apply();
    }

    public static boolean isImmediateJobRunning(Context context) {
        final Context appContext = context.getApplicationContext();
        final SharedPreferences preferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return preferences.getBoolean(IMMEDIATE_JOB_RUNNING, false);
    }

    public static void toggleImmediateJobRunning(Context context) {
        final Context appContext = context.getApplicationContext();
        final SharedPreferences preferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        boolean running = isImmediateJobRunning(context);

        editor.putBoolean(IMMEDIATE_JOB_RUNNING, !running);

        editor.apply();
    }

    public static boolean isPeriodicJobRunning(Context context) {
        final Context appContext = context.getApplicationContext();
        final SharedPreferences preferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        return preferences.getBoolean(PERIODIC_JOB_RUNNING, false);
    }

    public static void togglePeriodicJobRunning(Context context) {
        final Context appContext = context.getApplicationContext();
        final SharedPreferences preferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        boolean running = isPeriodicJobRunning(context);

        editor.putBoolean(PERIODIC_JOB_RUNNING, !running);

        editor.apply();
    }
}
