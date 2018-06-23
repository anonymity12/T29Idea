package com.paul.t29ideagarden2.bean;

/**
 * Created by paul on 2018/6/23
 * last modified at 3:48 PM.
 * Desc:
 */

public class Monk {
    private String name;
    private String imgPath;

    private int level;
    private int danCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDanCount() {
        return danCount;
    }

    public void setDanCount(int danCount) {
        this.danCount = danCount;
    }
}
