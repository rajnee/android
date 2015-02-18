package com.rneel.apps.mytwitter.models;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;


/**
 * Created by rneel on 2/12/15.
 */
public class LooperThread extends Thread {
    private static LooperThread looperThread;
    private Looper looper;
    
    private Handler mHandler;
    private Context context;
    public LooperThread(Context context)
    {
        this.context = context;
    }

    public static LooperThread getLooperThread(Context context)
    {
        if (looperThread == null)
        {
            looperThread = new LooperThread(context);
            looperThread.start();
        }
        return looperThread;
    }

    public Looper getLooper() {
        return looper;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public void run() {
        Looper.prepare();
        looper = Looper.myLooper();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Looper mainLooper = Looper.getMainLooper();
                new Handler(mainLooper).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LooperThread.this.context,"Message handled successfully", Toast.LENGTH_SHORT);
                    }
                });
            }
        };
        Looper.loop();
    }
}
