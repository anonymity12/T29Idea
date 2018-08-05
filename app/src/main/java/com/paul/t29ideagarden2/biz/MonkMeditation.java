package com.paul.t29ideagarden2.biz;

import android.os.Handler;
import android.os.Message;

import com.paul.t29ideagarden2.bean.Monk;

/**
 * Created by paul on 2018/6/23
 * last modified at 3:53 PM.
 * Desc:
 */

public class MonkMeditation implements IMonkMeditation {
    @Override
    public void meditation(final Monk monk, final OnMeditationFinishedListener listener, final Handler handler) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                int counterDown = 60;//tt:now it's 1 min
                for(;counterDown > 0;counterDown --){
                    try{
                        Message message = handler.obtainMessage(1);
                        message.arg1 = counterDown;
                        Thread.sleep(1000);
                        handler.sendMessage(message);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                //tt: 顺利完成meditation
                if (monk != null){
                    listener.meditationFinished();
                }else{
                //tt: 未正常完成
                    listener.meditationInterrupted();
                }
            }
        }.start();
    }
}
