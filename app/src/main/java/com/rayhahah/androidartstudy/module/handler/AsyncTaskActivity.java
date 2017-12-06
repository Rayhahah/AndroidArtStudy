package com.rayhahah.androidartstudy.module.handler;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rayhahah.androidartstudy.R;

public class AsyncTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_async_task);
        MyAsyncTask task = new MyAsyncTask();
//        task.execute()
    }

    class MyAsyncTask extends AsyncTask {

        /**
         * 还在UI线程
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 执行耗时操作
         *
         * @param objects
         * @return
         */
        @Override
        protected Object doInBackground(Object[] objects) {

            /**
             * 不断输出进度到onProgressUpdate
             */
            publishProgress();
            return null;
        }

        /**
         * UI线程
         *
         * @param values
         */
        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }
}
