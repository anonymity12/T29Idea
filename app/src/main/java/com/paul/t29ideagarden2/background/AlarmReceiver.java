package com.paul.t29ideagarden2.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by paul on 2018/12/20
 * last modified at 22:13.
 * Desc:
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MeditationService.class);
        context.startService(i);
    }
}
