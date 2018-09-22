package com.paul.t29ideagarden2.biz;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.paul.t29ideagarden2.bean.Monk;

import static com.paul.t29ideagarden2.util.Constants.TIME_UP_LIMIT;

/**
 * Created by paul on 2018/6/23
 * last modified at 3:53 PM.
 * Desc:
 * todo: 如何禁止用户在当前有冥想过程时，继续点击，触发呢？synchronized
 */

public class MonkMeditation implements IMonkMeditation {

    private boolean countingFlag = false;

    class CounterDownThread implements Runnable {

        //thread need vars
        Monk monk;
        OnMeditationFinishedListener listener;
        Handler handler;

        //todo: (0922)?make ur params final!
        CounterDownThread(Monk _monk, OnMeditationFinishedListener _listener, Handler _handler) {
            this.monk = _monk;
            this.listener = _listener;
            this.handler = _handler;
        }

        @Override
        public void run() {
            synchronized (this) {
                countingFlag = true;
                //count down by for loop
                int counterDown = TIME_UP_LIMIT;//tt:now it's 10 min
                for(;counterDown > 0;counterDown--){
                    try{
                        //send heart beat msg by for loop
                        Message message = handler.obtainMessage(1);
                        message.arg1 = counterDown;
                        Thread.sleep(1000);
                        handler.sendMessage(message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                //finished, check monk still there?
                if (monk != null) {
                    listener.meditationFinished();
                } else {
                    listener.meditationInterrupted();
                }
                countingFlag = false;
            }
        }
    }

    @Override
    public void meditation(final Monk monk, final OnMeditationFinishedListener listener, final Handler handler) {
        //if not counting now, we counting, or send msg to user that we are counting down, don't
        //bother us counting! fuck users!
        if (!countingFlag) {
            CounterDownThread counterDownThread = new CounterDownThread(monk, listener, handler);
            Thread countDownThreadWrapper = new Thread(counterDownThread);
            countDownThreadWrapper.start();
            countingFlag = true;
        } else {
            // thread is counting, don't bother!
            // todo: (0922) notice user we won't start counting
            Log.e("MonkMeditation","counting down, don't bother");
        }
    }
}
