package com.paul.t29ideagarden2.presenter;

import com.paul.t29ideagarden2.bean.Monk;
import com.paul.t29ideagarden2.biz.IMonkMeditation;
import com.paul.t29ideagarden2.biz.MonkMeditation;
import com.paul.t29ideagarden2.biz.OnMeditationFinishedListener;
import com.paul.t29ideagarden2.view.IMonkMeditationView;

import android.os.Handler;

/**
 * Created by paul on 2018/6/26
 * last modified at 4:34 PM.
 * Desc:
 */

public class MeditationPresenter {
    private IMonkMeditationView monkMeditationView;
    private IMonkMeditation monkMeditation;
    private Handler mHandler = new Handler();
    public MeditationPresenter(IMonkMeditationView iMonkMeditationView){
        this.monkMeditationView = iMonkMeditationView;
        this.monkMeditation = new MonkMeditation();


    }
    public void meditation(){
        monkMeditationView.beginMeditation();
        monkMeditation.meditation(monkMeditationView.getMonk(), new OnMeditationFinishedListener() {
            @Override
            public void meditationFinished() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        monkMeditationView.finishMeditation();
                    }
                });
            }

            @Override
            public void meditationInterrupted() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        monkMeditationView.interruptMeditation();
                    }
                });
            }
        });
    }
}
