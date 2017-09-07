package com.dhimandasgupta.jobtest;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by dhimandasgupta on 08/09/17.
 */

public class ImmediateJobService extends JobService {
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        new DelayAsyncTask(this).execute(jobParameters);

        // True Because want to job to run on separate thread
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }

    private static class DelayAsyncTask extends AsyncTask<JobParameters, Void, JobParameters> {
        private JobService jobService;

        public DelayAsyncTask(JobService service) {
            jobService = service;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            Log.d("Job", "Immediate Job Started");
            JobPref.saveLastImmediateJobStartTimeStamp(JobApp.getInstance(), System.currentTimeMillis());
            JobPref.toggleImmediateJobRunning(JobApp.getInstance());
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            try {
                Thread.sleep(20 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters parameters) {
            super.onPostExecute(parameters);

            Log.d("Job", "Immediate Job Stopped");
            jobService.jobFinished(parameters, false);
            JobPref.saveLastImmediateJobEndTimeStamp(JobApp.getInstance(), System.currentTimeMillis());
            JobPref.toggleImmediateJobRunning(JobApp.getInstance());
        }
    }
}
