package com.paul.t29ideagarden2.background;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.atys.MeditationActivity;

import java.util.Date;

import cn.bmob.v3.helper.NotificationCompat;

/**
 * Created by paul on 2018/12/18
 * last modified at 21:20.
 * Desc:
 */

public class MeditationService extends Service {
    private static final String TAG = "MeditationService";
    private MeditationBinder mBinder = new MeditationBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // s1 create a new notification obj
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(MeditationService.this.getResources(), R.drawable.ic_draw_flower))
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Purge Ugly")
                .setContentText("meditation in progress")
                .setProgress(100, 0, false);
        Notification mNotification = mBuilder.build();
        // s2 start this service at foreground mode
        startForeground(101, mNotification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // we open a new Thread to do something
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "execute at " + new Date().toString());
            }
        }).start();
        // we need AlarmManager
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int tenSec = 10*1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + tenSec;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);

        return super.onStartCommand(intent, flags, startId);
    }

    class MeditationBinder extends Binder {
        public void startDownload() {
            Log.e(TAG, "startDownload: ");
        }
        public int getProgress() {
            Log.e(TAG, "getProgress: ");
            return 34;
        }
    }
}
