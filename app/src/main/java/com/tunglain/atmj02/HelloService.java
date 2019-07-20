package com.tunglain.atmj02;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public class HelloService extends IntentService {
    public static final String ACTION_HELLO_DONE = "action_hello_done";
    private static final String TAG = HelloService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    public HelloService() {
        super("HelloService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent: " + intent.getStringExtra("NAME"));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent done = new Intent();
        done.setAction(ACTION_HELLO_DONE);
        sendBroadcast(done);
    }
}
