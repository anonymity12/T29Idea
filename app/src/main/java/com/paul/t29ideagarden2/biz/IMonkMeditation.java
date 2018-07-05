package com.paul.t29ideagarden2.biz;

import com.paul.t29ideagarden2.bean.Monk;

/**
 * Created by paul on 2018/6/23
 * last modified at 3:51 PM.
 * Desc: 这个接口扮演定义一个动作的作用，约定了冥想这个动作，需要一个monk参数，一个冥想监听器。
 */

public interface IMonkMeditation {
    void meditation(Monk monk,OnMeditationFinishedListener listener);
}
