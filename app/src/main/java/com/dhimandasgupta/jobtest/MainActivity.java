package com.dhimandasgupta.jobtest;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnSharedPreferenceChangeListener {
    private AppCompatButton immediateButton;
    private AppCompatButton periodicButton;

    private AppCompatTextView immediateTextView;
    private AppCompatTextView periodicTextView;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            updateUI();
            handler.postDelayed(this, 1000);
        }
    };

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        immediateButton = findViewById(R.id.immediate_job_button);
        immediateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startImmediateJob();
            }
        });

        periodicButton = findViewById(R.id.periodic_job_button);
        periodicButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startPeriodicJob();
            }
        });

        immediateTextView = findViewById(R.id.last_immediate_job_text);
        periodicTextView = findViewById(R.id.last_periodic_job_text);
    }

    @Override
    protected void onStart() {
        super.onStart();

        prefs = getSharedPreferences(JobPref.PREF_NAME, Context.MODE_PRIVATE);
        prefs.registerOnSharedPreferenceChangeListener(this);

        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        handler.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();

        handler.removeCallbacks(runnable);
    }

    @Override
    protected void onStop() {
        super.onStop();

        prefs = getSharedPreferences(JobPref.PREF_NAME, Context.MODE_PRIVATE);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void updateUI() {
        updateImmediateJobButton(prefs);
        updatePeriodicJobButton(prefs);
        updateImmediateJobText(prefs);
        updatePeriodicJobText(prefs);
    }

    private void startImmediateJob() {
        final JobInfo job = new JobInfo.Builder(100, new ComponentName(this, ImmediateJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresCharging(false)
                .setMinimumLatency(1)
                .setOverrideDeadline(1)
                .build();

        final JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.cancel(100);
        jobScheduler.schedule(job);
    }

    private void startPeriodicJob() {
        final JobInfo job = new JobInfo.Builder(200, new ComponentName(this, PeriodicJobService.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setRequiresCharging(false)
                .setPersisted(true)
                .setPeriodic(60 * 1000)
                .build();

        final JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(job);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equalsIgnoreCase(JobPref.IMMEDIATE_START_TIMESTAMP) || s.equalsIgnoreCase(JobPref.IMMEDIATE_END_TIMESTAMP)) {
            updateImmediateJobText(sharedPreferences);
        } else if (s.equalsIgnoreCase(JobPref.PERIODIC_START_TIMESTAMP) || s.equalsIgnoreCase(JobPref.PERIODIC_END_TIMESTAMP)) {
            updatePeriodicJobText(sharedPreferences);
        } else if (s.equalsIgnoreCase(JobPref.PERIODIC_JOB_RUNNING)) {
            updatePeriodicJobButton(sharedPreferences);
        } else if (s.equalsIgnoreCase(JobPref.IMMEDIATE_JOB_RUNNING)) {
            updateImmediateJobButton(sharedPreferences);
        }
    }

    private void updateImmediateJobText(SharedPreferences sharedPreferences) {
        final StringBuilder builder = new StringBuilder();

        final long start = sharedPreferences.getLong(JobPref.IMMEDIATE_START_TIMESTAMP, -1);
        final long end = sharedPreferences.getLong(JobPref.IMMEDIATE_END_TIMESTAMP, -1);

        if (start < 0) {
            builder.append("Immediate Job Start : NA");
        }
        else {
            builder.append("Immediate Job  Start : " + DateUtils.getRelativeTimeSpanString(start, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        }

        builder.append("\n");

        if (end < 0) {
            builder.append("Immediate Job End : NA");
        }
        else {
            builder.append("Immediate Job End : " + DateUtils.getRelativeTimeSpanString(end, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        }

        immediateTextView.setText(builder.toString());
    }

    private void updatePeriodicJobText(SharedPreferences sharedPreferences) {
        final StringBuilder builder = new StringBuilder();

        final long start = sharedPreferences.getLong(JobPref.PERIODIC_START_TIMESTAMP, -1);
        final long end = sharedPreferences.getLong(JobPref.PERIODIC_END_TIMESTAMP, -1);

        if (start < 0) {
            builder.append("Periodic Job Start : NA");
        }
        else {
            builder.append("Periodic Job Start : " + DateUtils.getRelativeTimeSpanString(start, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        }

        builder.append("\n");

        if (end < 0) {
            builder.append("Periodic Job End : NA");
        }
        else {
            builder.append("Periodic Job End : " + DateUtils.getRelativeTimeSpanString(end, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        }

        periodicTextView.setText(builder.toString());
    }

    private void updateImmediateJobButton(SharedPreferences sharedPreferences) {
        immediateButton.setEnabled(!JobPref.isImmediateJobRunning(JobApp.getInstance()));
    }

    private void updatePeriodicJobButton(SharedPreferences sharedPreferences) {
        periodicButton.setEnabled(!JobPref.isPeriodicJobRunning(JobApp.getInstance()));
    }
}
