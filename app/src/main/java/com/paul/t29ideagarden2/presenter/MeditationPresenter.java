package com.paul.t29ideagarden2.presenter;

import com.paul.t29ideagarden2.bean.Monk;
import com.paul.t29ideagarden2.biz.IMonkMeditation;
import com.paul.t29ideagarden2.biz.MonkMeditation;
import com.paul.t29ideagarden2.view.IMonkMeditationView;

/**
 * Created by paul on 2018/6/26
 * last modified at 4:34 PM.
 * Desc:
 */

public class MeditationPresenter {
    private IMonkMeditationView monkMeditationView;
    private IMonkMeditation monkMeditation;
    public MeditationPresenter(IMonkMeditationView iMonkMeditationView){
        this.monkMeditationView = iMonkMeditationView;
        this.monkMeditation = new MonkMeditation();
    }
    public void meditation(){
        monkMeditationView.beginMeditation();
        monkMeditation.meditation(monkMeditationView.getMonk());
        // TODO: 2018/6/26 go on, 这里添加好了获取monk对象的步骤，需要一个监听器作为上述函数的二参； 
    }
}
