package com.paul.t29ideagarden2.view;

/**
 * Created by paul on 2018/11/18
 * last modified at 20:25.
 * Desc:
 */

public interface IMainView {
    void onInfoLoaded();// use the grabbed User object to fill tvs;
    void onHeadClicked(); // goto user info activity
    void onMapClicked();// goto map activity
    void onFragmentClicked();// set unclickable and then start meditation
    void meditationOnProgress(int progress);// use this `progress` to update the fragment's view
    void meditationDone();// impl should do: 1. enable the view click events, wait another click; 2. call present's saveMeditationResult()
    void meditationInterrupt();// stub method, maybe u can save user's unfinished progress and then call meditationDone
}
