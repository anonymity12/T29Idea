package com.paul.t29ideagarden2.view;

import com.paul.t29ideagarden2.bean.Monk;

/**
 * Created by paul on 2018/6/23
 * last modified at 4:02 PM.
 * Desc:一个mvp里的v的具体实现，要定义v接口，我们要明白：
 * 1。 v里有什么操作
 * 2。 操作了有什么结果，应该导向给用户何种反馈
 * 3。 其他一些必要的用户友好的交互
 */

public interface IMonkMeditationView {
    Monk getMonk();//tt: 在启动时调用，这是获得基本的用户信息的行为。
    void beginMeditation();//tt: 表明用户开始修炼了。进行修炼进度的展示。
    void finishMeditation();//tt: 修炼结束，清空修炼进度池，dan数量加一,数据库变动。
    void interruptMeditation();//tt: 修行中途截止，废弃，dan数量不变，可弹toast
}
