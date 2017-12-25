package com.rayhahah.androidartstudy.module.framework;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.rayhahah.androidartstudy.R;
import com.rayhahah.androidartstudy.util.RLog;

import java.io.IOException;
import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FrameWorkActivity extends AppCompatActivity {

    public static String BASE_URL = "http://mall.rayhahah.com/";


    private static class MyHandler extends Handler {
        private WeakReference<FrameWorkActivity> activityWeakReference;

        public MyHandler(FrameWorkActivity activity) {
            activityWeakReference = new WeakReference<FrameWorkActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            FrameWorkActivity activity = activityWeakReference.get();
            if (activity != null) {

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_work);
    }

    public void clickRetrofit(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);

        Call<ResponseBody> call = retrofitApi.loginRaymall("test", "1234567");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    RLog.e(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                RLog.e(t.getMessage());
            }
        });
    }

    public void clickOkHttp(View view) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        Request request = new Request.Builder()
                .url("")
                .build();
        okhttp3.Call call = okHttpClient.newCall(request);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {

            }
        });
    }
}
