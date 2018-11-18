package com.paul.t29ideagarden2.presenter;

import com.paul.t29ideagarden2.model.IDanModel;
import com.paul.t29ideagarden2.view.IMainView;

/**
 * Created by paul on 2018/11/18
 * last modified at 20:23.
 * Desc: deal with view's all events, interact with view and model
 */

public class MainPresenter {

    IMainView view;

    public void onStartLoadingUserInfo(){
        // rx load from Bmob
        // the `view`'s method: onInfoLoaded() will be called inside rx logic
    }
    public void headClick(){
        // startUserInfoActivity
    }
    public void mapClick(){
        //startMapActivity
    }
    public void fragmentClick(){
        // user Rx to count down, each counting, call view.meditationOnProgress()
        // at rx's last, call view.meditationDone()
    }
    public void saveMeditationResult(IDanModel danModel){
        // upload danModel by Bmob
    }
}
