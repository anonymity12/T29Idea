package com.paul.t29ideagarden2.background;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.paul.t29ideagarden2.R;
import com.paul.t29ideagarden2.atys.MeditationActivity;

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
