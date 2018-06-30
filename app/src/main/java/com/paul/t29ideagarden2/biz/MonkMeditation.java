package com.paul.t29ideagarden2.biz;

import com.paul.t29ideagarden2.bean.Monk;

/**
 * Created by paul on 2018/6/23
 * last modified at 3:53 PM.
 * Desc:
 */

public class MonkMeditation implements IMonkMeditation {
    @Override
    public void meditation(final Monk monk, final OnMeditationFinishedListener listener) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    Thread.sleep(5000);//tt: now it's 5s
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                //tt: 顺利完成meditation
                if (monk != null){
                    monk.setDanCount(monk.getDanCount() + 1);
                    listener.meditationFinished();
                }else{
                //tt: 未正常完成
                    listener.meditationInterrupted();
                }
            }
        }.start();
    }
}
